package com.elisis.gtnhlanth.loader;

import static com.elisis.gtnhlanth.api.recipe.LanthanidesRecipeMaps.digesterRecipes;
import static com.elisis.gtnhlanth.api.recipe.LanthanidesRecipeMaps.dissolutionTankRecipes;
import static com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool.*;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.crackingRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.electroMagneticSeparatorRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.electrolyzerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidSolidifierRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.laserEngraverRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerNonCellRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.vacuumFreezerRecipes;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gregtech.api.util.GT_RecipeBuilder.TICKS;
import static gregtech.api.util.GT_RecipeConstants.COIL_HEAT;
import static gregtech.api.util.GT_RecipeConstants.DISSOLUTION_TANK_RATIO;
import static gregtech.api.util.GT_RecipeConstants.UniversalChemical;
import static gregtech.common.items.GT_MetaGenerated_Item_01.registerCauldronCleaningFor;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.chemicalDehydratorRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.simpleWasherRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashSet;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.ShapedRecipes;
import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.apache.commons.lang3.reflect.FieldUtils;

import com.elisis.gtnhlanth.Tags;
import com.elisis.gtnhlanth.common.register.BotWerkstoffMaterialPool;
import com.elisis.gtnhlanth.common.register.LanthItemList;
import com.elisis.gtnhlanth.common.register.WerkstoffMaterialPool;
import com.github.bartimaeusnek.bartworks.system.material.GT_Enhancement.PlatinumSludgeOverHaul;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;

import cpw.mods.fml.common.registry.GameRegistry;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_Log;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class RecipeLoader {

    private static final Materials[] BLACKLIST = null;

    public static void loadGeneral() {

        /* ZIRCONIUM */
        // ZrCl4
        // ZrO2 + 4HCl = ZrCl4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1), WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .itemOutputs(WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // ZrCl4-H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.ZirconiumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Zr
        // ZrCl4·H2O + 2Mg = Zr + 2MgCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .itemOutputs(
                WerkstoffMaterialPool.Zirconium.get(OrePrefixes.ingotHot, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.ZirconiumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 4500)
            .addTo(blastFurnaceRecipes);

        /* HAFNIUM */
        // HfCl4
        // HfO2 + 4HCl = HfCl4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1), WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3))
            .itemOutputs(WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(4000))
            .fluidOutputs(Materials.Water.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // HfCl4-H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.HafniumTetrachloride.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Water.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // LP-Hf
        // HfCl4 + 2Mg = ??Hf?? + 2MgCl2
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(2),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2))
            .itemOutputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6))
            .fluidInputs(WerkstoffMaterialPool.HafniumTetrachlorideSolution.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 2700)
            .addTo(blastFurnaceRecipes);

        // HfI4
        // ??Hf?? + 4I = HfI4
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .fluidInputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.LowPurityHafnium.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.Iodine.get(OrePrefixes.dust, 4))
            .itemOutputs(WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Hf
        // HfI4 = Hf + 4I
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(12),
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 5))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 1),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dustTiny, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(4000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Hf * 9
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(13),
                WerkstoffMaterialPool.HafniumIodide.get(OrePrefixes.dust, 45))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnium.get(OrePrefixes.ingotHot, 9),
                WerkstoffMaterialPool.HafniumRunoff.get(OrePrefixes.dust, 1))
            .fluidOutputs(WerkstoffMaterialPool.Iodine.getFluidOrGas(36000))
            .duration(4 * MINUTES + 30 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 3400)
            .addTo(blastFurnaceRecipes);

        // Zirconia-Hafnia
        // ??HfZr?? = HfO2 + ZrO2
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.Hafnia.get(OrePrefixes.dust, 3),
                WerkstoffMaterialPool.Zirconia.get(OrePrefixes.dust, 3))
            .eut(TierEU.RECIPE_EV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        // Ammonium Nitrate
        // HNO3 + NH3 = NH4NO3
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(12), Materials.NitricAcid.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ammonia.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(chemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(12))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), Materials.Ammonia.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(400)
            .addTo(multiblockChemicalReactorRecipes);

        // IODINE-START
        // SeaweedAsh
        GT_ModHandler.addSmeltingRecipe(
            GT_ModHandler.getModItem("harvestcraft", "seaweedItem", 1),
            WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dustSmall, 1));

        // SeaweedConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Calcite.getDust(1))
            .fluidInputs(Materials.DilutedSulfuricAcid.getFluid(1200))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(1200))
            .duration(30 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);
        // SeaweedConcentrate * 4
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SeaweedAsh.get(OrePrefixes.dust, 4))
            .itemOutputs(Materials.Calcite.getDust(2))
            .fluidInputs(Materials.DilutedSulfuricAcid.getFluid(2400))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2400))
            .duration(60 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);

        // Iodine
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Benzene.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.SeaweedConcentrate.getFluidOrGas(2000))
            .fluidOutputs(WerkstoffMaterialPool.SeaweedByproducts.getFluidOrGas(200))
            .eut(TierEU.RECIPE_HV)
            .duration(38 * SECONDS)
            .addTo(centrifugeRecipes);

        // IODINE-END

        // 2MnO2 + 2KOH + KClO3 = 2KMnO4 + H2O + KCl
        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Pyrolusite.getDust(6),
                GT_ModHandler.getModItem("dreamcraft", "item.PotassiumHydroxideDust", 6),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .itemOutputs(
                WerkstoffMaterialPool.PotassiumPermanganate.get(OrePrefixes.dust, 12),
                Materials.RockSalt.getDust(2))
            .fluidOutputs(Materials.Water.getFluid(1000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        // Mn + 2O = MnO2
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Manganese.getDust(1), GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(Materials.Pyrolusite.getDust(3))
            .fluidInputs(Materials.Oxygen.getGas(2000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // 6KOH + 6Cl = KClO3 + 5KCl + 3H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.PotassiumHydroxideDust", 18),
                GT_Utility.getIntegratedCircuit(3))
            .itemOutputs(
                Materials.RockSalt.getDust(10),
                WerkstoffMaterialPool.PotassiumChlorate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.Chlorine.getGas(6000))
            .fluidOutputs(Materials.Water.getFluid(3000))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Sodium Fluorosilicate
        // 2NaCl + H2SiF6 = 2HCl + Na2SiF6
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2), Materials.Salt.getDust(4))
            .itemOutputs(Materials.HydrochloricAcid.getCells(2))
            .fluidInputs(WerkstoffLoader.HexafluorosilicicAcid.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);
    }

    public static void loadLanthanideRecipes() {
        // Methanol
        // CH4O + CO + 3O =V2O5= H2C2O4 + H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(
                Materials.Methanol.getFluid(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Oxygen.getGas(3000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(1000), Materials.Water.getFluid(1000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9), MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(
                Materials.Methanol.getFluid(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Oxygen.getGas(27000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(9000), Materials.Water.getFluid(9000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        // Ethanol
        // C2H6O + 5O =V2O5= H2C2O4 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(MyMaterial.vanadiumPentoxide.get(OrePrefixes.dustTiny, 1))
            .itemOutputs()
            .fluidInputs(Materials.Ethanol.getFluid(1000), Materials.Oxygen.getGas(5000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(1000), Materials.Water.getFluid(2000))
            .duration(22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9), MyMaterial.vanadiumPentoxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Ethanol.getFluid(9000), Materials.Oxygen.getGas(45000))
            .fluidOutputs(MyMaterial.oxalate.getFluidOrGas(9000), Materials.Water.getFluid(18000))
            .duration(3 * MINUTES + 22 * SECONDS + 10 * TICKS)
            .eut(240)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Oxalate
        // 2CeCl3 + 3H2C2O4 = 6HCl + Ce2(C2O4)3
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 8))
            .itemOutputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(MyMaterial.oxalate.getFluidOrGas(3000))
            .fluidOutputs(Materials.HydrochloricAcid.getFluid(6000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Cerium
        // Ce2O3 = 2Ce + 3O
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5))
            .itemOutputs(GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Cerium, 2))
            .fluidOutputs(Materials.Oxygen.getFluid(3000))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_MV)
            .addTo(electrolyzerRecipes);

        // CHAIN BEGIN
        // MONAZITE
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Monazite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(1920)
            .duration(400)
            .metadata(COIL_HEAT,800)
            .addTo(digesterRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(10000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(1000))
            .itemInputs(GT_Utility.getIntegratedCircuit(1), Materials.Saltpeter.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(11000))
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dustTiny, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 1),
                Materials.Monazite.getDustTiny(2))
            .eut(480)
            .duration(900)
            .metadata(DISSOLUTION_TANK_RATIO,10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(90000),
                WerkstoffMaterialPool.MuddyRareEarthMonaziteSolution.getFluidOrGas(9000))
            .itemInputs(GT_Utility.getIntegratedCircuit(9), Materials.Saltpeter.getDust(9))
            .fluidOutputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(99000))
            .itemOutputs(
                WerkstoffMaterialPool.HafniaZirconiaBlend.get(OrePrefixes.dust, 4),
                WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 9),
                Materials.Monazite.getDust(2))
            .eut(480)
            .duration(8100)
            .metadata(DISSOLUTION_TANK_RATIO,10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedRareEarthMonaziteMud.getFluidOrGas(1000))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1),
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 20_00, 5_00, 20_00)
            .duration(20 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteSulfate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(6000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(7000))
            .duration(24 * SECONDS)
            .eut(400)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(13))
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dustTiny, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(1000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(200))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(14))
            .itemOutputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 3))
            .fluidInputs(
                WerkstoffMaterialPool.DilutedMonaziteSulfate.getFluidOrGas(9000),
                WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(1800))
            .duration(3 * MINUTES + 36 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicMonazitePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .outputChances(9000, 7000)
            .duration(30 * SECONDS)
            .eut(256)
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateCake.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(128)
            .metadata(COIL_HEAT, 1500)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.ThoriumPhosphateConcentrate.get(OrePrefixes.dust))
            .itemOutputs(Materials.Thorium.getDust(1), Materials.Phosphate.getDust(1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(thermalCentrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.AmmoniumNitrate.getFluidOrGas(320))
            .duration(6 * SECONDS)
            .eut(240)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeutralizedMonaziteRareEarthFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .outputChances(9000, 5000, 4000)
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.UraniumFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(100))
            .duration(18 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeutralizedUraniumFiltrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                Materials.Uranium.getDust(1),
                Materials.Uranium.getDust(1),
                Materials.Uranium.getDust(1),
                Materials.Uranium235.getDust(1),
                Materials.Uranium235.getDust(1))
            .outputChances(4500, 4000, 3000, 3000, 2000)
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRareEarthHydroxideConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DriedMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(500))
            .fluidOutputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(WerkstoffMaterialPool.NitratedRareEarthMonaziteConcentrate.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(2000))
            .duration(11 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.NitricLeachedMonaziteMixture.getFluidOrGas(1000))
            .itemOutputs(WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(11_11)
            .fluidOutputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000))
            .duration(20 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(sifterRecipes);

        // BEGIN Cerium
        // Cerium-rich mixture + 3HCl = CeCl3 + Monazite (to allow cerium processing without bastnazite/monazite)
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 15))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 1), Materials.Monazite.getDust(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(750))
            .fluidOutputs(Materials.Water.getFluid(750))
            .duration(25 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // CeO2 + 3NH4Cl + H = 3NH3 + CeCl3 + 2H2O
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 3),
                WerkstoffLoader.AmmoniumChloride.get(OrePrefixes.cell, 3))
            .itemOutputs(WerkstoffMaterialPool.CeriumChloride.get(OrePrefixes.dust, 4), Materials.Ammonia.getCells(3))
            .fluidInputs(Materials.Hydrogen.getGas(1000))
            .fluidOutputs(Materials.Water.getGas(2000))
            .duration(15 * SECONDS)
            .eut(450)
            .addTo(UniversalChemical);

        // Ce2(C2O4)3 + 3C = Ce2O3 + 9CO
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxalate.get(OrePrefixes.dust, 5), Materials.Carbon.getDust(3))
            .itemOutputs(WerkstoffMaterialPool.CeriumIIIOxide.get(OrePrefixes.dust, 5))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(9000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 800)
            .addTo(blastFurnaceRecipes);

        // END Cerium (NMLC)

        GT_Values.RA.stdBuilder()
            .itemOutputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.NitricMonaziteLeachedConcentrate.getFluidOrGas(1000))
            .duration(5 * SECONDS)
            .eut(240)
            .noOptimize()
            .addTo(vacuumFreezerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CooledMonaziteRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5))
            .outputChances(9000, 500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(electroMagneticSeparatorRecipes);

        // 5Eu2O3 + Eu = 4EuO
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumIIIOxide.get(OrePrefixes.dust, 5), Materials.Europium.getDust(1))
            .itemOutputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 6))
            .duration(15 * SECONDS)
            .eut(8400)
            .addTo(UniversalChemical);

        // 4 EuO = 2 Eu + 2O2
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumOxide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1))
            .fluidOutputs(Materials.Oxygen.getGas(1000L))
            .duration(15 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        // EuS = Eu + S
        // TODO old recipe. for compat only. remove material and recipe half a year later, i.e. after September 2023.
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.EuropiumSulfide.get(OrePrefixes.dust, 2))
            .itemOutputs(Materials.Europium.getDust(1), Materials.Sulfur.getDust(1))
            .duration(30 * SECONDS)
            .eut(33_000)
            .addTo(electrolyzerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MonaziteRarerEarthSediment.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Chlorine.getGas(1000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                Materials.Salt.getDust(1),
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .duration(10 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MonaziteHeterogenousHalogenicRareEarthMixture.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 3))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .duration(20 * SECONDS)
            .eut(240)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                WerkstoffMaterialPool.SaturatedMonaziteRareEarthMixture.get(OrePrefixes.dust, 8))
            .itemOutputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 6))
            .fluidOutputs(Materials.Chloromethane.getGas(800))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * MINUTES + 15 * SECONDS)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 9))
            .itemOutputs(Materials.Samarium.getDust(6), Materials.Gadolinium.getDust(3))
            .outputChances(10000, 10000)
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(sifterRecipes);

        // BASTNASITE (god help me)
        GT_Values.RA.stdBuilder()
            .fluidInputs(Materials.NitricAcid.getFluid(700))
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Bastnasite, 2))
            .fluidOutputs(WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(400))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .eut(1920)
            .duration(400)
            .metadata(COIL_HEAT,800)
            .addTo(digesterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(
                WerkstoffMaterialPool.MuddyRareEarthBastnasiteSolution.getFluidOrGas(1000),
                GT_ModHandler.getSteam(1000))
            .fluidOutputs(WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.getFluidOrGas(2000))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(crackingRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(6),
                WerkstoffMaterialPool.SteamCrackedBasnasiteSolution.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.SodiumFluorosilicate.getFluidOrGas(320))
            .fluidOutputs(WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1320))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(
                Materials.Water.getFluid(10000),
                WerkstoffMaterialPool.ConditionedBastnasiteMud.getFluidOrGas(1000))
            .itemInputs(Materials.Saltpeter.getDust(1))
            .fluidOutputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(11000))
            .eut(1920)
            .duration(1000)
            .metadata(DISSOLUTION_TANK_RATIO,10)
            .noOptimize()
            .addTo(dissolutionTankRecipes);

        GT_Values.RA.stdBuilder()
            .itemOutputs(
                Materials.SiliconDioxide.getDust(1),
                Materials.Rutile.getDust(1),
                WerkstoffLoader.RedZircon.get(OrePrefixes.dust, 1),
                Materials.Ilmenite.getDust(1))
            .outputChances(90_00, 75_00, 10_00, 5_00, 20_00)
            .fluidInputs(WerkstoffMaterialPool.DiltedRareEarthBastnasiteMud.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(400))
            .eut(240)
            .duration(20 * SECONDS)
            .noOptimize()
            .addTo(sifterRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .fluidInputs(WerkstoffMaterialPool.FilteredBastnasiteMud.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(600)
            .metadata(COIL_HEAT, 1400)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRareEarthOxidePowder.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(500))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                WerkstoffMaterialPool.LeachedBastnasiteRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Oxygen.getGas(1000))
            .fluidOutputs(Materials.Fluorine.getGas(13))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(7),
                WerkstoffMaterialPool.RoastedRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Water.getFluid(200))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.WetRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Fluorine.getGas(4000))
            .fluidOutputs(Materials.HydrofluoricAcid.getFluid(4000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.CeriumOxidisedRareEarthOxides.get(OrePrefixes.dust, 1))
            .itemOutputs(
                WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.CeriumDioxide.get(OrePrefixes.dust, 1))
            .outputChances(100_00, 90_00)
            .eut(TierEU.RECIPE_HV)
            .duration(30 * SECONDS)
            .addTo(centrifugeRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.BastnasiteRarerEarthOxides.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.NitricAcid.getFluid(400))
            .fluidOutputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(mixerRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NitratedBastnasiteRarerEarthOxides.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Acetone.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .duration(35 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemOutputs(
                WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .outputChances(80_00, 50_00)
            .fluidInputs(WerkstoffMaterialPool.SaturatedBastnasiteRarerEarthOxides.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(750))
            .eut(TierEU.RECIPE_HV)
            .duration(45 * SECONDS)
            .addTo(centrifugeRecipes);

        // Nd RE
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 2))
            .itemOutputs(
                WerkstoffMaterialPool.LanthaniumChloride.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.NeodymiumOxide.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(2000))
            .duration(45 * SECONDS)
            .eut(800)
            .addTo(UniversalChemical);

        // Sm RE
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamaricRareEarthConcentrate.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 2))
            .fluidInputs(Materials.HydrofluoricAcid.getFluid(2000))
            .duration(17 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.FluorinatedSamaricConcentrate.get(OrePrefixes.dust, 8),
                Materials.Calcium.getDust(4))
            .itemOutputs(
                Materials.Holmium.getDust(1),
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 4))
            .fluidOutputs(WerkstoffMaterialPool.CalciumFluoride.getFluidOrGas(12000))
            .duration(1 * MINUTES + 20 * TICKS)
            .eut(TierEU.RECIPE_EV)
            .metadata(COIL_HEAT, 1200)
            .addTo(blastFurnaceRecipes);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumTerbiumMixture.get(OrePrefixes.dust, 1),
                BotWerkstoffMaterialPool.AmmoniumNitrate.get(OrePrefixes.dust, 9))
            .itemOutputs(WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 1))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.NitratedSamariumTerbiumMixture.get(OrePrefixes.dust, 4),
                Materials.Copper.getDust(1))
            .itemOutputs(
                WerkstoffMaterialPool.TerbiumNitrate.get(OrePrefixes.dust, 2),
                WerkstoffMaterialPool.SamaricResidue.get(OrePrefixes.dust, 2) // Potentially make only Samarium
            )
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2),
                Materials.Calcium.getDust(3))
            .itemOutputs(
                WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
                Materials.TricalciumPhosphate.getDust(5))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 6))
            .itemOutputs(Materials.Samarium.getDust(1), WerkstoffLoader.Thorianit.get(OrePrefixes.dust, 2))
            .outputChances(90_00, 80_00)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(centrifugeRecipes);

        // TODO UV Tier Ion Extracting Method

        // Lanthanum Part
        // Digester to produce Lanthanum Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Lanthanum, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(LanthanumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(LanthanumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                LanthanumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Praseodymium Part
        // Digester to produce Praseodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Praseodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PraseodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PraseodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                PraseodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Cerium Part
        // Digester to produce Cerium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Cerium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(CeriumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(CeriumExtractingNanoResin.getFluidOrGas(1000), CeriumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Neodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(NeodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Neodymium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Neodymium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(NeodymiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                NeodymiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Neodymium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Promethium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PromethiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Promethium Part
        // Digester to produce Promethium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Promethium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(PromethiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                PromethiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Samarium Part
        // Digester to produce Samarium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(SamariumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                SamariumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Europium Part
        // Digester to produce Europium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Europium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(EuropiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(EuropiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                EuropiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Gadolinium Part
        // Digester to produce Gadolinium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Gadolinium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GadoliniumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(GadoliniumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                GadoliniumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Terbium Part
        // Digester to produce Terbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Terbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(TerbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(TerbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(TerbiumExtractingNanoResin.getFluidOrGas(1000), TerbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dysprosium Part
        // Digester to produce Dysprosium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Dysprosium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(DysprosiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(DysprosiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                DysprosiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Holmium Part
        // Digester to produce Holmium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Holmium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(HolmiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(HolmiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(HolmiumExtractingNanoResin.getFluidOrGas(1000), HolmiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Erbium Part
        // Digester to produce Erbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Erbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ErbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(ErbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(ErbiumExtractingNanoResin.getFluidOrGas(1000), ErbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Thulium Part
        // Digester to produce Thulium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Thulium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ThuliumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(ThuliumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(ThuliumExtractingNanoResin.getFluidOrGas(1000), ThuliumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ytterbium Part
        // Digester to produce Ytterbium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Ytterbium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(YtterbiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(YtterbiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                YtterbiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lutetium Part
        // Digester to produce Lutetium Chloride Concentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Lutetium, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(3))
            .fluidInputs(Materials.Chlorine.getGas(36000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(3000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(LutetiumOreConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(Materials.SiliconDioxide.getDust(1))
            .fluidInputs(Materials.Chlorine.getGas(12000))
            .fluidOutputs(LutetiumChlorideConcentrate.getFluidOrGas(1000))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_ZPM)
            .duration(2 * SECONDS)
            .addTo(digesterRecipes);
        // 1B oreChlorideConcentrate = 1 ore's rare earth metal + 3 any rare earth metal
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                LutetiumChlorideConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ion Extracting Process to produce Rare Earth Element (example Samarium) by Nano Resin
        // Get Extracting Nano Resin

        // Lanthanum
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Lanthanum.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(LanthanumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Praseodymium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Praseodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(PraseodymiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Cerium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Cerium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(CeriumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Neodymium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Neodymium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(NeodymiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Promethium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Promethium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(PromethiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Samarium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(SamariumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Europium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Europium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(EuropiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Gadolinium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Gadolinium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(GadoliniumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Terbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Terbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(TerbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Dysprosium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Dysprosium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(DysprosiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Holmium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Holmium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(HolmiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Erbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Erbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(ErbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Thulium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Thulium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(ThuliumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Ytterbium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Ytterbium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(YtterbiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // Lutetium
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_ModHandler.getModItem("dreamcraft", "item.MysteriousCrystalLens", 0),
                Materials.Lutetium.getDust(1),
                Materials.Carbon.getNanite(1))
            .fluidInputs(MyMaterial.P507.getFluidOrGas(4000))
            .fluidOutputs(LutetiumExtractingNanoResin.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(10 * SECONDS)
            .addTo(laserEngraverRecipes);

        // TODO Electrolyzer recycle Nano Resin and produce molten rare earth metal,

        // La
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLanthanumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                LanthanumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lanthanum, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Pr
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                PraseodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Praseodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ce
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledCeriumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                CeriumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Cerium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Nd
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                NeodymiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Neodymium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Po
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledPromethiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                PromethiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Promethium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledSamariumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                SamariumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Samarium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Eu
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledEuropiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                EuropiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Europium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ga
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                GadoliniumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Gadolinium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Tb
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledTerbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                TerbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Terbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Dy
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                DysprosiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Dysprosium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Ho
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledHolmiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                HolmiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Holmium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Er
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledErbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                ErbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Erbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Tm
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledThuliumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                ThuliumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Thulium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Yb
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                YtterbiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Ytterbium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // Lu
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Empty.getCells(2))
            .fluidInputs(FilledLutetiumExtractingNanoResin.getFluidOrGas(1000))
            .itemOutputs(
                LutetiumExtractingNanoResin.get(OrePrefixes.cell, 1),
                GT_OreDictUnificator.get(OrePrefixes.cellMolten, Materials.Lutetium, 1))
            .fluidOutputs(Materials.Chlorine.getGas(3000))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(electrolyzerRecipes);

        // TODO ChlorinitedRareEarthConcentrate process with every 15 Rare Earth Extracting Nano Resin

        // La
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LanthanumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLanthanumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pr
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPraseodymiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ce
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                CeriumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledCeriumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Nd
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                NeodymiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledNeodymiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Pm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                PromethiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledPromethiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Sm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                SamariumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledSamariumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Eu
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                EuropiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledEuropiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ga
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                GadoliniumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledGadoliniumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tb
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                TerbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledTerbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Dy
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                DysprosiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledDysprosiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Ho
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                HolmiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledHolmiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Er
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ErbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledErbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Tm
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                ThuliumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledThuliumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Yb
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                YtterbiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledYtterbiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // Lu
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthConcentrate.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthEnrichedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);
        GT_Values.RA.stdBuilder()
            .fluidInputs(
                LutetiumExtractingNanoResin.getFluidOrGas(1000),
                ChlorinatedRareEarthDilutedSolution.getFluidOrGas(1000))
            .fluidOutputs(
                FilledLutetiumExtractingNanoResin.getFluidOrGas(1000),
                MyMaterial.wasteLiquid.getFluidOrGas(1000))
            .eut(TierEU.RECIPE_UV)
            .duration(1 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // TODO Samarium Ore Concentrate Dust Processing Line Start

        // 16 SmOreDust + 200L NitricAcid =EV@10s= 800L MuddySamariumRareEarthSolution + 1 ?ThP?ConcentrateDust
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOreConcentrate.get(OrePrefixes.dust, 16))
            .fluidInputs(Materials.NitricAcid.getFluid(200))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(800))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1 CrushedSamariumOre = 3 SamariumOreConcentrate in process
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_OreDictUnificator.get(OrePrefixes.crushed, Materials.Samarium, 8))
            .fluidInputs(Materials.NitricAcid.getFluid(300))
            .itemOutputs(ThoriumPhosphateConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(1200))
            .metadata(COIL_HEAT,800)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(digesterRecipes);

        // 1B MuddySmSolution + 1B NitricAcid =EV@10s= 2B SamariumRareEarthMud + 0.8 CeriumDioxide + 0.6
        // CeriumRichMixture(CeriumOreConcentrate)
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), MuddySamariumRareEarthSolution.getFluidOrGas(1000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1), CeriumOreConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(2000))
            .metadata(DISSOLUTION_TANK_RATIO,1)
            .outputChances(8000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(dissolutionTankRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9))
            .fluidInputs(Materials.NitricAcid.getFluid(9000), MuddySamariumRareEarthSolution.getFluidOrGas(9000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 9), CeriumOreConcentrate.get(OrePrefixes.dust, 9))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(18000))
            .metadata(DISSOLUTION_TANK_RATIO,1)
            .outputChances(8000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_IV)
            .duration(300)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.NitricAcid.getFluid(1000), MuddySamariumRareEarthSolution.getFluidOrGas(1000))
            .itemOutputs(CeriumDioxide.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumRareEarthMud.getFluidOrGas(1000))
            .outputChances(5000)
            .eut(TierEU.RECIPE_EV)
            .duration(300)
            .addTo(multiblockChemicalReactorRecipes);

        // 1B SamariumRareEarthMud + 9B water =EV@30s= 10B DilutedSamariumRareEarthSolution
        // + (90% + 60%) NeodymiumREConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(Materials.Water.getFluid(9000), SamariumRareEarthMud.getFluidOrGas(1000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 1))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(10000))
            .metadata(DISSOLUTION_TANK_RATIO,9)
            .outputChances(9000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_EV)
            .duration(600)
            .addTo(dissolutionTankRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(9))
            .fluidInputs(Materials.Water.getFluid(81000), SamariumRareEarthMud.getFluidOrGas(9000))
            .itemOutputs(
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9),
                NeodymicRareEarthConcentrate.get(OrePrefixes.dust, 9))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(90000))
            .metadata(DISSOLUTION_TANK_RATIO,9)
            .outputChances(9000, 6000)
            .noOptimize()
            .eut(TierEU.RECIPE_IV)
            .duration(900)
            .addTo(dissolutionTankRecipes);
        // Low Efficiency method in LCR
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(1))
            .fluidInputs(SamariumRareEarthMud.getFluidOrGas(1000), Materials.Water.getFluid(16000))
            .fluidOutputs(DilutedSamariumRareEarthSolution.getFluidOrGas(8000))
            .eut(TierEU.RECIPE_EV)
            .duration(1200)
            .addTo(multiblockChemicalReactorRecipes);

        // 2B DilutedSamariumRareEarthSolution + 3B Oxalate
        // =EV@10s=
        // 5 ImpureSamariumOxalate + 50L MuddySamariumRareEarthSolution + 0.1*2 LepersonniteDust
        // LepersonniteDust -> DephosphatedSamariumConcentrate
        GT_Values.RA.stdBuilder()
            .itemInputs(GT_Utility.getIntegratedCircuit(13))
            .fluidInputs(DilutedSamariumRareEarthSolution.getFluidOrGas(2000), MyMaterial.oxalate.getFluidOrGas(3000))
            .itemOutputs(
                SamariumOxalate.get(OrePrefixes.dust, 5),
                DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 3))
            .fluidOutputs(MuddySamariumRareEarthSolution.getFluidOrGas(50))
            .outputChances(10000, 1000)
            .eut(TierEU.RECIPE_EV)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        // 5 ImpureSamariumOxalate + 6B HCL = 8 ImpureSamariumChloride + 6B CO
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumOxalate.get(OrePrefixes.dust, 5))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(6000))
            .itemOutputs(SamariumChloride.get(OrePrefixes.dust, 8))
            .fluidOutputs(Materials.CarbonMonoxide.getGas(6000))
            .eut(960)
            .duration(10 * SECONDS)
            .addTo(multiblockChemicalReactorRecipes);

        /**
         * ImpureSamariumChloride has 2 method to process 1. In IV-LuV, fix with NcCL then use electrolyzer to process
         * the mixture, get Samarium dust & Chlorine & Sodium. 2. In ZPM, put molten ImpureSamariumChloride and
         * LanthanumDust in Distillation Tower to get molten Samarium and impure Lanthanum Chloride.
         */

        // 2 ImpureSamariumChloride + 1 NaCl =LV@5s= 3 SamariumChlorideSodiumChlorideBlend
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 2), Materials.Salt.getDust(1))
            .itemOutputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 3))
            .eut(30)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 2), Materials.Sodium.getDust(1))
            .itemOutputs(SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 3))
            .eut(30)
            .duration(5 * SECONDS)
            .addTo(mixerNonCellRecipes);

        // 6 SamariumChlorideSodiumChlorideBlend =IV@1s= 1 SamariumDust + 1 SodiumDust + 2/9 RarestEarthResidue + 4B
        // Chlorine
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 6))
            .itemOutputs(
                Materials.Samarium.getDust(1),
                Materials.Sodium.getDust(1),
                RarestEarthResidue.get(OrePrefixes.dustTiny, 2))
            .fluidOutputs(Materials.Chlorine.getGas(4000))
            .eut(TierEU.RECIPE_IV)
            .duration(1 * SECONDS)
            .addTo(electrolyzerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(9),
                SamariumChlorideSodiumChlorideBlend.get(OrePrefixes.dust, 54))
            .itemOutputs(
                Materials.Samarium.getDust(9),
                Materials.Sodium.getDust(9),
                RarestEarthResidue.get(OrePrefixes.dust, 2))
            .fluidOutputs(Materials.Chlorine.getGas(36000))
            .eut(30720)
            .duration(2 * SECONDS)
            .addTo(electrolyzerRecipes);

        // ZPM molten distilling method

        // melt ImpureSamariumChloride
        GT_Values.RA.stdBuilder()
            .itemInputs(SamariumChloride.get(OrePrefixes.dust, 1))
            .fluidOutputs(SamariumChloride.getMolten(144))
            .eut(TierEU.RECIPE_EV)
            .duration(24)
            .addTo(fluidExtractionRecipes);

        // distill with LanthanumDust 36*144L moltenSmCl3 = 16*144L moltenSm + 27B Cl
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Lanthanum.getDust(9))
            .itemOutputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .fluidInputs(SamariumChloride.getMolten(5184))
            .fluidOutputs(Materials.Samarium.getMolten(2304))
            .eut(TierEU.RECIPE_ZPM)
            .duration(5 * SECONDS)
            .noOptimize()
            .addTo(distillationTowerRecipes);

        // Centrifuge ImpureLanthanumChlorideDust
        GT_Values.RA.stdBuilder()
            .itemInputs(ImpureLanthanumChloride.get(OrePrefixes.dust, 36))
            .itemOutputs(LanthaniumChloride.get(OrePrefixes.dust, 36), RarestEarthResidue.get(OrePrefixes.dust, 5))
            .eut(TierEU.RECIPE_EV)
            .duration(5 * SECONDS)
            .addTo(centrifugeRecipes);

        /**
         * DephosphatedSamariumConcentrate has a simple and not shit process. Just burn in furnace, then use
         * electolyzer.
         */
        GameRegistry.addSmelting(
            DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
            SamariumOxide.get(OrePrefixes.dustTiny, 2),
            114);
        GT_Values.RA.stdBuilder()
            .itemInputs(DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1))
            .itemOutputs(SamariumOxide.get(OrePrefixes.dust, 1))
            .metadata(COIL_HEAT, 1200)
            .eut(514)
            .duration(2 * SECONDS)
            .addTo(blastFurnaceRecipes);
        // GT_Values.RA.addBlastRecipe(
        // DephosphatedSamariumConcentrate.get(OrePrefixes.dust, 1),
        // null,
        // null,
        // null,
        // SamariumOxide.get(OrePrefixes.dust, 1),
        // null,
        // 40,
        // 514,
        // 1200);

    }

    public static void addRandomChemCrafting() {

        // PTMEG Elastomer
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Butanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(4000))
            .fluidOutputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(4000))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Toluene Tetramethyl Diisocyanate
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TolueneDiisocyanate.get(OrePrefixes.cell, 3),
                Materials.Hydrogen.getCells(2))
            .itemOutputs(Materials.Empty.getCells(5))
            .fluidInputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneTetramethylDiisocyanate.getFluidOrGas(2000))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // PTHF
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.TungstophosphoricAcid.get(OrePrefixes.cell, 1),
                Materials.Oxygen.getCells(1))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(144))
            .fluidOutputs(WerkstoffMaterialPool.Polytetrahydrofuran.getFluidOrGas(432))
            .duration(50 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(UniversalChemical);

        // THF
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.AcidicButanediol.get(OrePrefixes.cell, 1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.Ethanol.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.Tetrahydrofuran.getFluidOrGas(1000))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Acidicised Butanediol
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.SulfuricAcid.getCells(1))
            .itemOutputs(Materials.Water.getCells(1))
            .fluidInputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.AcidicButanediol.getFluidOrGas(1000))
            .duration(30 * SECONDS)
            .eut(2000)
            .addTo(mixerRecipes);

        // Butanediol
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dustTiny, 1))
            .fluidInputs(Materials.Butane.getGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(9),
                WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 1))
            .fluidInputs(Materials.Butane.getGas(9000))
            .fluidOutputs(WerkstoffMaterialPool.Butanediol.getFluidOrGas(9000))
            .duration(6 * MINUTES + 45 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(multiblockChemicalReactorRecipes);

        // Moly-Te-Oxide Catalyst
        GT_Values.RA.stdBuilder()
            .itemInputs(
                WerkstoffMaterialPool.MolybdenumIVOxide.get(OrePrefixes.dust, 1),
                WerkstoffMaterialPool.TelluriumIVOxide.get(OrePrefixes.dust, 1))
            .itemOutputs(WerkstoffMaterialPool.MoTeOCatalyst.get(OrePrefixes.dust, 2))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        // Tungstophosphoric Acid
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.PhosphoricAcid.getCells(1), Materials.HydrochloricAcid.getCells(24))
            .itemOutputs(Materials.Salt.getDust(24), Materials.Empty.getCells(25))
            .fluidInputs(BotWerkstoffMaterialPool.SodiumTungstate.getFluidOrGas(12000))
            .fluidOutputs(WerkstoffMaterialPool.TungstophosphoricAcid.getFluidOrGas(1000))
            .duration(25 * SECONDS)
            .eut(1024)
            .addTo(UniversalChemical);

        // Toluene Diisocyanate
        GT_Values.RA.stdBuilder()
            .itemInputs(WerkstoffMaterialPool.Diaminotoluene.get(OrePrefixes.cell, 1), Materials.Empty.getCells(3))
            .itemOutputs()
            .fluidInputs(BotWerkstoffMaterialPool.Phosgene.getFluidOrGas(2000))
            .fluidOutputs(WerkstoffMaterialPool.TolueneDiisocyanate.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Diaminotoluene
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.Hydrogen.getCells(4))
            .itemOutputs()
            .fluidInputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000))
            .fluidOutputs(WerkstoffMaterialPool.Diaminotoluene.getFluidOrGas(1000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Dinitrotoluene
        GT_Values.RA.stdBuilder()
            .itemInputs(Materials.NitricAcid.getCells(2))
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Toluene.getFluid(1000))
            .fluidOutputs(WerkstoffMaterialPool.Dinitrotoluene.getFluidOrGas(1000))
            .duration(45 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(UniversalChemical);

        // Digester Control Block
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_IV.get(1L),
                ItemList.Super_Tank_EV.get(2L),
                ItemList.Electric_Motor_IV.get(4L),
                ItemList.Electric_Pump_IV.get(4L),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Desh, 4L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4L),
                GT_Utility.getIntegratedCircuit(1))
            .itemOutputs(LanthItemList.DIGESTER)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(1440))
            .duration(30 * SECONDS)
            .eut(4096)
            .addTo(assemblerRecipes);
        // Dissolution Tank
        GT_Values.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_EV.get(1L),
                ItemList.Super_Tank_HV.get(2L),
                ItemList.Electric_Motor_EV.get(4L),
                ItemList.Electric_Pump_EV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.VibrantAlloy, 4L),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Data, 4L),
                GT_Utility.getIntegratedCircuit(2))
            .itemOutputs(LanthItemList.DISSOLUTION_TANK)
            .fluidInputs(Materials.Polytetrafluoroethylene.getMolten(720))
            .duration(20 * SECONDS)
            .eut(960)
            .addTo(assemblerRecipes);

        GT_Values.RA.stdBuilder()
            .fluidInputs(WerkstoffMaterialPool.DilutedAcetone.getFluidOrGas(250))
            .fluidOutputs(Materials.Acetone.getFluid(150))
            .duration(6 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(vacuumFreezerRecipes);

        // PTMEG Manipulation

        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Ingot.get(0L))
            .itemOutputs(WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.ingot, 1))
            .fluidInputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(144))
            .duration(2 * SECONDS)
            .eut(64)
            .addTo(fluidSolidifierRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(ItemList.Shape_Mold_Plate.get(0L))
            .itemOutputs(WerkstoffMaterialPool.PTMEGElastomer.get(OrePrefixes.plate, 1))
            .fluidInputs(WerkstoffMaterialPool.PTMEGElastomer.getMolten(144))
            .duration(2 * SECONDS)
            .eut(64)
            .addTo(fluidSolidifierRecipes);

        // TODO Cerium-doped Lutetium Aluminium Garnet (Ce:LuAG)
        /**
         * 1/9 Ce + 3 Lu + 5 Sapphire = 8 LuAG Blend 1/9 Ce + 3 Lu + 10 Green Sapphire = 8 LuAG Blend 2/9 Ce + 6 Lu + 25
         * Alumina + 9 Oxygen = 12 LuAG Blend
         *
         * 1 Ce + 60 Lu + 100 Sapphire = 160 LuAG Blend 1 Ce + 60 Lu +200 Green Sapphire = 160 LuAG Blend
         *
         */
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(1),
                Materials.Lutetium.getDust(3),
                Materials.Sapphire.getDust(5))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 8))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(1),
                Materials.Lutetium.getDust(3),
                Materials.GreenSapphire.getDust(10))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 8))
            .eut(TierEU.RECIPE_UV)
            .duration(5 * SECONDS)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(4),
                Materials.Cerium.getDustTiny(2),
                Materials.Lutetium.getDust(6),
                Materials.Aluminiumoxide.getDust(25))
            .itemOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 12))
            .fluidInputs(Materials.Oxygen.getGas(9000))
            .eut(TierEU.RECIPE_UV)
            .duration(400)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Cerium.getDust(1),
                Materials.Lutetium.getDust(60),
                Materials.Sapphire.getDust(64),
                Materials.Sapphire.getDust(36))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 32))
            .eut(TierEU.RECIPE_UV)
            .duration(1800)
            .addTo(mixerRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(5),
                Materials.Cerium.getDust(1),
                Materials.Lutetium.getDust(60),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(64),
                Materials.GreenSapphire.getDust(8))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 64),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 32))
            .eut(TierEU.RECIPE_UV)
            .duration(1800)
            .addTo(mixerRecipes);

        // Get LuAG Crystal seed
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .fluidInputs(Materials.Lutetium.getMolten(144 * 8))
            .outputChances(514)
            .eut(500000)
            .duration(500)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1.1(Og) 1.0(Xe) 0.99(Kr) LuAG in Autoclave
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Krypton.getFluidOrGas(40))
            .outputChances(8000, 1900)
            .eut(TierEU.RECIPE_UHV)
            .duration(512)
            .addTo(autoclaveRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Xenon.getFluidOrGas(25))
            .outputChances(9000, 1000)
            .eut(TierEU.RECIPE_UHV)
            .duration(256)
            .addTo(autoclaveRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                GT_Utility.getIntegratedCircuit(1),
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(10))
            .outputChances(10000, 100)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(
                CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gem, 1))
            .itemOutputs(
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1),
                CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 2))
            .fluidInputs(WerkstoffLoader.Oganesson.getFluidOrGas(10))
            .outputChances(10000, 2000)
            .eut(TierEU.RECIPE_UHV)
            .duration(128)
            .addTo(autoclaveRecipes);

        // 1 LuAG Blend = 1 LuAG in Vacuum Furnace
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.dust, 1))
            .itemOutputs(CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
            .fluidInputs(CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(108))
            .specialValue(9100)
            .eut(TierEU.RECIPE_UHV)
            .duration(5 * SECONDS)
            .addTo(vacuumFurnaceRecipes);
        GT_Values.RA.stdBuilder()
            .itemInputs(CeriumDopedLutetiumAluminiumOxygenBlend.get(OrePrefixes.dust, 1))
            .fluidOutputs(CeriumDopedLutetiumAluminiumOxygenBlend.getMolten(144))
            .eut(30000)
            .duration(1 * SECONDS)
            .addTo(fluidExtractionRecipes);

        // 16 Adv Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensBlue")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC2.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

        // 16 Crystal SoC
        for (ItemStack itemStack : OreDictionary.getOres("craftingLensGreen")) {
            GT_Values.RA.stdBuilder()
                .itemInputs(
                    GT_Utility.copyAmount(0, itemStack),
                    CeriumDopedLutetiumAluminiumGarnet.get(OrePrefixes.gemExquisite, 1))
                .itemOutputs(ItemList.Circuit_Chip_CrystalSoC.get(16))
                .requiresCleanRoom()
                .eut(160000)
                .duration(40 * SECONDS)
                .addTo(laserEngraverRecipes);
        }

    }

    // public static void loadZylon

    public static void removeCeriumSources() {

        GT_Log.out.print(Tags.MODID + ": AAAAAA");

        HashSet<GT_Recipe> remove = new HashSet<>(5000);
        HashSet<GT_Recipe> reAdd = new HashSet<>(5000);

        // For Crusher
        for (GT_Recipe recipe : maceratorRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            // GT_Log.out.print("\n" + input.getDisplayName());
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID)
                        .startsWith("ore")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("rawOre")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("crushed"))) {
                        GT_Log.out.print(OreDictionary.getOreName(oreDictID));
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        maceratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(maceratorRecipes::add);
        maceratorRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Crusher done!\n");

        // For Washer
        for (GT_Recipe recipe : oreWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed") /* && OreDictionary.getOreName(oreDictID).contains("Cerium") */) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        oreWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(oreWasherRecipes::add);
        oreWasherRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Washer done!\n");

        // For Thermal Centrifuge
        for (GT_Recipe recipe : thermalCentrifugeRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if ((OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("purified")) /* && OreDictionary.getOreName(oreDictID).contains("Cerium") */) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        thermalCentrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(thermalCentrifugeRecipes::add);
        thermalCentrifugeRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Thermal Centrifuge done!\n");

        // For Centrifuge
        for (GT_Recipe recipe : centrifugeRecipes.getAllRecipes()) {
            ItemStack input = null;
            FluidStack fluidInput = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (recipe.mFluidInputs.length > 0) fluidInput = recipe.mFluidInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dust")
                        && (!OreDictionary.getOreName(oreDictID)
                            .contains(
                                "Dephosphated")) /*
                                                  * OreDictionary.getOreName(oreDictID).startsWith("dustPureCerium")
                                                  * || OreDictionary.getOreName(oreDictID).startsWith(
                                                  * "dustImpureCerium") ||
                                                  * OreDictionary.getOreName(oreDictID).startsWith("dustSpace") ||
                                                  * OreDictionary.getOreName(oreDictID).startsWith("dustCerium")
                                                  */) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
            /*
             * GT_Recipe tRecipe = recipe.copy(); if (GT_Utility.isStackValid(fluidInput)) { if
             * (fluidInput.getLocalizedName() == MyMaterial.plutoniumBasedLiquidFuel.getDefaultName()) {
             * tRecipe.mOutputs[1] = GT_Utility.copyAmount(tRecipe.mOutputs[1].stackSize * 2,
             * WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1)); reAdd.add(tRecipe);
             * remove.add(tRecipe); } }
             */
        }
        centrifugeRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(centrifugeRecipes::add);
        centrifugeRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Centrifuge done!\n");

        // For Centrifuge (PA)
        for (GT_Recipe recipe : centrifugeNonCellRecipes.getAllRecipes()) {
            ItemStack input = null;
            FluidStack fluidInput = null;
            if (recipe.mInputs.length > 0) input = recipe.mInputs[0];
            if (recipe.mFluidInputs.length > 0) fluidInput = recipe.mFluidInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dust")
                        && (!OreDictionary.getOreName(oreDictID)
                            .contains("Dephosphated"))) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustTiny(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustTiny, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDustSmall(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dustSmall, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        centrifugeNonCellRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(centrifugeNonCellRecipes::add);
        centrifugeNonCellRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Centrifuge (PA) done!\n");

        // For Hammer
        for (GT_Recipe recipe : hammerRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("crushed") /* && OreDictionary.getOreName(oreDictID).contains("Cerium") */) {
                        GT_Recipe tRecipe = recipe.copy();
                        boolean modified = false;
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                modified = true;
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize * 2,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                modified = true;
                            }
                        }
                        if (modified) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        hammerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(hammerRecipes::add);
        hammerRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Hammer done!\n");

        // Electrolyzer
        for (GT_Recipe recipe : electrolyzerRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (GT_Utility.isStackValid(input)) {
                    GT_Log.out.print(input.getDisplayName() + "\n");
                    int[] oreDict = OreDictionary.getOreIDs(input);
                    for (int oreDictID : oreDict) {
                        String oreName = OreDictionary.getOreName(oreDictID);
                        if (oreName.equals("dustHibonite") || oreName.equals("dustLanthaniteCe")
                            || oreName.equals("dustZirconolite")
                            || oreName.equals("dustYttrocerite")
                            || oreName.equals("dustXenotime")
                            || oreName.equals("dustBastnasite")
                            || oreName.equals("dustFlorencite")) {
                            GT_Recipe tRecipe = recipe.copy();
                            boolean modified = false;
                            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                                if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                                if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize,
                                        WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                    modified = true;
                                } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize,
                                        WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                    modified = true;
                                }
                            }
                            if (modified) {
                                reAdd.add(tRecipe);
                                remove.add(recipe);
                            }
                            break;
                        }
                    }
                }
            }
        }

        electrolyzerRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(electrolyzerRecipes::add);
        electrolyzerRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Electrolyzer done!\n");

        // Electrolyzer (PA)
        for (GT_Recipe recipe : electrolyzerNonCellRecipes.getAllRecipes()) {
            for (ItemStack input : recipe.mInputs) {
                if (GT_Utility.isStackValid(input)) {
                    GT_Log.out.print(input.getDisplayName() + "\n");
                    int[] oreDict = OreDictionary.getOreIDs(input);
                    for (int oreDictID : oreDict) {
                        String oreName = OreDictionary.getOreName(oreDictID);
                        if (oreName.equals("dustHibonite") || oreName.equals("dustLanthaniteCe")
                            || oreName.equals("dustZirconolite")
                            || oreName.equals("dustYttrocerite")
                            || oreName.equals("dustXenotime")
                            || oreName.equals("dustBastnasite")
                            || oreName.equals("dustFlorencite")) {
                            GT_Recipe tRecipe = recipe.copy();
                            boolean modified = false;
                            for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                                if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                                if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize,
                                        WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                                    modified = true;
                                } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                    tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                        tRecipe.mOutputs[i].stackSize,
                                        WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                                    modified = true;
                                }
                            }
                            if (modified) {
                                reAdd.add(tRecipe);
                                remove.add(recipe);
                            }
                            break;
                        }
                    }
                }
            }
        }

        electrolyzerNonCellRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(electrolyzerNonCellRecipes::add);
        electrolyzerNonCellRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Electrolyzer (PA) done!\n");

        // For Simple Washer
        for (GT_Recipe recipe : simpleWasherRecipes.getAllRecipes()) {
            ItemStack input = recipe.mInputs[0];
            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    if (OreDictionary.getOreName(oreDictID)
                        .startsWith("dustImpureCerium")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustImpureSamarium")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustPureSamarium")
                        || OreDictionary.getOreName(oreDictID)
                            .startsWith("dustPureCerium")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            } else if (tRecipe.mOutputs[i].isItemEqual(Materials.Samarium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }
        simpleWasherRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(simpleWasherRecipes::add);
        simpleWasherRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Simple Washer done!\n");

        // Dehydrator
        for (GT_Recipe recipe : chemicalDehydratorRecipes.getAllRecipes()) {
            if (recipe.mInputs.length == 0) {
                continue;
            }
            ItemStack input = recipe.mInputs[0];

            if (GT_Utility.isStackValid(input)) {
                int[] oreDict = OreDictionary.getOreIDs(input);
                for (int oreDictID : oreDict) {
                    String oreName = OreDictionary.getOreName(oreDictID);
                    if (oreName.equals("dustCerite") || oreName.equals("dustFluorcaphite")
                        || oreName.equals("dustZirkelite")
                        || oreName.equals("dustGadoliniteCe")
                        || oreName.equals("dustGadoliniteY")
                        || oreName.equals("dustPolycrase")
                        || oreName.equals("dustBastnasite")) {
                        GT_Recipe tRecipe = recipe.copy();
                        for (int i = 0; i < tRecipe.mOutputs.length; i++) {
                            if (!GT_Utility.isStackValid(tRecipe.mOutputs[i])) continue;
                            if (tRecipe.mOutputs[i].isItemEqual(Materials.Cerium.getDust(1))) {
                                tRecipe.mOutputs[i] = GT_Utility.copyAmount(
                                    tRecipe.mOutputs[i].stackSize,
                                    WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 1));
                            }
                        }
                        if (!tRecipe.equals(recipe)) {
                            reAdd.add(tRecipe);
                            remove.add(recipe);
                        }
                        break;
                    }
                }
            }
        }

        chemicalDehydratorRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(chemicalDehydratorRecipes::add);
        chemicalDehydratorRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Dehydrator done!\n");

        /*
         * DOES NOT WORK, something to do with load times for sifter recipes or some shit //Sifter for (GT_Recipe recipe
         * : sifterRecipes.mRecipeList) { if (recipe.mInputs.length == 0) break; ItemStack input = recipe.mInputs[0];
         * GT_Log.out.print("Sift ore found " + input.getDisplayName() + "\n"); if (GT_Utility.isStackValid(input)) { if
         * (true) { GT_Log.out.print("Sift ore found and iffed " + input.getDisplayName() + "\n"); //GT_Recipe tRecipe =
         * recipe.copy(); remove.add(recipe); break; } } } sifterRecipes.mRecipeList.removeAll(remove);
         * sifterRecipes.mRecipeList.addAll(reAdd); sifterRecipes.reInit(); GT_Log.out.print(Tags.MODID + ": Replace " +
         * remove.size() + "! "); remove.clear(); reAdd.clear(); GT_Log.out.print("Sifter done!\n");
         */
        // Chemical Bath
        for (GT_Recipe recipe : chemicalBathRecipes.getAllRecipes()) {
            // ItemStack input = recipe.mInputs[0];
            for (ItemStack input : recipe.mInputs) {
                GT_Log.out.print(input.getDisplayName() + "\n");
                if (GT_Utility.isStackValid(input)) {
                    int[] oreDict = OreDictionary.getOreIDs(input);
                    for (int oreDictID : oreDict) {
                        String oreName = OreDictionary.getOreName(oreDictID);
                        if (oreName.equals("dustTin") || oreName.equals("dustRutile")) {
                            GT_Recipe tRecipe = recipe.copy();
                            remove.add(recipe);
                            break;
                        }
                    }
                }
            }
        }

        chemicalBathRecipes.getBackend()
            .removeRecipes(remove);
        reAdd.forEach(chemicalBathRecipes::add);
        chemicalBathRecipes.getBackend()
            .reInit();

        GT_Log.out.print(Tags.MODID + ": Replace " + remove.size() + "! ");

        remove.clear();
        reAdd.clear();

        GT_Log.out.print("Chemical Bath done!\n");

        // For Cauldron Wash
        registerCauldronCleaningFor(Materials.Cerium, WerkstoffMaterialPool.CeriumRichMixture.getBridgeMaterial());
        registerCauldronCleaningFor(
            Materials.Samarium,
            WerkstoffMaterialPool.SamariumOreConcentrate.getBridgeMaterial());
        GT_Log.out.print(Tags.MODID + ": Replace 3! ");
        GT_Log.out.print("Cauldron Wash done!\n");

        // For Crafting Table
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(RecipeLoader::replaceInCraftTable);

        GT_Log.out.print(Tags.MODID + ": Replace Unknown! ");
        GT_Log.out.print("Crafting Table done!\n");
    }

    // below are taken from GoodGenerator

    // I don't understand. . .
    // I use and copy some private methods in Bartworks because his system runs well.
    // Bartworks is under MIT License
    /*
     * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a
     * copy of this software and associated documentation files (the "Software"), to deal in the Software without
     * restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute,
     * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so,
     * subject to the following conditions: The above copyright notice and this permission notice shall be included in
     * all copies or substantial portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY
     * KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A
     * PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
     * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR
     * IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
     */
    public static void replaceInCraftTable(Object obj) {

        Constructor<?> cs = null;
        PlatinumSludgeOverHaul BartObj = null;
        try {
            cs = PlatinumSludgeOverHaul.class.getDeclaredConstructor();
            cs.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }

        if (cs == null) return;

        try {
            BartObj = (PlatinumSludgeOverHaul) cs.newInstance();
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }

        Method recipeCheck = null;

        try {
            recipeCheck = PlatinumSludgeOverHaul.class.getDeclaredMethod("checkRecipe", Object.class, Materials.class);
            recipeCheck.setAccessible(true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String inputName = "output";
        String inputItemName = "input";
        if (!(obj instanceof ShapedOreRecipe || obj instanceof ShapelessOreRecipe)) {
            if (obj instanceof ShapedRecipes || (obj instanceof ShapelessRecipes)) {
                inputName = "recipeOutput";
                inputItemName = "recipeItems";
            }
        }
        IRecipe recipe = (IRecipe) obj;
        ItemStack result = recipe.getRecipeOutput();

        Field out = FieldUtils.getDeclaredField(recipe.getClass(), inputName, true);
        if (out == null) out = FieldUtils.getField(recipe.getClass(), inputName, true);

        Field in = FieldUtils.getDeclaredField(recipe.getClass(), inputItemName, true);
        if (in == null) in = FieldUtils.getField(recipe.getClass(), inputItemName, true);
        if (in == null) return;

        // this part here is NOT MIT LICENSED BUT LICSENSED UNDER THE Apache License, Version 2.0!
        try {
            if (Modifier.isFinal(in.getModifiers())) {
                // Do all JREs implement Field with a private ivar called "modifiers"?
                Field modifiersField = Field.class.getDeclaredField("modifiers");
                boolean doForceAccess = !modifiersField.isAccessible();
                if (doForceAccess) {
                    modifiersField.setAccessible(true);
                }
                try {
                    modifiersField.setInt(in, in.getModifiers() & ~Modifier.FINAL);
                } finally {
                    if (doForceAccess) {
                        modifiersField.setAccessible(false);
                    }
                }
            }
        } catch (NoSuchFieldException ignored) {
            // The field class contains always a modifiers field
        } catch (IllegalAccessException ignored) {
            // The modifiers field is made accessible
        }
        // END OF APACHE COMMONS COLLECTION COPY

        Object input;
        try {
            input = in.get(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return;
        }

        if (out == null || recipeCheck == null) return;

        if (GT_Utility.areStacksEqual(result, Materials.Cerium.getDust(1), true)) {

            recipeCheck.setAccessible(true);
            boolean isOk = true;

            try {
                isOk = (boolean) recipeCheck.invoke(BartObj, input, Materials.Cerium);
            } catch (InvocationTargetException | IllegalAccessException ignored) {}

            if (isOk) return;
            try {
                out.set(recipe, WerkstoffMaterialPool.CeriumRichMixture.get(OrePrefixes.dust, 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        } else if (GT_Utility.areStacksEqual(result, Materials.Samarium.getDust(1), true)) {

            recipeCheck.setAccessible(true);
            boolean isOk = true;

            try {
                isOk = (boolean) recipeCheck.invoke(BartObj, input, Materials.Samarium);
            } catch (InvocationTargetException | IllegalAccessException ignored) {}

            if (isOk) return;
            try {
                out.set(recipe, WerkstoffMaterialPool.SamariumOreConcentrate.get(OrePrefixes.dust, 2));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
