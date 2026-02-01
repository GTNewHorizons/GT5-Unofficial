package gtPlusPlus.core.handler;

import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.PamsHarvestCraft;
import static gregtech.api.enums.Mods.Witchery;
import static gregtech.api.util.GTModHandler.getModItem;

import java.util.ArrayList;
import java.util.Set;

import net.minecraft.item.ItemStack;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.interfaces.RunnableWithInfo;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.handler.Recipes.RegistrationHandler;
import gtPlusPlus.core.item.chemistry.IonParticles;
import gtPlusPlus.core.item.chemistry.RecipeLoaderAgriculturalChem;
import gtPlusPlus.core.item.chemistry.RecipeLoaderCoalTar;
import gtPlusPlus.core.item.chemistry.RecipeLoaderGenericChem;
import gtPlusPlus.core.item.chemistry.RecipeLoaderMilling;
import gtPlusPlus.core.item.chemistry.RecipeLoaderRocketFuels;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialGenerator;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.core.recipe.RecipesGregTech;
import gtPlusPlus.core.recipe.RecipesLaserEngraver;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenFluidCanning;
import gtPlusPlus.xmod.gregtech.loaders.RecipeGenRecycling;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderAlgaePond;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderChemicalSkips;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderGTNH;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderGlueLine;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderIndustrialRockBreaker;
import gtPlusPlus.xmod.gregtech.loaders.recipe.RecipeLoaderNuclear;
import gtPlusPlus.xmod.gregtech.registration.gregtech.*;
import gtPlusPlus.xmod.pamsharvest.fishtrap.FishTrapHandler;

public class CompatHandler {

    public static Boolean areInitItemsLoaded = false;

    public static void registerMyModsOreDictEntries() {

        Logger.INFO("Registering Materials with OreDict.");
        // In-house

        ItemStack[] bufferCores = new ItemStack[] { GregtechItemList.Energy_Core_ULV.get(1),
            GregtechItemList.Energy_Core_LV.get(1), GregtechItemList.Energy_Core_MV.get(1),
            GregtechItemList.Energy_Core_HV.get(1), GregtechItemList.Energy_Core_EV.get(1),
            GregtechItemList.Energy_Core_IV.get(1), GregtechItemList.Energy_Core_LuV.get(1),
            GregtechItemList.Energy_Core_ZPM.get(1), GregtechItemList.Energy_Core_UV.get(1),
            GregtechItemList.Energy_Core_UHV.get(1), };
        for (int i = 1; i < 10; i++) {
            GTOreDictUnificator.registerOre("bufferCore_" + GTValues.VN[i - 1], bufferCores[i]);
        }

        for (Particle i : Particle.aMap) {
            GTOreDictUnificator
                .registerOre(OrePrefixes.particle + i.mParticleName.replace(" ", ""), Particle.getBaseParticle(i));
        }

        for (String i : IonParticles.ions) {
            GTOreDictUnificator.registerOre(OrePrefixes.particle + i, Particle.getIon(i, 0));
        }
    }

    public static void registerGregtechMachines() {
        // Free IDs
        /*
         * --- 859 to 868 --- 911 to 940
         */

        new RecipesLaserEngraver();
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
        GregtechIndustrialFishPond.run();
        GregtechIndustrialExtruder.run();
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
        GregtechRedstoneButtonPanel.run();
        GregtechRedstoneCircuitBlock.run();
        GregtechRedstoneLamp.run();
        GregtechRedstoneStrengthDisplay.run();
        GregtechRedstoneStrengthScale.run();
        GregtechIndustrialFramer.run();
    }

    // InterMod
    public static void intermodOreDictionarySupport() {
        if (ExtraUtilities.isModLoaded()) {
            GTOreDictUnificator
                .registerOre("ingotBedrockium", getModItem(Mods.ExtraUtilities.ID, "bedrockiumIngot", 1, 0));
        }
        if (PamsHarvestCraft.isModLoaded()) {
            FishTrapHandler.pamsHarvestCraftCompat();
        }
        if (Witchery.isModLoaded()) {
            // Koboldite
            GTOreDictUnificator.registerOre("dustKoboldite", getModItem(Witchery.ID, "ingredient", 1, 148));
            GTOreDictUnificator.registerOre("nuggetKoboldite", getModItem(Witchery.ID, "ingredient", 1, 149));
            GTOreDictUnificator.registerOre("ingotKoboldite", getModItem(Witchery.ID, "ingredient", 1, 150));
        }
    }

    public static void InitialiseHandlerThenAddRecipes() {
        RegistrationHandler.run();
    }

    public static void startLoadingGregAPIBasedRecipes() {
        // Add hand-made recipes
        RecipesGregTech.run();
        RecipeLoaderGTNH.generate();
        RecipeLoaderNuclear.generate();
        RecipeLoaderGlueLine.generate();
        RecipeLoaderChemicalSkips.generate();
        RecipeLoaderIndustrialRockBreaker.run();
        RecipeLoaderCoalTar.generate();
        RecipeLoaderGenericChem.generate();
        RecipeLoaderAgriculturalChem.generate();
        RecipeLoaderRocketFuels.generate();
        RecipeLoaderMilling.generate();
        RecipeLoaderAlgaePond.generate();
        // Add autogenerated Recipes from Item Components
        for (Set<RunnableWithInfo<Material>> m : MaterialGenerator.mRecipeMapsToGenerate) {
            for (RunnableWithInfo<Material> r : m) {
                r.run();
                Logger.INFO(
                    "[FIND] " + r.getInfoData()
                        .getLocalizedName() + " recipes generated.");
            }
        }
        RecipeGenRecycling.executeGenerators();

        // Do Fluid Canning Last, because they're not executed on demand, but rather queued.
        RecipeGenFluidCanning.init();
    }

    public static final ArrayList<RunnableWithInfo<String>> mRecipesToGenerate = new ArrayList<>();
    public static final ArrayList<RunnableWithInfo<String>> mGtRecipesToGenerate = new ArrayList<>();

    public static void runQueuedRecipes() {
        // Add autogenerated Recipes from Item Components
        for (RunnableWithInfo<String> m : mRecipesToGenerate) {
            m.run();
        }
        for (RunnableWithInfo<String> m : mGtRecipesToGenerate) {
            m.run();
        }
    }
}
