package gtPlusPlus.xmod.gregtech.registration.gregtech;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_ModHandler;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV.GT_MetaTileEntity_ULV_CombustionGenerator;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV.GT_MetaTileEntity_ULV_GasTurbine;
import gtPlusPlus.xmod.gregtech.common.tileentities.generators.ULV.GT_MetaTileEntity_ULV_SteamTurbine;

import static gtPlusPlus.core.recipe.common.CI.bitsd;

public class GregtechGeneratorsULV {
    public static void run(){
        
         GregtechItemList.Generator_Diesel_ULV.set(new GT_MetaTileEntity_ULV_CombustionGenerator(960, "basicgenerator.diesel.tier.00", "Simple Combustion Generator", 0).getStackForm(1L));
         GregtechItemList.Generator_Gas_Turbine_ULV.set(new GT_MetaTileEntity_ULV_GasTurbine(961, "basicgenerator.gas.tier.00", "Simple Gas Turbine", 0).getStackForm(1L));
         GregtechItemList.Generator_Steam_Turbine_ULV.set(new GT_MetaTileEntity_ULV_SteamTurbine(962, "basicgenerator.steam.tier.00", "Simple Steam Turbine", 0).getStackForm(1L));
    
         GT_ModHandler.addCraftingRecipe(GregtechItemList.Generator_Diesel_ULV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "EME", "GWG", 'M', ItemList.Hull_ULV, 'P', GregtechItemList.Electric_Piston_ULV, 'E', GregtechItemList.Electric_Motor_ULV, 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'G', OrePrefixes.gearGt.get(Materials.Bronze)});
         GT_ModHandler.addCraftingRecipe(GregtechItemList.Generator_Gas_Turbine_ULV.get(1L, new Object[0]), bitsd, new Object[]{"CRC", "RMR", "EWE", 'M', ItemList.Hull_ULV, 'E', GregtechItemList.Electric_Motor_ULV, 'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy)});
         GT_ModHandler.addCraftingRecipe(GregtechItemList.Generator_Steam_Turbine_ULV.get(1L, new Object[0]), bitsd, new Object[]{"PCP", "RMR", "EWE", 'M', ItemList.Hull_ULV, 'E', GregtechItemList.Electric_Motor_ULV, 'R', OrePrefixes.rotor.get(Materials.Tin), 'C', OrePrefixes.circuit.get(Materials.Primitive), 'W', OrePrefixes.cableGt01.get(Materials.RedAlloy), 'P', OrePrefixes.pipeMedium.get(Materials.Copper)});
            
    }    
}