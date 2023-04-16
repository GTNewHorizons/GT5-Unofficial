package gregtech.common.items.behaviors;

import java.util.List;

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
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SubTag;
import gregtech.api.interfaces.IItemBehaviour;
import gregtech.api.items.GT_MetaBase_Item;
import gregtech.api.util.GT_Utility;

public class Behaviour_None implements IItemBehaviour<GT_MetaBase_Item> {

    @Override
    public boolean onLeftClickEntity(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        return false;
    }

    @Override
    public boolean onItemUse(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(GT_MetaBase_Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        return aStack;
    }

    @Override
    public List<String> getAdditionalToolTips(GT_MetaBase_Item aItem, List<String> aList, ItemStack aStack) {
        return aList;
    }

    @Override
    public void onUpdate(GT_MetaBase_Item aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer,
        boolean aIsInHand) {}

    @Override
    public boolean isItemStackUsable(GT_MetaBase_Item aItem, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean canDispense(GT_MetaBase_Item aItem, IBlockSource aSource, ItemStack aStack) {
        return false;
    }

    @Override
    public ItemStack onDispense(GT_MetaBase_Item aItem, IBlockSource aSource, ItemStack aStack) {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
        IPosition iposition = BlockDispenser.func_149939_a(aSource);
        ItemStack itemstack1 = aStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
        return aStack;
    }

    @Override
    public boolean hasProjectile(GT_MetaBase_Item aItem, SubTag aProjectileType, ItemStack aStack) {
        return false;
    }

    @Override
    public EntityArrow getProjectile(GT_MetaBase_Item aItem, SubTag aProjectileType, ItemStack aStack, World aWorld,
        double aX, double aY, double aZ) {
        return null;
    }

    @Override
    public EntityArrow getProjectile(GT_MetaBase_Item aItem, SubTag aProjectileType, ItemStack aStack, World aWorld,
        EntityLivingBase aEntity, float aSpeed) {
        return null;
    }

    @Deprecated
    public String trans(String aKey, String aEnglish) {
        return GT_Utility.trans(aKey, aEnglish);
    }
}
