package gtPlusPlus.core.fluids;

import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class GenericFluid extends Fluid{

	protected String fluidName;
	public Fluid fluidFactory;
	public Block blockFactory;
	public short[] rgba;
	
	public GenericFluid(String fluidName, int luminosity, int density, int temperature, int viscosity, boolean isGas, short[] rgba) {
		super(fluidName);
		fluidFactory = this;
		this.rgba = rgba;
		fluidFactory.setLuminosity(luminosity);
		fluidFactory.setDensity(density);
		fluidFactory.setTemperature(temperature);
		fluidFactory.setViscosity(viscosity);
		fluidFactory.setGaseous(isGas);
		fluidFactory.setUnlocalizedName("fluid"+fluidName);
		FluidRegistry.registerFluid(fluidFactory);			
		blockFactory = new BlockFluidBase(fluidFactory, Material.water, Utils.rgbtoHexValue(rgba[0], rgba[1], rgba[2])).setBlockName("fluidblock"+fluidName);
		GameRegistry.registerBlock(blockFactory, CORE.MODID + "_" + blockFactory.getUnlocalizedName().substring(5));
		//fluidFactory.setUnlocalizedName(blockFactory.getUnlocalizedName());
		
	}
	
	@Override
	public int getColor() {
        return Math.max(0, Math.min(255, this.rgba[0])) << 16 | Math.max(0, Math.min(255, this.rgba[1])) << 8 | Math.max(0, Math.min(255, this.rgba[2]));
    }

}
