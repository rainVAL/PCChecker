package com.pcchecker;

import com.pcchecker.model.PCComponent;

import java.util.EnumMap;
import java.util.Map;

public class BuildManager {

    private static BuildManager instance;
    private Map<PCComponent.Category, PCComponent> build = new EnumMap<>(PCComponent.Category.class);
    private PCComponent.UseCase useCase = PCComponent.UseCase.GENERAL;
    private PCComponent.Category pendingSlot = null;

    private BuildManager() {}

    public static BuildManager getInstance() {
        if (instance == null) {
            instance = new BuildManager();
        }
        return instance;
    }

    public Map<PCComponent.Category, PCComponent> getBuild() { return build; }

    public void setComponent(PCComponent component) {
        build.put(component.getCategory(), component);
    }

    public void removeComponent(PCComponent.Category category) {
        build.remove(category);
    }

    public void clearBuild() { build.clear(); }

    public PCComponent getComponent(PCComponent.Category category) {
        return build.get(category);
    }

    public PCComponent.UseCase getUseCase() { return useCase; }
    public void setUseCase(PCComponent.UseCase useCase) { this.useCase = useCase; }

    public PCComponent.Category getPendingSlot() { return pendingSlot; }
    public void setPendingSlot(PCComponent.Category pendingSlot) { this.pendingSlot = pendingSlot; }

    public double getTotalPrice() {
        double total = 0;
        for (PCComponent c : build.values()) total += c.getPricePhp();
        return total;
    }

    public int getComponentCount() { return build.size(); }
}
