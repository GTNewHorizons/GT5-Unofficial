package gregtech.common.covers;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.covers.IControlsWorkCover;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_ControlsWork extends GT_CoverBehavior implements IControlsWorkCover {

    /**
     * @deprecated use {@link #GT_Cover_ControlsWork(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_ControlsWork() {
        this(null);
    }

    public GT_Cover_ControlsWork(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public int doCoverThings(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        if (!makeSureOnlyOne(aSide, aTileEntity)) return 0;
        if (aTileEntity instanceof IMachineProgress machine) {
            if (aCoverVariable < 2) {
                if ((aInputRedstone > 0) == (aCoverVariable == 0)) {
                    if (!machine.isAllowedToWork()) machine.enableWorking();
                } else if (machine.isAllowedToWork()) machine.disableWorking();
                machine.setWorkDataValue(aInputRedstone);
            } else if (aCoverVariable == 2) {
                machine.disableWorking();
            } else {
                if (machine.wasShutdown()) {
                    machine.disableWorking();
                    if (!mPlayerNotified) {
                        mPlayerNotified = true;
                        GT_Utility.sendChatToPlayer(
                            lastPlayer,
                            aTileEntity.getInventoryName() + "at "
                                + String.format(
                                    "(%d,%d,%d)",
                                    aTileEntity.getXCoord(),
                                    aTileEntity.getYCoord(),
                                    aTileEntity.getZCoord())
                                + " shut down.");
                    }
                    return 2;
                } else {
                    return 3 + doCoverThings(aSide, aInputRedstone, aCoverID, aCoverVariable - 3, aTileEntity, aTimer);
                }
            }
        }
        return aCoverVariable;
    }

    @Override
    protected boolean isRedstoneSensitiveImpl(ForgeDirection aSide, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return aCoverVariable.get() != 2; // always off, so no redstone needed either
    }

    /**
     * Make sure there is only one GT_Cover_ControlsWork on the aTileEntity TODO this is a migration thing. Remove this
     * after 2.3.0 is released.
     *
     * @return true if the cover is the first (side) one
     **/
    private boolean makeSureOnlyOne(ForgeDirection aSide, ICoverable aTileEntity) {
        return IControlsWorkCover.makeSureOnlyOne(aSide, aTileEntity);
    }

    @Override
    public boolean letsEnergyIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsEnergyOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsFluidOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsIn(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsItemsOut(ForgeDirection aSide, int aCoverID, int aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean onCoverRemoval(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        boolean aForced) {
        if ((aTileEntity instanceof IMachineProgress)) {
            ((IMachineProgress) aTileEntity).enableWorking();
            ((IMachineProgress) aTileEntity).setWorkDataValue((byte) 0);
        }
        return true;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 5;
        if (aCoverVariable < 0) {
            aCoverVariable = 2;
        }
        if (aCoverVariable == 0) {
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("003", "Enable with Signal"));
        }
        if (aCoverVariable == 1) {
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("004", "Disable with Signal"));
        }
        if (aCoverVariable == 2) {
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("005", "Disabled"));
        }
        if (aCoverVariable == 3) {
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("505", "Enable with Signal (Safe)"));
        }
        if (aCoverVariable == 4) {
            GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("506", "Disable with Signal (Safe)"));
        }
        // TODO: Set lastPlayer
        return aCoverVariable;
    }

    @Override
    public int getTickRate(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection aSide, ItemStack aStack, ICoverable aTileEntity) {
        if (!super.isCoverPlaceable(aSide, aStack, aTileEntity)) return false;
        for (byte tSide : ALL_VALID_SIDES) {
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
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new ControlsWorkUIFactory(buildContext).createWindow();
    }

    private class ControlsWorkUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ControlsWorkUIFactory(GT_CoverUIBuildContext buildContext) {
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
                        GT_Cover_ControlsWork.this,
                        (id, coverData) -> !getClickable(id, convert(coverData)),
                        (id, coverData) -> new ISerializableObject.LegacyCoverData(
                            getNewCoverVariable(id, convert(coverData))))
                                .addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_ON)
                                        .setPos(spaceX * 0, spaceY * 0))
                                .addToggleButton(
                                    1,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_REDSTONE_OFF)
                                        .setPos(spaceX * 0, spaceY * 1))
                                .addToggleButton(
                                    2,
                                    CoverDataFollower_ToggleButtonWidget.ofDisableable(),
                                    widget -> widget.setStaticTexture(GT_UITextures.OVERLAY_BUTTON_CROSS)
                                        .setPos(spaceX * 0, spaceY * 2))
                                .setPos(startX, startY))
                .widget(
                    new CoverDataControllerWidget<>(this::getCoverData, this::setCoverData, GT_Cover_ControlsWork.this)
                        .addFollower(
                            CoverDataFollower_ToggleButtonWidget.ofCheckAndCross(),
                            coverData -> convert(coverData) > 2,
                            (coverData, state) -> new ISerializableObject.LegacyCoverData(
                                adjustCoverVariable(state, convert(coverData))),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                        .setPos(startX, startY))
                .widget(
                    new TextWidget(GT_Utility.trans("243", "Enable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GT_Utility.trans("244", "Disable with Redstone"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GT_Utility.trans("245", "Disable machine")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GT_Utility.trans("507", "Safe Mode")).setDefaultColor(COLOR_TEXT_GRAY.get())
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
