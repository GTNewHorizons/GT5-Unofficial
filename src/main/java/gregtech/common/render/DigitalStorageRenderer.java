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
import net.minecraft.client.renderer.RenderBlocks;
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
import gregtech.api.GregTechAPI;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.TextureFactory;
import gregtech.common.config.Client;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;
import gregtech.common.tileentities.storage.MTEDigitalTankBase;

@SideOnly(Side.CLIENT)
public final class DigitalStorageRenderer {

    private static final double RENDER_EPSILON = 0.001D;
    private static final double DISPLAY_RENDER_DISTANCE = 64;

    private static final double CHEST_ITEM_MIN_RENDER_DISTANCE = 8;
    private static final double CHEST_ITEM_MAX_RENDER_DISTANCE = 20;
    private static final double CHEST_ITEM_HEIGHT = 0.25D;
    private static final float CHEST_ITEM_SCALE = 1.5F;
    private static final int CHEST_ITEM_ROTATION_TICKS = 40;

    private static final double FLUID_SIDE_MIN = 1 / 16.0 + RENDER_EPSILON;
    private static final double FLUID_SIDE_MAX = 15 / 16.0 - RENDER_EPSILON;
    private static final double FLUID_MIN_Y = 2 / 16.0;
    private static final double FLUID_MAX_Y = 14 / 16.0;

    private static final float CHEST_TEXT_DEPTH = -1 / 16f;
    private static final int CHEST_TEXT_Y = 40;
    private static final int CHEST_TEXT_MAX_WIDTH = 54;
    private static final float TANK_TEXT_DEPTH = 0.002F;
    private static final int TANK_TEXT_Y = 2;
    private static final int TANK_TEXT_MAX_WIDTH = 38;
    private static final double[] GLASS_BOX = { 1 / 16.0, 1 / 16.0, 1 / 16.0, 15 / 16.0, 15 / 16.0, 15 / 16.0 };
    private static final ForgeDirection[] HORIZONTAL_DIRECTIONS = { NORTH, SOUTH, WEST, EAST };
    private static final EnumMap<ForgeDirection, double[]> FRAME_BOXES = new EnumMap<>(ForgeDirection.class);
    private static final ITexture[] EMPTY_TEXTURES = new ITexture[0];
    private static final ITexture GLASS_TEXTURE = TextureFactory.of(OVERLAY_SCREEN_GLASS);
    private static final ThreadLocal<GTRendererBlock> STANDARD_RENDERER = ThreadLocal.withInitial(GTRendererBlock::new);
    private static final ThreadLocal<FluidStack> INVENTORY_FLUID = new ThreadLocal<>();

    static {
        FRAME_BOXES.put(UP, pixelBox(0, 14, 0, 16, 16, 16));
        FRAME_BOXES.put(DOWN, pixelBox(0, 0, 0, 16, 2, 16));
        FRAME_BOXES.put(WEST, pixelBox(0, 0, 0, 2, 16, 16));
        FRAME_BOXES.put(EAST, pixelBox(14, 0, 0, 16, 16, 16));
        FRAME_BOXES.put(SOUTH, pixelBox(0, 0, 14, 16, 16, 16));
        FRAME_BOXES.put(NORTH, pixelBox(0, 0, 0, 16, 16, 2));
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

    public static boolean renderTankInWorld(MTEDigitalTankBase mte, ISBRWorldContext ctx) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        ForgeDirection outputFacing = validDisplayFacing(base.getFrontFacing());
        ITexture[][] textures = new ITexture[6][];
        ITexture[] windowCasings = new ITexture[6];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture[] sideTextures = base.getTexture(ctx.getBlock(), side);
            textures[side.ordinal()] = sideTextures == null ? EMPTY_TEXTURES : sideTextures;
            if (isTankWindowSide(side, outputFacing, base)) {
                windowCasings[side.ordinal()] = sideTextures[0];
                textures[side.ordinal()] = EMPTY_TEXTURES;
            }
        }

        STANDARD_RENDERER.get()
            .renderStandardBlock(ctx, textures);
        renderTankWindows(ctx, windowCasings);
        restoreFullBounds(ctx);
        return true;
    }

    public static boolean renderTankInInventory(MTEDigitalTankBase mte, ISBRInventoryContext ctx) {
        ForgeDirection outputFacing = DOWN;
        ITexture[][] textures = new ITexture[6][];
        ITexture[] windowCasings = new ITexture[6];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture[] sideTextures = mte.getTexture(null, side, outputFacing, -1, true, false);
            textures[side.ordinal()] = sideTextures == null ? EMPTY_TEXTURES : sideTextures;
            if (isTankWindowSide(side, outputFacing, null)) {
                windowCasings[side.ordinal()] = sideTextures[0];
                textures[side.ordinal()] = EMPTY_TEXTURES;
            }
        }

        renderInventoryFaces(ctx, textures);
        renderTankWindows(ctx, windowCasings);
        FluidStack fluidStack = INVENTORY_FLUID.get();
        if (fluidStack != null) {
            renderTankFluid(fluidStack, mte.getDisplayFillLevel(fluidStack.amount), 0, 0, 0);
        }
        restoreFullBounds(ctx);
        return true;
    }

    public static void renderTankItem(ItemStack stack, MTEDigitalTankBase mte) {
        FluidStack fluidStack = stack.hasTagCompound() ? FluidStack.loadFluidStackFromNBT(
            stack.getTagCompound()
                .getCompoundTag("mFluid"))
            : null;
        INVENTORY_FLUID.set(fluidStack);
        try {
            RenderBlocks renderBlocks = new RenderBlocks();
            renderBlocks.useInventoryTint = true;
            renderBlocks.renderBlockAsItem(GregTechAPI.sBlockMachines, stack.getItemDamage(), 1.0F);
        } finally {
            INVENTORY_FLUID.remove();
        }
    }

    private static void renderTankWindows(ISBRContext ctx, ITexture[] windowCasings) {
        ITexture casing = null;
        for (ForgeDirection side : HORIZONTAL_DIRECTIONS) {
            if (windowCasings[side.ordinal()] != null) {
                renderTankWindow(ctx, side, windowCasings[side.ordinal()]);
                casing = windowCasings[side.ordinal()];
            }
        }
        if (casing != null) {
            renderTankCorner(ctx, windowCasings, NORTH, WEST);
            renderTankCorner(ctx, windowCasings, NORTH, EAST);
            renderTankCorner(ctx, windowCasings, SOUTH, WEST);
            renderTankCorner(ctx, windowCasings, SOUTH, EAST);
            // Seal the floor, ceiling, and non-window sides behind translucent fluid.
            renderFace(ctx, UP, FRAME_BOXES.get(DOWN), casing);
            renderFace(ctx, DOWN, FRAME_BOXES.get(UP), casing);
            for (ForgeDirection side : HORIZONTAL_DIRECTIONS) {
                if (windowCasings[side.ordinal()] == null) {
                    renderFace(ctx, side.getOpposite(), FRAME_BOXES.get(side), casing);
                }
            }
        }
    }

    private static void renderTankCorner(ISBRContext ctx, ITexture[] windowCasings, ForgeDirection northSouth,
        ForgeDirection eastWest) {
        ITexture casing = windowCasings[northSouth.ordinal()];
        if (casing == null || windowCasings[eastWest.ordinal()] == null) return;
        double minX = eastWest == WEST ? 1 : 14;
        double minZ = northSouth == NORTH ? 1 : 14;
        double[] bounds = pixelBox(minX, 0, minZ, minX + 1, 16, minZ + 1);
        // Overlap the jamb inset so the z-fighting offset does not leave a seam.
        if (eastWest == WEST) bounds[0] -= 2 * RENDER_EPSILON;
        else bounds[3] += 2 * RENDER_EPSILON;
        if (northSouth == NORTH) bounds[2] -= 2 * RENDER_EPSILON;
        else bounds[5] += 2 * RENDER_EPSILON;
        renderFace(ctx, northSouth.getOpposite(), bounds, casing);
        renderFace(ctx, eastWest.getOpposite(), bounds, casing);
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
        int color = mte.getBaseMetaTileEntity() == null ? 0
            : mte.getBaseMetaTileEntity()
                .getColorization() + 1;
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

    private static void renderTankWindow(ISBRContext ctx, ForgeDirection side, ITexture casing) {
        renderFace(ctx, side, GLASS_BOX, GLASS_TEXTURE);

        double minX = side == EAST ? 15 : 0;
        double maxX = side == WEST ? 1 : 16;
        double minZ = side == SOUTH ? 15 : 0;
        double maxZ = side == NORTH ? 1 : 16;
        double[] bottom = pixelBox(minX, 0, minZ, maxX, 2, maxZ);
        double[] top = pixelBox(minX, 14, minZ, maxX, 16, maxZ);
        double[] left;
        double[] right;
        ForgeDirection leftFacing;
        ForgeDirection rightFacing;
        if (side == NORTH || side == SOUTH) {
            left = pixelBox(0, 0, minZ, 2, 16, maxZ);
            right = pixelBox(14, 0, minZ, 16, 16, maxZ);
            leftFacing = EAST;
            rightFacing = WEST;
        } else {
            left = pixelBox(minX, 0, 0, maxX, 16, 2);
            right = pixelBox(minX, 0, 14, maxX, 16, 16);
            leftFacing = SOUTH;
            rightFacing = NORTH;
        }

        renderFace(ctx, side, bottom, casing);
        renderFace(ctx, side, top, casing);
        renderFace(ctx, side, left, casing);
        renderFace(ctx, side, right, casing);
        renderFace(ctx, UP, bottom, casing);
        renderFace(ctx, DOWN, top, casing);
        renderFace(ctx, leftFacing, left, casing);
        renderFace(ctx, rightFacing, right, casing);
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

    private static double[] pixelBox(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return new double[] { minX / 16 + RENDER_EPSILON, minY / 16 + RENDER_EPSILON, minZ / 16 + RENDER_EPSILON,
            maxX / 16 - RENDER_EPSILON, maxY / 16 - RENDER_EPSILON, maxZ / 16 - RENDER_EPSILON };
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

    public static void renderTankStack(MTEDigitalTankBase mte, double x, double y, double z, float timeSinceLastTick) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null || !canRender(x, y, z, DISPLAY_RENDER_DISTANCE)) return;

        ForgeDirection outputFacing = validDisplayFacing(base.getFrontFacing());
        FluidStack fluidStack = mte.getClientDisplayFluidStack();
        int fillLevel = mte.getClientDisplayFillLevel();
        renderTankFluid(fluidStack, fillLevel, x, y, z);

        if (Client.render.renderDigitalTankText) {
            String amountText = mte.getClientDisplayAmountText();
            for (ForgeDirection side : HORIZONTAL_DIRECTIONS) {
                if (isTankWindowSide(side, outputFacing, base)) {
                    renderAmountText(
                        x,
                        y,
                        z,
                        amountText,
                        side,
                        TANK_TEXT_DEPTH,
                        TANK_TEXT_Y,
                        true,
                        TANK_TEXT_MAX_WIDTH);
                }
            }
        }
    }

    private static void renderTankFluid(FluidStack fluidStack, int fillLevel, double x, double y, double z) {
        if (fluidStack == null || fillLevel <= 0) return;
        Fluid fluid = fluidStack.getFluid();
        if (fluid == null) return;

        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(
            GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                | GL11.GL_TEXTURE_BIT
                | GL11.GL_LIGHTING_BIT
                | GL11.GL_CURRENT_BIT);
        try {
            GL11.glDisable(GL11.GL_CULL_FACE);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            if (fluid.getLuminosity() > 0) {
                OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
            }

            IIcon icon = fluid.getIcon(fluidStack);
            if (icon != null) {
                int color = fluid.getColor(fluidStack);
                GL11.glColor4f(
                    (color >> 16 & 0xFF) / 255.0F,
                    (color >> 8 & 0xFF) / 255.0F,
                    (color & 0xFF) / 255.0F,
                    1.0F);
                TextureUtils.bindAtlas(fluid.getSpriteNumber());
                renderTankFluidVolume(x, y, z, icon, fillLevel);
            }
        } finally {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
            GL11.glPopAttrib();
        }
    }

    private static void renderTankFluidVolume(double x, double y, double z, IIcon icon, int fillLevel) {
        double fill = fillLevel / (double) MTEDigitalTankBase.DISPLAY_FILL_LEVELS;
        double top = FLUID_MIN_Y + (FLUID_MAX_Y - FLUID_MIN_Y) * fill;
        double uMin = icon.getMinU();
        double uMax = icon.getMaxU();
        double vMin = icon.getMinV();
        double vMax = icon.getMaxV();
        double vTop = vMax - (vMax - vMin) * fill;
        double minX = x + FLUID_SIDE_MIN;
        double maxX = x + FLUID_SIDE_MAX;
        double minY = y + FLUID_MIN_Y;
        double maxY = y + top;
        double minZ = z + FLUID_SIDE_MIN;
        double maxZ = z + FLUID_SIDE_MAX;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        // North face, min Z
        tessellator.addVertexWithUV(minX, minY, minZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, minY, minZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMax, vTop);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMin, vTop);

        // South face, max Z
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(minX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMax, vTop);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMin, vTop);

        // West face, min X
        tessellator.addVertexWithUV(minX, minY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(minX, minY, minZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMax, vTop);
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMin, vTop);

        // East face, max X
        tessellator.addVertexWithUV(maxX, minY, minZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMax, vTop);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMin, vTop);

        // Bottom face, min Y
        tessellator.addVertexWithUV(minX, minY, minZ, uMin, vMin);
        tessellator.addVertexWithUV(maxX, minY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(maxX, minY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(minX, minY, maxZ, uMin, vMax);

        // Top face, max Y
        tessellator.addVertexWithUV(minX, maxY, maxZ, uMin, vMax);
        tessellator.addVertexWithUV(maxX, maxY, maxZ, uMax, vMax);
        tessellator.addVertexWithUV(maxX, maxY, minZ, uMax, vMin);
        tessellator.addVertexWithUV(minX, maxY, minZ, uMin, vMin);
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

        double itemRenderDistance = Math.min(
            Math.max(Minecraft.getMinecraft().gameSettings.renderDistanceChunks, CHEST_ITEM_MIN_RENDER_DISTANCE),
            CHEST_ITEM_MAX_RENDER_DISTANCE);
        if (canRender(x, y, z, itemRenderDistance)) {
            EntityItem entityItem = mte.getClientDisplayEntity();
            if (entityItem != null) {
                float tick = base.getWorld()
                    .getTotalWorldTime() + timeSinceLastTick;
                float lastBrightnessX = OpenGlHelper.lastBrightnessX;
                float lastBrightnessY = OpenGlHelper.lastBrightnessY;
                GL11.glPushAttrib(
                    GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT
                        | GL11.GL_CURRENT_BIT
                        | GL11.GL_LIGHTING_BIT
                        | GL11.GL_TEXTURE_BIT
                        | GL11.GL_DEPTH_BUFFER_BIT);
                GlStateManager.pushMatrix();
                try {
                    GlStateManager.translate(x, y, z);
                    GlStateManager.translate(0.5D, CHEST_ITEM_HEIGHT, 0.5D);
                    GlStateManager.rotate(tick * (float) Math.PI * 2 / CHEST_ITEM_ROTATION_TICKS, 0, 1, 0);
                    GlStateManager.scale(CHEST_ITEM_SCALE, CHEST_ITEM_SCALE, CHEST_ITEM_SCALE);
                    RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);
                } finally {
                    GlStateManager.popMatrix();
                    OpenGlHelper
                        .setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
                    GL11.glPopAttrib();
                }
            }
        }
        if (Client.render.renderDigitalChestText) {
            renderAmountText(
                x,
                y,
                z,
                mte.getClientDisplayItemCountText(),
                mte.getDisplayFacing(),
                CHEST_TEXT_DEPTH,
                CHEST_TEXT_Y,
                false,
                CHEST_TEXT_MAX_WIDTH);
        }
    }

    private static void renderAmountText(double x, double y, double z, String amountText, ForgeDirection frontFacing,
        float faceOffset, int textY, boolean shadow, int maxTextWidth) {
        if (frontFacing == ForgeDirection.UNKNOWN || !canRender(x, y, z, DISPLAY_RENDER_DISTANCE)) return;

        FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
        int textWidth = fontRenderer.getStringWidth(amountText);
        float textScale = Math.min(1, maxTextWidth / (float) textWidth);
        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        GL11.glPushAttrib(GL11.GL_ENABLE_BIT | GL11.GL_COLOR_BUFFER_BIT | GL11.GL_CURRENT_BIT | GL11.GL_TEXTURE_BIT);
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
                GlStateManager.rotate(spin == EAST ? 90 : spin == NORTH ? 180 : spin == WEST ? -90 : 0, 0, 0, 1);
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
