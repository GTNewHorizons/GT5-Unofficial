package gregtech.common.covers.redstone;

import java.util.Objects;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneTransmitterBase extends CoverAdvancedWirelessRedstoneBase {

    protected boolean invert;

    public CoverAdvancedRedstoneTransmitterBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.invert = false;
    }

    public boolean isInverted() {
        return this.invert;
    }

    public CoverAdvancedRedstoneTransmitterBase setInverted(boolean inverted) {
        this.invert = inverted;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        String oldFrequency = frequency;
        UUID oldUuid = uuid;
        super.readDataFromNbt(nbt);
        unregisterOldSignal(oldUuid, oldFrequency);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        invert = tag.getBoolean("invert");
    }

    private void unregisterOldSignal(UUID oldUuid, String oldFrequency) {
        if (oldUuid != null && (!Objects.equals(uuid, oldUuid) || frequency != oldFrequency)) {
            unregisterSignal(oldUuid, oldFrequency);
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        int oldFrequency = frequency;
        UUID oldUuid = uuid;
        super.readDataFromPacket(byteData);
        unregisterOldSignal(oldUuid, oldFrequency);

        invert = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setBoolean("invert", invert);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeBoolean(invert);
    }

    private void unregisterSignal() {
        unregisterSignal(uuid, frequency);
    }

    private void unregisterSignal(UUID oldUuid, String oldFrequency) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) return;
        final long hash = hashCoverCoords(coverable, coverSide);
        removeSignalAt(oldUuid, oldFrequency, hash);
    }

    @Override
    public void onCoverRemoval() {
        unregisterSignal();
    }

    @Override
    public void onBaseTEDestroyed() {
        unregisterSignal();
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        invert = !invert;
        GTUtility
            .sendChatToPlayer(aPlayer, invert ? GTUtility.trans("054", "Inverted") : GTUtility.trans("055", "Normal"));
    }

}
