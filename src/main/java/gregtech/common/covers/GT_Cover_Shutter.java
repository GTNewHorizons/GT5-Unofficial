package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_Shutter extends GT_CoverBehavior {

    /**
     * @deprecated use {@link #GT_Cover_Shutter(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Shutter() {
        this(null);
    }

    public GT_Cover_Shutter(ITexture coverTexture) {
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
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, trans("082", "Open if work enabled"));
            case 1 -> GT_Utility.sendChatToPlayer(aPlayer, trans("083", "Open if work disabled"));
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, trans("084", "Only Output allowed"));
            case 3 -> GT_Utility.sendChatToPlayer(aPlayer, trans("085", "Only Input allowed"));
        }
        if (aTileEntity instanceof BaseMetaPipeEntity) {
            ((BaseMetaPipeEntity) aTileEntity).reloadLocks();
        }
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3
                : !(aTileEntity instanceof IMachineProgress)
                        || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2
                : !(aTileEntity instanceof IMachineProgress)
                        || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsEnergyIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3
                : !(aTileEntity instanceof IMachineProgress)
                        || (((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0));
    }

    @Override
    public boolean letsEnergyOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2
                : !(aTileEntity instanceof IMachineProgress)
                        || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsFluidIn(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3
                : !(aTileEntity instanceof IMachineProgress)
                        || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2
                : !(aTileEntity instanceof IMachineProgress)
                        || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsItemsIn(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 3
                : !(aTileEntity instanceof IMachineProgress)
                        || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean letsItemsOut(byte aSide, int aCoverID, int aCoverVariable, int aSlot, ICoverable aTileEntity) {
        return aCoverVariable >= 2 ? aCoverVariable == 2
                : !(aTileEntity instanceof IMachineProgress)
                        || ((IMachineProgress) aTileEntity).isAllowedToWork() == (aCoverVariable % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 0;
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
        return new ShutterUIFactory(buildContext).createWindow();
    }

    private class ShutterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ShutterUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                    new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                            this::getCoverData,
                            this::setCoverData,
                            GT_Cover_Shutter.this,
                            (index, coverData) -> index == convert(coverData),
                            (index, coverData) -> new ISerializableObject.LegacyCoverData(index)).addToggleButton(
                                    0,
                                    CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                    widget -> widget.setPos(spaceX * 0, spaceY * 0))
                                                                                                 .addToggleButton(
                                                                                                         1,
                                                                                                         CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                                                                                         widget -> widget.setPos(
                                                                                                                 spaceX * 0,
                                                                                                                 spaceY * 1))
                                                                                                 .addToggleButton(
                                                                                                         2,
                                                                                                         CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                                                                                         widget -> widget.setPos(
                                                                                                                 spaceX * 0,
                                                                                                                 spaceY * 2))
                                                                                                 .addToggleButton(
                                                                                                         3,
                                                                                                         CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                                                                                         widget -> widget.setPos(
                                                                                                                 spaceX * 0,
                                                                                                                 spaceY * 3))
                                                                                                 .setPos(
                                                                                                         startX,
                                                                                                         startY))
                   .widget(
                           new TextWidget(GT_Utility.trans("082", "Open if work enabled"))
                                                                                          .setDefaultColor(
                                                                                                  COLOR_TEXT_GRAY.get())
                                                                                          .setPos(
                                                                                                  3 + startX
                                                                                                          + spaceX * 1,
                                                                                                  4 + startY
                                                                                                          + spaceY * 0))
                   .widget(
                           new TextWidget(
                                   GT_Utility.trans("083", "Open if work disabled"))
                                                                                    .setDefaultColor(
                                                                                            COLOR_TEXT_GRAY.get())
                                                                                    .setPos(
                                                                                            3 + startX + spaceX * 1,
                                                                                            4 + startY + spaceY * 1))
                   .widget(
                           new TextWidget(GT_Utility.trans("084", "Only Output allowed"))
                                                                                         .setDefaultColor(
                                                                                                 COLOR_TEXT_GRAY.get())
                                                                                         .setPos(
                                                                                                 3 + startX
                                                                                                         + spaceX * 1,
                                                                                                 4 + startY
                                                                                                         + spaceY * 2))
                   .widget(
                           new TextWidget(GT_Utility.trans("085", "Only Input allowed"))
                                                                                        .setDefaultColor(
                                                                                                COLOR_TEXT_GRAY.get())
                                                                                        .setPos(
                                                                                                3 + startX + spaceX * 1,
                                                                                                4 + startY
                                                                                                        + spaceY * 3));
        }
    }
}
