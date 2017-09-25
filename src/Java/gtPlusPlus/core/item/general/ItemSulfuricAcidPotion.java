package gtPlusPlus.core.item.general;

import gregtech.api.enums.GT_Values;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.projectile.EntitySulfuricAcidPotion;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.util.fluid.FluidUtils;
import gtPlusPlus.core.util.item.ItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemSulfuricAcidPotion extends CoreItem {

	public ItemSulfuricAcidPotion(String unlocalizedName, String displayName, String description) {
		super(unlocalizedName, displayName, AddToCreativeTab.tabMisc, 16, 0, description, EnumRarity.common, EnumChatFormatting.GRAY, false, null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(Items.glass_bottle), ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion), FluidUtils.getFluidStack("sulfuricacid", 250), null);
		GT_Values.RA.addFluidCannerRecipe(ItemUtils.getSimpleStack(ModItems.itemSulfuricPotion), ItemUtils.getSimpleStack(Items.glass_bottle), null, FluidUtils.getFluidStack("sulfuricacid", 250));
	}
	
	public ItemStack onItemRightClick(ItemStack item, World world, EntityPlayer player) {
		if (!player.capabilities.isCreativeMode) {
			--item.stackSize;
		}
		world.playSoundAtEntity(player, "random.bow", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		if (!world.isRemote) {
			world.spawnEntityInWorld(new EntitySulfuricAcidPotion(world, player));
		}
		return item;
	}
}