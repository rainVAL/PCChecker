import { PCComponent } from "../data/components";

export type CompatibilityStatus = "compatible" | "partial" | "incompatible" | "missing";

export interface CompatibilityIssue {
  severity: "error" | "warning" | "info";
  title: string;
  description: string;
  components: string[];
}

export interface CompatibilityResult {
  status: CompatibilityStatus;
  score: number;
  issues: CompatibilityIssue[];
  estimatedPowerDraw: number;
  bottleneck: string | null;
  performanceBalance: number;
}

export interface BuildComponents {
  cpu?: PCComponent;
  gpu?: PCComponent;
  ram?: PCComponent;
  motherboard?: PCComponent;
  storage?: PCComponent;
  psu?: PCComponent;
  case?: PCComponent;
}

function checkCpuMotherboardCompatibility(
  cpu: PCComponent,
  mb: PCComponent,
  issues: CompatibilityIssue[]
): void {
  const cpuSocket = cpu.specs["socket"] as string;
  const mbSocket = mb.specs["socket"] as string;

  if (cpuSocket !== mbSocket) {
    issues.push({
      severity: "error",
      title: "Socket Mismatch",
      description: `CPU uses ${cpuSocket} socket but motherboard supports ${mbSocket}. These are physically incompatible.`,
      components: [cpu.name, mb.name],
    });
  }
}

function checkRamMotherboardCompatibility(
  ram: PCComponent,
  mb: PCComponent,
  cpu: PCComponent | undefined,
  issues: CompatibilityIssue[]
): void {
  const ramType = ram.specs["type"] as string;
  const mbMemType = mb.specs["memType"] as string;

  if (!mbMemType.includes(ramType)) {
    issues.push({
      severity: "error",
      title: "RAM Type Mismatch",
      description: `Motherboard supports ${mbMemType} memory, but selected RAM is ${ramType}. These are physically incompatible.`,
      components: [ram.name, mb.name],
    });
    return;
  }

  if (cpu) {
    const cpuMemTypes = (cpu.specs["memType"] as string).split(",");
    if (!cpuMemTypes.includes(ramType)) {
      issues.push({
        severity: "error",
        title: "CPU/RAM Type Mismatch",
        description: `CPU only supports ${cpu.specs["memType"]} memory, but selected RAM is ${ramType}.`,
        components: [ram.name, cpu.name],
      });
    }

    const ramSpeed = ram.specs["speed"] as number;
    const maxCpuRamSpeed = cpu.specs["maxRamSpeed"] as number;
    if (ramSpeed > maxCpuRamSpeed) {
      issues.push({
        severity: "warning",
        title: "RAM Speed Exceeds CPU Limit",
        description: `RAM is rated at ${ramSpeed}MHz, but CPU only supports up to ${maxCpuRamSpeed}MHz. RAM will run at ${maxCpuRamSpeed}MHz.`,
        components: [ram.name, cpu.name],
      });
    }
  }
}

function checkGpuCaseCompatibility(
  gpu: PCComponent,
  pcCase: PCComponent,
  issues: CompatibilityIssue[]
): void {
  const gpuLength = gpu.specs["length"] as number;
  const maxGpuLength = pcCase.specs["maxGpuLength"] as number;

  if (gpuLength > maxGpuLength) {
    issues.push({
      severity: "error",
      title: "GPU Too Long for Case",
      description: `GPU is ${gpuLength}mm long, but case only supports up to ${maxGpuLength}mm. The GPU will not physically fit.`,
      components: [gpu.name, pcCase.name],
    });
  } else if (gpuLength > maxGpuLength * 0.9) {
    issues.push({
      severity: "warning",
      title: "Tight GPU Fit",
      description: `GPU (${gpuLength}mm) is close to the case limit (${maxGpuLength}mm). Check clearance for cables.`,
      components: [gpu.name, pcCase.name],
    });
  }
}

function checkMotherboardCaseCompatibility(
  mb: PCComponent,
  pcCase: PCComponent,
  issues: CompatibilityIssue[]
): void {
  const mbForm = mb.specs["formFactor"] as string;
  const supportedForms = pcCase.specs["formFactor"] as string;

  if (!supportedForms.includes(mbForm)) {
    issues.push({
      severity: "error",
      title: "Form Factor Mismatch",
      description: `Case supports ${supportedForms} motherboards, but selected board is ${mbForm}. The motherboard will not fit.`,
      components: [mb.name, pcCase.name],
    });
  }
}

function estimatePowerDraw(components: BuildComponents): number {
  let total = 50;
  if (components.cpu) total += (components.cpu.specs["tdp"] as number) * 1.1;
  if (components.gpu) total += (components.gpu.specs["tdp"] as number) * 1.1;
  if (components.ram) total += 10;
  if (components.storage) total += 10;
  return Math.round(total);
}

function checkPsuAdequacy(
  psu: PCComponent,
  estimatedDraw: number,
  issues: CompatibilityIssue[]
): void {
  const wattage = psu.specs["wattage"] as number;
  const recommended = Math.round(estimatedDraw * 1.2);
  const comfortable = Math.round(estimatedDraw * 1.5);

  if (wattage < estimatedDraw) {
    issues.push({
      severity: "error",
      title: "Insufficient Power Supply",
      description: `Estimated power draw is ~${estimatedDraw}W, but PSU is only ${wattage}W. System will not boot or will crash under load.`,
      components: [psu.name],
    });
  } else if (wattage < recommended) {
    issues.push({
      severity: "error",
      title: "PSU Critically Underpowered",
      description: `PSU (${wattage}W) is below the recommended ${recommended}W (20% headroom). Risk of instability and hardware damage.`,
      components: [psu.name],
    });
  } else if (wattage < comfortable) {
    issues.push({
      severity: "warning",
      title: "Limited PSU Headroom",
      description: `PSU (${wattage}W) meets minimum needs but has limited headroom. Recommended: ${comfortable}W for stability during peak loads.`,
      components: [psu.name],
    });
  }
}

function detectBottleneck(components: BuildComponents): string | null {
  const { cpu, gpu } = components;
  if (!cpu || !gpu) return null;

  const cpuScore = cpu.performanceScore;
  const gpuScore = gpu.performanceScore;
  const diff = Math.abs(cpuScore - gpuScore);

  if (diff < 15) return null;

  if (cpuScore < gpuScore - 15) {
    return `CPU bottleneck: ${cpu.name} (score ${cpuScore}) may limit ${gpu.name} (score ${gpuScore}) performance by ~${Math.min(Math.round(diff * 0.8), 30)}%`;
  } else {
    return `GPU bottleneck: ${gpu.name} (score ${gpuScore}) may limit ${cpu.name} (score ${cpuScore}) performance by ~${Math.min(Math.round(diff * 0.8), 30)}%`;
  }
}

function calculatePerformanceBalance(components: BuildComponents): number {
  const scores: number[] = [];
  if (components.cpu) scores.push(components.cpu.performanceScore);
  if (components.gpu) scores.push(components.gpu.performanceScore);
  if (components.ram) scores.push(components.ram.performanceScore);
  if (components.storage) scores.push(components.storage.performanceScore);

  if (scores.length === 0) return 0;

  const avg = scores.reduce((a, b) => a + b, 0) / scores.length;
  const variance = scores.reduce((a, b) => a + Math.pow(b - avg, 2), 0) / scores.length;
  const stdDev = Math.sqrt(variance);

  const balance = Math.max(0, Math.round(100 - stdDev * 1.5));
  return balance;
}

export function checkCompatibility(components: BuildComponents): CompatibilityResult {
  const issues: CompatibilityIssue[] = [];

  const hasAny = Object.values(components).some(Boolean);
  if (!hasAny) {
    return {
      status: "missing",
      score: 0,
      issues: [],
      estimatedPowerDraw: 0,
      bottleneck: null,
      performanceBalance: 0,
    };
  }

  const { cpu, gpu, ram, motherboard, storage, psu, case: pcCase } = components;

  if (cpu && motherboard) {
    checkCpuMotherboardCompatibility(cpu, motherboard, issues);
  }

  if (ram && motherboard) {
    checkRamMotherboardCompatibility(ram, motherboard, cpu, issues);
  } else if (ram && cpu && !motherboard) {
    const cpuMemTypes = (cpu.specs["memType"] as string).split(",");
    const ramType = ram.specs["type"] as string;
    if (!cpuMemTypes.includes(ramType)) {
      issues.push({
        severity: "error",
        title: "CPU/RAM Type Mismatch",
        description: `CPU only supports ${cpu.specs["memType"]} memory, but selected RAM is ${ramType}.`,
        components: [ram.name, cpu.name],
      });
    }
  }

  if (gpu && pcCase) {
    checkGpuCaseCompatibility(gpu, pcCase, issues);
  }

  if (motherboard && pcCase) {
    checkMotherboardCaseCompatibility(motherboard, pcCase, issues);
  }

  const estimatedPowerDraw = estimatePowerDraw(components);

  if (psu) {
    checkPsuAdequacy(psu, estimatedPowerDraw, issues);
  }

  if (cpu && !cpu.specs["integratedGraphics"] && !gpu) {
    issues.push({
      severity: "warning",
      title: "No Display Output",
      description: `${cpu.name} has no integrated graphics. Without a dedicated GPU, there will be no video output.`,
      components: [cpu.name],
    });
  }

  const errorCount = issues.filter((i) => i.severity === "error").length;
  const warningCount = issues.filter((i) => i.severity === "warning").length;

  let score = 100;
  score -= errorCount * 30;
  score -= warningCount * 10;
  score = Math.max(0, score);

  let status: CompatibilityStatus;
  if (errorCount > 0) status = "incompatible";
  else if (warningCount > 0) status = "partial";
  else status = "compatible";

  const bottleneck = detectBottleneck(components);
  const performanceBalance = calculatePerformanceBalance(components);

  return {
    status,
    score,
    issues,
    estimatedPowerDraw,
    bottleneck,
    performanceBalance,
  };
}
