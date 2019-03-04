package gtPlusPlus.xmod.tinkers;

import java.lang.reflect.Field;

import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.xmod.tinkers.material.BaseTinkersMaterial;
import gtPlusPlus.xmod.tinkers.util.TinkersUtils;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class HANDLER_Tinkers {

	public static AutoMap<BaseTinkersMaterial> mTinkerMaterials = new AutoMap<BaseTinkersMaterial>();
	
	public static final void preInit() {
		if (LoadedMods.TiCon) {

		}
	}

	public static final void init() {
		if (LoadedMods.TiCon) {
			//Migrate TiCon further back in the oreDict so that I never grab items from it.
			//TinkersUtils.stopTiconLoadingFirst();
			Fluid pyrotheumFluid = FluidRegistry.getFluid("pyrotheum");
			if (pyrotheumFluid != null) {
				//Enable Pyrotheum as Fuel for the Smeltery
				TinkersUtils.addSmelteryFuel(pyrotheumFluid, 5000, 70); // pyrotheum lasts 3.5 seconds per 15 mb
			}
		}
	}

	public static final void postInit() {		
		if (LoadedMods.TiCon) {
			
				for (BaseTinkersMaterial y : mTinkerMaterials) {
					//y.generate();
				}			
			
				Class aTinkersSmeltery = ReflectionUtils.getClass("tconstruct.smeltery.TinkerSmeltery");
				AutoMap<Fluid> aTweakedFluids = new AutoMap<Fluid>();
				if (aTinkersSmeltery != null) {
					try {
						Logger.INFO("Manipulating the light levels of fluids in TiCon. Molten 'metals' in world are now very luminescent!");
						Field aFluidArrayField = ReflectionUtils.getField(aTinkersSmeltery, "fluids");
						Field aBlockArrayField = ReflectionUtils.getField(aTinkersSmeltery, "fluidBlocks");
						Fluid[] aTiconFluids = (Fluid[]) aFluidArrayField.get(null);
						Block[] aTiconFluidBlocks = (Block[]) aBlockArrayField.get(null);
						if (aTiconFluids != null && aTiconFluidBlocks != null) {
							for (Fluid a : aTiconFluids) {
								if (a == null) {
									continue;
								} else {
									if (a.getLuminosity() <= 15) {
										//if (a.getTemperature() >= 500) {
											a.setLuminosity(16);
											aTweakedFluids.put(a);
										//}
									} else {
										aTweakedFluids.put(a);
										continue;
									}
								}
							}
							for (Block a : aTiconFluidBlocks) {
								if (a == null) {
									continue;
								} else {
									Fluid f = FluidRegistry.lookupFluidForBlock(a);
									boolean isHot = false;
									if (f != null && f.getTemperature() >= 500) {
										if (f.getLuminosity() <= 16 && !aTweakedFluids.containsValue(f)) {
											f.setLuminosity(16);
										}
										isHot = true;
									}
									if (a.getLightValue() <= 16f) {
										if (isHot) {
											a.setLightLevel(16f);
										} else {
											if (a.getLightValue() <= 16f) {
												a.setLightLevel(16f);
											}
										}
									} else {
										continue;
									}
								}
							}
						}
					} catch (IllegalArgumentException | IllegalAccessException e) {
					}
				}
		}
	}

}
