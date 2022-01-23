package gtPlusPlus.xmod.forestry.bees.registry;

import static gregtech.api.enums.GT_Values.MOD_ID_FR;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gtPlusPlus.xmod.forestry.bees.items.output.*;

public class GTPP_Bees {
	
	public final static byte FORESTRY = 0;
	public final static byte EXTRABEES = 1;
	public final static byte GENDUSTRY = 2;
	public final static byte MAGICBEES = 3;
    public final static byte GREGTECH = 4;

    public static GTPP_Propolis propolis;
    public static GTPP_Pollen pollen;
    public static GTPP_Drop drop;
    public static GTPP_Comb combs;

    public GTPP_Bees() {
        if (Loader.isModLoaded(MOD_ID_FR) && GT_Mod.gregtechproxy.mGTBees) {
            propolis = new GTPP_Propolis();
            pollen = new GTPP_Pollen();
            drop = new GTPP_Drop();
            drop.initDropsRecipes();
            combs = new GTPP_Comb();
            combs.initCombsRecipes();
            GTPP_BeeDefinition.initBees();
        }
    }
}
