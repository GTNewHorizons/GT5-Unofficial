package gregtech.api.interfaces.tileentity;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    boolean canPlaceCoverIDAtSide(ForgeDirection aSide, int aID);

    boolean canPlaceCoverItemAtSide(ForgeDirection aSide, ItemStack aCover);

    boolean dropCover(ForgeDirection aSide, ForgeDirection aDroppedSide, boolean aForced);

    @Deprecated
    void setCoverDataAtSide(ForgeDirection aSide, int aData);

    default void setCoverDataAtSide(ForgeDirection aSide, ISerializableObject aData) {
        if (aData instanceof ISerializableObject.LegacyCoverData)
            setCoverDataAtSide(aSide, ((ISerializableObject.LegacyCoverData) aData).get());
    }

    void setCoverIdAndDataAtSide(ForgeDirection aSide, int aId, ISerializableObject aData);

    void setCoverIDAtSide(ForgeDirection aSide, int aID);

    boolean setCoverIDAtSideNoUpdate(ForgeDirection aSide, int aID);

    void setCoverItemAtSide(ForgeDirection aSide, ItemStack aCover);

    @Deprecated
    int getCoverDataAtSide(ForgeDirection aSide);

    default CoverInfo getCoverInfoAtSide(ForgeDirection aSide) {
        return null;
    }

    default ISerializableObject getComplexCoverDataAtSide(ForgeDirection aSide) {
        return new ISerializableObject.LegacyCoverData(getCoverDataAtSide(aSide));
    }

    int getCoverIDAtSide(ForgeDirection aSide);

    ItemStack getCoverItemAtSide(ForgeDirection aSide);

    @Deprecated
    GT_CoverBehavior getCoverBehaviorAtSide(ForgeDirection aSide);

    default GT_CoverBehaviorBase<?> getCoverBehaviorAtSideNew(ForgeDirection aSide) {
        return getCoverBehaviorAtSide(aSide);
    }

    /**
     * For use by the regular MetaTileEntities. Returns the Cover Manipulated input Redstone. Don't use this if you are
     * a Cover Behavior. Only for MetaTileEntities.
     */
    byte getInternalInputRedstoneSignal(ForgeDirection aSide);

    /**
     * For use by the regular MetaTileEntities. This makes it not conflict with Cover based Redstone Signals. Don't use
     * this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    void setInternalOutputRedstoneSignal(ForgeDirection aSide, byte aStrength);

    /**
     * Causes a general Cover Texture update. Sends 6 Integers to Client + causes @issueTextureUpdate()
     */
    void issueCoverUpdate(ForgeDirection aSide);

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
