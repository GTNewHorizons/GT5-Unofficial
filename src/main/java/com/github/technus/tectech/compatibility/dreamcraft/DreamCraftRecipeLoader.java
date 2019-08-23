package com.github.technus.tectech.compatibility.dreamcraft;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Centrifuge;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_ElectromagneticSeparator;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Recycler;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Method;

import static com.github.technus.tectech.loader.recipe.RecipeLoader.getOrDefault;

/**
 * Created by Tec on 06.08.2017.
 */
public class DreamCraftRecipeLoader implements Runnable {
    //region reflect a bit
    private Class CUSTOM_ITEM_LIST;
    private Method ADD_ASSEMBLER_RECIPE;

    private IItemContainer getItemContainer(String name) {
        return (IItemContainer) Enum.valueOf(CUSTOM_ITEM_LIST, name);
    }

    private void addAssemblerRecipeWithCleanroom(ItemStack[] items, FluidStack fluid, ItemStack output, int time, int eut) {
        try {
            ADD_ASSEMBLER_RECIPE.invoke(GT_Values.RA, items, fluid, output, time, eut, true);
        } catch (Exception e) {
            throw new Error(e);
        }
    }
    //endregion

    @Override
    public void run() {
        //region reflect a bit
        try {
            CUSTOM_ITEM_LIST = Class.forName("com.dreammaster.gthandler.CustomItemList");
            ADD_ASSEMBLER_RECIPE = GT_Values.RA.getClass().getMethod("addAssemblerRecipe", ItemStack[].class, FluidStack.class, ItemStack.class, int.class, int.class, boolean.class);
        } catch (Exception e) {
            throw new Error(e);
        }
        //endregion

        //Quantum Glass
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L)
        }, getOrDefault("Trinium", Materials.Osmium).getMolten(576), new ItemStack(QuantumGlassBlock.INSTANCE, 1), 200, 500000);

        //region pipes

        //Data
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                ItemList.Circuit_Parts_GlassFiber.get(8),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8)
        }, Materials.Polytetrafluoroethylene.getMolten(144), CustomItemList.DATApipe.get(1), 200, 30720);

        //Tunnel
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.DATApipe.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2),
                ItemList.Field_Generator_MV.get(1),
                ItemList.Circuit_Quantummainframe.get(1)
        }, Materials.Osmium.getMolten(288), CustomItemList.EMpipe.get(1), 400, 500000);

        //Laser
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.DATApipe.get(1),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 2)
        }, null, CustomItemList.LASERpipe.get(1), 100, 500000);

        //endregion

        //region casing

        //High Power Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2)
        }, Materials.TungstenSteel.getMolten(576), CustomItemList.eM_Power.get(1), 100, 30720);

        //Computer Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 8),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NiobiumTitanium, 2)
        }, Materials.Aluminium.getMolten(1296), CustomItemList.eM_Computer_Casing.get(1), 200, 122880);
        //Computer Vent Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.StainlessSteel, 1),
                ItemList.Electric_Motor_IV.get(2),
                GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.StainlessSteel, 2),
                GT_OreDictUnificator.get(OrePrefixes.pipeTiny, Materials.StainlessSteel, 16),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Copper, 16),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, getOrDefault("SuperconductorIV", Materials.Superconductor), 1)
        }, Materials.SolderingAlloy.getMolten(1296), CustomItemList.eM_Computer_Vent.get(1), 100, 1920);
        //Advanced Computer Casing
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, getOrDefault("SuperconductorLuV", Materials.Superconductor), 4)
        }, Materials.Iridium.getMolten(1296), CustomItemList.eM_Computer_Bus.get(1), 200, 122880);

        //Molecular Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.foil, getOrDefault("Trinium", Materials.Osmium), 12),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                ItemList.Field_Generator_IV.get(1)
        }, Materials.Osmium.getMolten(1296), CustomItemList.eM_Containment.get(1), 800, 500000);

        //Hollow Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.eM_Containment.get(1),
                12000, 32, 500000, 6, new ItemStack[]{
                        CustomItemList.eM_Containment.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("BlackPlutonium", Materials.Americium), 16),
                        GT_OreDictUnificator.get(OrePrefixes.screw, getOrDefault("Quantium", Materials.Neutronium), 16),
                }, new FluidStack[]{
                        getOrDefault("Trinium", Materials.Osmium).getMolten(1296),
                        Materials.Osmium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Argon.getGas(1000),
                }, CustomItemList.eM_Hollow.get(2), 200, 2000000);

        //EM Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.eM_Hollow.get(1),
                48000, 128, 1000000, 16, new ItemStack[]{
                        CustomItemList.eM_Hollow.get(1),
                        ItemList.Casing_Fusion_Coil.get(4),
                        ItemList.Casing_Coil_NaquadahAlloy.get(4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64),
                }, new FluidStack[]{
                        Materials.Glass.getMolten(2304),
                        Materials.Silicone.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        getOrDefault("Trinium", Materials.Osmium).getMolten(1296),
                }, CustomItemList.eM_Coil.get(4), 800, 2000000);

        //Tesla Base
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.NickelZincFerrite, 1)
        }, null, CustomItemList.tM_TeslaBase.get(1), 50, 16);
        GT_ModHandler.addCraftingRecipe(CustomItemList.tM_TeslaBase.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{"PhP", "PFP", "PwP",
                        'P', OrePrefixes.plate.get(Materials.NickelZincFerrite),
                        'F', OrePrefixes.frameGt.get(Materials.NickelZincFerrite)});

        //Tesla Toroid
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 6),
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Aluminium, 1)
        }, null, CustomItemList.tM_TeslaToroid.get(1), 50, 16);
        GT_ModHandler.addCraftingRecipe(CustomItemList.tM_TeslaToroid.get(1),
                GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE,
                new Object[]{"PhP", "PFP", "PwP",
                        'P', OrePrefixes.foil.get(Materials.Aluminium),
                        'F', OrePrefixes.frameGt.get(Materials.Aluminium)});

        //Tesla Secondary Windings
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.teslaComponent.getWithDamage(8, 0),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(12)
        }, Materials.Silver.getMolten(144), CustomItemList.tM_TeslaSecondary.get(1), 200, 120);

        //Tesla Primary Coils T0
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.RedstoneAlloy, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(8)
        }, Materials.RedAlloy.getMolten(144), CustomItemList.tM_TeslaPrimary_0.get(1), 200, 30);

        //Tesla Primary Coils T1
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorMV, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(12)
        }, Materials.Magnesium.getMolten(144), CustomItemList.tM_TeslaPrimary_1.get(1), 200, 120);

        //Tesla Primary Coils T2
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorHV, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(16)
        }, Materials.Barium.getMolten(144), CustomItemList.tM_TeslaPrimary_2.get(1), 200, 480);

        //Tesla Primary Coils T3
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorEV, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(20)
        }, Materials.Platinum.getMolten(144), CustomItemList.tM_TeslaPrimary_3.get(1), 200, 1920);

        //Tesla Primary Coils T4
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorIV, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(24)
        }, Materials.Vanadium.getMolten(144), CustomItemList.tM_TeslaPrimary_4.get(1), 200, 7680);

        //Tesla Primary Coils T5
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorLuV, 8),
                com.dreammaster.item.ItemList.MicaInsulatorFoil.getIS(28)
        }, Materials.Indium.getMolten(144), CustomItemList.tM_TeslaPrimary_5.get(1), 50, 30720);

        //endregion

        //region hatches

        //Dynamo Hatches IV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2)}, Materials.Silver.getMolten(144), CustomItemList.eM_dynamoMulti4_IV.get(1), 100, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_dynamoMulti4_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4)}, Materials.Electrum.getMolten(144), CustomItemList.eM_dynamoMulti16_IV.get(1), 200, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_LuV_IV").get(1), CustomItemList.eM_dynamoMulti16_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6)}, Materials.Tungsten.getMolten(144), CustomItemList.eM_dynamoMulti64_IV.get(1), 400, 1920);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2)}, Materials.Silver.getMolten(288), CustomItemList.eM_dynamoMulti4_LuV.get(1), 100, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_ZPM_LuV.get(1), CustomItemList.eM_dynamoMulti4_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4)}, Materials.Electrum.getMolten(288), CustomItemList.eM_dynamoMulti16_LuV.get(1), 200, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_ZPM_LuV").get(1), CustomItemList.eM_dynamoMulti16_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6)}, Materials.Tungsten.getMolten(288), CustomItemList.eM_dynamoMulti64_LuV.get(1), 400, 7680);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2)}, Materials.Silver.getMolten(576), CustomItemList.eM_dynamoMulti4_ZPM.get(1), 100, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_dynamoMulti4_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)}, Materials.Electrum.getMolten(576), CustomItemList.eM_dynamoMulti16_ZPM.get(1), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UV_ZPM").get(1), CustomItemList.eM_dynamoMulti16_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6)}, Materials.Tungsten.getMolten(576), CustomItemList.eM_dynamoMulti64_ZPM.get(1), 400, 30720);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1152), CustomItemList.eM_dynamoMulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_dynamoMulti4_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1152), CustomItemList.eM_dynamoMulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UHV_UV").get(1), CustomItemList.eM_dynamoMulti16_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1152), CustomItemList.eM_dynamoMulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2304), CustomItemList.eM_dynamoMulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UEV_UHV").get(1), CustomItemList.eM_dynamoMulti4_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2304), CustomItemList.eM_dynamoMulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UEV_UHV").get(1), CustomItemList.eM_dynamoMulti16_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2304), CustomItemList.eM_dynamoMulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hatch_Dynamo_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2)}, Materials.Silver.getMolten(4608), CustomItemList.eM_dynamoMulti4_UEV.get(1), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UIV_UEV").get(1), CustomItemList.eM_dynamoMulti4_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4)}, Materials.Electrum.getMolten(4608), CustomItemList.eM_dynamoMulti16_UEV.get(1), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UIV_UEV").get(1), CustomItemList.eM_dynamoMulti16_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6)}, Materials.Tungsten.getMolten(4608), CustomItemList.eM_dynamoMulti64_UEV.get(1), 400, 2000000);

        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Dynamo_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_dynamoMulti4_UIV.get(1), 100, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1), CustomItemList.eM_dynamoMulti4_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_dynamoMulti16_UIV.get(1), 200, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1), CustomItemList.eM_dynamoMulti16_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_dynamoMulti64_UIV.get(1), 400, 8000000);

        //Energy Hatches  UV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1000), CustomItemList.eM_energyMulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_energyMulti4_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1000), CustomItemList.eM_energyMulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UHV_UV").get(1), CustomItemList.eM_energyMulti16_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1000), CustomItemList.eM_energyMulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2000), CustomItemList.eM_energyMulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UEV_UHV").get(1), CustomItemList.eM_energyMulti4_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2000), CustomItemList.eM_energyMulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UEV_UHV").get(1), CustomItemList.eM_energyMulti16_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2000), CustomItemList.eM_energyMulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hatch_Energy_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 2)}, Materials.Silver.getMolten(4000), CustomItemList.eM_energyMulti4_UEV.get(1), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UIV_UEV").get(1), CustomItemList.eM_energyMulti4_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 4)}, Materials.Electrum.getMolten(4000), CustomItemList.eM_energyMulti16_UEV.get(1), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UIV_UEV").get(1), CustomItemList.eM_energyMulti16_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium", Materials.Neutronium), 6)}, Materials.Tungsten.getMolten(4000), CustomItemList.eM_energyMulti64_UEV.get(1), 400, 2000000);

        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Energy_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_energyMulti4_UIV.get(1), 100, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1), CustomItemList.eM_energyMulti4_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_energyMulti16_UIV.get(1), 200, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1), CustomItemList.eM_energyMulti16_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_energyMulti64_UIV.get(1), 400, 8000000);

        //Laser Dynamo IV-UEV 256/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_IV.get(1), ItemList.Electric_Pump_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_IV.get(1), 1000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_LuV.get(1), 1000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_ZPM.get(1), 1000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UV.get(1), ItemList.Electric_Pump_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_UV.get(1), 1000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UHV.get(1), ItemList.Electric_Pump_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_UHV.get(1), 1000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UEV.get(1), ItemList.Electric_Pump_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_UEV.get(1), 1000, 8000000);

        //Laser Dynamo IV-UEV 1024/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_IV.get(2), ItemList.Electric_Pump_IV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_IV.get(1), 2000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_LuV.get(2), ItemList.Electric_Pump_LuV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_LuV.get(1), 2000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_ZPM.get(1), 2000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UV.get(2), ItemList.Electric_Pump_UV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_UV.get(1), 2000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UHV.get(2), ItemList.Electric_Pump_UHV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_UHV.get(1), 2000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UEV.get(2), ItemList.Electric_Pump_UEV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_UEV.get(1), 2000, 8000000);

        //Laser Dynamo IV-UEV 4096/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_IV.get(4), ItemList.Electric_Pump_IV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_IV.get(1), 4000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_LuV.get(4), ItemList.Electric_Pump_LuV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_LuV.get(1), 4000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_ZPM.get(1), 4000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UV.get(4), ItemList.Electric_Pump_UV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_UV.get(1), 4000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UHV.get(4), ItemList.Electric_Pump_UHV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_UHV.get(1), 4000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UEV.get(4), ItemList.Electric_Pump_UEV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_UEV.get(1), 4000, 8000000);

        //Laser Dynamo IV-UEV 16384/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_IV.get(8), ItemList.Electric_Pump_IV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_IV.get(1), 8000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_LuV.get(8), ItemList.Electric_Pump_LuV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_LuV.get(1), 8000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_ZPM.get(1), 8000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UV.get(8), ItemList.Electric_Pump_UV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_UV.get(1), 8000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UHV.get(8), ItemList.Electric_Pump_UHV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_UHV.get(1), 8000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UEV.get(8), ItemList.Electric_Pump_UEV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_UEV.get(1), 8000, 8000000);

        //Laser Dynamo IV-UEV 65536/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_IV.get(16), ItemList.Electric_Pump_IV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_IV.get(1), 16000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_LuV.get(16), ItemList.Electric_Pump_LuV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_LuV.get(1), 16000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_ZPM.get(1), 16000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UV.get(16), ItemList.Electric_Pump_UV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_UV.get(1), 16000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UHV.get(16), ItemList.Electric_Pump_UHV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_UHV.get(1), 16000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UEV.get(16), ItemList.Electric_Pump_UEV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_UEV.get(1), 16000, 8000000);

        //Laser Dynamo IV-UEV 262144/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_IV.get(32), ItemList.Electric_Pump_IV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_IV.get(1), 32000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_LuV.get(32), ItemList.Electric_Pump_LuV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_LuV.get(1), 32000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_ZPM.get(1), 32000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UV.get(32), ItemList.Electric_Pump_UV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_UV.get(1), 32000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UHV.get(32), ItemList.Electric_Pump_UHV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_UHV.get(1), 32000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UEV.get(32), ItemList.Electric_Pump_UEV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_UEV.get(1), 32000, 8000000);

        //Laser Dynamo IV-UEV 1048576/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_IV.get(64), ItemList.Electric_Pump_IV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_IV.get(1), 64000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_LuV.get(64), ItemList.Electric_Pump_LuV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_LuV.get(1), 64000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_ZPM.get(1), 64000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UV.get(64), ItemList.Electric_Pump_UV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_UV.get(1), 64000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UHV.get(64), ItemList.Electric_Pump_UHV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_UHV.get(1), 64000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UEV.get(64), ItemList.Electric_Pump_UEV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_UEV.get(1), 64000, 8000000);

        //Laser Target IV-UEV 256/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_IV.get(1), ItemList.Electric_Pump_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_IV.get(1), 1000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_LuV.get(1), 1000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_ZPM.get(1), 1000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UV.get(1), ItemList.Electric_Pump_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_UV.get(1), 1000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UHV.get(1), ItemList.Electric_Pump_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_UHV.get(1), 1000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UEV.get(1), ItemList.Electric_Pump_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_UEV.get(1), 1000, 8000000);

        //Laser Target IV-UEV 1024/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_IV.get(2), ItemList.Electric_Pump_IV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_IV.get(1), 2000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_LuV.get(2), ItemList.Electric_Pump_LuV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_LuV.get(1), 2000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_ZPM.get(1), 2000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UV.get(2), ItemList.Electric_Pump_UV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_UV.get(1), 2000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UHV.get(2), ItemList.Electric_Pump_UHV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Bedrockium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_UHV.get(1), 2000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UEV.get(2), ItemList.Electric_Pump_UEV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Draconium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_UEV.get(1), 2000, 8000000);

        //Laser Target IV-UEV 4096/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_IV.get(4), ItemList.Electric_Pump_IV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_IV.get(1), 4000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_LuV.get(4), ItemList.Electric_Pump_LuV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_LuV.get(1), 4000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_ZPM.get(1), 4000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UV.get(4), ItemList.Electric_Pump_UV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_UV.get(1), 4000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UHV.get(4), ItemList.Electric_Pump_UHV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_UHV.get(1), 4000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UEV.get(4), ItemList.Electric_Pump_UEV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_UEV.get(1), 4000, 8000000);

        //Laser Target IV-UEV 16384/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_IV.get(8), ItemList.Electric_Pump_IV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_IV.get(1), 8000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_LuV.get(8), ItemList.Electric_Pump_LuV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_LuV.get(1), 8000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_ZPM.get(1), 8000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UV.get(8), ItemList.Electric_Pump_UV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_UV.get(1), 8000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UHV.get(8), ItemList.Electric_Pump_UHV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Bedrockium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_UHV.get(1), 8000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UEV.get(8), ItemList.Electric_Pump_UEV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_UEV.get(1), 8000, 8000000);

        //Laser Target IV-UEV 65536/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_IV.get(16), ItemList.Electric_Pump_IV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_IV.get(1), 16000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_LuV.get(16), ItemList.Electric_Pump_LuV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_LuV.get(1), 16000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_ZPM.get(1), 16000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UV.get(16), ItemList.Electric_Pump_UV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_UV.get(1), 16000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UHV.get(16), ItemList.Electric_Pump_UHV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_UHV.get(1), 16000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UEV.get(16), ItemList.Electric_Pump_UEV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_UEV.get(1), 16000, 8000000);

        //Laser Target IV-UEV 262144/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_IV.get(32), ItemList.Electric_Pump_IV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_IV.get(1), 32000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_LuV.get(32), ItemList.Electric_Pump_LuV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_LuV.get(1), 32000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_ZPM.get(1), 32000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UV.get(32), ItemList.Electric_Pump_UV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_UV.get(1), 32000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UHV.get(32), ItemList.Electric_Pump_UHV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Bedrockium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_UHV.get(1), 32000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UEV.get(32), ItemList.Electric_Pump_UEV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_UEV.get(1), 32000, 8000000);

        //Laser Target IV-UEV 1048576/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_IV.get(64), ItemList.Electric_Pump_IV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_IV.get(1), 64000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_LuV.get(64), ItemList.Electric_Pump_LuV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_LuV.get(1), 64000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_ZPM.get(1), 64000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UV.get(64), ItemList.Electric_Pump_UV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_UV.get(1), 64000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UHV.get(64), ItemList.Electric_Pump_UHV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Bedrockium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_UHV.get(1), 64000, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hull_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UEV.get(64), ItemList.Electric_Pump_UEV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Draconium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_UEV.get(1), 64000, 8000000);

        //Data Input
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Circuit_Crystalcomputer.get(1),
                CustomItemList.DATApipe.get(2)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataIn_Hatch.get(1), 200, 122880);
        //Data Output
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Output_Bus_LuV.get(1),
                ItemList.Circuit_Crystalcomputer.get(1),
                CustomItemList.DATApipe.get(2)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataOut_Hatch.get(1), 200, 122880);

        //Rack
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                ItemList.Circuit_Crystalcomputer.get(2),
                CustomItemList.DATApipe.get(4)
        }, Materials.Iridium.getMolten(1296), CustomItemList.rack_Hatch.get(1), 800, 122880);

        //Object Holder
        GT_Values.RA.addAssemblylineRecipe(ItemList.Hatch_Input_Bus_ZPM.get(1), 10000, new Object[]{
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Emitter_ZPM.get(8),
                ItemList.Robot_Arm_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 1)},
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2)},
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16)},
                CustomItemList.DATApipe.get(2),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(500),
                Materials.Iridium.getMolten(1000),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1000)
        }, CustomItemList.holder_Hatch.get(1), 1200, 100000);

        //Parameterizer
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Circuit_Masterquantumcomputer.get(1),
                CustomItemList.DATApipe.get(4),
                ItemList.Cover_Screen.get(1),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Parametrizer_Hatch.get(1), 800, 122880);
        //Uncertainty
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1),
                CustomItemList.DATApipe.get(16),
                ItemList.Cover_Screen.get(1),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Uncertainty_Hatch.get(1), 1200, 122880);

        //Elemental Input
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Input_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Sensor_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_in_UV.get(1), 800, 500000);
        //Elemental Output
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Output_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Emitter_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_out_UV.get(1), 800, 500000);
        //Overflow
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Muffler_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Naquadah, 1),
                ItemList.Field_Generator_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_muffler_UV.get(1), 800, 500000);

        //endregion

        //region multiblocks

        //Tesla Winding Components Ultimate (ADD BLOOD VARIANT)
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUV, 4),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 1),
        }, Materials.Polytetrafluoroethylene.getMolten(18), CustomItemList.teslaComponent.getWithDamage(1, 1), 320, 7680);

        //Tesla Coil
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_ModHandler.getIC2Item("teslaCoil", 1),
                CustomItemList.tM_TeslaSecondary.get(4),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.NickelZincFerrite, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                ItemList.Upgrade_Overclocker.get(4),
        }, Materials.Silver.getMolten(576), CustomItemList.Machine_Multi_TeslaCoil.get(1), 800, 480);

        //Microwave Grinder
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Machine_HV_Microwave.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.StainlessSteel, 4),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Advanced, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.AnnealedCopper, 16),
                ItemList.Upgrade_Overclocker.get(4),
        }, Materials.Copper.getMolten(576), CustomItemList.Machine_Multi_Microwave.get(1), 800, 480);

        //Active Transformer
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                getItemContainer("WetTransformer_ZPM_LuV").get(1),
                getItemContainer("HighEnergyFlowCircuit").get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, getOrDefault("SuperconductorLuV", Materials.Superconductor), 16),
        }, Materials.TungstenSteel.getMolten(576), CustomItemList.Machine_Multi_Transformer.get(1), 400, 30720);

        //Network Switch
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.Machine_Multi_Transformer.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Copper, 64),
                CustomItemList.DATApipe.get(4),
        }, Materials.Iridium.getMolten(1296), CustomItemList.Machine_Multi_Switch.get(1), 800, 122880);

        //Quantum Computer
        GT_Values.RA.addAssemblylineRecipe(ItemList.Tool_DataOrb.get(1), 20000, new Object[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 2)},
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1),
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8)},
                CustomItemList.DATApipe.get(8),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000),
        }, CustomItemList.Machine_Multi_Computer.get(1), 12000, 100000);

        //Research Station
        GT_Values.RA.addAssemblylineRecipe(getItemContainer("ScannerZPM").get(1), 80000, new Object[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                ItemList.Sensor_ZPM.get(8),
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 4)},
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4)},
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32)},
                CustomItemList.DATApipe.get(16),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Osmium.getMolten(1296),
        }, CustomItemList.Machine_Multi_Research.get(1), 12000, 100000);

        //Matter Junction
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_Switch.get(1),
                8000, 32, 500000, 4, new ItemStack[]{
                        CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Robot_Arm_LuV.get(2),
                        ItemList.Electric_Piston_LuV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(2),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 4),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(1000),
                        Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Osmium.getMolten(1296),
                }, CustomItemList.Machine_Multi_EMjunction.get(1), 12000, 100000);

        //Matter Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Hatch_Input_UV.get(1),
                12000, 32, 500000, 6, new ItemStack[]{
                        CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Emitter_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 2),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(1000),
                        Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Osmium.getMolten(1296),
                }, CustomItemList.Machine_Multi_MatterToEM.get(1), 12000, 100000);

        //Matter DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Hatch_Output_UV.get(1),
                12000, 32, 500000, 6, new ItemStack[]{
                        CustomItemList.Machine_Multi_Transformer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 4),
                        ItemList.Sensor_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 2),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(1000),
                        Materials.Naquadah.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Osmium.getMolten(1296),
                }, CustomItemList.Machine_Multi_EMToMatter.get(1), 12000, 100000);

        //Essentia Quantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_MatterToEM.get(1),
                15000, 32, 500000, 8, new ItemStack[]{
                        CustomItemList.Machine_Multi_MatterToEM.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Emitter_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(2000),
                        Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        Materials.Osmium.getMolten(1296),
                }, CustomItemList.Machine_Multi_EssentiaToEM.get(1), 24000, 500000);

        //Essentia DeQuantizer
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_EMToMatter.get(1),
                15000, 32, 500000, 8, new ItemStack[]{
                        CustomItemList.Machine_Multi_EMToMatter.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Neutronium, 4),
                        ItemList.Sensor_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 2),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(2000),
                        Materials.Void.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        Materials.Osmium.getMolten(1296),
                }, CustomItemList.Machine_Multi_EMToEssentia.get(1), 24000, 500000);

        //EM Scanner
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_Research.get(1),
                150000, 128, 500000, 16, new ItemStack[]{
                        CustomItemList.Machine_Multi_EMjunction.get(1),
                        CustomItemList.eM_Computer_Bus.get(4),
                        ItemList.Field_Generator_UV.get(4),
                        ItemList.Sensor_UV.get(4),
                        getItemContainer("NanoCircuit").get(4),
                        getItemContainer("MysteriousCrystalLens").get(4),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Draconium, 4),
                }, new FluidStack[]{
                        Materials.UUMatter.getFluid(2000),
                        Materials.Neutronium.getMolten(2592),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 4000),
                        Materials.Osmiridium.getMolten(1296),
                }, CustomItemList.Machine_Multi_Scanner.get(1), 24000, 500000);

        //Multi Infuser
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_Transformer.get(1),
                192000, 512, 2000000, 32, new ItemStack[]{
                        CustomItemList.Machine_Multi_Transformer.get(1),
                        CustomItemList.eM_Coil.get(8),
                        CustomItemList.eM_Power.get(8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16),
                }, new FluidStack[]{
                        Materials.Electrum.getMolten(2592),
                        Materials.Neutronium.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                }, CustomItemList.Machine_Multi_Infuser.get(1), 8000, 2000000);

        //Motor UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Motor_UV.get(1L),
                24000, 32, 100000, 4, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 4L),
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
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(2592),
                        Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Motor_UHV.get(1L), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Motor_UHV.get(1L),
                48000, 64, 2000000, 8, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.SamariumMagnetic, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),//TODO Fusion T4 Material
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(5184),
                        Materials.Lubricant.getFluid(8000)}, ItemList.Electric_Motor_UEV.get(1L), 2000, 800000);

        //Pumps UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Pump_UV.get(1L),
                24000, 32, 100000, 4, new ItemStack[]{
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.Neutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.CosmicNeutronium, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.AnySyntheticRubber, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(2592),
                        Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Pump_UHV.get(1), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Pump_UHV.get(1L),
                48000, 64, 200000, 8, new ItemStack[]{
                        ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.pipeLarge, Materials.NetherStar, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Infinity, 16L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, (Materials.AnySyntheticRubber), 64L),
                        GT_OreDictUnificator.get(OrePrefixes.rotor, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(5184),
                        Materials.Lubricant.getFluid(8000)}, ItemList.Electric_Pump_UEV.get(1), 2000, 800000);

        //Conveyor Belt UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Conveyor_Module_UV.get(1L),
                24000, 32, 100000, 4, new ItemStack[]{
                        ItemList.Electric_Motor_UHV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 2L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(2592),
                        Materials.Lubricant.getFluid(4000),
                        Materials.Silicone.getMolten(5760)}, ItemList.Conveyor_Module_UHV.get(1), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Conveyor_Module_UHV.get(1L),
                48000, 64, 200000, 8, new ItemStack[]{
                        ItemList.Electric_Motor_UEV.get(2L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 2L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(5184),
                        Materials.Lubricant.getFluid(8000),
                        Materials.Silicone.getMolten(11520)}, ItemList.Conveyor_Module_UEV.get(1), 2000, 800000);

        //Piston UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Piston_UV.get(1L),
                24000, 32, 100000, 4, new ItemStack[]{
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.CosmicNeutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 4L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(2592),
                        Materials.Lubricant.getFluid(4000)}, ItemList.Electric_Piston_UHV.get(1), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Piston_UHV.get(1L),
                48000, 64, 200000, 8, new ItemStack[]{
                        ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L),
                        GT_OreDictUnificator.get(OrePrefixes.ring, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.round, Materials.Infinity, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 4L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 4L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(5184),
                        Materials.Lubricant.getFluid(8000)}, ItemList.Electric_Piston_UEV.get(1), 2000, 800000);

        //Robot Arm UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Robot_Arm_UV.get(1L),
                24000, 32, 100000, 4, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.CosmicNeutronium, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.CosmicNeutronium, 6L),
                        ItemList.Electric_Motor_UHV.get(2L),
                        ItemList.Electric_Piston_UHV.get(1L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 2L},
                        new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 4L},
                        new Object[]{OrePrefixes.circuit.get(Materials.Ultimate), 8L},
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 6L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608),
                        Materials.Lubricant.getFluid(4000)}, ItemList.Robot_Arm_UHV.get(1L), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Robot_Arm_UHV.get(1L),
                48000, 64, 200000, 8, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 8L),
                        GT_OreDictUnificator.get(OrePrefixes.gear, Materials.Infinity, 2L),
                        GT_OreDictUnificator.get(OrePrefixes.gearGtSmall, Materials.Infinity, 6L),
                        ItemList.Electric_Motor_UEV.get(2L),
                        ItemList.Electric_Piston_UEV.get(1L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Bio), 2L},
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4L},
                        new Object[]{OrePrefixes.circuit.get(Materials.Superconductor), 8L},
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 6L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(9216),
                        Materials.Lubricant.getFluid(8000)}, ItemList.Robot_Arm_UEV.get(1L), 2000, 800000);

        //Emitter UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Emitter_UV.get(1L),
                24000, 32, 100000, 4, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4L},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Emitter_UHV.get(1L), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Emitter_UHV.get(1L),
                48000, 64, 200000, 8, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        ItemList.Electric_Motor_UEV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.stick, Materials.Infinity, 16L),
                        ItemList.Gravistar.get(16L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4L},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(9216)},
                ItemList.Emitter_UEV.get(1L), 2000, 800000);

        //Sensor UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Sensor_UV.get(1L),
                24000, 32, 100000, 4, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        ItemList.Electric_Motor_UHV.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 8L),
                        ItemList.Gravistar.get(8L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4L},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.ElectrumFlux, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 7L)}, new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Sensor_UHV.get(1L), 1000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Sensor_UHV.get(1L),
                48000, 64, 200000, 8, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        ItemList.Electric_Motor_UEV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 8L),
                        ItemList.Gravistar.get(16),
                        new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4L},
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.InfinityCatalyst, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 7L)}, new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(9216)},
                ItemList.Sensor_UEV.get(1L), 2000, 800000);

        //Fieldgen UV and UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Field_Generator_UV.get(1),
                48000, 64, 200000, 8, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.CosmicNeutronium, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.CosmicNeutronium, 6L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4L},
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Bedrockium, 8L)},
                new FluidStack[]{
                        Materials.Naquadria.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(4608)},
                ItemList.Field_Generator_UHV.get(1L), 2000, 200000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Field_Generator_UHV.get(1L),
                96000, 128, 400000, 16, new Object[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Infinity, 1L),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Infinity, 6L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Bio), 8L},
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Tritanium, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.cableGt04, Materials.Draconium, 8L)},
                new FluidStack[]{
                        Materials.Quantium.getMolten(2592),
                        Materials.SolderingAlloy.getMolten(9216)},
                ItemList.Field_Generator_UEV.get(1L), 4000, 800000);

        //UHV Energy Hatch
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Hatch_Energy_UV.get(1L),
                24000, 16, 50000, 2, new Object[]{
                        ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUHV, 2L),
                        ItemList.Circuit_Chip_QPIC.get(2L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 2L},
                        ItemList.UHV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Electric_Pump_UHV.get(1L)},
                new FluidStack[]{
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                        Materials.SolderingAlloy.getMolten(5760),
                }, ItemList.Hatch_Energy_MAX.get(1L), 1000, 2000000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Hatch_Dynamo_UV.get(1L),
                48000, 32, 100000, 4, new Object[]{
                        ItemList.Hull_MAX.get(1L),
                        GT_OreDictUnificator.get(OrePrefixes.spring, Materials.Longasssuperconductornameforuhvwire, 8L),
                        ItemList.Circuit_Chip_QPIC.get(2L),
                        new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 2L},
                        ItemList.UHV_Coil.get(2L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Reactor_Coolant_He_6.get(1L),
                        ItemList.Electric_Pump_UHV.get(1L)},
                new FluidStack[]{
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000),
                        Materials.SolderingAlloy.getMolten(5760)},
                ItemList.Hatch_Dynamo_MAX.get(1L), 1000, 2000000);

        //UHV Circuit
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Circuit_Wetwaresupercomputer.get(1L),
                24000, 64, 50000, 4, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 2),
                        ItemList.Circuit_Wetwaresupercomputer.get(2L),
                        ItemList.ZPM_Coil.get(16L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorSMD.get(64L),
                        ItemList.Circuit_Parts_TransistorSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeSMD.get(64L),
                        ItemList.Circuit_Chip_Ram.get(48L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorZPM, 64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, (Materials.AnySyntheticRubber), 64L),
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(2880L),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 10000),
                        Materials.Radon.getGas(2500L),
                }, ItemList.Circuit_Wetwaremainframe.get(1L), 2000, 300000);

        //Bio Chips
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Circuit_Biowarecomputer.get(1L),
                48000, 128, 500000, 8, new ItemStack[]{
                        ItemList.Circuit_Board_Bio_Ultra.get(2L),
                        ItemList.Circuit_Biowarecomputer.get(2L),
                        ItemList.Circuit_Parts_TransistorSMD.get(16L),
                        ItemList.Circuit_Parts_ResistorSMD.get(16L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(16L),
                        ItemList.Circuit_Parts_DiodeSMD.get(48L),
                        ItemList.Circuit_Chip_NOR.get(32L),
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.NiobiumTitanium, 32L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 16L),
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(1440L),
                        Materials.BioMediumSterilized.getFluid(1440L),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 10000)
                },
                ItemList.Circuit_Biowaresupercomputer.get(1L), 4000, 500000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Circuit_Biowaresupercomputer.get(1L),
                96000, 256, 1000000, 16, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4L),
                        ItemList.Circuit_Biowaresupercomputer.get(2L),
                        ItemList.UV_Coil.get(16L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorSMD.get(64L),
                        ItemList.Circuit_Parts_TransistorSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeSMD.get(64L),
                        ItemList.Circuit_Chip_Ram.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.SuperconductorUHV, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64)
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(2880L),
                        Materials.BioMediumSterilized.getFluid(2880L),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 20000)
                }, ItemList.Circuit_Biomainframe.get(1L), 6000, 2000000);

        //GTNH Circuits
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Circuit_Biomainframe.get(1L),
                192000, 512, 2000000, 32, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 8),
                        ItemList.Circuit_Biomainframe.get(2L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorSMD.get(64L),
                        ItemList.Circuit_Parts_TransistorSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeSMD.get(64L),
                        ItemList.Circuit_Chip_Ram.get(64L),
                        ItemList.Circuit_Chip_NPIC.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Draconium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Polybenzimidazole, 64)
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(3760L),
                        Materials.Naquadria.getMolten(4032L),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 20000)
                }, getItemContainer("NanoCircuit").get(1L), 8000, 8000000);


        TT_recipeAdder.addResearchableAssemblylineRecipe(getItemContainer("PicoWafer").get(1),
                384000, 1024, 4000000, 64, new ItemStack[]{
                        ItemList.Circuit_Board_Bio_Ultra.get(1L),
                        getItemContainer("PicoWafer").get(4L),
                        getItemContainer("NanoCircuit").get(2L),
                        ItemList.Circuit_Parts_TransistorSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorSMD.get(64L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeSMD.get(64L),
                        ItemList.Circuit_Chip_PPIC.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Osmium, 32),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Neutronium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 64)
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(3760L),
                        Materials.UUMatter.getFluid(8000L),
                        Materials.Osmium.getMolten(1152L)
                }, getItemContainer("PikoCircuit").get(1L), 10000, 8000000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(getItemContainer("PikoCircuit").get(1L),
                720000, 2048, 8000000, 128, new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 16),
                        getItemContainer("PikoCircuit").get(8L),
                        ItemList.Circuit_Parts_CapacitorSMD.get(64L),
                        ItemList.Circuit_Parts_DiodeSMD.get(64L),
                        ItemList.Circuit_Parts_TransistorSMD.get(64L),
                        ItemList.Circuit_Parts_ResistorSMD.get(64L),
                        ItemList.Circuit_Chip_QPIC.get(64L),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.NiobiumTitanium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.bolt, Materials.Indium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Bedrockium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Lanthanum, 64)
                }, new FluidStack[]{
                        Materials.SolderingAlloy.getMolten(3760L),
                        Materials.UUMatter.getFluid(24000L),
                        Materials.Osmium.getMolten(2304L)
                }, getItemContainer("QuantumCircuit").get(1L), 20000, 32000000);

        //Stargate Stuff
        if (Loader.isModLoaded("eternalsingularity") && Loader.isModLoaded("SGCraft")) {
            TT_recipeAdder.addResearchableAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Infinity, 1L),
                    192000, 512, 2000000, 32, new ItemStack[]{
                            GT_ModHandler.getModItem("eternalsingularity", "eternal_singularity", 1L),
                            ItemList.Sensor_UV.get(16L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.CosmicNeutronium, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 64L),
                            getItemContainer("NanoCircuit").get(1L).splitStack(16)
                    },
                    new FluidStack[]{
                            Materials.Neutronium.getMolten(36864L),
                            Materials.Tritanium.getMolten(36864L),
                            Materials.Tetranaquadahdiindiumhexaplatiumosminid.getMolten(36864L),
                            Materials.Silver.getPlasma(36864L)
                    },
                    getItemContainer("StargateShieldingFoil").get(1L), 72000, 2000000);

            TT_recipeAdder.addResearchableAssemblylineRecipe(getItemContainer("StargateShieldingFoil").get(1L),
                    192000, 512, 2000000, 32, new ItemStack[]{
                            ItemList.Electric_Piston_UV.get(16L),
                            ItemList.Electric_Motor_UV.get(64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.Infinity, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NaquadahAlloy, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.block, Materials.NetherStar, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ardite, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ardite, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Ardite, 8L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Ruby, 16L),
                            GT_OreDictUnificator.get(OrePrefixes.gemExquisite, Materials.Jasper, 16L),
                            getItemContainer("NanoCircuit").get(1L).splitStack(32)
                    },
                    new FluidStack[]{
                            Materials.Neutronium.getMolten(9216L),
                            Materials.Tritanium.getMolten(9216L),
                            Materials.Tetranaquadahdiindiumhexaplatiumosminid.getMolten(9216L),
                            Materials.Silver.getPlasma(9216L)
                    },
                    getItemContainer("StargateChevron").get(1L), 72000, 2000000);

            TT_recipeAdder.addResearchableAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Neutronium, 1L),
                    192000, 512, 2000000, 32, new ItemStack[]{
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Infinity, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.NaquadahAlloy, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.CosmicNeutronium, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Neutronium, 64L),
                            GT_OreDictUnificator.get(OrePrefixes.stickLong, Materials.Osmiridium, 64L)
                    },
                    new FluidStack[]{
                            Materials.Neutronium.getMolten(73728L),
                            Materials.Tritanium.getMolten(73728L),
                            Materials.Concrete.getMolten(73728L)
                    },
                    getItemContainer("StargateFramePart").get(1L), 72000, 2000000);

            //Batteries
            TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Energy_Cluster.get(1L), 12000, 16, 100000, 3, new Object[]{
                    GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Tritanium, 64L),
                    new Object[]{OrePrefixes.circuit.get(Materials.Infinite), 4L},
                    ItemList.Energy_Cluster.get(8L),
                    ItemList.Field_Generator_UV.get(2),
                    ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Wafer_HPIC.get(64),
                    ItemList.Circuit_Parts_DiodeSMD.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 32),
            }, new FluidStack[]{
                    Materials.SolderingAlloy.getMolten(2880),
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 16000)
            }, ItemList.ZPM2.get(1), 3000, 400000);

            TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.ZPM2.get(1L), 24000, 64, 200000, 6, new Object[]{
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                    GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 32L),
                    new Object[]{OrePrefixes.circuit.get(Materials.Bio), 4L},
                    ItemList.ZPM2.get(8),
                    ItemList.Field_Generator_UHV.get(4),
                    ItemList.Circuit_Wafer_UHPIC.get(64),
                    ItemList.Circuit_Wafer_UHPIC.get(64),
                    ItemList.Circuit_Wafer_SoC2.get(32),
                    ItemList.Circuit_Parts_DiodeSMD.get(64),
                    GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Neutronium, 64),
            }, new FluidStack[]{
                    Materials.SolderingAlloy.getMolten(3760),
                    Materials.Naquadria.getMolten(9000),
                    new FluidStack(FluidRegistry.getFluid("ic2coolant"), 32000)
            }, ItemList.ZPM3.get(1), 4000, 1600000);
        }
        //endregion

        //region singleblocks

        //Tesla Transceiver LV 1A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_LV.get(1), 400, 30);

        //Tesla Transceiver MV 1A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_MV.get(1), 400, 120);

        //Tesla Transceiver HV 1A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_HV.get(1), 400, 480);

        //Tesla Transceiver EV 1A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_EV.get(1), 400, 1920);

        //Tesla Transceiver IV 1A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_1by1_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_1by1_IV.get(1), 400, 7680);

        //Tesla Transceiver LV 4A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_LV.get(1), 400, 30);

        //Tesla Transceiver MV 4A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_MV.get(1), 400, 120);

        //Tesla Transceiver HV 4A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_HV.get(1), 400, 480);

        //Tesla Transceiver EV 4A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_EV.get(1), 400, 1920);

        //Tesla Transceiver IV 4A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_2by2_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_2by2_IV.get(1), 400, 7680);

        //Tesla Transceiver LV 9A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_LV.get(1), 400, 30);

        //Tesla Transceiver MV 9A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_MV.get(1), 400, 120);

        //Tesla Transceiver HV 9A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_HV.get(1), 400, 480);

        //Tesla Transceiver EV 9A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_EV.get(1), 400, 1920);

        //Tesla Transceiver IV 9A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_3by3_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_3by3_IV.get(1), 400, 7680);

        //Tesla Transceiver LV 16A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1), 400, 30);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_LV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_LV.get(1), 400, 30);

        //Tesla Transceiver MV 16A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1), 400, 120);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_MV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_MV.get(1), 400, 120);

        //Tesla Transceiver HV 16A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1), 400, 480);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_HV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_HV.get(1), 400, 480);

        //Tesla Transceiver EV 16A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1), 400, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_EV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_EV.get(1), 400, 1920);

        //Tesla Transceiver IV 16A
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Lead.getMolten(576),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.Tin.getMolten(288),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1), 400, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Battery_Buffer_4by4_IV.get(1),
                        CustomItemList.teslaCoilCover.getWithDamage(1, 0)},
                Materials.SolderingAlloy.getMolten(144),
                CustomItemList.Machine_TeslaCoil_4by4_IV.get(1), 400, 7680);

        //endregion

        //region crafting components

        //Tesla Winding Components
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 32),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.NickelZincFerrite, 8),
        }, Materials.Epoxid.getMolten(288), CustomItemList.teslaComponent.getWithDamage(1, 0), 320, 30);

        //endregion

        //region items

        //LV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tin, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 8),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 8),
        }, Materials.Epoxid.getMolten(72), CustomItemList.teslaCapacitor.getWithDamage(1, 0), 320, 30);

        //MV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Copper, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 12),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 12),
        }, Materials.Epoxid.getMolten(144), CustomItemList.teslaCapacitor.getWithDamage(1, 1), 320, 120);

        //HV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Gold, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 16),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 16),
        }, Materials.Epoxid.getMolten(216), CustomItemList.teslaCapacitor.getWithDamage(1, 2), 320, 480);

        //EV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Aluminium, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 20),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 20),
        }, Materials.Epoxid.getMolten(288), CustomItemList.teslaCapacitor.getWithDamage(1, 3), 320, 1920);

        //IV Tesla Capacitor
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.cableGt01, Materials.Tungsten, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Aluminium, 24),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silicone, 24),
        }, Materials.Epoxid.getMolten(360), CustomItemList.teslaCapacitor.getWithDamage(1, 4), 320, 7680);

        //endregion

        //region recycling

        //LV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(CustomItemList.teslaCapacitor.getWithDamage(1, 0),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 4), 300, 2);

        //MV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(CustomItemList.teslaCapacitor.getWithDamage(1, 1),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 6), 300, 2);

        //HV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(CustomItemList.teslaCapacitor.getWithDamage(1, 2),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 8), 300, 2);

        //EV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(CustomItemList.teslaCapacitor.getWithDamage(1, 3),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 10), 300, 2);

        //IV Tesla Capacitor
        GT_Values.RA.addExtractorRecipe(CustomItemList.teslaCapacitor.getWithDamage(1, 4),
                GT_OreDictUnificator.get(OrePrefixes.itemCasing, Materials.BatteryAlloy, 12), 300, 2);

        //endregion

        register_machine_EM_behaviours();
    }

    private void register_machine_EM_behaviours() {
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(5), ItemList.Machine_IV_Centrifuge.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(6), getItemContainer("CentrifugeLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(7), getItemContainer("CentrifugeZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(8), getItemContainer("CentrifugeUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(9), getItemContainer("CentrifugeUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(10), getItemContainer("CentrifugeUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(11), getItemContainer("CentrifugeUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Centrifuge(12), getItemContainer("CentrifugeUMV").get(1));

        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(5), ItemList.Machine_IV_ElectromagneticSeparator.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(6), getItemContainer("ElectromagneticSeparatorLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(7), getItemContainer("ElectromagneticSeparatorZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(8), getItemContainer("ElectromagneticSeparatorUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(9), getItemContainer("ElectromagneticSeparatorUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(10), getItemContainer("ElectromagneticSeparatorUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(11), getItemContainer("ElectromagneticSeparatorUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_ElectromagneticSeparator(12), getItemContainer("ElectromagneticSeparatorUMV").get(1));

        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(5), ItemList.Machine_IV_Recycler.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(6), getItemContainer("RecyclerLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(7), getItemContainer("RecyclerZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(8), getItemContainer("RecyclerUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(9), getItemContainer("RecyclerUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(10), getItemContainer("RecyclerUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(11), getItemContainer("RecyclerUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(() -> new Behaviour_Recycler(12), getItemContainer("RecyclerUMV").get(1));
    }
}