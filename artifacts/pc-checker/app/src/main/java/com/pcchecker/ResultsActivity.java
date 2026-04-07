package com.pcchecker;

import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.model.PCComponent;
import com.pcchecker.utils.BottleneckUtils;
import com.pcchecker.utils.CompatibilityUtils;
import com.pcchecker.utils.SuggestionUtils;

import java.util.List;

public class ResultsActivity extends AppCompatActivity {

    private BuildManager buildManager;

    private TextView tvCompatHeader, tvBalanceLabel;
    private ProgressBar pbBalance;
    private LinearLayout llErrors, llWarnings, llSuggestions;
    private TextView tvBottleneck, tvBottleneckDesc;
    private ProgressBar pbBottleneck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_results);
        buildManager = BuildManager.getInstance();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        tvCompatHeader = findViewById(R.id.tv_compat_header);
        tvBalanceLabel = findViewById(R.id.tv_balance_label);
        pbBalance = findViewById(R.id.pb_balance);
        llErrors = findViewById(R.id.ll_errors);
        llWarnings = findViewById(R.id.ll_warnings);
        llSuggestions = findViewById(R.id.ll_suggestions);
        tvBottleneck = findViewById(R.id.tv_bottleneck);
        tvBottleneckDesc = findViewById(R.id.tv_bottleneck_desc);
        pbBottleneck = findViewById(R.id.pb_bottleneck);

        loadResults();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadResults() {
        CompatibilityResult result = CompatibilityUtils.check(buildManager.getBuild());
        BottleneckUtils.BottleneckReport bottleneck = BottleneckUtils.analyze(buildManager.getBuild());
        List<String> suggestions = SuggestionUtils.getSuggestions(buildManager.getBuild());

        // Header
        if (result.isCompatible()) {
            tvCompatHeader.setText("Build is Compatible!");
            tvCompatHeader.setTextColor(Color.parseColor("#2E7D32"));
        } else {
            tvCompatHeader.setText(result.getErrors().size() + " Compatibility Error(s) Found");
            tvCompatHeader.setTextColor(Color.parseColor("#C62828"));
        }

        // Balance
        int balance = result.getPerformanceBalance();
        tvBalanceLabel.setText("Performance Balance: " + balance + "%");
        pbBalance.setProgress(balance);

        // Errors
        llErrors.removeAllViews();
        if (result.getErrors().isEmpty()) {
            addLabel(llErrors, "No errors — good to go!", Color.parseColor("#2E7D32"));
        } else {
            for (String err : result.getErrors()) {
                addLabel(llErrors, "✗ " + err, Color.parseColor("#C62828"));
            }
        }

        // Warnings
        llWarnings.removeAllViews();
        if (result.getWarnings().isEmpty()) {
            addLabel(llWarnings, "No warnings.", Color.parseColor("#757575"));
        } else {
            for (String warn : result.getWarnings()) {
                addLabel(llWarnings, "⚠ " + warn, Color.parseColor("#E65100"));
            }
        }

        // Bottleneck
        if (bottleneck.severity == 0) {
            tvBottleneck.setText("No significant bottleneck");
            tvBottleneck.setTextColor(Color.parseColor("#2E7D32"));
            pbBottleneck.setProgress(0);
        } else {
            tvBottleneck.setText("Bottleneck: " + bottleneck.component + " (" + bottleneck.severity + "% severity)");
            tvBottleneck.setTextColor(Color.parseColor("#E65100"));
            pbBottleneck.setProgress(bottleneck.severity);
        }
        tvBottleneckDesc.setText(bottleneck.description != null ? bottleneck.description : "");

        // Suggestions
        llSuggestions.removeAllViews();
        if (suggestions.isEmpty()) {
            addLabel(llSuggestions, "Build looks well-optimized!", Color.parseColor("#2E7D32"));
        } else {
            for (String s : suggestions) {
                addLabel(llSuggestions, "• " + s, Color.parseColor("#1565C0"));
            }
        }
    }

    private void addLabel(LinearLayout parent, String text, int color) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTextColor(color);
        tv.setTextSize(14);
        tv.setPadding(0, 8, 0, 8);
        parent.addView(tv);
    }
}
