package miscutil.core.intermod.forestry.apiculture.items.magicbees;

import miscutil.core.creative.AddToCreativeTab;
import miscutil.core.lib.CORE;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import forestry.api.apiculture.IBee;
import forestry.api.apiculture.IBeeHousing;
import forestry.api.apiculture.IBeeModifier;
import forestry.api.apiculture.IHiveFrame;

public class MB_ItemMagicHiveFrame extends Item implements IHiveFrame
{
	private MB_HiveFrameType type;

	public MB_ItemMagicHiveFrame(MB_HiveFrameType frameType)
	{
		super();
		this.type = frameType;
		this.setMaxDamage(this.type.maxDamage);
		this.setMaxStackSize(1);
		this.setCreativeTab(AddToCreativeTab.tabMisc);
		this.setUnlocalizedName("frame" + frameType.getName());
		GameRegistry.registerItem(this, "frame" + frameType.getName());
	}
	
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister par1IconRegister)
	{
		this.itemIcon = par1IconRegister.registerIcon(CORE.MODID + ":frame" + type.getName());
	}
	
	// --------- IHiveFrame functions -----------------------------------------
	
	@Override
	public ItemStack frameUsed(IBeeHousing housing, ItemStack frame, IBee queen, int wear) {
		frame.setItemDamage(frame.getItemDamage() + wear);

		if (frame.getItemDamage() >= frame.getMaxDamage()) {
			// Break the frame.
			frame = null;
		}

		return frame;
	}

	@Override
	public IBeeModifier getBeeModifier() {
		return type;
	}
	
	@Override
	public boolean isBookEnchantable(ItemStack itemstack1, ItemStack itemstack2)
	{
		return false;
	}

}
