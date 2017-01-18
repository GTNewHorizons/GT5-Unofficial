package gtPlusPlus.core.item.base.cell;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseItemCell extends BaseItemComponent{

	private IIcon base;
	private IIcon overlay;
	private final Material cellMaterial;
	ComponentTypes Cell = ComponentTypes.CELL;

	public BaseItemCell(Material material) {
		super(material, BaseItemComponent.ComponentTypes.CELL);	
		this.cellMaterial = material;
		fluidColour = (short[]) ((cellMaterial == null) ? extraData : cellMaterial.getRGBA());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override 
	public void registerIcons(IIconRegister i) {
		this.base = i.registerIcon(CORE.MODID + ":" + "item"+Cell.getComponent());	
		this.overlay = i.registerIcon(CORE.MODID + ":" + "item"+Cell.getComponent()+"_Overlay");
		//this.overlay = cellMaterial.getFluid(1000).getFluid().get
	}

	private int fluidBright = 0;
	private int tickValue;
	private short[] fluidColour;
	boolean upwards = true;

	@Override
	public int getColorFromItemStack(ItemStack stack, int renderPass) {
		if (renderPass == 0){
			return Utils.rgbtoHexValue(230, 230, 230);			
		}	
		

		/*if (tickValue == 20){
			if (upwards){
				fluidBright++;
			}
			else {
				fluidBright--;
			}
			if (fluidBright <= 0 || fluidBright >= 20){
				if (fluidBright <= 0) upwards = true;
				if (fluidBright >= 20) upwards = false;
			}
		}
		
		if (tickValue > 20){
			tickValue=0;
		}
		else {
			tickValue++;
		}

		return Utils.rgbtoHexValue(fluidColour[0]+fluidBright, fluidColour[1]+fluidBright, fluidColour[2]+fluidBright);*/
		return componentColour;
	}


	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		if(pass == 0) {
			return this.base;
		}
		return this.overlay;
	}

}
