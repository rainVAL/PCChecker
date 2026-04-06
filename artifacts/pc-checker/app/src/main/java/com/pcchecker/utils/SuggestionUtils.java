package com.pcchecker.utils;

import com.pcchecker.model.PCComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SuggestionUtils {

    public static List<String> getSuggestions(Map<PCComponent.Category, PCComponent> build,
                                               PCComponent.UseCase useCase) {
        List<String> suggestions = new ArrayList<>();

        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent gpu = build.get(PCComponent.Category.GPU);
        PCComponent ram = build.get(PCComponent.Category.RAM);
        PCComponent storage = build.get(PCComponent.Category.STORAGE);
        PCComponent psu = build.get(PCComponent.Category.PSU);

        // Missing components
        if (cpu == null) suggestions.add("Add a CPU to complete your build.");
        if (gpu == null && (cpu == null || !cpu.hasIntegratedGraphics())) {
            suggestions.add("Add a GPU — your CPU has no integrated graphics.");
        }
        if (ram == null) suggestions.add("Add RAM to your build.");
        if (storage == null) suggestions.add("Add a storage drive to install your OS.");
        if (psu == null) suggestions.add("Add a power supply unit.");

        // RAM upgrade suggestion
        if (ram != null && ram.getRamCapacityGb() < 16) {
            suggestions.add("Upgrade to at least 16GB RAM for a better experience.");
        }

        // Gaming-specific
        if (useCase == PCComponent.UseCase.GAMING) {
            if (gpu != null && gpu.getPerformanceScore() < 55) {
                suggestions.add("For gaming, consider a higher-tier GPU (RTX 4060 or above).");
            }
            if (storage != null && storage.getStorageType().equals("HDD")) {
                suggestions.add("Upgrade to an NVMe SSD for significantly faster game load times.");
            }
            if (ram != null && ram.getRamSpeedMhz() < 3200) {
                suggestions.add("For gaming, use DDR4-3200 or faster RAM.");
            }
        }

        // Productivity-specific
        if (useCase == PCComponent.UseCase.PRODUCTIVITY) {
            if (cpu != null && cpu.getPerformanceScore() < 60) {
                suggestions.add("For productivity workloads, consider a higher-core-count CPU.");
            }
            if (ram != null && ram.getRamCapacityGb() < 32) {
                suggestions.add("32GB or more RAM is recommended for heavy productivity workloads.");
            }
            if (storage != null && storage.getStorageCapacityGb() < 1000) {
                suggestions.add("Consider more storage capacity for large project files.");
            }
        }

        // Power headroom
        if (psu != null) {
            int draw = CompatibilityUtils.estimatePowerDraw(build);
            if (psu.getWattage() >= draw * 1.5) {
                suggestions.add("PSU wattage is more than enough — consider a lower wattage for efficiency.");
            }
        }

        return suggestions;
    }
}
