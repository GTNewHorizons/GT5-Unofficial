package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    boolean canPlaceCoverIDAtSide(byte aSide, int aID);

    boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover);

    boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced);

    @Deprecated
    void setCoverDataAtSide(byte aSide, int aData);

    default void setCoverDataAtSide(byte aSide, ISerializableObject aData) {
        if (aData instanceof ISerializableObject.LegacyCoverData)
            setCoverDataAtSide(aSide, ((ISerializableObject.LegacyCoverData) aData).get());
    }

    void setCoverIdAndDataAtSide(byte aSide, int aId, ISerializableObject aData);

    void setCoverIDAtSide(byte aSide, int aID);

    boolean setCoverIDAtSideNoUpdate(byte aSide, int aID);

    void setCoverItemAtSide(byte aSide, ItemStack aCover);

    @Deprecated
    int getCoverDataAtSide(byte aSide);

    default CoverInfo getCoverInfoAtSide(byte aSide) {
        return null;
    }

    default ISerializableObject getComplexCoverDataAtSide(byte aSide) {
        return new ISerializableObject.LegacyCoverData(getCoverDataAtSide(aSide));
    }

    /**
     * @deprecated use {@link #getCoverIDAtDirection(ForgeDirection)}
     */
    @Deprecated
    int getCoverIDAtSide(byte aSide);

    default int getCoverIDAtDirection(ForgeDirection direction) {
        return getCoverIDAtSide((byte) direction.ordinal());
    }

    ItemStack getCoverItemAtSide(byte aSide);

    @Deprecated
    GT_CoverBehavior getCoverBehaviorAtSide(byte aSide);

    default GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(byte aSide) {
        return getCoverBehaviorAtSide(aSide);
    }

    /**
     * For use by the regular MetaTileEntities. Returns the Cover Manipulated input Redstone. Don't use this if you are
     * a Cover Behavior. Only for MetaTileEntities.
     */
    byte getInternalInputRedstoneSignal(byte aSide);

    /**
     * For use by the regular MetaTileEntities. This makes it not conflict with Cover based Redstone Signals. Don't use
     * this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    void setInternalOutputRedstoneSignal(byte aSide, byte aStrength);

    /**
     * Causes a general Cover Texture update. Sends 6 Integers to Client + causes @issueTextureUpdate()
     */
    void issueCoverUpdate(byte aSide);

    /**
     * Receiving a packet with cover data.
     */
    void receiveCoverData(byte coverSide, int coverID, int coverData);

    /**
     * Receiving a packet with cover data.
     * 
     * @param aPlayer the player who made the change
     */
    default void receiveCoverData(byte aCoverSide, int aCoverID, ISerializableObject aCoverData,
        EntityPlayerMP aPlayer) {
        if (aCoverData instanceof ISerializableObject.LegacyCoverData)
            receiveCoverData(aCoverSide, aCoverID, ((ISerializableObject.LegacyCoverData) aCoverData).get());
    }
}
