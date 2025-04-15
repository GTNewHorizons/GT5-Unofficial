package gregtech.common.render;

import static gregtech.api.enums.Textures.BlockIcons.*;
import static net.minecraftforge.common.util.ForgeDirection.*;

import java.util.EnumMap;

import codechicken.lib.colour.ColourRGBA;
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
        boolean isDrawing = false;
        if (aRenderer.useInventoryTint && !GTUtilityClient.isDrawing(Tessellator.instance)) {
            // Draw if we're not already drawing
            isDrawing = true;
            Tessellator.instance.startDrawingQuads();
        }

        // front frame
        for (var boxFacing : boxFacingMap.keySet()) {
            // do not render the box at the front face when "facing" is "frontFacing"
            if (boxFacing == frontFacing) continue;

            // render when the box face matches facing
            renderFace(state, boxFacing, boxFacingMap.get(boxFacing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);

            // render when the box face is opposite of facing
            renderFace(state, boxFacing.getOpposite(), boxFacingMap.get(boxFacing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
        }

        // render the sides of the box that face the front face
        if (frontFacing == UP) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
        } else if (frontFacing == DOWN) {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.NORTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.SOUTH), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.EAST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.WEST), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
        } else {
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.DOWN), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(ForgeDirection.UP), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);

            ForgeDirection facing = rotateYCCW(frontFacing);
            renderFace(state, frontFacing, boxFacingMap.get(facing), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
            renderFace(state, frontFacing, boxFacingMap.get(facing.getOpposite()), aWorld, aX, aY, aZ, aBlock, aRenderer, mte);
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

    public static void renderFace(CCRenderState state, ForgeDirection face, Cuboid6 bounds,
                                  @Nullable IBlockAccess aWorld, int aX, int aY, int aZ,
                                  Block aBlock, RenderBlocks aRenderer, MTEDigitalChestBase mte) {
        int aColor = mte.getBaseMetaTileEntity().getColorization() - 1;
        short[] rgba = Dyes.getModulation(aColor,
            Dyes.MACHINE_METAL.mRGBa);
        Textures.BlockIcons casing = (Textures.BlockIcons) MACHINECASINGS_SIDE[mte.mTier];
        state.resetInstance();
        state.baseColour = new ColourRGBA(rgba[0],rgba[1],rgba[2],rgba[3]).rgba();
        state.setPipelineInstance(
            new Translation(aX, aY, aZ),
            new IconTransformation(casing.getIcon()));
        if(aWorld != null) {
            state.setBrightnessInstance(aWorld, aX, aY, aZ);
        }

        state.setModelInstance(blockFace);
        blockFace.loadCuboidFace(bounds, face.ordinal());
        state.renderInstance();
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
