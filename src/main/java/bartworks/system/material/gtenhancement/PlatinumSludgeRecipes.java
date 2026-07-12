/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.system.material.gtenhancement;

import static bartworks.system.material.WerkstoffLoader.AcidicIridiumSolution;
import static bartworks.system.material.WerkstoffLoader.AcidicOsmiumSolution;
import static bartworks.system.material.WerkstoffLoader.AmmoniumChloride;
import static bartworks.system.material.WerkstoffLoader.AquaRegia;
import static bartworks.system.material.WerkstoffLoader.CalciumChloride;
import static bartworks.system.material.WerkstoffLoader.CrudeRhMetall;
import static bartworks.system.material.WerkstoffLoader.FormicAcid;
import static bartworks.system.material.WerkstoffLoader.HotRutheniumTetroxideSollution;
import static bartworks.system.material.WerkstoffLoader.IrLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IrOsLeachResidue;
import static bartworks.system.material.WerkstoffLoader.IridiumChloride;
import static bartworks.system.material.WerkstoffLoader.IridiumDioxide;
import static bartworks.system.material.WerkstoffLoader.LeachResidue;
import static bartworks.system.material.WerkstoffLoader.OsmiumSolution;
import static bartworks.system.material.WerkstoffLoader.PDAmmonia;
import static bartworks.system.material.WerkstoffLoader.PDMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PDRawPowder;
import static bartworks.system.material.WerkstoffLoader.PDSalt;
import static bartworks.system.material.WerkstoffLoader.PGSDResidue;
import static bartworks.system.material.WerkstoffLoader.PGSDResidue2;
import static bartworks.system.material.WerkstoffLoader.PTConcentrate;
import static bartworks.system.material.WerkstoffLoader.PTMetallicPowder;
import static bartworks.system.material.WerkstoffLoader.PTRawPowder;
import static bartworks.system.material.WerkstoffLoader.PTResidue;
import static bartworks.system.material.WerkstoffLoader.PTSaltCrude;
import static bartworks.system.material.WerkstoffLoader.PTSaltRefined;
import static bartworks.system.material.WerkstoffLoader.PotassiumDisulfate;
import static bartworks.system.material.WerkstoffLoader.RHFilterCakeSolution;
import static bartworks.system.material.WerkstoffLoader.RHNitrate;
import static bartworks.system.material.WerkstoffLoader.RHSalt;
import static bartworks.system.material.WerkstoffLoader.RHSaltSolution;
import static bartworks.system.material.WerkstoffLoader.RHSulfate;
import static bartworks.system.material.WerkstoffLoader.RHSulfateSolution;
import static bartworks.system.material.WerkstoffLoader.ReRh;
import static bartworks.system.material.WerkstoffLoader.RhFilterCake;
import static bartworks.system.material.WerkstoffLoader.Rhodium;
import static bartworks.system.material.WerkstoffLoader.Ruthenium;
import static bartworks.system.material.WerkstoffLoader.RutheniumTetroxide;
import static bartworks.system.material.WerkstoffLoader.RutheniumTetroxideSollution;
import static bartworks.system.material.WerkstoffLoader.SodiumNitrate;
import static bartworks.system.material.WerkstoffLoader.SodiumRuthenate;
import static bartworks.system.material.WerkstoffLoader.Sodiumformate;
import static bartworks.system.material.WerkstoffLoader.Sodiumsulfate;
import static bartworks.system.material.WerkstoffLoader.ZincSulfate;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.OrePrefixes.cell;
import static gregtech.api.enums.OrePrefixes.crushed;
import static gregtech.api.enums.OrePrefixes.crushedCentrifuged;
import static gregtech.api.enums.OrePrefixes.crushedPurified;
import static gregtech.api.enums.OrePrefixes.dust;
import static gregtech.api.enums.OrePrefixes.dustImpure;
import static gregtech.api.enums.OrePrefixes.dustPure;
import static gregtech.api.enums.OrePrefixes.dustRefined;
import static gregtech.api.enums.OrePrefixes.dustSmall;
import static gregtech.api.enums.OrePrefixes.dustTiny;
import static gregtech.api.enums.OrePrefixes.ingot;
import static gregtech.api.enums.OrePrefixes.nugget;
import static gregtech.api.enums.OrePrefixes.rawOre;
import static gregtech.api.recipe.RecipeMaps.blastFurnaceRecipes;
import static gregtech.api.recipe.RecipeMaps.centrifugeRecipes;
import static gregtech.api.recipe.RecipeMaps.chemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.circuitAssemblerRecipes;
import static gregtech.api.recipe.RecipeMaps.distillationTowerRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidExtractionRecipes;
import static gregtech.api.recipe.RecipeMaps.fluidHeaterRecipes;
import static gregtech.api.recipe.RecipeMaps.fusionRecipes;
import static gregtech.api.recipe.RecipeMaps.mixerRecipes;
import static gregtech.api.recipe.RecipeMaps.multiblockChemicalReactorRecipes;
import static gregtech.api.recipe.RecipeMaps.packagerRecipes;
import static gregtech.api.recipe.RecipeMaps.replicatorRecipes;
import static gregtech.api.recipe.RecipeMaps.sifterRecipes;
import static gregtech.api.recipe.RecipeMaps.unpackagerRecipes;
import static gregtech.api.util.GTRecipeBuilder.HALF_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;
import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;
import static gregtech.api.util.GTRecipeConstants.UniversalChemical;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.alloyBlastSmelterRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.quantumForceTransformerRecipes;
import static gtPlusPlus.api.recipe.GTPPRecipeMaps.vacuumFurnaceRecipes;
import static gtPlusPlus.core.material.MaterialsAlloy.HELICOPTER;
import static kubatech.loaders.HTGRLoader.HTGRRecipes;
import static tectech.recipe.TecTechRecipeMaps.eyeOfHarmonyRecipes;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.BlockMaterialInfo;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.MainMod;
import bartworks.system.material.BWMetaGeneratedItems;
import bartworks.system.material.Werkstoff;
import bartworks.util.BWUtil;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2BlockShapes;
import gregtech.api.enums.materials2.Materials2CellShapes;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.items.GTGenericItem;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MU;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.BlockFrameBox;
import gregtech.common.blocks.GTBlockOre;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import gtPlusPlus.core.block.base.BlockBaseModular;
import gtPlusPlus.core.item.base.BaseItemComponent;
import kubatech.loaders.item.htgritem.HTGRItem;

public class PlatinumSludgeOverHaul {

    private static final List<Materials> MATERIALS_BLACKLIST = Arrays.asList(
        Materials.HSSS,
        Materials.EnderiumBase,
        Materials.Osmiridium,
        Materials.TPV,
        Materials.SuperconductorEVBase,
        Materials.SuperconductorZPMBase,
        Materials.SuperconductorUVBase);

    private static final List<OrePrefixes> ORE_PREFIXES_BLACKLIST = Arrays.asList(
        crushedCentrifuged,
        crushed,
        crushedPurified,
        dustPure,
        dustImpure,
        dustRefined,
        dust,
        dustTiny,
        dustSmall);

    private PlatinumSludgeOverHaul() {}

    private static void runHelperrecipes() {
        // DilutedSulfuricAcid
        // 2H2SO4 + H2O = 3H2SO4(d)
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, (int) (2)),
                Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.DilutedSulfuricAcid, Materials2CellShapes.cell, (int) (3)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.Water.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, (int) (2)))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(2))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.DilutedSulfuricAcid,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (3_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // FormicAcid
        // CO + NaOH = CHO2Na
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.CarbonMonoxide, Materials2CellShapes.cell, (int) (1)),
                Materials.SodiumHydroxide.getDust(3))
            .itemOutputs(Sodiumformate.get(cell))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2SO4 + 2CHO2Na = 2CH2O2 + Na2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(Sodiumformate.get(cell, 2))
            .circuit(1)
            .itemOutputs(FormicAcid.get(cell, 2), Sodiumsulfate.get(dust, 7))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.SulfuricAcid, Materials2CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1), Sodiumsulfate.get(dust, 7))
            .fluidInputs(Sodiumformate.getFluidOrGas(2_000))
            .fluidOutputs(FormicAcid.getFluidOrGas(2_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // AquaRegia
        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.HydrochloricAcid.getCells(3),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(AquaRegia.get(cell, 4))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                Materials.HydrochloricAcid.getCells(3),
                MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.cell, (int) (1)))
            .circuit(2)
            .itemOutputs(Materials.Empty.getCells(4))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NitricAcid, Materials2CellShapes.cell, (int) (1)))
            .circuit(3)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(3_000))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(3))
            .circuit(4)
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(AquaRegia.getFluidOrGas(4_000))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // AmmoniumChloride
        // NH3 + HCl = NH4Cl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(AmmoniumChloride.get(cell, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(Materials.HydrochloricAcid.getCells(1))
            .circuit(1)
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(AmmoniumChloride.getFluidOrGas(1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (64_000)),
                Materials.HydrochloricAcid.getFluid(64_000))
            .fluidOutputs(AmmoniumChloride.getFluidOrGas(64_000))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // base solution
        for (Werkstoff werkstoff : Werkstoff.werkstoffHashSet) {
            if (werkstoff.containsStuff(Materials.Sulfur)
                && (werkstoff.containsStuff(Materials.Copper) || werkstoff.containsStuff(Materials.Nickel))) {

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified))
                    .circuit(1)
                    .fluidInputs(AquaRegia.getFluidOrGas(300))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(300))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified, 9))
                    .circuit(9)
                    .fluidInputs(AquaRegia.getFluidOrGas(2_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
                    .duration(11 * SECONDS + 5 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(werkstoff.get(crushedPurified, 9), PTMetallicPowder.get(dust, 9))
                    .itemOutputs(PTResidue.get(dust))
                    .fluidInputs(AquaRegia.getFluidOrGas(20_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
            }
        }

        for (Materials material : Materials.values()) {
            if (materialsContains(material, Materials.Sulfur)
                && (materialsContains(material, Materials.Copper) || materialsContains(material, Materials.Nickel))) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 1))
                    .circuit(1)
                    .fluidInputs(AquaRegia.getFluidOrGas(300))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(300))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9))
                    .circuit(9)
                    .fluidInputs(AquaRegia.getFluidOrGas(2_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(2_700))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9), PTMetallicPowder.get(dust, 9))
                    .itemOutputs(PTResidue.get(dust))
                    .fluidInputs(AquaRegia.getFluidOrGas(20_700))
                    .fluidOutputs(PTConcentrate.getFluidOrGas(20_700))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

            }
        }

        // Pt
        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.nugget, (int) (2)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, MU.meltingPoint(Materials.Platinum))
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust))
            .circuit(1)
            .itemOutputs(PTResidue.get(dustTiny))
            .fluidInputs(AquaRegia.getFluidOrGas(2_000))
            .fluidOutputs(PTConcentrate.getFluidOrGas(2_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PTMetallicPowder.get(dust, 9))
            .circuit(9)
            .itemOutputs(PTResidue.get(dust))
            .fluidInputs(AquaRegia.getFluidOrGas(18_000))
            .fluidOutputs(PTConcentrate.getFluidOrGas(18_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PTConcentrate.get(cell, 4))
            .itemOutputs(
                PTSaltCrude.get(dustTiny, 16),
                PTRawPowder.get(dustTiny, 4),
                MaterialLibAPI.getStack(Materials2Materials.NitrogenDioxide, Materials2CellShapes.cell, (int) (1)),
                Materials.HydrochloricAcid.getCells(3))
            .fluidInputs(AmmoniumChloride.getFluidOrGas(400))
            .fluidOutputs(PDAmmonia.getFluidOrGas(400))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(PTSaltCrude.get(dustTiny, 16), PTRawPowder.get(dustTiny, 4))
            .fluidInputs(PTConcentrate.getFluidOrGas(4_000), AmmoniumChloride.getFluidOrGas(400))
            .fluidOutputs(
                PDAmmonia.getFluidOrGas(400),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (1_000)),
                Materials.HydrochloricAcid.getFluid(3_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(PTSaltCrude.get(dust, 16), PTRawPowder.get(dust, 4))
            .fluidInputs(PTConcentrate.getFluidOrGas(36_000), AmmoniumChloride.getFluidOrGas(3_600))
            .fluidOutputs(
                PDAmmonia.getFluidOrGas(3_600),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitrogenDioxide, Materials2FluidShapes.fluidGas, (int) (9_000)),
                Materials.HydrochloricAcid.getFluid(27_000))
            .duration(700)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PTSaltCrude.get(dust))
            .itemOutputs(
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust),
                PTSaltRefined.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PTSaltRefined.get(dust))
            .circuit(1)
            .itemOutputs(PTMetallicPowder.get(dust))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (87)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 900)
            .addTo(blastFurnaceRecipes);

        // 2PtCl + Ca = 2Pt + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(
                PTRawPowder.get(dust, 4),
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, (int) (2)),
                CalciumChloride.get(dust, 3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Pd
        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust))
            .circuit(1)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust))
            .circuit(1)
            .itemOutputs(PDSalt.get(dustTiny, 16), PDRawPowder.get(dustTiny, 2))
            .fluidInputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PDMetallicPowder.get(dust, 9))
            .circuit(9)
            .itemOutputs(PDSalt.get(dust, 16), PDRawPowder.get(dust, 2))
            .fluidInputs(PDAmmonia.getFluidOrGas(9_000))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(PDSalt.get(dust))
            .fluidInputs(PDAmmonia.getFluidOrGas(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(PDSalt.get(dust))
            .itemOutputs(
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust),
                PDMetallicPowder.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PDRawPowder.get(dust, 4), Materials.Empty.getCells(1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials2Materials.Ethylene, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(FormicAcid.getFluidOrGas(4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (4_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(PDRawPowder.get(dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.dust, (int) (2)))
            .fluidInputs(FormicAcid.getFluidOrGas(4_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (4_000)),
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ethylene, Materials2FluidShapes.fluidGas, (int) (1_000)),
                Materials.Water.getFluid(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // Na2SO4 + 2H = 2Na + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                Sodiumsulfate.get(dust, 7),
                MaterialLibAPI.getStack(Materials2Materials.Hydrogen, Materials2CellShapes.cell, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (2)),
                Materials.Empty.getCells(2))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SulfuricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Rh/Os/Ir/Ru
        GTValues.RA.stdBuilder()
            .itemInputs(PTResidue.get(dust))
            .circuit(11)
            .itemOutputs(LeachResidue.get(dust))
            .fluidInputs(PotassiumDisulfate.getMolten(2 * INGOTS + 1 * HALF_INGOTS))
            .fluidOutputs(RHSulfate.getFluidOrGas(360))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        // Ru
        GTValues.RA.stdBuilder()
            .itemInputs(
                LeachResidue.get(dust, 10),
                MaterialLibAPI.getStack(Materials2Materials.Saltpeter, Materials2Shapes.dust, (int) (10)))
            .itemOutputs(SodiumRuthenate.get(dust, 3), IrOsLeachResidue.get(dust, 6))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.SaltWater, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(Materials.Steam.getGas(1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                SodiumRuthenate.get(dust, 6),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (3)))
            .itemOutputs(Materials.Empty.getCells(3))
            .fluidOutputs(RutheniumTetroxideSollution.getFluidOrGas(9_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .fluidInputs(RutheniumTetroxideSollution.getFluidOrGas(1_000))
            .fluidOutputs(HotRutheniumTetroxideSollution.getFluidOrGas(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, (int) (6)))
            .fluidInputs(HotRutheniumTetroxideSollution.getFluidOrGas(9_000))
            .fluidOutputs(Materials.Water.getFluid(1_800), RutheniumTetroxide.getFluidOrGas(7_200))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(RutheniumTetroxide.get(dust, 1), Materials.HydrochloricAcid.getCells(6))
            .itemOutputs(
                Ruthenium.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (6)))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Os
        GTValues.RA.stdBuilder()
            .itemInputs(IrOsLeachResidue.get(dust, 2))
            .circuit(11)
            .itemOutputs(IrLeachResidue.get(dust, 1))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(500))
            .fluidOutputs(AcidicOsmiumSolution.getFluidOrGas(1_000))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(AcidicOsmiumSolution.getFluidOrGas(1_000))
            .fluidOutputs(OsmiumSolution.getFluidOrGas(100), Materials.Water.getFluid(900))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(OsmiumSolution.get(cell), Materials.HydrochloricAcid.getCells(6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Chlorine, Materials2CellShapes.cell, (int) (7)))
            .fluidOutputs(Materials.Water.getFluid(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ir
        GTValues.RA.stdBuilder()
            .itemInputs(IrLeachResidue.get(dust))
            .circuit(1)
            .itemOutputs(PGSDResidue.get(dust), IridiumDioxide.get(dust))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(IridiumDioxide.get(dust), Materials.HydrochloricAcid.getCells(1))
            .itemOutputs(Materials.Empty.getCells(1))
            .fluidOutputs(AcidicIridiumSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(AcidicIridiumSolution.get(cell), AmmoniumChloride.get(cell, 3))
            .itemOutputs(Materials.Empty.getCells(4), IridiumChloride.get(dust))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Ammonia, Materials2FluidShapes.fluidGas, (int) (3_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                IridiumChloride.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Calcium, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(
                PGSDResidue2.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, (int) (1)))
            .fluidOutputs(CalciumChloride.getFluidOrGas(3_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Rh
        GTValues.RA.stdBuilder()
            .itemInputs(RHSulfate.get(cell, 11))
            .circuit(1)
            .itemOutputs(RHSulfateSolution.get(cell, 11), LeachResidue.get(dustTiny, 10))
            .fluidInputs(Materials.Water.getFluid(10_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Potassium, Materials2FluidShapes.fluidMolten, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(LeachResidue.get(dust, 4))
            .fluidInputs(Materials.Water.getFluid(36_000), RHSulfate.getFluidOrGas(39_600))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Potassium,
                    Materials2FluidShapes.fluidMolten,
                    (int) (50 * INGOTS)),
                RHSulfateSolution.getFluidOrGas(39_600))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Zinc, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(ZincSulfate.get(dust, 6), CrudeRhMetall.get(dust))
            .fluidInputs(RHSulfateSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                CrudeRhMetall.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(RHSalt.get(dust, 3))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(RHSalt.get(dust, 1))
            .fluidInputs(Materials.Water.getFluid(200))
            .fluidOutputs(RHSaltSolution.getFluidOrGas(200))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(SodiumNitrate.get(dust, 5))
            .circuit(1)
            .itemOutputs(
                RHNitrate.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Salt, Materials2Shapes.dust, (int) (2)))
            .fluidInputs(RHSaltSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Na + HNO3 = NaNO3 + H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(SodiumNitrate.get(dust, 5))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.NitricAcid, Materials2FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(RHNitrate.get(dust))
            .itemOutputs(
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust),
                RhFilterCake.get(dust))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(RhFilterCake.get(dust))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(RHFilterCakeSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(ReRh.get(dust))
            .fluidInputs(RHFilterCakeSolution.getFluidOrGas(1_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(ReRh.get(dust), Materials.Empty.getCells(1))
            .itemOutputs(
                Rhodium.get(dust),
                MaterialLibAPI.getStack(Materials2Materials.Ammonia, Materials2CellShapes.cell, (int) (1)))
            .fluidInputs(Materials.HydrochloricAcid.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

    }

    private static boolean materialsContains(Materials one, ISubTagContainer other) {
        if (one == null || one.mMaterialList == null || one.mMaterialList.isEmpty()) return false;
        for (MaterialStack stack : one.mMaterialList) if (stack.mMaterial == other) return true;
        return false;
    }

    public static boolean isMapIgnored(RecipeMap<?> map) {
        return map == fusionRecipes || map == unpackagerRecipes
            || map == packagerRecipes
            || map == replicatorRecipes
            || map == eyeOfHarmonyRecipes
            || map == quantumForceTransformerRecipes
            || map == fluidExtractionRecipes
            || map == alloyBlastSmelterRecipes
            || map == HTGRRecipes
            || map == vacuumFurnaceRecipes;
    }

    public static String displayRecipe(GTRecipe recipe) {
        StringBuilder result = new StringBuilder();
        // item inputs
        result.append("Item inputs: ");
        for (ItemStack itemstack : recipe.mInputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid inputs
        result.append(" Fluid inputs: ");
        for (FluidStack fluidStack : recipe.mFluidInputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // item outputs
        result.append(" Item outputs: ");
        for (ItemStack itemstack : recipe.mOutputs) {
            if (itemstack == null) {
                result.append("nullstack, ");
            } else {
                result.append(itemstack.getUnlocalizedName());
                result.append(", ");
            }
        }

        // fluid outputs
        result.append(" Fluid outputs: ");
        for (FluidStack fluidStack : recipe.mFluidOutputs) {
            if (fluidStack == null) {
                result.append("nullstack, ");
            } else {
                result.append(fluidStack.getUnlocalizedName());
                result.append(", ");
            }
        }

        return result.toString();
    }

    public static void replacePureElements() {
        final ItemList[] itemList = ItemList.values();
        final ArrayList<ItemStack> availableItemList = new ArrayList<>(itemList.length);
        for (ItemList item : itemList) {
            if (item.hasBeenSet()) {
                availableItemList.add(item.get(1));
            }
        }

        // furnace
        for (Map.Entry<ItemStack, ItemStack> entry : FurnaceRecipes.smelting()
            .getSmeltingList()
            .entrySet()) {
            ItemStack input = entry.getKey();
            ItemStack output = entry.getValue();

            if (!GTUtility.isStackValid(input)) continue;
            if (!GTUtility.isStackValid(output)) continue;

            ItemData outputAssociation = GTOreDictUnificator.getAssociation(output);
            if (!BWUtil.checkStackAndPrefix(outputAssociation)) continue;

            final Werkstoff newOutput;
            if (outputAssociation.mMaterial.mMaterial == Materials.Platinum) {
                newOutput = PTMetallicPowder;
            } else if (outputAssociation.mMaterial.mMaterial == Materials.Palladium) {
                newOutput = PDMetallicPowder;
            } else {
                continue;
            }

            ItemData inputAssociation = GTOreDictUnificator.getAssociation(input);
            if (!BWUtil.checkStackAndPrefix(inputAssociation)) continue;

            if (inputAssociation.mMaterial.mMaterial == Materials.Platinum) {
                if (inputAssociation.mPrefix == dust || inputAssociation.mPrefix == dustTiny) {
                    continue;
                }
            }

            if (PlatinumSludgeOverHaul.isInBlackList(input, availableItemList)) continue;

            OrePrefixes prefix = outputAssociation.mPrefix == nugget ? dustTiny : dust;
            entry.setValue(newOutput.get(prefix, output.stackSize * 2));
        }

        // vanilla crafting
        CraftingManager.getInstance()
            .getRecipeList()
            .forEach(PlatinumSludgeOverHaul::setNewMaterialInRecipe);

        // gt crafting
        GTModHandler.sBufferRecipeList.forEach(PlatinumSludgeOverHaul::setNewMaterialInRecipe);

        // gt machines
        maploop: for (RecipeMap<?> map : RecipeMap.ALL_RECIPE_MAPS.values()) {
            if (isMapIgnored(map)) continue;

            GTLog.out.println("Processing recipe map: " + map.unlocalizedName);

            ArrayList<GTRecipe> toDelete = new ArrayList<>();

            recipeloop: for (GTRecipe recipe : map.getAllRecipes()) {
                if (recipe.mFakeRecipe) continue maploop;

                for (int i = 0; i < recipe.mFluidOutputs.length; i++) {
                    if (map == multiblockChemicalReactorRecipes || map == chemicalReactorRecipes) {
                        if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                            || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])) {
                            toDelete.add(recipe);
                            GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Iridium,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1)),
                            recipe.mFluidOutputs[i])) {
                                recipe.mFluidOutputs[i] = AcidicIridiumSolution.getFluidOrGas(1_000);
                                recipe.reloadOwner();
                                GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                            } else if (GTUtility.areFluidsEqual(
                                MaterialLibAPI.getFluidStack(
                                    Materials2Materials.Platinum,
                                    Materials2FluidShapes.fluidMolten,
                                    (int) (1)),
                                recipe.mFluidOutputs[i])) {
                                    recipe.mFluidOutputs[i] = PTConcentrate.getFluidOrGas(2_000);
                                    recipe.reloadOwner();
                                    GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                                } else if (GTUtility.areFluidsEqual(
                                    MaterialLibAPI.getFluidStack(
                                        Materials2Materials.Osmium,
                                        Materials2FluidShapes.fluidMolten,
                                        (int) (1)),
                                    recipe.mFluidOutputs[i])) {
                                        recipe.mFluidOutputs[i] = AcidicOsmiumSolution.getFluidOrGas(1_000);
                                        recipe.reloadOwner();
                                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                                    }
                    } else if (GTUtility.areFluidsEqual(Ruthenium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(Rhodium.getMolten(1), recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Iridium,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1)),
                            recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Platinum,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1)),
                            recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(
                                Materials2Materials.Osmium,
                                Materials2FluidShapes.fluidMolten,
                                (int) (1)),
                            recipe.mFluidOutputs[i])) {
                                toDelete.add(recipe);
                                GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                            }
                }

                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(recipe.mOutputs[i])) continue;

                    if ((GTUtility.areStacksEqual(Ruthenium.get(dust), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Ruthenium.get(dustImpure), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Ruthenium.get(dustPure), recipe.mOutputs[i]))
                        && !GTUtility.areStacksEqual(Ruthenium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = LeachResidue.get(dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    if ((GTUtility.areStacksEqual(Rhodium.get(dust), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Rhodium.get(dustImpure), recipe.mOutputs[i])
                        || GTUtility.areStacksEqual(Rhodium.get(dustPure), recipe.mOutputs[i]))
                        && !GTUtility.areStacksEqual(Rhodium.get(ingot), recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = CrudeRhMetall.get(dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    ItemData association = GTOreDictUnificator.getAssociation(recipe.mOutputs[i]);
                    if (!BWUtil.checkStackAndPrefix(association)) continue;

                    final Werkstoff replacementMaterial;
                    if (association.mMaterial.mMaterial == Materials.Platinum) {
                        replacementMaterial = PTMetallicPowder;
                    } else if (association.mMaterial.mMaterial == Materials.Palladium) {
                        replacementMaterial = PDMetallicPowder;
                    } else if (association.mMaterial.mMaterial == Materials.Osmium) {
                        replacementMaterial = IrOsLeachResidue;
                    } else if (association.mMaterial.mMaterial == Materials.Iridium) {
                        replacementMaterial = IrLeachResidue;
                    } else {
                        continue;
                    }

                    for (ItemStack mInput : recipe.mInputs) {
                        if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                    }

                    if (association.mPrefix == dust || association.mPrefix == dustImpure
                        || association.mPrefix == dustPure) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dust), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustSmall) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dustSmall), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustTiny) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = BWUtil.setStackSize(replacementMaterial.get(dustTiny), amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }
                }
            }

            map.getBackend()
                .removeRecipes(toDelete);
        }
        // TODO: remove EnderIO recipes

        // fix HV tier
        PlatinumSludgeOverHaul.replaceHVCircuitMaterials();
        // add new recipes
        PlatinumSludgeOverHaul.runHelperrecipes();
    }

    private static void replaceHVCircuitMaterials() {
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.Redstone, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Electrum, Materials2Shapes.dust, (int) (1)))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Electrotine, Materials2Shapes.dust, (int) (8)))
            .duration(1 * MINUTES + 30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(mixerRecipes);

        GTLog.out.println("Processing hv circuit materials (circuit assembler map)");
        for (GTRecipe recipe : circuitAssemblerRecipes.getAllRecipes()) {
            if (recipe.mEUt > 512) continue;
            if (BWUtil.checkStackAndPrefix(recipe.mOutputs[0])) {
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    ItemStack stack = recipe.mInputs[i];
                    ItemData association = GTOreDictUnificator.getAssociation(stack);
                    if (!BWUtil.checkStackAndPrefix(association)) continue;

                    if (association.mMaterial.mMaterial.equals(Materials.Platinum)) {
                        recipe.mInputs[i] = GTOreDictUnificator
                            .get(association.mPrefix, Materials.BlueAlloy, stack.stackSize);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }
                }
            }
        }
    }

    private static void setNewMaterialInRecipe(IRecipe recipe) {
        if (!(recipe instanceof IRecipeMutableAccess mutableRecipe)) {
            return;
        }

        Object input = mutableRecipe.gt5u$getRecipeInputs();
        if (input == null) {
            return;
        }

        ItemStack output = recipe.getRecipeOutput();

        if (GTUtility.areStacksEqual(
            output,
            MaterialLibAPI.getStack(Materials2Materials.Platinum, Materials2Shapes.dust, (int) (1)),
            true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Platinum)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(PTMetallicPowder.get(dust, output.stackSize * 2));
        } else if (GTUtility.areStacksEqual(
            output,
            MaterialLibAPI.getStack(Materials2Materials.Palladium, Materials2Shapes.dust, (int) (1)),
            true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Palladium)) return;
                mutableRecipe.gt5u$setRecipeOutputItem(PDMetallicPowder.get(dust, output.stackSize * 2));
            } else if (GTUtility.areStacksEqual(
                output,
                MaterialLibAPI.getStack(Materials2Materials.Iridium, Materials2Shapes.dust, (int) (1)),
                true)) {
                    if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Iridium)) return;
                    mutableRecipe.gt5u$setRecipeOutputItem(IrLeachResidue.get(dust, output.stackSize));
                } else if (GTUtility.areStacksEqual(
                    output,
                    MaterialLibAPI.getStack(Materials2Materials.Osmium, Materials2Shapes.dust, (int) (1)),
                    true)) {
                        if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Osmium)) return;
                        mutableRecipe.gt5u$setRecipeOutputItem(IrOsLeachResidue.get(dust, output.stackSize));
                    }
    }

    public static boolean checkRecipe(Object input, Materials material) {
        if (!(input instanceof List<?>) && !(input instanceof Object[])) {
            return false;
        }

        ArrayList<List<?>> lists = new ArrayList<>();
        ArrayList<ItemStack> stacks = new ArrayList<>();

        if (input instanceof List<?>listInput) {
            for (Object entry : listInput) {
                if (entry instanceof List<?>list) {
                    lists.add(list);
                } else if (entry instanceof ItemStack stack) {
                    stacks.add(stack);
                }
            }
        } else if (input instanceof Object[]arrayInput) {
            for (Object entry : arrayInput) {
                if (entry instanceof List<?>list) {
                    lists.add(list);
                } else if (entry instanceof ItemStack stack) {
                    stacks.add(stack);
                }
            }
        }

        for (List<?> list : lists) {
            if (list.isEmpty()) {
                continue;
            }

            Object first = list.get(0);
            if (!(first instanceof ItemStack stack)) {
                return false;
            }

            stacks.add(stack);
        }

        if (stacks.isEmpty()) {
            return false;
        }

        for (ItemStack stack : stacks) {
            ItemData association = GTOreDictUnificator.getAssociation(stack);
            if (!BWUtil.checkStackAndPrefix(association)) {
                return false;
            }

            if (association.mMaterial.mMaterial != material) {
                return false;
            }
        }

        return true;
    }

    private static boolean isInBlackList(ItemStack stack, List<ItemStack> availableItemList) {
        if (stack == null) return true;

        final Item item = stack.getItem();
        if (item == null) return true;
        if (item instanceof BWMetaGeneratedItems) return true;

        final String itemModId = GameRegistry.findUniqueIdentifierFor(item).modId;
        if (MainMod.MOD_ID.equals(itemModId) || BartWorksCrossmod.MOD_ID.equals(itemModId)) return true;
        if (isCutOverStorageBlock(stack)) return true;

        final String stackUnlocalizedName = stack.getUnlocalizedName();
        if (NewHorizonsCoreMod.ID.equals(itemModId) && !stackUnlocalizedName.contains("dust")
            && !stackUnlocalizedName.contains("Dust")) return true;

        Block block = Block.getBlockFromItem(item);
        if (block instanceof GTGenericBlock && !(block instanceof GTBlockOre)) return true;

        ItemData association = GTOreDictUnificator.getAssociation(stack);
        boolean isAssociationValid = BWUtil.checkStackAndPrefix(association);

        if (!isAssociationValid) {
            for (ItemStack itemStack : availableItemList) {
                if (GTUtility.areStacksEqual(itemStack, stack, true)) {
                    return true;
                }
            }
        }

        if (item instanceof GTGenericItem) {
            if (!isAssociationValid) return false;
            if (association.mPrefix != rawOre) {
                return !ORE_PREFIXES_BLACKLIST.contains(association.mPrefix)
                    || MATERIALS_BLACKLIST.contains(association.mMaterial.mMaterial);
            }
        }

        if (item instanceof BaseItemComponent && !stackUnlocalizedName.contains("dust")
            && !stackUnlocalizedName.contains("Dust")) {
            return true;
        }
        if (block instanceof BlockBaseModular) {
            return true;
        }
        if (block instanceof BlockFrameBox) {
            return true;
        }
        if (item == HELICOPTER.getDust(1)
            .getItem()) {
            return true;
        }
        if (item == HTGRItem.BURNED_TRISO) {
            return true;
        }
        if (Railcraft.isModLoaded()) {
            if (block.getUnlocalizedName()
                .equals("tile.railcraft.machine.zeta")
                || block.getUnlocalizedName()
                    .equals("tile.railcraft.machine.eta")) {
                return true;
            }
        }
        if (GalaxySpace.isModLoaded()) {
            if (item == GTModHandler.getModItem(GalaxySpace.ID, "metalsblock", 1L, 7)
                .getItem()) {
                return true;
            }
        }
        if (NewHorizonsCoreMod.isModLoaded()) {
            if (item == GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IndustryFrame", 1L)
                .getItem()) {
                return true;
            }
        }
        if (!isAssociationValid) {
            return false;
        }
        return MATERIALS_BLACKLIST.contains(association.mMaterial.mMaterial);
    }

    /// Whether `stack` is a MaterialLib storage block (`Materials2BlockShapes#block`) of a
    /// werkstoff- or gtPlusPlus-backed material -- the cutover equivalent of a legacy `bw.werkstoffblocks.01`/
    /// `BlockBaseModular` stack, which the bartworks-modid/`instanceof BlockBaseModular` checks above
    /// blacklisted wholesale. Blacklisting it keeps this overhaul sparing the lossless block-to-dust storage
    /// cycle (macerating a compressed Ruthenium/Rhodium/gtPlusPlus-material block returns its own dust),
    /// exactly as it did pre-cutover. Deliberately narrow: a material's other MaterialLib stacks (ore, crushed,
    /// dust, ...) stay subject to the overhaul the same way GT-modid inputs always were.
    private static boolean isCutOverStorageBlock(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block == null) return false;
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, stack.getItemDamage());
        if (info == null || info.shape() != Materials2BlockShapes.block || info.material() == null) {
            return false;
        }
        return info.material()
            .getProperty(GTMaterialProperties.WERKSTOFF) != null
            || info.material()
                .getProperty(GTMaterialProperties.GTPP) != null;
    }
}
