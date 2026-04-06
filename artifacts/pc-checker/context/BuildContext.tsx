import AsyncStorage from "@react-native-async-storage/async-storage";
import React, { createContext, useCallback, useContext, useEffect, useState } from "react";
import { BuildComponents } from "../utils/compatibility";
import { ComponentType, PCComponent, UseCase } from "../data/components";

interface BuildContextValue {
  build: BuildComponents;
  useCase: UseCase | null;
  setUseCase: (uc: UseCase) => void;
  addComponent: (component: PCComponent) => void;
  removeComponent: (type: ComponentType) => void;
  clearBuild: () => void;
  totalPrice: number;
  componentCount: number;
}

const BuildContext = createContext<BuildContextValue | null>(null);

const STORAGE_KEY = "pc_build_v2";

export function BuildProvider({ children }: { children: React.ReactNode }) {
  const [build, setBuild] = useState<BuildComponents>({});
  const [useCase, setUseCaseState] = useState<UseCase | null>(null);

  useEffect(() => {
    AsyncStorage.getItem(STORAGE_KEY)
      .then((stored) => {
        if (stored) {
          const parsed = JSON.parse(stored);
          setBuild(parsed.build ?? {});
          setUseCaseState(parsed.useCase ?? null);
        }
      })
      .catch(() => {});
  }, []);

  const persist = useCallback((newBuild: BuildComponents, newUseCase: UseCase | null) => {
    AsyncStorage.setItem(STORAGE_KEY, JSON.stringify({ build: newBuild, useCase: newUseCase })).catch(() => {});
  }, []);

  const setUseCase = useCallback(
    (uc: UseCase) => {
      setUseCaseState(uc);
      setBuild((prev) => {
        persist(prev, uc);
        return prev;
      });
    },
    [persist]
  );

  const addComponent = useCallback(
    (component: PCComponent) => {
      setBuild((prev) => {
        const next = { ...prev, [component.type]: component };
        setUseCaseState((uc) => {
          persist(next, uc);
          return uc;
        });
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
        setUseCaseState((uc) => {
          persist(next, uc);
          return uc;
        });
        return next;
      });
    },
    [persist]
  );

  const clearBuild = useCallback(() => {
    setBuild({});
    setUseCaseState(null);
    AsyncStorage.removeItem(STORAGE_KEY).catch(() => {});
  }, []);

  const totalPrice = Object.values(build).reduce((sum, c) => sum + (c?.price ?? 0), 0);
  const componentCount = Object.values(build).filter(Boolean).length;

  return (
    <BuildContext.Provider
      value={{ build, useCase, setUseCase, addComponent, removeComponent, clearBuild, totalPrice, componentCount }}
    >
      {children}
    </BuildContext.Provider>
  );
}

export function useBuild() {
  const ctx = useContext(BuildContext);
  if (!ctx) throw new Error("useBuild must be used within BuildProvider");
  return ctx;
}
