package gregtech.api.util;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.items.MetaGeneratedTool;

public class TurbineStatCalculator {

    public MetaGeneratedTool turbine;
    public ItemStack item;
    public long tMaxDamage;
    public Materials tMaterial;
    public IToolStats tStats;

    public TurbineStatCalculator(MetaGeneratedTool turbineItem, ItemStack aStack) {
        turbine = turbineItem;
        item = aStack;
        tMaxDamage = MetaGeneratedTool.getToolMaxDamage(aStack);
        tMaterial = MetaGeneratedTool.getPrimaryMaterial(aStack);
        tStats = turbine.getToolStats(aStack);
    }

    // Base stats

    public long getMaxDurability() {
        return tMaxDamage;
    }

    public long getCurrentDurability() {
        return getMaxDurability() - MetaGeneratedTool.getToolDamage(item);
    }

    // Efficiency in percentages
    public float getBaseEfficiency() {
        return 0.5F + (0.5F + turbine.getToolCombatDamage(item)) * 0.1F;
    }

    public float getSteamEfficiency() {
        return getBaseEfficiency();
    }

    public float getGasEfficiency() {
        return getBaseEfficiency();
    }

    public float getPlasmaEfficiency() {
        return getBaseEfficiency();
    }

    public float getLooseEfficiency() {
        // 0.85x - 0.3, where x is the base efficiency
        return (float) (-0.2f + Math.round(getBaseEfficiency() * 85.0f) * 0.01);
    }

    public float getLooseSteamEfficiency() {
        return getLooseEfficiency() * 0.9f;
    }

    public float getLooseGasEfficiency() {
        return getLooseEfficiency() * 0.95f;
    }

    public float getLoosePlasmaEfficiency() {
        return getLooseEfficiency();
    }

    // Base optimal flows

    public float getOptimalFlow() {
        return tStats.getSpeedMultiplier() * tMaterial.mToolSpeed * 50F;
    }

    // All values are in EU/t before efficiency
    public float getOptimalSteamFlow() {
        return getOptimalFlow() * tMaterial.mSteamMultiplier;
    }

    public float getOptimalGasFlow() {
        return getOptimalFlow() * tMaterial.mGasMultiplier;
    }

    public float getOptimalPlasmaFlow() {
        return getOptimalFlow() * tMaterial.mPlasmaMultiplier * 42;
    }

    // Loose optimal flows

    public float getOptimalLooseSteamFlow() {
        // 3 * 1.1^((Efficiency - 0.8) * 20)
        return 3.0f * getOptimalSteamFlow() * (float) Math.pow(1.1f, ((getBaseEfficiency() - 0.8f)) * 20f);
    }

    public float getOptimalLooseGasFlow() {
        // 2 * 1.05^((Efficiency - 0.8) * 20)
        return 2.0f * getOptimalGasFlow() * (float) Math.pow(1.05f, ((getBaseEfficiency() - 0.8f)) * 20f);
    }

    public float getOptimalLoosePlasmaFlow() {
        // 1 * 1.05^((Efficiency - 0.8) * 20)
        return 2.0f * getOptimalPlasmaFlow() * (float) Math.pow(1.03f, ((getBaseEfficiency() - 0.8f)) * 20f);
    }

    // Base EU/t from optimal flow

    public float getOptimalSteamEUt() {
        return getOptimalSteamFlow() * getSteamEfficiency() * 0.5f;
    }

    public float getOptimalGasEUt() {
        return getOptimalGasFlow() * getGasEfficiency();
    }

    public float getOptimalPlasmaEUt() {
        return getOptimalPlasmaFlow() * getPlasmaEfficiency();
    }

    // Loose EU/t from optimal flow

    public float getOptimalLooseSteamEUt() {
        return getOptimalLooseSteamFlow() * getLooseSteamEfficiency() * 0.5f;
    }

    public float getOptimalLooseGasEUt() {
        return getOptimalLooseGasFlow() * getLooseGasEfficiency();
    }

    public float getOptimalLoosePlasmaEUt() {
        return getOptimalLoosePlasmaFlow() * getLoosePlasmaEfficiency();
    }

    public int getOverflowEfficiency() {
        return (int) (1 + Math.min(2.0, tMaterial.mToolQuality / 3));
    }

}
