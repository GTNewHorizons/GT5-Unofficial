package gregtech.loaders.oreprocessing;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.material.ProcessingSubstitutes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingDirty implements PartRecipeGenerator {

    public static final ProcessingDirty INSTANCE = new ProcessingDirty();

    private ProcessingDirty() {}

    @Override
    public void generate(OrePrefixes orePrefix, Material material, Shape shape, ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        ProcessingUtils.addHammerRecipe(orePrefix, material, Shapes.dustImpure, false);
        if (shape == Shapes.crushed) {
            ProcessingUtils.addSimpleWasherRecipe(stack, MaterialLibAPI.findStack(material, Shapes.crushedPurified, 1));
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                ProcessingSubstitutes
                    .ore(material, MaterialLibAPI.getStack(MaterialUtils.macerateInto(material), Shapes.dustImpure, 1)))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                ProcessingSubstitutes.ore(
                    material,
                    GTUtility.copyAmount(
                        1L,
                        MaterialLibAPI.findStack(MaterialUtils.macerateInto(material), Shapes.dustImpure, 1),
                        MaterialLibAPI.getStack(MaterialUtils.macerateInto(material), Shapes.dust, 1)),
                    MaterialLibAPI.getStack(
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        Shapes.dust,
                        1)))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                ProcessingSubstitutes.ore(
                    material,
                    MaterialParts.stack(
                        orePrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                        material,
                        1L),
                    MaterialLibAPI.getStack(
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        Shapes.dust,
                        1),
                    MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                ProcessingSubstitutes.ore(
                    material,
                    MaterialParts.stack(
                        orePrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                        material,
                        1L),
                    MaterialLibAPI.getStack(
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        Shapes.dust,
                        1),
                    MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTModHandler.getDistilledWater(200))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        OrePrefixes prefix = orePrefix == OrePrefixes.crushed ? OrePrefixes.crushedCentrifuged : OrePrefixes.dust;

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                ProcessingSubstitutes.ore(
                    material,
                    MaterialParts.stack(prefix, material, 1L),
                    MaterialLibAPI.getStack(
                        GTUtility.selectItemInList(
                            1,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        Shapes.dust,
                        1),
                    MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
            .outputChances(10000, 1111, 10000)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        Set<Material> usedBaths = new HashSet<>();
        addChemicalBathRecipes(material, material, stack, orePrefix, usedBaths);

        for (Material tMaterial : MaterialUtils.oreByProducts(material)) {
            addChemicalBathRecipes(material, tMaterial, stack, orePrefix, usedBaths);
        }

    }

    private void addChemicalBathRecipes(Material material, Material byproduct, ItemStack stack, OrePrefixes prefix,
        Set<Material> usedBaths) {
        OrePrefixes chemicalBathPrefix = prefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified
            : OrePrefixes.dustPure;

        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY) && usedBaths.add(Materials.Mercury)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    ProcessingSubstitutes.ore(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialLibAPI.getStack(MaterialUtils.macerateInto(byproduct), Shapes.dust, 1),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);
        }
        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY_99_PERCENT)
            && usedBaths.add(Materials.Mercury)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    ProcessingSubstitutes.ore(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialLibAPI.getStack(MaterialUtils.macerateInto(byproduct), Shapes.dust, 1),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 9900, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);
        }
        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_SODIUMPERSULFATE)
            && usedBaths.add(Materials.SodiumPersulfate)) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    ProcessingSubstitutes.ore(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialLibAPI.getStack(MaterialUtils.macerateInto(byproduct), Shapes.dust, 1),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SodiumPersulfate, FluidShapes.fluidLiquid, 100))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);
        }
    }
}
