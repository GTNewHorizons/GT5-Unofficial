package com.github.technus.tectech.thing.cover;

import com.github.technus.tectech.loader.NetworkDispatcher;
import com.github.technus.tectech.mechanics.enderStorage.EnderLinkCoverMessage;
import com.github.technus.tectech.mechanics.enderStorage.EnderLinkTag;
import eu.usrv.yamcore.auxiliary.PlayerChatHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUICover;
import gregtech.api.gui.widgets.GT_GuiIcon;
import gregtech.api.gui.widgets.GT_GuiIconButton;
import gregtech.api.gui.widgets.GT_GuiIntegerTextBox;
import gregtech.api.interfaces.IGuiScreen;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.net.GT_Packet_TileEntityCover;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.GT_Utility;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import java.awt.*;
import java.util.UUID;

import static com.github.technus.tectech.mechanics.enderStorage.EnderWorldSavedData.*;

public class GT_Cover_TM_EnderFluidLink extends GT_CoverBehavior {
    private static final int L_PER_TICK = 8000;
    private final static int IMPORT_EXPORT_MASK = 0b0001;
    private final static int PUBLIC_PRIVATE_MASK = 0b0010;
    private static EnderLinkTag tag = new EnderLinkTag(Color.WHITE, null);//Client-Sided

    public GT_Cover_TM_EnderFluidLink() {
    }

    public static void setEnderLinkTag(EnderLinkTag inputTag) {
        if(inputTag != null) {
            tag = inputTag;
            //Hacky Way to update the gui
            GUI_INSTANCE.resetColorField();
        }
    }

    private void transferFluid(IFluidHandler source, byte sSide, IFluidHandler target, byte tSide, int amount) {
        FluidStack fluidStack = source.drain(ForgeDirection.getOrientation(sSide), amount, false);

        if (fluidStack != null) {
            int fluidTransferred = target.fill(ForgeDirection.getOrientation(tSide), fluidStack, true);
            source.drain(ForgeDirection.getOrientation(sSide), fluidTransferred, true);
        }
    }

    private boolean testBit(int aCoverVariable, int bitMask) {
        return (aCoverVariable & bitMask) != 0;
    }

    private int toggleBit(int aCoverVariable, int bitMask) {
        return (aCoverVariable ^ bitMask);
    }

    @Override
    public int doCoverThings(byte aSide, byte aInputRedstone, int aCoverID, int aCoverVariable, ICoverable aTileEntity, long aTimer) {
        if ((aTileEntity instanceof IFluidHandler)) {
            IFluidHandler fluidHandlerSelf = (IFluidHandler) aTileEntity;
            IFluidHandler fluidHandlerEnder = getEnderFluidContainer(getEnderLinkTag((IFluidHandler) aTileEntity));

            if (testBit(aCoverVariable, IMPORT_EXPORT_MASK)) {
                transferFluid(fluidHandlerEnder, (byte) 6, fluidHandlerSelf, aSide, L_PER_TICK);
            } else {
                transferFluid(fluidHandlerSelf, aSide, fluidHandlerEnder, (byte) 6, L_PER_TICK);
            }
        }
        return aCoverVariable;
    }

    @Override
    public String getDescription(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        return "";
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
    public int onCoverScrewdriverclick(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        int newCoverVariable = toggleBit(aCoverVariable, IMPORT_EXPORT_MASK);

        if (testBit(aCoverVariable, IMPORT_EXPORT_MASK)) {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Suction Engaged!");//TODO Translation support
        } else {
            PlayerChatHelper.SendInfo(aPlayer, "Ender Filling Engaged!");
        }
        return newCoverVariable;
    }

    @Override
    public int getTickRate(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
        //Runs each tick
        return 1;
    }

    //region GUI
    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public Object getClientGUI(byte aSide, int aCoverID, int coverData, ICoverable aTileEntity)  {
        //Only open gui if we're placed on a fluid tank
        Object gui = null;
        if (aTileEntity instanceof IFluidHandler) {
            gui = new TM_EnderFluidLinkCover(aSide, aCoverID, coverData, aTileEntity);
        }
        return gui;
    }

    private static TM_EnderFluidLinkCover GUI_INSTANCE;

    private class TM_EnderFluidLinkCover extends GT_GUICover {
        private final byte side;
        private final int coverID;
        private int coverVariable;
        private GT_GuiIntegerHexTextBox colorField;

        private final static int START_X = 10;
        private final static int START_Y = 25;
        private final static int SPACE_X = 18;
        private final static int SPACE_Y = 18;

        private final static int SIZE_X = 176;
        private final static int SIZE_Y = 107;

        private final static int BOX_SIZE_X = 34;
        private final static int BOX_SIZE_Y = 34;

        private final static int TEXT_FIELD_SIZE_X = 72;
        private final static int TEXT_FIELD_SIZE_Y = 12;
        private final static int TEXT_STRING_LENGTH = 9;

        private final static int FONT_COLOR = 0xFF555555;
        private final static int BOX_BORDER_COLOR = 0xFF000000;

        private final static int COLOR_FIELD_ID = 0;
        private final static int PUBLIC_BUTTON_ID = 1;
        private final static int PRIVATE_BUTTON_ID = 2;
        private final static int IMPORT_BUTTON_ID = 3;
        private final static int EXPORT_BUTTON_ID = 4;

        private GT_GuiIconButton newButtonWithSpacing(int id, int x, int y, GT_GuiIcon icon) {
            return new GT_GuiIconButton(this, id, START_X + SPACE_X * x, START_Y + SPACE_Y * y, icon);
        }

        private GT_GuiIntegerHexTextBox newTextField(int id, int x, int y) {
            GT_GuiIntegerHexTextBox field = new GT_GuiIntegerHexTextBox(this, id, START_X + SPACE_X * x,
                    START_Y + SPACE_Y * y, TEXT_FIELD_SIZE_X, TEXT_FIELD_SIZE_Y);
            field.setMaxStringLength(TEXT_STRING_LENGTH);
            return field;
        }

        private int drawNewString(String text, int x, int y) {
            int align = 4;
            return fontRendererObj.drawString(text, START_X + SPACE_X * x, align + START_Y + SPACE_Y * y, FONT_COLOR);
        }

        private void drawColorSquare(int x, int y) {
            //Draw the border square
            int borderX1 = START_X + SPACE_X * x;
            int borderY1 = START_Y + SPACE_Y * y;
            int borderX2 = borderX1 + BOX_SIZE_X;
            int borderY2 = borderY1 + BOX_SIZE_Y;
            drawRect(borderX1, borderY1, borderX2, borderY2, BOX_BORDER_COLOR);

            //Draw Checkerboard Pattern
            int white = 0xFFFFFFFF;
            int grey = 0xFFBFBFBF;
            boolean whiteOrGrey = true;
            int cGridXStart = borderX1 + 1;
            int cGridYStart = borderY1 + 1;
            int cGridXToDraw = 4;
            int cGridYToDraw = 4;
            int cSquareWidth = 8;
            int cSquareHeight = 8;
            for (int i = 0; i < cGridXToDraw; i++) {
                for (int j = 0; j < cGridYToDraw; j++) {
                    int cBoxX1 = cGridXStart + (cSquareWidth * i);
                    int cBoxY1 = cGridYStart + (cSquareHeight * j);
                    int cBoxX2 = cBoxX1 + cSquareWidth;
                    int cBoxY2 = cBoxY1 + cSquareHeight;
                    int cBoxColor = whiteOrGrey ? white : grey;
                    drawRect(cBoxX1, cBoxY1, cBoxX2, cBoxY2, cBoxColor);
                    whiteOrGrey = !whiteOrGrey;
                }
                whiteOrGrey = !whiteOrGrey;
            }

            //Draw the actual color
            int insideX1 = borderX1 + 1;
            int insideY1 = borderY1 + 1;
            int insideX2 = borderX2 - 1;
            int insideY2 = borderY2 - 1;
            drawRect(insideX1, insideY1, insideX2, insideY2, tag.getColorInt());
        }

        public TM_EnderFluidLinkCover(byte aSide, int aCoverID, int aCoverVariable, ICoverable aTileEntity) {
            super(aTileEntity, SIZE_X, SIZE_Y, GT_Utility.intToStack(aCoverID));
            side = aSide;
            coverID = aCoverID;
            coverVariable = aCoverVariable;
            NetworkDispatcher.INSTANCE.sendToServer(new EnderLinkCoverMessage.EnderLinkCoverQuery(tag, (IFluidHandler) tile));
            //Color Value Field
            colorField = newTextField(COLOR_FIELD_ID, 2, 1);
            GUI_INSTANCE = this;
            resetColorField();
            //Public/Private Buttons
            newButtonWithSpacing(PUBLIC_BUTTON_ID, 0, 2, GT_GuiIcon.WHITELIST)
                    .setTooltipText("Public");
            newButtonWithSpacing(PRIVATE_BUTTON_ID, 1, 2, GT_GuiIcon.BLACKLIST)
                    .setTooltipText("Private");
            //Import/Export Buttons
            newButtonWithSpacing(IMPORT_BUTTON_ID, 0, 3, GT_GuiIcon.IMPORT)
                    .setTooltipText(trans("007", "Import"));
            newButtonWithSpacing(EXPORT_BUTTON_ID, 1, 3, GT_GuiIcon.EXPORT)
                    .setTooltipText(trans("007", "Export"));
        }

        @Override
        public void drawExtras(int mouseX, int mouseY, float parTicks) {
            super.drawExtras(mouseX, mouseY, parTicks);
            drawColorSquare(0, 0);
            drawNewString("Color Value", 2, 0);
            drawNewString("Public/Private", 2, 2);
            drawNewString(trans("229", "Import/Export"), 2, 3);
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
        }

        private void switchPrivatePublic(int coverVar) {
            UUID ownerUUID = tag.getUUID();
            if (testBit(coverVar, PUBLIC_PRIVATE_MASK)){
                if (tile instanceof BaseMetaTileEntity){
                    BaseMetaTileEntity mte = (BaseMetaTileEntity) tile;
                    ownerUUID = mte.getOwnerUuid();
                }
            } else {
                ownerUUID = null;
            }
            EnderLinkTag newTag = new EnderLinkTag(new Color(tag.getColorInt(), true), ownerUUID);
            NetworkDispatcher.INSTANCE.sendToServer(new EnderLinkCoverMessage.EnderLinkCoverUpdate(newTag, (IFluidHandler) tile));
        }

        private int getNewCoverVariable(int id) {
            int tempCoverVariable = coverVariable;
            switch (id) {
                case PUBLIC_BUTTON_ID: case PRIVATE_BUTTON_ID:
                    tempCoverVariable = toggleBit(tempCoverVariable, PUBLIC_PRIVATE_MASK);
                    switchPrivatePublic(tempCoverVariable);
                    break;
                case IMPORT_BUTTON_ID: case EXPORT_BUTTON_ID:
                    tempCoverVariable = toggleBit(tempCoverVariable, IMPORT_EXPORT_MASK);
            }
            return tempCoverVariable;
        }

        private boolean getClickable(int id) {
            boolean canBeClicked = false;
            switch (id) {
                case PUBLIC_BUTTON_ID:
                    canBeClicked = testBit(coverVariable, PUBLIC_PRIVATE_MASK);
                    break;
                case PRIVATE_BUTTON_ID:
                    canBeClicked = !testBit(coverVariable, PUBLIC_PRIVATE_MASK);
                    break;
                case IMPORT_BUTTON_ID:
                    canBeClicked = testBit(coverVariable, IMPORT_EXPORT_MASK);
                    break;
                case EXPORT_BUTTON_ID:
                    canBeClicked = !testBit(coverVariable, IMPORT_EXPORT_MASK);
            }
            return canBeClicked;
        }

        @Override
        public void applyTextBox(GT_GuiIntegerTextBox box) {
            try {
                String string = box.getText();
                int colorValue = (int) Long.parseLong(string.replaceFirst("#", ""), 16);
                tag = new EnderLinkTag(new Color(colorValue, true), tag.getUUID());
                NetworkDispatcher.INSTANCE.sendToServer(new EnderLinkCoverMessage.EnderLinkCoverUpdate(tag, (IFluidHandler) tile));
            } catch (NumberFormatException ignored) {
            }
            resetColorField();
        }

        @Override
        public void resetTextBox(GT_GuiIntegerTextBox box) {
            //Solid White becomes: #FFFFFFFF
            box.setText("#" + String.format("%08X", tag.getColorInt()));
        }

        public void resetColorField() {
            resetTextBox(colorField);
        }

        private class GT_GuiIntegerHexTextBox extends GT_GuiIntegerTextBox {
            public GT_GuiIntegerHexTextBox(IGuiScreen gui, int id, int x, int y, int width, int height) {
                super(gui, id, x, y, width, height);
            }

            @Override
            public boolean validChar(char c, int key) {
                boolean isValid;
                if (getCursorPosition() == 0) {
                    isValid = c == '#';
                } else {
                    isValid = super.validChar(c, key)
                            || c == 'A' || c == 'B' || c == 'C' || c == 'D' || c == 'E' || c == 'F'
                            || c == 'a' || c == 'b' || c == 'c' || c == 'd' || c == 'e' || c == 'f';
                }
                return isValid;
            }
        }
    }
}
