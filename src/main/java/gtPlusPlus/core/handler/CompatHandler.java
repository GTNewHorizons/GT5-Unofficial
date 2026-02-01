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
import gtPlusPlus.xmod.gregtech.registration.gregtech.Gregtech4Content;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechAdvancedBoilers;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechAlgaeContent;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechAmazonWarehouse;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechBufferDynamos;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechCustomHatches;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechCyclotron;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechDehydrator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechEnergyBuffer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechFactoryGradeReplacementMultis;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechGeothermalThermalGenerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechHiAmpTransformer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialAlloySmelter;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialArcFurnace;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialBlastSmelter;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialCentrifuge;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialChisel;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialCokeOven;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialCuttingFactory;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialElectrolyzer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialElementDuplicator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialExtruder;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialFishPond;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialFluidHeater;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialForgeHammer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialFuelRefinery;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMacerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMassFabricator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialMixer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialPlatePress;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialRockBreaker;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialSifter;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialThermalCentrifuge;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialTreeFarm;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialWashPlant;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIndustrialWiremill;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechIsaMill;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechLFTR;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechLargeTurbinesAndHeatExchanger;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechMolecularTransformer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechNuclearSaltProcessingPlant;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechPollutionDevices;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechPowerSubStation;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechQuantumForceTransformer;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRTG;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRedstoneButtonPanel;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRedstoneCircuitBlock;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRedstoneLamp;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRedstoneStrengthDisplay;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRedstoneStrengthScale;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechRocketFuelGenerator;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSemiFluidgenerators;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSimpleWasher;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSolarTower;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechSteamMultis;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechThaumcraftDevices;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechThreadedBuffers;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechTieredFluidTanks;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechWaterPump;
import gtPlusPlus.xmod.gregtech.registration.gregtech.GregtechWirelessChargers;
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
