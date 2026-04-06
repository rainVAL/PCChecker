package com.pcchecker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.CompatibilityUtils;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private BuildManager buildManager;

    // Slot cards
    private TextView tvCpu, tvGpu, tvRam, tvMb, tvStorage, tvPsu, tvCase;
    private Button btnAddCpu, btnAddGpu, btnAddRam, btnAddMb, btnAddStorage, btnAddPsu, btnAddCase;
    private Button btnRemCpu, btnRemGpu, btnRemRam, btnRemMb, btnRemStorage, btnRemPsu, btnRemCase;

    // Summary
    private TextView tvTotalPrice, tvPowerDraw, tvCompatStatus;
    private Button btnViewResults, btnClearBuild;
    private RadioGroup radioUseCase;
    private RadioButton rbGaming, rbProductivity, rbGeneral;

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
        tvCpu = findViewById(R.id.tv_cpu);
        tvGpu = findViewById(R.id.tv_gpu);
        tvRam = findViewById(R.id.tv_ram);
        tvMb = findViewById(R.id.tv_mb);
        tvStorage = findViewById(R.id.tv_storage);
        tvPsu = findViewById(R.id.tv_psu);
        tvCase = findViewById(R.id.tv_case);

        btnAddCpu = findViewById(R.id.btn_add_cpu);
        btnAddGpu = findViewById(R.id.btn_add_gpu);
        btnAddRam = findViewById(R.id.btn_add_ram);
        btnAddMb = findViewById(R.id.btn_add_mb);
        btnAddStorage = findViewById(R.id.btn_add_storage);
        btnAddPsu = findViewById(R.id.btn_add_psu);
        btnAddCase = findViewById(R.id.btn_add_case);

        btnRemCpu = findViewById(R.id.btn_rem_cpu);
        btnRemGpu = findViewById(R.id.btn_rem_gpu);
        btnRemRam = findViewById(R.id.btn_rem_ram);
        btnRemMb = findViewById(R.id.btn_rem_mb);
        btnRemStorage = findViewById(R.id.btn_rem_storage);
        btnRemPsu = findViewById(R.id.btn_rem_psu);
        btnRemCase = findViewById(R.id.btn_rem_case);

        tvTotalPrice = findViewById(R.id.tv_total_price);
        tvPowerDraw = findViewById(R.id.tv_power_draw);
        tvCompatStatus = findViewById(R.id.tv_compat_status);
        btnViewResults = findViewById(R.id.btn_view_results);
        btnClearBuild = findViewById(R.id.btn_clear_build);
        radioUseCase = findViewById(R.id.radio_use_case);
        rbGaming = findViewById(R.id.rb_gaming);
        rbProductivity = findViewById(R.id.rb_productivity);
        rbGeneral = findViewById(R.id.rb_general);
    }

    private void setupUseCaseSelector() {
        radioUseCase.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_gaming) buildManager.setUseCase(PCComponent.UseCase.GAMING);
            else if (checkedId == R.id.rb_productivity) buildManager.setUseCase(PCComponent.UseCase.PRODUCTIVITY);
            else buildManager.setUseCase(PCComponent.UseCase.GENERAL);
        });

        // Set default
        rbGeneral.setChecked(true);

        btnViewResults.setOnClickListener(v -> {
            if (buildManager.getComponentCount() == 0) {
                Toast.makeText(this, "Add some components first!", Toast.LENGTH_SHORT).show();
                return;
            }
            startActivity(new Intent(this, ResultsActivity.class));
        });

        btnClearBuild.setOnClickListener(v -> {
            buildManager.clearBuild();
            updateUI();
            Toast.makeText(this, "Build cleared.", Toast.LENGTH_SHORT).show();
        });
    }

    private void setupSlotButtons() {
        setupSlot(btnAddCpu, btnRemCpu, PCComponent.Category.CPU);
        setupSlot(btnAddGpu, btnRemGpu, PCComponent.Category.GPU);
        setupSlot(btnAddRam, btnRemRam, PCComponent.Category.RAM);
        setupSlot(btnAddMb, btnRemMb, PCComponent.Category.MOTHERBOARD);
        setupSlot(btnAddStorage, btnRemStorage, PCComponent.Category.STORAGE);
        setupSlot(btnAddPsu, btnRemPsu, PCComponent.Category.PSU);
        setupSlot(btnAddCase, btnRemCase, PCComponent.Category.CASE);
    }

    private void setupSlot(Button addBtn, Button remBtn, PCComponent.Category category) {
        addBtn.setOnClickListener(v -> openBrowse(category));
        remBtn.setOnClickListener(v -> {
            buildManager.removeComponent(category);
            updateUI();
        });
    }

    private void openBrowse(PCComponent.Category category) {
        buildManager.setPendingSlot(category);
        startActivity(new Intent(this, BrowseActivity.class));
    }

    private void updateUI() {
        updateSlot(PCComponent.Category.CPU, tvCpu, btnAddCpu, btnRemCpu);
        updateSlot(PCComponent.Category.GPU, tvGpu, btnAddGpu, btnRemGpu);
        updateSlot(PCComponent.Category.RAM, tvRam, btnAddRam, btnRemRam);
        updateSlot(PCComponent.Category.MOTHERBOARD, tvMb, btnAddMb, btnRemMb);
        updateSlot(PCComponent.Category.STORAGE, tvStorage, btnAddStorage, btnRemStorage);
        updateSlot(PCComponent.Category.PSU, tvPsu, btnAddPsu, btnRemPsu);
        updateSlot(PCComponent.Category.CASE, tvCase, btnAddCase, btnRemCase);

        double price = buildManager.getTotalPrice();
        tvTotalPrice.setText(String.format(Locale.US, "Total: $%.2f", price));

        int power = CompatibilityUtils.estimatePowerDraw(buildManager.getBuild());
        tvPowerDraw.setText("Est. Power: " + power + "W");

        if (buildManager.getComponentCount() > 0) {
            CompatibilityResult result = CompatibilityUtils.check(buildManager.getBuild());
            tvCompatStatus.setText(result.getStatusSummary());
            tvCompatStatus.setTextColor(getColor(
                    result.isCompatible() ? android.R.color.holo_green_dark : android.R.color.holo_red_dark));
        } else {
            tvCompatStatus.setText("No components added yet.");
            tvCompatStatus.setTextColor(getColor(android.R.color.darker_gray));
        }
    }

    private void updateSlot(PCComponent.Category category, TextView tv, Button addBtn, Button remBtn) {
        PCComponent c = buildManager.getComponent(category);
        if (c != null) {
            tv.setText(c.getName() + "\n" + c.getSpecSummary());
            tv.setTextColor(getColor(android.R.color.black));
            addBtn.setText("Swap");
            remBtn.setVisibility(View.VISIBLE);
        } else {
            tv.setText("Not selected");
            tv.setTextColor(getColor(android.R.color.darker_gray));
            addBtn.setText("Add");
            remBtn.setVisibility(View.GONE);
        }
    }
}
