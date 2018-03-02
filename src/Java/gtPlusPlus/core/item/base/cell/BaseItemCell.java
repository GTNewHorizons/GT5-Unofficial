package gtPlusPlus.core.item.base.cell;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import ic2.core.Ic2Items;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class BaseItemCell extends BaseItemComponent{

	ComponentTypes Cell = ComponentTypes.CELL;

	public BaseItemCell(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.CELL);
		this.fluidColour = (short[]) material.getRGBA();
		//FluidContainerRegistry.registerFluidContainer(material.getFluid(1000), ItemUtils.getSimpleStack(this), Ic2Items.cell.copy());
	}
	
	public BaseItemCell(final String unlocalName, final String localName, final short[] RGBa) {
		super(unlocalName, localName, RGBa);
		this.fluidColour = RGBa;
		FluidContainerRegistry.registerFluidContainer(FluidUtils.getFluidStack(unlocalName.toLowerCase(), 0), ItemUtils.getSimpleStack(this), Ic2Items.cell.copy());
	}
	
	public BaseItemCell(final String unlocalName, final String localName, final short[] RGBa, final Fluid cellFluid) {
		super(unlocalName, localName, RGBa);
		this.fluidColour = RGBa;
		FluidContainerRegistry.registerFluidContainer(FluidUtils.getFluidStack(cellFluid, 1000), ItemUtils.getSimpleStack(this), Ic2Items.cell.copy());
	}

	@Override
	public void registerIcons(final IIconRegister i) {

		if (CORE.ConfigSwitches.useGregtechTextures){
			this.base = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "cell");
			this.overlay = i.registerIcon("gregtech" + ":" + "materialicons/METALLIC/" + "cell_OVERLAY");
		}
		else {
			this.base = i.registerIcon(CORE.MODID + ":" + "item"+this.Cell.getComponent());
			this.overlay = i.registerIcon(CORE.MODID + ":" + "item"+this.Cell.getComponent()+"_Overlay");
		}
		//this.overlay = cellMaterial.getFluid(1000).getFluid().get
	}

	private final short[] fluidColour;
	boolean upwards = true;

	@Override
	public int getColorFromItemStack(final ItemStack stack, final int renderPass) {
		if (renderPass == 0 && !CORE.ConfigSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(230, 230, 230);
		}
		if (renderPass == 1 && CORE.ConfigSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(230, 230, 230);
		}
		return this.componentColour;
	}

}
