package gregtech.loaders.oreprocessing;

import static goodgenerator.util.NaquadahRecipeOutputs.convert;
import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.recipe.RecipeMaps.hammerRecipes;
import static gregtech.api.recipe.RecipeMaps.maceratorRecipes;
import static gregtech.api.recipe.RecipeMaps.oreWasherRecipes;
import static gregtech.api.recipe.RecipeMaps.thermalCentrifugeRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gtnhlanth.util.LanthanidesRecipeOutputs.convertOre;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;

public class ProcessingDirty implements gregtech.api.interfaces.IOreRecipeRegistrator {

    public static ProcessingDirty INSTANCE;

    public ProcessingDirty() {
        INSTANCE = this;
        OrePrefixes.clump.add(this);
        OrePrefixes.shard.add(this);
        OrePrefixes.crushed.add(this);
        OrePrefixes.dirtyGravel.add(this);
    }

    private boolean didPersulfate = false;
    private boolean didMercury = false;

    @Override
    public void registerOre(OrePrefixes orePrefix, Material material, String oreDictName, String modName,
        net.minecraft.item.ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_ORE_PROCESSING)) {
            return;
        }
        if (ProcessingOreMachine.owns(material)) {
            return;
        }

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convert(
                    material,
                    MaterialParts.stack(OrePrefixes.dustImpure, MaterialUtils.macerateInto(material), 1L)))
            .duration(10)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(hammerRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convertOre(
                    material,
                    GTUtility.copyAmount(
                        1L,
                        MaterialParts.stack(OrePrefixes.dustImpure, MaterialUtils.macerateInto(material), 1),
                        MaterialParts.stack(OrePrefixes.dust, MaterialUtils.macerateInto(material), 1L)),
                    MaterialParts.stack(
                        OrePrefixes.dust,
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        1L)))
            .outputChances(10000, 1000)
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(maceratorRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convertOre(
                    material,
                    MaterialParts.stack(
                        orePrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                        material,
                        1L),
                    MaterialParts.stack(
                        OrePrefixes.dust,
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        1L),
                    MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
            .outputChances(100_00, 11_11, 100_00)
            .fluidInputs(GTUtility.getWater(1_000))
            .duration(25 * SECONDS)
            .eut(TierEU.RECIPE_LV / 2)
            .addTo(oreWasherRecipes);

        RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                convertOre(
                    material,
                    MaterialParts.stack(
                        orePrefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified : OrePrefixes.dustPure,
                        material,
                        1L),
                    MaterialParts.stack(
                        OrePrefixes.dust,
                        GTUtility.selectItemInList(
                            0,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        1L),
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
                convertOre(
                    material,
                    MaterialParts.stack(prefix, material, 1L),
                    MaterialParts.stack(
                        OrePrefixes.dust,
                        GTUtility.selectItemInList(
                            1,
                            MaterialUtils.macerateInto(material),
                            MaterialUtils.oreByProducts(material)),
                        1L),
                    MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
            .outputChances(10000, 1111, 10000)
            .duration(25 * SECONDS)
            .eut(48)
            .addTo(thermalCentrifugeRecipes);

        Material werkstoffWash = werkstoffWashFluid(material);
        if (werkstoffWash != null) {
            if (orePrefix == OrePrefixes.crushed) addWerkstoffWash(material, werkstoffWash, stack);
            return;
        }

        didPersulfate = false;
        didMercury = false;

        addChemicalBathRecipes(material, material, stack, orePrefix);

        for (Material tMaterial : MaterialUtils.oreByProducts(material)) {
            addChemicalBathRecipes(material, tMaterial, stack, orePrefix);
        }

    }

    /// The bath fluid of a former Werkstoff ore whose washing flag bartworks inherited from its composition, or
    /// null. These wash only as crushed ore, with their second ore byproduct, as bartworks' crushed-ore loader did.
    private static @Nullable Material werkstoffWashFluid(Material material) {
        if (material == Materials.Roquesite || material == Materials.Bornite || material == Materials.Wittichenite) {
            return Materials.SodiumPersulfate;
        }
        return material == Materials.PlatinumMetallicPowder ? Materials.Mercury : null;
    }

    private static void addWerkstoffWash(Material material, Material fluid, ItemStack stack) {
        GTValues.RA.stdBuilder()
            .itemInputs(GTUtility.copyAmount(1, stack))
            .itemOutputs(
                MaterialParts.stack(OrePrefixes.crushedPurified, material, 1L),
                MaterialParts.stack(
                    OrePrefixes.dust,
                    MaterialUtils.oreByProducts(material)
                        .get(1),
                    1L),
                MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(fluid, FluidShapes.fluidLiquid, fluid == Materials.Mercury ? 1_000 : 100))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
    }

    private void addChemicalBathRecipes(Material material, Material byproduct, ItemStack stack, OrePrefixes prefix) {
        OrePrefixes chemicalBathPrefix = prefix == OrePrefixes.crushed ? OrePrefixes.crushedPurified
            : OrePrefixes.dustPure;

        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY) && !didMercury) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    convert(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialParts.stack(OrePrefixes.dust, MaterialUtils.macerateInto(byproduct), 1L),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didMercury = true;
        }
        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_MERCURY_99_PERCENT) && !didMercury) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    convert(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialParts.stack(OrePrefixes.dust, MaterialUtils.macerateInto(byproduct), 1L),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 9900, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Mercury, FluidShapes.fluidLiquid, 1_000))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didMercury = true;
        }
        if (MaterialUtils.hasFlag(byproduct, GTMaterialFlag.WASHING_SODIUMPERSULFATE) && !didPersulfate) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTUtility.copyAmount(1, stack))
                .itemOutputs(
                    convert(
                        material,
                        MaterialParts.stack(chemicalBathPrefix, material, 1L),
                        MaterialParts.stack(OrePrefixes.dust, MaterialUtils.macerateInto(byproduct), 1L),
                        MaterialLibAPI.getStack(Materials.Stone, Shapes.dust, 1)))
                .outputChances(10000, 7000, 4000)
                .fluidInputs(MaterialLibAPI.getFluidStack(Materials.SodiumPersulfate, FluidShapes.fluidLiquid, 100))
                .duration(40 * SECONDS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);

            didPersulfate = true;
        }
    }
}
