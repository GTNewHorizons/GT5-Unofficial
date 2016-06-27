package miscutil.gregtech.common;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_Utility;
import miscutil.gregtech.api.enums.GregtechOrePrefixes.GT_Materials;
import miscutil.gregtech.api.objects.GregtechFluid;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class Meta_GT_Proxy {

	public static Fluid addFluid(String aName, String aLocalized, GT_Materials aMaterial, int aState, int aTemperatureK) {
		return addFluid(aName, aLocalized, aMaterial, aState, aTemperatureK, null, null, 0);
	}

	public static Fluid addFluid(String aName, String aLocalized, GT_Materials cryotheum, int aState, int aTemperatureK, ItemStack aFullContainer,
			ItemStack aEmptyContainer, int aFluidAmount) {
		return addFluid(aName, aName.toLowerCase(), aLocalized, cryotheum, null, aState, aTemperatureK, aFullContainer, aEmptyContainer, aFluidAmount);
	}

	public static Fluid addFluid(String aName, String aTexture, String aLocalized, GT_Materials aMaterial, short[] aRGBa, int aState, int aTemperatureK,
			ItemStack aFullContainer, ItemStack aEmptyContainer, int aFluidAmount) {
		aName = aName.toLowerCase();
		Fluid rFluid = new GregtechFluid(aName, aTexture, aRGBa != null ? aRGBa : Dyes._NULL.getRGBA());
		GT_LanguageManager.addStringLocalization(rFluid.getUnlocalizedName(), aLocalized == null ? aName : aLocalized);
		if (FluidRegistry.registerFluid(rFluid)) {
			switch (aState) {
			case 0:
				rFluid.setGaseous(false);
				rFluid.setViscosity(10000);
				break;
			case 1:
			case 4:
				rFluid.setGaseous(false);
				rFluid.setViscosity(1000);
				break;
			case 2:
				rFluid.setGaseous(true);
				rFluid.setDensity(-100);
				rFluid.setViscosity(200);
				break;
			case 3:
				rFluid.setGaseous(true);
				rFluid.setDensity(55536);
				rFluid.setViscosity(10);
				rFluid.setLuminosity(15);
			}
		} else {
			rFluid = FluidRegistry.getFluid(aName);
		}
		if (rFluid.getTemperature() == new Fluid("test").getTemperature()) {
			rFluid.setTemperature(aTemperatureK);
		}
		if (aMaterial != null) {
			switch (aState) {
			case 0:
				aMaterial.mSolid = rFluid;
				break;
			case 1:
				aMaterial.mFluid = rFluid;
				break;
			case 2:
				aMaterial.mGas = rFluid;
				break;
			case 3:
				aMaterial.mPlasma = rFluid;
				break;
			case 4:
				aMaterial.mStandardMoltenFluid = rFluid;
			}
		}
		if ((aFullContainer != null) && (aEmptyContainer != null)
				&& (!FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer))) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, GT_Utility.getContainerItem(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}
		return rFluid;
	}
	
}
