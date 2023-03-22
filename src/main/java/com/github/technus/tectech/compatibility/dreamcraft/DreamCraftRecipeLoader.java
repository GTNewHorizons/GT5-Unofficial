package com.github.technus.tectech.compatibility.dreamcraft;

import static com.github.technus.tectech.loader.recipe.BaseRecipeLoader.getOrDefault;
import static com.google.common.math.LongMath.pow;
import static gregtech.api.util.GT_ModHandler.getModItem;

import java.lang.reflect.Method;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import vexatos.tgregworks.reference.PartTypes;
import vexatos.tgregworks.util.TGregUtils;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry;
import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Centrifuge;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_ElectromagneticSeparator;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Recycler;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;

import cpw.mods.fml.common.Loader;
import gregtech.api.enums.*;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

/**
 * Created by Tec on 06.08.2017.
 */
public class DreamCraftRecipeLoader {

    // region reflect a bit
    @SuppressWarnings("rawtypes")
    private Class CUSTOM_ITEM_LIST;

    private Method ADD_ASSEMBLER_RECIPE;

    @SuppressWarnings("unchecked")
    private IItemContainer getItemContainer(String name) {
        return (IItemContainer) Enum.valueOf(CUSTOM_ITEM_LIST, name);
    }

    private void addAssemblerRecipeWithCleanroom(ItemStack[] items, FluidStack fluid, ItemStack output, int time,
            int eut) {
        try {
            ADD_ASSEMBLER_RECIPE.invoke(GT_Values.RA, items, fluid, output, time, eut, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to add clean room assembler recipe! " + output.getDisplayName(), e);
        }
    }
    // endregion

    public void run(EMTransformationRegistry transformationInfo) {
        // region reflect a bit
        try {
            CUSTOM_ITEM_LIST = Class.forName("com.dreammaster.gthandler.CustomItemList");
            ADD_ASSEMBLER_RECIPE = GT_Values.RA.getClass().getMethod(
                    "addAssemblerRecipe",
                    ItemStack[].class,
                    FluidStack.class,
                    ItemStack.class,
                    int.class,
                    int.class,
                    boolean.class);
        } catch (Exception e) {
            throw new Error(e);
        }
        // endregion

        Fluid solderIndalloy = FluidRegistry.getFluid("molten.indalloy140") != null
                ? FluidRegistry.getFluid("molten.indalloy140")
                : FluidRegistry.getFluid("molten.solderingalloy");
        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        // Quantum Glass
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Containment.get(1),
                        GT_ModHandler.getIC2Item("reinforcedGlass", 1L) },
                getOrDefault("Trinium", Materials.Osmium).getMolten(576),
                new ItemStack(QuantumGlassBlock.INSTANCE, 1),
                200,
                500000);

        // region pipes

        // Data
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { ItemList.Circuit_Parts_GlassFiber.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8) },
                Materials.Polytetrafluoroethylene.getMolten(144),
                CustomItemList.DATApipe.get(1),
                200,
                30720);

        // Data Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.DATApipeBlock.get(1),
                20,
                30720);

        // Tunnel
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2),
                        ItemList.Field_Generator_MV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1) },
                Materials.Osmium.getMolten(288),
                CustomItemList.EMpipe.get(1),
                400,
                500000);

        // Tunnel Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.EMpipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.EMpipeBlock.get(1),
                20,
                30720);

        // Laser
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.DATApipe.get(1), GT_ModHandler.getIC2Item("reinforcedGlass", 1L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 2) },
                null,
                CustomItemList.LASERpipe.get(1),
                100,
                500000);

        // Laser Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.LASERpipe.get(1), ItemList.Casing_LuV.get(1) },
                null,
                CustomItemList.LASERpipeBlock.get(1),
                20,
                30720);

        // endregion

        // region casing

        // High Power Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2) },
                Materials.TungstenSteel.getMolten(576),
                CustomItemList.eM_Power.get(1),
                100,
                30720);

        // Computer Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Power.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2) },
                Materials.Aluminium.getMolten(1296),
                CustomItemList.eM_Computer_Casing.get(1),
                200,
                122880);
        // Computer Vent Casing
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                        ItemList.Electric_Motor_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2),
                        GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 16),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                getOrDefault("SuperconductorIV", Materials.Superconductor),
                                1) },
                Materials.SolderingAlloy.getMolten(1296),
                CustomItemList.eM_Computer_Vent.get(1),
                100,
                1920);

        // Advanced Computer Casing
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt02,
                                getOrDefault("SuperconductorLuV", Materials.Superconductor),
                                4) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.eM_Computer_Bus.get(1),
                200,
                122880);

        // Molecular Casing
        GT_Values.RA
                .addAssemblerRecipe(
                        new ItemStack[] { CustomItemList.eM_Power.get(1),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                                GT_OreDictUnificator
                                        .get(OrePrefixes.foil, getOrDefault("Trinium", Materials.Osmium), 12),
                                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                                ItemList.Field_Generator_IV.get(1) },
                        Materials.Osmium.getMolten(1296),
                        CustomItemList.eM_Containment.get(1),
                        800,
                        500000);

        // Hollow Casing
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.eM_Containment.get(1),
                7500,
                new ItemStack[] { CustomItemList.eM_Containment.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Europium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Uranium, 16),
                        GT_OreDictUnificator
                                .get(OrePrefixes.screw, getOrDefault("Quantium", Materials.Europium), 16), },
                new FluidStack[] { getOrDefault("Trinium", Materials.Osmium).getMolten(1296),
                        Materials.Osmium.getMolten(1296), new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Argon.getGas(1000), },
                CustomItemList.eM_Hollow.get(2),
                200,
                200000);

        // EM Coil
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.eM_Hollow.get(1),
                7500,
                new ItemStack[] { CustomItemList.eM_Hollow.get(1), ItemList.Casing_Fusion_Coil.get(2),
                        ItemList.Casing_Coil_NaquadahAlloy.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Europium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Europium, 64), },
                new FluidStack[] { Materials.Glass.getMolten(2304), Materials.Silicone.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        getOrDefault("Trinium", Materials.Osmium).getMolten(1296), },
                CustomItemList.eM_Coil.get(4),
                800,
                200000);

        // Infinite Oil Rig
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.OilDrill4.get(1),
                16777216,
                2048,
                2000000,
                4,
                new Object[] { ItemList.OilDrill4.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        ItemList.Electric_Motor_UHV.get(4), ItemList.Electric_Pump_UHV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.Neutronium, 4),
                        ItemList.Sensor_UHV.get(3), ItemList.Field_Generator_UHV.get(3),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 12) },
                new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.Neutronium.getMolten(576) },
                ItemList.OilDrillInfinite.get(1),
                6000,
                2000000);

        // Infinity Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Casing_Coil_AwakenedDraconium.get(1),
                16_777_216,
                2048,
                8_000_000,
                1,
                new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Infinity, 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 8),
                        getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                        getModItem("dreamcraft", "item.MicaInsulatorFoil", 32, 0) },
                new FluidStack[] { Materials.DraconiumAwakened.getMolten(576), },
                ItemList.Casing_Coil_Infinity.get(1),
                60 * 20,
                8_000_000);

        if (Loader.isModLoaded(Reference.GTPLUSPLUS)) {
            // Hypogen Coil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Coil_Infinity.get(1),
                    16_777_216 * 2,
                    2048 * 2,
                    32_000_000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            GT_OreDictUnificator.get("wireGt02Hypogen", 8L),
                            getModItem("miscutils", "itemScrewHypogen", 8, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0), },
                    new FluidStack[] { Materials.Infinity.getMolten(576), },
                    ItemList.Casing_Coil_Hypogen.get(1),
                    60 * 20,
                    8000000 * 4);

            // Eternal coil
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Coil_Hypogen.get(1),
                    16_777_216 * 4,
                    8_192,
                    128_000_000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                            GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SpaceTime, 8),
                            GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 8),
                            getModItem("eternalsingularity", "eternal_singularity", 1L),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0),
                            getModItem("dreamcraft", "item.MicaInsulatorFoil", 64, 0), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("molten.hypogen"), 576), },
                    ItemList.Casing_Coil_Eternal.get(1),
                    60 * 20,
                    8_000_000 * 16);
        }

        // Tesla Base
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NickelZincFerrite, 1) },
                null,
                CustomItemList.tM_TeslaBase.get(1),
                50,
                16);
        GT_ModHandler.addCraftingRecipe(
                CustomItemList.tM_TeslaBase.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.plate.get(Materials.NickelZincFerrite), 'F',
                        OrePrefixes.frameGt.get(Materials.NickelZincFerrite) });
        // Tesla Toroid
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 6),
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1) },
                null,
                CustomItemList.tM_TeslaToroid.get(1),
                50,
                16);
        GT_ModHandler.addCraftingRecipe(
                CustomItemList.tM_TeslaToroid.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[] { "PhP", "PFP", "PwP", 'P', OrePrefixes.foil.get(Materials.Aluminium), 'F',
                        OrePrefixes.frameGt.get(Materials.Aluminium) });
        // Tesla Secondary Windings
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(8, 0),
                        getItemContainer("MicaInsulatorFoil").get(12) },
                Materials.Silver.getMolten(144),
                CustomItemList.tM_TeslaSecondary.get(1),
                200,
                120);
        // Tesla Primary Coils T0
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.RedstoneAlloy, 8),
                        getItemContainer("MicaInsulatorFoil").get(8) },
                Materials.RedAlloy.getMolten(144),
                CustomItemList.tM_TeslaPrimary_0.get(1),
                200,
                30);
        // Tesla Primary Coils T1
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorMV, 8),
                        getItemContainer("MicaInsulatorFoil").get(12) },
                Materials.Magnesium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_1.get(1),
                200,
                120);
        // Tesla Primary Coils T2
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorHV, 8),
                        getItemContainer("MicaInsulatorFoil").get(16) },
                Materials.Barium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_2.get(1),
                200,
                480);
        // Tesla Primary Coils T3
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorEV, 8),
                        getItemContainer("MicaInsulatorFoil").get(20) },
                Materials.Platinum.getMolten(144),
                CustomItemList.tM_TeslaPrimary_3.get(1),
                200,
                1920);
        // Tesla Primary Coils T4
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 8),
                        getItemContainer("MicaInsulatorFoil").get(24) },
                Materials.Vanadium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_4.get(1),
                200,
                7680);
        // Tesla Primary Coils T5
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 8),
                        getItemContainer("MicaInsulatorFoil").get(28) },
                Materials.Indium.getMolten(144),
                CustomItemList.tM_TeslaPrimary_5.get(1),
                200,
                30720);
        // Tesla Primary Coils T6
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorZPM, 8),
                        getItemContainer("MicaInsulatorFoil").get(32) },
                Materials.Naquadah.getMolten(144),
                CustomItemList.tM_TeslaPrimary_6.get(1),
                200,
                122880);

        // endregion

        // region hatches

        // Dynamo Hatches EV-UMV
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2) },
                Materials.Silver.getMolten(144),
                CustomItemList.eM_dynamoMulti4_EV.get(1),
                100,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_dynamoMulti4_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4) },
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_dynamoMulti16_EV.get(1),
                200,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_dynamoMulti16_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6) },
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_dynamoMulti64_EV.get(1),
                400,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2) },
                Materials.Silver.getMolten(144),
                CustomItemList.eM_dynamoMulti4_IV.get(1),
                100,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_dynamoMulti4_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4) },
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_dynamoMulti16_IV.get(1),
                200,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_LuV_IV").get(1),
                        CustomItemList.eM_dynamoMulti16_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6) },
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_dynamoMulti64_IV.get(1),
                400,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2) },
                Materials.Silver.getMolten(288),
                CustomItemList.eM_dynamoMulti4_LuV.get(1),
                100,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1), CustomItemList.eM_dynamoMulti4_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4) },
                Materials.Electrum.getMolten(288),
                CustomItemList.eM_dynamoMulti16_LuV.get(1),
                200,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                        CustomItemList.eM_dynamoMulti16_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6) },
                Materials.Tungsten.getMolten(288),
                CustomItemList.eM_dynamoMulti64_LuV.get(1),
                400,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2) },
                Materials.Silver.getMolten(576),
                CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                100,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4) },
                Materials.Electrum.getMolten(576),
                CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                200,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UV_ZPM").get(1),
                        CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6) },
                Materials.Tungsten.getMolten(576),
                CustomItemList.eM_dynamoMulti64_ZPM.get(1),
                400,
                30720);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2) },
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_dynamoMulti4_UV.get(1),
                100,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_dynamoMulti4_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4) },
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_dynamoMulti16_UV.get(1),
                200,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UHV_UV").get(1),
                        CustomItemList.eM_dynamoMulti16_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6) },
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_dynamoMulti64_UV.get(1),
                400,
                122880);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Dynamo_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2) },
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_dynamoMulti4_UHV.get(1),
                100,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                        CustomItemList.eM_dynamoMulti4_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4) },
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_dynamoMulti16_UHV.get(1),
                200,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UEV_UHV").get(1),
                        CustomItemList.eM_dynamoMulti16_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6) },
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_dynamoMulti64_UHV.get(1),
                400,
                2000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Dynamo_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2) },
                Materials.Silver.getMolten(4608),
                CustomItemList.eM_dynamoMulti4_UEV.get(1),
                100,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                        CustomItemList.eM_dynamoMulti4_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4) },
                Materials.Electrum.getMolten(4608),
                CustomItemList.eM_dynamoMulti16_UEV.get(1),
                200,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UIV_UEV").get(1),
                        CustomItemList.eM_dynamoMulti16_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6) },
                Materials.Tungsten.getMolten(4608),
                CustomItemList.eM_dynamoMulti64_UEV.get(1),
                400,
                2000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Dynamo_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2) },
                Materials.Silver.getMolten(9216),
                CustomItemList.eM_dynamoMulti4_UIV.get(1),
                100,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                        CustomItemList.eM_dynamoMulti4_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 4) },
                Materials.Electrum.getMolten(9216),
                CustomItemList.eM_dynamoMulti16_UIV.get(1),
                200,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UMV_UIV").get(1),
                        CustomItemList.eM_dynamoMulti16_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 6) },
                Materials.Tungsten.getMolten(9216),
                CustomItemList.eM_dynamoMulti64_UIV.get(1),
                400,
                8000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Dynamo_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2) },
                Materials.Silver.getMolten(9216),
                CustomItemList.eM_dynamoMulti4_UMV.get(1),
                100,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UXV_UMV").get(1),
                        CustomItemList.eM_dynamoMulti4_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4) },
                Materials.Electrum.getMolten(9216),
                CustomItemList.eM_dynamoMulti16_UMV.get(1),
                200,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UXV_UMV").get(1),
                        CustomItemList.eM_dynamoMulti16_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6) },
                Materials.Tungsten.getMolten(9216),
                CustomItemList.eM_dynamoMulti64_UMV.get(1),
                400,
                32000000);

        // Energy Hatches EV-UIV
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 2) },
                Materials.Silver.getMolten(144),
                CustomItemList.eM_energyMulti4_EV.get(1),
                100,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_energyMulti4_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 4) },
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_energyMulti16_EV.get(1),
                200,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_energyMulti16_EV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Aluminium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Titanium, 6) },
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_energyMulti64_EV.get(1),
                400,
                480);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2) },
                Materials.Silver.getMolten(144),
                CustomItemList.eM_energyMulti4_IV.get(1),
                100,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_energyMulti4_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4) },
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_energyMulti16_IV.get(1),
                200,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_LuV_IV").get(1),
                        CustomItemList.eM_energyMulti16_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6) },
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_energyMulti64_IV.get(1),
                400,
                1920);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 2) },
                Materials.Silver.getMolten(288),
                CustomItemList.eM_energyMulti4_LuV.get(1),
                100,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1), CustomItemList.eM_energyMulti4_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 4) },
                Materials.Electrum.getMolten(288),
                CustomItemList.eM_energyMulti16_LuV.get(1),
                200,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                        CustomItemList.eM_energyMulti16_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome), 6) },
                Materials.Tungsten.getMolten(288),
                CustomItemList.eM_energyMulti64_LuV.get(1),
                400,
                7680);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2) },
                Materials.Silver.getMolten(576),
                CustomItemList.eM_energyMulti4_ZPM.get(1),
                100,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_energyMulti4_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4) },
                Materials.Electrum.getMolten(576),
                CustomItemList.eM_energyMulti16_ZPM.get(1),
                200,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UV_ZPM").get(1),
                        CustomItemList.eM_energyMulti16_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6) },
                Materials.Tungsten.getMolten(576),
                CustomItemList.eM_energyMulti64_ZPM.get(1),
                400,
                30720);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2) },
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_energyMulti4_UV.get(1),
                100,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_energyMulti4_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4) },
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_energyMulti16_UV.get(1),
                200,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UHV_UV").get(1),
                        CustomItemList.eM_energyMulti16_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6) },
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_energyMulti64_UV.get(1),
                400,
                122880);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Energy_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2) },
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_energyMulti4_UHV.get(1),
                100,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                        CustomItemList.eM_energyMulti4_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4) },
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_energyMulti16_UHV.get(1),
                200,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UEV_UHV").get(1),
                        CustomItemList.eM_energyMulti16_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6) },
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_energyMulti64_UHV.get(1),
                400,
                500000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Energy_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2) },
                Materials.Silver.getMolten(4608),
                CustomItemList.eM_energyMulti4_UEV.get(1),
                100,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                        CustomItemList.eM_energyMulti4_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4) },
                Materials.Electrum.getMolten(4608),
                CustomItemList.eM_energyMulti16_UEV.get(1),
                200,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UIV_UEV").get(1),
                        CustomItemList.eM_energyMulti16_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6) },
                Materials.Tungsten.getMolten(4608),
                CustomItemList.eM_energyMulti64_UEV.get(1),
                400,
                2000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Energy_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2) },
                Materials.Silver.getMolten(9216),
                CustomItemList.eM_energyMulti4_UIV.get(1),
                100,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                        CustomItemList.eM_energyMulti4_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 4) },
                Materials.Electrum.getMolten(9216),
                CustomItemList.eM_energyMulti16_UIV.get(1),
                200,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UMV_UIV").get(1),
                        CustomItemList.eM_energyMulti16_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 6) },
                Materials.Tungsten.getMolten(9216),
                CustomItemList.eM_energyMulti64_UIV.get(1),
                400,
                8000000);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hatch_Energy_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2) },
                Materials.Silver.getMolten(9216),
                CustomItemList.eM_energyMulti4_UMV.get(1),
                100,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Transformer_UXV_UMV").get(1),
                        CustomItemList.eM_energyMulti4_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4) },
                Materials.Electrum.getMolten(9216),
                CustomItemList.eM_energyMulti16_UMV.get(1),
                200,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_UXV_UMV").get(1),
                        CustomItemList.eM_energyMulti16_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Quantium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6) },
                Materials.Tungsten.getMolten(9216),
                CustomItemList.eM_energyMulti64_UMV.get(1),
                400,
                32000000);

        // Buck Converter IV-UIV
        if (Loader.isModLoaded("bartworks")) {
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_LuV_IV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Elite, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 2L, 2) },
                    Materials.TungstenSteel.getMolten(288),
                    CustomItemList.Machine_BuckConverter_IV.get(1),
                    100,
                    7680);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_ZPM_LuV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.plate,
                                    getOrDefault("Rhodium-PlatedPalladium", Materials.Chrome),
                                    2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NiobiumTitanium, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 2L, 3) },
                    new FluidStack(FluidRegistry.getFluid("molten.rhodium-plated palladium"), 288),
                    CustomItemList.Machine_BuckConverter_LuV.get(1),
                    100,
                    30720);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_UV_ZPM.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 2L, 4) },
                    Materials.Iridium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_ZPM.get(1),
                    100,
                    122880);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { ItemList.Transformer_MAX_UV.get(1), getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 2L, 5) },
                    Materials.Osmium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_UV.get(1),
                    100,
                    500000);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UEV_UHV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Infinite, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.ElectrumFlux, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 4L, 5) },
                    Materials.Neutronium.getMolten(288),
                    CustomItemList.Machine_BuckConverter_UHV.get(1),
                    100,
                    2000000);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UIV_UEV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 2),
                            GT_OreDictUnificator
                                    .get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 8L, 5) },
                    getOrDefault("Bedrockium", Materials.Neutronium).getMolten(288),
                    CustomItemList.Machine_BuckConverter_UEV.get(1),
                    100,
                    8000000);
            GT_Values.RA.addAssemblerRecipe(
                    new ItemStack[] { getItemContainer("Transformer_UMV_UIV").get(1),
                            getItemContainer("Display").get(1),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Optical, 2),
                            GT_OreDictUnificator
                                    .get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Neutronium), 2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 4),
                            getModItem("bartworks", "BW_GlasBlocks", 16L, 5) },
                    getOrDefault("BlackPlutonium", Materials.Neutronium).getMolten(288),
                    CustomItemList.Machine_BuckConverter_UIV.get(1),
                    200,
                    8000000);
        }
        // Laser Dynamo IV-UEV 256/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_IV.get(1),
                        ItemList.Electric_Pump_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_IV.get(1),
                1000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_LuV.get(1),
                        ItemList.Electric_Pump_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_LuV.get(1),
                1000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_ZPM.get(1),
                        ItemList.Electric_Pump_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_ZPM.get(1),
                1000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UV.get(1),
                        ItemList.Electric_Pump_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_UV.get(1),
                1000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UHV.get(1),
                        ItemList.Electric_Pump_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_UHV.get(1),
                1000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UEV.get(1),
                        ItemList.Electric_Pump_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_UEV.get(1),
                1000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UIV.get(1),
                        ItemList.Electric_Pump_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_UIV.get(1),
                1000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UMV.get(1),
                        ItemList.Electric_Pump_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_dynamoTunnel1_UMV.get(1),
                1000,
                128000000);

        // Laser Dynamo IV-UEV 1024/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_IV.get(2),
                        ItemList.Electric_Pump_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_IV.get(1),
                2000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_LuV.get(2),
                        ItemList.Electric_Pump_LuV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_LuV.get(1),
                2000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_ZPM.get(1),
                2000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UV.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_UV.get(1),
                2000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UHV.get(2),
                        ItemList.Electric_Pump_UHV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_UHV.get(1),
                2000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UEV.get(2),
                        ItemList.Electric_Pump_UEV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_UEV.get(1),
                2000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UIV.get(2),
                        ItemList.Electric_Pump_UIV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_UIV.get(1),
                2000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UMV.get(2),
                        ItemList.Electric_Pump_UMV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_dynamoTunnel2_UMV.get(1),
                2000,
                128000000);

        // Laser Dynamo IV-UEV 4096/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_IV.get(4),
                        ItemList.Electric_Pump_IV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_IV.get(1),
                4000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_LuV.get(4),
                        ItemList.Electric_Pump_LuV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_LuV.get(1),
                4000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_ZPM.get(4),
                        ItemList.Electric_Pump_ZPM.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_ZPM.get(1),
                4000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UV.get(4),
                        ItemList.Electric_Pump_UV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_UV.get(1),
                4000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UHV.get(4),
                        ItemList.Electric_Pump_UHV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_UHV.get(1),
                4000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UEV.get(4),
                        ItemList.Electric_Pump_UEV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_UEV.get(1),
                4000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UIV.get(4),
                        ItemList.Electric_Pump_UIV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_UIV.get(1),
                4000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UMV.get(4),
                        ItemList.Electric_Pump_UMV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_dynamoTunnel3_UMV.get(1),
                4000,
                128000000);

        // Laser Dynamo IV-UEV 16384/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_IV.get(8),
                        ItemList.Electric_Pump_IV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_IV.get(1),
                8000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_LuV.get(8),
                        ItemList.Electric_Pump_LuV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_LuV.get(1),
                8000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_ZPM.get(8),
                        ItemList.Electric_Pump_ZPM.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_ZPM.get(1),
                8000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UV.get(8),
                        ItemList.Electric_Pump_UV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_UV.get(1),
                8000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UHV.get(8),
                        ItemList.Electric_Pump_UHV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_UHV.get(1),
                8000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UEV.get(8),
                        ItemList.Electric_Pump_UEV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_UEV.get(1),
                8000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UIV.get(8),
                        ItemList.Electric_Pump_UIV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_UIV.get(1),
                8000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UMV.get(8),
                        ItemList.Electric_Pump_UMV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_dynamoTunnel4_UMV.get(1),
                8000,
                128000000);

        // Laser Dynamo IV-UEV 65536/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_IV.get(16),
                        ItemList.Electric_Pump_IV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_IV.get(1),
                16000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_LuV.get(16),
                        ItemList.Electric_Pump_LuV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_LuV.get(1),
                16000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_ZPM.get(16),
                        ItemList.Electric_Pump_ZPM.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_ZPM.get(1),
                16000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UV.get(16),
                        ItemList.Electric_Pump_UV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_UV.get(1),
                16000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UHV.get(16),
                        ItemList.Electric_Pump_UHV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_UHV.get(1),
                16000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UEV.get(16),
                        ItemList.Electric_Pump_UEV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_UEV.get(1),
                16000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UIV.get(16),
                        ItemList.Electric_Pump_UIV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_UIV.get(1),
                16000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UMV.get(16),
                        ItemList.Electric_Pump_UMV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_dynamoTunnel5_UMV.get(1),
                16000,
                128000000);

        // Laser Dynamo IV-UEV 262144/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_IV.get(32),
                        ItemList.Electric_Pump_IV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_IV.get(1),
                32000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_LuV.get(32),
                        ItemList.Electric_Pump_LuV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_LuV.get(1),
                32000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_ZPM.get(32),
                        ItemList.Electric_Pump_ZPM.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_ZPM.get(1),
                32000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UV.get(32),
                        ItemList.Electric_Pump_UV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_UV.get(1),
                32000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UHV.get(32),
                        ItemList.Electric_Pump_UHV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_UHV.get(1),
                32000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UEV.get(32),
                        ItemList.Electric_Pump_UEV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_UEV.get(1),
                32000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UIV.get(32),
                        ItemList.Electric_Pump_UIV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_UIV.get(1),
                32000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UMV.get(32),
                        ItemList.Electric_Pump_UMV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_dynamoTunnel6_UMV.get(1),
                32000,
                128000000);

        // Laser Dynamo IV-UEV 1048576/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_IV.get(64),
                        ItemList.Electric_Pump_IV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_IV.get(1),
                64000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_LuV.get(64),
                        ItemList.Electric_Pump_LuV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_LuV.get(1),
                64000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_ZPM.get(64),
                        ItemList.Electric_Pump_ZPM.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_ZPM.get(1),
                64000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UV.get(64),
                        ItemList.Electric_Pump_UV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_UV.get(1),
                64000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UHV.get(64),
                        ItemList.Electric_Pump_UHV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_UHV.get(1),
                64000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UEV.get(64),
                        ItemList.Electric_Pump_UEV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_UEV.get(1),
                64000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UIV.get(64),
                        ItemList.Electric_Pump_UIV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_UIV.get(1),
                64000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UMV.get(64),
                        ItemList.Electric_Pump_UMV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_dynamoTunnel7_UMV.get(1),
                64000,
                128000000);

        // Laser Target IV-UEV 256/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_IV.get(1),
                        ItemList.Electric_Pump_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_IV.get(1),
                1000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_LuV.get(1),
                        ItemList.Electric_Pump_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_LuV.get(1),
                1000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_ZPM.get(1),
                        ItemList.Electric_Pump_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_ZPM.get(1),
                1000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UV.get(1),
                        ItemList.Electric_Pump_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_UV.get(1),
                1000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UHV.get(1),
                        ItemList.Electric_Pump_UHV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_UHV.get(1),
                1000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UEV.get(1),
                        ItemList.Electric_Pump_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_UEV.get(1),
                1000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UIV.get(1),
                        ItemList.Electric_Pump_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NetherStar, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_UIV.get(1),
                1000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UMV.get(1),
                        ItemList.Electric_Pump_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Quantium, 2),
                        GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.eM_energyTunnel1_UMV.get(1),
                1000,
                128000000);

        // Laser Target IV-UEV 1024/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_IV.get(2),
                        ItemList.Electric_Pump_IV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_IV.get(1),
                2000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_LuV.get(2),
                        ItemList.Electric_Pump_LuV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_LuV.get(1),
                2000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_ZPM.get(2),
                        ItemList.Electric_Pump_ZPM.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_ZPM.get(1),
                2000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UV.get(2),
                        ItemList.Electric_Pump_UV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_UV.get(1),
                2000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UHV.get(2),
                        ItemList.Electric_Pump_UHV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_UHV.get(1),
                2000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UEV.get(2),
                        ItemList.Electric_Pump_UEV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_UEV.get(1),
                2000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UIV.get(2),
                        ItemList.Electric_Pump_UIV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_UIV.get(1),
                2000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UMV.get(2),
                        ItemList.Electric_Pump_UMV.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(2) },
                null,
                CustomItemList.eM_energyTunnel2_UMV.get(1),
                2000,
                128000000);

        // Laser Target IV-UEV 4096/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_IV.get(4),
                        ItemList.Electric_Pump_IV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_IV.get(1),
                4000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_LuV.get(4),
                        ItemList.Electric_Pump_LuV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_LuV.get(1),
                4000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_ZPM.get(4),
                        ItemList.Electric_Pump_ZPM.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_ZPM.get(1),
                4000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UV.get(4),
                        ItemList.Electric_Pump_UV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_UV.get(1),
                4000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UHV.get(4),
                        ItemList.Electric_Pump_UHV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_UHV.get(1),
                4000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UEV.get(4),
                        ItemList.Electric_Pump_UEV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_UEV.get(1),
                4000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UIV.get(4),
                        ItemList.Electric_Pump_UIV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_UIV.get(1),
                4000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UMV.get(4),
                        ItemList.Electric_Pump_UMV.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 4),
                        GT_Utility.getIntegratedCircuit(3) },
                null,
                CustomItemList.eM_energyTunnel3_UMV.get(1),
                4000,
                128000000);

        // Laser Target IV-UEV 16384/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_IV.get(8),
                        ItemList.Electric_Pump_IV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_IV.get(1),
                8000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_LuV.get(8),
                        ItemList.Electric_Pump_LuV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_LuV.get(1),
                8000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_ZPM.get(8),
                        ItemList.Electric_Pump_ZPM.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_ZPM.get(1),
                8000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UV.get(8),
                        ItemList.Electric_Pump_UV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_UV.get(1),
                8000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UHV.get(8),
                        ItemList.Electric_Pump_UHV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_UHV.get(1),
                8000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UEV.get(8),
                        ItemList.Electric_Pump_UEV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_UEV.get(1),
                8000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UIV.get(8),
                        ItemList.Electric_Pump_UIV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_UIV.get(1),
                8000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UMV.get(8),
                        ItemList.Electric_Pump_UMV.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(4) },
                null,
                CustomItemList.eM_energyTunnel4_UMV.get(1),
                8000,
                128000000);

        // Laser Target IV-UEV 65536/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_IV.get(16),
                        ItemList.Electric_Pump_IV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_IV.get(1),
                16000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_LuV.get(16),
                        ItemList.Electric_Pump_LuV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_LuV.get(1),
                16000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_ZPM.get(16),
                        ItemList.Electric_Pump_ZPM.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_ZPM.get(1),
                16000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UV.get(16),
                        ItemList.Electric_Pump_UV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_UV.get(1),
                16000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UHV.get(16),
                        ItemList.Electric_Pump_UHV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_UHV.get(1),
                16000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UEV.get(16),
                        ItemList.Electric_Pump_UEV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_UEV.get(1),
                16000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UIV.get(16),
                        ItemList.Electric_Pump_UIV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_UIV.get(1),
                16000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UMV.get(16),
                        ItemList.Electric_Pump_UMV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 8),
                        GT_Utility.getIntegratedCircuit(5) },
                null,
                CustomItemList.eM_energyTunnel5_UMV.get(1),
                16000,
                128000000);

        // Laser Target IV-UEV 262144/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_IV.get(32),
                        ItemList.Electric_Pump_IV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_IV.get(1),
                32000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_LuV.get(32),
                        ItemList.Electric_Pump_LuV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_LuV.get(1),
                32000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_ZPM.get(32),
                        ItemList.Electric_Pump_ZPM.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_ZPM.get(1),
                32000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UV.get(32),
                        ItemList.Electric_Pump_UV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_UV.get(1),
                32000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UHV.get(32),
                        ItemList.Electric_Pump_UHV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_UHV.get(1),
                32000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UEV.get(32),
                        ItemList.Electric_Pump_UEV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_UEV.get(1),
                32000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UIV.get(32),
                        ItemList.Electric_Pump_UIV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_UIV.get(1),
                32000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UMV.get(32),
                        ItemList.Electric_Pump_UMV.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(6) },
                null,
                CustomItemList.eM_energyTunnel6_UMV.get(1),
                32000,
                128000000);

        // Laser Target IV-UEV 1048576/t
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_IV.get(64),
                        ItemList.Electric_Pump_IV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_IV.get(1),
                64000,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_LuV.get(64),
                        ItemList.Electric_Pump_LuV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_LuV.get(1),
                64000,
                30720);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_ZPM.get(64),
                        ItemList.Electric_Pump_ZPM.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_ZPM.get(1),
                64000,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UV.get(64),
                        ItemList.Electric_Pump_UV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_UV.get(1),
                64000,
                500000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hull_MAX.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UHV.get(64),
                        ItemList.Electric_Pump_UHV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_UHV.get(1),
                64000,
                2000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UEV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UEV.get(64),
                        ItemList.Electric_Pump_UEV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_UEV.get(1),
                64000,
                8000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UIV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UIV.get(64),
                        ItemList.Electric_Pump_UIV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NetherStar, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_UIV.get(1),
                64000,
                32000000);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("Hull_UMV").get(1),
                        GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UMV.get(64),
                        ItemList.Electric_Pump_UMV.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Quantium, 16),
                        GT_Utility.getIntegratedCircuit(7) },
                null,
                CustomItemList.eM_energyTunnel7_UMV.get(1),
                64000,
                128000000);

        // Data Input
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1), ItemList.Hatch_Input_Bus_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(2) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.dataIn_Hatch.get(1),
                200,
                122880);
        // Data Output
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1), ItemList.Hatch_Output_Bus_LuV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(2) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.dataOut_Hatch.get(1),
                200,
                122880);

        // Rack
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Bus.get(1), ItemList.Hatch_Input_Bus_ZPM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        CustomItemList.DATApipe.get(4) },
                Materials.Iridium.getMolten(1296),
                CustomItemList.rack_Hatch.get(1),
                800,
                122880);

        // Object Holder
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                10000,
                new Object[] { ItemList.Hatch_Input_Bus_ZPM.get(1), CustomItemList.eM_Computer_Bus.get(1),
                        ItemList.Emitter_ZPM.get(8), ItemList.Robot_Arm_ZPM.get(1), ItemList.Electric_Motor_ZPM.get(2),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1 },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2) },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16) },
                        CustomItemList.DATApipe.get(2), },
                new FluidStack[] { Materials.UUMatter.getFluid(500), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000) },
                CustomItemList.holder_Hatch.get(1),
                1200,
                100000);

        // Parameterizer
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                        CustomItemList.DATApipe.get(4), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(1), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.Parametrizer_Hatch.get(1),
                800,
                122880);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        CustomItemList.DATApipe.get(6), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 32), GT_Utility.getIntegratedCircuit(2), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.ParametrizerX_Hatch.get(1),
                800,
                122880);

        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1), CustomItemList.DATApipe.get(8),
                        ItemList.Cover_Screen.get(2), new ItemStack(Blocks.stone_button, 64),
                        GT_Utility.getIntegratedCircuit(3), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.ParametrizerTXT_Hatch.get(1),
                800,
                122880);
        // Uncertainty
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                        CustomItemList.DATApipe.get(16), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(4), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.Uncertainty_Hatch.get(1),
                1200,
                122880);

        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Computer_Casing.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Bio, 1),
                        CustomItemList.DATApipe.get(32), ItemList.Cover_Screen.get(1),
                        new ItemStack(Blocks.stone_button, 16), GT_Utility.getIntegratedCircuit(5), },
                Materials.Iridium.getMolten(2592),
                CustomItemList.UncertaintyX_Hatch.get(1),
                1200,
                122880);

        // Elemental Input
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Input_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                        ItemList.Sensor_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_in_UV.get(1),
                800,
                500000);
        // Elemental Output
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Output_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                        ItemList.Emitter_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_out_UV.get(1),
                800,
                500000);
        // Overflow
        addAssemblerRecipeWithCleanroom(
                new ItemStack[] { CustomItemList.eM_Containment.get(1), ItemList.Hatch_Muffler_UV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1),
                        ItemList.Field_Generator_UV.get(1) },
                Materials.Osmiridium.getMolten(1296),
                CustomItemList.eM_muffler_UV.get(1),
                800,
                500000);

        // Capacitor Hatch
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Input_Bus_HV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4), },
                Materials.Silver.getMolten(576),
                CustomItemList.capacitor_Hatch.get(1),
                800,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Hatch_Output_Bus_HV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Gold, 4), },
                Materials.Silver.getMolten(576),
                CustomItemList.capacitor_Hatch.get(1),
                800,
                480);

        // endregion

        // region multiblocks

        // Tesla Tower
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_ModHandler.getIC2Item("teslaCoil", 1), CustomItemList.tM_TeslaSecondary.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                        ItemList.Upgrade_Overclocker.get(4), },
                Materials.Silver.getMolten(576),
                CustomItemList.Machine_Multi_TeslaCoil.get(1),
                800,
                480);

        // Microwave Grinder
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Machine_HV_Microwave.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 4),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 16),
                        ItemList.Upgrade_Overclocker.get(4), },
                Materials.Copper.getMolten(576),
                CustomItemList.Machine_Multi_Microwave.get(1),
                800,
                480);

        // Active Transformer
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { getItemContainer("WetTransformer_ZPM_LuV").get(1),
                        getItemContainer("HighEnergyFlowCircuit").get(1),
                        GT_OreDictUnificator.get(
                                OrePrefixes.wireGt01,
                                getOrDefault("SuperconductorLuV", Materials.Superconductor),
                                16),
                        ItemList.valueOf("Circuit_Chip_UHPIC").get(2), },
                Materials.TungstenSteel.getMolten(576),
                CustomItemList.Machine_Multi_Transformer.get(1),
                400,
                30720);

        // Network Switch
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 64),
                        CustomItemList.DATApipe.get(4), },
                Materials.Iridium.getMolten(1296),
                CustomItemList.Machine_Multi_Switch.get(1),
                800,
                122880);

        // Quantum Computer
        GT_Values.RA.addAssemblylineRecipe(
                ItemList.Tool_DataOrb.get(1),
                20000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 2 },
                        ItemList.Tool_DataOrb.get(1), ItemList.Cover_Screen.get(1),
                        new ItemStack[] {
                                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8) },
                        CustomItemList.DATApipe.get(8), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Hydrogen.getGas(1000), },
                CustomItemList.Machine_Multi_Computer.get(1),
                12000,
                100000);

        // Research Station
        GT_Values.RA.addAssemblylineRecipe(
                getItemContainer("ScannerZPM").get(1),
                80000,
                new Object[] { CustomItemList.Machine_Multi_Switch.get(1), ItemList.Sensor_ZPM.get(8),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4 },
                        ItemList.Field_Generator_ZPM.get(1), ItemList.Electric_Motor_ZPM.get(2),
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4) },
                        new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32) },
                        CustomItemList.DATApipe.get(16), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Iridium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_Research.get(1),
                12000,
                100000);

        // Matter Junction
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_Switch.get(1),
                8000,
                32,
                500000,
                4,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Robot_Arm_LuV.get(2), ItemList.Electric_Piston_LuV.get(2),
                        new Object[] { "circuitSuperconductor", 2 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 4), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMjunction.get(1),
                12000,
                100000);

        // Matter Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Input_UV.get(1),
                12000,
                32,
                500000,
                6,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Emitter_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_MatterToEM.get(1),
                12000,
                100000);

        // Matter DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Output_UV.get(1),
                12000,
                32,
                500000,
                6,
                new Object[] { CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Sensor_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(1000), Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMToMatter.get(1),
                12000,
                100000);

        // Essentia Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_MatterToEM.get(1),
                15000,
                32,
                500000,
                8,
                new Object[] { CustomItemList.Machine_Multi_MatterToEM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Emitter_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EssentiaToEM.get(1),
                24000,
                500000);

        // Essentia DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_EMToMatter.get(1),
                15000,
                32,
                500000,
                8,
                new Object[] { CustomItemList.Machine_Multi_EMToMatter.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Sensor_UV.get(2), new Object[] { "circuitSuperconductor", 1 },
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000), Materials.Osmium.getMolten(1296), },
                CustomItemList.Machine_Multi_EMToEssentia.get(1),
                24000,
                500000);

        // EM Scanner
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.Machine_Multi_Research.get(1),
                150000,
                128,
                500000,
                16,
                new Object[] { CustomItemList.Machine_Multi_EMjunction.get(1), CustomItemList.eM_Computer_Bus.get(4),
                        ItemList.Field_Generator_UV.get(4), ItemList.Sensor_UV.get(4),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getItemContainer("MysteriousCrystalLens").get(4),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 4), },
                new FluidStack[] { Materials.UUMatter.getFluid(2000), Materials.Neutronium.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        Materials.Osmiridium.getMolten(1296), },
                CustomItemList.Machine_Multi_Scanner.get(1),
                24000,
                500000);

        // Multi Infuser
        GT_Values.RA.addAssemblylineRecipe(
                CustomItemList.Machine_Multi_Transformer.get(1),
                7500,
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1), CustomItemList.eM_Coil.get(8),
                        CustomItemList.eM_Power.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16), },
                new FluidStack[] { Materials.Electrum.getMolten(2592), Materials.Europium.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000), },
                CustomItemList.Machine_Multi_Infuser.get(1),
                8000,
                200000);

        itemPartsUHVAsslineRecipes();
        itemPartsUEVAsslineRecipes();
        itemPartsUIVAsslineRecipes();
        itemPartsUMVAsslineRecipes();
        itemPartsUXVAsslineRecipes();

        addWirelessEnergyRecipes();

        if (Loader.isModLoaded("TGregworks")) {
            addEOHRecipes();
        }

        // UHV-UMV Energy Hatch & Dynamo
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Energy_UV.get(1L),
                24000,
                16,
                50000,
                2,
                new Object[] { ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(2L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L }, ItemList.UHV_Coil.get(2L),
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
                ItemList.Hatch_Energy_MAX.get(1L),
                1000,
                2000000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Dynamo_UV.get(1L),
                48000,
                32,
                100000,
                4,
                new Object[] { ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
                        ItemList.Circuit_Chip_QPIC.get(2L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L }, ItemList.UHV_Coil.get(2L),
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
                ItemList.Hatch_Dynamo_MAX.get(1L),
                1000,
                2000000);

        // UEV Dynamo / energy hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Energy_MAX.get(1L),
                48000,
                32,
                100000,
                4,
                new Object[] { getItemContainer("Hull_UEV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                        new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                getItemContainer("Hatch_Energy_UEV").get(1L),
                1000,
                8000000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_Dynamo_MAX.get(1L),
                96000,
                64,
                200000,
                8,
                new Object[] { getItemContainer("Hull_UEV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUEVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L },
                        ItemList.UHV_Coil.get(4L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UEV.get(1L) },
                new FluidStack[] { new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000),
                        new FluidStack(solderUEV, 20 * 144), Materials.UUMatter.getFluid(8000L) },
                getItemContainer("Hatch_Dynamo_UEV").get(1L),
                1000,
                8000000);

        // UIV Dynamo/energy hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Energy_UEV").get(1L),
                96_000,
                64,
                200_000,
                8,
                new Object[] { getItemContainer("Hull_UIV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUIV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L }, ItemList.UHV_Coil.get(8L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UIV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L), new FluidStack(solderUEV, 20 * 144),
                        Materials.UUMatter.getFluid(16_000L) },
                getItemContainer("Hatch_Energy_UIV").get(1L),
                1000,
                32_000_000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Dynamo_UEV").get(1L),
                192_000,
                128,
                400_000,
                16,
                new Object[] { getItemContainer("Hull_UIV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUIVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L }, ItemList.UHV_Coil.get(8L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UIV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(16_000L), new FluidStack(solderUEV, 20 * 144),
                        Materials.UUMatter.getFluid(16_000L) },
                getItemContainer("Hatch_Dynamo_UIV").get(1L),
                1000,
                32_000_000);

        // UMV Dynamo/energy hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Energy_UIV").get(1L),
                192000,
                128,
                400000,
                16,
                new Object[] { getItemContainer("Hull_UMV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUMV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(4L), getItemContainer("PikoCircuit").get(2),
                        ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UMV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L), new FluidStack(solderUEV, 40 * 144),
                        Materials.UUMatter.getFluid(32000L) },
                getItemContainer("Hatch_Energy_UMV").get(1L),
                1000,
                128_000_000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Dynamo_UIV").get(1L),
                384000,
                256,
                800000,
                32,
                new Object[] { getItemContainer("Hull_UMV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 8L),
                        ItemList.Circuit_Chip_QPIC.get(4L), getItemContainer("PikoCircuit").get(2),
                        ItemList.UHV_Coil.get(16L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Electric_Pump_UMV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(32_000L), new FluidStack(solderUEV, 40 * 144),
                        Materials.UUMatter.getFluid(32000L) },
                getItemContainer("Hatch_Dynamo_UMV").get(1L),
                1000,
                128_000_000);

        // UXV Dynamo/energy hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Energy_UMV").get(1L),
                384000,
                256,
                800000,
                32,
                new Object[] { getItemContainer("Hull_UXV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUMV, 4L),
                        ItemList.Circuit_Chip_QPIC.get(16L), getItemContainer("QuantumCircuit").get(2),
                        ItemList.UHV_Coil.get(32L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L), new FluidStack(solderUEV, 80 * 144),
                        Materials.UUMatter.getFluid(64000L) },
                getItemContainer("Hatch_Energy_UXV").get(1L),
                1000,
                512_000_000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("Hatch_Dynamo_UMV").get(1L),
                384000,
                256,
                800000,
                32,
                new Object[] { getItemContainer("Hull_UXV").get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.SuperconductorUMVBase, 16L),
                        ItemList.Circuit_Chip_QPIC.get(16L), getItemContainer("QuantumCircuit").get(2),
                        ItemList.UHV_Coil.get(32L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Electric_Pump_UXV.get(1L) },
                new FluidStack[] { Materials.SuperCoolant.getFluid(64_000L), new FluidStack(solderUEV, 80 * 144),
                        Materials.UUMatter.getFluid(64000L) },
                getItemContainer("Hatch_Dynamo_UXV").get(1L),
                1000,
                512_000_000);

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
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                        ItemList.Circuit_Chip_Ram.get(48L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 10000), Materials.Radon.getGas(2500L), },
                ItemList.Circuit_Wetwaremainframe.get(1L),
                2000,
                300000);

        // Bioware circuits.
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_Biowarecomputer.get(1L),
                48000,
                128,
                500000,
                8,
                new Object[] { ItemList.Circuit_Board_Bio_Ultra.get(2L), ItemList.Circuit_Biowarecomputer.get(2L),
                        new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(16L),
                                ItemList.Circuit_Parts_TransistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(16L),
                                ItemList.Circuit_Parts_ResistorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(16L),
                                ItemList.Circuit_Parts_CapacitorXSMD.get(4L) },
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(16L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(4L) },
                        ItemList.Circuit_Chip_NOR.get(32L), ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32L),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.BioMediumSterilized.getFluid(1440L),
                        Materials.SuperCoolant.getFluid(10_000L), },
                ItemList.Circuit_Biowaresupercomputer.get(1L),
                4000,
                500000);

        // Bio
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
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(24L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(6L) },
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.BioMediumSterilized.getFluid(2880L),
                        Materials.SuperCoolant.getFluid(20_000L), },
                ItemList.Circuit_Biomainframe.get(1L),
                6000,
                2000000);

        // Optical Assembly
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalProcessor.get(1L),
                192_000,
                256,
                2_000_000,
                16,
                new Object[] { ItemList.Circuit_Board_Optical.get(1L), ItemList.Circuit_OpticalProcessor.get(2L),
                        ItemList.Circuit_Parts_InductorXSMD.get(16L), ItemList.Circuit_Parts_CapacitorXSMD.get(20L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(20L), ItemList.Circuit_Chip_NOR.get(32L),
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedwireFine", 24L, 10101), // Fine Lumiium
                        // Wire
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L }, },
                new FluidStack[] { new FluidStack(solderUEV, 1440), Materials.Radon.getPlasma(1440L),
                        Materials.SuperCoolant.getFluid(10_000L),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500) },
                ItemList.Circuit_OpticalAssembly.get(1L),
                20 * 20,
                2_000_000);

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
                        GT_ModHandler.getModItem("bartworks", "gt.bwMetaGeneratedwireFine", 32L, 10101), // Fine Lumiium
                        // Wire
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64), },
                new FluidStack[] { new FluidStack(solderUEV, 1440 * 2), Materials.Radon.getPlasma(1440L * 2),
                        Materials.SuperCoolant.getFluid(10_000L * 2),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 2) },
                ItemList.Circuit_OpticalComputer.get(1L),
                200 * 20,
                2_000_000);

        // Optical Mainframe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalComputer.get(1L),
                768_000,
                1024,
                8_000_000,
                64,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 8),
                        ItemList.Circuit_OpticalComputer.get(2L), ItemList.Circuit_Parts_InductorXSMD.get(32L),
                        ItemList.Circuit_Parts_TransistorXSMD.get(32L), ItemList.Circuit_Parts_ResistorXSMD.get(32L),
                        ItemList.Circuit_Parts_CapacitorXSMD.get(32L), ItemList.Circuit_Parts_DiodeXSMD.get(32L),
                        ItemList.Circuit_Chip_SoC2.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUEV, 64),
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        new Object[] { OrePrefixes.foil.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 1440 * 4), Materials.Radon.getPlasma(1440L * 4),
                        Materials.SuperCoolant.getFluid(10_000L * 4),
                        new FluidStack(FluidRegistry.getFluid("oganesson"), 500 * 4) },
                ItemList.Circuit_OpticalMainframe.get(1L),
                300 * 20,
                8_000_000);

        // GTNH UMV, UXV Circuits
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_OpticalMainframe.get(1L),
                384000,
                1024,
                4000000,
                64,
                new Object[] { ItemList.Circuit_Board_Optical.get(1L), getItemContainer("PicoWafer").get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                        ItemList.Circuit_Parts_TransistorXSMD.get(48L), ItemList.Circuit_Parts_ResistorXSMD.get(48L),
                        ItemList.Circuit_Parts_CapacitorXSMD.get(48L), ItemList.Circuit_Parts_DiodeXSMD.get(48L),
                        ItemList.Circuit_Chip_PPIC.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Osmium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Neutronium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 64) },
                new FluidStack[] { new FluidStack(solderUEV, 3744), Materials.UUMatter.getFluid(8000L),
                        Materials.Osmium.getMolten(1152L) },
                getItemContainer("PikoCircuit").get(1L),
                10000,
                128_000_000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getItemContainer("PikoCircuit").get(1L),
                720000,
                2048,
                8000000,
                128,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                        getItemContainer("PikoCircuit").get(2L), ItemList.Circuit_Parts_CapacitorXSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64L), ItemList.Circuit_Parts_TransistorXSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(64L), ItemList.Circuit_Chip_QPIC.get(64L),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Indium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 8),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 16) },
                new FluidStack[] { new FluidStack(solderUEV, 3744), Materials.UUMatter.getFluid(24000L),
                        Materials.Osmium.getMolten(2304L) },
                getItemContainer("QuantumCircuit").get(1L),
                20000,
                512_000_000);

        // Transcendent Plasma Mixer - TPM.
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                CustomItemList.eM_energyTunnel7_UV.get(1),
                32_000_000,
                4096,
                32_000_000,
                1,
                new Object[] { CustomItemList.eM_energyTunnel7_UV.get(32),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 32L },
                        ItemList.Electric_Pump_UIV.get(16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 64),

                        GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.TranscendentMetal, 16),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TranscendentMetal, 64),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 64),
                        ItemList.EnergisedTesseract.get(32),

                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                        ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L), },
                new FluidStack[] { Materials.ExcitedDTCC.getFluid(2_048_000), Materials.ExcitedDTPC.getFluid(2_048_000),
                        Materials.ExcitedDTRC.getFluid(2_048_000), Materials.ExcitedDTEC.getFluid(2_048_000), },
                ItemList.Machine_Multi_TranscendentPlasmaMixer.get(1),
                36_000,
                32_000_000);

        // Stargate Recipes
        if (Loader.isModLoaded("eternalsingularity") && Loader.isModLoaded("SGCraft")) {

            final int baseStargateTime = 125_000 * 20;

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GT_OreDictUnificator
                            .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                    (int) TierEU.RECIPE_MAX,
                    32768,
                    (int) TierEU.RECIPE_UXV,
                    64,
                    new ItemStack[] { ItemList.Casing_Dim_Bridge.get(64),
                            CustomItemList.StabilisationFieldGeneratorTier8.get(64),
                            GT_OreDictUnificator.get("blockShirabon", 64L),
                            GT_OreDictUnificator.get("blockShirabon", 64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.SpaceTime, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Quantum, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.SpaceTime, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Universium, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 8L),
                            GT_OreDictUnificator.get("plateDenseShirabon", 8L), ItemList.Sensor_UXV.get(16L),
                            ItemList.Emitter_UXV.get(16L), getModItem("eternalsingularity", "eternal_singularity", 16L),
                            Materials.Universium.getNanite(16), Materials.BlackDwarfMatter.getNanite(16),
                            Materials.WhiteDwarfMatter.getNanite(16) },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            Materials.SpaceTime.getMolten(4 * 36864L),
                            Materials.SuperconductorUMVBase.getMolten(4 * 36864L),
                            Materials.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateShieldingFoil").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getItemContainer("StargateShieldingFoil").get(1L),
                    (int) TierEU.RECIPE_MAX,
                    32_768,
                    (int) TierEU.RECIPE_UXV,
                    64,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.block, Materials.TranscendentMetal, 64L),
                            GT_OreDictUnificator.get("blockShirabon", 64),
                            CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),
                            CustomItemList.EOH_Reinforced_Spatial_Casing.get(64),

                            GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 16L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.frameGt,
                                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                    16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Jasper, 16L),

                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Opal, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Sapphire, 16L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.plateDense,
                                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                    8L),
                            GT_OreDictUnificator.get("plateDenseShirabon", 8),

                            ItemList.Electric_Motor_UXV.get(64L), ItemList.Electric_Piston_UXV.get(64L),
                            ItemList.Field_Generator_UXV.get(16L),
                            getItemContainer("QuantumCircuit").get(1L).splitStack(32) },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            Materials.SpaceTime.getMolten(4 * 36864L),
                            Materials.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(36864L),
                            Materials.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateChevron").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                    (int) TierEU.RECIPE_MAX,
                    32_768,
                    500_000_000,
                    64,
                    new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 64L),
                            GT_OreDictUnificator.get(
                                    OrePrefixes.stickLong,
                                    Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                    64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 64L),

                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmiridium, 64L),
                            GT_OreDictUnificator.get("stickLongShirabon", 64),
                            getModItem("bartworks", "gt.bwMetaGeneratedstickLong", 64L, 39),
                            getModItem("miscutils", "itemRodLongQuantum", 64L),

                            getModItem("miscutils", "itemRodLongHypogen", 64L),
                            getModItem("miscutils", "itemRodLongCelestialTungsten", 64L),
                            getModItem("bartworks", "gt.bwMetaGeneratedstickLong", 64L, 10106),
                            getModItem("miscutils", "itemRodLongAstralTitanium", 64L),

                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SuperconductorUMVBase, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Universium, 64L),
                            getModItem("miscutils", "itemRodLongAbyssalAlloy", 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 64L), },
                    new FluidStack[] { Materials.Neutronium.getMolten(32_768_000L),
                            Materials.SpaceTime.getMolten(4 * 36864L), Materials.Universium.getMolten(4 * 36864L),
                            Materials.ExcitedDTEC.getFluid(4 * 36864L) },
                    getItemContainer("StargateFramePart").get(1L),
                    baseStargateTime,
                    (int) TierEU.RECIPE_UMV);
        }

        // Dimensionally Transcendent Plasma Forge (DTPF)

        if (Loader.isModLoaded("eternalsingularity")) {

            // DTPF Controller.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Bridge.get(1),
                    32_000_000,
                    4096,
                    32_000_000,
                    1,
                    new Object[] { ItemList.Casing_Dim_Bridge.get(4),
                            getModItem("gregtech", "gt.blockmachines", 16L, 12730),
                            getItemContainer("Hatch_Energy_UEV").get(4L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUEV, 6),
                            ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                            ItemList.Reactor_Coolant_Sp_6.get(1L), ItemList.Reactor_Coolant_Sp_6.get(1L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Optical), 20L },
                            ItemList.Field_Generator_UEV.get(4),
                            getModItem("eternalsingularity", "eternal_singularity", 4L),
                            getModItem("miscutils", "MU-metaitem.01", 1L, 32105),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 64L),
                            ItemList.Electric_Pump_UEV.get(4), ItemList.ZPM3.get(1),
                            getModItem("IC2", "blockMachine2", 1, 0) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 128000),
                            new FluidStack(solderUEV, 36864 * 2),
                            new FluidStack(FluidRegistry.getFluid("molten.californium"), 36864),
                            Materials.NaquadahEnriched.getMolten(36864L) },
                    ItemList.Machine_Multi_PlasmaForge.get(1),
                    72000,
                    32_000_000);

            // Dimensional bridge.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Injector.get(1),
                    8_000_000,
                    4096,
                    32_000_000,
                    1,
                    new Object[] { ItemList.Casing_Dim_Trans.get(1), ItemList.MicroTransmitter_UV.get(1),
                            new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 2L },
                            getModItem("Avaritia", "Singularity", 2L, 0),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 6),
                            getModItem("dreamcraft", "item.PicoWafer", 2, 0), ItemList.Field_Generator_UHV.get(1L) },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 8000),
                            new FluidStack(solderUEV, 1152 * 8), Materials.NaquadahEnriched.getMolten(1296L) },
                    ItemList.Casing_Dim_Bridge.get(1),
                    240 * 20,
                    32_000_000);

            // Dimensional injection casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Dim_Trans.get(1),
                    2_000_000,
                    2048,
                    32_000_000,
                    1,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 4),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ledox, 1),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.CallistoIce, 1),
                            ItemList.Reactor_Coolant_Sp_6.get(1L), getModItem("miscutils", "itemScrewLaurenium", 12, 0),
                            new Object[] { OrePrefixes.circuit.get(Materials.Elite), 2L },
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 2),
                            ItemList.Super_Chest_IV.get(1), ItemList.Super_Tank_IV.get(1),
                            getModItem("dreamcraft", "item.PicoWafer", 1, 0), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 1000),
                            new FluidStack(solderUEV, 576), Materials.NaquadahEnriched.getMolten(288L) },
                    ItemList.Casing_Dim_Injector.get(1),
                    20 * 20,
                    32_000_000);

            // Dimensionally Transcendent Casing.
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getModItem("Avaritia", "Singularity", 1L, 0),
                    2_000_000,
                    2048,
                    32_000_000,
                    1,
                    new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmiridium, 6),
                            getModItem("miscutils", "itemScrewLaurenium", 12, 0), ItemList.Reactor_Coolant_Sp_6.get(1L),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 1), },
                    new FluidStack[] { new FluidStack(FluidRegistry.getFluid("oganesson"), 500),
                            new FluidStack(solderUEV, 288), Materials.NaquadahEnriched.getMolten(144L) },
                    ItemList.Casing_Dim_Trans.get(1),
                    20 * 20,
                    32_000_000);
        }

        Fluid oganesson = FluidRegistry.getFluid("oganesson");
        // Deep Dark Portal
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                getModItem("dreamcraft", "item.HeavyDutyPlateTier8", 1, 0),
                16_777_216,
                2048,
                2_000_000,
                64,
                new Object[] { getModItem("ExtraUtilities", "cobblestone_compressed", 1, 7),
                        getModItem("IC2", "blockMachine2", 1, 0),
                        GT_OreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                        getModItem("dreamcraft", "item.PicoWafer", 32, 0), ItemList.Robot_Arm_UMV.get(4),
                        ItemList.Emitter_UMV.get(4), ItemList.Sensor_UMV.get(4), },
                new FluidStack[] { oganesson != null ? new FluidStack(oganesson, 50000) : null,
                        Materials.Infinity.getMolten(144L * 512), Materials.Cheese.getMolten(232000L), },
                ItemList.Block_BedrockiumCompressed.get(1),
                10000,
                5000000);

        // Debug maintenance hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Hatch_AutoMaintenance.get(1L),
                2764800,
                128,
                500000,
                6,
                new Object[] { ItemList.Hatch_AutoMaintenance.get(1L), ItemList.Robot_Arm_UV.get(1L),
                        ItemList.Electric_Pump_UV.get(1L), ItemList.Conveyor_Module_UV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4L },
                        ItemList.Energy_LapotronicOrb2.get(1L), ItemList.Duct_Tape.get(64L),
                        ItemList.Duct_Tape.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Americium, 64L), },
                new FluidStack[] { Materials.Lubricant.getFluid(256000), new FluidStack(solderIndalloy, 1296), },
                CustomItemList.hatch_CreativeMaintenance.get(1),
                6000,
                500000);

        // Batteries
        // Alternate Energy Module Recipe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Energy_LapotronicOrb2.get(1L),
                128_000,
                64,
                2_000_000,
                16,
                new Object[] { ItemList.Circuit_Board_Wetware_Extreme.get(1),
                        new Object[] { OrePrefixes.foil.get(Materials.Bedrockium), 64L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 4 },
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_UHPIC.get(64L),
                        new ItemStack[] { ItemList.Circuit_Parts_DiodeASMD.get(64L),
                                ItemList.Circuit_Parts_DiodeXSMD.get(8L) },
                        new ItemStack[] { ItemList.Circuit_Parts_CapacitorASMD.get(64L),
                                ItemList.Circuit_Parts_CapacitorXSMD.get(8L) },
                        new ItemStack[] { ItemList.Circuit_Parts_ResistorASMD.get(64L),
                                ItemList.Circuit_Parts_ResistorXSMD.get(8L) },
                        new ItemStack[] { ItemList.Circuit_Parts_TransistorASMD.get(64L),
                                ItemList.Circuit_Parts_TransistorXSMD.get(8L) },
                        getModItem("miscutils", "itemFineWireHypogen", 48, 0) },
                new FluidStack[] { new FluidStack(solderUEV, 720) },
                ItemList.Energy_Module.get(1),
                50 * 20,
                320_000);
        // Alternate Energy Cluster Recipe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Energy_Module.get(1L),
                576_000,
                256,
                8_000_000,
                32,
                new Object[] { ItemList.Circuit_Board_Bio_Ultra.get(1),
                        new Object[] { OrePrefixes.foil.get(Materials.CosmicNeutronium), 64L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4L },
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_NPIC.get(64L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(32L), ItemList.Circuit_Parts_CapacitorXSMD.get(32L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(32L), ItemList.Circuit_Parts_TransistorXSMD.get(32L),
                        new Object[] { OrePrefixes.wireGt01.get(Materials.SpaceTime), 12L } },
                new FluidStack[] { new FluidStack(solderUEV, 1440) },
                ItemList.Energy_Cluster.get(1),
                50 * 20,
                (int) TierEU.RECIPE_UHV);

        // Ultimate Battery
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Energy_Cluster.get(1L),
                12000,
                16,
                100000,
                3,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 64L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L },
                        ItemList.Energy_Cluster.get(8L), ItemList.Field_Generator_UV.get(2),
                        ItemList.Circuit_Wafer_HPIC.get(64), ItemList.Circuit_Wafer_HPIC.get(64),
                        ItemList.Circuit_Parts_DiodeASMD.get(32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 32), },
                new FluidStack[] { new FluidStack(solderIndalloy, 2880),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000) },
                ItemList.ZPM2.get(1),
                3000,
                400000);
        // Alternate Ultimate Battery Recipe
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Circuit_Chip_PPIC.get(1L),
                2_304_000,
                1024,
                32_000_000,
                64,
                new Object[] { ItemList.Circuit_Board_Optical.get(1), GT_OreDictUnificator.get("foilShirabon", 64),
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L),
                        ItemList.Circuit_Parts_Crystal_Chip_Master.get(64L), ItemList.Circuit_Chip_PPIC.get(64L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64L), ItemList.Circuit_Parts_CapacitorXSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorXSMD.get(64L), ItemList.Circuit_Parts_TransistorXSMD.get(64L),
                        new Object[] { OrePrefixes.bolt.get(Materials.MagnetohydrodynamicallyConstrainedStarMatter),
                                4L } },
                new FluidStack[] { new FluidStack(solderUEV, 2880), Materials.WhiteDwarfMatter.getMolten(576),
                        Materials.BlackDwarfMatter.getMolten(576) },
                ItemList.ZPM2.get(1),
                50 * 20,
                (int) TierEU.RECIPE_UEV);
        // Really Ultimate Battery
        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.ZPM2.get(1L),
                24000,
                64,
                200000,
                6,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L }, ItemList.ZPM2.get(8),
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
                8_000_000,
                16,
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.InfinityCatalyst, 32L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L }, ItemList.ZPM3.get(8),
                        ItemList.Field_Generator_UEV.get(4), ItemList.Circuit_Wafer_PPIC.get(64),
                        ItemList.Circuit_Wafer_PPIC.get(64), ItemList.Circuit_Wafer_SoC2.get(64),
                        ItemList.Circuit_Parts_DiodeXSMD.get(64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUEV, 64), },
                new FluidStack[] { new FluidStack(solderUEV, 9216), Materials.Quantium.getMolten(18_432),
                        Materials.Naquadria.getMolten(9_216 * 2), Materials.SuperCoolant.getFluid(64_000) },
                ItemList.ZPM4.get(1),
                250 * 20,
                6_400_000);

        if (Loader.isModLoaded(Reference.GTPLUSPLUS)) {
            // Insanely Ultimate Battery
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.ZPM4.get(1),
                    24_000_000,
                    1_280,
                    32_000_000,
                    32,
                    new Object[] { ELEMENT.STANDALONE.HYPOGEN.getPlateDouble(32),
                            ELEMENT.STANDALONE.HYPOGEN.getPlateDouble(32),
                            new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, ItemList.ZPM4.get(8L),
                            ItemList.Field_Generator_UIV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                            ItemList.Circuit_Wafer_QPIC.get(64),
                            GT_ModHandler.getModItem("dreamcraft", "item.RawPicoWafer", 64),
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
                    128_000_000,
                    64,
                    new Object[] { ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                            ELEMENT.STANDALONE.DRAGON_METAL.getPlateDouble(32),
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 },
                            new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1 }, ItemList.ZPM5.get(8L),
                            ItemList.Field_Generator_UMV.get(4), ItemList.Circuit_Wafer_QPIC.get(64),
                            ItemList.Circuit_Wafer_QPIC.get(64),
                            GT_ModHandler.getModItem("dreamcraft", "item.PicoWafer", 64),
                            ItemList.Circuit_Parts_DiodeXSMD.get(64), ItemList.Circuit_Parts_InductorXSMD.get(64),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                    new FluidStack[] { new FluidStack(solderUEV, 36_864),
                            ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(36_864),
                            ELEMENT.STANDALONE.CELESTIAL_TUNGSTEN.getFluidStack(36_864),
                            Materials.SuperCoolant.getFluid(256_000) },
                    ItemList.ZPM6.get(1),
                    350 * 20,
                    (int) TierEU.RECIPE_UMV);

            // MK4 Computer
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    GregtechItemList.Compressed_Fusion_Reactor.get(1),
                    320000,
                    512,
                    2000000,
                    1,
                    new Object[] { GregtechItemList.Casing_Fusion_Internal.get(1),
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L },
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 4),
                            ItemList.Field_Generator_UHV.get(2), ItemList.Circuit_Wafer_QPIC.get(64),
                            GT_OreDictUnificator
                                    .get(OrePrefixes.wireGt04, Materials.Longasssuperconductornameforuhvwire, 32), },
                    new FluidStack[] { Materials.UUMatter.getFluid(50000), ALLOY.CINOBITE.getFluidStack(9216),
                            ALLOY.OCTIRON.getFluidStack(9216),
                            ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(9216), },
                    GregtechItemList.FusionComputer_UV2.get(1),
                    6000,
                    2000000);

            // MK4 Coils
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Fusion_Coil.get(1L),
                    160000,
                    512,
                    2000000,
                    1,
                    new Object[] { ItemList.Energy_LapotronicOrb2.get(16L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Master), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 8L },
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                            ItemList.Emitter_UHV.get(1), ItemList.Sensor_UHV.get(1),
                            ItemList.Casing_Fusion_Coil.get(1L), },
                    new FluidStack[] { Materials.UUMatter.getFluid(8000L), ALLOY.CINOBITE.getFluidStack(2304),
                            ALLOY.OCTIRON.getFluidStack(2304),
                            ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(2304), },
                    GregtechItemList.Casing_Fusion_Internal.get(1),
                    1200,
                    2000000);

            // MK4 Casing
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    ItemList.Casing_Fusion2.get(1L),
                    80000,
                    512,
                    2000000,
                    1,
                    new Object[] { new Object[] { OrePrefixes.circuit.get(Materials.Data), 16L },
                            new Object[] { OrePrefixes.circuit.get(Materials.Elite), 8L },
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.TungstenCarbide, 8),
                            GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8),
                            ItemList.Electric_Motor_UHV.get(2), ItemList.Electric_Piston_UHV.get(1),
                            ItemList.Casing_Fusion2.get(1L), },
                    new FluidStack[] { Materials.UUMatter.getFluid(1000L), ALLOY.CINOBITE.getFluidStack(576),
                            ALLOY.OCTIRON.getFluidStack(576), ELEMENT.STANDALONE.ASTRAL_TITANIUM.getFluidStack(576), },
                    GregtechItemList.Casing_Fusion_External.get(1),
                    300,
                    2000000);
        }

        // Draconic Evolution Fusion Crafter Controller
        if (Loader.isModLoaded("AWWayofTime") && Loader.isModLoaded("EMT")) {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    getModItem("EMT", "EMT_GTBLOCK_CASEING", 1, 8),
                    16_777_216,
                    1024,
                    2_000_000,
                    8,
                    new Object[] { getModItem("gregtech", "gt.blockmachines", 1, 10783),
                            getModItem("EMT", "EMT_GTBLOCK_CASEING", 1, 8),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, MaterialsBotania.GaiaSpirit, 1L),
                            ItemList.Casing_Coil_AwakenedDraconium.get(8L), ItemList.Electric_Motor_UHV.get(8L),
                            ItemList.Robot_Arm_UHV.get(4L),
                            new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4 },
                            ItemList.Gravistar.get(4, new Object() {}),
                            getModItem("Thaumcraft", "ItemEldritchObject", 1, 3),
                            getModItem("AWWayofTime", "bloodMagicBaseItems", 8, 29),
                            getModItem("AWWayofTime", "bloodMagicBaseItems", 8, 28), },
                    new FluidStack[] { new FluidStack(solderIndalloy, 2880), Materials.Void.getMolten(2880L),
                            Materials.DraconiumAwakened.getMolten(1440), },
                    getModItem("gregtech", "gt.blockmachines", 1, 5001),
                    1500,
                    8_000_000);
            if (Loader.isModLoaded("DraconicEvolution")) {
                // DE Schematics Cores Tier 1
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem("EMT", "EMT_GTBLOCK_CASEING", 1, 9),
                        5_000_000,
                        512,
                        1_000_000,
                        4,
                        new Object[] { getModItem("DraconicEvolution", "draconicCore", 1, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Ichorium, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1 }, },
                        new FluidStack[] { Materials.Sunnarium.getMolten(14400L), Materials.Void.getMolten(28800L), },
                        getModItem("EMT", "EMTItems", 1, 16),
                        6000,
                        500_000);

                // DE Schematics Cores Tier 2
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem("DraconicEvolution", "draconicCore", 1, 0),
                        10_000_000,
                        1024,
                        4_000_000,
                        8,
                        new Object[] { getModItem("DraconicEvolution", "draconicCore", 4, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Draconium, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1 }, },
                        new FluidStack[] { Materials.Neutronium.getMolten(14400L), Materials.Void.getMolten(57600L), },
                        getModItem("EMT", "EMTItems", 1, 17),
                        12000,
                        2_000_000);

                // DE Schematics Cores Tier 3
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem("DraconicEvolution", "wyvernCore", 1, 0),
                        20_000_000,
                        2048,
                        16_000_000,
                        16,
                        new Object[] { getModItem("DraconicEvolution", "wyvernCore", 4, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 1L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L } },
                        new FluidStack[] { Materials.Infinity.getMolten(14400L), Materials.Void.getMolten(115200L), },
                        getModItem("EMT", "EMTItems", 1, 18),
                        24000,
                        8_000_000);
                // DE Schematics Cores Tier 4
                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        getModItem("DraconicEvolution", "awakenedCore", 1, 0),
                        40_000_000,
                        4096,
                        64_000_000,
                        64,
                        new Object[] { getModItem("DraconicEvolution", "awakenedCore", 8, 0),
                                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.DraconiumAwakened, 4L),
                                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 1L),
                                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1 }, },
                        new FluidStack[] { Materials.SpaceTime.getMolten(14400L), Materials.Void.getMolten(230400L), },
                        getModItem("EMT", "EMTItems", 1, 19),
                        36000,
                        32_000_000);
            }
        }

        // region singleblocks

        // Tesla Transceiver LV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 1A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 4A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 9A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1),
                400,
                7680);
        // Tesla Transceiver LV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1),
                400,
                30);
        // Tesla Transceiver MV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1),
                400,
                120);
        // Tesla Transceiver HV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1),
                400,
                480);
        // Tesla Transceiver EV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1),
                400,
                1920);
        // Tesla Transceiver IV 16A
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCover.getWithDamage(1, 0) },
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1),
                400,
                7680);

        // endregion

        // region components

        // Tesla Winding Components
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 32),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8), },
                Materials.Epoxid.getMolten(288),
                CustomItemList.teslaComponent.getWithDamage(1, 0),
                320,
                30);
        // Tesla Winding Components Ultimate (ADD BLOOD VARIANT)
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorLuV, 4),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8), },
                Materials.Epoxid.getMolten(576),
                CustomItemList.teslaComponent.getWithDamage(1, 1),
                320,
                7680);

        // endregion

        // region items

        // Optical Processor
        GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { ItemList.Optically_Perfected_CPU.get(1L),
                        ItemList.Optically_Compatible_Memory.get(2L), ItemList.Circuit_Parts_CapacitorXSMD.get(16L),
                        ItemList.Circuit_Parts_DiodeXSMD.get(16L), CustomItemList.DATApipe.get(4L),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.EnrichedHolmium, 16) },
                new FluidStack(solderUEV, 288),
                ItemList.Circuit_OpticalProcessor.get(1L),
                20 * 20,
                614400,
                true);

        // Parametrizer Memory Card
        GT_Values.RA.addCircuitAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Basic, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Gold, 4), },
                Materials.Plastic.getMolten(72),
                CustomItemList.parametrizerMemory.get(1),
                200,
                480,
                true);

        // LV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 8), },
                Materials.Epoxid.getMolten(72),
                CustomItemList.teslaCapacitor.getWithDamage(1, 0),
                320,
                30);
        // MV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 12),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 12), },
                Materials.Epoxid.getMolten(144),
                CustomItemList.teslaCapacitor.getWithDamage(1, 1),
                320,
                120);
        // HV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 16), },
                Materials.Epoxid.getMolten(216),
                CustomItemList.teslaCapacitor.getWithDamage(1, 2),
                320,
                480);
        // EV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 20),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 20), },
                Materials.Epoxid.getMolten(288),
                CustomItemList.teslaCapacitor.getWithDamage(1, 3),
                320,
                1920);
        // IV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 24),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 24), },
                Materials.Epoxid.getMolten(360),
                CustomItemList.teslaCapacitor.getWithDamage(1, 4),
                320,
                7680);
        // LuV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.HSSG, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 28),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 28), },
                Materials.Epoxid.getMolten(432),
                CustomItemList.teslaCapacitor.getWithDamage(1, 5),
                320,
                30720);
        // ZPM Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Naquadah, 4),
                        GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 32), },
                Materials.Epoxid.getMolten(504),
                CustomItemList.teslaCapacitor.getWithDamage(1, 6),
                320,
                122880);
        // Tesla Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Lead.getMolten(288),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Tin.getMolten(144),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 0),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Gold, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.SolderingAlloy.getMolten(72),
                CustomItemList.teslaCover.getWithDamage(1, 0),
                320,
                480);
        // Ultimate Tesla Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Lead.getMolten(288),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.Tin.getMolten(144),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.teslaComponent.getWithDamage(4, 1),
                        GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 2),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tungsten, 16),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 2),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8), },
                Materials.SolderingAlloy.getMolten(72),
                CustomItemList.teslaCover.getWithDamage(1, 1),
                320,
                7680);

        // Ender Fluid Link Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Enderium, 4),
                        ItemList.Sensor_LuV.get(1), ItemList.Emitter_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), },
                Materials.Chrome.getMolten(288),
                CustomItemList.enderLinkFluidCover.getWithDamage(1, 0),
                320,
                30720);

        // Power Pass Upgrade Cover
        GT_Values.RA.addAssemblerRecipe(
                new ItemStack[] { CustomItemList.Machine_Multi_Transformer.get(1), GT_Utility.getIntegratedCircuit(1) },
                null,
                CustomItemList.powerPassUpgradeCover.getWithDamage(1, 0),
                320,
                30720);
        // endregion

        // region recycling

        // LV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 0),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                300,
                2);
        // MV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 1),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                300,
                2);
        // HV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 2),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                300,
                2);
        // EV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 3),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                300,
                2);
        // IV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                300,
                2);
        // LuV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 5),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 14),
                300,
                2);
        // ZPM Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(
                CustomItemList.teslaCapacitor.getWithDamage(1, 6),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 16),
                300,
                2);

        // endregion

        registerMachineEMBehaviours();
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
        int research_eu_per_tick = 500_000;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Naquadria.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(solderIndalloy, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 500_000;

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
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 8L },
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
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L), new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L), new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                        ItemList.Gravistar.get(4L), ItemList.Emitter_UHV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
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
        int research_eu_per_tick = 2_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = Materials.Quantium.getMolten(2592);
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 2_000_000;

        // -------------------------------------------------------------

        // ------------------------- UEV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UHV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 8L),
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
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 8L },
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
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 16L),
                        ItemList.Gravistar.get(16L), new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
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
                        ItemList.Electric_Motor_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 8L), ItemList.Gravistar.get(16),
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
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
                        ItemList.Emitter_UEV.get(4L), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
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
        int research_eu_per_tick = 8_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_1 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_2 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 8_000_000;

        // -------------------------------------------------------------

        // ------------------------- UIV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UEV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 32L),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TranscendentMetal, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.TranscendentMetal, 4L),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 64L),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TranscendentMetal, 6L),
                        ItemList.Electric_Motor_UIV.get(2L), ItemList.Electric_Piston_UIV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 8L },
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.TranscendentMetal, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.TranscendentMetal, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.TranscendentMetal, 4L),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                        ItemList.Electric_Motor_UIV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.TranscendentMetal, 16L),
                        ItemList.Gravistar.get(32L), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getModItem("miscutils", "itemFoilArceusAlloy2B", 64, 0),
                        getModItem("miscutils", "itemFoilLafiumCompound", 64, 0),
                        getModItem("miscutils", "itemFoilCinobiteA243", 64, 0),
                        getModItem("miscutils", "itemFoilPikyonium64B", 64, 0),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                        ItemList.Electric_Motor_UIV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 8L),
                        ItemList.Gravistar.get(32), new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        getModItem("miscutils", "itemFoilArceusAlloy2B", 64, 0),
                        getModItem("miscutils", "itemFoilLafiumCompound", 64, 0),
                        getModItem("miscutils", "itemFoilCinobiteA243", 64, 0),
                        getModItem("miscutils", "itemFoilPikyonium64B", 64, 0),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.TranscendentMetal, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TranscendentMetal, 6L),
                        ItemList.Gravistar.get(16L), ItemList.Emitter_UIV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4 },
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
        int research_eu_per_tick = 32_000_000;
        int research_amperage = 1;

        FluidStack fluid_0 = hypogen != null ? new FluidStack(hypogen, 576) : null;
        FluidStack fluid_1 = celestialTungsten != null ? new FluidStack(celestialTungsten, 576) : null;
        FluidStack fluid_2 = new FluidStack(mutated_living_solder, 2592);
        FluidStack fluid_3 = Materials.Lubricant.getFluid(4000);

        int crafting_time_in_ticks = 1000;
        int crafting_eu_per_tick = 32_000_000;

        // -------------------------------------------------------------

        // ------------------------- UMV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UIV.get(1L),
                total_computation,
                comp_per_second,
                research_eu_per_tick,
                research_amperage,
                new ItemStack[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 32L),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.SpaceTime, 16L),
                        new Object[] { OrePrefixes.ring.get(Materials.AnySyntheticRubber), 64L },
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.SpaceTime, 4L),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 64L),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 6L),
                        ItemList.Electric_Motor_UMV.get(2L), ItemList.Electric_Piston_UMV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Bio), 8L },
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.SpaceTime, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SpaceTime, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, 4L),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                        ItemList.Electric_Motor_UMV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.SpaceTime, 16L),
                        ItemList.Gravistar.get(64), new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        getModItem("miscutils", "itemFoilCelestialTungsten", 64, 0),
                        getModItem("miscutils", "itemFoilQuantum", 64, 0),
                        getModItem("miscutils", "itemFoilAstralTitanium", 64, 0),
                        getModItem("miscutils", "itemFoilTitansteel", 64, 0),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Quantium, 7L) },
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                        ItemList.Electric_Motor_UMV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 8L),
                        ItemList.Gravistar.get(64), new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        getModItem("miscutils", "itemFoilCelestialTungsten", 64, 0),
                        getModItem("miscutils", "itemFoilQuantum", 64, 0),
                        getModItem("miscutils", "itemFoilAstralTitanium", 64, 0),
                        getModItem("miscutils", "itemFoilTitansteel", 64, 0),
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
                new Object[] { GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SpaceTime, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.SpaceTime, 6L),
                        ItemList.Gravistar.get(32L), ItemList.Emitter_UMV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4 },
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
                        getModItem("miscutils", "itemFineWireHypogen", 64, 0),
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

        FluidStack moltenMHDCSM_576 = Materials.MagnetohydrodynamicallyConstrainedStarMatter.getMolten(576);
        FluidStack moltenSpaceTime_576 = Materials.SpaceTime.getMolten(576);
        FluidStack moltenUniversium_576 = Materials.Universium.getMolten(576);
        FluidStack lubricantFluid_8000 = Materials.Lubricant.getFluid(8000);
        FluidStack solderingAlloy_14_400 = new FluidStack(mutatedLivingSolder, 14_400);

        int totalComputation = 384_000;
        int compPerSecond = 512;
        int researchEuPerTick = 64_000_000;
        int researchAmperage = 2;

        int craftingTimeInTicks = 2000;
        int craftingEuPerTick = (int) TierEU.RECIPE_UXV;

        // -------------------------------------------------------------

        // ------------------------- UXV Motor -------------------------

        TT_recipeAdder.addResearchableAssemblylineRecipe(
                ItemList.Electric_Motor_UMV.get(1L),
                totalComputation,
                compPerSecond,
                researchEuPerTick,
                researchAmperage,
                new ItemStack[] { ItemList.EnergisedTesseract.get(1), GT_OreDictUnificator
                        .get(OrePrefixes.stickLong, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.ring, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.round, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 32L),

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),

                        GT_OreDictUnificator
                                .get(OrePrefixes.wireFine, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireFine, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),

                        GT_OreDictUnificator.get("wireFineShirabon", 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 2L),
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
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.SpaceTime, 2L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 4L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.screw, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, MaterialsKevlar.Kevlar, 64L),
                        GT_OreDictUnificator.get("ringRadoxPoly", 64L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.rotor, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 4L),
                        GT_OreDictUnificator.get("rotorShirabon", 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 2L),
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
                                .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.ring, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.round, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Infinity, 2L),
                        MaterialsKevlar.Kevlar.getPlates(64), MaterialsKevlar.Kevlar.getPlates(16),
                        GT_OreDictUnificator.get("plateRadoxPoly", 64L),
                        GT_OreDictUnificator.get("plateRadoxPoly", 16L), Materials.Neutronium.getNanite(4) },
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
                                .get(OrePrefixes.stickLong, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.gear, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                        GT_OreDictUnificator.get("gearGtShirabon", 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                6L),
                        GT_OreDictUnificator.get("gearGtSmallShirabon", 6L), ItemList.Electric_Motor_UXV.get(2L),
                        ItemList.Electric_Piston_UXV.get(1L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 2L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Piko), 4L },
                        new Object[] { OrePrefixes.circuit.get(Materials.Optical), 8L },
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 6L),
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
                                .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.ring, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.round, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.stick, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.gear, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 2L),
                        GT_OreDictUnificator.get("gearGtShirabon", 2L),
                        GT_OreDictUnificator.get(
                                OrePrefixes.gearGtSmall,
                                Materials.MagnetohydrodynamicallyConstrainedStarMatter,
                                4L),
                        GT_OreDictUnificator.get("gearGtSmallShirabon", 4L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 4L),
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
                                .get(OrePrefixes.frameGt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        ItemList.Electric_Motor_UXV.get(1L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.stick, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 16L),
                        ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L },
                        GT_OreDictUnificator
                                .get(OrePrefixes.foil, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SpaceTime, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Universium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 7L),
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
                                .get(OrePrefixes.frameGt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        ItemList.Electric_Motor_UXV.get(1L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8L),
                        ItemList.NuclearStar.get(16), new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L },
                        GT_OreDictUnificator
                                .get(OrePrefixes.foil, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64),
                        GT_OreDictUnificator.get("foilShirabon", 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.SpaceTime, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Universium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 7L),
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
                                .get(OrePrefixes.frameGt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 1L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.plate, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 6L),
                        ItemList.NuclearStar.get(64L), ItemList.Emitter_UXV.get(4L),
                        new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 8 },

                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.SuperconductorUMVBase, 64L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireFine, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                        GT_OreDictUnificator
                                .get(OrePrefixes.wireFine, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Universium, 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),
                        GT_OreDictUnificator.get("wireFineShirabon", 64L),

                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SpaceTime, 8L),
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

        ItemStack largeShirabonPlate = Loader.isModLoaded("TGregworks")
                ? TGregUtils.newItemStack(Materials.get("Shirabon"), PartTypes.LargePlate, 1)
                : GT_OreDictUnificator.get("plateDenseShirabon", 1);

        final FluidStack[] specialFluid = new FluidStack[] { Materials.SpaceTime.getMolten(1_440),
                Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
                Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
                Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440),
                Materials.SpaceTime.getMolten(1_440), Materials.SpaceTime.getMolten(1_440) };

        final ItemStack[] plateList = new ItemStack[] {
                // Dense Shirabon plate.
                GT_OreDictUnificator.get("boltShirabon", 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.WhiteDwarfMatter, 32),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 8),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.BlackDwarfMatter, 32),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 2),
                GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.MagnetohydrodynamicallyConstrainedStarMatter, 8) };

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
                            getModItem("gregtech", "gt.blockmachines", 16, 14003),
                            ItemList.Machine_Multi_PlasmaForge.get(4),

                            CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                            CustomItemList.TimeAccelerationFieldGeneratorTier0.get(1),
                            CustomItemList.SpacetimeCompressionFieldGeneratorTier0.get(1),
                            CustomItemList.StabilisationFieldGeneratorTier0.get(1),

                            CustomItemList.Machine_Multi_Computer.get(64),
                            // Ultimate Time Anomaly.
                            getModItem("gregtech", "gt.blockmachines", 64, 11107), ItemList.Quantum_Chest_IV.get(64),
                            // Void miner III.
                            getModItem("gregtech", "gt.blockmachines", 64, 12739),

                            ItemList.Field_Generator_UMV.get(16), ItemList.Robot_Arm_UMV.get(16), ItemList.ZPM4.get(4),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 64) },
                    new FluidStack[] { Materials.Time.getMolten(144_000), Materials.Space.getMolten(144_000),
                            FluidUtils.getFluidStack("molten.metastable oganesson", 144 * 256 * 4),
                            FluidUtils.getFluidStack("molten.shirabon", 144 * 256 * 4), },
                    CustomItemList.Machine_Multi_EyeOfHarmony.get(1),
                    1_000_000,
                    (int) TierEU.RECIPE_UMV);
        }

        // EOH Spatial Individual Casing
        {
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    // Dyson Swarm Module Deployment Unit Base Casing
                    getModItem("GalaxySpace", "dysonswarmparts", 1, 2),
                    256_000_000, // total comp
                    16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    32, // amperage
                    new Object[] {
                            // Space elevator blocks.
                            getModItem("gtnhintergalactic", "gt.blockcasingsSE", 64, 0),
                            // Cosmic neutronium block.
                            getModItem("Avaritia", "Resource_Block", 64, 0),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),

                            // Large Bedrockium Plate
                            getModItem("TConstruct", "heavyPlate", 1, 315),
                            // Large Cosmic Neutronium Plate
                            getModItem("TConstruct", "heavyPlate", 1, 500),
                            // Large Shirabon Plate
                            largeShirabonPlate,
                            // Large Infinity Plate
                            getModItem("TConstruct", "heavyPlate", 1, 501),

                            // UV Solar panel
                            getModItem("supersolarpanel", "PhotonicSolarPanel", 1, 0), ItemList.Quantum_Chest_IV.get(1),
                            // Gravitation Engine
                            getModItem("GraviSuite", "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                    new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 4),
                            new FluidStack(solderUEV, 144 * 256 * 2), Materials.Space.getMolten(1_440) },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier] },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Cosmic fabric manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 8), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Infinity infused manipulator
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 9), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, plateList[absoluteTier], getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
                                Materials.Space.getMolten(1_440 * (absoluteTier + 1)), specialFluid[absoluteTier], },
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
                                getModItem("GoodGenerator", "yottaFluidTankCells", tier, (5 + set)),
                                // quantum tank V (max tier)
                                ItemList.Quantum_Tank_IV.get(4 * (1 + absoluteTier)),
                                // Inf chest
                                getModItem("avaritiaddons", "InfinityChest", absoluteTier + 1),
                                // Spacetime continuum ripper
                                getModItem("miscutils", "gtplusplus.blockcasings.5", tier, 10), ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity, ME_Singularity,
                                ME_Singularity, ME_Singularity, ME_Singularity, plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(set) },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
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
                    ItemList.FusionComputer_UV.get(1), ItemList.FusionComputer_UV.get(2),
                    ItemList.FusionComputer_UV.get(3),
                    // MK4 Fusion Computer.
                    getModItem("gregtech", "gt.blockmachines", 1, 965),
                    getModItem("gregtech", "gt.blockmachines", 2, 965),
                    getModItem("gregtech", "gt.blockmachines", 3, 965) };

            final ItemStack[] fusionCoils = new ItemStack[] { getModItem("GoodGenerator", "compactFusionCoil", 1, 1),
                    getModItem("GoodGenerator", "compactFusionCoil", 2, 1),
                    getModItem("GoodGenerator", "compactFusionCoil", 3, 1),
                    getModItem("GoodGenerator", "compactFusionCoil", 1, 2),
                    getModItem("GoodGenerator", "compactFusionCoil", 2, 2),
                    getModItem("GoodGenerator", "compactFusionCoil", 3, 2),
                    getModItem("GoodGenerator", "compactFusionCoil", 1, 3),
                    getModItem("GoodGenerator", "compactFusionCoil", 2, 3),
                    getModItem("GoodGenerator", "compactFusionCoil", 3, 3) };

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

            for (int absoluteTier = 0; absoluteTier < 9; absoluteTier++) {

                TT_recipeAdder.addResearchableAssemblylineRecipe(
                        researchStuff[absoluteTier],
                        (absoluteTier + 1) * 48_000_000, // total comp
                        (absoluteTier + 1) * baseCompPerSec, // comp/s
                        (int) TierEU.RECIPE_MAX, // eu/t
                        (absoluteTier + 1) * 8, // amperage
                        new Object[] { baseCasing, fusionReactors[absoluteTier], fusionCoils[absoluteTier],
                                // UV Solar panel
                                getModItem("supersolarpanel", "PhotonicSolarPanel", absoluteTier + 1, 0),

                                getItemContainer("QuantumCircuit").get(absoluteTier + 1),
                                // Red Spectral Component
                                getModItem("supersolarpanel", "redcomponent", 64),
                                // Green Spectral Component
                                getModItem("supersolarpanel", "greencomponent", 64),
                                // Blue Spectral Component
                                getModItem("supersolarpanel", "bluecomponent", 64),

                                plateList[absoluteTier],
                                // Dyson Swarm Module Deployment Unit Base Casing
                                getModItem("GalaxySpace", "dysonswarmparts", (absoluteTier + 1) * 4, 2),
                                // Dyson Swarm Energy Receiver Dish Block
                                getModItem("GalaxySpace", "dysonswarmparts", (absoluteTier + 1) * 4, 1),
                                // Ultimate Time Anomaly.
                                getModItem("gregtech", "gt.blockmachines", (absoluteTier + 1) * 4, 11107),

                                ItemList.Energy_Module.get(absoluteTier + 1), GT_OreDictUnificator
                                        .get(OrePrefixes.wireGt01, Materials.SuperconductorUMV, (absoluteTier + 1) * 4),

                        },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
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
                                getModItem("GalaxySpace", "item.DysonSwarmParts", 4 * (absoluteTier + 1), 0),

                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUMVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUIVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.SuperconductorUEVBase, 4 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Longasssuperconductornameforuhvwire, 4 * (absoluteTier + 1)),

                                // Gravitation Engine
                                getModItem("GraviSuite", "itemSimpleItem", 64, 3),
                                getModItem("GraviSuite", "itemSimpleItem", 64, 3),
                                getModItem("GraviSuite", "itemSimpleItem", 64, 3),
                                getModItem("GraviSuite", "itemSimpleItem", 64, 3),

                                plateList[absoluteTier],
                                getItemContainer("QuantumCircuit").get(2 * (absoluteTier + 1)),
                                GT_OreDictUnificator.get(OrePrefixes.gearGt, Materials.SpaceTime, absoluteTier + 1),
                                GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.SpaceTime, absoluteTier + 1)


                        },
                        new FluidStack[] { new FluidStack(solderUEV, (int) (2_880 * pow(2L, absoluteTier))),
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
            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    // Ultimate Time Anomaly.
                    getModItem("gregtech", "gt.blockmachines", 1, 11107),
                    256_000_000, // total comp
                    16_384, // comp/s
                    (int) TierEU.RECIPE_MAX, // eu/t
                    32, // amperage
                    new Object[] {
                            // Space elevator blocks.
                            getModItem("gtnhintergalactic", "gt.blockcasingsSE", 64, 0),
                            // Cosmic neutronium block.
                            getModItem("Avaritia", "Resource_Block", 64, 0),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Neutronium, 64),
                            GT_OreDictUnificator.get(OrePrefixes.nanite, Materials.Neutronium, 48),

                            // Large Bedrockium Plate
                            getModItem("TConstruct", "heavyPlate", 1, 315),
                            // Large Cosmic Neutronium Plate
                            getModItem("TConstruct", "heavyPlate", 1, 500),
                            // Large Shirabon Plate
                            largeShirabonPlate,
                            // Large Infinity Plate
                            getModItem("TConstruct", "heavyPlate", 1, 501),

                            // UV Solar panel
                            getModItem("supersolarpanel", "PhotonicSolarPanel", 1, 0),
                            // Ultimate Time Anomaly.
                            getModItem("gregtech", "gt.blockmachines", 4, 11107),
                            // Gravitation Engine.
                            getModItem("GraviSuite", "itemSimpleItem", 64, 3), ItemList.EnergisedTesseract.get(1) },

                    new FluidStack[] { Materials.Neutronium.getMolten(144 * 256 * 4),
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 4),
                            new FluidStack(solderUEV, 144 * 256 * 2), Materials.Time.getMolten(1_440) },
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
                    new Object[] { getModItem("gregtech", "gt.blockmachines", 1, 13106),
                            // UV Solar panel
                            getModItem("supersolarpanel", "PhotonicSolarPanel", 1, 0),
                            // UHV Capacitor block
                            getModItem("kekztech", "kekztech_lapotronicenergyunit_block", 1, 5),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.SuperconductorUMV, 4),

                            CustomItemList.Machine_Multi_Transformer.get(16), ItemList.Wireless_Hatch_Energy_UMV.get(4),
                            CustomItemList.eM_energyTunnel5_UMV.get(1),
                            // High Energy Flow Circuit.
                            getModItem("dreamcraft", "item.HighEnergyFlowCircuit", 64, 0),

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
                            Materials.CosmicNeutronium.getMolten(144 * 256 * 16),
                            new FluidStack(solderUEV, 144 * 256 * 8), Materials.SpaceTime.getMolten(16_000) },
                    CustomItemList.EOH_Infinite_Energy_Casing.get(1),
                    10_000,
                    (int) TierEU.RECIPE_UMV);
        }

    }

    private void addWirelessEnergyRecipes() {

        Fluid solderUEV = FluidRegistry.getFluid("molten.mutatedlivingsolder") != null
                ? FluidRegistry.getFluid("molten.mutatedlivingsolder")
                : FluidRegistry.getFluid("molten.solderingalloy");

        int recipeDurationTicks = 20 * 20;
        int recipeEuPerTick = 128_000_000;

        int researchEuPerTick = 128_000_000;
        int researchAmperage = 4;
        int compPerSecond = 2000;
        int totalComputation = 500_000;

        ItemStack[] energyHatches = { ItemList.Hatch_Energy_ULV.get(1), ItemList.Hatch_Energy_LV.get(1),
                ItemList.Hatch_Energy_MV.get(1), ItemList.Hatch_Energy_HV.get(1), ItemList.Hatch_Energy_EV.get(1),
                ItemList.Hatch_Energy_IV.get(1), ItemList.Hatch_Energy_LuV.get(1), ItemList.Hatch_Energy_ZPM.get(1),
                ItemList.Hatch_Energy_UV.get(1), ItemList.Hatch_Energy_MAX.get(1),
                getItemContainer("Hatch_Energy_UEV").get(1L), getItemContainer("Hatch_Energy_UIV").get(1L),
                getItemContainer("Hatch_Energy_UMV").get(1L), getItemContainer("Hatch_Energy_UXV").get(1L) };

        ItemStack[] dynamoHatches = { ItemList.Hatch_Dynamo_ULV.get(1), ItemList.Hatch_Dynamo_LV.get(1),
                ItemList.Hatch_Dynamo_MV.get(1), ItemList.Hatch_Dynamo_HV.get(1), ItemList.Hatch_Dynamo_EV.get(1),
                ItemList.Hatch_Dynamo_IV.get(1), ItemList.Hatch_Dynamo_LuV.get(1), ItemList.Hatch_Dynamo_ZPM.get(1),
                ItemList.Hatch_Dynamo_UV.get(1), ItemList.Hatch_Dynamo_MAX.get(1),
                getItemContainer("Hatch_Dynamo_UEV").get(1L), getItemContainer("Hatch_Dynamo_UIV").get(1L),
                getItemContainer("Hatch_Dynamo_UMV").get(1L), getItemContainer("Hatch_Dynamo_UXV").get(1L) };

        Object[] circuitsTierPlusTwo = { new Object[] { OrePrefixes.circuit.get(Materials.Good), 1L }, // MV
                new Object[] { OrePrefixes.circuit.get(Materials.Advanced), 1L }, // HV
                new Object[] { OrePrefixes.circuit.get(Materials.Data), 1L }, // EV
                new Object[] { OrePrefixes.circuit.get(Materials.Elite), 1L }, // IV
                new Object[] { OrePrefixes.circuit.get(Materials.Master), 1L }, // LuV
                new Object[] { OrePrefixes.circuit.get(Materials.Ultimate), 1L }, // ZPM
                new Object[] { OrePrefixes.circuit.get(Materials.Superconductor), 1L }, // UV
                new Object[] { OrePrefixes.circuit.get(Materials.Infinite), 1L }, // UHV
                new Object[] { OrePrefixes.circuit.get(Materials.Bio), 1L }, // UEV
                new Object[] { OrePrefixes.circuit.get(Materials.Optical), 1L }, // UIV
                new Object[] { OrePrefixes.circuit.get(Materials.Piko), 1L }, // UMV
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 1L }, // UXV
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 4L }, // MAX (Technically not MAX, can be
                // changed once MAX circuits become
                // craftable)
                new Object[] { OrePrefixes.circuit.get(Materials.Quantum), 16L }, // MAX (Technically not MAX, can be
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
                ItemList.Wireless_Hatch_Energy_UXV.get(1), };

        ItemStack[] wirelessDynamos = { ItemList.Wireless_Dynamo_Energy_ULV.get(1),
                ItemList.Wireless_Dynamo_Energy_LV.get(1), ItemList.Wireless_Dynamo_Energy_MV.get(1),
                ItemList.Wireless_Dynamo_Energy_HV.get(1), ItemList.Wireless_Dynamo_Energy_EV.get(1),
                ItemList.Wireless_Dynamo_Energy_IV.get(1), ItemList.Wireless_Dynamo_Energy_LuV.get(1),
                ItemList.Wireless_Dynamo_Energy_ZPM.get(1), ItemList.Wireless_Dynamo_Energy_UV.get(1),
                ItemList.Wireless_Dynamo_Energy_UHV.get(1), ItemList.Wireless_Dynamo_Energy_UEV.get(1),
                ItemList.Wireless_Dynamo_Energy_UIV.get(1), ItemList.Wireless_Dynamo_Energy_UMV.get(1),
                ItemList.Wireless_Dynamo_Energy_UXV.get(1), };

        // ------------------------ Wireless EU hatches ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    (i == 0) ? ItemList.Tesseract.get(1) : wirelessHatches[i - 1],
                    totalComputation,
                    compPerSecond,
                    researchEuPerTick,
                    researchAmperage,
                    new Object[] { energyHatches[i], getModItem("GoodGenerator", "compactFusionCoil", 1),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                            circuitsTierPlusTwo[i], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.ExcitedDTEC.getFluid(500L) },
                    wirelessHatches[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }

        // ------------------------ Wireless EU dynamos ------------------------

        for (int i = 0; i < wirelessHatches.length; i++) {

            TT_recipeAdder.addResearchableAssemblylineRecipe(
                    (i == 0) ? ItemList.EnergisedTesseract.get(1) : wirelessDynamos[i - 1],
                    totalComputation,
                    compPerSecond,
                    researchEuPerTick,
                    researchAmperage,
                    new Object[] { dynamoHatches[i], getModItem("GoodGenerator", "compactFusionCoil", 1),
                            ItemList.Casing_Coil_Superconductor.get(1), CustomItemList.Machine_Multi_Transformer.get(1),
                            CustomItemList.eM_Power.get(2),
                            GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SpaceTime, 2),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Infinity, 1),
                            circuitsTierPlusTwo[i], ItemList.EnergisedTesseract.get(1) },
                    new FluidStack[] { new FluidStack(solderUEV, 1296), Materials.ExcitedDTEC.getFluid(500L) },
                    wirelessDynamos[i],
                    recipeDurationTicks,
                    recipeEuPerTick);
        }
    }

    private void registerMachineEMBehaviours() {
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(5), ItemList.Machine_IV_Centrifuge.get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(6), getItemContainer("CentrifugeLuV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(7), getItemContainer("CentrifugeZPM").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(8), getItemContainer("CentrifugeUV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(9), getItemContainer("CentrifugeUHV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(10), getItemContainer("CentrifugeUEV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(11), getItemContainer("CentrifugeUIV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Centrifuge(12), getItemContainer("CentrifugeUMV").get(1));

        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(5),
                ItemList.Machine_IV_ElectromagneticSeparator.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(6),
                getItemContainer("ElectromagneticSeparatorLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(7),
                getItemContainer("ElectromagneticSeparatorZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(8),
                getItemContainer("ElectromagneticSeparatorUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(9),
                getItemContainer("ElectromagneticSeparatorUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(10),
                getItemContainer("ElectromagneticSeparatorUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(11),
                getItemContainer("ElectromagneticSeparatorUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(
                () -> new Behaviour_ElectromagneticSeparator(12),
                getItemContainer("ElectromagneticSeparatorUMV").get(1));

        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(5), ItemList.Machine_IV_Recycler.get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(6), getItemContainer("RecyclerLuV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(7), getItemContainer("RecyclerZPM").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(8), getItemContainer("RecyclerUV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(9), getItemContainer("RecyclerUHV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(10), getItemContainer("RecyclerUEV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(11), getItemContainer("RecyclerUIV").get(1));
        GT_MetaTileEntity_EM_machine
                .registerBehaviour(() -> new Behaviour_Recycler(12), getItemContainer("RecyclerUMV").get(1));
    }
}
