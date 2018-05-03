package com.github.technus.tectech.loader;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_Centrifuge;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.Behaviour_ElectromagneticSeparator;
import com.github.technus.tectech.thing.metaTileEntity.multi.em_machine.GT_MetaTileEntity_EM_machine;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

/**
 * Created by Tec on 06.08.2017.
 */
public class BloodyRecipeLoader implements Runnable {
    @Override
    public void run() {
        //Quantum Glass
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L)
        }, Materials.Osmium.getMolten(576), new ItemStack(QuantumGlassBlock.INSTANCE, 1), 200, 500000);

        //region pipes

        //Data
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Circuit_Parts_GlassFiber.get(8),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Silver, 6)
        }, Materials.Polytetrafluoroethylene.getMolten(144), CustomItemList.DATApipe.get(1), 200, 30720);

        //endregion


        //region casing

        //High Power Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                GT_OreDictUnificator.get(OrePrefixes.frameGt, Materials.Iridium, 1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Iridium, 6),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Master, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Cobalt, 16),
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
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 1)
        }, Materials.SolderingAlloy.getMolten(1296), CustomItemList.eM_Computer_Vent.get(1), 100, 1920);
        //Advanced Computer Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Ultimate, 1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Cobalt, 32),
                GT_OreDictUnificator.get(OrePrefixes.wireFine, Materials.Electrum, 64),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 4)
        }, Materials.Iridium.getMolten(1296), CustomItemList.eM_Computer_Bus.get(1), 200, 122880);

        //Molecular Casing
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Power.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 16),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 12),
                GT_OreDictUnificator.get(OrePrefixes.screw, Materials.TungstenSteel, 24),
                GT_OreDictUnificator.get(OrePrefixes.ring, Materials.TungstenSteel, 24),
                ItemList.Field_Generator_IV.get(1)
        }, Materials.Osmium.getMolten(1296), CustomItemList.eM_Containment.get(1), 800, 500000);

        //Hollow Casing
        TT_recipeAdder.addResearchableAssemblylineRecipe(CustomItemList.eM_Containment.get(1),
                12000,32, 500000, 6, new ItemStack[]{
                        CustomItemList.eM_Containment.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Plutonium, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Lead, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 16),
                        GT_OreDictUnificator.get(OrePrefixes.screw, Materials.Neutronium, 16),
                }, new FluidStack[]{
                        Materials.Americium.getMolten(1296),
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
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Americium, 16),
                }, new FluidStack[]{
                        Materials.Glass.getMolten(2304),
                        Materials.Silicone.getMolten(1872),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Americium.getMolten(1296),
                }, CustomItemList.eM_Coil.get(4), 800, 2000000);

        //endregion


        //region hatches

        //Dynamo Hatches IV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2)},
                Materials.Silver.getMolten(144),
                CustomItemList.eM_dynamomulti4_IV.get(1), 100, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4)},
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_dynamomulti16_IV.get(1), 200, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6)},
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_dynamomulti64_IV.get(1), 400, 1920);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2)},
                Materials.Silver.getMolten(288),
                CustomItemList.eM_dynamomulti4_LuV.get(1), 100, 7860);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4)},
                Materials.Electrum.getMolten(288),
                CustomItemList.eM_dynamomulti16_LuV.get(1), 200, 7860);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6)},
                Materials.Tungsten.getMolten(288),
                CustomItemList.eM_dynamomulti64_LuV.get(1), 400, 7860);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2)},
                Materials.Silver.getMolten(576),
                CustomItemList.eM_dynamomulti4_ZPM.get(1), 100, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)},
                Materials.Electrum.getMolten(576),
                CustomItemList.eM_dynamomulti16_ZPM.get(1), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6)},
                Materials.Tungsten.getMolten(576),
                CustomItemList.eM_dynamomulti64_ZPM.get(1), 400, 30720);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)},
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_dynamomulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)},
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_dynamomulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)},
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_dynamomulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_MAX.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)},
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_dynamomulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)},
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_dynamomulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)},
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_dynamomulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_MAX.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8)},
                Materials.Silver.getMolten(4608),
                CustomItemList.eM_dynamomulti4_UEV.get(1), 500, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti4_UEV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16)},
                Materials.Electrum.getMolten(4608),
                CustomItemList.eM_dynamomulti16_UEV.get(1), 1000, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamomulti16_UEV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 24)},
                Materials.Tungsten.getMolten(4608),
                CustomItemList.eM_dynamomulti64_UEV.get(1), 2000, 500000);

        //Energy Hatches  UV-UIV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)},
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_energymulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti4_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)},
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_energymulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti16_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)},
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_energymulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_MAX.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)},
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_energymulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti4_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)},
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_energymulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti16_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)},
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_energymulti64_UHV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_MAX.get(4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 8)},
            Materials.Silver.getMolten(4608),
            CustomItemList.eM_energymulti4_UEV.get(1), 100, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti4_UEV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 16)},
            Materials.Electrum.getMolten(4608),
            CustomItemList.eM_energymulti16_UEV.get(1), 200, 2000000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energymulti16_UEV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 8),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 24)},
            Materials.Tungsten.getMolten(4608),
            CustomItemList.eM_energymulti64_UEV.get(1), 400, 2000000);

        //Data Input
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Input_Bus_LuV.get(1),
                ItemList.Circuit_Crystalcomputer.get(1),
                CustomItemList.DATApipe.get(2)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataIn_Hatch.get(1), 200, 122880);
        //Data Output
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Hatch_Output_Bus_LuV.get(1),
                ItemList.Circuit_Crystalcomputer.get(1),
                CustomItemList.DATApipe.get(2)
        }, Materials.Iridium.getMolten(1296), CustomItemList.dataOut_Hatch.get(1), 200, 122880);

        //Rack
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
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
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 16),
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
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Computer_Casing.get(1),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1),
                CustomItemList.DATApipe.get(16),
                ItemList.Cover_Screen.get(1 ),
                new ItemStack(Blocks.stone_button, 16),
        }, Materials.Iridium.getMolten(2592), CustomItemList.Uncertainty_Hatch.get(1), 1200, 122880);

        //Elemental Input
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Input_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 8),
                ItemList.Sensor_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_in_UV.get(1), 800, 500000);
        //Elemental Output
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Output_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 8),
                ItemList.Emitter_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_out_UV.get(1), 800, 500000);
        //Overflow
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Muffler_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 4),
                ItemList.Field_Generator_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_muffler_UV.get(1), 800, 500000);

        //endregion


        //region multiblocks

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
                ItemList.Transformer_ZPM_LuV.get(16),
                ItemList.Circuit_Board_Elite.get(1),//?
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Superconductor, 16),
                ItemList.Circuit_Chip_HPIC.get(2),
        }, Materials.TungstenSteel.getMolten(576), CustomItemList.Machine_Multi_Transformer.get(1), 400, 30720);

        //Network Switch
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.Machine_Multi_Transformer.get(1),
                ItemList.Circuit_Ultimatecrystalcomputer.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Cobalt, 32),
                GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Copper, 32),
                CustomItemList.DATApipe.get(4),
        }, Materials.Iridium.getMolten(1296), CustomItemList.Machine_Multi_Switch.get(1), 800, 122880);

        //Quantum Computer
        GT_Values.RA.addAssemblylineRecipe(ItemList.Tool_DataOrb.get(1), 20000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                GT_OreDictUnificator.get(OrePrefixes.circuit, Materials.Superconductor, 2),
                ItemList.Tool_DataOrb.get(1),
                ItemList.Cover_Screen.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 8),
                CustomItemList.DATApipe.get(8),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000),
        }, CustomItemList.Machine_Multi_Computer.get(1), 12000, 100000);

        //Research Station
        GT_Values.RA.addAssemblylineRecipe(ItemList.Tool_Scanner.get(1), 80000, new ItemStack[]{
                CustomItemList.Machine_Multi_Switch.get(1),
                ItemList.Sensor_ZPM.get(8),
                ItemList.Circuit_Crystalmainframe.get(4),
                ItemList.Field_Generator_ZPM.get(1),
                ItemList.Electric_Motor_ZPM.get(2),
                GT_OreDictUnificator.get(OrePrefixes.cableGt02, Materials.Naquadah, 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Naquadah, 32),
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
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Naquadah, 16),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 16),
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
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Naquadah, 16),
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
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8),
                        ItemList.Emitter_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 8),
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
                        GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Neutronium, 8),
                        ItemList.Sensor_UV.get(2),
                        ItemList.Circuit_Wetwaresupercomputer.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 8),
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
                        ItemList.Circuit_Wetwaresupercomputer.get(4),//?
                        GT_OreDictUnificator.get(OrePrefixes.lens,Materials.Diamond,32),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 16),
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

        //endregion

        //ha trafos
        //if(Loader.isModLoaded("miscutils")){
        //    GT_Values.RA.addAssemblerRecipe(CustomItemList.HA)
        //}

        //power trafos
        //GT_Values.RA.addAssemblerRecipe()

        register_machine_EM_behaviours();
    }

    private void register_machine_EM_behaviours(){
        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(6),ItemList.Machine_IV_Centrifuge.get(1));
        try {
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(7),ItemList.valueOf("Machine_LuV_Centrifuge").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(8),ItemList.valueOf("Machine_ZPM_Centrifuge").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(9),ItemList.valueOf("Machine_UV_Centrifuge").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(10),ItemList.valueOf("Machine_UV_Centrifuge").get(4));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(11),ItemList.valueOf("Machine_UV_Centrifuge").get(16));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(12),ItemList.valueOf("Machine_UV_Centrifuge").get(64));
        }catch (IllegalArgumentException|NullPointerException e){
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(7),ItemList.Machine_IV_Centrifuge.get(2));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(8),ItemList.Machine_IV_Centrifuge.get(4));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(9),ItemList.Machine_IV_Centrifuge.get(8));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(10),ItemList.Machine_IV_Centrifuge.get(16));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(11),ItemList.Machine_IV_Centrifuge.get(32));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_Centrifuge(12),ItemList.Machine_IV_Centrifuge.get(64));
        }

        GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(6),ItemList.Machine_IV_ElectromagneticSeparator.get(1));
        try {
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(7),ItemList.valueOf("Machine_LuV_ElectromagneticSeparator").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(8),ItemList.valueOf("Machine_ZPM_ElectromagneticSeparator").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(9),ItemList.valueOf("Machine_UV_ElectromagneticSeparator").get(1));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(10),ItemList.valueOf("Machine_UV_ElectromagneticSeparator").get(4));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(11),ItemList.valueOf("Machine_UV_ElectromagneticSeparator").get(16));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(12),ItemList.valueOf("Machine_UV_ElectromagneticSeparator").get(64));
        }catch (IllegalArgumentException|NullPointerException e){
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(7),ItemList.Machine_IV_ElectromagneticSeparator.get(2));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(8),ItemList.Machine_IV_ElectromagneticSeparator.get(4));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(9),ItemList.Machine_IV_ElectromagneticSeparator.get(8));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(10),ItemList.Machine_IV_ElectromagneticSeparator.get(16));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(11),ItemList.Machine_IV_ElectromagneticSeparator.get(32));
            GT_MetaTileEntity_EM_machine.registerBehaviour(new Behaviour_ElectromagneticSeparator(12),ItemList.Machine_IV_ElectromagneticSeparator.get(64));
        }
    }
}
