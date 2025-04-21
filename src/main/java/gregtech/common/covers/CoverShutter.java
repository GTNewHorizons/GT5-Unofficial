package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fluids.Fluid;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.covers.CoverContext;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;

public class CoverShutter extends CoverBehavior {

    public CoverShutter(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public ISerializableObject.LegacyCoverData onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY,
        float aZ) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return coverData;
        }
        int coverDataValue = coverData.get();
        coverDataValue = (coverDataValue + (aPlayer.isSneaking() ? -1 : 1)) % 4;
        if (coverDataValue < 0) {
            coverDataValue = 3;
        }
        switch (coverDataValue) {
            case 0 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("082", "Open if work enabled"));
            case 1 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("083", "Open if work disabled"));
            case 2 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("084", "Only Output allowed"));
            case 3 -> GTUtility.sendChatToPlayer(aPlayer, GTUtility.trans("085", "Only Input allowed"));
        }
        if (coverable instanceof BaseMetaPipeEntity bmpe) {
            bmpe.reloadLocks();
        }
        return ISerializableObject.LegacyCoverData.of(coverDataValue);
    }

    public boolean letsRedstoneGoIn() {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 3
            : !(coveredTile.get() instanceof IMachineProgress)
                || (((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0));
    }

    public boolean letsRedstoneGoOut() {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 2
            : !(coveredTile.get() instanceof IMachineProgress)
                || (((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0));
    }

    @Override
    public boolean letsEnergyIn() {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 3
            : !(coveredTile.get() instanceof IMachineProgress)
                || (((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0));
    }

    @Override
    public boolean letsEnergyOut() {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 2
            : !(coveredTile.get() instanceof IMachineProgress)
                || ((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0);
    }

    @Override
    public boolean letsFluidIn(Fluid aFluid) {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 3
            : !(coveredTile.get() instanceof IMachineProgress)
                || ((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0);
    }

    public boolean letsFluidOut(Fluid fluid) {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 2
            : !(coveredTile.get() instanceof IMachineProgress)
                || ((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0);
    }

    public boolean letsItemsIn(int slot) {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 3
            : !(coveredTile.get() instanceof IMachineProgress)
                || ((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0);
    }

    public boolean letsItemsOut(int slot) {
        int coverDataValue = coverData.get();
        return coverDataValue >= 2 ? coverDataValue == 2
            : !(coveredTile.get() instanceof IMachineProgress)
                || ((IMachineProgress) coveredTile).isAllowedToWork() == (coverDataValue % 2 == 0);
    }

    @Override
    public boolean alwaysLookConnected() {
        return true;
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new ShutterUIFactory(buildContext).createWindow();
    }

    private class ShutterUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public ShutterUIFactory(CoverUIBuildContext buildContext) {
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
                        CoverShutter.this::loadFromNbt,
                        (index, coverData) -> index == convert(coverData),
                        (index, coverData) -> new ISerializableObject.LegacyCoverData(index))
                            .addToggleButton(
                                0,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.setPos(spaceX * 0, spaceY * 0))
                            .addToggleButton(
                                1,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.setPos(spaceX * 0, spaceY * 1))
                            .addToggleButton(
                                2,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.setPos(spaceX * 0, spaceY * 2))
                            .addToggleButton(
                                3,
                                CoverDataFollowerToggleButtonWidget.ofCheck(),
                                widget -> widget.setPos(spaceX * 0, spaceY * 3))
                            .setPos(startX, startY))
                .widget(
                    new TextWidget(GTUtility.trans("082", "Open if work enabled"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("083", "Open if work disabled"))
                        .setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("084", "Only Output allowed")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("085", "Only Input allowed")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
        }
    }
}
