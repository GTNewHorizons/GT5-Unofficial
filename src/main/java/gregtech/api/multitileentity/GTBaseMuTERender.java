package gregtech.api.multitileentity;

import java.io.IOException;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.common.util.ForgeDirection;

import com.badlogic.ashley.core.Entity;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizons.mutecore.api.block.MultiTileEntityBlock;
import com.gtnewhorizons.mutecore.api.render.MuTERender;

import gregtech.api.enums.Mods;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.data.ExtendedFacingComponent;
import gregtech.api.render.TextureFactory;

public class GTBaseMuTERender implements MuTERender {

    protected ITexture front;
    protected ITexture back;
    protected ITexture left;
    protected ITexture right;
    protected ITexture top;
    protected ITexture bottom;

    public GTBaseMuTERender(String folder) {
        ITexture baseTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture frontOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture backOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture leftOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture rightOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture topOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        ITexture bottomOverlayTexture = TextureFactory.of(Textures.BlockIcons.VOID);
        for (SidedTextureNames textureName : SidedTextureNames.TEXTURES) {
            ITexture texture;
            try {
                Minecraft.getMinecraft()
                    .getResourceManager()
                    .getResource(
                        new ResourceLocation(
                            Mods.GregTech.ID,
                            "textures/blocks/multitileentity/" + folder + "/" + textureName.getName() + ".png"));
                texture = TextureFactory
                    .of(new GTMuTEIcon(Mods.GregTech.ID, "multitileentity/" + folder + "/" + textureName.getName()));
                switch (textureName) {
                    case Top -> topOverlayTexture = texture;
                    case Bottom -> bottomOverlayTexture = texture;
                    case Back -> backOverlayTexture = texture;
                    case Front -> frontOverlayTexture = texture;
                    case Left -> leftOverlayTexture = texture;
                    case Right -> rightOverlayTexture = texture;
                    case Base -> baseTexture = texture;
                }
            } catch (IOException ignored) {

            }
        }
        front = TextureFactory.of(baseTexture, frontOverlayTexture);
        back = TextureFactory.of(baseTexture, backOverlayTexture);
        left = TextureFactory.of(baseTexture, leftOverlayTexture);
        right = TextureFactory.of(baseTexture, rightOverlayTexture);
        top = TextureFactory.of(baseTexture, topOverlayTexture);
        bottom = TextureFactory.of(baseTexture, bottomOverlayTexture);
    }

    @Override
    public void render(Entity entity, RenderBlocks render, int x, int y, int z, IBlockAccess world) {
        Block block = null;
        if (world != null) {
            block = world.getBlock(x, y, z);
            if (!(block instanceof MultiTileEntityBlock)) {
                return;
            }
        }
        ExtendedFacingComponent extFacingComp = entity.getComponent(ExtendedFacingComponent.class);
        ExtendedFacing facing = extFacingComp != null ? extFacingComp.getExtendedFacing() : ExtendedFacing.WEST_NORMAL_NONE;
        renderYNegative(world, render, x, y, z, block, bottom, facing.getRelativeUpInWorld());
        renderYPositive(world, render, x, y, z, block, top, facing.getRelativeDownInWorld());
        renderXNegative(world, render, x, y, z, block, front, facing.getRelativeForwardInWorld());
        renderXPositive(world, render, x, y, z, block, back, facing.getRelativeBackInWorld());
        renderZNegative(world, render, x, y, z, block, left, facing.getRelativeLeftInWorld());
        renderZPositive(world, render, x, y, z, block, right, facing.getRelativeRightInWorld());
    }

    private static void renderYNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y - 1, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y - 1, z));
        }
        texture.renderYNeg(renderer, block, x, y, z);
    }

    private static void renderZNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y, z - 1, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z - 1));
        }
        texture.renderZNeg(renderer, block, x, y, z);
    }

    private static void renderXNegative(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x - 1, y, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x - 1, y, z));
        }
        texture.renderXNeg(renderer, block, x, y, z);
    }

    private static void renderYPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y + 1, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y + 1, z));
        }
        texture.renderYPos(renderer, block, x, y, z);
    }

    private static void renderXPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x + 1, y, z, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x + 1, y, z));
        }
        texture.renderXPos(renderer, block, x, y, z);
    }

    private static void renderZPositive(IBlockAccess world, RenderBlocks renderer, int x, int y, int z, Block block,
        ITexture texture, ForgeDirection side) {
        if (world != null) {
            if (!block.shouldSideBeRendered(world, x, y, z + 1, side.ordinal())) return;
            Tessellator.instance.setBrightness(block.getMixedBrightnessForBlock(world, x, y, z + 1));
        }
        texture.renderZPos(renderer, block, x, y, z);
    }
}
