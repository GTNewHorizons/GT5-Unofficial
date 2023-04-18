package gregtech.api.interfaces;

import java.util.List;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SubTag;
import gregtech.api.items.GT_MetaBase_Item;

public interface IItemBehaviour<E extends Item> {

    boolean onLeftClickEntity(E aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity);

    boolean onItemUse(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide,
        float hitX, float hitY, float hitZ);

    boolean onItemUseFirst(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        ForgeDirection side, float hitX, float hitY, float hitZ);

    ItemStack onItemRightClick(E aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer);

    List<String> getAdditionalToolTips(E aItem, List<String> aList, ItemStack aStack);

    void onUpdate(E aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand);

    boolean isItemStackUsable(E aItem, ItemStack aStack);

    boolean canDispense(E aItem, IBlockSource aSource, ItemStack aStack);

    ItemStack onDispense(E aItem, IBlockSource aSource, ItemStack aStack);

    boolean hasProjectile(GT_MetaBase_Item aItem, SubTag aProjectileType, ItemStack aStack);

    EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, double aX, double aY,
        double aZ);

    EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, EntityLivingBase aEntity,
        float aSpeed);
}
