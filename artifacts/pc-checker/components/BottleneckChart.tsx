import { Feather } from "@expo/vector-icons";
import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { ComponentScore } from "@/utils/bottleneck";
import { useColors } from "@/hooks/useColors";
import { ComponentIcon } from "@/components/ComponentIcon";

interface Props {
  components: ComponentScore[];
  primaryBottleneck: string | null;
}

const TIER_COLORS = {
  low: "#ef4444",
  mid: "#f59e0b",
  high: "#3b82f6",
  ultra: "#10b981",
};

const TIER_LABELS = {
  low: "Entry",
  mid: "Mid",
  high: "High",
  ultra: "Ultra",
};

export function BottleneckChart({ components, primaryBottleneck }: Props) {
  const colors = useColors();
  const maxScore = Math.max(...components.map((c) => c.score), 1);

  return (
    <View style={styles.container}>
      {components.map((comp) => {
        const barColor = TIER_COLORS[comp.tier];
        const isPrimary = comp.type === primaryBottleneck;
        const barWidth = (comp.score / 100) * 100;

        return (
          <View key={comp.type} style={styles.row}>
            <View style={styles.labelCol}>
              <View style={styles.labelRow}>
                <ComponentIcon type={comp.type} size={13} color={isPrimary ? colors.destructive : colors.mutedForeground} />
                <Text
                  style={[
                    styles.label,
                    { color: isPrimary ? colors.destructive : colors.foreground },
                  ]}
                >
                  {comp.label}
                </Text>
                {isPrimary && (
                  <View style={[styles.bottleneckBadge, { backgroundColor: colors.destructive + "20" }]}>
                    <Text style={[styles.bottleneckBadgeText, { color: colors.destructive }]}>
                      bottleneck
                    </Text>
                  </View>
                )}
              </View>
              <Text style={[styles.note, { color: colors.mutedForeground }]} numberOfLines={1}>
                {comp.note}
              </Text>
            </View>

            <View style={styles.barCol}>
              <View style={[styles.barTrack, { backgroundColor: colors.muted }]}>
                <View
                  style={[
                    styles.barFill,
                    {
                      width: `${barWidth}%`,
                      backgroundColor: isPrimary ? colors.destructive : barColor,
                      borderRadius: 4,
                    },
                  ]}
                />
                {isPrimary && (
                  <View
                    style={[
                      styles.bottleneckLine,
                      {
                        left: `${(maxScore / 100) * 100}%`,
                        backgroundColor: colors.destructive + "50",
                      },
                    ]}
                  />
                )}
              </View>
              <View style={styles.scoreRow}>
                <View style={[styles.tierPill, { backgroundColor: barColor + "22" }]}>
                  <Text style={[styles.tierText, { color: barColor }]}>
                    {TIER_LABELS[comp.tier]}
                  </Text>
                </View>
                <Text style={[styles.score, { color: isPrimary ? colors.destructive : colors.foreground }]}>
                  {comp.score}
                </Text>
              </View>
            </View>
          </View>
        );
      })}
    </View>
  );
}

const styles = StyleSheet.create({
  container: { gap: 14 },
  row: { flexDirection: "row", gap: 10, alignItems: "flex-start" },
  labelCol: { width: 110, gap: 2 },
  labelRow: { flexDirection: "row", alignItems: "center", gap: 4, flexWrap: "wrap" },
  label: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  bottleneckBadge: {
    paddingHorizontal: 5,
    paddingVertical: 1,
    borderRadius: 4,
  },
  bottleneckBadgeText: { fontFamily: "Inter_600SemiBold", fontSize: 9 },
  note: { fontFamily: "Inter_400Regular", fontSize: 10, lineHeight: 13 },
  barCol: { flex: 1, gap: 4 },
  barTrack: {
    height: 10,
    borderRadius: 5,
    overflow: "visible",
    position: "relative",
  },
  barFill: { height: "100%", minWidth: 4 },
  bottleneckLine: {
    position: "absolute",
    top: -3,
    width: 1.5,
    height: 16,
    borderRadius: 1,
  },
  scoreRow: { flexDirection: "row", alignItems: "center", justifyContent: "space-between" },
  tierPill: {
    paddingHorizontal: 6,
    paddingVertical: 2,
    borderRadius: 5,
  },
  tierText: { fontFamily: "Inter_600SemiBold", fontSize: 10 },
  score: { fontFamily: "Inter_700Bold", fontSize: 12 },
});
