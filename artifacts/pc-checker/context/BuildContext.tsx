import AsyncStorage from "@react-native-async-storage/async-storage";
import React, { createContext, useCallback, useContext, useEffect, useState } from "react";
import { BuildComponents } from "../utils/compatibility";
import { ComponentType, PCComponent } from "../data/components";

interface BuildContextValue {
  build: BuildComponents;
  addComponent: (component: PCComponent) => void;
  removeComponent: (type: ComponentType) => void;
  clearBuild: () => void;
  totalPrice: number;
  componentCount: number;
}

const BuildContext = createContext<BuildContextValue | null>(null);

const STORAGE_KEY = "pc_build_v1";

export function BuildProvider({ children }: { children: React.ReactNode }) {
  const [build, setBuild] = useState<BuildComponents>({});

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_KEY)
      .then((stored) => {
        if (stored) {
          setBuild(JSON.parse(stored));
        }
      })
      .catch(() => {});
  }, []);

  const persist = useCallback((newBuild: BuildComponents) => {
    AsyncStorage.setItem(STORAGE_KEY, JSON.stringify(newBuild)).catch(() => {});
  }, []);

  const addComponent = useCallback(
    (component: PCComponent) => {
      setBuild((prev) => {
        const next = { ...prev, [component.type]: component };
        persist(next);
        return next;
      });
    },
    [persist]
  );

  const removeComponent = useCallback(
    (type: ComponentType) => {
      setBuild((prev) => {
        const next = { ...prev };
        delete next[type];
        persist(next);
        return next;
      });
    },
    [persist]
  );

  const clearBuild = useCallback(() => {
    setBuild({});
    AsyncStorage.removeItem(STORAGE_KEY).catch(() => {});
  }, []);

  const totalPrice = Object.values(build).reduce((sum, c) => sum + (c?.price ?? 0), 0);
  const componentCount = Object.values(build).filter(Boolean).length;

  return (
    <BuildContext.Provider value={{ build, addComponent, removeComponent, clearBuild, totalPrice, componentCount }}>
      {children}
    </BuildContext.Provider>
  );
}

export function useBuild() {
  const ctx = useContext(BuildContext);
  if (!ctx) throw new Error("useBuild must be used within BuildProvider");
  return ctx;
}
