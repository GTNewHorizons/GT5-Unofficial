package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.CoverBehavior;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    boolean canPlaceCoverIDAtSide(ForgeDirection side, int aID);

    boolean canPlaceCoverItemAtSide(ForgeDirection side, ItemStack aCover);

    boolean dropCover(ForgeDirection side, ForgeDirection droppedSide, boolean aForced);

    @Deprecated
    void setCoverDataAtSide(ForgeDirection side, int aData);

    default void setCoverDataAtSide(ForgeDirection side, ISerializableObject aData) {
        if (aData instanceof ISerializableObject.LegacyCoverData)
            setCoverDataAtSide(side, ((ISerializableObject.LegacyCoverData) aData).get());
    }

    void setCoverIdAndDataAtSide(ForgeDirection side, int aId, ISerializableObject aData);

    void setCoverIDAtSide(ForgeDirection side, int aID);

    boolean setCoverIDAtSideNoUpdate(ForgeDirection side, int aID);

    void setCoverItemAtSide(ForgeDirection side, ItemStack aCover);

    @Deprecated
    int getCoverDataAtSide(ForgeDirection side);

    default CoverInfo getCoverInfoAtSide(ForgeDirection side) {
        return null;
    }

    default ISerializableObject getComplexCoverDataAtSide(ForgeDirection side) {
        return new ISerializableObject.LegacyCoverData(getCoverDataAtSide(side));
    }

    int getCoverIDAtSide(ForgeDirection side);

    ItemStack getCoverItemAtSide(ForgeDirection side);

    @Deprecated
    CoverBehavior getCoverBehaviorAtSide(ForgeDirection side);

    default CoverBehaviorBase<?> getCoverBehaviorAtSideNew(ForgeDirection side) {
        return getCoverBehaviorAtSide(side);
    }

    /**
     * For use by the regular MetaTileEntities. Returns the Cover Manipulated input Redstone. Don't use this if you are
     * a Cover Behavior. Only for MetaTileEntities.
     */
    byte getInternalInputRedstoneSignal(ForgeDirection side);

    /**
     * For use by the regular MetaTileEntities. This makes it not conflict with Cover based Redstone Signals. Don't use
     * this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    void setInternalOutputRedstoneSignal(ForgeDirection side, byte aStrength);

    /**
     * Causes a general Cover Texture update. Sends 6 Integers to Client + causes @issueTextureUpdate()
     */
    void issueCoverUpdate(ForgeDirection side);

    /**
     * Receiving a packet with cover data.
     */
    void receiveCoverData(ForgeDirection coverSide, int coverID, int coverData);

    /**
     * Receiving a packet with cover data.
     *
     * @param coverSide cover side
     * @param aPlayer   the player who made the change
     */
    default void receiveCoverData(ForgeDirection coverSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        if (aCoverData instanceof ISerializableObject.LegacyCoverData)
            receiveCoverData(coverSide, aCoverID, ((ISerializableObject.LegacyCoverData) aCoverData).get());
    }
}
