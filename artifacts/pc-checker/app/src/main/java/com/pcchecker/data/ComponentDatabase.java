package com.pcchecker.data;

import com.pcchecker.model.PCComponent;
import com.pcchecker.model.PCComponent.Category;
import com.pcchecker.model.PCComponent.UseCase;
import com.pcchecker.model.PCComponent.PriceTier;

import java.util.ArrayList;
import java.util.List;

public class ComponentDatabase {

    private static List<PCComponent> components;

    public static List<PCComponent> getAll() {
        if (components == null) {
            components = buildDatabase();
        }
        return components;
    }

    public static List<PCComponent> getByCategory(Category category) {
        List<PCComponent> result = new ArrayList<>();
        for (PCComponent c : getAll()) {
            if (c.getCategory() == category) result.add(c);
        }
        return result;
    }

    private static List<PCComponent> buildDatabase() {
        List<PCComponent> list = new ArrayList<>();

        // ---- CPUs ----
        list.add(cpu("cpu-1", "Intel Core i9-14900K", "Intel", "LGA1700", 125, true, 89, 549.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(cpu("cpu-2", "Intel Core i7-14700K", "Intel", "LGA1700", 125, true, 78, 389.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(cpu("cpu-3", "Intel Core i5-14600K", "Intel", "LGA1700", 125, true, 68, 279.99, PriceTier.MID, UseCase.GENERAL));
        list.add(cpu("cpu-4", "Intel Core i5-13400", "Intel", "LGA1700", 65, true, 58, 189.99, PriceTier.MID, UseCase.GENERAL));
        list.add(cpu("cpu-5", "Intel Core i3-13100", "Intel", "LGA1700", 60, true, 42, 109.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(cpu("cpu-6", "AMD Ryzen 9 7950X", "AMD", "AM5", 170, false, 95, 699.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));
        list.add(cpu("cpu-7", "AMD Ryzen 9 7900X", "AMD", "AM5", 170, false, 88, 449.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));
        list.add(cpu("cpu-8", "AMD Ryzen 7 7700X", "AMD", "AM5", 105, false, 76, 299.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(cpu("cpu-9", "AMD Ryzen 5 7600X", "AMD", "AM5", 105, false, 65, 199.99, PriceTier.MID, UseCase.GAMING));
        list.add(cpu("cpu-10", "AMD Ryzen 5 5600X", "AMD", "AM4", 65, false, 60, 149.99, PriceTier.MID, UseCase.GAMING));
        list.add(cpu("cpu-11", "AMD Ryzen 5 5500", "AMD", "AM4", 65, false, 48, 89.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(cpu("cpu-12", "Intel Core i9-13900K", "Intel", "LGA1700", 125, true, 85, 499.99, PriceTier.HIGH_END, UseCase.GAMING));

        // ---- GPUs ----
        list.add(gpu("gpu-1", "NVIDIA RTX 4090", "NVIDIA", 336, 450, 99, 1599.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-2", "NVIDIA RTX 4080", "NVIDIA", 336, 320, 91, 999.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-3", "NVIDIA RTX 4070 Ti", "NVIDIA", 285, 285, 84, 749.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-4", "NVIDIA RTX 4070", "NVIDIA", 244, 200, 76, 549.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-5", "NVIDIA RTX 4060 Ti", "NVIDIA", 240, 165, 68, 399.99, PriceTier.MID, UseCase.GAMING));
        list.add(gpu("gpu-6", "NVIDIA RTX 4060", "NVIDIA", 240, 115, 60, 299.99, PriceTier.MID, UseCase.GAMING));
        list.add(gpu("gpu-7", "AMD RX 7900 XTX", "AMD", 287, 355, 93, 949.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-8", "AMD RX 7900 XT", "AMD", 276, 315, 86, 799.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(gpu("gpu-9", "AMD RX 7800 XT", "AMD", 267, 263, 73, 449.99, PriceTier.MID, UseCase.GAMING));
        list.add(gpu("gpu-10", "AMD RX 7600", "AMD", 200, 165, 55, 249.99, PriceTier.MID, UseCase.GENERAL));
        list.add(gpu("gpu-11", "NVIDIA RTX 3060", "NVIDIA", 242, 170, 52, 199.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(gpu("gpu-12", "AMD RX 6600", "AMD", 230, 132, 46, 169.99, PriceTier.BUDGET, UseCase.GENERAL));

        // ---- RAM ----
        list.add(ram("ram-1", "Corsair Vengeance DDR5 32GB", "Corsair", "DDR5", 6000, 32, 72, 149.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(ram("ram-2", "G.Skill Trident Z5 32GB", "G.Skill", "DDR5", 6400, 32, 80, 179.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));
        list.add(ram("ram-3", "Corsair Vengeance DDR5 16GB", "Corsair", "DDR5", 5600, 16, 60, 79.99, PriceTier.MID, UseCase.GENERAL));
        list.add(ram("ram-4", "Kingston Fury DDR4 32GB", "Kingston", "DDR4", 3600, 32, 62, 89.99, PriceTier.MID, UseCase.GENERAL));
        list.add(ram("ram-5", "Corsair Vengeance DDR4 16GB", "Corsair", "DDR4", 3200, 16, 50, 49.99, PriceTier.MID, UseCase.GENERAL));
        list.add(ram("ram-6", "TeamGroup DDR4 8GB", "TeamGroup", "DDR4", 2666, 8, 30, 19.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(ram("ram-7", "G.Skill Ripjaws DDR4 16GB", "G.Skill", "DDR4", 3600, 16, 55, 59.99, PriceTier.MID, UseCase.GAMING));
        list.add(ram("ram-8", "Corsair Dominator DDR5 64GB", "Corsair", "DDR5", 5200, 64, 88, 259.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));

        // ---- Motherboards ----
        list.add(mb("mb-1", "ASUS ROG Maximus Z790", "ASUS", "LGA1700", "DDR5", "ATX", 82, 549.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(mb("mb-2", "MSI MEG Z790 ACE", "MSI", "LGA1700", "DDR5", "ATX", 78, 449.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(mb("mb-3", "Gigabyte Z790 AORUS Elite", "Gigabyte", "LGA1700", "DDR5", "ATX", 70, 279.99, PriceTier.MID, UseCase.GENERAL));
        list.add(mb("mb-4", "ASUS Prime Z790-P", "ASUS", "LGA1700", "DDR4", "ATX", 60, 189.99, PriceTier.MID, UseCase.GENERAL));
        list.add(mb("mb-5", "MSI Pro B760M-A", "MSI", "LGA1700", "DDR4", "mATX", 45, 109.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(mb("mb-6", "ASUS ROG Crosshair X670E", "ASUS", "AM5", "DDR5", "ATX", 85, 599.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(mb("mb-7", "MSI MEG X670E ACE", "MSI", "AM5", "DDR5", "ATX", 80, 499.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));
        list.add(mb("mb-8", "Gigabyte X670 AORUS Elite", "Gigabyte", "AM5", "DDR5", "ATX", 72, 299.99, PriceTier.MID, UseCase.GENERAL));
        list.add(mb("mb-9", "MSI MAG B650M Mortar", "MSI", "AM5", "DDR5", "mATX", 58, 189.99, PriceTier.MID, UseCase.GENERAL));
        list.add(mb("mb-10", "ASUS TUF B550-Plus", "ASUS", "AM4", "DDR4", "ATX", 55, 149.99, PriceTier.MID, UseCase.GENERAL));
        list.add(mb("mb-11", "Gigabyte B550M DS3H", "Gigabyte", "AM4", "DDR4", "mATX", 40, 79.99, PriceTier.BUDGET, UseCase.GENERAL));

        // ---- Storage ----
        list.add(storage("sto-1", "Samsung 990 Pro 2TB", "Samsung", "NVMe SSD", 2000, 80, 199.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(storage("sto-2", "Samsung 980 Pro 1TB", "Samsung", "NVMe SSD", 1000, 72, 109.99, PriceTier.MID, UseCase.GAMING));
        list.add(storage("sto-3", "WD Black SN850X 1TB", "Western Digital", "NVMe SSD", 1000, 74, 99.99, PriceTier.MID, UseCase.GAMING));
        list.add(storage("sto-4", "Crucial MX500 1TB", "Crucial", "SATA SSD", 1000, 52, 59.99, PriceTier.MID, UseCase.GENERAL));
        list.add(storage("sto-5", "Seagate Barracuda 2TB", "Seagate", "HDD", 2000, 30, 49.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(storage("sto-6", "Kingston A2000 500GB", "Kingston", "NVMe SSD", 500, 48, 44.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(storage("sto-7", "WD Blue 4TB", "Western Digital", "HDD", 4000, 28, 79.99, PriceTier.BUDGET, UseCase.PRODUCTIVITY));
        list.add(storage("sto-8", "Sabrent Rocket 4TB", "Sabrent", "NVMe SSD", 4000, 85, 349.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));

        // ---- PSUs ----
        list.add(psu("psu-1", "Corsair HX1000i", "Corsair", 1000, 75, 199.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(psu("psu-2", "EVGA SuperNOVA 850 G6", "EVGA", 850, 65, 139.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(psu("psu-3", "Seasonic Focus GX-750", "Seasonic", 750, 62, 119.99, PriceTier.MID, UseCase.GENERAL));
        list.add(psu("psu-4", "be quiet! Pure Power 11 650W", "be quiet!", 650, 55, 89.99, PriceTier.MID, UseCase.GENERAL));
        list.add(psu("psu-5", "Corsair CX550", "Corsair", 550, 45, 59.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(psu("psu-6", "EVGA 430 W1", "EVGA", 430, 30, 34.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(psu("psu-7", "Corsair RM1000x", "Corsair", 1000, 78, 179.99, PriceTier.HIGH_END, UseCase.PRODUCTIVITY));
        list.add(psu("psu-8", "Seasonic Prime TX-1300", "Seasonic", 1300, 85, 299.99, PriceTier.HIGH_END, UseCase.GAMING));

        // ---- Cases ----
        list.add(pccase("cas-1", "Lian Li PC-O11 Dynamic", "Lian Li", "ATX", 400, 68, 149.99, PriceTier.HIGH_END, UseCase.GAMING));
        list.add(pccase("cas-2", "Fractal Design Meshify 2", "Fractal Design", "ATX", 467, 65, 129.99, PriceTier.HIGH_END, UseCase.GENERAL));
        list.add(pccase("cas-3", "NZXT H510", "NZXT", "ATX", 360, 55, 69.99, PriceTier.MID, UseCase.GENERAL));
        list.add(pccase("cas-4", "Phanteks Eclipse P400A", "Phanteks", "ATX", 420, 58, 79.99, PriceTier.MID, UseCase.GENERAL));
        list.add(pccase("cas-5", "Corsair 4000D Airflow", "Corsair", "ATX", 360, 60, 94.99, PriceTier.MID, UseCase.GENERAL));
        list.add(pccase("cas-6", "Cooler Master N200", "Cooler Master", "mATX", 320, 38, 49.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(pccase("cas-7", "Thermaltake Versa H18", "Thermaltake", "mATX", 310, 32, 29.99, PriceTier.BUDGET, UseCase.GENERAL));
        list.add(pccase("cas-8", "Lian Li Lancool 216", "Lian Li", "ATX", 435, 72, 109.99, PriceTier.MID, UseCase.GAMING));

        return list;
    }

    // ---- Builders ----

    private static PCComponent cpu(String id, String name, String brand, String socket,
                                   int tdp, boolean iGPU, int score, double price,
                                   PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.CPU);
        c.setSocket(socket); c.setTdp(tdp); c.setHasIntegratedGraphics(iGPU);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent gpu(String id, String name, String brand, int length,
                                   int tdp, int score, double price,
                                   PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.GPU);
        c.setGpuLengthMm(length); c.setGpuTdp(tdp);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent ram(String id, String name, String brand, String memType,
                                   int speed, int capacityGb, int score, double price,
                                   PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.RAM);
        c.setMemoryType(memType); c.setRamSpeedMhz(speed); c.setRamCapacityGb(capacityGb);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent mb(String id, String name, String brand, String socket,
                                  String memType, String formFactor, int score, double price,
                                  PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.MOTHERBOARD);
        c.setSocket(socket); c.setSupportedMemoryType(memType); c.setFormFactor(formFactor);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent storage(String id, String name, String brand, String type,
                                       int capacityGb, int score, double price,
                                       PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.STORAGE);
        c.setStorageType(type); c.setStorageCapacityGb(capacityGb);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent psu(String id, String name, String brand, int wattage,
                                   int score, double price, PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.PSU);
        c.setWattage(wattage);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }

    private static PCComponent pccase(String id, String name, String brand, String formFactor,
                                      int maxGpuMm, int score, double price,
                                      PriceTier tier, UseCase useCase) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.CASE);
        c.setSupportedFormFactor(formFactor); c.setMaxGpuLengthMm(maxGpuMm);
        c.setFormFactor(formFactor);
        c.setPerformanceScore(score); c.setPrice(price);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        return c;
    }
}
