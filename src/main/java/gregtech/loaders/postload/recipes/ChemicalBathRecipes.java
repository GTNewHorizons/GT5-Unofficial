package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.BuildCraftTransport;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.recipe.RecipeMaps.chemicalBathRecipes;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.system.material.WerkstoffLoader;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials2.Materials2FluidShapes;
import gregtech.api.enums.materials2.Materials2Materials;
import gregtech.api.enums.materials2.Materials2Shapes;
import gregtech.api.objects.OreDictItemStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;

@SuppressWarnings({ "PointlessArithmeticExpression" })
public class ChemicalBathRecipes implements Runnable {

    @Override
    public void run() {
        if (Railcraft.isModLoaded()) {
            GTValues.RA.stdBuilder()
                .itemInputs(new OreDictItemStack("logWood", 1))
                .itemOutputs(GTModHandler.getModItem(Railcraft.ID, "cube", 1L, 8))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Creosote, Materials2FluidShapes.fluidLiquid, (int) (750)))
                .duration(16 * TICKS)
                .eut(TierEU.RECIPE_ULV)
                .addTo(chemicalBathRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.Food_Raw_Fries.get(1))
            .itemOutputs(ItemList.Food_Fries.get(1))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.FryingOilHot, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTModHandler.getIC2Item("dynamite", 1))
            .itemOutputs(GTModHandler.getIC2Item("stickyDynamite", 1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Glue, Materials2FluidShapes.fluidLiquid, (int) (10)))
            .duration(16 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("frameGtSteel", 1))
            .itemOutputs(ItemList.Block_ReinforcedConcrete.get(1L))
            .fluidInputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Concrete, Materials2FluidShapes.fluidMolten, (int) (1 * INGOTS)))
            .duration(10 * SECONDS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HydratedCoal, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(Materials.Water.getFluid(125))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        // paper creation recipes
        ItemStack[] paperSources = new ItemStack[] {
            MaterialLibAPI.getStack(Materials2Materials.Wood, Materials2Shapes.dust, (int) (1)),
            MaterialLibAPI.getStack(Materials2Materials.Paper, Materials2Shapes.dust, (int) (1)),
            new ItemStack(Items.reeds, 1, 32767) };
        for (ItemStack paperSource : paperSources) {
            GTValues.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(Materials.Water.getFluid(100))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(paperSource)
                .itemOutputs(new ItemStack(Items.paper, 1, 0))
                .fluidInputs(GTModHandler.getDistilledWater(100))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Coal, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.HydratedCoal, Materials2Shapes.dust, (int) (1)))
            .fluidInputs(GTModHandler.getDistilledWater(125))
            .duration(12 * TICKS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        for (int i = 1; i < 16; i++) {
            // wool cleaning recipes
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.wool, 1, i))
                .itemOutputs(new ItemStack(Blocks.wool, 1, 0))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (50)))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(chemicalBathRecipes);

            // carpet cleaning recipes
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(Blocks.carpet, 1, i))
                .itemOutputs(new ItemStack(Blocks.carpet, 1, 0))
                .fluidInputs(
                    MaterialLibAPI
                        .getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (25)))
                .duration(20 * SECONDS)
                .eut(2)
                .addTo(chemicalBathRecipes);
        }

        // stained hardened clay cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_hardened_clay, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.hardened_clay, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (50)))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // stained glass cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (50)))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // stained glass pane cleaning
        GTValues.RA.stdBuilder()
            .itemInputs(new ItemStack(Blocks.stained_glass_pane, 1, 32767))
            .itemOutputs(new ItemStack(Blocks.glass_pane, 1, 0))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Chlorine, Materials2FluidShapes.fluidGas, (int) (20)))
            .duration(20 * SECONDS)
            .eut(2)
            .addTo(chemicalBathRecipes);

        // light -> dark concrete recipes
        for (int i = 0; i < 8; i++) {
            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i))
                .fluidInputs(Materials.Water.getFluid(250))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i + 8))
                .itemOutputs(new ItemStack(GregTechAPI.sBlockConcretes, 1, i))
                .fluidInputs(GTModHandler.getDistilledWater(250))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        // reinforced blocks
        {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtBronze", 1))
                .itemOutputs(ItemList.Block_BronzePlate.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Concrete,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtSteel", 1))
                .itemOutputs(ItemList.Block_SteelPlate.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Steel,
                        Materials2FluidShapes.fluidMolten,
                        (int) (2 * INGOTS)))
                .duration(12 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtTitanium", 1))
                .itemOutputs(ItemList.Block_TitaniumPlate.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Titanium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(15 * SECONDS)
                .eut(TierEU.RECIPE_LV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtTungstenSteel", 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.TungstenSteel,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(17 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_MV / 2)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtIridium", 1))
                .itemOutputs(ItemList.Block_IridiumTungstensteel.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Iridium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(20 * SECONDS)
                .eut(TierEU.RECIPE_MV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtNaquadah", 1))
                .itemOutputs(ItemList.Block_NaquadahPlate.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Osmium,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(22 * SECONDS + 10 * TICKS)
                .eut(TierEU.RECIPE_HV / 2)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtNeutronium", 1))
                .itemOutputs(ItemList.Block_NeutroniumPlate.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Naquadria,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(25 * SECONDS)
                .eut(TierEU.RECIPE_HV)
                .addTo(chemicalBathRecipes);

            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("frameGtTungstenSteel", 1))
                .itemOutputs(ItemList.Block_TungstenSteelReinforced.get(1))
                .fluidInputs(
                    MaterialLibAPI.getFluidStack(
                        Materials2Materials.Concrete,
                        Materials2FluidShapes.fluidMolten,
                        (int) (1 * INGOTS)))
                .duration(10 * SECONDS)
                .eut(4)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeRed.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("wireGt01RedAlloy", 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 0))
                .fluidInputs(new FluidStack(dyeFluid, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeBlue.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("wireGt01RedAlloy", 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 1))
                .fluidInputs(new FluidStack(dyeFluid, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeGreen.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("wireGt01RedAlloy", 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 2))
                .fluidInputs(new FluidStack(dyeFluid, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(chemicalBathRecipes);
        }

        for (Fluid dyeFluid : Dyes.dyeYellow.getFluidDyes()) {
            GTValues.RA.stdBuilder()
                .itemInputs(GTOreDictUnificator.get("wireGt01RedAlloy", 1))
                .itemOutputs(getModItem(BuildCraftTransport.ID, "pipeWire", 4L, 3))
                .fluidInputs(new FluidStack(dyeFluid, 72))
                .duration(1 * SECONDS + 12 * TICKS)
                .eut(TierEU.RECIPE_LV / 2)
                .addTo(chemicalBathRecipes);
        }

        for (Dyes dye : Dyes.VALUES) {
            for (Fluid dyeFluid : dye.getFluidDyes()) {
                if (dye != Dyes.dyeWhite) {
                    GTValues.RA.stdBuilder()
                        .itemInputs(new ItemStack(Blocks.wool, 1, 0))
                        .itemOutputs(new ItemStack(Blocks.wool, 1, Dyes.transformDyeIndex(dye.mIndex)))
                        .fluidInputs(new FluidStack(dyeFluid, 72))
                        .duration(3 * SECONDS + 4 * TICKS)
                        .eut(2)
                        .addTo(chemicalBathRecipes);
                }

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.glass, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_glass, 1, Dyes.transformDyeIndex(dye.mIndex)))
                    .fluidInputs(new FluidStack(dyeFluid, 18))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(chemicalBathRecipes);

                GTValues.RA.stdBuilder()
                    .itemInputs(new ItemStack(Blocks.hardened_clay, 1, 0))
                    .itemOutputs(new ItemStack(Blocks.stained_hardened_clay, 1, Dyes.transformDyeIndex(dye.mIndex)))
                    .fluidInputs(new FluidStack(dyeFluid, 18))
                    .duration(3 * SECONDS + 4 * TICKS)
                    .eut(2)
                    .addTo(chemicalBathRecipes);
            }
        }

        // Rn relate quantum recipe
        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("gemEnderEye", 1))
            .itemOutputs(ItemList.QuantumEye.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidGas, (int) (250)))
            .duration(24 * SECONDS)
            .eut(384)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get("gemNetherStar", 1))
            .itemOutputs(ItemList.QuantumStar.get(1))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(Materials2Materials.Radon, Materials2FluidShapes.fluidGas, (int) (1250)))
            .duration(1 * MINUTES + 36 * SECONDS)
            .eut(384)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(ItemList.WovenKevlar.get(1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Kevlar, Materials2Shapes.plate, (int) (1)))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.PolyurethaneResin,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (1_000)))
            .duration(60 * SECONDS)
            .eut(TierEU.RECIPE_LV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(WerkstoffLoader.CubicZirconia.get(OrePrefixes.gemExquisite, 1))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.Firestone, Materials2Shapes.gem, (int) (1)))
            .fluidInputs(new FluidStack(FluidRegistry.getFluid("ic2hotcoolant"), 250))
            .duration(2 * SECONDS)
            .eut(TierEU.RECIPE_ZPM)
            .addTo(chemicalBathRecipes);

        // Na + H2O = NaOH + H
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Sodium, Materials2Shapes.dust, (int) (1)))
            .itemOutputs(MaterialLibAPI.getStack(Materials2Materials.SodiumHydroxideGT5U, Materials2Shapes.dust, 3))
            .fluidInputs(Materials.Water.getFluid(1_000))
            .fluidOutputs(
                MaterialLibAPI
                    .getFluidStack(Materials2Materials.Hydrogen, Materials2FluidShapes.fluidGas, (int) (1_000)))
            .duration(5 * SECONDS)
            .eut(4)
            .addTo(chemicalBathRecipes);

        // Custom Sodium Persulfate Ore Processing Recipes

        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Tantalite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Tantalite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Tantalum, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Pyrolusite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Pyrolusite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Manganese, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Quartzite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.CertusQuartz, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Barium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 7000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Bauxite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Bauxite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Rutile, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Thorium, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Uranium, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 3000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);
        GTValues.RA.stdBuilder()
            .itemInputs(MaterialLibAPI.getStack(Materials2Materials.Stibnite, Materials2Shapes.crushed, (int) (1)))
            .itemOutputs(
                MaterialLibAPI.getStack(Materials2Materials.Stibnite, Materials2Shapes.crushedPurified, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Antimony, Materials2Shapes.dust, (int) (1)),
                MaterialLibAPI.getStack(Materials2Materials.Stone, Materials2Shapes.dust, (int) (1)))
            .outputChances(10000, 5000, 4000)
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.SodiumPersulfate,
                    Materials2FluidShapes.fluidLiquid,
                    (int) (100)))
            .duration(40 * SECONDS)
            .eut(TierEU.RECIPE_ULV)
            .addTo(chemicalBathRecipes);

        this.protoHalkoniteRecipes();
    }

    private void protoHalkoniteRecipes() {
        this.addProtoHalkonitePartRecipe(OrePrefixes.frameGt, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.ingot, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plate, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plateDouble, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.plateDense, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.stick, 2);
        this.addProtoHalkonitePartRecipe(OrePrefixes.round, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.bolt, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.screw, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.ring, 4);
        this.addProtoHalkonitePartRecipe(OrePrefixes.foil, 8);
        this.addProtoHalkonitePartRecipe(OrePrefixes.gearGtSmall, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.rotor, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.stickLong, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.gearGt, 1);
        this.addProtoHalkonitePartRecipe(OrePrefixes.wireFine, 8);
    }

    private void addProtoHalkonitePartRecipe(OrePrefixes prefix, final int multiplier) {

        final int partFraction = (int) (prefix.getMaterialAmount() * INGOTS / M);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(prefix, Materials.Infinity, multiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.HotProtoHalkonite, multiplier))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.protohalkonitebase,
                    Materials2FluidShapes.fluidLiquid,
                    (int) ((long) partFraction * multiplier)))
            .duration((int) (multiplier * (8 * SECONDS * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UEV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(prefix, Materials.Creon, multiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.HotProtoHalkonite, multiplier))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.protohalkonitebase,
                    Materials2FluidShapes.fluidLiquid,
                    (int) ((long) partFraction * multiplier / 2L)))
            .duration((int) (multiplier * (2 * SECONDS * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UIV)
            .addTo(chemicalBathRecipes);

        GTValues.RA.stdBuilder()
            .itemInputs(GTOreDictUnificator.get(prefix, Materials.Mellion, multiplier))
            .itemOutputs(GTOreDictUnificator.get(prefix, Materials.HotProtoHalkonite, multiplier))
            .fluidInputs(
                MaterialLibAPI.getFluidStack(
                    Materials2Materials.protohalkonitebase,
                    Materials2FluidShapes.fluidLiquid,
                    (int) ((long) partFraction * multiplier / 2L)))
            .duration((int) (multiplier * (2 * SECONDS * partFraction / (float) INGOTS)))
            .eut(TierEU.RECIPE_UIV)
            .addTo(chemicalBathRecipes);
    }
}
