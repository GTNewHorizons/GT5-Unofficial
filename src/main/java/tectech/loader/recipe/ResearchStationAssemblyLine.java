package tectech.loader.recipe;

import static com.google.common.math.LongMath.pow;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static goodgenerator.loader.Loaders.yottaFluidTankCell;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.AvaritiaAddons;
import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EtFuturumRequiem;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GalacticraftAmunRa;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SGCraft;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkersGregworks;
import static gregtech.api.util.GTModHandler.getModItem;
import static gregtech.api.util.GTRecipeBuilder.INGOTS;
import static gregtech.api.util.GTRecipeBuilder.MINUTES;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.STACKS;
import static gtPlusPlus.core.material.MaterialsAlloy.CINOBITE;
import static gtPlusPlus.core.material.MaterialsAlloy.LAFIUM;
import static gtPlusPlus.core.material.MaterialsAlloy.LAURENIUM;
import static gtPlusPlus.core.material.MaterialsAlloy.PIKYONIUM;
import static gtPlusPlus.core.material.MaterialsAlloy.QUANTUM;
import static gtPlusPlus.core.material.MaterialsAlloy.TITANSTEEL;
import static gtPlusPlus.core.material.MaterialsAlloy.TRINIUM_REINFORCED_STEEL;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.ASTRAL_TITANIUM;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.CHRONOMATIC_GLASS;
import static gtPlusPlus.core.material.MaterialsElements.STANDALONE.HYPOGEN;
import static kekztech.common.Blocks.lscLapotronicEnergyUnit;
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

import com.rwtema.extrautils.ExtraUtils;

import bartworks.common.loaders.ItemRegistry;
import bartworks.system.material.WerkstoffLoader;
import fox.spiteful.avaritia.compat.ticon.Tonkers;
import goodgenerator.items.GGMaterial;
import goodgenerator.util.ItemRefer;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.material.MaterialMisc;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.core.material.MaterialsElements;
import gtPlusPlus.core.material.Particle;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import kekztech.common.Blocks;
import kekztech.common.TileEntities;
import tconstruct.tools.TinkerTools;
import tectech.recipe.TTRecipeAdder;
import tectech.thing.CustomItemList;
import tectech.thing.block.BlockGodforgeGlass;
import tectech.thing.block.BlockQuantumGlass;
import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

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

        if (TinkersGregworks.isModLoaded() && Avaritia.isModLoaded() // Infinity, Cosmic Neutronium
            && ExtraUtilities.isModLoaded() // Bedrockium
        ) {
            addEOHRecipes();
        }

        // Infinite Oil Rig
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.OilDrill4.get(1),
            16777216,
            2048,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.OilDrill4.get(1),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L }, ItemList.Electric_Motor_UHV.get(4),
                ItemList.Electric_Pump_UHV.get(4), GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
                ItemList.Sensor_UHV.get(3), ItemList.Field_Generator_UHV.get(3),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 12) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9 * INGOTS),
                Materials.Neutronium.getMolten(4 * INGOTS) },
            ItemList.OilDrillInfinite.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Infinity Coil
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_AwakenedDraconium.get(1),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.Infinity, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 8),
                ItemList.Naquarite_Universal_Insulator_Foil.get(24) },
            new FluidStack[] { Materials.DraconiumAwakened.getMolten(576), },
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
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                GTOreDictUnificator.get("wireGt02Hypogen", 8L), HYPOGEN.getScrew(8),
                ItemList.Naquarite_Universal_Insulator_Foil.get(32) },
            new FluidStack[] { Materials.Infinity.getMolten(576), },
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
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SpaceTime, 8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 8),
                getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                ItemList.Naquarite_Universal_Insulator_Foil.get(64) },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 576), },
            ItemList.Casing_Coil_Eternal.get(1),
            60 * 20,
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
                    new Object[] { ItemList.Hull_MAX.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(2L), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 2L },
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
                        MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS) },
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
                    new Object[] { ItemList.Hull_UEV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(32_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                        Materials.UUMatter.getFluid(8_000) },
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
                    new Object[] { ItemList.Hull_UIV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                        ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(16_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                        Materials.UUMatter.getFluid(16_000) },
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
                    new Object[] { ItemList.Hull_UMV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2L },
                        ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(32_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS),
                        Materials.UUMatter.getFluid(32_000) },
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
                    new Object[] { ItemList.Hull_UXV.get(1L),
                        GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 4L),
                        ItemList.Circuit_Chip_QPIC.get(16L),
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 2L }, ItemList.UHV_Coil.get(32L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(64_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS + 16 * INGOTS),
                        Materials.UUMatter.getFluid(64_000) },
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
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUHVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(2L), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 2L },
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
                        MaterialsAlloy.INDALLOY_140.getFluidStack(40 * INGOTS) },
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
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUEVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { GTModHandler.getIC2Coolant(32_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                        Materials.UUMatter.getFluid(8_000) },
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
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUIVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                        ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                        Materials.UUMatter.getFluid(16_000L) },
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
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2L },
                        ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(32_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS),
                        Materials.UUMatter.getFluid(32_000) },
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
                        GTOreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 16L),
                        ItemList.Circuit_Chip_QPIC.get(16L),
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 2L }, ItemList.UHV_Coil.get(32L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(64_000),
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS + 16 * INGOTS),
                        Materials.UUMatter.getFluid(64_000) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
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
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 8L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 4L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 2L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                GTModHandler.getIC2Coolant(10_000), Materials.Radon.getGas(2_500), },
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
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32L),
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS),
                Materials.BioMediumSterilized.getFluid(10 * INGOTS), Materials.SuperCoolant.getFluid(10_000), },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4L),
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
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.BioMediumSterilized.getFluid(20 * INGOTS), Materials.SuperCoolant.getFluid(20_000L), },
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
            new Object[] { ItemList.Circuit_Board_Optical.get(1L), ItemList.Circuit_OpticalProcessor.get(2L),
                ItemList.Circuit_Parts_InductorXSMD.get(16L), ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                ItemList.Circuit_Parts_ResistorXSMD.get(20L), ItemList.Circuit_Chip_NOR.get(32L),
                ItemList.Circuit_Chip_Ram.get(64L),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.wireFine), 24, 10101), // Fine
                // Lumiium
                // Wire
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(10 * INGOTS),
                Materials.Radon.getPlasma(10 * INGOTS), Materials.SuperCoolant.getFluid(10_000L),
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
            new Object[] { ItemList.Circuit_Board_Optical.get(2L), ItemList.Circuit_OpticalAssembly.get(2L),
                ItemList.Circuit_Parts_TransistorXSMD.get(24L), ItemList.Circuit_Parts_ResistorXSMD.get(24L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(24L), ItemList.Circuit_Parts_DiodeXSMD.get(24L),
                ItemList.Circuit_Chip_NOR.get(64L), ItemList.Circuit_Chip_SoC2.get(32L),
                new ItemStack(WerkstoffLoader.items.get(OrePrefixes.wireFine), 32, 10101), // Fine
                // Lumiium
                // Wire
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64), },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.Radon.getPlasma(20 * INGOTS), Materials.SuperCoolant.getFluid(20_000),
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 8),
                ItemList.Circuit_OpticalComputer.get(2L), ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L), ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L), ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(40 * INGOTS),
                Materials.Radon.getPlasma(40 * INGOTS), Materials.SuperCoolant.getFluid(40_000),
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
                CHRONOMATIC_GLASS.getPlateDense(1), ItemList.Circuit_Chip_QPIC.get(2L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L } },
            new FluidStack[] { Materials.Grade4PurifiedWater.getFluid(4_000) },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32L }, ItemList.Electric_Pump_UEV.get(16),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.ProtoHalkonite, 64),

                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.ProtoHalkonite, 16),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.ProtoHalkonite, 64),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.ProtoHalkonite, 64),
                ItemList.EnergisedTesseract.get(32),

                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L), },
            new FluidStack[] { Materials.ExcitedDTCC.getFluid(2_048_000), Materials.ExcitedDTPC.getFluid(2_048_000),
                Materials.ExcitedDTRC.getFluid(2_048_000), Materials.ExcitedDTEC.getFluid(2_048_000), },
            ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
            5 * MINUTES,
            (int) TierEU.RECIPE_UIV);

        // Stargate Recipes
        if (EternalSingularity.isModLoaded() && SGCraft.isModLoaded()) {

            // spotless:off

            final int baseStargateTime = 125_000 * SECONDS;

            // Stargate-Radiation-Containment-Plate
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 1L),
                (int) TierEU.RECIPE_MAX,
                32768,
                (int) TierEU.RECIPE_UXV,
                64,
                new Object[] {
                    CustomItemList.Godforge_MagneticConfinementCasing.get(64),
                    CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                    CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(32),
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.MagMatter, 64L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MagMatter, 8L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Universium, 8L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Eternity, 8L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SpaceTime, 8L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 16L },
                    ItemList.Sensor_UXV.get(16L),
                    ItemList.Emitter_UXV.get(16L),
                    getModItem(EternalSingularity.ID, "combined_singularity", 64, 15), // chronic singularity
                    Materials.Universium.getNanite(16),
                    Materials.BlackDwarfMatter.getNanite(16),
                    Materials.WhiteDwarfMatter.getNanite(16),
                    Materials.SixPhasedCopper.getNanite(16) },
                new FluidStack[] {
                    Materials.QuarkGluonPlasma.getFluid(1_024_000),
                    Materials.PhononMedium.getFluid(256_000),
                    Materials.SuperconductorUMVBase.getMolten(64 * STACKS),
                    Materials.ExcitedDTSC.getFluid(512_000) },
                getNHCoreModItem("StargateShieldingFoil", 1),
                baseStargateTime,
                (int) TierEU.RECIPE_UXV);

            // Stargate Chevron
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                getNHCoreModItem("StargateShieldingFoil", 1),
                (int) TierEU.RECIPE_MAX,
                32768,
                (int) TierEU.RECIPE_UXV,
                64,
                new Object[] {
                    CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                    CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),
                    new ItemStack(BlockGodforgeGlass.INSTANCE, 64),
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.MagMatter, 64L),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MagMatter, 16L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MagMatter, 8L),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MHDCSM, 8L),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 16L),
                    GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 64L),
                    GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Jasper, 64L),
                    GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Opal, 64L),
                    GTOreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Sapphire, 64L),
                    ItemList.Electric_Motor_UXV.get(64L),
                    ItemList.Electric_Piston_UXV.get(64L),
                    ItemList.Field_Generator_UXV.get(16L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 32L } },
                new FluidStack[] {
                    Materials.QuarkGluonPlasma.getFluid(1_024_000L),
                    Materials.PhononMedium.getFluid(256_000L),
                    Materials.MagMatter.getMolten(128 * STACKS),
                    Materials.ExcitedDTSC.getFluid(512_000L) },
                getNHCoreModItem("StargateChevron", 1),
                baseStargateTime,
                (int) TierEU.RECIPE_UXV);

            // Stargate Frame Part
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                (int) TierEU.RECIPE_MAX,
                32_768,
                (int) TierEU.RECIPE_UXV,
                64,
                new ItemStack[] {
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Mellion, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Universium, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Eternity, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUMVBase, 64L),
                    GGMaterial.shirabon.get(OrePrefixes.stickLong, 64),
                    HYPOGEN.getLongRod(64),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SixPhasedCopper, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MHDCSM, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.ProtoHalkonite, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.WhiteDwarfMatter, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MagMatter, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.BlackDwarfMatter, 64L),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 64L) },
                new FluidStack[] {
                    Materials.QuarkGluonPlasma.getFluid(1_024_000L),
                    Materials.PhononMedium.getFluid(256_000L),
                    Materials.Universium.getMolten(16 * STACKS),
                    Materials.ExcitedDTSC.getFluid(512_000L) },
                getNHCoreModItem("StargateFramePart", 1),
                baseStargateTime,
                (int) TierEU.RECIPE_UXV);

            // spotless:on
        }

        // Dimensionally Transcendent Plasma Forge (DTPF)
        if (EternalSingularity.isModLoaded()) {

            // DTPF Controller.
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Bridge.get(1),
                32_000_000,
                4096,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { ItemList.Casing_Dim_Bridge.get(4),
                    GTUtility.copyAmount(16, ItemRegistry.megaMachines[0]), ItemList.Hatch_Energy_UEV.get(4L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 6),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 20L }, ItemList.Field_Generator_UEV.get(4),
                    getModItem(EternalSingularity.ID, "eternal_singularity", 4L),
                    GregtechItemList.Laser_Lens_Special.get(1),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Osmiridium, 4L),
                    ItemList.Electric_Pump_UEV.get(4), ItemList.ZPM3.get(1),
                    getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 128_000),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(8 * STACKS),
                    new FluidStack(FluidRegistry.getFluid("molten.californium"), 4 * STACKS),
                    Materials.NaquadahEnriched.getMolten(4 * STACKS) },
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
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2L },
                    getModItem(Avaritia.ID, "Singularity", 2L, 0),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 6),
                    getModItem(NewHorizonsCoreMod.ID, "PicoWafer", 2, 0), ItemList.Field_Generator_UHV.get(1L) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 8_000),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                    Materials.NaquadahEnriched.getMolten(9 * INGOTS) },
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), LAURENIUM.getScrew(12),
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 2L },
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
                    ItemList.Super_Chest_IV.get(1), ItemList.Super_Tank_IV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "PicoWafer", 1, 0), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 1_000),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * INGOTS),
                    Materials.NaquadahEnriched.getMolten(2 * INGOTS) },
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                    LAURENIUM.getScrew(12), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * INGOTS),
                    Materials.NaquadahEnriched.getMolten(1 * INGOTS) },
                ItemList.Casing_Dim_Trans.get(1),
                20 * 20,
                (int) TierEU.RECIPE_UIV);

            // Transdimensional Alignment Matrix
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.MicroTransmitter_UV.get(1),
                32_000_000,
                8192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    GregtechItemList.SpaceTimeContinuumRipper.get(4), ItemList.Robot_Arm_UMV.get(64),
                    ItemList.Sensor_UMV.get(16), ItemList.Field_Generator_UMV.get(4), ItemList.ZPM5.get(1),
                    ItemList.EnergisedTesseract.get(32),
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.TranscendentMetal, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 64),
                    GGMaterial.metastableOganesson.get(OrePrefixes.plateDense, 32) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * STACKS),
                    Materials.Lead.getPlasma(1 * STACKS), Materials.Space.getMolten(1 * STACKS) },
                ItemList.Transdimensional_Alignment_Matrix.get(1),
                50 * SECONDS,
                (int) TierEU.RECIPE_UMV);
        }

        // Deep Dark Portal
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            getModItem(NewHorizonsCoreMod.ID, "HeavyDutyPlateTier8", 1, 0),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UHV,
            64,
            new Object[] { getModItem(ExtraUtilities.ID, "cobblestone_compressed", 1, 7),
                getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0),
                GTOreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4 },
                getModItem(NewHorizonsCoreMod.ID, "PicoWafer", 32, 0), ItemList.Robot_Arm_UIV.get(4),
                ItemList.Emitter_UIV.get(4), ItemList.Sensor_UIV.get(4), },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 50_000),
                Materials.Infinity.getMolten(8 * STACKS), Materials.Cheese.getMolten(232_000L), },
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 64L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L }, ItemList.Energy_Cluster.get(8L),
                    ItemList.Field_Generator_UV.get(2), ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Parts_DiodeASMD.get(32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 32), },
                new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 64L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L }, ItemList.ZPM2.get(8),
                    ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                    ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                    ItemList.Circuit_Parts_DiodeASMD.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64), },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                    Materials.Naquadria.getMolten(1 * STACKS), GTModHandler.getIC2Coolant(32_000) },
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 64L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L }, ItemList.ZPM3.get(8),
                    ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                    ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64), },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                    Materials.Quantium.getMolten(2 * STACKS), Materials.Naquadria.getMolten(2 * STACKS),
                    Materials.SuperCoolant.getFluid(64_000) },
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
                new Object[] { HYPOGEN.getPlateDouble(64), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4 },
                    ItemList.ZPM4.get(8L), ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "RawPicoWafer", 64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                    GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * STACKS),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(2 * STACKS),
                    Materials.Quantium.getMolten(2 * STACKS), Materials.SuperCoolant.getFluid(128_000) },
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
                new Object[] { MaterialsElements.STANDALONE.DRAGON_METAL.getPlateDouble(64),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4 }, ItemList.ZPM5.get(8L),
                    ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                    ItemList.Circuit_Wafer_QPIC.get(64),
                    GTModHandler.getModItem(NewHorizonsCoreMod.ID, "PicoWafer", 64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * STACKS),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(4 * STACKS),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(4 * STACKS),
                    Materials.SuperCoolant.getFluid(256_000) },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Neutronium, 1),
                ItemList.Field_Generator_UHV.get(2), ItemList.Circuit_Wafer_QPIC.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
            new FluidStack[] { Materials.UUMatter.getFluid(50_000), CINOBITE.getFluidStack(1 * STACKS),
                MaterialsAlloy.OCTIRON.getFluidStack(1 * STACKS),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(1 * STACKS), },
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
            new Object[] { ItemList.Energy_LapotronicOrb2.get(16L),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8L },
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8), ItemList.Emitter_UHV.get(1),
                ItemList.Sensor_UHV.get(1), ItemList.Casing_Fusion_Coil.get(1L), },
            new FluidStack[] { Materials.UUMatter.getFluid(8_000), CINOBITE.getFluidStack(16 * INGOTS),
                MaterialsAlloy.OCTIRON.getFluidStack(16 * INGOTS),
                MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(16 * INGOTS), },
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
                new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.EV), 16L },
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 8L },
                    getModItem(EtFuturumRequiem.ID, "netherite_block", 2),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                    ItemList.Electric_Motor_UHV.get(2), ItemList.Electric_Piston_UHV.get(1),
                    ItemList.Casing_Fusion2.get(1L), },
                new FluidStack[] { Materials.UUMatter.getFluid(1_000), CINOBITE.getFluidStack(4 * INGOTS),
                    MaterialsAlloy.OCTIRON.getFluidStack(4 * INGOTS),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFluidStack(4 * INGOTS), },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                GTOreDictUnificator.get("plateDenseMetastableOganesson", 4), ItemList.Field_Generator_UEV.get(2),
                getNHCoreModItem("PicoWafer", 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32) },
            new FluidStack[] { MaterialsElements.getInstance().CURIUM.getFluidStack(1 * STACKS),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(1 * STACKS),
                MaterialsAlloy.ABYSSAL.getFluidStack(1 * STACKS),
                MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(1 * STACKS) },
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
            new Object[] { ItemList.Energy_Module.get(16), new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L },
                MaterialsElements.STANDALONE.RHUGNOR.getPlate(8), ItemList.Emitter_UEV.get(1),
                ItemList.Sensor_UEV.get(1), new ItemStack(compactFusionCoil, 1, 2) },
            new FluidStack[] { MaterialsElements.getInstance().NEPTUNIUM.getFluidStack(16 * INGOTS),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(16 * INGOTS),
                MaterialsAlloy.ABYSSAL.getFluidStack(16 * INGOTS),
                MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(16 * INGOTS) },
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
                new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.IV), 16L },
                    new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8L },
                    getModItem(EtFuturumRequiem.ID, "netherite_block", 8),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlate(8), ItemList.Electric_Motor_UEV.get(2),
                    ItemList.Electric_Piston_UEV.get(1), GregtechItemList.Casing_Fusion_External.get(1L) },
                new FluidStack[] { MaterialsElements.getInstance().FERMIUM.getFluidStack(8 * INGOTS),
                    MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(8 * INGOTS),
                    MaterialsAlloy.ABYSSAL.getFluidStack(8 * INGOTS),
                    MaterialsElements.STANDALONE.DRAGON_METAL.getFluidStack(8 * INGOTS) },
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.GaiaSpirit, 1L),
                    ItemList.Casing_Coil_AwakenedDraconium.get(8L), ItemList.Electric_Motor_UHV.get(8L),
                    ItemList.Robot_Arm_UHV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                    ItemList.Gravistar.get(4, new Object() {}), getModItem(Thaumcraft.ID, "ItemEldritchObject", 1, 3),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 29),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 28),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Void, 1) },
                new FluidStack[] { MaterialsAlloy.INDALLOY_140.getFluidStack(20 * INGOTS),
                    Materials.Void.getMolten(20 * INGOTS), Materials.DraconiumAwakened.getMolten(10 * INGOTS), },
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 }, },
                new FluidStack[] { Materials.Sunnarium.getMolten(1 * STACKS + 36 * INGOTS),
                    Materials.Void.getMolten(3 * STACKS + 8 * INGOTS), },
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 }, },
                new FluidStack[] { Materials.Neutronium.getMolten(14400L), Materials.Void.getMolten(57600L), },
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 1L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L } },
                new FluidStack[] { Materials.Infinity.getMolten(14400L), Materials.Void.getMolten(115200L), },
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 4L),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 }, },
                new FluidStack[] { Materials.SpaceTime.getMolten(1 * STACKS + 36 * INGOTS),
                    Materials.Void.getMolten(25 * STACKS), },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L }, ItemList.Energy_LapotronicOrb2.get(1L),
                ItemList.Duct_Tape.get(64L), ItemList.Duct_Tape.get(64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L), },
            new FluidStack[] { Materials.Lubricant.getFluid(256_000),
                MaterialsAlloy.INDALLOY_140.getFluidStack(9 * INGOTS), },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2 }, CustomItemList.DATApipe.get(64),
                CustomItemList.DATApipe.get(64), ItemList.Cover_Screen.get(1) },
            new FluidStack[] { Materials.Iridium.getMolten(1 * STACKS + 36 * INGOTS),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18 * INGOTS),
                new FluidStack(MaterialsElements.getInstance().NEPTUNIUM.getPlasma(), 20_000),
                new FluidStack(MaterialsElements.getInstance().FERMIUM.getPlasma(), 20_000) },
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
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.MagMatter, 1L),
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 8L),
                GGMaterial.shirabon.get(OrePrefixes.plateDense, 64) },
            new FluidStack[] { Materials.Space.getMolten(1 * STACKS),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS), Materials.MHDCSM.getMolten(16) },
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
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 1L),
                ItemList.Field_Generator_UMV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GGMaterial.shirabon.get(OrePrefixes.wireFine, 64),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Flerovium, 16L), },
            new FluidStack[] { new FluidStack(CHRONOMATIC_GLASS.getPlasma(), 1 * STACKS),
                MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                Materials.Eternity.getMolten(1 * STACKS) },
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

        FluidStack fluid_0 = Materials.Naquadria.getMolten(18 * INGOTS);
        FluidStack fluid_1 = MaterialsAlloy.INDALLOY_140.getFluidStack(18 * INGOTS);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4_000);

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
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 4L),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 32L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
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
            new Object[] { ItemList.Electric_Motor_UHV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 32L },
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 2L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40L } },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 6L),
                ItemList.Electric_Motor_UHV.get(2L), ItemList.Electric_Piston_UHV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8L },
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 6L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                ItemList.Electric_Motor_UHV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L), ItemList.Gravistar.get(8L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                ItemList.Electric_Motor_UHV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 8L), ItemList.Gravistar.get(8L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L), ItemList.Gravistar.get(4L),
                ItemList.Emitter_UHV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8L) },
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

        FluidStack fluid_0 = Materials.Quantium.getMolten(18 * INGOTS);
        FluidStack fluid_1 = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18 * INGOTS);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4_000);

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
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 8L),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 16L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 32L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
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
            new Object[] { ItemList.Electric_Motor_UEV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 2L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 2L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 6L),
                ItemList.Electric_Motor_UEV.get(2L), ItemList.Electric_Piston_UEV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8L },
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 6L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 4L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                ItemList.Electric_Motor_UEV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 16L), ItemList.Gravistar.get(16L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                ItemList.Electric_Motor_UEV.get(1), GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 8L),
                ItemList.Gravistar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L), ItemList.Gravistar.get(8L),
                ItemList.Emitter_UEV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8L) },
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

        FluidStack fluid_0 = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(4 * INGOTS);
        FluidStack fluid_1 = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18 * INGOTS);
        FluidStack fluid_2 = Materials.DimensionallyShiftedSuperfluid.getFluid(4_000);

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
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 16L),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 16L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 32L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
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
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.DraconiumAwakened, 2L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.TranscendentMetal, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 2L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TranscendentMetal, 6L),
                ItemList.Electric_Motor_UIV.get(2L), ItemList.Electric_Piston_UIV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L },
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 6L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 6L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 64L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TranscendentMetal, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 4L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                ItemList.Electric_Motor_UIV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 16L),
                ItemList.NuclearStar.get(2L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                TRINIUM_REINFORCED_STEEL.getFoil(64), LAFIUM.getFoil(64), CINOBITE.getFoil(64), PIKYONIUM.getFoil(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                ItemList.Electric_Motor_UIV.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 8L),
                ItemList.NuclearStar.get(2L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                TRINIUM_REINFORCED_STEEL.getFoil(64), LAFIUM.getFoil(64), CINOBITE.getFoil(64), PIKYONIUM.getFoil(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 6L),
                ItemList.NuclearStar.get(1L), ItemList.Emitter_UIV.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4 },
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.ProtoHalkonite, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8L) },
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

        FluidStack fluid_0 = MaterialsElements.STANDALONE.HYPOGEN.getFluidStack(4 * INGOTS);
        FluidStack fluid_1 = MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(4 * INGOTS);
        FluidStack fluid_2 = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(18 * INGOTS);
        FluidStack fluid_3 = Materials.DimensionallyShiftedSuperfluid.getFluid(4_000);

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
            new ItemStack[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 32L),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 16L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 32L), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
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
            new Object[] { ItemList.Electric_Motor_UMV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Infinity, 2L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.SpaceTime, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 64L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.SpaceTime, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 6L),
                ItemList.Electric_Motor_UMV.get(2L), ItemList.Electric_Piston_UMV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8L },
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 6L) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 64L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.SpaceTime, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.SpaceTime, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 4L),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 4L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                ItemList.Electric_Motor_UMV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.SpaceTime, 16L), ItemList.NuclearStar.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L },
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(64), QUANTUM.getFoil(64),
                ASTRAL_TITANIUM.getFoil(64), TITANSTEEL.getFoil(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                ItemList.Electric_Motor_UMV.get(1), GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 8L),
                ItemList.NuclearStar.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L },
                MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFoil(64), QUANTUM.getFoil(64),
                ASTRAL_TITANIUM.getFoil(64), TITANSTEEL.getFoil(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6L), ItemList.NuclearStar.get(2L),
                ItemList.Emitter_UMV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4 },
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                GTOreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8L) },
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

        FluidStack moltenMHDCSM_576 = Materials.MHDCSM.getMolten(4 * INGOTS);
        FluidStack moltenSpaceTime_576 = Materials.SpaceTime.getMolten(4 * INGOTS);
        FluidStack moltenUniversium_576 = Materials.Universium.getMolten(4 * INGOTS);
        FluidStack dimensionallyShiftedSuperfluid8000 = Materials.DimensionallyShiftedSuperfluid.getFluid(8_000);
        FluidStack solderingAlloy_14_400 = MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS + 36 * INGOTS);

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
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MHDCSM, 16L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.MHDCSM, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.MHDCSM, 32L),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MHDCSM, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MHDCSM, 64L),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 64L),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 4L),
                Materials.Neutronium.getNanite(4) },
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
            new Object[] { ItemList.Electric_Motor_UXV.get(1L),
                GTOreDictUnificator.get(OrePrefixes.pipeLarge, Materials.SpaceTime, 2L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 4L),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.MHDCSM, 16L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.Kevlar, 64L),
                GTOreDictUnificator.get("ringRadoxPoly", 64L),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.MHDCSM, 4L),
                GTOreDictUnificator.get(OrePrefixes.rotor, Materials.MagMatter, 4L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 4L),
                Materials.Neutronium.getNanite(4) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 2L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.MHDCSM, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.MHDCSM, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 4L), Materials.Kevlar.getPlates(64),
                Materials.Kevlar.getPlates(16), GTOreDictUnificator.get("plateRadoxPoly", 64L),
                GTOreDictUnificator.get("plateRadoxPoly", 16L), Materials.Neutronium.getNanite(4) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.MHDCSM, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.MHDCSM, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MagMatter, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MHDCSM, 6L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MagMatter, 6L),
                ItemList.Electric_Motor_UXV.get(2L), ItemList.Electric_Piston_UXV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 8L },
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 12L),
                Materials.Neutronium.getNanite(8) },
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
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 6L),
                GTOreDictUnificator.get(OrePrefixes.ring, Materials.MHDCSM, 8L),
                GTOreDictUnificator.get(OrePrefixes.round, Materials.MHDCSM, 64L),
                GTOreDictUnificator.get(OrePrefixes.stick, Materials.MHDCSM, 8L),
                GTOreDictUnificator.get(OrePrefixes.gear, Materials.MHDCSM, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.MagMatter, 2L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MHDCSM, 4L),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.MagMatter, 4L),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 8L),
                Materials.Neutronium.getNanite(4) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 1L),
                ItemList.Electric_Motor_UXV.get(1L), GTOreDictUnificator.get(OrePrefixes.stick, Materials.MHDCSM, 16L),
                ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MHDCSM, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MagMatter, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Universium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 14L),
                Materials.Neutronium.getNanite(8)

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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 1L),
                ItemList.Electric_Motor_UXV.get(1L), GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 8L),
                ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4L },
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MHDCSM, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.MagMatter, 64L),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.SpaceTime, 64),
                GTOreDictUnificator.get(OrePrefixes.foil, Materials.Universium, 64),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 14L),
                Materials.Neutronium.getNanite(8) },
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
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.MHDCSM, 1L),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.MHDCSM, 6L), ItemList.NuclearStar.get(64L),
                ItemList.Emitter_UXV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 8 },

                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MHDCSM, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MHDCSM, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 64L),
                GTOreDictUnificator.get(OrePrefixes.wireFine, Materials.MagMatter, 64L),

                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 16L),
                Materials.Neutronium.getNanite(12) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Field_Generator_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // ---------------------------------------------------------------------

    }

    private void addEOHRecipes() {
        ItemStack largeShirabonPlate = TGregUtils.newItemStack(Materials.get("Shirabon"), PartTypes.LargePlate, 1);
        ItemStack largeInfinityPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.infinityMetalId);
        ItemStack largeBedrockiumPlate = new ItemStack(TinkerTools.largePlate, 1, ExtraUtils.tcon_bedrock_material_id);
        ItemStack largeCosmicNeutroniumPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.neutroniumId);

        final FluidStack[] specialFluid = new FluidStack[] { Materials.SpaceTime.getMolten(1_440),
            Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
            Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
            Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
            Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440) };

        final ItemStack[] plateList = new ItemStack[] {
            // Dense Shirabon plate.
            GTOreDictUnificator.get("boltShirabon", 2),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 2),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 8),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 32),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 2),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 8),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 32),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.MHDCSM, 2),
            GTOreDictUnificator.get(OrePrefixes.bolt, Materials.MHDCSM, 8) };

        // EOH Controller Recipe.
        {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_PlasmaForge.get(1),
                512_000_000, // total comp
                2 * 16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                64, // amperage
                new Object[] { ItemList.SpaceElevatorController.get(16),
                    CustomItemList.Machine_Multi_ForgeOfGods.get(4), ItemList.Machine_Multi_PlasmaForge.get(4),

                    CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier0.get(1),

                    CustomItemList.Machine_Multi_Computer.get(64), ItemList.AcceleratorUV.get(64),
                    ItemList.Quantum_Chest_IV.get(64),
                    // Void miner III.
                    GTUtility.copyAmount(64, ItemRegistry.voidminer[2]), ItemList.OilDrillInfinite.get(64),

                    ItemList.Field_Generator_UMV.get(16), ItemList.Robot_Arm_UMV.get(16), ItemList.ZPM5.get(4),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                new FluidStack[] { Materials.Time.getMolten(144_000), Materials.Space.getMolten(144_000),
                    GGMaterial.metastableOganesson.getMolten(16 * STACKS),
                    GGMaterial.shirabon.getMolten(16 * STACKS), },
                CustomItemList.Machine_Multi_EyeOfHarmony.get(1),
                400 * MINUTES,
                (int) TierEU.RECIPE_UMV);
        }

        // EOH Spatial Individual Casing
        {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.DysonSwarmDeploymentUnitCasing.get(1),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                    // Cosmic neutronium block.
                    getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48), largeBedrockiumPlate,
                    largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                    // UV Solar panel
                    ItemList.Machine_UV_SolarPanel.get(1), ItemList.Quantum_Chest_IV.get(1),
                    // Gravitation Engine
                    getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                new FluidStack[] { Materials.Neutronium.getMolten(16 * STACKS),
                    Materials.CosmicNeutronium.getMolten(16 * STACKS),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(8 * STACKS),
                    Materials.Space.getMolten(10 * INGOTS) },
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(4),
                10_000,
                (int) TierEU.RECIPE_UMV);
        }

        // EOH Spacetime Compression
        {
            // ME Digital singularity.
            final ItemStack ME_Singularity = getModItem(
                "appliedenergistics2",
                "item.ItemExtremeStorageCell.Singularity",
                1);
            final ItemStack baseCasing = CustomItemList.EOH_Reinforced_Spatial_Casing.get(1);

            int baseCompPerSec = 16_384;

            int set;
            int tier;
            int absoluteTier;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            {
                tier = 1;
                set = 1;

                absoluteTier = 0;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.EOH_Reinforced_Spatial_Casing.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T7 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Cosmic fabric manipulator
                        GregtechItemList.CosmicFabricManipulator.get(tier), ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 1;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T7 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Cosmic fabric manipulator
                        GregtechItemList.CosmicFabricManipulator.get(tier), ME_Singularity, ME_Singularity,
                        plateList[absoluteTier], new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 2;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T7 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Cosmic fabric manipulator
                        GregtechItemList.CosmicFabricManipulator.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 2;
                absoluteTier = 3;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T8 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Infinity infused manipulator
                        GregtechItemList.InfinityInfusedManipulator.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 4;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T8 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Infinity infused manipulator
                        GregtechItemList.InfinityInfusedManipulator.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 5;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T8 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Infinity infused manipulator
                        GregtechItemList.InfinityInfusedManipulator.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 3;
                absoluteTier = 6;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T9 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Spacetime continuum ripper
                        GregtechItemList.SpaceTimeContinuumRipper.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                        plateList[absoluteTier], new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 7;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T9 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Spacetime continuum ripper
                        GregtechItemList.SpaceTimeContinuumRipper.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                        plateList[absoluteTier], new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 8;
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing,
                        // T9 Yotta cell.
                        new ItemStack(yottaFluidTankCell, tier, (5 + set)),
                        // quantum tank V (max tier)
                        ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                        // Inf chest
                        getModItem(AvaritiaAddons.ID, "InfinityChest", absoluteTier + 1),
                        // Spacetime continuum ripper
                        GregtechItemList.SpaceTimeContinuumRipper.get(tier), ME_Singularity, ME_Singularity,
                        ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                        ME_Singularity, plateList[absoluteTier],
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), set } },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }
        }

        // EOH Time Dilation Field Generators.
        {
            final ItemStack baseCasing = CustomItemList.EOH_Reinforced_Temporal_Casing.get(1);

            int baseCompPerSec = 16_384;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            final ItemStack[] fusionReactors = new ItemStack[] { ItemList.FusionComputer_ZPMV.get(1),
                ItemList.FusionComputer_ZPMV.get(2), ItemList.FusionComputer_ZPMV.get(3),
                ItemList.FusionComputer_UV.get(1), ItemList.FusionComputer_UV.get(2), ItemList.FusionComputer_UV.get(3),
                // MK4 Fusion Computer.
                GregtechItemList.FusionComputer_UV2.get(1), GregtechItemList.FusionComputer_UV2.get(2),
                GregtechItemList.FusionComputer_UV2.get(3) };

            final ItemStack[] fusionCoils = new ItemStack[] { new ItemStack(compactFusionCoil, 1, 1),
                new ItemStack(compactFusionCoil, 2, 1), new ItemStack(compactFusionCoil, 3, 1),
                new ItemStack(compactFusionCoil, 1, 2), new ItemStack(compactFusionCoil, 2, 2),
                new ItemStack(compactFusionCoil, 3, 2), new ItemStack(compactFusionCoil, 1, 3),
                new ItemStack(compactFusionCoil, 2, 3), new ItemStack(compactFusionCoil, 3, 3) };

            final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

            // Spectral Components
            // Cycling should fix issues with conflicting recipes for T1-T2, T4-T5 & T7-T8
            final ItemStack[] spectralComponents = new ItemStack[] {
                // Red Spectral Component
                getModItem(SuperSolarPanels.ID, "redcomponent", 64),
                // Green Spectral Component
                getModItem(SuperSolarPanels.ID, "greencomponent", 64),
                // Blue Spectral Component
                getModItem(SuperSolarPanels.ID, "bluecomponent", 64) };

            for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {

                TTRecipeAdder.addResearchableAssemblylineRecipe(
                    researchStuff[absoluteTier],
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing, fusionReactors[absoluteTier], fusionCoils[absoluteTier],
                        // UV Solar panel
                        ItemList.Machine_UV_SolarPanel.get(absoluteTier + 1),

                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), absoluteTier + 1 },
                        // Red Spectral Component
                        spectralComponents[absoluteTier % spectralComponents.length],
                        // Green Spectral Component
                        spectralComponents[(absoluteTier + 1) % spectralComponents.length],
                        // Blue Spectral Component
                        spectralComponents[(absoluteTier + 2) % spectralComponents.length],

                        plateList[absoluteTier], ItemList.DysonSwarmDeploymentUnitCasing.get((absoluteTier + 1) * 4),
                        ItemList.DysonSwarmReceiverDish.get((absoluteTier + 1) * 4),
                        ItemList.AcceleratorUV.get((absoluteTier + 1) * 4),

                        ItemList.Energy_Module.get(absoluteTier + 1), GTOreDictUnificator
                            .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4),

                    },
                    new FluidStack[] {
                        MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                        Materials.Time.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
                    researchStuff[absoluteTier + 1],
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }

        }

        // EOH Stabilisation Field Generators.
        {
            final ItemStack baseCasing = CustomItemList.EOH_Infinite_Energy_Casing.get(1);

            int baseCompPerSec = 16_384;

            // T0 - Shirabon
            // T1 - White Dwarf Matter
            // T2 - White Dwarf Matter
            // T3 - White Dwarf Matter
            // T4 - Black Dwarf Matter
            // T5 - Black Dwarf Matter
            // T6 - Black Dwarf Matter
            // T7 - Black Dwarf Matter
            // T8 - MHDCSM.

            final ItemStack[] researchStuff = new ItemStack[] { baseCasing,
                CustomItemList.StabilisationFieldGeneratorTier0.get(1),
                CustomItemList.StabilisationFieldGeneratorTier1.get(1),
                CustomItemList.StabilisationFieldGeneratorTier2.get(1),
                CustomItemList.StabilisationFieldGeneratorTier3.get(1),
                CustomItemList.StabilisationFieldGeneratorTier4.get(1),
                CustomItemList.StabilisationFieldGeneratorTier5.get(1),
                CustomItemList.StabilisationFieldGeneratorTier6.get(1),
                CustomItemList.StabilisationFieldGeneratorTier7.get(1),
                CustomItemList.StabilisationFieldGeneratorTier8.get(1) };

            final ItemStack[] timeCasings = new ItemStack[] { CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier1.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier2.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier3.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier4.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier5.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier6.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier7.get(1),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(1) };

            final ItemStack[] spatialCasings = new ItemStack[] {
                CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1) };

            for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {

                // spotless:off
                TTRecipeAdder.addResearchableAssemblylineRecipe(
                        researchStuff[absoluteTier],
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] {
                                timeCasings[absoluteTier],
                                spatialCasings[absoluteTier],
                                baseCasing,
                                ItemList.DysonSwarmModule.get(4 * (absoluteTier + 1)),

                                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUHVBase, 4 * (absoluteTier + 1)),

                                // Gravitation Engine
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                                plateList[absoluteTier],
                                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 2 * (absoluteTier + 1) },
                                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, absoluteTier + 1),
                                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, absoluteTier + 1)


                        },
                        new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack((int) (20 * INGOTS * pow(2L, absoluteTier))),
                                Materials.Time.getMolten(1_440 * (absoluteTier + 1)),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        researchStuff[absoluteTier + 1],
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
                // spotless:on
            }

        }

        // EOH Reinforced Temporal casings
        {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                ItemList.AcceleratorUV.get(1),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(32),
                    // Cosmic neutronium block.
                    getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                    GTOreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                    GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),
                    // Large Bedrockium Plate
                    largeBedrockiumPlate, largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                    // UV Solar panel
                    ItemList.Machine_UV_SolarPanel.get(1),
                    // Ultimate Time Anomaly.
                    ItemList.AcceleratorUV.get(4),
                    // Gravitation Engine.
                    getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                new FluidStack[] { Materials.Neutronium.getMolten(16 * STACKS),
                    Materials.CosmicNeutronium.getMolten(16 * STACKS),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(8 * STACKS),
                    Materials.Time.getMolten(10 * INGOTS) },
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(4),
                10_000,
                (int) TierEU.RECIPE_UMV);
        }

        // EOH Infinite Spacetime Energy Boundary Casing
        {
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 1),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] { TileEntities.lsc.getStackForm(1),
                    // UV Solar panel
                    ItemList.Machine_UV_SolarPanel.get(1),
                    // UHV Capacitor block
                    new ItemStack(lscLapotronicEnergyUnit, 1, 5),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 4),

                    CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                    CustomItemList.eM_energyTunnel5_UMV.get(1),
                    // High Energy Flow Circuit.
                    getModItem(NewHorizonsCoreMod.ID, "HighEnergyFlowCircuit", 64, 0),

                    // Metastable Oganesson Plate.
                    GTOreDictUnificator.get("plateMetastableOganesson", 6),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.BlueTopaz, 6),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 6),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 6),

                    // Metastable Oganesson Screw.
                    GTOreDictUnificator.get("screwMetastableOganesson", 6),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.BlueTopaz, 6),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.CallistoIce, 6),
                    GTOreDictUnificator.get(OrePrefixes.screw, Materials.Ledox, 6), },

                new FluidStack[] { Materials.Neutronium.getMolten(64 * STACKS),
                    Materials.CosmicNeutronium.getMolten(64 * STACKS),
                    MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * STACKS),
                    Materials.SpaceTime.getMolten(16_000) },
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                10_000,
                (int) TierEU.RECIPE_UMV);
        }

        // Astral Array Fabricator
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1),
            480_000_000,
            32_768,
            (int) TierEU.RECIPE_MAX,
            64,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.WhiteDwarfMatter, 8),
                GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.BlackDwarfMatter, 8),
                ItemList.EnergisedTesseract.get(32),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Eternity, 16),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(64),
                CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(10),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(64),
                CustomItemList.TimeAccelerationFieldGeneratorTier8.get(40),
                CustomItemList.StabilisationFieldGeneratorTier8.get(48),
                CustomItemList.EOH_Infinite_Energy_Casing.get(32),
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(64),
                CustomItemList.EOH_Reinforced_Spatial_Casing.get(64), ItemList.Field_Generator_UMV.get(16) },
            new FluidStack[] { Materials.Space.getMolten(32_768L * 64), Materials.Eternity.getMolten(16_384L * 64),
                Materials.ExcitedDTSC.getFluid(8_192L * 64) },
            CustomItemList.astralArrayFabricator.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UXV);
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
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Mellion, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SixPhasedCopper, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 16),
                    GGMaterial.metastableOganesson.get(OrePrefixes.plateDense, 16),
                    new ItemStack(
                        Particle.getBaseParticle(Particle.GRAVITON)
                            .getItem(),
                        64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 16),
                    ItemList.Sensor_UIV.get(32), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 64L },
                    CustomItemList.eM_energyTunnel7_UIV.get(1), ItemRegistry.energyDistributor[11] },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * STACKS),
                    Materials.ExcitedDTEC.getFluid(8_192_000), Materials.Thorium.getPlasma(4 * STACKS),
                    Materials.TranscendentMetal.getMolten(32 * STACKS) },
                CustomItemList.Machine_Multi_ForgeOfGods.get(1),
                300 * SECONDS,
                (int) TierEU.RECIPE_UMV);

        }

        // Magnetic Confinement Casing
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 8),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.block, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TengamAttuned, 32),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                MaterialsElements.STANDALONE.HYPOGEN.getScrew(8),
                GTOreDictUnificator.get(OrePrefixes.screw, Materials.SixPhasedCopper, 8),
                ItemList.SuperconductorComposite.get(1), ItemList.Emitter_UIV.get(2),
                ItemList.Electromagnet_Tengam.get(1) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                Materials.Plutonium241.getPlasma(16 * INGOTS) },
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
                new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.Mellion, 16),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SixPhasedCopper, 16),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 8),
                    MaterialsElements.STANDALONE.ASTRAL_TITANIUM.getFrameBox(8),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 6),
                    new ItemStack(
                        Particle.getBaseParticle(Particle.GRAVITON)
                            .getItem(),
                        8),
                    ItemList.Field_Generator_UEV.get(2),
                    // Artificial Gravity Generator
                    getModItem(GalacticraftAmunRa.ID, "tile.machines3", 4L, 1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                    Materials.Lead.getPlasma(2 * INGOTS) },
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
                ItemList.Emitter_UIV.get(3), GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 6),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Mellion, 8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                Materials.Thorium.getPlasma(2 * INGOTS) },
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
                GTOreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 32),
                ItemList.neutroniumHeatCapacitor.get(1L), ItemList.neutroniumHeatCapacitor.get(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                CustomItemList.eM_energyTunnel7_UIV.get(1), ItemList.Generator_Plasma_UV.get(64),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 6),
                MaterialsElements.STANDALONE.HYPOGEN.getPlate(6) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * INGOTS),
                Materials.SuperconductorUIVBase.getMolten(32 * INGOTS), Materials.ExcitedDTEC.getFluid(128_000L) },
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
                new ItemStack(ItemRegistry.bw_glasses[1], 8, 0), GregtechItemList.ForceFieldGlass.get(8),
                new ItemStack(
                    Particle.getBaseParticle(Particle.GRAVITON)
                        .getItem(),
                    32),
                getNHCoreModItem("RadoxPolymerLens", 6), getNHCoreModItem("ChromaticLens", 6),
                getNHCoreModItem("MysteriousCrystalLens", 6),
                WerkstoffLoader.MagnetoResonaticDust.get(OrePrefixes.lens, 6),
                MaterialsElements.STANDALONE.CHRONOMATIC_GLASS.getPlateDense(36),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 6),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Mellion, 6),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SixPhasedCopper, 6) },
            new FluidStack[] { MaterialsElements.STANDALONE.RHUGNOR.getFluidStack(16 * INGOTS),
                Materials.Creon.getMolten(16 * INGOTS),
                MaterialsElements.STANDALONE.ADVANCED_NITINOL.getFluidStack(16 * STACKS) },
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
                ItemRefer.Field_Restriction_Coil_T3.get(1),
                GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                GTOreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Mellion, 8),
                GregtechItemList.Battery_Gem_4.get(2), GregtechItemList.Laser_Lens_Special.get(4),
                ItemList.Emitter_UIV.get(4), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 16L },
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 2) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(32 * INGOTS),
                Materials.SuperconductorUIVBase.getMolten(32 * INGOTS), Materials.Infinity.getMolten(32 * INGOTS) },
            CustomItemList.Godforge_GravitonFlowModulatorTier1.get(2),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        // Graviton Modulator 2
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Godforge_GravitonFlowModulatorTier1.get(1),
            96_000_000,
            16_384,
            (int) TierEU.RECIPE_UXV,
            128,
            new Object[] { CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                ItemRefer.Field_Restriction_Coil_T4.get(1),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Mellion, 4),
                GregtechItemList.Battery_Gem_4.get(4), GregtechItemList.Laser_Lens_Special.get(8),
                ItemList.Emitter_UMV.get(4), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 8L },
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 2),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 2) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                Materials.SuperconductorUMVBase.getMolten(1 * STACKS), Materials.SpaceTime.getMolten(1 * STACKS) },
            CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // Graviton Modulator 3
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.Godforge_GravitonFlowModulatorTier2.get(1),
            192_000_000,
            32_768,
            (int) TierEU.RECIPE_MAX,
            256,
            new Object[] { CustomItemList.Godforge_MagneticConfinementCasing.get(1),
                ItemRefer.Field_Restriction_Coil_T4.get(4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.gearGt, Materials.Mellion, 64),
                GregtechItemList.SpaceTimeContinuumRipper.get(8), GregtechItemList.Battery_Gem_4.get(8),
                GregtechItemList.Laser_Lens_Special.get(8), ItemList.Emitter_UXV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 8L },
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Silver, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Gold, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.SixPhasedCopper, 8),
                GTOreDictUnificator.get(OrePrefixes.nanite, Materials.Universium, 8) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(4 * STACKS),
                Materials.SuperconductorUMVBase.getMolten(4 * STACKS), Materials.WhiteDwarfMatter.getMolten(4 * STACKS),
                Materials.Eternity.getMolten(4 * STACKS) },
            CustomItemList.Godforge_GravitonFlowModulatorTier3.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // Phonon Transmission Conduit
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Relativistic_Heat_Capacitor.get(1),
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1),
                GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.Creon, 12),
                new ItemStack(Blocks.tfftStorageField, 1, 9), ItemList.Tesseract.get(8),
                ItemList.Relativistic_Heat_Capacitor.get(4), ItemList.Thermal_Superconductor.get(6),
                ItemList.Field_Generator_UEV.get(4),
                GTOreDictUnificator.get(OrePrefixes.bolt, Materials.SixPhasedCopper, 24) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(1 * STACKS),
                Materials.PhononMedium.getFluid(1_000), Materials.Plutonium241.getPlasma(16 * INGOTS) },
            CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(1),
            10 * SECONDS,
            (int) TierEU.RECIPE_UIV);

        ItemStack megaEBF = GTUtility.copyAmount(64, ItemRegistry.megaMachines[0]);

        // Smelting Module Controller
        TTRecipeAdder.addResearchableAssemblylineRecipe(
            // mega ebf controller
            ItemRegistry.megaMachines[0],
            48_000_000,
            8_192,
            (int) TierEU.RECIPE_UMV,
            64,
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4), megaEBF,
                ItemList.Machine_Multi_Furnace.get(64), ItemList.ZPM4.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 16),
                ItemList.Robot_Arm_UIV.get(16), ItemList.Conveyor_Module_UIV.get(32),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SixPhasedCopper, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Mellion, 8),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32L } },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * STACKS),
                Materials.ExcitedDTEC.getFluid(2_048_000), Materials.Lead.getPlasma(4 * STACKS),
                Materials.TranscendentMetal.getMolten(16 * STACKS) },
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
            new Object[] { CustomItemList.Godforge_SingularityShieldingCasing.get(4), megaEBF,
                GregtechItemList.Mega_AlloyBlastSmelter.get(64), ItemList.ZPM4.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 32),
                ItemList.Robot_Arm_UIV.get(16), ItemList.Conveyor_Module_UIV.get(32),
                ItemList.Electric_Pump_UIV.get(64), ItemList.Relativistic_Heat_Capacitor.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.SixPhasedCopper, 16),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Creon, 8),
                GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Mellion, 8),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32L } },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * STACKS),
                Materials.ExcitedDTEC.getFluid(2_048_000), Materials.PhononMedium.getFluid(32_000),
                Materials.TranscendentMetal.getMolten(16 * STACKS) },
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
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 32),
                ItemList.Robot_Arm_UIV.get(16), ItemList.Conveyor_Module_UIV.get(32),
                ItemList.Electric_Pump_UIV.get(64), ItemList.Relativistic_Heat_Capacitor.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SixPhasedCopper, 2),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Creon, 1),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Mellion, 1),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 32L } },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * STACKS),
                Materials.ExcitedDTEC.getFluid(2_048_000), Materials.PhononMedium.getFluid(32_000),
                Materials.TranscendentMetal.getMolten(16 * STACKS) },
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
                ItemList.ZPM4.get(4), GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUIV, 64),
                ItemList.Robot_Arm_UIV.get(16), ItemList.Conveyor_Module_UIV.get(32),
                ItemList.Electric_Pump_UIV.get(64), CustomItemList.Godforge_HarmonicPhononTransmissionConduit.get(8),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.SixPhasedCopper, 4),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Creon, 2),
                GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Mellion, 2),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 64L } },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(16 * STACKS),
                Materials.ExcitedDTEC.getFluid(2_048_000), Materials.PhononMedium.getFluid(64_000),
                Materials.TranscendentMetal.getMolten(16 * STACKS) },
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

        ItemStack[] dynamoHatches = { ItemList.Hatch_Dynamo_ULV.get(1), ItemList.Hatch_Dynamo_LV.get(1),
            ItemList.Hatch_Dynamo_MV.get(1), ItemList.Hatch_Dynamo_HV.get(1), ItemList.Hatch_Dynamo_EV.get(1),
            ItemList.Hatch_Dynamo_IV.get(1), ItemList.Hatch_Dynamo_LuV.get(1), ItemList.Hatch_Dynamo_ZPM.get(1),
            ItemList.Hatch_Dynamo_UV.get(1), ItemList.Hatch_Dynamo_UHV.get(1), ItemList.Hatch_Dynamo_UEV.get(1L),
            ItemList.Hatch_Dynamo_UIV.get(1L), ItemList.Hatch_Dynamo_UMV.get(1L), ItemList.Hatch_Dynamo_UXV.get(1L) };

        Object[] circuitsTierPlusTwo = { new Object[] { OrePrefixes.circuit.get(Materials.MV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.HV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.EV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.IV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.LuV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1L },
            new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4L }, // MAX (Technically not MAX, can be
            // changed once MAX circuits become
            // craftable)
            new Object[] { OrePrefixes.circuit.get(Materials.UXV), 16L } // MAX (Technically not MAX, can be
            // changed once MAX circuits become
            // craftable)
        };

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
                    CustomItemList.eM_Power.get(2),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1), circuitsTierPlusTwo[i],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9 * INGOTS),
                    Materials.ExcitedDTEC.getFluid(500) },
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
                    CustomItemList.eM_Power.get(4),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 4),
                    GTOreDictUnificator.get("plateTripleShirabon", 4L),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 4), circuitsTierPlusTwo[i + 4],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * INGOTS),
                    Materials.ExcitedDTEC.getFluid(2_000) },
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
                    CustomItemList.eM_Power.get(16),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 16),
                    GTOreDictUnificator.get("plateTripleShirabon", 16L),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.TranscendentMetal, 4),
                    circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * STACKS + 16 * INGOTS),
                    Materials.ExcitedDTEC.getFluid(8_000) },
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
                    CustomItemList.eM_Power.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 64),
                    GTOreDictUnificator.get("plateTripleShirabon", 64L),
                    GTOreDictUnificator.get("plateDenseMetastableOganesson", 4), circuitsTierPlusTwo[i + 4],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(9 * STACKS),
                    Materials.ExcitedDTEC.getFluid(32_000) },
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
                    CustomItemList.eM_Power.get(64),
                    GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.SpaceTime, 64),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.Eternity, 32),
                    GTOreDictUnificator.get(OrePrefixes.plateDense, Materials.MHDCSM, 16),
                    new Object[] { OrePrefixes.circuit.get(Materials.UXV), 16L }, ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * STACKS),
                    Materials.ExcitedDTSC.getFluid(32_000) },
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
            new Object[] { ItemList.Hull_UMV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UMV.get(64),
                ItemList.Sensor_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 32) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * STACKS) },
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
            new Object[] { ItemList.Hull_UXV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * STACKS) },
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
            new Object[] { ItemList.Hull_UXV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(72 * STACKS) },
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
            new Object[] { ItemList.Hull_UMV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UMV.get(64),
                ItemList.Emitter_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 32) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * STACKS) },
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
            new Object[] { ItemList.Hull_UXV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(36 * STACKS) },
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
            new Object[] { ItemList.Hull_UXV.get(1), GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GTOreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GTOreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(72 * STACKS) },
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
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L }, ItemList.Field_Generator_UMV.get(1),
                GTOreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 16),
                CustomItemList.Machine_Multi_Transformer.get(1) },
            new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(20 * INGOTS),
                Materials.ExcitedDTSC.getFluid(8000), GGMaterial.shirabon.getMolten(20 * INGOTS) },
            CustomItemList.eM_dynamoWirelessMulti.get(1),
            30 * SECONDS,
            (int) TierEU.RECIPE_UMV);

    }

    public void runLateRecipes() {
        if (EternalSingularity.isModLoaded()) {
            // Shielding Casing
            TTRecipeAdder.addResearchableAssemblylineRecipe(
                GTOreDictUnificator.get(OrePrefixes.block, Materials.TranscendentMetal, 1),
                48_000_000,
                8_192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.SixPhasedCopper, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Infinity, 2),
                    MaterialsAlloy.QUANTUM.getPlate(16),
                    GTOreDictUnificator.get(OrePrefixes.frameGt, Materials.InfinityCatalyst, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.Netherite, 2),
                    getModItem(EternalSingularity.ID, "combined_singularity", 1L, 2),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUIVBase, 8),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Creon, 16),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.Mellion, 16),
                    GTOreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUEVBase, 8),
                    getModItem(EternalSingularity.ID, "combined_singularity", 1L, 4),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.TranscendentMetal, 2),
                    MaterialsAlloy.TITANSTEEL.getFrameBox(4),
                    GTOreDictUnificator.get(OrePrefixes.plate, Materials.ProtoHalkonite, 16),
                    GTOreDictUnificator.get(OrePrefixes.plateSuperdense, Materials.CosmicNeutronium, 2),
                    MaterialsAlloy.ABYSSAL.getFrameBox(4) },
                new FluidStack[] { MaterialMisc.MUTATED_LIVING_SOLDER.getFluidStack(2 * STACKS),
                    Materials.Bedrockium.getMolten(256 * STACKS),
                    MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(32 * STACKS),
                    Materials.Neutronium.getMolten(32 * STACKS) },
                CustomItemList.Godforge_SingularityShieldingCasing.get(4),
                30 * SECONDS,
                (int) TierEU.RECIPE_UIV);
        }
    }
}
