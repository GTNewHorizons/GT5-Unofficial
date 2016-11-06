package gtPlusPlus.core.item.base.dusts;

import gtPlusPlus.core.lib.CORE;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class BaseItemDustAbstract extends Item{
	
	protected int colour = 0;
	protected String materialName;
	protected String pileType;
	
	public BaseItemDustAbstract(String unlocalizedName, String materialName, int colour, String pileSize) {
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);	
		if (pileSize == "dust" || pileSize == "Dust"){
			this.setTextureName(CORE.MODID + ":" + "dust");			
		}
		else{
			this.setTextureName(CORE.MODID + ":" + "dust"+pileSize);			
		}
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;		
		setUnlocalizedName(unlocalizedName);
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public abstract void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool);

	public abstract String getMaterialName();

	@Override
	public abstract int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF);
	
}
