package gregtech.common.covers.redstone;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.covers.CoverPosition;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.redstone.CoverWirelessMaintenenceDetectorGui;
import gregtech.common.gui.mui1.cover.WirelessMaintenanceDetectorUIFactory;
import io.netty.buffer.ByteBuf;

public class CoverWirelessMaintenanceDetector extends CoverAdvancedRedstoneTransmitterBase {

    private MaintenanceMode mode;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessMaintenanceDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
        this.mode = MaintenanceMode.ONE_ISSUE;
        this.physical = true;
    }

    public MaintenanceMode getMode() {
        return mode;
    }

    public CoverWirelessMaintenanceDetector setMode(MaintenanceMode mode) {
        this.mode = mode;
        return this;
    }

    public boolean isPhysical() {
        return physical;
    }

    public CoverWirelessMaintenanceDetector setPhysical(boolean physical) {
        this.physical = physical;
        return this;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        super.readDataFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        mode = MaintenanceMode.values()[tag.getInteger("mode")];
        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        super.readDataFromPacket(byteData);
        mode = MaintenanceMode.values()[byteData.readInt()];
        physical = byteData.readBoolean();
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = (NBTTagCompound) super.saveDataToNbt();
        tag.setInteger("mode", mode.ordinal());
        tag.setBoolean("physical", physical);

        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        super.writeDataToByteBuf(byteBuf);
        byteBuf.writeInt(mode.ordinal());
        byteBuf.writeBoolean(physical);
    }

    private byte computeSignalBasedOnMaintenance(ICoverable tileEntity) {
        boolean signal = false;

        if (tileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity metaTE = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
            if (metaTE instanceof MTEMultiBlockBase multiTE) {
                int ideal = multiTE.getIdealStatus();
                int real = multiTE.getRepairStatus();

                switch (mode) {
                    case NO_ISSUE -> signal = ideal == real;
                    case ONE_ISSUE, TWO_ISSUES, THREE_ISSUES, FOUR_ISSUES, FIVE_ISSUES -> signal = ideal - real
                        >= mode.ordinal();
                    case ROTOR_80, ROTOR_100 -> {
                        ItemStack rotor = multiTE.getRealInventory()[1];
                        if (CoverNeedMaintainance.isRotor(rotor)) {
                            long max = MetaGeneratedTool.getToolMaxDamage(rotor);
                            long current = MetaGeneratedTool.getToolDamage(rotor);

                            if (mode == MaintenanceMode.ROTOR_80) {
                                signal = current >= max * 8 / 10;
                            } else {
                                long expectedDamage = Math.round(
                                    Math.min(
                                        (double) multiTE.mEUt / multiTE.damageFactorLow,
                                        Math.pow(multiTE.mEUt, multiTE.damageFactorHigh)));
                                signal = current + expectedDamage * 2 >= max;
                            }
                        } else {
                            signal = true;
                        }
                    }
                }
            }
        }

        if (invert) {
            signal = !signal;
        }

        return (byte) (signal ? 15 : 0);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        final byte signal = computeSignalBasedOnMaintenance(coverable);
        final CoverPosition key = getCoverKey(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), key, signal);

        if (physical) {
            coverable.setOutputRedstoneSignal(coverSide, signal);
        } else {
            coverable.setOutputRedstoneSignal(coverSide, (byte) 0);
        }
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    @Override
    public int getMinimumTickRate() {
        return 60;
    }

    public enum MaintenanceMode {

        NO_ISSUE("GT5U.gui.text.wireless_maintenance_detector.extra.no_issues"),
        ONE_ISSUE("GT5U.gui.text.wireless_maintenance_detector.extra.ge1_issues"),
        TWO_ISSUES("GT5U.gui.text.wireless_maintenance_detector.extra.ge2_issues"),
        THREE_ISSUES("GT5U.gui.text.wireless_maintenance_detector.extra.ge3_issues"),
        FOUR_ISSUES("GT5U.gui.text.wireless_maintenance_detector.extra.ge4_issues"),
        FIVE_ISSUES("GT5U.gui.text.wireless_maintenance_detector.extra.ge5_issues"),
        ROTOR_80("GT5U.gui.text.wireless_maintenance_detector.extra.rotor_lt20"),
        ROTOR_100("GT5U.gui.text.wireless_maintenance_detector.extra.rotor_0");

        private final String descriptorKey;

        MaintenanceMode(String key) {
            this.descriptorKey = key;
        }

        public String getDescriptorKey() {
            return descriptorKey;
        }
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessMaintenanceDetectorUIFactory(buildContext).createWindow();
    }

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverWirelessMaintenenceDetectorGui(this);
    }
}
