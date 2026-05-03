package com.pcchecker.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CompatibilityResult {

    private List<String> errors = new ArrayList<>();
    private List<String> warnings = new ArrayList<>();
    private Map<PCComponent.Category, String> errorMap = new HashMap<>();
    private int performanceBalance;
    private String bottleneck;
    private int bottleneckSeverity;
    private List<String> suggestions = new ArrayList<>();

    public List<String> getErrors() { return errors; }
    public void addError(PCComponent.Category category, String error) {
        this.errors.add(error);
        if (category != null) errorMap.put(category, error);
    }

    public boolean hasError(PCComponent.Category category) {
        return errorMap.containsKey(category);
    }

    public List<String> getWarnings() { return warnings; }
    public void addWarning(String warning) { this.warnings.add(warning); }

    public int getPerformanceBalance() { return performanceBalance; }
    public void setPerformanceBalance(int performanceBalance) { this.performanceBalance = performanceBalance; }

    public String getBottleneck() { return bottleneck; }
    public void setBottleneck(String bottleneck) { this.bottleneck = bottleneck; }

    public int getBottleneckSeverity() { return bottleneckSeverity; }
    public void setBottleneckSeverity(int bottleneckSeverity) { this.bottleneckSeverity = bottleneckSeverity; }

    public List<String> getSuggestions() { return suggestions; }
    public void addSuggestion(String suggestion) { this.suggestions.add(suggestion); }

    public boolean isCompatible() { return errors.isEmpty(); }

    public String getStatusSummary() {
        if (!errors.isEmpty()) {
            return errors.size() + " error(s), " + warnings.size() + " warning(s)";
        } else if (!warnings.isEmpty()) {
            return warnings.size() + " warning(s) — check results";
        } else {
            return "All compatible!";
        }
    }
}
