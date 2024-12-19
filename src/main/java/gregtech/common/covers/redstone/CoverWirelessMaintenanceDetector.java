package gregtech.common.covers.redstone;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.util.ISerializableObject;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverWirelessMaintenanceDetector
    extends CoverAdvancedRedstoneTransmitterBase<CoverWirelessMaintenanceDetector.MaintenanceTransmitterData> {

    public CoverWirelessMaintenanceDetector(ITexture coverTexture) {
        super(MaintenanceTransmitterData.class, coverTexture);
    }

    @Override
    public MaintenanceTransmitterData createDataObject() {
        return new MaintenanceTransmitterData();
    }

    @Override
    public MaintenanceTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    private static byte computeSignalBasedOnMaintenance(MaintenanceTransmitterData coverVariable,
        ICoverable tileEntity) {
        boolean signal = false;

        if (tileEntity instanceof IGregTechTileEntity) {
            IMetaTileEntity metaTE = ((IGregTechTileEntity) tileEntity).getMetaTileEntity();
            if (metaTE instanceof MTEMultiBlockBase multiTE) {
                int ideal = multiTE.getIdealStatus();
                int real = multiTE.getRepairStatus();

                switch (coverVariable.mode) {
                    case NO_ISSUE -> signal = ideal == real;
                    case ONE_ISSUE, TWO_ISSUES, THREE_ISSUES, FOUR_ISSUES, FIVE_ISSUES -> signal = ideal - real
                        >= coverVariable.mode.ordinal();
                    case ROTOR_80, ROTOR_100 -> {
                        ItemStack rotor = multiTE.getRealInventory()[1];
                        if (CoverNeedMaintainance.isRotor(rotor)) {
                            long max = MetaGeneratedTool.getToolMaxDamage(rotor);
                            long current = MetaGeneratedTool.getToolDamage(rotor);

                            if (coverVariable.mode == MaintenanceMode.ROTOR_80) {
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

        if (coverVariable.invert) {
            signal = !signal;
        }

        return (byte) (signal ? 15 : 0);
    }

    @Override
    public MaintenanceTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final byte signal = computeSignalBasedOnMaintenance(aCoverVariable, aTileEntity);
        final long hash = hashCoverCoords(aTileEntity, side);
        setSignalAt(aCoverVariable.getUuid(), aCoverVariable.getFrequency(), hash, signal);

        if (aCoverVariable.physical) {
            aTileEntity.setOutputRedstoneSignal(side, signal);
        } else {
            aTileEntity.setOutputRedstoneSignal(side, (byte) 0);
        }

        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, MaintenanceTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        MaintenanceTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRateImpl(ForgeDirection side, int aCoverID, MaintenanceTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return 60;
    }

    public enum MaintenanceMode {
        NO_ISSUE,
        ONE_ISSUE,
        TWO_ISSUES,
        THREE_ISSUES,
        FOUR_ISSUES,
        FIVE_ISSUES,
        ROTOR_80,
        ROTOR_100
    }

    public static class MaintenanceTransmitterData extends CoverAdvancedRedstoneTransmitterBase.TransmitterData {

        private MaintenanceMode mode;
        /** Whether the wireless detector cover also sets the tiles sided Redstone output */
        private boolean physical;

        public MaintenanceTransmitterData(int frequency, UUID uuid, boolean invert, MaintenanceMode mode,
            boolean physical) {
            super(frequency, uuid, invert);
            this.mode = mode;
            this.physical = physical;
        }

        public MaintenanceTransmitterData() {
            super();
            this.mode = MaintenanceMode.ONE_ISSUE;
            this.physical = true;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new MaintenanceTransmitterData(frequency, uuid, invert, mode, physical);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            NBTTagCompound tag = (NBTTagCompound) super.saveDataToNBT();
            tag.setInteger("mode", mode.ordinal());
            tag.setBoolean("physical", physical);

            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            super.writeToByteBuf(aBuf);
            aBuf.writeInt(mode.ordinal());
            aBuf.writeBoolean(physical);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            super.loadDataFromNBT(aNBT);

            NBTTagCompound tag = (NBTTagCompound) aNBT;
            mode = MaintenanceMode.values()[tag.getInteger("mode")];
            if (tag.hasKey("physical")) {
                physical = tag.getBoolean("physical");
            } else {
                physical = false;
            }
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            super.readFromPacket(aBuf, aPlayer);
            mode = MaintenanceMode.values()[aBuf.readInt()];
            physical = aBuf.readBoolean();

            return this;
        }
    }

    // GUI stuff

    private static final String[] extraTexts = new String[] { "No Issues", ">= 1 Issue", ">= 2 Issues", ">= 3 Issues",
        ">= 4 Issues", ">= 5 Issues", "Rotor < 20%", "Rotor ≈ 0%" };

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessMaintenanceDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessMaintenanceDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        public WirelessMaintenanceDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 175;
        }

        @Override
        protected int getFrequencyRow() {
            return 0;
        }

        @Override
        protected int getButtonRow() {
            return 1;
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            for (int i = 0; i < 8; i++) {
                builder.widget(
                    new TextWidget(extraTexts[i]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * (i % 2 == 0 ? 1 : 7), 4 + startY + spaceY * (2 + i / 2)));
            }
            builder.widget(TextWidget.dynamicString(() -> {
                MaintenanceTransmitterData coverData = getCoverData();
                if (coverData != null) {
                    return getCoverData().physical
                        ? StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.1")
                        : StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.0");
                } else {
                    return "";
                }
            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_GRAY.get())
                .setTextAlignment(Alignment.CenterLeft)
                .setPos(startX + spaceX, 4 + startY + spaceY * 6)
                .setSize(spaceX * 10, 12));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<MaintenanceTransmitterData> controller) {
            super.addUIForDataController(controller);
            for (int i = 0; i < 8; i++) {
                final int index = i;
                controller.addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == MaintenanceMode.values()[index],
                    (coverData, state) -> {
                        coverData.mode = MaintenanceMode.values()[index];
                        return coverData;
                    },
                    widget -> widget.setToggleTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK, GTUITextures.TRANSPARENT)
                        .setPos(spaceX * (index % 2 == 0 ? 0 : 6), spaceY * (2 + index / 2)));
            }
            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.physical,
                (coverData, state) -> {
                    coverData.physical = state;
                    return coverData;
                },
                widget -> widget
                    .addTooltip(StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.tooltip"))
                    .setPos(0, 1 + spaceY * 6));
        }
    }
}
