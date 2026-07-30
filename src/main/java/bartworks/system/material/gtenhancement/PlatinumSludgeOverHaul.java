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
import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.MainMod;
import bartworks.util.BWUtil;
import bwcrossmod.BartWorksCrossmod;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.LegacyWerkstoffIndex;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.items.GTGenericBlock;
import gregtech.api.items.GTGenericItem;
import gregtech.api.material.GTMaterialProperties;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.GTBlockOre;
import gregtech.loaders.materials.LegacyNameDomain;
import gregtech.mixin.interfaces.accessors.IRecipeMutableAccess;
import kubatech.loaders.item.htgritem.HTGRItem;

public class PlatinumSludgeOverHaul {

    private static final List<Material> MATERIALS_BLACKLIST = Arrays.asList(
        Materials.HSSS,
        Materials.EnderiumBase,
        Materials.Osmiridium,
        Materials.TPVAlloy,
        Materials.Uraniumtriplatinid,
        Materials.Tetranaquadahdiindiumhexaplatiumosminid,
        Materials.Longasssuperconductornameforuvwire);

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
                MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, (int) (2)),
                GTOreDictUnificator.get(cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.DilutedSulfuricAcid, CellShapes.cell, (int) (3)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(cell, Materials.Water, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, (int) (2)))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 2))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.DilutedSulfuricAcid, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // FormicAcid
        // CO + NaOH = CHO2Na
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CarbonMonoxide, CellShapes.cell, (int) (1)),
                MaterialLibAPI.getStack(Materials.SodiumHydroxideGT5U, Shapes.dust, 3))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumFormate, CellShapes.cell, 1))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // H2SO4 + 2CHO2Na = 2CH2O2 + Na2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumFormate, CellShapes.cell, 2))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.FormicAcid, CellShapes.cell, 2),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SulfuricAcid, CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(
                GTOreDictUnificator.get(cell, Materials.Empty, 1),
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SodiumFormate, FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, (int) (2_000)))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        // AquaRegia
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AquaRegia, CellShapes.cell, 4))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3),
                MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, (int) (1)))
            .circuit(2)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 4))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.NitricAcid, CellShapes.cell, (int) (1)))
            .circuit(3)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3))
            .circuit(4)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (4_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        // AmmoniumChloride
        // NH3 + HCl = NH4Cl
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Ammonia, CellShapes.cell, (int) (1)))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.AmmoniumChloride, CellShapes.cell, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .circuit(1)
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(9)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (64_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 64_000))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, (int) (64_000)))
            .duration(3 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        // base solution
        for (Material ml : MaterialLibAPI.getMaterials()) {
            if (ml.getProperty(GTMaterialProperties.WERKSTOFF_IDS) == null) continue;
            if (ml.hasShape(Shapes.crushedPurified) && materialsContains(ml, Materials.Sulfur)
                && (materialsContains(ml, Materials.Copper) || materialsContains(ml, Materials.Nickel))) {

                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 1))
                    .circuit(1)
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (300)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (300)))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 9))
                    .circuit(9)
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (2_700)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (2_700)))
                    .duration(11 * SECONDS + 5 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        MaterialLibAPI.getStack(ml, Shapes.crushedPurified, 9),
                        MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
                    .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (20_700)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (20_700)))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);
            }
        }

        for (Material material : MaterialLibAPI.getMaterials()) {
            if (!LegacyNameDomain.contains(material)) continue;
            if (materialsContains(material, Materials.Sulfur)
                && (materialsContains(material, Materials.Copper) || materialsContains(material, Materials.Nickel))) {

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 1))
                    .circuit(1)
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (300)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (300)))
                    .duration(12 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(GTOreDictUnificator.get(crushedPurified, material, 9))
                    .circuit(9)
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (2_700)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (2_700)))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

                GTValues.RA.stdBuilder()
                    .itemInputs(
                        GTOreDictUnificator.get(crushedPurified, material, 9),
                        MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
                    .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
                    .fluidInputs(
                        MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (20_700)))
                    .fluidOutputs(
                        MaterialLibAPI
                            .getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (20_700)))
                    .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
                    .eut(TierEU.RECIPE_LV)
                    .addTo(UniversalChemical);

            }
        }

        // Pt
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 3))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Platinum, Shapes.nugget, (int) (2)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, MaterialUtils.meltingPoint(Materials.Platinum))
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dustTiny, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (2_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (2_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AquaRegia, FluidShapes.fluidLiquid, (int) (18_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (18_000)))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumConcentrate, CellShapes.cell, 4))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dustTiny, 4),
                MaterialLibAPI.getStack(Materials.NitrogenDioxide, CellShapes.cell, (int) (1)),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, (int) (400)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (400)))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(centrifugeRecipes);

        GTValues.RA.stdBuilder()
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dustTiny, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, (int) (400)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (400)),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, (int) (1_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 3_000))
            .duration(1 * MINUTES + 4 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dust, 4))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.PlatinumConcentrate, FluidShapes.fluidLiquid, (int) (36_000)),
                MaterialLibAPI.getFluidStack(Materials.AmmoniumChloride, FluidShapes.fluidLiquid, (int) (3_600)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (3_600)),
                MaterialLibAPI.getFluidStack(Materials.NitrogenDioxide, FluidShapes.fluidGas, (int) (9_000)),
                MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 27_000))
            .duration(700)
            .eut(TierEU.RECIPE_HV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumSalt, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RefinedPlatinumSalt, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (87)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 900)
            .addTo(blastFurnaceRecipes);

        // 2PtCl + Ca = 2Pt + CaCl2
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedPlatinum, Shapes.dust, 4),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.CalciumChloride, Shapes.dust, 3))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Pd
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dustTiny, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dustTiny, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 9))
            .circuit(9)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 16),
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 2))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (9_000)))
            .duration(1 * MINUTES + 52 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.PalladiumEnrichedAmmonia, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PalladiumSalt, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 4),
                GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (2)),
                MaterialLibAPI.getStack(Materials.Ethylene, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (4_000)))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.ReprecipitatedPalladium, Shapes.dust, 4))
            .itemOutputs(MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (2)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.FormicAcid, FluidShapes.fluidLiquid, (int) (4_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (4_000)),
                MaterialLibAPI.getFluidStack(Materials.Ethylene, FluidShapes.fluidGas, (int) (1_000)),
                GTUtility.getWater(1_000))
            .duration(12 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        // Na2SO4 + 2H = 2Na + H2SO4
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumSulfate, Shapes.dust, 7),
                MaterialLibAPI.getStack(Materials.Hydrogen, CellShapes.cell, (int) (2)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (2)),
                GTOreDictUnificator.get(cell, Materials.Empty, 2))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.SulfuricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Rh/Os/Ir/Ru
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.PlatinumResidue, Shapes.dust, 1))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials.PotassiumDisulfate,
                    FluidShapes.fluidMolten,
                    (int) (2 * INGOTS + 1 * HALF_INGOTS)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.RhodiumSulfate, FluidShapes.fluidLiquid, (int) (360)))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        // Ru
        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 10),
                MaterialLibAPI.getStack(Materials.Saltpeter, Shapes.dust, (int) (10)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SodiumRuthenate, Shapes.dust, 3),
                MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 6))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SaltWater, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialUtils.gas(Materials.Steam, 1_000))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.SodiumRuthenate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (3)))
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 3))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.RutheniumTetroxideSolution, FluidShapes.fluidLiquid, (int) (9_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.RutheniumTetroxideSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HotRutheniumTetroxideSolution, FluidShapes.fluidLiquid, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(fluidHeaterRecipes);

        GTValues.RA.stdBuilder()
            .itemOutputs(MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, (int) (6)))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.HotRutheniumTetroxideSolution, FluidShapes.fluidLiquid, (int) (9_000)))
            .fluidOutputs(
                GTUtility.getWater(1_800),
                MaterialLibAPI.getFluidStack(Materials.RutheniumTetroxide, FluidShapes.fluidLiquid, (int) (7_200)))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.RutheniumTetroxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (6)))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Os
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, 2))
            .circuit(11)
            .itemOutputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 500))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AcidicOsmiumSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.AcidicOsmiumSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.OsmiumSolution, FluidShapes.fluidLiquid, (int) (100)),
                GTUtility.getWater(900))
            .duration(7 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_IV)
            .addTo(distillationTowerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.OsmiumSolution, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 6))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Chlorine, CellShapes.cell, (int) (7)))
            .fluidOutputs(GTUtility.getWater(2_000))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Ir
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, 1))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.SludgeDustResidue, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.IridiumDioxide, Shapes.dust, 1))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 775)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IridiumDioxide, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.HydrochloricAcidGT5U, CellShapes.cell, 1))
            .itemOutputs(GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.AcidicIridiumSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.AcidicIridiumSolution, CellShapes.cell, 1),
                MaterialLibAPI.getStack(Materials.AmmoniumChloride, CellShapes.cell, 3))
            .itemOutputs(
                GTOreDictUnificator.get(cell, Materials.Empty, 4),
                MaterialLibAPI.getStack(Materials.IridiumChloride, Shapes.dust, 1))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Ammonia, FluidShapes.fluidGas, (int) (3_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.IridiumChloride, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Calcium, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.MetallicSludgeDustResidue, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (1)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.CalciumChloride, FluidShapes.fluidLiquid, (int) (3_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(UniversalChemical);

        // Rh
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumSulfate, CellShapes.cell, 11))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumSulfateSolution, CellShapes.cell, 11),
                MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dustTiny, 10))
            .fluidInputs(GTUtility.getWater(10_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidMolten, (int) (2_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .circuit(3)
            .itemOutputs(MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, 4))
            .fluidInputs(
                GTUtility.getWater(36_000),
                MaterialLibAPI.getFluidStack(Materials.RhodiumSulfate, FluidShapes.fluidLiquid, (int) (39_600)))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.Potassium, FluidShapes.fluidMolten, (int) (50 * INGOTS)),
                MaterialLibAPI.getFluidStack(Materials.RhodiumSulfateSolution, FluidShapes.fluidLiquid, (int) (39_600)))
            .duration(1 * MINUTES)
            .eut(TierEU.RECIPE_LV)
            .addTo(multiblockChemicalReactorRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Zinc, Shapes.dust, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.ZincSulfate, Shapes.dust, 6),
                MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RhodiumSulfateSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.RhodiumSalt, Shapes.dust, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .metadata(COIL_HEAT, 600)
            .addTo(blastFurnaceRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumSalt, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(200))
            .fluidOutputs(
                MaterialLibAPI.getFluidStack(Materials.RhodiumSaltSolution, FluidShapes.fluidLiquid, (int) (200)))
            .duration(1 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumNitrate, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Salt, Shapes.dust, (int) (2)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.RhodiumSaltSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        // Na + HNO3 = NaNO3 + H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.Sodium, Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials.SodiumNitrate, Shapes.dust, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.NitricAcid, FluidShapes.fluidLiquid, (int) (1_000)))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, (int) (1_000)))
            .duration(8 * TICKS)
            .eut(TierEU.RECIPE_MV / 2)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumNitrate, Shapes.dust, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1))
            .outputChances(1000, 1000, 1000, 1000, 1000, 1000, 1000, 1000, 1500)
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(sifterRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials.RhodiumFilterCake, Shapes.dust, 1))
            .fluidInputs(GTUtility.getWater(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials.RhodiumFilterCakeSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(mixerRecipes);

        GTValues.RA.stdBuilder()
            .circuit(2)
            .itemOutputs(MaterialLibAPI.getStack(Materials.ReprecipitatedRhodium, Shapes.dust, 1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials.RhodiumFilterCakeSolution, FluidShapes.fluidLiquid, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.ReprecipitatedRhodium, Shapes.dust, 1),
                GTOreDictUnificator.get(cell, Materials.Empty, 1))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1),
                MaterialLibAPI.getStack(Materials.Ammonia, CellShapes.cell, (int) (1)))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.HydrochloricAcidGT5U, FluidShapes.fluidLiquid, 1_000))
            .fluidOutputs(MaterialLibAPI.getFluidStack(Materials.Chlorine, FluidShapes.fluidGas, (int) (1_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(UniversalChemical);

    }

    private static boolean materialsContains(Material one, Material other) {
        if (one == null) return false;
        for (MaterialStack stack : MaterialUtils.materialList(one)) if (stack.mMaterial == other) return true;
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

            final Material newOutput;
            if (outputAssociation.mMaterial.mMaterial == Materials.Platinum) {
                newOutput = LegacyWerkstoffIndex.get(47);
            } else if (outputAssociation.mMaterial.mMaterial == Materials.Palladium) {
                newOutput = LegacyWerkstoffIndex.get(53);
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
            entry.setValue(MaterialParts.stack(prefix, newOutput, output.stackSize * 2));
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
                        if (GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Ruthenium, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])
                            || GTUtility.areFluidsEqual(
                                MaterialLibAPI.getFluidStack(Materials.Rhodium, FluidShapes.fluidMolten, (int) (1)),
                                recipe.mFluidOutputs[i])) {
                            toDelete.add(recipe);
                            GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                        } else if (GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])) {
                                recipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                                    Materials.AcidicIridiumSolution,
                                    FluidShapes.fluidLiquid,
                                    (int) (1_000));
                                recipe.reloadOwner();
                                GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                            } else if (GTUtility.areFluidsEqual(
                                MaterialLibAPI.getFluidStack(Materials.Platinum, FluidShapes.fluidMolten, (int) (1)),
                                recipe.mFluidOutputs[i])) {
                                    recipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                                        Materials.PlatinumConcentrate,
                                        FluidShapes.fluidLiquid,
                                        (int) (2_000));
                                    recipe.reloadOwner();
                                    GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                                } else if (GTUtility.areFluidsEqual(
                                    MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, (int) (1)),
                                    recipe.mFluidOutputs[i])) {
                                        recipe.mFluidOutputs[i] = MaterialLibAPI.getFluidStack(
                                            Materials.AcidicOsmiumSolution,
                                            FluidShapes.fluidLiquid,
                                            (int) (1_000));
                                        recipe.reloadOwner();
                                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                                    }
                    } else if (GTUtility.areFluidsEqual(
                        MaterialLibAPI.getFluidStack(Materials.Ruthenium, FluidShapes.fluidMolten, (int) (1)),
                        recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Rhodium, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Platinum, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])
                        || GTUtility.areFluidsEqual(
                            MaterialLibAPI.getFluidStack(Materials.Osmium, FluidShapes.fluidMolten, (int) (1)),
                            recipe.mFluidOutputs[i])) {
                                toDelete.add(recipe);
                                GTLog.out.println("Recipe marked for deletion: " + displayRecipe(recipe));
                            }
                }

                for (int i = 0; i < recipe.mOutputs.length; i++) {
                    if (!GTUtility.isStackValid(recipe.mOutputs[i])) continue;

                    if (GTUtility.areStacksEqual(
                        MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.dust, 1),
                        recipe.mOutputs[i])
                        && !GTUtility.areStacksEqual(
                            MaterialLibAPI.getStack(Materials.Ruthenium, Shapes.ingot, 1),
                            recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = MaterialLibAPI.getStack(Materials.LeachResidue, Shapes.dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    if (GTUtility
                        .areStacksEqual(MaterialLibAPI.getStack(Materials.Rhodium, Shapes.dust, 1), recipe.mOutputs[i])
                        && !GTUtility.areStacksEqual(
                            MaterialLibAPI.getStack(Materials.Rhodium, Shapes.ingot, 1),
                            recipe.mInputs[0])) {
                        for (ItemStack mInput : recipe.mInputs) {
                            if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                        }
                        int amount = recipe.mOutputs[i].stackSize * 2;
                        recipe.mOutputs[i] = MaterialLibAPI.getStack(Materials.CrudeRhodiumMetal, Shapes.dust, amount);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    }

                    ItemData association = GTOreDictUnificator.getAssociation(recipe.mOutputs[i]);
                    if (!BWUtil.checkStackAndPrefix(association)) continue;

                    final Material replacementMaterial;
                    if (association.mMaterial.mMaterial == Materials.Platinum) {
                        replacementMaterial = LegacyWerkstoffIndex.get(47);
                    } else if (association.mMaterial.mMaterial == Materials.Palladium) {
                        replacementMaterial = LegacyWerkstoffIndex.get(53);
                    } else if (association.mMaterial.mMaterial == Materials.Osmium) {
                        replacementMaterial = LegacyWerkstoffIndex.get(69);
                    } else if (association.mMaterial.mMaterial == Materials.Iridium) {
                        replacementMaterial = LegacyWerkstoffIndex.get(70);
                    } else {
                        continue;
                    }

                    for (ItemStack mInput : recipe.mInputs) {
                        if (PlatinumSludgeOverHaul.isInBlackList(mInput, availableItemList)) continue recipeloop;
                    }

                    if (association.mPrefix == dust || association.mPrefix == dustImpure
                        || association.mPrefix == dustPure) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = MaterialLibAPI.getStack(replacementMaterial, Shapes.dust, amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustSmall) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = MaterialLibAPI.getStack(replacementMaterial, Shapes.dustSmall, amount * 2);
                        recipe.reloadOwner();
                        GTLog.out.println("Recipe edited: " + displayRecipe(recipe));
                    } else if (association.mPrefix == dustTiny) {
                        int amount = recipe.mOutputs[i].stackSize;
                        recipe.mOutputs[i] = MaterialLibAPI.getStack(replacementMaterial, Shapes.dustTiny, amount * 2);
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
                MaterialLibAPI.getStack(Materials.Redstone, Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials.Electrum, Shapes.dust, (int) (1)))
            .circuit(1)
            .itemOutputs(MaterialLibAPI.getStack(Materials.Electrotine, Shapes.dust, (int) (8)))
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

                    if (association.mMaterial.mMaterial == Materials.Platinum) {
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

        if (GTUtility
            .areStacksEqual(output, MaterialLibAPI.getStack(Materials.Platinum, Shapes.dust, (int) (1)), true)) {
            if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Platinum)) return;
            mutableRecipe.gt5u$setRecipeOutputItem(
                MaterialLibAPI.getStack(Materials.PlatinumMetallicPowder, Shapes.dust, output.stackSize * 2));
        } else if (GTUtility
            .areStacksEqual(output, MaterialLibAPI.getStack(Materials.Palladium, Shapes.dust, (int) (1)), true)) {
                if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Palladium)) return;
                mutableRecipe.gt5u$setRecipeOutputItem(
                    MaterialLibAPI.getStack(Materials.PalladiumMetallicPowder, Shapes.dust, output.stackSize * 2));
            } else if (GTUtility
                .areStacksEqual(output, MaterialLibAPI.getStack(Materials.Iridium, Shapes.dust, (int) (1)), true)) {
                    if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Iridium)) return;
                    mutableRecipe.gt5u$setRecipeOutputItem(
                        MaterialLibAPI.getStack(Materials.IridiumMetalResidue, Shapes.dust, output.stackSize));
                } else if (GTUtility
                    .areStacksEqual(output, MaterialLibAPI.getStack(Materials.Osmium, Shapes.dust, (int) (1)), true)) {
                        if (PlatinumSludgeOverHaul.checkRecipe(input, Materials.Osmium)) return;
                        mutableRecipe.gt5u$setRecipeOutputItem(
                            MaterialLibAPI.getStack(Materials.RarestMetalResidue, Shapes.dust, output.stackSize));
                    }
    }

    public static boolean checkRecipe(Object input, Material material) {
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

        if (association != null && association.mMaterial != null
            && association.mMaterial.mMaterial != null
            && association.mMaterial.mMaterial.getProperty(GTMaterialProperties.GTPP_STATE) != null
            && !ORE_PREFIXES_BLACKLIST.contains(association.mPrefix)) {
            return true;
        }
        if (block instanceof FrameShapeBlock) {
            return true;
        }
        if (item == MaterialLibAPI.getStack(Materials.HeLiCoPtEr, Shapes.dust, 1)
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

    /// Whether `stack` is a MaterialLib storage block (`BlockShapes#block`) of a werkstoff- or
    /// gtPlusPlus-backed material -- the cutover equivalent of the legacy `bw.werkstoffblocks.01`/
    /// `BlockBaseModular` stacks that the bartworks-modid check above (`WERKSTOFF_IDS`) and the gtpp-material
    /// association check below (`GTPP_STATE`) blacklisted wholesale. Blacklisting it keeps this overhaul
    /// sparing the lossless block-to-dust storage cycle (macerating a compressed Ruthenium/Rhodium/gtPlusPlus-
    /// material block returns its own dust). Deliberately narrow: a material's
    /// other MaterialLib stacks (ore, crushed, dust, ...) stay subject to the overhaul the same way GT-modid
    /// inputs always were.
    private static boolean isCutOverStorageBlock(ItemStack stack) {
        Block block = Block.getBlockFromItem(stack.getItem());
        if (block == null) return false;
        BlockMaterialInfo info = MaterialLibAPI.lookupBlock(block, stack.getItemDamage());
        if (info == null || info.shape() != BlockShapes.block || info.material() == null) {
            return false;
        }
        return info.material()
            .getProperty(GTMaterialProperties.WERKSTOFF_IDS) != null
            || info.material()
                .getProperty(GTMaterialProperties.GTPP_STATE) != null;
    }
}
