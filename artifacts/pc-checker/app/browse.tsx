import { Feather } from "@expo/vector-icons";
import * as Haptics from "expo-haptics";
import { router, useLocalSearchParams } from "expo-router";
import React, { useMemo, useState } from "react";
import {
  FlatList,
  Platform,
  Pressable,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { ComponentIcon } from "@/components/ComponentIcon";
import { ALL_COMPONENTS, COMPONENT_LABELS, ComponentType } from "@/data/components";
import { useColors } from "@/hooks/useColors";
import { useBuild } from "@/context/BuildContext";

export default function BrowseScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { type } = useLocalSearchParams<{ type: ComponentType }>();
  const { addComponent, build } = useBuild();
  const [search, setSearch] = useState("");

  const components = useMemo(() => {
    const filtered = ALL_COMPONENTS.filter((c) => c.type === type);
    if (!search) return filtered;
    const q = search.toLowerCase();
    return filtered.filter(
      (c) =>
        c.name.toLowerCase().includes(q) ||
        c.brand.toLowerCase().includes(q)
    );
  }, [type, search]);

  const currentComponent = build[type as ComponentType];
  const topPad = Platform.OS === "web" ? Math.max(insets.top, 67) : insets.top;
  const bottomPad = Platform.OS === "web" ? 34 : 0;

  const handleSelect = (component: typeof components[0]) => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    addComponent(component);
    router.back();
  };

  const getSpecSummary = (component: typeof components[0]) => {
    switch (component.type) {
      case "cpu":
        return `${component.specs["cores"]} cores · ${component.specs["socket"]} · ${component.specs["memType"]}`;
      case "gpu":
        return `${component.specs["vram"]}GB VRAM · ${component.specs["tdp"]}W TDP`;
      case "ram":
        return `${component.specs["capacity"]}GB ${component.specs["type"]} ${component.specs["speed"]}MHz`;
      case "motherboard":
        return `${component.specs["socket"]} · ${component.specs["chipset"]} · ${component.specs["formFactor"]}`;
      case "storage":
        return `${(component.specs["capacity"] as number) >= 1000 ? (component.specs["capacity"] as number) / 1000 + "TB" : component.specs["capacity"] + "GB"} ${component.specs["type"]}`;
      case "psu":
        return `${component.specs["wattage"]}W · ${component.specs["efficiency"]} · ${component.specs["modular"]} Modular`;
      case "case":
        return `${component.specs["formFactor"]} · GPU up to ${component.specs["maxGpuLength"]}mm`;
      default:
        return "";
    }
  };

  const getScoreColor = (score: number) => {
    if (score >= 80) return colors.success;
    if (score >= 60) return colors.warning;
    return colors.destructive;
  };

  return (
    <View style={[styles.container, { backgroundColor: colors.background }]}>
      <View style={[styles.header, { paddingTop: topPad + 12, backgroundColor: colors.background, borderBottomColor: colors.border }]}>
        <Pressable onPress={() => router.back()} style={styles.backBtn} hitSlop={8}>
          <Feather name="arrow-left" size={22} color={colors.foreground} />
        </Pressable>
        <View style={styles.headerCenter}>
          {type && <ComponentIcon type={type} size={18} color={colors.primary} />}
          <Text style={[styles.headerTitle, { color: colors.foreground }]}>
            {type ? COMPONENT_LABELS[type] : "Browse"}
          </Text>
        </View>
        <View style={{ width: 38 }} />
      </View>

      <View style={[styles.searchWrap, { borderColor: colors.border, backgroundColor: colors.card }]}>
        <Feather name="search" size={16} color={colors.mutedForeground} />
        <TextInput
          style={[styles.searchInput, { color: colors.foreground }]}
          placeholder="Search components..."
          placeholderTextColor={colors.mutedForeground}
          value={search}
          onChangeText={setSearch}
          autoCorrect={false}
        />
        {search.length > 0 && (
          <Pressable onPress={() => setSearch("")} hitSlop={8}>
            <Feather name="x" size={16} color={colors.mutedForeground} />
          </Pressable>
        )}
      </View>

      <FlatList
        data={components}
        keyExtractor={(item) => item.id}
        contentContainerStyle={[styles.list, { paddingBottom: bottomPad + 30 }]}
        showsVerticalScrollIndicator={false}
        renderItem={({ item }) => {
          const isSelected = currentComponent?.id === item.id;
          return (
            <Pressable
              onPress={() => handleSelect(item)}
              style={({ pressed }) => [
                styles.card,
                {
                  backgroundColor: isSelected ? colors.primary + "15" : colors.card,
                  borderColor: isSelected ? colors.primary : colors.border,
                  opacity: pressed ? 0.85 : 1,
                },
              ]}
            >
              <View style={styles.cardHeader}>
                <View style={styles.cardLeft}>
                  <Text style={[styles.brand, { color: colors.mutedForeground }]}>{item.brand}</Text>
                  <Text style={[styles.name, { color: colors.foreground }]}>{item.name}</Text>
                  <Text style={[styles.specs, { color: colors.mutedForeground }]}>{getSpecSummary(item)}</Text>
                </View>
                <View style={styles.cardRight}>
                  <View style={[styles.scoreWrap, { backgroundColor: getScoreColor(item.performanceScore) + "20" }]}>
                    <Text style={[styles.scoreText, { color: getScoreColor(item.performanceScore) }]}>
                      {item.performanceScore}
                    </Text>
                  </View>
                  <Text style={[styles.price, { color: colors.foreground }]}>${item.price}</Text>
                </View>
              </View>
              {isSelected && (
                <View style={[styles.selectedBadge, { backgroundColor: colors.primary }]}>
                  <Feather name="check" size={11} color="#fff" />
                  <Text style={styles.selectedText}>Selected</Text>
                </View>
              )}
            </Pressable>
          );
        }}
        ListEmptyComponent={
          <View style={styles.emptyState}>
            <Feather name="search" size={32} color={colors.mutedForeground} />
            <Text style={[styles.emptyText, { color: colors.mutedForeground }]}>No components found</Text>
          </View>
        }
      />
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
  backBtn: { width: 38, alignItems: "flex-start" },
  headerCenter: {
    flex: 1,
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "center",
    gap: 8,
  },
  headerTitle: { fontFamily: "Inter_700Bold", fontSize: 18 },
  searchWrap: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    margin: 16,
    borderRadius: 14,
    borderWidth: 1,
    paddingHorizontal: 14,
    paddingVertical: 11,
  },
  searchInput: {
    flex: 1,
    fontFamily: "Inter_400Regular",
    fontSize: 15,
    padding: 0,
  },
  list: { paddingHorizontal: 16, gap: 10 },
  card: {
    borderRadius: 16,
    borderWidth: 1.5,
    padding: 16,
  },
  cardHeader: { flexDirection: "row", gap: 12 },
  cardLeft: { flex: 1 },
  cardRight: { alignItems: "flex-end", gap: 6 },
  brand: { fontFamily: "Inter_500Medium", fontSize: 11, marginBottom: 2 },
  name: { fontFamily: "Inter_600SemiBold", fontSize: 15, marginBottom: 4 },
  specs: { fontFamily: "Inter_400Regular", fontSize: 12, lineHeight: 17 },
  scoreWrap: {
    paddingHorizontal: 10,
    paddingVertical: 4,
    borderRadius: 8,
  },
  scoreText: { fontFamily: "Inter_700Bold", fontSize: 14 },
  price: { fontFamily: "Inter_700Bold", fontSize: 16 },
  selectedBadge: {
    flexDirection: "row",
    alignItems: "center",
    gap: 4,
    alignSelf: "flex-start",
    marginTop: 10,
    paddingHorizontal: 8,
    paddingVertical: 4,
    borderRadius: 8,
  },
  selectedText: { fontFamily: "Inter_600SemiBold", fontSize: 11, color: "#fff" },
  emptyState: { alignItems: "center", padding: 40, gap: 12 },
  emptyText: { fontFamily: "Inter_500Medium", fontSize: 15 },
});
