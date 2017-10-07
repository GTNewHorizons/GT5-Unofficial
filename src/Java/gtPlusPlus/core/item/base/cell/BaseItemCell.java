package gtPlusPlus.core.item.base.cell;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import ic2.core.Ic2Items;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;

public class BaseItemCell extends BaseItemComponent{

	private IIcon base;
	private IIcon overlay;
	ComponentTypes Cell = ComponentTypes.CELL;

	public BaseItemCell(final Material material) {
		super(material, BaseItemComponent.ComponentTypes.CELL);
		this.fluidColour = (short[]) ((material == null) ? this.extraData : material.getRGBA());
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
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public void registerIcons(final IIconRegister i) {

		if (CORE.configSwitches.useGregtechTextures){
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
		if (renderPass == 0 && !CORE.configSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(230, 230, 230);
		}
		if (renderPass == 1 && CORE.configSwitches.useGregtechTextures){
			return Utils.rgbtoHexValue(230, 230, 230);
		}
		return this.componentColour;
	}


	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {
		if(pass == 0) {
			return this.base;
		}
		return this.overlay;
	}

}
