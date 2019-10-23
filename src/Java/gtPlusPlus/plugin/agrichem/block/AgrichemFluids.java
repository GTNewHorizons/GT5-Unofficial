package gtPlusPlus.plugin.agrichem.block;

import gtPlusPlus.core.util.minecraft.FluidUtils;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class AgrichemFluids {

	/*
	 * Saline Water - saltwater
	 * Sulfuric Waste Water - sulfuricapatite
	 * Methanol - methanol
	 * Hot Water - ic2hotwater
	 * Acetic Acid
	 * Propionic Acid
	 * Fermentation Base
	 * Ethylene - ethylene
	 * Ethanol - bioethanol
	 * Diluted SA - filutedsulfuricacid
	 * Sulfuric Acid - sulfuricacid
	 * Urea
	 * Formaldehyde - fluid.formaldehyde
	 * Liquid Resin
	 * Methane - methane
	 * Benzene - benzene
	 * Ethylbenzene - fluid.ethylbenzene
	 * Styrene - styrene
	 */

	public static Fluid mAceticAcid;
	public static Fluid mPropionicAcid;
	public static Fluid mFermentationBase;
	public static Fluid mUrea;
	public static Fluid mLiquidResin;
	public static Fluid mAcetone;
	public static Fluid mButanol;
	
	
	
	public static void init() {
		if (!FluidRegistry.isFluidRegistered("aceticacid")) {
			mAceticAcid = FluidUtils.generateFluidNoPrefix("aceticacid", "Acetic Acid", 200, new short[] { 97, 168, 96, 100 }, true);			
		}
		else {
			mAceticAcid = FluidRegistry.getFluid("aceticacid");
		}
		if (!FluidRegistry.isFluidRegistered("propionicacid")) {
			mPropionicAcid = FluidUtils.generateFluidNoPrefix("propionicacid", "Propionic Acid", 200,	new short[] { 198, 209, 148, 100 }, true);			
		}
		else {
			mPropionicAcid = FluidRegistry.getFluid("propionicacid");
		}
		if (!FluidRegistry.isFluidRegistered("fermentation.base")) {
			mFermentationBase = FluidUtils.generateFluidNoPrefix("fermentation.base", "Fermentation Base", 200,	new short[] { 107, 100, 63, 100 }, true);			
		}
		else {
			mFermentationBase = FluidRegistry.getFluid("fermentation.base");
		}
		if (!FluidRegistry.isFluidRegistered("ureamix")) {
			mUrea = FluidUtils.generateFluidNoPrefix("ureamix", "Urea Mix", 200,	new short[] { 71, 55, 12, 100 }, true);			
		}
		else {
			mUrea = FluidRegistry.getFluid("ureamix");
		}
		if (!FluidRegistry.isFluidRegistered("liquidresin")) {
			mLiquidResin = FluidUtils.generateFluidNoPrefix("liquidresin", "Liquid Resin", 200,	new short[] { 59, 58, 56, 100 }, true);			
		}
		else {
			mLiquidResin = FluidRegistry.getFluid("liquidresin");
		}
		
		if (!FluidRegistry.isFluidRegistered("acetone")) {
			mAcetone = FluidUtils.generateFluidNoPrefix("acetone", "Acetone", 200,	new short[] { 59, 58, 56, 100 }, true);			
		}
		else {
			mAcetone = FluidRegistry.getFluid("acetone");
		}
		if (!FluidRegistry.isFluidRegistered("butanol")) {
			mButanol = FluidUtils.generateFluidNoPrefix("butanol", "Butanol", 200,	new short[] { 159, 58, 56, 100 }, true);			
		}
		else {
			mButanol = FluidRegistry.getFluid("butanol");
		}
	}
	
	
	
}
