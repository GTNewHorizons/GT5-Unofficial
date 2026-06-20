package detrav.gui;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import detrav.gui.textures.DetravMapTexture;
import detrav.items.DetravMetaGeneratedTool01;
import gregtech.api.util.GTUtility;

/**
 * Created by wital_000 on 21.03.2016.
 */
public class DetravScannerGUI extends GuiScreen {

    public static final int GUI_ID = 20;
    private static DetravMapTexture map = null;
    private OresList oresList = null;
    private GuiTextField searchField = null;

    private final static int minHeight = 128;
    private final static int minWidth = 128;
    private final static int searchHeight = 14; // height of the search box
    private final static int hintHeight = 5; // height of the hint line below ore list
    private final static int scrollbar = 4; // thickness of the map scrollbars

    // layout, recomputed in initGui()
    private int aX, aY;
    private int mapW, mapH;
    private int listX, listW;
    private int viewW, viewH; // visible area of the (full-scale) map
    // scroll offset into the full-scale map
    private int panX, panY;
    private int maxPanX, maxPanY;
    private boolean panInit = false;
    private int dragMode = 0; // 0 = none, 1 = grab-pan, 2 = horizontal bar, 3 = vertical bar
    private int dragLastX, dragLastY;
    private boolean wasMouseDown = false;
    private int frameTexW, frameTexH; // real pixel size of the frame texture
    private String searchText = "";

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
    public void initGui() {
        Keyboard.enableRepeatEvents(true);
        if (map == null) return;

        // size the list column to the longest entry, clamped to keep the GUI sane
        int names = 100;
        for (var e : map.packet.objects.short2ObjectEntrySet()) {
            names = Math.max(
                names,
                mc.fontRenderer.getStringWidth(
                    e.getValue()
                        .left())
                    + 24);
        }
        listW = Math.min(names, 220);

        // the map is drawn at full 1:1 scale inside a viewport; when the scan is larger than the
        // window (big range and/or high GUI scale) the viewport scrolls instead of shrinking.
        viewW = Math.min(map.width, Math.max(minWidth, this.width - listW - 12));
        viewH = Math.min(map.height, Math.max(minHeight, this.height - 12));
        maxPanX = Math.max(0, map.width - viewW);
        maxPanY = Math.max(0, map.height - viewH);

        mapW = Math.max(viewW, minWidth);
        mapH = Math.max(viewH, minHeight);

        aX = (this.width - mapW - listW) / 2;
        aY = (this.height - mapH) / 2;
        listX = aX + mapW;

        if (!panInit) {
            // start centred on the player marker
            panX = map.packet.posX - (map.packet.chunkX - map.packet.size) * 16 - 1 - viewW / 2;
            panY = map.packet.posZ - (map.packet.chunkZ - map.packet.size) * 16 - 1 - viewH / 2;
            panInit = true;
        }
        panX = clamp(panX, 0, maxPanX);
        panY = clamp(panY, 0, maxPanY);

        searchField = new GuiTextField(mc.fontRenderer, listX + 1, aY, listW - 2, searchHeight);
        searchField.setMaxStringLength(64);
        searchField.setText(searchText);

        int listTop = aY + searchHeight + 1;
        int listBottom = aY + mapH - hintHeight;

        oresList = new OresList(
            this,
            listW,
            listBottom - listTop,
            listTop,
            listBottom,
            listX,
            10,
            map.packet,
            ((name, invert) -> { if (map != null) map.loadTexture(null, name, invert); }));
        oresList.setFilter(searchText);
    }

    @Override
    public void onGuiClosed() {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    public void updateScreen() {
        if (searchField != null) searchField.updateCursorCounter();
    }

    @Override
    protected void mouseClicked(int mx, int my, int btn) {
        super.mouseClicked(mx, my, btn);
        if (searchField != null) searchField.mouseClicked(mx, my, btn);
    }

    /** Polls the mouse to pan the map or drag its scrollbars. */
    private void updateMapDrag(int x, int y) {
        boolean down = Mouse.isButtonDown(0);

        if (down && !wasMouseDown) {
            if (maxPanY > 0 && x >= aX + viewW - scrollbar && x < aX + viewW && y >= aY && y < aY + viewH) {
                dragMode = 3;
            } else if (maxPanX > 0 && y >= aY + viewH - scrollbar && y < aY + viewH && x >= aX && x < aX + viewW) {
                dragMode = 2;
            } else if (inViewport(x, y) && (maxPanX > 0 || maxPanY > 0)) {
                dragMode = 1;
                dragLastX = x;
                dragLastY = y;
            }
        }

        if (dragMode != 0) {
            if (!down) {
                dragMode = 0;
            } else if (dragMode == 1) {
                panX = clamp(panX - (x - dragLastX), 0, maxPanX);
                panY = clamp(panY - (y - dragLastY), 0, maxPanY);
                dragLastX = x;
                dragLastY = y;
            } else if (dragMode == 2) {
                dragToHBar(x);
            } else {
                dragToVBar(y);
            }
        }

        wasMouseDown = down;
    }

    /** Centres the horizontal view on the cursor position along the bottom scrollbar. */
    private void dragToHBar(int mx) {
        panX = clamp(Math.round((mx - aX) / (float) viewW * map.width - viewW / 2f), 0, maxPanX);
    }

    /** Centres the vertical view on the cursor position along the right scrollbar. */
    private void dragToVBar(int my) {
        panY = clamp(Math.round((my - aY) / (float) viewH * map.height - viewH / 2f), 0, maxPanY);
    }

    private static int clamp(int v, int min, int max) {
        return v < min ? min : Math.min(v, max);
    }

    @Override
    protected void keyTyped(char c, int key) {
        if (searchField != null && searchField.isFocused() && key != Keyboard.KEY_ESCAPE) {
            if (searchField.textboxKeyTyped(c, key)) {
                searchText = searchField.getText();
                if (oresList != null) oresList.setFilter(searchText);
            }
            return;
        }
        super.keyTyped(c, key);
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        this.drawDefaultBackground();
        if (map == null || oresList == null) return;

        updateMapDrag(x, y);

        // grey panel behind the map and the list
        drawRect(aX, aY, aX + mapW + listW, aY + mapH, 0xFFC6C6C6);

        // full-scale map, clipped to its viewport and scrolled by (panX, panY)
        ScaledResolution res = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
        int sf = res.getScaleFactor();
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(aX * sf, mc.displayHeight - (aY + viewH) * sf, viewW * sf, viewH * sf);

        map.glBindTexture();
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        map.draw(aX - panX, aY - panY);
        drawPlayerDirectionMarker();

        GL11.glDisable(GL11.GL_SCISSOR_TEST);

        // "North" label pinned to the top of the viewport
        mc.fontRenderer.drawStringWithShadow("N", aX + viewW / 2 - 2, aY + 1, 0xFFFFFFFF);

        drawMapScrollbars();

        searchField.drawTextBox();
        if (searchField.getText()
            .isEmpty() && !searchField.isFocused()) {
            mc.fontRenderer.drawString(
                StatCollector.translateToLocal("gui.detrav.scanner.search.hint"),
                listX + 4,
                aY + 3,
                0xFF808080);
        }

        // feed an out-of-bounds Y while dragging the map so the list does not latch and scroll too
        oresList.drawScreen(x, dragMode != 0 ? -1 : y, f);

        // colour inversion hint under list
        GL11.glPushMatrix();
        GL11.glScalef(0.5F, 0.5F, 1F);
        String hint = mc.fontRenderer.trimStringToWidth(
            EnumChatFormatting.ITALIC + StatCollector.translateToLocal("gui.detrav.scanner.hint.invert"),
            (listW - 4) * 2);
        int hintX = (listX + listW - 2) * 2 - mc.fontRenderer.getStringWidth(hint);
        int hintY = (aY + mapH - hintHeight) * 2 + (hintHeight * 2 - 8) / 2;
        mc.fontRenderer.drawString(hint, hintX, hintY, 0xFF5e5e5e);
        GL11.glPopMatrix();

        mc.getTextureManager()
            .bindTexture(back);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        if (frameTexW <= 0) {
            frameTexW = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_WIDTH);
            frameTexH = GL11.glGetTexLevelParameteri(GL11.GL_TEXTURE_2D, 0, GL11.GL_TEXTURE_HEIGHT);
        }

        // draw corners
        drawFramePiece(aX - 5, aY - 5, 0, 0, 5, 5); // leftTop
        drawFramePiece(aX + mapW + listW, aY - 5, 171, 0, 5, 5); // RightTop
        drawFramePiece(aX - 5, aY + mapH, 0, 161, 5, 5); // leftDown
        drawFramePiece(aX + mapW + listW, aY + mapH, 171, 161, 5, 5); // RightDown

        // draw edges
        for (int i = aX; i < aX + mapW + listW; i += 128)
            drawFramePiece(i, aY - 5, 5, 0, Math.min(128, aX + mapW + listW - i), 5); // top
        for (int i = aX; i < aX + mapW + listW; i += 128)
            drawFramePiece(i, aY + mapH, 5, 161, Math.min(128, aX + mapW + listW - i), 5); // down
        for (int i = aY; i < aY + mapH; i += 128) drawFramePiece(aX - 5, i, 0, 5, 5, Math.min(128, aY + mapH - i)); // left
        for (int i = aY; i < aY + mapH; i += 128)
            drawFramePiece(aX + mapW + listW, i, 171, 5, 5, Math.min(128, aY + mapH - i)); // right

        // Mouse hover ore pos
        if (inViewport(x, y) && (map.packet.ptype == DetravMetaGeneratedTool01.MODE_BIG_ORES
            || map.packet.ptype == DetravMetaGeneratedTool01.MODE_ALL_ORES)) {
            int bx = x - aX + panX;
            int bz = y - aY + panY;

            String name = map.getTopOreName(bx, bz);
            if (name != null) {
                List<String> info = new ArrayList<>();
                int worldX = bx + (map.packet.chunkX - map.packet.size) * 16;
                int worldZ = bz + (map.packet.chunkZ - map.packet.size) * 16;

                info.add(name);
                info.add(StatCollector.translateToLocalFormatted("gui.detrav.scanner.tooltip.ore_pos", worldX, worldZ));
                info.add(
                    StatCollector
                        .translateToLocalFormatted("gui.detrav.scanner.tooltip.ore_depth", map.getTopOreY(bx, bz)));
                func_146283_a(info, x, y);
            }
        }

        if (inViewport(x, y) && map.packet.ptype == DetravMetaGeneratedTool01.MODE_FLUIDS) {
            int cX = (x - aX + panX) / 16;
            int cZ = (y - aY + panY) / 16;

            if (cX >= 0 && cZ >= 0 && cX < map.packet.size * 2 + 1 && cZ < map.packet.size * 2 + 1) {
                List<String> info = new ArrayList<>();

                short objectId = map.packet.map.getOrDefault(CoordinatePacker.pack(cX, 0, cZ), (short) -1);
                int amount = map.packet.getAmount(cX, cZ);

                if (objectId != -1 && amount > 0) {
                    var object = map.packet.objects.get(objectId);

                    info.add(StatCollector.translateToLocal("gui.detrav.scanner.tooltip.fluid_name") + object.left());
                    info.add(
                        StatCollector.translateToLocal("gui.detrav.scanner.tooltip.fluid_amount") + formatNumber(amount)
                            + " L");
                } else {
                    info.add(StatCollector.translateToLocal("gui.detrav.scanner.tooltip.no_fluid"));
                }
                func_146283_a(info, x, y);
            }
        }

        if (inViewport(x, y) && map.packet.ptype == DetravMetaGeneratedTool01.MODE_POLLUTION) {
            int cX = (x - aX + panX) / 16;
            int cZ = (y - aY + panY) / 16;

            if (cX >= 0 && cZ >= 0 && cX < map.packet.size * 2 + 1 && cZ < map.packet.size * 2 + 1) {
                List<String> info = new ArrayList<>();

                int amount = map.packet.getAmount(cX, cZ);

                if (amount > 0) {
                    info.add(
                        StatCollector.translateToLocal("gui.detrav.scanner.pollution") + ": "
                            + formatNumber(amount)
                            + GTUtility.trans("203", " gibbl"));
                }

                func_146283_a(info, x, y);
            }
        }
    }

    private boolean inViewport(int x, int y) {
        return x >= aX && x < aX + viewW && y >= aY && y < aY + viewH;
    }

    /** Overlay scrollbars on the bottom/right edges of the map viewport when it is larger than the window. */
    private void drawMapScrollbars() {
        if (maxPanX > 0 && maxPanY > 0) {
            // fill the gap in the corner where the two scrollbars meet
            drawRect(aX + viewW - scrollbar, aY + viewH - scrollbar, aX + viewW, aY + viewH, 0x80000000);
        }
        if (maxPanX > 0) {
            int trackW = viewW - (maxPanY > 0 ? scrollbar : 0);
            int top = aY + viewH - scrollbar;
            drawRect(aX, top, aX + trackW, top + scrollbar, 0x80000000);
            int thumbW = Math.max(12, (int) ((long) trackW * viewW / map.width));
            int thumbX = aX + (int) ((long) (trackW - thumbW) * panX / maxPanX);
            drawRect(thumbX, top, thumbX + thumbW, top + scrollbar, 0xFFB0B0B0);
        }
        if (maxPanY > 0) {
            int trackH = viewH - (maxPanX > 0 ? scrollbar : 0);
            int left = aX + viewW - scrollbar;
            drawRect(left, aY, left + scrollbar, aY + trackH, 0x80000000);
            int thumbH = Math.max(12, (int) ((long) trackH * viewH / map.height));
            int thumbY = aY + (int) ((long) (trackH - thumbH) * panY / maxPanY);
            drawRect(left, thumbY, left + scrollbar, thumbY + thumbH, 0xFFB0B0B0);
        }
    }

    private void drawPlayerDirectionMarker() {
        if (mc.thePlayer == null) return;

        int playerI = map.packet.posX - (map.packet.chunkX - map.packet.size) * 16 - 1;
        int playerJ = map.packet.posZ - (map.packet.chunkZ - map.packet.size) * 16 - 1;

        if (playerI < 0 || playerJ < 0 || playerI >= map.width || playerJ >= map.height) return;

        drawPlayerHeading(aX - panX + playerI, aY - panY + playerJ, mc.thePlayer.rotationYaw);
    }

    private void drawFramePiece(int x, int y, int u, int v, int w, int h) {
        float fw = 1F / (frameTexW > 0 ? frameTexW : 256);
        float fh = 1F / (frameTexH > 0 ? frameTexH : 256);
        Tessellator t = Tessellator.instance;
        t.startDrawingQuads();
        t.addVertexWithUV(x, y + h, this.zLevel, u * fw, (v + h) * fh);
        t.addVertexWithUV(x + w, y + h, this.zLevel, (u + w) * fw, (v + h) * fh);
        t.addVertexWithUV(x + w, y, this.zLevel, (u + w) * fw, v * fh);
        t.addVertexWithUV(x, y, this.zLevel, u * fw, v * fh);
        t.draw();
    }

    private void drawPlayerHeading(int cx, int cy, float yaw) {
        GL11.glPushMatrix();
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_CULL_FACE);
        GL11.glTranslatef(cx + 0.5F, cy + 0.5F, 0F);
        GL11.glRotatef(yaw + 180F, 0F, 0F, 1F);

        Tessellator t = Tessellator.instance;

        // Teardrop perimeter: round bottom, sharp tip at top (0,-14). Center ~(0,2).
        float[][] body = { { 6.06F, -1.5F }, { 7F, 2F }, { 6F, 5F }, { 3F, 8F }, { 0F, 9F }, { -3F, 8F }, { -6F, 5F },
            { -7F, 2F }, { -6.06F, -1.5F }, { 0F, -14F } };
        float ccx = 0F, ccy = 2F;

        fanShape(t, body, ccx, ccy, 0.9F, 0xEDD8FF); // Outline
        fanShape(t, body, ccx, ccy, 0.75F, 0xC071FF); // fill

        GL11.glEnable(GL11.GL_CULL_FACE);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glPopMatrix();
        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    // Fills a closed polygon as a fan, scaled about (cx,cy). Scale > 1 = the outline pass.
    private void fanShape(Tessellator t, float[][] pts, float cx, float cy, float scale, int color) {
        t.startDrawing(GL11.GL_TRIANGLE_FAN);
        t.setColorOpaque_I(color);
        t.addVertex(cx, cy, 0); // fan hub
        for (int i = 0; i <= pts.length; i++) {
            float[] p = pts[i % pts.length]; // repeat first vertex to close
            t.addVertex(cx + (p[0] - cx) * scale, cy + (p[1] - cy) * scale, 0);
        }
        t.draw();
    }
}
