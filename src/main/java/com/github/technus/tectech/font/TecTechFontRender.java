package com.github.technus.tectech.font;

import java.io.IOException;
import java.io.InputStream;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.text.ArabicShaping;
import com.ibm.icu.text.ArabicShapingException;
import com.ibm.icu.text.Bidi;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class TecTechFontRender extends FontRenderer {

    public static final TecTechFontRender INSTANCE = new TecTechFontRender();

    private static final float DISTANCE_L = .125F;
    private static final float DISTANCE_L2 = DISTANCE_L * 2F;

    private static final float DISTANCE_M = 0.06F;
    private static final float DISTANCE_M2 = DISTANCE_M * 2F;

    private static final float DISTANCE_A = 0.06F;
    private static final float DISTANCE_A2 = DISTANCE_A * 2F;

    private TecTechFontRender() {
        super(
                Minecraft.getMinecraft().gameSettings,
                new ResourceLocation("textures/font/ascii.png"),
                Minecraft.getMinecraft().renderEngine,
                false);
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

            this.red = (float) (p_78258_4_ >> 16 & 255) / 255.0F;
            this.blue = (float) (p_78258_4_ >> 8 & 255) / 255.0F;
            this.green = (float) (p_78258_4_ & 255) / 255.0F;
            this.alpha = (float) (p_78258_4_ >> 24 & 255) / 255.0F;
            this.setColor(this.red, this.blue, this.green, this.alpha);
            this.posX = (float) p_78258_2_;
            this.posY = (float) p_78258_3_;
            this.renderStringAtPos(p_78258_1_, p_78258_5_);
            return (int) this.posX;
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
        for (int i = 0; i < p_78255_1_.length(); ++i) {
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
                    this.setColor(
                            (float) (k >> 16) / 255.0F,
                            (float) (k >> 8 & 255) / 255.0F,
                            (float) (k & 255) / 255.0F,
                            this.alpha);
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
                j = "\u00C0\u00C1\u00C2\u00C8\u00CA\u00CB\u00CD\u00D3\u00D4\u00D5\u00DA\u00DF\u00E3\u00F5\u011F\u0130\u0131\u0152\u0153\u015E\u015F\u0174\u0175\u017E\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00C7\u00FC\u00E9\u00E2\u00E4\u00E0\u00E5\u00E7\u00EA\u00EB\u00E8\u00EF\u00EE\u00EC\u00C4\u00C5\u00C9\u00E6\u00C6\u00F4\u00F6\u00F2\u00FB\u00F9\u00FF\u00D6\u00DC\u00F8\u00A3\u00D8\u00D7\u0192\u00E1\u00ED\u00F3\u00FA\u00F1\u00D1\u00AA\u00BA\u00BF\u00AE\u00AC\u00BD\u00BC\u00A1\u00AB\u00BB\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255D\u255C\u255B\u2510\u2514\u2534\u252C\u251C\u2500\u253C\u255E\u255F\u255A\u2554\u2569\u2566\u2560\u2550\u256C\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256B\u256A\u2518\u250C\u2588\u2584\u258C\u2590\u2580\u03B1\u03B2\u0393\u03C0\u03A3\u03C3\u03BC\u03C4\u03A6\u0398\u03A9\u03B4\u221E\u2205\u2208\u2229\u2261\u00B1\u2265\u2264\u2320\u2321\u00F7\u2248\u00B0\u2219\u00B7\u221A\u207F\u00B2\u25A0\u0000"
                        .indexOf(c0);
                if (this.randomStyle && j != -1) {
                    do {
                        k = this.fontRandom.nextInt(this.charWidth.length);
                    } while (this.charWidth[j] != this.charWidth[k]);

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

    @Override
    protected void doDraw(float f) {
        Tessellator tessellator = Tessellator.instance;

        if (this.strikethroughStyle) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            tessellator.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) (this.FONT_HEIGHT / 2), 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0D);
            tessellator.addVertex(this.posX, this.posY + (float) (this.FONT_HEIGHT / 2) - 1.0F, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        if (this.underlineStyle) {
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            tessellator.startDrawingQuads();
            int l = this.underlineStyle ? -1 : 0;
            tessellator.addVertex(this.posX + (float) l, this.posY + (float) this.FONT_HEIGHT, 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) this.FONT_HEIGHT, 0.0D);
            tessellator.addVertex(this.posX + f, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0D);
            tessellator.addVertex(this.posX + (float) l, this.posY + (float) this.FONT_HEIGHT - 1.0F, 0.0D);
            tessellator.draw();
            GL11.glEnable(GL11.GL_TEXTURE_2D);
        }

        this.posX += (float) ((int) f);
    }

    private float renderCharAtPos(int p_78278_1_, char p_78278_2_, boolean p_78278_3_) {
        return p_78278_2_ == ' ' ? 4.0F
                : ("\u00C0\u00C1\u00C2\u00C8\u00CA\u00CB\u00CD\u00D3\u00D4\u00D5\u00DA\u00DF\u00E3\u00F5\u011F\u0130\u0131\u0152\u0153\u015E\u015F\u0174\u0175\u017E\u0207\u0000\u0000\u0000\u0000\u0000\u0000\u0000 !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~\u0000\u00C7\u00FC\u00E9\u00E2\u00E4\u00E0\u00E5\u00E7\u00EA\u00EB\u00E8\u00EF\u00EE\u00EC\u00C4\u00C5\u00C9\u00E6\u00C6\u00F4\u00F6\u00F2\u00FB\u00F9\u00FF\u00D6\u00DC\u00F8\u00A3\u00D8\u00D7\u0192\u00E1\u00ED\u00F3\u00FA\u00F1\u00D1\u00AA\u00BA\u00BF\u00AE\u00AC\u00BD\u00BC\u00A1\u00AB\u00BB\u2591\u2592\u2593\u2502\u2524\u2561\u2562\u2556\u2555\u2563\u2551\u2557\u255D\u255C\u255B\u2510\u2514\u2534\u252C\u251C\u2500\u253C\u255E\u255F\u255A\u2554\u2569\u2566\u2560\u2550\u256C\u2567\u2568\u2564\u2565\u2559\u2558\u2552\u2553\u256B\u256A\u2518\u250C\u2588\u2584\u258C\u2590\u2580\u03B1\u03B2\u0393\u03C0\u03A3\u03C3\u03BC\u03C4\u03A6\u0398\u03A9\u03B4\u221E\u2205\u2208\u2229\u2261\u00B1\u2265\u2264\u2320\u2321\u00F7\u2248\u00B0\u2219\u00B7\u221A\u207F\u00B2\u25A0\u0000"
                        .indexOf(p_78278_2_) != -1 && !this.unicodeFlag ? this.renderDefaultChar(p_78278_1_, p_78278_3_)
                                : this.renderUnicodeChar(p_78278_2_, p_78278_3_));
    }

    @Override
    protected float renderDefaultChar(int p_78266_1_, boolean p_78266_2_) {
        return super.renderDefaultChar(p_78266_1_, p_78266_2_);
    }

    @Override
    protected float renderUnicodeChar(char p_78277_1_, boolean p_78277_2_) {
        return super.renderUnicodeChar(p_78277_1_, p_78277_2_);
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

    @Override
    protected void setColor(float r, float g, float b, float a) {
        super.setColor(r, g, b, a);
    }

    @Override
    protected void enableAlpha() {
        super.enableAlpha();
    }

    @Override
    protected void bindTexture(ResourceLocation location) {
        super.bindTexture(location);
    }

    @Override
    protected InputStream getResourceInputStream(ResourceLocation location) throws IOException {
        return super.getResourceInputStream(location);
    }

    private int drawStringFront(String text, int x, int y, int color) {
        GL11.glEnable(3008);
        resetStyles();
        return renderString(text, x, y, color, false);
    }

    private int drawStringBack(String text, int x, int y, int color) {
        GL11.glEnable(3008);
        resetStyles();
        return renderString(text, x + 1, y + 1, color, true);
    }

    @Override
    public int getStringWidth(String p_78256_1_) {
        if (Minecraft.getMinecraft().gameSettings.guiScale == 1) {
            return Minecraft.getMinecraft().fontRenderer.getStringWidth(p_78256_1_);
        }
        if (p_78256_1_ == null) {
            return 0;
        } else {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < p_78256_1_.length(); ++j) {
                char c0 = p_78256_1_.charAt(j);
                int k = this.getCharWidth(c0);

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
