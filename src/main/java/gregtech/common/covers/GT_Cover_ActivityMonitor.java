package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;

public class GT_Cover_ActivityMonitor extends GT_CoverBehavior {

    public static final int aliveWidth = 13;
    public static final int aliveOffset = 32 - aliveWidth;
    public static final int activeWidth = 13;
    public static final int activeOffset = aliveOffset - activeWidth;
    public static final int recipeWidth = 32 - (aliveWidth + activeWidth);
    public static final int recipeOffset = 0;

    public GT_Cover_ActivityMonitor() {
        this(null);
    }

    public GT_Cover_ActivityMonitor(ITexture coverTexture) {
        super(coverTexture);
    }

    private static int getTicksAlive(int aCoverVariable) {
        return (aCoverVariable & 0xFFF80000) >> aliveOffset;
    }

    private static int getTicksActive(int aCoverVariable) {
        return (aCoverVariable & 0x0007FFC0) >> activeOffset;
    }

    private static int getRecipeCount(int aCoverVariable) {
        return (aCoverVariable & 0x0000003F) >> recipeOffset;
    }

    @Override
    public int doCoverThings(
            byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if (aTileEntity instanceof IMachineProgress) {
            aCoverVariable += 1 << aliveOffset;
            IMachineProgress machine = (IMachineProgress) aTileEntity;
            if (machine.isActive()) {
                aCoverVariable += 1 << activeOffset;
                if (machine.getProgress() >= machine.getMaxProgress()) {
                    aCoverVariable += 1 << recipeOffset;
                }
            }
        }
        return aCoverVariable;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity) {
        return new GT_Cover_ActivityMonitor.GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;
        private final byte side;
        private final int coverID;
        private final int textColor = this.getTextColorOrDefault("text", 0xFF555555);
        private final int coverVariable;

        private final ICoverable tileEntity;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {

            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;
            this.tileEntity = aTileEntity;

            new GT_GuiIconButton(this, 0, startX, startY + spaceY * 3, GT_GuiIcon.CROSS);
        }

        private int getCurrentCoverVariable() {
            GT_CoverBehavior behavior = (GT_CoverBehavior) tile.getCoverBehaviorAtSideNew(side);
            if (behavior instanceof GT_Cover_ActivityMonitor) {
                GT_Cover_ActivityMonitor monitor = (GT_Cover_ActivityMonitor) behavior;
                return monitor.doCoverThings(side, (byte) 0, coverID, coverVariable, tile, 0);
            }
            return 0;
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {}

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            renderTime();
            renderRecipesCompleted();
            renderRecipesPerMinute();
            renderButtonText();
        }

        private void renderTime() {
            int ticksAlive = getTicksAlive(getCurrentCoverVariable());
            int ticksActive = getTicksActive(getCurrentCoverVariable());
            double uptimePercentage = ticksActive / (double) ticksAlive * 100.0;
            this.fontRendererObj.drawString(
                    String.format("Active/Alive: %d/%d (%.2f %%)", ticksActive, ticksAlive, uptimePercentage),
                    3 + startX,
                    4 + startY,
                    textColor);
        }

        private void renderRecipesCompleted() {
            this.fontRendererObj.drawString(
                    String.format("Recipes Completed: %d", getRecipeCount(getCurrentCoverVariable())),
                    3 + startX,
                    4 + startY + spaceY,
                    textColor);
        }

        private void renderRecipesPerMinute() {
            this.fontRendererObj.drawString(
                    String.format(
                            "Recipes per Minute: %.2f",
                            getRecipeCount(getCurrentCoverVariable())
                                    / (double) getTicksAlive(getCurrentCoverVariable())
                                    * 1200),
                    3 + startX,
                    4 + startY + spaceY * 2,
                    textColor);
        }

        private void renderButtonText() {
            this.fontRendererObj.drawString("Reset", 3 + startX + spaceX * 1, 4 + startY + spaceY * 3, textColor);
        }

        @Override
        public void buttonClicked(GuiButton button) {
            GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, 0, tile));
        }
    }
}
