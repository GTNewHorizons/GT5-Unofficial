package miscutil.core.xmod.gregtech.api.items.types;

import gregtech.api.enums.SubTag;

import java.util.List;

import miscutil.core.xmod.gregtech.api.interfaces.internal.Interface_ItemBehaviour;
import miscutil.core.xmod.gregtech.api.items.Gregtech_MetaItem_Base;
import net.minecraft.block.BlockDispenser;
import net.minecraft.dispenser.BehaviorDefaultDispenseItem;
import net.minecraft.dispenser.IBlockSource;
import net.minecraft.dispenser.IPosition;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ToolType_Base implements Interface_ItemBehaviour<Gregtech_MetaItem_Base> {
    @Override
	public boolean onLeftClickEntity(Gregtech_MetaItem_Base aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        return false;
    }

    @Override
	public boolean onItemUse(Gregtech_MetaItem_Base aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
	public boolean onItemUseFirst(Gregtech_MetaItem_Base aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
	public ItemStack onItemRightClick(Gregtech_MetaItem_Base aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        return aStack;
    }

    @Override
	public List<String> getAdditionalToolTips(Gregtech_MetaItem_Base aItem, List<String> aList, ItemStack aStack) {
        return aList;
    }

    @Override
	public void onUpdate(Gregtech_MetaItem_Base aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand) {
    }

    @Override
	public boolean isItemStackUsable(Gregtech_MetaItem_Base aItem, ItemStack aStack) {
        return true;
    }

    @Override
	public boolean canDispense(Gregtech_MetaItem_Base aItem, IBlockSource aSource, ItemStack aStack) {
        return false;
    }

    @Override
	public ItemStack onDispense(Gregtech_MetaItem_Base aItem, IBlockSource aSource, ItemStack aStack) {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
        IPosition iposition = BlockDispenser.func_149939_a(aSource);
        ItemStack itemstack1 = aStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
        return aStack;
    }

    @Override
	public boolean hasProjectile(Gregtech_MetaItem_Base aItem, SubTag aProjectileType, ItemStack aStack) {
        return false;
    }

    @Override
	public EntityArrow getProjectile(Gregtech_MetaItem_Base aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, double aX, double aY, double aZ) {
        return null;
    }

    @Override
	public EntityArrow getProjectile(Gregtech_MetaItem_Base aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, EntityLivingBase aEntity, float aSpeed) {
        return null;
    }
}
