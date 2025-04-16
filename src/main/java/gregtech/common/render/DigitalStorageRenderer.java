package gregtech.common.render;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.EnumMap;

import gregtech.api.util.GTUtility;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import com.cleanroommc.modularui.utils.GlStateManager;

import codechicken.lib.colour.ColourRGBA;
import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Translation;
import cofh.lib.util.helpers.MathHelper;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtilityClient;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

// Backported from GTCEu
public class DigitalStorageRenderer {

    private static final Cuboid6 glassBox = new Cuboid6(1 / 16.0, 1 / 16.0, 1 / 16.0, 15 / 16.0, 15 / 16.0, 15 / 16.0);
    private static final BlockRenderer.BlockFace blockFace = new BlockRenderer.BlockFace();
    private static final EnumMap<ForgeDirection, Cuboid6> boxFacingMap = new EnumMap<>(ForgeDirection.class);

    private static final FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;

    static {
        boxFacingMap
            .put(ForgeDirection.UP, new Cuboid6(0 / 16.0, 14 / 16.0, 0 / 16.0, 16 / 16.0, 16 / 16.0, 16 / 16.0));
        boxFacingMap
            .put(ForgeDirection.DOWN, new Cuboid6(0 / 16.0, 0 / 16.0, 0 / 16.0, 16 / 16.0, 2 / 16.0, 16 / 16.0));
        boxFacingMap
            .put(ForgeDirection.WEST, new Cuboid6(0 / 16.0, 0 / 16.0, 0 / 16.0, 2 / 16.0, 16 / 16.0, 16 / 16.0));
        boxFacingMap
            .put(ForgeDirection.EAST, new Cuboid6(14 / 16.0, 0 / 16.0, 0 / 16.0, 16 / 16.0, 16 / 16.0, 16 / 16.0));
        boxFacingMap
            .put(ForgeDirection.SOUTH, new Cuboid6(0 / 16.0, 0 / 16.0, 14 / 16.0, 16 / 16.0, 16 / 16.0, 16 / 16.0));
        boxFacingMap
            .put(ForgeDirection.NORTH, new Cuboid6(0 / 16.0, 0 / 16.0, 0 / 16.0, 16 / 16.0, 16 / 16.0, 2 / 16.0));
    }

    public static void renderMachineInventory(MTEDigitalChestBase mte, @Nullable IBlockAccess aWorld, int aX, int aY,
        int aZ, Block aBlock, RenderBlocks aRenderer) {
        mte.getBaseMetaTileEntity()
            .setFrontFacing(ForgeDirection.WEST);
        renderMachine(mte, aWorld, aX, aY, aZ, aBlock, aRenderer);
    }

    public static void renderMachine(MTEDigitalChestBase mte, @Nullable IBlockAccess aWorld, int aX, int aY, int aZ,
        Block aBlock, RenderBlocks aRenderer) {
        ForgeDirection frontFacing = mte.getBaseMetaTileEntity()
            .getFrontFacing();
        IIcon casingIcon = MACHINECASINGS_SIDE[mte.mTier].getIcon();

        CCRenderState state = CCRenderState.instance();
        boolean isDrawing = false;
        if (aRenderer.useInventoryTint && !GTUtilityClient.isDrawing(Tessellator.instance)) {
            // Draw if we're not already drawing
            isDrawing = true;
            Tessellator.instance.startDrawingQuads();
        }

        // spotless:off
        renderFace(state, frontFacing, glassBox, aWorld, aX, aY, aZ, aBlock, aRenderer, mte, OVERLAY_SCREEN_GLASS.getIcon());
        // front frame
        for (var boxFacing : boxFacingMap.keySet()) {
            // do not render the box at the front face when "facing" is "frontFacing"
            if (boxFacing == frontFacing) continue;

            // render when the box face matches facing
            renderFace(state, boxFacing, boxFacingMap.get(boxFacing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);

            // render when the box face is opposite of facing
            renderFace(state, boxFacing.getOpposite(), boxFacingMap.get(boxFacing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
        }

        // render the sides of the box that face the front face
        if (frontFacing == UP) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
        } else if (frontFacing == DOWN) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
        } else {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);

            ForgeDirection facing = rotateYCCW(frontFacing);
            renderFace(state, frontFacing, boxFacingMap.get(facing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
            renderFace(state, frontFacing, boxFacingMap.get(facing.getOpposite()), aWorld, aX, aY, aZ, aBlock, aRenderer, mte, casingIcon);
        }
        //spotless:on

        if (aRenderer.useInventoryTint && isDrawing) {
            // Draw if we initiated the drawing
            isDrawing = false;
            Tessellator.instance.draw();
        }
    }

    private static void renderFace(CCRenderState state, ForgeDirection face, Cuboid6 bounds,
                                   @Nullable IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer,
                                   MTEDigitalChestBase mte, IIcon icon) {
        int aColor = mte.getBaseMetaTileEntity()
            .getColorization();
        short[] rgba = Dyes.getModulation(aColor, Dyes.MACHINE_METAL.mRGBa);
        state.resetInstance();
        state.baseColour = new ColourRGBA(rgba[0], rgba[1], rgba[2], 255).rgba();
        state.setDynamicInstance();
        state.pullLightmapInstance();
        state.setPipelineInstance(
            new Translation(aX, aY, aZ),
            new IconTransformation(icon),
            state.lightMatrix);
        if (aWorld != null) {
            state.setBrightnessInstance(aWorld, aX, aY, aZ);
        }

        state.setModelInstance(blockFace);
        blockFace.loadCuboidFace(bounds, face.ordinal());
        state.renderInstance();
    }

    private static ForgeDirection rotateYCCW(ForgeDirection dir) {
        switch (dir) {
            case NORTH:
                return ForgeDirection.WEST;
            case WEST:
                return ForgeDirection.SOUTH;
            case SOUTH:
                return ForgeDirection.EAST;
            case EAST:
                return ForgeDirection.NORTH;
            default:
                throw new IllegalArgumentException("Can't rotate Y on " + dir);
        }
    }

    public static void renderChestStack(MTEDigitalChestBase mte, double x, double y, double z,
        float timeSinceLastTick) {
         ItemStack content = mte.displayItem;
         if(content == null) {
            return;
         }
         content.stackSize = 1;
        // TODO: config fancyrenderer

        float lastBrightnessX = OpenGlHelper.lastBrightnessX;
        float lastBrightnessY = OpenGlHelper.lastBrightnessY;
        World world = mte.getBaseMetaTileEntity()
            .getWorld();

        if (canRender(
            x,
            y,
            z,
            8 * MathHelper.clamp((double) Minecraft.getMinecraft().gameSettings.renderDistanceChunks / 8, 1.0, 2.5))) {
            float tick = world.getTotalWorldTime() + timeSinceLastTick;
            EntityItem entityItem = new EntityItem(world, 0, 0, 0, content);
            entityItem.hoverStart = 0f;
            GlStateManager.pushMatrix();
            GlStateManager.translate(x, y, z);
            GlStateManager.translate(0.5D, 0.25D, 0.5D);
            GlStateManager.rotate(tick * (float) Math.PI * 2 / 40, 0, 1, 0);
            GlStateManager.scale(1.5f, 1.5f, 1.5f);
            RenderManager.instance.renderEntityWithPosYaw(entityItem, 0, 0, 0, 0, 0);
            GlStateManager.popMatrix();
        }

         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, 240, 240);
         renderAmountText(x, y, z, mte.displayItemCount, mte.getBaseMetaTileEntity().getFrontFacing());
         OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, lastBrightnessX, lastBrightnessY);
    }

    public static void renderAmountText(double x, double y, double z, long amount, ForgeDirection frontFacing) {
        if (!canRender(x, y, z, 64))
            return;

        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, z);
        GlStateManager.translate(frontFacing.offsetX * -1 / 16f, frontFacing.offsetY * -1 / 16f,
            frontFacing.offsetZ * -1 / 16f);
        GlStateManager.translate(0.5 + frontFacing.offsetX * 0.5, 0.5 + frontFacing.offsetY * 0.5,
            0.5 + frontFacing.offsetZ * 0.5);
        if (frontFacing == UP || frontFacing == DOWN) {
            rotateToFace(frontFacing, ForgeDirection.SOUTH);
        } else {
            rotateToFace(frontFacing, null);
        }
        String amountText = GTUtility.formatNumbers(amount);
        GlStateManager.scale(1f / 64, 1f / 64, 0);
        GlStateManager.translate(-32, -32, 0);
        GlStateManager.disableLighting();
        fontRenderer.drawString(amountText, 32 - fontRenderer.getStringWidth(amountText) / 2, 40, 0xFFFFFF,false);
        GlStateManager.enableLighting();
        GlStateManager.popMatrix();
    }

    /**
     * Takes in the difference in x, y, and z from the camera to the rendering TE and
     * calculates the squared distance and checks if it's within the range squared
     *
     * @param x     the difference in x from entity to this rendering TE
     * @param y     the difference in y from entity to this rendering TE
     * @param z     the difference in z from entity to this rendering TE
     * @param range distance needed to be rendered
     * @return true if the camera is within the given range, otherwise false
     */
    public static boolean canRender(double x, double y, double z, double range) {
        double distance = (x * x) + (y * y) + (z * z);
        return distance < range * range;
    }

    public static void rotateToFace(ForgeDirection face, @Nullable ForgeDirection spin) {
        int angle = spin == ForgeDirection.EAST ? 90 : spin == ForgeDirection.SOUTH ? 180 : spin == ForgeDirection.WEST ? -90 : 0;
        switch (face) {
            case UP:
                GlStateManager.scale(1.0f, -1.0f, 1.0f);
                GlStateManager.rotate(90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
                break;
            case DOWN:
                GlStateManager.scale(1.0f, -1.0f, 1.0f);
                GlStateManager.rotate(-90.0f, 1.0f, 0.0f, 0.0f);
                GlStateManager.rotate(spin == ForgeDirection.EAST ? 90 :
                    spin == ForgeDirection.NORTH ? 180 : spin == ForgeDirection.WEST ? -90 : 0, 0, 0, 1);
                break;
            case EAST:
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(-90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
                break;
            case WEST:
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(90.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
                break;
            case NORTH:
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
                break;
            case SOUTH:
                GlStateManager.scale(-1.0f, -1.0f, -1.0f);
                GlStateManager.rotate(180.0f, 0.0f, 1.0f, 0.0f);
                GlStateManager.rotate(angle, 0, 0, 1);
                break;
            default:
                break;
        }
    }
}
