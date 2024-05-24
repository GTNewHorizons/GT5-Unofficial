package gtPlusPlus.xmod.forestry.bees.registry;

import static gregtech.api.enums.Mods.Forestry;

import java.util.HashMap;

import gregtech.GT_Mod;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PollenType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPP_Comb;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPP_Drop;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPP_Pollen;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPP_Propolis;

public class GTPP_Bees {

    public static final byte FORESTRY = 0;
    public static final byte EXTRABEES = 1;
    public static final byte GENDUSTRY = 2;
    public static final byte MAGICBEES = 3;
    public static final byte GREGTECH = 4;

    public static GTPP_Propolis propolis;
    public static GTPP_Pollen pollen;
    public static GTPP_Drop drop;
    public static GTPP_Comb combs;

    public static HashMap<String, Material> sMaterialMappings = new HashMap<>();
    public static HashMap<Integer, GTPP_PropolisType> sPropolisMappings = new HashMap<>();
    public static HashMap<Integer, GTPP_PollenType> sPollenMappings = new HashMap<>();
    public static HashMap<Integer, GTPP_DropType> sDropMappings = new HashMap<>();
    public static HashMap<Integer, GTPP_CombType> sCombMappings = new HashMap<>();

    public GTPP_Bees() {
        if (Forestry.isModLoaded() && GT_Mod.gregtechproxy.mGTBees) {
            Logger.BEES("Creating required items.");
            propolis = new GTPP_Propolis();
            pollen = new GTPP_Pollen();
            drop = new GTPP_Drop();
            combs = new GTPP_Comb();

            Logger.BEES("Loading types.");
            initTypes();

            Logger.BEES("Adding recipes.");
            GTPP_Drop.initDropsRecipes();
            GTPP_Propolis.initPropolisRecipes();
            GTPP_Comb.initCombsRecipes();

            Logger.BEES("Initialising bees.");
            GTPP_BeeDefinition.initBees();

            Logger.BEES("Done!");
        }
    }

    private static void initTypes() {
        ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.registry.GTPP_BeeDefinition");
        ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_CombType");
        ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_DropType");
        ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_PollenType");
        ReflectionUtils.loadClass("gtPlusPlus.xmod.forestry.bees.handler.GTPP_PropolisType");
    }
}
