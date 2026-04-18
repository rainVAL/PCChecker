package com.pcchecker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.utils.BottleneckUtils;
import com.pcchecker.utils.CompatibilityUtils;
import com.pcchecker.utils.SuggestionUtils;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private BuildManager buildManager;

    private TextView tvCompatHeader, tvBalanceLabel;
    private LinearProgressIndicator pbBalance;
    private LinearLayout llErrors, llWarnings, llSuggestions, llStatusHeader;
    private TextView tvBottleneck, tvBottleneckDesc;
    private LinearProgressIndicator pbBottleneck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        buildManager = BuildManager.getInstance();

        tvCompatHeader = findViewById(R.id.tv_compat_header);
        tvBalanceLabel = findViewById(R.id.tv_balance_label);
        pbBalance = findViewById(R.id.pb_balance);
        llErrors = findViewById(R.id.ll_errors);
        llWarnings = findViewById(R.id.ll_warnings);
        llSuggestions = findViewById(R.id.ll_suggestions);
        llStatusHeader = findViewById(R.id.ll_status_header);
        tvBottleneck = findViewById(R.id.tv_bottleneck);
        tvBottleneckDesc = findViewById(R.id.tv_bottleneck_desc);
        pbBottleneck = findViewById(R.id.pb_bottleneck);

        loadResults();
    }

    private void loadResults() {
        CompatibilityResult result = CompatibilityUtils.check(buildManager.getBuild());
        BottleneckUtils.BottleneckReport bottleneck = BottleneckUtils.analyze(buildManager.getBuild());
        List<String> suggestions = SuggestionUtils.getSuggestions(
                buildManager.getBuild(), buildManager.getUseCase());

        // Header
        if (result.isCompatible()) {
            tvCompatHeader.setText("Build is Compatible!");
            llStatusHeader.setBackgroundColor(getResources().getColor(R.color.success, getTheme()));
        } else {
            tvCompatHeader.setText(result.getErrors().size() + " Compatibility Error(s)");
            llStatusHeader.setBackgroundColor(getResources().getColor(R.color.error, getTheme()));
        }

        // Balance
        int balance = result.getPerformanceBalance();
        tvBalanceLabel.setText("Overall Sync: " + balance + "%");
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
        tvBottleneckDesc.setText(bottleneck.description != null ? bottleneck.description : "");

        // Suggestions
        llSuggestions.removeAllViews();
        if (suggestions.isEmpty()) {
            addSuggestionLabel(llSuggestions, "Build looks well-optimized!");
        } else {
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
        tv.setPadding(32, 24, 32, 24);
        
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 0, 0, 16);
        tv.setLayoutParams(params);
        
        android.graphics.drawable.GradientDrawable gd = new android.graphics.drawable.GradientDrawable();
        gd.setColor(bgColor);
        gd.setCornerRadius(12);
        tv.setBackground(gd);
        
        parent.addView(tv);
    }

    private void addSuggestionLabel(LinearLayout parent, String text) {
        TextView tv = new TextView(this);
        tv.setText("• " + text);
        tv.setTextColor(getResources().getColor(R.color.text_primary, getTheme()));
        tv.setTextSize(14);
        tv.setPadding(0, 8, 0, 12);
        parent.addView(tv);
    }
}
