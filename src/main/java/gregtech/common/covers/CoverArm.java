package gregtech.common.covers;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.CoverArmGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ArmUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverArm extends Cover {

    private boolean export;
    private int internalSlotId;
    private int externalSlotId;
    public final int mTickRate;
    // TODO: REMOVE AFTER 2.9
    // msb converted, 2nd : direction (1=export)
    // right 14 bits: internalSlot, next 14 bits adjSlot, 0 = all, slot = -1
    public static final int EXPORT_MASK = 0x40000000;
    public static final int SLOT_ID_MASK = 0x3FFF;
    public static final int CONVERTED_BIT = 0x80000000;

    public CoverArm(CoverContext context, int aTickRate, ITexture coverTexture) {
        super(context, coverTexture);
        this.mTickRate = aTickRate;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null || (((coverable instanceof IMachineProgress machine)) && (!machine.isAllowedToWork()))
            || !(coverable instanceof TileEntity tileEntity)) {
            return;
        }

        GTItemTransfer transfer = new GTItemTransfer();

        final int toSlot;
        final int fromSlot;

        if (export) {
            transfer.push(coverable, coverSide);

            fromSlot = internalSlotId;
            toSlot = externalSlotId;
        } else {
            transfer.pull(coverable, coverSide);

            fromSlot = externalSlotId;
            toSlot = internalSlotId;
        }

        if (fromSlot > 0) transfer.setSourceSlots(fromSlot - 1);
        if (toSlot > 0) transfer.setSinkSlots(toSlot - 1);

        transfer.transfer();
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) {
            internalSlotId += aPlayer.isSneaking() ? 16 : 1;
        } else {
            internalSlotId = Math.max(0, internalSlotId - (aPlayer.isSneaking() ? 16 : 1));
        }
        sendMessageToPlayer(aPlayer);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int step = (GTUtility.getClickedFacingCoords(coverSide, aX, aY, aZ)[0] >= 0.5F) ? 1 : -1;
        internalSlotId = Math.max(0, internalSlotId + step);
        sendMessageToPlayer(aPlayer);
        return true;
    }

    private void sendMessageToPlayer(EntityPlayer aPlayer) {
        if (export) GTUtility.sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.out_slot") + externalSlotId);
        else GTUtility.sendChatToPlayer(aPlayer, translateToLocal("gt.interact.desc.in_slot") + internalSlotId);
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(export);
        byteBuf.writeInt(internalSlotId);
        byteBuf.writeInt(externalSlotId);
    }

    @Override
    protected void readDataFromPacket(ByteArrayDataInput byteData) {
        this.export = byteData.readBoolean();
        this.internalSlotId = byteData.readInt();
        this.externalSlotId = byteData.readInt();
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagInt legacyData) {
            int data = legacyData.func_150287_d();
            this.export = getFlagExport(data);
            this.internalSlotId = getFlagInternalSlot(data);
            this.externalSlotId = getFlagExternalSlot(data);
            return;
        }
        NBTTagCompound tag = (NBTTagCompound) nbt;
        this.export = tag.getBoolean("export");
        this.internalSlotId = tag.getInteger("internalSlotId");
        this.externalSlotId = tag.getInteger("externalSlotId");
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("export", this.export);
        tag.setInteger("internalSlotId", this.internalSlotId);
        tag.setInteger("externalSlotId", this.externalSlotId);

        return tag;
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    public boolean letsRedstoneGoOut() {
        return true;
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
    public boolean letsFluidIn(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return true;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return true;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return true;
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return this.mTickRate;
    }

    public boolean isExport() {
        return export;
    }

    public void setExport(boolean export) {
        this.export = export;
    }

    public int getInternalSlotId() {
        return internalSlotId;
    }

    public void setInternalSlotId(int internalSlotId) {
        this.internalSlotId = internalSlotId;
    }

    public int getExternalSlotId() {
        return externalSlotId;
    }

    public void setExternalSlotId(int externalSlotId) {
        this.externalSlotId = externalSlotId;
    }

    private boolean getFlagExport(int coverVariable) {
        return (coverVariable & CoverArm.EXPORT_MASK) != 0;
    }

    private int getFlagInternalSlot(int coverVariable) {
        return coverVariable & CoverArm.SLOT_ID_MASK;
    }

    private int getFlagExternalSlot(int coverVariable) {
        return (coverVariable >> 14) & CoverArm.SLOT_ID_MASK;
    }
    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverArmGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ArmUIFactory(buildContext).createWindow();
    }

}
