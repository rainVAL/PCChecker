import { Feather } from "@expo/vector-icons";
import { router } from "expo-router";
import React, { useMemo } from "react";
import {
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { StatusBadge } from "@/components/StatusBadge";
import { useColors } from "@/hooks/useColors";
import { useBuild } from "@/context/BuildContext";
import { checkCompatibility } from "@/utils/compatibility";
import { COMPONENT_LABELS, COMPONENT_ORDER } from "@/data/components";
import { ComponentIcon } from "@/components/ComponentIcon";

export default function ResultsScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { build, totalPrice } = useBuild();

  const compat = useMemo(() => checkCompatibility(build), [build]);
  const topPad = Platform.OS === "web" ? Math.max(insets.top, 67) : insets.top;
  const bottomPad = Platform.OS === "web" ? 34 : 0;

  const errors = compat.issues.filter((i) => i.severity === "error");
  const warnings = compat.issues.filter((i) => i.severity === "warning");

  const getBalanceLabel = (score: number) => {
    if (score >= 85) return "Excellent balance";
    if (score >= 70) return "Good balance";
    if (score >= 50) return "Fair balance";
    return "Imbalanced build";
  };

  const getBalanceColor = (score: number) => {
    if (score >= 85) return colors.success;
    if (score >= 70) return colors.primary;
    if (score >= 50) return colors.warning;
    return colors.destructive;
  };

  return (
    <View style={[styles.container, { backgroundColor: colors.background }]}>
      <View style={[styles.header, { paddingTop: topPad + 12, backgroundColor: colors.background, borderBottomColor: colors.border }]}>
        <Pressable onPress={() => router.back()} style={styles.backBtn} hitSlop={8}>
          <Feather name="arrow-left" size={22} color={colors.foreground} />
        </Pressable>
        <Text style={[styles.headerTitle, { color: colors.foreground }]}>Compatibility Report</Text>
        <View style={{ width: 38 }} />
      </View>

      <ScrollView
        showsVerticalScrollIndicator={false}
        contentContainerStyle={[styles.scrollContent, { paddingBottom: bottomPad + 30 }]}
      >
        <View style={[styles.statusCard, {
          backgroundColor: colors.card,
          borderColor: colors.border,
        }]}>
          <StatusBadge status={compat.status} size="lg" />
          <View style={styles.scoreRow}>
            <View style={styles.scoreItem}>
              <Text style={[styles.scoreValue, { color: colors.foreground }]}>{compat.score}</Text>
              <Text style={[styles.scoreLabel, { color: colors.mutedForeground }]}>Score</Text>
            </View>
            <View style={[styles.scoreDivider, { backgroundColor: colors.border }]} />
            <View style={styles.scoreItem}>
              <Text style={[styles.scoreValue, { color: colors.foreground }]}>{errors.length}</Text>
              <Text style={[styles.scoreLabel, { color: colors.mutedForeground }]}>Errors</Text>
            </View>
            <View style={[styles.scoreDivider, { backgroundColor: colors.border }]} />
            <View style={styles.scoreItem}>
              <Text style={[styles.scoreValue, { color: colors.foreground }]}>{warnings.length}</Text>
              <Text style={[styles.scoreLabel, { color: colors.mutedForeground }]}>Warnings</Text>
            </View>
          </View>
        </View>

        {compat.performanceBalance > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <Text style={[styles.sectionTitle, { color: colors.foreground }]}>Performance Balance</Text>
            <View style={styles.balanceRow}>
              <View style={[styles.balanceBar, { backgroundColor: colors.muted }]}>
                <View
                  style={[
                    styles.balanceFill,
                    {
                      width: `${compat.performanceBalance}%`,
                      backgroundColor: getBalanceColor(compat.performanceBalance),
                    },
                  ]}
                />
              </View>
              <Text style={[styles.balanceValue, { color: getBalanceColor(compat.performanceBalance) }]}>
                {compat.performanceBalance}%
              </Text>
            </View>
            <Text style={[styles.balanceLabel, { color: colors.mutedForeground }]}>
              {getBalanceLabel(compat.performanceBalance)}
            </Text>

            {compat.bottleneck && (
              <View style={[styles.bottleneckRow, { backgroundColor: colors.warning + "15", borderColor: colors.warning + "30" }]}>
                <Feather name="alert-circle" size={15} color={colors.warning} />
                <Text style={[styles.bottleneckText, { color: colors.warning }]}>{compat.bottleneck}</Text>
              </View>
            )}
          </View>
        )}

        {COMPONENT_ORDER.some((t) => build[t]) && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <Text style={[styles.sectionTitle, { color: colors.foreground }]}>Build Summary</Text>
            {COMPONENT_ORDER.filter((t) => build[t]).map((type) => {
              const c = build[type]!;
              return (
                <View key={type} style={styles.buildRow}>
                  <ComponentIcon type={type} size={16} color={colors.mutedForeground} />
                  <View style={styles.buildInfo}>
                    <Text style={[styles.buildType, { color: colors.mutedForeground }]}>{COMPONENT_LABELS[type]}</Text>
                    <Text style={[styles.buildName, { color: colors.foreground }]}>{c.name}</Text>
                  </View>
                  <Text style={[styles.buildPrice, { color: colors.foreground }]}>${c.price}</Text>
                </View>
              );
            })}
            <View style={[styles.totalRow, { borderTopColor: colors.border }]}>
              <Text style={[styles.totalLabel, { color: colors.foreground }]}>Total</Text>
              <Text style={[styles.totalValue, { color: colors.primary }]}>${totalPrice.toLocaleString()}</Text>
            </View>
            <View style={styles.powerRow}>
              <Feather name="zap" size={14} color={colors.mutedForeground} />
              <Text style={[styles.powerText, { color: colors.mutedForeground }]}>
                Estimated power draw: ~{compat.estimatedPowerDraw}W
              </Text>
            </View>
          </View>
        )}

        {errors.length > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.issueSectionHeader}>
              <Feather name="x-circle" size={16} color={colors.destructive} />
              <Text style={[styles.sectionTitle, { color: colors.destructive }]}>Errors ({errors.length})</Text>
            </View>
            {errors.map((issue, i) => (
              <View key={i} style={[styles.issueItem, { borderColor: colors.destructive + "25", backgroundColor: colors.destructive + "08" }]}>
                <Text style={[styles.issueTitle, { color: colors.destructive }]}>{issue.title}</Text>
                <Text style={[styles.issueDesc, { color: colors.foreground }]}>{issue.description}</Text>
                <View style={styles.issueComponents}>
                  {issue.components.map((comp, j) => (
                    <View key={j} style={[styles.compTag, { backgroundColor: colors.destructive + "15" }]}>
                      <Text style={[styles.compTagText, { color: colors.destructive }]}>{comp}</Text>
                    </View>
                  ))}
                </View>
              </View>
            ))}
          </View>
        )}

        {warnings.length > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.issueSectionHeader}>
              <Feather name="alert-triangle" size={16} color={colors.warning} />
              <Text style={[styles.sectionTitle, { color: colors.warning }]}>Warnings ({warnings.length})</Text>
            </View>
            {warnings.map((issue, i) => (
              <View key={i} style={[styles.issueItem, { borderColor: colors.warning + "25", backgroundColor: colors.warning + "08" }]}>
                <Text style={[styles.issueTitle, { color: colors.warning }]}>{issue.title}</Text>
                <Text style={[styles.issueDesc, { color: colors.foreground }]}>{issue.description}</Text>
                <View style={styles.issueComponents}>
                  {issue.components.map((comp, j) => (
                    <View key={j} style={[styles.compTag, { backgroundColor: colors.warning + "15" }]}>
                      <Text style={[styles.compTagText, { color: colors.warning }]}>{comp}</Text>
                    </View>
                  ))}
                </View>
              </View>
            ))}
          </View>
        )}

        {compat.status === "compatible" && compat.issues.length === 0 && (
          <View style={[styles.successCard, { backgroundColor: colors.success + "12", borderColor: colors.success + "30" }]}>
            <Feather name="check-circle" size={32} color={colors.success} />
            <Text style={[styles.successTitle, { color: colors.success }]}>All Good!</Text>
            <Text style={[styles.successDesc, { color: colors.foreground }]}>
              All selected components are fully compatible. Your build looks great!
            </Text>
          </View>
        )}
      </ScrollView>
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1 },
  header: {
    flexDirection: "row",
    alignItems: "center",
    paddingHorizontal: 16,
    paddingBottom: 12,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  backBtn: { width: 38 },
  headerTitle: { flex: 1, textAlign: "center", fontFamily: "Inter_700Bold", fontSize: 18 },
  scrollContent: { padding: 16, gap: 12 },
  statusCard: {
    borderRadius: 20,
    borderWidth: 1,
    padding: 20,
    gap: 16,
    alignItems: "center",
  },
  scoreRow: { flexDirection: "row", alignItems: "center", width: "100%" },
  scoreItem: { flex: 1, alignItems: "center", gap: 4 },
  scoreDivider: { width: 1, height: 36 },
  scoreValue: { fontFamily: "Inter_700Bold", fontSize: 28 },
  scoreLabel: { fontFamily: "Inter_500Medium", fontSize: 12 },
  section: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
    gap: 12,
  },
  sectionTitle: { fontFamily: "Inter_700Bold", fontSize: 16 },
  issueSectionHeader: { flexDirection: "row", alignItems: "center", gap: 8 },
  balanceRow: { flexDirection: "row", alignItems: "center", gap: 10 },
  balanceBar: { flex: 1, height: 8, borderRadius: 4, overflow: "hidden" },
  balanceFill: { height: "100%", borderRadius: 4 },
  balanceValue: { fontFamily: "Inter_700Bold", fontSize: 16, minWidth: 40, textAlign: "right" },
  balanceLabel: { fontFamily: "Inter_500Medium", fontSize: 13 },
  bottleneckRow: {
    flexDirection: "row",
    alignItems: "flex-start",
    gap: 8,
    padding: 12,
    borderRadius: 10,
    borderWidth: 1,
  },
  bottleneckText: { flex: 1, fontFamily: "Inter_500Medium", fontSize: 13, lineHeight: 18 },
  buildRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
  },
  buildInfo: { flex: 1 },
  buildType: { fontFamily: "Inter_400Regular", fontSize: 11 },
  buildName: { fontFamily: "Inter_500Medium", fontSize: 14 },
  buildPrice: { fontFamily: "Inter_600SemiBold", fontSize: 14 },
  totalRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingTop: 12,
    borderTopWidth: 1,
  },
  totalLabel: { fontFamily: "Inter_700Bold", fontSize: 16 },
  totalValue: { fontFamily: "Inter_700Bold", fontSize: 20 },
  powerRow: { flexDirection: "row", alignItems: "center", gap: 6 },
  powerText: { fontFamily: "Inter_400Regular", fontSize: 13 },
  issueItem: {
    borderRadius: 12,
    borderWidth: 1,
    padding: 12,
    gap: 6,
  },
  issueTitle: { fontFamily: "Inter_700Bold", fontSize: 14 },
  issueDesc: { fontFamily: "Inter_400Regular", fontSize: 13, lineHeight: 18 },
  issueComponents: { flexDirection: "row", flexWrap: "wrap", gap: 6 },
  compTag: {
    paddingHorizontal: 8,
    paddingVertical: 3,
    borderRadius: 6,
  },
  compTagText: { fontFamily: "Inter_500Medium", fontSize: 11 },
  successCard: {
    borderRadius: 20,
    borderWidth: 1,
    padding: 28,
    alignItems: "center",
    gap: 10,
  },
  successTitle: { fontFamily: "Inter_700Bold", fontSize: 22 },
  successDesc: { fontFamily: "Inter_400Regular", fontSize: 14, textAlign: "center", lineHeight: 20 },
});
