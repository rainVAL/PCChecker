import { COMPONENT_LABELS, ComponentType } from "../data/components";
import { BuildComponents } from "./compatibility";

export interface ComponentScore {
  type: ComponentType;
  label: string;
  score: number;
  isBottleneck: boolean;
  bottleneckImpact: number;
  tier: "low" | "mid" | "high" | "ultra";
  note: string;
}

export interface BottleneckAnalysis {
  components: ComponentScore[];
  primaryBottleneck: ComponentType | null;
  secondaryBottleneck: ComponentType | null;
  overallTier: "entry" | "mid" | "high" | "enthusiast";
  summary: string;
}

function getTier(score: number): "low" | "mid" | "high" | "ultra" {
  if (score >= 88) return "ultra";
  if (score >= 70) return "high";
  if (score >= 50) return "mid";
  return "low";
}

function getTierLabel(tier: "low" | "mid" | "high" | "ultra"): string {
  return { low: "Entry", mid: "Mid-range", high: "High-end", ultra: "Enthusiast" }[tier];
}

function getComponentNote(type: ComponentType, score: number): string {
  const tier = getTier(score);
  const map: Record<ComponentType, Record<string, string>> = {
    cpu: {
      ultra: "Exceptional — no CPU bottleneck",
      high: "Strong — handles any workload",
      mid: "Capable for most use cases",
      low: "Limited — may cap GPU & RAM performance",
    },
    gpu: {
      ultra: "Top-tier — maxes out any display",
      high: "Excellent — 1440p/4K capable",
      mid: "Solid — good 1080p/1440p performance",
      low: "Entry-level — 1080p budget gaming",
    },
    ram: {
      ultra: "Blazing fast — no memory bottleneck",
      high: "High-speed — ideal for gaming & creative",
      mid: "Adequate — sufficient for daily use",
      low: "Slow — may throttle CPU & GPU throughput",
    },
    motherboard: {
      ultra: "Feature-rich — unlocks full CPU potential",
      high: "Well-equipped — great overclocking headroom",
      mid: "Solid foundation — all essentials covered",
      low: "Budget — limits overclocking and expansion",
    },
    storage: {
      ultra: "NVMe Gen4 — fastest loads available",
      high: "Fast NVMe — excellent responsiveness",
      mid: "Good SSD — decent load times",
      low: "HDD — significant bottleneck for OS & apps",
    },
    psu: {
      ultra: "Premium — ample headroom & efficiency",
      high: "Reliable — good headroom for upgrades",
      mid: "Adequate — covers current load",
      low: "Tight — limited headroom",
    },
    case: {
      ultra: "Excellent airflow — thermal headroom",
      high: "Good airflow — keeps temps in check",
      mid: "Decent — acceptable thermals",
      low: "Restrictive — watch temps under load",
    },
  };
  return map[type][tier] ?? "";
}

export function analyzeBottleneck(build: BuildComponents): BottleneckAnalysis {
  const scored: ComponentScore[] = [];

  const scoreMap: Partial<Record<ComponentType, number>> = {};
  if (build.cpu) scoreMap.cpu = build.cpu.performanceScore;
  if (build.gpu) scoreMap.gpu = build.gpu.performanceScore;
  if (build.ram) scoreMap.ram = build.ram.performanceScore;
  if (build.motherboard) scoreMap.motherboard = build.motherboard.performanceScore;
  if (build.storage) scoreMap.storage = build.storage.performanceScore;
  if (build.psu) scoreMap.psu = build.psu.performanceScore;
  if (build.case) scoreMap.case = build.case.performanceScore;

  const allScores = Object.values(scoreMap).filter(Boolean) as number[];
  if (allScores.length === 0) {
    return {
      components: [],
      primaryBottleneck: null,
      secondaryBottleneck: null,
      overallTier: "entry",
      summary: "Add components to see bottleneck analysis.",
    };
  }

  const maxScore = Math.max(...allScores);

  const ordered: ComponentType[] = ["cpu", "gpu", "ram", "motherboard", "storage", "psu", "case"];

  for (const type of ordered) {
    const score = scoreMap[type];
    if (score === undefined) continue;
    const gap = maxScore - score;
    const bottleneckImpact = Math.round((gap / maxScore) * 100);
    const isBottleneck = bottleneckImpact > 20;
    scored.push({
      type,
      label: COMPONENT_LABELS[type],
      score,
      isBottleneck,
      bottleneckImpact,
      tier: getTier(score),
      note: getComponentNote(type, score),
    });
  }

  scored.sort((a, b) => a.score - b.score);

  const bottlenecks = scored.filter((c) => c.isBottleneck);
  const primaryBottleneck = bottlenecks[0]?.type ?? null;
  const secondaryBottleneck = bottlenecks[1]?.type ?? null;

  const avgScore = allScores.reduce((a, b) => a + b, 0) / allScores.length;
  const overallTier: BottleneckAnalysis["overallTier"] =
    avgScore >= 88 ? "enthusiast" : avgScore >= 70 ? "high" : avgScore >= 50 ? "mid" : "entry";

  let summary: string;
  if (bottlenecks.length === 0) {
    summary = "Well-balanced build — no significant bottlenecks detected.";
  } else if (bottlenecks.length === 1) {
    summary = `${COMPONENT_LABELS[primaryBottleneck!]} is the weakest link, holding back ${Math.round(bottlenecks[0].bottleneckImpact)}% of potential performance.`;
  } else {
    summary = `${COMPONENT_LABELS[primaryBottleneck!]} and ${COMPONENT_LABELS[secondaryBottleneck!]} are bottlenecking your build — consider upgrading them first.`;
  }

  return { components: scored, primaryBottleneck, secondaryBottleneck, overallTier, summary };
}
