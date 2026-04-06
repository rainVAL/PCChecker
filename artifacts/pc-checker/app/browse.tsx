import { Feather } from "@expo/vector-icons";
import * as Haptics from "expo-haptics";
import { router, useLocalSearchParams } from "expo-router";
import React, { useMemo, useState } from "react";
import {
  FlatList,
  Platform,
  Pressable,
  ScrollView,
  StyleSheet,
  Text,
  TextInput,
  View,
} from "react-native";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { ComponentIcon } from "@/components/ComponentIcon";
import {
  ALL_COMPONENTS,
  COMPONENT_LABELS,
  ComponentType,
  USE_CASE_CONFIG,
} from "@/data/components";
import { useColors } from "@/hooks/useColors";
import { useBuild } from "@/context/BuildContext";

type SortKey = "performance" | "price-asc" | "price-desc";
type PriceTier = "all" | "budget" | "mid" | "high";

const SORT_OPTIONS: { key: SortKey; label: string; icon: string }[] = [
  { key: "performance", label: "Performance", icon: "zap" },
  { key: "price-asc", label: "Price ↑", icon: "trending-up" },
  { key: "price-desc", label: "Price ↓", icon: "trending-down" },
];

const PRICE_TIERS: { key: PriceTier; label: string; range: string }[] = [
  { key: "all", label: "All", range: "" },
  { key: "budget", label: "Budget", range: "< $150" },
  { key: "mid", label: "Mid", range: "$150–$400" },
  { key: "high", label: "High-end", range: "> $400" },
];

function matchesTier(price: number, tier: PriceTier) {
  if (tier === "all") return true;
  if (tier === "budget") return price < 150;
  if (tier === "mid") return price >= 150 && price <= 400;
  return price > 400;
}

export default function BrowseScreen() {
  const colors = useColors();
  const insets = useSafeAreaInsets();
  const { type } = useLocalSearchParams<{ type: ComponentType }>();
  const { addComponent, build, useCase } = useBuild();

  const [search, setSearch] = useState("");
  const [showAll, setShowAll] = useState(false);
  const [sort, setSort] = useState<SortKey>("performance");
  const [priceTier, setPriceTier] = useState<PriceTier>("all");
  const [selectedBrand, setSelectedBrand] = useState<string | null>(null);
  const [showFilters, setShowFilters] = useState(false);

  const allForType = useMemo(
    () => ALL_COMPONENTS.filter((c) => c.type === type),
    [type]
  );

  const brands = useMemo(
    () => [...new Set(allForType.map((c) => c.brand))].sort(),
    [allForType]
  );

  const filteredByCategory = useMemo(() => {
    if (!useCase || showAll) return allForType;
    return allForType.filter((c) => c.categories.includes(useCase));
  }, [allForType, useCase, showAll]);

  const components = useMemo(() => {
    let list = filteredByCategory;

    if (search) {
      const q = search.toLowerCase();
      list = list.filter(
        (c) =>
          c.name.toLowerCase().includes(q) ||
          c.brand.toLowerCase().includes(q) ||
          Object.values(c.specs).some((v) =>
            String(v).toLowerCase().includes(q)
          )
      );
    }

    if (selectedBrand) {
      list = list.filter((c) => c.brand === selectedBrand);
    }

    if (priceTier !== "all") {
      list = list.filter((c) => matchesTier(c.price, priceTier));
    }

    return [...list].sort((a, b) => {
      if (sort === "price-asc") return a.price - b.price;
      if (sort === "price-desc") return b.price - a.price;
      return b.performanceScore - a.performanceScore;
    });
  }, [filteredByCategory, search, selectedBrand, priceTier, sort]);

  const hiddenCount = allForType.length - filteredByCategory.length;
  const currentComponent = build[type as ComponentType];
  const topPad = Platform.OS === "web" ? Math.max(insets.top, 67) : insets.top;
  const bottomPad = Platform.OS === "web" ? 34 : 0;

  const useCaseConfig = useCase ? USE_CASE_CONFIG[useCase] : null;

  const activeFilterCount =
    (priceTier !== "all" ? 1 : 0) + (selectedBrand ? 1 : 0);

  const handleSelect = (component: (typeof components)[0]) => {
    Haptics.impactAsync(Haptics.ImpactFeedbackStyle.Medium);
    addComponent(component);
    router.back();
  };

  const clearFilters = () => {
    setPriceTier("all");
    setSelectedBrand(null);
    setSearch("");
  };

  const getSpecSummary = (component: (typeof components)[0]) => {
    switch (component.type) {
      case "cpu":
        return `${component.specs["cores"]} cores · ${component.specs["socket"]} · ${component.specs["memType"]}`;
      case "gpu":
        return `${component.specs["vram"]}GB VRAM · ${component.specs["tdp"]}W TDP`;
      case "ram":
        return `${component.specs["capacity"]}GB ${component.specs["type"]} ${component.specs["speed"]}MHz`;
      case "motherboard":
        return `${component.specs["socket"]} · ${component.specs["chipset"]} · ${component.specs["formFactor"]}`;
      case "storage": {
        const cap = component.specs["capacity"] as number;
        return `${cap >= 1000 ? cap / 1000 + "TB" : cap + "GB"} ${component.specs["type"]}`;
      }
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
      {/* Header */}
      <View
        style={[
          styles.header,
          {
            paddingTop: topPad + 12,
            backgroundColor: colors.background,
            borderBottomColor: colors.border,
          },
        ]}
      >
        <Pressable onPress={() => router.back()} style={styles.backBtn} hitSlop={8}>
          <Feather name="arrow-left" size={22} color={colors.foreground} />
        </Pressable>
        <View style={styles.headerCenter}>
          {type && <ComponentIcon type={type} size={18} color={colors.primary} />}
          <Text style={[styles.headerTitle, { color: colors.foreground }]}>
            {type ? COMPONENT_LABELS[type] : "Browse"}
          </Text>
        </View>
        <Pressable
          onPress={() => setShowFilters((v) => !v)}
          hitSlop={8}
          style={[
            styles.filterToggle,
            {
              backgroundColor: showFilters || activeFilterCount > 0
                ? colors.primary + "18"
                : colors.muted,
              borderColor: showFilters || activeFilterCount > 0
                ? colors.primary + "40"
                : colors.border,
            },
          ]}
        >
          <Feather
            name="sliders"
            size={15}
            color={showFilters || activeFilterCount > 0 ? colors.primary : colors.mutedForeground}
          />
          {activeFilterCount > 0 && (
            <View style={[styles.filterBadge, { backgroundColor: colors.primary }]}>
              <Text style={styles.filterBadgeText}>{activeFilterCount}</Text>
            </View>
          )}
        </Pressable>
      </View>

      {/* Search bar */}
      <View style={[styles.searchWrap, { borderColor: colors.border, backgroundColor: colors.card }]}>
        <Feather name="search" size={16} color={colors.mutedForeground} />
        <TextInput
          style={[styles.searchInput, { color: colors.foreground }]}
          placeholder={`Search ${type ? COMPONENT_LABELS[type].toLowerCase() : "components"}...`}
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

      {/* Filter panel */}
      {showFilters && (
        <View style={[styles.filterPanel, { backgroundColor: colors.card, borderColor: colors.border }]}>
          {/* Sort */}
          <View style={styles.filterRow}>
            <Text style={[styles.filterGroupLabel, { color: colors.mutedForeground }]}>SORT BY</Text>
            <View style={styles.chipRow}>
              {SORT_OPTIONS.map((opt) => {
                const active = sort === opt.key;
                return (
                  <Pressable
                    key={opt.key}
                    onPress={() => setSort(opt.key)}
                    style={[
                      styles.chip,
                      {
                        backgroundColor: active ? colors.primary : colors.muted,
                        borderColor: active ? colors.primary : colors.border,
                      },
                    ]}
                  >
                    <Feather
                      name={opt.icon as any}
                      size={11}
                      color={active ? "#fff" : colors.mutedForeground}
                    />
                    <Text style={[styles.chipText, { color: active ? "#fff" : colors.foreground }]}>
                      {opt.label}
                    </Text>
                  </Pressable>
                );
              })}
            </View>
          </View>

          {/* Price tier */}
          <View style={styles.filterRow}>
            <Text style={[styles.filterGroupLabel, { color: colors.mutedForeground }]}>PRICE TIER</Text>
            <View style={styles.chipRow}>
              {PRICE_TIERS.map((tier) => {
                const active = priceTier === tier.key;
                return (
                  <Pressable
                    key={tier.key}
                    onPress={() => setPriceTier(tier.key)}
                    style={[
                      styles.chip,
                      {
                        backgroundColor: active ? colors.primary : colors.muted,
                        borderColor: active ? colors.primary : colors.border,
                      },
                    ]}
                  >
                    <Text style={[styles.chipText, { color: active ? "#fff" : colors.foreground }]}>
                      {tier.label}
                    </Text>
                    {tier.range && (
                      <Text style={[styles.chipSub, { color: active ? "#ffffff99" : colors.mutedForeground }]}>
                        {tier.range}
                      </Text>
                    )}
                  </Pressable>
                );
              })}
            </View>
          </View>

          {/* Brand */}
          {brands.length > 1 && (
            <View style={styles.filterRow}>
              <Text style={[styles.filterGroupLabel, { color: colors.mutedForeground }]}>BRAND</Text>
              <ScrollView horizontal showsHorizontalScrollIndicator={false}>
                <View style={[styles.chipRow, { flexWrap: "nowrap" }]}>
                  <Pressable
                    onPress={() => setSelectedBrand(null)}
                    style={[
                      styles.chip,
                      {
                        backgroundColor: !selectedBrand ? colors.primary : colors.muted,
                        borderColor: !selectedBrand ? colors.primary : colors.border,
                      },
                    ]}
                  >
                    <Text style={[styles.chipText, { color: !selectedBrand ? "#fff" : colors.foreground }]}>
                      All brands
                    </Text>
                  </Pressable>
                  {brands.map((brand) => {
                    const active = selectedBrand === brand;
                    return (
                      <Pressable
                        key={brand}
                        onPress={() => setSelectedBrand(active ? null : brand)}
                        style={[
                          styles.chip,
                          {
                            backgroundColor: active ? colors.primary : colors.muted,
                            borderColor: active ? colors.primary : colors.border,
                          },
                        ]}
                      >
                        <Text style={[styles.chipText, { color: active ? "#fff" : colors.foreground }]}>
                          {brand}
                        </Text>
                      </Pressable>
                    );
                  })}
                </View>
              </ScrollView>
            </View>
          )}

          {activeFilterCount > 0 && (
            <Pressable onPress={clearFilters} style={styles.clearBtn}>
              <Feather name="x-circle" size={13} color={colors.destructive} />
              <Text style={[styles.clearBtnText, { color: colors.destructive }]}>Clear filters</Text>
            </Pressable>
          )}
        </View>
      )}

      {/* Use case filter banner */}
      {useCaseConfig && !showAll && (
        <View
          style={[
            styles.useCaseBanner,
            {
              backgroundColor: useCaseConfig.color + "12",
              borderColor: useCaseConfig.color + "30",
            },
          ]}
        >
          <View style={[styles.filterDot, { backgroundColor: useCaseConfig.color }]} />
          <Text style={[styles.filterText, { color: useCaseConfig.color }]}>
            Filtered for {useCaseConfig.label}
          </Text>
          {hiddenCount > 0 && (
            <Pressable onPress={() => setShowAll(true)} style={styles.showAllBtn}>
              <Text style={[styles.showAllText, { color: useCaseConfig.color }]}>
                Show all ({hiddenCount} hidden)
              </Text>
            </Pressable>
          )}
        </View>
      )}

      {showAll && (
        <View style={[styles.useCaseBanner, { backgroundColor: colors.muted, borderColor: colors.border }]}>
          <Feather name="list" size={13} color={colors.mutedForeground} />
          <Text style={[styles.filterText, { color: colors.mutedForeground }]}>Showing all parts</Text>
          <Pressable onPress={() => setShowAll(false)} style={styles.showAllBtn}>
            <Text style={[styles.showAllText, { color: colors.primary }]}>
              Back to {useCaseConfig?.label} filter
            </Text>
          </Pressable>
        </View>
      )}

      {/* Results count */}
      <View style={styles.resultsBar}>
        <Text style={[styles.resultsCount, { color: colors.mutedForeground }]}>
          {components.length} result{components.length !== 1 ? "s" : ""}
        </Text>
        {(search || activeFilterCount > 0) && (
          <Pressable onPress={clearFilters} hitSlop={8}>
            <Text style={[styles.resetText, { color: colors.primary }]}>Reset</Text>
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
                  <Text style={[styles.specs, { color: colors.mutedForeground }]}>
                    {getSpecSummary(item)}
                  </Text>
                </View>
                <View style={styles.cardRight}>
                  <View
                    style={[
                      styles.scoreWrap,
                      { backgroundColor: getScoreColor(item.performanceScore) + "20" },
                    ]}
                  >
                    <Text style={[styles.scoreText, { color: getScoreColor(item.performanceScore) }]}>
                      {item.performanceScore}
                    </Text>
                  </View>
                  <Text style={[styles.price, { color: colors.foreground }]}>
                    ${item.price}
                  </Text>
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
            <Text style={[styles.emptyTitle, { color: colors.foreground }]}>No parts found</Text>
            {useCase && !showAll ? (
              <Pressable onPress={() => setShowAll(true)}>
                <Text style={[styles.emptyAction, { color: colors.primary }]}>
                  Show all {type ? COMPONENT_LABELS[type] : ""} parts
                </Text>
              </Pressable>
            ) : activeFilterCount > 0 ? (
              <Pressable onPress={clearFilters}>
                <Text style={[styles.emptyAction, { color: colors.primary }]}>Clear filters</Text>
              </Pressable>
            ) : (
              <Text style={[styles.emptyText, { color: colors.mutedForeground }]}>
                Try a different search term
              </Text>
            )}
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
  filterToggle: {
    width: 36,
    height: 36,
    borderRadius: 10,
    borderWidth: 1,
    alignItems: "center",
    justifyContent: "center",
  },
  filterBadge: {
    position: "absolute",
    top: -5,
    right: -5,
    width: 16,
    height: 16,
    borderRadius: 8,
    alignItems: "center",
    justifyContent: "center",
  },
  filterBadgeText: { fontFamily: "Inter_700Bold", fontSize: 10, color: "#fff" },
  searchWrap: {
    flexDirection: "row",
    alignItems: "center",
    gap: 10,
    margin: 16,
    marginBottom: 0,
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
  filterPanel: {
    margin: 16,
    marginBottom: 0,
    borderRadius: 16,
    borderWidth: 1,
    padding: 14,
    gap: 12,
  },
  filterRow: { gap: 8 },
  filterGroupLabel: { fontFamily: "Inter_600SemiBold", fontSize: 10, letterSpacing: 0.6 },
  chipRow: { flexDirection: "row", flexWrap: "wrap", gap: 7 },
  chip: {
    flexDirection: "row",
    alignItems: "center",
    gap: 4,
    paddingHorizontal: 10,
    paddingVertical: 6,
    borderRadius: 10,
    borderWidth: 1,
  },
  chipText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  chipSub: { fontFamily: "Inter_400Regular", fontSize: 10 },
  clearBtn: { flexDirection: "row", alignItems: "center", gap: 6, alignSelf: "flex-start" },
  clearBtnText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  useCaseBanner: {
    flexDirection: "row",
    alignItems: "center",
    gap: 8,
    marginHorizontal: 16,
    marginTop: 12,
    paddingHorizontal: 12,
    paddingVertical: 8,
    borderRadius: 10,
    borderWidth: 1,
  },
  filterDot: { width: 7, height: 7, borderRadius: 4 },
  filterText: { fontFamily: "Inter_500Medium", fontSize: 13, flex: 1 },
  showAllBtn: { paddingLeft: 4 },
  showAllText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  resultsBar: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    paddingHorizontal: 16,
    paddingTop: 10,
    paddingBottom: 4,
  },
  resultsCount: { fontFamily: "Inter_400Regular", fontSize: 12 },
  resetText: { fontFamily: "Inter_600SemiBold", fontSize: 12 },
  list: { paddingHorizontal: 16, paddingTop: 4, gap: 10 },
  card: { borderRadius: 16, borderWidth: 1.5, padding: 16 },
  cardHeader: { flexDirection: "row", gap: 12 },
  cardLeft: { flex: 1 },
  cardRight: { alignItems: "flex-end", gap: 6 },
  brand: { fontFamily: "Inter_500Medium", fontSize: 11, marginBottom: 2 },
  name: { fontFamily: "Inter_600SemiBold", fontSize: 15, marginBottom: 4 },
  specs: { fontFamily: "Inter_400Regular", fontSize: 12, lineHeight: 17 },
  scoreWrap: { paddingHorizontal: 10, paddingVertical: 4, borderRadius: 8 },
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
  emptyState: { alignItems: "center", padding: 40, gap: 10 },
  emptyTitle: { fontFamily: "Inter_700Bold", fontSize: 18 },
  emptyText: { fontFamily: "Inter_500Medium", fontSize: 14, color: "#888" },
  emptyAction: { fontFamily: "Inter_600SemiBold", fontSize: 14 },
});
