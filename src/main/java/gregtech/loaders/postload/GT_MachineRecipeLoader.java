package gregtech.loaders.postload;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.util.GT_ModHandler.getModItem;

import codechicken.nei.api.API;
import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.enums.*;
import gregtech.api.util.*;
import gregtech.common.GT_DummyWorld;
import gregtech.common.items.GT_MetaGenerated_Item_03;
import gregtech.loaders.postload.chains.GT_BauxiteRefineChain;
import gregtech.loaders.postload.chains.GT_NaniteChain;
import gregtech.loaders.postload.chains.GT_PCBFactoryRecipes;
import ic2.api.recipe.ILiquidHeatExchangerManager;
import ic2.api.recipe.Recipes;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class GT_MachineRecipeLoader implements Runnable {

    public static final String aTextAE = "appliedenergistics2";
    public static final String aTextAEMM = "item.ItemMultiMaterial";
    public static final String aTextForestry = "Forestry";
    public static final String aTextEBXL = "ExtrabiomesXL";
    public static final String aTextTCGTPage = "gt.research.page.1.";
    public static final Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
    public static final Boolean isThaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
    public static final Boolean isBartWorksLoaded = Loader.isModLoaded("bartworks");
    public static final Boolean isGTNHLanthanidLoaded = Loader.isModLoaded("gtnhlanth");
    public static final Boolean isGTPPLoaded = Loader.isModLoaded(MOD_ID_GTPP);
    public static final Boolean isGalaxySpaceLoaded = Loader.isModLoaded("GalaxySpace");
    public static final Boolean isGalacticraftMarsLoaded = Loader.isModLoaded("GalacticraftMars");
    public static final Boolean isIronChestLoaded = Loader.isModLoaded("IronChest");
    public static final Boolean isCoremodLoaded = Loader.isModLoaded(MOD_ID_DC);
    public static final Boolean isBuildCraftFactoryLoaded = Loader.isModLoaded("BuildCraft|Factory");
    public static final Boolean isIronTankLoaded = Loader.isModLoaded("irontank");
    public static final Boolean isExtraUtilitiesLoaded = Loader.isModLoaded("ExtraUtilities");
    public static final Boolean isEBXLLoaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextEBXL);
    public static final Boolean isRailcraftLoaded = Loader.isModLoaded(MOD_ID_RC);
    public static final Boolean isForestryloaded =
            Loader.isModLoaded(GT_MachineRecipeLoader.aTextForestry); // TODO OW YEAH NEW PLANK GEN CODE!!!

    @Override
    public void run() {
        GT_Log.out.println("GT_Mod: Adding non-OreDict Machine Recipes.");


        try {
            GT_DummyWorld tWorld = (GT_DummyWorld) GT_Values.DW;
            while (tWorld.mRandom.mIterationStep > 0) {
                GT_Values.RA.addFluidExtractionRecipe(
                        GT_Utility.copyAmount(1L, ForgeHooks.getGrassSeed(tWorld)),
                        GT_Values.NI,
                        Materials.SeedOil.getFluid(5L),
                        10000,
                        64,
                        2);
            }
        } catch (Throwable e) {
            GT_Log.out.println(
                    "GT_Mod: failed to iterate somehow, maybe it's your Forge Version causing it. But it's not that important\n");
            e.printStackTrace(GT_Log.err);
        }

        GT_Values.RA.addSimpleArcFurnaceRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1L),
                Materials.Oxygen.getGas(2000L),
                new ItemStack[] {GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3)},
                null,
                1200,
                30);

        this.run2();

        if (Loader.isModLoaded("HardcoreEnderExpansion")) {
            GT_OreDictUnificator.set(
                    OrePrefixes.ingot,
                    Materials.HeeEndium,
                    getModItem("HardcoreEnderExpansion", "endium_ingot", 1),
                    true,
                    true);
        }

        GT_Values.RA.addAmplifier(ItemList.IC2_Scrap.get(9L), 180, 1);
        GT_Values.RA.addAmplifier(ItemList.IC2_Scrapbox.get(1L), 180, 1);

        for (int g = 0; g < 16; g++) {
            if (!GT_MachineRecipeLoader.isNEILoaded) {
                break;
            }
            API.hideItem(new ItemStack(GT_MetaGenerated_Item_03.INSTANCE, 1, g));
        }

        GT_Values.RA.addElectromagneticSeparatorRecipe(
                MaterialsOreAlum.SluiceSand.getDust(1),
                Materials.Iron.getDust(1),
                Materials.Neodymium.getDust(1),
                Materials.Chrome.getDust(1),
                new int[] {4000, 2000, 2000},
                200,
                240);

        GT_BauxiteRefineChain.run();
    }

    public void run2() {
        GT_NaniteChain.run();
        GT_PCBFactoryRecipes.load();

        if (!GregTech_API.mIC2Classic) {
            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange =
                    ic2.api.recipe.Recipes.liquidCooldownManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator =
                    tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if (tEntry.getKey().equals("ic2hotcoolant")) {
                        tIterator.remove();
                        Recipes.liquidCooldownManager.addFluid("ic2hotcoolant", "ic2coolant", 100);
                    }
                }
            } catch (Throwable e) {
                /*Do nothing*/
            }

            try {
                Map<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tLiqExchange =
                    ic2.api.recipe.Recipes.liquidHeatupManager.getHeatExchangeProperties();
                Iterator<Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty>> tIterator =
                    tLiqExchange.entrySet().iterator();
                while (tIterator.hasNext()) {
                    Map.Entry<String, ILiquidHeatExchangerManager.HeatExchangeProperty> tEntry = tIterator.next();
                    if (tEntry.getKey().equals("ic2coolant")) {
                        tIterator.remove();
                        Recipes.liquidHeatupManager.addFluid("ic2coolant", "ic2hotcoolant", 100);
                    }
                }
            } catch (Throwable e) {
                /*Do nothing*/
            }
        }
    }
}
