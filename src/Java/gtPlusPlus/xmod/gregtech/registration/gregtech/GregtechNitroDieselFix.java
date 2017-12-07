package gtPlusPlus.xmod.gregtech.registration.gregtech;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

import gregtech.GT_Mod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Materials;
import gregtech.api.enums.TextureSet;
import gregtech.api.objects.MaterialStack;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class GregtechNitroDieselFix {

	@SuppressWarnings("unchecked")
	public static void run(){
		if (CORE.configSwitches.enableNitroFix){

			if (GT_Mod.VERSION == 509){
				Class<GT_Mod> clazz;
				try {
					clazz = (Class<GT_Mod>) Class.forName("gregtech.GT_Mod");
					Field mSubversion = ReflectionUtils.getField(clazz, "SUBVERSION");
					if (mSubversion != null){
						int mSub = 0;
						mSub = mSubversion.getInt(clazz);
						if (mSub != 0){
							if (mSub >= 31){								
								Class mb = Class.forName("gregtech.api.enums.MaterialBuilder");
								Object df = mb.getConstructor(int.class, TextureSet.class, String.class).newInstance(975, TextureSet.SET_FLUID, "Nitro-Diesel [Old]");
								if (mb.isInstance(df)){
									//Get Methods
									Method addFluid = mb.getMethod("addFluid");
									Method addCell = mb.getMethod("addCell");									
									Method setColour = mb.getMethod("setColour", Dyes.class);
									Method setFuelPower = mb.getMethod("setFuelPower", int.class);
									Method setMaterials = mb.getMethod("setMaterialList", List.class);
									Method setTemp = mb.getMethod("setLiquidTemperature", int.class);
									Method setRGB = mb.getMethod("setRGB", int.class, int.class, int.class);							
									Method construct = mb.getMethod("constructMaterial");
									
									//Invoke the methods
									addFluid.invoke(df);
									addCell.invoke(df);									
									setColour.invoke(df, Dyes.dyeLime);
									setFuelPower.invoke(df, 512000);
									setMaterials.invoke(df, Arrays.asList(new MaterialStack(Materials.Glyceryl, 1), new MaterialStack(Materials.Fuel, 4)));
									setTemp.invoke(df, 384);
									setRGB.invoke(df, 200, 255, 0);									
									construct.invoke(df);
									
								}
								//GT_Mod.gregtechproxy.addFluid("NitroFuel_Old", "Nitro Diesel [Old]", Materials.NitroFuel, 1, 295, GT_OreDictUnificator.get(OrePrefixes.cell, Materials.NitroFuel, 1L), ItemList.Cell_Empty.get(1L, new Object[0]), 1000);
							}
						}
					}
				}
				catch (ClassNotFoundException | NoSuchFieldException | IllegalArgumentException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException | SecurityException e) {
					
				}				
			}
		}
	}
}
