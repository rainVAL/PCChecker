package com.pcchecker.utils;

import com.pcchecker.data.ComponentDatabase;
import com.pcchecker.model.PCComponent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SmartBuildUtils {

    public static Map<PCComponent.Category, PCComponent> autoComplete(Map<PCComponent.Category, PCComponent> currentBuild) {
        Map<PCComponent.Category, PCComponent> newBuild = new HashMap<>(currentBuild);
        List<PCComponent> allComponents = ComponentDatabase.getAll();

        // Determine target tier based on existing components
        PCComponent.PriceTier targetTier = PCComponent.PriceTier.MID;
        int scoreCount = 0;
        int totalScore = 0;
        for (PCComponent c : currentBuild.values()) {
            totalScore += c.getPerformanceScore();
            scoreCount++;
        }
        if (scoreCount > 0) {
            int avg = totalScore / scoreCount;
            if (avg > 80) targetTier = PCComponent.PriceTier.HIGH_END;
            else if (avg < 50) targetTier = PCComponent.PriceTier.BUDGET;
        }

        // Fill missing components
        for (PCComponent.Category cat : PCComponent.Category.values()) {
            if (!newBuild.containsKey(cat)) {
                PCComponent bestMatch = findBestMatch(cat, newBuild, allComponents, targetTier);
                if (bestMatch != null) {
                    newBuild.put(cat, bestMatch);
                }
            }
        }

        return newBuild;
    }

    private static PCComponent findBestMatch(PCComponent.Category cat,
                                              Map<PCComponent.Category, PCComponent> build,
                                              List<PCComponent> all,
                                              PCComponent.PriceTier tier) {
        List<PCComponent> candidates = new ArrayList<>();
        for (PCComponent c : all) {
            if (c.getCategory() == cat) {
                // Basic compatibility check
                if (isCompatible(c, build)) {
                    candidates.add(c);
                }
            }
        }

        if (candidates.isEmpty()) return null;

        // Score candidates based on how close they are to target tier and performance
        Collections.sort(candidates, (a, b) -> {
            int scoreA = (a.getPriceTier() == tier ? 50 : 0) + a.getPerformanceScore();
            int scoreB = (b.getPriceTier() == tier ? 50 : 0) + b.getPerformanceScore();
            return Integer.compare(scoreB, scoreA);
        });

        return candidates.get(0);
    }

    private static boolean isCompatible(PCComponent c, Map<PCComponent.Category, PCComponent> build) {
        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent mb = build.get(PCComponent.Category.MOTHERBOARD);
        PCComponent ram = build.get(PCComponent.Category.RAM);
        PCComponent gpu = build.get(PCComponent.Category.GPU);
        PCComponent pcCase = build.get(PCComponent.Category.CASE);

        switch (c.getCategory()) {
            case CPU:
                if (mb != null && !c.getSocket().equals(mb.getSocket())) return false;
                break;
            case MOTHERBOARD:
                if (cpu != null && !c.getSocket().equals(cpu.getSocket())) return false;
                if (ram != null && !c.getSupportedMemoryType().equals(ram.getMemoryType())) return false;
                if (pcCase != null && "ATX".equals(c.getFormFactor()) && "mATX".equals(pcCase.getSupportedFormFactor())) return false;
                break;
            case RAM:
                if (mb != null && !c.getMemoryType().equals(mb.getSupportedMemoryType())) return false;
                break;
            case GPU:
                if (pcCase != null && c.getGpuLengthMm() > pcCase.getMaxGpuLengthMm()) return false;
                break;
            case CASE:
                if (mb != null && "ATX".equals(mb.getFormFactor()) && "mATX".equals(c.getSupportedFormFactor())) return false;
                if (gpu != null && gpu.getGpuLengthMm() > c.getMaxGpuLengthMm()) return false;
                break;
        }
        return true;
    }
}
