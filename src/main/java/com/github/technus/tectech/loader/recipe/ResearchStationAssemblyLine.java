package com.github.technus.tectech.loader.recipe;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getItemContainer;
import static com.google.common.math.LongMath.pow;
import static common.Blocks.lscLapotronicEnergyUnit;
import static goodgenerator.loader.Loaders.compactFusionCoil;
import static goodgenerator.loader.Loaders.yottaFluidTankCell;
import static gregtech.api.enums.Mods.Avaritia;
import static gregtech.api.enums.Mods.AvaritiaAddons;
import static gregtech.api.enums.Mods.BloodMagic;
import static gregtech.api.enums.Mods.DraconicEvolution;
import static gregtech.api.enums.Mods.EternalSingularity;
import static gregtech.api.enums.Mods.ExtraUtilities;
import static gregtech.api.enums.Mods.GTNHIntergalactic;
import static gregtech.api.enums.Mods.GTPlusPlusEverglades;
import static gregtech.api.enums.Mods.GalaxySpace;
import static gregtech.api.enums.Mods.GraviSuite;
import static gregtech.api.enums.Mods.GregTech;
import static gregtech.api.enums.Mods.IndustrialCraft2;
import static gregtech.api.enums.Mods.NewHorizonsCoreMod;
import static gregtech.api.enums.Mods.SGCraft;
import static gregtech.api.enums.Mods.SuperSolarPanels;
import static gregtech.api.enums.Mods.Thaumcraft;
import static gregtech.api.enums.Mods.TinkersGregworks;
import static gregtech.api.util.GT_ModHandler.getModItem;
import static gregtech.api.util.GT_RecipeBuilder.INGOTS;
import static gregtech.api.util.GT_RecipeBuilder.MINUTES;
import static gregtech.api.util.GT_RecipeBuilder.SECONDS;
import static gtPlusPlus.core.material.ALLOY.ABYSSAL;
import static gtPlusPlus.core.material.ALLOY.CINOBITE;
import static gtPlusPlus.core.material.ALLOY.LAFIUM;
import static gtPlusPlus.core.material.ALLOY.LAURENIUM;
import static gtPlusPlus.core.material.ALLOY.PIKYONIUM;
import static gtPlusPlus.core.material.ALLOY.QUANTUM;
import static gtPlusPlus.core.material.ALLOY.TITANSTEEL;
import static gtPlusPlus.core.material.ALLOY.TRINIUM_REINFORCED_STEEL;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.ASTRAL_TITANIUM;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.CHRONOMATIC_GLASS;
import static gtPlusPlus.core.material.ELEMENT.STANDALONE.HYPOGEN;
import static kubatech.api.enums.ItemList.DEFCAwakenedSchematic;
import static kubatech.api.enums.ItemList.DEFCChaoticSchematic;
import static kubatech.api.enums.ItemList.DEFCDraconicSchematic;
import static kubatech.api.enums.ItemList.DEFCWyvernSchematic;
import static kubatech.api.enums.ItemList.DraconicEvolutionFusionCrafter;
import static kubatech.loaders.BlockLoader.defcCasingBlock;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.github.bartimaeusnek.bartworks.common.loaders.ItemRegistry;
import com.github.bartimaeusnek.bartworks.system.material.WerkstoffLoader;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.rwtema.extrautils.ExtraUtils;
import common.TileEntities;

import fox.spiteful.avaritia.compat.ticon.Tonkers;
import goodgenerator.items.MyMaterial;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.MaterialsBotania;
import gregtech.api.enums.MaterialsKevlar;
import gregtech.api.enums.MaterialsUEVplus;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TierEU;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import tconstruct.tools.TinkerTools;
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

        if (TinkersGregworks.isModLoaded() && Avaritia.isModLoaded() // Infinity, Cosmic Neutronium
            && ExtraUtilities.isModLoaded() // Bedrockium
        ) {
            addEOHRecipes();
        }

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        // Infinite Oil Rig
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.OilDrill4.get(1),
            16777216,
            2048,
            (int) TierEU.RECIPE_UHV,
            4,
            new Object[] { ItemList.OilDrill4.get(1),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L }, ItemList.Electric_Motor_UHV.get(4),
                ItemList.Electric_Pump_UHV.get(4),
                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4), ItemList.Sensor_UHV.get(3),
                ItemList.Field_Generator_UHV.get(3),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 12) },
            new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Neutronium.getMolten(576) },
            ItemList.OilDrillInfinite.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Infinity Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_AwakenedDraconium.get(1),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UEV,
            1,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Infinity, 8),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 8),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 32, 0) },
            new FluidStack[] { Materials.DraconiumAwakened.getMolten(576), },
            ItemList.Casing_Coil_Infinity.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UEV);

        // Hypogen Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_Infinity.get(1),
            16_777_216 * 2,
            2048 * 2,
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                GT_OreDictUnificator.get("wireGt02Hypogen", 8L), HYPOGEN.getScrew(8),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0), },
            new FluidStack[] { Materials.Infinity.getMolten(576), },
            ItemList.Casing_Coil_Hypogen.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UIV);

        // Eternal coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Coil_Hypogen.get(1),
            16_777_216 * 4,
            8_192,
            (int) TierEU.RECIPE_UMV,
            1,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, MaterialsUEVplus.SpaceTime, 8),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 8),
                getModItem(EternalSingularity.ID, "eternal_singularity", 1L),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0),
                getModItem(NewHorizonsCoreMod.ID, "item.MicaInsulatorFoil", 64, 0), },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 576), },
            ItemList.Casing_Coil_Eternal.get(1),
            60 * 20,
            (int) TierEU.RECIPE_UMV);

        // UHV-UMV Energy Hatch & Dynamo
        {
            // Energy Hatches
            {
                // Energy Hatch UHV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UV.get(1L),
                    24000,
                    16,
                    50000,
                    2,
                    new Object[] { ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
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
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                        new FluidStack(solderIndalloy, 40 * 144) },
                    ItemList.Hatch_Energy_UHV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UHV);

                // Energy Hatch UEV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UHV.get(1L),
                    48000,
                    32,
                    100000,
                    4,
                    new Object[] { ItemList.Hull_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                        new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                    ItemList.Hatch_Energy_UEV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UEV);

                // Energy Hatch UIV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UEV.get(1L),
                    96_000,
                    64,
                    200_000,
                    8,
                    new Object[] { ItemList.Hull_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                        ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L), new FluidStack(solderUEV, 20 * 144),
                        Materials.UUMatter.getFluid(16_000L) },
                    ItemList.Hatch_Energy_UIV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UIV);

                // Energy Hatch UMV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UIV.get(1L),
                    192000,
                    128,
                    400000,
                    16,
                    new Object[] { ItemList.Hull_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2), ItemList.UHV_Coil.get(16L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L), new FluidStack(solderUEV, 40 * 144),
                        Materials.UUMatter.getFluid(32000L) },
                    ItemList.Hatch_Energy_UMV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UMV);

                // Energy Hatch UXV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Energy_UMV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UXV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 4L),
                        ItemList.Circuit_Chip_QPIC.get(16L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2), ItemList.UHV_Coil.get(32L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L), new FluidStack(solderUEV, 80 * 144),
                        Materials.UUMatter.getFluid(64000L) },
                    ItemList.Hatch_Energy_UXV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UXV);
            }

            // Dynamo Hatch
            {
                // Dynamo Hatch UHV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UV.get(1L),
                    48000,
                    32,
                    100000,
                    4,
                    new Object[] { ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
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
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                        new FluidStack(solderIndalloy, 40 * 144) },
                    ItemList.Hatch_Dynamo_UHV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UHV);

                // Dynamo Hatch UEV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UHV.get(1L),
                    96000,
                    64,
                    200000,
                    8,
                    new Object[] { ItemList.Hull_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUEVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                        new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                    ItemList.Hatch_Dynamo_UEV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UEV);

                // Dynamo Hatch UIV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UEV.get(1L),
                    192_000,
                    128,
                    400_000,
                    16,
                    new Object[] { ItemList.Hull_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUIVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                        ItemList.UHV_Coil.get(8L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UIV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L), new FluidStack(solderUEV, 20 * 144),
                        Materials.UUMatter.getFluid(16_000L) },
                    ItemList.Hatch_Dynamo_UIV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UIV);

                // Dynamo Hatch UMV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UIV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UMV, 2), ItemList.UHV_Coil.get(16L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UMV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L), new FluidStack(solderUEV, 40 * 144),
                        Materials.UUMatter.getFluid(32000L) },
                    ItemList.Hatch_Dynamo_UMV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UMV);

                // Dynamo Hatch UXV
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Hatch_Dynamo_UMV.get(1L),
                    384000,
                    256,
                    800000,
                    32,
                    new Object[] { ItemList.Hull_UXV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 16L),
                        ItemList.Circuit_Chip_QPIC.get(16L),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2), ItemList.UHV_Coil.get(32L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UXV.get(1L) },
                    new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L), new FluidStack(solderUEV, 80 * 144),
                        Materials.UUMatter.getFluid(64000L) },
                    ItemList.Hatch_Dynamo_UXV.get(1L),
                    1000,
                    (int) TierEU.RECIPE_UXV);
            }
        }

        // UHV Circuit Wetwaremainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_Wetwaresupercomputer.get(1L),
            24000,
            64,
            50000,
            4,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
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
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 8L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 2L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
            new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 10000), Materials.Radon.getGas(2500L), },
            ItemList.Circuit_Wetwaremainframe.get(1L),
            2000,
            300000);

        // Bioware SuperComputer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32L),
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
            new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.BioMediumSterilized.getFluid(1440L),
                Materials.SuperCoolant.getFluid(10_000L), },
            ItemList.Circuit_Biowaresupercomputer.get(1L),
            4000,
            (int) TierEU.RECIPE_UV);

        // Bio Mainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_Biowaresupercomputer.get(1L),
            96000,
            256,
            1000000,
            16,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4L),
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
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 8L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.BioMediumSterilized.getFluid(2880L),
                Materials.SuperCoolant.getFluid(20_000L), },
            ItemList.Circuit_Biomainframe.get(1L),
            6000,
            (int) TierEU.RECIPE_UHV);

        // Optical Assembly
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
            new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.Radon.getPlasma(1440L),
                Materials.SuperCoolant.getFluid(10_000L), new FluidStack(FluidRegistry.getFluid("oganesson"), 500) },
            ItemList.Circuit_OpticalAssembly.get(1L),
            20 * 20,
            (int) TierEU.RECIPE_UHV);

        // Optical Computer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64), },
            new FluidStack[] { new FluidStack(solderUEV, 1440 * 2), Materials.Radon.getPlasma(1440L * 2),
                Materials.SuperCoolant.getFluid(10_000L * 2),
                new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 2) },
            ItemList.Circuit_OpticalComputer.get(1L),
            200 * 20,
            (int) TierEU.RECIPE_UHV);

        // Optical Mainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Circuit_OpticalComputer.get(1L),
            768_000,
            1024,
            (int) TierEU.RECIPE_UEV,
            64,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 8),
                ItemList.Circuit_OpticalComputer.get(2L), ItemList.Circuit_Parts_InductorXSMD.get(32L),
                ItemList.Circuit_Parts_TransistorXSMD.get(32L), ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                ItemList.Circuit_Parts_CapacitorXSMD.get(32L), ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                ItemList.Circuit_Chip_SoC2.get(64L),
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUIV, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, 16L) },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 1440 * 4), Materials.Radon.getPlasma(1440L * 4),
                Materials.SuperCoolant.getFluid(10_000L * 4),
                new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 4) },
            ItemList.Circuit_OpticalMainframe.get(1L),
            300 * 20,
            (int) TierEU.RECIPE_UEV);

        // Laser Vacuum Mirror
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.LASERpipe.get(1L),
            20_000,
            16,
            40_000,
            2,
            new Object[] { CustomItemList.eM_Power.get(1L), CustomItemList.LASERpipe.get(4L),
                CHRONOMATIC_GLASS.getPlateDense(1), ItemList.Circuit_Chip_QPIC.get(2L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L } },
            new FluidStack[] { Materials.Grade4PurifiedWater.getFluid(4000L) },
            CustomItemList.LASERpipeSmart.get(1L),
            10 * 20,
            (int) TierEU.RECIPE_UHV);

        // Transcendent Plasma Mixer - TPM.
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel6_UEV.get(1),
            32_000_000,
            4096,
            (int) TierEU.RECIPE_UIV,
            1,
            new Object[] { CustomItemList.eM_energyTunnel6_UEV.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 32L }, ItemList.Electric_Pump_UIV.get(16),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 64),

                GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.TranscendentMetal, 16),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 64),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 64),
                ItemList.EnergisedTesseract.get(32),

                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L), },
            new FluidStack[] { MaterialsUEVplus.ExcitedDTCC.getFluid(2_048_000),
                MaterialsUEVplus.ExcitedDTPC.getFluid(2_048_000), MaterialsUEVplus.ExcitedDTRC.getFluid(2_048_000),
                MaterialsUEVplus.ExcitedDTEC.getFluid(2_048_000), },
            ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
            5 * MINUTES,
            (int) TierEU.RECIPE_UIV);

        // Stargate Recipes
        if (EternalSingularity.isModLoaded() && SGCraft.isModLoaded()) {

            final int baseStargateTime = 125_000 * 20;

            // Stargate shield foil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                (int) TierEU.RECIPE_MAX,
                32768,
                (int) TierEU.RECIPE_UXV,
                64,
                new ItemStack[] { ItemList.Casing_Dim_Bridge.get(64),
                    CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                    GT_OreDictUnificator.get("blockShirabon", 64L), GT_OreDictUnificator.get("blockShirabon", 64L),
                    GT_OreDictUnificator.get(OrePrefixes.block, MaterialsUEVplus.SpaceTime, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.SpaceTime, 8L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Universium, 8L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8L),
                    GT_OreDictUnificator.get("plateDenseShirabon", 8L), ItemList.Sensor_UXV.get(16L),
                    ItemList.Emitter_UXV.get(16L), getModItem(EternalSingularity.ID, "eternal_singularity", 16L),
                    MaterialsUEVplus.Universium.getNanite(16), MaterialsUEVplus.BlackDwarfMatter.getNanite(16),
                    MaterialsUEVplus.WhiteDwarfMatter.getNanite(16) },
                new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                    MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L),
                    Materials.SuperconductorUMVBase.getMolten(4 * 36864L),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                getItemContainer("StargateShieldingFoil").get(1L),
                baseStargateTime,
                (int) TierEU.RECIPE_UMV);

            // Stargate chevron
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("StargateShieldingFoil").get(1L),
                (int) TierEU.RECIPE_MAX,
                32_768,
                (int) TierEU.RECIPE_UXV,
                64,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.block, MaterialsUEVplus.TranscendentMetal, 64L),
                    GT_OreDictUnificator.get("blockShirabon", 64), CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                    CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),

                    GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 16L),
                    GT_OreDictUnificator
                        .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Jasper, 16L),

                    GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Opal, 16L),
                    GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Sapphire, 16L),
                    GT_OreDictUnificator
                        .get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                    GT_OreDictUnificator.get("plateDenseShirabon", 8),

                    ItemList.Electric_Motor_UXV.get(64L), ItemList.Electric_Piston_UXV.get(64L),
                    ItemList.Field_Generator_UXV.get(16L),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 1L)
                        .splitStack(32) },
                new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                    MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L),
                    MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(36864L),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                getItemContainer("StargateChevron").get(1L),
                baseStargateTime,
                (int) TierEU.RECIPE_UMV);

            // Stargate Frame Part
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                (int) TierEU.RECIPE_MAX,
                32_768,
                (int) TierEU.RECIPE_UXV,
                64,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 64L),
                    GT_OreDictUnificator
                        .get(OrePrefixes.stickLong, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 64L),

                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmiridium, 64L),
                    GT_OreDictUnificator.get("stickLongShirabon", 64),
                    new ItemStack(WerkstoffLoader.items.get(OrePrefixes.stickLong), 64, 39), QUANTUM.getLongRod(64),
                    HYPOGEN.getLongRod(64), CELESTIAL_TUNGSTEN.getLongRod(64),
                    new ItemStack(WerkstoffLoader.items.get(OrePrefixes.stickLong), 64, 10106),
                    ASTRAL_TITANIUM.getLongRod(64),

                    GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUMVBase, 64L),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.Universium, 64L),
                    ABYSSAL.getLongRod(64),
                    GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 64L), },
                new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                    MaterialsUEVplus.SpaceTime.getMolten(4 * 36864L), MaterialsUEVplus.Universium.getMolten(4 * 36864L),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(4 * 36864L) },
                getItemContainer("StargateFramePart").get(1L),
                baseStargateTime,
                (int) TierEU.RECIPE_UMV);
        }

        // Dimensionally Transcendent Plasma Forge (DTPF)
        if (EternalSingularity.isModLoaded()) {

            // DTPF Controller.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Bridge.get(1),
                32_000_000,
                4096,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { ItemList.Casing_Dim_Bridge.get(4),
                    GT_Utility.copyAmount(16, ItemRegistry.megaMachines[0]), ItemList.Hatch_Energy_UEV.get(4L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 6),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 20L }, ItemList.Field_Generator_UEV.get(4),
                    getModItem(EternalSingularity.ID, "eternal_singularity", 4L),
                    GregtechItemList.Laser_Lens_Special.get(1),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64L),
                    ItemList.Electric_Pump_UEV.get(4), ItemList.ZPM3.get(1),
                    getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 128000),
                    new FluidStack(solderUEV, 36864 * 2),
                    new FluidStack(FluidRegistry.getFluid("molten.californium"), 36864),
                    Materials.NaquadahEnriched.getMolten(36864L) },
                ItemList.Machine_Multi_PlasmaForge.get(1),
                5 * MINUTES,
                (int) TierEU.RECIPE_UIV);

            // Dimensional bridge.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Injector.get(1),
                8_000_000,
                4096,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { ItemList.Casing_Dim_Trans.get(1), ItemList.MicroTransmitter_UV.get(1),
                    new Object[] { OrePrefixes.circuit.get(Materials.UV), 2L },
                    getModItem(Avaritia.ID, "Singularity", 2L, 0),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 6),
                    getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 2, 0), ItemList.Field_Generator_UHV.get(1L) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 8000),
                    new FluidStack(solderUEV, 1152 * 8), Materials.NaquadahEnriched.getMolten(1296L) },
                ItemList.Casing_Dim_Bridge.get(1),
                240 * 20,
                (int) TierEU.RECIPE_UIV);

            // Dimensional injection casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Dim_Trans.get(1),
                2_000_000,
                2048,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                    ItemList.Reactor_Coolant_Sp_6.get(1L), LAURENIUM.getScrew(12),
                    new Object[] { OrePrefixes.circuit.get(Materials.IV), 2L },
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
                    ItemList.Super_Chest_IV.get(1), ItemList.Super_Tank_IV.get(1),
                    getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 1, 0), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 1000),
                    new FluidStack(solderUEV, 576), Materials.NaquadahEnriched.getMolten(288L) },
                ItemList.Casing_Dim_Injector.get(1),
                20 * 20,
                (int) TierEU.RECIPE_UIV);

            // Dimensionally Transcendent Casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                getModItem(Avaritia.ID, "Singularity", 1L, 0),
                2_000_000,
                2048,
                (int) TierEU.RECIPE_UIV,
                1,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                    LAURENIUM.getScrew(12), ItemList.Reactor_Coolant_Sp_6.get(1L),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1), },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                    new FluidStack(solderUEV, 288), Materials.NaquadahEnriched.getMolten(144L) },
                ItemList.Casing_Dim_Trans.get(1),
                20 * 20,
                (int) TierEU.RECIPE_UIV);

            // Transdimensional Alignment Matrix
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.MicroTransmitter_UV.get(1),
                32_000_000,
                8192,
                (int) TierEU.RECIPE_UMV,
                64,
                new Object[] { CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    GregtechItemList.SpaceTimeContinuumRipper.get(4), ItemList.Robot_Arm_UMV.get(64),
                    ItemList.Sensor_UMV.get(16), ItemList.Field_Generator_UMV.get(4), ItemList.ZPM5.get(1),
                    ItemList.EnergisedTesseract.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.TranscendentMetal, 16),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 64),
                    MyMaterial.metastableOganesson.get(OrePrefixes.plateDense, 32) },
                new FluidStack[] { new FluidStack(solderUEV, 1024 * 144), Materials.Lead.getPlasma(64 * 144),
                    MaterialsUEVplus.Space.getMolten(64 * 144) },
                ItemList.Transdimensional_Alignment_Matrix.get(1),
                50 * SECONDS,
                (int) TierEU.RECIPE_UMV);
        }

        // Deep Dark Portal
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            getModItem(NewHorizonsCoreMod.ID, "item.HeavyDutyPlateTier8", 1, 0),
            16_777_216,
            2048,
            (int) TierEU.RECIPE_UHV,
            64,
            new Object[] { getModItem(ExtraUtilities.ID, "cobblestone_compressed", 1, 7),
                getModItem(IndustrialCraft2.ID, "blockMachine2", 1, 0),
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 32, 0), ItemList.Robot_Arm_UMV.get(4),
                ItemList.Emitter_UMV.get(4), ItemList.Sensor_UMV.get(4), },
            new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 50000),
                Materials.Infinity.getMolten(144L * 512), Materials.Cheese.getMolten(232000L), },
            ItemList.Block_BedrockiumCompressed.get(1),
            10000,
            5000000);

        // Batteries
        {

            // Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Energy_Cluster.get(1L),
                12000,
                16,
                100000,
                3,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 64L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1L }, ItemList.Energy_Cluster.get(8L),
                    ItemList.Field_Generator_UV.get(2), ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Parts_DiodeASMD.get(32),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                ItemList.ZPM2.get(1),
                3000,
                400000);

            // Really Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM2.get(1L),
                24000,
                64,
                200000,
                6,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1L }, ItemList.ZPM2.get(8),
                    ItemList.Field_Generator_UHV.get(4), ItemList.Circuit_Wafer_UHPIC.get(64),
                    ItemList.Circuit_Wafer_UHPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(32),
                    ItemList.Circuit_Parts_DiodeASMD.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64), },
                new FluidStack[] { new FluidStack(solderUEV, 4608), Materials.Naquadria.getMolten(9216),
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000) },
                ItemList.ZPM3.get(1),
                4000,
                1600000);

            // Extremely Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM3.get(1L),
                1_200_000,
                128,
                (int) TierEU.RECIPE_UEV,
                16,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L },
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L }, ItemList.ZPM3.get(8),
                    ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                    ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                    ItemList.Circuit_Parts_DiodeXSMD.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64), },
                new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18_432),
                    Materials.Naquadria.getMolten(9_216 * 2), Materials.SuperCoolant.getFluid(64_000) },
                ItemList.ZPM4.get(1),
                250 * 20,
                6_400_000);

            if (GTPlusPlusEverglades.isModLoaded()) {
                // Insanely Ultimate Battery
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.ZPM4.get(1),
                    24_000_000,
                    1_280,
                    (int) TierEU.RECIPE_UIV,
                    32,
                    new Object[] { HYPOGEN.getPlateDouble(32), HYPOGEN.getPlateDouble(32),
                        new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 }, ItemList.ZPM4.get(8L),
                        ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                        ItemList.Circuit_Wafer_QPIC.get(64),
                        GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.RawPicoWafer", 64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUIV, 64) },
                    new FluidStack[] { new FluidStack(solderUEV, 18_432),
                        ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(18_432),
                        Materials.Quantium.getMolten(18_432), Materials.SuperCoolant.getFluid(128_000) },
                    ItemList.ZPM5.get(1),
                    300 * 20,
                    (int) TierEU.RECIPE_UIV);

                // Mega Ultimate Battery
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.ZPM5.get(1L),
                    480_000_000,
                    12_288,
                    (int) TierEU.RECIPE_UMV,
                    64,
                    new Object[] { ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                        ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.UXV), 1 }, ItemList.ZPM5.get(8L),
                        ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                        ItemList.Circuit_Wafer_QPIC.get(64),
                        GT_ModHandler.getModItem(NewHorizonsCoreMod.ID, "item.PicoWafer", 64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                    new FluidStack[] { new FluidStack(solderUEV, 36_864),
                        ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(36_864),
                        ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(36_864),
                        Materials.SuperCoolant.getFluid(256_000) },
                    ItemList.ZPM6.get(1),
                    350 * 20,
                    (int) TierEU.RECIPE_UMV);
            }
        }

        // MK4 Computer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 4),
                ItemList.Field_Generator_UHV.get(2), ItemList.Circuit_Wafer_QPIC.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 32) },
            new FluidStack[] { Materials.UUMatter.getFluid(50000), CINOBITE.getFluidStack(9216),
                ALLOY.OCTIRON.getFluidStack(9216), ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(9216), },
            GregtechItemList.FusionComputer_UV2.get(1),
            6000,
            (int) TierEU.RECIPE_UHV);

        // MK4 Coils
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Fusion_Coil.get(1L),
            160000,
            512,
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { ItemList.Energy_LapotronicOrb2.get(16L),
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8), ItemList.Emitter_UHV.get(1),
                ItemList.Sensor_UHV.get(1), ItemList.Casing_Fusion_Coil.get(1L), },
            new FluidStack[] { Materials.UUMatter.getFluid(8000L), CINOBITE.getFluidStack(2304),
                ALLOY.OCTIRON.getFluidStack(2304), ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(2304), },
            GregtechItemList.Casing_Fusion_Internal.get(1),
            1200,
            (int) TierEU.RECIPE_UHV);

        // MK4 Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Casing_Fusion2.get(1L),
            80000,
            512,
            (int) TierEU.RECIPE_UHV,
            1,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.EV), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.IV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.TungstenCarbide, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                ItemList.Electric_Motor_UHV.get(2), ItemList.Electric_Piston_UHV.get(1),
                ItemList.Casing_Fusion2.get(1L), },
            new FluidStack[] { Materials.UUMatter.getFluid(1000L), CINOBITE.getFluidStack(576),
                ALLOY.OCTIRON.getFluidStack(576), ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(576), },
            GregtechItemList.Casing_Fusion_External.get(1),
            300,
            (int) TierEU.RECIPE_UHV);

        // MK5 Computer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                GT_OreDictUnificator.get("plateDenseMetastableOganesson", 4), ItemList.Field_Generator_UEV.get(2),
                getItemContainer("PicoWafer").get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 32) },
            new FluidStack[] { ELEMENT.getInstance().CURIUM.getFluidStack(9216),
                ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(9216), ALLOY.ABYSSAL.getFluidStack(9216),
                ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(9216) },
            GregtechItemList.FusionComputer_UV3.get(1),
            6000,
            (int) TierEU.RECIPE_UEV);

        // MK5 Coils
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Fusion_Internal.get(1),
            2560000,
            4096,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] { ItemList.Energy_Module.get(16), new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L }, ELEMENT.STANDALONE.RHUGNOR.getPlate(8),
                ItemList.Emitter_UEV.get(1), ItemList.Sensor_UEV.get(1), new ItemStack(compactFusionCoil, 1, 2) },
            new FluidStack[] { ELEMENT.getInstance().NEPTUNIUM.getFluidStack(2304),
                ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(2304), ALLOY.ABYSSAL.getFluidStack(2304),
                ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(2304) },
            GregtechItemList.Casing_Fusion_Internal2.get(1),
            1200,
            (int) TierEU.RECIPE_UEV);

        // MK5 Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            GregtechItemList.Casing_Fusion_External.get(1L),
            2560000,
            4096,
            (int) TierEU.RECIPE_UEV,
            8,
            new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.IV), 16L },
                new Object[] { OrePrefixes.circuit.get(Materials.LuV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 8),
                ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getPlate(8), ItemList.Electric_Motor_UEV.get(2),
                ItemList.Electric_Piston_UEV.get(1), GregtechItemList.Casing_Fusion_External.get(1L) },
            new FluidStack[] { ELEMENT.getInstance().FERMIUM.getFluidStack(1152),
                ELEMENT.STANDALONE.CHRONOMATIC_GLASS.getFluidStack(1152), ALLOY.ABYSSAL.getFluidStack(1152),
                ELEMENT.STANDALONE.DRAGON_METAL.getFluidStack(1152) },
            GregtechItemList.Casing_Fusion_External2.get(1),
            300,
            (int) TierEU.RECIPE_UEV);

        if (BloodMagic.isModLoaded() && DraconicEvolution.isModLoaded()) {
            // Draconic Evolution Fusion Crafter Controller
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(defcCasingBlock, 1, 8),
                16_777_216,
                1024,
                (int) TierEU.RECIPE_UHV,
                8,
                new Object[] { ItemList.AssemblingMachineUHV.get(1), new ItemStack(defcCasingBlock, 1, 8),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsBotania.GaiaSpirit, 1L),
                    ItemList.Casing_Coil_AwakenedDraconium.get(8L), ItemList.Electric_Motor_UHV.get(8L),
                    ItemList.Robot_Arm_UHV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4 },
                    ItemList.Gravistar.get(4, new Object() {}), getModItem(Thaumcraft.ID, "ItemEldritchObject", 1, 3),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 29),
                    getModItem(BloodMagic.ID, "bloodMagicBaseItems", 8, 28), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.Void.getMolten(2880L),
                    Materials.DraconiumAwakened.getMolten(1440), },
                DraconicEvolutionFusionCrafter.get(1),
                1500,
                (int) TierEU.RECIPE_UEV);
            // DE Schematics Cores Tier 1
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                new ItemStack(defcCasingBlock, 1, 9),
                5_000_000,
                512,
                1_000_000,
                4,
                new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UHV), 1 }, },
                new FluidStack[] { Materials.Sunnarium.getMolten(14400L), Materials.Void.getMolten(28800L), },
                DEFCDraconicSchematic.get(1),
                6000,
                (int) TierEU.RECIPE_UV);

            // DE Schematics Cores Tier 2
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "draconicCore", 1, 0),
                10_000_000,
                1024,
                4_000_000,
                8,
                new Object[] { getModItem(DraconicEvolution.ID, "draconicCore", 4, 0),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UEV), 1 }, },
                new FluidStack[] { Materials.Neutronium.getMolten(14400L), Materials.Void.getMolten(57600L), },
                DEFCWyvernSchematic.get(1),
                12000,
                (int) TierEU.RECIPE_UHV);

            // DE Schematics Cores Tier 3
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "wyvernCore", 1, 0),
                20_000_000,
                2048,
                16_000_000,
                16,
                new Object[] { getModItem(DraconicEvolution.ID, "wyvernCore", 4, 0),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 1L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UIV), 1L } },
                new FluidStack[] { Materials.Infinity.getMolten(14400L), Materials.Void.getMolten(115200L), },
                DEFCAwakenedSchematic.get(1),
                24000,
                (int) TierEU.RECIPE_UEV);

            // DE Schematics Cores Tier 4
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                getModItem(DraconicEvolution.ID, "awakenedCore", 1, 0),
                40_000_000,
                4096,
                64_000_000,
                64,
                new Object[] { getModItem(DraconicEvolution.ID, "awakenedCore", 8, 0),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 4L),
                    GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 1L),
                    new Object[] { OrePrefixes.circuit.get(Materials.UMV), 1 }, },
                new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(14400L), Materials.Void.getMolten(230400L), },
                DEFCChaoticSchematic.get(1),
                36000,
                (int) TierEU.RECIPE_UIV);
        }

        // Debug maintenance hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Hatch_AutoMaintenance.get(1L),
            2764800,
            128,
            (int) TierEU.RECIPE_UV,
            6,
            new Object[] { ItemList.Hatch_AutoMaintenance.get(1L), ItemList.Robot_Arm_UV.get(1L),
                ItemList.Electric_Pump_UV.get(1L), ItemList.Conveyor_Module_UV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L }, ItemList.Energy_LapotronicOrb2.get(1L),
                ItemList.Duct_Tape.get(64L), ItemList.Duct_Tape.get(64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L), },
            new FluidStack[] { Materials.Lubricant.getFluid(256000), new FluidStack(solderIndalloy, 1296), },
            CustomItemList.hatch_CreativeMaintenance.get(1),
            6000,
            (int) TierEU.RECIPE_UV);

        // Debug uncertainty resolver
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.UncertaintyX_Hatch.get(1L),
            72_000_000,
            5_120,
            16_000_000,
            6,
            new Object[] { CustomItemList.eM_Computer_Bus.get(1), CustomItemList.hatch_CreativeMaintenance.get(1),
                ItemList.Field_Generator_UIV.get(1L), GregtechItemList.Laser_Lens_Special.get(4),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2 }, CustomItemList.DATApipe.get(64),
                CustomItemList.DATApipe.get(64), ItemList.Cover_Screen.get(1) },
            new FluidStack[] { Materials.Iridium.getMolten(INGOTS * 100), new FluidStack(solderUEV, 2592),
                new FluidStack(ELEMENT.getInstance().NEPTUNIUM.getPlasma(), 20000),
                new FluidStack(ELEMENT.getInstance().FERMIUM.getPlasma(), 20000) },
            CustomItemList.hatch_CreativeUncertainty.get(1),
            200 * 20,
            (int) TierEU.RECIPE_UIV);
    }

    private void itemPartsUHVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
            ? FluidRegistry.getFluid("molten.indalloy140")
            : FluidRegistry.getFluid("molten.solderingalloy");

        int total_computation = 24000;
        int comp_per_second = 32;
        int research_eu_per_tick = (int) TierEU.RECIPE_UV;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Naquadria.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(solderIndalloy, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UV;

        // -------------------------------------------------------------

        // ------------------------- UHV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 4L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 32L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UHV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UHV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 32L },
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UHV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UHV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 40L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 6L),
                ItemList.Electric_Motor_UHV.get(2L), ItemList.Electric_Piston_UHV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.ZPM), 8L },
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UHV Electric Piston --------------------
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UHV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UHV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                ItemList.Electric_Motor_UHV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L), ItemList.Gravistar.get(8L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UHV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                ItemList.Electric_Motor_UHV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 8L), ItemList.Gravistar.get(8L),
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UHV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L), ItemList.Gravistar.get(4L),
                ItemList.Emitter_UHV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Field_Generator_UHV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);
    }

    private void itemPartsUEVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        int total_computation = 48_000;
        int comp_per_second = 64;
        int research_eu_per_tick = (int) TierEU.RECIPE_UHV;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Quantium.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UHV;

        // -------------------------------------------------------------

        // ------------------------- UEV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 8L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 16L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 32L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UEV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UEV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 4L),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UEV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UEV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 6L),
                ItemList.Electric_Motor_UEV.get(2L), ItemList.Electric_Piston_UEV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UEV Electric Piston --------------------
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UEV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UEV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                ItemList.Electric_Motor_UEV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 16L), ItemList.Gravistar.get(16L),
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UEV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UHV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                ItemList.Electric_Motor_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 8L),
                ItemList.Gravistar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UEV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UHV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L), ItemList.Gravistar.get(8L),
                ItemList.Emitter_UEV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Field_Generator_UEV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);
    }

    private void itemPartsUIVAsslineRecipes() {

        // ----------------------------------------------------------------------
        // ------------------------- Set up information -------------------------
        // ----------------------------------------------------------------------

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        int total_computation = 96_000;
        int comp_per_second = 128;
        int research_eu_per_tick = (int) TierEU.RECIPE_UEV;
        int research_amperage = 1;

        FluidStack fluid_0 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UEV;

        // -------------------------------------------------------------

        // ------------------------- UIV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 16L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 16L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 32L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Motor_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UIV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UIV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.DraconiumAwakened, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 4L),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.TranscendentMetal, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GT_OreDictUnificator.get(OrePrefixes.rotor, MaterialsUEVplus.TranscendentMetal, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Pump_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UIV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UIV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Conveyor_Module_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.TranscendentMetal, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.TranscendentMetal, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 6L),
                ItemList.Electric_Motor_UIV.get(2L), ItemList.Electric_Piston_UIV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UHV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Robot_Arm_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UIV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UIV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.TranscendentMetal, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.TranscendentMetal, 64L),
                GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.TranscendentMetal, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.TranscendentMetal, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Electric_Piston_UIV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                ItemList.Electric_Motor_UIV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.TranscendentMetal, 16L),
                ItemList.Gravistar.get(32L), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                TRINIUM_REINFORCED_STEEL.getFoil(64), LAFIUM.getFoil(64), CINOBITE.getFoil(64), PIKYONIUM.getFoil(64),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Emitter_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UIV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UEV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                ItemList.Electric_Motor_UIV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 8L),
                ItemList.Gravistar.get(32), new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                TRINIUM_REINFORCED_STEEL.getFoil(64), LAFIUM.getFoil(64), CINOBITE.getFoil(64), PIKYONIUM.getFoil(64),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 7L) },
            new FluidStack[] { fluid_0, fluid_1 },
            ItemList.Sensor_UIV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UIV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UEV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.TranscendentMetal, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.TranscendentMetal, 6L),
                ItemList.Gravistar.get(16L), ItemList.Emitter_UIV.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4 },
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Infinity, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.NetherStar, 8L) },
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

        Fluid mutated_living_solder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        Fluid hypogen = FluidRegistry.getFluid("molten.hypogen");
        Fluid celestialTungsten = FluidRegistry.getFluid("molten.celestialtungsten");

        int total_computation = 192_000;
        int comp_per_second = 256;
        int research_eu_per_tick = (int) TierEU.RECIPE_UIV;
        int research_amperage = 1;

        FluidStack fluid_0 = hypogen != null ? new FluidStack(hypogen, 576) : null;
        FluidStack fluid_1 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_2 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_3 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = (int) TierEU.RECIPE_UIV;

        // -------------------------------------------------------------

        // ------------------------- UMV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TengamAttuned, 32L),
                GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 16L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 32L), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Motor_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // --------------------- UMV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UMV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Infinity, 2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 4L),
                GT_OreDictUnificator.get(OrePrefixes.screw, MaterialsUEVplus.SpaceTime, 16L),
                new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                GT_OreDictUnificator.get(OrePrefixes.rotor, MaterialsUEVplus.SpaceTime, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Pump_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ----------------------- UMV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { ItemList.Electric_Motor_UMV.get(2L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 64L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 2L),
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 64L },
                new Object[] { OrePrefixes.plate.get(Materials.AnySyntheticRubber), 16L } },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Conveyor_Module_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 6L),
                ItemList.Electric_Motor_UMV.get(2L), ItemList.Electric_Piston_UMV.get(1L),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UEV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 6L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Robot_Arm_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // -------------------- UMV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new ItemStack[] { ItemList.Electric_Motor_UMV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.round, MaterialsUEVplus.SpaceTime, 64L),
                GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.gear, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, 4L),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 4L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2, fluid_3 },
            ItemList.Electric_Piston_UMV.get(1),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                ItemList.Electric_Motor_UMV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.stick, MaterialsUEVplus.SpaceTime, 16L),
                ItemList.Gravistar.get(64), new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L },
                CELESTIAL_TUNGSTEN.getFoil(64), QUANTUM.getFoil(64), ASTRAL_TITANIUM.getFoil(64),
                TITANSTEEL.getFoil(64), GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Emitter_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // -------------------------------------------------------------

        // ------------------------ UMV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UIV.get(1L),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                ItemList.Electric_Motor_UMV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 8L), ItemList.Gravistar.get(64),
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L }, CELESTIAL_TUNGSTEN.getFoil(64),
                QUANTUM.getFoil(64), ASTRAL_TITANIUM.getFoil(64), TITANSTEEL.getFoil(64),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
            new FluidStack[] { fluid_0, fluid_1, fluid_2 },
            ItemList.Sensor_UMV.get(1L),
            crafting_time_in_ticks,
            crafting_eu_per_tick);

        // ---------------------------------------------------------------------

        // ------------------------ UMV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UIV.get(1),
            total_computation,
            comp_per_second,
            research_eu_per_tick,
            research_amperage,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.SpaceTime, 1L),
                GT_OreDictUnificator.get(OrePrefixes.plate, MaterialsUEVplus.SpaceTime, 6L),
                ItemList.Gravistar.get(32L), ItemList.Emitter_UMV.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4 }, HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64), HYPOGEN.getFineWire(64),
                GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 8L) },
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

        Fluid mutatedLivingSolder = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

        FluidStack moltenMHDCSM_576 = MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(576);
        FluidStack moltenSpaceTime_576 = MaterialsUEVplus.SpaceTime.getMolten(576);
        FluidStack moltenUniversium_576 = MaterialsUEVplus.Universium.getMolten(576);
        FluidStack lubricantFluid_8000 = Materials.Lubricant.getFluid(8000);
        FluidStack solderingAlloy_14_400 = new FluidStack(mutatedLivingSolder, 14_400);

        int totalComputation = 384_000;
        int compPerSecond = 512;
        int researchEuPerTick = 64_000_000;
        int researchAmperage = 2;

        int craftingTimeInTicks = 1000;
        int craftingEuPerTick = (int) TierEU.RECIPE_UMV;

        // -------------------------------------------------------------

        // ------------------------- UXV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Motor_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new ItemStack[] { ItemList.EnergisedTesseract.get(1),
                GT_OreDictUnificator
                    .get(OrePrefixes.stickLong, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                GT_OreDictUnificator
                    .get(OrePrefixes.ring, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                GT_OreDictUnificator
                    .get(OrePrefixes.round, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 32L),

                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),

                GT_OreDictUnificator
                    .get(OrePrefixes.wireFine, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                GT_OreDictUnificator
                    .get(OrePrefixes.wireFine, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),

                GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),

                GT_OreDictUnificator.get("wireFineShirabon", 64L), GT_OreDictUnificator.get("wireFineShirabon", 64L),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                Materials.Neutronium.getNanite(4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
            ItemList.Electric_Motor_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // --------------------- UXV Electric Pump ---------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Pump_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { ItemList.Electric_Motor_UXV.get(1L),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 4L),
                GT_OreDictUnificator
                    .get(OrePrefixes.screw, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsKevlar.Kevlar, 64L),
                GT_OreDictUnificator.get("ringRadoxPoly", 64L),
                GT_OreDictUnificator
                    .get(OrePrefixes.rotor, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 4L),
                GT_OreDictUnificator.get("rotorShirabon", 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                Materials.Neutronium.getNanite(4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
            ItemList.Electric_Pump_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ----------------------- UXV Conveyor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Conveyor_Module_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] { ItemList.Electric_Motor_UXV.get(2L),
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                GT_OreDictUnificator
                    .get(OrePrefixes.ring, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                GT_OreDictUnificator
                    .get(OrePrefixes.round, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 2L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                MaterialsKevlar.Kevlar.getPlates(64), MaterialsKevlar.Kevlar.getPlates(16),
                GT_OreDictUnificator.get("plateRadoxPoly", 64L), GT_OreDictUnificator.get("plateRadoxPoly", 16L),
                Materials.Neutronium.getNanite(4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
            ItemList.Conveyor_Module_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Robot Arm --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Robot_Arm_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] {
                GT_OreDictUnificator
                    .get(OrePrefixes.stickLong, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                GT_OreDictUnificator
                    .get(OrePrefixes.gear, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                GT_OreDictUnificator.get("gearGtShirabon", 2L),
                GT_OreDictUnificator
                    .get(OrePrefixes.gearGtSmall, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                GT_OreDictUnificator.get("gearGtSmallShirabon", 6L), ItemList.Electric_Motor_UXV.get(2L),
                ItemList.Electric_Piston_UXV.get(1L), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 2L },
                new Object[] { OrePrefixes.circuit.get(Materials.UMV), 4L },
                new Object[] { OrePrefixes.circuit.get(Materials.UIV), 8L },
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 6L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 6L),
                Materials.Neutronium.getNanite(8) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
            ItemList.Robot_Arm_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // -------------------- UXV Electric Piston --------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Electric_Piston_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new ItemStack[] { ItemList.Electric_Motor_UXV.get(1L),
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                GT_OreDictUnificator
                    .get(OrePrefixes.ring, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                GT_OreDictUnificator
                    .get(OrePrefixes.round, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                GT_OreDictUnificator
                    .get(OrePrefixes.stick, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                GT_OreDictUnificator
                    .get(OrePrefixes.gear, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                GT_OreDictUnificator.get("gearGtShirabon", 2L),
                GT_OreDictUnificator
                    .get(OrePrefixes.gearGtSmall, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 4L),
                GT_OreDictUnificator.get("gearGtSmallShirabon", 4L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 4L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 4L),
                Materials.Neutronium.getNanite(4) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, lubricantFluid_8000 },
            ItemList.Electric_Piston_UXV.get(1),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Emitter ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Emitter_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] {
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                ItemList.Electric_Motor_UXV.get(1L),
                GT_OreDictUnificator
                    .get(OrePrefixes.stick, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4L },
                GT_OreDictUnificator
                    .get(OrePrefixes.foil, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                GT_OreDictUnificator.get("foilShirabon", 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 7L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 7L),
                Materials.Neutronium.getNanite(8)

            },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Emitter_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // -------------------------------------------------------------

        // ------------------------ UXV Sensor ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Sensor_UMV.get(1L),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] {
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                ItemList.Electric_Motor_UXV.get(1L),
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.UXV), 4L },
                GT_OreDictUnificator
                    .get(OrePrefixes.foil, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                GT_OreDictUnificator.get("foilShirabon", 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.SpaceTime, 64),
                GT_OreDictUnificator.get(OrePrefixes.foil, MaterialsUEVplus.Universium, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 7L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 7L),
                Materials.Neutronium.getNanite(8) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Sensor_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // ---------------------------------------------------------------------

        // ------------------------ UXV Field Generator ------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
            ItemList.Field_Generator_UMV.get(1),
            totalComputation,
            compPerSecond,
            researchEuPerTick,
            researchAmperage,
            new Object[] {
                GT_OreDictUnificator
                    .get(OrePrefixes.frameGt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                GT_OreDictUnificator
                    .get(OrePrefixes.plate, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                ItemList.NuclearStar.get(64L), ItemList.Emitter_UXV.get(4L),
                new Object[] { OrePrefixes.circuit.get(Materials.UXV), 8 },

                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                GT_OreDictUnificator
                    .get(OrePrefixes.wireFine, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                GT_OreDictUnificator
                    .get(OrePrefixes.wireFine, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, MaterialsUEVplus.Universium, 64L),
                GT_OreDictUnificator.get("wireFineShirabon", 64L), GT_OreDictUnificator.get("wireFineShirabon", 64L),

                GT_OreDictUnificator.get(OrePrefixes.wireGt04, MaterialsUEVplus.SpaceTime, 8L),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 8L),
                Materials.Neutronium.getNanite(12) },
            new FluidStack[] { moltenMHDCSM_576, moltenSpaceTime_576, moltenUniversium_576, solderingAlloy_14_400 },
            ItemList.Field_Generator_UXV.get(1L),
            craftingTimeInTicks,
            craftingEuPerTick);

        // ---------------------------------------------------------------------

    }

    private void addEOHRecipes() {
        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");
        ItemStack largeShirabonPlate = TGregUtils.newItemStack(Materials.get("Shirabon"), PartTypes.LargePlate, 1);
        ItemStack largeInfinityPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.infinityMetalId);
        ItemStack largeBedrockiumPlate = new ItemStack(TinkerTools.largePlate, 1, ExtraUtils.tcon_bedrock_material_id);
        ItemStack largeCosmicNeutroniumPlate = new ItemStack(TinkerTools.largePlate, 1, Tonkers.neutroniumId);

        final FluidStack[] specialFluid = new FluidStack[] { MaterialsUEVplus.SpaceTime.getMolten(1_440),
            MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
            MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
            MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440),
            MaterialsUEVplus.SpaceTime.getMolten(1_440), MaterialsUEVplus.SpaceTime.getMolten(1_440) };

        final ItemStack[] plateList = new ItemStack[] {
            // Dense Shirabon plate.
            GT_OreDictUnificator.get("boltShirabon", 2),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 2),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 8),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.WhiteDwarfMatter, 32),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 2),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 8),
            GT_OreDictUnificator.get(OrePrefixes.bolt, MaterialsUEVplus.BlackDwarfMatter, 32),
            GT_OreDictUnificator
                .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 2),
            GT_OreDictUnificator
                .get(OrePrefixes.bolt, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 8) };

        // EOH Controller Recipe.
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Machine_Multi_PlasmaForge.get(1),
                512_000_000, // total comp
                2 * 16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                64, // amperage
                new Object[] {
                    // Space elevator controller.
                    getModItem(GregTech.ID, "gt.blockmachines", 16, 14003), ItemList.Machine_Multi_PlasmaForge.get(4),

                    CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    CustomItemList.StabilisationFieldGeneratorTier0.get(1),

                    CustomItemList.Machine_Multi_Computer.get(64), ItemList.AcceleratorUV.get(1),
                    ItemList.Quantum_Chest_IV.get(64),
                    // Void miner III.
                    GT_Utility.copyAmount(64, ItemRegistry.voidminer[2]),

                    ItemList.Field_Generator_UMV.get(16), ItemList.Robot_Arm_UMV.get(16), ItemList.ZPM4.get(4),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                new FluidStack[] { MaterialsUEVplus.Time.getMolten(144_000), MaterialsUEVplus.Space.getMolten(144_000),
                    FluidUtils.getFluidStack("molten.metastable oganesson", 144 * 256 * 4),
                    FluidUtils.getFluidStack("molten.shirabon", 144 * 256 * 4), },
                CustomItemList.Machine_Multi_EyeOfHarmony.get(1),
                400 * MINUTES,
                (int) TierEU.RECIPE_UMV);
        }

        // EOH Spatial Individual Casing
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                // Dyson Swarm Module Deployment Unit Base Casing
                getModItem(GalaxySpace.ID, "dysonswarmparts", 1, 2),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] {
                    // Space elevator blocks.
                    getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64, 0),
                    // Cosmic neutronium block.
                    getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48), largeBedrockiumPlate,
                    largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                    // UV Solar panel
                    getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0), ItemList.Quantum_Chest_IV.get(1),
                    // Gravitation Engine
                    getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                    Materials.CosmicNeutronium.getMolten(144 * 256 * 4), new FluidStack(solderUEV, 144 * 256 * 2),
                    MaterialsUEVplus.Space.getMolten(1_440) },
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
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 1;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        plateList[absoluteTier], GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier1.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 2;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier2.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 2;
                absoluteTier = 3;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier3.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 4;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier4.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 5;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier5.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);
            }

            {
                tier = 1;
                set = 3;
                absoluteTier = 6;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        plateList[absoluteTier], GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier6.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 7;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        plateList[absoluteTier], GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
                    CustomItemList.SpacetimeCompressionFieldGeneratorTier7.get(1),
                    (absoluteTier + 1) * 4_000 * 20,
                    (int) TierEU.RECIPE_UMV);

                tier++;
                absoluteTier = 8;
                TT_recipeAdder.addResearchableAssemblylineRecipe(
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
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, set) },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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

                TT_recipeAdder.addResearchableAssemblylineRecipe(
                    researchStuff[absoluteTier],
                    (absoluteTier + 1) * 48_000_000, // total comp
                    (absoluteTier + 1) * baseCompPerSec, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    (absoluteTier + 1) * 8, // amperage
                    new Object[] { baseCasing, fusionReactors[absoluteTier], fusionCoils[absoluteTier],
                        // UV Solar panel
                        getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", absoluteTier + 1, 0),

                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, absoluteTier + 1),
                        // Red Spectral Component
                        spectralComponents[absoluteTier % spectralComponents.length],
                        // Green Spectral Component
                        spectralComponents[(absoluteTier + 1) % spectralComponents.length],
                        // Blue Spectral Component
                        spectralComponents[(absoluteTier + 2) % spectralComponents.length],

                        plateList[absoluteTier],
                        // Dyson Swarm Module Deployment Unit Base Casing
                        getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                        // Dyson Swarm Energy Receiver Dish Block
                        getModItem(GalaxySpace.ID, "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                        ItemList.AcceleratorUV.get((absoluteTier + 1) * 4),

                        ItemList.Energy_Module.get(absoluteTier + 1), GT_OreDictUnificator
                            .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4),

                    },
                    new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                        MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
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
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        researchStuff[absoluteTier],
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] {
                                timeCasings[absoluteTier],
                                spatialCasings[absoluteTier],
                                baseCasing,
                                // Dyson Swarm Module.
                                getModItem(GalaxySpace.ID, "item.DysonSwarmParts", 4 * (absoluteTier + 1), 0),

                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Longasssuperconductornameforuhvwire, 4 * (absoluteTier + 1)),

                                // Gravitation Engine
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),
                                getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3),

                                plateList[absoluteTier],
                                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 2 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, MaterialsUEVplus.SpaceTime, absoluteTier + 1),
                                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, MaterialsUEVplus.SpaceTime, absoluteTier + 1)


                        },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                MaterialsUEVplus.Time.getMolten(1_440 * (absoluteTier + 1)),
                                MaterialsUEVplus.Space.getMolten(1_440 * (absoluteTier + 1)),
                                specialFluid[absoluteTier] },
                        researchStuff[absoluteTier + 1],
                        (absoluteTier + 1) * 4_000 * 20,
                        (int) TierEU.RECIPE_UMV);
                // spotless:on
            }

        }

        // EOH Reinforced Temporal casings
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.AcceleratorUV.get(1),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] {
                    // Space elevator blocks.
                    getModItem(GTNHIntergalactic.ID, "gt.blockcasingsSE", 64, 0),
                    // Cosmic neutronium block.
                    getModItem(Avaritia.ID, "Resource_Block", 64, 0),
                    GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                    GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),
                    // Large Bedrockium Plate
                    largeBedrockiumPlate, largeCosmicNeutroniumPlate, largeShirabonPlate, largeInfinityPlate,
                    // UV Solar panel
                    getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0),
                    // Ultimate Time Anomaly.
                    ItemList.AcceleratorUV.get(4),
                    // Gravitation Engine.
                    getModItem(GraviSuite.ID, "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                    Materials.CosmicNeutronium.getMolten(144 * 256 * 4), new FluidStack(solderUEV, 144 * 256 * 2),
                    MaterialsUEVplus.Time.getMolten(1_440) },
                CustomItemList.EOH_Reinforced_Temporal_Casing.get(4),
                10_000,
                (int) TierEU.RECIPE_UMV);
        }

        // EOH Infinite Spacetime Energy Boundary Casing
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 1),
                256_000_000, // total comp
                16_384, // comp/s
                (int) TierEU.RECIPE_MAX, // eu/t
                32, // amperage
                new Object[] { TileEntities.lsc.getStackForm(1),
                    // UV Solar panel
                    getModItem(SuperSolarPanels.ID, "PhotonicSolarPanel", 1, 0),
                    // UHV Capacitor block
                    new ItemStack(lscLapotronicEnergyUnit, 1, 5),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 4),

                    CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                    CustomItemList.eM_energyTunnel5_UMV.get(1),
                    // High Energy Flow Circuit.
                    getModItem(NewHorizonsCoreMod.ID, "item.HighEnergyFlowCircuit", 64, 0),

                    // Metastable Oganesson Plate.
                    GT_OreDictUnificator.get("plateMetastableOganesson", 6),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlueTopaz, 6),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CallistoIce, 6),
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ledox, 6),

                    // Metastable Oganesson Screw.
                    GT_OreDictUnificator.get("screwMetastableOganesson", 6),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.BlueTopaz, 6),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CallistoIce, 6),
                    GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Ledox, 6), },

                new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 16),
                    Materials.CosmicNeutronium.getMolten(144 * 256 * 16), new FluidStack(solderUEV, 144 * 256 * 8),
                    MaterialsUEVplus.SpaceTime.getMolten(16_000) },
                CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                10_000,
                (int) TierEU.RECIPE_UMV);
        }

        // Astral Array Fabricator
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.SpacetimeCompressionFieldGeneratorTier8.get(1),
            480_000_000,
            32_768,
            (int) TierEU.RECIPE_MAX,
            64,
            new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.WhiteDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, MaterialsUEVplus.BlackDwarfMatter, 8),
                ItemList.EnergisedTesseract.get(32),
                GT_OreDictUnificator.get(OrePrefixes.nanite, MaterialsUEVplus.Eternity, 16),
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
            new FluidStack[] { MaterialsUEVplus.Space.getMolten(32_768L * 64),
                MaterialsUEVplus.Eternity.getMolten(16_384L * 64), MaterialsUEVplus.ExcitedDTSC.getFluid(8_192L * 64) },
            CustomItemList.astralArrayFabricator.get(1),
            300 * SECONDS,
            (int) TierEU.RECIPE_UXV);

    }

    private void addWirelessEnergyRecipes() {

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
            ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
            : FluidRegistry.getFluid("molten.solderingalloy");

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

        ItemStack[] wirelessDynamos = { ItemList.Wireless_Dynamo_Energy_ULV.get(1),
            ItemList.Wireless_Dynamo_Energy_LV.get(1), ItemList.Wireless_Dynamo_Energy_MV.get(1),
            ItemList.Wireless_Dynamo_Energy_HV.get(1), ItemList.Wireless_Dynamo_Energy_EV.get(1),
            ItemList.Wireless_Dynamo_Energy_IV.get(1), ItemList.Wireless_Dynamo_Energy_LuV.get(1),
            ItemList.Wireless_Dynamo_Energy_ZPM.get(1), ItemList.Wireless_Dynamo_Energy_UV.get(1),
            ItemList.Wireless_Dynamo_Energy_UHV.get(1), ItemList.Wireless_Dynamo_Energy_UEV.get(1),
            ItemList.Wireless_Dynamo_Energy_UIV.get(1), ItemList.Wireless_Dynamo_Energy_UMV.get(1),
            ItemList.Wireless_Dynamo_Energy_UXV.get(1) };

        // ------------------------ Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                (i == 0) ? ItemList.Tesseract.get(1) : wirelessHatches[i - 1],
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { energyHatches[i], new ItemStack(compactFusionCoil, 1),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(2),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1), circuitsTierPlusTwo[i],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1296), MaterialsUEVplus.ExcitedDTEC.getFluid(500L) },
                wirelessHatches[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 4A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_4A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_4A[i],
                totalComputation * 4,
                compPerSecond * 4,
                researchEuPerTick,
                researchAmperage * 2,
                new Object[] { energyHatches_4A[i], new ItemStack(compactFusionCoil, 1, 1),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(4),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 4),
                    GT_OreDictUnificator.get("plateTripleShirabon", 4L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Flerovium, 4),
                    circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1_296 * 4),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 4) },
                wirelessHatches_4A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 16A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_16A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_16A[i],
                totalComputation * 16,
                compPerSecond * 16,
                researchEuPerTick,
                researchAmperage * 4,
                new Object[] { energyHatches_16A[i], new ItemStack(compactFusionCoil, 1, 2),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(16),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 16),
                    GT_OreDictUnificator.get("plateTripleShirabon", 16L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.TranscendentMetal, 4),
                    circuitsTierPlusTwo[i + 4], ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1_296 * 16),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 16) },
                wirelessHatches_16A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 64A Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches_64A.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                energyHatches_64A[i],
                totalComputation * 64,
                compPerSecond * 64,
                researchEuPerTick,
                researchAmperage * 8,
                new Object[] { energyHatches_64A[i], new ItemStack(compactFusionCoil, 1, 3),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 64),
                    GT_OreDictUnificator.get("plateTripleShirabon", 64L),
                    GT_OreDictUnificator.get("plateDenseMetastableOganesson", 4), circuitsTierPlusTwo[i + 4],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64),
                    MaterialsUEVplus.ExcitedDTEC.getFluid(500L * 64) },
                wirelessHatches_64A[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ Wireless UXV Lasers ------------------------

        for (int i = 0; i < wirelessLasers.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                laserTargets_UXV[i],
                totalComputation * 64,
                compPerSecond * 64,
                researchEuPerTick * 4,
                researchAmperage * 16,
                new Object[] { laserTargets_UXV[i], new ItemStack(compactFusionCoil, 1, 4),
                    // Dyson Swarm Module Deployment Unit Superconducting Magnet
                    getModItem(GalaxySpace.ID, "dysonswarmparts", 1, 4),
                    CustomItemList.Machine_Multi_Transformer.get(1), CustomItemList.eM_Power.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt16, MaterialsUEVplus.SpaceTime, 64),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsUEVplus.Eternity, 32),
                    GT_OreDictUnificator
                        .get(OrePrefixes.plateDense, MaterialsUEVplus.MagnetohydrodynamicallyConstrainedStarMatter, 16),
                    GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.UXV, 16L),
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64 * 4),
                    MaterialsUEVplus.ExcitedDTSC.getFluid(500L * 64) },
                wirelessLasers[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }

        // ------------------------ 4MA+ Lasers ------------------------

        // 4M UMV Target
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel7_UMV.get(1),
            totalComputation * 48,
            compPerSecond * 48,
            researchEuPerTick * 3,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UMV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UMV.get(64),
                ItemList.Emitter_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 32) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64 * 4) },
            CustomItemList.eM_energyTunnel8_UMV.get(1),
            53 * MINUTES + 20 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // 4M UXV Target
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel8_UMV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64 * 4) },
            CustomItemList.eM_energyTunnel8_UXV.get(1),
            106 * MINUTES + 40 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 16M UXV Target
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_energyTunnel8_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64), ItemList.Sensor_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 128 * 4) },
            CustomItemList.eM_energyTunnel9_UXV.get(1),
            213 * MINUTES + 20 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 4M UMV Source
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel7_UMV.get(1),
            totalComputation * 48,
            compPerSecond * 48,
            researchEuPerTick * 3,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UMV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UMV.get(64),
                ItemList.Sensor_UMV.get(64), ItemList.Electric_Pump_UMV.get(64), ItemList.Electric_Pump_UMV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 32) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64 * 4) },
            CustomItemList.eM_dynamoTunnel8_UMV.get(1),
            53 * MINUTES + 20 * SECONDS,
            (int) TierEU.RECIPE_UMV);

        // 4M UXV Source
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel7_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UXV.get(64),
                ItemList.Sensor_UXV.get(64), ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 32) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 64 * 4) },
            CustomItemList.eM_dynamoTunnel8_UXV.get(1),
            106 * MINUTES + 40 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // 16M UXV Source
        TT_recipeAdder.addResearchableAssemblylineRecipe(
            CustomItemList.eM_dynamoTunnel8_UXV.get(1),
            totalComputation * 64,
            compPerSecond * 64,
            researchEuPerTick * 4,
            researchAmperage * 16,
            new Object[] { ItemList.Hull_UXV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64),
                GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UXV.get(64),
                ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64), ItemList.Emitter_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                ItemList.Electric_Pump_UXV.get(64), ItemList.Electric_Pump_UXV.get(64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.BlackPlutonium, 64) },
            new FluidStack[] { new FluidStack(solderUEV, 1_296 * 128 * 4) },
            CustomItemList.eM_dynamoTunnel9_UXV.get(1),
            213 * MINUTES + 20 * SECONDS,
            (int) TierEU.RECIPE_UXV);

        // ------------------------ Wireless EU dynamos ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                (i == 0) ? ItemList.EnergisedTesseract.get(1) : wirelessDynamos[i - 1],
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new Object[] { dynamoHatches[i], new ItemStack(compactFusionCoil, 1),
                    ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                    CustomItemList.eM_Power.get(2),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, MaterialsUEVplus.SpaceTime, 2),
                    GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1), circuitsTierPlusTwo[i],
                    ItemList.EnergisedTesseract.get(1) },
                new FluidStack[] { new FluidStack(solderUEV, 1296), MaterialsUEVplus.ExcitedDTEC.getFluid(500L) },
                wirelessDynamos[i],
                recipeDurationTicks,
                recipeEuPerTick);
        }
    }
}
