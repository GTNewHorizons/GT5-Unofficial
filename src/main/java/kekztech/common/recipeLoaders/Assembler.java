package kekztech.common.recipeLoaders;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.util.GTModHandler;
import kekztech.Items;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import kekztech.common.items.MetaItemCraftingComponent;

public class Assembler implements Runnable {

    @Override
    public void run() {
        // TFFT Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.DarkSteel, Shapes.plate, 3),
                MaterialLibAPI.getStack(Materials.EnderPearl, Shapes.plate, 3))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials.Polytetrafluoroethylene, FluidShapes.fluidMolten, INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFT Multi Hatch

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Hull_HV.get(1),
                ItemList.Cover_FluidStorageMonitor.get(1),
                ItemList.Field_Generator_LV.get(4),
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, TEBlockShapes.pipeTiny, 25))
            .itemOutputs(TileEntities.tfftHatch.getStackForm(1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 3 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField1

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.CrudeSteel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.CrudeSteel, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.pipeNonuple, 3),
                ItemList.FluidRegulator_LV.get(1))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Glass, FluidShapes.fluidMolten, INGOTS))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField2

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Tank_1.get(1),
                MaterialLibAPI.getStack(Materials.EnergeticSilver, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Plastic, TEBlockShapes.pipeNonuple, 3),
                ItemList.FluidRegulator_MV.get(1))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 2))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 2 * INGOTS))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField3

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Tank_3.get(1),
                MaterialLibAPI.getStack(Materials.VividAlloy, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.StainlessSteel, TEBlockShapes.pipeNonuple, 3),
                ItemList.Field_Generator_LV.get(1),
                ItemList.FluidRegulator_HV.get(1))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 3))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Plastic, FluidShapes.fluidMolten, 3 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // TFFTStorageField4

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Tank_5.get(1),
                MaterialLibAPI.getStack(Materials.Enderium, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Polytetrafluoroethylene, TEBlockShapes.pipeNonuple, 3),
                ItemList.Field_Generator_MV.get(2),
                ItemList.FluidRegulator_EV.get(1))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 4))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, 6 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // TFFTStorageField5

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Casing_Tank_7.get(1),
                MaterialLibAPI.getStack(Materials.CrystallineAlloy, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Enderium, TEBlockShapes.pipeNonuple, 3),
                ItemList.Field_Generator_HV.get(4),
                ItemList.FluidRegulator_IV.get(1))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.tfftStorageField, 1, 5))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Epoxid, FluidShapes.fluidMolten, 8 * INGOTS))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // LSC Casing

        GTValues.RA.stdBuilder()
            .itemInputs(
                MaterialLibAPI.getStack(Materials.Tantalum, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.TungstenSteel, TEBlockShapes.frameGt, 2),
                MaterialLibAPI.getStack(Materials.TungstenSteel, Shapes.stickLong, 2),
                MaterialLibAPI.getStack(Materials.Lapis, BlockShapes.block, 1))
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 0))
            .duration(5 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // EV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6),
                GTModHandler.getIC2Item("lapotronCrystal", 1L, WILDCARD))
            .circuit(7)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 7))
            .duration(10 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // IV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 6), ItemList.Energy_LapotronicOrb.get(1L))
            .circuit(1)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 1))
            .duration(20 * SECONDS)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // LuV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_LapotronicOrb2.get(1),
                MaterialLibAPI.getStack(Materials.Osmiridium, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 2))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_IV)
            .addTo(assemblerRecipes);
        // ZPM Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_Module.get(1),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.NaquadahAlloy, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 3))
            .duration(1 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_LuV)
            .addTo(assemblerRecipes);
        // UV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.Energy_Cluster.get(1),
                MaterialLibAPI.getStack(Materials.Neutronium, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 4))
            .duration(2 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(assemblerRecipes);
        // UHV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM3.get(1),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 5))
            .duration(5 * MINUTES + 20 * SECONDS)
            .eut(TierEU.RECIPE_UV)
            .addTo(assemblerRecipes);
        // UEV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM4.get(1),
                MaterialLibAPI.getStack(Materials.Infinity, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 8))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UHV)
            .addTo(assemblerRecipes);
        // UIV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM5.get(1),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 9))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UEV)
            .addTo(assemblerRecipes);
        // UMV Capacitor alt recipe

        GTValues.RA.stdBuilder()
            .itemInputs(
                ItemList.ZPM6.get(1),
                MaterialLibAPI.getStack(Materials.SpaceTime, TEBlockShapes.frameGt, 4),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.screw, 24))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.lscLapotronicEnergyUnit, 1, 10))
            .duration(10 * MINUTES + 40 * SECONDS)
            .eut(TierEU.RECIPE_UIV)
            .addTo(assemblerRecipes);

        final MetaItemCraftingComponent craftingItem = MetaItemCraftingComponent.getInstance();

        // YSZ Unit

        GTValues.RA.stdBuilder()
            .itemInputs(
                craftingItem.getStackOfAmountFromDamage(Items.YSZCeramicPlate.getMetaID(), 4),
                MaterialLibAPI.getStack(Materials.Yttrium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.StainlessSteel, Shapes.rotor, 1),
                ItemList.Electric_Motor_HV.get(1L))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.yszUnit, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 4_000))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_HV)
            .addTo(assemblerRecipes);
        // GDC Unit

        GTValues.RA.stdBuilder()
            .itemInputs(
                craftingItem.getStackOfAmountFromDamage(Items.GDCCeramicPlate.getMetaID(), 8),
                MaterialLibAPI.getStack(Materials.Gadolinium, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.Desh, Shapes.rotor, 1),
                ItemList.Electric_Motor_IV.get(1L))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.gdcUnit, 1))
            .fluidInputs(MaterialLibAPI.getFluidStack(Materials.Hydrogen, FluidShapes.fluidGas, 16_000))
            .duration(2 * MINUTES)
            .eut(TierEU.RECIPE_EV)
            .addTo(assemblerRecipes);
        // Hex Tiles

        GTValues.RA.stdBuilder()
            .itemInputs(
                new ItemStack(GregTechAPI.sBlockConcretes, 1, 0),
                MaterialLibAPI.getStack(Materials.Steel, TEBlockShapes.frameGt, 1),
                MaterialLibAPI.getStack(Materials.DarkSteel, Shapes.foil, 2))
            .circuit(6)
            .itemOutputs(new ItemStack(Blocks.largeHexPlate, 2))
            .fluidInputs(FluidRegistry.getFluidStack("molten.plastic", 36))
            .duration(30 * SECONDS)
            .eut(TierEU.RECIPE_MV)
            .addTo(assemblerRecipes);

    }
}
