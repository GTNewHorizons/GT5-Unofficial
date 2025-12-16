package gregtech.common.covers;

import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.CoverRedstoneWirelessBaseGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.RedstoneWirelessBaseUIFactory;
import io.netty.buffer.ByteBuf;

public abstract class CoverRedstoneWirelessBase extends Cover {

    private int frequency;
    private boolean privateChannel;
    public static final int MAX_CHANNEL = 65535;
    private static final int PRIVATE_MASK = 0xFFFE0000;
    public static final int CHANNEL_MASK = 0x0000FFFF;
    public static final int CHECKBOX_MASK = 0x00010000;

    public CoverRedstoneWirelessBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX != 0) && ((aY > 0.375D) && (aY < 0.625D)))) {
            int newFrequency = (((Integer) GTUtility.stackToInt(aPlayer.inventory.getCurrentItem())).hashCode()
                & CHANNEL_MASK);
            processCoverData(newFrequency, this.privateChannel);
            GTUtility.sendChatToPlayer(
                aPlayer,
                translateToLocalFormatted("gt.interact.desc.freq_format", frequency, privateChannel));
            return true;
        }
        return false;
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (((aX > 0.375D) && (aX < 0.625D)) || ((coverSide.offsetX == 0)
            || (((aY > 0.375D) && (aY < 0.625D)) || ((((aZ <= 0.375D) || (aZ >= 0.625D))))))) {
            final float[] tCoords = GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ);

            final short adjustFrequencyValue = switch ((byte) ((byte) (int) (tCoords[0] * 2.0F)
                + 2 * (byte) (int) (tCoords[1] * 2.0F))) {
                case 0 -> -32;
                case 1 -> 32;
                case 2 -> -1024;
                case 3 -> 1024;
                default -> 0;
            };

            int newFrequency = Math.max(0, Math.min(MAX_CHANNEL, this.frequency + adjustFrequencyValue));
            processCoverData(newFrequency, this.privateChannel);
        }
        GTUtility.sendChatToPlayer(
            aPlayer,
            translateToLocalFormatted("gt.interact.desc.freq_format", frequency, privateChannel));
    }

    @Override
    public boolean letsEnergyIn() {
        return true;
    }

    @Override
    public boolean letsEnergyOut() {
        return true;
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid aFluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int aSlot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int aSlot) {
        return true;
    }

    @Override
    public String getDescription() {
        return translateToLocalFormatted("gt.interact.desc.freq_format", frequency, privateChannel);
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public boolean isPrivateChannel() {
        return privateChannel;
    }

    public void setPrivateChannel(boolean privateChannel) {
        this.privateChannel = privateChannel;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagInt nbtTagInt) {
            readLegacyDataFromNbt(nbtTagInt);
            return;
        }

        NBTTagCompound tag = (NBTTagCompound) nbt;
        processCoverData(tag.getInteger("frequency"), tag.getBoolean("privateChannel"));
    }

    private void readLegacyDataFromNbt(NBTTagInt nbtTagInt) {
        int data = nbtTagInt.func_150287_d();
        processCoverData(getFlagFrequency(data), getFlagCheckbox(data));
    }

    @Override
    protected void readDataFromPacket(ByteArrayDataInput byteData) {
        processCoverData(byteData.readInt(), byteData.readBoolean());
    }

    protected void processCoverData(int frequency, boolean privateChannel) {
        if (this.frequency != frequency || this.privateChannel != privateChannel) {
            GregTechAPI.sWirelessRedstone.remove(getMapFrequency());

            ICoverable tile = coveredTile.get();
            if (tile == null) {
                return;
            }
            this.frequency = frequency;
            this.privateChannel = privateChannel;
            GregTechAPI.sWirelessRedstone.put(getMapFrequency(), tile.getOutputRedstoneSignal(coverSide));
        }
    }

    protected int getMapFrequency() {
        return this.frequency + (this.privateChannel ? MAX_CHANNEL + 1 : 0);
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("frequency", this.frequency);
        tag.setBoolean("privateChannel", this.privateChannel);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeInt(this.frequency);
        byteBuf.writeBoolean(this.privateChannel);
    }

    protected int getFlagFrequency(int coverVariable) {
        return coverVariable & CoverRedstoneWirelessBase.CHANNEL_MASK;
    }

    protected boolean getFlagCheckbox(int coverVariable) {
        return (coverVariable & CoverRedstoneWirelessBase.CHECKBOX_MASK) != 0;
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return true;
    }
    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverRedstoneWirelessBaseGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new RedstoneWirelessBaseUIFactory(buildContext).createWindow();
    }

}
