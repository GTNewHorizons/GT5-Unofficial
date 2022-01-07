package gtPlusPlus.api.helpers;

import net.minecraft.item.ItemStack;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.metatileentity.implementations.GT_MetaPipeEntity_Fluid;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class MaterialHelper {

	public static ItemStack getComponentFromMaterial(OrePrefixes oreprefix, Material material, int amount){
		return ItemUtils.getOrePrefixStack(oreprefix, material, amount);
	}
	public static ItemStack getComponentFromGtMaterial(OrePrefixes oreprefix, Materials material, int amount){
		return ItemUtils.getGregtechOreStack(oreprefix, material, amount);
	}
	
	/**
	 * Generates a 16 Fluid Pipe 
	 * @see {@code Example: Copper 16x Pipe (Materials.Copper, Materials.Copper.mName, "Copper", ID, 60, 1000, true)}
	 * @param aMaterial - Pipe Material
	 * @param name - Pipe Internal name
	 * @param displayName - Pipe Display Name
	 * @param aID - Pipe's Meta ID
	 * @param baseCapacity - Pipes Base Capacity
	 * @param heatCapacity - Pipe Max Temp
	 * @param gasProof - Is Gas Proof?
	 * @return A boolean which corresponds to whether or not the Pipe was registered to the Ore Dictionary.
	 */
	public static boolean generateHexadecuplePipe(Materials aMaterial, String name, String displayName, int aID,
			int baseCapacity, int heatCapacity, boolean gasProof) {
		if (Utils.getGregtechVersionAsInt() >= 50930) {
			try {
				Class<GT_MetaPipeEntity_Fluid> aPipeEntity = GT_MetaPipeEntity_Fluid.class;
				Constructor<GT_MetaPipeEntity_Fluid> constructor = aPipeEntity
						.getConstructor(new Class[] { int.class, String.class, String.class, float.class,
								Materials.class, int.class, int.class, boolean.class, int.class });
				if (constructor != null) {
					GT_MetaPipeEntity_Fluid aPipe;
					aPipe = constructor.newInstance(aID, "GT_Pipe_" + name + "_Hexadecuple",
							"Hexadecuple " + displayName + " Fluid Pipe", 1.0F, aMaterial, baseCapacity, heatCapacity,
							gasProof, 16);
					return GT_OreDictUnificator.registerOre("pipeHexadecuple" + aMaterial, aPipe.getStackForm(1L));
				}

			} catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException
					| IllegalArgumentException | InvocationTargetException e) {
			}
		}
		return false;
	}
	
	public static ItemStack getCells(Materials aMaterial, int i) {
		return ItemUtils.getOrePrefixStack(OrePrefixes.cell, aMaterial, i);		
	}
	public static ItemStack getDust(Materials aMaterial, int i) {
		return ItemUtils.getOrePrefixStack(OrePrefixes.dust, aMaterial, i);		
	}
	public static ItemStack getDustSmall(Materials aMaterial, int i) {
		return ItemUtils.getOrePrefixStack(OrePrefixes.dustSmall, aMaterial, i);		
	}
	public static ItemStack getDustTiny(Materials aMaterial, int i) {
		return ItemUtils.getOrePrefixStack(OrePrefixes.dustTiny, aMaterial, i);		
	}
	public static ItemStack getGems(Materials aMaterial, int i) {
		return ItemUtils.getOrePrefixStack(OrePrefixes.gem, aMaterial, i);		
	}
}
