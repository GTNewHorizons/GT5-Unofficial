package gregtech.common.render;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
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
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.GlStateManager;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.ISBRContext;
import gregtech.api.render.ISBRInventoryContext;
import gregtech.api.render.ISBRWorldContext;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

@SideOnly(Side.CLIENT)
public final class DigitalStorageRenderer {

    private static final double COVER_DIF = 0.001D;
    private static final double[] GLASS_BOX = { 1 / 16.0, 1 / 16.0, 1 / 16.0, 15 / 16.0, 15 / 16.0,
        15 / 16.0 };
    private static final EnumMap<ForgeDirection, double[]> FRAME_BOXES = new EnumMap<>(ForgeDirection.class);
    private static final ITexture[] EMPTY_TEXTURES = new ITexture[0];
    private static final ITexture GLASS_TEXTURE = TextureFactory.of(OVERLAY_SCREEN_GLASS);
    private static final FontRenderer FONT_RENDERER = Minecraft.getMinecraft().fontRenderer;
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
            renderWindow(ctx, displayFacing, getCasingTexture(displayTextures, mte));
        }
        restoreFullBounds(ctx);
        return true;
    }

    public static boolean renderChestInInventory(MTEDigitalChestBase mte, ISBRInventoryContext ctx) {
        ForgeDirection displayFacing = WEST;
        ITexture[][] textures = new ITexture[6][];
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            ITexture[] sideTextures = mte.getTexture(null, side, displayFacing, -1, true, false);
            textures[side.ordinal()] = sideTextures == null ? EMPTY_TEXTURES : sideTextures;
        }

        ITexture[] displayTextures = textures[displayFacing.ordinal()];
        textures[displayFacing.ordinal()] = EMPTY_TEXTURES;
        renderInventoryFaces(ctx, textures);
        renderWindow(ctx, displayFacing, getCasingTexture(displayTextures, mte));
        restoreFullBounds(ctx);
        return true;
    }

    private static ITexture getCasingTexture(@Nullable ITexture[] textures, MTEDigitalChestBase mte) {
        if (textures != null && textures.length > 0 && textures[0] != null) return textures[0];
        int color = mte.getBaseMetaTileEntity() == null ? 0 : mte.getBaseMetaTileEntity().getColorization() + 1;
        return MACHINE_CASINGS[mte.mTier][Math.max(0, Math.min(color, MACHINE_CASINGS[mte.mTier].length - 1))];
    }

    private static void renderInventoryFaces(ISBRInventoryContext ctx, ITexture[][] textures) {
        ctx.getBlock()
            .setBlockBounds(0, 0, 0, 1, 1, 1);
        ctx.setRenderBoundsFromBlock();
        ctx.renderNegativeYFacing(textures[DOWN.ordinal()]);
        ctx.renderPositiveYFacing(textures[UP.ordinal()]);
        ctx.renderNegativeZFacing(textures[NORTH.ordinal()]);
        ctx.renderPositiveZFacing(textures[SOUTH.ordinal()]);
        ctx.renderNegativeXFacing(textures[WEST.ordinal()]);
        ctx.renderPositiveXFacing(textures[EAST.ordinal()]);
    }

    private static void renderWindow(ISBRContext ctx, ForgeDirection displayFacing, ITexture casing) {
        renderFace(ctx, displayFacing, GLASS_BOX, GLASS_TEXTURE);

        for (ForgeDirection frameFacing : ForgeDirection.VALID_DIRECTIONS) {
            if (frameFacing == displayFacing) continue;
            double[] frameBox = FRAME_BOXES.get(frameFacing);
            renderFace(ctx, frameFacing, frameBox, casing);
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
        ctx.getBlock()
            .setBlockBounds(
                (float) bounds[0],
                (float) bounds[1],
                (float) bounds[2],
                (float) bounds[3],
                (float) bounds[4],
                (float) bounds[5]);
        ctx.setRenderBoundsFromBlock();
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
        ctx.getBlock()
            .setBlockBounds(0, 0, 0, 1, 1, 1);
        ctx.setRenderBoundsFromBlock();
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

    public static void renderChestStack(MTEDigitalChestBase mte, double x, double y, double z,
        float timeSinceLastTick) {
        IGregTechTileEntity base = mte.getBaseMetaTileEntity();
        if (base == null || base.getWorld() == null || base.hasCoverAtSide(validDisplayFacing(mte.getDisplayFacing()))) {
            return;
        }

        ItemStack content = mte.getClientDisplayItem();
        if (content == null) return;

        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
        try {
            if (canRender(
                x,
                y,
                z,
                8 * Math.min(Math.max(Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 8.0, 1.0), 2.5))) {
                EntityItem entityItem = mte.getClientDisplayEntity();
                if (entityItem != null) {
                    float tick = base.getWorld().getTotalWorldTime() + timeSinceLastTick;
                    GlStateManager.pushMatrix();
                    try {
                        GlStateManager.translate(x, y, z);
                        GlStateManager.translate(0.5D, 0.25D, 0.5D);
                        GlStateManager.rotate(tick * (float) Math.PI * 2 / 40, 0, 1, 0);
                        GlStateManager.scale(1.5f, 1.5f, 1.5f);
                        RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);
                    } finally {
                        GlStateManager.popMatrix();
                    }
                }
            }
            renderAmountText(x, y, z, mte.getClientDisplayItemCount(), mte.getDisplayFacing());
        } finally {
            OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
        }
    }

    private static void renderAmountText(double x, double y, double z, long amount, ForgeDirection frontFacing) {
        if (frontFacing == ForgeDirection.UNKNOWN || !canRender(x, y, z, 64)) return;

        GlStateManager.pushMatrix();
        try {
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(frontFacing.offsetX * -1 / 16f, frontFacing.offsetY * -1 / 16f,
                frontFacing.offsetZ * -1 / 16f);
            GlStateManager.translate(
                0.5 + frontFacing.offsetX * 0.5,
                0.5 + frontFacing.offsetY * 0.5,
                0.5 + frontFacing.offsetZ * 0.5);
            if (frontFacing == UP || frontFacing == DOWN) {
                rotateToFace(frontFacing, SOUTH);
            } else {
                rotateToFace(frontFacing, null);
            }
            String amountText = formatNumber(amount);
            GlStateManager.scale(1f / 64, 1f / 64, 0);
            GlStateManager.translate(-32, -32, 0);
            GlStateManager.disableLighting();
            try {
                FONT_RENDERER.drawString(
                    amountText,
                    32 - FONT_RENDERER.getStringWidth(amountText) / 2,
                    40,
                    0xFFFFFF,
                    false);
            } finally {
                GlStateManager.enableLighting();
            }
        } finally {
            GlStateManager.popMatrix();
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
