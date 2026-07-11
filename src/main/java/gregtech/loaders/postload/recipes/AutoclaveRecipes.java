package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.Mods.AppliedEnergistics2;
import static gregtech.api.recipe.RecipeMaps.autoclaveRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.EIGHTH_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.QUARTER_INGOTS;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialsElements;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class AutoclaveRecipes implements Runnable {

    @Override
    public void run() {
        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.EnergeticAlloy,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.IC2_Energium_Dust.get(9L))
            .itemOutputs(ItemList.IC2_EnergyCrystal.get(1L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.ConductiveIron,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (4 * INGOTS)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV / 2)
            .addTo(autoclaveRecipes);

        final ItemStack certusQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 0);
        NBTTagCompound certusQuartzTag = new NBTTagCompound();
        certusQuartzTag.setInteger("progress", 0);
        certusQuartzSeed.setTagCompound(certusQuartzTag);

        final ItemStack netherQuartzSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 600);
        NBTTagCompound netherQuartzTag = new NBTTagCompound();
        netherQuartzTag.setInteger("progress", 600);
        netherQuartzSeed.setTagCompound(netherQuartzTag);

        final ItemStack fluixSeed = getModItem(AppliedEnergistics2.ID, "item.ItemCrystalSeed", 1L, 1200);
        NBTTagCompound fluixTag = new NBTTagCompound();
        fluixTag.setInteger("progress", 1200);
        fluixSeed.setTagCompound(fluixTag);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(8000)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(certusQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 10))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Void,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(netherQuartzSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 11))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Void,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(fluixSeed)
            .itemOutputs(getModItem(AppliedEnergistics2.ID, "item.ItemMultiMaterial", 1L, 12))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Void,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(25 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (8)))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 16L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Polybenzimidazole,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (9)))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_EV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (4)))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 4L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Epoxid, Materials2FluidShapes.shapeFluidMolten, (int) (9)))
            .duration(18 * TICKS)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (4)))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 2L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Polytetrafluoroethylene,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * EIGHTH_INGOTS)))
            .duration(1 * SECONDS + 5 * TICKS)
            .eut((int) TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Carbon, Materials2Shapes.shapeDust, (int) (4)))
            .itemOutputs(GTModHandler.getIC2Item("carbonFiber", 1L))
            .outputChances(10000)
            .fluidInputs(Materials.Polyethylene.getMolten(1 * QUARTER_INGOTS))
            .duration(1 * SECONDS + 17 * TICKS)
            .eut((int) TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.NetherStar, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.gem, Materials.NetherStar, 1))
            .outputChances(3333)
            .fluidInputs(Materials.UUMatter.getFluid(4 * INGOTS))
            .duration(60 * MINUTES)
            .eut((int) TierEU.RECIPE_HV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.QuantumStar.get(1L)))
            .itemOutputs(ItemList.Gravistar.get(1L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Neutronium,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(24 * SECONDS)
            .eut((int) TierEU.RECIPE_IV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(ItemList.Gravistar.get(16L)))
            .itemOutputs(ItemList.NuclearStar.get(1L))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Infinity,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (2 * INGOTS)))
            .duration(24 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeGem, (int) (1)))
            .outputChances(750)
            .fluidInputs(Materials.Water.getFluid(200L))
            .duration(1 * MINUTES + 40 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeGem, (int) (1)))
            .outputChances(1000)
            .fluidInputs(GTModHandler.getDistilledWater(100L))
            .duration(1 * MINUTES + 15 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.shapeGem, (int) (1)))
            .outputChances(10000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Void,
                    Materials2FluidShapes.shapeFluidMolten,
                    (int) (1 * QUARTER_INGOTS)))
            .duration(50 * SECONDS)
            .eut(24)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, (int) (1)))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3)))
            .outputChances(7500)
            .fluidInputs(Materials.Water.getFluid(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Silicon, Materials2Shapes.shapeDust, (int) (1)))
            .circuit(1)
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.SiliconDioxide, Materials2Shapes.shapeDust, (int) (3)))
            .outputChances(9000)
            .fluidInputs(GTModHandler.getDistilledWater(1_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Olivine, Materials2Shapes.shapeGem, (int) (15)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Asbestos, Materials2Shapes.shapeDust, (int) (18)),
                MaterialLibAPI.getStack(Materials2Materials.Magnetite, Materials2Shapes.shapeDust, (int) (7)))
            .fluidInputs(GTModHandler.getDistilledWater(9_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.shapeFluidGas, (int) (14_000)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(autoclaveRecipes);

        // Marble Block
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.block, Materials.Marble, 1L))
            .fluidInputs(Materials.Water.getFluid(1_000L))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Calcite, Materials2Shapes.shapeDust, (int) (1)))
            .itemOutputs(GTOreDictUnificator.get(OrePrefixes.stone, Materials.Marble, 1L))
            .fluidInputs(GTModHandler.getDistilledWater(500L))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 1L),
                MaterialLibAPI.getStack(Materials2Materials.Mellion, Materials2Shapes.shapeDust, (int) (32)))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(8L))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.Grade8PurifiedWater,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (32_000)))
            .duration(15 * SECONDS)
            .eut(TierEU.RECIPE_UMV)
            .addTo(autoclaveRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(OrePrefixes.round, Materials.MagMatter, 1))
            .itemOutputs(ItemList.Phononic_Seed_Crystal.get(5))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PhononCrystalSolution,
                    Materials2FluidShapes.shapeFluidLiquid,
                    (int) (250)))
            .duration(2 * SECONDS + 10 * TICKS)
            .eut(TierEU.RECIPE_UXV)
            .addTo(autoclaveRecipes);

        // Exquisite Amalgatite
        GTValues.RA.stdBuilder()
            .itemInputs(
                GTUtility.copyAmount(0, GTOreDictUnificator.get(OrePrefixes.nanite, Materials.MagMatter, 1)),
                MaterialLibAPI.getStack(Materials2Materials.Amalgatite, Materials2Shapes.shapeGemFlawless, (int) (3)),
                WerkstoffLoader.Olenit.get(OrePrefixes.gemChipped, 64),
                WerkstoffLoader.Salt.get(OrePrefixes.gemChipped, 64),
                MaterialLibAPI.getStack(Materials2Materials.Diamond, Materials2Shapes.shapeGemChipped, (int) (64)),
                WerkstoffLoader.VanadioOxyDravit.get(OrePrefixes.gemChipped, 64))
            .fluidInputs(new FluidStack(MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlasma(), 1_000_000))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Amalgatite, Materials2Shapes.shapeGemExquisite, (int) (1)))
            .duration(100 * SECONDS)
            .eut(TierEU.RECIPE_MAX)
            .addTo(autoclaveRecipes);

    }
}
