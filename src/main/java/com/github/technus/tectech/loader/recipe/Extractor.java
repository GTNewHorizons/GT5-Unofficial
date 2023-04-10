package com.github.technus.tectech.loader.recipe;

import com.github.technus.tectech.thing.CustomItemList;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;

public class Extractor implements Runnable {

    @Override
    public void run() {
        // LV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 0),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                300,
                2);
        // MV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 1),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                300,
                2);
        // HV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 2),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                300,
                2);
        // EV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 3),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                300,
                2);
        // IV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                300,
                2);
        // LuV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 5),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14),
                300,
                2);
        // ZPM Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 6),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16),
                300,
                2);
    }
}
