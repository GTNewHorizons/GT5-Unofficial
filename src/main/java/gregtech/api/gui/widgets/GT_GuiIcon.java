package gregtech.api.gui.widgets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

public enum GT_GuiIcon {
    BUTTON_NORMAL           (0, 0,     0),
    BUTTON_DOWN             (0, 32,    0),
    BUTTON_HIGHLIGHT        (0, 32*2,  0),
    BUTTON_HIGHLIGHT_DOWN   (0, 32*3,  0),
    BUTTON_DISABLED         (0, 32*4,  0),

    DISABLE                 (0, 0,     32),
    REDSTONE_OFF            (0, 32,    32),
    REDSTONE_ON             (0, 32*2,  32),
    CHECKMARK               (0, 32*3,  32),
    CROSS                   (0, 32*4,  32),
    WHITELIST               (0, 32*5,  32),
    BLACKLIST               (0, 32*6,  32),

    EXPORT                  (0, 0,     32*2),
    IMPORT                  (0, 32,    32*2),
    ALLOW_INPUT             (0, 32*2,  32*2),
    BLOCK_INPUT             (0, 32*3,  32*2),

    SLOT_DARKGRAY           (1, 176,0,18,18),
    SLOT_GRAY               (1, 176,18,18,18);

    private static final int T_SIZE = 256;
    private static final ResourceLocation[] TEXTURES = {
            new ResourceLocation("gregtech", "textures/gui/GuiButtons.png"),
            new ResourceLocation("gregtech", "textures/gui/GuiCover.png")
    };

    public final int x, y, width, height;
    public final GT_GuiIcon overlay;
    private final int texID;

    GT_GuiIcon(int texID, int x, int y, int width, int height, GT_GuiIcon overlay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.overlay = overlay;
        this.texID = texID;
    }

    GT_GuiIcon(int texID, int x, int y) {
        this(texID, x, y,32,32,null);
    }
    GT_GuiIcon(int texID, int x, int y, int width, int height) {
        this(texID, x, y, width, height,null);
    }

    public static void render(GT_GuiIcon icon, double x, double y, double width, double height, double zLevel, boolean doDraw) {
        Tessellator tess = Tessellator.instance;
        if (doDraw) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES[icon.texID]);
            tess.startDrawingQuads();
        }
        double minU = (double) icon.x / T_SIZE;
        double maxU = (double) (icon.x + icon.width) / T_SIZE;
        double minV = (double) icon.y / T_SIZE;
        double maxV = (double) (icon.y + icon.height) / T_SIZE;
        tess.addVertexWithUV(x, y + height, zLevel, minU, maxV);
        tess.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
        tess.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
        tess.addVertexWithUV(x, y + 0, zLevel, minU, minV);

        if (icon.overlay != null)
            render(icon.overlay, x, y, width, height, zLevel, false);

        if (doDraw)
            tess.draw();
    }
}
