import React from "react";
import { StyleSheet, Text, View } from "react-native";
import { useColors } from "@/hooks/useColors";

interface Props {
  score: number;
  size?: number;
  label?: string;
}

export function ScoreRing({ score, size = 80, label }: Props) {
  const colors = useColors();

  const getColor = () => {
    if (score >= 80) return colors.success;
    if (score >= 50) return colors.warning;
    return colors.destructive;
  };

  const color = getColor();
  const strokeWidth = size * 0.1;
  const radius = (size - strokeWidth) / 2;
  const circumference = 2 * Math.PI * radius;
  const filled = (score / 100) * circumference;

  return (
    <View style={styles.container}>
      <View
        style={[
          styles.ring,
          {
            width: size,
            height: size,
            borderRadius: size / 2,
            borderWidth: strokeWidth,
            borderColor: color + "30",
          },
        ]}
      >
        <View
          style={[
            styles.fill,
            {
              width: size - strokeWidth * 2,
              height: size - strokeWidth * 2,
              borderRadius: (size - strokeWidth * 2) / 2,
              borderWidth: strokeWidth,
              borderColor: color,
              transform: [{ rotate: "-90deg" }],
            },
          ]}
        />
        <View style={[StyleSheet.absoluteFill, styles.center]}>
          <Text style={[styles.score, { color, fontSize: size * 0.28 }]}>
            {score}
          </Text>
        </View>
      </View>
      {label && (
        <Text style={[styles.label, { color: colors.mutedForeground, marginTop: 6 }]}>
          {label}
        </Text>
      )}
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    alignItems: "center",
  },
  ring: {
    alignItems: "center",
    justifyContent: "center",
    position: "relative",
  },
  fill: {
    position: "absolute",
  },
  center: {
    alignItems: "center",
    justifyContent: "center",
  },
  score: {
    fontFamily: "Inter_700Bold",
  },
  label: {
    fontFamily: "Inter_500Medium",
    fontSize: 11,
  },
});
