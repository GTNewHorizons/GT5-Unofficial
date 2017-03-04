package gtPlusPlus.core.fluids;

import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.core.item.base.itemblock.ItemBlockFluid;
import gtPlusPlus.core.material.Material;
import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class GenericFluid extends Fluid{

	protected final String fluidName;
	protected final Fluid fluidFactory;
	protected final Block blockFactory;
	protected final short[] rgba;

	public GenericFluid(final String displayName, final String fluidName, final int luminosity, final int density, final int temperature, final int viscosity, final boolean isGas, final short[] rgba) {
		super(fluidName);
		this.fluidFactory = this;
		this.rgba = rgba;
		this.fluidName = fluidName;
		this.fluidFactory.setLuminosity(luminosity);
		this.fluidFactory.setDensity(density);
		this.fluidFactory.setTemperature(temperature);
		this.fluidFactory.setViscosity(viscosity);
		this.fluidFactory.setGaseous(isGas);
		this.fluidFactory.setUnlocalizedName("fluid"+fluidName);
		FluidRegistry.registerFluid(this.fluidFactory);
		this.blockFactory = new BlockFluidBase(displayName, this.fluidFactory, rgba).setBlockName("fluidblock"+fluidName);
		GameRegistry.registerBlock(this.blockFactory, ItemBlockFluid.class, this.blockFactory.getUnlocalizedName().substring(5));
		this.fluidFactory.setBlock(this.blockFactory);

		//fluidFactory.setUnlocalizedName(blockFactory.getUnlocalizedName());

	}

	public GenericFluid(final Material fluidMaterial, final int luminosity, final int density, final int temperature, final int viscosity, final boolean isGas) {

		super(fluidMaterial.getUnlocalizedName());
		//IC2_ItemFluidCell fullFluidCell = emptyCell.fill(emptyCell, FluidUtils.getFluidStack(getUnlocalizedName(), 1), true);

		this.fluidFactory = this;
		this.rgba = fluidMaterial.getRGBA();
		this.fluidName = fluidMaterial.getUnlocalizedName();
		this.fluidFactory.setLuminosity(luminosity);
		this.fluidFactory.setDensity(density);
		this.fluidFactory.setTemperature(temperature);
		this.fluidFactory.setViscosity(viscosity);
		this.fluidFactory.setGaseous(isGas);
		this.fluidFactory.setUnlocalizedName("fluid"+this.fluidName);
		FluidRegistry.registerFluid(this.fluidFactory);
		this.blockFactory = new BlockFluidBase(this.fluidFactory, fluidMaterial).setBlockName("fluidblock"+this.fluidName);
		GameRegistry.registerBlock(this.blockFactory, ItemBlockFluid.class, this.blockFactory.getUnlocalizedName().substring(5));
		this.fluidFactory.setBlock(this.blockFactory);
		//IC2_ItemFluidCell emptyCell = new IC2_ItemFluidCell(fluidName);
		/*if (aFullContainer != null && aEmptyContainer != null && !FluidContainerRegistry.registerFluidContainer(new FluidStack(rFluid, aFluidAmount), aFullContainer, aEmptyContainer)) {
			GT_Values.RA.addFluidCannerRecipe(aFullContainer, container(aFullContainer, false), null, new FluidStack(rFluid, aFluidAmount));
		}*/
		//fluidFactory.setUnlocalizedName(blockFactory.getUnlocalizedName());

	}

	@Override
	public int getColor() {
		return (Math.max(0, Math.min(255, this.rgba[0])) << 16) | (Math.max(0, Math.min(255, this.rgba[1])) << 8) | Math.max(0, Math.min(255, this.rgba[2]));
	}

}
