import { Feather } from "@expo/vector-icons";
import * as Haptics from "expo-haptics";
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
import { ComponentIcon } from "@/components/ComponentIcon";
import { useColors } from "@/hooks/useColors";
import { useBuild } from "@/context/BuildContext";
import { checkCompatibility } from "@/utils/compatibility";
import { generateSuggestions, Suggestion } from "@/utils/suggestions";
import { analyzeBottleneck } from "@/utils/bottleneck";
import { BottleneckChart } from "@/components/BottleneckChart";
import { COMPONENT_LABELS, COMPONENT_ORDER, ComponentType, USE_CASE_CONFIG } from "@/data/components";

function SuggestionCard({ suggestion, onSwap }: { suggestion: Suggestion; onSwap?: () => void }) {
  const colors = useColors();

  const typeConfig = {
    upgrade: { icon: "arrow-up-circle" as const, color: colors.primary, label: "Upgrade" },
    balance: { icon: "activity" as const, color: colors.warning, label: "Balance Fix" },
    optimization: { icon: "sliders" as const, color: colors.success, label: "Optimization" },
    tip: { icon: "info" as const, color: "#8b5cf6", label: "Tip" },
  }[suggestion.type];

  const priorityDot = {
    high: colors.destructive,
    medium: colors.warning,
    low: colors.success,
  }[suggestion.priority];

  return (
    <View
      style={[
        styles.suggCard,
        {
          backgroundColor: typeConfig.color + "0C",
          borderColor: typeConfig.color + "28",
        },
      ]}
    >
      <View style={styles.suggHeader}>
        <View style={[styles.suggIconWrap, { backgroundColor: typeConfig.color + "20" }]}>
          <Feather name={typeConfig.icon} size={15} color={typeConfig.color} />
        </View>
        <View style={styles.suggMeta}>
          <View style={styles.suggMetaRow}>
            <Text style={[styles.suggTypeLabel, { color: typeConfig.color }]}>
              {typeConfig.label}
            </Text>
            <View style={[styles.priorityDot, { backgroundColor: priorityDot }]} />
            <Text style={[styles.priorityLabel, { color: colors.mutedForeground }]}>
              {suggestion.priority === "high" ? "High priority" : suggestion.priority === "medium" ? "Medium" : "Low priority"}
            </Text>
          </View>
          <Text style={[styles.suggTitle, { color: colors.foreground }]}>
            {suggestion.title}
          </Text>
        </View>
      </View>

      <Text style={[styles.suggDesc, { color: colors.mutedForeground }]}>
        {suggestion.description}
      </Text>

      {suggestion.impact && (
        <View style={[styles.impactRow, { backgroundColor: typeConfig.color + "14", borderColor: typeConfig.color + "25" }]}>
          <Feather name="trending-up" size={12} color={typeConfig.color} />
          <Text style={[styles.impactText, { color: typeConfig.color }]}>{suggestion.impact}</Text>
        </View>
      )}

      {suggestion.suggestedComponent && suggestion.currentComponent && (
        <View style={[styles.swapRow, { borderColor: colors.border }]}>
          <View style={styles.swapItem}>
            <Text style={[styles.swapLabel, { color: colors.mutedForeground }]}>Current</Text>
            <Text style={[styles.swapName, { color: colors.foreground }]} numberOfLines={1}>
              {suggestion.currentComponent.name}
            </Text>
            <Text style={[styles.swapPrice, { color: colors.mutedForeground }]}>
              ${suggestion.currentComponent.price}
            </Text>
          </View>
          <View style={[styles.swapArrow, { backgroundColor: typeConfig.color + "18" }]}>
            <Feather name="arrow-right" size={14} color={typeConfig.color} />
          </View>
          <View style={styles.swapItem}>
            <Text style={[styles.swapLabel, { color: colors.mutedForeground }]}>Suggested</Text>
            <Text style={[styles.swapName, { color: typeConfig.color }]} numberOfLines={1}>
              {suggestion.suggestedComponent.name}
            </Text>
            <Text style={[styles.swapPrice, { color: colors.foreground }]}>
              ${suggestion.suggestedComponent.price}
              {suggestion.savings ? (
                <Text style={{ color: colors.success }}> (save ${suggestion.savings})</Text>
              ) : suggestion.suggestedComponent.price > suggestion.currentComponent.price ? (
                <Text style={{ color: colors.mutedForeground }}>
                  {" "}(+${suggestion.suggestedComponent.price - suggestion.currentComponent.price})
                </Text>
              ) : null}
            </Text>
          </View>
        </View>
      )}

      {suggestion.suggestedComponent && !suggestion.currentComponent && (
        <View style={[styles.addRow, { backgroundColor: typeConfig.color + "10", borderColor: typeConfig.color + "25" }]}>
          <View style={styles.swapItem}>
            <Text style={[styles.swapLabel, { color: colors.mutedForeground }]}>Recommended</Text>
            <Text style={[styles.swapName, { color: typeConfig.color }]} numberOfLines={1}>
              {suggestion.suggestedComponent.name}
            </Text>
            <Text style={[styles.swapPrice, { color: colors.foreground }]}>
              ${suggestion.suggestedComponent.price}
            </Text>
          </View>
          <Pressable
            onPress={onSwap}
            style={[styles.browseBtn, { backgroundColor: typeConfig.color }]}
          >
            <Feather name="plus" size={13} color="#fff" />
            <Text style={styles.browseBtnText}>Add</Text>
          </Pressable>
        </View>
      )}

      {suggestion.suggestedComponent && onSwap && suggestion.currentComponent && (
        <Pressable
          onPress={onSwap}
          style={[styles.swapBtn, { borderColor: typeConfig.color, backgroundColor: typeConfig.color + "10" }]}
        >
          <Feather name="refresh-cw" size={13} color={typeConfig.color} />
          <Text style={[styles.swapBtnText, { color: typeConfig.color }]}>
            Browse {suggestion.componentType ? COMPONENT_LABELS[suggestion.componentType as ComponentType] : "part"}
          </Text>
        </Pressable>
      )}
    </View>
  );
}

export default function ResultsScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { build, totalPrice, useCase } = useBuild();

  const compat = useMemo(() => checkCompatibility(build), [build]);
  const suggestions = useMemo(() => generateSuggestions(build, useCase), [build, useCase]);
  const bottleneck = useMemo(() => analyzeBottleneck(build), [build]);

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

  const handleSuggestionSwap = (suggestion: Suggestion) => {
    if (suggestion.componentType) {
      Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
      router.push({ pathname: "/browse", params: { type: suggestion.componentType } });
    }
  };

  return (
    <View style={[styles.container, { backgroundColor: colors.background }]}>
      <View
        style={[
          styles.header,
          { paddingTop: topPad + 12, backgroundColor: colors.background, borderBottomColor: colors.border },
        ]}
      >
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
        {/* Status card */}
        <View style={[styles.statusCard, { backgroundColor: colors.card, borderColor: colors.border }]}>
          <StatusBadge status={compat.status} size="lg" />
          {useCase && (
            <View style={[styles.useCasePill, { backgroundColor: USE_CASE_CONFIG[useCase].color + "18" }]}>
              <Feather name={USE_CASE_CONFIG[useCase].icon as any} size={12} color={USE_CASE_CONFIG[useCase].color} />
              <Text style={[styles.useCasePillText, { color: USE_CASE_CONFIG[useCase].color }]}>
                {USE_CASE_CONFIG[useCase].label} Build
              </Text>
            </View>
          )}
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
            <View style={[styles.scoreDivider, { backgroundColor: colors.border }]} />
            <View style={styles.scoreItem}>
              <Text style={[styles.scoreValue, { color: colors.foreground }]}>{suggestions.length}</Text>
              <Text style={[styles.scoreLabel, { color: colors.mutedForeground }]}>Suggestions</Text>
            </View>
          </View>
        </View>

        {/* Performance balance */}
        {compat.performanceBalance > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <Text style={[styles.sectionTitle, { color: colors.foreground }]}>Performance Balance</Text>
            <View style={styles.balanceRow}>
              <View style={[styles.balanceBar, { backgroundColor: colors.muted }]}>
                <View
                  style={[
                    styles.balanceFill,
                    { width: `${compat.performanceBalance}%`, backgroundColor: getBalanceColor(compat.performanceBalance) },
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

        {/* Bottleneck Analysis */}
        {bottleneck.components.length > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.sectionTitleRow}>
              <Feather name="activity" size={15} color={bottleneck.primaryBottleneck ? colors.destructive : colors.success} />
              <Text style={[styles.sectionTitle, { color: colors.foreground }]}>Bottleneck Analysis</Text>
              <View style={[
                styles.tierBadge,
                { backgroundColor: (bottleneck.primaryBottleneck ? colors.destructive : colors.success) + "18" }
              ]}>
                <Text style={[styles.tierBadgeText, { color: bottleneck.primaryBottleneck ? colors.destructive : colors.success }]}>
                  {{entry: "Entry", mid: "Mid-range", high: "High-end", enthusiast: "Enthusiast"}[bottleneck.overallTier]}
                </Text>
              </View>
            </View>
            <Text style={[styles.bottleneckSummary, { color: colors.mutedForeground }]}>
              {bottleneck.summary}
            </Text>
            <View style={styles.chartWrap}>
              <BottleneckChart
                components={bottleneck.components}
                primaryBottleneck={bottleneck.primaryBottleneck}
              />
            </View>
          </View>
        )}

        {/* Suggestions */}
        {suggestions.length > 0 && (
          <View style={styles.suggestionsSection}>
            <View style={styles.suggestionsTitleRow}>
              <Feather name="zap" size={16} color={colors.primary} />
              <Text style={[styles.sectionTitle, { color: colors.foreground }]}>
                Optimization & Upgrade Suggestions
              </Text>
            </View>
            <Text style={[styles.suggestionsSubtitle, { color: colors.mutedForeground }]}>
              {suggestions.length} suggestion{suggestions.length !== 1 ? "s" : ""} to improve your build
            </Text>
            {suggestions.map((suggestion, i) => (
              <SuggestionCard
                key={i}
                suggestion={suggestion}
                onSwap={suggestion.componentType ? () => handleSuggestionSwap(suggestion) : undefined}
              />
            ))}
          </View>
        )}

        {/* Errors */}
        {errors.length > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.issueSectionHeader}>
              <Feather name="x-circle" size={16} color={colors.destructive} />
              <Text style={[styles.sectionTitle, { color: colors.destructive }]}>
                Errors ({errors.length})
              </Text>
            </View>
            {errors.map((issue, i) => (
              <View
                key={i}
                style={[
                  styles.issueItem,
                  { borderColor: colors.destructive + "25", backgroundColor: colors.destructive + "08" },
                ]}
              >
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

        {/* Warnings */}
        {warnings.length > 0 && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.issueSectionHeader}>
              <Feather name="alert-triangle" size={16} color={colors.warning} />
              <Text style={[styles.sectionTitle, { color: colors.warning }]}>
                Warnings ({warnings.length})
              </Text>
            </View>
            {warnings.map((issue, i) => (
              <View
                key={i}
                style={[
                  styles.issueItem,
                  { borderColor: colors.warning + "25", backgroundColor: colors.warning + "08" },
                ]}
              >
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

        {/* Build summary */}
        {COMPONENT_ORDER.some((t) => build[t]) && (
          <View style={[styles.section, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <Text style={[styles.sectionTitle, { color: colors.foreground }]}>Build Summary</Text>
            {COMPONENT_ORDER.filter((t) => build[t]).map((type) => {
              const c = build[type]!;
              return (
                <View key={type} style={styles.buildRow}>
                  <ComponentIcon type={type} size={16} color={colors.mutedForeground} />
                  <View style={styles.buildInfo}>
                    <Text style={[styles.buildType, { color: colors.mutedForeground }]}>
                      {COMPONENT_LABELS[type]}
                    </Text>
                    <Text style={[styles.buildName, { color: colors.foreground }]}>{c.name}</Text>
                  </View>
                  <Text style={[styles.buildPrice, { color: colors.foreground }]}>${c.price}</Text>
                </View>
              );
            })}
            <View style={[styles.totalRow, { borderTopColor: colors.border }]}>
              <Text style={[styles.totalLabel, { color: colors.foreground }]}>Total</Text>
              <Text style={[styles.totalValue, { color: colors.primary }]}>
                ${totalPrice.toLocaleString()}
              </Text>
            </View>
            <View style={styles.powerRow}>
              <Feather name="zap" size={14} color={colors.mutedForeground} />
              <Text style={[styles.powerText, { color: colors.mutedForeground }]}>
                Estimated power draw: ~{compat.estimatedPowerDraw}W
              </Text>
            </View>
          </View>
        )}

        {/* All good state */}
        {compat.status === "compatible" && compat.issues.length === 0 && suggestions.length === 0 && (
          <View
            style={[
              styles.successCard,
              { backgroundColor: colors.success + "12", borderColor: colors.success + "30" },
            ]}
          >
            <Feather name="check-circle" size={32} color={colors.success} />
            <Text style={[styles.successTitle, { color: colors.success }]}>All Good!</Text>
            <Text style={[styles.successDesc, { color: colors.foreground }]}>
              All components are fully compatible and well-balanced.
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
  statusCard: { borderRadius: 20, borderWidth: 1, padding: 20, gap: 12, alignItems: "center" },
  useCasePill: {
    flexDirection: "row",
    alignItems: "center",
    gap: 5,
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 20,
  },
  useCasePillText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  scoreRow: { flexDirection: "row", alignItems: "center", width: "100%" },
  scoreItem: { flex: 1, alignItems: "center", gap: 4 },
  scoreDivider: { width: 1, height: 36 },
  scoreValue: { fontFamily: "Inter_700Bold", fontSize: 24 },
  scoreLabel: { fontFamily: "Inter_500Medium", fontSize: 11 },
  section: { borderRadius: 16, borderWidth: 1, padding: 16, gap: 12 },
  sectionTitle: { fontFamily: "Inter_700Bold", fontSize: 16, flex: 1 },
  sectionTitleRow: { flexDirection: "row", alignItems: "center", gap: 8 },
  tierBadge: { paddingHorizontal: 8, paddingVertical: 3, borderRadius: 8 },
  tierBadgeText: { fontFamily: "Inter_600SemiBold", fontSize: 11 },
  bottleneckSummary: { fontFamily: "Inter_400Regular", fontSize: 13, lineHeight: 18, marginTop: -4 },
  chartWrap: { marginTop: 4 },
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
  suggestionsSection: { gap: 10 },
  suggestionsTitleRow: { flexDirection: "row", alignItems: "center", gap: 8 },
  suggestionsSubtitle: { fontFamily: "Inter_400Regular", fontSize: 13, marginTop: -4 },
  suggCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 14,
    gap: 10,
  },
  suggHeader: { flexDirection: "row", gap: 10, alignItems: "flex-start" },
  suggIconWrap: {
    width: 32,
    height: 32,
    borderRadius: 10,
    alignItems: "center",
    justifyContent: "center",
    marginTop: 1,
  },
  suggMeta: { flex: 1, gap: 3 },
  suggMetaRow: { flexDirection: "row", alignItems: "center", gap: 6 },
  suggTypeLabel: { fontFamily: "Inter_600SemiBold", fontSize: 11 },
  priorityDot: { width: 5, height: 5, borderRadius: 3 },
  priorityLabel: { fontFamily: "Inter_400Regular", fontSize: 11 },
  suggTitle: { fontFamily: "Inter_700Bold", fontSize: 14 },
  suggDesc: { fontFamily: "Inter_400Regular", fontSize: 13, lineHeight: 18 },
  impactRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 8,
    borderWidth: 1,
  },
  impactText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  swapRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    paddingTop: 10,
    borderTopWidth: 1,
  },
  addRow: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    padding: 10,
    borderRadius: 10,
    borderWidth: 1,
  },
  swapItem: { flex: 1 },
  swapLabel: { fontFamily: "Inter_400Regular", fontSize: 10, marginBottom: 2 },
  swapName: { fontFamily: "Inter_600SemiBold", fontSize: 13, marginBottom: 2 },
  swapPrice: { fontFamily: "Inter_500Medium", fontSize: 12 },
  swapArrow: {
    width: 28,
    height: 28,
    borderRadius: 8,
    alignItems: "center",
    justifyContent: "center",
  },
  swapBtn: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    paddingVertical: 8,
    paddingHorizontal: 14,
    borderRadius: 10,
    borderWidth: 1,
    alignSelf: "flex-start",
  },
  swapBtnText: { fontFamily: "Inter_600SemiBold", fontSize: 13 },
  browseBtn: {
    flexDirection: "row",
    alignItems: "center",
    gap: 4,
    paddingVertical: 7,
    paddingHorizontal: 12,
    borderRadius: 10,
  },
  browseBtnText: { fontFamily: "Inter_600SemiBold", fontSize: 13, color: "#fff" },
  buildRow: { flexDirection: "row", alignItems: "center", gap: 10 },
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
  issueItem: { borderRadius: 12, borderWidth: 1, padding: 12, gap: 6 },
  issueTitle: { fontFamily: "Inter_700Bold", fontSize: 14 },
  issueDesc: { fontFamily: "Inter_400Regular", fontSize: 13, lineHeight: 18 },
  issueComponents: { flexDirection: "row", flexWrap: "wrap", gap: 6 },
  compTag: { paddingHorizontal: 8, paddingVertical: 3, borderRadius: 6 },
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
