package com.pcchecker.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PreBuiltPC implements Serializable {
    private String name;
    private String description;
    private String imageUrl;
    private List<PCComponent> components;
    private PCComponent.PriceTier tier;

    public PreBuiltPC(String name, String description, PCComponent.PriceTier tier, String imageUrl) {
        this.name = name;
        this.description = description;
        this.tier = tier;
        this.imageUrl = imageUrl;
        this.components = new ArrayList<>();
    }

    public void addComponent(PCComponent component) {
        components.add(component);
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public List<PCComponent> getComponents() { return components; }
    public PCComponent.PriceTier getTier() { return tier; }

    public String getPartsSummary() {
        StringBuilder sb = new StringBuilder();
        for (PCComponent c : components) {
            sb.append("• ").append(c.getCategoryDisplayName()).append(": ").append(c.getName()).append("\n");
        }
        return sb.toString().trim();
    }

    public double getTotalPrice() {
        double total = 0;
        for (PCComponent c : components) total += c.getPricePhp();
        return total;
    }
}
