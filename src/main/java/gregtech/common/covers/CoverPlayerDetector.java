package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject.LegacyCoverData;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverPlayerDetector extends CoverBehavior {

    private String placer = "";
    private int range = 8;

    public CoverPlayerDetector(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

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
        boolean playerDetected = false;

        if (coverable instanceof IGregTechTileEntity) {
            if (coverable.isUniversalEnergyStored(20)) {
                coverable.decreaseStoredEnergyUnits(20, true);
                range = 32;
            } else {
                range = 8;
            }
            placer = ((IGregTechTileEntity) coverable).getOwnerName();
        }
        for (Object tObject : coverable.getWorld().playerEntities) {
            if ((tObject instanceof EntityPlayerMP tEntity)) {
                int dist = Math.max(
                    1,
                    (int) tEntity.getDistance(
                        coverable.getXCoord() + 0.5D,
                        coverable.getYCoord() + 0.5D,
                        coverable.getZCoord() + 0.5D));
                if (dist < range) {
                    if (coverDataValue == 0) {
                        playerDetected = true;
                        break;
                    }
                    if (tEntity.getDisplayName()
                        .equalsIgnoreCase(placer)) {
                        if (coverDataValue == 1) {
                            playerDetected = true;
                            break;
                        }
                    } else if (coverDataValue == 2) {
                        playerDetected = true;
                        break;
                    }
                }
            }
        }

        coverable.setOutputRedstoneSignal(coverSide, (byte) (playerDetected ? 15 : 0));
        return LegacyCoverData.of(coverDataValue);
    }

    @Override
    public LegacyCoverData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {

        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 3;
        if (coverDataValue < 0) {
            coverDataValue = 2;
        }
        switch (coverDataValue) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("068.1", "Emit if any Player is close"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("069.1", "Emit if other Player is close"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("070", "Emit if you are close"));
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
    public int getTickRate() {
        return 20;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new PlayerDetectorUIFactory(buildContext).createWindow();
    }

    private class PlayerDetectorUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public PlayerDetectorUIFactory(CoverUIBuildContext buildContext) {
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
                        CoverPlayerDetector.this::createDataObject,
                        (index, coverData) -> index == convert(coverData),
                        (index, coverData) -> new LegacyCoverData(index))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("068.1", "Emit if any Player is close"))
                                    .setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("069.1", "Emit if other Player is close"))
                                    .setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.addTooltip(GTUtility.trans("070", "Emit if you are close"))
                                    .setPos(spaceX * 0, spaceY * 2))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("319", "Any player")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("320", "Other players")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("321", "Only owner")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(startX + spaceX * 1, 4 + startY + spaceY * 2));
        }
    }
}
