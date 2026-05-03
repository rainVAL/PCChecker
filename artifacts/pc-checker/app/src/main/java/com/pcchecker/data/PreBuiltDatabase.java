package com.pcchecker.data;

import com.pcchecker.model.PCComponent;
import com.pcchecker.model.PreBuiltPC;

import java.util.ArrayList;
import java.util.List;

public class PreBuiltDatabase {

    public static List<PreBuiltPC> getPreBuilts() {
        List<PreBuiltPC> list = new ArrayList<>();
        List<PCComponent> all = ComponentDatabase.getAll();

        // 1. Budget Starter
        PreBuiltPC budget = new PreBuiltPC("Budget Starter", "A solid entry-level build for home office and light 1080p gaming.", PCComponent.PriceTier.BUDGET, "https://example.com/budget_pc.jpg");
        addById(budget, all, "cpu-11"); // Ryzen 5 5500
        addById(budget, all, "gpu-12"); // RX 6600
        addById(budget, all, "ram-6");  // 8GB DDR4
        addById(budget, all, "mb-11");  // B550M DS3H
        addById(budget, all, "sto-5");  // 2TB HDD
        addById(budget, all, "psu-6");  // 430W PSU
        addById(budget, all, "cas-7");  // Versa H18
        list.add(budget);

        // 2. Mid-Range Beast
        PreBuiltPC mid = new PreBuiltPC("Mid-Range Beast", "Perfectly balanced for high-refresh 1440p gaming.", PCComponent.PriceTier.MID, "https://example.com/mid_pc.jpg");
        addById(mid, all, "cpu-3");  // i5-14600K
        addById(mid, all, "gpu-5");  // RTX 4060 Ti
        addById(mid, all, "ram-5");  // 16GB DDR4
        addById(mid, all, "mb-4");   // Prime Z790-P
        addById(mid, all, "sto-2");  // 1TB NVMe
        addById(mid, all, "psu-4");  // 650W PSU
        addById(mid, all, "cas-5");  // 4000D Airflow
        list.add(mid);

        // 3. Ultimate Enthusiast
        PreBuiltPC high = new PreBuiltPC("Ultimate Enthusiast", "No compromises. Top-of-the-line performance for 4K gaming and creation.", PCComponent.PriceTier.HIGH_END, "https://example.com/high_pc.jpg");
        addById(high, all, "cpu-1");  // i9-14900K
        addById(high, all, "gpu-1");  // RTX 4090
        addById(high, all, "ram-1");  // 32GB DDR5
        addById(high, all, "mb-1");   // ROG Maximus Z790
        addById(high, all, "sto-1");  // 2TB Gen4 NVMe
        addById(high, all, "psu-1");  // 1000W PSU
        addById(high, all, "cas-1");  // O11 Dynamic
        list.add(high);

        return list;
    }

    private static void addById(PreBuiltPC pc, List<PCComponent> all, String id) {
        for (PCComponent c : all) {
            if (c.getId().equals(id)) {
                pc.addComponent(c);
                return;
            }
        }
    }
}
