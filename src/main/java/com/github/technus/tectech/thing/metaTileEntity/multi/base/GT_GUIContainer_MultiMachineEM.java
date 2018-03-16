package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import java.util.List;

import static com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM.*;
import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

/**
 * Created by Tec on 21.02.2017.
 */

public class GT_GUIContainer_MultiMachineEM extends GT_GUIContainerMetaTile_Machine {
    private String mName;
    private static byte counter = 0;
    private final boolean ePowerPassButton, eSafeVoidButton, allowedToWorkButton;

    public GT_GUIContainer_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile, boolean enablePowerPass, boolean enableSafeVoid, boolean enablePowerButton) {
        super(new GT_Container_MultiMachineEM(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
        ePowerPassButton=enablePowerPass;
        eSafeVoidButton=enableSafeVoid;
        allowedToWorkButton=enablePowerButton;
    }

    public GT_GUIContainer_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity, String aName, String aTextureFile) {
        super(new GT_Container_MultiMachineEM(aInventoryPlayer, aTileEntity), RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mName = aName;
        ePowerPassButton=eSafeVoidButton=allowedToWorkButton=true;
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int par1, int par2) {
        fontRendererObj.drawString(mName, 10, -18, 16448255);

        if (mContainer != null) {
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 1) != 0) {
                fontRendererObj.drawString("Pipe is loose.", 10, -10, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 2) != 0) {
                fontRendererObj.drawString("Screws are loose.", 10, -2, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 4) != 0) {
                fontRendererObj.drawString("Something is stuck.", 10, 6, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 8) != 0) {
                fontRendererObj.drawString("Plating is dented.", 10, 14, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 16) != 0) {
                fontRendererObj.drawString("Circuitry burned out.", 10, 22, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 32) != 0) {
                fontRendererObj.drawString("That doesn't belong there.", 10, 30, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 64) != 0) {
                fontRendererObj.drawString("Incomplete Structure.", 10, 38, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 128) != 0) {
                fontRendererObj.drawString("Too Uncertain.", 10, 46, 16448255);
            }
            if ((((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode & 256) != 0) {
                fontRendererObj.drawString("Invalid Parameters.", 10, 54, 16448255);
            }

            if (((GT_Container_MultiMachineEM) mContainer).mDisplayErrorCode == 0) {
                if (((GT_Container_MultiMachineEM) mContainer).mActive == 0) {
                    fontRendererObj.drawString("Soft Hammer or press Button", 10, -10, 16448255);
                    fontRendererObj.drawString("to (re-)start the Machine", 10, -2, 16448255);
                    fontRendererObj.drawString("if it doesn't start.", 10, 6, 16448255);
                } else {
                    fontRendererObj.drawString("Running perfectly.", 10, -10, 16448255);
                }
            }

            int x = (width - xSize) / 2;
            int y = (height - ySize) / 2;
            LEDtooltips(par1-x, par2-y+26);
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        y -= 26;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize + 26);
        if (mContainer != null && ((GT_Container_MultiMachineEM) mContainer).eParamsInStatus != null) {
            counter = (byte) ((1 + counter) % 6);
            GL11.glColor4f(1f, 1f, 1f, 1f);

            if(!ePowerPassButton) {
                drawTexturedModalRect(x + 151, y + 23, 215, 23, 18, 18);
            } else if (((GT_Container_MultiMachineEM) mContainer).ePowerPass) {
                drawTexturedModalRect(x + 151, y + 23, 183, 23, 18, 18);
            }

            if(!eSafeVoidButton) {
                drawTexturedModalRect(x + 151, y + 41, 215, 41, 18, 18);
            } else if (((GT_Container_MultiMachineEM) mContainer).eSafeVoid) {
                drawTexturedModalRect(x + 151, y + 41, 183, 41, 18, 18);
            }

            if(!allowedToWorkButton) {
                drawTexturedModalRect(x + 151, y + 57, 215, 57, 18, 18);
            } else if (((GT_Container_MultiMachineEM) mContainer).allowedToWork) {
                drawTexturedModalRect(x + 151, y + 57, 183, 57, 18, 18);
            }

            x += 11;
            y += 96;
            for (int i = 0; i < 20; ) {
                byte b = (byte) (i >>> 1);
                LEDdrawP(x, y, i, 0, ((GT_Container_MultiMachineEM) mContainer).eParamsInStatus[b]);
                LEDdrawP(x, y, i++, 1, ((GT_Container_MultiMachineEM) mContainer).eParamsOutStatus[b]);
                LEDdrawP(x, y, i, 0, ((GT_Container_MultiMachineEM) mContainer).eParamsInStatus[b + 10]);
                LEDdrawP(x, y, i++, 1, ((GT_Container_MultiMachineEM) mContainer).eParamsOutStatus[b + 10]);
            }

            short rU = 183, Vs = 77;
            x += 140;
            y -= 19;
            byte state = ((GT_Container_MultiMachineEM) mContainer).eCertainStatus;
            switch (((GT_Container_MultiMachineEM) mContainer).eCertainMode) {
                case 1://ooo oxo ooo
                    drawTexturedModalRect(x + 6, y + 6,
                            rU + (state == 0 ? 38 : 6),
                            Vs + 6, 6, 6);
                    break;
                case 2://ooo xox ooo
                    drawTexturedModalRect(x, y + 6,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs + 6, 6, 6);
                    break;
                case 3://oxo xox oxo
                    drawTexturedModalRect(x + 6, y,
                            rU + ((state & 1) == 0 ? 38 : 6),
                            Vs, 6, 6);
                    drawTexturedModalRect(x, y + 6,
                            rU + ((state & 2) == 0 ? 32 : 0),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6,
                            rU + ((state & 4) == 0 ? 44 : 12),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 6, y + 12,
                            rU + ((state & 8) == 0 ? 38 : 6),
                            Vs + 12, 6, 6);
                    break;
                case 4://xox ooo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs, 6, 6);
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 4) == 0 ? 32 : 0),
                            Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + ((state & 8) == 0 ? 44 : 12),
                            Vs + 12, 6, 6);
                    break;
                case 5://xox oxo xox
                    drawTexturedModalRect(x, y,
                            rU + ((state & 1) == 0 ? 32 : 0),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y,
                            rU + ((state & 2) == 0 ? 44 : 12),
                            Vs, 6, 6);
                    drawTexturedModalRect(x + 6, y + 6,
                            rU + ((state & 4) == 0 ? 38 : 6),
                            Vs + 6, 6, 6);
                    drawTexturedModalRect(x, y + 12,
                            rU + ((state & 8) == 0 ? 32 : 0),
                            Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12,
                            rU + ((state & 16) == 0 ? 44 : 12),
                            Vs + 12, 6, 6);
                    break;
            }
        }
    }

    private void LEDdrawP(int x, int y, int i, int j, byte status) {
        int v = 192, su = 8, sv = 6, u = 11;
        switch (status) {
            case STATUS_WRONG: //fallthrough
                if (counter < 2) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv);
                    break;
                } else if (counter < 4) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv);
                    break;
                }
            case STATUS_OK://ok
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (4 + j), su, sv);
                break;
            case STATUS_TOO_LOW://too low blink
                if (counter < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv);
                    break;
                }
            case STATUS_LOW:// too low
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (2 + j), su, sv);
                break;
            case STATUS_TOO_HIGH://too high blink
                if (counter < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv);
                    break;
                }
            case STATUS_HIGH:// too high
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (6 + j), su, sv);
                break;
        }
    }

    private void LEDtooltips(float x,float y){
        //drawHoveringText(Arrays.asList(""+x,""+y), -1, -11, fontRendererObj);
        if(mContainer.mTileEntity!=null){
            IMetaTileEntity mte=mContainer.mTileEntity.getMetaTileEntity();
            if(mte instanceof GT_MetaTileEntity_MultiblockBase_EM){
                int su = 8, sv = 6, u=11,v=96;
                if(x<u || y<v) return;
                v+=sv;
                for(int hatch=0;hatch<10;hatch++){
                    for(int param=0;param<2;param++){
                        if(x<(u+=su)){
                            if(y<v){
                                hoveringText(((GT_MetaTileEntity_MultiblockBase_EM) mte).getFullLedDescriptionIn(hatch,param), u-su-1, v-11, fontRendererObj);
                                return;
                            }else if(y>=v && y<v+sv){
                                hoveringText(((GT_MetaTileEntity_MultiblockBase_EM) mte).getFullLedDescriptionOut(hatch,param), u-su-1, v+sv-11, fontRendererObj);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    private void hoveringText(List strings, int x, int y, FontRenderer font) {
        if (!strings.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            //RenderHelper.disableStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            int k = 0;

            for (Object aP_146283_1_ : strings) {
                String s = (String) aP_146283_1_;
                int l = font.getStringWidth(s);

                if (l > k) {
                    k = l;
                }
            }

            int x2 = x + 12;
            int y2 = y - 12;
            int i1 = 8;

            if (strings.size() > 1) {
                i1 += 2 + (strings.size() - 1) * 10;
            }

            if (x2 + k > this.width) {
                x2 -= 28 + k;
            }

            if (y2 + i1 + 6 > this.height) {
                y2 = this.height - i1 - 6;
            }

            //this.zLevel = 300.0F;
            //itemRender.zLevel = 300.0F;
            int j1 = 0xf0001040;//bg
            this.drawGradientRect(x2 - 3, y2 - 4, x2 + k + 3, y2 - 3, j1, j1);
            this.drawGradientRect(x2 - 3, y2 + i1 + 3, x2 + k + 3, y2 + i1 + 4, j1, j1);
            this.drawGradientRect(x2 - 3, y2 - 3, x2 + k + 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 - 4, y2 - 3, x2 - 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 + k + 3, y2 - 3, x2 + k + 4, y2 + i1 + 3, j1, j1);
            int k1 = 0x500040ff;//border bright
            int l1 = (k1 & 0xfefefe) >> 1 | k1 & 0xff000000;//border dark???
            this.drawGradientRect(x2 - 3, y2 - 3 + 1, x2 - 3 + 1, y2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(x2 + k + 2, y2 - 3 + 1, x2 + k + 3, y2 + i1 + 3 - 1, k1, l1);
            this.drawGradientRect(x2 - 3, y2 - 3, x2 + k + 3, y2 - 3 + 1, k1, k1);
            this.drawGradientRect(x2 - 3, y2 + i1 + 2, x2 + k + 3, y2 + i1 + 3, l1, l1);

            for (int i2 = 0; i2 < strings.size(); ++i2) {
                String s1 = (String) strings.get(i2);
                font.drawStringWithShadow(s1, x2, y2, -1);

                if (i2 == 0) {
                    y2 += 2;
                }

                y2 += 10;
            }

            //this.zLevel = 0.0F;
            //itemRender.zLevel = 0.0F;
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //RenderHelper.enableStandardItemLighting();
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}