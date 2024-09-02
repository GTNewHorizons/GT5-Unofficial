package gtPlusPlus.xmod.forestry.bees.registry;

import static gregtech.api.enums.Mods.Forestry;

import java.util.HashMap;

import gregtech.GTMod;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPDropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPollenType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPComb;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPDrop;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPPollen;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPPropolis;

public class GTPP_Bees {

    public static final byte FORESTRY = 0;
    public static final byte EXTRABEES = 1;
    public static final byte GENDUSTRY = 2;
    public static final byte MAGICBEES = 3;
    public static final byte GREGTECH = 4;

    public static GTPPPropolis propolis;
    public static GTPPPollen pollen;
    public static GTPPDrop drop;
    public static GTPPComb combs;

    public static HashMap<String, Material> sMaterialMappings = new HashMap<>();
    public static HashMap<Integer, GTPPPropolisType> sPropolisMappings = new HashMap<>();
    public static HashMap<Integer, GTPPPollenType> sPollenMappings = new HashMap<>();
    public static HashMap<Integer, GTPPDropType> sDropMappings = new HashMap<>();
    public static HashMap<Integer, GTPPCombType> sCombMappings = new HashMap<>();

    public GTPP_Bees() {
        if (Forestry.isModLoaded() && GTMod.gregtechproxy.mGTBees) {
            Logger.BEES("Creating required items.");
            propolis = new GTPPPropolis();
            pollen = new GTPPPollen();
            drop = new GTPPDrop();
            combs = new GTPPComb();

            Logger.BEES("Loading types.");
            initTypes();

            Logger.BEES("Adding recipes.");
            GTPPDrop.initDropsRecipes();
            GTPPPropolis.initPropolisRecipes();
            GTPPComb.initCombsRecipes();

            Logger.BEES("Initialising bees.");
            GTPP_BeeDefinition.initBees();

            Logger.BEES("Done!");
        }
    }

    private static void initTypes() {
        // This is stupid
        ReflectionUtils.loadClass(GTPP_BeeDefinition.class.getName());
        ReflectionUtils.loadClass(GTPPCombType.class.getName());
        ReflectionUtils.loadClass(GTPPDropType.class.getName());
        ReflectionUtils.loadClass(GTPPPollenType.class.getName());
        ReflectionUtils.loadClass(GTPPPropolisType.class.getName());
    }
}
