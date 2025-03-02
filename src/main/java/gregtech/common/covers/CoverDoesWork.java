package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.api.util.ISerializableObject.LegacyCoverData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverDoesWork extends CoverBehavior {

    private static final int FLAG_INVERTED = 0x1;
    private static final int FLAG_PROGRESS = 0x2;
    private static final int FLAG_ENABLED = 0x4;

    public CoverDoesWork(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public LegacyCoverData doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        if ((coverable instanceof IMachineProgress mProgress)) {
            boolean inverted = isFlagSet(coverDataValue, FLAG_INVERTED);
            int signal = 0;

            if (isFlagSet(coverDataValue, FLAG_ENABLED)) {
                signal = inverted == mProgress.isAllowedToWork() ? 0 : 15;
            } else if (isFlagSet(coverDataValue, FLAG_PROGRESS)) {
                signal = inverted == (mProgress.getMaxProgress() == 0) ? 0 : 15;
            } else {
                int tScale = mProgress.getMaxProgress() / 15;

                if (tScale > 0 && mProgress.hasThingsToDo()) {
                    signal = inverted ? (15 - mProgress.getProgress() / tScale) : (mProgress.getProgress() / tScale);
                } else {
                    signal = inverted ? 15 : 0;
                }
            }

            coverable.setOutputRedstoneSignal(coverSide, (byte) signal);
        } else {
            coverable.setOutputRedstoneSignal(coverSide, (byte) 0);
        }
        return coverData;
    }

    @Override
    public ISerializableObject.LegacyCoverData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 6;
        if (coverDataValue < 0) {
            coverDataValue = 5;
        }
        switch (coverDataValue) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("018", "Normal"));
            // Progress scaled
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("019", "Inverted"));
            // ^ inverted
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("020", "Ready to work"));
            // Not Running
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("021", "Not ready to work"));
            // Running
            case 4 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("028", "Machine Enabled"));
            // Enabled
            case 5 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("029", "Machine Disabled"));
            // Disabled
        }
        return LegacyCoverData.of(coverDataValue);
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
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new DoesWorkUIFactory(buildContext).createWindow();
    }

    private static boolean isFlagSet(int coverVariable, int flag) {
        return (coverVariable & flag) == flag;
    }

    private class DoesWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public DoesWorkUIFactory(CoverUIBuildContext buildContext) {
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
                        CoverDoesWork.this::loadFromNbt,
                        (id, coverData) -> isEnabled(id, convert(coverData)),
                        (id, coverData) -> new LegacyCoverData(getNewCoverVariable(id, convert(coverData))))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_PROGRESS)
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_CHECKMARK)
                                    .setPos(spaceX * 1, spaceY * 0))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofDisableable(),
                                widget -> widget.setStaticTexture(GTUITextures.OVERLAY_BUTTON_POWER_SWITCH_ON)
                                    .setPos(spaceX * 2, spaceY * 0))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofRedstone(),
                                widget -> widget.setPos(spaceX * 0, spaceY * 1))
                            .setPos(startX, startY))
                .widget(TextWidget.dynamicString(() -> {
                    int coverVariable = convert(getCoverData());

                    if (isFlagSet(coverVariable, FLAG_ENABLED)) {
                        return GTUtility.trans("271", "Machine enabled");
                    } else if (isFlagSet(coverVariable, FLAG_PROGRESS)) {
                        return GTUtility.trans("242", "Machine idle");
                    } else {
                        return GTUtility.trans("241", "Recipe progress");
                    }

                })
                    .setSynced(false)
                    .setDefaultColor(COLOR_TEXT_GRAY.get())
                    .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
                .widget(
                    TextWidget
                        .dynamicString(
                            () -> isFlagSet(convert(getCoverData()), FLAG_INVERTED)
                                ? GTUtility.trans("INVERTED", "Inverted")
                                : GTUtility.trans("NORMAL", "Normal"))
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            switch (id) {
                case 0 -> {
                    return (coverVariable & ~FLAG_ENABLED) & ~FLAG_PROGRESS;
                }
                case 1 -> {
                    return (coverVariable & ~FLAG_ENABLED) | FLAG_PROGRESS;
                }
                case 2 -> {
                    return (coverVariable & ~FLAG_PROGRESS) | FLAG_ENABLED;
                }
                case 3 -> {
                    if (isFlagSet(coverVariable, FLAG_INVERTED)) {
                        return coverVariable & ~FLAG_INVERTED;
                    } else {
                        return coverVariable | FLAG_INVERTED;
                    }
                }
            }
            return coverVariable;
        }

        private boolean isEnabled(int id, int coverVariable) {
            return switch (id) {
                case 0 -> !isFlagSet(coverVariable, FLAG_PROGRESS) && !isFlagSet(coverVariable, FLAG_ENABLED);
                case 1 -> isFlagSet(coverVariable, FLAG_PROGRESS);
                case 2 -> isFlagSet(coverVariable, FLAG_ENABLED);
                case 3 -> isFlagSet(coverVariable, FLAG_INVERTED);
                default -> true;
            };
        }
    }
}
