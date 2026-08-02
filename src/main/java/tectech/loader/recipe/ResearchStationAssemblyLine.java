package tectech.loader.recipe;

import static goodgenerator.loader.Loaders.NeutronAccelerators;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static kubatech.api.enums.ItemList.DEFCAwakenedSchematic;
import static kubatech.api.enums.ItemList.DEFCChaoticSchematic;
import static kubatech.api.enums.ItemList.DEFCDraconicSchematic;
import static kubatech.api.enums.ItemList.DEFCWyvernSchematic;
import static kubatech.api.enums.ItemList.DraconicEvolutionFusionCrafter;
import static kubatech.loaders.BlockLoader.defcCasingBlock;
import static tectech.loader.recipe.BaseRecipeLoader.getNHCoreModItem;
import static tectech.thing.CustomItemList.eM_dynamoTunnel5_UMV;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.ruling_0.materiallib.api.MaterialLibAPI;

import bartworks.common.loaders.ItemRegistry;
import goodgenerator.util.ItemRefer;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Circuits;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.FluidShapes;
import gregtech.api.enums.materials.MaterialFacades;
import gregtech.api.enums.materials.Materials;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtnhlanth.common.register.LanthItemList;
import kekztech.common.Blocks;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;

public class ResearchStationAssemblyLine implements Runnable {

    @Override
    public void run() {
        itemPartsUHVAsslineRecipes();
        itemPartsUEVAsslineRecipes();
        itemPartsUIVAsslineRecipes();
        itemPartsUMVAsslineRecipes();
        itemPartsUXVAsslineRecipes();
        addWirelessEnergyRecipes();
        addGodforgeRecipes();
        addBeamcraftingRecipes();

        if (NewHorizonsCoreMod.isModLoaded() && Railcraft.isModLoaded()) {
            // Here instead of CoreMod to coincide with the non-AAL BEC Recipes
            addBecCasingRecipes();
        }

        // Infinite Oil Rig
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.OilDrill4.get(1),
            16777216,
            2048,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.OilDrill4.get(1), GTOreDictUnificator.get("frameGtNeutronium", 4),
                new Object[] { Circuits.UHV.getIngredient(), 4L }, ItemList.Electric_Motor_UHV.get(4),
                ItemList.Electric_Pump_UHV.get(4), MaterialLibAPI.getStack(Materials.Neutronium, Shapes.gearGt, 4),
                ItemList.Sensor_UHV.get(3), ItemList.Field_Generator_UHV.get(3),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.screw, 12) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 9 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 4 * INGOTS) },
            ItemList.InfiniteFluidDrillingRig.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Infinity Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_AwakenedDraconium.get(1),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { new Object[] { Circuits.UHV.getIngredient(), 1L },
                GTOreDictUnificator.get("wireGt02Infinity", 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.screw, 8),
                ItemList.Naquarite_Universal_Insulator_Foil.get(24) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 576), },
            ItemList.Casing_Coil_Infinity.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        // Hypogen Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_Infinity.get(1),
            16_777_216 * 2,
            2048 * 2,
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { new Object[] { Circuits.UEV.getIngredient(), 1L },
                GTOreDictUnificator.get("wireGt02Hypogen", 8L),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.screw, 8),
                ItemList.Naquarite_Universal_Insulator_Foil.get(32) },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 576), },
            ItemList.Casing_Coil_Hypogen.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UIV);

        // Eternal coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_Hypogen.get(1),
            16_777_216 * 4,
            8_192,
            (int) TierEU.RECIPE_UMV,
            1,
            new Object[] { new Object[] { Circuits.UIV.getIngredient(), 1L },
                GTOreDictUnificator.get("wireGt02SpaceTime", 8),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.screw, 8),
                getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                ItemList.Naquarite_Universal_Insulator_Foil.get(64) },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 576), },
            ItemList.Casing_Coil_Eternal.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UMV);

        // EOH Controller Recipe.
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_PlasmaForge.get(1),
            512_000_000, // total comp
            2 * 16_384, // comp/s
            (int) TierEU.RECIPE_MAX, // eu/t
            64, // amperage
            new Object[] { ItemList.SpaceElevatorController.get(16), CustomItemList.Machine_Multi_ForgeOfGods.get(4),
                ItemList.Machine_Multi_PlasmaForge.get(4),

                CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                CustomItemList.StabilisationFieldGeneratorTier0.get(1),

                CustomItemList.Machine_Multi_Computer.get(64), ItemList.AcceleratorUV.get(64),
                ItemList.Quantum_Chest_IV.get(64),
                // Void miner III.
                GTUtility.copyAmount(64, ItemRegistry.voidminer[2]), ItemList.InfiniteFluidDrillingRig.get(64),

                ItemList.Field_Generator_UMV.get(16), ItemList.Robot_Arm_UMV.get(16), ItemList.ZPM5.get(4),
                GTOreDictUnificator.get("wireGt16SuperconductorUMV", 64) },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.temporalFluid, FluidShapes.fluidMolten, 144_000),
                MaterialLibAPI.getFluidStack(Materials.spatialFluid, FluidShapes.fluidMolten, 144_000),
                MaterialLibAPI
                    .getFluidStack(Materials.MetastableOganesson, FluidShapes.fluidMolten, (int) (16 * STACKS)),
                MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (16 * STACKS)), },
            CustomItemList.Machine_Multi_EyeOfHarmony.get(1),
            400 * MINUTES,
            (int) TierEU.RECIPE_UMV);

        // UHV-UMV Energy Hatch & Dynamo
        {
            // Energy Hatches
            {
                // Energy Hatch UHV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UV.get(1L),
                    24000,
                    16,
                    50000,
                    2,
                    new Object[] { ItemList.Hull_MAX.get(1L), GTOreDictUnificator.get("wireGt04Superconductor", 2L),
                        ItemList.Circuit_Chip_QPIC.get(2L), new Object[] { Circuits.UHV.getIngredient(), 2L },
                        ItemList.UHV_Coil.get(2L),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        ItemList.Electric_Pump_UHV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(16_000),
                        MaterialUtils.anyFluid(Materials.Indalloy140, 40 * INGOTS) },
                    ItemList.Hatch_Energy_UHV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UHV);

                // Energy Hatch UEV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UHV.get(1L),
                    48000,
                    32,
                    100000,
                    4,
                    new Object[] { ItemList.Hull_UEV.get(1L), GTOreDictUnificator.get("wireGt04SuperconductorUEV", 2L),
                        ItemList.Circuit_Chip_FPIC.get(2L), new Object[] { Circuits.UEV.getIngredient(), 2L },
                        ItemList.UEV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(32_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 8_000) },
                    ItemList.Hatch_Energy_UEV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UEV);

                // Energy Hatch UIV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UEV.get(1L),
                    96_000,
                    64,
                    200_000,
                    8,
                    new Object[] { ItemList.Hull_UIV.get(1L), GTOreDictUnificator.get("wireGt04SuperconductorUIV", 2L),
                        ItemList.Circuit_Chip_APIC.get(2L), new Object[] { Circuits.UIV.getIngredient(), 2L },
                        ItemList.UIV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 16_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 16_000) },
                    ItemList.Hatch_Energy_UIV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UIV);

                // Energy Hatch UMV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UIV.get(1L),
                    192000,
                    128,
                    400000,
                    16,
                    new Object[] { ItemList.Hull_UMV.get(1L), GTOreDictUnificator.get("wireGt04SuperconductorUMV", 2L),
                        ItemList.Circuit_Chip_ZPIC.get(2L), new Object[] { Circuits.UMV.getIngredient(), 2L },
                        ItemList.UMV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 32_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 40 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 32_000) },
                    ItemList.Hatch_Energy_UMV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UMV);

                // Energy Hatch UXV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UMV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UXV.get(1L), GTOreDictUnificator.get("wireGt08SuperconductorUMV", 4L),
                        ItemList.Circuit_Chip_YPIC.get(2L), new Object[] { Circuits.UXV.getIngredient(), 2L },
                        ItemList.UXV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 64_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS + 16 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 64_000) },
                    ItemList.Hatch_Energy_UXV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UXV);
            }

            // Dynamo Hatch
            {
                // Dynamo Hatch UHV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UV.get(1L),
                    48000,
                    32,
                    100000,
                    4,
                    new Object[] { ItemList.Hull_MAX.get(1L),
                        GTOreDictUnificator.get("springLongasssuperconductornameforuhvwire", 8L),
                        ItemList.Circuit_Chip_QPIC.get(2L), new Object[] { Circuits.UHV.getIngredient(), 2L },
                        ItemList.UHV_Coil.get(2L),
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        new ItemStack[] { ItemList.Reactor_Coolant_He_6.get(1L), ItemList.Reactor_Coolant_NaK_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_2.get(1L) },
                        ItemList.Electric_Pump_UHV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(16_000),
                        MaterialUtils.anyFluid(Materials.Indalloy140, 40 * INGOTS) },
                    ItemList.Hatch_Dynamo_UHV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UHV);

                // Dynamo Hatch UEV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UHV.get(1L),
                    96000,
                    64,
                    200000,
                    8,
                    new Object[] { ItemList.Hull_UEV.get(1L),
                        MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.spring, 8),
                        ItemList.Circuit_Chip_FPIC.get(2L), new Object[] { Circuits.UEV.getIngredient(), 2L },
                        ItemList.UEV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(32_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 8_000) },
                    ItemList.Hatch_Dynamo_UEV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UEV);

                // Dynamo Hatch UIV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UEV.get(1L),
                    192_000,
                    128,
                    400_000,
                    16,
                    new Object[] { ItemList.Hull_UIV.get(1L),
                        MaterialLibAPI.getStack(Materials.SuperconductorUIVBase, Shapes.spring, 8),
                        ItemList.Circuit_Chip_APIC.get(2L), new Object[] { Circuits.UIV.getIngredient(), 2L },
                        ItemList.UIV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 16_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 16_000L) },
                    ItemList.Hatch_Dynamo_UIV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UIV);

                // Dynamo Hatch UMV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UIV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UMV.get(1L),
                        MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.spring, 8),
                        ItemList.Circuit_Chip_ZPIC.get(2L), new Object[] { Circuits.UMV.getIngredient(), 2L },
                        ItemList.UMV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 32_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 40 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 32_000) },
                    ItemList.Hatch_Dynamo_UMV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UMV);

                // Dynamo Hatch UXV
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UMV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UXV.get(1L),
                        MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.spring, 16),
                        ItemList.Circuit_Chip_YPIC.get(2L), new Object[] { Circuits.UXV.getIngredient(), 2L },
                        ItemList.UXV_Coil.get(2L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] {
                        MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 64_000),
                        MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS + 16 * INGOTS),
                        MaterialUtils.fluid(Materials.UUMatter, 64_000) },
                    ItemList.Hatch_Dynamo_UXV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UXV);
            }
        }

        // UHV Circuit Wetwaremainframe
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_Wetwaresupercomputer.get(1L),
            24000,
            64,
            50000,
            4,
            new Object[] { GTOreDictUnificator.get("frameGtTritanium", 2),
                ItemList.Circuit_Wetwaresupercomputer.get(2L),
                new ItemStack[] { ItemList.Circuit_Parts_InductorASMD.get(16L),
                    ItemList.Circuit_Parts_InductorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(16L),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(16L),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L), ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                ItemList.Circuit_Chip_Ram.get(48L),
                new ItemStack[] { GTOreDictUnificator.get("wireGt01SuperconductorZPM", 64L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUV", 32L),
                    GTOreDictUnificator.get("wireGt01Superconductor", 16L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUEV", 8L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUIV", 4L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUMV", 2L) },
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.Indalloy140, 20 * INGOTS),
                GTModHandler.getIC2Coolant(10_000),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidGas, 2_500), },
            ItemList.Circuit_Wetwaremainframe.get(1L),
            2000,
            300000);

        // Bioware SuperComputer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_Biowarecomputer.get(1L),
            48000,
            128,
            (int) TierEU.RECIPE_UV,
            8,
            new Object[] { ItemList.Circuit_Board_Bio_Ultra.get(2L), ItemList.Circuit_Biowarecomputer.get(2L),
                new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(16L),
                    ItemList.Circuit_Parts_TransistorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(16L),
                    ItemList.Circuit_Parts_ResistorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(4L) },
                new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L), ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                ItemList.Circuit_Chip_NOR.get(32L), ItemList.Circuit_Chip_Ram.get(64L),
                MaterialLibAPI.getStack(Materials.NiobiumTitanium, Shapes.wireFine, 32),
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 10 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.BiohMediumSterilized, FluidShapes.fluidLiquid, 10 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 10_000), },
            ItemList.Circuit_Biowaresupercomputer.get(1L),
            4000,
            (int) TierEU.RECIPE_UV);

        // Bio Mainframe
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_Biowaresupercomputer.get(1L),
            96000,
            256,
            1000000,
            16,
            new Object[] { GTOreDictUnificator.get("frameGtTritanium", 4L),
                ItemList.Circuit_Biowaresupercomputer.get(2L),
                new ItemStack[] { ItemList.Circuit_Parts_InductorASMD.get(24L),
                    ItemList.Circuit_Parts_InductorXSMD.get(6L) },
                new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(24L),
                    ItemList.Circuit_Parts_TransistorXSMD.get(6L) },
                new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(24L),
                    ItemList.Circuit_Parts_ResistorXSMD.get(6L) },
                new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(24L),
                    ItemList.Circuit_Parts_CapacitorXSMD.get(6L) },
                new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(24L), ItemList.Circuit_Parts_DiodeXSMD.get(6L) },
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack[] { GTOreDictUnificator.get("wireGt01Superconductor", 64L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUEV", 32L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUIV", 16L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUMV", 8L) },
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.foil, 64) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.BiohMediumSterilized, FluidShapes.fluidLiquid, 20 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 20_000), },
            ItemList.Circuit_Biomainframe.get(1L),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Optical Assembly
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_OpticalProcessor.get(1L),
            192_000,
            256,
            (int) TierEU.RECIPE_UHV,
            16,
            new Object[] { ItemList.Circuit_Board_Optical.get(1L), ItemList.Circuit_OpticalProcessor.get(4L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L), ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L), ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L), MaterialLibAPI.getStack(Materials.Lumiium, Shapes.wireFine, 24), // Fine
                // Lumiium
                // Wire
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 10 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 10 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 10_000),
                new FluidStack(FluidRegistry.getFluid("oganesson"), 500) },
            ItemList.Circuit_OpticalAssembly.get(1L),
            20 * 20,
            (int) TierEU.RECIPE_UHV);

        // Optical Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_OpticalAssembly.get(1L),
            384_000,
            512,
            4_000_000,
            32,
            new Object[] { ItemList.Circuit_Board_Optical.get(2L), ItemList.Circuit_OpticalAssembly.get(4L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L), ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L), ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L), ItemList.Circuit_Chip_SoC2.get(32L),
                MaterialLibAPI.getStack(Materials.Lumiium, Shapes.wireFine, 32), // Fine
                // Lumiium
                // Wire
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.foil, 64), },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 20 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 20_000),
                new FluidStack(FluidRegistry.getFluid("oganesson"), 1_000) },
            ItemList.Circuit_OpticalComputer.get(1L),
            200 * 20,
            (int) TierEU.RECIPE_UHV);

        // Optical Mainframe
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_OpticalComputer.get(1L),
            768_000,
            1024,
            (int) TierEU.RECIPE_UEV,
            64,
            new Object[] { GTOreDictUnificator.get("frameGtTritanium", 8), ItemList.Circuit_OpticalComputer.get(4L),
                ItemList.Circuit_Parts_InductorXSMD.get(32L), ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                ItemList.Circuit_Parts_ResistorXSMD.get(32L), ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                ItemList.Circuit_Parts_DiodeXSMD.get(32L), ItemList.Circuit_Chip_SoC2.get(64L),
                new ItemStack[] { GTOreDictUnificator.get("wireGt01SuperconductorUEV", 64L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUIV", 32L),
                    GTOreDictUnificator.get("wireGt01SuperconductorUMV", 16L) },
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.foil.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Polybenzimidazole, Shapes.foil, 64) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 40 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Radon, FluidShapes.fluidPlasma, 40 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 40_000),
                new FluidStack(FluidRegistry.getFluid("oganesson"), 2_000) },
            ItemList.Circuit_OpticalMainframe.get(1L),
            300 * 20,
            (int) TierEU.RECIPE_UEV);

        // Laser Vacuum Mirror
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.LASERpipe.get(1L),
            20_000,
            16,
            40_000,
            2,
            new Object[] { CustomItemList.eM_Power.get(1L), CustomItemList.LASERpipe.get(4L),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plateDense, 1),
                ItemList.Circuit_Chip_QPIC.get(2L), new Object[] { Circuits.UHV.getIngredient(), 1L } },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Grade4PurifiedWater, FluidShapes.fluidLiquid, 4_000) },
            CustomItemList.LASERpipeSmart.get(1L),
            10 * 20,
            (int) TierEU.RECIPE_UHV);

        // Transcendent Plasma Mixer - TPM.
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel6_UEV.get(1),
            32_000_000,
            4096,
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { CustomItemList.eM_energyTunnel6_UEV.get(4),
                new Object[] { Circuits.UIV.getIngredient(), 32L }, ItemList.Electric_Pump_UEV.get(16),
                GTOreDictUnificator.get("plateprotohalkonite", 64),

                GTOreDictUnificator.get("gearGtprotohalkonite", 16),
                GTOreDictUnificator.get("gearGtSmallprotohalkonite", 64),
                GTOreDictUnificator.get("screwprotohalkonite", 64), ItemList.EnergisedTesseract.get(32),

                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L), },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.ExcitedDTCC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTPC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTRC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_048_000), },
            ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
            5 * MINUTES,
            (int) TierEU.RECIPE_UIV);

        // Dimensionally Transcendent Plasma Forge (DTPF)
        if (EternalSingularity.isModLoaded()) {

            // DTPF Controller.
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Bridge.get(1),
                32_000_000,
                4096,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { ItemList.Casing_Dim_Bridge.get(4), ItemList.ExothermicHearth.get(16),
                    ItemList.Hatch_Energy_UEV.get(4L), GTOreDictUnificator.get("wireGt16SuperconductorUEV", 6),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    new Object[] { Circuits.UIV.getIngredient(), 20L }, ItemList.Field_Generator_UEV.get(4),
                    getModItem(EternalSingularity.ID, "eternal_singularity", 4L),
                    GregtechItemList.Laser_Lens_Special.get(1),
                    MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.plateSuperdense, 4),
                    ItemList.Electric_Pump_UEV.get(4), ItemList.ZPM3.get(1),
                    getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 128_000),
                    MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 8 * STACKS),
                    new FluidStack(FluidRegistry.getFluid("molten.californium"), 4 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 4 * STACKS) },
                ItemList.Machine_Multi_PlasmaForge.get(1),
                5 * MINUTES,
                (int) TierEU.RECIPE_UIV);

            // Dimensional bridge.
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Injector.get(1),
                8_000_000,
                4096,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { ItemList.Casing_Dim_Trans.get(1), ItemList.MicroTransmitter_UV.get(1),
                    new Object[] { Circuits.UV.getIngredient(), 2L }, getModItem(Avaritia.ID, "Singularity", 2L, 0),
                    GTOreDictUnificator.get("wireGt01Superconductor", 6), ItemList.Circuit_Wafer_FPIC.get(4),
                    ItemList.Field_Generator_UHV.get(1L) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 8_000),
                    MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 9 * INGOTS) },
                ItemList.Casing_Dim_Bridge.get(1),
                240 * 20,
                (int) TierEU.RECIPE_UIV);

            // Dimensional injection casing.
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Trans.get(1),
                2_000_000,
                2048,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.plate, 4),
                    MaterialLibAPI.getStack(Materials.Ledox, Shapes.plateDense, 1),
                    MaterialLibAPI.getStack(Materials.CallistoIce, Shapes.plateDense, 1),
                    ItemList.Reactor_Coolant_Sp_6.get(1L),
                    MaterialLibAPI.getStack(Materials.Laurenium, Shapes.screw, 12),
                    new Object[] { Circuits.IV.getIngredient(), 2L },
                    GTOreDictUnificator.get("wireGt01Superconductor", 2), ItemList.Super_Chest_IV.get(1),
                    ItemList.Super_Tank_IV.get(1), ItemList.Circuit_Wafer_FPIC.get(2) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 1_000),
                    MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 4 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 2 * INGOTS) },
                ItemList.Casing_Dim_Injector.get(1),
                20 * 20,
                (int) TierEU.RECIPE_UIV);

            // Dimensionally Transcendent Casing.
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                getModItem(Avaritia.ID, "Singularity", 1L, 0),
                2_000_000,
                2048,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { MaterialLibAPI.getStack(Materials.Osmiridium, Shapes.plate, 6),
                    MaterialLibAPI.getStack(Materials.Laurenium, Shapes.screw, 12),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), GTOreDictUnificator.get("wireGt01Superconductor", 1), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                    MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 2 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.NaquadahEnriched, FluidShapes.fluidMolten, 1 * INGOTS) },
                ItemList.Casing_Dim_Trans.get(1),
                20 * 20,
                (int) TierEU.RECIPE_UIV);
        }

        // Deep Dark Portal
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier8", 1, 0),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UHV,
            64,
            new Object[] { getModItem(ExtraUtilities.ID, "cobblestone_compressed", 1, 7),
                getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0), GTOreDictUnificator.get("blockInfinity", 4L),
                new Object[] { Circuits.UIV.getIngredient(), 4 }, ItemList.Circuit_Wafer_APIC.get(32),
                ItemList.Robot_Arm_UIV.get(4), ItemList.Emitter_UIV.get(4), ItemList.Sensor_UIV.get(4), },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 50_000),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 8 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Cheese, FluidShapes.fluidMolten, 232_000), },
            ItemList.Block_BedrockiumCompressed.get(1),
            3 * MINUTES,
            (int) TierEU.RECIPE_UMV);

        // Batteries
        {

            // Ultimate Battery
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Energy_Cluster.get(1L),
                12000,
                16,
                100000,
                3,
                new Object[] { MaterialLibAPI.getStack(Materials.Tritanium, Shapes.plate, 64),
                    new Object[] { Circuits.UHV.getIngredient(), 4L }, ItemList.Energy_Cluster.get(8L),
                    ItemList.Field_Generator_UV.get(2), ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Parts_DiodeASMD.get(32),
                    GTOreDictUnificator.get("wireGt01Superconductor", 32), },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.Indalloy140, 20 * INGOTS),
                    GTModHandler.getIC2Coolant(16_000) },
                ItemList.ZPM2.get(1),
                3000,
                400000);

            // Really Ultimate Battery
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM2.get(1L),
                24000,
                64,
                200000,
                6,
                new Object[] { MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plateDouble, 64),
                    new Object[] { Circuits.UEV.getIngredient(), 4L }, ItemList.ZPM2.get(8),
                    ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                    ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                    ItemList.Circuit_Parts_DiodeASMD.get(64), GTOreDictUnificator.get("wireGt02Superconductor", 64), },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 32 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 1 * STACKS),
                    GTModHandler.getIC2Coolant(32_000) },
                ItemList.ZPM3.get(1),
                4000,
                1600000);

            // Extremely Ultimate Battery
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM3.get(1L),
                1_200_000,
                128,
                (int) TierEU.RECIPE_UEV,
                16,
                new Object[] { MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.plateDouble, 64),
                    new Object[] { Circuits.UIV.getIngredient(), 4L }, ItemList.ZPM3.get(8),
                    ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                    ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64),
                    GTOreDictUnificator.get("wireGt04SuperconductorUEV", 64), },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 2 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 2 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 64_000) },
                ItemList.ZPM4.get(1),
                250 * 20,
                6_400_000);

            // Insanely Ultimate Battery
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM4.get(1),
                24_000_000,
                1_280,
                (int) TierEU.RECIPE_UIV,
                32,
                new Object[] { MaterialLibAPI.getStack(Materials.Hypogen, Shapes.plateDouble, 64),
                    new Object[] { Circuits.UMV.getIngredient(), 4 }, ItemList.ZPM4.get(8L),
                    ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64), ItemList.Circuit_Parts_Crystal_Chip_Wetware.get(64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                    GTOreDictUnificator.get("wireGt08SuperconductorUIV", 64) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 2 * STACKS),
                    MaterialUtils.anyFluid(Materials.CelestialTungsten, 2 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 2 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 128_000) },
                ItemList.ZPM5.get(1),
                300 * 20,
                (int) TierEU.RECIPE_UIV);

            // Mega Ultimate Battery
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM5.get(1L),
                480_000_000,
                12_288,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { MaterialLibAPI.getStack(Materials.Dragonblood, Shapes.plateDouble, 64),
                    new Object[] { Circuits.UXV.getIngredient(), 4 }, ItemList.ZPM5.get(8L),
                    ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_FPIC.get(64),
                    ItemList.Circuit_Wafer_FPIC.get(64), ItemList.Circuit_Parts_Chip_Bioware.get(64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                    GTOreDictUnificator.get("wireGt16SuperconductorUMV", 64) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 4 * STACKS),
                    MaterialUtils.anyFluid(Materials.AstralTitanium, 4 * STACKS),
                    MaterialUtils.anyFluid(Materials.CelestialTungsten, 4 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 256_000) },
                ItemList.ZPM6.get(1),
                360 * 20,
                (int) TierEU.RECIPE_UMV);
        }

        // MK4 Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Compressed_Fusion_Reactor.get(1),
            320000,
            512,
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { GregtechItemList.Casing_Fusion_Internal.get(1),
                new Object[] { Circuits.UEV.getIngredient(), 1L }, new Object[] { Circuits.UEV.getIngredient(), 1L },
                new Object[] { Circuits.UEV.getIngredient(), 1L }, new Object[] { Circuits.UEV.getIngredient(), 1L },
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plateSuperdense, 1),
                ItemList.Field_Generator_UHV.get(2), ItemList.Circuit_Wafer_FPIC.get(64),
                GTOreDictUnificator.get("wireGt04Superconductor", 32) },
            new FluidStack[] { MaterialUtils.fluid(Materials.UUMatter, 50_000),
                MaterialUtils.anyFluid(Materials.CinobiteA243, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.Octiron, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.AstralTitanium, 1 * STACKS), },
            GregtechItemList.FusionComputer_UV2.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // MK4 Coils
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Fusion_Coil.get(1L),
            160000,
            512,
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { ItemList.Energy_LapotronicOrb2.get(16L), new Object[] { Circuits.LuV.getIngredient(), 16L },
                new Object[] { Circuits.UV.getIngredient(), 8L },
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, 8), ItemList.Emitter_UHV.get(1),
                ItemList.Sensor_UHV.get(1), ItemList.Casing_Fusion_Coil.get(1L), },
            new FluidStack[] { MaterialUtils.fluid(Materials.UUMatter, 8_000),
                MaterialUtils.anyFluid(Materials.CinobiteA243, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.Octiron, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.AstralTitanium, 16 * INGOTS), },
            GregtechItemList.Casing_Fusion_Internal.get(1),
            1200,
            (int) TierEU.RECIPE_UHV);

        if (EtFuturumRequiem.isModLoaded()) {
            // MK4 Casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Fusion2.get(1L),
                80000,
                512,
                (int) TierEU.RECIPE_UHV,
                1,
                new Object[] { new Object[] { Circuits.EV.getIngredient(), 16L },
                    new Object[] { Circuits.IV.getIngredient(), 8L },
                    getModItem(EtFuturumRequiem.ID, "netherite_block", 2),
                    MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, 8), ItemList.Electric_Motor_UHV.get(2),
                    ItemList.Electric_Piston_UHV.get(1), ItemList.Casing_Fusion2.get(1L), },
                new FluidStack[] { MaterialUtils.fluid(Materials.UUMatter, 1_000),
                    MaterialUtils.anyFluid(Materials.CinobiteA243, 4 * INGOTS),
                    MaterialUtils.anyFluid(Materials.Octiron, 4 * INGOTS),
                    MaterialUtils.anyFluid(Materials.AstralTitanium, 4 * INGOTS), },
                GregtechItemList.Casing_Fusion_External.get(1),
                300,
                (int) TierEU.RECIPE_UHV);
        }

        // MK5 Computer
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.FusionComputer_UV2.get(1),
            2560000,
            4096,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] { GregtechItemList.Casing_Fusion_Internal2.get(1),
                new Object[] { Circuits.UIV.getIngredient(), 1L }, new Object[] { Circuits.UIV.getIngredient(), 1L },
                new Object[] { Circuits.UIV.getIngredient(), 1L }, new Object[] { Circuits.UIV.getIngredient(), 1L },
                GTOreDictUnificator.get("plateDenseMetastableOganesson", 4), ItemList.Field_Generator_UEV.get(2),
                ItemList.Circuit_Wafer_APIC.get(64), GTOreDictUnificator.get("wireGt04SuperconductorUEV", 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.Curium, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.ChromaticGlass, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.AbyssalAlloy, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.Dragonblood, 1 * STACKS) },
            GregtechItemList.FusionComputer_UV3.get(1),
            6000,
            (int) TierEU.RECIPE_UEV);

        // MK5 Coils
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Fusion_Internal.get(1),
            2560000,
            4096,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] { ItemList.Energy_Module.get(16), new Object[] { Circuits.ZPM.getIngredient(), 16L },
                new Object[] { Circuits.UHV.getIngredient(), 8L },
                MaterialLibAPI.getStack(Materials.Rhugnor, Shapes.plate, 8), ItemList.Emitter_UEV.get(1),
                ItemList.Sensor_UEV.get(1), new ItemStack(compactFusionCoil, 1, 2) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.Neptunium, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.ChromaticGlass, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.AbyssalAlloy, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.Dragonblood, 16 * INGOTS) },
            GregtechItemList.Casing_Fusion_Internal2.get(1),
            1200,
            (int) TierEU.RECIPE_UEV);

        if (EtFuturumRequiem.isModLoaded()) {
            // MK5 Casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GregtechItemList.Casing_Fusion_External.get(1L),
                2560000,
                4096,
                (int) TierEU.RECIPE_UEV,
                8,
                new Object[] { new Object[] { Circuits.IV.getIngredient(), 16L },
                    new Object[] { Circuits.LuV.getIngredient(), 8L },
                    getModItem(EtFuturumRequiem.ID, "netherite_block", 8),
                    MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plate, 8),
                    ItemList.Electric_Motor_UEV.get(2), ItemList.Electric_Piston_UEV.get(1),
                    GregtechItemList.Casing_Fusion_External.get(1L) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.Fermium, 8 * INGOTS),
                    MaterialUtils.anyFluid(Materials.ChromaticGlass, 8 * INGOTS),
                    MaterialUtils.anyFluid(Materials.AbyssalAlloy, 8 * INGOTS),
                    MaterialUtils.anyFluid(Materials.Dragonblood, 8 * INGOTS) },
                GregtechItemList.Casing_Fusion_External2.get(1),
                300,
                (int) TierEU.RECIPE_UEV);
        }

        if (BloodMagic.isModLoaded() && DraconicEvolution.isModLoaded()) {
            // Draconic Evolution Fusion Crafter Controller
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(defcCasingBlock, 1, 8),
                16_777_216,
                1024,
                (int) TierEU.RECIPE_UHV,
                8,
                new Object[] { ItemList.AssemblingMachineUHV.get(1), new ItemStack(defcCasingBlock, 1, 8),
                    MaterialLibAPI.getStack(Materials.GaiaSpirit, Shapes.plateDense, 1),
                    ItemList.Casing_Coil_AwakenedDraconium.get(8L), ItemList.Electric_Motor_UHV.get(8L),
                    ItemList.Robot_Arm_UHV.get(4L), new Object[] { Circuits.UHV.getIngredient(), 4 },
                    ItemList.Gravistar.get(4, new Object() {}), getModItem(Thaumcraft.ID, "ItemEldritchObject", 1, 3),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 29),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 28),
                    MaterialLibAPI.getStack(Materials.Void, Shapes.plateSuperdense, 1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.Indalloy140, 20 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, 20 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.DraconiumAwakened, FluidShapes.fluidMolten, 10 * INGOTS), },
                DraconicEvolutionFusionCrafter.get(1),
                1500,
                (int) TierEU.RECIPE_UEV);
            // DE Schematics Cores Tier 1
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(defcCasingBlock, 1, 9),
                5_000_000,
                512,
                1_000_000,
                4,
                new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                    MaterialLibAPI.getStack(Materials.Draconium, Shapes.plateDense, 1),
                    MaterialLibAPI.getStack(Materials.Ichorium, Shapes.plate, 1),
                    new Object[] { Circuits.UHV.getIngredient(), 1 }, },
                new FluidStack[] {
                    MaterialLibAPI
                        .getFluidStack(Materials.Sunnarium, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, 3 * STACKS + 8 * INGOTS), },
                DEFCDraconicSchematic.get(1),
                3 * MINUTES,
                (int) TierEU.RECIPE_UHV);

            // DE Schematics Cores Tier 2
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                10_000_000,
                1024,
                4_000_000,
                8,
                new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 4, 0),
                    MaterialLibAPI.getStack(Materials.Draconium, Shapes.plateDense, 1),
                    MaterialLibAPI.getStack(Materials.Neutronium, Shapes.plate, 1),
                    new Object[] { Circuits.UEV.getIngredient(), 1 }, },
                new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 14400),
                    MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, 57600), },
                DEFCWyvernSchematic.get(1),
                3 * MINUTES,
                (int) TierEU.RECIPE_UEV);

            // DE Schematics Cores Tier 3
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "wyvernCore", 1, 0),
                20_000_000,
                2048,
                16_000_000,
                16,
                new Object[] { getModItem(DraconicEvolution.ID, "wyvernCore", 4, 0),
                    MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 1),
                    MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 1),
                    new Object[] { Circuits.UIV.getIngredient(), 1L } },
                new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 14400),
                    MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, 115200), },
                DEFCAwakenedSchematic.get(1),
                3 * MINUTES,
                (int) TierEU.RECIPE_UIV);

            // DE Schematics Cores Tier 4
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "awakenedCore", 1, 0),
                40_000_000,
                4096,
                64_000_000,
                64,
                new Object[] { getModItem(DraconicEvolution.ID, "awakenedCore", 8, 0),
                    MaterialLibAPI.getStack(Materials.DraconiumAwakened, Shapes.plateDense, 4),
                    MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 1),
                    new Object[] { Circuits.UMV.getIngredient(), 1 }, },
                new FluidStack[] {
                    MaterialLibAPI
                        .getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Void, FluidShapes.fluidMolten, 25 * STACKS), },
                DEFCChaoticSchematic.get(1),
                3 * MINUTES,
                (int) TierEU.RECIPE_UMV);
        }

        // Debug maintenance hatch
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Hatch_AutoMaintenance.get(1L),
            2764800,
            128,
            (int) TierEU.RECIPE_UV,
            6,
            new Object[] { ItemList.Hatch_AutoMaintenance.get(1L), ItemList.Robot_Arm_UV.get(1L),
                ItemList.Electric_Pump_UV.get(1L), ItemList.Conveyor_Module_UV.get(1L),
                new Object[] { Circuits.UV.getIngredient(), 4L }, ItemList.Energy_LapotronicOrb2.get(1L),
                ItemList.Duct_Tape.get(64L), ItemList.Duct_Tape.get(64L),
                MaterialLibAPI.getStack(Materials.Americium, Shapes.wireFine, 64), },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 256_000),
                MaterialUtils.anyFluid(Materials.Indalloy140, 9 * INGOTS), },
            CustomItemList.hatch_CreativeMaintenance.get(1),
            6000,
            (int) TierEU.RECIPE_UV);

        // Debug uncertainty resolver
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.UncertaintyX_Hatch.get(1L),
            72_000_000,
            5_120,
            16_000_000,
            6,
            new Object[] { CustomItemList.eM_Computer_Bus.get(1), CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Field_Generator_UIV.get(1L), GregtechItemList.Laser_Lens_Special.get(4),
                new Object[] { Circuits.UMV.getIngredient(), 2 }, CustomItemList.DATApipe.get(64),
                CustomItemList.DATApipe.get(64), ItemList.Cover_Screen.get(1) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Iridium, FluidShapes.fluidMolten, 1 * STACKS + 36 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 18 * INGOTS),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Neptunium), 20_000),
                new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.Fermium), 20_000) },
            CustomItemList.hatch_CreativeUncertainty.get(1),
            200 * 20,
            (int) TierEU.RECIPE_UIV);

        // Magnetic Anchor Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.MagneticFluxCasing.get(1),
            144_000_000,
            25_600,
            (int) TierEU.RECIPE_MAX,
            16,
            new Object[] { ItemRefer.MagneticFluxCasing.get(16),
                CustomItemList.Godforge_MagneticConfinementCasing.get(16), ItemList.Field_Generator_UMV.get(1L),
                GTOreDictUnificator.get("plateSuperdenseMagmatter", 1L),
                GTOreDictUnificator.get("wireGt08SuperconductorUMV", 8L),
                MaterialLibAPI.getStack(Materials.Shirabon, Shapes.plateDense, 64) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.spatialFluid, FluidShapes.fluidMolten, 1 * STACKS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS),
                MaterialLibAPI.getFluidStack(
                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                    FluidShapes.fluidMolten,
                    16) },
            ItemList.MagneticAnchorCasing.get(4),
            20 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // Precision Field Sync Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.GravityStabilizationCasing.get(1),
            144_000_000,
            25_600,
            (int) TierEU.RECIPE_MAX,
            16,
            new Object[] { ItemRefer.GravityStabilizationCasing.get(16), ItemRefer.Field_Restriction_Coil_T4.get(2),
                CustomItemList.StabilisationFieldGeneratorTier0.get(1),
                GTOreDictUnificator.get("frameGtWhiteDwarfMatter", 1L), ItemList.Field_Generator_UMV.get(1L),
                GTOreDictUnificator.get("wireFineMagmatter", 64L),
                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Shirabon, Shapes.wireFine, 64),
                GTOreDictUnificator.get("plateSuperdenseFlerovium_GT5U", 16L), },
            new FluidStack[] { new FluidStack(MaterialUtils.legacyGtppPlasmaOf(Materials.ChromaticGlass), 1 * STACKS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.Eternity, FluidShapes.fluidMolten, 1 * STACKS) },
            ItemList.PrecisionFieldSyncCasing.get(8),
            10 * SECONDS,
            (int) TierEU.RECIPE_UMV);
    }

    private void itemPartsUHVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        int total_computation = 24000;
        int comp_per_second = 32;
        int research_eu_per_tick = (int) TierEU.RECIPE_UV;
        int research_amperage = 1;

        FluidStack fluid_0 = MaterialLibAPI.getFluidStack(Materials.Naquadria, FluidShapes.fluidMolten, 18 * INGOTS);
        FluidStack fluid_1 = MaterialUtils.anyFluid(Materials.Indalloy140, 18 * INGOTS);
        FluidStack fluid_2 = MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 4_000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UV;

        // -------------------------------------------------------------

        // ------------------------- UHV Motor -------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.SamariumMagnetic, Shapes.stickLong, 4),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.stickLong, 8),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.round, 32),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Bedrockium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UHV Electric Pump ---------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UHV.get(1L), GTOreDictUnificator.get("pipeLargeNeutronium", 2L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.screw, 16),
                new Object[] { OrePrefixes.ring.ingredient(MaterialFacades.AnySyntheticRubber), 32L },
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.rotor, 4),
                GTOreDictUnificator.get("cableGt04Bedrockium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UHV Conveyor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UHV.get(2L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.round, 64),
                GTOreDictUnificator.get("cableGt04Bedrockium", 2L),
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 40L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Robot Arm --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.stickLong, 8),
                GTOreDictUnificator.get("gearCosmicNeutronium", 2L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.gearGtSmall, 6),
                ItemList.Electric_Motor_UHV.get(2L), ItemList.Electric_Piston_UHV.get(1L),
                new Object[] { Circuits.UHV.getIngredient(), 2L }, new Object[] { Circuits.UV.getIngredient(), 4L },
                new Object[] { Circuits.ZPM.getIngredient(), 8L }, GTOreDictUnificator.get("cableGt04Bedrockium", 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Electric Piston --------------------
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UHV.get(1L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.round, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.stick, 8),
                GTOreDictUnificator.get("gearCosmicNeutronium", 2L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.gearGtSmall, 4),
                GTOreDictUnificator.get("cableGt04Bedrockium", 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Emitter ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtCosmicNeutronium", 1L), ItemList.Electric_Motor_UHV.get(1L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.stick, 8), ItemList.Gravistar.get(8L),
                new Object[] { Circuits.UHV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Bedrockium", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Sensor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtCosmicNeutronium", 1L), ItemList.Electric_Motor_UHV.get(1L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 8), ItemList.Gravistar.get(8L),
                new Object[] { Circuits.UHV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.ElectrumFlux, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Bedrockium", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UHV Field Generator ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtCosmicNeutronium", 1L),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plate, 6), ItemList.Gravistar.get(4L),
                ItemList.Emitter_UHV.get(4L), new Object[] { Circuits.UEV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Neutronium, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Bedrockium", 8L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Field_Generator_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);
    }

    private void itemPartsUEVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        int total_computation = 48_000;
        int comp_per_second = 64;
        int research_eu_per_tick = (int) TierEU.RECIPE_UHV;
        int research_amperage = 1;

        FluidStack fluid_0 = MaterialLibAPI.getFluidStack(Materials.Quantium, FluidShapes.fluidMolten, 18 * INGOTS);
        FluidStack fluid_1 = MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 18 * INGOTS);
        FluidStack fluid_2 = MaterialLibAPI.getFluidStack(Materials.Lubricant, FluidShapes.fluidLiquid, 4_000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UHV;

        // -------------------------------------------------------------

        // ------------------------- UEV Motor -------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.TengamAttuned, Shapes.stickLong, 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.stickLong, 16),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.round, 32),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Draconium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UEV Electric Pump ---------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UEV.get(1L), GTOreDictUnificator.get("pipeLargeNetherStar", 2L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.screw, 16),
                new Object[] { OrePrefixes.ring.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.rotor, 4),
                GTOreDictUnificator.get("cableGt04Draconium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UEV Conveyor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UEV.get(2L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.round, 64),
                GTOreDictUnificator.get("cableGt04Draconium", 2L),
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Robot Arm --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { MaterialLibAPI.getStack(Materials.Infinity, Shapes.stickLong, 8),
                GTOreDictUnificator.get("gearInfinity", 2L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.gearGtSmall, 6), ItemList.Electric_Motor_UEV.get(2L),
                ItemList.Electric_Piston_UEV.get(1L), new Object[] { Circuits.UEV.getIngredient(), 2L },
                new Object[] { Circuits.UHV.getIngredient(), 4L }, new Object[] { Circuits.UV.getIngredient(), 8L },
                GTOreDictUnificator.get("cableGt04Draconium", 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Electric Piston --------------------
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UEV.get(1L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.round, 64),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.stick, 8),
                GTOreDictUnificator.get("gearInfinity", 2L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.gearGtSmall, 4),
                GTOreDictUnificator.get("cableGt04Draconium", 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Emitter ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtInfinity", 1L), ItemList.Electric_Motor_UEV.get(1L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.stick, 16), ItemList.Gravistar.get(16L),
                new Object[] { Circuits.UEV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Draconium", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Sensor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtInfinity", 1L), ItemList.Electric_Motor_UEV.get(1),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 8), ItemList.Gravistar.get(16),
                new Object[] { Circuits.UEV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.InfinityCatalyst, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Draconium", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UEV Field Generator ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UHV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtInfinity", 1L),
                MaterialLibAPI.getStack(Materials.Infinity, Shapes.plate, 6), ItemList.Gravistar.get(8L),
                ItemList.Emitter_UEV.get(4L), new Object[] { Circuits.UIV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Tritanium, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Draconium", 8L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Field_Generator_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);
    }

    private void itemPartsUIVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        int total_computation = 96_000;
        int comp_per_second = 128;
        int research_eu_per_tick = (int) TierEU.RECIPE_UEV;
        int research_amperage = 1;

        FluidStack fluid_0 = MaterialUtils.anyFluid(Materials.CelestialTungsten, 4 * INGOTS);
        FluidStack fluid_1 = MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 18 * INGOTS);
        FluidStack fluid_2 = MaterialLibAPI
            .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 4_000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UEV;

        // -------------------------------------------------------------

        // ------------------------- UIV Motor -------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.TengamAttuned, Shapes.stickLong, 16),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.stickLong, 16),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.round, 32),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("cableGt04NetherStar", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UIV Electric Pump ---------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UIV.get(1L),
                GTOreDictUnificator.get("pipeLargeDraconiumAwakened", 2L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.screw, 16),
                new Object[] { OrePrefixes.ring.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.rotor, 4),
                GTOreDictUnificator.get("cableGt04NetherStar", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UIV Conveyor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UIV.get(2L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.round, 64),
                GTOreDictUnificator.get("cableGt04NetherStar", 2L),
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Robot Arm --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.stickLong, 8),
                GTOreDictUnificator.get("gearTranscendentMetal", 2L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.gearGtSmall, 6),
                ItemList.Electric_Motor_UIV.get(2L), ItemList.Electric_Piston_UIV.get(1L),
                new Object[] { Circuits.UIV.getIngredient(), 2L }, new Object[] { Circuits.UEV.getIngredient(), 4L },
                new Object[] { Circuits.UHV.getIngredient(), 8L }, GTOreDictUnificator.get("cableGt04NetherStar", 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Electric Piston --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UIV.get(1L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.round, 64),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.stick, 8),
                GTOreDictUnificator.get("gearTranscendentMetal", 2L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.gearGtSmall, 4),
                GTOreDictUnificator.get("cableGt04NetherStar", 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Emitter ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 1L), ItemList.Electric_Motor_UIV.get(1L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.stick, 16), ItemList.NuclearStar.get(2L),
                new Object[] { Circuits.UIV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.ArceusAlloy2B, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.LafiumCompound, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.CinobiteA243, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04NetherStar", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Sensor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 1L), ItemList.Electric_Motor_UIV.get(1),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plate, 8), ItemList.NuclearStar.get(2L),
                new Object[] { Circuits.UIV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.ArceusAlloy2B, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.LafiumCompound, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.CinobiteA243, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Pikyonium64B, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04NetherStar", 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UIV Field Generator ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UEV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 1L),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plate, 6), ItemList.NuclearStar.get(1L),
                ItemList.Emitter_UIV.get(4L), new Object[] { Circuits.UMV.getIngredient(), 4 },
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("wireFineprotohalkonite", 64L),
                GTOreDictUnificator.get("cableGt04NetherStar", 8L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Field_Generator_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

    }

    private void itemPartsUMVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        int total_computation = 192_000;
        int comp_per_second = 256;
        int research_eu_per_tick = (int) TierEU.RECIPE_UIV;
        int research_amperage = 1;

        FluidStack fluid_0 = MaterialUtils.anyFluid(Materials.Hypogen, 4 * INGOTS);
        FluidStack fluid_1 = MaterialUtils.anyFluid(Materials.CelestialTungsten, 4 * INGOTS);
        FluidStack fluid_2 = MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 18 * INGOTS);
        FluidStack fluid_3 = MaterialLibAPI
            .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 4_000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UIV;

        // -------------------------------------------------------------

        // ------------------------- UMV Motor -------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { MaterialLibAPI.getStack(Materials.TengamAttuned, Shapes.stickLong, 32),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.stickLong, 16),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.round, 32),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Quantium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Motor_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UMV Electric Pump ---------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UMV.get(1L), GTOreDictUnificator.get("pipeLargeInfinity", 2L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 4),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.screw, 16),
                new Object[] { OrePrefixes.ring.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.rotor, 4),
                GTOreDictUnificator.get("cableGt04Quantium", 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Pump_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UMV Conveyor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UMV.get(2L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 2),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.round, 64),
                GTOreDictUnificator.get("cableGt04Quantium", 2L),
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.ingredient(MaterialFacades.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Conveyor_Module_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Robot Arm --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.stickLong, 8),
                GTOreDictUnificator.get("gearSpaceTime", 2L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.gearGtSmall, 6),
                ItemList.Electric_Motor_UMV.get(2L), ItemList.Electric_Piston_UMV.get(1L),
                new Object[] { Circuits.UMV.getIngredient(), 2L }, new Object[] { Circuits.UIV.getIngredient(), 4L },
                new Object[] { Circuits.UEV.getIngredient(), 8L }, GTOreDictUnificator.get("cableGt04Quantium", 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Robot_Arm_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Electric Piston --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UMV.get(1L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.ring, 8),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.round, 64),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.stick, 8),
                GTOreDictUnificator.get("gearSpaceTime", 2L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.gearGtSmall, 4),
                GTOreDictUnificator.get("cableGt04Quantium", 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Piston_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Emitter ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtSpaceTime", 1L), ItemList.Electric_Motor_UMV.get(1L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.stick, 16), ItemList.NuclearStar.get(4L),
                new Object[] { Circuits.UMV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Quantium", 7L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Emitter_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Sensor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtSpaceTime", 1L), ItemList.Electric_Motor_UMV.get(1),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 8), ItemList.NuclearStar.get(4L),
                new Object[] { Circuits.UMV.getIngredient(), 4L },
                MaterialLibAPI.getStack(Materials.CelestialTungsten, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Quantum, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.AstralTitanium, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Titansteel, Shapes.foil, 64),
                GTOreDictUnificator.get("cableGt04Quantium", 7L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Sensor_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UMV Field Generator ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UIV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GTOreDictUnificator.get("frameGtSpaceTime", 1L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.plate, 6), ItemList.NuclearStar.get(2L),
                ItemList.Emitter_UMV.get(4L), new Object[] { Circuits.UXV.getIngredient(), 4 },
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.wireFine, 64),
                GTOreDictUnificator.get("cableGt04Quantium", 8L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Field_Generator_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

    }

    private void itemPartsUXVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        FluidStack moltenMHDCSM_576 = MaterialLibAPI
            .getFluidStack(Materials.MagnetohydrodynamicallyConstrainedStarMatter, FluidShapes.fluidMolten, 4 * INGOTS);
        FluidStack moltenSpaceTime_576 = MaterialLibAPI
            .getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 4 * INGOTS);
        FluidStack moltenUniversium_576 = MaterialLibAPI
            .getFluidStack(Materials.Universium, FluidShapes.fluidMolten, 4 * INGOTS);
        FluidStack dimensionallyShiftedSuperfluid8000 = MaterialLibAPI
            .getFluidStack(Materials.dimensionallyshiftedsuperfluid, FluidShapes.fluidLiquid, 8_000);
        FluidStack solderingAlloy_14_400 = MaterialUtils
            .anyFluid(Materials.MutatedLivingSolder, 1 * STACKS + 36 * INGOTS);

        int totalComputation = 384_000;
        int compPerSecond = 512;
        int researchEuPerTick = 64_000_000;
        int researchAmperage = 2;

        int craftingTimeInTicks = 1000;
        int craftingEuPerTick = (int) TierEU.RECIPE_UMV;

        // -------------------------------------------------------------

        // ------------------------- UXV Motor -------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new ItemStack[] { ItemList.EnergisedTesseract.get(1),
                GTOreDictUnificator.get("stickLongMagnetohydrodynamicallyConstrainedStarMatter", 16L),
                GTOreDictUnificator.get("ringMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                GTOreDictUnificator.get("roundMagnetohydrodynamicallyConstrainedStarMatter", 32L),

                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.wireFine, 64),

                GTOreDictUnificator.get("wireFineMagnetohydrodynamicallyConstrainedStarMatter", 64L),
                GTOreDictUnificator.get("wireFineMagnetohydrodynamicallyConstrainedStarMatter", 64L),

                MaterialLibAPI.getStack(Materials.Universium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.wireFine, 64),

                GTOreDictUnificator.get("wireFineMagmatter", 64L), GTOreDictUnificator.get("wireFineMagmatter", 64L),

                GTOreDictUnificator.get("wireGt04SpaceTime", 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576,
                dimensionallyShiftedSuperfluid8000 },
            ItemList.Electric_Motor_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // --------------------- UXV Electric Pump ---------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { ItemList.Electric_Motor_UXV.get(1L), GTOreDictUnificator.get("pipeLargeSpaceTime", 2L),
                GTOreDictUnificator.get("plateMagnetohydrodynamicallyConstrainedStarMatter", 4L),
                GTOreDictUnificator.get("screwMagnetohydrodynamicallyConstrainedStarMatter", 16L),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.ring, 64),
                GTOreDictUnificator.get("ringRadoxPoly", 64L),
                GTOreDictUnificator.get("rotorMagnetohydrodynamicallyConstrainedStarMatter", 4L),
                GTOreDictUnificator.get("rotorMagmatter", 4L), GTOreDictUnificator.get("wireGt04SpaceTime", 4L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576,
                dimensionallyShiftedSuperfluid8000 },
            ItemList.Electric_Pump_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ----------------------- UXV Conveyor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { ItemList.Electric_Motor_UXV.get(2L),
                GTOreDictUnificator.get("plateMagnetohydrodynamicallyConstrainedStarMatter", 2L),
                GTOreDictUnificator.get("ringMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                GTOreDictUnificator.get("roundMagnetohydrodynamicallyConstrainedStarMatter", 64L),
                GTOreDictUnificator.get("wireGt04SpaceTime", 4L),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.plate, 64),
                MaterialLibAPI.getStack(Materials.Kevlar, Shapes.plate, 16),
                GTOreDictUnificator.get("plateRadoxPoly", 64L), GTOreDictUnificator.get("plateRadoxPoly", 16L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576,
                dimensionallyShiftedSuperfluid8000 },
            ItemList.Conveyor_Module_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Robot Arm --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { GTOreDictUnificator.get("stickLongMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                GTOreDictUnificator.get("gearMagnetohydrodynamicallyConstrainedStarMatter", 2L),
                GTOreDictUnificator.get("gearGtMagmatter", 2L),
                GTOreDictUnificator.get("gearGtSmallMagnetohydrodynamicallyConstrainedStarMatter", 6L),
                GTOreDictUnificator.get("gearGtSmallMagmatter", 6L), ItemList.Electric_Motor_UXV.get(2L),
                ItemList.Electric_Piston_UXV.get(1L), new Object[] { Circuits.UXV.getIngredient(), 2L },
                new Object[] { Circuits.UMV.getIngredient(), 4L }, new Object[] { Circuits.UIV.getIngredient(), 8L },
                GTOreDictUnificator.get("wireGt04SpaceTime", 12L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 8) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576,
                dimensionallyShiftedSuperfluid8000 },
            ItemList.Robot_Arm_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Electric Piston --------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new ItemStack[] { ItemList.Electric_Motor_UXV.get(1L),
                GTOreDictUnificator.get("plateMagnetohydrodynamicallyConstrainedStarMatter", 6L),
                GTOreDictUnificator.get("ringMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                GTOreDictUnificator.get("roundMagnetohydrodynamicallyConstrainedStarMatter", 64L),
                GTOreDictUnificator.get("stickMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                GTOreDictUnificator.get("gearMagnetohydrodynamicallyConstrainedStarMatter", 2L),
                GTOreDictUnificator.get("gearGtMagmatter", 2L),
                GTOreDictUnificator.get("gearGtSmallMagnetohydrodynamicallyConstrainedStarMatter", 4L),
                GTOreDictUnificator.get("gearGtSmallMagmatter", 4L), GTOreDictUnificator.get("wireGt04SpaceTime", 8L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576,
                dimensionallyShiftedSuperfluid8000 },
            ItemList.Electric_Piston_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Emitter ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { GTOreDictUnificator.get("frameGtMagnetohydrodynamicallyConstrainedStarMatter", 1L),
                ItemList.Electric_Motor_UXV.get(1L),
                GTOreDictUnificator.get("stickMagnetohydrodynamicallyConstrainedStarMatter", 16L),
                ItemList.NuclearStar.get(16), new Object[] { Circuits.UXV.getIngredient(), 4L },
                GTOreDictUnificator.get("foilMagnetohydrodynamicallyConstrainedStarMatter", 64),
                GTOreDictUnificator.get("foilMagmatter", 64L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.foil, 64),
                GTOreDictUnificator.get("wireGt04SpaceTime", 14L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 8)

            },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Emitter_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Sensor ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { GTOreDictUnificator.get("frameGtMagnetohydrodynamicallyConstrainedStarMatter", 1L),
                ItemList.Electric_Motor_UXV.get(1L),
                GTOreDictUnificator.get("plateMagnetohydrodynamicallyConstrainedStarMatter", 8L),
                ItemList.NuclearStar.get(16), new Object[] { Circuits.UXV.getIngredient(), 4L },
                GTOreDictUnificator.get("foilMagnetohydrodynamicallyConstrainedStarMatter", 64),
                GTOreDictUnificator.get("foilMagmatter", 64L),
                MaterialLibAPI.getStack(Materials.SpaceTime, Shapes.foil, 64),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.foil, 64),
                GTOreDictUnificator.get("wireGt04SpaceTime", 14L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 8) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Sensor_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // ---------------------------------------------------------------------

        // ------------------------ UXV Field Generator ------------------------

        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UMV.get(1),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { GTOreDictUnificator.get("frameGtMagnetohydrodynamicallyConstrainedStarMatter", 1L),
                GTOreDictUnificator.get("plateMagnetohydrodynamicallyConstrainedStarMatter", 6L),
                ItemList.NuclearStar.get(64L), ItemList.Emitter_UXV.get(4L),
                new Object[] { Circuits.MAX.getIngredient(), 4 },

                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.SuperconductorUMVBase, Shapes.wireFine, 64),
                GTOreDictUnificator.get("wireFineMagnetohydrodynamicallyConstrainedStarMatter", 64L),
                GTOreDictUnificator.get("wireFineMagnetohydrodynamicallyConstrainedStarMatter", 64L),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.wireFine, 64),
                MaterialLibAPI.getStack(Materials.Universium, Shapes.wireFine, 64),
                GTOreDictUnificator.get("wireFineMagmatter", 64L), GTOreDictUnificator.get("wireFineMagmatter", 64L),

                GTOreDictUnificator.get("wireGt04SpaceTime", 16L),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 12) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Field_Generator_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // ---------------------------------------------------------------------

    }

    private void addBeamcraftingRecipes() {

        ItemStack neutronAcceleratorUV = NeutronAccelerators[8].copy();
        neutronAcceleratorUV.stackSize = 8;

        // LHC Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            LanthItemList.SYNCHROTRON,
            256_000,
            256,
            (int) TierEU.RECIPE_UV,
            32,
            new Object[] { CustomItemList.Machine_Multi_Research.get(8), neutronAcceleratorUV,
                new ItemStack(compactFusionCoil, 64, 1), new ItemStack(compactFusionCoil, 32, 2),
                new Object[] { Circuits.UHV.getIngredient(), 64L }, ItemList.Field_Generator_UV.get(64),
                ItemList.Electromagnet_Samarium.get(1), ItemList.CMSCasing.get(8) },
            new FluidStack[] {
                MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 64 * 9 * 8 * 144),
                MaterialLibAPI.getFluidStack(Materials.CosmicNeutronium, FluidShapes.fluidMolten, 64 * 9 * 8 * 144) },
            ItemList.LargeHadronCollider.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UHV);

        // Advanced Beam Output Hatch
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            LanthItemList.LUV_BEAMLINE_OUTPUT_HATCH,
            128_000,
            256,
            (int) TierEU.RECIPE_UV,
            4,
            new Object[] { LanthItemList.LUV_BEAMLINE_OUTPUT_HATCH, ItemList.Sensor_UV.get(4),
                ItemList.Emitter_UV.get(4), ItemList.ActivatedCarbonFilterMesh.get(32), },
            new FluidStack[] { MaterialUtils.fluid(Materials.UUMatter, 1000) },
            ItemList.AdvancedBeamlineOutputHatch.get(1),
            60 * SECONDS,
            (int) TierEU.RECIPE_UV);

        // Beamcrafter controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            LanthItemList.TARGET_CHAMBER,
            64_000,
            128,
            (int) TierEU.RECIPE_ZPM,
            4,
            new Object[] { LanthItemList.TARGET_CHAMBER, ItemList.Field_Generator_ZPM.get(4),
                new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_CASING, 32), ItemList.LargeMolecularAssembler.get(2) },
            new FluidStack[] { MaterialUtils.fluid(Materials.UUMatter, 8000) },
            ItemList.BeamCrafter.get(1),
            60 * SECONDS,
            (int) TierEU.RECIPE_UV);

        // Beamcrafter splitter
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Machine_Multi_Switch.get(1),
            64_000,
            128,
            (int) TierEU.RECIPE_UV,
            4,
            new Object[] { CustomItemList.Machine_Multi_Switch.get(1), ItemList.Electromagnet_Samarium.get(1),
                new ItemStack(LanthItemList.SHIELDED_ACCELERATOR_CASING, 4), },
            new FluidStack[] { MaterialLibAPI.getFluidStack(Materials.SuperCoolant, FluidShapes.fluidLiquid, 16000) },
            ItemList.BeamSplitter.get(1),
            60 * SECONDS,
            (int) TierEU.RECIPE_UV);

    }

    private void addBecCasingRecipes() {
        // Electromagnetically-Isolated Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.BlockQuarkContainmentCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemList.BlockQuarkContainmentCasing.get(6),
                MaterialLibAPI.getStack(Materials.Tairitsu, TEBlockShapes.frameGt, 6),
                GTOreDictUnificator.get("frameGtChuritsu", 6), GTOreDictUnificator.get("frameGtShijima", 6),
                GTModHandler.getModItem(NewHorizonsCoreMod.ID, "IrradiantReinforcedBedrockiumPlate", 8),
                GTOreDictUnificator.get("foilprotohalkonite", 32),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.plate, 8),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.screw, 64),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.bolt, 64),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.bolt, 24),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.ring, 12),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.screw, 24), ItemList.EnergisedTesseract.get(2),
                ItemList.Field_Generator_UEV.get(1) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 384 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 32 * STACKS) },
            ItemList.ElectromagneticallyIsolatedCasing.get(4),
            30 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Coherence-Preserving Plasma Conduit
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.PeaceEnforcementCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemList.PeaceEnforcementCasing.get(6), Casings.ParticleBeamGuidancePipeCasing.toStack(6),
                GTOreDictUnificator.get("pipeHugeTranscendentMetal", 6), ItemList.Electromagnet_Tengam.get(6),
                GTOreDictUnificator.get("wireGt04SuperconductorUIV", 6),
                GTOreDictUnificator.get("stickprotohalkonite", 6),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.foil, 24),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.ring, 12),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 48),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.rotor, 1),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.stickLong, 3),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.rotor, 1),
                MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.stickLong, 6),
                ItemList.Electric_Pump_UIV.get(2) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 384 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS) },
            ItemList.SuperconductivePlasmaEnergyConduit.get(4),
            60 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Fine-structure Constant Manipulator
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.ConflictInducementCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemList.ConflictInducementCasing.get(6), ItemList.ElectromagneticallyIsolatedCasing.get(2),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.gearGtSmall, 3),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.gearGt, 2),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.gearGtSmall, 3), ItemList.UIV_Coil.get(6),
                GTOreDictUnificator.get("stickLongprotohalkonite", 6),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.wireFine, 48),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.foil, 24),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 48),
                GTModHandler.getModItem(Railcraft.ID, "machine.eta", 6, 8),
                GTOreDictUnificator.get("ringprotohalkonite", 12),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.gearGtSmall, 6),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 48), ItemList.Sensor_UIV.get(2),
                ItemList.Emitter_UIV.get(2) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 384 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 48 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS) },
            ItemList.FineStructureConstantManipulator.get(4),
            30 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Condensate Guidance Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.UIV_Coil.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemRefer.Field_Restriction_Coil_T3.get(2),
                ItemList.Naquarite_Universal_Insulator_Foil.get(6), ItemList.UIV_Coil.get(3),
                GTOreDictUnificator.get("stickLongprotohalkonite", 3),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.wireFine, 24),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.foil, 12),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 24),
                GTOreDictUnificator.get("frameGtChuritsu", 2),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.screw, 32),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.bolt, 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 96 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 12 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 12 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 4 * INGOTS) },
            ItemList.CondensateGuidanceCoil.get(1),
            15 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Condensate Transformative Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.CondensateGuidanceCoil.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemList.CondensateGuidanceCoil.get(1),
                GTOreDictUnificator.get("wireGt02SuperconductorUIV", 6), ItemList.Circuit_Chip_APIC.get(6),
                ItemList.Sensor_UHV.get(12), MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.plate, 12),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.itemCasing, 6),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 64), ItemList.Field_Generator_UHV.get(3),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.stickLong, 6),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.ring, 3),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.plateDouble, 6) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 96 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 12 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 12 * INGOTS),
                MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 4 * INGOTS) },
            ItemList.CondensateTransformativeCoil.get(1),
            30 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Electromagnetic Waveguide
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemRefer.AntimatterContainmentCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { ItemRefer.AntimatterContainmentCasing.get(6),
                GTOreDictUnificator.get("stickLongprotohalkonite", 6),
                MaterialLibAPI.getStack(Materials.Tairitsu, Shapes.wireFine, 24),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.foil, 12),
                MaterialLibAPI.getStack(Materials.Shijima, Shapes.screw, 24),
                MaterialLibAPI.getStack(Materials.Churitsu, Shapes.itemCasing, 6),
                ItemList.Field_Generator_UEV.get(2) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.ChromaticGlass, 768 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 192 * INGOTS),
                MaterialUtils.anyFluid(Materials.CelestialTungsten, 192 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SpaceTime, FluidShapes.fluidMolten, 24 * INGOTS) },
            ItemList.ElectromagneticWaveguide.get(4),
            120 * SECONDS,
            (int) TierEU.RECIPE_UIV);
    }

    private void addGodforgeRecipes() {
        if (EternalSingularity.isModLoaded()) {
            // Controller
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Godforge_StellarEnergySiphonCasing.get(1),
                48_000_000,
                8_192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { CustomItemList.Godforge_StellarEnergySiphonCasing.get(4), ItemList.ZPM4.get(2),
                    ItemList.Casing_Dim_Bridge.get(64), getModItem(EternalSingularity.ID, "eternal_singularity", 32L),
                    MaterialLibAPI.getStack(Materials.Mellion, Shapes.plateDense, 16),
                    MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.plateDense, 16),
                    MaterialLibAPI.getStack(Materials.Creon, Shapes.plateDense, 16),
                    MaterialLibAPI.getStack(Materials.MetastableOganesson, Shapes.plateDense, 16),
                    ItemList.StableBosonContainmentUnit.get(8),
                    GTOreDictUnificator.get("wireGt16SuperconductorUIV", 16), ItemList.Sensor_UIV.get(32),
                    new Object[] { Circuits.UIV.getIngredient(), 64L }, CustomItemList.eM_energyTunnel7_UIV.get(1),
                    ItemRegistry.energyDistributor[11] },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 32 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 8_192_000),
                    MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 4 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 32 * STACKS) },
                CustomItemList.Machine_Multi_ForgeOfGods.get(1),
                300 * SECONDS,
                (int) TierEU.RECIPE_UMV);
        }

        // Magnetic Confinement Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get("frameGtTranscendentMetal", 1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 8),
                MaterialLibAPI.getStack(Materials.MagnetoResonatic, BlockShapes.block, 16),
                MaterialLibAPI.getStack(Materials.TengamAttuned, Shapes.plateDense, 32),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.plate, 16),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.screw, 8),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.screw, 8),
                ItemList.SuperconductorComposite.get(1), ItemList.Emitter_UIV.get(2),
                ItemList.Electromagnet_Tengam.get(1) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidPlasma, 16 * INGOTS) },
            CustomItemList.Godforge_MagneticConfinementCasing.get(8),
            50 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        if (GalacticraftAmunRa.isModLoaded()) {
            // Structure Casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                48_000_000,
                8_192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { GTOreDictUnificator.get("frameGtMellion", 16),
                    GTOreDictUnificator.get("frameGtSixPhasedCopper", 16),
                    GTOreDictUnificator.get("frameGtTranscendentMetal", 8),
                    MaterialLibAPI.getStack(Materials.AstralTitanium, TEBlockShapes.frameGt, 8),
                    MaterialLibAPI.getStack(Materials.Creon, Shapes.plate, 6),
                    ItemList.StableBosonContainmentUnit.get(1), ItemList.Field_Generator_UEV.get(2),
                    // Artificial Gravity Generator
                    getModItem(GalacticraftAmunRa.ID, "tile.machines3", 4L, 1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, 2 * INGOTS) },
                CustomItemList.Godforge_BoundlessStructureCasing.get(1),
                10 * SECONDS,
                (int) TierEU.RECIPE_UIV);
        }

        // Guidance Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Godforge_BoundlessStructureCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_BoundlessStructureCasing.get(1), ItemList.ZPM2.get(1),
                GregtechItemList.CosmicFabricManipulator.get(1), ItemList.Field_Generator_UEV.get(2),
                ItemList.Emitter_UIV.get(3), MaterialLibAPI.getStack(Materials.Creon, Shapes.plate, 6),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.gearGt, 8),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.gearGtSmall, 8) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Thorium, FluidShapes.fluidPlasma, 2 * INGOTS) },
            CustomItemList.Godforge_GuidanceCasing.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Energy Siphon Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Godforge_GuidanceCasing.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_BoundlessStructureCasing.get(1),
                ItemList.Casing_Coil_Hypogen.get(64), ItemList.Casing_Coil_Hypogen.get(64),
                GTOreDictUnificator.get("wireGt08SuperconductorUIV", 32), ItemList.neutroniumHeatCapacitor.get(1L),
                ItemList.neutroniumHeatCapacitor.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L), CustomItemList.eM_energyTunnel7_UIV.get(1),
                ItemList.Generator_Plasma_UV.get(64), MaterialLibAPI.getStack(Materials.Creon, Shapes.plateDense, 6),
                MaterialLibAPI.getStack(Materials.Hypogen, Shapes.plate, 6) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperconductorUIVBase, FluidShapes.fluidMolten, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 128_000) },
            CustomItemList.Godforge_StellarEnergySiphonCasing.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Gravitational Lens
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            new ItemStack(BlockQuantumGlass.INSTANCE, 1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { new ItemStack(BlockQuantumGlass.INSTANCE, 8),
                new ItemStack(ItemRegistry.bw_glasses[0], 8, 8), GregtechItemList.ForceFieldGlass.get(8),
                ItemList.StableBosonContainmentUnit.get(4), getNHCoreModItem("RadoxPolymerLens", 6),
                getNHCoreModItem("ChromaticLens", 6), getNHCoreModItem("MysteriousCrystalLens", 6),
                MaterialLibAPI.getStack(Materials.MagnetoResonatic, Shapes.lens, 6),
                MaterialLibAPI.getStack(Materials.ChromaticGlass, Shapes.plateDense, 36),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.stickLong, 6),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.stickLong, 6),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.stickLong, 6) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.Rhugnor, 16 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Creon, FluidShapes.fluidMolten, 16 * INGOTS),
                MaterialUtils.anyFluid(Materials.AdvancedNitinol, 16 * STACKS) },
            new ItemStack(BlockGodforgeGlass.INSTANCE, 1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Graviton Modulator 1
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Battery_Gem_4.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_MagneticConfinementCasing.get(2),
                ItemRefer.Field_Restriction_Coil_T3.get(1), MaterialLibAPI.getStack(Materials.Creon, Shapes.plate, 16),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.gearGtSmall, 8),
                GregtechItemList.Battery_Gem_4.get(2), GregtechItemList.Laser_Lens_Special.get(4),
                ItemList.Emitter_UIV.get(4), new Object[] { Circuits.UEV.getIngredient(), 16L },
                GTOreDictUnificator.get("naniteSilver", 2) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.SuperconductorUIVBase, FluidShapes.fluidMolten, 32 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.Infinity, FluidShapes.fluidMolten, 32 * INGOTS) },
            CustomItemList.Godforge_GravitonFlowModulatorTier1.get(2),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Phonon Transmission Conduit
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Relativistic_Heat_Capacitor.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GTOreDictUnificator.get("frameGtTranscendentMetal", 1),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.stickLong, 12),
                new ItemStack(Blocks.tfftStorageField, 1, 9), ItemList.Tesseract.get(8),
                ItemList.Relativistic_Heat_Capacitor.get(4), ItemList.Thermal_Superconductor.get(6),
                ItemList.Field_Generator_UEV.get(4),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.bolt, 24) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 1 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 1_000),
                MaterialLibAPI.getFluidStack(Materials.Plutonium241, FluidShapes.fluidPlasma, 16 * INGOTS) },
            CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Smelting Module Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            // mega ebf controller
            ItemList.ExothermicHearth.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4), ItemList.ExothermicHearth.get(64),
                ItemList.Machine_Multi_Furnace.get(64), ItemList.ZPM4.get(1),
                GTOreDictUnificator.get("wireGt16SuperconductorUIV", 16), ItemList.Robot_Arm_UIV.get(16),
                ItemList.Conveyor_Module_UIV.get(32),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.plateDense, 8),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.plateDense, 8),
                new Object[] { Circuits.UIV.getIngredient(), 32L } },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.Lead, FluidShapes.fluidPlasma, 4 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 16 * STACKS) },
            CustomItemList.Machine_Multi_SmeltingModule.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // Molten Module Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Mega_AlloyBlastSmelter.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4), ItemList.ExothermicHearth.get(64),
                GregtechItemList.Mega_AlloyBlastSmelter.get(64), ItemList.ZPM4.get(1),
                GTOreDictUnificator.get("wireGt16SuperconductorUIV", 32), ItemList.Robot_Arm_UIV.get(16),
                ItemList.Conveyor_Module_UIV.get(32), ItemList.Electric_Pump_UIV.get(64),
                ItemList.Relativistic_Heat_Capacitor.get(8),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.plateDense, 16),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.plateDense, 8),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.plateDense, 8),
                new Object[] { Circuits.UIV.getIngredient(), 32L } },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 32_000),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 16 * STACKS) },
            CustomItemList.Machine_Multi_MoltenModule.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // Plasma Module Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.FluidHeaterUIV.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4), ItemList.FluidHeaterUIV.get(64),
                GregtechItemList.FusionComputer_UV3.get(8), ItemList.ZPM4.get(1),
                GTOreDictUnificator.get("wireGt16SuperconductorUIV", 32), ItemList.Robot_Arm_UIV.get(16),
                ItemList.Conveyor_Module_UIV.get(32), ItemList.Electric_Pump_UIV.get(64),
                ItemList.Relativistic_Heat_Capacitor.get(8),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.plateSuperdense, 2),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.plateSuperdense, 1),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.plateSuperdense, 1),
                new Object[] { Circuits.UIV.getIngredient(), 32L } },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 32_000),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 16 * STACKS) },
            CustomItemList.Machine_Multi_PlasmaModule.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // Exotic Module Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4),
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(4), ItemRefer.Compact_Fusion_MK5.get(1),
                ItemList.ZPM4.get(4), GTOreDictUnificator.get("wireGt16SuperconductorUIV", 64),
                ItemList.Robot_Arm_UIV.get(16), ItemList.Conveyor_Module_UIV.get(32),
                ItemList.Electric_Pump_UIV.get(64), CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(8),
                MaterialLibAPI.getStack(Materials.SixPhasedCopper, Shapes.plateSuperdense, 4),
                MaterialLibAPI.getStack(Materials.Creon, Shapes.plateSuperdense, 2),
                MaterialLibAPI.getStack(Materials.Mellion, Shapes.plateSuperdense, 2),
                new Object[] { Circuits.UIV.getIngredient(), 64L } },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 16 * STACKS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_048_000),
                MaterialLibAPI.getFluidStack(Materials.PhononMedium, FluidShapes.fluidLiquid, 64_000),
                MaterialLibAPI.getFluidStack(Materials.TranscendentMetal, FluidShapes.fluidMolten, 16 * STACKS) },
            CustomItemList.Machine_Multi_QuarkGluonPlasmaModule.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UMV);
    }

    private void addWirelessEnergyRecipes() {
        int recipeDurationTicks = 20 * 20;
        int recipeEuPerTick = (int) TierEU.RECIPE_UMV;

        int researchEuPerTick = (int) TierEU.RECIPE_UMV;
        int researchAmperage = 4;
        int compPerSecond = 2000;
        int totalComputation = 500_000;

        ItemStack[] energyHatches = { ItemList.Hatch_Energy_ULV.get(1), ItemList.Hatch_Energy_LV.get(1),
            ItemList.Hatch_Energy_MV.get(1), ItemList.Hatch_Energy_HV.get(1), ItemList.Hatch_Energy_EV.get(1),
            ItemList.Hatch_Energy_IV.get(1), ItemList.Hatch_Energy_LuV.get(1), ItemList.Hatch_Energy_ZPM.get(1),
            ItemList.Hatch_Energy_UV.get(1), ItemList.Hatch_Energy_UHV.get(1), ItemList.Hatch_Energy_UEV.get(1L),
            ItemList.Hatch_Energy_UIV.get(1L), ItemList.Hatch_Energy_UMV.get(1L), ItemList.Hatch_Energy_UXV.get(1L) };

        ItemStack[] energyHatches_4A = { CustomItemList.eM_energyMulti4_EV.get(1),
            CustomItemList.eM_energyMulti4_IV.get(1), CustomItemList.eM_energyMulti4_LuV.get(1),
            CustomItemList.eM_energyMulti4_ZPM.get(1), CustomItemList.eM_energyMulti4_UV.get(1),
            CustomItemList.eM_energyMulti4_UHV.get(1), CustomItemList.eM_energyMulti4_UEV.get(1),
            CustomItemList.eM_energyMulti4_UIV.get(1), CustomItemList.eM_energyMulti4_UMV.get(1),
            CustomItemList.eM_energyMulti4_UXV.get(1) };

        ItemStack[] energyHatches_16A = { CustomItemList.eM_energyMulti16_EV.get(1),
            CustomItemList.eM_energyMulti16_IV.get(1), CustomItemList.eM_energyMulti16_LuV.get(1),
            CustomItemList.eM_energyMulti16_ZPM.get(1), CustomItemList.eM_energyMulti16_UV.get(1),
            CustomItemList.eM_energyMulti16_UHV.get(1), CustomItemList.eM_energyMulti16_UEV.get(1),
            CustomItemList.eM_energyMulti16_UIV.get(1), CustomItemList.eM_energyMulti16_UMV.get(1),
            CustomItemList.eM_energyMulti16_UXV.get(1) };

        ItemStack[] energyHatches_64A = { CustomItemList.eM_energyMulti64_EV.get(1),
            CustomItemList.eM_energyMulti64_IV.get(1), CustomItemList.eM_energyMulti64_LuV.get(1),
            CustomItemList.eM_energyMulti64_ZPM.get(1), CustomItemList.eM_energyMulti64_UV.get(1),
            CustomItemList.eM_energyMulti64_UHV.get(1), CustomItemList.eM_energyMulti64_UEV.get(1),
            CustomItemList.eM_energyMulti64_UIV.get(1), CustomItemList.eM_energyMulti64_UMV.get(1),
            CustomItemList.eM_energyMulti64_UXV.get(1) };

        ItemStack[] laserTargets_UXV = { CustomItemList.eM_energyTunnel1_UXV.get(1),
            CustomItemList.eM_energyTunnel2_UXV.get(1), CustomItemList.eM_energyTunnel3_UXV.get(1),
            CustomItemList.eM_energyTunnel4_UXV.get(1), CustomItemList.eM_energyTunnel5_UXV.get(1),
            CustomItemList.eM_energyTunnel6_UXV.get(1), CustomItemList.eM_energyTunnel7_UXV.get(1),
            CustomItemList.eM_energyTunnel8_UXV.get(1), CustomItemList.eM_energyTunnel9_UXV.get(1) };

        Object[] circuitsTierPlusOne = { new Object[] { Circuits.LV.getIngredient(), 4L },
            new Object[] { Circuits.MV.getIngredient(), 4L }, new Object[] { Circuits.HV.getIngredient(), 4L },
            new Object[] { Circuits.EV.getIngredient(), 4L }, new Object[] { Circuits.IV.getIngredient(), 4L },
            new Object[] { Circuits.LuV.getIngredient(), 4L }, new Object[] { Circuits.ZPM.getIngredient(), 4L },
            new Object[] { Circuits.UV.getIngredient(), 4L }, new Object[] { Circuits.UHV.getIngredient(), 4L },
            new Object[] { Circuits.UEV.getIngredient(), 4L }, new Object[] { Circuits.UIV.getIngredient(), 4L },
            new Object[] { Circuits.UMV.getIngredient(), 4L }, new Object[] { Circuits.UXV.getIngredient(), 4L },
            new Object[] { Circuits.MAX.getIngredient(), 4L } };

        ItemStack[] wirelessHatches = { ItemList.Wireless_Hatch_Energy_ULV.get(1),
            ItemList.Wireless_Hatch_Energy_LV.get(1), ItemList.Wireless_Hatch_Energy_MV.get(1),
            ItemList.Wireless_Hatch_Energy_HV.get(1), ItemList.Wireless_Hatch_Energy_EV.get(1),
            ItemList.Wireless_Hatch_Energy_IV.get(1), ItemList.Wireless_Hatch_Energy_LuV.get(1),
            ItemList.Wireless_Hatch_Energy_ZPM.get(1), ItemList.Wireless_Hatch_Energy_UV.get(1),
            ItemList.Wireless_Hatch_Energy_UHV.get(1), ItemList.Wireless_Hatch_Energy_UEV.get(1),
            ItemList.Wireless_Hatch_Energy_UIV.get(1), ItemList.Wireless_Hatch_Energy_UMV.get(1),
            ItemList.Wireless_Hatch_Energy_UXV.get(1) };

        ItemStack[] wirelessHatches_4A = { CustomItemList.eM_energyWirelessMulti4_EV.get(1),
            CustomItemList.eM_energyWirelessMulti4_IV.get(1), CustomItemList.eM_energyWirelessMulti4_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti4_ZPM.get(1), CustomItemList.eM_energyWirelessMulti4_UV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UHV.get(1), CustomItemList.eM_energyWirelessMulti4_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UIV.get(1), CustomItemList.eM_energyWirelessMulti4_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti4_UXV.get(1) };

        ItemStack[] wirelessHatches_16A = { CustomItemList.eM_energyWirelessMulti16_EV.get(1),
            CustomItemList.eM_energyWirelessMulti16_IV.get(1), CustomItemList.eM_energyWirelessMulti16_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti16_ZPM.get(1), CustomItemList.eM_energyWirelessMulti16_UV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UHV.get(1), CustomItemList.eM_energyWirelessMulti16_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UIV.get(1), CustomItemList.eM_energyWirelessMulti16_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti16_UXV.get(1) };

        ItemStack[] wirelessHatches_64A = { CustomItemList.eM_energyWirelessMulti64_EV.get(1),
            CustomItemList.eM_energyWirelessMulti64_IV.get(1), CustomItemList.eM_energyWirelessMulti64_LuV.get(1),
            CustomItemList.eM_energyWirelessMulti64_ZPM.get(1), CustomItemList.eM_energyWirelessMulti64_UV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UHV.get(1), CustomItemList.eM_energyWirelessMulti64_UEV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UIV.get(1), CustomItemList.eM_energyWirelessMulti64_UMV.get(1),
            CustomItemList.eM_energyWirelessMulti64_UXV.get(1) };

        ItemStack[] wirelessLasers = { CustomItemList.eM_energyWirelessTunnel1_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel2_UXV.get(1), CustomItemList.eM_energyWirelessTunnel3_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel4_UXV.get(1), CustomItemList.eM_energyWirelessTunnel5_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel6_UXV.get(1), CustomItemList.eM_energyWirelessTunnel7_UXV.get(1),
            CustomItemList.eM_energyWirelessTunnel8_UXV.get(1), CustomItemList.eM_energyWirelessTunnel9_UXV.get(1) };

        // ------------------------ Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                (i == 0) ? ItemList.Tesseract.get(1) : wirelessHatches[i - 1],
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { energyHatches[i], new ItemStack(compactFusionCoil, 1),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(2), GTOreDictUnificator.get("wireGt01SpaceTime", 2),
                    MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateDense, 1), circuitsTierPlusOne[i],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 9 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 500) },
                wirelessHatches[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 4A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_4A.length; i++) {

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_4A[i],
                totalComputation * 4,
                compPerSecond * 4,
                researchEuPerTick,
                researchAmperage * 2,
                new Object[] { energyHatches_4A[i], new ItemStack(compactFusionCoil, 1, 1),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(4), GTOreDictUnificator.get("wireGt01SpaceTime", 4),
                    GTOreDictUnificator.get("plateTripleShirabon", 4L),
                    GTOreDictUnificator.get("plateDenseFlerovium_GT5U", 4), circuitsTierPlusOne[i + 4],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 2_000) },
                wirelessHatches_4A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 16A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_16A.length; i++) {

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_16A[i],
                totalComputation * 16,
                compPerSecond * 16,
                researchEuPerTick,
                researchAmperage * 4,
                new Object[] { energyHatches_16A[i], new ItemStack(compactFusionCoil, 1, 2),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(16), GTOreDictUnificator.get("wireGt01SpaceTime", 16),
                    GTOreDictUnificator.get("plateTripleShirabon", 16L),
                    MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plateDense, 4),
                    circuitsTierPlusOne[i + 4], ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 2 * STACKS + 16 * INGOTS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 8_000) },
                wirelessHatches_16A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 64A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_64A.length; i++) {

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_64A[i],
                totalComputation * 64,
                compPerSecond * 64,
                researchEuPerTick,
                researchAmperage * 8,
                new Object[] { energyHatches_64A[i], new ItemStack(compactFusionCoil, 1, 3),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(64), GTOreDictUnificator.get("wireGt01SpaceTime", 64),
                    GTOreDictUnificator.get("plateTripleShirabon", 64L),
                    GTOreDictUnificator.get("plateDenseMetastableOganesson", 4), circuitsTierPlusOne[i + 4],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 9 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTEC, FluidShapes.fluidLiquid, 32_000) },
                wirelessHatches_64A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ Wireless UXV Lasers ------------------------

        for (int i = 0; i < wirelessLasers.length; i++) {

            TTRecipeAdder.addResearchableAssemblylineRecipe(
                i != 7 ? laserTargets_UXV[i] : CustomItemList.eM_energyWirelessTunnel7_UXV.get(1),
                // If current laser registered is UXV 4MA, switch research item to avoid research conflict
                totalComputation * 64,
                compPerSecond * 64,
                researchEuPerTick * 4,
                researchAmperage * 16,
                new Object[] { laserTargets_UXV[i], new ItemStack(compactFusionCoil, 1, 4),
                    ItemList.DysonSwarmDeploymentUnitMagnet.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(64), GTOreDictUnificator.get("wireGt16SpaceTime", 64),
                    MaterialLibAPI.getStack(Materials.Eternity, Shapes.plateDense, 32),
                    GTOreDictUnificator.get("plateDenseMagnetohydrodynamicallyConstrainedStarMatter", 16),
                    new Object[] { Circuits.MAX.getIngredient(), 8L }, ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, 32_000) },
                wirelessLasers[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 4MA+ Lasers ------------------------

        // 4M UMV Target
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel7_UMV.get(1),
            totalComputation * 48,
            compPerSecond * 48,
            researchEuPerTick * 3,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UMV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Sensor_UMV.get(64),
                ItemList.Sensor_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GTOreDictUnificator.get("wireGt16Quantium", 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * STACKS) },
            CustomItemList.eM_energyTunnel8_UMV.get(1),
            100 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // 4M UXV Target
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel8_UMV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get("wireGt16BlackPlutonium", 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * STACKS) },
            CustomItemList.eM_energyTunnel8_UXV.get(1),
            100 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 16M UXV Target
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel8_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get("wireGt16BlackPlutonium", 64) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 72 * STACKS) },
            CustomItemList.eM_energyTunnel9_UXV.get(1),
            110 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 4M UMV Source
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel7_UMV.get(1),
            totalComputation * 48,
            compPerSecond * 48,
            researchEuPerTick * 3,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UMV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Emitter_UMV.get(64),
                ItemList.Emitter_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GTOreDictUnificator.get("wireGt16Quantium", 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * STACKS) },
            CustomItemList.eM_dynamoTunnel8_UMV.get(1),
            100 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // 4M UXV Source
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel7_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get("wireGt16BlackPlutonium", 32) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 36 * STACKS) },
            CustomItemList.eM_dynamoTunnel8_UXV.get(1),
            100 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 16M UXV Source
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel8_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64),
                MaterialLibAPI.getStack(Materials.Diamond, Shapes.lens, 64), ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get("wireGt16BlackPlutonium", 64) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 72 * STACKS) },
            CustomItemList.eM_dynamoTunnel9_UXV.get(1),
            110 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // ------------------------ Wireless EU dynamos ------------------------

        // Wireless Dynamo Powerful
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel8_UMV.get(1),
            24_000_000,
            3200,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { eM_dynamoTunnel5_UMV.get(1), ItemList.ZPM3.get(1),
                new Object[] { Circuits.UMV.getIngredient(), 4L }, ItemList.Field_Generator_UMV.get(1),
                GTOreDictUnificator.get("wireGt04SpaceTime", 16), CustomItemList.Machine_Multi_Transformer.get(1) },
            new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 20 * INGOTS),
                MaterialLibAPI.getFluidStack(Materials.ExcitedDTSC, FluidShapes.fluidLiquid, 8000),
                MaterialLibAPI.getFluidStack(Materials.Shirabon, FluidShapes.fluidMolten, (int) (20 * INGOTS)) },
            CustomItemList.eM_dynamoWirelessMulti.get(1),
            30 * SECONDS,
            (int) TierEU.RECIPE_UMV);

    }

    public void runLateRecipes() {
        if (EternalSingularity.isModLoaded()) {
            // Shielding Casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get("blockTranscendentMetal", 1),
                48_000_000,
                8_192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { GTOreDictUnificator.get("frameGtSixPhasedCopper", 4),
                    MaterialLibAPI.getStack(Materials.Infinity, Shapes.plateSuperdense, 2),
                    MaterialLibAPI.getStack(Materials.Quantum, Shapes.plate, 16),
                    GTOreDictUnificator.get("frameGtInfinityCatalyst", 4),
                    MaterialLibAPI.getStack(Materials.Netherite, Shapes.plateSuperdense, 2),
                    getModItem(EternalSingularity.ID, "combined_singularity", 1L, 2),
                    MaterialLibAPI.getStack(Materials.SuperconductorUIVBase, Shapes.stickLong, 8),
                    MaterialLibAPI.getStack(Materials.Creon, Shapes.plate, 16),
                    MaterialLibAPI.getStack(Materials.Mellion, Shapes.plate, 16),
                    MaterialLibAPI.getStack(Materials.SuperconductorUEVBase, Shapes.stickLong, 8),
                    getModItem(EternalSingularity.ID, "combined_singularity", 1L, 4),
                    MaterialLibAPI.getStack(Materials.TranscendentMetal, Shapes.plateSuperdense, 2),
                    MaterialLibAPI.getStack(Materials.Titansteel, TEBlockShapes.frameGt, 4),
                    GTOreDictUnificator.get("plateprotohalkonite", 16),
                    MaterialLibAPI.getStack(Materials.CosmicNeutronium, Shapes.plateSuperdense, 2),
                    MaterialLibAPI.getStack(Materials.AbyssalAlloy, TEBlockShapes.frameGt, 4) },
                new FluidStack[] { MaterialUtils.anyFluid(Materials.MutatedLivingSolder, 2 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Bedrockium, FluidShapes.fluidMolten, 256 * STACKS),
                    MaterialUtils.anyFluid(Materials.CelestialTungsten, 32 * STACKS),
                    MaterialLibAPI.getFluidStack(Materials.Neutronium, FluidShapes.fluidMolten, 32 * STACKS) },
                CustomItemList.Godforge_SingularityShieldingCasing.get(4),
                30 * SECONDS,
                (int) TierEU.RECIPE_UIV);
        }
    }
}
