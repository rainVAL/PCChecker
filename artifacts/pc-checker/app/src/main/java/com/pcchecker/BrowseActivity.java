package com.pcchecker;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.pcchecker.adapter.ComponentAdapter;
import com.pcchecker.data.ComponentDatabase;
import com.pcchecker.model.PCComponent;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BrowseActivity extends AppCompatActivity {

    private BuildManager buildManager;
    private ComponentAdapter adapter;
    private List<PCComponent> allComponents;
    private List<PCComponent> filteredComponents;

    private EditText etSearch;
    private Spinner spinnerSort, spinnerTier;
    private TextView tvTitle;

    private String searchQuery = "";
    private String sortBy = "Performance";
    private String tierFilter = "All";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browse);
        buildManager = BuildManager.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        etSearch = findViewById(R.id.et_search);
        spinnerSort = findViewById(R.id.spinner_sort);
        spinnerTier = findViewById(R.id.spinner_tier);
        tvTitle = findViewById(R.id.tv_browse_title);
        RecyclerView recycler = findViewById(R.id.recycler_components);

        PCComponent.Category slot = buildManager.getPendingSlot();
        if (slot != null) {
            tvTitle.setText("Select " + slot.name().charAt(0) +
                    slot.name().substring(1).toLowerCase());
            allComponents = ComponentDatabase.getByCategory(slot);
        } else {
            tvTitle.setText("All Components");
            allComponents = new ArrayList<>(ComponentDatabase.getAll());
        }

        filteredComponents = new ArrayList<>(allComponents);

        String currentId = null;
        if (slot != null) {
            PCComponent current = buildManager.getComponent(slot);
            if (current != null) currentId = current.getId();
        }

        adapter = new ComponentAdapter(filteredComponents, currentId, component -> {
            buildManager.setComponent(component);
            buildManager.setPendingSlot(null);
            Toast.makeText(this, component.getName() + " added!", Toast.LENGTH_SHORT).show();
            finish();
        });

        recycler.setLayoutManager(new LinearLayoutManager(this));
        recycler.setAdapter(adapter);

        setupFilters();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupFilters() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                searchQuery = s.toString();
                applyFilters();
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        ArrayAdapter<String> sortAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"Performance", "Price: Low to High", "Price: High to Low"});
        sortAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSort.setAdapter(sortAdapter);
        spinnerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                sortBy = (String) parent.getItemAtPosition(position);
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        ArrayAdapter<String> tierAdapter = new ArrayAdapter<>(this,
                android.R.layout.simple_spinner_item,
                new String[]{"All", "Budget", "Mid-range", "High-end"});
        tierAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTier.setAdapter(tierAdapter);
        spinnerTier.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> parent, android.view.View view, int position, long id) {
                tierFilter = (String) parent.getItemAtPosition(position);
                applyFilters();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void applyFilters() {
        List<PCComponent> result = new ArrayList<>();

        for (PCComponent c : allComponents) {
            if (!searchQuery.isEmpty() &&
                    !c.getName().toLowerCase().contains(searchQuery.toLowerCase()) &&
                    !c.getBrand().toLowerCase().contains(searchQuery.toLowerCase())) {
                continue;
            }
            if (!tierFilter.equals("All")) {
                String label = tierLabel(c.getPriceTier());
                if (!label.equals(tierFilter)) continue;
            }
            result.add(c);
        }

        // Sort
        switch (sortBy) {
            case "Performance":
                result.sort((a, b) -> b.getPerformanceScore() - a.getPerformanceScore());
                break;
            case "Price: Low to High":
                result.sort(Comparator.comparingDouble(PCComponent::getPrice));
                break;
            case "Price: High to Low":
                result.sort((a, b) -> Double.compare(b.getPrice(), a.getPrice()));
                break;
        }

        filteredComponents = result;
        adapter.updateList(filteredComponents);
    }

    private String tierLabel(PCComponent.PriceTier tier) {
        switch (tier) {
            case BUDGET: return "Budget";
            case MID: return "Mid-range";
            case HIGH_END: return "High-end";
            default: return "All";
        }
    }
}
