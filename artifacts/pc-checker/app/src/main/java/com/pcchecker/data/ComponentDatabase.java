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
        list.add(cpu("cpu-1", "Intel Core i9-14900K", "Intel", "LGA1700", 125, true, 89, 32000, PriceTier.HIGH_END, UseCase.GAMING, "Extreme performance for enthusiasts and professional workloads.", "https://m.media-amazon.com/images/I/61bL89NXunL._AC_SL1500_.jpg"));
        list.add(cpu("cpu-2", "Intel Core i7-14700K", "Intel", "LGA1700", 125, true, 78, 24000, PriceTier.HIGH_END, UseCase.GAMING, "High-end gaming and multi-tasking powerhouse.", "https://m.media-amazon.com/images/I/61bL89NXunL._AC_SL1500_.jpg"));
        list.add(cpu("cpu-3", "Intel Core i5-14600K", "Intel", "LGA1700", 125, true, 68, 18000, PriceTier.MID, UseCase.GENERAL, "The sweet spot for gaming performance and value.", "https://m.media-amazon.com/images/I/61bL89NXunL._AC_SL1500_.jpg"));
        list.add(cpu("cpu-4", "Intel Core i5-13400", "Intel", "LGA1700", 65, true, 58, 13500, PriceTier.MID, UseCase.GENERAL, "Excellent mid-range CPU for everyday use and light gaming.", "https://m.media-amazon.com/images/I/51pI-E9TzTL._AC_SL1200_.jpg"));
        list.add(cpu("cpu-5", "Intel Core i3-13100", "Intel", "LGA1700", 60, true, 42, 7500, PriceTier.BUDGET, UseCase.GENERAL, "Budget-friendly quad-core CPU for home and office tasks.", "https://m.media-amazon.com/images/I/51pI-E9TzTL._AC_SL1200_.jpg"));
        list.add(cpu("cpu-6", "AMD Ryzen 9 7950X", "AMD", "AM5", 170, false, 95, 38000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Ultimate productivity monster with 16 cores for heavy workloads.", "https://m.media-amazon.com/images/I/51296N7p7tL._AC_SL1069_.jpg"));
        list.add(cpu("cpu-7", "AMD Ryzen 9 7900X", "AMD", "AM5", 170, false, 88, 28000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Powerful 12-core CPU for content creation and high-end gaming.", "https://m.media-amazon.com/images/I/51296N7p7tL._AC_SL1069_.jpg"));
        list.add(cpu("cpu-8", "AMD Ryzen 7 7700X", "AMD", "AM5", 105, false, 76, 19500, PriceTier.HIGH_END, UseCase.GAMING, "Fast 8-core processor ideal for modern gaming titles.", "https://m.media-amazon.com/images/I/51296N7p7tL._AC_SL1069_.jpg"));
        list.add(cpu("cpu-9", "AMD Ryzen 5 7600X", "AMD", "AM5", 105, false, 65, 14500, PriceTier.MID, UseCase.GAMING, "Efficient 6-core processor offering great gaming performance.", "https://m.media-amazon.com/images/I/51296N7p7tL._AC_SL1069_.jpg"));
        list.add(cpu("cpu-10", "AMD Ryzen 5 5600X", "AMD", "AM4", 65, false, 60, 9500, PriceTier.MID, UseCase.GAMING, "Popular last-gen gaming CPU still offering great value.", "https://m.media-amazon.com/images/I/616VM20+AzL._AC_SL1384_.jpg"));
        list.add(cpu("cpu-11", "AMD Ryzen 5 5500", "AMD", "AM4", 65, false, 48, 5500, PriceTier.BUDGET, UseCase.GENERAL, "Affordable 6-core entry point for budget builders.", "https://m.media-amazon.com/images/I/616VM20+AzL._AC_SL1384_.jpg"));
        list.add(cpu("cpu-12", "Intel Core i9-13900K", "Intel", "LGA1700", 125, true, 85, 30000, PriceTier.HIGH_END, UseCase.GAMING, "Top-tier performance from Intel's previous flagship.", "https://m.media-amazon.com/images/I/61bL89NXunL._AC_SL1500_.jpg"));

        // ---- GPUs ----
        list.add(gpu("gpu-1", "NVIDIA RTX 4090", "NVIDIA", 336, 450, 99, 110000, PriceTier.HIGH_END, UseCase.GAMING, "The world's most powerful graphics card for 4K gaming and AI.", "https://m.media-amazon.com/images/I/81S2H670SLL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-2", "NVIDIA RTX 4080", "NVIDIA", 336, 320, 91, 75000, PriceTier.HIGH_END, UseCase.GAMING, "Premium 4K gaming card with exceptional ray tracing capabilities.", "https://m.media-amazon.com/images/I/81S2H670SLL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-3", "NVIDIA RTX 4070 Ti", "NVIDIA", 285, 285, 84, 52000, PriceTier.HIGH_END, UseCase.GAMING, "High-performance card for high-refresh 1440p gaming.", "https://m.media-amazon.com/images/I/81S2H670SLL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-4", "NVIDIA RTX 4070", "NVIDIA", 244, 200, 76, 38000, PriceTier.HIGH_END, UseCase.GAMING, "Excellent 1440p performance with great power efficiency.", "https://m.media-amazon.com/images/I/71YyS2G1hBL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-5", "NVIDIA RTX 4060 Ti", "NVIDIA", 240, 165, 68, 28000, PriceTier.MID, UseCase.GAMING, "Fast mid-range card for 1080p and light 1440p gaming.", "https://m.media-amazon.com/images/I/71YyS2G1hBL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-6", "NVIDIA RTX 4060", "NVIDIA", 240, 115, 60, 21000, PriceTier.MID, UseCase.GAMING, "Ideal choice for competitive 1080p gaming with DLSS 3.", "https://m.media-amazon.com/images/I/71YyS2G1hBL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-7", "AMD RX 7900 XTX", "AMD", 287, 355, 93, 65000, PriceTier.HIGH_END, UseCase.GAMING, "AMD's flagship card offering massive VRAM and raw raster performance.", "https://m.media-amazon.com/images/I/71Y88SRE9xL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-8", "AMD RX 7900 XT", "AMD", 276, 315, 86, 54000, PriceTier.HIGH_END, UseCase.GAMING, "Powerful high-end card for enthusiasts favoring AMD's ecosystem.", "https://m.media-amazon.com/images/I/71Y88SRE9xL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-9", "AMD RX 7800 XT", "AMD", 267, 263, 73, 33000, PriceTier.MID, UseCase.GAMING, "The king of 1440p value in the current generation.", "https://m.media-amazon.com/images/I/71Y88SRE9xL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-10", "AMD RX 7600", "AMD", 200, 165, 55, 17500, PriceTier.MID, UseCase.GENERAL, "Modern budget-friendly card for 1080p gaming.", "https://m.media-amazon.com/images/I/71Y88SRE9xL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-11", "NVIDIA RTX 3060", "NVIDIA", 242, 170, 52, 16000, PriceTier.BUDGET, UseCase.GENERAL, "Versatile last-gen favorite with plenty of VRAM.", "https://m.media-amazon.com/images/I/71YyS2G1hBL._AC_SL1500_.jpg"));
        list.add(gpu("gpu-12", "AMD RX 6600", "AMD", 230, 132, 46, 12500, PriceTier.BUDGET, UseCase.GENERAL, "Incredible value for entry-level 1080p gaming builds.", "https://m.media-amazon.com/images/I/71Y88SRE9xL._AC_SL1500_.jpg"));

        // ---- RAM ----
        list.add(ram("ram-1", "Corsair Vengeance DDR5 32GB", "Corsair", "DDR5", 6000, 32, 72, 8500, PriceTier.HIGH_END, UseCase.GAMING, "High-speed DDR5 memory for cutting-edge platforms.", "https://m.media-amazon.com/images/I/61m6-NIn4vL._AC_SL1500_.jpg"));
        list.add(ram("ram-2", "G.Skill Trident Z5 32GB", "G.Skill", "DDR5", 6400, 32, 80, 10500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Ultra-fast low-latency RAM with premium aesthetics.", "https://m.media-amazon.com/images/I/61m6-NIn4vL._AC_SL1500_.jpg"));
        list.add(ram("ram-3", "Corsair Vengeance DDR5 16GB", "Corsair", "DDR5", 5600, 16, 60, 4800, PriceTier.MID, UseCase.GENERAL, "Solid DDR5 entry point for modern systems.", "https://m.media-amazon.com/images/I/61m6-NIn4vL._AC_SL1500_.jpg"));
        list.add(ram("ram-4", "Kingston Fury DDR4 32GB", "Kingston", "DDR4", 3600, 32, 62, 5500, PriceTier.MID, UseCase.GENERAL, "Large capacity DDR4 for productivity and heavy gaming.", "https://m.media-amazon.com/images/I/6169hK+O6dL._AC_SL1500_.jpg"));
        list.add(ram("ram-5", "Corsair Vengeance DDR4 16GB", "Corsair", "DDR4", 3200, 16, 50, 3200, PriceTier.MID, UseCase.GENERAL, "The standard for reliable, high-quality DDR4 memory.", "https://m.media-amazon.com/images/I/6169hK+O6dL._AC_SL1500_.jpg"));
        list.add(ram("ram-6", "TeamGroup DDR4 8GB", "TeamGroup", "DDR4", 2666, 8, 30, 1400, PriceTier.BUDGET, UseCase.GENERAL, "Basic memory for simple home and office computers.", "https://m.media-amazon.com/images/I/6169hK+O6dL._AC_SL1500_.jpg"));
        list.add(ram("ram-7", "G.Skill Ripjaws DDR4 16GB", "G.Skill", "DDR4", 3600, 16, 55, 3800, PriceTier.MID, UseCase.GAMING, "Optimized DDR4 performance for gaming enthusiasts.", "https://m.media-amazon.com/images/I/6169hK+O6dL._AC_SL1500_.jpg"));
        list.add(ram("ram-8", "Corsair Dominator DDR5 64GB", "Corsair", "DDR5", 5200, 64, 88, 16500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Massive capacity and stability for professional workstations.", "https://m.media-amazon.com/images/I/61m6-NIn4vL._AC_SL1500_.jpg"));

        // ---- Motherboards ----
        list.add(mb("mb-1", "ASUS ROG Maximus Z790", "ASUS", "LGA1700", "DDR5", "ATX", 82, 35000, PriceTier.HIGH_END, UseCase.GAMING, "Premium board with extreme power delivery for overclocking.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-2", "MSI MEG Z790 ACE", "MSI", "LGA1700", "DDR5", "ATX", 78, 28000, PriceTier.HIGH_END, UseCase.GAMING, "Robust enthusiast board with extensive connectivity options.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-3", "Gigabyte Z790 AORUS Elite", "Gigabyte", "LGA1700", "DDR5", "ATX", 70, 18000, PriceTier.MID, UseCase.GENERAL, "Great balance of features and performance for Z790.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-4", "ASUS Prime Z790-P", "ASUS", "LGA1700", "DDR4", "ATX", 60, 12500, PriceTier.MID, UseCase.GENERAL, "Versatile Z790 board with support for affordable DDR4 RAM.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-5", "MSI Pro B760M-A", "MSI", "LGA1700", "DDR4", "mATX", 45, 7500, PriceTier.BUDGET, UseCase.GENERAL, "Compact and affordable board for Intel budget builds.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-6", "ASUS ROG Crosshair X670E", "ASUS", "AM5", "DDR5", "ATX", 85, 38000, PriceTier.HIGH_END, UseCase.GAMING, "The pinnacle of AM5 motherboard engineering.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-7", "MSI MEG X670E ACE", "MSI", "AM5", "DDR5", "ATX", 80, 32000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Flagship AM5 board designed for maximum reliability.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-8", "Gigabyte X670 AORUS Elite", "Gigabyte", "AM5", "DDR5", "ATX", 72, 19500, PriceTier.MID, UseCase.GENERAL, "Solid foundation for high-end Ryzen 7000 systems.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-9", "MSI MAG B650M Mortar", "MSI", "AM5", "DDR5", "mATX", 58, 11500, PriceTier.MID, UseCase.GENERAL, "Highly-rated mATX board for AMD builders.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-10", "ASUS TUF B550-Plus", "ASUS", "AM4", "DDR4", "ATX", 55, 9500, PriceTier.MID, UseCase.GENERAL, "Durable last-gen board for reliable AM4 builds.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));
        list.add(mb("mb-11", "Gigabyte B550M DS3H", "Gigabyte", "AM4", "DDR4", "mATX", 40, 5200, PriceTier.BUDGET, UseCase.GENERAL, "The go-to budget motherboard for Ryzen 5000 CPUs.", "https://m.media-amazon.com/images/I/81L7-oOlvzL._AC_SL1500_.jpg"));

        // ---- Storage ----
        list.add(storage("sto-1", "Samsung 990 Pro 2TB", "Samsung", "NVMe SSD", 2000, 80, 12500, PriceTier.HIGH_END, UseCase.GAMING, "Blazing fast Gen4 storage for the ultimate PC experience.", "https://m.media-amazon.com/images/I/61f7o6-aLpL._AC_SL1500_.jpg"));
        list.add(storage("sto-2", "Samsung 980 Pro 1TB", "Samsung", "NVMe SSD", 1000, 72, 7500, PriceTier.MID, UseCase.GAMING, "Reliable high-speed NVMe favorite for gamers.", "https://m.media-amazon.com/images/I/61f7o6-aLpL._AC_SL1500_.jpg"));
        list.add(storage("sto-3", "WD Black SN850X 1TB", "Western Digital", "NVMe SSD", 1000, 74, 6500, PriceTier.MID, UseCase.GAMING, "Top-tier gaming SSD with optimized heat management.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));
        list.add(storage("sto-4", "Crucial MX500 1TB", "Crucial", "SATA SSD", 1000, 52, 4200, PriceTier.MID, UseCase.GENERAL, "Dependable SATA SSD for mass storage or older systems.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));
        list.add(storage("sto-5", "Seagate Barracuda 2TB", "Seagate", "HDD", 2000, 30, 3500, PriceTier.BUDGET, UseCase.GENERAL, "Classic hard drive for affordable bulk file storage.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));
        list.add(storage("sto-6", "Kingston A2000 500GB", "Kingston", "NVMe SSD", 500, 48, 2800, PriceTier.BUDGET, UseCase.GENERAL, "Excellent budget NVMe upgrade from traditional drives.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));
        list.add(storage("sto-7", "WD Blue 4TB", "Western Digital", "HDD", 4000, 28, 5500, PriceTier.BUDGET, UseCase.PRODUCTIVITY, "Large capacity drive for archives and media backups.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));
        list.add(storage("sto-8", "Sabrent Rocket 4TB", "Sabrent", "NVMe SSD", 4000, 85, 22000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Massive high-speed storage for professional video editors.", "https://m.media-amazon.com/images/I/71RBS9K8SLL._AC_SL1500_.jpg"));

        // ---- PSUs ----
        list.add(psu("psu-1", "Corsair HX1000i", "Corsair", 1000, 75, 13500, PriceTier.HIGH_END, UseCase.GAMING, "Platinum rated digital power supply for heavy-duty systems.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-2", "EVGA SuperNOVA 850 G6", "EVGA", 850, 65, 8800, PriceTier.HIGH_END, UseCase.GAMING, "Compact and highly efficient gold-rated power unit.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-3", "Seasonic Focus GX-750", "Seasonic", 750, 62, 7500, PriceTier.MID, UseCase.GENERAL, "Ultra-reliable PSU with a small footprint and quiet operation.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-4", "be quiet! Pure Power 11 650W", "be quiet!", 650, 55, 5800, PriceTier.MID, UseCase.GENERAL, "Quiet and stable power delivery for mid-range builds.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-5", "Corsair CX550", "Corsair", 550, 45, 3800, PriceTier.BUDGET, UseCase.GENERAL, "Reliable entry-level PSU for home and office computers.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-6", "EVGA 430 W1", "EVGA", 430, 30, 2400, PriceTier.BUDGET, UseCase.GENERAL, "Basic, cost-effective power supply for simple builds.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-7", "Corsair RM1000x", "Corsair", 1000, 78, 11500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Gold-rated silent performer for high-end builds.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));
        list.add(psu("psu-8", "Seasonic Prime TX-1300", "Seasonic", 1300, 85, 21000, PriceTier.HIGH_END, UseCase.GAMING, "The ultimate titanium-rated beast for multi-GPU setups.", "https://m.media-amazon.com/images/I/71jGzS9SSTL._AC_SL1500_.jpg"));

        // ---- Cases ----
        list.add(pccase("cas-1", "Lian Li PC-O11 Dynamic", "Lian Li", "ATX", 400, 68, 9500, PriceTier.HIGH_END, UseCase.GAMING, "Showcase chassis with modular dual-chamber design.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-2", "Fractal Design Meshify 2", "Fractal Design", "ATX", 467, 65, 8500, PriceTier.HIGH_END, UseCase.GENERAL, "Superior airflow with iconic angular mesh front panel.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-3", "NZXT H510", "NZXT", "ATX", 360, 55, 4500, PriceTier.MID, UseCase.GENERAL, "Clean, minimalistic design with easy cable management.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-4", "Phanteks Eclipse P400A", "Phanteks", "ATX", 420, 58, 5200, PriceTier.MID, UseCase.GENERAL, "Award-winning airflow case with a high-performance mesh front.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-5", "Corsair 4000D Airflow", "Corsair", "ATX", 360, 60, 6200, PriceTier.MID, UseCase.GENERAL, "A modern classic focusing on ease of build and cooling.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-6", "Cooler Master N200", "Cooler Master", "mATX", 320, 38, 3200, PriceTier.BUDGET, UseCase.GENERAL, "Compact and practical micro-ATX case for budget builders.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-7", "Thermaltake Versa H18", "Thermaltake", "mATX", 310, 32, 2100, PriceTier.BUDGET, UseCase.GENERAL, "Simple and efficient windowed case for entry-level setups.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));
        list.add(pccase("cas-8", "Lian Li Lancool 216", "Lian Li", "ATX", 435, 72, 7200, PriceTier.MID, UseCase.GAMING, "High-airflow mid-tower with large pre-installed fans.", "https://m.media-amazon.com/images/I/71p8vS9SSTL._AC_SL1500_.jpg"));

        return list;
    }

    // ---- Builders ----

    private static PCComponent cpu(String id, String name, String brand, String socket,
                                   int tdp, boolean iGPU, int score, double pricePhp,
                                   PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.CPU);
        c.setSocket(socket); c.setTdp(tdp); c.setHasIntegratedGraphics(iGPU);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent gpu(String id, String name, String brand, int length,
                                   int tdp, int score, double pricePhp,
                                   PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.GPU);
        c.setGpuLengthMm(length); c.setGpuTdp(tdp);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent ram(String id, String name, String brand, String memType,
                                   int speed, int capacityGb, int score, double pricePhp,
                                   PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.RAM);
        c.setMemoryType(memType); c.setRamSpeedMhz(speed); c.setRamCapacityGb(capacityGb);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent mb(String id, String name, String brand, String socket,
                                  String memType, String formFactor, int score, double pricePhp,
                                  PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.MOTHERBOARD);
        c.setSocket(socket); c.setSupportedMemoryType(memType); c.setFormFactor(formFactor);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent storage(String id, String name, String brand, String type,
                                       int capacityGb, int score, double pricePhp,
                                       PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.STORAGE);
        c.setStorageType(type); c.setStorageCapacityGb(capacityGb);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent psu(String id, String name, String brand, int wattage,
                                   int score, double pricePhp, PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.PSU);
        c.setWattage(wattage);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }

    private static PCComponent pccase(String id, String name, String brand, String formFactor,
                                      int maxGpuMm, int score, double pricePhp,
                                      PriceTier tier, UseCase useCase, String desc, String imageUrl) {
        PCComponent c = new PCComponent();
        c.setId(id); c.setName(name); c.setBrand(brand);
        c.setCategory(Category.CASE);
        c.setSupportedFormFactor(formFactor); c.setMaxGpuLengthMm(maxGpuMm);
        c.setFormFactor(formFactor);
        c.setPerformanceScore(score); c.setPricePhp(pricePhp);
        c.setPriceTier(tier); c.setRecommendedUseCase(useCase);
        c.setDescription(desc);
        c.setImageUrl(imageUrl);
        return c;
    }
}
