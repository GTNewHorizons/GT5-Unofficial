/*
 * To change this license header, choose License Headers in Project Properties. To change this template file, choose
 * Tools | Templates and open the template in the editor.
 */
package Ic2ExpReactorPlanner;

import static gregtech.api.enums.Mods.BartWorks;
import static gregtech.api.enums.Mods.GoodGenerator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import Ic2ExpReactorPlanner.components.Condensator;
import Ic2ExpReactorPlanner.components.CoolantCell;
import Ic2ExpReactorPlanner.components.Exchanger;
import Ic2ExpReactorPlanner.components.FuelRod;
import Ic2ExpReactorPlanner.components.Plating;
import Ic2ExpReactorPlanner.components.ReactorItem;
import Ic2ExpReactorPlanner.components.Reflector;
import Ic2ExpReactorPlanner.components.Vent;

import com.github.bartimaeusnek.bartworks.system.material.BW_NonMeta_MaterialItems;

import gregtech.api.enums.ItemList;
import gregtech.api.objects.GT_ItemStack;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.xmod.goodgenerator.GG_Utils;
import gtPlusPlus.xmod.goodgenerator.GG_Utils.GG_Fuel_Rod;

/**
 * Factory class to handle creating components by id or name.
 * 
 * @author Brian McCloud
 */
public class ComponentFactory {

    public static int MAX_COMPONENT_ID = 64;

    static ItemList[] aGtItems = new ItemList[] { ItemList.Neutron_Reflector, ItemList.Moxcell_1, ItemList.Moxcell_2,
            ItemList.Moxcell_4 };

    private ComponentFactory() {
        // do nothing, this class should not be instantiated.
    }

    private static LinkedHashMap<Integer, ReactorItem> ITEM_LIST = new LinkedHashMap<Integer, ReactorItem>();

    static {
        int aID = 0;
        ITEM_LIST.put(aID++, null);
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        1,
                        "fuelRodUranium",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorUraniumSimple", 1).copy()),
                        20e3,
                        1,
                        null,
                        100,
                        2,
                        1,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        2,
                        "dualFuelRodUranium",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorUraniumDual", 1).copy()),
                        20e3,
                        1,
                        null,
                        200,
                        4,
                        2,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        3,
                        "quadFuelRodUranium",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorUraniumQuad", 1).copy()),
                        20e3,
                        1,
                        null,
                        400,
                        8,
                        4,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        4,
                        "fuelRodMox",
                        new GT_ItemStack(aGtItems[1].get(1).copy()),
                        10e3,
                        1,
                        null,
                        100,
                        2,
                        1,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        5,
                        "dualFuelRodMox",
                        new GT_ItemStack(aGtItems[2].get(1).copy()),
                        10e3,
                        1,
                        null,
                        200,
                        4,
                        2,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        6,
                        "quadFuelRodMox",
                        new GT_ItemStack(aGtItems[3].get(1).copy()),
                        10e3,
                        1,
                        null,
                        400,
                        8,
                        4,
                        true));
        ITEM_LIST.put(
                aID++,
                new Reflector(
                        7,
                        "neutronReflector",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorReflector", 1).copy()),
                        30e3,
                        1,
                        null));
        ITEM_LIST.put(
                aID++,
                new Reflector(
                        8,
                        "thickNeutronReflector",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorReflectorThick", 1).copy()),
                        120e3,
                        1,
                        null));
        ITEM_LIST.put(
                aID++,
                new Vent(
                        9,
                        "heatVent",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorVent", 1).copy()),
                        1,
                        1000,
                        null,
                        6,
                        0,
                        0));
        ITEM_LIST.put(
                aID++,
                new Vent(
                        10,
                        "advancedHeatVent",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorVentDiamond", 1).copy()),
                        1,
                        1000,
                        null,
                        12,
                        0,
                        0));
        ITEM_LIST.put(
                aID++,
                new Vent(
                        11,
                        "reactorHeatVent",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorVentCore", 1).copy()),
                        1,
                        1000,
                        null,
                        5,
                        5,
                        0));
        ITEM_LIST.put(
                aID++,
                new Vent(
                        12,
                        "componentHeatVent",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorVentSpread", 1).copy()),
                        1,
                        1,
                        null,
                        0,
                        0,
                        4));
        ITEM_LIST.put(
                aID++,
                new Vent(
                        13,
                        "overclockedHeatVent",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorVentGold", 1).copy()),
                        1,
                        1000,
                        null,
                        20,
                        36,
                        0));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        14,
                        "coolantCell10k",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorCoolantSimple", 1).copy()),
                        1,
                        10e3,
                        null));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        15,
                        "coolantCell30k",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorCoolantTriple", 1).copy()),
                        1,
                        30e3,
                        null));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        16,
                        "coolantCell60k",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorCoolantSix", 1).copy()),
                        1,
                        60e3,
                        null));
        ITEM_LIST.put(
                aID++,
                new Exchanger(
                        17,
                        "heatExchanger",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorHeatSwitch", 1).copy()),
                        1,
                        2500,
                        null,
                        12,
                        4));
        ITEM_LIST.put(
                aID++,
                new Exchanger(
                        18,
                        "advancedHeatExchanger",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorHeatSwitchDiamond", 1).copy()),
                        1,
                        10e3,
                        null,
                        24,
                        8));
        ITEM_LIST.put(
                aID++,
                new Exchanger(
                        19,
                        "coreHeatExchanger",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorHeatSwitchCore", 1).copy()),
                        1,
                        5000,
                        null,
                        0,
                        72));
        ITEM_LIST.put(
                aID++,
                new Exchanger(
                        20,
                        "componentHeatExchanger",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorHeatSwitchSpread", 1).copy()),
                        1,
                        5000,
                        null,
                        36,
                        0));
        ITEM_LIST.put(
                aID++,
                new Plating(
                        21,
                        "reactorPlating",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorPlating", 1).copy()),
                        1,
                        1,
                        null,
                        1000,
                        0.9025));
        ITEM_LIST.put(
                aID++,
                new Plating(
                        22,
                        "heatCapacityReactorPlating",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorPlatingHeat", 1).copy()),
                        1,
                        1,
                        null,
                        1700,
                        0.9801));
        ITEM_LIST.put(
                aID++,
                new Plating(
                        23,
                        "containmentReactorPlating",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorPlatingExplosive", 1).copy()),
                        1,
                        1,
                        null,
                        500,
                        0.81));
        ITEM_LIST.put(
                aID++,
                new Condensator(
                        24,
                        "rshCondensator",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorCondensator", 1).copy()),
                        1,
                        20e3,
                        null));
        ITEM_LIST.put(
                aID++,
                new Condensator(
                        25,
                        "lzhCondensator",
                        new GT_ItemStack(GT_ModHandler.getIC2Item("reactorCondensatorLap", 1).copy()),
                        1,
                        100e3,
                        null));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        26,
                        "fuelRodThorium",
                        new GT_ItemStack(ItemList.ThoriumCell_1.get(1).copy()),
                        50e3,
                        1,
                        "GregTech",
                        20,
                        0.5,
                        1,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        27,
                        "dualFuelRodThorium",
                        new GT_ItemStack(ItemList.ThoriumCell_2.get(1).copy()),
                        50e3,
                        1,
                        "GregTech",
                        40,
                        1,
                        2,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        28,
                        "quadFuelRodThorium",
                        new GT_ItemStack(ItemList.ThoriumCell_4.get(1).copy()),
                        50e3,
                        1,
                        "GregTech",
                        80,
                        2,
                        4,
                        false));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        29,
                        "coolantCellHelium60k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_He_1.get(1).copy()),
                        1,
                        60e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        30,
                        "coolantCellHelium180k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_He_3.get(1).copy()),
                        1,
                        180e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        31,
                        "coolantCellHelium360k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_He_6.get(1).copy()),
                        1,
                        360e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        32,
                        "coolantCellNak60k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_NaK_1.get(1).copy()),
                        1,
                        60e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        33,
                        "coolantCellNak180k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_NaK_3.get(1).copy()),
                        1,
                        180e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        34,
                        "coolantCellNak360k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_NaK_3.get(1).copy()),
                        1,
                        360e3,
                        "GregTech"));
        ITEM_LIST.put(
                aID++,
                new Reflector(
                        35,
                        "iridiumNeutronReflector",
                        new GT_ItemStack(ItemList.Neutron_Reflector.get(1).copy()),
                        1,
                        1,
                        null));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        36,
                        "fuelRodNaquadah",
                        new GT_ItemStack(ItemList.NaquadahCell_1.get(1).copy()),
                        100e3,
                        1,
                        "GregTech",
                        100,
                        2,
                        1,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        37,
                        "dualFuelRodNaquadah",
                        new GT_ItemStack(ItemList.NaquadahCell_2.get(1).copy()),
                        100e3,
                        1,
                        "GregTech",
                        200,
                        4,
                        2,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        38,
                        "quadFuelRodNaquadah",
                        new GT_ItemStack(ItemList.NaquadahCell_4.get(1).copy()),
                        100e3,
                        1,
                        "GregTech",
                        400,
                        8,
                        4,
                        true));

        // aID = 39;
        // ITEM_LIST.put(aID++, new FuelRod(39, "fuelRodCoaxium", null, 20e3, 1, "Coaxium", 100, 0, 1, false));
        // ITEM_LIST.put(aID++, new FuelRod(40, "dualFuelRodCoaxium", null, 20e3, 1, "Coaxium", 200, 0, 2, false));
        // ITEM_LIST.put(aID++, new FuelRod(41, "quadFuelRodCoaxium", null, 20e3, 1, "Coaxium", 400, 0, 4, false));
        // ITEM_LIST.put(aID++, new FuelRod(42, "fuelRodCesium", null, 10861, 1, "Coaxium", 200, 1, 1, false));
        // ITEM_LIST.put(aID++, new FuelRod(43, "dualFuelRodCesium", null, 10861, 1, "Coaxium", 400, 6, 2, false));
        // ITEM_LIST.put(aID++, new FuelRod(44, "quadFuelRodCesium", null, 10861, 1, "Coaxium", 800, 24, 4, false));

        aID = 45;
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        45,
                        "fuelRodNaquadahGTNH",
                        new GT_ItemStack(ItemList.NaquadahCell_1.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        100,
                        2,
                        1,
                        false)); // Naq rods are not MOX-like in GTNH,
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        46,
                        "dualFuelRodNaquadahGTNH",
                        new GT_ItemStack(ItemList.NaquadahCell_2.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        200,
                        4,
                        2,
                        false)); // we have naquadria for that
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        47,
                        "quadFuelRodNaquadahGTNH",
                        new GT_ItemStack(ItemList.NaquadahCell_4.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        400,
                        8,
                        4,
                        false));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        48,
                        "fuelRodNaquadria",
                        new GT_ItemStack(ItemList.MNqCell_1.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        100,
                        2,
                        1,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        49,
                        "dualFuelRodNaquadria",
                        new GT_ItemStack(ItemList.MNqCell_2.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        200,
                        4,
                        2,
                        true));
        ITEM_LIST.put(
                aID++,
                new FuelRod(
                        50,
                        "quadFuelRodNaquadria",
                        new GT_ItemStack(ItemList.MNqCell_4.get(1).copy()),
                        100e3,
                        1,
                        "GTNH",
                        400,
                        8,
                        4,
                        true));

        aID = 51;
        if (BartWorks.isModLoaded()) {
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            51,
                            "fuelRodTiberium",
                            new GT_ItemStack(BW_NonMeta_MaterialItems.TiberiumCell_1.get(1)),
                            50e3,
                            1,
                            BartWorks.ID,
                            100,
                            1,
                            1,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            52,
                            "dualFuelRodTiberium",
                            new GT_ItemStack(BW_NonMeta_MaterialItems.TiberiumCell_2.get(1)),
                            50e3,
                            1,
                            BartWorks.ID,
                            200,
                            2,
                            2,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            53,
                            "quadFuelRodTiberium",
                            new GT_ItemStack(BW_NonMeta_MaterialItems.TiberiumCell_4.get(1)),
                            50e3,
                            1,
                            BartWorks.ID,
                            400,
                            4,
                            4,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            54,
                            "fuelRodTheCore",
                            new GT_ItemStack(BW_NonMeta_MaterialItems.TheCoreCell.get(1)),
                            100e3,
                            1,
                            BartWorks.ID,
                            72534,
                            816,
                            32,
                            false));
        }

        aID = 55;
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        55,
                        "coolantCellSpace180k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_Sp_1.get(1).copy()),
                        1,
                        180e3,
                        "GTNH"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        56,
                        "coolantCellSpace360k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_Sp_2.get(1).copy()),
                        1,
                        360e3,
                        "GTNH"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        57,
                        "coolantCellSpace540k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_Sp_3.get(1).copy()),
                        1,
                        540e3,
                        "GTNH"));
        ITEM_LIST.put(
                aID++,
                new CoolantCell(
                        58,
                        "coolantCellSpace1080k",
                        new GT_ItemStack(ItemList.Reactor_Coolant_Sp_6.get(1).copy()),
                        1,
                        1080e3,
                        "GTNH"));

        aID = 59;
        if (GoodGenerator.isModLoaded()) {
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            59,
                            "fuelRodCompressedUranium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            100,
                            1,
                            1,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            60,
                            "fuelRodDoubleCompressedUranium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium_2, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            100,
                            1,
                            2,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            61,
                            "fuelRodQuadCompressedUranium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedUranium_4, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            100,
                            1,
                            4,
                            false));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            62,
                            "fuelRodCompressedPlutonium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            50,
                            1,
                            1,
                            true));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            63,
                            "fuelRodDoubleCompressedPlutonium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium_2, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            50,
                            1,
                            2,
                            true));
            ITEM_LIST.put(
                    aID++,
                    new FuelRod(
                            64,
                            "fuelRodQuadCompressedPlutonium",
                            new GT_ItemStack(GG_Utils.getGG_Fuel_Rod(GG_Fuel_Rod.rodCompressedPlutonium_4, 1)),
                            50e3,
                            1,
                            GoodGenerator.ID,
                            50,
                            1,
                            4,
                            true));
        }
    }

    private static final Map<String, ReactorItem> ITEM_MAP = makeItemMap();

    private static Map<String, ReactorItem> makeItemMap() {
        Map<String, ReactorItem> result = new HashMap<>((int) (ITEM_LIST.size() * 1.5));
        for (ReactorItem reactorItem : ITEM_LIST.values()) {
            if (reactorItem != null) {
                result.put(reactorItem.baseName, reactorItem);
            }
        }
        return Collections.unmodifiableMap(result);
    }

    private static ReactorItem copy(ReactorItem source) {
        if (source != null) {
            Class<? extends ReactorItem> aClass = source.getClass();
            if (aClass == Condensator.class) {
                return new Condensator((Condensator) source);
            } else if (aClass == CoolantCell.class) {
                return new CoolantCell((CoolantCell) source);
            } else if (aClass == Exchanger.class) {
                return new Exchanger((Exchanger) source);
            } else if (aClass == FuelRod.class) {
                return new FuelRod((FuelRod) source);
            } else if (aClass == Plating.class) {
                return new Plating((Plating) source);
            } else if (aClass == Reflector.class) {
                return new Reflector((Reflector) source);
            } else if (aClass == Vent.class) {
                return new Vent((Vent) source);
            }
        }
        return null;
    }

    /**
     * Gets a default instances of the specified component (such as for drawing button images)
     * 
     * @param id the id of the component.
     * @return the component with the specified id, or null if the id is out of range.
     */
    public static ReactorItem getDefaultComponent(int id) {
        ReactorItem aItem = ITEM_LIST.get(id);
        if (aItem != null) {
            return aItem;
        }
        Logger.INFO("Tried to get default component with ID " + id + ". This is invalid.");
        return null;
    }

    /**
     * Gets a default instances of the specified component (such as for drawing button images)
     * 
     * @param name the name of the component.
     * @return the component with the specified name, or null if the name is not found.
     */
    public static ReactorItem getDefaultComponent(String name) {
        if (name != null) {
            return ITEM_MAP.get(name);
        }
        return null;
    }

    /**
     * Creates a new instance of the specified component.
     * 
     * @param id the id of the component to create.
     * @return a new instance of the specified component, or null if the id is out of range.
     */
    public static ReactorItem createComponent(int id) {
        ReactorItem aItem = ITEM_LIST.get(id);
        if (aItem != null) {
            return copy(aItem);
        }
        Logger.INFO("Tried to create component with ID " + id + ". This is invalid.");
        return null;
    }

    /**
     * Creates a new instance of the specified component.
     * 
     * @param name the name of the component to create.
     * @return a new instance of the specified component, or null if the name is not found.
     */
    public static ReactorItem createComponent(String name) {
        if (name != null) {
            return copy(ITEM_MAP.get(name));
        }
        return null;
    }

    /**
     * Get the number of defined components.
     * 
     * @return the number of defined components.
     */
    public static int getComponentCount() {
        return ITEM_LIST.size();
    }
}
