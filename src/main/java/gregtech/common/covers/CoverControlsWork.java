package gregtech.common.covers;

import java.lang.ref.WeakReference;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverControlsWork extends CoverBehavior {

    private enum State {
        ENABLE_WITH_SIGNAL,
        DISABLE_WITH_SIGNAL,
        DISABLED,
        ENABLE_WITH_SIGNAL_SAFE,
        DISABLE_WITH_SIGNAL_SAFE;
    }

    private boolean handledShutdown = false;
    protected WeakReference<EntityPlayer> lastPlayer = null;
    private boolean mPlayerNotified = false;

    public CoverControlsWork(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean onCoverShiftRightClick(EntityPlayer aPlayer) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && hasCoverGUI() && aPlayer instanceof EntityPlayerMP) {
            lastPlayer = new WeakReference<>(aPlayer);
            mPlayerNotified = false;
            GTUIInfos.openCoverUI(coverable, aPlayer, coverSide);
            return true;
        }
        return false;
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable instanceof IMachineProgress machine) {
            int coverDataValue = coverData.get();
            State state = coverDataValue < State.values().length ? State.values()[coverDataValue] : State.DISABLED;
            switch (state) {
                case ENABLE_WITH_SIGNAL, DISABLE_WITH_SIGNAL -> {
                    if ((aInputRedstone > 0) == (state == State.ENABLE_WITH_SIGNAL)) {
                        if (!machine.isAllowedToWork()) {
                            machine.enableWorking();
                            handledShutdown = false;
                        }
                    } else {
                        if (machine.isAllowedToWork()) machine.disableWorking();
                    }
                }
                case DISABLED -> {
                    if (machine.isAllowedToWork()) machine.disableWorking();
                }
                case ENABLE_WITH_SIGNAL_SAFE, DISABLE_WITH_SIGNAL_SAFE -> {
                    if (machine.wasShutdown() && machine.getLastShutDownReason()
                        .wasCritical() && !handledShutdown) {
                        if (!mPlayerNotified) {
                            EntityPlayer player = lastPlayer == null ? null : lastPlayer.get();
                            if (player != null) {
                                lastPlayer = null;
                                mPlayerNotified = true;
                                GTUtility.sendChatToPlayer(
                                    player,
                                    coverable.getInventoryName() + "at "
                                        + String.format(
                                            "(%d,%d,%d)",
                                            coverable.getXCoord(),
                                            coverable.getYCoord(),
                                            coverable.getZCoord())
                                        + " shut down.");
                            }
                        }
                        handledShutdown = true;
                        coverData.set(State.DISABLED.ordinal());
                    } else {
                        if ((aInputRedstone > 0) == (state == State.ENABLE_WITH_SIGNAL_SAFE)) {
                            if (!machine.isAllowedToWork()) {
                                machine.enableWorking();
                                handledShutdown = false;
                            }
                        } else {
                            if (machine.isAllowedToWork()) machine.disableWorking();
                        }
                    }
                }
            }
        }
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return coverData.get() != 2; // always off, so no redstone needed either
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
    public void onCoverRemoval() {
        if ((coveredTile.get() instanceof IMachineProgress machine)) {
            machine.enableWorking();
        }
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int newCoverData = (convert(coverData) + (aPlayer.isSneaking() ? -1 : 1)) % 5;
        if (newCoverData < 0) {
            newCoverData = 2;
        }
        if (newCoverData == 0) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("003", "Enable with Signal"));
        }
        if (newCoverData == 1) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("004", "Disable with Signal"));
        }
        if (newCoverData == 2) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("005", "Disabled"));
        }
        if (newCoverData == 3) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("505", "Enable with Signal (Safe)"));
        }
        if (newCoverData == 4) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("506", "Disable with Signal (Safe)"));
        }
        // TODO: Set lastPlayer
        coverData.set(newCoverData);
    }

    @Override
    public int getMinimumTickRate() {
        return 1;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ControlsWorkUIFactory(buildContext).createWindow();
    }

    private class ControlsWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ControlsWorkUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder
                .widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                        this::getCoverData,
                        this::setCoverData,
                        CoverControlsWork.this::loadFromNbt,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new LegacyCoverData(getNewCoverVariable(id, convert(coverData))))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_ON)
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                    .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CROSS)
                                    .setPos(spaceX * 0, spaceY * 2))
                            .setPos(startX, startY))
                .widget(
                    new CoverDataControllerWidget<>(
                        this::getCoverData,
                        this::setCoverData,
                        CoverControlsWork.this::loadFromNbt).addFollower(
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            coverData -> convert(coverData) > 2,
                            (coverData, state) -> new LegacyCoverData(adjustCoverVariable(state, convert(coverData))),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("243", "Enable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("244", "Disable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("245", "Disable machine")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("507", "Safe Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            if (coverVariable > 2) {
                return id + 3;
            } else {
                return id;
            }
        }

        private boolean getClickable(int id, int coverVariable) {
            return ((id != coverVariable && id != coverVariable - 3) || id == 3);
        }

        private int adjustCoverVariable(boolean safeMode, int coverVariable) {
            if (safeMode && coverVariable <= 2) {
                coverVariable += 3;
            }
            if (!safeMode && coverVariable > 2) {
                coverVariable -= 3;
            }
            return coverVariable;
        }
    }
}
