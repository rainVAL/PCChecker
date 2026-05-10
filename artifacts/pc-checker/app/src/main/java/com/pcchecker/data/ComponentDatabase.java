package com.pcchecker.data;

import com.pcchecker.model.PCComponent;
import com.pcchecker.model.PCComponent.Category;
import com.pcchecker.model.PCComponent.UseCase;
import com.pcchecker.model.PCComponent.PriceTier;

import java.util.ArrayList;
import java.util.List;

public class ComponentDatabase {

    private static List<PCComponent> components;
    private static final String ASSET_PATH = "file:///android_asset/images/components/";

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
        list.add(cpu("cpu-1", "Intel Core i9-14900K", "Intel", "LGA1700", 125, true, 89, 32000, PriceTier.HIGH_END, UseCase.GAMING, "Intel's flagship 14th Gen processor with 24 cores (8P+16E) and a massive 6.0GHz max turbo frequency. Engineered for extreme gaming performance, high-end content creation, and professional multi-threaded workloads. Requires high-end Z790 motherboards and premium 360mm+ AIO cooling solutions for optimal thermal management.", ASSET_PATH + "i9.jpg"));
        list.add(cpu("cpu-2", "Intel Core i7-14700K", "Intel", "LGA1700", 125, true, 78, 24000, PriceTier.HIGH_END, UseCase.GAMING, "A powerhouse high-end CPU featuring 20 cores (8P+12E). It offers an exceptional balance between top-tier gaming frame rates and professional-grade multi-tasking capabilities. Ideal for streamers and editors who need consistent performance without the extreme price of the i9. Pairs best with Z790 or high-end B760 motherboards.", ASSET_PATH + "i7.jpg"));
        list.add(cpu("cpu-3", "Intel Core i5-14600K", "Intel", "LGA1700", 125, true, 68, 18000, PriceTier.MID, UseCase.GENERAL, "Widely considered the 'sweet spot' for modern gaming. This 14-core processor delivers flagship-level single-core performance, ensuring smooth 1440p and 4K gaming. It features unlocked multipliers for overclocking enthusiasts and provides enough multi-threaded power for heavy creative work and background applications.", ASSET_PATH + "i5.jpg"));
        list.add(cpu("cpu-4", "Intel Core i5-13400", "Intel", "LGA1700", 65, true, 58, 13500, PriceTier.MID, UseCase.GENERAL, "An incredibly efficient 10-core mid-range CPU that excels in daily computing and mainstream 1080p gaming. With its low 65W TDP, it runs cool even with standard air coolers. It’s a perfect choice for reliable, all-purpose home builds and efficient small-form-factor gaming rigs.", ASSET_PATH + "i5.jpg"));
        list.add(cpu("cpu-5", "Intel Core i3-13100", "Intel", "LGA1700", 60, true, 42, 7500, PriceTier.BUDGET, UseCase.GENERAL, "The go-to choice for budget-conscious builders and office workstations. This quad-core processor handles web browsing, high-definition media consumption, and light productivity tasks with ease. While entry-level, its modern architecture ensures snappy system responsiveness for all standard computing needs.", ASSET_PATH + "i3.jpg"));
        list.add(cpu("cpu-6", "AMD Ryzen 9 7950X", "AMD", "AM5", 170, false, 95, 38000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "AMD's ultimate productivity monster featuring 16 high-performance 'Zen 4' cores. It is built on the advanced AM5 platform and designed specifically for heavy-duty video rendering, 3D modeling, and scientific simulations. It supports PCIe 5.0 and requires DDR5 memory, offering unmatched multi-core longevity.", ASSET_PATH + "ryzen9.jpg"));
        list.add(cpu("cpu-7", "AMD Ryzen 9 7900X", "AMD", "AM5", 170, false, 88, 28000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "A high-performance 12-core processor designed for creators and serious gamers. It provides massive processing power for multi-threaded applications while maintaining high clock speeds for competitive gaming. A great choice for those on the AM5 platform who need professional power for video editing and high-resolution rendering.", ASSET_PATH + "ryzen9.jpg"));
        list.add(cpu("cpu-8", "AMD Ryzen 7 7700X", "AMD", "AM5", 105, false, 76, 19500, PriceTier.HIGH_END, UseCase.GAMING, "An elite 8-core processor that is highly optimized for modern gaming engines. It offers high IPC (Instructions Per Clock) which translates into exceptional smoothness in CPU-intensive titles. Its AM5 socket ensures a clear upgrade path for future AMD generations, making it a smart long-term investment for high-end builds.", ASSET_PATH + "ryzen7.jpg"));
        list.add(cpu("cpu-9", "AMD Ryzen 5 7600X", "AMD", "AM5", 105, false, 65, 14500, PriceTier.MID, UseCase.GAMING, "The entry point to the high-performance AM5 platform. This 6-core processor uses the same architecture as its bigger siblings to deliver impressive single-core speeds. It's an ideal choice for a modern, DDR5-based gaming build that focuses on maximum value and future-proofing.", ASSET_PATH + "ryzen5.jpg"));
        list.add(cpu("cpu-10", "AMD Ryzen 5 5600X", "AMD", "AM4", 65, false, 60, 9500, PriceTier.MID, UseCase.GAMING, "A legendary gaming CPU that continues to offer incredible value on the AM4 platform. Its 6 cores provide the perfect balance for 1080p gaming and general multi-tasking. It is highly efficient and compatible with a wide range of affordable B450 and B550 motherboards, making it a favorite for budget-to-mid builds.", ASSET_PATH + "5600x.jpg"));
        list.add(cpu("cpu-11", "AMD Ryzen 5 5500", "AMD", "AM4", 65, false, 48, 5500, PriceTier.BUDGET, UseCase.GENERAL, "The ultimate budget 6-core processor for value builders. It brings the power of the Zen 3 architecture down to an incredibly low price point. Perfect for building a highly capable home PC or a low-cost gaming system when paired with a dedicated graphics card. Extremely easy to cool and highly reliable.", ASSET_PATH + "5500.jpg"));
        list.add(cpu("cpu-12", "Intel Core i9-13900K", "Intel", "LGA1700", 125, true, 85, 30000, PriceTier.HIGH_END, UseCase.GAMING, "Intel's previous flagship, still outperforming almost everything on the market. With its 24 cores, it handles the most demanding 4K gaming and professional 8K video editing tasks without breaking a sweat. A proven high-end platform for those who want top-tier power on the established LGA1700 socket.", ASSET_PATH + "i9.jpg"));

        // ---- GPUs ----
        list.add(gpu("gpu-1", "NVIDIA RTX 4090", "NVIDIA", 336, 450, 99, 110000, PriceTier.HIGH_END, UseCase.GAMING, "The undisputed king of graphics cards. With 24GB of G6X VRAM, it's designed for flawless 4K ultra-settings gaming, real-time ray tracing, and intensive AI workloads. Features DLSS 3 Frame Generation technology for incredible smoothness in the most demanding titles. Requires a massive 850W+ power supply and a large case for its triple-fan design.", ASSET_PATH + "4090.jpg"));
        list.add(gpu("gpu-2", "NVIDIA RTX 4080", "NVIDIA", 336, 320, 91, 75000, PriceTier.HIGH_END, UseCase.GAMING, "A premium 4K gaming powerhouse with 16GB of VRAM. It delivers exceptional performance in ray-traced games and professional creative applications like Blender or Premiere Pro. Much more efficient than previous generations, it offers the ultimate high-refresh 1440p or steady 4K experience for serious enthusiasts.", ASSET_PATH + "rtx4080.jpg"));
        list.add(gpu("gpu-3", "NVIDIA RTX 4070 Ti", "NVIDIA", 285, 285, 84, 52000, PriceTier.HIGH_END, UseCase.GAMING, "The perfect card for competitive 1440p gaming at ultra settings. It comfortably outpaces last-generation flagships and utilizes DLSS 3 to push frame rates higher than ever before. Its compact power makes it ideal for high-end mid-tower builds that prioritize smooth, high-resolution visuals and reliable performance.", ASSET_PATH + "4070ti.jpg"));
        list.add(gpu("gpu-4", "NVIDIA RTX 4070", "NVIDIA", 244, 200, 76, 38000, PriceTier.HIGH_END, UseCase.GAMING, "An extremely efficient and capable card for high-end 1440p gaming. With 12GB of VRAM, it's prepared for the latest AAA titles. It consumes remarkably little power for its performance level, making it easy to cool and a great fit for a wide variety of power supplies and case sizes.", ASSET_PATH + "4070.jpg"));
        list.add(gpu("gpu-5", "NVIDIA RTX 4060 Ti", "NVIDIA", 240, 165, 68, 28000, PriceTier.MID, UseCase.GAMING, "A versatile mid-range card that dominates 1080p and handles 1440p gaming with ease. It's the standard choice for modern gamers looking for the benefits of the Ada Lovelace architecture, including full support for ray tracing and AI-powered upscaling, all within a reasonable budget and power envelope.", ASSET_PATH + "4060ti.jpg"));
        list.add(gpu("gpu-6", "NVIDIA RTX 4060", "NVIDIA", 240, 115, 60, 21000, PriceTier.MID, UseCase.GAMING, "The ideal choice for competitive 1080p gamers. It provides high frame rates in eSports titles and solid performance in the latest AAA games. With extremely low power consumption (only 115W), it's the perfect upgrade for older systems or a centerpiece for a high-value new build.", ASSET_PATH + "4060.jpg"));
        list.add(gpu("gpu-7", "AMD RX 7900 XTX", "AMD", 287, 355, 93, 65000, PriceTier.HIGH_END, UseCase.GAMING, "AMD's flagship RDNA 3 graphics card. Boasting a massive 24GB of VRAM, it is built to handle raw 4K gaming performance at its highest level. It features a unique chiplet design that provides exceptional rasterization speed, making it the best alternative to NVIDIA's top-tier offerings for those who value VRAM and pure performance.", ASSET_PATH + "7900xtx.jpg"));
        list.add(gpu("gpu-8", "AMD RX 7900 XT", "AMD", 276, 315, 86, 54000, PriceTier.HIGH_END, UseCase.GAMING, "A high-end card that provides excellent 4K gaming and superb high-refresh 1440p performance. With 20GB of VRAM, it offers great longevity for future games. It features AMD's latest technologies like FSR 3 and dedicated AI accelerators, delivering a premium gaming experience for enthusiasts.", ASSET_PATH + "7900xt.jpg"));
        list.add(gpu("gpu-9", "AMD RX 7800 XT", "AMD", 267, 263, 73, 33000, PriceTier.MID, UseCase.GAMING, "Commonly recognized as the king of 1440p value. It offers high frame rates and a generous 16GB of VRAM, ensuring compatibility with demanding textures for years to come. It’s a robust, well-balanced card that fits perfectly into any mid-to-high end gaming rig.", ASSET_PATH + "7800xt.jpg"));
        list.add(gpu("gpu-10", "AMD RX 7600", "AMD", 200, 165, 55, 17500, PriceTier.MID, UseCase.GENERAL, "A modern, affordable 1080p gaming solution. It delivers smooth performance in all current titles and is highly efficient. A great starting point for new gamers or home users who want a snappy system that can also handle moderate creative work and casual gaming.", ASSET_PATH + "7600.jpg"));
        list.add(gpu("gpu-11", "NVIDIA RTX 3060", "NVIDIA", 242, 170, 52, 16000, PriceTier.BUDGET, UseCase.GENERAL, "A community favorite that remains incredibly relevant due to its large 12GB VRAM buffer. It's a versatile last-gen card that is perfect for entry-level 1080p gaming, moderate 3D work, and video editing. Highly reliable and compatible with most budget platforms.", ASSET_PATH + "3060.jpg"));
        list.add(gpu("gpu-12", "AMD RX 6600", "AMD", 230, 132, 46, 12500, PriceTier.BUDGET, UseCase.GENERAL, "The undisputed value champion for entry-level 1080p builds. It offers very low power draw while providing enough power to play modern games at high settings. Perfect for builders on a strict budget who still want a genuine gaming experience.", ASSET_PATH + "6600.jpg"));

        // ---- RAM ----
        list.add(ram("ram-1", "Corsair Vengeance DDR5 32GB", "Corsair", "DDR5", 6000, 32, 72, 8500, PriceTier.HIGH_END, UseCase.GAMING, "High-performance DDR5 memory clocked at 6000MHz. Designed for the latest Intel and AMD platforms, it provides a massive bandwidth boost that eliminates bottlenecks in modern gaming and high-end productivity. Features a low-profile aluminum heatspreader for excellent compatibility and stability.", ASSET_PATH + "corsairvengeance.jpg"));
        list.add(ram("ram-2", "G.Skill Trident Z5 32GB", "G.Skill", "DDR5", 6400, 32, 80, 10500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Ultra-fast DDR5 memory featuring a sleek, high-end design. With its 6400MHz speed and low-latency timings, it is specifically tuned for enthusiast builders who demand the absolute best system responsiveness. Perfect for high-resolution video editing and extreme gaming rigs.", ASSET_PATH + "gskiltridentz5.jpg"));
        list.add(ram("ram-3", "Corsair Vengeance DDR5 16GB", "Corsair", "DDR5", 5600, 16, 60, 4800, PriceTier.MID, UseCase.GENERAL, "A modern 16GB DDR5 kit that serves as an excellent foundation for any mainstream build. It offers significantly more speed than older DDR4 standards, ensuring your system stays fast and responsive during multitasking and everyday gaming. Reliable, durable, and highly compatible.", ASSET_PATH + "vengeance16gb.jpg"));
        list.add(ram("ram-4", "Kingston Fury DDR4 32GB", "Kingston", "DDR4", 3600, 32, 62, 5500, PriceTier.MID, UseCase.GENERAL, "High-capacity DDR4 memory for those who need to run multiple intensive applications simultaneously. The 32GB capacity is ideal for heavy Chrome users, streamers, and creative professionals on older platforms. Features a stylish heatspreader and automatic overclocking support.", ASSET_PATH + "kinstonfury.jpg"));
        list.add(ram("ram-5", "Corsair Vengeance DDR4 16GB", "Corsair", "DDR4", 3200, 16, 50, 3200, PriceTier.MID, UseCase.GENERAL, "The worldwide industry standard for reliable DDR4 memory. This 16GB kit at 3200MHz provides exactly what is needed for smooth gaming and snappy Windows performance. It is well-known for its extreme reliability and compatibility with almost every motherboard in existence.", ASSET_PATH + "corsairvengeance.jpg"));
        list.add(ram("ram-6", "TeamGroup DDR4 8GB", "TeamGroup", "DDR4", 2666, 8, 30, 1400, PriceTier.BUDGET, UseCase.GENERAL, "Essential, no-frills memory for basic home and office computers. This 8GB stick ensures smooth operation for web browsing and office documents. It is extremely cost-effective and provides a reliable way to get a basic PC up and running.", ASSET_PATH + "teamgroup.jpg"));
        list.add(ram("ram-7", "G.Skill Ripjaws DDR4 16GB", "G.Skill", "DDR4", 3600, 16, 55, 3800, PriceTier.MID, UseCase.GAMING, "Performance-oriented DDR4 RAM specifically designed for gaming. The 3600MHz frequency offers a sweet spot for performance on the Ryzen 5000 series, providing lower latencies and higher frame rates in CPU-bound games. A favorite among gamers for its aggressive looks and speed.", ASSET_PATH + "gskillripjaws.jpg"));
        list.add(ram("ram-8", "Corsair Dominator DDR5 64GB", "Corsair", "DDR5", 5200, 64, 88, 16500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "The pinnacle of high-capacity memory. This 64GB kit is built with premium components for maximum stability during professional workstations tasks like 4K rendering and large-scale data processing. Includes custom LED lighting and industrial-grade thermal management.", ASSET_PATH + "corsairdominator.jpg"));

        // ---- Motherboards ----
        list.add(mb("mb-1", "ASUS ROG Maximus Z790", "ASUS", "LGA1700", "DDR5", "ATX", 82, 35000, PriceTier.HIGH_END, UseCase.GAMING, "An elite Z790 motherboard designed for extreme overclocking and enthusiast performance. It features massive VRM cooling, support for PCIe 5.0 graphics cards and SSDs, and integrated Wi-Fi 7. The ultimate foundation for an i9-14900K build with no compromises on connectivity or speed.", ASSET_PATH + "maximusz790.jpg"));
        list.add(mb("mb-2", "MSI MEG Z790 ACE", "MSI", "LGA1700", "DDR5", "ATX", 78, 28000, PriceTier.HIGH_END, UseCase.GAMING, "A robust enthusiast-grade board with an industrial aesthetic. It offers five M.2 slots for massive storage arrays, high-speed dual networking, and a premium audio solution. Tuned for stability under heavy loads, it is ideal for 24/7 high-performance computing and professional creative work.", ASSET_PATH + "z790ace.jpg"));
        list.add(mb("mb-3", "Gigabyte Z790 AORUS Elite", "Gigabyte", "LGA1700", "DDR5", "ATX", 70, 18000, PriceTier.MID, UseCase.GENERAL, "A well-balanced Z790 board that provides all the essential high-end features at a more accessible price. It supports fast DDR5 memory, features a powerful power delivery system for i7 processors, and includes plenty of USB ports and high-speed expansion slots for modern peripherals.", ASSET_PATH + "z790elite.jpg"));
        list.add(mb("mb-4", "ASUS Prime Z790-P", "ASUS", "LGA1700", "DDR4", "ATX", 60, 12500, PriceTier.MID, UseCase.GENERAL, "A versatile motherboard that allows users to leverage the latest 13th and 14th Gen Intel CPUs while using more affordable DDR4 memory. It offers a clean white aesthetic, robust connectivity, and a straightforward BIOS that is perfect for mainstream gaming and high-performance home office builds.", ASSET_PATH + "z790-p.jpg"));
        list.add(mb("mb-5", "MSI Pro B760M-A", "MSI", "LGA1700", "DDR4", "mATX", 45, 7500, PriceTier.BUDGET, UseCase.GENERAL, "The standard for professional-grade budget Intel builds. This micro-ATX board features strong VRMs for its class, ensuring stability for i3 and i5 processors. It's a reliable, no-nonsense foundation for a compact system that still requires high-speed connectivity and expansion options.", ASSET_PATH + "b760m-a.jpg"));
        list.add(mb("mb-6", "ASUS ROG Crosshair X670E", "ASUS", "AM5", "DDR5", "ATX", 85, 38000, PriceTier.HIGH_END, UseCase.GAMING, "The flagship motherboard for the AMD AM5 platform. Designed for the Ryzen 7000 and 9000 series, it features full PCIe 5.0 support for both storage and graphics. With its extreme power delivery and premium build quality, it's the only choice for the highest-end AMD gaming and creative systems.", ASSET_PATH + "x670e.jpg"));
        list.add(mb("mb-7", "MSI MEG X670E ACE", "MSI", "AM5", "DDR5", "ATX", 80, 32000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "A top-tier AM5 board specifically designed for content creators and workstation users. It offers exceptional stability, massive storage expansion with M.2 Frozr cooling, and support for the highest speed DDR5 kits. Built to handle 16-core Ryzen processors under sustained 100% load.", ASSET_PATH + "x670eace.jpg"));
        list.add(mb("mb-8", "Gigabyte X670 AORUS Elite", "Gigabyte", "AM5", "DDR5", "ATX", 72, 19500, PriceTier.MID, UseCase.GENERAL, "A high-value X670 motherboard that brings the benefits of the new AMD platform to a wider audience. It offers excellent power delivery, comprehensive cooling for SSDs, and built-in Wi-Fi 6E. A great middle-ground for enthusiast builds that don't need extreme overclocking features.", ASSET_PATH + "x670.jpg"));
        list.add(mb("mb-9", "MSI MAG B650M Mortar", "MSI", "AM5", "DDR5", "mATX", 58, 11500, PriceTier.MID, UseCase.GENERAL, "Highly-rated micro-ATX board for AMD builders. It offers a solid power phase design and premium build materials usually found on larger boards. Perfect for high-performance compact systems that need to fit into smaller cases while still supporting high-speed memory and storage.", ASSET_PATH + "mortar_b650.jpg"));
        list.add(mb("mb-10", "ASUS TUF B550-Plus", "ASUS", "AM4", "DDR4", "ATX", 55, 9500, PriceTier.MID, UseCase.GENERAL, "A durable, battle-tested board for the widely popular AM4 platform. It features military-grade components and a focus on essential gaming features. A reliable choice for Ryzen 5000 series builds that need to be stable, cool, and compatible with established hardware.", ASSET_PATH + "b550-plus.jpg"));
        list.add(mb("mb-11", "Gigabyte B550M DS3H", "Gigabyte", "AM4", "DDR4", "mATX", 40, 5200, PriceTier.BUDGET, UseCase.GENERAL, "The ultimate budget champion for AMD builders. This compact board provides everything needed for a solid Ryzen 5000 system at an unbeatable price point. It features dual M.2 slots and plenty of fan headers, making it easy to build a high-value system without sacrificing quality.", ASSET_PATH + "b550mds3h.jpg"));

        // ---- Storage ----
        list.add(storage("sto-1", "Samsung 990 Pro 2TB", "Samsung", "NVMe SSD", 2000, 80, 12500, PriceTier.HIGH_END, UseCase.GAMING, "The world's fastest consumer Gen4 SSD. With read speeds up to 7,450 MB/s, it virtually eliminates loading times in games and provides instant responsiveness in video editing and heavy multitasking. Features a premium controller and Samsung's legendary reliability.", ASSET_PATH + "990pro.jpg"));
        list.add(storage("sto-2", "Samsung 980 Pro 1TB", "Samsung", "NVMe SSD", 1000, 72, 7500, PriceTier.MID, UseCase.GAMING, "A industry-standard high-speed NVMe favorite. It offers a perfect balance of top-tier PCIe 4.0 performance and value. A favorite for both gamers and professional users who need consistent, reliable speed for their operating system and primary applications.", ASSET_PATH + "980pro.jpg"));
        list.add(storage("sto-3", "WD Black SN850X 1TB", "Western Digital", "NVMe SSD", 1000, 74, 6500, PriceTier.MID, UseCase.GAMING, "Optimized for elite-level gaming. This drive features a dedicated Game Mode that reduces latency and ensures consistent performance during long sessions. It's one of the most reliable and fastest drives available for high-end gaming consoles and PCs.", ASSET_PATH + "sn850x.jpg"));
        list.add(storage("sto-4", "Crucial MX500 1TB", "Crucial", "SATA SSD", 1000, 52, 4200, PriceTier.MID, UseCase.GENERAL, "A dependable SATA SSD that serves as excellent secondary storage for games and large project files. While slower than NVMe, it provides a massive speed boost over traditional hard drives and is highly reliable for long-term data storage in any system.", ASSET_PATH + "mx500.jpg"));
        list.add(storage("sto-5", "Seagate Barracuda 2TB", "Seagate", "HDD", 2000, 30, 3500, PriceTier.BUDGET, UseCase.GENERAL, "Classic mechanical storage for those who need a large amount of space for a small price. Ideal for bulk storage of movies, photos, and backups. While not recommended for modern games or OS installs, it's a reliable workhorse for massive file libraries.", ASSET_PATH + "barracuda.jpg"));
        list.add(storage("sto-6", "Kingston A2000 500GB", "Kingston", "NVMe SSD", 500, 48, 2800, PriceTier.BUDGET, UseCase.GENERAL, "An amazing entry-point for NVMe storage. It provides speeds up to 3x faster than SATA SSDs at a similar price. Perfect for budget builds that still want quick boot times and snappy application performance on a modern platform.", ASSET_PATH + "a2000.jpg"));
        list.add(storage("sto-7", "WD Blue 4TB", "Western Digital", "HDD", 4000, 28, 5500, PriceTier.BUDGET, UseCase.PRODUCTIVITY, "A high-capacity mechanical drive designed for longevity and consistent bulk storage. With 4TB of space, it’s a perfect companion for a fast SSD, providing a safe and reliable place to store archives, creative project assets, and media libraries.", ASSET_PATH + "wdblue.jpg"));
        list.add(storage("sto-8", "Sabrent Rocket 4TB", "Sabrent", "NVMe SSD", 4000, 85, 22000, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "Massive high-speed storage for professionals. This 4TB Gen4 drive combines huge capacity with extreme performance, making it an essential tool for 4K and 8K video editors, database managers, and anyone handling massive datasets every day.", ASSET_PATH + "sabrent.jpg"));

        // ---- PSUs ----
        list.add(psu("psu-1", "Corsair HX1000i", "Corsair", 1000, 75, 13500, PriceTier.HIGH_END, UseCase.GAMING, "A Platinum-rated digital power supply built for extreme systems. With 1000W of clean power and fully modular cables, it easily handles dual GPUs or high-end i9/RTX 4090 builds. Features a custom quiet-fan mode and digital monitoring through software.", ASSET_PATH + "hx1000i.jpg"));
        list.add(psu("psu-2", "EVGA SuperNOVA 850 G6", "EVGA", 850, 65, 8800, PriceTier.HIGH_END, UseCase.GAMING, "A premium 850W Gold-rated unit featuring compact size and exceptional efficiency. Its high-quality Japanese capacitors ensure maximum reliability for high-end gaming rigs. Fully modular cables make cable management in mid-tower cases extremely simple.", ASSET_PATH + "850g6.jpg"));
        list.add(psu("psu-3", "Seasonic Focus GX-750", "Seasonic", 750, 62, 7500, PriceTier.MID, UseCase.GENERAL, "Widely regarded as the benchmark for reliable mid-range power. This 750W Gold unit features Seasonic's industry-leading voltage regulation and whisper-quiet operation. Its 10-year warranty reflects the extreme confidence in its build quality and longevity.", ASSET_PATH + "gx-750.jpg"));
        list.add(psu("psu-4", "be quiet! Pure Power 11 650W", "be quiet!", 650, 55, 5800, PriceTier.MID, UseCase.GENERAL, "Specifically designed for silence and stability. This Gold-rated unit is perfect for mid-range builds where noise levels are a primary concern. It delivers extremely steady power and uses high-end fans to ensure your system remains quiet even under load.", ASSET_PATH + "purepower.jpg"));
        list.add(psu("psu-5", "Corsair CX550", "Corsair", 550, 45, 3800, PriceTier.BUDGET, UseCase.GENERAL, "A reliable entry-level PSU that provides more than enough power for home, office, and budget gaming builds. It features essential protection technologies and a durable fan, ensuring your system stays safe and powered up for years to come.", ASSET_PATH + "cx550.jpg"));
        list.add(psu("psu-6", "EVGA 430 W1", "EVGA", 430, 30, 2400, PriceTier.BUDGET, UseCase.GENERAL, "A cost-effective solution for basic systems. This 430W unit is designed to provide stable power to office PCs and entry-level computers with low-power components. It's a simple, straightforward choice for builders who prioritize budget above all else.", ASSET_PATH + "evga.jpg"));
        list.add(psu("psu-7", "Corsair RM1000x", "Corsair", 1000, 78, 11500, PriceTier.HIGH_END, UseCase.PRODUCTIVITY, "A legendary Gold-rated power supply known for its 'Zero RPM' fan mode, making it completely silent during low and medium loads. Its 1000W output and high-quality internal architecture make it a favorite for professional workstations and flagship gaming builds.", ASSET_PATH + "rm1000x.jpg"));
        list.add(psu("psu-8", "Seasonic Prime TX-1300", "Seasonic", 1300, 85, 21000, PriceTier.HIGH_END, UseCase.GAMING, "The absolute pinnacle of power supply engineering. Titanium-rated for nearly 100% efficiency, this 1300W beast is designed for the most power-hungry configurations on earth. It provides the cleanest possible power for extreme overclockers and multi-GPU systems.", ASSET_PATH + "tx1300.jpg"));

        // ---- Cases ----
        list.add(pccase("cas-1", "Lian Li PC-O11 Dynamic", "Lian Li", "ATX", 400, 68, 9500, PriceTier.HIGH_END, UseCase.GAMING, "The iconic showpiece chassis. Its unique dual-chamber design and three-sided tempered glass panels make it the ultimate choice for water-cooled or highly aesthetic builds. Offers massive radiator support and enough space for even the largest RTX 4090 cards.", ASSET_PATH + "pc-011dynamic.jpg"));
        list.add(pccase("cas-2", "Fractal Design Meshify 2", "Fractal Design", "ATX", 467, 65, 8500, PriceTier.HIGH_END, UseCase.GENERAL, "A masterclass in airflow and modularity. Its distinct angular mesh front panel provides superior cooling for high-end components, while its highly customizable interior can be adjusted for either extreme storage or maximum airflow. Built with premium materials for a silent, sleek look.", ASSET_PATH + "meshify2.jpg"));
        list.add(pccase("cas-3", "NZXT H510", "NZXT", "ATX", 360, 55, 4500, PriceTier.MID, UseCase.GENERAL, "The standard for minimalistic design. Its clean lines, tempered glass side panel, and signature cable management bar make it incredibly easy to build a professional-looking PC. A popular choice for gamers who want a sleek, modern aesthetic without any visual clutter.", ASSET_PATH + "h510.jpg"));
        list.add(pccase("cas-4", "Phanteks Eclipse P400A", "Phanteks", "ATX", 420, 58, 5200, PriceTier.MID, UseCase.GENERAL, "An award-winning high-airflow case featuring an ultra-fine mesh front panel. It provides exceptional cooling out of the box and includes integrated RGB controllers. Its spacious interior supports extra-long graphics cards and large air coolers, perfect for performance gaming.", ASSET_PATH + "eclipsep400a.jpg"));
        list.add(pccase("cas-5", "Corsair 4000D Airflow", "Corsair", "ATX", 360, 60, 6200, PriceTier.MID, UseCase.GENERAL, "A modern classic that prioritizes ease of use and maximum cooling. Its high-airflow front panel and innovative RapidRoute cable management system make it one of the best cases for both beginners and veteran builders. Durable, functional, and exceptionally cool.", ASSET_PATH + "4000dairflow.jpg"));
        list.add(pccase("cas-6", "Cooler Master N200", "Cooler Master", "mATX", 320, 38, 3200, PriceTier.BUDGET, UseCase.GENERAL, "A highly practical and compact micro-ATX case. It provides an efficient layout for budget builders who want a small footprint without sacrificing compatibility for standard hardware. Features excellent ventilation and a rugged, professional design.", ASSET_PATH + "n200.jpg"));
        list.add(pccase("cas-7", "Thermaltake Versa H18", "Thermaltake", "mATX", 310, 32, 2100, PriceTier.BUDGET, UseCase.GENERAL, "The ultimate value case for entry-level builds. It features a transparent window to show off your hardware and a clean, ventilated front panel for efficient cooling. Its compact size is perfect for home office desks and simple gaming setups.", ASSET_PATH + "h18.jpg"));
        list.add(pccase("cas-8", "Lian Li Lancool 216", "Lian Li", "ATX", 435, 72, 7200, PriceTier.MID, UseCase.GAMING, "A powerhouse mid-tower specifically optimized for air cooling. It comes with two massive 160mm fans pre-installed and features a unique 'Continuous Mesh' design for unrestricted airflow. The best choice for high-performance builds using traditional air coolers.", ASSET_PATH + "lancool216.jpg"));

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