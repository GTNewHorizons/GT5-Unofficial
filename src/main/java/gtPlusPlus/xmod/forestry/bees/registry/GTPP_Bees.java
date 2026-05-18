package gtPlusPlus.xmod.forestry.bees.registry;

import static gregtech.api.enums.Mods.Forestry;

import java.util.HashMap;

import gregtech.GTMod;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPCombType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPDropType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPollenType;
import gtPlusPlus.xmod.forestry.bees.handler.GTPPPropolisType;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPComb;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPDrop;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPPollen;
import gtPlusPlus.xmod.forestry.bees.items.output.GTPPPropolis;

public class GTPP_Bees {

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
        if (Forestry.isModLoaded() && GTMod.proxy.mGTBees) {
            propolis = new GTPPPropolis();
            pollen = new GTPPPollen();
            drop = new GTPPDrop();
            combs = new GTPPComb();

            // call values() to force initialization of enum entries
            GTPP_BeeDefinition.values();
            GTPPCombType.values();
            GTPPDropType.values();
            GTPPPollenType.values();
            GTPPPropolisType.values();

            GTPPDrop.initDropsRecipes();
            GTPPPropolis.initPropolisRecipes();
            GTPPComb.initCombsRecipes();

            GTPP_BeeDefinition.initBees();
        }
    }
}
