package gregtech.common.covers.redstone;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.common.covers.Cover;
import gregtech.common.covers.CoverNeedMaintainance;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public class CoverWirelessMaintenanceDetector extends CoverAdvancedRedstoneTransmitterBase {

    private MaintenanceMode mode;
    /** Whether the wireless detector cover also sets the tiles sided Redstone output */
    private boolean physical;

    public CoverWirelessMaintenanceDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
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
    protected void initializeData() {
        super.initializeData();
        this.mode = MaintenanceMode.ONE_ISSUE;
        this.physical = true;
    }

    @Override
    protected void loadFromNbt(NBTBase nbt) {
        super.loadFromNbt(nbt);

        NBTTagCompound tag = (NBTTagCompound) nbt;
        mode = MaintenanceMode.values()[tag.getInteger("mode")];
        if (tag.hasKey("physical")) {
            physical = tag.getBoolean("physical");
        } else {
            physical = false;
        }
    }

    @Override
    protected void readFromPacket(ByteArrayDataInput byteData) {
        super.readFromPacket(byteData);
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
        super.writeToByteBuf(byteBuf);
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
        final long hash = hashCoverCoords(coverable, coverSide);
        setSignalAt(getUuid(), getFrequency(), hash, signal);

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
        NO_ISSUE,
        ONE_ISSUE,
        TWO_ISSUES,
        THREE_ISSUES,
        FOUR_ISSUES,
        FIVE_ISSUES,
        ROTOR_80,
        ROTOR_100
    }

    // GUI stuff

    private static final String[] extraTexts = new String[] { "No Issues", ">= 1 Issue", ">= 2 Issues", ">= 3 Issues",
        ">= 4 Issues", ">= 5 Issues", "Rotor < 20%", "Rotor ≈ 0%" };

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new WirelessMaintenanceDetectorUIFactory(buildContext).createWindow();
    }

    private static class WirelessMaintenanceDetectorUIFactory
        extends AdvancedRedstoneTransmitterBaseUIFactory<CoverWirelessMaintenanceDetector> {

        public WirelessMaintenanceDetectorUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @Override
        protected int getGUIHeight() {
            return 175;
        }

        @Override
        protected CoverWirelessMaintenanceDetector adaptCover(Cover cover) {
            if (cover instanceof CoverWirelessMaintenanceDetector adaptedCover) {
                return adaptedCover;
            }
            return null;
        }

        @Override
        protected @NotNull CoverDataControllerWidget<CoverWirelessMaintenanceDetector> getDataController() {
            return new CoverDataControllerWidget<>(this::getCover, getUIBuildContext());
        }

        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            super.addUIWidgets(builder);
            for (int i = 0; i < 8; i++) {
                builder.widget(
                    new TextWidget(extraTexts[i]).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * (i % 2 == 0 ? 1 : 7), 4 + startY + spaceY * (2 + i / 2)));
            }
            builder.widget(
                TextWidget
                    .dynamicString(
                        getCoverString(
                            c -> c.isPhysical() ? StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.1")
                                : StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.0")))
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setTextAlignment(Alignment.CenterLeft)
                    .setPos(startX + spaceX, 4 + startY + spaceY * 6)
                    .setSize(spaceX * 10, 12));
        }

        @Override
        protected void addUIForDataController(CoverDataControllerWidget<CoverWirelessMaintenanceDetector> controller) {
            super.addUIForDataController(controller);
            for (int i = 0; i < 8; i++) {
                final int index = i;
                controller.addFollower(
                    CoverDataFollowerToggleButtonWidget.ofDisableable(),
                    coverData -> coverData.getMode() == MaintenanceMode.values()[index],
                    (coverData, state) -> coverData.setMode(MaintenanceMode.values()[index]),
                    widget -> widget.setToggleTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK, GTUITextures.TRANSPARENT)
                        .setPos(spaceX * (index % 2 == 0 ? 0 : 6), spaceY * (2 + index / 2)));
            }
            controller.addFollower(
                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                CoverWirelessMaintenanceDetector::isPhysical,
                CoverWirelessMaintenanceDetector::setPhysical,
                widget -> widget
                    .addTooltip(StatCollector.translateToLocal("gt.cover.wirelessdetector.redstone.tooltip"))
                    .setPos(0, 1 + spaceY * 6));
        }
    }
}
