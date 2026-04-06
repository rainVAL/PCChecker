import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { CompatibilityStatus } from "../utils/compatibility";
import { useColors } from "@/hooks/useColors";

interface Props {
  status: CompatibilityStatus;
  size?: "sm" | "md" | "lg";
}

const STATUS_CONFIG = {
  compatible: { label: "Compatible", emoji: "✓" },
  partial: { label: "Partial", emoji: "⚠" },
  incompatible: { label: "Incompatible", emoji: "✗" },
  missing: { label: "Add Parts", emoji: "○" },
};

export function StatusBadge({ status, size = "md" }: Props) {
  const colors = useColors();

  const getColors = () => {
    switch (status) {
      case "compatible":
        return { bg: colors.success + "22", text: colors.success, border: colors.success + "44" };
      case "partial":
        return { bg: colors.warning + "22", text: colors.warning, border: colors.warning + "44" };
      case "incompatible":
        return { bg: colors.destructive + "22", text: colors.destructive, border: colors.destructive + "44" };
      case "missing":
        return { bg: colors.muted, text: colors.mutedForeground, border: colors.border };
    }
  };

  const c = getColors();
  const config = STATUS_CONFIG[status];

  const sizeStyles = {
    sm: { px: 8, py: 3, fontSize: 11 },
    md: { px: 10, py: 5, fontSize: 13 },
    lg: { px: 14, py: 7, fontSize: 15 },
  }[size];

  return (
    <View
      style={[
        styles.badge,
        {
          backgroundColor: c.bg,
          borderColor: c.border,
          paddingHorizontal: sizeStyles.px,
          paddingVertical: sizeStyles.py,
        },
      ]}
    >
      <Text style={[styles.text, { color: c.text, fontSize: sizeStyles.fontSize }]}>
        {config.emoji}  {config.label}
      </Text>
    </View>
  );
}

const styles = StyleSheet.create({
  badge: {
    borderRadius: 100,
    borderWidth: 1,
    alignSelf: "flex-start",
  },
  text: {
    fontFamily: "Inter_600SemiBold",
    letterSpacing: 0.2,
  },
});
