import { Feather } from "@expo/vector-icons";
import * as Haptics from "expo-haptics";
import { router } from "expo-router";
import React, { useMemo } from "react";
import {
  Alert,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  View,
} from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { ComponentIcon } from "@/components/ComponentIcon";
import { StatusBadge } from "@/components/StatusBadge";
import { generateSuggestions } from "@/utils/suggestions";
import {
  COMPONENT_LABELS,
  COMPONENT_ORDER,
  ComponentType,
  USE_CASE_CONFIG,
  UseCase,
} from "@/data/components";
import { useColors } from "@/hooks/useColors";
import { useBuild } from "@/context/BuildContext";
import { checkCompatibility } from "@/utils/compatibility";

const USE_CASES: UseCase[] = ["gaming", "general", "productivity"];

export default function BuildScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { build, useCase, setUseCase, removeComponent, clearBuild, totalPrice, componentCount } =
    useBuild();

  const compatibility = useMemo(() => checkCompatibility(build), [build]);
  const suggestions = useMemo(() => generateSuggestions(build, useCase), [build, useCase]);

  const topPad = Platform.OS === "web" ? Math.max(insets.top, 67) : insets.top;
  const bottomPad = Platform.OS === "web" ? 34 : 0;

  const handleAddComponent = (type: ComponentType) => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Light);
    router.push({ pathname: "/browse", params: { type } });
  };

  const handleRemoveComponent = (type: ComponentType) => {
    Alert.alert("Remove Component", `Remove ${COMPONENT_LABELS[type]}?`, [
      { text: "Cancel", style: "cancel" },
      {
        text: "Remove",
        style: "destructive",
        onPress: () => {
          Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
          removeComponent(type);
        },
      },
    ]);
  };

  const handleClearBuild = () => {
    Alert.alert("Clear Build", "Remove all components and reset your use case?", [
      { text: "Cancel", style: "cancel" },
      {
        text: "Clear All",
        style: "destructive",
        onPress: () => {
          Haptics.notificationAsync(Haptics.NotificationFeedbackType.Warning);
          clearBuild();
        },
      },
    ]);
  };

  const handleSelectUseCase = (uc: UseCase) => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    setUseCase(uc);
  };

  const getStatusColor = () => {
    switch (compatibility.status) {
      case "compatible": return colors.success;
      case "partial": return colors.warning;
      case "incompatible": return colors.destructive;
      default: return colors.mutedForeground;
    }
  };

  return (
    <View style={[styles.container, { backgroundColor: colors.background }]}>
      <View
        style={[
          styles.header,
          { paddingTop: topPad + 16, backgroundColor: colors.background, borderBottomColor: colors.border },
        ]}
      >
        <View>
          <Text style={[styles.headerTitle, { color: colors.foreground }]}>My Build</Text>
          <Text style={[styles.headerSub, { color: colors.mutedForeground }]}>
            {componentCount} of 7 components
          </Text>
        </View>
        <View style={styles.headerRight}>
          {(componentCount > 0 || useCase) && (
            <Pressable onPress={handleClearBuild} style={styles.clearBtn}>
              <Feather name="trash-2" size={18} color={colors.mutedForeground} />
            </Pressable>
          )}
          <Pressable
            onPress={() => router.push("/results")}
            style={[styles.checkBtn, { backgroundColor: getStatusColor() }]}
          >
            <Text style={[styles.checkBtnText, { color: "#fff" }]}>Check</Text>
            <Feather name="check-circle" size={15} color="#fff" />
          </Pressable>
        </View>
      </View>

      <ScrollView
        style={styles.scroll}
        contentContainerStyle={[styles.scrollContent, { paddingBottom: bottomPad + 100 }]}
        showsVerticalScrollIndicator={false}
      >
        {/* Use Case Selector */}
        <View style={styles.useCaseSection}>
          <Text style={[styles.sectionLabel, { color: colors.mutedForeground }]}>USE CASE</Text>
          <View style={styles.useCaseRow}>
            {USE_CASES.map((uc) => {
              const config = USE_CASE_CONFIG[uc];
              const isSelected = useCase === uc;
              return (
                <Pressable
                  key={uc}
                  onPress={() => handleSelectUseCase(uc)}
                  style={({ pressed }) => [
                    styles.useCaseCard,
                    {
                      backgroundColor: isSelected ? config.color + "18" : colors.card,
                      borderColor: isSelected ? config.color : colors.border,
                      borderWidth: isSelected ? 1.5 : 1,
                      opacity: pressed ? 0.8 : 1,
                    },
                  ]}
                >
                  <View
                    style={[
                      styles.useCaseIconWrap,
                      { backgroundColor: isSelected ? config.color + "25" : colors.muted },
                    ]}
                  >
                    <Feather
                      name={config.icon as any}
                      size={18}
                      color={isSelected ? config.color : colors.mutedForeground}
                    />
                  </View>
                  <Text
                    style={[
                      styles.useCaseLabel,
                      { color: isSelected ? config.color : colors.foreground },
                    ]}
                  >
                    {config.label}
                  </Text>
                  <Text
                    style={[styles.useCaseDesc, { color: colors.mutedForeground }]}
                    numberOfLines={2}
                  >
                    {config.description}
                  </Text>
                  {isSelected && (
                    <View style={[styles.useCaseCheck, { backgroundColor: config.color }]}>
                      <Feather name="check" size={10} color="#fff" />
                    </View>
                  )}
                </Pressable>
              );
            })}
          </View>
          {!useCase && (
            <Text style={[styles.useCaseHint, { color: colors.mutedForeground }]}>
              Select a use case to filter recommended parts
            </Text>
          )}
        </View>

        {componentCount > 0 && (
          <View style={[styles.summaryCard, { backgroundColor: colors.card, borderColor: colors.border }]}>
            <View style={styles.summaryRow}>
              <View style={styles.summaryItem}>
                <Text style={[styles.summaryValue, { color: colors.foreground }]}>
                  ${totalPrice.toLocaleString()}
                </Text>
                <Text style={[styles.summaryLabel, { color: colors.mutedForeground }]}>Total Cost</Text>
              </View>
              <View style={[styles.summaryDivider, { backgroundColor: colors.border }]} />
              <View style={styles.summaryItem}>
                <Text style={[styles.summaryValue, { color: colors.foreground }]}>
                  ~{compatibility.estimatedPowerDraw}W
                </Text>
                <Text style={[styles.summaryLabel, { color: colors.mutedForeground }]}>Est. Power</Text>
              </View>
              <View style={[styles.summaryDivider, { backgroundColor: colors.border }]} />
              <View style={styles.summaryItem}>
                <StatusBadge status={compatibility.status} size="sm" />
                <Text style={[styles.summaryLabel, { color: colors.mutedForeground }]}>Status</Text>
              </View>
            </View>
          </View>
        )}

        <Text style={[styles.sectionLabel, { color: colors.mutedForeground }]}>COMPONENTS</Text>

        {COMPONENT_ORDER.map((type) => {
          const component = build[type];
          return (
            <View
              key={type}
              style={[
                styles.componentRow,
                { backgroundColor: colors.card, borderColor: colors.border },
              ]}
            >
              <View style={[styles.iconWrap, { backgroundColor: colors.accent }]}>
                <ComponentIcon type={type} size={20} color={colors.primary} />
              </View>
              <View style={styles.componentInfo}>
                <Text style={[styles.componentType, { color: colors.mutedForeground }]}>
                  {COMPONENT_LABELS[type]}
                </Text>
                {component ? (
                  <Text
                    style={[styles.componentName, { color: colors.foreground }]}
                    numberOfLines={1}
                  >
                    {component.name}
                  </Text>
                ) : (
                  <Text style={[styles.componentEmpty, { color: colors.mutedForeground }]}>
                    Not selected
                  </Text>
                )}
              </View>
              {component ? (
                <View style={styles.componentActions}>
                  <Text style={[styles.componentPrice, { color: colors.foreground }]}>
                    ${component.price}
                  </Text>
                  <Pressable
                    onPress={() => handleAddComponent(type)}
                    hitSlop={8}
                    style={[styles.changeBtn, { borderColor: colors.border }]}
                  >
                    <Feather name="refresh-cw" size={13} color={colors.mutedForeground} />
                  </Pressable>
                  <Pressable onPress={() => handleRemoveComponent(type)} hitSlop={8}>
                    <Feather name="x" size={16} color={colors.mutedForeground} />
                  </Pressable>
                </View>
              ) : (
                <Pressable
                  onPress={() => handleAddComponent(type)}
                  style={[
                    styles.addBtn,
                    {
                      backgroundColor: colors.primary + "18",
                      borderColor: colors.primary + "30",
                    },
                  ]}
                >
                  <Feather name="plus" size={14} color={colors.primary} />
                  <Text style={[styles.addBtnText, { color: colors.primary }]}>Add</Text>
                </Pressable>
              )}
            </View>
          );
        })}

        {suggestions.length > 0 && compatibility.issues.length === 0 && (
          <Pressable
            onPress={() => router.push("/results")}
            style={[styles.suggTeaser, { backgroundColor: colors.primary + "10", borderColor: colors.primary + "28" }]}
          >
            <View style={[styles.suggTeaserIcon, { backgroundColor: colors.primary + "20" }]}>
              <Feather name="zap" size={16} color={colors.primary} />
            </View>
            <View style={styles.suggTeaserText}>
              <Text style={[styles.suggTeaserTitle, { color: colors.primary }]}>
                {suggestions.length} optimization suggestion{suggestions.length !== 1 ? "s" : ""} available
              </Text>
              <Text style={[styles.suggTeaserDesc, { color: colors.mutedForeground }]}>
                {suggestions[0].title}
              </Text>
            </View>
            <Feather name="chevron-right" size={16} color={colors.primary} />
          </Pressable>
        )}

        {compatibility.issues.length > 0 && (
          <>
            <Text
              style={[styles.sectionLabel, { color: colors.mutedForeground, marginTop: 20 }]}
            >
              ISSUES DETECTED
            </Text>
            {compatibility.issues.slice(0, 3).map((issue, i) => (
              <View
                key={i}
                style={[
                  styles.issueCard,
                  {
                    backgroundColor:
                      issue.severity === "error"
                        ? colors.destructive + "10"
                        : colors.warning + "10",
                    borderColor:
                      issue.severity === "error"
                        ? colors.destructive + "30"
                        : colors.warning + "30",
                  },
                ]}
              >
                <Feather
                  name={issue.severity === "error" ? "x-circle" : "alert-triangle"}
                  size={16}
                  color={
                    issue.severity === "error" ? colors.destructive : colors.warning
                  }
                  style={{ marginTop: 1 }}
                />
                <View style={styles.issueContent}>
                  <Text
                    style={[
                      styles.issueTitle,
                      {
                        color:
                          issue.severity === "error" ? colors.destructive : colors.warning,
                      },
                    ]}
                  >
                    {issue.title}
                  </Text>
                  <Text style={[styles.issueDesc, { color: colors.mutedForeground }]}>
                    {issue.description}
                  </Text>
                </View>
              </View>
            ))}
            {compatibility.issues.length > 3 && (
              <Pressable onPress={() => router.push("/results")}>
                <Text style={[styles.seeAll, { color: colors.primary }]}>
                  + {compatibility.issues.length - 3} more issues — view all
                </Text>
              </Pressable>
            )}
          </>
        )}

        {componentCount === 0 && useCase && (
          <View style={[styles.emptyState, { borderColor: colors.border }]}>
            <Feather name="cpu" size={40} color={colors.mutedForeground} />
            <Text style={[styles.emptyTitle, { color: colors.foreground }]}>
              Start adding parts
            </Text>
            <Text style={[styles.emptyDesc, { color: colors.mutedForeground }]}>
              Tap "Add" on any component above. Parts are filtered for{" "}
              {USE_CASE_CONFIG[useCase].label.toLowerCase()} use.
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
    alignItems: "flex-end",
    justifyContent: "space-between",
    paddingHorizontal: 20,
    paddingBottom: 14,
    borderBottomWidth: StyleSheet.hairlineWidth,
  },
  headerTitle: { fontFamily: "Inter_700Bold", fontSize: 28 },
  headerSub: { fontFamily: "Inter_400Regular", fontSize: 13, marginTop: 2 },
  headerRight: { flexDirection: "row", alignItems: "center", gap: 10 },
  clearBtn: { padding: 8 },
  checkBtn: {
    flexDirection: "row",
    alignItems: "center",
    gap: 6,
    paddingHorizontal: 14,
    paddingVertical: 8,
    borderRadius: 20,
  },
  checkBtnText: { fontFamily: "Inter_600SemiBold", fontSize: 14 },
  scroll: { flex: 1 },
  scrollContent: { padding: 20, gap: 8 },
  useCaseSection: { gap: 10, marginBottom: 4 },
  useCaseRow: { flexDirection: "row", gap: 8 },
  useCaseCard: {
    flex: 1,
    borderRadius: 16,
    padding: 12,
    gap: 6,
    position: "relative",
  },
  useCaseIconWrap: {
    width: 34,
    height: 34,
    borderRadius: 10,
    alignItems: "center",
    justifyContent: "center",
    marginBottom: 2,
  },
  useCaseLabel: { fontFamily: "Inter_700Bold", fontSize: 13 },
  useCaseDesc: { fontFamily: "Inter_400Regular", fontSize: 11, lineHeight: 15 },
  useCaseCheck: {
    position: "absolute",
    top: 8,
    right: 8,
    width: 18,
    height: 18,
    borderRadius: 9,
    alignItems: "center",
    justifyContent: "center",
  },
  useCaseHint: {
    fontFamily: "Inter_400Regular",
    fontSize: 12,
    textAlign: "center",
    fontStyle: "italic",
  },
  summaryCard: {
    borderRadius: 16,
    borderWidth: 1,
    padding: 16,
    marginBottom: 4,
  },
  summaryRow: { flexDirection: "row", alignItems: "center" },
  summaryItem: { flex: 1, alignItems: "center", gap: 4 },
  summaryDivider: { width: 1, height: 40 },
  summaryValue: { fontFamily: "Inter_700Bold", fontSize: 18 },
  summaryLabel: { fontFamily: "Inter_400Regular", fontSize: 11 },
  sectionLabel: {
    fontFamily: "Inter_600SemiBold",
    fontSize: 11,
    letterSpacing: 1.2,
    marginBottom: 2,
    marginTop: 4,
  },
  componentRow: {
    flexDirection: "row",
    alignItems: "center",
    borderRadius: 14,
    borderWidth: 1,
    padding: 14,
    gap: 12,
  },
  iconWrap: {
    width: 40,
    height: 40,
    borderRadius: 12,
    alignItems: "center",
    justifyContent: "center",
  },
  componentInfo: { flex: 1 },
  componentType: { fontFamily: "Inter_500Medium", fontSize: 11, marginBottom: 2 },
  componentName: { fontFamily: "Inter_600SemiBold", fontSize: 14 },
  componentEmpty: { fontFamily: "Inter_400Regular", fontSize: 13, fontStyle: "italic" },
  componentActions: { flexDirection: "row", alignItems: "center", gap: 8 },
  componentPrice: { fontFamily: "Inter_600SemiBold", fontSize: 14 },
  changeBtn: { padding: 6, borderRadius: 8, borderWidth: 1 },
  addBtn: {
    flexDirection: "row",
    alignItems: "center",
    gap: 4,
    paddingHorizontal: 10,
    paddingVertical: 7,
    borderRadius: 10,
    borderWidth: 1,
  },
  addBtnText: { fontFamily: "Inter_600SemiBold", fontSize: 13 },
  issueCard: {
    flexDirection: "row",
    gap: 10,
    borderRadius: 12,
    borderWidth: 1,
    padding: 12,
  },
  issueContent: { flex: 1 },
  issueTitle: { fontFamily: "Inter_600SemiBold", fontSize: 13, marginBottom: 2 },
  issueDesc: { fontFamily: "Inter_400Regular", fontSize: 12, lineHeight: 17 },
  seeAll: {
    fontFamily: "Inter_500Medium",
    fontSize: 13,
    marginTop: 4,
    textAlign: "center",
  },
  emptyState: {
    alignItems: "center",
    padding: 36,
    marginTop: 8,
    borderRadius: 20,
    borderWidth: 1,
    borderStyle: "dashed",
    gap: 10,
  },
  emptyTitle: { fontFamily: "Inter_700Bold", fontSize: 18 },
  emptyDesc: {
    fontFamily: "Inter_400Regular",
    fontSize: 13,
    textAlign: "center",
    lineHeight: 19,
  },
  suggTeaser: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    borderRadius: 14,
    borderWidth: 1,
    padding: 12,
  },
  suggTeaserIcon: {
    width: 34,
    height: 34,
    borderRadius: 10,
    alignItems: "center",
    justifyContent: "center",
  },
  suggTeaserText: { flex: 1 },
  suggTeaserTitle: { fontFamily: "Inter_600SemiBold", fontSize: 13 },
  suggTeaserDesc: { fontFamily: "Inter_400Regular", fontSize: 12, marginTop: 1 },
});
