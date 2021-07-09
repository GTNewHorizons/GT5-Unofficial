package gregtech.common.covers;

import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiFakeItemButton;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fluids.Fluid;

import java.util.Collections;
import java.util.List;

import static gregtech.api.util.GT_Utility.*;

public class GT_Cover_ItemFilter extends GT_CoverBehavior {

    private final boolean mExport;

    public GT_Cover_ItemFilter(boolean isExport){
        this.mExport = isExport;
    }

    @Override
    public boolean isRedstoneSensitive(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        return false;
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        TileEntity tTileEntity = aTileEntity.getTileEntityAtSide(aSide);
        Object fromEntity = mExport ? aTileEntity : tTileEntity,
                toEntity = !mExport ? aTileEntity : tTileEntity;
        byte fromSide = !mExport ? GT_Utility.getOppositeSide(aSide) : aSide,
                toSide = mExport ? GT_Utility.getOppositeSide(aSide) : aSide;

        int FilterId = aCoverVariable >>> 1;
        List<ItemStack> Filter = Collections.singletonList(intToStack(FilterId));

        boolean isWhiteList = (aCoverVariable & 1) != 0;

        moveMultipleItemStacks(fromEntity, toEntity, fromSide , toSide, Filter, isWhiteList, (byte) 64, (byte) 1, (byte) 64, (byte) 1,64);

        return aCoverVariable;
    }

    @Override
    public boolean onCoverRightclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ){
            ItemStack tStack = aPlayer.inventory.getCurrentItem();
            if (tStack != null){
                aCoverVariable = (stackToInt(tStack) << 1) + (aCoverVariable & 1);
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                GT_Utility.sendChatToPlayer(aPlayer, trans("301", "Item Filter: ") + tStack.getDisplayName());
            }
            else{
                aCoverVariable = aCoverVariable & 1;
                aTileEntity.setCoverDataAtSide(aSide, aCoverVariable);
                GT_Utility.sendChatToPlayer(aPlayer, trans("300", "Filter Cleared!"));
            }
            return true;
    }

    @Override
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int mode = aCoverVariable & 1;
        if (mode == 1) mode = 0;
        else mode = 1;
        if (mode == 1){
            GT_Utility.sendChatToPlayer(aPlayer, trans("124", "Blacklist Mode"));
        }
        else{
            GT_Utility.sendChatToPlayer(aPlayer, trans("125", "Whitelist Mode"));
        }
        aCoverVariable = (aCoverVariable & ~0x1) + mode;
        return aCoverVariable;
    }

    @Override
    public boolean letsRedstoneGoIn(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public boolean letsRedstoneGoOut(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
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
        return false;
    }

    @Override
    public boolean letsFluidOut(byte aSide, int aCoverID, int aCoverVariable, Fluid aFluid, ICoverable aTileEntity) {
        return false;
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
    public boolean alwaysLookConnected(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return true;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return 1;
    }

    /**
     * GUI Stuff
     */

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity)  {
        return new GT_Cover_ItemFilter.GUI(aSide, aCoverID, coverData, aTileEntity);
    }

    private class GUI extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private int coverVariable;
        private final GT_GuiFakeItemButton itemFilterButtons;

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public GUI(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, 176, 107, GT_Utility.intToStack(aCoverID));
            this.side = aSide;
            this.coverID = aCoverID;
            this.coverVariable = aCoverVariable;

            GT_GuiIconButton b;
            b = new GT_GuiIconButton(this, 0, startX + spaceX*0, startY+spaceY*0, GT_GuiIcon.WHITELIST).setTooltipText(trans("125","Whitelist Mode"));
            b = new GT_GuiIconButton(this, 1, startX + spaceX*1, startY+spaceY*0, GT_GuiIcon.BLACKLIST).setTooltipText(trans("124","Blacklist Mode"));

            itemFilterButtons = new GT_GuiFakeItemButton(this ,startX + spaceX*0, startY+spaceY*2, GT_GuiIcon.SLOT_GRAY);
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            this.fontRendererObj.drawString(trans("303", "Filter: "),    startX + spaceX*0, 3+startY+spaceY*1, 0xFF555555);
            this.fontRendererObj.drawString(trans("302", "Check Mode"),  startX + spaceX*2, 3+startY+spaceY*0, 0xFF555555);
        }

        @Override
        protected void onInitGui(int guiLeft, int guiTop, int gui_width, int gui_height) {
            updateButtons();
        }

        @Override
        public void buttonClicked(GuiButton btn){
            if (getClickable(btn.id)){
                coverVariable = getNewCoverVariable(btn.id);
                GT_Values.NW.sendToServer(new GT_Packet_TileEntityCover(side, coverID, coverVariable, tile));
            }
            updateButtons();
        }

        private void updateButtons(){
            GuiButton b;
            for (Object o : buttonList) {
                b = (GuiButton) o;
                b.enabled = getClickable(b.id);
            }
            ItemStack tItemStack = intToStack(coverVariable >>> 1);
            if (tItemStack != null){
                itemFilterButtons.setItem(tItemStack);
                return;
            }
            itemFilterButtons.setItem(null);
        }

        private int getNewCoverVariable(int id) {
            switch (id) {
                case 0:
                    return coverVariable & ~0x1;
                case 1:
                    return coverVariable | 0x1;
            }
            return coverVariable;
        }

        private boolean getClickable(int id) {
            switch (id) {
                case 0:
                    return (0x1 & coverVariable) != 0;
                case 1:
                    return (0x1 & coverVariable) == 0;
            }
            return false;
        }
    }
}
