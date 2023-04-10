package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_DoesWork extends GT_CoverBehavior {

    /**
     * @deprecated use {@link #GT_Cover_DoesWork(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_DoesWork() {
        this(null);
    }

    public GT_Cover_DoesWork(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        if ((aTileEntity instanceof IMachineProgress)) {
            if (aCoverVariable < 2) {
                int tScale = ((IMachineProgress) aTileEntity).getMaxProgress() / 15;
                if ((tScale > 0) && (((IMachineProgress) aTileEntity).hasThingsToDo())) {
                    aTileEntity.setOutputRedstoneSignal(
                        aSide,
                        aCoverVariable % 2 == 0 ? (byte) (((IMachineProgress) aTileEntity).getProgress() / tScale)
                            : (byte) (15 - ((IMachineProgress) aTileEntity).getProgress() / tScale));
                } else {
                    aTileEntity.setOutputRedstoneSignal(aSide, (byte) (aCoverVariable % 2 == 0 ? 0 : 15));
                }
            } else {
                aTileEntity.setOutputRedstoneSignal(
                    aSide,
                    (byte) ((aCoverVariable % 2 == 0 ? 1 : 0)
                        != (((IMachineProgress) aTileEntity).getMaxProgress() == 0 ? 1 : 0) ? 0 : 15));
            }
        } else {
            aTileEntity.setOutputRedstoneSignal(aSide, (byte) 0);
        }
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 4;
        if (aCoverVariable < 0) {
            aCoverVariable = 3;
        }
        switch (aCoverVariable) {
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("018", "Normal"));
            // Progress scaled
            case 1 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("019", "Inverted"));
            // ^ inverted
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("020", "Ready to work"));
            // Not Running
            case 3 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("021", "Not ready to work"));
            // Running
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput(byte aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 5;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new DoesWorkUIFactory(buildContext).createWindow();
    }

    private class DoesWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public DoesWorkUIFactory(GT_CoverUIBuildContext buildContext) {
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
                        GT_Cover_DoesWork.this,
                        (id, coverData) -> isEnabled(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_PROGRESS)
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CHECKMARK)
                                        .setPos(spaceX * 1, spaceY * 0))
                                .addToggleButton(
                                    2,
                                    CoverDataFollower_ToggleButtonWidget.ofRedstone(),
                                    widget -> widget.setPos(spaceX * 0, spaceY * 1))
                                .setPos(startX, startY))
                .widget(
                    TextWidget
                        .dynamicString(
                            () -> ((convert(getCoverData()) & 0x2) > 0) ? GT_Utility.trans("242", "Machine idle")
                                : GT_Utility.trans("241", "Recipe progress"))
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 0))
                .widget(
                    TextWidget
                        .dynamicString(
                            () -> ((convert(getCoverData()) & 0x1) > 0) ? GT_Utility.trans("INVERTED", "Inverted")
                                : GT_Utility.trans("NORMAL", "Normal"))
                        .setSynced(false)
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 3, 4 + startY + spaceY * 1));
        }

        private int getNewCoverVariable(int id, int coverVariable) {
            switch (id) {
                case 0 -> {
                    return coverVariable & ~0x2;
                }
                case 1 -> {
                    return coverVariable | 0x2;
                }
                case 2 -> {
                    if ((coverVariable & 0x1) > 0) return coverVariable & ~0x1;
                    return coverVariable | 0x1;
                }
            }
            return coverVariable;
        }

        private boolean isEnabled(int id, int coverVariable) {
            return switch (id) {
                case 0 -> (coverVariable & 0x2) == 0;
                case 1 -> (coverVariable & 0x2) > 0;
                case 2 -> (coverVariable & 0x1) > 0;
                default -> true;
            };
        }
    }
}
