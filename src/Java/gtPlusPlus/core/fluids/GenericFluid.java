package gtPlusPlus.core.fluids;

import gtPlusPlus.core.item.base.itemblock.ItemBlockFluid;
import gtPlusPlus.core.material.Material;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class GenericFluid extends Fluid{

	protected final String fluidName;
	protected final Fluid fluidFactory;
	protected final Block blockFactory;
	protected final short[] rgba;
	
	public GenericFluid(String displayName, String fluidName, int luminosity, int density, int temperature, int viscosity, boolean isGas, short[] rgba) {
		super(fluidName);
		fluidFactory = this;
		this.rgba = rgba;
		this.fluidName = fluidName;
		fluidFactory.setLuminosity(luminosity);
		fluidFactory.setDensity(density);
		fluidFactory.setTemperature(temperature);
		fluidFactory.setViscosity(viscosity);
		fluidFactory.setGaseous(isGas);
		fluidFactory.setUnlocalizedName("fluid"+fluidName);
		FluidRegistry.registerFluid(fluidFactory);			
		blockFactory = new BlockFluidBase(displayName, fluidFactory, rgba).setBlockName("fluidblock"+fluidName);
		GameRegistry.registerBlock(blockFactory, ItemBlockFluid.class, blockFactory.getUnlocalizedName().substring(5));
		fluidFactory.setBlock(blockFactory);
		
		//fluidFactory.setUnlocalizedName(blockFactory.getUnlocalizedName());
		
	}
	
	public GenericFluid(Material fluidMaterial, int luminosity, int density, int temperature, int viscosity, boolean isGas) {
		
		super(fluidMaterial.getUnlocalizedName());
		//IC2_ItemFluidCell fullFluidCell = emptyCell.fill(emptyCell, FluidUtils.getFluidStack(getUnlocalizedName(), 1), true);
		
		fluidFactory = this;
		this.rgba = fluidMaterial.getRGBA();
		this.fluidName = fluidMaterial.getUnlocalizedName();
		fluidFactory.setLuminosity(luminosity);
		fluidFactory.setDensity(density);
		fluidFactory.setTemperature(temperature);
		fluidFactory.setViscosity(viscosity);
		fluidFactory.setGaseous(isGas);
		fluidFactory.setUnlocalizedName("fluid"+fluidName);
		FluidRegistry.registerFluid(fluidFactory);			
		blockFactory = new BlockFluidBase(fluidFactory, fluidMaterial).setBlockName("fluidblock"+fluidName);
		GameRegistry.registerBlock(blockFactory, ItemBlockFluid.class, blockFactory.getUnlocalizedName().substring(5));
		fluidFactory.setBlock(blockFactory);
		//IC2_ItemFluidCell emptyCell = new IC2_ItemFluidCell(fluidName);
		/*if (aFullContainer != null && aEmptyContainer != null && !FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer)) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, container(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}*/
		//fluidFactory.setUnlocalizedName(blockFactory.getUnlocalizedName());
		
	}
	
	@Override
	public int getColor() {
        return Math.max(0, Math.min(255, this.rgba[0])) << 16 | Math.max(0, Math.min(255, this.rgba[1])) << 8 | Math.max(0, Math.min(255, this.rgba[2]));
    }	

}
