package gregtech.api.multitileentity.interfaces;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IDebugableTileEntity;
import gregtech.api.interfaces.tileentity.ITurnable;
import gregtech.api.multitileentity.MultiTileEntityBlock;
import gregtech.api.multitileentity.MultiTileEntityRegistry;

/*
 * Heavily inspired by GT6
 */
public interface IMultiTileEntity extends ICoverable, ITurnable, IDebugableTileEntity {

    /**
     * Those two IDs HAVE to be saved inside the NBT of the TileEntity itself. They get set by the Registry itself, when
     * the TileEntity is placed.
     */
    short getMultiTileEntityID();

    short getMultiTileEntityRegistryID();

    /**
     * Called by the Registry with the default NBT Parameters and the two IDs you have to save, when the TileEntity is
     * created. aNBT may be null, take that into account if you decide to call the regular readFromNBT Function from
     * here.
     */
    void initFromNBT(NBTTagCompound aNBT, short aMTEID, short aMTERegistry);

    /** Writes Item Data to the NBT. */
    NBTTagCompound writeItemNBT(NBTTagCompound aNBT);

    /** Sets the Item Display Name. Use null to reset it. */
    void setCustomName(String aName);

    String getCustomName();

    /** return the internal Name of this TileEntity to be registered. */
    String getTileEntityName();

    /**
     * Called when a TileEntity of this particular Class is being registered first at any MultiTileEntity Registry. So
     * basically one call per Class.
     */
    void onRegistrationFirst(MultiTileEntityRegistry aRegistry, short aID);

    /** Called after the TileEntity has been placed and set up. */
    void onTileEntityPlaced();

    /** Checks if the TileEntity is Invalid or Unloaded, should bes required for every TileEntity. */
    @Override
    boolean isDead();

    void loadTextures(String folder);

    void copyTextures();

    void issueClientUpdate();

    void sendClientData(EntityPlayerMP aPlayer);

    boolean receiveClientData(int aEventID, int aValue);

    void setShouldRefresh(boolean aShouldRefresh);

    void addCollisionBoxesToList(AxisAlignedBB aAABB, List<AxisAlignedBB> aList, Entity aEntity);

    AxisAlignedBB getCollisionBoundingBoxFromPool();

    AxisAlignedBB getSelectedBoundingBoxFromPool();

    void setBlockBoundsBasedOnState(Block aBlock);

    void onBlockAdded();

    boolean playerOwnsThis(EntityPlayer aPlayer, boolean aCheckPrecicely);

    boolean privateAccess();

    /** @return the amount of Time this TileEntity has been loaded. */
    @Override
    long getTimer();

    /** Sets the Owner of the Machine. Returns the set Name. */
    String setOwnerName(String aName);

    /** gets the Name of the Machines Owner or "Player" if not set. */
    String getOwnerName();

    /** Gets the UniqueID of the Machines Owner. */
    UUID getOwnerUuid();

    /** Sets the UniqueID of the Machines Owner. */
    void setOwnerUuid(UUID uuid);

    /**
     * Causes a general Texture update. Only used Client Side to mark Blocks dirty.
     */
    void issueTextureUpdate();

    /**
     * Paintable Support
     */
    boolean unpaint();

    boolean isPainted();

    boolean paint(int aRGB);

    int getPaint();

    /**
     * Sets the main facing to {side} and update as appropriately
     *
     * @return Whether the facing was changed
     */
    boolean setMainFacing(ForgeDirection side);

    boolean isFacingValid(ForgeDirection facing);

    void onFacingChange();

    @Override
    default void setFrontFacing(ForgeDirection side) {
        setMainFacing(side);
    }

    boolean shouldTriggerBlockUpdate();

    void onMachineBlockUpdate();

    boolean allowInteraction(Entity aEntity);

    default void onLeftClick(EntityPlayer aPlayer) {
        /* do nothing */
    }

    boolean onBlockActivated(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ);

    boolean onRightClick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ);

    ArrayList<ItemStack> getDrops(int aFortune, boolean aSilkTouch);

    boolean isSideSolid(ForgeDirection side);

    float getExplosionResistance(Entity aExploder, double aExplosionX, double aExplosionY, double aExplosionZ);

    float getExplosionResistance();

    void onExploded(Explosion aExplosion);

    boolean recolourBlock(ForgeDirection side, byte aColor);

    /** Adds to the Creative Tab. return false to prevent it from being added. */
    boolean getSubItems(MultiTileEntityBlock block, Item item, CreativeTabs tab, List<ItemStack> list, short aID);

    ItemStack getPickBlock(MovingObjectPosition aTarget);

    boolean shouldSideBeRendered(ForgeDirection side);

    boolean isSurfaceOpaque(ForgeDirection side);

    boolean onPlaced(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, ForgeDirection side,
        float aHitX, float aHitY, float aHitZ);

    /** return true to prevent the TileEntity from being removed. */
    boolean onBlockBroken();

    default void onNeighborBlockChange(World aWorld, Block aBlock) {
        /* Do Nothing */
    }

    // ItemStack getPickBlock(MovingObjectPosition aTarget);

    /** Remember that it passes the opposite Side due to the way vanilla works! */
    default int isProvidingWeakPower(ForgeDirection oppositeSide) {
        return 0;
    }

    default boolean providesStrongPower() {
        return false;
    }

    /** Remember that it passes the opposite Side due to the way vanilla works! */
    default int isProvidingStrongPower(ForgeDirection oppositeSide) {
        return 0;
    }

    default boolean shouldCheckWeakPower(ForgeDirection side) {
        return false;
    }

    default boolean getWeakChanges() {
        return false;
    }

    default boolean hasComparatorInputOverride() {
        return false;
    }

    default int getComparatorInputOverride(ForgeDirection side) {
        return 0;
    }

    default float getBlockHardness() {
        return 1.0f;
    }

    /** Adds ToolTips to the Item. */
    default void addToolTips(List<String> aList, ItemStack aStack, boolean aF3_H) {
        // Do nothing
    }

    // interface IMTE_HasMultiBlockMachineRelevantData extends IMultiTileEntity {
    //
    // /** Return true to mark this Block as a Machine Block for Multiblocks. (Triggers machine update thread) */
    // boolean hasMultiBlockMachineRelevantData();
    // }

}
