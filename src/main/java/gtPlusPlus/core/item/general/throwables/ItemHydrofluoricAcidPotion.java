package gtPlusPlus.core.item.general.throwables;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.projectile.EntityHydrofluoricAcidPotion;
import gtPlusPlus.core.item.base.CoreItem;

public class ItemHydrofluoricAcidPotion extends CoreItem {

	public ItemHydrofluoricAcidPotion(String unlocalizedName, String displayName, String description) {
		super(unlocalizedName, displayName, AddToCreativeTab.tabMisc, 16, 0, new String[] {description}, EnumRarity.uncommon, EnumChatFormatting.GRAY, false, null);
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			--item.stackSize;
		}
		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote) {
			world.spawnEntityInWorld(new EntityHydrofluoricAcidPotion(world, player));
		}
		return item;
	}
}