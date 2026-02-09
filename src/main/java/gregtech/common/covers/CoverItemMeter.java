package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.CoverItemMeterGui;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.mui1.cover.ItemMeterUIFactory;
import gregtech.common.tileentities.machines.outputme.MTEHatchOutputBusME;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import io.netty.buffer.ByteBuf;

public class CoverItemMeter extends Cover {

    private boolean inverted;
    /** The special value {@code -1} means all slots. */
    private int slot;
    /** The special value {@code 0} means threshold check is disabled. */
    private int threshold;

    public CoverItemMeter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        inverted = false;
        slot = -1;
        threshold = 0;
    }

    public int getSlot() {
        return this.slot;
    }

    public CoverItemMeter setSlot(int slot) {
        this.slot = slot;
        return this;
    }

    public boolean isInverted() {
        return this.inverted;
    }

    public CoverItemMeter setInverted(boolean inverted) {
        this.inverted = inverted;
        return this;
    }

    public int getThreshold() {
        return this.threshold;
    }

    public CoverItemMeter setThreshold(int threshold) {
        this.threshold = threshold;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        inverted = tag.getBoolean("invert");
        slot = tag.getInteger("slot");
        threshold = tag.getInteger("threshold");
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        inverted = byteData.readBoolean();
        slot = byteData.readInt();
        threshold = byteData.readInt();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setBoolean("invert", inverted);
        tag.setInteger("slot", slot);
        tag.setInteger("threshold", threshold);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeBoolean(inverted);
        byteBuf.writeInt(slot);
        byteBuf.writeInt(threshold);
    }

    public static byte computeSignalBasedOnItems(ICoverable tileEntity, boolean inverted, int threshold, int slot,
        int ordinalSide) {
        long max = 0;
        long used = 0;
        final IMetaTileEntity mte = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
        if (mte instanceof MTEDigitalChestBase dc) {
            max = dc.getMaxItemCount();
            used = dc.getProgresstime();
        } else if (mte instanceof MTEHatchOutputBusME meoutputbus) {
            // todo for cache mode
            if (meoutputbus.canAcceptAnyItem()) {
                max = 64;
                used = 0;
            }
        } else {
            final int[] slots = slot >= 0 ? new int[] { slot } : tileEntity.getAccessibleSlotsFromSide(ordinalSide);

            for (int i : slots) {
                if (i >= 0 && i < tileEntity.getSizeInventory()) {
                    max += 64;
                    final ItemStack stack = tileEntity.getStackInSlot(i);
                    if (stack != null) used += ((long) stack.stackSize << 6) / stack.getMaxStackSize();
                }
            }
        }

        return GTUtility.convertRatioToRedstone(used, max, threshold, inverted);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null) {
            byte signal = computeSignalBasedOnItems(coverable, inverted, threshold, slot, coverSide.ordinal());
            coverable.setOutputRedstoneSignal(coverSide, signal);
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if (aPlayer.isSneaking()) {
            if (inverted) {
                inverted = false;
                GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.normal");
            } else {
                inverted = true;
                GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.inverted");
            }
        } else {
            slot++;
            if (slot > coverable.getSizeInventory()) slot = -1;

            if (slot == -1)
                GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + GTUtility.trans("ALL", "All"));
            else GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("053", "Slot: ") + slot);
        }
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
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    @Override
    public int getDefaultTickRate() {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverItemMeterGui(this);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ItemMeterUIFactory(buildContext).createWindow();
    }

}
