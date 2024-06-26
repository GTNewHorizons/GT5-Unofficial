package gtPlusPlus.core.handler;

import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.Witchery;

import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.minecraft.ItemPackage;
import gtPlusPlus.core.common.compat.COMPAT_ExtraUtils;
import gtPlusPlus.core.common.compat.COMPAT_HarvestCraft;
import gtPlusPlus.core.common.compat.COMPAT_IC2;
import gtPlusPlus.core.common.compat.COMPAT_Thaumcraft;
import gtPlusPlus.core.common.compat.COMPAT_Witchery;
import gtPlusPlus.core.handler.Recipes.LateRegistrationHandler;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.recipe.RECIPES_GREGTECH;
import gtPlusPlus.core.recipe.RECIPES_LaserEngraver;
import gtPlusPlus.core.recipe.ShapedRecipeObject;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.RecipeUtils;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_FluidCanning;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGen_Recycling;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_ChemicalSkips;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_GTNH;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_GlueLine;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoader_Nuclear;
import gtPlusPlus.xmod.gregtech.registration.gregtech.*;

public class COMPAT_HANDLER {

    public static Queue<Object> RemoveRecipeQueue = new LinkedList<>();
    public static Queue<ShapedRecipeObject> AddRecipeQueue = new LinkedList<>();
    public static Boolean areInitItemsLoaded = false;

    public static void registerMyModsOreDictEntries() {

        Logger.INFO("Registering Materials with OreDict.");
        // In-house

        for (int i = 1; i <= 10; i++) {
            GT_OreDictUnificator.registerOre(
                "bufferCore_" + GT_Values.VN[i - 1],
                new ItemStack(ItemUtils.getItemFromFQRN("miscutils:item.itemBufferCore" + i)));
        }
    }

    public static void registerGregtechMachines() {
        // Free IDs
        /*
         * --- 859 to 868 --- 911 to 940
         */

        new RECIPES_LaserEngraver();
        GregtechEnergyBuffer.run();
        GregtechLFTR.run();
        GregtechNuclearSaltProcessingPlant.run();
        GregtechIndustrialCentrifuge.run();
        GregtechIndustrialCokeOven.run();
        GregtechIndustrialPlatePress.run();
        GregtechRocketFuelGenerator.run();
        GregtechIndustrialElectrolyzer.run();
        GregtechIndustrialMacerator.run();
        GregtechIndustrialWiremill.run();
        GregtechIndustrialMassFabricator.run();
        GregtechWaterPump.run();
        GregtechIndustrialBlastSmelter.run();
        GregtechQuantumForceTransformer.run();
        GregtechPowerSubStation.run();
        GregtechDehydrator.run();
        GregtechAdvancedBoilers.run();
        GregtechPollutionDevices.run();
        GregtechTieredFluidTanks.run();
        GregtechGeothermalThermalGenerator.run();
        Gregtech4Content.run();
        GregtechIndustrialFuelRefinery.run();
        GregtechTreeFarmerTE.run();
        GregtechIndustrialTreeFarm.run();
        GregtechIndustrialSifter.run();
        GregtechSimpleWasher.run();
        GregtechRTG.run();
        GregtechCyclotron.run();
        GregtechHiAmpTransformer.run();
        GregtechIndustrialThermalCentrifuge.run();
        GregtechIndustrialWashPlant.run();
        GregtechSemiFluidgenerators.run();
        GregtechWirelessChargers.run();
        GregtechIndustrialCuttingFactory.run();
        GregtechSuperChests.run();
        GregtechIndustrialFishPond.run();
        GregtechIndustrialExtruder.run();
        GregtechIndustrialMultiMachine.run();
        GregtechBufferDynamos.run();
        GregtechAmazonWarehouse.run();
        GregtechFactoryGradeReplacementMultis.run();
        GregtechThaumcraftDevices.run();
        GregtechThreadedBuffers.run();
        GregtechIndustrialMixer.run();
        GregtechCustomHatches.run();
        GregtechIndustrialArcFurnace.run();
        GregtechSolarTower.run();
        GregtechLargeTurbinesAndHeatExchanger.run();
        GregtechAlgaeContent.run();
        GregtechIndustrialAlloySmelter.run();
        GregtechIsaMill.run();
        GregtechSteamMultis.run();
        GregtechIndustrialForgeHammer.run();
        GregtechMolecularTransformer.run();
        GregtechIndustrialElementDuplicator.run();
        GregtechIndustrialRockBreaker.run();
        GregtechIndustrialChisel.run();
        GregtechIndustrialFluidHeater.run();
    }

    // InterMod
    public static void intermodOreDictionarySupport() {
        if (Thaumcraft.isModLoaded()) {
            COMPAT_Thaumcraft.OreDict();
        }
        if (ExtraUtilities.isModLoaded()) {
            COMPAT_ExtraUtils.OreDict();
        }
        if (PamsHarvestCraft.isModLoaded()) {
            COMPAT_HarvestCraft.OreDict();
        }
        COMPAT_IC2.OreDict();
        if (Witchery.isModLoaded()) {
            COMPAT_Witchery.OreDict();
        }
    }

    public static void RemoveRecipesFromOtherMods() {
        // Removal of Recipes
        for (final Object item : RemoveRecipeQueue) {
            RecipeUtils.removeCraftingRecipe(item);
        }
    }

    public static void InitialiseHandlerThenAddRecipes() {
        RegistrationHandler.run();
    }

    public static void InitialiseLateHandlerThenAddRecipes() {
        LateRegistrationHandler.run();
    }

    public static void startLoadingGregAPIBasedRecipes() {
        // Add hand-made recipes
        RECIPES_GREGTECH.run();
        RecipeLoader_GTNH.generate();
        RecipeLoader_Nuclear.generate();
        RecipeLoader_GlueLine.generate();
        RecipeLoader_ChemicalSkips.generate();
        // Add autogenerated Recipes from Item Components
        for (Set<RunnableWithInfo<Material>> m : MaterialGenerator.mRecipeMapsToGenerate) {
            for (RunnableWithInfo<Material> r : m) {
                try {
                    r.run();
                    Logger.INFO(
                        "[FIND] " + r.getInfoData()
                            .getLocalizedName() + " recipes generated.");
                } catch (Throwable t) {
                    t.printStackTrace();
                    Logger.INFO(
                        "[ERROR] " + r.getInfoData()
                            .getLocalizedName() + " recipes failed to generated.");
                }
            }
        }
        RecipeGen_Recycling.executeGenerators();
        runQueuedMisc();

        // Do Fluid Canning Last, because they're not executed on demand, but rather queued.
        RecipeGen_FluidCanning.init();
    }

    public static void onLoadComplete(FMLLoadCompleteEvent event) {
        runQueuedOnLoadComplete(event);
    }

    public static final AutoMap<RunnableWithInfo<String>> mRecipesToGenerate = new AutoMap<>();
    public static final AutoMap<RunnableWithInfo<String>> mGtRecipesToGenerate = new AutoMap<>();

    public static final AutoMap<RunnableWithInfo<String>> mObjectsToRunInPostInit = new AutoMap<>();
    public static final AutoMap<ItemPackage> mObjectsToRunInOnLoadComplete = new AutoMap<>();

    public static void runQueuedRecipes() {
        // Add autogenerated Recipes from Item Components
        for (RunnableWithInfo<String> m : mRecipesToGenerate) {
            try {
                m.run();
            } catch (Throwable t) {
                t.printStackTrace();
                Logger.INFO("[ERROR] " + m.getInfoData() + " recipe failed to generated.");
            }
        }
        for (RunnableWithInfo<String> m : mGtRecipesToGenerate) {
            try {
                m.run();
            } catch (Throwable t) {
                t.printStackTrace();
                Logger.INFO("[ERROR] " + m.getInfoData() + " recipe failed to generated.");
            }
        }
    }

    public static void runQueuedMisc() {
        for (RunnableWithInfo<String> m : mObjectsToRunInPostInit) {
            try {
                m.run();
            } catch (Throwable t) {
                t.printStackTrace();
                Logger.INFO("[ERROR] " + m.getInfoData());
            }
        }
    }

    /**
     * Generally used to register GT recipe map changes after they've been populated.
     */
    public static void runQueuedOnLoadComplete(FMLLoadCompleteEvent event) {
        for (ItemPackage m : mObjectsToRunInOnLoadComplete) {
            try {
                m.onLoadComplete(event);
            } catch (Throwable t) {
                t.printStackTrace();
                Logger.INFO("[ERROR] " + m.getInfoData());
            }
        }
    }
}
