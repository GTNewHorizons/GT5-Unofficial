package gregtech.api.interfaces.tileentity;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.common.covers.Cover;

public interface ICoverable extends IRedstoneTileEntity, IHasInventory, IBasicEnergyContainer {

    /**
     * Remove the cover from the coverable and spawn the result of {@link #detachCover(ForgeDirection)} in the world on
     * the dropped side.
     */
    void dropCover(ForgeDirection side, ForgeDirection droppedSide);

    /**
     * Actually removes the cover from the coverable and return the cover item. <br>
     * Called by {@link #dropCover(ForgeDirection, ForgeDirection)}
     */
    ItemStack detachCover(ForgeDirection side);

    /**
     * Called when the cover is initially attached to a machine.
     *
     * @param cover The cover
     */
    void attachCover(@NotNull Cover cover);

    boolean hasCoverAtSide(ForgeDirection side);

    @NotNull
    Cover getCoverAtSide(ForgeDirection side);

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
     * Receiving nbt with cover data.
     *
     * @param nbt
     */
    void updateAttachedCover(int coverId, ForgeDirection side, NBTTagCompound nbt);

    /**
     * Receiving a packet with cover data.
     *
     * @param data
     */
    void updateAttachedCover(ByteArrayDataInput data);
}
