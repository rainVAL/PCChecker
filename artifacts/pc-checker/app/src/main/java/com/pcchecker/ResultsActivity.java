package com.pcchecker;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.BottleneckUtils;
import com.pcchecker.utils.BuildStorage;
import com.pcchecker.utils.CompatibilityUtils;
import com.pcchecker.utils.SuggestionUtils;

import java.util.List;
import java.util.Locale;

public class ResultsActivity extends AppCompatActivity {

    private BuildManager buildManager;

    private TextView tvCompatHeader, tvBalanceLabel;
    private LinearProgressIndicator pbBalance;
    private LinearLayout llErrors, llWarnings, llSuggestions, llStatusHeader;
    private TextView tvBottleneck;
    private LinearProgressIndicator pbBottleneck;
    private ImageView ivStatusIcon;
    private View btnSaveBuild;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        buildManager = BuildManager.getInstance();

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar_results);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvCompatHeader = findViewById(R.id.tv_compat_header);
        tvBalanceLabel = findViewById(R.id.tv_balance_label);
        pbBalance = findViewById(R.id.pb_balance);
        llErrors = findViewById(R.id.ll_errors);
        llWarnings = findViewById(R.id.ll_warnings);
        llSuggestions = findViewById(R.id.ll_suggestions);
        llStatusHeader = findViewById(R.id.ll_status_header);
        tvBottleneck = findViewById(R.id.tv_bottleneck);
        pbBottleneck = findViewById(R.id.pb_bottleneck);
        ivStatusIcon = findViewById(R.id.iv_status_icon);
        btnSaveBuild = findViewById(R.id.btn_save_build);

        btnSaveBuild.setOnClickListener(v -> showSaveDialog());

        findViewById(R.id.btn_find_all_shops).setOnClickListener(v -> {
            startActivity(new android.content.Intent(this, StoreMapActivity.class));
        });

        loadResults();
    }

    private void showSaveDialog() {
        android.widget.EditText input = new android.widget.EditText(this);
        input.setHint("Enter build name");
        new androidx.appcompat.app.AlertDialog.Builder(this)
                .setTitle("Save Build")
                .setView(input)
                .setPositiveButton("Save", (dialog, which) -> {
                    String name = input.getText().toString().trim();
                    if (!name.isEmpty()) {
                        BuildStorage.saveBuild(this, name, buildManager.getBuild());
                        android.widget.Toast.makeText(this, "Build saved!", android.widget.Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void loadResults() {
        CompatibilityResult result = CompatibilityUtils.check(buildManager.getBuild());
        BottleneckUtils.BottleneckReport bottleneck = BottleneckUtils.analyze(buildManager.getBuild());
        List<String> suggestions = SuggestionUtils.getSuggestions(
                buildManager.getBuild(), buildManager.getUseCase());

        // Header
        if (result.isCompatible()) {
            tvCompatHeader.setText(R.string.results_compatible);
            llStatusHeader.setBackgroundResource(R.drawable.bg_status_gradient);
            ivStatusIcon.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
            tvCompatHeader.setText(getString(R.string.results_errors, result.getErrors().size()));
            llStatusHeader.setBackgroundColor(getResources().getColor(R.color.error, getTheme()));
            ivStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert);
        }

        // Balance
        int balance = result.getPerformanceBalance();
        tvBalanceLabel.setText(balance >= 80 ? "Highly Optimized" : "Balanced");
        pbBalance.setProgress(balance);

        // Errors
        llErrors.removeAllViews();
        for (String err : result.getErrors()) {
            addIssueCard(llErrors, err, getResources().getColor(R.color.error, getTheme()));
        }

        // Warnings
        llWarnings.removeAllViews();
        for (String warn : result.getWarnings()) {
            addIssueCard(llWarnings, warn, getResources().getColor(R.color.warning, getTheme()));
        }

        // Bottleneck
        if (bottleneck.severity == 0) {
            tvBottleneck.setText("Perfectly Balanced");
            pbBottleneck.setProgress(5); // Show tiny progress for visibility
        } else {
            tvBottleneck.setText(bottleneck.severity > 15 ? "High Risk" : "Low Risk");
            pbBottleneck.setProgress(bottleneck.severity);
        }

        // Suggestions
        llSuggestions.removeAllViews();
        if (!suggestions.isEmpty()) {
            addSectionTitle(llSuggestions, "Optimization Tips");
            for (String s : suggestions) {
                addIssueCard(llSuggestions, s, getResources().getColor(R.color.primary, getTheme()));
            }
        }
    }

    private void addSectionTitle(LinearLayout parent, String title) {
        TextView tv = new TextView(this);
        tv.setText(title);
        tv.setTextSize(18);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setTextColor(getResources().getColor(R.color.text_primary, getTheme()));
        tv.setPadding(16, 32, 0, 16);
        parent.addView(tv);
    }

    private void addIssueCard(LinearLayout parent, String text, int accentColor) {
        com.google.android.material.card.MaterialCardView card = new com.google.android.material.card.MaterialCardView(this);
        card.setRadius(40);
        card.setCardElevation(0);
        card.setStrokeWidth(0);
        card.setUseCompatPadding(true);
        
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.HORIZONTAL);
        
        View accent = new View(this);
        accent.setLayoutParams(new LinearLayout.LayoutParams(12, LinearLayout.LayoutParams.MATCH_PARENT));
        accent.setBackgroundColor(accentColor);
        layout.addView(accent);
        
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(getResources().getColor(R.color.text_primary, getTheme()));
        tv.setTextSize(14);
        tv.setPadding(32, 32, 32, 32);
        layout.addView(tv);
        
        card.addView(layout);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 12);
        card.setLayoutParams(params);
        
        parent.addView(card);
    }
}
