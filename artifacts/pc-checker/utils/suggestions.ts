import {
  ALL_COMPONENTS,
  PCComponent,
  UseCase,
} from "../data/components";
import { BuildComponents } from "./compatibility";

export type SuggestionType = "upgrade" | "optimization" | "balance" | "tip";

export interface Suggestion {
  type: SuggestionType;
  priority: "high" | "medium" | "low";
  title: string;
  description: string;
  componentType?: string;
  suggestedComponent?: PCComponent;
  currentComponent?: PCComponent;
  impact?: string;
  savings?: number;
}

function findBestUpgrade(
  current: PCComponent,
  useCase: UseCase | null,
  maxPriceDelta = 300
): PCComponent | null {
  const candidates = ALL_COMPONENTS.filter(
    (c) =>
      c.type === current.type &&
      c.id !== current.id &&
      c.performanceScore > current.performanceScore + 8 &&
      c.price <= current.price + maxPriceDelta &&
      (!useCase || c.categories.includes(useCase))
  );

  if (candidates.length === 0) return null;
  return candidates.sort(
    (a, b) => b.performanceScore - a.performanceScore
  )[0];
}

function findSidegrade(
  current: PCComponent,
  useCase: UseCase | null
): PCComponent | null {
  const candidates = ALL_COMPONENTS.filter(
    (c) =>
      c.type === current.type &&
      c.id !== current.id &&
      c.performanceScore >= current.performanceScore - 5 &&
      c.price < current.price - 40 &&
      (!useCase || c.categories.includes(useCase))
  );

  if (candidates.length === 0) return null;
  return candidates.sort((a, b) => b.performanceScore - a.performanceScore)[0];
}

export function generateSuggestions(
  build: BuildComponents,
  useCase: UseCase | null
): Suggestion[] {
  const suggestions: Suggestion[] = [];
  const { cpu, gpu, ram, motherboard, storage, psu } = build;

  // ─── CPU bottleneck ───────────────────────────────────────────
  if (cpu && gpu) {
    const cpuScore = cpu.performanceScore;
    const gpuScore = gpu.performanceScore;

    if (gpuScore > cpuScore + 18) {
      const upgrade = findBestUpgrade(cpu, useCase, 250);
      if (upgrade) {
        suggestions.push({
          type: "balance",
          priority: "high",
          title: "CPU is bottlenecking your GPU",
          description: `${cpu.name} (score ${cpuScore}) can't keep up with ${gpu.name} (score ${gpuScore}). Upgrading the CPU would unlock more GPU performance.`,
          componentType: "cpu",
          currentComponent: cpu,
          suggestedComponent: upgrade,
          impact: `+${upgrade.performanceScore - cpuScore} performance score`,
        });
      }
    }

    if (cpuScore > gpuScore + 18) {
      const upgrade = findBestUpgrade(gpu, useCase, 300);
      if (upgrade) {
        suggestions.push({
          type: "balance",
          priority: "high",
          title: "GPU is limiting your CPU's potential",
          description: `${gpu.name} (score ${gpuScore}) is the weak link — ${cpu.name} (score ${cpuScore}) can drive a much better GPU.`,
          componentType: "gpu",
          currentComponent: gpu,
          suggestedComponent: upgrade,
          impact: `+${upgrade.performanceScore - gpuScore} performance score`,
        });
      }
    }
  }

  // ─── RAM speed optimization ───────────────────────────────────
  if (ram && cpu) {
    const ramSpeed = ram.specs["speed"] as number;
    const maxCpuRamSpeed = cpu.specs["maxRamSpeed"] as number;
    const ramType = ram.specs["type"] as string;

    if (ramSpeed < maxCpuRamSpeed * 0.75 && ramType === "DDR5") {
      const fasterRam = ALL_COMPONENTS.filter(
        (c) =>
          c.type === "ram" &&
          (c.specs["type"] as string) === ramType &&
          (c.specs["speed"] as number) > ramSpeed &&
          (c.specs["speed"] as number) <= maxCpuRamSpeed &&
          c.price <= (ram.price ?? 0) + 60 &&
          (!useCase || c.categories.includes(useCase))
      ).sort((a, b) => (b.specs["speed"] as number) - (a.specs["speed"] as number))[0];

      if (fasterRam) {
        suggestions.push({
          type: "upgrade",
          priority: "medium",
          title: "RAM is running below CPU potential",
          description: `Your CPU supports up to ${maxCpuRamSpeed}MHz, but your RAM is only ${ramSpeed}MHz. Faster RAM can improve gaming and productivity performance by 5–15%.`,
          componentType: "ram",
          currentComponent: ram,
          suggestedComponent: fasterRam,
          impact: `+${(fasterRam.specs["speed"] as number) - ramSpeed}MHz effective bandwidth`,
        });
      }
    }

    if (ramSpeed < 3600 && ramType === "DDR4" && useCase === "gaming") {
      suggestions.push({
        type: "optimization",
        priority: "medium",
        title: "Enable XMP / EXPO in BIOS",
        description:
          "DDR4 RAM often ships at 2133MHz by default. Enabling XMP (Intel) or EXPO (AMD) in your BIOS will run it at the advertised speed with no additional cost.",
        impact: "Free speed boost — 5–10% improvement in some workloads",
      });
    }
  }

  // ─── Storage upgrade ──────────────────────────────────────────
  if (storage) {
    const storageType = storage.specs["type"] as string;
    if (storageType === "HDD" && useCase !== "general") {
      const nvmeSsd = ALL_COMPONENTS.filter(
        (c) =>
          c.type === "storage" &&
          (c.specs["type"] as string) === "NVMe" &&
          (!useCase || c.categories.includes(useCase))
      ).sort((a, b) => a.price - b.price)[0];

      if (nvmeSsd) {
        suggestions.push({
          type: "upgrade",
          priority: "high",
          title: "HDD is a major bottleneck",
          description: `HDDs are 20–40× slower than NVMe SSDs for OS and app loading. Switching to an NVMe drive dramatically improves system responsiveness.`,
          componentType: "storage",
          currentComponent: storage,
          suggestedComponent: nvmeSsd,
          impact: "Up to 40× faster load times",
        });
      }
    }

    if (storageType === "SATA SSD" && useCase === "gaming") {
      const nvmeSsd = ALL_COMPONENTS.filter(
        (c) =>
          c.type === "storage" &&
          (c.specs["type"] as string) === "NVMe" &&
          c.price <= (storage.price ?? 0) + 80
      ).sort((a, b) => a.price - b.price)[0];

      if (nvmeSsd) {
        suggestions.push({
          type: "upgrade",
          priority: "medium",
          title: "Upgrade to NVMe for faster game loading",
          description: `NVMe drives load games 3–5× faster than SATA SSDs and are required for DirectStorage in next-gen games.`,
          componentType: "storage",
          currentComponent: storage,
          suggestedComponent: nvmeSsd,
          impact: "3–5× faster game load times",
        });
      }
    }
  }

  // ─── PSU headroom ─────────────────────────────────────────────
  if (psu && cpu && gpu) {
    const wattage = psu.specs["wattage"] as number;
    const cpuTdp = cpu.specs["tdp"] as number;
    const gpuTdp = gpu.specs["tdp"] as number;
    const estimatedDraw = Math.round((cpuTdp + gpuTdp) * 1.1 + 70);
    const headroom = wattage - estimatedDraw;

    if (headroom > 250) {
      const cheaperPsu = ALL_COMPONENTS.filter(
        (c) =>
          c.type === "psu" &&
          (c.specs["wattage"] as number) >= estimatedDraw * 1.25 &&
          c.price < psu.price - 40 &&
          (!useCase || c.categories.includes(useCase))
      ).sort((a, b) => b.performanceScore - a.performanceScore)[0];

      if (cheaperPsu) {
        const saved = psu.price - cheaperPsu.price;
        suggestions.push({
          type: "optimization",
          priority: "low",
          title: "Oversized PSU — save money",
          description: `Your ${wattage}W PSU has ${headroom}W of unused headroom for your current build. A lower-wattage unit would be more cost-effective.`,
          componentType: "psu",
          currentComponent: psu,
          suggestedComponent: cheaperPsu,
          savings: saved,
          impact: `Save $${saved} with no performance loss`,
        });
      }
    }
  }

  // ─── No GPU warning for gaming ────────────────────────────────
  if (!gpu && useCase === "gaming") {
    const recGpu = ALL_COMPONENTS.filter(
      (c) => c.type === "gpu" && c.categories.includes("gaming")
    ).sort((a, b) => a.price - b.price)[2];

    if (recGpu) {
      suggestions.push({
        type: "tip",
        priority: "high",
        title: "No GPU selected for gaming",
        description:
          "Gaming builds need a dedicated GPU. Even integrated graphics can't match a discrete card for modern titles.",
        componentType: "gpu",
        suggestedComponent: recGpu,
        impact: "Required for any meaningful gaming performance",
      });
    }
  }

  // ─── Large RAM capacity for productivity ─────────────────────
  if (ram && useCase === "productivity") {
    const capacity = ram.specs["capacity"] as number;
    if (capacity < 32) {
      const moreRam = ALL_COMPONENTS.filter(
        (c) =>
          c.type === "ram" &&
          (c.specs["capacity"] as number) >= 32 &&
          (c.specs["type"] as string) === (ram.specs["type"] as string) &&
          c.categories.includes("productivity")
      ).sort((a, b) => a.price - b.price)[0];

      if (moreRam) {
        suggestions.push({
          type: "upgrade",
          priority: "medium",
          title: "More RAM recommended for productivity",
          description: `${capacity}GB can be limiting for video editing, 3D rendering, and VMs. 32GB+ ensures smooth multitasking.`,
          componentType: "ram",
          currentComponent: ram,
          suggestedComponent: moreRam,
          impact: "Prevents swapping and keeps heavy apps responsive",
        });
      }
    }
  }

  // ─── Resizable BAR tip ────────────────────────────────────────
  if (gpu && motherboard && useCase === "gaming") {
    suggestions.push({
      type: "tip",
      priority: "low",
      title: "Enable Resizable BAR (Smart Access Memory)",
      description:
        "If your GPU and motherboard both support it, enabling Resizable BAR in BIOS can improve gaming performance by 3–12% at no cost.",
      impact: "Free 3–12% GPU performance boost in supported games",
    });
  }

  // ─── Value sidegrades ─────────────────────────────────────────
  if (gpu && useCase !== "gaming") {
    const cheaper = findSidegrade(gpu, useCase);
    if (cheaper) {
      const saved = gpu.price - cheaper.price;
      suggestions.push({
        type: "optimization",
        priority: "low",
        title: "Similar GPU at lower cost",
        description: `For ${useCase === "general" ? "everyday use" : "productivity workloads"}, ${cheaper.name} delivers similar performance for $${saved} less.`,
        componentType: "gpu",
        currentComponent: gpu,
        suggestedComponent: cheaper,
        savings: saved,
        impact: `Save $${saved} — ${cheaper.performanceScore} vs ${gpu.performanceScore} score`,
      });
    }
  }

  return suggestions.sort((a, b) => {
    const p = { high: 0, medium: 1, low: 2 };
    return p[a.priority] - p[b.priority];
  });
}
