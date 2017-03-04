package gtPlusPlus.xmod.gregtech.api.items.types;

import java.util.List;

import gregtech.api.enums.SubTag;
import gtPlusPlus.xmod.gregtech.api.interfaces.internal.Interface_ItemBehaviour;
import gtPlusPlus.xmod.gregtech.api.items.Gregtech_MetaItem_Base;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ToolType_Base implements Interface_ItemBehaviour<Gregtech_MetaItem_Base> {
	@Override
	public boolean onLeftClickEntity(final Gregtech_MetaItem_Base aItem, final ItemStack aStack, final EntityPlayer aPlayer, final Entity aEntity) {
		return false;
	}

	@Override
	public boolean onItemUse(final Gregtech_MetaItem_Base aItem, final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX, final int aY, final int aZ, final int aSide, final float hitX, final float hitY, final float hitZ) {
		return false;
	}

	@Override
	public boolean onItemUseFirst(final Gregtech_MetaItem_Base aItem, final ItemStack aStack, final EntityPlayer aPlayer, final World aWorld, final int aX, final int aY, final int aZ, final int aSide, final float hitX, final float hitY, final float hitZ) {
		return false;
	}

	@Override
	public ItemStack onItemRightClick(final Gregtech_MetaItem_Base aItem, final ItemStack aStack, final World aWorld, final EntityPlayer aPlayer) {
		return aStack;
	}

	@Override
	public List<String> getAdditionalToolTips(final Gregtech_MetaItem_Base aItem, final List<String> aList, final ItemStack aStack) {
		return aList;
	}

	@Override
	public void onUpdate(final Gregtech_MetaItem_Base aItem, final ItemStack aStack, final World aWorld, final Entity aPlayer, final int aTimer, final boolean aIsInHand) {
	}

	@Override
	public boolean isItemStackUsable(final Gregtech_MetaItem_Base aItem, final ItemStack aStack) {
		return true;
	}

	@Override
	public boolean canDispense(final Gregtech_MetaItem_Base aItem, final IBlockSource aSource, final ItemStack aStack) {
		return false;
	}

	@Override
	public ItemStack onDispense(final Gregtech_MetaItem_Base aItem, final IBlockSource aSource, final ItemStack aStack) {
		final EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
		final IPosition iposition = BlockDispenser.func_149939_a(aSource);
		final ItemStack itemstack1 = aStack.splitStack(1);
		BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
		return aStack;
	}

	@Override
	public boolean hasProjectile(final Gregtech_MetaItem_Base aItem, final SubTag aProjectileType, final ItemStack aStack) {
		return false;
	}

	@Override
	public EntityArrow getProjectile(final Gregtech_MetaItem_Base aItem, final SubTag aProjectileType, final ItemStack aStack, final World aWorld, final double aX, final double aY, final double aZ) {
		return null;
	}

	@Override
	public EntityArrow getProjectile(final Gregtech_MetaItem_Base aItem, final SubTag aProjectileType, final ItemStack aStack, final World aWorld, final EntityLivingBase aEntity, final float aSpeed) {
		return null;
	}
}
