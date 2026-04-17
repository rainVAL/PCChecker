package com.pcchecker.model;

import java.io.Serializable;
import java.util.Locale;

public class PCComponent implements Serializable {

    public enum Category {
        CPU, GPU, RAM, MOTHERBOARD, STORAGE, PSU, CASE
    }

    public enum UseCase {
        GAMING, PRODUCTIVITY, GENERAL
    }

    public enum PriceTier {
        BUDGET, MID, HIGH_END
    }

    private String id;
    private String name;
    private String brand;
    private Category category;
    private double pricePhp;
    private int performanceScore;
    private UseCase recommendedUseCase;
    private PriceTier priceTier;
    private String description;
    private String imageUrl; // Added imageUrl field

    // CPU / Motherboard
    private String socket;
    // CPU
    private int tdp;
    private boolean hasIntegratedGraphics;
    // RAM
    private String memoryType;
    private int ramSpeedMhz;
    private int ramCapacityGb;
    // GPU
    private int gpuLengthMm;
    private int gpuTdp;
    // Motherboard
    private String supportedMemoryType;
    private String formFactor;
    // PSU
    private int wattage;
    // Case
    private int maxGpuLengthMm;
    private String supportedFormFactor;
    // Storage
    private int storageCapacityGb;
    private String storageType;

    public PCComponent() {}

    // ---- Getters and Setters ----

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getBrand() { return brand; }
    public void setBrand(String brand) { this.brand = brand; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public double getPricePhp() { return pricePhp; }
    public void setPricePhp(double pricePhp) { this.pricePhp = pricePhp; }

    public String getPriceRange() {
        double min = Math.floor(pricePhp / 1000) * 1000;
        double max = min + 5000;
        return String.format(Locale.US, "₱%,.0f - ₱%,.0f", min, max);
    }

    public int getPerformanceScore() { return performanceScore; }
    public void setPerformanceScore(int performanceScore) { this.performanceScore = performanceScore; }

    public UseCase getRecommendedUseCase() { return recommendedUseCase; }
    public void setRecommendedUseCase(UseCase recommendedUseCase) { this.recommendedUseCase = recommendedUseCase; }

    public PriceTier getPriceTier() { return priceTier; }
    public void setPriceTier(PriceTier priceTier) { this.priceTier = priceTier; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getSocket() { return socket; }
    public void setSocket(String socket) { this.socket = socket; }

    public int getTdp() { return tdp; }
    public void setTdp(int tdp) { this.tdp = tdp; }

    public boolean hasIntegratedGraphics() { return hasIntegratedGraphics; }
    public void setHasIntegratedGraphics(boolean hasIntegratedGraphics) { this.hasIntegratedGraphics = hasIntegratedGraphics; }

    public String getMemoryType() { return memoryType; }
    public void setMemoryType(String memoryType) { this.memoryType = memoryType; }

    public int getRamSpeedMhz() { return ramSpeedMhz; }
    public void setRamSpeedMhz(int ramSpeedMhz) { this.ramSpeedMhz = ramSpeedMhz; }

    public int getRamCapacityGb() { return ramCapacityGb; }
    public void setRamCapacityGb(int ramCapacityGb) { this.ramCapacityGb = ramCapacityGb; }

    public int getGpuLengthMm() { return gpuLengthMm; }
    public void setGpuLengthMm(int gpuLengthMm) { this.gpuLengthMm = gpuLengthMm; }

    public int getGpuTdp() { return gpuTdp; }
    public void setGpuTdp(int gpuTdp) { this.gpuTdp = gpuTdp; }

    public String getSupportedMemoryType() { return supportedMemoryType; }
    public void setSupportedMemoryType(String supportedMemoryType) { this.supportedMemoryType = supportedMemoryType; }

    public String getFormFactor() { return formFactor; }
    public void setFormFactor(String formFactor) { this.formFactor = formFactor; }

    public int getWattage() { return wattage; }
    public void setWattage(int wattage) { this.wattage = wattage; }

    public int getMaxGpuLengthMm() { return maxGpuLengthMm; }
    public void setMaxGpuLengthMm(int maxGpuLengthMm) { this.maxGpuLengthMm = maxGpuLengthMm; }

    public String getSupportedFormFactor() { return supportedFormFactor; }
    public void setSupportedFormFactor(String supportedFormFactor) { this.supportedFormFactor = supportedFormFactor; }

    public int getStorageCapacityGb() { return storageCapacityGb; }
    public void setStorageCapacityGb(int storageCapacityGb) { this.storageCapacityGb = storageCapacityGb; }

    public String getStorageType() { return storageType; }
    public void setStorageType(String storageType) { this.storageType = storageType; }

    public String getCategoryDisplayName() {
        switch (category) {
            case CPU: return "CPU";
            case GPU: return "GPU";
            case RAM: return "RAM";
            case MOTHERBOARD: return "Motherboard";
            case STORAGE: return "Storage";
            case PSU: return "Power Supply";
            case CASE: return "Case";
            default: return "Unknown";
        }
    }

    public String getSpecSummary() {
        switch (category) {
            case CPU:
                return socket + " | " + tdp + "W TDP" + (hasIntegratedGraphics ? " | iGPU" : "");
            case GPU:
                return gpuLengthMm + "mm | " + gpuTdp + "W TDP";
            case RAM:
                return ramCapacityGb + "GB " + memoryType + " " + ramSpeedMhz + "MHz";
            case MOTHERBOARD:
                return socket + " | " + supportedMemoryType + " | " + formFactor;
            case STORAGE:
                return storageCapacityGb + "GB " + storageType;
            case PSU:
                return wattage + "W";
            case CASE:
                return formFactor + " | Max GPU: " + maxGpuLengthMm + "mm";
            default:
                return "";
        }
    }
}
