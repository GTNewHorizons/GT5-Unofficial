package com.github.technus.tectech.thing.metaTileEntity.multi.base;

import static gregtech.api.enums.GT_Values.RES_PATH_GUI;

import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.github.technus.tectech.TecTech;

import gregtech.api.gui.GT_GUIContainerMetaTile_Machine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

/**
 * Created by Tec on 21.02.2017.
 */
@Deprecated
public class GT_GUIContainer_MultiMachineEM extends GT_GUIContainerMetaTile_Machine {

    protected final String mName;
    protected static byte counter = 0;
    protected final boolean eSafeVoidButton, allowedToWorkButton, ePowerPassButton;
    protected final GT_Container_MultiMachineEM mContainer;

    protected GT_GUIContainer_MultiMachineEM(GT_Container_MultiMachineEM container, String aName, String aTextureFile,
            boolean enablePowerPass, boolean enableSafeVoid, boolean enablePowerButton) {
        super(container, RES_PATH_GUI + "multimachines/" + (aTextureFile == null ? "MultiblockDisplay" : aTextureFile));
        mContainer = (GT_Container_MultiMachineEM) super.mContainer;
        mName = aName;
        ePowerPassButton = enablePowerPass;
        eSafeVoidButton = enableSafeVoid;
        allowedToWorkButton = enablePowerButton;
        ySize = 192;
        xSize = 198;
    }

    public GT_GUIContainer_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity,
            String aName, String aTextureFile, boolean enablePowerPass, boolean enableSafeVoid,
            boolean enablePowerButton) {
        this(
                new GT_Container_MultiMachineEM(aInventoryPlayer, aTileEntity),
                aName,
                aTextureFile,
                enablePowerPass,
                enableSafeVoid,
                enablePowerButton);
    }

    protected GT_GUIContainer_MultiMachineEM(GT_Container_MultiMachineEM container, String aName, String aTextureFile) {
        this(container, aName, aTextureFile, true, true, true);
    }

    public GT_GUIContainer_MultiMachineEM(InventoryPlayer aInventoryPlayer, IGregTechTileEntity aTileEntity,
            String aName, String aTextureFile) {
        this(new GT_Container_MultiMachineEM(aInventoryPlayer, aTileEntity), aName, aTextureFile);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float par3) {
        super.drawScreen(mouseX, mouseY, par3);
        if (mContainer != null) {
            if (mContainer.mTileEntity != null
                    && mContainer.mTileEntity.getMetaTileEntity() instanceof GT_MetaTileEntity_MultiblockBase_EM) {
                LEDtooltips(mouseX, mouseY);
            }
        }
    }

    @Override
    protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY) {
        fontRendererObj.drawString(mName, 7, 8, 16448255);

        if (mContainer != null) {
            if ((mContainer.mDisplayErrorCode & 1) != 0) {
                fontRendererObj.drawString("Pipe is loose.", 7, 16, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 2) != 0) {
                fontRendererObj.drawString("Screws are loose.", 7, 24, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 4) != 0) {
                fontRendererObj.drawString("Something is stuck.", 7, 32, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 8) != 0) {
                fontRendererObj.drawString("Plating is dented.", 7, 40, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 16) != 0) {
                fontRendererObj.drawString("Circuitry burned out.", 7, 48, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 32) != 0) {
                fontRendererObj.drawString("That doesn't belong there.", 7, 56, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 64) != 0) {
                fontRendererObj.drawString("Incomplete Structure.", 7, 64, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 128) != 0) {
                fontRendererObj.drawString("Too Uncertain.", 7, 72, 16448255);
            }
            if ((mContainer.mDisplayErrorCode & 256) != 0) {
                fontRendererObj.drawString("Invalid Parameters.", 7, 80, 16448255);
            }

            if (mContainer.mDisplayErrorCode == 0) {
                if (mContainer.mActive == 0) {
                    fontRendererObj.drawString("Soft Hammer or press Button", 7, 16, 16448255);
                    fontRendererObj.drawString("to (re-)start the Machine", 7, 24, 16448255);
                    fontRendererObj.drawString("if it doesn't start.", 7, 32, 16448255);
                } else {
                    fontRendererObj.drawString("Running perfectly.", 7, 16, 16448255);
                }
            }
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(float par1, int par2, int par3) {
        super.drawGuiContainerBackgroundLayer(par1, par2, par3);
        int x = (width - xSize) / 2;
        int y = (height - ySize) / 2;
        drawTexturedModalRect(x, y, 0, 0, xSize, ySize);
        if (mContainer != null && mContainer.eParamsInStatus != null) {
            counter = (byte) ((1 + counter) % 6);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            x += 173;
            if (!ePowerPassButton && !mContainer.ePowerPassCover) { // no function
                drawTexturedModalRect(x, y + 115, 231, 23, 18, 18);
            } else {
                if (mContainer.ePowerPass) { //
                    drawTexturedModalRect(x, y + 115, 207, 23, 18, 18);
                }
            }

            if (!eSafeVoidButton) {
                drawTexturedModalRect(x, y + 132, 231, 41, 18, 18);
            } else if (mContainer.eSafeVoid) {
                drawTexturedModalRect(x, y + 132, 207, 41, 18, 18);
            }

            if (!allowedToWorkButton) {
                drawTexturedModalRect(x, y + 147, 231, 57, 18, 18);
            } else if (mContainer.allowedToWork) {
                drawTexturedModalRect(x, y + 147, 207, 57, 18, 18);
            }
            x -= 162;
            y += 96;
            for (int i = 0; i < 20;) {
                byte hatch = (byte) (i >>> 1);
                LEDdrawP(x, y, i, 0, mContainer.eParamsInStatus[hatch]);
                LEDdrawP(x, y, i++, 1, mContainer.eParamsOutStatus[hatch]);
                LEDdrawP(x, y, i, 0, mContainer.eParamsInStatus[hatch + 10]);
                LEDdrawP(x, y, i++, 1, mContainer.eParamsOutStatus[hatch + 10]);
            }

            short rU = 207, Vs = 77;
            x += 162;
            byte state = mContainer.eCertainStatus;
            switch (mContainer.eCertainMode) {
                case 1: // ooo oxo ooo
                    drawTexturedModalRect(x + 6, y + 6, rU + (state == 0 ? 30 : 6), Vs + 6, 6, 6);
                    break;
                case 2: // ooo xox ooo
                    drawTexturedModalRect(x, y + 6, rU + ((state & 1) == 0 ? 24 : 0), Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6, rU + ((state & 2) == 0 ? 36 : 12), Vs + 6, 6, 6);
                    break;
                case 3: // oxo xox oxo
                    drawTexturedModalRect(x + 6, y, rU + ((state & 1) == 0 ? 30 : 6), Vs, 6, 6);
                    drawTexturedModalRect(x, y + 6, rU + ((state & 2) == 0 ? 24 : 0), Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 12, y + 6, rU + ((state & 4) == 0 ? 36 : 12), Vs + 6, 6, 6);
                    drawTexturedModalRect(x + 6, y + 12, rU + ((state & 8) == 0 ? 30 : 6), Vs + 12, 6, 6);
                    break;
                case 4: // xox ooo xox
                    drawTexturedModalRect(x, y, rU + ((state & 1) == 0 ? 24 : 0), Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y, rU + ((state & 2) == 0 ? 36 : 12), Vs, 6, 6);
                    drawTexturedModalRect(x, y + 12, rU + ((state & 4) == 0 ? 24 : 0), Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12, rU + ((state & 8) == 0 ? 36 : 12), Vs + 12, 6, 6);
                    break;
                case 5: // xox oxo xox
                    drawTexturedModalRect(x, y, rU + ((state & 1) == 0 ? 24 : 0), Vs, 6, 6);
                    drawTexturedModalRect(x + 12, y, rU + ((state & 2) == 0 ? 36 : 12), Vs, 6, 6);
                    drawTexturedModalRect(x + 6, y + 6, rU + ((state & 4) == 0 ? 30 : 6), Vs + 6, 6, 6);
                    drawTexturedModalRect(x, y + 12, rU + ((state & 8) == 0 ? 24 : 0), Vs + 12, 6, 6);
                    drawTexturedModalRect(x + 12, y + 12, rU + ((state & 16) == 0 ? 36 : 12), Vs + 12, 6, 6);
                    break;
            }
        }
    }

    protected void LEDdrawP(int x, int y, int i, int j, LedStatus status) {
        int v = 192, su = 8, sv = 6, u = 11;
        switch (status) {
            case STATUS_WTF: {
                int c = counter;
                if (c > 4) {
                    c = TecTech.RANDOM.nextInt(5);
                }
                switch (c) {
                    case 0:
                        drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv); // BLUE
                        break;
                    case 1:
                        drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (2 + j), su, sv); // cyan
                        break;
                    case 2:
                        drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (4 + j), su, sv); // green
                        break;
                    case 3:
                        drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (6 + j), su, sv); // orangeyello
                        break;
                    case 4:
                        drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv); // redd
                        break;
                }
                break;
            }
            case STATUS_WRONG: // fallthrough
                if (counter < 2) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv); // blue
                    break;
                } else if (counter < 4) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv); // red
                    break;
                }
            case STATUS_OK: // ok
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (4 + j), su, sv); // green
                break;
            case STATUS_TOO_LOW: // too low blink
                if (counter < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * j, su, sv); // BLUE
                    break;
                }
            case STATUS_LOW: // too low
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (2 + j), su, sv); // cyan
                break;
            case STATUS_TOO_HIGH: // too high blink
                if (counter < 3) {
                    drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (8 + j), su, sv); // redd
                    break;
                }
            case STATUS_HIGH: // too high
                drawTexturedModalRect(x + su * i, y + sv * j, u + su * i, v + sv * (6 + j), su, sv); // orangeyello
                break;
            case STATUS_NEUTRAL:
                if (counter < 3) {
                    GL11.glColor4f(.85f, .9f, .95f, .5F);
                    drawTexturedModalRect(x + su * i, y + sv * j, 212, 96, su + 2, sv + 2);
                } else {
                    GL11.glColor4f(.8f, .9f, 1f, .5F);
                    drawTexturedModalRect(x + su * i, y + sv * j, 212, 96, su + 2, sv + 2);
                }
                GL11.glColor4f(1f, 1f, 1f, 1f);
                break;
            case STATUS_UNDEFINED:
                if (counter < 3) {
                    GL11.glColor4f(.5f, .1f, .15f, .5F);
                    drawTexturedModalRect(x + su * i, y + sv * j, 212, 96, su + 2, sv + 2);
                } else {
                    GL11.glColor4f(0f, .1f, .2f, .5F);
                    drawTexturedModalRect(x + su * i, y + sv * j, 212, 96, su + 2, sv + 2);
                }
                GL11.glColor4f(1f, 1f, 1f, 1f);
                break;
            case STATUS_UNUSED:
            default:

        }
    }

    protected void LEDtooltips(int x, int y) {
        int renderPosX = x;
        int renderPosY = y;
        x -= (width - xSize) / 2;
        y -= (height - ySize) / 2;
        // drawHoveringText(Arrays.asList(""+x,""+y), -1, -11, fontRendererObj);
        if (mContainer.mTileEntity != null) {
            IMetaTileEntity mte = mContainer.mTileEntity.getMetaTileEntity();
            if (mte instanceof GT_MetaTileEntity_MultiblockBase_EM) {
                Parameters parametrization = ((GT_MetaTileEntity_MultiblockBase_EM) mte).parametrization;
                parametrization.eParamsInStatus = mContainer.eParamsInStatus;
                parametrization.eParamsOutStatus = mContainer.eParamsOutStatus;
                parametrization.iParamsIn = mContainer.eParamsIn;
                parametrization.iParamsOut = mContainer.eParamsOut;
                int su = 8, sv = 6, u = 11, v = 96;
                if (x < u || y < v) return;
                v += sv;
                for (int hatch = 0; hatch < 10; hatch++) {
                    for (int param = 0; param < 2; param++) {
                        if (x < (u += su)) {
                            if (y < v) {

                                hoveringText(
                                        ((GT_MetaTileEntity_MultiblockBase_EM) mte)
                                                .getFullLedDescriptionIn(hatch, param),
                                        renderPosX,
                                        renderPosY,
                                        fontRendererObj);
                                return;
                            } else if (y >= v && y < v + sv) {

                                hoveringText(
                                        ((GT_MetaTileEntity_MultiblockBase_EM) mte)
                                                .getFullLedDescriptionOut(hatch, param),
                                        renderPosX,
                                        renderPosY,
                                        fontRendererObj);
                                return;
                            }
                        }
                    }
                }
            }
        }
    }

    protected void hoveringText(List<String> strings, int x, int y, FontRenderer font) {
        if (!strings.isEmpty()) {
            GL11.glDisable(GL12.GL_RESCALE_NORMAL);
            // RenderHelper.disableStandardItemLighting();
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

            int j1 = 0xf0001040; // bg
            this.drawGradientRect(x2 - 3, y2 - 4, x2 + k + 3, y2 - 3, j1, j1);
            this.drawGradientRect(x2 - 3, y2 + i1 + 3, x2 + k + 3, y2 + i1 + 4, j1, j1);
            this.drawGradientRect(x2 - 3, y2 - 3, x2 + k + 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 - 4, y2 - 3, x2 - 3, y2 + i1 + 3, j1, j1);
            this.drawGradientRect(x2 + k + 3, y2 - 3, x2 + k + 4, y2 + i1 + 3, j1, j1);
            int k1 = 0x500040ff; // border bright
            int l1 = (k1 & 0xfefefe) >> 1 | k1 & 0xff000000; // border dark???
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

            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        }
    }
}
