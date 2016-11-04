package gtPlusPlus.xmod.gregtech.api.objects;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.*;
import gtPlusPlus.core.util.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;

public class GregtechRenderedTexture implements ITexture, IColorModulationContainer {
	private final IIconContainer	mIconContainer;
	private final boolean			mAllowAlpha;
	/**
	 * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
	 * <p/>
	 * Just set this variable to another different Array instead. Otherwise some
	 * colored things will get Problems.
	 */
	public short[]					mRGBa;

	public GregtechRenderedTexture(final IIconContainer aIcon) {
		this(aIcon, Dyes._NULL.mRGBa);
	}

	public GregtechRenderedTexture(final IIconContainer aIcon, final short[] aRGBa) {
		this(aIcon, aRGBa, true);
	}

	public GregtechRenderedTexture(final IIconContainer aIcon, final short[] aRGBa, final boolean aAllowAlpha) {
		if (aRGBa.length != 4) {
			throw new IllegalArgumentException("RGBa doesn't have 4 Values @ GT_RenderedTexture");
		}
		this.mIconContainer = aIcon;
		this.mAllowAlpha = aAllowAlpha;
		this.mRGBa = aRGBa;
	}

	@Override
	public short[] getRGBA() {
		return this.mRGBa;
	}

	@Override
	public boolean isValidTexture() {
		return this.mIconContainer != null;
	}

	@Override
	public void renderXNeg(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 0.6F), (int) (this.mRGBa[1] * 0.6F),
				(int) (this.mRGBa[2] * 0.6F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
		aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, this.mIconContainer.getIcon());
		if (this.mIconContainer.getOverlayIcon() != null) {
			Tessellator.instance.setColorRGBA(153, 153, 153, 255);
			aRenderer.renderFaceXNeg(aBlock, aX, aY, aZ, this.mIconContainer.getOverlayIcon());
		}
	}

	@Override
	public void renderXPos(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 0.6F), (int) (this.mRGBa[1] * 0.6F),
				(int) (this.mRGBa[2] * 0.6F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
		aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, this.mIconContainer.getIcon());
		if (this.mIconContainer.getOverlayIcon() != null) {
			Tessellator.instance.setColorRGBA(153, 153, 153, 255);
			aRenderer.renderFaceXPos(aBlock, aX, aY, aZ, this.mIconContainer.getOverlayIcon());
		}
	}

	@Override
	public void renderYNeg(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 0.5F), (int) (this.mRGBa[1] * 0.5F),
				(int) (this.mRGBa[2] * 0.5F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
		IIcon aIcon = this.mIconContainer.getIcon();

		double d3 = aIcon.getInterpolatedU(aRenderer.renderMaxX * 16.0D);
		double d4 = aIcon.getInterpolatedU(aRenderer.renderMinX * 16.0D);
		double d5 = aIcon.getInterpolatedV(aRenderer.renderMinZ * 16.0D);
		double d6 = aIcon.getInterpolatedV(aRenderer.renderMaxZ * 16.0D);

		if (aRenderer.renderMinX < 0.0D || aRenderer.renderMaxX > 1.0D) {
			d3 = aIcon.getMaxU();
			d4 = aIcon.getMinU();
		}

		if (aRenderer.renderMinZ < 0.0D || aRenderer.renderMaxZ > 1.0D) {
			d5 = aIcon.getMinV();
			d6 = aIcon.getMaxV();
		}

		final double d11 = aX + aRenderer.renderMinX;
		final double d12 = aX + aRenderer.renderMaxX;
		final double d13 = aY + aRenderer.renderMinY;
		final double d14 = aZ + aRenderer.renderMinZ;
		final double d15 = aZ + aRenderer.renderMaxZ;

		Tessellator.instance.addVertexWithUV(d11, d13, d15, d3, d6);
		Tessellator.instance.addVertexWithUV(d11, d13, d14, d3, d5);
		Tessellator.instance.addVertexWithUV(d12, d13, d14, d4, d5);
		Tessellator.instance.addVertexWithUV(d12, d13, d15, d4, d6);

		if ((aIcon = this.mIconContainer.getOverlayIcon()) != null) {
			Tessellator.instance.setColorRGBA(128, 128, 128, 255);

			Tessellator.instance.addVertexWithUV(d11, d13, d15, d3, d6);
			Tessellator.instance.addVertexWithUV(d11, d13, d14, d3, d5);
			Tessellator.instance.addVertexWithUV(d12, d13, d14, d4, d5);
			Tessellator.instance.addVertexWithUV(d12, d13, d15, d4, d6);
		}
	}

	@Override
	public void renderYPos(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 1.0F), (int) (this.mRGBa[1] * 1.0F),
				(int) (this.mRGBa[2] * 1.0F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
		aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, this.mIconContainer.getIcon());
		if (this.mIconContainer.getOverlayIcon() != null) {
			Tessellator.instance.setColorRGBA(255, 255, 255, 255);
			aRenderer.renderFaceYPos(aBlock, aX, aY, aZ, this.mIconContainer.getOverlayIcon());
		}
	}

	@Override
	public void renderZNeg(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		try {
			Utils.LOG_WARNING("renderZNeg Method();.");
			Utils.LOG_WARNING("Setting RGBA of instance.");
			Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 0.8F), (int) (this.mRGBa[1] * 0.8F),
					(int) (this.mRGBa[2] * 0.8F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
			aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, this.mIconContainer.getIcon());
			Utils.LOG_WARNING("Is mIconContainer null? " + this.mIconContainer.toString());
			Utils.LOG_WARNING("mIconContainer.getIcon.getIconName(): " + this.mIconContainer.getIcon().getIconName());
			Utils.LOG_WARNING("mIconContainer.getTextureFile().getResourceDomain()"
					+ this.mIconContainer.getTextureFile().getResourceDomain());
			Utils.LOG_WARNING("mIconContainer.getTextureFile().getResourcePath()"
					+ this.mIconContainer.getTextureFile().getResourcePath());
			if (this.mIconContainer.getOverlayIcon() != null) {
				Tessellator.instance.setColorRGBA(204, 204, 204, 255);
				aRenderer.renderFaceZNeg(aBlock, aX, aY, aZ, this.mIconContainer.getOverlayIcon());
				Utils.LOG_WARNING(
						"Is miconContainer.getOverlayIcon null? " + this.mIconContainer.getOverlayIcon().toString());
				Utils.LOG_WARNING(
						"mIconContainer.getIcon.getIconName(): " + this.mIconContainer.getOverlayIcon().getIconName());
				Utils.LOG_WARNING("mIconContainer.getTextureFile().getResourceDomain()"
						+ this.mIconContainer.getTextureFile().getResourceDomain());
				Utils.LOG_WARNING("mIconContainer.getTextureFile().getResourcePath()"
						+ this.mIconContainer.getTextureFile().getResourcePath());
			}
		}
		catch (final NullPointerException e) {

			e.printStackTrace();
			Utils.LOG_WARNING(
					"renderZNeg failed." + e.getMessage() + "|" + e.getClass() + "|" + e.getLocalizedMessage());
			Minecraft.getMinecraft().shutdown();
		}
	}

	@Override
	public void renderZPos(final RenderBlocks aRenderer, final Block aBlock, final int aX, final int aY, final int aZ) {
		try {
			Tessellator.instance.setColorRGBA((int) (this.mRGBa[0] * 0.8F), (int) (this.mRGBa[1] * 0.8F),
					(int) (this.mRGBa[2] * 0.8F), this.mAllowAlpha ? 255 - this.mRGBa[3] : 255);
			aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, this.mIconContainer.getIcon());
			if (this.mIconContainer.getOverlayIcon() != null) {
				Tessellator.instance.setColorRGBA(204, 204, 204, 255);
				aRenderer.renderFaceZPos(aBlock, aX, aY, aZ, this.mIconContainer.getOverlayIcon());
			}
		}
		catch (final NullPointerException e) {
			Utils.LOG_WARNING("renderZPos failed.");
		}
	}
}