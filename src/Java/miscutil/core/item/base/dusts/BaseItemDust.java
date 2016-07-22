package miscutil.core.item.base.dusts;

import java.util.List;

import miscutil.core.lib.CORE;
import miscutil.core.util.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import cpw.mods.fml.common.registry.GameRegistry;

public class BaseItemDust extends Item{

	protected int colour;
	protected String materialName;
	protected String pileType;

	public BaseItemDust(String unlocalizedName, String materialName, int colour, String pileSize) {
		setUnlocalizedName(unlocalizedName);
		this.setUnlocalizedName(unlocalizedName);
		this.setMaxStackSize(64);	
		if (pileSize == "dust" || pileSize == "Dust"){
			this.setTextureName(CORE.MODID + ":" + "dust");}
		else{
			this.setTextureName(CORE.MODID + ":" + "dust"+pileSize);}
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
		GameRegistry.registerItem(this, unlocalizedName);
	}

	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		if (pileType != null && materialName != null && pileType != "" && materialName != "" && !pileType.equals("") && !materialName.equals("")){
			if (this.pileType == "dust"){
				list.add(EnumChatFormatting.GRAY+"A pile of " + materialName + " dust.");		
			}
			else{
				list.add(EnumChatFormatting.GRAY+"A "+this.pileType.toLowerCase()+" pile of " + materialName + " dust.");		
			}
		}
		super.addInformation(stack, aPlayer, list, bool);
	}

	public final String getMaterialName() {
		return materialName;
	}

	@Override
	public int getColorFromItemStack(ItemStack stack, int HEX_OxFFFFFF) {
		if (colour == 0){
			return Utils.generateSingularRandomHexValue();
		}
		return colour;

	}

}
