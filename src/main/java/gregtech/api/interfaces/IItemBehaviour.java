package gregtech.api.interfaces;

import java.util.List;
import java.util.Optional;

import net.minecraft.dispenser.IBlockSource;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.BlockSnapshot;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.SubTag;
import gregtech.api.items.MetaBaseItem;

public interface IItemBehaviour<E extends Item> {

    default boolean onLeftClick(E aItem, ItemStack aStack, EntityPlayer aPlayer) {
        return false;
    }

    default boolean onMiddleClick(E aItem, ItemStack aStack, EntityPlayer aPlayer) {
        return false;
    }

    /**
     * Allows a GT Tool wielded in the offhand to perform an action on a placed block in the same tick.
     * 
     * @param blockSnapshot Data about what block was placed and where
     * @param itemStack     The tool being used
     * @param player        The player initiating the action
     * @return true to cancel all further tool actions, false to keep going
     */
    default boolean onBlockPlacedWhileWieldingOffhanded(BlockSnapshot blockSnapshot, ItemStack itemStack,
        EntityPlayer player) {
        return false;
    }

    /**
     * Suppresses standard block activation for a {@link gregtech.common.blocks.BlockMachines GT machine block}. Put
     * your item's right click activation in
     * {@link #onItemUse(Item, ItemStack, EntityPlayer, World, int, int, int, int, float, float, float) onItemUse} for
     * best results.
     * <p>
     * Typically used when the item needs support for the Ring of Loki (from Botania.) If you don't care about that,
     * using
     * {@link #onItemUseFirst(Item, ItemStack, EntityPlayer, World, int, int, int, ForgeDirection, float, float, float)
     * onItemUseFirst} instead of
     * {@link #onItemUse(Item, ItemStack, EntityPlayer, World, int, int, int, int, float, float, float) onItemUse} will
     * act before block activation with a little less overhead.
     *
     * @param player     the player making the request
     * @param tileEntity the tile entity that is attempting to be activated
     * @param side       the side of the tile entity that the player clicked on
     * @return true if standard block activation should be suppressed
     */
    default boolean shouldInterruptBlockActivation(EntityPlayer player, TileEntity tileEntity, ForgeDirection side) {
        return false;
    }

    boolean onLeftClickEntity(E aItem, ItemStack aStack, EntityPlayer aPlayer, Entity aEntity);

    boolean onItemUse(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float hitX, float hitY, float hitZ);

    boolean onItemUseFirst(E aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        ForgeDirection side, float hitX, float hitY, float hitZ);

    ItemStack onItemRightClick(E aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer);

    List<String> getAdditionalToolTips(E aItem, List<String> aList, ItemStack aStack);

    default Optional<List<String>> getAdditionalToolTipsWhileSneaking(E aItem, List<String> aList, ItemStack aStack) {
        return Optional.empty();
    }

    void onUpdate(E aItem, ItemStack aStack, World aWorld, Entity aPlayer, int aTimer, boolean aIsInHand);

    boolean isItemStackUsable(E aItem, ItemStack aStack);

    boolean canDispense(E aItem, IBlockSource aSource, ItemStack aStack);

    ItemStack onDispense(E aItem, IBlockSource aSource, ItemStack aStack);

    boolean hasProjectile(MetaBaseItem aItem, SubTag aProjectileType, ItemStack aStack);

    EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, double aX, double aY,
        double aZ);

    EntityArrow getProjectile(E aItem, SubTag aProjectileType, ItemStack aStack, World aWorld, EntityLivingBase aEntity,
        float aSpeed);
}
