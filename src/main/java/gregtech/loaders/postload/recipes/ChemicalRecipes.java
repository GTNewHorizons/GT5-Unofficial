package gregtech.loaders.postload.recipes;

import static gregtech.api.enums.GT_Values.*;
import static gregtech.api.enums.ModIDs.*;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.loaders.postload.GT_MachineRecipeLoader.*;
import static net.minecraftforge.fluids.FluidRegistry.getFluidStack;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.*;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class ChemicalRecipes implements Runnable {

    @Override
    public void run() {
        singleBlockOnly();
        multiblockOnly();
        polymerizationRecipes();
        GT_Values.RA.addChemicalRecipe(
                new ItemStack(Items.paper, 1),
                new ItemStack(Items.string, 1),
                Materials.Glyceryl.getFluid(500),
                GT_Values.NF,
                GT_ModHandler.getIC2Item("dynamite", 1L),
                160,
                4);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 3),
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1),
                Materials.SulfuricAcid.getFluid(4000),
                new FluidStack(ItemList.sIndiumConcentrate, 8000),
                null,
                null,
                60,
                150);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                GT_Utility.getIntegratedCircuit(1),
                new FluidStack(ItemList.sIndiumConcentrate, 8000),
                new FluidStack(ItemList.sLeadZincSolution, 8000),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Indium, 1),
                50,
                600);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 36),
                GT_Utility.getIntegratedCircuit(9),
                new FluidStack(ItemList.sIndiumConcentrate, 72000),
                new FluidStack(ItemList.sLeadZincSolution, 72000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Indium, 1),
                450,
                600);

        // Platinum Group Sludge chain
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pentlandite, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000L),
                new FluidStack(ItemList.sNickelSulfate, 2000),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1),
                50,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000L),
                new FluidStack(ItemList.sBlueVitriol, 2000),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.PlatinumGroupSludge, 1),
                50,
                30);

        // Fe + 3HCl = FeCl3 + 3H
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 1),
                ItemList.Cell_Empty.get(3),
                Materials.HydrochloricAcid.getFluid(3000),
                Materials.IronIIIChloride.getFluid(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 3),
                400,
                30);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.InfusedGold, 8L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Iron, 8L),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Thaumium, 16L),
                400,
                480);

        GT_Values.RA.addChemicalRecipe(
                getModItem("GalaxySpace", "item.UnknowCrystal", 4L),
                Materials.Osmiridium.getDust(2),
                Materials.GrowthMediumSterilized.getFluid(1000L),
                getFluidStack("bacterialsludge", 1000),
                ItemList.Circuit_Chip_Stemcell.get(64L),
                GT_Values.NI,
                600,
                30720);
        GT_Values.RA.addChemicalRecipe(
                ItemList.Circuit_Chip_Stemcell.get(32L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CosmicNeutronium, 4),
                Materials.BioMediumSterilized.getFluid(2000L),
                getFluidStack("mutagen", 2000),
                ItemList.Circuit_Chip_Biocell.get(32L),
                GT_Values.NI,
                1200,
                500000);

        GT_Values.RA.addChemicalRecipe(
                new ItemStack(Items.sugar),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Plastic, 1),
                new FluidStack(ItemList.sToluene, 133),
                GT_Values.NF,
                ItemList.GelledToluene.get(2),
                140,
                192);
        GT_Values.RA.addChemicalRecipe(
                new ItemStack(Items.sugar, 9),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plastic, 1),
                new FluidStack(ItemList.sToluene, 1197),
                GT_Values.NF,
                ItemList.GelledToluene.get(18),
                1260,
                192);
        GT_Values.RA.addChemicalRecipe(
                ItemList.GelledToluene.get(4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(250),
                GT_Values.NF,
                new ItemStack(Blocks.tnt, 1),
                200,
                24);
        GT_Values.RA.addChemicalRecipe(
                ItemList.GelledToluene.get(4),
                GT_Utility.getIntegratedCircuit(1),
                new FluidStack(ItemList.sNitrationMixture, 200),
                Materials.DilutedSulfuricAcid.getFluid(150),
                GT_ModHandler.getIC2Item("industrialTnt", 1L),
                80,
                480);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.NatruralGas.getGas(16000),
                Materials.Gas.getGas(16000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NatruralGas, 16L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Gas, 16L),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.SulfuricGas.getGas(16000),
                Materials.Gas.getGas(16000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricGas, 16L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Gas, 16L),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.SulfuricNaphtha.getFluid(12000),
                Materials.Naphtha.getFluid(12000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricNaphtha, 12L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Naphtha, 12L),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.SulfuricLightFuel.getFluid(12000),
                Materials.LightFuel.getFluid(12000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricLightFuel, 12L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.LightFuel, 12L),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.SulfuricHeavyFuel.getFluid(8000),
                Materials.HeavyFuel.getFluid(8000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HydricSulfide, 1L),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfuricHeavyFuel, 8L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.HeavyFuel, 8L),
                160);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Naphtha.getFluid(576),
                Materials.Polycaprolactam.getMolten(1296),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Potassium, 1),
                640);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 9L),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Naphtha.getFluid(5184),
                Materials.Polycaprolactam.getMolten(11664),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Potassium, 1),
                5760);

        for (Fluid tFluid : new Fluid[] { FluidRegistry.WATER, GT_ModHandler.getDistilledWater(1L).getFluid() }) {
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(1L),
                    100);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(1L),
                    100);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(4L),
                    400);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(4L),
                    400);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Apatite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(4L),
                    400);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Glauconite, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.TricalciumPhosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(4L),
                    400);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Phosphate, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(3L),
                    300);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Ash, 3L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.GlauconiteSand, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.DarkAsh, 1L),
                    new FluidStack(tFluid, 1000),
                    GT_Values.NF,
                    ItemList.IC2_Fertilizer.get(2L),
                    200);
        }

        // 3quartz dust + Na + H2O = 3quartz gem (Na loss
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L),
                500);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L),
                500);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                Materials.Water.getFluid(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L),
                500);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_ModHandler.getDistilledWater(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.NetherQuartz, 3L),
                500);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_ModHandler.getDistilledWater(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.CertusQuartz, 3L),
                500);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 1L),
                GT_ModHandler.getDistilledWater(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.Quartzite, 3L),
                500);

        // 3UO2 + 4Al = 3U + 2Al2O3
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 9L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 10L),
                1000);
        // UO2 + 2Mg = U + 2MgO
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uraninite, 3L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesia, 4L),
                1000);
        // Ca + C + 3O = CaCO3
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 1L),
                Materials.Oxygen.getGas(3000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcite, 5L),
                500);
        // C + 4H = CH4
        GT_Values.RA.addChemicalRecipe(
                Materials.Carbon.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(4000L),
                Materials.Methane.getGas(1000L),
                GT_Values.NI,
                200);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Carbon.getDust(1),
                Materials.Empty.getCells(1),
                Materials.Hydrogen.getGas(4000L),
                GT_Values.NF,
                Materials.Methane.getCells(1),
                GT_Values.NI,
                200,
                30);
        // O + 2H = H2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Utility.getIntegratedCircuit(22),
                Materials.Hydrogen.getGas(2000L),
                GT_ModHandler.getDistilledWater(1000L),
                ItemList.Cell_Empty.get(1L),
                GT_Values.NI,
                10,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 1L),
                GT_Utility.getIntegratedCircuit(22),
                Materials.Oxygen.getGas(500L),
                GT_ModHandler.getDistilledWater(500L),
                ItemList.Cell_Empty.get(1L),
                GT_Values.NI,
                5,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(22) },
                new FluidStack[] { Materials.Hydrogen.getGas(16000), Materials.Oxygen.getGas(8000) },
                new FluidStack[] { GT_ModHandler.getDistilledWater(8000) },
                new ItemStack[] {},
                80,
                30);
        // TiO2 + 2C + 4Cl = TiCl4 + 2CO
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Carbon, 2L),
                Materials.Chlorine.getGas(4000L),
                Materials.Titaniumtetrachloride.getFluid(1000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonMonoxide, 2L),
                400,
                480);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Rutile, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Carbon, 2L),
                Materials.Chlorine.getGas(4000L),
                Materials.Titaniumtetrachloride.getFluid(1000L),
                GT_Values.NI,
                400,
                480);
        // 4Na + 2MgCl2 = 2Mg + 4NaCl
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesiumchloride, 6L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 8L),
                400,
                300);
        // rubber
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 9L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 1L),
                GT_Values.NF,
                Materials.Rubber.getMolten(1296L),
                GT_Values.NI,
                600,
                16);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.RawRubber, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Sulfur, 1L),
                GT_Values.NF,
                Materials.Rubber.getMolten(144L),
                GT_Values.NI,
                100,
                16);
        // vanilla recipe
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L),
                new ItemStack(Items.melon, 1, 32767),
                new ItemStack(Items.speckled_melon, 1, 0),
                50);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.nugget, Materials.Gold, 8L),
                new ItemStack(Items.carrot, 1, 32767),
                new ItemStack(Items.golden_carrot, 1, 0),
                50);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Gold, 8L),
                new ItemStack(Items.apple, 1, 32767),
                new ItemStack(Items.golden_apple, 1, 0),
                50);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Gold, 8L),
                new ItemStack(Items.apple, 1, 32767),
                new ItemStack(Items.golden_apple, 1, 1),
                50);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderPearl, 1L),
                GT_OreDictUnificator.get(OrePrefixes.gem, Materials.EnderEye, 1L),
                200,
                480);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Blaze, 1L),
                new ItemStack(Items.slime_ball, 1, 32767),
                new ItemStack(Items.magma_cream, 1, 0),
                50);
        // 1/9U +Air ==Pu== 0.1Rn
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Plutonium, 8),
                GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Uranium, 1),
                Materials.Air.getGas(1000),
                Materials.Radon.getGas(100),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 8),
                12000,
                8);

        // Silicon Line
        // SiO2 + 2Mg = 2MgO + Si
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesia, 4),
                100,
                8);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesia, 4),
                100,
                8);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 6),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesia, 4),
                100,
                8);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesium, 2),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Magnesia, 4),
                100,
                8);

        // 3SiF4 + 4Al = 3Si + 4AlF3
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SiliconTetrafluoride.getGas(3000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 3),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.AluminiumFluoride, 16),
                600,
                30);
        // SiO2 + 4HF = SiF4 + 2H2O
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 3),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.SiliconTetrafluoride.getGas(1000),
                GT_Values.NI,
                300,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.NetherQuartz, 3),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.SiliconTetrafluoride.getGas(1000),
                GT_Values.NI,
                300,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CertusQuartz, 3),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.SiliconTetrafluoride.getGas(1000),
                GT_Values.NI,
                300,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Quartzite, 6),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.SiliconTetrafluoride.getGas(1000),
                GT_Values.NI,
                300,
                30);
        // 4Na + SiCl4 = 4NaCl + Si
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SiliconTetrachloride.getFluid(1000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Salt, 8),
                100,
                30);

        if (BartWorks.isModLoaded()) {
            // CaSi2 + 2HCl = 2Si + CaCl2 + 2H
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CalciumDisilicide, 3),
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.HydrochloricAcid.getFluid(2000),
                    Materials.Hydrogen.getGas(2000),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 2),
                    getModItem("bartworks", "gt.bwMetaGenerateddust", 3L, 63),
                    900,
                    30);
            // SiCl4 + 2Zn = 2ZnCl2 + Si
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Zinc, 2),
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.SiliconTetrachloride.getFluid(1000),
                    GT_Values.NF,
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                    getModItem("bartworks", "gt.bwMetaGenerateddust", 6L, 10052),
                    400,
                    30);
        }
        // Si + 4Cl = SiCl4
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                ItemList.Cell_Empty.get(2L),
                Materials.HydrochloricAcid.getFluid(3000),
                Materials.Trichlorosilane.getFluid(1000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Values.NI,
                300,
                30);

        // HSiCl3 + 2H = 3HCl + Si
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Trichlorosilane.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(3000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                ItemList.Cell_Empty.get(2L),
                300,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Trichlorosilane, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(2000),
                Materials.HydrochloricAcid.getFluid(3000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                ItemList.Cell_Empty.get(1L),
                300,
                30);
        // 4HSiCl3 = 3SiCl4 + SiH4
        GT_Values.RA.addChemicalRecipe(
                ItemList.Cell_Empty.get(1L),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Trichlorosilane.getFluid(4000),
                Materials.SiliconTetrachloride.getFluid(3000),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Silane, 1),
                240,
                30);
        // SiH4 = Si + 4H
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NI,
                Materials.Silane.getGas(1000),
                Materials.Hydrogen.getGas(4000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                300,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Silane, 1),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                Materials.Hydrogen.getGas(4000),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                ItemList.Cell_Empty.get(1L),
                300,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Silane, 1),
                ItemList.Cell_Empty.get(3L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconSG, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 4),
                300,
                30);
        // Ca + 2H = CaH2
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calcium, 1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(2000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Calciumhydride, 3),
                400,
                30);

        // Si + 4Cl = SiCl4
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Chlorine.getGas(4000),
                Materials.SiliconTetrachloride.getFluid(1000),
                null,
                400,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 1),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 4),
                GT_Values.NF,
                Materials.SiliconTetrachloride.getFluid(1000),
                ItemList.Cell_Empty.get(4L),
                GT_Values.NI,
                400,
                30);

        // 2Na + S = Na2S
        GT_Values.RA.addChemicalRecipe(
                Materials.Sodium.getDust(2),
                Materials.Sulfur.getDust(1),
                Materials.SodiumSulfide.getDust(3),
                60);
        // H2S + H2O + (O2) = 0.5H2SO4(Diluted) ( S loss
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(1),
                GT_Values.NI,
                Materials.Water.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(750),
                Materials.Empty.getCells(1),
                60);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Values.NI,
                Materials.HydricSulfide.getGas(1000),
                Materials.DilutedSulfuricAcid.getFluid(750),
                Materials.Empty.getCells(1),
                60);

        // Ni + 4CO = Ni(CO)4
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.CarbonMonoxide, 4L),
                GT_Values.NF,
                MaterialsKevlar.NickelTetracarbonyl.getFluid(1000),
                ItemList.Cell_Empty.get(4L),
                400,
                1920);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                GT_Utility.getIntegratedCircuit(1),
                Materials.CarbonMonoxide.getGas(4000),
                MaterialsKevlar.NickelTetracarbonyl.getFluid(1000),
                GT_Values.NI,
                400,
                1920);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Nickel, 1L),
                ItemList.Cell_Empty.get(1L),
                Materials.CarbonMonoxide.getGas(4000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.NickelTetracarbonyl, 1L),
                400,
                1920);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.EthyleneOxide, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 5L),
                Materials.Dimethyldichlorosilane.getFluid(4000),
                MaterialsKevlar.SiliconOil.getFluid(5000),
                ItemList.Cell_Empty.get(6L),
                GT_Values.NI,
                600,
                480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.EthyleneOxide, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Dimethyldichlorosilane, 4L),
                Materials.Water.getFluid(5000),
                MaterialsKevlar.SiliconOil.getFluid(5000),
                ItemList.Cell_Empty.get(5L),
                GT_Values.NI,
                600,
                480);

        // C2H4O + H2O = C2H6O2
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.EthyleneOxide, 1L),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                MaterialsKevlar.Ethyleneglycol.getFluid(1000),
                ItemList.Cell_Empty.get(1L),
                200,
                480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                GT_Utility.getIntegratedCircuit(2),
                MaterialsKevlar.EthyleneOxide.getGas(1000),
                MaterialsKevlar.Ethyleneglycol.getFluid(1000),
                ItemList.Cell_Empty.get(1L),
                GT_Values.NI,
                200,
                480);
        // C2H4 + O = C2H4O
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Ethylene, 2L),
                GT_Utility.getIntegratedCircuit(4),
                Materials.Oxygen.getGas(1000),
                MaterialsKevlar.Acetaldehyde.getGas(1000),
                ItemList.Cell_Empty.get(2),
                200,
                120);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Utility.getIntegratedCircuit(5),
                Materials.Ethylene.getGas(2000),
                MaterialsKevlar.Acetaldehyde.getGas(1000),
                ItemList.Cell_Empty.get(1),
                200,
                120);

        // NiAl3 + 2NaOH + 2H2O = NiAl + 2NaAlO2 + 6H
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.ingot, MaterialsKevlar.NickelAluminide, 4L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 6L),
                Materials.Water.getFluid(2000),
                Materials.Hydrogen.getGas(6000),
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.RaneyNickelActivated, 2L),
                Materials.SodiumAluminate.getDust(8),
                1200,
                1920);
        // Cu + O = CuO
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 1L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CupricOxide, 2L),
                ItemList.Cell_Empty.get(1),
                100,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CupricOxide, 2L),
                100,
                30);
        // 2Bi + 3O = Bi2O3
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Oxygen, 6L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.BismuthIIIOxide, 10L),
                ItemList.Cell_Empty.get(6),
                200,
                120);
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Bismuth, 4L),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(6000L),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.BismuthIIIOxide, 10L),
                200,
                120);

        // C4H6O2 + CNH5 = C5H9NO + H2O
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.Methylamine, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.GammaButyrolactone, 1L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.NMethylIIPyrrolidone, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Water, 1L),
                600,
                7680);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 8L),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Chlorine.getGas(16000),
                MaterialsKevlar.SulfurDichloride.getFluid(8000),
                GT_Values.NI,
                GT_Values.NI,
                800,
                30);
        // SCl2 + SO3 = SO2 + SOCl2
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.SulfurTrioxide, 1L),
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.SulfurDichloride, 1L),
                GT_Values.NF,
                Materials.SulfurDioxide.getGas(1000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.ThionylChloride, 1L),
                ItemList.Cell_Empty.get(1),
                150,
                480);

        // C8H10 + 6O =CoC22H14O4= C8H6O4 + 2H2O
        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.IVDimethylbenzene, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CobaltIINaphthenate, 41L),
                Materials.Oxygen.getGas(6000L),
                Materials.Water.getFluid(2000L),
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.TerephthalicAcid, 1L),
                150,
                480);

        // 2CH4 + C6H6 = C8H10 + 4H
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(2),
                GT_Utility.getIntegratedCircuit(13),
                Materials.Benzene.getFluid(1000),
                Materials.Hydrogen.getGas(4000),
                MaterialsKevlar.IIIDimethylbenzene.getCells(1),
                Materials.Empty.getCells(1),
                4000,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(14),
                Materials.Methane.getGas(2000),
                Materials.Hydrogen.getGas(4000),
                MaterialsKevlar.IIIDimethylbenzene.getCells(1),
                4000,
                120);

        // 2CH4 + C6H6 = C8H10 + 4H
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(2),
                GT_Utility.getIntegratedCircuit(15),
                Materials.Benzene.getFluid(1000),
                Materials.Hydrogen.getGas(4000),
                MaterialsKevlar.IVDimethylbenzene.getCells(1),
                Materials.Empty.getCells(1),
                4000,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(16),
                Materials.Methane.getGas(2000),
                Materials.Hydrogen.getGas(4000),
                MaterialsKevlar.IVDimethylbenzene.getCells(1),
                4000,
                120);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                MaterialsKevlar.CobaltIIHydroxide.getDust(5),
                MaterialsKevlar.NaphthenicAcid.getCells(1),
                GT_Values.NF,
                GT_Values.NF,
                MaterialsKevlar.CobaltIINaphthenate.getDust(41),
                ItemList.Cell_Empty.get(1L),
                100,
                480);
        GT_Values.RA.addChemicalRecipe(
                MaterialsKevlar.CobaltIIHydroxide.getDust(5),
                GT_Utility.getIntegratedCircuit(1),
                MaterialsKevlar.NaphthenicAcid.getFluid(1000L),
                GT_Values.NF,
                MaterialsKevlar.CobaltIINaphthenate.getDust(41),
                200,
                480);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                MaterialsKevlar.CobaltIIAcetate.getDust(15),
                MaterialsKevlar.NaphthenicAcid.getCells(1),
                GT_Values.NF,
                Materials.AceticAcid.getFluid(1500L),
                MaterialsKevlar.CobaltIINaphthenate.getDust(41),
                ItemList.Cell_Empty.get(1L),
                100,
                480);
        GT_Values.RA.addChemicalRecipe(
                MaterialsKevlar.CobaltIIAcetate.getDust(15),
                GT_Utility.getIntegratedCircuit(1),
                MaterialsKevlar.NaphthenicAcid.getFluid(1000L),
                Materials.AceticAcid.getFluid(1500L),
                MaterialsKevlar.CobaltIINaphthenate.getDust(41),
                100,
                480);

        // Co + 2HNO3 = Co(NO3)2 + 2H
        GT_Values.RA.addChemicalRecipe(
                Materials.Cobalt.getDust(1),
                Materials.NitricAcid.getCells(2),
                GT_Values.NF,
                GT_Values.NF,
                MaterialsKevlar.CobaltIINitrate.getDust(9),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Hydrogen, 2L),
                100,
                120);
        // Co(NO3)2 + 2KOH = CoH2O2 + 2KNO3
        GT_Values.RA.addChemicalRecipe(
                MaterialsKevlar.CobaltIINitrate.getDust(9),
                getModItem(MOD_ID_DC, "item.PotassiumHydroxideDust", 6L, 0),
                GT_Values.NF,
                GT_Values.NF,
                MaterialsKevlar.CobaltIIHydroxide.getDust(5),
                Materials.Saltpeter.getDust(10),
                100,
                120);
        // CoO + 2C2H4O2 = CoC4H6O4 + 2H
        GT_Values.RA.addChemicalRecipe(
                Materials.CobaltOxide.getDust(2),
                Materials.AceticAcid.getCells(2),
                GT_Values.NF,
                Materials.Water.getFluid(2000L),
                MaterialsKevlar.CobaltIIAcetate.getDust(15),
                ItemList.Cell_Empty.get(2L),
                100,
                120);

        // P + 3Cl = PCl3
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Phosphorus.getDust(1),
                Materials.Chlorine.getCells(3),
                GT_Values.NF,
                MaterialsKevlar.PhosphorusTrichloride.getFluid(1000L),
                ItemList.Cell_Empty.get(3L),
                GT_Values.NI,
                200,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Phosphorus.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(3000L),
                MaterialsKevlar.PhosphorusTrichloride.getFluid(1000L),
                GT_Values.NI,
                200,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Phosphorus.getDust(9),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Chlorine.getGas(27000L),
                MaterialsKevlar.PhosphorusTrichloride.getFluid(9000L),
                GT_Values.NI,
                1500,
                120);

        // Na + H = NaH
        GT_Values.RA.addChemicalRecipe(
                Materials.Sodium.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Hydrogen.getGas(1000L),
                GT_Values.NF,
                MaterialsKevlar.SodiumHydride.getDust(2),
                200,
                120);

        // CH3ONa + H2O = CH4O + NaOH
        GT_Values.RA.addChemicalRecipe(
                MaterialsKevlar.SodiumMethoxide.getDust(6),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000L),
                Materials.Methanol.getFluid(1000L),
                Materials.SodiumHydroxide.getDust(3),
                200,
                480);

        // K + HNO3 = KNO3 + H (not real, but gameplay
        GT_Values.RA.addChemicalRecipe(
                Materials.Potassium.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.NitricAcid.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.Saltpeter.getDust(5),
                100,
                30);

        // CH3COOH + CH3OH = CH3COOCH3 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Methanol.getFluid(1000),
                Materials.MethylAcetate.getFluid(1000),
                Materials.Water.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.AceticAcid.getFluid(1000),
                Materials.MethylAcetate.getFluid(1000),
                Materials.Water.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Methanol.getFluid(1000),
                Materials.MethylAcetate.getFluid(1000),
                Materials.Empty.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.AceticAcid.getFluid(1000),
                Materials.MethylAcetate.getFluid(1000),
                Materials.Empty.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Methanol.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.MethylAcetate.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.AceticAcid.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.MethylAcetate.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Methanol.getFluid(1000),
                GT_Values.NF,
                Materials.MethylAcetate.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.AceticAcid.getFluid(1000),
                GT_Values.NF,
                Materials.MethylAcetate.getCells(1),
                240);

        // CO and CO2 recipes
        GT_Values.RA.addChemicalRecipe(
                Materials.Carbon.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.CarbonMonoxide.getGas(1000),
                GT_Values.NI,
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getGems(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                80,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                80,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getGems(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                80,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                80,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Carbon.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(2000),
                Materials.CarbonDioxide.getGas(1000),
                GT_Values.NI,
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getGems(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getGems(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getDust(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(2000),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Ash.getDustTiny(1),
                40,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Carbon.getDust(1),
                GT_Values.NI,
                Materials.CarbonDioxide.getGas(1000),
                Materials.CarbonMonoxide.getGas(2000),
                GT_Values.NI,
                800);

        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getGems(9),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Oxygen.getGas(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Ash.getDust(1),
                720,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getDust(9),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Oxygen.getGas(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Ash.getDust(1),
                720,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getGems(9),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Oxygen.getGas(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Ash.getDust(1),
                720,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getDust(9),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Oxygen.getGas(9000),
                Materials.CarbonMonoxide.getGas(9000),
                Materials.Ash.getDust(1),
                720,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getGems(9),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Oxygen.getGas(18000),
                Materials.CarbonDioxide.getGas(9000),
                Materials.Ash.getDust(1),
                360,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Coal.getDust(9),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Oxygen.getGas(18000),
                Materials.CarbonDioxide.getGas(9000),
                Materials.Ash.getDust(1),
                360,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getGems(9),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Oxygen.getGas(18000),
                Materials.CarbonDioxide.getGas(9000),
                Materials.Ash.getDust(1),
                360,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Charcoal.getDust(9),
                GT_Utility.getIntegratedCircuit(8),
                Materials.Oxygen.getGas(18000),
                Materials.CarbonDioxide.getGas(9000),
                Materials.Ash.getDust(1),
                360,
                8);
        // CO + 4H = CH3OH
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonMonoxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(4000),
                Materials.Methanol.getFluid(1000),
                Materials.Empty.getCells(1),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.Methanol.getFluid(1000),
                Materials.Empty.getCells(4),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonMonoxide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Hydrogen.getGas(4000),
                GT_Values.NF,
                Materials.Methanol.getCells(1),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(4),
                GT_Utility.getIntegratedCircuit(11),
                Materials.CarbonMonoxide.getGas(1000),
                GT_Values.NF,
                Materials.Methanol.getCells(1),
                Materials.Empty.getCells(3),
                120,
                96);
        // CO2 + 6H = CH3OH + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(6000),
                Materials.Methanol.getFluid(1000),
                Materials.Water.getCells(1),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(6),
                GT_Utility.getIntegratedCircuit(1),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Methanol.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(5),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Hydrogen.getGas(6000),
                Materials.Methanol.getFluid(1000),
                Materials.Empty.getCells(1),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(6),
                GT_Utility.getIntegratedCircuit(2),
                Materials.CarbonDioxide.getGas(1000),
                Materials.Methanol.getFluid(1000),
                Materials.Empty.getCells(6),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Hydrogen.getGas(6000),
                GT_Values.NF,
                Materials.Methanol.getCells(1),
                120,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(6),
                GT_Utility.getIntegratedCircuit(12),
                Materials.CarbonDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Methanol.getCells(1),
                Materials.Empty.getCells(5),
                120,
                96);

        // CH3OH + CO = CH3COOH
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.CarbonMonoxide.getGas(1000),
                Materials.AceticAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                300);
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonMonoxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Methanol.getFluid(1000),
                Materials.AceticAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                300);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.CarbonMonoxide.getGas(1000),
                GT_Values.NF,
                Materials.AceticAcid.getCells(1),
                300);
        GT_Values.RA.addChemicalRecipe(
                Materials.CarbonMonoxide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Methanol.getFluid(1000),
                GT_Values.NF,
                Materials.AceticAcid.getCells(1),
                300);
        // CH2CH2 + 2O = CH3COOH
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Oxygen.getGas(2000),
                Materials.AceticAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                100);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(2),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Ethylene.getGas(1000),
                Materials.AceticAcid.getFluid(1000),
                Materials.Empty.getCells(2),
                100);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(19),
                Materials.Oxygen.getGas(2000),
                GT_Values.NF,
                Materials.AceticAcid.getCells(1),
                100);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(2),
                GT_Utility.getIntegratedCircuit(19),
                Materials.Ethylene.getGas(1000),
                GT_Values.NF,
                Materials.AceticAcid.getCells(1),
                Materials.Empty.getCells(1),
                100);

        // O + C2H4O2 + C2H4 = C4H6O2 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                Materials.AceticAcid.getCells(1),
                Materials.Oxygen.getGas(1000),
                Materials.VinylAcetate.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                180);
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                Materials.Oxygen.getCells(1),
                Materials.Ethylene.getGas(1000),
                Materials.VinylAcetate.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                180);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(1),
                Materials.Ethylene.getCells(1),
                Materials.AceticAcid.getFluid(1000),
                Materials.VinylAcetate.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                180);

        // Ethanol -> Ethylene (Intended loss for Sulfuric Acid)
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethanol.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Ethylene.getCells(1),
                1200,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethanol.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Ethylene.getCells(1),
                1200,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethanol.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Ethylene.getGas(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                1200,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Ethanol.getFluid(1000),
                Materials.Ethylene.getGas(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                1200,
                120);

        // H2O + Na = NaOH + H
        GT_Values.RA.addChemicalRecipe(
                Materials.Sodium.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.SodiumHydroxide.getDust(3),
                200,
                30);

        // H + Cl = HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Hydrogen.getGas(1000),
                GT_Values.NF,
                Materials.HydrochloricAcid.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorine.getGas(1000),
                GT_Values.NF,
                Materials.HydrochloricAcid.getCells(1),
                60,
                8);

        // C3H6 + 2Cl = HCl + C3H5Cl
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Propene.getGas(1000),
                Materials.AllylChloride.getFluid(1000),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                Materials.AllylChloride.getFluid(1000),
                Materials.HydrochloricAcid.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Propene.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.AllylChloride.getCells(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorine.getGas(2000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.AllylChloride.getCells(1),
                160);

        // H2O + Cl =Hg= HClO + H
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Chlorine.getCells(10),
                Materials.Mercury.getCells(1),
                Materials.Water.getFluid(10000),
                Materials.HypochlorousAcid.getFluid(10000),
                Materials.Hydrogen.getCells(10),
                Materials.Empty.getCells(1),
                600,
                8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Water.getCells(10),
                Materials.Mercury.getCells(1),
                Materials.Chlorine.getGas(10000),
                Materials.HypochlorousAcid.getFluid(10000),
                Materials.Hydrogen.getCells(10),
                Materials.Empty.getCells(1),
                600,
                8);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Chlorine.getCells(1),
                Materials.Water.getCells(1),
                Materials.Mercury.getFluid(100),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.Hydrogen.getCells(1),
                Materials.Empty.getCells(1),
                60,
                8);

        // 2Cl + H2O = HCl + HClO (Intended loss)
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getCells(1),
                GT_Values.NI,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Water.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.HypochlorousAcid.getCells(1),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorine.getGas(2000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.HypochlorousAcid.getCells(1),
                GT_Values.NI,
                120);

        // HClO + NaOH + C3H5Cl = C3H5ClO + NaCl·H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.HypochlorousAcid.getCells(1),
                Materials.SodiumHydroxide.getDust(3),
                Materials.AllylChloride.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.SaltWater.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(3),
                Materials.AllylChloride.getCells(1),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.SaltWater.getCells(1),
                480);
        // HCl + C3H8O3 = C3H5ClO + 2H2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.HydrochloricAcid.getCells(1),
                Materials.Glycerol.getCells(1),
                GT_Values.NF,
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.Water.getCells(2),
                GT_Values.NI,
                480,
                30);

        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Glycerol.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.Water.getCells(2),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.Glycerol.getCells(1),
                Materials.Empty.getCells(1),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.Water.getCells(2),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Glycerol.getFluid(1000),
                Materials.Water.getFluid(2000),
                Materials.Epichlorohydrin.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.Glycerol.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Water.getFluid(2000),
                Materials.Epichlorohydrin.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Glycerol.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.Empty.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.Glycerol.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Epichlorohydrin.getFluid(1000),
                Materials.Empty.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Glycerol.getFluid(1000),
                GT_Values.NF,
                Materials.Epichlorohydrin.getCells(1),
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.Glycerol.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.HydrochloricAcid.getFluid(1000),
                GT_Values.NF,
                Materials.Epichlorohydrin.getCells(1),
                480);

        // Ca5(PO4)3Cl + 5H2SO4 + 10H2O = 5CaSO4(H2O)2 + HCl + 3H3PO4
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Apatite.getDust(9),
                Materials.SulfuricAcid.getCells(5),
                Materials.Water.getFluid(10000),
                Materials.PhosphoricAcid.getFluid(3000),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(4),
                320,
                30);

        // 10O + 4P = P4O10
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Phosphorus.getDust(4),
                GT_Values.NI,
                Materials.Oxygen.getGas(10000),
                GT_Values.NF,
                Materials.PhosphorousPentoxide.getDust(14),
                GT_Values.NI,
                40,
                30);

        // P4O10 + 6H2O = 4H3PO4
        GT_Values.RA.addChemicalRecipe(
                Materials.PhosphorousPentoxide.getDust(14),
                GT_Values.NI,
                Materials.Water.getFluid(6000),
                Materials.PhosphoricAcid.getFluid(4000),
                GT_Values.NI,
                40);

        // C9H12 + 2O = C6H6O + C3H6O
        GT_Values.RA.addChemicalRecipe(
                Materials.Cumene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(2000),
                Materials.Acetone.getFluid(1000),
                Materials.Phenol.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Cumene.getFluid(1000),
                Materials.Acetone.getFluid(1000),
                Materials.Phenol.getCells(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Cumene.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Oxygen.getGas(2000),
                Materials.Phenol.getFluid(1000),
                Materials.Acetone.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Cumene.getFluid(1000),
                Materials.Phenol.getFluid(1000),
                Materials.Acetone.getCells(1),
                Materials.Empty.getCells(1),
                160);

        // C15H16O2 + 2C3H5ClO + 2NaOH = C15H14O2(C3H5O)2 + 2NaCl·H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(6),
                Materials.Epichlorohydrin.getCells(2),
                Materials.BisphenolA.getFluid(1000),
                Materials.Epoxid.getMolten(1000),
                Materials.SaltWater.getCells(2),
                200);

        // CH4O + HCl = CH3Cl + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chloromethane.getGas(1000),
                Materials.Water.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Methanol.getFluid(1000),
                Materials.Chloromethane.getGas(1000),
                Materials.Water.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.Chloromethane.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Methanol.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.Chloromethane.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chloromethane.getGas(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Methanol.getFluid(1000),
                Materials.Chloromethane.getGas(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.HydrochloricAcid.getFluid(1000),
                GT_Values.NF,
                Materials.Chloromethane.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrochloricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Methanol.getFluid(1000),
                GT_Values.NF,
                Materials.Chloromethane.getCells(1),
                160);

        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Methane.getGas(1000),
                Materials.Chloromethane.getGas(1000),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                Materials.Chloromethane.getGas(1000),
                Materials.HydrochloricAcid.getCells(1),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Methane.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chloromethane.getCells(1),
                Materials.Empty.getCells(1),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorine.getGas(2000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chloromethane.getCells(1),
                80);

        // Cl6 + CH4 = CHCl3 + 3HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(6),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Methane.getGas(1000),
                Materials.Chloroform.getFluid(1000),
                Materials.HydrochloricAcid.getCells(3),
                Materials.Empty.getCells(3),
                80);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Methane.getCells(1),
                Materials.Empty.getCells(2),
                Materials.Chlorine.getGas(6000),
                Materials.Chloroform.getFluid(1000),
                Materials.HydrochloricAcid.getCells(3),
                GT_Values.NI,
                80,
                30);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(6),
                GT_Utility.getIntegratedCircuit(13),
                Materials.Methane.getGas(1000),
                Materials.HydrochloricAcid.getFluid(3000),
                Materials.Chloroform.getCells(1),
                Materials.Empty.getCells(5),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(1),
                GT_Utility.getIntegratedCircuit(13),
                Materials.Chlorine.getGas(6000),
                Materials.HydrochloricAcid.getFluid(3000),
                Materials.Chloroform.getCells(1),
                80);

        // H + F = HF
        GT_Values.RA.addChemicalRecipe(
                Materials.Fluorine.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(1000),
                Materials.HydrofluoricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Fluorine.getGas(1000),
                Materials.HydrofluoricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Fluorine.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Hydrogen.getGas(1000),
                GT_Values.NF,
                Materials.HydrofluoricAcid.getCells(1),
                60,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Hydrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Fluorine.getGas(1000),
                GT_Values.NF,
                Materials.HydrofluoricAcid.getCells(1),
                60,
                8);

        // 4HF + 2CHCl3 = C2F4 + 6HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Chloroform.getCells(2),
                Materials.HydrofluoricAcid.getCells(4),
                GT_Values.NF,
                Materials.Tetrafluoroethylene.getGas(1000),
                Materials.HydrochloricAcid.getCells(6),
                480,
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chloroform.getCells(2),
                Materials.Empty.getCells(4),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.Tetrafluoroethylene.getGas(1000),
                Materials.HydrochloricAcid.getCells(6),
                480,
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrofluoricAcid.getCells(4),
                Materials.Empty.getCells(2),
                Materials.Chloroform.getFluid(2000),
                Materials.Tetrafluoroethylene.getGas(1000),
                Materials.HydrochloricAcid.getCells(6),
                480,
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydrofluoricAcid.getCells(4),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chloroform.getFluid(2000),
                Materials.HydrochloricAcid.getFluid(6000),
                Materials.Tetrafluoroethylene.getCells(1),
                Materials.Empty.getCells(3),
                480,
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chloroform.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HydrofluoricAcid.getFluid(4000),
                Materials.HydrochloricAcid.getFluid(6000),
                Materials.Tetrafluoroethylene.getCells(1),
                Materials.Empty.getCells(1),
                480,
                240);

        // Si + 2CH3Cl = C2H6Cl2Si
        GT_Values.RA.addChemicalRecipe(
                Materials.Silicon.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chloromethane.getGas(2000),
                Materials.Dimethyldichlorosilane.getFluid(1000),
                GT_Values.NI,
                240,
                96);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Silicon.getDust(1),
                Materials.Chloromethane.getCells(2),
                GT_Values.NF,
                Materials.Dimethyldichlorosilane.getFluid(1000),
                Materials.Empty.getCells(2),
                GT_Values.NI,
                240,
                96);

        GT_Values.RA.addChemicalRecipe(
                Materials.Dimethyldichlorosilane.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.Polydimethylsiloxane.getDust(3),
                Materials.Empty.getCells(1),
                240,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Dimethyldichlorosilane.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.Polydimethylsiloxane.getDust(3),
                Materials.Empty.getCells(1),
                240,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Dimethyldichlorosilane.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Water.getFluid(1000),
                GT_Values.NF,
                Materials.Polydimethylsiloxane.getDust(3),
                Materials.DilutedHydrochloricAcid.getCells(1),
                240,
                96);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Dimethyldichlorosilane.getFluid(1000),
                GT_Values.NF,
                Materials.Polydimethylsiloxane.getDust(3),
                Materials.DilutedHydrochloricAcid.getCells(1),
                240,
                96);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Dimethyldichlorosilane.getCells(1),
                Materials.Water.getCells(1),
                GT_Values.NF,
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.Polydimethylsiloxane.getDust(3),
                Materials.Empty.getCells(2),
                240,
                96);

        GT_Values.RA.addChemicalRecipe(
                Materials.Polydimethylsiloxane.getDust(9),
                Materials.Sulfur.getDust(1),
                GT_Values.NF,
                Materials.Silicone.getMolten(1296),
                GT_Values.NI,
                600);

        // Potassium Nitride
        // K + HNO3 = KNO3 + H
        GT_Values.RA.addChemicalRecipe(
                Materials.Potassium.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitricAcid.getFluid(1000),
                Materials.Hydrogen.getGas(1000),
                Materials.PotassiumNitrade.getDust(5),
                100,
                30);

        // Chromium Trioxide
        // CrO2 + O = CrO3
        GT_Values.RA.addChemicalRecipe(
                Materials.ChromiumDioxide.getDust(3),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.ChromiumTrioxide.getDust(4),
                GT_Values.NI,
                100,
                60);

        // Nitrochlorobenzene
        // C6H5Cl + HNO3 = C6H4ClNO2 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorobenzene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitrationMixture.getFluid(2000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Nitrochlorobenzene.getCells(1),
                100,
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorobenzene.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.NitrationMixture.getFluid(2000),
                Materials.Nitrochlorobenzene.getFluid(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                100,
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrationMixture.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorobenzene.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Nitrochlorobenzene.getCells(1),
                Materials.Empty.getCells(1),
                100,
                480);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrationMixture.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorobenzene.getFluid(1000),
                Materials.Nitrochlorobenzene.getFluid(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                Materials.Empty.getCells(1),
                100,
                480);

        // C6H6 + 2CH4 = C8H10 + 4H
        GT_Values.RA.addChemicalRecipe(
                Materials.Methane.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Benzene.getFluid(1000),
                Materials.Hydrogen.getGas(4000),
                Materials.Dimethylbenzene.getCells(1),
                Materials.Empty.getCells(1),
                4000,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Methane.getGas(2000),
                Materials.Hydrogen.getGas(4000),
                Materials.Dimethylbenzene.getCells(1),
                4000,
                120);

        // Phthalic Acid
        // C8H10 + 6O =K2Cr2O7= C8H6O4 + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Dimethylbenzene.getCells(1),
                Materials.Potassiumdichromate.getDustTiny(1),
                Materials.Oxygen.getGas(6000),
                Materials.Water.getFluid(2000),
                Materials.PhthalicAcid.getCells(1),
                100,
                1920);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(6),
                Materials.Potassiumdichromate.getDustTiny(1),
                Materials.Dimethylbenzene.getFluid(1000),
                Materials.Water.getFluid(2000),
                Materials.PhthalicAcid.getCells(1),
                ItemList.Cell_Empty.get(5L),
                100,
                1920);

        GT_Values.RA.addChemicalRecipe(
                Materials.Dimethylbenzene.getCells(9),
                Materials.Potassiumdichromate.getDust(1),
                Materials.Oxygen.getGas(54000),
                Materials.Water.getFluid(18000),
                Materials.PhthalicAcid.getCells(9),
                900,
                1920);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(54),
                Materials.Potassiumdichromate.getDust(1),
                Materials.Dimethylbenzene.getFluid(9000),
                Materials.Water.getFluid(18000),
                Materials.PhthalicAcid.getCells(9),
                ItemList.Cell_Empty.get(45L),
                900,
                1920);

        // These following recipes are broken in element term.
        // But they are kept in gamewise, too much existed setup will be broken.
        // Dichlorobenzidine
        GT_Values.RA.addChemicalRecipe(
                Materials.Copper.getDustTiny(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Nitrochlorobenzene.getFluid(2000),
                Materials.Dichlorobenzidine.getFluid(1000),
                null,
                200,
                1920);
        GT_Values.RA.addChemicalRecipe(
                Materials.Copper.getDust(1),
                GT_Utility.getIntegratedCircuit(9),
                Materials.Nitrochlorobenzene.getFluid(18000),
                Materials.Dichlorobenzidine.getFluid(9000),
                null,
                1800,
                1920);

        // Diphenyl Isophthalate
        GT_Values.RA.addChemicalRecipe(
                Materials.PhthalicAcid.getCells(1),
                Materials.SulfuricAcid.getCells(1),
                Materials.Phenol.getFluid(2000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Diphenylisophthalate.getCells(1),
                ItemList.Cell_Empty.get(1L),
                100,
                7680);
        GT_Values.RA.addChemicalRecipe(
                Materials.PhthalicAcid.getCells(1),
                Materials.Phenol.getCells(2),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Diphenylisophthalate.getCells(1),
                ItemList.Cell_Empty.get(2L),
                100,
                7680);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                Materials.Phenol.getCells(2),
                Materials.PhthalicAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Diphenylisophthalate.getCells(1),
                ItemList.Cell_Empty.get(2L),
                100,
                7680);

        // Diaminobenzidin
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(2),
                Materials.Zinc.getDust(1),
                Materials.Dichlorobenzidine.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(2000),
                Materials.Diaminobenzidin.getCells(1),
                ItemList.Cell_Empty.get(1L),
                100,
                7680);

        // Polybenzimidazole
        // C12H14N4 + C20H14O4 = C20H12N4 + 2C6H6O + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Diphenylisophthalate.getCells(1),
                Materials.Diaminobenzidin.getCells(1),
                GT_Values.NF,
                Materials.Polybenzimidazole.getMolten(1000),
                Materials.Phenol.getCells(2),
                100,
                7680);

        GT_Values.RA.addChemicalRecipe(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L),
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Saltpeter, 1L),
                Materials.Glass.getMolten(864L),
                GT_Values.NF,
                getModItem(MOD_ID_RC, "tile.railcraft.glass", 6L),
                50);

        // NH3 + 2CH4O = C2H7N + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ammonia.getGas(1000),
                Materials.Dimethylamine.getGas(1000),
                Materials.Water.getCells(2),
                240,
                120);

        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Ammonia.getGas(1000),
                Materials.Water.getFluid(1000),
                Materials.Dimethylamine.getCells(1),
                Materials.Empty.getCells(1),
                240,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Methanol.getFluid(2000),
                Materials.Water.getFluid(1000),
                Materials.Dimethylamine.getCells(1),
                240,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Ammonia.getGas(1000),
                Materials.Dimethylamine.getGas(1000),
                Materials.Empty.getCells(2),
                240,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Methanol.getCells(2),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Ammonia.getGas(1000),
                GT_Values.NF,
                Materials.Dimethylamine.getCells(1),
                Materials.Empty.getCells(1),
                240,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Methanol.getFluid(2000),
                GT_Values.NF,
                Materials.Dimethylamine.getCells(1),
                240,
                120);

        // NH3 + HClO = NH2Cl + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.Chloramine.getFluid(1000),
                Materials.Water.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HypochlorousAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ammonia.getGas(1000),
                Materials.Chloramine.getFluid(1000),
                Materials.Water.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.Chloramine.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HypochlorousAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Ammonia.getGas(1000),
                Materials.Water.getFluid(1000),
                Materials.Chloramine.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HypochlorousAcid.getFluid(1000),
                Materials.Chloramine.getFluid(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HypochlorousAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Ammonia.getGas(1000),
                Materials.Chloramine.getFluid(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.HypochlorousAcid.getFluid(1000),
                GT_Values.NF,
                Materials.Chloramine.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.HypochlorousAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Ammonia.getGas(1000),
                GT_Values.NF,
                Materials.Chloramine.getCells(1),
                160);

        // 2NO2 = N2O4
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NI,
                Materials.NitrogenDioxide.getGas(2000),
                Materials.DinitrogenTetroxide.getGas(1000),
                GT_Values.NI,
                640);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrogenDioxide.getCells(2),
                GT_Utility.getIntegratedCircuit(2),
                GT_Values.NF,
                Materials.DinitrogenTetroxide.getGas(1000),
                Materials.Empty.getCells(2),
                640);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrogenDioxide.getCells(2),
                GT_Utility.getIntegratedCircuit(12),
                GT_Values.NF,
                GT_Values.NF,
                Materials.DinitrogenTetroxide.getCells(1),
                Materials.Empty.getCells(1),
                640);

        // 2NH3 + 5O = 2NO + 3H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(4),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(10000),
                Materials.Water.getFluid(6000),
                Materials.NitricOxide.getCells(4),
                320);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(10),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ammonia.getGas(4000),
                Materials.Water.getFluid(6000),
                Materials.NitricOxide.getCells(4),
                Materials.Empty.getCells(6),
                320);

        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(10),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Ammonia.getGas(4000),
                Materials.NitricOxide.getGas(4000),
                Materials.Water.getCells(6),
                Materials.Empty.getCells(4),
                320);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ammonia.getCells(4),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(10000),
                GT_Values.NF,
                Materials.NitricOxide.getCells(4),
                320);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(10),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Ammonia.getGas(4000),
                GT_Values.NF,
                Materials.NitricOxide.getCells(4),
                Materials.Empty.getCells(6),
                320);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(10),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Ammonia.getGas(4000),
                Materials.NitricOxide.getGas(4000),
                Materials.Empty.getCells(10),
                320);

        // NO + O = NO2
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricOxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.NitrogenDioxide.getGas(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitricOxide.getGas(1000),
                Materials.NitrogenDioxide.getGas(1000),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricOxide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.NitrogenDioxide.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.NitricOxide.getGas(1000),
                GT_Values.NF,
                Materials.NitrogenDioxide.getCells(1),
                160);

        // H2O + 3NO2 = 2HNO3 + NO
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitrogenDioxide.getGas(3000),
                Materials.NitricAcid.getFluid(2000),
                Materials.NitricOxide.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrogenDioxide.getCells(3),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.NitricAcid.getFluid(2000),
                Materials.NitricOxide.getCells(1),
                Materials.Empty.getCells(2),
                240);

        GT_Values.RA.addChemicalRecipe(
                Materials.NitrogenDioxide.getCells(3),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Water.getFluid(1000),
                Materials.NitricOxide.getGas(1000),
                Materials.NitricAcid.getCells(2),
                Materials.Empty.getCells(1),
                240);

        // S + 2H = H2S
        GT_Values.RA.addChemicalRecipe(
                Materials.Sulfur.getDust(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(2000),
                Materials.HydricSulfide.getGas(1000),
                GT_Values.NI,
                60,
                8);

        // S + 2O = SO2
        GT_Values.RA.addChemicalRecipe(
                Materials.Sulfur.getDust(1),
                GT_Utility.getIntegratedCircuit(3),
                Materials.Oxygen.getGas(2000),
                Materials.SulfurDioxide.getGas(1000),
                GT_Values.NI,
                60,
                8);

        // H2S + 3O = SO2 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.Water.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(3),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydricSulfide.getGas(1000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Oxygen.getGas(3000),
                Materials.Water.getFluid(1000),
                Materials.SulfurDioxide.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(3),
                GT_Utility.getIntegratedCircuit(11),
                Materials.HydricSulfide.getGas(1000),
                Materials.Water.getFluid(1000),
                Materials.SulfurDioxide.getCells(1),
                Materials.Empty.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Oxygen.getGas(3000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(3),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydricSulfide.getGas(1000),
                Materials.SulfurDioxide.getGas(1000),
                Materials.Empty.getCells(3),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Oxygen.getGas(3000),
                GT_Values.NF,
                Materials.SulfurDioxide.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(3),
                GT_Utility.getIntegratedCircuit(12),
                Materials.HydricSulfide.getGas(1000),
                GT_Values.NF,
                Materials.SulfurDioxide.getCells(1),
                Materials.Empty.getCells(2),
                120);

        // 2H2S + SO2 = 3S + 2H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.HydricSulfide.getGas(2000),
                Materials.Water.getFluid(2000),
                Materials.Sulfur.getDust(3),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfurDioxide.getGas(1000),
                Materials.Water.getFluid(2000),
                Materials.Sulfur.getDust(3),
                Materials.Empty.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.HydricSulfide.getGas(2000),
                GT_Values.NF,
                Materials.Sulfur.getDust(3),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.HydricSulfide.getCells(2),
                GT_Utility.getIntegratedCircuit(2),
                Materials.SulfurDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Sulfur.getDust(3),
                Materials.Empty.getCells(2),
                120);

        // SO2 + O = SO3
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfurDioxide.getGas(1000),
                Materials.SulfurTrioxide.getGas(1000),
                Materials.Empty.getCells(1),
                200);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000),
                Materials.SulfurTrioxide.getGas(1000),
                Materials.Empty.getCells(1),
                200);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.SulfurDioxide.getGas(1000),
                GT_Values.NF,
                Materials.SulfurTrioxide.getCells(1),
                200);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurDioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.SulfurTrioxide.getCells(1),
                200);

        // SO3 + H2O = H2SO4
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfurTrioxide.getGas(1000),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                320,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurTrioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Empty.getCells(1),
                320,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.SulfurTrioxide.getGas(1000),
                GT_Values.NF,
                Materials.SulfuricAcid.getCells(1),
                320,
                8);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfurTrioxide.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Water.getFluid(1000),
                GT_Values.NF,
                Materials.SulfuricAcid.getCells(1),
                320,
                8);

        // C2H4 + 2Cl = C2H3Cl + HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethylene.getGas(1000),
                Materials.VinylChloride.getGas(1000),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                Materials.VinylChloride.getGas(1000),
                Materials.HydrochloricAcid.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Ethylene.getGas(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.VinylChloride.getCells(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorine.getGas(2000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.VinylChloride.getCells(1),
                160);

        // C2H4O2 =H2SO4= C2H2O + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Ethenone.getCells(1),
                160,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.AceticAcid.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(1000),
                Materials.Ethenone.getCells(1),
                160,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.AceticAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Ethenone.getGas(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                160,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.SulfuricAcid.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.AceticAcid.getFluid(1000),
                Materials.Ethenone.getGas(1000),
                Materials.DilutedSulfuricAcid.getCells(1),
                160,
                120);

        // C2H2O + 8HNO3 = 2CN4O8 + 9H2O
        // Chemically this recipe is wrong, but kept for minimizing breaking change.
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethenone.getCells(1),
                Materials.Empty.getCells(1),
                Materials.NitricAcid.getFluid(8000),
                Materials.Water.getFluid(9000),
                Materials.Tetranitromethane.getCells(2),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethenone.getCells(1),
                GT_Utility.getIntegratedCircuit(12),
                Materials.NitricAcid.getFluid(8000),
                Materials.Tetranitromethane.getFluid(2000),
                Materials.Empty.getCells(1),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricAcid.getCells(8),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethenone.getGas(1000),
                Materials.Water.getFluid(9000),
                Materials.Tetranitromethane.getCells(2),
                Materials.Empty.getCells(6),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricAcid.getCells(8),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Ethenone.getGas(1000),
                GT_Values.NF,
                Materials.Tetranitromethane.getCells(2),
                Materials.Empty.getCells(6),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricAcid.getCells(8),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Ethenone.getGas(1000),
                Materials.Tetranitromethane.getFluid(2000),
                Materials.Empty.getCells(8),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitricAcid.getCells(8),
                Materials.Empty.getCells(1),
                Materials.Ethenone.getGas(1000),
                Materials.Tetranitromethane.getFluid(2000),
                Materials.Water.getCells(9),
                480,
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethenone.getCells(1),
                Materials.NitricAcid.getCells(8),
                GT_Values.NF,
                Materials.Tetranitromethane.getFluid(2000),
                Materials.Water.getCells(9),
                480,
                120);

        // C3H6 + C2H4 = C5H8 + 2H
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Ethylene.getGas(1000),
                Materials.Isoprene.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Propene.getGas(1000),
                Materials.Isoprene.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethylene.getGas(1000),
                Materials.Hydrogen.getGas(2000),
                Materials.Isoprene.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Propene.getGas(1000),
                Materials.Hydrogen.getGas(2000),
                Materials.Isoprene.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(5),
                Materials.Propene.getGas(2000),
                Materials.Isoprene.getFluid(1000),
                Materials.Methane.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(2),
                GT_Utility.getIntegratedCircuit(5),
                GT_Values.NF,
                Materials.Isoprene.getFluid(1000),
                Materials.Methane.getCells(1),
                Materials.Empty.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Empty.getCells(1),
                GT_Utility.getIntegratedCircuit(15),
                Materials.Propene.getGas(2000),
                Materials.Methane.getGas(1000),
                Materials.Isoprene.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Propene.getCells(2),
                GT_Utility.getIntegratedCircuit(15),
                GT_Values.NF,
                Materials.Methane.getGas(1000),
                Materials.Isoprene.getCells(1),
                Materials.Empty.getCells(1),
                120);

        GT_Values.RA.addChemicalRecipe(
                ItemList.Cell_Air.get(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Isoprene.getFluid(144),
                GT_Values.NF,
                Materials.RawRubber.getDust(1),
                Materials.Empty.getCells(1),
                160);
        GT_Values.RA.addChemicalRecipe(
                Materials.Oxygen.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Isoprene.getFluid(288),
                GT_Values.NF,
                Materials.RawRubber.getDust(3),
                Materials.Empty.getCells(2),
                320);
        GT_Values.RA.addChemicalRecipe(
                Materials.Isoprene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Air.getGas(14000),
                GT_Values.NF,
                Materials.RawRubber.getDust(7),
                Materials.Empty.getCells(1),
                1120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Isoprene.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(14000),
                GT_Values.NF,
                Materials.RawRubber.getDust(21),
                Materials.Empty.getCells(2),
                2240);

        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Ethylene.getGas(1000),
                Materials.Hydrogen.getGas(2000),
                Materials.Styrene.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Benzene.getFluid(1000),
                Materials.Hydrogen.getGas(2000),
                Materials.Styrene.getCells(1),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Ethylene.getGas(1000),
                Materials.Styrene.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                120);
        GT_Values.RA.addChemicalRecipe(
                Materials.Ethylene.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Benzene.getFluid(1000),
                Materials.Styrene.getFluid(1000),
                Materials.Hydrogen.getCells(2),
                120);

        GT_Values.RA.addChemicalRecipe(
                Materials.RawStyreneButadieneRubber.getDust(9),
                Materials.Sulfur.getDust(1),
                GT_Values.NF,
                Materials.StyreneButadieneRubber.getMolten(1296),
                GT_Values.NI,
                600);

        // C6H6 + 4Cl = C6H4Cl2 + 2HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Chlorine.getGas(4000),
                Materials.HydrochloricAcid.getFluid(2000),
                Materials.Dichlorobenzene.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(4),
                GT_Utility.getIntegratedCircuit(2),
                Materials.Benzene.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(2000),
                Materials.Dichlorobenzene.getCells(1),
                Materials.Empty.getCells(3),
                240);

        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(4),
                GT_Utility.getIntegratedCircuit(12),
                Materials.Benzene.getFluid(1000),
                Materials.Dichlorobenzene.getFluid(1000),
                Materials.HydrochloricAcid.getCells(2),
                Materials.Empty.getCells(2),
                240);

        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumSulfide.getDust(3),
                ItemList.Cell_Air.get(8),
                Materials.Dichlorobenzene.getFluid(1000),
                Materials.PolyphenyleneSulfide.getMolten(1000),
                Materials.Salt.getDust(2),
                Materials.Empty.getCells(8),
                240,
                360);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumSulfide.getDust(3),
                Materials.Oxygen.getCells(8),
                Materials.Dichlorobenzene.getFluid(1000),
                Materials.PolyphenyleneSulfide.getMolten(1500),
                Materials.Salt.getDust(2),
                Materials.Empty.getCells(8),
                240,
                360);

        // NaCl + H2SO4 = NaHSO4 + HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Salt.getDust(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.SodiumBisulfate.getDust(7),
                60);

        // NaOH + H2SO4 = NaHSO4 + H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(3),
                GT_Utility.getIntegratedCircuit(1),
                Materials.SulfuricAcid.getFluid(1000),
                Materials.Water.getFluid(1000),
                Materials.SodiumBisulfate.getDust(7),
                60);

        // Biodiesel recipes
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.Methanol.getCells(1),
                Materials.SeedOil.getFluid(6000),
                Materials.BioDiesel.getFluid(6000),
                Materials.Glycerol.getCells(1),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.SeedOil.getCells(6),
                Materials.Methanol.getFluid(1000),
                Materials.Glycerol.getFluid(1000),
                Materials.BioDiesel.getCells(6),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.Methanol.getCells(1),
                Materials.FishOil.getFluid(6000),
                Materials.BioDiesel.getFluid(6000),
                Materials.Glycerol.getCells(1),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.FishOil.getCells(6),
                Materials.Methanol.getFluid(1000),
                Materials.Glycerol.getFluid(1000),
                Materials.BioDiesel.getCells(6),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.Ethanol.getCells(1),
                Materials.SeedOil.getFluid(6000),
                Materials.BioDiesel.getFluid(6000),
                Materials.Glycerol.getCells(1),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.SeedOil.getCells(6),
                Materials.Ethanol.getFluid(1000),
                Materials.Glycerol.getFluid(1000),
                Materials.BioDiesel.getCells(6),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.Ethanol.getCells(1),
                Materials.FishOil.getFluid(6000),
                Materials.BioDiesel.getFluid(6000),
                Materials.Glycerol.getCells(1),
                600);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDustTiny(1),
                Materials.FishOil.getCells(6),
                Materials.Ethanol.getFluid(1000),
                Materials.Glycerol.getFluid(1000),
                Materials.BioDiesel.getCells(6),
                600);

        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.Methanol.getCells(9),
                Materials.SeedOil.getFluid(54000),
                Materials.BioDiesel.getFluid(54000),
                Materials.Glycerol.getCells(9),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.SeedOil.getCells(54),
                Materials.Methanol.getFluid(9000),
                Materials.Glycerol.getFluid(9000),
                Materials.BioDiesel.getCells(54),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.Methanol.getCells(9),
                Materials.FishOil.getFluid(54000),
                Materials.BioDiesel.getFluid(54000),
                Materials.Glycerol.getCells(9),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.FishOil.getCells(54),
                Materials.Methanol.getFluid(9000),
                Materials.Glycerol.getFluid(9000),
                Materials.BioDiesel.getCells(54),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.Ethanol.getCells(9),
                Materials.SeedOil.getFluid(54000),
                Materials.BioDiesel.getFluid(54000),
                Materials.Glycerol.getCells(9),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.SeedOil.getCells(54),
                Materials.Ethanol.getFluid(9000),
                Materials.Glycerol.getFluid(9000),
                Materials.BioDiesel.getCells(54),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.Ethanol.getCells(9),
                Materials.FishOil.getFluid(54000),
                Materials.BioDiesel.getFluid(54000),
                Materials.Glycerol.getCells(9),
                5400);
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(1),
                Materials.FishOil.getCells(54),
                Materials.Ethanol.getFluid(9000),
                Materials.Glycerol.getFluid(9000),
                Materials.BioDiesel.getCells(54),
                5400);

        // C3H8O3 + 3HNO3 =H2SO4= C3H5N3O9 + 3H2O
        GT_Values.RA.addChemicalRecipe(
                Materials.Glycerol.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.NitrationMixture.getFluid(6000),
                Materials.DilutedSulfuricAcid.getFluid(3000),
                Materials.Glyceryl.getCells(1),
                180);
        GT_Values.RA.addChemicalRecipe(
                Materials.NitrationMixture.getCells(6),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Glycerol.getFluid(1000),
                Materials.DilutedSulfuricAcid.getFluid(3000),
                Materials.Glyceryl.getCells(1),
                Materials.Empty.getCells(5),
                180);

        GT_Values.RA.addChemicalRecipe(
                Materials.NitrationMixture.getCells(6),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Glycerol.getFluid(1000),
                Materials.Glyceryl.getFluid(1000),
                Materials.DilutedSulfuricAcid.getCells(3),
                Materials.Empty.getCells(3),
                180);

        // CaO + CO2 = CaCO3
        GT_Values.RA.addChemicalRecipe(
                Materials.Quicklime.getDust(2),
                GT_Values.NI,
                Materials.CarbonDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Calcite.getDust(5),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Calcite.getDust(5),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Quicklime.getDust(2),
                240);
        // MgO + CO2 = MgCO3
        GT_Values.RA.addChemicalRecipe(
                Materials.Magnesia.getDust(2),
                GT_Values.NI,
                Materials.CarbonDioxide.getGas(1000),
                GT_Values.NF,
                Materials.Magnesite.getDust(5),
                80);
        GT_Values.RA.addChemicalRecipe(
                Materials.Magnesite.getDust(5),
                GT_Utility.getIntegratedCircuit(1),
                GT_Values.NF,
                Materials.CarbonDioxide.getGas(1000),
                Materials.Magnesia.getDust(2),
                240);

        // C6H6 + 2Cl = C6H5Cl + HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Benzene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorine.getGas(2000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chlorobenzene.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Benzene.getFluid(1000),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.Chlorobenzene.getCells(1),
                Materials.Empty.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorine.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Benzene.getFluid(1000),
                Materials.Chlorobenzene.getFluid(1000),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Empty.getCells(1),
                240);

        // C6H5Cl + H2O = C6H6O + HCl
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorobenzene.getFluid(1000),
                Materials.Phenol.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorobenzene.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Water.getFluid(1000),
                Materials.Phenol.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Water.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Chlorobenzene.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.Phenol.getCells(1),
                240);
        GT_Values.RA.addChemicalRecipe(
                Materials.Chlorobenzene.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Water.getFluid(1000),
                Materials.DilutedHydrochloricAcid.getFluid(1000),
                Materials.Phenol.getCells(1),
                240);

        // C6H5Cl + NaOH = C6H6O + NaCl
        GT_Values.RA.addChemicalRecipe(
                Materials.SodiumHydroxide.getDust(12),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Chlorobenzene.getFluid(4000),
                Materials.Phenol.getFluid(4000),
                Materials.Salt.getDust(8),
                960);

        GT_Values.RA.addChemicalRecipe(
                Materials.GasolineRaw.getCells(10),
                Materials.Toluene.getCells(1),
                GT_Values.NF,
                GT_Values.NF,
                Materials.GasolineRegular.getCells(11),
                10,
                480);

        // Oxide Recipe
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                Materials.Antimony.getDust(2),
                Materials.Oxygen.getGas(3000),
                GT_Values.NF,
                Materials.AntimonyTrioxide.getDust(5),
                20,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                Materials.Lead.getDust(1),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.Massicot.getDust(2),
                20,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                Materials.Arsenic.getDust(2),
                Materials.Oxygen.getGas(3000),
                GT_Values.NF,
                Materials.ArsenicTrioxide.getDust(5),
                20,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                Materials.Cobalt.getDust(1),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.CobaltOxide.getDust(2),
                20,
                30);
        GT_Values.RA.addChemicalRecipe(
                GT_Utility.getIntegratedCircuit(2),
                Materials.Zinc.getDust(1),
                Materials.Oxygen.getGas(1000),
                GT_Values.NF,
                Materials.Zincite.getDust(2),
                20,
                30);

        if (GTNHLanthanides.isModLoaded() && GTPlusPlus.isModLoaded()) {

            // C4H8O + 2H =Pd= C4H10O
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.Butyraldehyde, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dustTiny, Materials.Palladium, 1L),
                    Materials.Hydrogen.getGas(2000),
                    new FluidStack(FluidRegistry.getFluid("butanol"), 1000),
                    ItemList.Cell_Empty.get(1L),
                    200,
                    30);

            // 4CH2O + C2H4O =NaOH= C5H12O4 + CO
            GT_Values.RA.addChemicalRecipe(
                    getModItem(MOD_ID_GTPP, "Formaldehyde", 4L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1L),
                    MaterialsKevlar.Acetaldehyde.getGas(1000),
                    Materials.CarbonMonoxide.getGas(1000),
                    GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.Pentaerythritol, 21L),
                    Materials.Empty.getCells(4),
                    600,
                    480);

            // 4CH2O + C2H4O =NaOH= C5H12O4 + CO
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.Acetaldehyde, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 1L),
                    new FluidStack(FluidRegistry.getFluid("formaldehyde"), 4000),
                    Materials.CarbonMonoxide.getGas(1000),
                    GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.Pentaerythritol, 21L),
                    Materials.Empty.getCells(1),
                    600,
                    480);
            // CaC2 + 2H2O = Ca(OH)2 + C2H2
            GT_Values.RA.addChemicalRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CalciumCarbide, 3L),
                    GT_Utility.getIntegratedCircuit(1),
                    Materials.Water.getFluid(2000L),
                    MaterialsKevlar.Acetylene.getGas(1000L),
                    getModItem(MOD_ID_GTPP, "itemDustCalciumHydroxide", 5L),
                    300,
                    480);

            // Co(NO3)2 + 2NaOH = Co(OH)2 + 2NaNO3
            GT_Values.RA.addChemicalRecipe(
                    MaterialsKevlar.CobaltIINitrate.getDust(9),
                    GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SodiumHydroxide, 6L),
                    GT_Values.NF,
                    GT_Values.NF,
                    MaterialsKevlar.CobaltIIHydroxide.getDust(5),
                    getModItem(MOD_ID_GTPP, "itemDustSodiumNitrate", 10L),
                    100,
                    120);
        }
    }

    public void polymerizationRecipes() {
        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.VinylAcetate.mFluid,
                Materials.VinylAcetate.getCells(1),
                Materials.PolyvinylAcetate.mFluid);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Ethylene.mGas,
                Materials.Ethylene.getCells(1),
                Materials.Plastic.mStandardMoltenFluid);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Tetrafluoroethylene.mGas,
                Materials.Tetrafluoroethylene.getCells(1),
                Materials.Polytetrafluoroethylene.mStandardMoltenFluid);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.VinylChloride.mGas,
                Materials.VinylChloride.getCells(1),
                Materials.PolyvinylChloride.mStandardMoltenFluid);

        GT_Values.RA.addDefaultPolymerizationRecipes(
                Materials.Styrene.mFluid,
                Materials.Styrene.getCells(1),
                Materials.Polystyrene.mStandardMoltenFluid);
    }

    public void singleBlockOnly() {
        // S + 2Cl = SCl2
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 8L),
                GT_OreDictUnificator.get(OrePrefixes.cell, Materials.Chlorine, 16L),
                GT_Values.NF,
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.SulfurDichloride, 8L),
                ItemList.Cell_Empty.get(8),
                800,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sulfur, 8L),
                ItemList.Cell_Empty.get(8L),
                Materials.Chlorine.getGas(16000),
                GT_Values.NF,
                GT_OreDictUnificator.get(OrePrefixes.cell, MaterialsKevlar.SulfurDichloride, 8L),
                GT_Values.NI,
                800,
                30);

        // C6H6 + C3H6 = C9H12
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Propene.getCells(8),
                Materials.PhosphoricAcid.getCells(1),
                Materials.Benzene.getFluid(8000),
                Materials.Cumene.getFluid(8000),
                Materials.Empty.getCells(9),
                GT_Values.NI,
                1920,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.PhosphoricAcid.getCells(1),
                Materials.Benzene.getCells(8),
                Materials.Propene.getGas(8000),
                Materials.Cumene.getFluid(8000),
                Materials.Empty.getCells(9),
                GT_Values.NI,
                1920,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Benzene.getCells(1),
                Materials.Propene.getCells(1),
                Materials.PhosphoricAcid.getFluid(125),
                Materials.Cumene.getFluid(1000),
                Materials.Empty.getCells(2),
                GT_Values.NI,
                240,
                30);

        // C3H6O + 2C6H6O =HCl= C15H16O2 + H2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Acetone.getCells(1),
                Materials.Phenol.getCells(2),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.BisphenolA.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(2),
                160,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.HydrochloricAcid.getCells(1),
                Materials.Acetone.getCells(1),
                Materials.Phenol.getFluid(2000),
                Materials.BisphenolA.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                160,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Phenol.getCells(2),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Acetone.getFluid(1000),
                Materials.BisphenolA.getFluid(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(2),
                160,
                30);

        // N + 3H = NH3
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Nitrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Hydrogen.getGas(3000),
                Materials.Ammonia.getGas(1000),
                Materials.Empty.getCells(1),
                GT_Values.NI,
                320,
                384);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Hydrogen.getCells(3),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Nitrogen.getGas(1000),
                Materials.Ammonia.getGas(1000),
                Materials.Empty.getCells(3),
                GT_Values.NI,
                320,
                384);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Nitrogen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Hydrogen.getGas(3000),
                GT_Values.NF,
                Materials.Ammonia.getCells(1),
                GT_Values.NI,
                320,
                384);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Hydrogen.getCells(3),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Nitrogen.getGas(1000),
                GT_Values.NF,
                Materials.Ammonia.getCells(1),
                Materials.Empty.getCells(2),
                320,
                384);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Ammonia.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Methanol.getFluid(2000),
                Materials.Dimethylamine.getGas(1000),
                Materials.Water.getCells(2),
                GT_Values.NI,
                240,
                120);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Ammonia.getCells(4),
                Materials.Empty.getCells(2),
                Materials.Oxygen.getGas(10000),
                Materials.NitricOxide.getGas(4000),
                Materials.Water.getCells(6),
                GT_Values.NI,
                320,
                30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                Materials.NitrogenDioxide.getGas(3000),
                Materials.NitricOxide.getGas(1000),
                Materials.NitricAcid.getCells(2),
                GT_Values.NI,
                240,
                30);

        // 2NO2 + O + H2O = 2HNO3
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.NitrogenDioxide.getCells(2),
                Materials.Oxygen.getCells(1),
                Materials.Water.getFluid(1000),
                Materials.NitricAcid.getFluid(2000),
                Materials.Empty.getCells(3),
                GT_Values.NI,
                240,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Oxygen.getCells(1),
                Materials.Water.getCells(1),
                Materials.NitrogenDioxide.getGas(2000),
                Materials.NitricAcid.getFluid(2000),
                Materials.Empty.getCells(2),
                GT_Values.NI,
                240,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Water.getCells(1),
                Materials.NitrogenDioxide.getCells(2),
                Materials.Oxygen.getGas(1000),
                Materials.NitricAcid.getFluid(2000),
                Materials.Empty.getCells(3),
                GT_Values.NI,
                240,
                30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Sulfur.getDust(1),
                Materials.Empty.getCells(1),
                Materials.Hydrogen.getGas(2000),
                GT_Values.NF,
                Materials.HydricSulfide.getCells(1),
                GT_Values.NI,
                60,
                8);

        // C2H4 + HCl + O = C2H3Cl + H2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Ethylene.getCells(1),
                Materials.HydrochloricAcid.getCells(1),
                Materials.Oxygen.getGas(1000),
                Materials.VinylChloride.getGas(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                160,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.HydrochloricAcid.getCells(1),
                Materials.Oxygen.getCells(1),
                Materials.Ethylene.getGas(1000),
                Materials.VinylChloride.getGas(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                160,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Oxygen.getCells(1),
                Materials.Ethylene.getCells(1),
                Materials.HydrochloricAcid.getFluid(1000),
                Materials.VinylChloride.getGas(1000),
                Materials.Water.getCells(1),
                Materials.Empty.getCells(1),
                160,
                30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Butadiene.getCells(1),
                ItemList.Cell_Air.get(5),
                Materials.Styrene.getFluid(350),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(9),
                Materials.Empty.getCells(6),
                160,
                240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Butadiene.getCells(1),
                Materials.Oxygen.getCells(5),
                Materials.Styrene.getFluid(350),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(13),
                Materials.Empty.getCells(6),
                160,
                240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Styrene.getCells(1),
                ItemList.Cell_Air.get(15),
                Materials.Butadiene.getGas(3000),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(27),
                Materials.Empty.getCells(16),
                480,
                240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Styrene.getCells(1),
                Materials.Oxygen.getCells(15),
                Materials.Butadiene.getGas(3000),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(41),
                Materials.Empty.getCells(16),
                480,
                240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Styrene.getCells(1),
                Materials.Butadiene.getCells(3),
                Materials.Air.getGas(15000),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(27),
                Materials.Empty.getCells(4),
                480,
                240);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Styrene.getCells(1),
                Materials.Butadiene.getCells(3),
                Materials.Oxygen.getGas(15000),
                GT_Values.NF,
                Materials.RawStyreneButadieneRubber.getDust(41),
                Materials.Empty.getCells(4),
                480,
                240);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Benzene.getCells(1),
                Materials.Empty.getCells(1),
                Materials.Chlorine.getGas(4000),
                Materials.Dichlorobenzene.getFluid(1000),
                Materials.HydrochloricAcid.getCells(2),
                GT_Values.NI,
                240,
                30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Glycerol.getCells(1),
                Materials.Empty.getCells(2),
                Materials.NitrationMixture.getFluid(6000),
                Materials.Glyceryl.getFluid(1000),
                Materials.DilutedSulfuricAcid.getCells(3),
                GT_Values.NI,
                180,
                30);

        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.SodiumHydroxide.getDust(12),
                Materials.Empty.getCells(4),
                Materials.Chlorobenzene.getFluid(4000),
                GT_Values.NF,
                Materials.Salt.getDust(8),
                Materials.Phenol.getCells(4),
                960,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.SodiumHydroxide.getDust(12),
                Materials.Chlorobenzene.getCells(4),
                GT_Values.NF,
                GT_Values.NF,
                Materials.Salt.getDust(8),
                Materials.Phenol.getCells(4),
                960,
                30);

        // Recipes for gasoline
        // 2N + O = N2O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Nitrogen.getCells(2),
                Materials.Oxygen.getCells(1),
                GT_Values.NF,
                GT_Values.NF,
                Materials.NitrousOxide.getCells(1),
                Materials.Empty.getCells(2),
                200,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Nitrogen.getCells(2),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Oxygen.getGas(1000L),
                Materials.NitrousOxide.getGas(1000L),
                Materials.Empty.getCells(2),
                GT_Values.NI,
                200,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Nitrogen.getCells(2),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Oxygen.getGas(1000L),
                GT_Values.NF,
                Materials.NitrousOxide.getCells(1),
                Materials.Empty.getCells(1),
                200,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(1),
                Materials.Nitrogen.getGas(2000L),
                Materials.NitrousOxide.getGas(1000L),
                Materials.Empty.getCells(1),
                GT_Values.NI,
                200,
                30);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Oxygen.getCells(1),
                GT_Utility.getIntegratedCircuit(11),
                Materials.Nitrogen.getGas(2000L),
                GT_Values.NF,
                Materials.NitrousOxide.getCells(1),
                GT_Values.NI,
                200,
                30);

        // C2H6O + C4H8 = C6H14O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Ethanol.getCells(1),
                Materials.Butene.getCells(1),
                GT_Values.NF,
                GT_Values.NF,
                Materials.AntiKnock.getCells(1),
                Materials.Empty.getCells(1),
                400,
                480);

        // Potassium Dichromate
        // 2KNO3 + 2CrO3 = K2Cr2O7 + 2NO + 3O
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.Saltpeter.getDust(10),
                Materials.ChromiumTrioxide.getDust(8),
                GT_Values.NF,
                Materials.NitricOxide.getGas(2000),
                Materials.Potassiumdichromate.getDust(11),
                GT_Values.NI,
                100,
                480);
        GT_Values.RA.addChemicalRecipeForBasicMachineOnly(
                Materials.PotassiumNitrade.getDust(10),
                Materials.ChromiumTrioxide.getDust(8),
                GT_Values.NF,
                Materials.NitricOxide.getGas(2000),
                Materials.Potassiumdichromate.getDust(11),
                GT_Values.NI,
                100,
                480);
    }

    public void multiblockOnly() {
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.PotassiumNitrade.getDust(10), Materials.ChromiumTrioxide.getDust(8) },
                null,
                new FluidStack[] { Materials.NitricOxide.getGas(2000), Materials.Oxygen.getGas(3000) },
                new ItemStack[] { Materials.Potassiumdichromate.getDust(11) },
                100,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Saltpeter.getDust(10), Materials.ChromiumTrioxide.getDust(8) },
                null,
                new FluidStack[] { Materials.NitricOxide.getGas(2000), Materials.Oxygen.getGas(3000) },
                new ItemStack[] { Materials.Potassiumdichromate.getDust(11) },
                100,
                480);

        // Potassium Dichromate shortcut
        // 2 Cr + 6O + 10 Saltpeter/Potassium Dichromate = 10 K2Cr2O7 + 2NO + 3O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.PotassiumNitrade.getDust(64), Materials.PotassiumNitrade.getDust(64),
                        Materials.PotassiumNitrade.getDust(32), Materials.Chrome.getDust(2 * 16),
                        GT_Utility.getIntegratedCircuit(11) },
                new FluidStack[] { Materials.Oxygen.getGas(6000 * 16) },
                new FluidStack[] { Materials.NitricOxide.getGas(2000 * 16), Materials.Oxygen.getGas(3000 * 16) },
                new ItemStack[] { Materials.Potassiumdichromate.getDust(64), Materials.Potassiumdichromate.getDust(64),
                        Materials.Potassiumdichromate.getDust(48) },
                2560,
                (int) GT_Values.VP[7]);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Saltpeter.getDust(64), Materials.Saltpeter.getDust(64),
                        Materials.Saltpeter.getDust(32), Materials.Chrome.getDust(2 * 16),
                        GT_Utility.getIntegratedCircuit(11) },
                new FluidStack[] { Materials.Oxygen.getGas(6000 * 16) },
                new FluidStack[] { Materials.NitricOxide.getGas(2000 * 16), Materials.Oxygen.getGas(3000 * 16) },
                new ItemStack[] { Materials.Potassiumdichromate.getDust(64), Materials.Potassiumdichromate.getDust(64),
                        Materials.Potassiumdichromate.getDust(48) },
                2560,
                (int) GT_Values.VP[7]);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { Materials.Benzene.getFluid(1000L), Materials.Methane.getGas(2000L) },
                new FluidStack[] { Materials.Dimethylbenzene.getFluid(1000L), Materials.Hydrogen.getGas(4000L) },
                null,
                4000,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 3),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 1) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(4000) },
                new FluidStack[] { new FluidStack(ItemList.sIndiumConcentrate, 8000) },
                null,
                60,
                150);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Galena, 27),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Sphalerite, 9) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(36000) },
                new FluidStack[] { new FluidStack(ItemList.sIndiumConcentrate, 72000) },
                null,
                160,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Pentlandite, 9) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000L) },
                new FluidStack[] { new FluidStack(ItemList.sNickelSulfate, 18000) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PlatinumGroupSludge, 1) },
                25,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        GT_OreDictUnificator.get(OrePrefixes.crushedPurified, Materials.Chalcopyrite, 9) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000L) },
                new FluidStack[] { new FluidStack(ItemList.sBlueVitriol, 18000) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.PlatinumGroupSludge, 1) },
                25,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.ingot, Materials.Plutonium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Uranium, 1L),
                        GT_Utility.getIntegratedCircuit(8) },
                new FluidStack[] { Materials.Air.getGas(8000L) },
                new FluidStack[] { Materials.Radon.getGas(800L) },
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Plutonium, 64L) },
                1500,
                480);

        // 3SiO2 + 4Al = 3Si + 2Al2O3
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 9),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminium, 4) },
                null,
                null,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 10) },
                10,
                120);

        // 10Si + 30HCl -> 0.3 SiH2Cl2 + 9 HSiCl3 + 0.3 SiCl4 + 0.2 Si2Cl6 + 20.4H
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silicon, 10) },
                new FluidStack[] { Materials.HydrochloricAcid.getFluid(30000) },
                new FluidStack[] { Materials.Trichlorosilane.getFluid(9000),
                        Materials.SiliconTetrachloride.getFluid(300), Materials.Hexachlorodisilane.getFluid(200),
                        Materials.Dichlorosilane.getGas(300), Materials.Hydrogen.getGas(20400) },
                null,
                150,
                480);

        // 2CO + 2C3H6 + 4H =RhHCO(P(C6H5)3)3= C4H8O + C4H8O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(4),
                        MaterialsKevlar.OrganorhodiumCatalyst.getDustTiny(1) },
                new FluidStack[] { Materials.Hydrogen.getGas(4000), Materials.Propene.getGas(2000),
                        Materials.CarbonMonoxide.getGas(2000) },
                new FluidStack[] { MaterialsKevlar.Butyraldehyde.getFluid(1000),
                        MaterialsKevlar.Isobutyraldehyde.getFluid(1000) },
                null,
                300,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        MaterialsKevlar.OrganorhodiumCatalyst.getDust(1) },
                new FluidStack[] { Materials.Hydrogen.getGas(36000), Materials.Propene.getGas(18000),
                        Materials.CarbonMonoxide.getGas(18000) },
                new FluidStack[] { MaterialsKevlar.Butyraldehyde.getFluid(9000),
                        MaterialsKevlar.Isobutyraldehyde.getFluid(9000) },
                null,
                2000,
                30);

        // C2H4 + O =Al2O3,Ag= C2H4O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1L) },
                new FluidStack[] { Materials.Ethylene.getGas(1000), Materials.Oxygen.getGas(1000) },
                new FluidStack[] { MaterialsKevlar.EthyleneOxide.getGas(1000) },
                null,
                50,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Silver, 9L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L) },
                new FluidStack[] { Materials.Ethylene.getGas(9000), Materials.Oxygen.getGas(9000) },
                new FluidStack[] { MaterialsKevlar.EthyleneOxide.getGas(9000) },
                null,
                400,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { MaterialsKevlar.EthyleneOxide.getGas(1000),
                        Materials.Dimethyldichlorosilane.getFluid(4000), Materials.Water.getFluid(5000) },
                new FluidStack[] { MaterialsKevlar.SiliconOil.getFluid(5000) },
                null,
                15,
                1920);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8) },
                new FluidStack[] { MaterialsKevlar.EthyleneOxide.getGas(9000),
                        Materials.Dimethyldichlorosilane.getFluid(36000), Materials.Water.getFluid(45000) },
                new FluidStack[] { MaterialsKevlar.SiliconOil.getFluid(45000) },
                null,
                100,
                1920);

        // NH3 + CH4O =SiO2,Al2O3= CH5N + H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(10),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L) },
                new FluidStack[] { Materials.Methanol.getFluid(1000), Materials.Ammonia.getGas(1000) },
                new FluidStack[] { MaterialsKevlar.Methylamine.getGas(1000L), Materials.Water.getFluid(1000L) },
                null,
                1500,
                500000);

        GT_Values.RA
                .addMultiblockChemicalRecipe(
                        new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.KevlarCatalyst, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.Pentaerythritol, 1L),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.dust, MaterialsKevlar.DiphenylmethaneDiisocyanate, 5L) },
                        new FluidStack[] { MaterialsKevlar.Ethyleneglycol.getFluid(4000),
                                MaterialsKevlar.SiliconOil.getFluid(1000) },
                        new FluidStack[] { MaterialsKevlar.PolyurethaneResin.getFluid(1000L) },
                        null,
                        200,
                        500000);
        GT_Values.RA
                .addMultiblockChemicalRecipe(
                        new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.KevlarCatalyst, 9L),
                                GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.Pentaerythritol, 9L),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.dust, MaterialsKevlar.DiphenylmethaneDiisocyanate, 45L) },
                        new FluidStack[] { MaterialsKevlar.Ethyleneglycol.getFluid(36000),
                                MaterialsKevlar.SiliconOil.getFluid(9000) },
                        new FluidStack[] { MaterialsKevlar.PolyurethaneResin.getFluid(9000L) },
                        null,
                        1500,
                        500000);

        // 3NH3 + 6CH4O =Al2O3,SiO2= CH5N + C2H7N + C3H9N + 6H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L) },
                new FluidStack[] { Materials.Methanol.getFluid(6000), Materials.Ammonia.getGas(3000) },
                new FluidStack[] { MaterialsKevlar.Methylamine.getGas(1000L), Materials.Dimethylamine.getGas(1000L),
                        MaterialsKevlar.Trimethylamine.getGas(1000L), Materials.Water.getFluid(6000L) },
                null,
                400,
                1920);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(11),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Aluminiumoxide, 9L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 9L) },
                new FluidStack[] { Materials.Methanol.getFluid(54000), Materials.Ammonia.getGas(27000) },
                new FluidStack[] { MaterialsKevlar.Methylamine.getGas(9000L), Materials.Dimethylamine.getGas(9000L),
                        MaterialsKevlar.Trimethylamine.getGas(9000L), Materials.Water.getFluid(54000L) },
                null,
                3000,
                1920);

        // 18SOCl2 + 5C10H10O4 + 6CO2 = 7C8H4Cl2O2 + 22HCl + 18SO2
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { MaterialsKevlar.ThionylChloride.getFluid(18000),
                        MaterialsKevlar.DimethylTerephthalate.getFluid(5000L), Materials.CarbonDioxide.getGas(6000L) },
                new FluidStack[] { Materials.DilutedHydrochloricAcid.getFluid(22000L),
                        Materials.SulfurDioxide.getGas(18000L) },
                new ItemStack[] {
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.TerephthaloylChloride, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.TerephthaloylChloride, 48L) },
                400,
                1920);

        // 2CH4O + C8H6O4 =H2SO4= C10H10O4 + 2H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { MaterialsKevlar.TerephthalicAcid.getFluid(1000L), Materials.Methanol.getFluid(2000),
                        Materials.SulfuricAcid.getFluid(2000) },
                new FluidStack[] { MaterialsKevlar.DimethylTerephthalate.getFluid(1000L),
                        Materials.DilutedSulfuricAcid.getFluid(2000) },
                null,
                250,
                1920);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { MaterialsKevlar.TerephthalicAcid.getFluid(9000L), Materials.Methanol.getFluid(18000),
                        Materials.SulfuricAcid.getFluid(18000) },
                new FluidStack[] { MaterialsKevlar.DimethylTerephthalate.getFluid(9000L),
                        Materials.DilutedSulfuricAcid.getFluid(18000) },
                null,
                1750,
                1920);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.Benzene.getFluid(1000), Materials.Methane.getGas(2000) },
                new FluidStack[] { MaterialsKevlar.IIIDimethylbenzene.getFluid(1000L),
                        Materials.Hydrogen.getGas(4000) },
                null,
                4000,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3) },
                new FluidStack[] { Materials.Benzene.getFluid(1000), Materials.Methane.getGas(2000) },
                new FluidStack[] { MaterialsKevlar.IVDimethylbenzene.getFluid(1000L), Materials.Hydrogen.getGas(4000) },
                null,
                4000,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CobaltIIHydroxide, 45L) },
                new FluidStack[] { MaterialsKevlar.NaphthenicAcid.getFluid(10000L) },
                new FluidStack[] { GT_Values.NF, },
                new ItemStack[] { MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(49), },
                200,
                1920);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CobaltIIAcetate, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CobaltIIAcetate, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.CobaltIIAcetate, 7L) },
                new FluidStack[] { MaterialsKevlar.NaphthenicAcid.getFluid(10000L) },
                new FluidStack[] { Materials.AceticAcid.getFluid(15000L), },
                new ItemStack[] { MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(64),
                        MaterialsKevlar.CobaltIINaphthenate.getDust(49), },
                200,
                1920);

        // PCl3 + 3C6H5Cl + 6Na = 6NaCl + C18H15P
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Sodium, 6L) },
                new FluidStack[] { MaterialsKevlar.PhosphorusTrichloride.getFluid(1000L),
                        Materials.Chlorobenzene.getFluid(3000) },
                null,
                new ItemStack[] { MaterialsKevlar.Triphenylphosphene.getDust(34), Materials.Salt.getDust(12) },
                400,
                1920);

        // 4NaH + C3H9BO3 = NaBH4 + 3CH3ONa
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1), MaterialsKevlar.SodiumHydride.getDust(8) },
                new FluidStack[] { MaterialsKevlar.TrimethylBorate.getFluid(1000L) },
                null,
                new ItemStack[] { MaterialsKevlar.SodiumBorohydride.getDust(6),
                        MaterialsKevlar.SodiumMethoxide.getDust(18) },
                600,
                1920);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9), MaterialsKevlar.SodiumHydride.getDust(64) },
                new FluidStack[] { MaterialsKevlar.TrimethylBorate.getFluid(8000L) },
                null,
                new ItemStack[] { MaterialsKevlar.SodiumBorohydride.getDust(48),
                        MaterialsKevlar.SodiumMethoxide.getDust(64), MaterialsKevlar.SodiumMethoxide.getDust(64),
                        MaterialsKevlar.SodiumMethoxide.getDust(16) },
                600,
                1920);

        // 2CH3COOH = CH3COCH3 + CO2 + H
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.copyAmount(0, Materials.Calcite.getDust(5)),
                        GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.AceticAcid.getFluid(2000) },
                new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000),
                        Materials.Water.getFluid(1000) },
                null,
                400,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.copyAmount(0, Materials.Calcium.getDust(1)),
                        GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.AceticAcid.getFluid(2000) },
                new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000),
                        Materials.Water.getFluid(1000) },
                null,
                400,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.copyAmount(0, Materials.Quicklime.getDust(2)),
                        GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.AceticAcid.getFluid(2000) },
                new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.CarbonDioxide.getGas(1000),
                        Materials.Water.getFluid(1000) },
                null,
                400,
                480);

        // C + 4H + O = CH3OH
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Carbon.getDust(1), GT_Utility.getIntegratedCircuit(23) },
                new FluidStack[] { Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(1000) },
                new FluidStack[] { Materials.Methanol.getFluid(1000) },
                null,
                320,
                96);

        // This recipe collides with one for Vinyl Chloride
        // 2C + 4H + 2O = CH3COOH
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Carbon.getDust(2), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Hydrogen.getGas(4000), Materials.Oxygen.getGas(2000) },
                new FluidStack[] { Materials.AceticAcid.getFluid(1000) },
                null,
                480,
                30);
        // 2CO + 4H = CH3COOH
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.CarbonMonoxide.getGas(2000), Materials.Hydrogen.getGas(4000) },
                new FluidStack[] { Materials.AceticAcid.getFluid(1000) },
                null,
                320,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8) },
                new FluidStack[] { Materials.Hydrogen.getGas(9000), Materials.Chlorine.getGas(9000) },
                new FluidStack[] { Materials.HydrochloricAcid.getFluid(9000) },
                null,
                7,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.Chlorine.getGas(10000), Materials.Water.getFluid(10000),
                        Materials.Mercury.getFluid(1000) },
                new FluidStack[] { Materials.HypochlorousAcid.getFluid(10000), Materials.Hydrogen.getGas(10000) },
                null,
                600,
                8);

        // H2O + 4Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + 2HCl
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.SodiumHydroxide.getDust(3), GT_Utility.getIntegratedCircuit(23) },
                new FluidStack[] { Materials.Propene.getGas(1000), Materials.Chlorine.getGas(4000),
                        Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000),
                        Materials.HydrochloricAcid.getFluid(2000) },
                null,
                640,
                30);
        // H2O + 2Cl + C3H6 + NaOH =Hg= C3H5ClO + NaCl·H2O + 2H
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.SodiumHydroxide.getDust(3), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Propene.getGas(1000), Materials.Chlorine.getGas(2000),
                        Materials.Water.getFluid(1000), Materials.Mercury.getFluid(100) },
                new FluidStack[] { Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000),
                        Materials.Hydrogen.getGas(2000) },
                null,
                640,
                30);
        // HClO + 2Cl + C3H6 + NaOH = C3H5ClO + NaCl·H2O + HCl
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.SodiumHydroxide.getDust(3), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Propene.getGas(1000), Materials.Chlorine.getGas(2000),
                        Materials.HypochlorousAcid.getFluid(1000) },
                new FluidStack[] { Materials.Epichlorohydrin.getFluid(1000), Materials.SaltWater.getFluid(1000),
                        Materials.HydrochloricAcid.getFluid(1000) },
                null,
                640,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Apatite.getDust(9) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(5000), Materials.Water.getFluid(10000) },
                new FluidStack[] { Materials.PhosphoricAcid.getFluid(3000), Materials.HydrochloricAcid.getFluid(1000) },
                new ItemStack[] { Materials.Gypsum.getDust(40) },
                320,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Phosphorus.getDust(4), GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { Materials.Oxygen.getGas(10000) },
                null,
                new ItemStack[] { Materials.PhosphorousPentoxide.getDust(14) },
                40,
                30);

        // 2P + 5O + 3H2O = 2H3PO4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Phosphorus.getDust(1), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Oxygen.getGas(2500), Materials.Water.getFluid(1500) },
                new FluidStack[] { Materials.PhosphoricAcid.getFluid(1000) },
                null,
                320,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { Materials.Propene.getGas(8000), Materials.Benzene.getFluid(8000),
                        Materials.PhosphoricAcid.getFluid(1000) },
                new FluidStack[] { Materials.Cumene.getFluid(8000) },
                null,
                1920,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Propene.getGas(1000), Materials.Benzene.getFluid(1000),
                        Materials.PhosphoricAcid.getFluid(100), Materials.Oxygen.getGas(2000) },
                new FluidStack[] { Materials.Phenol.getFluid(1000), Materials.Acetone.getFluid(1000) },
                null,
                480,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000),
                        Materials.HydrochloricAcid.getFluid(1000) },
                new FluidStack[] { Materials.BisphenolA.getFluid(1000), Materials.Water.getFluid(1000) },
                null,
                160,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.SodiumHydroxide.getDust(6), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Acetone.getFluid(1000), Materials.Phenol.getFluid(2000),
                        Materials.HydrochloricAcid.getFluid(1000), Materials.Epichlorohydrin.getFluid(2000) },
                new FluidStack[] { Materials.Epoxid.getMolten(1000), Materials.SaltWater.getFluid(2000) },
                null,
                480,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.Hydrogen.getGas(9000), Materials.Fluorine.getGas(9000) },
                new FluidStack[] { Materials.HydrofluoricAcid.getFluid(9000) },
                null,
                7,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.HydrofluoricAcid.getFluid(4000), Materials.Methane.getGas(2000),
                        Materials.Chlorine.getGas(12000) },
                new FluidStack[] { Materials.Tetrafluoroethylene.getGas(1000),
                        Materials.HydrochloricAcid.getFluid(12000) },
                null,
                540,
                240);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Methane.getGas(2000), Materials.Chlorine.getGas(4000),
                        Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.HydrochloricAcid.getFluid(2000),
                        Materials.DilutedHydrochloricAcid.getFluid(2000) },
                new ItemStack[] { Materials.Polydimethylsiloxane.getDust(3) },
                480,
                96);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.Silicon.getDust(1), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Methanol.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000) },
                new FluidStack[] { Materials.DilutedHydrochloricAcid.getFluid(2000) },
                new ItemStack[] { Materials.Polydimethylsiloxane.getDust(3) },
                480,
                96);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Nitrogen.getGas(10000), Materials.Hydrogen.getGas(30000) },
                new FluidStack[] { Materials.Ammonia.getGas(10000) },
                new ItemStack[] { null },
                800,
                480);

        // 2NH3 + 7O = N2O4 + 3H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(23) },
                new FluidStack[] { Materials.Ammonia.getGas(2000), Materials.Oxygen.getGas(7000) },
                new FluidStack[] { Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000) },
                null,
                480,
                30);
        // 7O + 6H + 2N = N2O4 + 3H2O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(23) },
                new FluidStack[] { Materials.Nitrogen.getGas(2000), Materials.Hydrogen.getGas(6000),
                        Materials.Oxygen.getGas(7000) },
                new FluidStack[] { Materials.DinitrogenTetroxide.getGas(1000), Materials.Water.getFluid(3000) },
                null,
                1100,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.Oxygen.getGas(100000), Materials.Ammonia.getGas(36000) },
                new FluidStack[] { Materials.NitricOxide.getGas(36000), Materials.Water.getFluid(54000) },
                null,
                170,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8) },
                new FluidStack[] { Materials.Oxygen.getGas(100000), Materials.Ammonia.getGas(36000) },
                new FluidStack[] { Materials.NitricOxide.getGas(36000) },
                null,
                170,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.NitricOxide.getGas(9000), Materials.Oxygen.getGas(9000) },
                new FluidStack[] { Materials.NitrogenDioxide.getGas(9000) },
                null,
                80,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.NitrogenDioxide.getGas(27000), Materials.Water.getFluid(9000) },
                new FluidStack[] { Materials.NitricAcid.getFluid(18000), Materials.NitricOxide.getGas(9000) },
                null,
                120,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Hydrogen.getGas(3000), Materials.Nitrogen.getGas(1000),
                        Materials.Oxygen.getGas(4000) },
                new FluidStack[] { Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000) },
                null,
                320,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Ammonia.getGas(1000), Materials.Oxygen.getGas(4000) },
                new FluidStack[] { Materials.NitricAcid.getFluid(1000), Materials.Water.getFluid(1000) },
                null,
                320,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.NitrogenDioxide.getGas(2000), Materials.Oxygen.getGas(1000),
                        Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.NitricAcid.getFluid(2000) },
                null,
                320,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9), Materials.Sulfur.getDust(9) },
                new FluidStack[] { Materials.Hydrogen.getGas(18000) },
                new FluidStack[] { Materials.HydricSulfide.getGas(9000) },
                null,
                4,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9), Materials.Sulfur.getDust(9) },
                new FluidStack[] { Materials.Oxygen.getGas(18000) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(9000) },
                null,
                4,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.HydricSulfide.getGas(9000), Materials.Oxygen.getGas(27000) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(9000), Materials.Water.getFluid(9000) },
                null,
                60,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(8) },
                new FluidStack[] { Materials.HydricSulfide.getGas(9000), Materials.Oxygen.getGas(27000) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(9000) },
                null,
                60,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(7) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(9000), Materials.HydricSulfide.getGas(18000) },
                null,
                new ItemStack[] { Materials.Sulfur.getDust(27) },
                60,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.SulfurTrioxide.getGas(9000), Materials.Water.getFluid(9000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000) },
                null,
                260,
                480);

        // S + O3 + H2O = H2SO4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24), Materials.Sulfur.getDust(1) },
                new FluidStack[] { Materials.Oxygen.getGas(3000), Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(1000) },
                null,
                480,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(7), Materials.Sulfur.getDust(9) },
                new FluidStack[] { Materials.Oxygen.getGas(27000), Materials.Water.getFluid(9000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000) },
                null,
                260,
                480);

        // H2S + O4 = H2SO4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.HydricSulfide.getGas(1000), Materials.Oxygen.getGas(4000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(1000) },
                null,
                480,
                30);

        // SO2 + O + H2O = H2SO4
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(1000), Materials.Oxygen.getGas(1000),
                        Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(1000) },
                null,
                600,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                new FluidStack[] { Materials.SulfurDioxide.getGas(9000), Materials.Oxygen.getGas(9000),
                        Materials.Water.getFluid(9000) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000) },
                null,
                150,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.HydrochloricAcid.getFluid(1000), Materials.Ethylene.getGas(1000),
                        Materials.Oxygen.getGas(1000) },
                new FluidStack[] { Materials.VinylChloride.getGas(1000), Materials.Water.getFluid(1000) },
                null,
                160,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Chlorine.getGas(2000), Materials.Ethylene.getGas(2000),
                        Materials.Oxygen.getGas(1000) },
                new FluidStack[] { Materials.VinylChloride.getGas(2000), Materials.Water.getFluid(1000) },
                null,
                240,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.Isoprene.getFluid(1728), Materials.Air.getGas(6000),
                        Materials.Titaniumtetrachloride.getFluid(80) },
                null,
                new ItemStack[] { Materials.RawRubber.getDust(18) },
                640,
                30);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                new FluidStack[] { Materials.Isoprene.getFluid(1728), Materials.Oxygen.getGas(6000),
                        Materials.Titaniumtetrachloride.getFluid(80) },
                null,
                new ItemStack[] { Materials.RawRubber.getDust(24) },
                640,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3) },
                new FluidStack[] { Materials.Styrene.getFluid(36), Materials.Butadiene.getGas(108),
                        Materials.Air.getGas(2000) },
                null,
                new ItemStack[] { Materials.RawStyreneButadieneRubber.getDust(1) },
                160,
                240);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(3) },
                new FluidStack[] { Materials.Styrene.getFluid(72), Materials.Butadiene.getGas(216),
                        Materials.Oxygen.getGas(2000) },
                null,
                new ItemStack[] { Materials.RawStyreneButadieneRubber.getDust(3) },
                160,
                240);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(4) },
                new FluidStack[] { Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620),
                        Materials.Titaniumtetrachloride.getFluid(100), Materials.Air.getGas(15000) },
                null,
                new ItemStack[] { Materials.RawStyreneButadieneRubber.getDust(22),
                        Materials.RawStyreneButadieneRubber.getDustSmall(2) },
                640,
                240);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(4) },
                new FluidStack[] { Materials.Styrene.getFluid(540), Materials.Butadiene.getGas(1620),
                        Materials.Titaniumtetrachloride.getFluid(100), Materials.Oxygen.getGas(7500) },
                null,
                new ItemStack[] { Materials.RawStyreneButadieneRubber.getDust(30) },
                640,
                240);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9), Materials.Salt.getDust(18) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000) },
                new FluidStack[] { Materials.HydrochloricAcid.getFluid(9000) },
                new ItemStack[] { Materials.SodiumBisulfate.getDust(63) },
                135,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(9), Materials.SodiumHydroxide.getDust(27) },
                new FluidStack[] { Materials.SulfuricAcid.getFluid(9000) },
                new FluidStack[] { Materials.Water.getFluid(9000) },
                new ItemStack[] { Materials.SodiumBisulfate.getDust(63) },
                135,
                120);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Benzene.getFluid(1000), Materials.Chlorine.getGas(2000),
                        Materials.Water.getFluid(1000) },
                new FluidStack[] { Materials.Phenol.getFluid(1000), Materials.HydrochloricAcid.getFluid(1000),
                        Materials.DilutedHydrochloricAcid.getFluid(1000) },
                null,
                560,
                30);
        // C6H6 + 2Cl + NaOH = C6H6O + NaCl + HCl
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { Materials.SodiumHydroxide.getDust(6), GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Benzene.getFluid(2000), Materials.Chlorine.getGas(4000) },
                new FluidStack[] { Materials.Phenol.getFluid(2000), Materials.HydrochloricAcid.getFluid(2000) },
                new ItemStack[] { Materials.Salt.getDust(4) },
                1120,
                30);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.LightFuel.getFluid(20000), Materials.HeavyFuel.getFluid(4000) },
                new FluidStack[] { Materials.Fuel.getFluid(24000) },
                null,
                100,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Fuel.getFluid(10000), Materials.Tetranitromethane.getFluid(200) },
                new FluidStack[] { Materials.NitroFuel.getFluid(10000) },
                null,
                120,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.BioDiesel.getFluid(10000), Materials.Tetranitromethane.getFluid(400) },
                new FluidStack[] { Materials.NitroFuel.getFluid(9000) },
                null,
                120,
                480);
        // CH4 + 2H2O = CO2 + 8H
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(11) },
                new FluidStack[] { Materials.Methane.getGas(5000), GT_ModHandler.getDistilledWater(10000) },
                new FluidStack[] { Materials.CarbonDioxide.getGas(5000), Materials.Hydrogen.getGas(40000) },
                null,
                200,
                480);
        // CH4 + H2O = CO + 6H
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(12) },
                new FluidStack[] { Materials.Methane.getGas(5000), GT_ModHandler.getDistilledWater(5000) },
                new FluidStack[] { Materials.CarbonMonoxide.getGas(5000), Materials.Hydrogen.getGas(30000) },
                null,
                200,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Nitrogen.getGas(20000), Materials.Oxygen.getGas(10000) },
                new FluidStack[] { Materials.NitrousOxide.getGas(10000) },
                new ItemStack[] { null },
                50,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Naphtha.getFluid(16000), Materials.Gas.getGas(2000),
                        Materials.Methanol.getFluid(1000), Materials.Acetone.getFluid(1000) },
                new FluidStack[] { Materials.GasolineRaw.getFluid(20000) },
                null,
                100,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.GasolineRaw.getFluid(10000), Materials.Toluene.getFluid(1000) },
                new FluidStack[] { Materials.GasolineRegular.getFluid(11000) },
                null,
                10,
                480);
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.GasolineRegular.getFluid(20000), Materials.Octane.getFluid(2000),
                        Materials.NitrousOxide.getGas(6000), Materials.Toluene.getFluid(1000),
                        Materials.AntiKnock.getFluid(3000L) },
                new FluidStack[] { Materials.GasolinePremium.getFluid(32000L) },
                null,
                50,
                1920);
        // C2H6O + C4H8 = C6H14O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Ethanol.getFluid(1000), Materials.Butene.getGas(1000) },
                new FluidStack[] { Materials.AntiKnock.getFluid(1000) },
                null,
                400,
                480);
        // CH4O + C4H8 = C5H12O
        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Methanol.getFluid(1000), Materials.Butene.getGas(1000) },
                new FluidStack[] { Materials.MTBEMixture.getGas(1000) },
                null,
                20,
                480);

        GT_Values.RA.addMultiblockChemicalRecipe(
                new ItemStack[] { GT_Utility.getIntegratedCircuit(24) },
                new FluidStack[] { Materials.Naquadria.getMolten(4608L), Materials.ElectrumFlux.getMolten(4608L),
                        Materials.Radon.getGas(16000L) },
                new FluidStack[] { Materials.EnrichedNaquadria.getFluid(9216L) },
                null,
                600,
                500000);

        if (GTNHLanthanides.isModLoaded() && GTPlusPlus.isModLoaded()) {
            // CH2O + 2C6H7N + HCl = C13H14N2(HCl) + H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("formaldehyde"), 1000),
                            new FluidStack(FluidRegistry.getFluid("aniline"), 2000),
                            Materials.HydrochloricAcid.getFluid(1000) },
                    new FluidStack[] { MaterialsKevlar.DiaminodiphenylmethanMixture.getFluid(1000L) },
                    null,
                    1200,
                    1920);
            // C6H5NO2 + 6H =Pd= C6H7N + 2H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 9000),
                            Materials.Hydrogen.getGas(54000) },
                    new FluidStack[] { Materials.Water.getFluid(18000),
                            new FluidStack(FluidRegistry.getFluid("aniline"), 9000) },
                    null,
                    900,
                    1920);
            // C6H6 + HNO3 =H2SO4= C6H5NO2 + H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                    new FluidStack[] { Materials.Benzene.getFluid(5000), Materials.SulfuricAcid.getFluid(3000),
                            Materials.NitricAcid.getFluid(5000), GT_ModHandler.getDistilledWater(10000) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("nitrobenzene"), 5000),
                            Materials.DilutedSulfuricAcid.getFluid(3000) },
                    null,
                    8,
                    122880);
            // C13H14N2(HCl) + 2COCl2 = C15H10N2O2(5HCl)
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                    new FluidStack[] { MaterialsKevlar.DiaminodiphenylmethanMixture.getFluid(1000L),
                            new FluidStack(FluidRegistry.getFluid("phosgene"), 2000) },
                    new FluidStack[] { MaterialsKevlar.DiphenylmethaneDiisocyanateMixture.getFluid(1000L) },
                    null,
                    600,
                    1920);

            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1L) },
                    new FluidStack[] { MaterialsKevlar.Butyraldehyde.getFluid(9000), Materials.Hydrogen.getGas(18000) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("butanol"), 9000) },
                    null,
                    80,
                    480);

            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("butanol"), 2000),
                            new FluidStack(FluidRegistry.getFluid("propionicacid"), 1000),
                            Materials.IronIIIChloride.getFluid(100) },
                    null,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.KevlarCatalyst, 1L) },
                    600,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Tin, 9L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("butanol"), 18000),
                            new FluidStack(FluidRegistry.getFluid("propionicacid"), 9000),
                            Materials.IronIIIChloride.getFluid(900) },
                    null,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.KevlarCatalyst, 9L) },
                    4500,
                    1920);
            // C2H4 + CO + H2O =C4NiO= C3H6O2
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                    new FluidStack[] { Materials.Ethylene.getGas(1000), Materials.CarbonMonoxide.getGas(1000),
                            MaterialsKevlar.NickelTetracarbonyl.getFluid(100), Materials.Water.getFluid(1000) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("propionicacid"), 1000) },
                    null,
                    200,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                    new FluidStack[] { Materials.Ethylene.getGas(9000), Materials.CarbonMonoxide.getGas(9000),
                            MaterialsKevlar.NickelTetracarbonyl.getFluid(900), Materials.Water.getFluid(9000) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("propionicacid"), 9000) },
                    null,
                    1500,
                    1920);
            // C6H7N + HNO3 =H2SO4,C4H6O3= C6H6N2O2 + H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(2) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("aniline"), 1000),
                            new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 100),
                            Materials.NitrationMixture.getFluid(2000) },
                    new FluidStack[] { MaterialsKevlar.IVNitroaniline.getFluid(1000L),
                            Materials.DilutedSulfuricAcid.getFluid(1000) },
                    null,
                    300,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("aniline"), 9000),
                            new FluidStack(FluidRegistry.getFluid("molten.aceticanhydride"), 900),
                            Materials.NitrationMixture.getFluid(18000) },
                    new FluidStack[] { MaterialsKevlar.IVNitroaniline.getFluid(9000L),
                            Materials.DilutedSulfuricAcid.getFluid(9000) },
                    null,
                    2000,
                    1920);
            // C6H6N2O2 + 6H =Pd,NO2= C6H8N2 + 2H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Palladium, 1L) },
                    new FluidStack[] { Materials.NitrogenDioxide.getGas(100), Materials.Hydrogen.getGas(6000),
                            MaterialsKevlar.IVNitroaniline.getFluid(1000L) },
                    new FluidStack[] { Materials.Water.getFluid(2000L) },
                    new ItemStack[] {
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.ParaPhenylenediamine, 16L) },
                    400,
                    500000);
            // C4H10O2 =Cu= C4H6O2 + 4H
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1000) },
                    new FluidStack[] { MaterialsKevlar.GammaButyrolactone.getFluid(1000),
                            Materials.Hydrogen.getGas(4000) },
                    null,
                    100,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Copper, 9L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 9000) },
                    new FluidStack[] { MaterialsKevlar.GammaButyrolactone.getFluid(9000),
                            Materials.Hydrogen.getGas(36000) },
                    null,
                    700,
                    1920);

            // 2CH2O + C2H2 =SiO2,CuO,Bi2O3= C4H6O2
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CupricOxide, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.BismuthIIIOxide, 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 1L) },
                    new FluidStack[] { MaterialsKevlar.Acetylene.getGas(1000L),
                            new FluidStack(FluidRegistry.getFluid("formaldehyde"), 2000) },
                    null,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.IIButinIIVdiol, 12L) },
                    400,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.CupricOxide, 9L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.BismuthIIIOxide, 9L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.SiliconDioxide, 9L) },
                    new FluidStack[] { MaterialsKevlar.Acetylene.getGas(9000L),
                            new FluidStack(FluidRegistry.getFluid("formaldehyde"), 18000) },
                    null,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.IIButinIIVdiol, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.IIButinIIVdiol, 44L) },
                    3000,
                    1920);
            // C4H6O2 + 4H =NiAl= C4H10O2
            GT_Values.RA
                    .addMultiblockChemicalRecipe(
                            new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                                    GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.IIButinIIVdiol, 12L),
                                    GT_OreDictUnificator
                                            .get(OrePrefixes.dust, MaterialsKevlar.RaneyNickelActivated, 1L) },
                            new FluidStack[] { Materials.Hydrogen.getGas(4000L) },
                            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("1,4-butanediol"), 1000) },
                            null,
                            300,
                            500000);

            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            getModItem(MOD_ID_GTPP, "itemDustCalciumChloride", 1L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.ParaPhenylenediamine, 9L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.TerephthaloylChloride, 9L) },
                    new FluidStack[] { MaterialsKevlar.NMethylIIPyrrolidone.getFluid(1000) },
                    new FluidStack[] { MaterialsKevlar.LiquidCrystalKevlar.getFluid(9000L),
                            Materials.DilutedHydrochloricAcid.getFluid(2000L) },
                    null,
                    600,
                    500000);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9),
                            getModItem(MOD_ID_GTPP, "itemDustCalciumChloride", 7L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.ParaPhenylenediamine, 63L),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.TerephthaloylChloride, 63L) },
                    new FluidStack[] { MaterialsKevlar.NMethylIIPyrrolidone.getFluid(7000) },
                    new FluidStack[] { MaterialsKevlar.LiquidCrystalKevlar.getFluid(63000L),
                            Materials.DilutedHydrochloricAcid.getFluid(14000L) },
                    null,
                    3500,
                    500000);

            // Na2B4O7(H2O)10 + 2HCl = 2NaCl + 4H3BO3 + 5H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Borax, 23L) },
                    new FluidStack[] { Materials.HydrochloricAcid.getFluid(2000L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("boricacid"), 4000),
                            Materials.Water.getFluid(5000L) },
                    new ItemStack[] { Materials.Salt.getDust(4) },
                    800,
                    480);
            // H3BO3 + 3CH4O =H2SO4= C3H9BO3 + 3H2O
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1) },
                    new FluidStack[] { Materials.Methanol.getFluid(3000L),
                            new FluidStack(FluidRegistry.getFluid("boricacid"), 1000),
                            Materials.SulfuricAcid.getFluid(6000L) },
                    new FluidStack[] { Materials.DilutedSulfuricAcid.getFluid(6000L),
                            MaterialsKevlar.TrimethylBorate.getFluid(1000) },
                    null,
                    600,
                    960);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9) },
                    new FluidStack[] { Materials.Methanol.getFluid(27000L),
                            new FluidStack(FluidRegistry.getFluid("boricacid"), 9000),
                            Materials.SulfuricAcid.getFluid(54000L) },
                    new FluidStack[] { Materials.DilutedSulfuricAcid.getFluid(54000L),
                            MaterialsKevlar.TrimethylBorate.getFluid(9000) },
                    null,
                    4500,
                    960);
            // RhCl3 + 3C18H15P + 3NaBH4 + CO = RhC55H46P3O + 3NaCl + 3B + 11H
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(1),
                            GT_OreDictUnificator.get(OrePrefixes.dust, MaterialsKevlar.RhodiumChloride, 4L),
                            MaterialsKevlar.Triphenylphosphene.getDust(64),
                            MaterialsKevlar.Triphenylphosphene.getDust(38),
                            MaterialsKevlar.SodiumBorohydride.getDust(18) },
                    new FluidStack[] { Materials.CarbonMonoxide.getGas(1000L) },
                    new FluidStack[] { Materials.Hydrogen.getGas(11000L) },
                    new ItemStack[] { MaterialsKevlar.OrganorhodiumCatalyst.getDust(64),
                            MaterialsKevlar.OrganorhodiumCatalyst.getDust(42), Materials.Salt.getDust(6),
                            Materials.Boron.getDust(3) },
                    800,
                    500000);
            // 2NaOH + N2H4 =Mn= 2N + 2H2O + 2NaH
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(9), Materials.SodiumHydroxide.getDust(6),
                            Materials.Manganese.getDustTiny(1) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("hydrazine"), 1000) },
                    new FluidStack[] { Materials.Nitrogen.getGas(2000L), Materials.Water.getFluid(2000L) },
                    new ItemStack[] { MaterialsKevlar.SodiumHydride.getDust(4) },
                    10,
                    1920);
            GT_Values.RA.addMultiblockChemicalRecipe(
                    new ItemStack[] { GT_Utility.getIntegratedCircuit(18), Materials.SodiumHydroxide.getDust(54),
                            Materials.Manganese.getDust(1) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("hydrazine"), 9000) },
                    new FluidStack[] { Materials.Nitrogen.getGas(18000L), Materials.Water.getFluid(18000L) },
                    new ItemStack[] { MaterialsKevlar.SodiumHydride.getDust(36) },
                    70,
                    1920);
        }
    }
}
