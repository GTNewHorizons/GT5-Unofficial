package com.github.technus.tectech.font;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class TecTechFontRender extends FontRenderer {
    public static final TecTechFontRender INSTANCE = new TecTechFontRender();

    private static float DISTANCE_L  = .125F;
    private static float DISTANCE_L2 = DISTANCE_L * 2F;

    private static float DISTANCE_M  = 0.06F;
    private static float DISTANCE_M2 = DISTANCE_M * 2F;

    private static float DISTANCE_A  = 0.06F;
    private static float DISTANCE_A2 = DISTANCE_A * 2F;


    private TecTechFontRender() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
    }

    private void resetStyles() {
        this.randomStyle = false;
        this.boldStyle = false;
        this.italicStyle = false;
        this.underlineStyle = false;
        this.strikethroughStyle = false;
    }

    private int renderString(String p_78258_1_, int p_78258_2_, int p_78258_3_, int p_78258_4_, boolean p_78258_5_) {
        if (p_78258_1_ == null) {
            return 0;
        } else {
            if (this.bidiFlag) {
                p_78258_1_ = this.bidiReorder(p_78258_1_);
            }

            if ((p_78258_4_ & -67108864) == 0) {
                p_78258_4_ |= -16777216;
            }

            if (p_78258_5_) {
                p_78258_4_ = (p_78258_4_ & 16579836) >> 2 | p_78258_4_ & -16777216;
            }

            this.red = (float)(p_78258_4_ >> 16 & 255) / 255.0F;
            this.blue = (float)(p_78258_4_ >> 8 & 255) / 255.0F;
            this.green = (float)(p_78258_4_ & 255) / 255.0F;
            this.alpha = (float)(p_78258_4_ >> 24 & 255) / 255.0F;
            this.setColor(this.red, this.blue, this.green, this.alpha);
            this.posX = (float)p_78258_2_;
            this.posY = (float)p_78258_3_;
            this.renderStringAtPos(p_78258_1_, p_78258_5_);
            return (int)this.posX;
        }
    }

    private String bidiReorder(String p_147647_1_) {
        try {
            Bidi bidi = new Bidi((new ArabicShaping(8)).shape(p_147647_1_), 127);
            bidi.setReorderingMode(0);
            return bidi.writeReordered(2);
        } catch (ArabicShapingException var3) {
            return p_147647_1_;
        }
    }

    private void renderStringAtPos(String p_78255_1_, boolean p_78255_2_) {
        for(int i = 0; i < p_78255_1_.length(); ++i) {
            char c0 = p_78255_1_.charAt(i);
            int j;
            int k;
            if (c0 == 167 && i + 1 < p_78255_1_.length()) {
                j = "0123456789abcdefklmnor".indexOf(p_78255_1_.toLowerCase().charAt(i + 1));
                if (j < 16) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    if (j < 0 || j > 15) {
                        j = 15;
                    }

                    if (p_78255_2_) {
                        j += 16;
                    }

                    k = this.colorCode[j];
                    this.textColor = k;
                    this.setColor((float)(k >> 16) / 255.0F, (float)(k >> 8 & 255) / 255.0F, (float)(k & 255) / 255.0F, this.alpha);
                } else if (j == 16) {
                    this.randomStyle = true;
                } else if (j == 17) {
                    this.boldStyle = true;
                } else if (j == 18) {
                    this.strikethroughStyle = true;
                } else if (j == 19) {
                    this.underlineStyle = true;
                } else if (j == 20) {
                    this.italicStyle = true;
                } else if (j == 21) {
                    this.randomStyle = false;
                    this.boldStyle = false;
                    this.strikethroughStyle = false;
                    this.underlineStyle = false;
                    this.italicStyle = false;
                    this.setColor(this.red, this.blue, this.green, this.alpha);
                }

                ++i;
            } else {
                j = "ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(c0);
                if (this.randomStyle && j != -1) {
                    do {
                        k = this.fontRandom.nextInt(this.charWidth.length);
                    } while(this.charWidth[j] != this.charWidth[k]);

                    j = k;
                }

                float f1 = this.unicodeFlag ? 0.5F : 1.0F;
                boolean flag1 = (c0 == 0 || j == -1 || this.unicodeFlag) && p_78255_2_;
                if (flag1) {
                    this.posX -= f1;
                    this.posY -= f1;
                }

                float f = this.renderCharAtPos(j, c0, this.italicStyle);
                if (flag1) {
                    this.posX += f1;
                    this.posY += f1;
                }

                if (this.boldStyle) {
                    this.posX += f1;
                    if (flag1) {
                        this.posX -= f1;
                        this.posY -= f1;
                    }

                    this.renderCharAtPos(j, c0, this.italicStyle);
                    this.posX -= f1;
                    if (flag1) {
                        this.posX += f1;
                        this.posY += f1;
                    }

                    ++f;
                }

                this.doDraw(f);
            }
        }

    }

    private float renderCharAtPos(int p_78278_1_, char p_78278_2_, boolean p_78278_3_) {
        return p_78278_2_ == ' ' ? 4.0F : ("ÀÁÂÈÊËÍÓÔÕÚßãõğİıŒœŞşŴŵžȇ\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000ÇüéâäàåçêëèïîìÄÅÉæÆôöòûùÿÖÜø£Ø×ƒáíóúñÑªº¿®¬½¼¡«»░▒▓│┤╡╢╖╕╣║╗╝╜╛┐└┴┬├─┼╞╟╚╔╩╦╠═╬╧╨╤╥╙╘╒╓╫╪┘┌█▄▌▐▀αβΓπΣσμτΦΘΩδ∞∅∈∩≡±≥≤⌠⌡÷≈°∙·√ⁿ²■\u0000".indexOf(p_78278_2_) != -1 && !this.unicodeFlag ? this.renderDefaultChar(p_78278_1_, p_78278_3_) : this.renderUnicodeChar(p_78278_2_, p_78278_3_));
    }

    @Override
    public int drawString(String str, int x, int y, int color, boolean dropShadow) {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 0:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                if (dropShadow) {
                    GL11.glTranslatef(DISTANCE_A, DISTANCE_A, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(-DISTANCE_A2, 0, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(0, -DISTANCE_A2, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(DISTANCE_A2, 0, 0F);
                }

                GL11.glTranslatef(DISTANCE_A, DISTANCE_A, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(-DISTANCE_A2, 0, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(0, -DISTANCE_A2, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(DISTANCE_A2, 0, 0F);

                GL11.glPopMatrix();
                break;
            case 1:
                return Minecraft.getMinecraft().fontRenderer.drawString(str, x, y, color, dropShadow);
            case 2:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                if (dropShadow) {
                    GL11.glTranslatef(DISTANCE_M, DISTANCE_M, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(-DISTANCE_M2, 0, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(0, -DISTANCE_M2, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(DISTANCE_M2, 0, 0F);
                }

                GL11.glTranslatef(DISTANCE_M, DISTANCE_M, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(-DISTANCE_M2, 0, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(0, -DISTANCE_M2, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(DISTANCE_M2, 0, 0F);

                GL11.glPopMatrix();
                break;
            case 3:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                if (dropShadow) {
                    GL11.glTranslatef(DISTANCE_L, DISTANCE_L, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(-DISTANCE_L2, 0, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(0, -DISTANCE_L2, 0F);
                    drawStringBack(str, x, y, color);
                    GL11.glTranslatef(DISTANCE_L2, 0, 0F);
                }

                GL11.glTranslatef(DISTANCE_L, DISTANCE_L, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(-DISTANCE_L2, 0, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(0, -DISTANCE_L2, 0F);
                drawStringFront(str, x, y, color);
                GL11.glTranslatef(DISTANCE_L2, 0, 0F);

                GL11.glPopMatrix();
                break;
        }
        return drawStringFront(str, x, y, color);
    }

    @Override
    public void drawSplitString(String str, int x, int y, int maxWidth, int color) {
        switch (Minecraft.getMinecraft().gameSettings.guiScale) {
            case 0:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                GL11.glTranslatef(DISTANCE_A, DISTANCE_A, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(-DISTANCE_A2, 0, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(0, -DISTANCE_A2, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(DISTANCE_A2, 0, 0F);

                GL11.glPopMatrix();
                break;
            case 1:
                Minecraft.getMinecraft().fontRenderer.drawSplitString(str, x, y, maxWidth, color);
                break;
            case 2:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                GL11.glTranslatef(DISTANCE_M, DISTANCE_M, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(-DISTANCE_M2, 0, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(0, -DISTANCE_M2, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(DISTANCE_M2, 0, 0F);

                GL11.glPopMatrix();
                break;
            case 3:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                GL11.glTranslatef(DISTANCE_L, DISTANCE_L, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(-DISTANCE_L2, 0, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(0, -DISTANCE_L2, 0F);
                super.drawSplitString(str, x, y, maxWidth, color);
                GL11.glTranslatef(DISTANCE_L2, 0, 0F);

                GL11.glPopMatrix();
                break;
        }
    }

    private int drawStringFront(String p_85187_1_, int p_85187_2_, int p_85187_3_, int p_85187_4_) {
        GL11.glEnable(3008);
        resetStyles();
        return renderString(p_85187_1_, p_85187_2_, p_85187_3_, p_85187_4_, false);
    }

    private int drawStringBack(String p_85187_1_, int p_85187_2_, int p_85187_3_, int p_85187_4_) {
        GL11.glEnable(3008);
        resetStyles();
        return renderString(p_85187_1_, p_85187_2_ + 1, p_85187_3_ + 1, p_85187_4_, true);
    }

    @Override
    public int getStringWidth(String p_78256_1_) {
        if (Minecraft.getMinecraft().gameSettings.guiScale == 1) {
            return Minecraft.getMinecraft().fontRenderer.getStringWidth(p_78256_1_);
        }
        if (p_78256_1_ == null) {
            return 0;
        } else {
            int     i    = 0;
            boolean flag = false;

            for (int j = 0; j < p_78256_1_.length(); ++j) {
                char c0 = p_78256_1_.charAt(j);
                int  k  = this.getCharWidth(c0);

                if (k < 0 && j < p_78256_1_.length() - 1) {
                    ++j;
                    c0 = p_78256_1_.charAt(j);

                    if (c0 != 108 && c0 != 76) {
                        if (c0 == 114 || c0 == 82) {
                            flag = false;
                        }
                    } else {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag && k > 0) {
                    ++i;
                }
            }

            return i;
        }
    }


}
