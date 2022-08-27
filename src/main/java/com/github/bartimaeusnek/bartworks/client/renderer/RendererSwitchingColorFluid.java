/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.client.renderer;

import com.github.bartimaeusnek.bartworks.common.blocks.BioFluidBlock;
import com.github.bartimaeusnek.bartworks.common.loaders.FluidLoader;
import com.github.bartimaeusnek.bartworks.common.tileentities.multis.GT_TileEntity_BioVat;
import com.github.bartimaeusnek.bartworks.util.Coords;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.IFluidBlock;

@SideOnly(Side.CLIENT)
public class RendererSwitchingColorFluid implements ISimpleBlockRenderingHandler {
    private static final float LIGHT_Y_NEG = 0.5f;
    private static final float LIGHT_Y_POS = 1f;
    private static final float LIGHT_XZ_NEG = 0.8f;
    private static final float LIGHT_XZ_POS = 0.6f;
    private static final float THREE_QUARTERS_FILLED = 0.875f;
    private static final double RENDER_OFFSET = 0.0010000000474974513;

    public static RendererSwitchingColorFluid instance = new RendererSwitchingColorFluid();

    private float getFluidHeightAverage(float[] flow) {
        float total = 0.0f;
        int count = 0;
        float end = 0.0f;
        for (float aFlow : flow) {
            if (aFlow >= RendererSwitchingColorFluid.THREE_QUARTERS_FILLED
                    && end != RendererSwitchingColorFluid.LIGHT_Y_POS) {
                end = aFlow;
            }
            if (aFlow >= 0.0f) {
                total += aFlow;
                ++count;
            }
        }
        if (end == 0.0f && count != 0) {
            end = total / count;
        }
        return end;
    }

    private float getFluidHeightForRender(IBlockAccess world, int x, int y, int z, BlockFluidBase block) {

        if (world.getBlock(x, y, z) == block) {
            Block vOrigin = world.getBlock(x, y + 1, z);
            if (vOrigin.getMaterial().isLiquid() || vOrigin instanceof IFluidBlock) {
                return RendererSwitchingColorFluid.LIGHT_Y_POS;
            }
            if (world.getBlockMetadata(x, y, z) == block.getMaxRenderHeightMeta()) {
                return RendererSwitchingColorFluid.THREE_QUARTERS_FILLED;
            }
        }
        return (!world.getBlock(x, y, z).getMaterial().isSolid() && world.getBlock(x, y + 1, z) == block)
                ? RendererSwitchingColorFluid.LIGHT_Y_POS
                : (block.getQuantaPercentage(world, x, y, z) * RendererSwitchingColorFluid.THREE_QUARTERS_FILLED);
    }

    public void renderInventoryBlock(Block block, int metadata, int modelID, RenderBlocks renderer) {}

    @SideOnly(Side.CLIENT)
    public boolean renderWorldBlock(
            IBlockAccess iBlockAccess, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) {
        if ((!(block instanceof BioFluidBlock))) return false;
        Tessellator tessellator = Tessellator.instance;
        Coords blockat = new Coords(x, y, z, iBlockAccess.getTileEntity(x, y, z).getWorldObj().provider.dimensionId);
        Integer rgb = GT_TileEntity_BioVat.staticColorMap.get(blockat);

        int r, g, b;

        if (rgb != null) {
            r = (rgb >> 16) & 0xFF;
            g = (rgb >> 8) & 0xFF;
            b = rgb & 0xFF;
        } else {
            r = 0;
            g = 0;
            b = 255;
        }
        float red = ((float) r) / 255f, green = ((float) g) / 255f, blue = ((float) b) / 255f;

        BlockFluidBase blockFluidBase = (BlockFluidBase) block;
        boolean renderTop = iBlockAccess.getBlock(x, y + 1, z) != blockFluidBase;
        boolean renderBottom = block.shouldSideBeRendered(iBlockAccess, x, y - 1, z, 0)
                && iBlockAccess.getBlock(x, y - 1, z) != blockFluidBase;
        boolean[] renderSides = {
            block.shouldSideBeRendered(iBlockAccess, x, y, z - 1, 2),
            block.shouldSideBeRendered(iBlockAccess, x, y, z + 1, 3),
            block.shouldSideBeRendered(iBlockAccess, x - 1, y, z, 4),
            block.shouldSideBeRendered(iBlockAccess, x + 1, y, z, 5)
        };
        if (!renderTop && !renderBottom && !renderSides[0] && !renderSides[1] && !renderSides[2] && !renderSides[3]) {
            return false;
        }
        boolean rendered = false;
        float fluidHeightForRender = this.getFluidHeightForRender(iBlockAccess, x, y, z, blockFluidBase);
        double heightNW, heightSW, heightSE, heightNE;

        if (fluidHeightForRender != RendererSwitchingColorFluid.LIGHT_Y_POS) {
            float fluidHeightForRender1 = this.getFluidHeightForRender(iBlockAccess, x - 1, y, z - 1, blockFluidBase);
            float fluidHeightForRender2 = this.getFluidHeightForRender(iBlockAccess, x - 1, y, z, blockFluidBase);
            float fluidHeightForRender3 = this.getFluidHeightForRender(iBlockAccess, x - 1, y, z + 1, blockFluidBase);
            float fluidHeightForRender4 = this.getFluidHeightForRender(iBlockAccess, x, y, z - 1, blockFluidBase);
            float fluidHeightForRender5 = this.getFluidHeightForRender(iBlockAccess, x, y, z + 1, blockFluidBase);
            float fluidHeightForRender6 = this.getFluidHeightForRender(iBlockAccess, x + 1, y, z - 1, blockFluidBase);
            float fluidHeightForRender7 = this.getFluidHeightForRender(iBlockAccess, x + 1, y, z, blockFluidBase);
            float fluidHeightForRender8 = this.getFluidHeightForRender(iBlockAccess, x + 1, y, z + 1, blockFluidBase);
            heightNW = this.getFluidHeightAverage(new float[] {
                fluidHeightForRender1, fluidHeightForRender2, fluidHeightForRender4, fluidHeightForRender
            });
            heightSW = this.getFluidHeightAverage(new float[] {
                fluidHeightForRender2, fluidHeightForRender3, fluidHeightForRender5, fluidHeightForRender
            });
            heightSE = this.getFluidHeightAverage(new float[] {
                fluidHeightForRender5, fluidHeightForRender7, fluidHeightForRender8, fluidHeightForRender
            });
            heightNE = this.getFluidHeightAverage(new float[] {
                fluidHeightForRender4, fluidHeightForRender6, fluidHeightForRender7, fluidHeightForRender
            });
        } else {
            heightNW = fluidHeightForRender;
            heightSW = fluidHeightForRender;
            heightSE = fluidHeightForRender;
            heightNE = fluidHeightForRender;
        }

        if (renderer.renderAllFaces || renderTop) {
            rendered = true;
            IIcon iconStill = this.getNullCheckedIiconOrFallbackTexture();
            float flowDir = (float) BlockFluidBase.getFlowDirection(iBlockAccess, x, y, z);

            heightNW -= RendererSwitchingColorFluid.RENDER_OFFSET;
            heightSW -= RendererSwitchingColorFluid.RENDER_OFFSET;
            heightSE -= RendererSwitchingColorFluid.RENDER_OFFSET;
            heightNE -= RendererSwitchingColorFluid.RENDER_OFFSET;

            double dInterpolatedU,
                    dInterpolatedV,
                    dInterpolatedU2,
                    dInterpolatedV2,
                    dInterpolatedU3,
                    dInterpolatedV3,
                    dInterpolatedU4,
                    dInterpolatedV4;

            if (flowDir < -999.0f) {
                dInterpolatedU = iconStill.getInterpolatedU(0.0);
                dInterpolatedV = iconStill.getInterpolatedV(0.0);
                dInterpolatedU2 = dInterpolatedU;
                dInterpolatedV2 = iconStill.getInterpolatedV(16.0);
                dInterpolatedU3 = iconStill.getInterpolatedU(16.0);
                dInterpolatedV3 = dInterpolatedV2;
                dInterpolatedU4 = dInterpolatedU3;
                dInterpolatedV4 = dInterpolatedV;
            } else {
                float xFlow = MathHelper.sin(flowDir) * 0.25f, zFlow = MathHelper.cos(flowDir) * 0.25f;
                dInterpolatedU = iconStill.getInterpolatedU(8.0f + (-zFlow - xFlow) * 16.0f);
                dInterpolatedV = iconStill.getInterpolatedV(8.0f + (-zFlow + xFlow) * 16.0f);
                dInterpolatedU2 = iconStill.getInterpolatedU(8.0f + (-zFlow + xFlow) * 16.0f);
                dInterpolatedV2 = iconStill.getInterpolatedV(8.0f + (zFlow + xFlow) * 16.0f);
                dInterpolatedU3 = iconStill.getInterpolatedU(8.0f + (zFlow + xFlow) * 16.0f);
                dInterpolatedV3 = iconStill.getInterpolatedV(8.0f + (zFlow - xFlow) * 16.0f);
                dInterpolatedU4 = iconStill.getInterpolatedU(8.0f + (zFlow - xFlow) * 16.0f);
                dInterpolatedV4 = iconStill.getInterpolatedV(8.0f + (-zFlow - xFlow) * 16.0f);
            }

            tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y, z));
            tessellator.setColorOpaque_F(
                    RendererSwitchingColorFluid.LIGHT_Y_POS * red,
                    RendererSwitchingColorFluid.LIGHT_Y_POS * green,
                    RendererSwitchingColorFluid.LIGHT_Y_POS * blue);

            tessellator.addVertexWithUV(x, y + heightNW, z, dInterpolatedU, dInterpolatedV);
            tessellator.addVertexWithUV(x, y + heightSW, z + 1, dInterpolatedU2, dInterpolatedV2);
            tessellator.addVertexWithUV(x + 1, y + heightSE, z + 1, dInterpolatedU3, dInterpolatedV3);
            tessellator.addVertexWithUV(x + 1, y + heightNE, z, dInterpolatedU4, dInterpolatedV4);
            tessellator.addVertexWithUV(x, y + heightNW, z, dInterpolatedU, dInterpolatedV);
            tessellator.addVertexWithUV(x + 1, y + heightNE, z, dInterpolatedU4, dInterpolatedV4);
            tessellator.addVertexWithUV(x + 1, y + heightSE, z + 1, dInterpolatedU3, dInterpolatedV3);
            tessellator.addVertexWithUV(x, y + heightSW, z + 1, dInterpolatedU2, dInterpolatedV2);
        }

        if (renderer.renderAllFaces || renderBottom) {
            rendered = true;
            tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x, y - 1, z));
            tessellator.setColorOpaque_F(
                    RendererSwitchingColorFluid.LIGHT_Y_NEG * red,
                    RendererSwitchingColorFluid.LIGHT_Y_NEG * green,
                    RendererSwitchingColorFluid.LIGHT_Y_NEG * blue);
            renderer.renderFaceYNeg(
                    block,
                    x,
                    y + RendererSwitchingColorFluid.RENDER_OFFSET,
                    z,
                    this.getNullCheckedIiconOrFallbackTexture());
        }

        for (int side = 0; side < 4; ++side) {
            int x2 = x, z2 = z;

            switch (side) {
                case 0:
                    --z2;
                    break;
                case 1:
                    ++z2;
                    break;
                case 2:
                    --x2;
                    break;
                case 3:
                    ++x2;
                    break;
                default:
                    break;
            }

            IIcon iconFlow = this.getNullCheckedIiconOrFallbackTexture();

            if (renderer.renderAllFaces || renderSides[side]) {
                rendered = true;
                double dHeight1, dHeight2, dXcoord1, dXcoord2, dZcoord1, dZcoord2;
                if (side == 0) {
                    dHeight1 = heightNW;
                    dHeight2 = heightNE;
                    dXcoord1 = x;
                    dXcoord2 = x + 1;
                    dZcoord1 = z + RendererSwitchingColorFluid.RENDER_OFFSET;
                    dZcoord2 = z + RendererSwitchingColorFluid.RENDER_OFFSET;
                } else if (side == 1) {
                    dHeight1 = heightSE;
                    dHeight2 = heightSW;
                    dXcoord1 = x + 1;
                    dXcoord2 = x;
                    dZcoord1 = z + 1 - RendererSwitchingColorFluid.RENDER_OFFSET;
                    dZcoord2 = z + 1 - RendererSwitchingColorFluid.RENDER_OFFSET;
                } else if (side == 2) {
                    dHeight1 = heightSW;
                    dHeight2 = heightNW;
                    dXcoord1 = x + RendererSwitchingColorFluid.RENDER_OFFSET;
                    dXcoord2 = x + RendererSwitchingColorFluid.RENDER_OFFSET;
                    dZcoord1 = z + 1;
                    dZcoord2 = z;
                } else {
                    dHeight1 = heightNE;
                    dHeight2 = heightSE;
                    dXcoord1 = x + 1 - RendererSwitchingColorFluid.RENDER_OFFSET;
                    dXcoord2 = x + 1 - RendererSwitchingColorFluid.RENDER_OFFSET;
                    dZcoord1 = z;
                    dZcoord2 = z + 1;
                }
                float u1Flow = iconFlow.getInterpolatedU(0.0);
                float u2Flow = iconFlow.getInterpolatedU(8.0);
                float v1Flow = iconFlow.getInterpolatedV((1.0 - dHeight1) * 16.0 * 0.5);
                float v2Flow = iconFlow.getInterpolatedV((1.0 - dHeight2) * 16.0 * 0.5);
                float v3Flow = iconFlow.getInterpolatedV(8.0);
                tessellator.setBrightness(block.getMixedBrightnessForBlock(iBlockAccess, x2, y, z2));
                float sideLighting;
                if (side < 2) {
                    sideLighting = RendererSwitchingColorFluid.LIGHT_XZ_NEG;
                } else {
                    sideLighting = RendererSwitchingColorFluid.LIGHT_XZ_POS;
                }
                tessellator.setColorOpaque_F(
                        RendererSwitchingColorFluid.LIGHT_Y_POS * sideLighting * red,
                        RendererSwitchingColorFluid.LIGHT_Y_POS * sideLighting * green,
                        RendererSwitchingColorFluid.LIGHT_Y_POS * sideLighting * blue);

                tessellator.addVertexWithUV(dXcoord1, y + dHeight1, dZcoord1, u1Flow, v1Flow);
                tessellator.addVertexWithUV(dXcoord2, y + dHeight2, dZcoord2, u2Flow, v2Flow);
                tessellator.addVertexWithUV(dXcoord2, y, dZcoord2, u2Flow, v3Flow);
                tessellator.addVertexWithUV(dXcoord1, y, dZcoord1, u1Flow, v3Flow);
                tessellator.addVertexWithUV(dXcoord1, y + dHeight1, dZcoord1, u1Flow, v1Flow);
                tessellator.addVertexWithUV(dXcoord1, y, dZcoord1, u1Flow, v3Flow);
                tessellator.addVertexWithUV(dXcoord2, y, dZcoord2, u2Flow, v3Flow);
                tessellator.addVertexWithUV(dXcoord2, y + dHeight2, dZcoord2, u2Flow, v2Flow);
            }
        }
        renderer.renderMinY = 0.0;
        renderer.renderMaxY = 1.0;
        return rendered;
    }

    public boolean shouldRender3DInInventory(int modelId) {
        return false;
    }

    public int getRenderId() {
        return FluidLoader.renderID;
    }

    private IIcon getNullCheckedIiconOrFallbackTexture() {
        return FluidLoader.autogenIIcon != null
                ? FluidLoader.autogenIIcon
                : ((TextureMap) Minecraft.getMinecraft()
                                .getTextureManager()
                                .getTexture(TextureMap.locationBlocksTexture))
                        .getAtlasSprite("missingno");
    }
}
