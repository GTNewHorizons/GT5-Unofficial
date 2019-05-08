package gregtech.loaders.misc;

import cpw.mods.fml.common.Loader;
import forestry.api.apiculture.EnumBeeChromosome;
import forestry.api.genetics.*;
import forestry.core.genetics.alleles.Allele;
import forestry.core.utils.StringUtil;
import gregtech.GT_Mod;
import gregtech.common.bees.GT_AlleleHelper;
import gregtech.common.items.ItemComb;
import gregtech.common.items.ItemDrop;
import gregtech.common.items.ItemPollen;
import gregtech.common.items.ItemPropolis;
import gregtech.loaders.misc.GT_BeeDefinition;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class GT_Bees {

	public static IAlleleInteger noFertility;
    public static IAlleleInteger superFertility;

    public static IAlleleInteger noFlowering;
    public static IAlleleInteger superFlowering;

    public static IAlleleArea noTerritory;
    public static IAlleleArea superTerritory;

    public static IAlleleFloat noWork;
    public static IAlleleFloat speedBlinding;
    public static IAlleleFloat superSpeed;

    public static IAlleleInteger blinkLife;
    public static IAlleleInteger superLife;

    public static ItemPropolis propolis;
    public static ItemPollen pollen;
    public static ItemDrop drop;
    public static ItemComb combs;

    public GT_Bees() {
        if (Loader.isModLoaded("Forestry") && GT_Mod.gregtechproxy.mGTBees) {
        	GT_AlleleHelper.initialisation();
            propolis = new ItemPropolis();
            propolis.initPropolisRecipes();
            pollen = new ItemPollen();
            drop = new ItemDrop();
            drop.initDropsRecipes();
            combs = new ItemComb();
            combs.initCombsRecipes();
            GT_BeeDefinition.initBees();          
        }

    }
    
    public static class DimensionMutationCondition implements IMutationCondition {

        int dimID;
        String dimName;

        public DimensionMutationCondition(int id, String name) {
            dimID = id;
            dimName = name;
        }

        @Override
        public float getChance(World world, int x, int y, int z, IAllele allele0, IAllele allele1, IGenome genome0,IGenome genome1) {
            if(world.provider.dimensionId == dimID)return 1;
            return 0;
        }

        @Override
        public String getDescription() {
            return StringUtil.localizeAndFormat("mutation.condition.dim") + " " + dimName;
        }

    }
}