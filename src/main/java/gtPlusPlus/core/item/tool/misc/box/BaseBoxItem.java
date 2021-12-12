package gtPlusPlus.core.item.tool.misc.box;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class BaseBoxItem extends CoreItem {

	private final int GUI;

	public BaseBoxItem(String displayName, String[] description, int GUI_ID) {
		super("item." + Utils.sanitizeString(displayName), displayName, AddToCreativeTab.tabTools, 1, 0,
				modifyDescriptionStringArray(description), EnumRarity.uncommon, EnumChatFormatting.GRAY, false, null);
		GUI = GUI_ID;
	}

	private static String[] modifyDescriptionStringArray(String[] array) {
		String[] a = new String[array.length + 1];
		for (int b = 0; b < array.length; b++) {
			a[b] = array[b];
		}
		a[a.length - 1] = "Right Click to open";
		return a;
	}

	// Without this method, your inventory will NOT work!!!
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 1; // return any value greater than zero
	}

	@Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player) {
		if (!world.isRemote) {
			// If player not sneaking, open the inventory gui
			if (!player.isSneaking()) {
				player.openGui(GTplusplus.instance, GUI, world, (int) player.posX, (int) player.posY,
						(int) player.posZ);
			}
		}
		return itemstack;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		this.itemIcon = iconRegister.registerIcon(CORE.MODID + ":" + this.getUnlocalizedName().substring(5));
	}
}
