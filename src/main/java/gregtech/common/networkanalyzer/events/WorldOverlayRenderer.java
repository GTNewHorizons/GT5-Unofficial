package gregtech.common.networkanalyzer.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.util.ColorUtils;
import gregtech.api.util.GTUtility;
import gregtech.api.util.Localized;
import gregtech.common.tools.ItemNetworkAnalyzer;

public class WorldOverlayRenderer {

    private static final float NONE_SIZE = 0.10f;
    private static final float FORK_SIZE = 0.15f;
    private static final float TERMINAL_SIZE = 0.2f;

    public enum GraphNodeState {

        INPUT,
        OUTPUT,
        FORK;

        public static final GraphNodeState[] VALUES = values();

        public int[] baseColor() {
            return switch (this) {
                case INPUT -> unpackRGB(ColorUtils.networkAnalyzerNodeInput.getColor());
                case OUTPUT -> unpackRGB(ColorUtils.networkAnalyzerNodeOutput.getColor());
                case FORK -> unpackRGB(ColorUtils.networkAnalyzerNodeFork.getColor());
            };
        }

        public float size() {
            return this == FORK ? FORK_SIZE : TERMINAL_SIZE;
        }
    }

    public enum SeverityLevel {

        NONE,
        SUCCESS,
        WARNING,
        ALERT;

        public static final SeverityLevel[] VALUES = values();

        public int[] color() {
            return switch (this) {
                case NONE -> unpackRGB(ColorUtils.networkAnalyzerSeverityNone.getColor());
                case SUCCESS -> unpackRGB(ColorUtils.networkAnalyzerSeveritySuccess.getColor());
                case WARNING -> unpackRGB(ColorUtils.networkAnalyzerSeverityWarning.getColor());
                case ALERT -> unpackRGB(ColorUtils.networkAnalyzerSeverityAlert.getColor());
            };
        }
    }

    public static class OverlayNode {

        public final int x;
        public final int y;
        public final int z;

        public final GraphNodeState state;
        public final SeverityLevel level;

        public final Localized[] label;
        public final Localized[] tooltip;

        public OverlayNode(int x, int y, int z, GraphNodeState state, SeverityLevel level, Localized[] label,
            Localized[] tooltip) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.state = state;
            this.level = level;
            this.label = label;
            this.tooltip = tooltip;
        }

        public OverlayNode(TileEntity tile, GraphNodeState state, SeverityLevel level, Localized[] label,
            Localized[] tooltip) {
            this(tile.xCoord, tile.yCoord, tile.zCoord, state, level, label, tooltip);
        }

        public float size() {
            return level == SeverityLevel.NONE || state == null ? NONE_SIZE : state.size();
        }

        public int[] color() {
            return level == SeverityLevel.SUCCESS && state != null ? state.baseColor() : level.color();
        }

        public boolean isTerminal() {
            return level != SeverityLevel.NONE && state != null && state != GraphNodeState.FORK;
        }
    }

    public static class OverlayEdge {

        public final int x1;
        public final int y1;
        public final int z1;

        public final int x2;
        public final int y2;
        public final int z2;

        public final SeverityLevel level;

        public OverlayEdge(int x1, int y1, int z1, int x2, int y2, int z2, SeverityLevel level) {
            this.x1 = x1;
            this.y1 = y1;
            this.z1 = z1;
            this.x2 = x2;
            this.y2 = y2;
            this.z2 = z2;
            this.level = level;
        }

        public OverlayEdge(TileEntity tileA, TileEntity tileB, SeverityLevel level) {
            this(tileA.xCoord, tileA.yCoord, tileA.zCoord, tileB.xCoord, tileB.yCoord, tileB.zCoord, level);
        }

        public int[] color() {
            return level.color();
        }
    }

    private static final double MAX_LABEL_DISTANCE_SQ = 36 * 36;
    private static final double MAX_NODE_DISTANCE_SQ = 36 * 36;
    private static final float LINE_WIDTH = 3f;

    public static List<OverlayNode> nodes = new ArrayList<>();
    public static List<OverlayEdge> edges = new ArrayList<>();

    public static boolean needListRefresh = true;
    public static int staticList;

    public static void init() {
        MinecraftForge.EVENT_BUS.register(new WorldOverlayRenderer());
        WorldOverlayRenderer.staticList = GL11.glGenLists(1);
    }

    @SubscribeEvent
    public void onRenderWorldLastEvent(RenderWorldLastEvent ev) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player == null) {
            return;
        }

        final ItemStack stack = player.inventory.getCurrentItem();

        if (stack == null || !(stack.getItem() instanceof ItemNetworkAnalyzer)
            || !stack.hasTagCompound()
            || nodes.isEmpty() && edges.isEmpty()) {
            return;
        }

        final EntityLivingBase p = Minecraft.getMinecraft().renderViewEntity;
        final double viewX = p.lastTickPosX + (p.posX - p.lastTickPosX) * ev.partialTicks;
        final double viewY = p.lastTickPosY + (p.posY - p.lastTickPosY) * ev.partialTicks;
        final double viewZ = p.lastTickPosZ + (p.posZ - p.lastTickPosZ) * ev.partialTicks;

        GL11.glPushMatrix();
        GL11.glTranslated(-viewX, -viewY, -viewZ);

        doRender(player, ev.partialTicks, viewX, viewY, viewZ);

        GL11.glPopMatrix();
    }

    private static void doRender(EntityClientPlayerMP player, float partialTicks, double viewX, double viewY,
        double viewZ) {
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT);

        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (needListRefresh) {
            needListRefresh = false;
            GL11.glNewList(staticList, GL11.GL_COMPILE);

            renderNodes(nodes, viewX, viewY, viewZ);

            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_NICEST);

            renderEdges(edges, LINE_WIDTH);

            GL11.glDisable(GL11.GL_LINE_SMOOTH);
            GL11.glHint(GL11.GL_LINE_SMOOTH_HINT, GL11.GL_DONT_CARE);

            GL11.glEndList();
        }

        GL11.glCallList(staticList);

        MovingObjectPosition position = GTUtility.getPlayerLookingTarget(player);

        if (position != null && position.typeOfHit != MovingObjectType.BLOCK) {
            position = null;
        }

        // Labels are rendered every frame because they need to face the camera
        for (OverlayNode node : nodes) {
            final double nx = node.x + 0.5d;
            final double ny = node.y + 0.5d;
            final double nz = node.z + 0.5d;

            if (position != null && node.tooltip != null
                && node.tooltip.length > 0
                && node.x == position.blockX
                && node.y == position.blockY
                && node.z == position.blockZ) {
                renderFloatingText(translate(node.tooltip), nx, ny, nz, 0xFFFFFF);
            } else if (node.label != null && node.label.length > 0
                && distSq(nx, ny, nz, viewX, viewY, viewZ) <= MAX_LABEL_DISTANCE_SQ) {
                    renderFloatingText(translate(node.label), nx, ny, nz, 0xFFFFFF);
                }
        }

        GL11.glPopAttrib();
    }

    private static String translate(Localized[] parts) {
        if (parts == null || parts.length == 0) return "";
        return Arrays.stream(parts)
            .map(Localized::toString)
            .collect(Collectors.joining("\n"));
    }

    private static double distSq(double x1, double y1, double z1, double x2, double y2, double z2) {
        final double dx = x1 - x2;
        final double dy = y1 - y2;
        final double dz = z1 - z2;

        return dx * dx + dy * dy + dz * dz;
    }

    private static int[] unpackRGB(int rgb) {
        return new int[] { (rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF };
    }

    private static void setColorRGBA_F(Tessellator tessellator, int argb) {
        tessellator.setColorRGBA_F(
            ((argb >> 16) & 0xFF) / 255f,
            ((argb >> 8) & 0xFF) / 255f,
            (argb & 0xFF) / 255f,
            ((argb >> 24) & 0xFF) / 255f);
    }

    private static void renderFloatingText(String text, double x, double y, double z, int color) {
        final RenderManager renderManager = RenderManager.instance;
        final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        final Tessellator tessellator = Tessellator.instance;

        final String[] lines = text.split("\n");
        final int lineHeight = fontRenderer.FONT_HEIGHT + 1;

        final float scale = 0.027f;
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);
        GL11.glNormal3f(0.0f, 1.0f, 0.0f);
        GL11.glRotatef(-renderManager.playerViewY, 0.0f, 1.0f, 0.0f);
        GL11.glRotatef(renderManager.playerViewX, 1.0f, 0.0f, 0.0f);
        GL11.glScalef(-scale, -scale, scale);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glDepthMask(false);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        int maxWidth = 0;
        for (String line : lines) {
            maxWidth = Math.max(maxWidth, fontRenderer.getStringWidth(line));
        }
        final int halfWidth = maxWidth / 2;
        final int totalHeight = lines.length * lineHeight;
        final int yOffset = -(totalHeight / 2);

        GL11.glDisable(GL11.GL_TEXTURE_2D);
        tessellator.startDrawingQuads();
        setColorRGBA_F(tessellator, ColorUtils.networkAnalyzerLabelBackground.getColor());
        tessellator.addVertex(-halfWidth - 1, yOffset - 1, 0.0d);
        tessellator.addVertex(-halfWidth - 1, yOffset + totalHeight, 0.0d);
        tessellator.addVertex(halfWidth + 1, yOffset + totalHeight, 0.0d);
        tessellator.addVertex(halfWidth + 1, yOffset - 1, 0.0d);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glColor4f(1f, 1f, 1f, 0.5f);
        for (int i = 0; i < lines.length; i++) {
            final int lineY = yOffset + i * lineHeight;
            final int lineWidth = fontRenderer.getStringWidth(lines[i]);
            fontRenderer.drawString(lines[i], -lineWidth / 2, lineY, color);
        }
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glDepthMask(true);
        for (int i = 0; i < lines.length; i++) {
            final int lineY = yOffset + i * lineHeight;
            final int lineWidth = fontRenderer.getStringWidth(lines[i]);
            fontRenderer.drawString(lines[i], -lineWidth / 2, lineY, color);
        }
        GL11.glEnable(GL11.GL_LIGHTING);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
        GL11.glPopMatrix();
    }

    private static void renderNodes(List<OverlayNode> nodes, double viewX, double viewY, double viewZ) {
        final Tessellator tess = Tessellator.instance;
        tess.startDrawing(GL11.GL_QUADS);

        for (OverlayNode node : nodes) {
            if (!node.isTerminal() && distSq(node.x, node.y, node.z, viewX, viewY, viewZ) > MAX_NODE_DISTANCE_SQ) {
                continue;
            }

            final float size = node.size();
            final double nx = node.x + 0.5d;
            final double ny = node.y + 0.5d;
            final double nz = node.z + 0.5d;
            final int[] color = node.color();

            tess.setColorRGBA(color[0], color[1], color[2], 255); // +Y
            tess.addVertex(nx - size, ny + size, nz + size);
            tess.addVertex(nx + size, ny + size, nz + size);
            tess.addVertex(nx + size, ny + size, nz - size);
            tess.addVertex(nx - size, ny + size, nz - size);

            tess.setColorRGBA(color[0] / 2, color[1] / 2, color[2] / 2, 255); // -Y
            tess.addVertex(nx + size, ny - size, nz - size);
            tess.addVertex(nx + size, ny - size, nz + size);
            tess.addVertex(nx - size, ny - size, nz + size);
            tess.addVertex(nx - size, ny - size, nz - size);

            tess.setColorRGBA(color[0] * 8 / 10, color[1] * 8 / 10, color[2] * 8 / 10, 255); // +/- Z
            tess.addVertex(nx + size, ny - size, nz + size);
            tess.addVertex(nx + size, ny + size, nz + size);
            tess.addVertex(nx - size, ny + size, nz + size);
            tess.addVertex(nx - size, ny - size, nz + size);
            tess.addVertex(nx - size, ny + size, nz - size);
            tess.addVertex(nx + size, ny + size, nz - size);
            tess.addVertex(nx + size, ny - size, nz - size);
            tess.addVertex(nx - size, ny - size, nz - size);

            tess.setColorRGBA(color[0] * 6 / 10, color[1] * 6 / 10, color[2] * 6 / 10, 255); // +/- X
            tess.addVertex(nx + size, ny + size, nz - size);
            tess.addVertex(nx + size, ny + size, nz + size);
            tess.addVertex(nx + size, ny - size, nz + size);
            tess.addVertex(nx + size, ny - size, nz - size);
            tess.addVertex(nx - size, ny - size, nz + size);
            tess.addVertex(nx - size, ny + size, nz + size);
            tess.addVertex(nx - size, ny + size, nz - size);
            tess.addVertex(nx - size, ny - size, nz - size);
        }

        tess.draw();
    }

    private static void renderEdges(List<OverlayEdge> edges, float width) {
        final float previousLineWidth = GL11.glGetFloat(GL11.GL_LINE_WIDTH);
        final Tessellator tess = Tessellator.instance;

        GL11.glLineWidth(width);
        tess.startDrawing(GL11.GL_LINES);

        for (OverlayEdge edge : edges) {
            final int[] color = edge.color();
            tess.setColorRGBA(color[0], color[1], color[2], 255);
            tess.addVertex(edge.x1 + 0.5d, edge.y1 + 0.5d, edge.z1 + 0.5d);
            tess.addVertex(edge.x2 + 0.5d, edge.y2 + 0.5d, edge.z2 + 0.5d);
        }

        tess.draw();
        GL11.glLineWidth(previousLineWidth);
    }

}
