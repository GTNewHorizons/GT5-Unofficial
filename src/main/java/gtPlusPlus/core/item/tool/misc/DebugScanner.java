package gtPlusPlus.core.item.tool.misc;

import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.CoreItem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class DebugScanner extends CoreItem {

	public DebugScanner() {
		super("gtpp.debug.scanner", AddToCreativeTab.tabTools, 1, 0, 
				new String[] {
					"Used to obtain information from GT/GT++ content",
					"Right Click to use",
					},
				EnumRarity.epic);
		setTextureName(CORE.MODID + ":itemStickyRubber");
	}

	@Override
	public boolean isDamageable() {
		return false;
	}

	@Override
	public boolean onItemUse(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int p_77648_4_,
			int p_77648_5_, int p_77648_6_, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_) {
		// TODO Auto-generated method stub
		return super.onItemUse(aStack, aPlayer, aWorld, p_77648_4_, p_77648_5_, p_77648_6_, p_77648_7_, p_77648_8_,
				p_77648_9_, p_77648_10_);
	}

	@Override
	public boolean isRepairable() {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_) {
		// TODO Auto-generated method stub
		return super.onItemRightClick(p_77659_1_, p_77659_2_, p_77659_3_);
	}

	@Override
	public float getDigSpeed(ItemStack itemstack, Block block, int metadata) {
		return 0f;
	}

	@Override
	public boolean onLeftClickEntity(ItemStack stack, EntityPlayer player, Entity entity) {
		if (entity != null && player != null) {
			PlayerUtils.messagePlayer(player, "Entity ID: "+entity.getEntityId());
			PlayerUtils.messagePlayer(player, "UUID: "+entity.getUniqueID());
			PlayerUtils.messagePlayer(player, "Invulnerable? "+entity.isEntityInvulnerable());
			PlayerUtils.messagePlayer(player, "Invisible? "+entity.isInvisible());
			PlayerUtils.messagePlayer(player, "Age: "+entity.ticksExisted);
			
			if (entity instanceof EntityLivingBase) {
				EntityLivingBase g = (EntityLivingBase) entity;
				PlayerUtils.messagePlayer(player, "Health: "+g.getHealth()+"/"+g.getMaxHealth());
				PlayerUtils.messagePlayer(player, "On ground? "+g.onGround);
				PlayerUtils.messagePlayer(player, "Child? "+g.isChild());				
			}
			if (entity instanceof EntityLiving) {
				EntityLiving g = (EntityLiving) entity;
				PlayerUtils.messagePlayer(player, "Can Loot? "+g.canPickUpLoot());				
				
			}
			if (entity instanceof EntityPlayer) {
				EntityPlayer y = (EntityPlayer) entity;
				PlayerUtils.messagePlayer(player, "Experience: "+y.experience);
				PlayerUtils.messagePlayer(player, "Name: "+y.getCommandSenderName());				
			}
			
		}
		return true;
	}

	@Override
	public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player) {
		return false;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}

	@Override
	public int getHarvestLevel(ItemStack stack, String toolClass) {
		return 0;
	}

	
	
}
