package com.pcchecker.utils;

import com.pcchecker.model.CompatibilityResult;
import com.pcchecker.model.PCComponent;

import java.util.Map;

public class CompatibilityUtils {

    public static CompatibilityResult check(Map<PCComponent.Category, PCComponent> build) {
        CompatibilityResult result = new CompatibilityResult();

        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent gpu = build.get(PCComponent.Category.GPU);
        PCComponent ram = build.get(PCComponent.Category.RAM);
        PCComponent mb = build.get(PCComponent.Category.MOTHERBOARD);
        PCComponent psu = build.get(PCComponent.Category.PSU);
        PCComponent pcCase = build.get(PCComponent.Category.CASE);

        // CPU + Motherboard socket
        if (cpu != null && mb != null) {
            if (!cpu.getSocket().equals(mb.getSocket())) {
                result.addError("CPU socket " + cpu.getSocket() +
                        " is incompatible with motherboard socket " + mb.getSocket() + ".");
            }
        }

        // RAM + Motherboard memory type
        if (ram != null && mb != null) {
            if (!ram.getMemoryType().equals(mb.getSupportedMemoryType())) {
                result.addError("RAM type " + ram.getMemoryType() +
                        " is not supported by this motherboard (" + mb.getSupportedMemoryType() + " required).");
            }
        }

        // GPU length vs case clearance
        if (gpu != null && pcCase != null) {
            if (gpu.getGpuLengthMm() > pcCase.getMaxGpuLengthMm()) {
                result.addError("GPU (" + gpu.getGpuLengthMm() + "mm) is too long for the case" +
                        " (max " + pcCase.getMaxGpuLengthMm() + "mm).");
            }
        }

        // Motherboard form factor vs case
        if (mb != null && pcCase != null) {
            String mbForm = mb.getFormFactor();
            String caseForm = pcCase.getSupportedFormFactor();
            if ("mATX".equals(mbForm) && "ATX".equals(caseForm)) {
                // mATX fits in ATX case — no issue
            } else if ("ATX".equals(mbForm) && "mATX".equals(caseForm)) {
                result.addError("ATX motherboard does not fit in a mATX case.");
            }
        }

        // Power check
        if (psu != null) {
            int totalDraw = estimatePowerDraw(build);
            int requiredWithHeadroom = (int) (totalDraw * 1.25);
            if (psu.getWattage() < totalDraw) {
                result.addError("PSU (" + psu.getWattage() + "W) is insufficient for this build" +
                        " (estimated " + totalDraw + "W draw).");
            } else if (psu.getWattage() < requiredWithHeadroom) {
                result.addWarning("PSU has little headroom. Recommended: " +
                        requiredWithHeadroom + "W for safety (current: " + psu.getWattage() + "W).");
            }
        }

        // No GPU + no iGPU
        if (gpu == null && cpu != null && !cpu.hasIntegratedGraphics()) {
            result.addWarning("No GPU selected and CPU has no integrated graphics — no display output.");
        }

        // RAM below recommended
        if (ram != null && ram.getRamCapacityGb() < 16) {
            result.addWarning("8GB RAM is low. 16GB or more is recommended for modern use.");
        }

        // Performance balance
        int balance = calculateBalance(build);
        result.setPerformanceBalance(balance);
        if (balance < 60) {
            result.addWarning("Performance is imbalanced — some components may be bottlenecking others.");
        }

        return result;
    }

    public static int estimatePowerDraw(Map<PCComponent.Category, PCComponent> build) {
        int total = 75; // base system power

        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent gpu = build.get(PCComponent.Category.GPU);
        PCComponent ram = build.get(PCComponent.Category.RAM);
        PCComponent storage = build.get(PCComponent.Category.STORAGE);

        if (cpu != null) total += cpu.getTdp();
        if (gpu != null) total += gpu.getGpuTdp();
        if (ram != null) total += 10;
        if (storage != null) total += 10;

        return total;
    }

    private static int calculateBalance(Map<PCComponent.Category, PCComponent> build) {
        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent gpu = build.get(PCComponent.Category.GPU);
        if (cpu == null || gpu == null) return 70;

        int diff = Math.abs(cpu.getPerformanceScore() - gpu.getPerformanceScore());
        if (diff <= 5) return 100;
        if (diff <= 10) return 90;
        if (diff <= 15) return 80;
        if (diff <= 20) return 70;
        if (diff <= 30) return 60;
        if (diff <= 40) return 50;
        return 40;
    }
}
