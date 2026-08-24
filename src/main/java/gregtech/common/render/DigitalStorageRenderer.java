package gregtech.common.render;

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SCREEN_GLASS;
import static net.minecraftforge.common.util.ForgeDirection.DOWN;
import static net.minecraftforge.common.util.ForgeDirection.EAST;
import static net.minecraftforge.common.util.ForgeDirection.NORTH;
import static net.minecraftforge.common.util.ForgeDirection.SOUTH;
import static net.minecraftforge.common.util.ForgeDirection.UP;
import static net.minecraftforge.common.util.ForgeDirection.WEST;

import java.util.EnumMap;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.utils.GlStateManager;

import codechicken.lib.render.TextureUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

@SideOnly(Side.CLIENT)
public final class DigitalStorageRenderer {

    private static final double COVER_DIF = 0.001D;
    private static final double FLUID_MIN = 3 / 16.0;
    private static final double FLUID_MAX = 13 / 16.0;
    private static final double FLUID_DEPTH = 0.001D;
    private static final double TEXT_DEPTH = 0.002D;
    private static final int FLUID_LEVELS = 64;
    private static final double[] GLASS_BOX = { 1 / 16.0, 1 / 16.0, 1 / 16.0, 15 / 16.0, 15 / 16.0,
        15 / 16.0 };
    private static final ForgeDirection[] HORIZONTAL_DIRECTIONS = { NORTH, SOUTH, WEST, EAST };
    private static final EnumMap<ForgeDirection, double[]> FRAME_BOXES = new EnumMap<>(ForgeDirection.class);
    private static final ITexture[] EMPTY_TEXTURES = new ITexture[0];
    private static final ITexture GLASS_TEXTURE = TextureFactory.of(OVERLAY_SCREEN_GLASS);
    private static final ThreadLocal<GTRendererBlock> STANDARD_RENDERER = ThreadLocal.withInitial(GTRendererBlock::new);

    static {
        FRAME_BOXES.put(UP, box(0, 14, 0, 16, 16, 16));
        FRAME_BOXES.put(DOWN, box(0, 0, 0, 16, 2, 16));
        FRAME_BOXES.put(WEST, box(0, 0, 0, 2, 16, 16));
        FRAME_BOXES.put(EAST, box(14, 0, 0, 16, 16, 16));
        FRAME_BOXES.put(SOUTH, box(0, 0, 14, 16, 16, 16));
        FRAME_BOXES.put(NORTH, box(0, 0, 0, 16, 16, 2));
    }

    private DigitalStorageRenderer() {}

    public static boolean renderChestInWorld(MTEDigitalChestBase mte, ISBRWorldContext ctx) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        ForgeDirection displayFacing = validDisplayFacing(mte.getDisplayFacing());
        ITexture[][] textures = new ITexture[6][];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture[] sideTextures = base == null ? mte.getTexture(base, side, displayFacing, -1, false, false)
                : base.getTexture(ctx.getBlock(), side);
            textures[side.ordinal()] = sideTextures == null ? EMPTY_TEXTURES : sideTextures;
        }

        boolean covered = base != null && base.hasCoverAtSide(displayFacing);
        ITexture[] displayTextures = textures[displayFacing.ordinal()];
        if (!covered) textures[displayFacing.ordinal()] = EMPTY_TEXTURES;

        STANDARD_RENDERER.get()
            .renderStandardBlock(ctx, textures);
        if (!covered) {
            renderWindow(ctx, displayFacing, getCasingTexture(displayTextures, mte), base);
        }
        restoreFullBounds(ctx);
        return true;
    }

    public static boolean renderChestInInventory(MTEDigitalChestBase mte, ISBRInventoryContext ctx) {
        ForgeDirection displayFacing = WEST;
        ForgeDirection outputFacing = EAST;
        ITexture[][] textures = new ITexture[6][];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture[] sideTextures = mte.getTexture(null, side, outputFacing, -1, true, false);
            textures[side.ordinal()] = sideTextures == null ? EMPTY_TEXTURES : sideTextures;
        }

        ITexture[] displayTextures = textures[displayFacing.ordinal()];
        textures[displayFacing.ordinal()] = EMPTY_TEXTURES;
        renderInventoryFaces(ctx, textures);
        renderWindow(ctx, displayFacing, getCasingTexture(displayTextures, mte), null);
        restoreFullBounds(ctx);
        return true;
    }

    private static boolean isTankWindowSide(ForgeDirection side, ForgeDirection outputFacing,
        @Nullable IGregTechTileEntity base) {
        return isHorizontal(side) && side != outputFacing && (base == null || !base.hasCoverAtSide(side));
    }

    private static boolean isHorizontal(ForgeDirection side) {
        return side == NORTH || side == SOUTH || side == WEST || side == EAST;
    }

    private static ITexture getCasingTexture(@Nullable ITexture[] textures, MTEDigitalChestBase mte) {
        if (textures != null && textures.length > 0 && textures[0] != null) return textures[0];
        int color = mte.getBaseMetaTileEntity() == null ? 0 : mte.getBaseMetaTileEntity().getColorization() + 1;
        return MACHINE_CASINGS[mte.mTier][Math.max(0, Math.min(color, MACHINE_CASINGS[mte.mTier].length - 1))];
    }

    private static void renderInventoryFaces(ISBRInventoryContext ctx, ITexture[][] textures) {
        ctx.getRenderBlocks()
            .setRenderBounds(0, 0, 0, 1, 1, 1);
        ctx.renderNegativeYFacing(textures[DOWN.ordinal()]);
        ctx.renderPositiveYFacing(textures[UP.ordinal()]);
        ctx.renderNegativeZFacing(textures[NORTH.ordinal()]);
        ctx.renderPositiveZFacing(textures[SOUTH.ordinal()]);
        ctx.renderNegativeXFacing(textures[WEST.ordinal()]);
        ctx.renderPositiveXFacing(textures[EAST.ordinal()]);
    }

    private static void renderWindow(ISBRContext ctx, ForgeDirection displayFacing, ITexture casing,
        @Nullable IGregTechTileEntity base) {
        renderFace(ctx, displayFacing, GLASS_BOX, GLASS_TEXTURE);

        for (ForgeDirection frameFacing : ForgeDirection.VALID_DIRECTIONS) {
            if (frameFacing == displayFacing) continue;
            double[] frameBox = FRAME_BOXES.get(frameFacing);
            if (base == null || !base.hasCoverAtSide(frameFacing)) renderFace(ctx, frameFacing, frameBox, casing);
            renderFace(ctx, frameFacing.getOpposite(), frameBox, casing);
        }

        if (displayFacing == UP || displayFacing == DOWN) {
            renderFace(ctx, displayFacing, FRAME_BOXES.get(NORTH), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(SOUTH), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(EAST), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(WEST), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(displayFacing.getOpposite()), casing);
        } else {
            renderFace(ctx, displayFacing, FRAME_BOXES.get(DOWN), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(UP), casing);

            ForgeDirection perpendicular = rotateYCCW(displayFacing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(perpendicular), casing);
            renderFace(ctx, displayFacing, FRAME_BOXES.get(perpendicular.getOpposite()), casing);
        }
    }

    private static void renderFace(ISBRContext ctx, ForgeDirection face, double[] bounds, ITexture texture) {
        ctx.getRenderBlocks()
            .setRenderBounds(bounds[0], bounds[1], bounds[2], bounds[3], bounds[4], bounds[5]);
        ITexture[] textures = new ITexture[] { texture };
        switch (face) {
            case DOWN -> ctx.renderNegativeYFacing(textures);
            case UP -> ctx.renderPositiveYFacing(textures);
            case NORTH -> ctx.renderNegativeZFacing(textures);
            case SOUTH -> ctx.renderPositiveZFacing(textures);
            case WEST -> ctx.renderNegativeXFacing(textures);
            case EAST -> ctx.renderPositiveXFacing(textures);
            default -> {}
        }
    }

    private static void restoreFullBounds(ISBRContext ctx) {
        ctx.getRenderBlocks()
            .setRenderBounds(0, 0, 0, 1, 1, 1);
    }

    private static double[] box(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[] { minX / 16 + COVER_DIF, minY / 16 + COVER_DIF, minZ / 16 + COVER_DIF,
            maxX / 16 - COVER_DIF, maxY / 16 - COVER_DIF, maxZ / 16 - COVER_DIF };
    }

    private static ForgeDirection validDisplayFacing(ForgeDirection facing) {
        return facing == ForgeDirection.UNKNOWN ? WEST : facing;
    }

    private static ForgeDirection rotateYCCW(ForgeDirection dir) {
        return switch (dir) {
            case NORTH -> WEST;
            case WEST -> SOUTH;
            case SOUTH -> EAST;
            case EAST -> NORTH;
            default -> throw new IllegalArgumentException("Can't rotate Y on " + dir);
        };
    }

    public static void renderTankStack(MTEDigitalTankBase mte, double x, double y, double z,
        float timeSinceLastTick) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null || !canRender(x, y, z, 64)) return;

        ForgeDirection outputFacing = validDisplayFacing(base.getFrontFacing());
        FluidStack fluidStack = mte.getClientDisplayFluidStack();
        Fluid fluid = fluidStack == null ? null : fluidStack.getFluid();
        int fillLevel = mte.getClientDisplayFillLevel();
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_TEXTURE_BIT | GL11.GL_LIGHTING_BIT
                | GL11.GL_CURRENT_BIT);
        try {
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            if (fluid != null && fluid.getLuminosity() > 0) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            }

            if (fluidStack != null && fluid != null && fillLevel > 0) {
                IIcon icon = fluid.getIcon(fluidStack);
                if (icon != null) {
                    int color = fluid.getColor(fluidStack);
                    GL11.glColor4f(
                        (color >> 16 & 0xFF) / 255.0F,
                        (color >> 8 & 0xFF) / 255.0F,
                        (color & 0xFF) / 255.0F,
                        1.0F);
                    TextureUtils.bindAtlas(fluid.getSpriteNumber());
                    for (ForgeDirection side : HORIZONTAL_DIRECTIONS) {
                        if (isTankWindowSide(side, outputFacing, base)) {
                            renderTankFluidFace(x, y, z, side, icon, fillLevel);
                        }
                    }
                }
            }

            String amountText = mte.getClientDisplayAmountText();
            for (ForgeDirection side : HORIZONTAL_DIRECTIONS) {
                if (isTankWindowSide(side, outputFacing, base)) {
                    renderAmountText(x, y, z, amountText, side, (float) TEXT_DEPTH, 4, true, 38);
                }
            }
        } finally {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
            GL11.glPopAttrib();
        }
    }

    private static void renderTankFluidFace(double x, double y, double z, ForgeDirection side,
        IIcon icon, int fillLevel) {
        double fill = fillLevel / (double) FLUID_LEVELS;
        double top = FLUID_MIN + (FLUID_MAX - FLUID_MIN) * fill;
        double uMin = icon.getMinU();
        double uMax = icon.getMaxU();
        double vMin = icon.getMinV();
        double vMax = icon.getMaxV();
        double vTop = vMax - (vMax - vMin) * fill;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        switch (side) {
            case NORTH -> {
                double face = z - FLUID_DEPTH;
                tessellator.addVertexWithUV(x + FLUID_MIN, y + FLUID_MIN, face, uMin, vMax);
                tessellator.addVertexWithUV(x + FLUID_MAX, y + FLUID_MIN, face, uMax, vMax);
                tessellator.addVertexWithUV(x + FLUID_MAX, y + top, face, uMax, vTop);
                tessellator.addVertexWithUV(x + FLUID_MIN, y + top, face, uMin, vTop);
            }
            case SOUTH -> {
                double face = z + 1 + FLUID_DEPTH;
                tessellator.addVertexWithUV(x + FLUID_MAX, y + FLUID_MIN, face, uMin, vMax);
                tessellator.addVertexWithUV(x + FLUID_MIN, y + FLUID_MIN, face, uMax, vMax);
                tessellator.addVertexWithUV(x + FLUID_MIN, y + top, face, uMax, vTop);
                tessellator.addVertexWithUV(x + FLUID_MAX, y + top, face, uMin, vTop);
            }
            case WEST -> {
                double face = x - FLUID_DEPTH;
                tessellator.addVertexWithUV(face, y + FLUID_MIN, z + FLUID_MAX, uMin, vMax);
                tessellator.addVertexWithUV(face, y + FLUID_MIN, z + FLUID_MIN, uMax, vMax);
                tessellator.addVertexWithUV(face, y + top, z + FLUID_MIN, uMax, vTop);
                tessellator.addVertexWithUV(face, y + top, z + FLUID_MAX, uMin, vTop);
            }
            case EAST -> {
                double face = x + 1 + FLUID_DEPTH;
                tessellator.addVertexWithUV(face, y + FLUID_MIN, z + FLUID_MIN, uMin, vMax);
                tessellator.addVertexWithUV(face, y + FLUID_MIN, z + FLUID_MAX, uMax, vMax);
                tessellator.addVertexWithUV(face, y + top, z + FLUID_MAX, uMax, vTop);
                tessellator.addVertexWithUV(face, y + top, z + FLUID_MIN, uMin, vTop);
            }
            default -> {}
        }
        tessellator.draw();
    }

    public static void renderChestStack(MTEDigitalChestBase mte, double x, double y, double z,
        float timeSinceLastTick) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null
            || base.hasCoverAtSide(validDisplayFacing(mte.getDisplayFacing()))) {
            return;
        }

        ItemStack content = mte.getClientDisplayItem();
        if (content == null) return;

        if (canRender(
            x,
            y,
            z,
            8 * Math.min(Math.max(Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 8.0, 1.0), 2.5))) {
            EntityItem entityItem = mte.getClientDisplayEntity();
            if (entityItem != null) {
                float tick = base.getWorld().getTotalWorldTime() + timeSinceLastTick;
                float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                float lastBrightnessY = OpenGlHelper.lastBrightnessY;
                GL11.glPushAttrib(
                    GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT | GL11.GL_LIGHTING_BIT
                        | GL11.GL_TEXTURE_BIT
                        | GL11.GL_DEPTH_BUFFER_BIT);
                GlStateManager.pushMatrix();
                try {
                    GlStateManager.translate(x, y, z);
                    GlStateManager.translate(0.5D, 0.25D, 0.5D);
                    GlStateManager.rotate(tick * (float) Math.PI * 2 / 40, 0, 1, 0);
                    GlStateManager.scale(1.5f, 1.5f, 1.5f);
                    RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);
                } finally {
                    GlStateManager.popMatrix();
                    OpenGlHelper
                        .setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
                    GL11.glPopAttrib();
                }
            }
        }
        renderAmountText(
            x,
            y,
            z,
            mte.getClientDisplayItemCountText(),
            mte.getDisplayFacing(),
            -1 / 16f,
            40,
            false,
            54);
    }

    private static void renderAmountText(double x, double y, double z, String amountText,
        ForgeDirection frontFacing, float faceOffset, int textY, boolean shadow, int maxTextWidth) {
        if (frontFacing == ForgeDirection.UNKNOWN || !canRender(x, y, z, 64)) return;

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int textWidth = fontRenderer.getStringWidth(amountText);
        float textScale = Math.min(1, maxTextWidth / (float) textWidth);
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT | GL11.GL_TEXTURE_BIT);
        GlStateManager.pushMatrix();
        try {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(
                frontFacing.offsetX * faceOffset,
                frontFacing.offsetY * faceOffset,
                frontFacing.offsetZ * faceOffset);
            GlStateManager.translate(
                0.5 + frontFacing.offsetX * 0.5,
                0.5 + frontFacing.offsetY * 0.5,
                0.5 + frontFacing.offsetZ * 0.5);
            if (frontFacing == UP || frontFacing == DOWN) {
                rotateToFace(frontFacing, SOUTH);
            } else {
                rotateToFace(frontFacing, null);
            }
            GlStateManager.scale(textScale / 64, textScale / 64, 0);
            GlStateManager.translate(-32 / textScale, -32 / textScale, 0);
            GlStateManager.disableLighting();
            fontRenderer.drawString(
                amountText,
                Math.round(32 / textScale - textWidth / 2f),
                Math.round(textY / textScale),
                0xFFFFFF,
                shadow);
        } finally {
            GlStateManager.popMatrix();
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
            GL11.glPopAttrib();
        }
    }

    private static boolean canRender(double x, double y, double z, double range) {
        double distance = x * x + y * y + z * z;
        return distance < range * range;
    }

    private static void rotateToFace(ForgeDirection face, @Nullable ForgeDirection spin) {
        int angle = spin == EAST ? 90 : spin == SOUTH ? 180 : spin == WEST ? -90 : 0;
        switch (face) {
            case UP -> {
                GlStateManager.scale(1.0f, -1.0f, 1.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
            }
            case DOWN -> {
                GlStateManager.scale(1.0f, -1.0f, 1.0f);
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(
                    spin == EAST ? 90 : spin == NORTH ? 180 : spin == WEST ? -90 : 0,
                    0,
                    0,
                    1);
            }
            case EAST -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
            }
            case WEST -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
            }
            case NORTH -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
            }
            case SOUTH -> {
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
            }
            default -> {}
        }
    }
}
