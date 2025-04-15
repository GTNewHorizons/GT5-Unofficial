package gregtech.common.render;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.EnumMap;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;

import codechicken.lib.render.BlockRenderer;
import codechicken.lib.render.CCRenderState;
import codechicken.lib.render.uv.IconTransformation;
import codechicken.lib.vec.Cuboid6;
import codechicken.lib.vec.Translation;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtilityClient;
import gregtech.api.util.LightingHelper;
import gregtech.common.tileentities.storage.MTEDigitalChestBase;

// Backported from GTCEu
public class DigitalChestRenderer {

    private static final BlockRenderer.BlockFace blockFace = new BlockRenderer.BlockFace();

    private static final EnumMap<ForgeDirection, Cuboid6> boxFacingMap = new EnumMap<>(ForgeDirection.class);

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

        CCRenderState state = CCRenderState.instance();
        state.resetInstance();
        Textures.BlockIcons casing = (Textures.BlockIcons) MACHINECASINGS_SIDE[mte.mTier];
        state.setPipelineInstance(new Translation(aX, aY, aZ), new IconTransformation(casing.getIcon()));
        CCRenderState.changeTexture(casing.getTextureFile());
        boolean isDrawing = false;
        if (aRenderer.useInventoryTint && !GTUtilityClient.isDrawing(Tessellator.instance)) {
            // Draw if we're not already drawing
            isDrawing = true;
            Tessellator.instance.startDrawingQuads();
        }
        final LightingHelper lighting = new LightingHelper(aRenderer);
        if (aWorld != null) {
            Tessellator.instance.setBrightness(
                aBlock.getMixedBrightnessForBlock(
                    aWorld,
                    aX + frontFacing.offsetX,
                    aY + frontFacing.offsetY,
                    aZ + frontFacing.offsetZ));
            lighting.setupLighting(aBlock, aX, aY, aZ, frontFacing)
                .setupColor(
                    frontFacing,
                    Dyes.getModulation(
                        mte.getBaseMetaTileEntity()
                            .getColorization() - 1,
                        Dyes.MACHINE_METAL.mRGBa));
        }

        // front frame
        for (var boxFacing : boxFacingMap.keySet()) {
            // do not render the box at the front face when "facing" is "frontFacing"
            if (boxFacing == frontFacing) continue;

            // render when the box face matches facing
            renderFace(state, boxFacing, boxFacingMap.get(boxFacing));

            // render when the box face is opposite of facing
            renderFace(state, boxFacing.getOpposite(), boxFacingMap.get(boxFacing));
        }

        // render the sides of the box that face the front face
        if (frontFacing == UP) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN));
        } else if (frontFacing == DOWN) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP));
        } else {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN));
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP));

            ForgeDirection facing = rotateYCCW(frontFacing);
            renderFace(state, frontFacing, boxFacingMap.get(facing));
            renderFace(state, frontFacing, boxFacingMap.get(facing.getOpposite()));
        }

        if (aRenderer.useInventoryTint && isDrawing) {
            // Draw if we initiated the drawing
            isDrawing = false;
            Tessellator.instance.draw();
        }

        // render glass panel at the front
        ITexture[][] textureArray = new ITexture[6][];
        textureArray[frontFacing.ordinal()] = new ITexture[] { TextureFactory.builder()
            .addIcon(OVERLAY_SCREEN_GLASS)
            .build() };
        GTRendererBlock.INSTANCE.renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, textureArray);
    }

    public static void renderFace(CCRenderState renderState, ForgeDirection face, Cuboid6 bounds) {
        renderState.setModelInstance(blockFace);
        blockFace.loadCuboidFace(bounds, face.ordinal());
        renderState.renderInstance();
    }

    public static ForgeDirection rotateYCCW(ForgeDirection dir) {
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
}
