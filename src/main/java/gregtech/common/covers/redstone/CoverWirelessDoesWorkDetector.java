package gregtech.common.covers.redstone;

import java.util.UUID;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayerMP;
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
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverWirelessDoesWorkDetector
    extends CoverAdvancedRedstoneTransmitterBase<CoverWirelessDoesWorkDetector.ActivityTransmitterData> {

    public CoverWirelessDoesWorkDetector(ITexture coverTexture) {
        super(ActivityTransmitterData.class, coverTexture);
    }

    @Override
    public ActivityTransmitterData createDataObject() {
        return new ActivityTransmitterData();
    }

    @Override
    public ActivityTransmitterData createDataObject(int aLegacyData) {
        return createDataObject();
    }

    private static byte computeSignalBasedOnActivity(ActivityTransmitterData coverVariable, ICoverable tileEntity) {

        if (tileEntity instanceof IMachineProgress mProgress) {
            boolean inverted = coverVariable.invert;
            int signal = 0;

            switch (coverVariable.mode) {
                case MACHINE_ENABLED -> signal = inverted == mProgress.isAllowedToWork() ? 0 : 15;
                case MACHINE_IDLE -> signal = inverted == (mProgress.getMaxProgress() == 0) ? 0 : 15;
                case RECIPE_PROGRESS -> {
                    int tScale = mProgress.getMaxProgress() / 15;

                    if (tScale > 0 && mProgress.hasThingsToDo()) {
                        signal = inverted ? (15 - mProgress.getProgress() / tScale)
                            : (mProgress.getProgress() / tScale);
                    } else {
                        signal = inverted ? 15 : 0;
                    }
                }
            }

            return (byte) signal;
        } else {
            return (byte) 0;
        }

    }

    @Override
    public ActivityTransmitterData doCoverThingsImpl(ForgeDirection side, byte aInputRedstone, int aCoverID,
        ActivityTransmitterData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        final byte signal = computeSignalBasedOnActivity(aCoverVariable, aTileEntity);
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
    public boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, ActivityTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected boolean manipulatesSidedRedstoneOutputImpl(ForgeDirection side, int aCoverID,
        ActivityTransmitterData aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRateImpl(ForgeDirection side, int aCoverID, ActivityTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return 1;
    }

    @Override
    protected int getDefaultTickRateImpl(ForgeDirection side, int aCoverID, ActivityTransmitterData aCoverVariable,
        ICoverable aTileEntity) {
        return 5;
    }

    public enum ActivityMode {
        RECIPE_PROGRESS,
        MACHINE_IDLE,
        MACHINE_ENABLED,
    }

    public static class ActivityTransmitterData extends CoverAdvancedRedstoneTransmitterBase.TransmitterData {

        private ActivityMode mode;
        /** Whether the wireless detector cover also sets the tiles sided Redstone output */
        private boolean physical;

        public ActivityTransmitterData(int frequency, UUID uuid, boolean invert, ActivityMode mode, boolean physical) {
            super(frequency, uuid, invert);
            this.mode = mode;
            this.physical = physical;
        }

        public ActivityTransmitterData() {
            super();
            this.mode = ActivityMode.MACHINE_IDLE;
            this.physical = true;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new ActivityTransmitterData(frequency, uuid, invert, mode, physical);
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
            mode = ActivityMode.values()[tag.getInteger("mode")];
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
            mode = ActivityMode.values()[aBuf.readInt()];
            physical = aBuf.readBoolean();

            return this;
        }
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessActivityDetectorUIFactory(buildContext).createWindow();
    }

    private class WirelessActivityDetectorUIFactory extends AdvancedRedstoneTransmitterBaseUIFactory {

        public WirelessActivityDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 123;
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
            builder.widget(TextWidget.dynamicString(() -> {

                ActivityMode mode = getCoverData().mode;

                if (mode == ActivityMode.MACHINE_ENABLED) {
                    return GTUtility.trans("271", "Machine enabled");
                } else if (mode == ActivityMode.MACHINE_IDLE) {
                    return GTUtility.trans("242", "Machine idle");
                } else {
                    return GTUtility.trans("241", "Recipe progress");
                }

            })
                .setSynced(false)
                .setDefaultColor(COLOR_TEXT_GRAY.get())
                .setPos(startX + spaceX * 3, 4 + startY + spaceY * 2))
                .widget(TextWidget.dynamicString(() -> {
                    ActivityTransmitterData coverData = getCoverData();
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
                    .setPos(startX + spaceX, 4 + startY + spaceY * 3)
                    .setSize(spaceX * 10, 12));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<ActivityTransmitterData> controller) {
            super.addUIForDataController(controller);

            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                coverData -> coverData.mode == ActivityMode.RECIPE_PROGRESS,
                (coverData, state) -> {
                    coverData.mode = ActivityMode.RECIPE_PROGRESS;
                    return coverData;
                },
                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_PROGRESS)
                    .addTooltip(GTUtility.trans("241", "Recipe progress"))
                    .setPos(spaceX * 0, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == ActivityMode.MACHINE_IDLE,
                    (coverData, state) -> {
                        coverData.mode = ActivityMode.MACHINE_IDLE;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                        .addTooltip(GTUtility.trans("242", "Machine idle"))
                        .setPos(spaceX * 1, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.mode == ActivityMode.MACHINE_ENABLED,
                    (coverData, state) -> {
                        coverData.mode = ActivityMode.MACHINE_ENABLED;
                        return coverData;
                    },
                    widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                        .addTooltip(GTUtility.trans("271", "Machine enabled"))
                        .setPos(spaceX * 2, spaceY * 2))
                .addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.physical,
                    (coverData, state) -> {
                        coverData.physical = state;
                        return coverData;
                    },
                    widget -> widget
                        .addTooltip(StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.tooltip"))
                        .setPos(0, 1 + spaceY * 3));
        }
    }

}
