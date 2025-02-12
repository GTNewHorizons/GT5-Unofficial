package gregtech.api.interfaces.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverInfo;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    boolean dropCover(ForgeDirection side, ForgeDirection droppedSide, boolean aForced);

    @Deprecated
    void setCoverDataAtSide(ForgeDirection side, int aData);

    void setCoverDataAtSide(ForgeDirection side, ISerializableObject aData);

    /**
     * Called when the cover is initially attached to a machine.
     *
     * @param cover The cover
     * @param side  Which side the cover is attached to
     */
    void attachCover(CoverInfo cover, ForgeDirection side);

    CoverInfo getCoverInfoAtSide(ForgeDirection side);

    int getCoverIDAtSide(ForgeDirection side);

    ItemStack getCoverItemAtSide(ForgeDirection side);

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
     *
     * @param coverInfo
     * @param side      cover side
     */
    void updateCover(CoverInfo coverInfo, ForgeDirection side);
}
