package com.github.technus.tectech.font;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@SideOnly(Side.CLIENT)
public class TecTechFontRender extends FontRenderer {
    public static final TecTechFontRender INSTANCE = new TecTechFontRender();

    private static float DISTANCE_L = .125F;
    private static float DISTANCE_L2 = DISTANCE_L *2F;

    private static float DISTANCE_M = 0.06F;
    private static float DISTANCE_M2 = DISTANCE_M *2F;

    private static float DISTANCE_A = 0.06F;
    private static float DISTANCE_A2 = DISTANCE_A *2F;

    private static final Method reset;
    private static final Method render;

    private final GameSettings gameSettings;

    static {
        Method resetMethod,renderMethod;
        try {
            resetMethod =FontRenderer.class.getDeclaredMethod("resetStyles");
            renderMethod=FontRenderer.class.getDeclaredMethod("renderString", String.class, int.class, int.class, int.class, boolean.class);
        } catch (NoSuchMethodException e) {
            try {
                resetMethod =FontRenderer.class.getDeclaredMethod("func_78265_b");
                renderMethod=FontRenderer.class.getDeclaredMethod("func_78258_a", String.class, int.class, int.class, int.class, boolean.class);
            } catch (NoSuchMethodException ex) {
                throw new RuntimeException("Cannot get methods!",ex);
            }
        }
        resetMethod.setAccessible(true);
        renderMethod.setAccessible(true);
        reset=resetMethod;
        render=renderMethod;
    }

    private TecTechFontRender() {
        super(Minecraft.getMinecraft().gameSettings, new ResourceLocation("textures/font/ascii.png"), Minecraft.getMinecraft().renderEngine, false);
        gameSettings = Minecraft.getMinecraft().gameSettings;
    }

    private void resetStyles2(){
        try {
            reset.invoke(this);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot run method resetStyles!",e);
        }
    }

    private int renderString2(String str, int x, int y, int color, boolean dropShadow){
        try {
            return (int)render.invoke(this,str,x,y,color,dropShadow);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException("Cannot run method renderString!",e);
        }
    }


    @Override
    public int drawString(String str, int x, int y, int color, boolean dropShadow) {
        switch (gameSettings.guiScale){
            case 0:
                setUnicodeFlag(true);
                y--;
                GL11.glPushMatrix();

                if (dropShadow)
                {
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

                if (dropShadow)
                {
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

                if (dropShadow)
                {
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
        switch (gameSettings.guiScale){
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
        resetStyles2();
        return renderString2(p_85187_1_, p_85187_2_, p_85187_3_, p_85187_4_, false);
    }

    private int drawStringBack(String p_85187_1_, int p_85187_2_, int p_85187_3_, int p_85187_4_) {
        GL11.glEnable(3008);
        resetStyles2();
        return renderString2(p_85187_1_, p_85187_2_ + 1, p_85187_3_ + 1, p_85187_4_, true);
    }

    @Override
    public int getStringWidth(String p_78256_1_)
    {
        if(gameSettings.guiScale==1){
            return Minecraft.getMinecraft().fontRenderer.getStringWidth(p_78256_1_);
        }
        if (p_78256_1_ == null)
        {
            return 0;
        }
        else
        {
            int i = 0;
            boolean flag = false;

            for (int j = 0; j < p_78256_1_.length(); ++j)
            {
                char c0 = p_78256_1_.charAt(j);
                int k = this.getCharWidth(c0);

                if (k < 0 && j < p_78256_1_.length() - 1)
                {
                    ++j;
                    c0 = p_78256_1_.charAt(j);

                    if (c0 != 108 && c0 != 76)
                    {
                        if (c0 == 114 || c0 == 82)
                        {
                            flag = false;
                        }
                    }
                    else
                    {
                        flag = true;
                    }

                    k = 0;
                }

                i += k;

                if (flag && k > 0)
                {
                    ++i;
                }
            }

            return i;
        }
    }
}
