package miscutil.core.item.base.dusts;

import java.util.List;

import miscutil.core.lib.CORE;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

public class BaseItemDustTiny extends BaseItemDust{
	
	protected int colour = 0;
	protected String materialName;
	
	public BaseItemDustTiny(String unlocalizedName, String materialName, int colour, String size) {
		super(unlocalizedName, materialName, colour, size);
		this.setTextureName(CORE.MODID + ":" + "dustTiny");
		this.setMaxStackSize(64);
		this.colour = colour;
		this.materialName = materialName;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
		list.add(EnumChatFormatting.GRAY+"A tiny pile of " + materialName + " dust.");
		super.addInformation(stack, aPlayer, list, bool);
	}
	
}
