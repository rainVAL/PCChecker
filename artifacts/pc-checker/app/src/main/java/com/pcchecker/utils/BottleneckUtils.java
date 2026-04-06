package com.pcchecker.utils;

import com.pcchecker.model.PCComponent;

import java.util.Map;

public class BottleneckUtils {

    public static class BottleneckReport {
        public String component;
        public int severity; // 0-100
        public String description;
    }

    public static BottleneckReport analyze(Map<PCComponent.Category, PCComponent> build) {
        PCComponent cpu = build.get(PCComponent.Category.CPU);
        PCComponent gpu = build.get(PCComponent.Category.GPU);

        BottleneckReport report = new BottleneckReport();
        report.severity = 0;

        if (cpu == null || gpu == null) {
            report.description = "Add both a CPU and GPU to see bottleneck analysis.";
            return report;
        }

        int cpuScore = cpu.getPerformanceScore();
        int gpuScore = gpu.getPerformanceScore();
        int diff = cpuScore - gpuScore;

        if (Math.abs(diff) <= 10) {
            report.description = "CPU and GPU are well-matched. No significant bottleneck detected.";
            report.severity = 0;
        } else if (diff > 10) {
            report.component = "GPU";
            report.severity = Math.min((diff - 10) * 3, 100);
            report.description = "Your GPU (" + gpu.getName() + ") is weaker than your CPU. " +
                    "The CPU is sitting idle in intensive GPU workloads. " +
                    "Consider a higher-tier GPU for better balance.";
        } else {
            report.component = "CPU";
            report.severity = Math.min((Math.abs(diff) - 10) * 3, 100);
            report.description = "Your CPU (" + cpu.getName() + ") is limiting your GPU performance. " +
                    "In GPU-intensive games, the CPU may not feed frames fast enough. " +
                    "Consider a faster CPU or a lower-tier GPU.";
        }

        return report;
    }
}
