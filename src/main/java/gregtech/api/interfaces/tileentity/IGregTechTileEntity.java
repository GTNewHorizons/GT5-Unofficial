package gregtech.api.interfaces.tileentity;

import gregtech.api.interfaces.IDescribable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * A simple compound Interface for all my TileEntities.
 * <p/>
 * Also delivers most of the Information about my TileEntities.
 * <p/>
 * It can cause Problems to include this Interface!
 */
public interface IGregTechTileEntity extends ITexturedTileEntity, IGearEnergyTileEntity, ICoverable, IFluidHandler, ITurnable, IGregTechDeviceInformation, IUpgradableMachine, IDigitalChest, IDescribable, IMachineBlockUpdateable, IGregtechWailaProvider {
    /**
     * gets the Error displayed on the GUI
     */
    int getErrorDisplayID();

    /**
     * sets the Error displayed on the GUI
     */
    void setErrorDisplayID(int aErrorID);

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
     * Sets the MetaTileEntity.
     * Even though this uses the Universal Interface, certain BaseMetaTileEntities only accept one kind of MetaTileEntity
     * so only use this if you are sure its the correct one or you will get a Class cast Error.
     *
     * @param aMetaTileEntity
     */
    void setMetaTileEntity(IMetaTileEntity aMetaTileEntity);

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
    boolean onRightclick(EntityPlayer aPlayer, byte aSide, float aX, float aY, float aZ);

    float getBlastResistance(byte aSide);

    ArrayList<ItemStack> getDrops();

    /**
     * 255 = 100%
     */
    int getLightOpacity();

    void addCollisionBoxesToList(World aWorld, int aX, int aY, int aZ, AxisAlignedBB inputAABB, List<AxisAlignedBB> outputAABB, Entity collider);

    AxisAlignedBB getCollisionBoundingBoxFromPool(World aWorld, int aX, int aY, int aZ);

    void onEntityCollidedWithBlock(World aWorld, int aX, int aY, int aZ, Entity collider);

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    default void onMachineBlockUpdate(){
        if(!isDead() && getMetaTileEntity() != null && getMetaTileEntity().getBaseMetaTileEntity() == this){
            getMetaTileEntity().onMachineBlockUpdate();
        }
    }

    /**
     * Checks validity of meta tile and delegates to it
     */
    @Override
    default boolean isMachineBlockUpdateRecursive() {
        return !isDead() && getMetaTileEntity() != null &&
                getMetaTileEntity().getBaseMetaTileEntity() == this &&
                getMetaTileEntity().isMachineBlockUpdateRecursive();
    }

    default void setShutdownStatus(boolean newStatus) {return;}
}
