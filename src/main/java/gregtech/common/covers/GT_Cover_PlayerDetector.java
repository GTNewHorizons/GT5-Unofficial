package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;

public class GT_Cover_PlayerDetector extends GT_CoverBehavior {

    private String placer = "";
    private int range = 8;

    /**
     * @deprecated use {@link #GT_Cover_PlayerDetector(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_PlayerDetector() {
        this(null);
    }

    public GT_Cover_PlayerDetector(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(ForgeDirection aSide, byte aInputRedstone, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity, long aTimer) {
        boolean playerDetected = false;

        if (aTileEntity instanceof IGregTechTileEntity) {
            if (aTileEntity.isUniversalEnergyStored(20)) {
                aTileEntity.decreaseStoredEnergyUnits(20, true);
                range = 32;
            } else {
                range = 8;
            }
            placer = ((IGregTechTileEntity) aTileEntity).getOwnerName();
        }
        for (Object tObject : aTileEntity.getWorld().playerEntities) {
            if ((tObject instanceof EntityPlayerMP tEntity)) {
                int dist = Math.max(
                    1,
                    (int) tEntity.getDistance(
                        aTileEntity.getXCoord() + 0.5D,
                        aTileEntity.getYCoord() + 0.5D,
                        aTileEntity.getZCoord() + 0.5D));
                if (dist < range) {
                    if (aCoverVariable == 0) {
                        playerDetected = true;
                        break;
                    }
                    if (tEntity.getDisplayName()
                        .equalsIgnoreCase(placer)) {
                        if (aCoverVariable == 1) {
                            playerDetected = true;
                            break;
                        }
                    } else if (aCoverVariable == 2) {
                        playerDetected = true;
                        break;
                    }
                }
            }
        }

        aTileEntity.setOutputRedstoneSignal(aSide, (byte) (playerDetected ? 15 : 0));
        return aCoverVariable;
    }

    @Override
    public int onCoverScrewdriverclick(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable = (aCoverVariable + (aPlayer.isSneaking() ? -1 : 1)) % 3;
        if (aCoverVariable < 0) {
            aCoverVariable = 2;
        }
        switch (aCoverVariable) {
            case 0 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("068.1", "Emit if any Player is close"));
            case 1 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("069.1", "Emit if other Player is close"));
            case 2 -> GT_Utility.sendChatToPlayer(aPlayer, GT_Utility.trans("070", "Emit if you are close"));
        }
        return aCoverVariable;
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
    public boolean manipulatesSidedRedstoneOutput(ForgeDirection aSide, int aCoverID, int aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(ForgeDirection aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 20;
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
        return new PlayerDetectorUIFactory(buildContext).createWindow();
    }

    private class PlayerDetectorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public PlayerDetectorUIFactory(GT_CoverUIBuildContext buildContext) {
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
                        GT_Cover_PlayerDetector.this,
                        (index, coverData) -> index == convert(coverData),
                        (index, coverData) -> new ISerializableObject.LegacyCoverData(index))
                            .addToggleButton(
                                0,
                                CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GT_Utility.trans("068.1", "Emit if any Player is close"))
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GT_Utility.trans("069.1", "Emit if other Player is close"))
                                    .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                2,
                                CoverDataFollower_ToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GT_Utility.trans("070", "Emit if you are close"))
                                    .setPos(spaceX * 0, spaceY * 2))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GT_Utility.trans("319", "Any player")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GT_Utility.trans("320", "Other players")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GT_Utility.trans("321", "Only owner")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2));
        }
    }
}
