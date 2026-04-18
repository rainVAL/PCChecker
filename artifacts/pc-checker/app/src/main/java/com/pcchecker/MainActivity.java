package com.pcchecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.CompatibilityUtils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BuildManager buildManager;

    // Summary
    private TextView tvTotalPrice, tvPowerDraw, tvCompatStatus;
    private Button btnViewResults, btnClearBuild;
    private RadioGroup radioUseCase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buildManager = BuildManager.getInstance();

        initViews();
        setupUseCaseSelector();
        setupSlotButtons();
        updateUI();
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateUI();
    }

    private void initViews() {
        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvPowerDraw = findViewById(R.id.tv_power_draw);
        tvCompatStatus = findViewById(R.id.tv_compat_status);
        btnViewResults = findViewById(R.id.btn_view_results);
        btnClearBuild = findViewById(R.id.btn_clear_build);
        radioUseCase = findViewById(R.id.radio_use_case);
    }

    private void setupUseCaseSelector() {
        radioUseCase.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_gaming) buildManager.setUseCase(PCComponent.UseCase.GAMING);
            else if (checkedId == R.id.rb_productivity) buildManager.setUseCase(PCComponent.UseCase.PRODUCTIVITY);
            else buildManager.setUseCase(PCComponent.UseCase.GENERAL);
        });

        radioUseCase.check(R.id.rb_general);

        btnViewResults.setOnClickListener(v -> {
            if (buildManager.getComponentCount() == 0) {
                Toast.makeText(this, "Add components to run analysis", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, ResultsActivity.class));
        });

        btnClearBuild.setOnClickListener(v -> {
            buildManager.clearBuild();
            updateUI();
            Toast.makeText(this, "Build reset", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSlotButtons() {
        setupSlot(R.id.slot_cpu, "CPU", PCComponent.Category.CPU);
        setupSlot(R.id.slot_gpu, "GPU", PCComponent.Category.GPU);
        setupSlot(R.id.slot_ram, "RAM", PCComponent.Category.RAM);
        setupSlot(R.id.slot_mb, "Motherboard", PCComponent.Category.MOTHERBOARD);
        setupSlot(R.id.slot_storage, "Storage", PCComponent.Category.STORAGE);
        setupSlot(R.id.slot_psu, "Power Supply", PCComponent.Category.PSU);
        setupSlot(R.id.slot_case, "Case", PCComponent.Category.CASE);
    }

    private void setupSlot(int layoutId, String label, PCComponent.Category category) {
        View layout = findViewById(layoutId);
        TextView tvLabel = layout.findViewById(R.id.tv_slot_label);
        tvLabel.setText(label);

        Button btnAction = layout.findViewById(R.id.btn_slot_action);
        btnAction.setOnClickListener(v -> {
            buildManager.setPendingSlot(category);
            startActivity(new Intent(this, BrowseActivity.class));
        });

        ImageButton btnRemove = layout.findViewById(R.id.btn_slot_remove);
        btnRemove.setOnClickListener(v -> {
            buildManager.removeComponent(category);
            updateUI();
        });
    }

    private void updateUI() {
        updateSlotUI(R.id.slot_cpu, PCComponent.Category.CPU);
        updateSlotUI(R.id.slot_gpu, PCComponent.Category.GPU);
        updateSlotUI(R.id.slot_ram, PCComponent.Category.RAM);
        updateSlotUI(R.id.slot_mb, PCComponent.Category.MOTHERBOARD);
        updateSlotUI(R.id.slot_storage, PCComponent.Category.STORAGE);
        updateSlotUI(R.id.slot_psu, PCComponent.Category.PSU);
        updateSlotUI(R.id.slot_case, PCComponent.Category.CASE);

        double price = buildManager.getTotalPrice();
        tvTotalPrice.setText(String.format(Locale.US, "Total: ₱%,.0f", price));

        int power = CompatibilityUtils.estimatePowerDraw(buildManager.getBuild());
        tvPowerDraw.setText("Est. Power: " + power + "W");

        if (buildManager.getComponentCount() > 0) {
            CompatibilityResult result = CompatibilityUtils.check(buildManager.getBuild());
            tvCompatStatus.setText(result.getStatusSummary());
        } else {
            tvCompatStatus.setText("No components added yet.");
        }
    }

    private void updateSlotUI(int layoutId, PCComponent.Category category) {
        View layout = findViewById(layoutId);
        TextView tvValue = layout.findViewById(R.id.tv_slot_value);
        Button btnAction = layout.findViewById(R.id.btn_slot_action);
        ImageButton btnRemove = layout.findViewById(R.id.btn_slot_remove);

        PCComponent c = buildManager.getComponent(category);
        if (c != null) {
            tvValue.setText(c.getName());
            tvValue.setTextColor(getResources().getColor(R.color.text_primary, getTheme()));
            btnAction.setText("Swap");
            btnRemove.setVisibility(View.VISIBLE);
        } else {
            tvValue.setText("Not selected");
            tvValue.setTextColor(getResources().getColor(R.color.text_hint, getTheme()));
            btnAction.setText("Add");
            btnRemove.setVisibility(View.GONE);
        }
    }
}
