package gtPlusPlus.core.item.base.cell;

import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BaseItemCell extends BaseItemComponent{

	private IIcon base;
	private IIcon overlay;
	ComponentTypes Cell = ComponentTypes.CELL;
	
	public BaseItemCell(Material material) {
		super(material, BaseItemComponent.ComponentTypes.CELL);		
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
	}
	

	@Override
	public IIcon getIconFromDamageForRenderPass(int damage, int pass) {
		if(pass == 0) {
			return this.base;
		}
		return this.overlay;
	}

}
