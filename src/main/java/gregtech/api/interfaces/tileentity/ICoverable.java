package gregtech.api.interfaces.tileentity;

import gregtech.api.util.GT_CoverBehavior;
import net.minecraft.item.ItemStack;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {
    boolean canPlaceCoverIDAtSide(byte aSide, int aID);

    boolean canPlaceCoverItemAtSide(byte aSide, ItemStack aCover);

    boolean dropCover(byte aSide, byte aDroppedSide, boolean aForced);

    void setCoverDataAtSide(byte aSide, int aData);

    void setCoverIDAtSide(byte aSide, int aID);

    void setCoverItemAtSide(byte aSide, ItemStack aCover);

    int getCoverDataAtSide(byte aSide);

    int getCoverIDAtSide(byte aSide);

    ItemStack getCoverItemAtSide(byte aSide);

    GT_CoverBehavior getCoverBehaviorAtSide(byte aSide);

    /**
     * For use by the regular MetaTileEntities. Returns the Cover Manipulated input Redstone.
     * Don't use this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    byte getInternalInputRedstoneSignal(byte aSide);

    /**
     * For use by the regular MetaTileEntities. This makes it not conflict with Cover based Redstone Signals.
     * Don't use this if you are a Cover Behavior. Only for MetaTileEntities.
     */
    void setInternalOutputRedstoneSignal(byte aSide, byte aStrength);

    /**
     * Causes a general Cover Texture update.
     * Sends 6 Integers to Client + causes @issueTextureUpdate()
     */
    void issueCoverUpdate(byte aSide);

    /**
     * Receiving a packet with cover data.
     */
    void receiveCoverData(byte coverSide, int coverID, int coverData);
}