package com.github.technus.tectech.compatibility.spartakcore;

import com.github.technus.tectech.recipe.TT_recipeAdder;
import com.github.technus.tectech.thing.CustomItemList;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
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
 * Created by Spartak1997 on 28.07.2019.
 */
public class SpartakCoreRecipeLoader implements Runnable {
    
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
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Silver, 8)
        }, Materials.Polytetrafluoroethylene.getMolten(144), CustomItemList.DATApipe.get(1), 200, 30720);

        //endregion

        //Tunnel
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.DATApipe.get(1),
                GT_OreDictUnificator.get(OrePrefixes.plateDouble, Materials.Osmiridium, 4),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 4),
                GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Superconductor, 2),
                ItemList.Field_Generator_MV.get(1),
                ItemList.Circuit_Quantummainframe.get(1)
        }, Materials.Osmium.getMolten(288), CustomItemList.EMpipe.get(1), 400, 500000);
        
        //Laser
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.DATApipe.get(1),
                GT_ModHandler.getIC2Item("reinforcedGlass", 1L),
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmiridium, 2)
        }, null, CustomItemList.LASERpipe.get(1), 100, 500000);
        
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
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
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
                GT_OreDictUnificator.get(OrePrefixes.foil, Materials.Osmium, 12),
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
                        getOrDefault("Trinium",Materials.Americium).getMolten(1296),
                        Materials.Osmium.getMolten(1296),
                        new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                        Materials.Argon.getGas(1000),
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

        //Dynamo Hatches IV-UHV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2)},
                Materials.Silver.getMolten(144),
                CustomItemList.eM_dynamoMulti4_IV.get(1), 100, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti4_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4)},
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_dynamoMulti16_IV.get(1), 200, 1920);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti16_IV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6)},
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_dynamoMulti64_IV.get(1), 400, 1920);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2)},
                Materials.Silver.getMolten(288),
                CustomItemList.eM_dynamoMulti4_LuV.get(1), 100, 7860);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti4_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4)},
                Materials.Electrum.getMolten(288),
                CustomItemList.eM_dynamoMulti16_LuV.get(1), 200, 7860);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti16_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6)},
                Materials.Tungsten.getMolten(288),
                CustomItemList.eM_dynamoMulti64_LuV.get(1), 400, 7860);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2)},
                Materials.Silver.getMolten(576),
                CustomItemList.eM_dynamoMulti4_ZPM.get(1), 100, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti4_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)},
                Materials.Electrum.getMolten(576),
                CustomItemList.eM_dynamoMulti16_ZPM.get(1), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti16_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6)},
                Materials.Tungsten.getMolten(576),
                CustomItemList.eM_dynamoMulti64_ZPM.get(1), 400, 30720);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)},
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_dynamoMulti4_UV.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti4_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)},
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_dynamoMulti16_UV.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti16_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)},
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_dynamoMulti64_UV.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Dynamo_MAX.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)},
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_dynamoMulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti4_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)},
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_dynamoMulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_dynamoMulti16_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)},
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_dynamoMulti64_UHV.get(1), 400, 500000);

        //Energy Hatches 4A-64A  IV-UHV
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        ItemList.Hatch_Energy_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 2)},
                Materials.Silver.getMolten(144),
                CustomItemList.eM_energyMulti4_IV.get(1), 100, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        CustomItemList.eM_energyMulti4_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 4)},
                Materials.Electrum.getMolten(144),
                CustomItemList.eM_energyMulti16_IV.get(1), 200, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                        CustomItemList.eM_energyMulti16_IV.get(1),
                        GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Tungsten, 2),
                        GT_OreDictUnificator.get(OrePrefixes.plate, Materials.TungstenSteel, 6)},
                Materials.Tungsten.getMolten(144),
                CustomItemList.eM_energyMulti64_IV.get(1), 400, 7680);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 2)},
                Materials.Silver.getMolten(288),
                CustomItemList.eM_energyMulti4_LuV.get(1), 100, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti4_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 4)},
                Materials.Electrum.getMolten(288),
                CustomItemList.eM_energyMulti16_LuV.get(1), 200, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti16_LuV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.VanadiumGallium, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Chrome, 6)},
                Materials.Tungsten.getMolten(288),
                CustomItemList.eM_energyMulti64_LuV.get(1), 400, 30720);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_ZPM.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 2)},
                Materials.Silver.getMolten(576),
                CustomItemList.eM_energyMulti4_ZPM.get(1), 100, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                 CustomItemList.eM_energyMulti4_ZPM.get(1),
                 GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 2),
                 GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 4)},
                Materials.Electrum.getMolten(576),
                CustomItemList.eM_energyMulti16_ZPM.get(1), 200, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                 CustomItemList.eM_energyMulti16_ZPM.get(1),
                 GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Naquadah, 2),
                 GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Iridium, 6)},
                Materials.Tungsten.getMolten(576),
                CustomItemList.eM_energyMulti64_ZPM.get(1), 400, 122880);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 2)},
                Materials.Silver.getMolten(1152),
                CustomItemList.eM_energyMulti4_UV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti4_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 4)},
                Materials.Electrum.getMolten(1152),
                CustomItemList.eM_energyMulti16_UV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti16_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.NaquadahAlloy, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Osmium, 6)},
                Materials.Tungsten.getMolten(1152),
                CustomItemList.eM_energyMulti64_UV.get(1), 400, 500000);

        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                ItemList.Hatch_Energy_MAX.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 2)},
                Materials.Silver.getMolten(2304),
                CustomItemList.eM_energyMulti4_UHV.get(1), 100, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti4_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 4)},
                Materials.Electrum.getMolten(2304),
                CustomItemList.eM_energyMulti16_UHV.get(1), 200, 500000);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_energyMulti16_UHV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.wireGt12, Materials.Superconductor, 2),
                GT_OreDictUnificator.get(OrePrefixes.plate, Materials.Neutronium, 6)},
                Materials.Tungsten.getMolten(2304),
                CustomItemList.eM_energyMulti64_UHV.get(1), 400, 500000);

        //Laser Dynamo IV-UV 256/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_IV.get(1), ItemList.Electric_Pump_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_IV.get(1), 1000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_LuV.get(1), 1000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_ZPM.get(1), 1000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Emitter_UV.get(1), ItemList.Electric_Pump_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_dynamoTunnel1_UV.get(1), 1000, 500000);
        
        //Laser Dynamo IV-UV 1024/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_IV.get(2), ItemList.Electric_Pump_IV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_IV.get(1), 2000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_LuV.get(2), ItemList.Electric_Pump_LuV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_LuV.get(1), 2000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_ZPM.get(1), 2000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Emitter_UV.get(2), ItemList.Electric_Pump_UV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_dynamoTunnel2_UV.get(1), 2000, 500000);
        
        //Laser Dynamo IV-UV 4096/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_IV.get(4), ItemList.Electric_Pump_IV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_IV.get(1), 4000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_LuV.get(4), ItemList.Electric_Pump_LuV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_LuV.get(1), 4000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_ZPM.get(1), 4000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Emitter_UV.get(4), ItemList.Electric_Pump_UV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_dynamoTunnel3_UV.get(1), 4000, 500000);
        
        //Laser Dynamo IV-UV 16384/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_IV.get(8), ItemList.Electric_Pump_IV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_IV.get(1), 8000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_LuV.get(8), ItemList.Electric_Pump_LuV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_LuV.get(1), 8000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_ZPM.get(1), 8000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Emitter_UV.get(8), ItemList.Electric_Pump_UV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_dynamoTunnel4_UV.get(1), 8000, 500000);
        
        //Laser Dynamo IV-UV 65536/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_IV.get(16), ItemList.Electric_Pump_IV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_IV.get(1), 16000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_LuV.get(16), ItemList.Electric_Pump_LuV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_LuV.get(1), 16000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_ZPM.get(1), 16000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Emitter_UV.get(16), ItemList.Electric_Pump_UV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_dynamoTunnel5_UV.get(1), 16000, 500000);
        
        //Laser Dynamo IV-UV 262144/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_IV.get(32), ItemList.Electric_Pump_IV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_IV.get(1), 32000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_LuV.get(32), ItemList.Electric_Pump_LuV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_LuV.get(1), 32000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_ZPM.get(1), 32000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Emitter_UV.get(32), ItemList.Electric_Pump_UV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_dynamoTunnel6_UV.get(1), 32000, 500000);
        
        //Laser Dynamo IV-UV 1048576/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_IV.get(64), ItemList.Electric_Pump_IV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_IV.get(1), 64000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_LuV.get(64), ItemList.Electric_Pump_LuV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_LuV.get(1), 64000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_ZPM.get(1), 64000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Emitter_UV.get(64), ItemList.Electric_Pump_UV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_dynamoTunnel7_UV.get(1), 64000, 500000);
        
        //Laser Target IV-UV 256/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_IV.get(1), ItemList.Electric_Pump_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.TungstenSteel, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_IV.get(1), 1000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_LuV.get(1), ItemList.Electric_Pump_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.VanadiumGallium, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_LuV.get(1), 1000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_ZPM.get(1), ItemList.Electric_Pump_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.Naquadah, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_ZPM.get(1), 1000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1), ItemList.Sensor_UV.get(1), ItemList.Electric_Pump_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.wireGt01, Materials.NaquadahAlloy, 2), GT_Utility.getIntegratedCircuit(1)}, null, CustomItemList.eM_energyTunnel1_UV.get(1), 1000, 500000);
        
        //Laser Target IV-UV 1024/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_IV.get(2), ItemList.Electric_Pump_IV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_IV.get(1), 2000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_LuV.get(2), ItemList.Electric_Pump_LuV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_LuV.get(1), 2000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_ZPM.get(2), ItemList.Electric_Pump_ZPM.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_ZPM.get(1), 2000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 2), ItemList.Sensor_UV.get(2), ItemList.Electric_Pump_UV.get(2), GT_OreDictUnificator.get(OrePrefixes.wireGt02, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(2)}, null, CustomItemList.eM_energyTunnel2_UV.get(1), 2000, 500000);
        
        //Laser Target IV-UV 4096/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_IV.get(4), ItemList.Electric_Pump_IV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_IV.get(1), 4000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_LuV.get(4), ItemList.Electric_Pump_LuV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_LuV.get(1), 4000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_ZPM.get(4), ItemList.Electric_Pump_ZPM.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_ZPM.get(1), 4000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 4), ItemList.Sensor_UV.get(4), ItemList.Electric_Pump_UV.get(4), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 4), GT_Utility.getIntegratedCircuit(3)}, null, CustomItemList.eM_energyTunnel3_UV.get(1), 4000, 500000);
        
        //Laser Target IV-UV 16384/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_IV.get(8), ItemList.Electric_Pump_IV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_IV.get(1), 8000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_LuV.get(8), ItemList.Electric_Pump_LuV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_LuV.get(1), 8000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_ZPM.get(8), ItemList.Electric_Pump_ZPM.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_ZPM.get(1), 8000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 8), ItemList.Sensor_UV.get(8), ItemList.Electric_Pump_UV.get(8), GT_OreDictUnificator.get(OrePrefixes.wireGt04, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(4)}, null, CustomItemList.eM_energyTunnel4_UV.get(1), 8000, 500000);
        
        //Laser Target IV-UV 65536/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_IV.get(16), ItemList.Electric_Pump_IV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_IV.get(1), 16000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_LuV.get(16), ItemList.Electric_Pump_LuV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_LuV.get(1), 16000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_ZPM.get(16), ItemList.Electric_Pump_ZPM.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_ZPM.get(1), 16000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 16), ItemList.Sensor_UV.get(16), ItemList.Electric_Pump_UV.get(16), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 8), GT_Utility.getIntegratedCircuit(5)}, null, CustomItemList.eM_energyTunnel5_UV.get(1), 16000, 500000);
        
        //Laser Target IV-UV 262144/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_IV.get(32), ItemList.Electric_Pump_IV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_IV.get(1), 32000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_LuV.get(32), ItemList.Electric_Pump_LuV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_LuV.get(1), 32000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_ZPM.get(32), ItemList.Electric_Pump_ZPM.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_ZPM.get(1), 32000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 32), ItemList.Sensor_UV.get(32), ItemList.Electric_Pump_UV.get(32), GT_OreDictUnificator.get(OrePrefixes.wireGt08, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(6)}, null, CustomItemList.eM_energyTunnel6_UV.get(1), 32000, 500000);
        
        //Laser Target IV-UV 1048576/t
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_IV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_IV.get(64), ItemList.Electric_Pump_IV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.TungstenSteel, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_IV.get(1), 64000, 7680);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_LuV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_LuV.get(64), ItemList.Electric_Pump_LuV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.VanadiumGallium, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_LuV.get(1), 64000, 30720);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_ZPM.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_ZPM.get(64), ItemList.Electric_Pump_ZPM.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.Naquadah, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_ZPM.get(1), 64000, 122880);
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{ItemList.Hull_UV.get(1), GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 64), ItemList.Sensor_UV.get(64), ItemList.Electric_Pump_UV.get(64), GT_OreDictUnificator.get(OrePrefixes.wireGt16, Materials.NaquadahAlloy, 16), GT_Utility.getIntegratedCircuit(7)}, null, CustomItemList.eM_energyTunnel7_UV.get(1), 64000, 500000);

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
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Sensor_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_in_UV.get(1), 800, 500000);
        //Elemental Output
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
                CustomItemList.eM_Containment.get(1),
                ItemList.Hatch_Output_UV.get(1),
                GT_OreDictUnificator.get(OrePrefixes.pipeMedium, Materials.Naquadah, 2),
                ItemList.Emitter_UV.get(1)
        }, Materials.Osmiridium.getMolten(1296), CustomItemList.eM_out_UV.get(1), 800, 500000);
        //Overflow
        GT_Values.RA.addAssemblerRecipe(new ItemStack[]{
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
                new ItemStack[]{GT_OreDictUnificator.get(OrePrefixes.wireGt04, getOrDefault("SuperconductorUV",Materials.Superconductor), 8)},
                CustomItemList.DATApipe.get(8),
        }, new FluidStack[]{
                Materials.UUMatter.getFluid(1000),
                Materials.Iridium.getMolten(1296),
                new FluidStack(FluidRegistry.getFluid("ic2coolant"), 2000),
                Materials.Hydrogen.getGas(1000),
        }, CustomItemList.Machine_Multi_Computer.get(1), 12000, 100000);

        //Research Station
        GT_Values.RA.addAssemblylineRecipe(ItemList.Machine_IV_Scanner.get(1), 80000, new Object[]{
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
    }
}
