import { Feather } from "@expo/vector-icons";
import React from "react";
import { ComponentType } from "../data/components";

interface Props {
  type: ComponentType;
  size?: number;
  color?: string;
}

const ICON_MAP: Record<ComponentType, keyof typeof Feather.glyphMap> = {
  cpu: "cpu",
  gpu: "monitor",
  ram: "layers",
  motherboard: "grid",
  storage: "hard-drive",
  psu: "zap",
  case: "box",
};

export function ComponentIcon({ type, size = 20, color = "#888" }: Props) {
  const icon = ICON_MAP[type];
  return <Feather name={icon} size={size} color={color} />;
}
