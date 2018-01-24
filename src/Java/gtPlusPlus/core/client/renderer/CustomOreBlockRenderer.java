package gtPlusPlus.core.client.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import gregtech.api.interfaces.ITexture;
import gtPlusPlus.api.interfaces.ITexturedBlock;
import gtPlusPlus.api.objects.Logger;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;

public class CustomOreBlockRenderer implements ISimpleBlockRenderingHandler {

	public static CustomOreBlockRenderer INSTANCE;
	public final int mRenderID;

	public CustomOreBlockRenderer() {
		this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
		INSTANCE = this;
		RenderingRegistry.registerBlockHandler(this);
		Logger.INFO("Registered Custom Ore Block Renderer.");
	}

	public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer) {
		Block tTileEntity = aBlock;
		if ((tTileEntity instanceof ITexturedBlock)) {
			return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer, new ITexture[][]{((ITexturedBlock) tTileEntity).getTexture((byte) 0), ((ITexturedBlock) tTileEntity).getTexture((byte) 1), ((ITexturedBlock) tTileEntity).getTexture((byte) 2), ((ITexturedBlock) tTileEntity).getTexture((byte) 3), ((ITexturedBlock) tTileEntity).getTexture((byte) 4), ((ITexturedBlock) tTileEntity).getTexture((byte) 5)});
		}
		return false;
	}

	public boolean renderStandardBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, RenderBlocks aRenderer, ITexture[][] aTextures) {
		aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		aRenderer.setRenderBoundsFromBlock(aBlock);
		int l = aBlock.colorMultiplier(aWorld, aX, aY, aZ);
        float RED = (float)(l >> 16 & 255) / 255.0F;
        float GREEN = (float)(l >> 8 & 255) / 255.0F;
        float BLUE = (float)(l & 255) / 255.0F;
		
		if (Minecraft.isAmbientOcclusionEnabled() && aBlock.getLightValue() == 0){
			if (RenderBlocks.getInstance().partialRenderBounds){
				return INSTANCE.renderStandardBlockWithAmbientOcclusionPartial(aWorld, aRenderer, aTextures, aBlock, aX, aY, aZ, RED, GREEN, BLUE);
			}
			else {
				return INSTANCE.renderStandardBlockWithAmbientOcclusion(aWorld, aRenderer, aTextures, aBlock, aX, aY, aZ, RED, GREEN, BLUE);
			}
		}
		else {
			renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[0], true);
			renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[1], true);
			renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[2], true);
			renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[3], true);
			renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[4], true);
			renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aTextures[5], true); 
		}
		return true;
	}

	public static void renderFaceYNeg(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderNegativeYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[0], true);
	}
	public static void renderFaceYPos(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderPositiveYFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[1], true);
	}
	public static void renderFaceZNeg(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderNegativeZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[2], true);
	}
	public static void renderFaceZPos(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderPositiveZFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[3], true);
	}
	public static void renderFaceXNeg(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderNegativeXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[4], true);
	}
	public static void renderFaceXPos(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[][] aIcon) {
		renderPositiveXFacing(aWorld, aRenderer, aBlock, aX, aY, aZ, aIcon[5], true);
	}

	public static void renderNegativeYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY - 1, aZ, 0))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY - 1 : aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderYNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveYFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY + 1, aZ, 1))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aFullBlock ? aY + 1 : aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderYPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderNegativeZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ - 1, 2))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ - 1 : aZ));
		}
		aRenderer.flipTexture = (!aFullBlock);
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderZNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveZFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX, aY, aZ + 1, 3))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aX, aY, aFullBlock ? aZ + 1 : aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderZPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderNegativeXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX - 1, aY, aZ, 4))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX - 1 : aX, aY, aZ));
		}
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderXNeg(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public static void renderPositiveXFacing(IBlockAccess aWorld, RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ, ITexture[] aIcon, boolean aFullBlock) {
		if (aWorld != null) {
			if ((aFullBlock) && (!aBlock.shouldSideBeRendered(aWorld, aX + 1, aY, aZ, 5))) {
				return;
			}
			Tessellator.instance.setBrightness(aBlock.getMixedBrightnessForBlock(aWorld, aFullBlock ? aX + 1 : aX, aY, aZ));
		}
		aRenderer.flipTexture = (!aFullBlock);
		if (aIcon != null) {
			for (int i = 0; i < aIcon.length; i++) {
				if (aIcon[i] != null) {
					aIcon[i].renderXPos(aRenderer, aBlock, aX, aY, aZ);
				}
			}
		}
		aRenderer.flipTexture = false;
	}

	public void renderInventoryBlock(Block aBlock, int aMeta, int aModelID, RenderBlocks aRenderer) {
		aBlock.setBlockBoundsForItemRender();
		aRenderer.setRenderBoundsFromBlock(aBlock);
		GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, -1.0F, 0.0F);
		renderNegativeYFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 0), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 1.0F, 0.0F);
		renderPositiveYFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 1), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 0.0F, -1.0F);
		renderNegativeZFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 2), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(0.0F, 0.0F, 1.0F);
		renderPositiveZFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 3), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(-1.0F, 0.0F, 0.0F);
		renderNegativeXFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 4), true);
		Tessellator.instance.draw();
		Tessellator.instance.startDrawingQuads();
		Tessellator.instance.setNormal(1.0F, 0.0F, 0.0F);
		renderPositiveXFacing(null, aRenderer, aBlock, 0, 0, 0, ((ITexturedBlock) aBlock).getTexture((byte) 5), true);
		Tessellator.instance.draw();
		aBlock.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
		aRenderer.setRenderBoundsFromBlock(aBlock);
		GL11.glTranslatef(0.5F, 0.5F, 0.5F);
	}

	public boolean renderWorldBlock(IBlockAccess aWorld, int aX, int aY, int aZ, Block aBlock, int aModelID, RenderBlocks aRenderer) {
		blockAccess = aWorld;
		return renderStandardBlock(aWorld, aX, aY, aZ, aBlock, aRenderer);
	}

	public boolean shouldRender3DInInventory(int aModel) {
		return true;
	}

	public int getRenderId() {
		return this.mRenderID;
	}
	
	public void setRenderBounds(double p_147782_1_, double p_147782_3_, double p_147782_5_, double p_147782_7_, double p_147782_9_, double p_147782_11_)
	{
		if (!this.lockBlockBounds)
		{
			this.renderMinX = p_147782_1_;
			this.renderMaxX = p_147782_7_;
			this.renderMinY = p_147782_3_;
			this.renderMaxY = p_147782_9_;
			this.renderMinZ = p_147782_5_;
			this.renderMaxZ = p_147782_11_;
			this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
		}
	}

	/**
	 * Like setRenderBounds, but automatically pulling the bounds from the given Block.
	 */
	public void setRenderBoundsFromBlock(Block block)
	{
		if (!this.lockBlockBounds)
		{
			this.renderMinX = block.getBlockBoundsMinX();
			this.renderMaxX = block.getBlockBoundsMaxX();
			this.renderMinY = block.getBlockBoundsMinY();
			this.renderMaxY = block.getBlockBoundsMaxY();
			this.renderMinZ = block.getBlockBoundsMinZ();
			this.renderMaxZ = block.getBlockBoundsMaxZ();
			this.partialRenderBounds = this.minecraftRB.gameSettings.ambientOcclusion >= 2 && (this.renderMinX > 0.0D || this.renderMaxX < 1.0D || this.renderMinY > 0.0D || this.renderMaxY < 1.0D || this.renderMinZ > 0.0D || this.renderMaxZ < 1.0D);
		}
	}

	/**
	 * Vanilla Variables
	 */

	/** The minimum X value for rendering (default 0.0). */
	public double renderMinX;
	/** The maximum X value for rendering (default 1.0). */
	public double renderMaxX;
	/** The minimum Y value for rendering (default 0.0). */
	public double renderMinY;
	/** The maximum Y value for rendering (default 1.0). */
	public double renderMaxY;
	/** The minimum Z value for rendering (default 0.0). */
	public double renderMinZ;
	/** The maximum Z value for rendering (default 1.0). */
	public double renderMaxZ;
	public boolean lockBlockBounds;
	public boolean partialRenderBounds;
	public final Minecraft minecraftRB = RenderBlocks.getInstance().minecraftRB;
	public int uvRotateEast;
	public int uvRotateWest;
	public int uvRotateSouth;
	public int uvRotateNorth;
	public int uvRotateTop;
	public int uvRotateBottom;
	/** Whether ambient occlusion is enabled or not */
	public boolean enableAO;
	/** Used as a scratch variable for ambient occlusion on the north/bottom/east corner. */
	public float aoLightValueScratchXYZNNN;
	/** Used as a scratch variable for ambient occlusion between the bottom face and the north face. */
	public float aoLightValueScratchXYNN;
	/** Used as a scratch variable for ambient occlusion on the north/bottom/west corner. */
	public float aoLightValueScratchXYZNNP;
	/** Used as a scratch variable for ambient occlusion between the bottom face and the east face. */
	public float aoLightValueScratchYZNN;
	/** Used as a scratch variable for ambient occlusion between the bottom face and the west face. */
	public float aoLightValueScratchYZNP;
	/** Used as a scratch variable for ambient occlusion on the south/bottom/east corner. */
	public float aoLightValueScratchXYZPNN;
	/** Used as a scratch variable for ambient occlusion between the bottom face and the south face. */
	public float aoLightValueScratchXYPN;
	/** Used as a scratch variable for ambient occlusion on the south/bottom/west corner. */
	public float aoLightValueScratchXYZPNP;
	/** Used as a scratch variable for ambient occlusion on the north/top/east corner. */
	public float aoLightValueScratchXYZNPN;
	/** Used as a scratch variable for ambient occlusion between the top face and the north face. */
	public float aoLightValueScratchXYNP;
	/** Used as a scratch variable for ambient occlusion on the north/top/west corner. */
	public float aoLightValueScratchXYZNPP;
	/** Used as a scratch variable for ambient occlusion between the top face and the east face. */
	public float aoLightValueScratchYZPN;
	/** Used as a scratch variable for ambient occlusion on the south/top/east corner. */
	public float aoLightValueScratchXYZPPN;
	/** Used as a scratch variable for ambient occlusion between the top face and the south face. */
	public float aoLightValueScratchXYPP;
	/** Used as a scratch variable for ambient occlusion between the top face and the west face. */
	public float aoLightValueScratchYZPP;
	/** Used as a scratch variable for ambient occlusion on the south/top/west corner. */
	public float aoLightValueScratchXYZPPP;
	/** Used as a scratch variable for ambient occlusion between the north face and the east face. */
	public float aoLightValueScratchXZNN;
	/** Used as a scratch variable for ambient occlusion between the south face and the east face. */
	public float aoLightValueScratchXZPN;
	/** Used as a scratch variable for ambient occlusion between the north face and the west face. */
	public float aoLightValueScratchXZNP;
	/** Used as a scratch variable for ambient occlusion between the south face and the west face. */
	public float aoLightValueScratchXZPP;
	/** Ambient occlusion brightness XYZNNN */
	public int aoBrightnessXYZNNN;
	/** Ambient occlusion brightness XYNN */
	public int aoBrightnessXYNN;
	/** Ambient occlusion brightness XYZNNP */
	public int aoBrightnessXYZNNP;
	/** Ambient occlusion brightness YZNN */
	public int aoBrightnessYZNN;
	/** Ambient occlusion brightness YZNP */
	public int aoBrightnessYZNP;
	/** Ambient occlusion brightness XYZPNN */
	public int aoBrightnessXYZPNN;
	/** Ambient occlusion brightness XYPN */
	public int aoBrightnessXYPN;
	/** Ambient occlusion brightness XYZPNP */
	public int aoBrightnessXYZPNP;
	/** Ambient occlusion brightness XYZNPN */
	public int aoBrightnessXYZNPN;
	/** Ambient occlusion brightness XYNP */
	public int aoBrightnessXYNP;
	/** Ambient occlusion brightness XYZNPP */
	public int aoBrightnessXYZNPP;
	/** Ambient occlusion brightness YZPN */
	public int aoBrightnessYZPN;
	/** Ambient occlusion brightness XYZPPN */
	public int aoBrightnessXYZPPN;
	/** Ambient occlusion brightness XYPP */
	public int aoBrightnessXYPP;
	/** Ambient occlusion brightness YZPP */
	public int aoBrightnessYZPP;
	/** Ambient occlusion brightness XYZPPP */
	public int aoBrightnessXYZPPP;
	/** Ambient occlusion brightness XZNN */
	public int aoBrightnessXZNN;
	/** Ambient occlusion brightness XZPN */
	public int aoBrightnessXZPN;
	/** Ambient occlusion brightness XZNP */
	public int aoBrightnessXZNP;
	/** Ambient occlusion brightness XZPP */
	public int aoBrightnessXZPP;
	/** Brightness top left */
	public int brightnessTopLeft;
	/** Brightness bottom left */
	public int brightnessBottomLeft;
	/** Brightness bottom right */
	public int brightnessBottomRight;
	/** Brightness top right */
	public int brightnessTopRight;
	/** Red color value for the top left corner */
	public float colorRedTopLeft;
	/** Red color value for the bottom left corner */
	public float colorRedBottomLeft;
	/** Red color value for the bottom right corner */
	public float colorRedBottomRight;
	/** Red color value for the top right corner */
	public float colorRedTopRight;
	/** Green color value for the top left corner */
	public float colorGreenTopLeft;
	/** Green color value for the bottom left corner */
	public float colorGreenBottomLeft;
	/** Green color value for the bottom right corner */
	public float colorGreenBottomRight;
	/** Green color value for the top right corner */
	public float colorGreenTopRight;
	/** Blue color value for the top left corner */
	public float colorBlueTopLeft;
	/** Blue color value for the bottom left corner */
	public float colorBlueBottomLeft;
	/** Blue color value for the bottom right corner */
	public float colorBlueBottomRight;
	/** Blue color value for the top right corner */
	public float colorBlueTopRight;
	/** If set to >=0, all block faces will be rendered using this texture index */
	public IIcon overrideBlockTexture;
	/**
	 * Clear override block texture
	 */
	public void clearOverrideBlockTexture()
	{
		this.overrideBlockTexture = null;
	}

	public boolean hasOverrideBlockTexture()
	{
		return this.overrideBlockTexture != null;
	}

	public IIcon getBlockIcon(Block block, IBlockAccess access, int x, int y, int z, int side)
	{
		return this.getIconSafe(block.getIcon(access, x, y, z, side));
	}

	public IIcon getBlockIconFromSideAndMetadata(Block block, int side, int meta)
	{
		return this.getIconSafe(block.getIcon(side, meta));
	}

	public IIcon getBlockIconFromSide(Block block, int side)
	{
		return this.getIconSafe(block.getBlockTextureFromSide(side));
	}

	public IIcon getBlockIcon(Block block)
	{
		return this.getIconSafe(block.getBlockTextureFromSide(1));
	}

	public IIcon getIconSafe(IIcon iicon)
	{
		if (iicon == null)
		{
			iicon = ((TextureMap) Minecraft.getMinecraft().getTextureManager().getTexture(TextureMap.locationBlocksTexture)).getAtlasSprite("missingno");
		}

		return (IIcon)iicon;
	}

	IBlockAccess blockAccess = RenderBlocks.getInstance().blockAccess;
	
	public boolean renderStandardBlockWithAmbientOcclusion(IBlockAccess aWorld, RenderBlocks aRenderer, ITexture[][] aTextures, Block block, int xPos, int yPos, int zPos, float R, float G, float B)
	{
        this.enableAO = true;
        boolean flag = false;
        float f3 = 0.0F;
        float f4 = 0.0F;
        float f5 = 0.0F;
        float f6 = 0.0F;
        boolean flag1 = true;
        int l = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos);
        Tessellator tessellator = Tessellator.instance;
        tessellator.setBrightness(983055);

        if (this.getBlockIcon(block).getIconName().equals("grass_top"))
        {
            flag1 = false;
        }
        else if (this.hasOverrideBlockTexture())
        {
            flag1 = false;
        }

        boolean flag2;
        boolean flag3;
        boolean flag4;
        boolean flag5;
        int i1;
        float f7;

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos - 1, zPos, 0))
        {
            if (this.renderMinY <= 0.0D)
            {
                --yPos;
            }

            this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
            this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
            this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
            this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
            this.aoLightValueScratchXYNN = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZNN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZNP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXYPN = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
                this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos + 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos - 1);
            }

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
                this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos + 1);
            }

            if (this.renderMinY <= 0.0D)
            {
                ++yPos;
            }

            i1 = l;

            if (this.renderMinY <= 0.0D || !blockAccess.getBlock(xPos, yPos - 1, zPos).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
            }

            f7 = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
            f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + f7) / 4.0F;
            f6 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
            f5 = (f7 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
            f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + f7 + this.aoLightValueScratchYZNN) / 4.0F;
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.5F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            CustomOreBlockRenderer.renderFaceYNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            flag = true;
        }

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos + 1, zPos, 1))
        {
            if (this.renderMaxY >= 1.0D)
            {
                ++yPos;
            }

            this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
            this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
            this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
            this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
            this.aoLightValueScratchXYNP = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXYPP = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZPN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZPP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
            flag2 = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getCanBlockGrass();

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos - 1);
            }

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos + 1);
            }

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos + 1);
            }

            if (this.renderMaxY >= 1.0D)
            {
                --yPos;
            }

            i1 = l;

            if (this.renderMaxY >= 1.0D || !blockAccess.getBlock(xPos, yPos + 1, zPos).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
            }

            f7 = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
            f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + f7) / 4.0F;
            f3 = (this.aoLightValueScratchYZPP + f7 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R;
            this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G;
            this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B;
            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            CustomOreBlockRenderer.renderFaceYPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            flag = true;
        }

        IIcon iicon;

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos, zPos - 1, 2))
        {
            if (this.renderMinZ <= 0.0D)
            {
                --zPos;
            }

            this.aoLightValueScratchXZNN = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZNN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZPN = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZPN = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
            this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
            this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
            this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
            flag2 = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos - 1, zPos);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos + 1, zPos);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos - 1, zPos);
            }

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos + 1, zPos);
            }

            if (this.renderMinZ <= 0.0D)
            {
                ++zPos;
            }

            i1 = l;

            if (this.renderMinZ <= 0.0D || !blockAccess.getBlock(xPos, yPos, zPos - 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
            }

            f7 = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
            f3 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
            f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
            f5 = (this.aoLightValueScratchYZNN + f7 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
            f6 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + f7) / 4.0F;
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 2);
            CustomOreBlockRenderer.renderFaceZNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

            RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= R;
                this.colorRedBottomLeft *= R;
                this.colorRedBottomRight *= R;
                this.colorRedTopRight *= R;
                this.colorGreenTopLeft *= G;
                this.colorGreenBottomLeft *= G;
                this.colorGreenBottomRight *= G;
                this.colorGreenTopRight *= G;
                this.colorBlueTopLeft *= B;
                this.colorBlueBottomLeft *= B;
                this.colorBlueBottomRight *= B;
                this.colorBlueTopRight *= B;
                CustomOreBlockRenderer.renderFaceZNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            }

            flag = true;
        }

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos, zPos + 1, 3))
        {
            if (this.renderMaxZ >= 1.0D)
            {
                ++zPos;
            }

            this.aoLightValueScratchXZNP = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZPP = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZNP = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchYZPP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
            this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
            this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
            this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
            this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
            flag2 = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos - 1, zPos);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos + 1, zPos);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos - 1, zPos);
            }

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos + 1, zPos);
            }

            if (this.renderMaxZ >= 1.0D)
            {
                --zPos;
            }

            i1 = l;

            if (this.renderMaxZ >= 1.0D || !blockAccess.getBlock(xPos, yPos, zPos + 1).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
            }

            f7 = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
            f3 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + f7 + this.aoLightValueScratchYZPP) / 4.0F;
            f6 = (f7 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            f5 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
            f4 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + f7) / 4.0F;
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.8F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 3);
            CustomOreBlockRenderer.renderFaceZPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

            RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= R;
                this.colorRedBottomLeft *= R;
                this.colorRedBottomRight *= R;
                this.colorRedTopRight *= R;
                this.colorGreenTopLeft *= G;
                this.colorGreenBottomLeft *= G;
                this.colorGreenBottomRight *= G;
                this.colorGreenTopRight *= G;
                this.colorBlueTopLeft *= B;
                this.colorBlueBottomLeft *= B;
                this.colorBlueBottomRight *= B;
                this.colorBlueTopRight *= B;
                CustomOreBlockRenderer.renderFaceZPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            }

            flag = true;
        }

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos - 1, yPos, zPos, 4))
        {
            if (this.renderMinX <= 0.0D)
            {
                --xPos;
            }

            this.aoLightValueScratchXYNN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZNN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZNP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXYNP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
            this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
            this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
            this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
            this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
            flag2 = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getCanBlockGrass();

            if (!flag4 && !flag3)
            {
                this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos - 1);
            }

            if (!flag5 && !flag3)
            {
                this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos + 1);
            }

            if (!flag4 && !flag2)
            {
                this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
                this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
            }
            else
            {
                this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos - 1);
            }

            if (!flag5 && !flag2)
            {
                this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
                this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
            }
            else
            {
                this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos + 1);
            }

            if (this.renderMinX <= 0.0D)
            {
                ++xPos;
            }

            i1 = l;

            if (this.renderMinX <= 0.0D || !blockAccess.getBlock(xPos - 1, yPos, zPos).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
            }

            f7 = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
            f6 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + f7 + this.aoLightValueScratchXZNP) / 4.0F;
            f3 = (f7 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
            f4 = (this.aoLightValueScratchXZNN + f7 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
            f5 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + f7) / 4.0F;
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 4);
            CustomOreBlockRenderer.renderFaceXNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

            RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= R;
                this.colorRedBottomLeft *= R;
                this.colorRedBottomRight *= R;
                this.colorRedTopRight *= R;
                this.colorGreenTopLeft *= G;
                this.colorGreenBottomLeft *= G;
                this.colorGreenBottomRight *= G;
                this.colorGreenTopRight *= G;
                this.colorBlueTopLeft *= B;
                this.colorBlueBottomLeft *= B;
                this.colorBlueBottomRight *= B;
                this.colorBlueTopRight *= B;
                CustomOreBlockRenderer.renderFaceXNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            }

            flag = true;
        }

        if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos + 1, yPos, zPos, 5))
        {
            if (this.renderMaxX >= 1.0D)
            {
                ++xPos;
            }

            this.aoLightValueScratchXYPN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZPN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXZPP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
            this.aoLightValueScratchXYPP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
            this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
            this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
            this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
            this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
            flag2 = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getCanBlockGrass();
            flag3 = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getCanBlockGrass();
            flag4 = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getCanBlockGrass();
            flag5 = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getCanBlockGrass();

            if (!flag3 && !flag5)
            {
                this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos - 1);
            }

            if (!flag3 && !flag4)
            {
                this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos + 1);
            }

            if (!flag2 && !flag5)
            {
                this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
                this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
            }
            else
            {
                this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos - 1);
            }

            if (!flag2 && !flag4)
            {
                this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
                this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
            }
            else
            {
                this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getAmbientOcclusionLightValue();
                this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos + 1);
            }

            if (this.renderMaxX >= 1.0D)
            {
                --xPos;
            }

            i1 = l;

            if (this.renderMaxX >= 1.0D || !blockAccess.getBlock(xPos + 1, yPos, zPos).isOpaqueCube())
            {
                i1 = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
            }

            f7 = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
            f3 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + f7 + this.aoLightValueScratchXZPP) / 4.0F;
            f4 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + f7) / 4.0F;
            f5 = (this.aoLightValueScratchXZPN + f7 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
            f6 = (f7 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
            this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
            this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
            this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
            this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);

            if (flag1)
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.6F;
            }
            else
            {
                this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
                this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
                this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
            }

            this.colorRedTopLeft *= f3;
            this.colorGreenTopLeft *= f3;
            this.colorBlueTopLeft *= f3;
            this.colorRedBottomLeft *= f4;
            this.colorGreenBottomLeft *= f4;
            this.colorBlueBottomLeft *= f4;
            this.colorRedBottomRight *= f5;
            this.colorGreenBottomRight *= f5;
            this.colorBlueBottomRight *= f5;
            this.colorRedTopRight *= f6;
            this.colorGreenTopRight *= f6;
            this.colorBlueTopRight *= f6;
            iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 5);
            CustomOreBlockRenderer.renderFaceXPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

            RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
            {
                this.colorRedTopLeft *= R;
                this.colorRedBottomLeft *= R;
                this.colorRedBottomRight *= R;
                this.colorRedTopRight *= R;
                this.colorGreenTopLeft *= G;
                this.colorGreenBottomLeft *= G;
                this.colorGreenBottomRight *= G;
                this.colorGreenTopRight *= G;
                this.colorBlueTopLeft *= B;
                this.colorBlueBottomLeft *= B;
                this.colorBlueBottomRight *= B;
                this.colorBlueTopRight *= B;
                CustomOreBlockRenderer.renderFaceXPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
            }

            flag = true;
        }

        this.enableAO = false;
        return flag;
    }
	
	/**
	 * Renders non-full-cube block with ambient occusion.  Args: block, x, y, z, red, green, blue (lighting)
	 */
	public boolean renderStandardBlockWithAmbientOcclusionPartial(IBlockAccess aWorld, RenderBlocks aRenderer, ITexture[][] aTextures, Block block, int xPos, int yPos, int zPos, float R, float G, float B)
	{
		this.enableAO = true;
		boolean flag = false;
		float f3 = 0.0F;
		float f4 = 0.0F;
		float f5 = 0.0F;
		float f6 = 0.0F;
		boolean flag1 = true;
		int l = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos);
		Tessellator tessellator = Tessellator.instance;
		tessellator.setBrightness(983055);

		if (this.getBlockIcon(block).getIconName().equals("grass_top"))
		{
			flag1 = false;
		}
		else if (this.hasOverrideBlockTexture())
		{
			flag1 = false;
		}

		boolean flag2;
		boolean flag3;
		boolean flag4;
		boolean flag5;
		int i1;
		float f7;

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos - 1, zPos, 0))
		{
			if (this.renderMinY <= 0.0D)
			{
				--yPos;
			}

			this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
			this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
			this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
			this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
			this.aoLightValueScratchXYNN = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZNN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZNP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXYPN = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXYNN;
			}
			else
			{
				this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos - 1);
			}

			if (!flag4 && !flag3)
			{
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXYNN;
				this.aoBrightnessXYZNNP = this.aoBrightnessXYNN;
			}
			else
			{
				this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos + 1);
			}

			if (!flag5 && !flag2)
			{
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXYPN;
			}
			else
			{
				this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos - 1);
			}

			if (!flag4 && !flag2)
			{
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXYPN;
				this.aoBrightnessXYZPNP = this.aoBrightnessXYPN;
			}
			else
			{
				this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos + 1);
			}

			if (this.renderMinY <= 0.0D)
			{
				++yPos;
			}

			i1 = l;

			if (this.renderMinY <= 0.0D || !blockAccess.getBlock(xPos, yPos - 1, zPos).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
			}

			f7 = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
			f3 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXYNN + this.aoLightValueScratchYZNP + f7) / 4.0F;
			f6 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXYPN) / 4.0F;
			f5 = (f7 + this.aoLightValueScratchYZNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNN) / 4.0F;
			f4 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNN + f7 + this.aoLightValueScratchYZNN) / 4.0F;
			this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXYNN, this.aoBrightnessYZNP, i1);
			this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXYPN, i1);
			this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYPN, this.aoBrightnessXYZPNN, i1);
			this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNN, this.aoBrightnessYZNN, i1);

			if (flag1)
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.5F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.5F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.5F;
			}
			else
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.5F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.5F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.5F;
			}

			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			CustomOreBlockRenderer.renderFaceYNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			flag = true;
		}

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos + 1, zPos, 1))
		{
			if (this.renderMaxY >= 1.0D)
			{
				++yPos;
			}

			this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
			this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
			this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
			this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
			this.aoLightValueScratchXYNP = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXYPP = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZPN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZPP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
			flag2 = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getCanBlockGrass();

			if (!flag5 && !flag3)
			{
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPN = this.aoBrightnessXYNP;
			}
			else
			{
				this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos - 1);
			}

			if (!flag5 && !flag2)
			{
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPN = this.aoBrightnessXYPP;
			}
			else
			{
				this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos - 1);
			}

			if (!flag4 && !flag3)
			{
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXYNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXYNP;
			}
			else
			{
				this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos + 1);
			}

			if (!flag4 && !flag2)
			{
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXYPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXYPP;
			}
			else
			{
				this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos + 1);
			}

			if (this.renderMaxY >= 1.0D)
			{
				--yPos;
			}

			i1 = l;

			if (this.renderMaxY >= 1.0D || !blockAccess.getBlock(xPos, yPos + 1, zPos).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
			}

			f7 = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
			f6 = (this.aoLightValueScratchXYZNPP + this.aoLightValueScratchXYNP + this.aoLightValueScratchYZPP + f7) / 4.0F;
			f3 = (this.aoLightValueScratchYZPP + f7 + this.aoLightValueScratchXYZPPP + this.aoLightValueScratchXYPP) / 4.0F;
			f4 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPN) / 4.0F;
			f5 = (this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
			this.brightnessTopRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNPP, this.aoBrightnessXYNP, this.aoBrightnessYZPP, i1);
			this.brightnessTopLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXYZPPP, this.aoBrightnessXYPP, i1);
			this.brightnessBottomLeft = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXYPP, this.aoBrightnessXYZPPN, i1);
			this.brightnessBottomRight = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNP, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
			this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R;
			this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G;
			this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B;
			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			CustomOreBlockRenderer.renderFaceYPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			flag = true;
		}

		float f8;
		float f9;
		float f10;
		float f11;
		int j1;
		int k1;
		int l1;
		int i2;
		IIcon iicon;

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos, zPos - 1, 2))
		{
			if (this.renderMinZ <= 0.0D)
			{
				--zPos;
			}

			this.aoLightValueScratchXZNN = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZNN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZPN = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZPN = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
			this.aoBrightnessYZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
			this.aoBrightnessYZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
			this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
			flag2 = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			}
			else
			{
				this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos - 1, zPos);
			}

			if (!flag3 && !flag4)
			{
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			}
			else
			{
				this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos + 1, zPos);
			}

			if (!flag2 && !flag5)
			{
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			}
			else
			{
				this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos - 1, zPos);
			}

			if (!flag2 && !flag4)
			{
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			}
			else
			{
				this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos + 1, zPos);
			}

			if (this.renderMinZ <= 0.0D)
			{
				++zPos;
			}

			i1 = l;

			if (this.renderMinZ <= 0.0D || !blockAccess.getBlock(xPos, yPos, zPos - 1).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
			}

			f7 = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
			f8 = (this.aoLightValueScratchXZNN + this.aoLightValueScratchXYZNPN + f7 + this.aoLightValueScratchYZPN) / 4.0F;
			f9 = (f7 + this.aoLightValueScratchYZPN + this.aoLightValueScratchXZPN + this.aoLightValueScratchXYZPPN) / 4.0F;
			f10 = (this.aoLightValueScratchYZNN + f7 + this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXZPN) / 4.0F;
			f11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXZNN + this.aoLightValueScratchYZNN + f7) / 4.0F;
			f3 = (float)((double)f8 * this.renderMaxY * (1.0D - this.renderMinX) + (double)f9 * this.renderMaxY * this.renderMinX + (double)f10 * (1.0D - this.renderMaxY) * this.renderMinX + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			f4 = (float)((double)f8 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMaxY * this.renderMaxX + (double)f10 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			f5 = (float)((double)f8 * this.renderMinY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMinY * this.renderMaxX + (double)f10 * (1.0D - this.renderMinY) * this.renderMaxX + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			f6 = (float)((double)f8 * this.renderMinY * (1.0D - this.renderMinX) + (double)f9 * this.renderMinY * this.renderMinX + (double)f10 * (1.0D - this.renderMinY) * this.renderMinX + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
			j1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessYZPN, i1);
			k1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPN, this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, i1);
			l1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNN, this.aoBrightnessXYZPNN, this.aoBrightnessXZPN, i1);
			i2 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXZNN, this.aoBrightnessYZNN, i1);
			this.brightnessTopLeft = RenderBlocks.getInstance().mixAoBrightness(j1, k1, l1, i2, this.renderMaxY * (1.0D - this.renderMinX), this.renderMaxY * this.renderMinX, (1.0D - this.renderMaxY) * this.renderMinX, (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			this.brightnessBottomLeft = RenderBlocks.getInstance().mixAoBrightness(j1, k1, l1, i2, this.renderMaxY * (1.0D - this.renderMaxX), this.renderMaxY * this.renderMaxX, (1.0D - this.renderMaxY) * this.renderMaxX, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			this.brightnessBottomRight = RenderBlocks.getInstance().mixAoBrightness(j1, k1, l1, i2, this.renderMinY * (1.0D - this.renderMaxX), this.renderMinY * this.renderMaxX, (1.0D - this.renderMinY) * this.renderMaxX, (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			this.brightnessTopRight = RenderBlocks.getInstance().mixAoBrightness(j1, k1, l1, i2, this.renderMinY * (1.0D - this.renderMinX), this.renderMinY * this.renderMinX, (1.0D - this.renderMinY) * this.renderMinX, (1.0D - this.renderMinY) * (1.0D - this.renderMinX));

			if (flag1)
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.8F;
			}
			else
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
			}

			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 2);
			CustomOreBlockRenderer.renderFaceZNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

			RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
			{
				this.colorRedTopLeft *= R;
				this.colorRedBottomLeft *= R;
				this.colorRedBottomRight *= R;
				this.colorRedTopRight *= R;
				this.colorGreenTopLeft *= G;
				this.colorGreenBottomLeft *= G;
				this.colorGreenBottomRight *= G;
				this.colorGreenTopRight *= G;
				this.colorBlueTopLeft *= B;
				this.colorBlueBottomLeft *= B;
				this.colorBlueBottomRight *= B;
				this.colorBlueTopRight *= B;
				CustomOreBlockRenderer.renderFaceZNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			}

			flag = true;
		}

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos, yPos, zPos + 1, 3))
		{
			if (this.renderMaxZ >= 1.0D)
			{
				++zPos;
			}

			this.aoLightValueScratchXZNP = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZPP = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZNP = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchYZPP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
			this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
			this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
			this.aoBrightnessYZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
			this.aoBrightnessYZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
			flag2 = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			}
			else
			{
				this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos - 1, zPos);
			}

			if (!flag3 && !flag4)
			{
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			}
			else
			{
				this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos + 1, zPos);
			}

			if (!flag2 && !flag5)
			{
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			}
			else
			{
				this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos - 1, zPos);
			}

			if (!flag2 && !flag4)
			{
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			}
			else
			{
				this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos + 1, zPos);
			}

			if (this.renderMaxZ >= 1.0D)
			{
				--zPos;
			}

			i1 = l;

			if (this.renderMaxZ >= 1.0D || !blockAccess.getBlock(xPos, yPos, zPos + 1).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
			}

			f7 = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
			f8 = (this.aoLightValueScratchXZNP + this.aoLightValueScratchXYZNPP + f7 + this.aoLightValueScratchYZPP) / 4.0F;
			f9 = (f7 + this.aoLightValueScratchYZPP + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			f10 = (this.aoLightValueScratchYZNP + f7 + this.aoLightValueScratchXYZPNP + this.aoLightValueScratchXZPP) / 4.0F;
			f11 = (this.aoLightValueScratchXYZNNP + this.aoLightValueScratchXZNP + this.aoLightValueScratchYZNP + f7) / 4.0F;
			f3 = (float)((double)f8 * this.renderMaxY * (1.0D - this.renderMinX) + (double)f9 * this.renderMaxY * this.renderMinX + (double)f10 * (1.0D - this.renderMaxY) * this.renderMinX + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinX));
			f4 = (float)((double)f8 * this.renderMinY * (1.0D - this.renderMinX) + (double)f9 * this.renderMinY * this.renderMinX + (double)f10 * (1.0D - this.renderMinY) * this.renderMinX + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMinX));
			f5 = (float)((double)f8 * this.renderMinY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMinY * this.renderMaxX + (double)f10 * (1.0D - this.renderMinY) * this.renderMaxX + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxX));
			f6 = (float)((double)f8 * this.renderMaxY * (1.0D - this.renderMaxX) + (double)f9 * this.renderMaxY * this.renderMaxX + (double)f10 * (1.0D - this.renderMaxY) * this.renderMaxX + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX));
			j1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYZNPP, this.aoBrightnessYZPP, i1);
			k1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZPP, this.aoBrightnessXZPP, this.aoBrightnessXYZPPP, i1);
			l1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessYZNP, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
			i2 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, this.aoBrightnessYZNP, i1);
			this.brightnessTopLeft = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, this.renderMaxY * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * (1.0D - this.renderMinX), (1.0D - this.renderMaxY) * this.renderMinX, this.renderMaxY * this.renderMinX);
			this.brightnessBottomLeft = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, this.renderMinY * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * (1.0D - this.renderMinX), (1.0D - this.renderMinY) * this.renderMinX, this.renderMinY * this.renderMinX);
			this.brightnessBottomRight = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, this.renderMinY * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * (1.0D - this.renderMaxX), (1.0D - this.renderMinY) * this.renderMaxX, this.renderMinY * this.renderMaxX);
			this.brightnessTopRight = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, this.renderMaxY * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxX), (1.0D - this.renderMaxY) * this.renderMaxX, this.renderMaxY * this.renderMaxX);

			if (flag1)
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.8F;
			}
			else
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.8F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.8F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.8F;
			}

			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 3);
			CustomOreBlockRenderer.renderFaceZPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

			RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
			{
				this.colorRedTopLeft *= R;
				this.colorRedBottomLeft *= R;
				this.colorRedBottomRight *= R;
				this.colorRedTopRight *= R;
				this.colorGreenTopLeft *= G;
				this.colorGreenBottomLeft *= G;
				this.colorGreenBottomRight *= G;
				this.colorGreenTopRight *= G;
				this.colorBlueTopLeft *= B;
				this.colorBlueBottomLeft *= B;
				this.colorBlueBottomRight *= B;
				this.colorBlueTopRight *= B;
				CustomOreBlockRenderer.renderFaceZPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			}

			flag = true;
		}

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos - 1, yPos, zPos, 4))
		{
			if (this.renderMinX <= 0.0D)
			{
				--xPos;
			}

			this.aoLightValueScratchXYNN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZNN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZNP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXYNP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
			this.aoBrightnessXYNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
			this.aoBrightnessXZNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
			this.aoBrightnessXZNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
			this.aoBrightnessXYNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
			flag2 = blockAccess.getBlock(xPos - 1, yPos + 1, zPos).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos - 1, yPos - 1, zPos).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos - 1, yPos, zPos - 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos - 1, yPos, zPos + 1).getCanBlockGrass();

			if (!flag4 && !flag3)
			{
				this.aoLightValueScratchXYZNNN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNNN = this.aoBrightnessXZNN;
			}
			else
			{
				this.aoLightValueScratchXYZNNN = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos - 1);
			}

			if (!flag5 && !flag3)
			{
				this.aoLightValueScratchXYZNNP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNNP = this.aoBrightnessXZNP;
			}
			else
			{
				this.aoLightValueScratchXYZNNP = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos + 1);
			}

			if (!flag4 && !flag2)
			{
				this.aoLightValueScratchXYZNPN = this.aoLightValueScratchXZNN;
				this.aoBrightnessXYZNPN = this.aoBrightnessXZNN;
			}
			else
			{
				this.aoLightValueScratchXYZNPN = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos - 1);
			}

			if (!flag5 && !flag2)
			{
				this.aoLightValueScratchXYZNPP = this.aoLightValueScratchXZNP;
				this.aoBrightnessXYZNPP = this.aoBrightnessXZNP;
			}
			else
			{
				this.aoLightValueScratchXYZNPP = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZNPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos + 1);
			}

			if (this.renderMinX <= 0.0D)
			{
				++xPos;
			}

			i1 = l;

			if (this.renderMinX <= 0.0D || !blockAccess.getBlock(xPos - 1, yPos, zPos).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos - 1, yPos, zPos);
			}

			f7 = blockAccess.getBlock(xPos - 1, yPos, zPos).getAmbientOcclusionLightValue();
			f8 = (this.aoLightValueScratchXYNN + this.aoLightValueScratchXYZNNP + f7 + this.aoLightValueScratchXZNP) / 4.0F;
			f9 = (f7 + this.aoLightValueScratchXZNP + this.aoLightValueScratchXYNP + this.aoLightValueScratchXYZNPP) / 4.0F;
			f10 = (this.aoLightValueScratchXZNN + f7 + this.aoLightValueScratchXYZNPN + this.aoLightValueScratchXYNP) / 4.0F;
			f11 = (this.aoLightValueScratchXYZNNN + this.aoLightValueScratchXYNN + this.aoLightValueScratchXZNN + f7) / 4.0F;
			f3 = (float)((double)f9 * this.renderMaxY * this.renderMaxZ + (double)f10 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)f8 * (1.0D - this.renderMaxY) * this.renderMaxZ);
			f4 = (float)((double)f9 * this.renderMaxY * this.renderMinZ + (double)f10 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)f11 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)f8 * (1.0D - this.renderMaxY) * this.renderMinZ);
			f5 = (float)((double)f9 * this.renderMinY * this.renderMinZ + (double)f10 * this.renderMinY * (1.0D - this.renderMinZ) + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)f8 * (1.0D - this.renderMinY) * this.renderMinZ);
			f6 = (float)((double)f9 * this.renderMinY * this.renderMaxZ + (double)f10 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)f11 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)f8 * (1.0D - this.renderMinY) * this.renderMaxZ);
			j1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYNN, this.aoBrightnessXYZNNP, this.aoBrightnessXZNP, i1);
			k1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNP, this.aoBrightnessXYNP, this.aoBrightnessXYZNPP, i1);
			l1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZNN, this.aoBrightnessXYZNPN, this.aoBrightnessXYNP, i1);
			i2 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZNNN, this.aoBrightnessXYNN, this.aoBrightnessXZNN, i1);
			this.brightnessTopLeft = RenderBlocks.getInstance().mixAoBrightness(k1, l1, i2, j1, this.renderMaxY * this.renderMaxZ, this.renderMaxY * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMaxY) * this.renderMaxZ);
			this.brightnessBottomLeft = RenderBlocks.getInstance().mixAoBrightness(k1, l1, i2, j1, this.renderMaxY * this.renderMinZ, this.renderMaxY * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), (1.0D - this.renderMaxY) * this.renderMinZ);
			this.brightnessBottomRight = RenderBlocks.getInstance().mixAoBrightness(k1, l1, i2, j1, this.renderMinY * this.renderMinZ, this.renderMinY * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), (1.0D - this.renderMinY) * this.renderMinZ);
			this.brightnessTopRight = RenderBlocks.getInstance().mixAoBrightness(k1, l1, i2, j1, this.renderMinY * this.renderMaxZ, this.renderMinY * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), (1.0D - this.renderMinY) * this.renderMaxZ);

			if (flag1)
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.6F;
			}
			else
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
			}

			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 4);
			CustomOreBlockRenderer.renderFaceXNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

			RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
			{
				this.colorRedTopLeft *= R;
				this.colorRedBottomLeft *= R;
				this.colorRedBottomRight *= R;
				this.colorRedTopRight *= R;
				this.colorGreenTopLeft *= G;
				this.colorGreenBottomLeft *= G;
				this.colorGreenBottomRight *= G;
				this.colorGreenTopRight *= G;
				this.colorBlueTopLeft *= B;
				this.colorBlueBottomLeft *= B;
				this.colorBlueBottomRight *= B;
				this.colorBlueTopRight *= B;
				CustomOreBlockRenderer.renderFaceXNeg(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			}

			flag = true;
		}

		if (RenderBlocks.getInstance().renderAllFaces || block.shouldSideBeRendered(blockAccess, xPos + 1, yPos, zPos, 5))
		{
			if (this.renderMaxX >= 1.0D)
			{
				++xPos;
			}

			this.aoLightValueScratchXYPN = blockAccess.getBlock(xPos, yPos - 1, zPos).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZPN = blockAccess.getBlock(xPos, yPos, zPos - 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXZPP = blockAccess.getBlock(xPos, yPos, zPos + 1).getAmbientOcclusionLightValue();
			this.aoLightValueScratchXYPP = blockAccess.getBlock(xPos, yPos + 1, zPos).getAmbientOcclusionLightValue();
			this.aoBrightnessXYPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos);
			this.aoBrightnessXZPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos - 1);
			this.aoBrightnessXZPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos, zPos + 1);
			this.aoBrightnessXYPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos);
			flag2 = blockAccess.getBlock(xPos + 1, yPos + 1, zPos).getCanBlockGrass();
			flag3 = blockAccess.getBlock(xPos + 1, yPos - 1, zPos).getCanBlockGrass();
			flag4 = blockAccess.getBlock(xPos + 1, yPos, zPos + 1).getCanBlockGrass();
			flag5 = blockAccess.getBlock(xPos + 1, yPos, zPos - 1).getCanBlockGrass();

			if (!flag3 && !flag5)
			{
				this.aoLightValueScratchXYZPNN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPNN = this.aoBrightnessXZPN;
			}
			else
			{
				this.aoLightValueScratchXYZPNN = blockAccess.getBlock(xPos, yPos - 1, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos - 1);
			}

			if (!flag3 && !flag4)
			{
				this.aoLightValueScratchXYZPNP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPNP = this.aoBrightnessXZPP;
			}
			else
			{
				this.aoLightValueScratchXYZPNP = blockAccess.getBlock(xPos, yPos - 1, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPNP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos - 1, zPos + 1);
			}

			if (!flag2 && !flag5)
			{
				this.aoLightValueScratchXYZPPN = this.aoLightValueScratchXZPN;
				this.aoBrightnessXYZPPN = this.aoBrightnessXZPN;
			}
			else
			{
				this.aoLightValueScratchXYZPPN = blockAccess.getBlock(xPos, yPos + 1, zPos - 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPN = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos - 1);
			}

			if (!flag2 && !flag4)
			{
				this.aoLightValueScratchXYZPPP = this.aoLightValueScratchXZPP;
				this.aoBrightnessXYZPPP = this.aoBrightnessXZPP;
			}
			else
			{
				this.aoLightValueScratchXYZPPP = blockAccess.getBlock(xPos, yPos + 1, zPos + 1).getAmbientOcclusionLightValue();
				this.aoBrightnessXYZPPP = block.getMixedBrightnessForBlock(blockAccess, xPos, yPos + 1, zPos + 1);
			}

			if (this.renderMaxX >= 1.0D)
			{
				--xPos;
			}

			i1 = l;

			if (this.renderMaxX >= 1.0D || !blockAccess.getBlock(xPos + 1, yPos, zPos).isOpaqueCube())
			{
				i1 = block.getMixedBrightnessForBlock(blockAccess, xPos + 1, yPos, zPos);
			}

			f7 = blockAccess.getBlock(xPos + 1, yPos, zPos).getAmbientOcclusionLightValue();
			f8 = (this.aoLightValueScratchXYPN + this.aoLightValueScratchXYZPNP + f7 + this.aoLightValueScratchXZPP) / 4.0F;
			f9 = (this.aoLightValueScratchXYZPNN + this.aoLightValueScratchXYPN + this.aoLightValueScratchXZPN + f7) / 4.0F;
			f10 = (this.aoLightValueScratchXZPN + f7 + this.aoLightValueScratchXYZPPN + this.aoLightValueScratchXYPP) / 4.0F;
			f11 = (f7 + this.aoLightValueScratchXZPP + this.aoLightValueScratchXYPP + this.aoLightValueScratchXYZPPP) / 4.0F;
			f3 = (float)((double)f8 * (1.0D - this.renderMinY) * this.renderMaxZ + (double)f9 * (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ) + (double)f10 * this.renderMinY * (1.0D - this.renderMaxZ) + (double)f11 * this.renderMinY * this.renderMaxZ);
			f4 = (float)((double)f8 * (1.0D - this.renderMinY) * this.renderMinZ + (double)f9 * (1.0D - this.renderMinY) * (1.0D - this.renderMinZ) + (double)f10 * this.renderMinY * (1.0D - this.renderMinZ) + (double)f11 * this.renderMinY * this.renderMinZ);
			f5 = (float)((double)f8 * (1.0D - this.renderMaxY) * this.renderMinZ + (double)f9 * (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ) + (double)f10 * this.renderMaxY * (1.0D - this.renderMinZ) + (double)f11 * this.renderMaxY * this.renderMinZ);
			f6 = (float)((double)f8 * (1.0D - this.renderMaxY) * this.renderMaxZ + (double)f9 * (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ) + (double)f10 * this.renderMaxY * (1.0D - this.renderMaxZ) + (double)f11 * this.renderMaxY * this.renderMaxZ);
			j1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYPN, this.aoBrightnessXYZPNP, this.aoBrightnessXZPP, i1);
			k1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZPP, this.aoBrightnessXYPP, this.aoBrightnessXYZPPP, i1);
			l1 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXZPN, this.aoBrightnessXYZPPN, this.aoBrightnessXYPP, i1);
			i2 = RenderBlocks.getInstance().getAoBrightness(this.aoBrightnessXYZPNN, this.aoBrightnessXYPN, this.aoBrightnessXZPN, i1);
			this.brightnessTopLeft = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, (1.0D - this.renderMinY) * this.renderMaxZ, (1.0D - this.renderMinY) * (1.0D - this.renderMaxZ), this.renderMinY * (1.0D - this.renderMaxZ), this.renderMinY * this.renderMaxZ);
			this.brightnessBottomLeft = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, (1.0D - this.renderMinY) * this.renderMinZ, (1.0D - this.renderMinY) * (1.0D - this.renderMinZ), this.renderMinY * (1.0D - this.renderMinZ), this.renderMinY * this.renderMinZ);
			this.brightnessBottomRight = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, (1.0D - this.renderMaxY) * this.renderMinZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMinZ), this.renderMaxY * (1.0D - this.renderMinZ), this.renderMaxY * this.renderMinZ);
			this.brightnessTopRight = RenderBlocks.getInstance().mixAoBrightness(j1, i2, l1, k1, (1.0D - this.renderMaxY) * this.renderMaxZ, (1.0D - this.renderMaxY) * (1.0D - this.renderMaxZ), this.renderMaxY * (1.0D - this.renderMaxZ), this.renderMaxY * this.renderMaxZ);

			if (flag1)
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = R * 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = G * 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = B * 0.6F;
			}
			else
			{
				this.colorRedTopLeft = this.colorRedBottomLeft = this.colorRedBottomRight = this.colorRedTopRight = 0.6F;
				this.colorGreenTopLeft = this.colorGreenBottomLeft = this.colorGreenBottomRight = this.colorGreenTopRight = 0.6F;
				this.colorBlueTopLeft = this.colorBlueBottomLeft = this.colorBlueBottomRight = this.colorBlueTopRight = 0.6F;
			}

			this.colorRedTopLeft *= f3;
			this.colorGreenTopLeft *= f3;
			this.colorBlueTopLeft *= f3;
			this.colorRedBottomLeft *= f4;
			this.colorGreenBottomLeft *= f4;
			this.colorBlueBottomLeft *= f4;
			this.colorRedBottomRight *= f5;
			this.colorGreenBottomRight *= f5;
			this.colorBlueBottomRight *= f5;
			this.colorRedTopRight *= f6;
			this.colorGreenTopRight *= f6;
			this.colorBlueTopRight *= f6;
			iicon = this.getBlockIcon(block, blockAccess, xPos, yPos, zPos, 5);
			CustomOreBlockRenderer.renderFaceXPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);

			RenderBlocks.getInstance();
			if (RenderBlocks.fancyGrass && iicon.getIconName().equals("grass_side") && !this.hasOverrideBlockTexture())
			{
				this.colorRedTopLeft *= R;
				this.colorRedBottomLeft *= R;
				this.colorRedBottomRight *= R;
				this.colorRedTopRight *= R;
				this.colorGreenTopLeft *= G;
				this.colorGreenBottomLeft *= G;
				this.colorGreenBottomRight *= G;
				this.colorGreenTopRight *= G;
				this.colorBlueTopLeft *= B;
				this.colorBlueBottomLeft *= B;
				this.colorBlueBottomRight *= B;
				this.colorBlueTopRight *= B;
				CustomOreBlockRenderer.renderFaceXPos(aWorld, aRenderer, block, xPos, yPos, zPos, aTextures);
			}

			flag = true;
		}

		this.enableAO = false;
		return flag;
	}

}
