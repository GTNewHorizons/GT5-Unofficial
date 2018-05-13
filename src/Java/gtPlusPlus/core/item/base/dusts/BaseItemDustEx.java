package gtPlusPlus.core.item.base.dusts;

import java.util.*;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_OreDictUnificator;

import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemDustEx extends BaseItemComponent{

	public static Map<String, String> mCachedPileLinkages = new HashMap<String, String>();
	
	protected IIcon[] baseAr = new IIcon[3];
	protected IIcon[] overlayAr = new IIcon[3];
	public final static ComponentTypes[] componentTypeAr = new ComponentTypes[] {ComponentTypes.DUST, ComponentTypes.DUSTSMALL, ComponentTypes.DUSTTINY};

	public BaseItemDustEx(final Material material) {
		super(material, componentTypeAr[0]);		
		//OreDict this beauty
		GT_OreDictUnificator.registerOre(componentTypeAr[0].getOreDictName()+material.getUnlocalizedName(), ItemUtils.simpleMetaStack(this, 0, 1));
		GT_OreDictUnificator.registerOre(componentTypeAr[1].getOreDictName()+material.getUnlocalizedName(), ItemUtils.simpleMetaStack(this, 0, 1));
		GT_OreDictUnificator.registerOre(componentTypeAr[2].getOreDictName()+material.getUnlocalizedName(), ItemUtils.simpleMetaStack(this, 0, 1));
		bakeRegistry();
	}
	
	private void bakeRegistry() {
		String unlocalName = getUnlocalizedName();		
		if (unlocalName.contains("item.")) {
			unlocalName = StringUtils.remove(unlocalName, "item.");
		}
		if (unlocalName.contains("itemDust")) {
			unlocalName = StringUtils.remove(unlocalName, "itemDust");
		}		
		mCachedPileLinkages.put(this.getUnlocalizedName(), "itemDust"+unlocalName);
		mCachedPileLinkages.put(this.getUnlocalizedName(), "itemDustSmall"+unlocalName);
		mCachedPileLinkages.put(this.getUnlocalizedName(), "itemDustTiny"+unlocalName);
	}

	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list) {
		for (int i = 0; i < 3; i ++) {
			list.add(new ItemStack(item, 1, i));
		}
	}

	@Override
	public IIcon getIconFromDamageForRenderPass(int meta, int pass) {
		if (CORE.ConfigSwitches.useGregtechTextures) {
			if (meta == 0) {
				if (pass == 0) {
					return this.baseAr[0];
				}
				else {
					return this.overlayAr[0];
				}
			}
			else if (meta == 1) {
				if (pass == 0) {
					return this.baseAr[1];
				}
				else {
					return this.overlayAr[1];
				}
			}
			else {
				if (pass == 0) {
					return this.baseAr[2];
				}
				else {
					return this.overlayAr[2];
				}
			}
		}
		else {
			return this.baseAr[meta];
		}
	}

	@Override
	public void registerIcons(final IIconRegister i) {
		if (CORE.ConfigSwitches.useGregtechTextures){
			this.baseAr[0] = i.registerIcon(getCorrectTextures());
			this.overlayAr[0] = i.registerIcon(getCorrectTextures() + "_OVERLAY");
			this.baseAr[1] = i.registerIcon(getCorrectTextures()+"Small");
			this.overlayAr[1] = i.registerIcon(getCorrectTextures() + "Small_OVERLAY");
			this.baseAr[2] = i.registerIcon(getCorrectTextures()+"Tiny");
			this.overlayAr[2] = i.registerIcon(getCorrectTextures() + "Tiny_OVERLAY");
		}
		else {
			this.baseAr[0] = i.registerIcon(getCorrectTextures());
			this.baseAr[1] = i.registerIcon(getCorrectTextures()+"Small");
			this.baseAr[2] = i.registerIcon(getCorrectTextures()+"Tiny");
		}
	}

	@Override
	public String getCorrectTextures(){
		if (!CORE.ConfigSwitches.useGregtechTextures){
			return CORE.MODID + ":" + "item"+this.componentType.getComponent();
		}
		String metType = "9j4852jyo3rjmh3owlhw9oe"; 
		if (this.componentMaterial != null) {
			TextureSet u = this.componentMaterial.getTextureSet();
			if (u != null) {
				metType = u.mSetName;				
			}
		}		
		metType = (metType.equals("9j4852jyo3rjmh3owlhw9oe") ? "METALLIC" : metType);		
		return "gregtech" + ":" + "materialicons/"+metType+"/" + this.componentType.getOreDictName();
	}


}
