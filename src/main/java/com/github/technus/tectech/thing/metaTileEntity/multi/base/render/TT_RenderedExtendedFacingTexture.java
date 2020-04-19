package com.github.technus.tectech.thing.metaTileEntity.multi.base.render;

import com.github.technus.tectech.mechanics.alignment.IAlignment;
import com.github.technus.tectech.mechanics.alignment.enumerable.ExtendedFacing;
import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class TT_RenderedExtendedFacingTexture implements ITexture,IColorModulationContainer {
    private final IIconContainer mIconContainer;
    private final boolean mAllowAlpha;
    /**
     * DO NOT MANIPULATE THE VALUES INSIDE THIS ARRAY!!!
     * <p/>
     * Just set this variable to another different Array instead.
     * Otherwise some colored things will get Problems.
     */
    public short[] mRGBa;

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa, boolean aAllowAlpha) {
        if (aRGBa.length != 4) throw new IllegalArgumentException("RGBa doesn't have 4 Values @ TT_RenderedTexture");
        mIconContainer = aIcon;
        mAllowAlpha = aAllowAlpha;
        mRGBa = aRGBa;
    }

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon, short[] aRGBa) {
        this(aIcon, aRGBa, true);
    }

    public TT_RenderedExtendedFacingTexture(IIconContainer aIcon) {
        this(aIcon, Dyes._NULL.mRGBa);
    }

    @Override
    public void renderXPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 0.6F), (int) (mRGBa[1] * 0.6F), (int) (mRGBa[2] * 0.6F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(153, 153, 153, 255);
            renderFaceXPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }

    @Override
    public void renderXNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 0.6F), (int) (mRGBa[1] * 0.6F), (int) (mRGBa[2] * 0.6F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(153, 153, 153, 255);
            renderFaceXNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }

    @Override
    public void renderYPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 1.0F), (int) (mRGBa[1] * 1.0F), (int) (mRGBa[2] * 1.0F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(255, 255, 255, 255);
            renderFaceYPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }

    @Override
    public void renderYNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 0.5F), (int) (mRGBa[1] * 0.5F), (int) (mRGBa[2] * 0.5F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(255, 255, 255, 255);
            renderFaceYNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }

    @Override
    public void renderZPos(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 0.8F), (int) (mRGBa[1] * 0.8F), (int) (mRGBa[2] * 0.8F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(204, 204, 204, 255);
            renderFaceZPos(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }

    @Override
    public void renderZNeg(RenderBlocks aRenderer, Block aBlock, int aX, int aY, int aZ) {
        Tessellator.instance.setColorRGBA((int) (mRGBa[0] * 0.8F), (int) (mRGBa[1] * 0.8F), (int) (mRGBa[2] * 0.8F), mAllowAlpha ? 255 - mRGBa[3] : 255);
        ExtendedFacing rotation = getExtendedFacing(aX, aY, aZ);
        renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getIcon(), rotation);
        if (mIconContainer.getOverlayIcon() != null) {
            Tessellator.instance.setColorRGBA(204, 204, 204, 255);
            renderFaceZNeg(aRenderer, aX, aY, aZ, mIconContainer.getOverlayIcon(), rotation);
        }
    }


    /**
     * Renders the given texture to the bottom face of the block. Args: block, x, y, z, texture
     */
    public void renderFaceYNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMaxX * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMinX * 16.0D);
        double d5 = icon.getInterpolatedV(aRenderer.renderMinZ * 16.0D);
        double d6 = icon.getInterpolatedV(aRenderer.renderMaxZ * 16.0D);

        if (aRenderer.renderMinX < 0.0D || aRenderer.renderMaxX > 1.0D) {
            d3 = icon.getMaxU();
            d4 = icon.getMinU();
        }

        if (aRenderer.renderMinZ < 0.0D || aRenderer.renderMaxZ > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 3:
                d3 = icon.getInterpolatedU(aRenderer.renderMaxZ * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxX * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMinZ * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinX * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 1:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinZ * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxZ * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMaxX * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMinX * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMinZ * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxZ * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMinX;
        double d12 = x + aRenderer.renderMaxX;
        double d13 = y + aRenderer.renderMinY;
        double d14 = z + aRenderer.renderMinZ;
        double d15 = z + aRenderer.renderMaxZ;

        if (aRenderer.renderFromInside) {
            d11 = x + aRenderer.renderMaxX;
            d12 = x + aRenderer.renderMinX;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        } else {
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
        }
    }

    /**
     * Renders the given texture to the top face of the block. Args: block, x, y, z, texture
     */
    public void renderFaceYPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMinX * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMaxX * 16.0D);
        double d5 = icon.getInterpolatedV(aRenderer.renderMinZ * 16.0D);
        double d6 = icon.getInterpolatedV(aRenderer.renderMaxZ * 16.0D);

        if (aRenderer.renderMinX < 0.0D || aRenderer.renderMaxX > 1.0D) {
            d3 = icon.getMinU();
            d4 = icon.getMaxU();
        }

        if (aRenderer.renderMinZ < 0.0D || aRenderer.renderMaxZ > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        double d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 1:
                d3 = icon.getInterpolatedU(aRenderer.renderMinZ * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxX * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMaxZ * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinX * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 3:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxZ * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMinZ * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMaxX * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxX * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMinZ * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxZ * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMinX;
        double d12 = x + aRenderer.renderMaxX;
        double d13 = y + aRenderer.renderMaxY;
        double d14 = z + aRenderer.renderMinZ;
        double d15 = z + aRenderer.renderMaxZ;

        if (aRenderer.renderFromInside) {
            d11 = x + aRenderer.renderMaxX;
            d12 = x + aRenderer.renderMinX;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        } else {
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
        }
    }

    /**
     * Renders the given texture to the north (z-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceZNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMinX * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMaxX * 16.0D);

        if (aRenderer.field_152631_f) {
            d4 = icon.getInterpolatedU((1.0D - aRenderer.renderMinX) * 16.0D);
            d3 = icon.getInterpolatedU((1.0D - aRenderer.renderMaxX) * 16.0D);
        }

        double d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxY * 16.0D);
        double d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinY * 16.0D);

        double d7;

        if (aRenderer.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (aRenderer.renderMinX < 0.0D || aRenderer.renderMaxX > 1.0D) {
            d3 = icon.getMinU();
            d4 = icon.getMaxU();
        }

        if (aRenderer.renderMinY < 0.0D || aRenderer.renderMaxY > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 3:
                d3 = icon.getInterpolatedU(aRenderer.renderMaxY * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMinY * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMinX * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxX * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 1:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinY * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxY * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxX * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinX * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxX * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinY * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMinX;
        double d12 = x + aRenderer.renderMaxX;
        double d13 = y + aRenderer.renderMinY;
        double d14 = y + aRenderer.renderMaxY;
        double d15 = z + aRenderer.renderMinZ;

        if (aRenderer.renderFromInside) {
            d11 = x + aRenderer.renderMaxX;
            d12 = x + aRenderer.renderMinX;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
        } else {
            tessellator.addVertexWithUV(d11, d14, d15, d7, d9);
            tessellator.addVertexWithUV(d12, d14, d15, d3, d5);
            tessellator.addVertexWithUV(d12, d13, d15, d8, d10);
            tessellator.addVertexWithUV(d11, d13, d15, d4, d6);
        }
    }

    /**
     * Renders the given texture to the south (z-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceZPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMinX * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMaxX * 16.0D);
        double d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxY * 16.0D);
        double d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinY * 16.0D);
        double d7;

        if (aRenderer.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (aRenderer.renderMinX < 0.0D || aRenderer.renderMaxX > 1.0D) {
            d3 = icon.getMinU();
            d4 = icon.getMaxU();
        }

        if (aRenderer.renderMinY < 0.0D || aRenderer.renderMaxY > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 1:
                d3 = icon.getInterpolatedU(aRenderer.renderMinY * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMaxY * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxX * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 3:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxY * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMinY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMaxX * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinX * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxX * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinY * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMinX;
        double d12 = x + aRenderer.renderMaxX;
        double d13 = y + aRenderer.renderMinY;
        double d14 = y + aRenderer.renderMaxY;
        double d15 = z + aRenderer.renderMaxZ;

        if (aRenderer.renderFromInside) {
            d11 = x + aRenderer.renderMaxX;
            d12 = x + aRenderer.renderMinX;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
        } else {
            tessellator.addVertexWithUV(d11, d14, d15, d3, d5);
            tessellator.addVertexWithUV(d11, d13, d15, d8, d10);
            tessellator.addVertexWithUV(d12, d13, d15, d4, d6);
            tessellator.addVertexWithUV(d12, d14, d15, d7, d9);
        }
    }

    /**
     * Renders the given texture to the west (x-negative) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceXNeg(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMinZ * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMaxZ * 16.0D);
        double d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxY * 16.0D);
        double d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinY * 16.0D);
        double d7;

        if (aRenderer.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (aRenderer.renderMinZ < 0.0D || aRenderer.renderMaxZ > 1.0D) {
            d3 = icon.getMinU();
            d4 = icon.getMaxU();
        }

        if (aRenderer.renderMinY < 0.0D || aRenderer.renderMaxY > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 1:
                d3 = icon.getInterpolatedU(aRenderer.renderMinY * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxZ * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinZ * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 3:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxY * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMinZ * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMinY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMaxZ * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinZ * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxZ * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinY * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMinX;
        double d12 = y + aRenderer.renderMinY;
        double d13 = y + aRenderer.renderMaxY;
        double d14 = z + aRenderer.renderMinZ;
        double d15 = z + aRenderer.renderMaxZ;

        if (aRenderer.renderFromInside) {
            d14 = z + aRenderer.renderMaxZ;
            d15 = z + aRenderer.renderMinZ;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
        } else {
            tessellator.addVertexWithUV(d11, d13, d15, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d14, d3, d5);
            tessellator.addVertexWithUV(d11, d12, d14, d8, d10);
            tessellator.addVertexWithUV(d11, d12, d15, d4, d6);
        }
    }

    /**
     * Renders the given texture to the east (x-positive) face of the block.  Args: block, x, y, z, texture
     */
    public void renderFaceXPos(RenderBlocks aRenderer, double x, double y, double z, IIcon icon, ExtendedFacing rotation) {
        Tessellator tessellator = Tessellator.instance;

        if (aRenderer.hasOverrideBlockTexture()) {
            icon = aRenderer.overrideBlockTexture;
        }

        double d3 = icon.getInterpolatedU(aRenderer.renderMinZ * 16.0D);
        double d4 = icon.getInterpolatedU(aRenderer.renderMaxZ * 16.0D);

        if (aRenderer.field_152631_f) {
            d4 = icon.getInterpolatedU((1.0D - aRenderer.renderMinZ) * 16.0D);
            d3 = icon.getInterpolatedU((1.0D - aRenderer.renderMaxZ) * 16.0D);
        }

        double d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxY * 16.0D);
        double d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMinY * 16.0D);
        double d7;

        if (aRenderer.flipTexture) {
            d7 = d3;
            d3 = d4;
            d4 = d7;
        }

        if (aRenderer.renderMinZ < 0.0D || aRenderer.renderMaxZ > 1.0D) {
            d3 = icon.getMinU();
            d4 = icon.getMaxU();
        }

        if (aRenderer.renderMinY < 0.0D || aRenderer.renderMaxY > 1.0D) {
            d5 = icon.getMinV();
            d6 = icon.getMaxV();
        }

        {
            double temp;
            switch (rotation.getFlip().ordinal()){
                case 1:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
                case 2:
                    temp=d5;
                    d5=d6;
                    d6=temp;
                case 3:
                    temp=d3;
                    d3=d4;
                    d4=temp;
                    break;
            }
        }

        d7 = d4;
        double d8 = d3;
        double d9 = d5;
        double d10 = d6;

        switch (rotation.getRotation().ordinal()) {
            case 3:
                d3 = icon.getInterpolatedU(aRenderer.renderMaxY * 16.0D);
                d5 = icon.getInterpolatedV(16.0D - aRenderer.renderMinZ * 16.0D);
                d4 = icon.getInterpolatedU(aRenderer.renderMinY * 16.0D);
                d6 = icon.getInterpolatedV(16.0D - aRenderer.renderMaxZ * 16.0D);
                d9 = d5;
                d10 = d6;
                d7 = d3;
                d8 = d4;
                d5 = d6;
                d6 = d9;
                break;
            case 1:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinY * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxZ * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinZ * 16.0D);
                d7 = d4;
                d8 = d3;
                d3 = d4;
                d4 = d8;
                d9 = d6;
                d10 = d5;
                break;
            case 2:
                d3 = icon.getInterpolatedU(16.0D - aRenderer.renderMinZ * 16.0D);
                d4 = icon.getInterpolatedU(16.0D - aRenderer.renderMaxZ * 16.0D);
                d5 = icon.getInterpolatedV(aRenderer.renderMaxY * 16.0D);
                d6 = icon.getInterpolatedV(aRenderer.renderMinY * 16.0D);
                d7 = d4;
                d8 = d3;
                d9 = d5;
                d10 = d6;
                break;
        }

        double d11 = x + aRenderer.renderMaxX;
        double d12 = y + aRenderer.renderMinY;
        double d13 = y + aRenderer.renderMaxY;
        double d14 = z + aRenderer.renderMinZ;
        double d15 = z + aRenderer.renderMaxZ;

        if (aRenderer.renderFromInside) {
            d14 = z + aRenderer.renderMaxZ;
            d15 = z + aRenderer.renderMinZ;
        }

        if (aRenderer.enableAO) {
            tessellator.setColorOpaque_F(aRenderer.colorRedTopLeft, aRenderer.colorGreenTopLeft, aRenderer.colorBlueTopLeft);
            tessellator.setBrightness(aRenderer.brightnessTopLeft);
            tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomLeft, aRenderer.colorGreenBottomLeft, aRenderer.colorBlueBottomLeft);
            tessellator.setBrightness(aRenderer.brightnessBottomLeft);
            tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
            tessellator.setColorOpaque_F(aRenderer.colorRedBottomRight, aRenderer.colorGreenBottomRight, aRenderer.colorBlueBottomRight);
            tessellator.setBrightness(aRenderer.brightnessBottomRight);
            tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
            tessellator.setColorOpaque_F(aRenderer.colorRedTopRight, aRenderer.colorGreenTopRight, aRenderer.colorBlueTopRight);
            tessellator.setBrightness(aRenderer.brightnessTopRight);
            tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
        } else {
            tessellator.addVertexWithUV(d11, d12, d15, d8, d10);
            tessellator.addVertexWithUV(d11, d12, d14, d4, d6);
            tessellator.addVertexWithUV(d11, d13, d14, d7, d9);
            tessellator.addVertexWithUV(d11, d13, d15, d3, d5);
        }
    }

    @Override
    public short[] getRGBA() {
        return mRGBa;
    }

    @Override
    public boolean isValidTexture() {
        return mIconContainer != null;
    }

    private static ExtendedFacing getExtendedFacing(int x, int y, int z) {
        World w = Minecraft.getMinecraft().theWorld;
        if (w != null) {
            TileEntity te = w.getTileEntity(x, y, z);
            if (te instanceof IGregTechTileEntity) {
                IMetaTileEntity meta = ((IGregTechTileEntity) te).getMetaTileEntity();
                if (meta instanceof IAlignment) {
                    return ((IAlignment) meta).getExtendedFacing();
                }else{
                    return ExtendedFacing.of(ForgeDirection.getOrientation(meta.getBaseMetaTileEntity().getFrontFacing()));
                }
            }
        }
        return ExtendedFacing.DEFAULT;
    }
}
