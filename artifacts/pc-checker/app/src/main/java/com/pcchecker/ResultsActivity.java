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
            llStatusHeader.setBackgroundColor(getResources().getColor(R.color.success, getTheme()));
            ivStatusIcon.setImageResource(android.R.drawable.checkbox_on_background);
        } else {
            tvCompatHeader.setText(getString(R.string.results_errors, result.getErrors().size()));
            llStatusHeader.setBackgroundColor(getResources().getColor(R.color.error, getTheme()));
            ivStatusIcon.setImageResource(android.R.drawable.ic_dialog_alert);
        }

        // Balance
        int balance = result.getPerformanceBalance();
        tvBalanceLabel.setText(getString(balance >= 80 ? R.string.results_sync_optimized : R.string.results_sync_balanced, balance));
        pbBalance.setProgress(balance);

        // Errors
        llErrors.removeAllViews();
        for (String err : result.getErrors()) {
            addIssueLabel(llErrors, err, getResources().getColor(R.color.error, getTheme()), 
                    getResources().getColor(R.color.error_light, getTheme()));
        }

        // Warnings
        llWarnings.removeAllViews();
        for (String warn : result.getWarnings()) {
            addIssueLabel(llWarnings, warn, getResources().getColor(R.color.warning, getTheme()), 
                    getResources().getColor(R.color.warning_light, getTheme()));
        }

        // Bottleneck
        if (bottleneck.severity == 0) {
            tvBottleneck.setText("Perfectly balanced CPU & GPU");
            pbBottleneck.setProgress(0);
        } else {
            tvBottleneck.setText(bottleneck.component + " Bottleneck (" + bottleneck.severity + "% severity)");
            pbBottleneck.setProgress(bottleneck.severity);
        }

        // Suggestions
        llSuggestions.removeAllViews();
        if (!suggestions.isEmpty()) {
            TextView title = new TextView(this);
            title.setText(R.string.results_tips_header);
            title.setTextSize(18);
            title.setTypeface(null, android.graphics.Typeface.BOLD);
            title.setTextColor(getResources().getColor(R.color.text_primary, getTheme()));
            title.setPadding(0, 24, 0, 16);
            llSuggestions.addView(title);

            for (String s : suggestions) {
                addSuggestionLabel(llSuggestions, s);
            }
        }
    }

    private void addIssueLabel(LinearLayout parent, String text, int textColor, int bgColor) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(14);
        tv.setPadding(32, 32, 32, 32);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        tv.setLayoutParams(params);
        
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(bgColor);
        gd.setCornerRadius(16);
        tv.setBackground(gd);
        
        parent.addView(tv);
    }

    private void addSuggestionLabel(LinearLayout parent, String text) {
        TextView tv = new TextView(this);
        tv.setText("• " + text);
        tv.setTextColor(getResources().getColor(R.color.text_secondary, getTheme()));
        tv.setTextSize(14);
        tv.setPadding(0, 8, 0, 12);
        parent.addView(tv);
    }
}
