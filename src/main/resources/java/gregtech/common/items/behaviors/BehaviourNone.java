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
import gregtech.api.items.MetaBaseItem;

public class BehaviourNone implements IItemBehaviour<MetaBaseItem> {

    @Override
    public boolean onLeftClickEntity(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity) {
        return false;
    }

    @Override
    public boolean onItemUse(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY,
        int aZ, int ordinalSide, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public ItemStack onItemRightClick(MetaBaseItem aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        return aStack;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        return aList;
    }

    @Override
    public void onUpdate(MetaBaseItem aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer,
        boolean aIsInHand) {}

    @Override
    public boolean isItemStackUsable(MetaBaseItem aItem, ItemStack aStack) {
        return true;
    }

    @Override
    public boolean canDispense(MetaBaseItem aItem, IBlockSource aSource, ItemStack aStack) {
        return false;
    }

    @Override
    public ItemStack onDispense(MetaBaseItem aItem, IBlockSource aSource, ItemStack aStack) {
        EnumFacing enumfacing = BlockDispenser.func_149937_b(aSource.getBlockMetadata());
        IPosition iposition = BlockDispenser.func_149939_a(aSource);
        ItemStack itemstack1 = aStack.splitStack(1);
        BehaviorDefaultDispenseItem.doDispense(aSource.getWorld(), itemstack1, 6, enumfacing, iposition);
        return aStack;
    }

    @Override
    public boolean hasProjectile(MetaBaseItem aItem, SubTag aProjectileType, ItemStack aStack) {
        return false;
    }

    @Override
    public EntityArrow getProjectile(MetaBaseItem aItem, SubTag aProjectileType, ItemStack aStack, World aWorld,
        double aX, double aY, double aZ) {
        return null;
    }

    @Override
    public EntityArrow getProjectile(MetaBaseItem aItem, SubTag aProjectileType, ItemStack aStack, World aWorld,
        EntityLivingBase aEntity, float aSpeed) {
        return null;
    }
}
