package gregtech.api.interfaces.tileentity;

import java.util.ArrayList;
import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizon.gtnhlib.capability.CapabilityProvider;

import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddInventorySlots;
import gregtech.api.interfaces.modularui.IGetGUITextureSet;
import gregtech.api.util.shutdown.ShutDownReason;

/**
 * A simple compound Interface for all my TileEntities.
 * <p/>
 * Also delivers most of the Information about my TileEntities.
 * <p/>
 * It can cause Problems to include this Interface!
 */
public interface IGregTechTileEntity extends ITexturedTileEntity, ICoverable, IFluidHandler, ITurnable,
    IGregTechDeviceInformation, IUpgradableMachine, IDigitalChest, IDescribable, IMachineBlockUpdateable,
    IGregtechWailaProvider, IGetGUITextureSet, IAddInventorySlots, CapabilityProvider {

    /**
     * @return the MetaID of the Block or the MetaTileEntity ID.
     */
    int getMetaTileID();

    /**
     * Internal Usage only!
     */
    int setMetaTileID(short aID);

    /**
     * @return the MetaTileEntity which is belonging to this, or null if it doesnt has one.
     */
    IMetaTileEntity getMetaTileEntity();

    /**
     * Sets the MetaTileEntity. Even though this uses the Universal Interface, certain BaseMetaTileEntities only accept
     * one kind of MetaTileEntity so only use this if you are sure its the correct one or you will get a Class cast
     * Error.
     *
     * @param aMetaTileEntity a MetaTileEntity
     */
    void setMetaTileEntity(IMetaTileEntity aMetaTileEntity);

    /**
     * @return If this TileEntity is valid as a MetaTileEntity holder.
     */
    boolean canAccessData();

    /**
     * Causes a general Texture update.
     * <p/>
     * Only used Client Side to mark Blocks dirty.
     */
    void issueTextureUpdate();

    /**
     * Causes the Machine to send its initial Data, like Covers and its ID.
     */
    void issueClientUpdate();

    /**
     * Causes the machine to send a tile entity description packet to the client. Only has an effect on the server.
     *
     * @see IMetaTileEntity#getDescriptionData()
     * @see IMetaTileEntity#onDescriptionPacket(NBTTagCompound)
     * @see TileEntity#getDescriptionPacket()
     * @see TileEntity#onDataPacket(NetworkManager, S35PacketUpdateTileEntity)
     * @see net.minecraft.world.World#markBlockForUpdate(int, int, int)
     */
    default void issueTileUpdate() {

    }

    /**
     * causes Explosion. Strength in Overload-EU
     */
    void doExplosion(long aExplosionEU);

    /**
     * Sets the Block on Fire in all 6 Directions
     */
    void setOnFire();

    /**
     * Sets the Block to Fire
     */
    void setToFire();

    /**
     * Sets the Owner of the Machine. Returns the set Name.
     */
    String setOwnerName(String aName);

    /**
     * gets the Name of the Machines Owner or "Player" if not set.
     */
    String getOwnerName();

    /**
     * Gets the UniqueID of the Machines Owner.
     */
    UUID getOwnerUuid();

    /**
     * Sets the UniqueID of the Machines Owner.
     */
    void setOwnerUuid(UUID uuid);

    /**
     * Sets initial Values from NBT
     *
     * @param aNBT is the NBTTag of readFromNBT
     * @param aID  is the MetaTileEntityID
     */
    void setInitialValuesAsNBT(NBTTagCompound aNBT, short aID);

    /**
     * Called when leftclicking the TileEntity
     */
    void onLeftclick(EntityPlayer aPlayer);

    /**
     * Called when rightclicking the TileEntity
     */
    boolean onRightclick(EntityPlayer aPlayer, ForgeDirection side, float aX, float aY, float aZ);

    ArrayList<ItemStack> getDrops();

    /**
     * 255 = 100%
     */
    int getLightOpacity();

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    default void onMachineBlockUpdate() {
        if (!isDead() && getMetaTileEntity() != null && getMetaTileEntity().getBaseMetaTileEntity() == this) {
            getMetaTileEntity().onMachineBlockUpdate();
        }
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    default boolean isMachineBlockUpdateRecursive() {
        return !isDead() && getMetaTileEntity() != null
            && getMetaTileEntity().getBaseMetaTileEntity() == this
            && getMetaTileEntity().isMachineBlockUpdateRecursive();
    }

    default void setShutdownStatus(boolean newStatus) {}

    default void setShutDownReason(@Nonnull ShutDownReason reason) {}

    void enableTicking();
    
    void disableTicking();

    /**
     * gets the time statistics used for CPU timing
     */
    default int[] getTimeStatistics() {
        return null;
    }

    default void startTimeStatistics() {}

    /**
     * Returns true if steam powers the tile entity
     */
    default boolean isSteampowered() {
        return false;
    };
}
