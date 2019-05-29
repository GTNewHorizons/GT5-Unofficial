package com.github.technus.tectech.compatibility.dreamcraft;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Centrifuge;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_ElectromagneticSeparator;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import cpw.mods.fml.common.Loader;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.IItemContainer;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
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
        }, getOrDefault("Trinium",Materials.Osmium).getMolten(576), new ItemStack(QuantumGlassBlock.INSTANCE, 1), 200, 500000);

        //region pipes

        //Data
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                ItemList.Circuit_Parts_GlassFiber.get(8),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8)
        }, Materials.Polytetrafluoroethylene.getMolten(144), CustomItemList.DATApipe.get(1), 200, 30720);

        //endregion

        //Tunnel
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.DATApipe.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.SuperconductorUHV, 2),
                ItemList.Field_Generator_MV.get(1),
                ItemList.Circuit_Quantummainframe.get(1)
        }, Materials.Osmium.getMolten(288), CustomItemList.EMpipe.get(1), 400, 500000);
        
        //endregoin

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
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, getOrDefault("SuperconductorIV",Materials.Superconductor), 1)
        }, Materials.SolderingAlloy.getMolten(1296), CustomItemList.eM_Computer_Vent.get(1), 100, 1920);
        //Advanced Computer Casing
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Cobalt, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, getOrDefault("SuperconductorLuV",Materials.Superconductor), 4)
        }, Materials.Iridium.getMolten(1296), CustomItemList.eM_Computer_Bus.get(1), 200, 122880);

        //Molecular Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Osmiridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.foil, getOrDefault("Trinium",Materials.Osmium), 12),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                ItemList.Field_Generator_IV.get(1)
        }, Materials.Osmium.getMolten(1296), CustomItemList.eM_Containment.get(1), 800, 500000);

        //Hollow Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.eM_Containment.get(1),
                12000,32, 500000, 6, new ItemStack[]{
                        CustomItemList.eM_Containment.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDense, Materials.Neutronium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateQuadruple, Materials.Plutonium, 4),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("BlackPlutonium",Materials.Americium), 16),
                        GT_OreDictUnificator.get(OrePrefixes.screw, getOrDefault("Quantium",Materials.Neutronium), 16),
                }, new FluidStack[]{
                        getOrDefault("Trinium",Materials.Osmium).getMolten(1296),
                        Materials.Osmium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Argon.getGas(576),
                }, CustomItemList.eM_Hollow.get(2), 200, 2000000);

        //EM Coil
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.eM_Hollow.get(1),
                48000,128, 1000000, 16, new ItemStack[]{
                        CustomItemList.eM_Hollow.get(1),
                        ItemList.Casing_Fusion_Coil.get(4),
                        ItemList.Casing_Coil_NaquadahAlloy.get( 4),
                        GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Neutronium, 64),
                        GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Neutronium, 64),
                }, new FluidStack[]{
                        Materials.Glass.getMolten(2304),
                        Materials.Silicone.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        getOrDefault("Trinium",Materials.Osmium).getMolten(1296),
                }, CustomItemList.eM_Coil.get(4), 800, 2000000);

        //endregion


        //region hatches

        //Dynamo Hatches IV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2)}, Materials.Silver.getMolten(144), CustomItemList.eM_dynamomulti4_IV.get(1), 100, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_LuV_IV.get(1), CustomItemList.eM_dynamomulti4_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4)}, Materials.Electrum.getMolten(144), CustomItemList.eM_dynamomulti16_IV.get(1), 200, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_LuV_IV").get(1), CustomItemList.eM_dynamomulti16_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6)}, Materials.Tungsten.getMolten(144), CustomItemList.eM_dynamomulti64_IV.get(1), 400, 1920);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2)}, Materials.Silver.getMolten(288), CustomItemList.eM_dynamomulti4_LuV.get(1), 100, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_ZPM_LuV.get(1), CustomItemList.eM_dynamomulti4_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4)}, Materials.Electrum.getMolten(288), CustomItemList.eM_dynamomulti16_LuV.get(1), 200, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_ZPM_LuV").get(1), CustomItemList.eM_dynamomulti16_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6)}, Materials.Tungsten.getMolten(288), CustomItemList.eM_dynamomulti64_LuV.get(1), 400, 7680);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2)}, Materials.Silver.getMolten(576), CustomItemList.eM_dynamomulti4_ZPM.get(1), 100, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_UV_ZPM.get(1), CustomItemList.eM_dynamomulti4_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)}, Materials.Electrum.getMolten(576), CustomItemList.eM_dynamomulti16_ZPM.get(1), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UV_ZPM").get(1), CustomItemList.eM_dynamomulti16_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6)}, Materials.Tungsten.getMolten(576), CustomItemList.eM_dynamomulti64_ZPM.get(1), 400, 30720);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1152), CustomItemList.eM_dynamomulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_dynamomulti4_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1152), CustomItemList.eM_dynamomulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UHV_UV").get(1), CustomItemList.eM_dynamomulti16_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1152), CustomItemList.eM_dynamomulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Dynamo_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2304), CustomItemList.eM_dynamomulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UEV_UHV").get(1), CustomItemList.eM_dynamomulti4_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2304), CustomItemList.eM_dynamomulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UEV_UHV").get(1), CustomItemList.eM_dynamomulti16_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2304), CustomItemList.eM_dynamomulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hatch_Dynamo_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium), 2)}, Materials.Silver.getMolten(4608), CustomItemList.eM_dynamomulti4_UEV.get(1), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UIV_UEV").get(1), CustomItemList.eM_dynamomulti4_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium), 4)}, Materials.Electrum.getMolten(4608), CustomItemList.eM_dynamomulti16_UEV.get(1), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UIV_UEV").get(1), CustomItemList.eM_dynamomulti16_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium), 6)}, Materials.Tungsten.getMolten(4608), CustomItemList.eM_dynamomulti64_UEV.get(1), 400, 2000000);

        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Dynamo_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_dynamomulti4_UIV.get(1), 100, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1), CustomItemList.eM_dynamomulti4_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_dynamomulti16_UIV.get(1), 200, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1), CustomItemList.eM_dynamomulti16_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_dynamomulti64_UIV.get(1), 400, 8000000);

        //Energy Hatches  UV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)}, Materials.Silver.getMolten(1000), CustomItemList.eM_energymulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Transformer_MAX_UV.get(1), CustomItemList.eM_energymulti4_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)}, Materials.Electrum.getMolten(1000), CustomItemList.eM_energymulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UHV_UV").get(1), CustomItemList.eM_energymulti16_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)}, Materials.Tungsten.getMolten(1000), CustomItemList.eM_energymulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hatch_Energy_MAX.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)}, Materials.Silver.getMolten(2000), CustomItemList.eM_energymulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UEV_UHV").get(1), CustomItemList.eM_energymulti4_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)}, Materials.Electrum.getMolten(2000), CustomItemList.eM_energymulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UEV_UHV").get(1), CustomItemList.eM_energymulti16_UHV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.SuperconductorUHV, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)}, Materials.Tungsten.getMolten(2000), CustomItemList.eM_energymulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Hatch_Energy_UEV").get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium), 2)}, Materials.Silver.getMolten(4000), CustomItemList.eM_energymulti4_UEV.get(1), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("Transformer_UIV_UEV").get(1), CustomItemList.eM_energymulti4_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium), 4)}, Materials.Electrum.getMolten(4000), CustomItemList.eM_energymulti16_UEV.get(1), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{getItemContainer("WetTransformer_UIV_UEV").get(1), CustomItemList.eM_energymulti16_UEV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Draconium, 2), GT_OreDictUnificator.get(OrePrefixes.plate, getOrDefault("Bedrockium",Materials.Neutronium),6)}, Materials.Tungsten.getMolten(4000), CustomItemList.eM_energymulti64_UEV.get(1), 400, 2000000);

        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Hatch_Energy_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 2)}, Materials.Silver.getMolten(8000), CustomItemList.eM_energymulti4_UIV.get(1), 100, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.Transformer_UMV_UIV.get(1), CustomItemList.eM_energymulti4_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 4)}, Materials.Electrum.getMolten(8000), CustomItemList.eM_energymulti16_UIV.get(1), 200, 8000000);
        //GT_Values.RA.ADD_ASSEMBLER_RECIPE(new ItemStack[]{com.dreammaster.gthandler.CustomItemList.WetTransformer_UMV_UIV.get(1), CustomItemList.eM_energymulti16_UIV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NetherStar, 2), GT_OreDictUnificator.get(OrePrefixes.plate, Materials.BlackPlutonium, 6)}, Materials.Tungsten.getMolten(8000), CustomItemList.eM_energymulti64_UIV.get(1), 400, 8000000);

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
        GT_Values.RA.addAssemblylineRecipe(ItemList.Hatch_Input_Bus_ZPM.get(1), 10000, new ItemStack[]{
                ItemList.Hatch_Input_Bus_ZPM.get(1),
                CustomItemList.eM_Computer_Bus.get(1),
                ItemList.Emitter_ZPM.get(8),
                ItemList.Robot_Arm_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                ItemList.Circuit_Crystalmainframe.get(1),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 16),
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
                ItemList.Cover_Screen.get(1 ),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Parametrizer_Hatch.get(1), 800, 122880);
        //Uncertainty
        addAssemblerRecipeWithCleanroom(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1),
                CustomItemList.DATApipe.get(16),
                ItemList.Cover_Screen.get(1 ),
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


        //region multi blocks

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
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, getOrDefault("SuperconductorLuV",Materials.Superconductor), 16),
                ItemList.valueOf("Circuit_Chip_UHPIC").get(2),
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
        GT_Values.RA.addAssemblylineRecipe(ItemList.Tool_DataOrb.get(1), 20000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 2),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.SuperconductorUV, 8),
                CustomItemList.DATApipe.get(8),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000),
        }, CustomItemList.Machine_Multi_Computer.get(1), 12000, 100000);

        //Research Station
        GT_Values.RA.addAssemblylineRecipe(getItemContainer("ScannerZPM").get(1), 80000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                ItemList.Sensor_ZPM.get(8),
                ItemList.Circuit_Crystalmainframe.get(4),
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Naquadah, 32),
                CustomItemList.DATApipe.get(16),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Osmium.getMolten(1296),
        }, CustomItemList.Machine_Multi_Research.get(1), 12000, 100000);

        //Matter Junction
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.Machine_Multi_Switch.get(1),
                8000,32, 500000, 4, new ItemStack[]{
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
                12000,32, 500000, 6, new ItemStack[]{
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
                12000,32, 500000, 6, new ItemStack[]{
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
                15000,32, 500000, 8, new ItemStack[]{
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
                15000,32, 500000, 8,  new ItemStack[]{
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
                150000,128, 500000, 16,  new ItemStack[]{
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
                192000,512, 2000000, 32, new ItemStack[]{
                        CustomItemList.Machine_Multi_Transformer.get(1),
                        CustomItemList.eM_Coil.get(8),
                        CustomItemList.eM_Power.get( 8),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.NeodymiumMagnetic, 16),
                }, new FluidStack[]{
                        Materials.Electrum.getMolten(2592),
                        Materials.Neutronium.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                }, CustomItemList.Machine_Multi_Infuser.get(1), 8000, 2000000);

        //Motor UV-UHV
        TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Electric_Motor_UV.get(1L),
                24000, 32, 100000, 4,  new ItemStack[]{
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
                48000, 64, 2000000, 8,  new ItemStack[]{
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
                24000, 32, 100000, 4,  new ItemStack[]{
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
                48000, 64, 200000, 8,  new ItemStack[]{
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
                24000, 32, 100000, 4,  new ItemStack[]{
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
                48000, 64, 200000, 8,  new ItemStack[]{
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
                24000, 32, 100000, 4,  new ItemStack[]{
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
                48000, 64, 200000, 8,  new ItemStack[]{
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
                24000, 32, 100000, 4,  new Object[]{
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
                48000, 64, 200000, 8,  new Object[]{
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
                24000, 32, 100000, 4,  new Object[]{
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
                48000, 64, 200000, 8,  new Object[]{
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
                24000, 32, 100000, 4,  new Object[]{
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
                48000, 64, 200000, 8,  new Object[]{
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
                48000, 64, 200000, 8,  new Object[]{
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
                96000, 128, 400000, 16,  new Object[]{
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
                24000, 16, 50000, 2,  new Object[]{
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
                48000, 32, 100000, 4,  new Object[]{
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
                24000, 64, 50000, 4,  new ItemStack[]{
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
                48000, 128, 500000, 8,  new ItemStack[]{
                        ItemList.Circuit_Board_Bio_Ultra.get(2L),
                        ItemList.Circuit_Biowarecomputer.get(2L),
                        ItemList.Circuit_Parts_DiodeSMD.get(48L),
                        ItemList.Circuit_Parts_ResistorSMD.get(16L),
                        ItemList.Circuit_Parts_TransistorSMD.get(16L),
                        ItemList.Circuit_Parts_DiodeSMD.get(16L),
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
                96000, 256, 1000000, 16,  new ItemStack[]{
                        GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Tritanium, 4L),
                        ItemList.Circuit_Biowaresupercomputer.get(2L),
                        ItemList.Circuit_Parts_Coil.get(64L),
                        ItemList.Circuit_Parts_Coil.get(64L),
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
                192000, 512, 2000000, 32,  new ItemStack[]{
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
                384000, 1024, 4000000, 64,  new ItemStack[]{
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
        },      getItemContainer("PikoCircuit").get(1L), 10000, 8000000);

        TT_recipeAdder.addResearchableAssemblylineRecipe(getItemContainer("PikoCircuit").get(1L),
                720000, 2048, 8000000, 128,  new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt,Materials.Neutronium, 16),
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
        },      getItemContainer("QuantumCircuit").get(1L), 20000, 32000000);

        //Stargate Stuff
        if (Loader.isModLoaded("eternalsingularity")&&Loader.isModLoaded("SGCraft")) {
            TT_recipeAdder.addResearchableAssemblylineRecipe(GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Infinity, 1L),
                    192000, 512, 2000000, 32,  new ItemStack[]{
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
                    192000, 512, 2000000, 32,  new ItemStack[]{
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
                    192000, 512, 2000000, 32,  new ItemStack[]{
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
            TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.Energy_Cluster.get(1L),12000,16,100000,3,new Object[]{
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

            TT_recipeAdder.addResearchableAssemblylineRecipe(ItemList.ZPM2.get(1L),24000,64,200000,6,new Object[]{
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

        register_machine_EM_behaviours();
    }

    private void register_machine_EM_behaviours(){
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(5),ItemList.Machine_IV_Centrifuge.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(6),getItemContainer("CentrifugeLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(7),getItemContainer("CentrifugeZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(8),getItemContainer("CentrifugeUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(9),getItemContainer("CentrifugeUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(10),getItemContainer("CentrifugeUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(11),getItemContainer("CentrifugeUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(12),getItemContainer("CentrifugeUMV").get(1));

        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(5),ItemList.Machine_IV_ElectromagneticSeparator.get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(6),getItemContainer("ElectromagneticSeparatorLuV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(7),getItemContainer("ElectromagneticSeparatorZPM").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(8),getItemContainer("ElectromagneticSeparatorUV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(9),getItemContainer("ElectromagneticSeparatorUHV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(10),getItemContainer("ElectromagneticSeparatorUEV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(11),getItemContainer("ElectromagneticSeparatorUIV").get(1));
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(12),getItemContainer("ElectromagneticSeparatorUMV").get(1));
    }
}
