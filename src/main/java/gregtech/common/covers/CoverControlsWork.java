package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.covers.IControlsWorkCover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.CoverBehavior;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverControlsWork extends CoverBehavior implements IControlsWorkCover {

    public CoverControlsWork(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public int doCoverThings(ForgeDirection side, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (!makeSureOnlyOne(side, aTileEntity)) return 0;
        if (aTileEntity instanceof IMachineProgress machine) {
            if (aCoverVariable < 2) {
                if ((aInputRedstone > 0) == (aCoverVariable == 0)) {
                    if (!machine.isAllowedToWork()) machine.enableWorking();
                } else if (machine.isAllowedToWork()) machine.disableWorking();
                machine.setWorkDataValue(aInputRedstone);
            } else if (aCoverVariable == 2) {
                machine.disableWorking();
            } else {
                if (machine.wasShutdown() && machine.getLastShutDownReason()
                    .wasCritical()) {
                    machine.disableWorking();
                    if (!mPlayerNotified) {
                        EntityPlayer player = lastPlayer == null ? null : lastPlayer.get();
                        if (player != null) {
                            lastPlayer = null;
                            mPlayerNotified = true;
                            GTUtility.sendChatToPlayer(
                                player,
                                aTileEntity.getInventoryName() + "at "
                                    + String.format(
                                        "(%d,%d,%d)",
                                        aTileEntity.getXCoord(),
                                        aTileEntity.getYCoord(),
                                        aTileEntity.getZCoord())
                                    + " shut down.");
                        }
                    }
                    return 2;
                } else {
                    return 3 + doCoverThings(side, aInputRedstone, aCoverID, aCoverVariable - 3, aTileEntity, aTimer);
                }
            }
        }
        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable.get() != 2; // always off, so no redstone needed either
    }

    /**
     * Make sure there is only one GT_Cover_ControlsWork on the aTileEntity TODO this is a migration thing. Remove this
     * after 2.3.0 is released.
     *
     * @return true if the cover is the first (side) one
     **/
    private boolean makeSureOnlyOne(ForgeDirection side, ICoverable aTileEntity) {
        return IControlsWorkCover.makeSureOnlyOne(side, aTileEntity);
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection side, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection side, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        if ((aTileEntity instanceof IMachineProgress)) {
            ((IMachineProgress) aTileEntity).enableWorking();
            ((IMachineProgress) aTileEntity).setWorkDataValue((byte) 0);
        }
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 5;
        if (aCoverVariable < 0) {
            aCoverVariable = 2;
        }
        if (aCoverVariable == 0) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("003", "Enable with Signal"));
        }
        if (aCoverVariable == 1) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("004", "Disable with Signal"));
        }
        if (aCoverVariable == 2) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("005", "Disabled"));
        }
        if (aCoverVariable == 3) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("505", "Enable with Signal (Safe)"));
        }
        if (aCoverVariable == 4) {
            GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("506", "Disable with Signal (Safe)"));
        }
        // TODO: Set lastPlayer
        return aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        for (final ForgeDirection tSide : ForgeDirection.VALID_DIRECTIONS) {
            if (aTileEntity.getCoverBehaviorAtSideNew(tSide) instanceof IControlsWorkCover) {
                return false;
            }
        }
        return true;
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
                        CoverControlsWork.this,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
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
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, CoverControlsWork.this)
                        .addFollower(
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            coverData -> convert(coverData) > 2,
                            (coverData, state) -> new ISerializableObject.LegacyCoverData(
                                adjustCoverVariable(state, convert(coverData))),
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
