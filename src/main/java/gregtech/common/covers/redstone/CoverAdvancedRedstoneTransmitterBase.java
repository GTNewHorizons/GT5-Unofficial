package gregtech.common.covers.redstone;

import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverPosition;
import io.netty.buffer.ByteBuf;

public abstract class CoverAdvancedRedstoneTransmitterBase extends CoverAdvancedWirelessRedstoneBase {

    protected boolean invert;

    public CoverAdvancedRedstoneTransmitterBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isInverted() {
        return this.invert;
    }

    public CoverAdvancedRedstoneTransmitterBase setInverted(boolean inverted) {
        this.invert = inverted;
        return this;
    }

    @Override
    protected void initializeData() {
        super.initializeData();
        this.invert = false;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        invert = tag.getBoolean("invert");
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
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
        super.writeToByteBuf(byteBuf);
        byteBuf.writeBoolean(invert);
    }

    private void unregisterSignal() {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) return;
        final CoverPosition key = getCoverKey(coverable, coverSide);
        removeSignalAt(uuid, frequency, key);
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

    @Override
    public void preDataChanged(Cover newCover) {
        if (newCover instanceof CoverAdvancedRedstoneTransmitterBase newTransmitterCover
            && (!Objects.equals(frequency, newTransmitterCover.frequency)
                || !Objects.equals(uuid, newTransmitterCover.uuid))) {
            unregisterSignal();
        }
    }

    // GUI stuff

}
