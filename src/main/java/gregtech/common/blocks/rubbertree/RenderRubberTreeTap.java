package gregtech.common.blocks.rubbertree;

import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;

@SideOnly(Side.CLIENT)
public class RenderRubberTreeTap extends TileEntitySpecialRenderer {

    @Override
    public void renderTileEntityAt(TileEntity tileEntity, double x, double y, double z, float partialTicks) {
        if (!(tileEntity instanceof TileEntityRubberLogTapped tile)) {
            return;
        }

        if (!tile.hasInstalledTap()) {
            return;
        }

        int side = tile.getSideForRender();

        if (!BlockRubberLogNatural.hasTappedHole(side)) {
            return;
        }

        IIcon icon = getTreeTapIcon(tile.getTapStackForRender());

        if (icon == null) {
            return;
        }

        World world = tile.getWorldObj();

        if (world == null) {
            return;
        }

        int brightness = getTapBrightness(tile, side);

        bindTexture(TextureMap.locationBlocksTexture);

        boolean wasLightingEnabled = GL11.glIsEnabled(GL11.GL_LIGHTING);
        boolean wasCullEnabled = GL11.glIsEnabled(GL11.GL_CULL_FACE);

        GL11.glPushMatrix();
        GL11.glTranslated(x, y, z);

        // Important: avoid inheriting a black/dark GL color from another renderer.
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // Important: TESR rendering can be affected by OpenGL fixed lighting.
        // We use Minecraft's packed lightmap brightness instead.
        GL11.glDisable(GL11.GL_LIGHTING);

        int lightX = brightness & 0xFFFF;
        int lightY = brightness >> 16 & 0xFFFF;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lightX, lightY);

        GL11.glDisable(GL11.GL_CULL_FACE);

        renderTap(side, icon, brightness);

        if (wasCullEnabled) {
            GL11.glEnable(GL11.GL_CULL_FACE);
        } else {
            GL11.glDisable(GL11.GL_CULL_FACE);
        }

        if (wasLightingEnabled) {
            GL11.glEnable(GL11.GL_LIGHTING);
        } else {
            GL11.glDisable(GL11.GL_LIGHTING);
        }

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    private static int getTapBrightness(@NotNull TileEntityRubberLogTapped tile, int side) {
        World world = tile.getWorldObj();

        if (world == null) {
            return 0xF000F0;
        }

        ForgeDirection direction = ForgeDirection.getOrientation(side);

        int ownBrightness = world.getLightBrightnessForSkyBlocks(tile.xCoord, tile.yCoord, tile.zCoord, 0);

        int outsideBrightness = world.getLightBrightnessForSkyBlocks(
            tile.xCoord + direction.offsetX,
            tile.yCoord + direction.offsetY,
            tile.zCoord + direction.offsetZ,
            0);

        return combineMaxBrightness(ownBrightness, outsideBrightness);
    }

    private static int combineMaxBrightness(int first, int second) {
        int firstBlock = first & 0xFFFF;
        int firstSky = first >> 16 & 0xFFFF;

        int secondBlock = second & 0xFFFF;
        int secondSky = second >> 16 & 0xFFFF;

        int block = Math.max(firstBlock, secondBlock);
        int sky = Math.max(firstSky, secondSky);

        return block | sky << 16;
    }

    private static IIcon getTreeTapIcon(ItemStack tapStack) {
        if (tapStack == null) {
            return null;
        }

        if (isSameItem(tapStack, ItemList.Tree_Tap_Wood.get(1))) {
            return BlockRubberLogNatural.getWoodendTreeTapRenderIcon();
        }

        if (isSameItem(tapStack, ItemList.Tree_Tap_Bronze.get(1))) {
            return BlockRubberLogNatural.getBronzeTreeTapRenderIcon();
        }

        if (isSameItem(tapStack, ItemList.Tree_Tap_Steel.get(1))) {
            return BlockRubberLogNatural.getSteelTreeTapRenderIcon();
        }

        return BlockRubberLogNatural.getWoodendTreeTapRenderIcon();
    }

    private static boolean isSameItem(ItemStack stack, ItemStack expected) {
        return stack != null && expected != null && stack.getItem() == expected.getItem();
    }

    private static void renderTap(int side, IIcon icon, int brightness) {
        switch (side) {
            case 2:
                renderNorthTap(icon, brightness);
                break;
            case 3:
                renderSouthTap(icon, brightness);
                break;
            case 4:
                renderWestTap(icon, brightness);
                break;
            case 5:
                renderEastTap(icon, brightness);
                break;
            default:
                break;
        }
    }

    private static void renderNorthTap(IIcon icon, int brightness) {
        // Mounting plate on north face.
        renderCuboid(icon, brightness, 0.3125D, 0.3750D, -0.03125D, 0.6875D, 0.6875D, 0.03125D);

        // Main spout.
        renderCuboid(icon, brightness, 0.4375D, 0.46875D, -0.3750D, 0.5625D, 0.59375D, 0.0000D);

        // Small lower drip part.
        renderCuboid(icon, brightness, 0.4375D, 0.31250D, -0.3750D, 0.5625D, 0.50000D, -0.2500D);
    }

    private static void renderSouthTap(IIcon icon, int brightness) {
        renderCuboid(icon, brightness, 0.3125D, 0.3750D, 0.96875D, 0.6875D, 0.6875D, 1.03125D);
        renderCuboid(icon, brightness, 0.4375D, 0.46875D, 1.00000D, 0.5625D, 0.59375D, 1.37500D);
        renderCuboid(icon, brightness, 0.4375D, 0.31250D, 1.25000D, 0.5625D, 0.50000D, 1.37500D);
    }

    private static void renderWestTap(IIcon icon, int brightness) {
        renderCuboid(icon, brightness, -0.03125D, 0.3750D, 0.3125D, 0.03125D, 0.6875D, 0.6875D);
        renderCuboid(icon, brightness, -0.37500D, 0.46875D, 0.4375D, 0.00000D, 0.59375D, 0.5625D);
        renderCuboid(icon, brightness, -0.37500D, 0.31250D, 0.4375D, -0.2500D, 0.50000D, 0.5625D);
    }

    private static void renderEastTap(IIcon icon, int brightness) {
        renderCuboid(icon, brightness, 0.96875D, 0.3750D, 0.3125D, 1.03125D, 0.6875D, 0.6875D);
        renderCuboid(icon, brightness, 1.00000D, 0.46875D, 0.4375D, 1.37500D, 0.59375D, 0.5625D);
        renderCuboid(icon, brightness, 1.25000D, 0.31250D, 0.4375D, 1.37500D, 0.50000D, 0.5625D);
    }

    private static void renderCuboid(@NotNull IIcon icon, int brightness, double minX, double minY, double minZ,
        double maxX, double maxY, double maxZ) {
        Tessellator tessellator = Tessellator.instance;

        double uMin = icon.getMinU();
        double uMax = icon.getMaxU();
        double vMin = icon.getMinV();
        double vMax = icon.getMaxV();

        tessellator.startDrawingQuads();
        tessellator.setBrightness(brightness);
        tessellator.setColorOpaque_F(1.0F, 1.0F, 1.0F);

        // Down
        tessellator.setNormal(0.0F, -1.0F, 0.0F);
        tessellator.addVertexWithUV(minX, minY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, minY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(minX, minY, minZ, uMin, vMin);

        // Up
        tessellator.setNormal(0.0F, 1.0F, 0.0F);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMin, vMin);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMin, vMax);

        // North
        tessellator.setNormal(0.0F, 0.0F, -1.0F);
        tessellator.addVertexWithUV(minX, minY, minZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, minY, minZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMin, vMin);

        // South
        tessellator.setNormal(0.0F, 0.0F, 1.0F);
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(minX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMax, vMin);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMin, vMin);

        // West
        tessellator.setNormal(-1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV(minX, minY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(minX, minY, minZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMin, vMin);

        // East
        tessellator.setNormal(1.0F, 0.0F, 0.0F);
        tessellator.addVertexWithUV(maxX, minY, minZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMax, vMin);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMin, vMin);

        tessellator.draw();
    }
}
