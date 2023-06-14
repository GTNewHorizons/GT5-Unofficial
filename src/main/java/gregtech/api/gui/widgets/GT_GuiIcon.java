package gregtech.api.gui.widgets;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Arrays;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;

import gregtech.api.interfaces.IGuiIcon;

public enum GT_GuiIcon implements IGuiIcon {

    BUTTON_NORMAL(0, 0, 0),
    BUTTON_DOWN(0, 32, 0),
    BUTTON_HIGHLIGHT(0, 32 * 2, 0),
    BUTTON_HIGHLIGHT_DOWN(0, 32 * 3, 0),
    BUTTON_DISABLED(0, 32 * 4, 0),

    DISABLE(0, 0, 32),
    REDSTONE_OFF(0, 32, 32),
    REDSTONE_ON(0, 32 * 2, 32),
    CHECKMARK(0, 32 * 3, 32),
    CROSS(0, 32 * 4, 32),
    WHITELIST(0, 32 * 5, 32),
    BLACKLIST(0, 32 * 6, 32),
    PROGRESS(0, 32 * 7, 32),

    EXPORT(0, 0, 32 * 2),
    IMPORT(0, 32, 32 * 2),
    ALLOW_INPUT(0, 32 * 2, 32 * 2),
    BLOCK_INPUT(0, 32 * 3, 32 * 2),
    GREEN_ARROW_UP(0, 32 * 4, 32 * 2),
    GREEN_ARROW_DOWN(0, 32 * 5, 32 * 2),
    CYCLIC(0, 32 * 6, 32 * 2),

    AND_GATE(0, 0, 32 * 3),
    NAND_GATE(0, 32, 32 * 3),
    OR_GATE(0, 32 * 2, 32 * 3),
    NOR_GATE(0, 32 * 3, 32 * 3),
    ANALOG_MODE(0, 32 * 4, 32 * 3),

    SLOT_DARKGRAY(1, 176, 0, 18, 18),
    SLOT_GRAY(1, 176, 18, 18, 18),

    TAB_NORMAL(2, 0, 0, 18, 20),
    TAB_HIGHLIGHT(2, 18, 0, 18, 20),
    TAB_DISABLED(2, 18 * 2, 0, 18, 20),
    TAB_NORMAL_BRONZE(2, 0, 20, 18, 20),
    TAB_HIGHLIGHT_BRONZE(2, 18, 20, 18, 20),
    TAB_DISABLED_BRONZE(2, 18 * 2, 20, 18, 20),
    TAB_NORMAL_STEEL(2, 0, 2 * 20, 18, 20),
    TAB_HIGHLIGHT_STEEL(2, 18, 2 * 20, 18, 20),
    TAB_DISABLED_STEEL(2, 18 * 2, 2 * 20, 18, 20),
    TAB_NORMAL_BRICK(2, 0, 3 * 20, 18, 20),
    TAB_HIGHLIGHT_BRICK(2, 18, 3 * 20, 18, 20),
    TAB_DISABLED_BRICK(2, 18 * 2, 3 * 20, 18, 20),
    TAB_INFO_GRAY(2, 220, 0, 18, 20),
    TAB_INFO_BLUE(2, 220 + 18, 0, 18, 20),;

    private static final int T_SIZE = 256;
    private static ResourceLocation[] TEXTURES = { new ResourceLocation(GregTech.ID, "textures/gui/GuiButtons.png"),
        new ResourceLocation(GregTech.ID, "textures/gui/GuiCover.png"),
        new ResourceLocation(GregTech.ID, "textures/gui/GuiTabs.png"), };

    public final int x, y, width, height;
    public final IGuiIcon overlay;
    private final int texID;

    GT_GuiIcon(int texID, int x, int y, int width, int height, IGuiIcon overlay) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.overlay = overlay;
        this.texID = texID;
    }

    GT_GuiIcon(int texID, int x, int y) {
        this(texID, x, y, 32, 32, null);
    }

    GT_GuiIcon(int texID, int x, int y, int width, int height) {
        this(texID, x, y, width, height, null);
    }

    public static void render(IGuiIcon icon, double x, double y, double width, double height, double zLevel,
        boolean doDraw) {
        render(icon, x, y, width, height, zLevel, doDraw, false);
    }

    public static void render(IGuiIcon icon, double x, double y, double width, double height, double zLevel,
        boolean doDraw, boolean flipHoritontally) {
        Tessellator tess = Tessellator.instance;
        if (doDraw) {
            Minecraft.getMinecraft().renderEngine.bindTexture(TEXTURES[icon.getTexId()]);
            tess.startDrawingQuads();
        }
        double minU = (double) (icon.getX() + (flipHoritontally ? icon.getWidth() : 0)) / T_SIZE;
        double maxU = (double) (icon.getX() + (flipHoritontally ? 0 : icon.getWidth())) / T_SIZE;
        double minV = (double) icon.getY() / T_SIZE;
        double maxV = (double) (icon.getY() + icon.getHeight()) / T_SIZE;
        tess.addVertexWithUV(x, y + height, zLevel, minU, maxV);
        tess.addVertexWithUV(x + width, y + height, zLevel, maxU, maxV);
        tess.addVertexWithUV(x + width, y + 0, zLevel, maxU, minV);
        tess.addVertexWithUV(x, y + 0, zLevel, minU, minV);

        if (icon.getOverlay() != null) render(icon.getOverlay(), x, y, width, height, zLevel, false);

        if (doDraw) tess.draw();
    }

    /**
     * This is intended to enable addon mods to register additional textures. They can then add to this enum using
     * EnumHelper.addEnum or by creating their enum that implements IGuiIcon (still requires adding a texture here)
     *
     * @param location location of the texture to add
     */
    public static void addTextures(ResourceLocation... location) {
        if (location == null || location.length == 0) return;

        int startIndex = TEXTURES.length;
        TEXTURES = Arrays.copyOf(TEXTURES, location.length);
        System.arraycopy(location, 0, TEXTURES, startIndex, location.length);
    }

    @Override
    public int getX() {
        return this.x;
    }

    @Override
    public int getY() {
        return this.y;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public int getTexId() {
        return this.texID;
    }

    @Override
    public IGuiIcon getOverlay() {
        return this.overlay;
    }
}
