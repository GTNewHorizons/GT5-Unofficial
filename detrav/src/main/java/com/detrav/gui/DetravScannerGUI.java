package com.detrav.gui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.opengl.GL11;

import com.detrav.gui.textures.DetravMapTexture;

import gregtech.api.util.GT_Utility;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravScannerGUI extends GuiScreen {

    public static final int GUI_ID = 20;
    private static DetravMapTexture map = null;
    OresList oresList = null;

    private final static int minHeight = 128;
    private final static int minWidth = 128;
    private int prevW;
    private int prevH;

    private static final ResourceLocation back = new ResourceLocation("gregtech:textures/gui/propick.png");

    public DetravScannerGUI() {

    }

    public static void newMap(DetravMapTexture aMap) {
        if (map != null) {
            map.deleteGlTexture();
            map = null;
        }
        map = aMap;
        map.loadTexture(null);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if (map == null) return;
        int currentWidth = Math.max(map.width, minWidth);
        int currentHeight = Math.max(map.height, minHeight);
        int aX = (this.width - currentWidth - 100) / 2;
        int aY = (this.height - currentHeight) / 2;

        if (oresList == null || (prevW != width || prevH != height)) {
            oresList = new OresList(
                    this,
                    100,
                    currentHeight,
                    aY,
                    aY + currentHeight,
                    aX + currentWidth,
                    10,
                    map.packet.ores,
                    ((name, invert) -> { if (map != null) map.loadTexture(null, name, invert); }));
            prevW = width;
            prevH = height;
        }

        // draw back for ores
        drawRect(aX, aY, aX + currentWidth + 100, aY + currentHeight, 0xFFC6C6C6);
        map.glBindTexture();
        map.draw(aX, aY);
        oresList.drawScreen(x, y, f);
        mc.getTextureManager().bindTexture(back);
        GL11.glColor4f(0xFF, 0xFF, 0xFF, 0xFF);

        // draw corners
        drawTexturedModalRect(aX - 5, aY - 5, 0, 0, 5, 5);// leftTop
        drawTexturedModalRect(aX + currentWidth + 100, aY - 5, 171, 0, 5, 5);// RightTop
        drawTexturedModalRect(aX - 5, aY + currentHeight, 0, 161, 5, 5);// leftDown
        drawTexturedModalRect(aX + currentWidth + 100, aY + currentHeight, 171, 161, 5, 5);// RightDown

        // draw edges
        for (int i = aX; i < aX + currentWidth + 100; i += 128)
            drawTexturedModalRect(i, aY - 5, 5, 0, Math.min(128, aX + currentWidth + 100 - i), 5); // top
        for (int i = aX; i < aX + currentWidth + 100; i += 128)
            drawTexturedModalRect(i, aY + currentHeight, 5, 161, Math.min(128, aX + currentWidth + 100 - i), 5); // down
        for (int i = aY; i < aY + currentHeight; i += 128)
            drawTexturedModalRect(aX - 5, i, 0, 5, 5, Math.min(128, aY + currentHeight - i)); // left
        for (int i = aY; i < aY + currentHeight; i += 128)
            drawTexturedModalRect(aX + currentWidth + 100, i, 171, 5, 5, Math.min(128, aY + currentHeight - i)); // right

        if (map.packet.ptype == 2) {
            HashMap<Byte, Short>[][] fluidInfo = map.packet.map;
            int tX = x - aX;
            int tY = y - aY;
            if (tX >= 0 && tY >= 0 && tX < fluidInfo.length && tY < fluidInfo[0].length) {
                List<String> info = new ArrayList<>();
                if (fluidInfo[tX][tY] != null) {
                    short fluidId = fluidInfo[tX][tY].get((byte) 1);
                    short fluidAmount = fluidInfo[tX][tY].get((byte) 2);
                    if (fluidId != 0 && fluidAmount > 0) {
                        info.add(
                                StatCollector.translateToLocal("gui.detrav.scanner.tooltip.fluid_name")
                                        + map.packet.metaMap.get(fluidId));
                        info.add(
                                StatCollector.translateToLocal("gui.detrav.scanner.tooltip.fluid_amount")
                                        + GT_Utility.formatNumbers(fluidAmount)
                                        + " L");
                    } else info.add(StatCollector.translateToLocal("gui.detrav.scanner.tooltip.no_fluid"));
                } else {
                    info.add(StatCollector.translateToLocal("gui.detrav.scanner.tooltip.no_fluid"));
                }
                func_146283_a(info, x, y);
            }
        }
    }

}
