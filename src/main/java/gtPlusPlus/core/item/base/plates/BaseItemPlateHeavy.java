package gtPlusPlus.core.item.base.plates;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class BaseItemPlateHeavy extends BaseItemComponent{

	final static ComponentTypes HEAVY = ComponentTypes.PLATEHEAVY;

	public BaseItemPlateHeavy(final Material material) {
		super(material, HEAVY);
	}

	@Override
	public String getCorrectTextures(){
		return CORE.MODID + ":" + "itemHeavyPlate";
	}	

	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses(){
		return true;
	}

	@Override
	public void registerIcons(final IIconRegister i) {
		this.base = i.registerIcon(CORE.MODID + ":" + "itemHeavyPlate");
		this.overlay = i.registerIcon(CORE.MODID + ":" + "itemHeavyPlate_Overlay");
	}
	
	@Override
	public IIcon getIconFromDamageForRenderPass(final int damage, final int pass) {		
		
		if (pass == 0) {
			return this.base;			
		}
		else {
			return this.overlay;			
		}
		
	}
}
