/**
 *
 * Inspired/ported from GregTech 6 under the LGPL license
 *
 * Copyright (c) 2020 GregTech-6 Team
 *
 * This file is part of GregTech.
 *
 * GregTech is free software: you can redistribute it and/or modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * GregTech is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with GregTech. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package gregtech.common.tileentities.machines.long_distance;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_BACK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE_LEFT_RIGHT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE_LEFT_RIGHT_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE_UP_DOWN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE_UP_DOWN_GLOW;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class MTELongDistancePipelineFluid extends MTELongDistancePipelineBase {

    static final FluidTankInfo[] emptyTank = { new FluidTankInfo(null, Integer.MAX_VALUE) };

    public MTELongDistancePipelineFluid(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Sends fluids over long distances");
    }

    public MTELongDistancePipelineFluid(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isSameClass(MTELongDistancePipelineBase other) {
        return other instanceof MTELongDistancePipelineFluid;
    }

    @Override
    public int getPipeMeta() {
        return 0;
    }

    public IFluidHandler getTank() {
        final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
        TileEntity tankTile = tTile.getTileEntityAtSide(tTile.getBackFacing());
        if (tankTile instanceof IFluidHandler) return (IFluidHandler) tankTile;
        else return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection side) {
        if (checkTarget()) {
            final IFluidHandler tankTile = getTank();
            if (tankTile != null) return tankTile.getTankInfo(side);
        }

        return emptyTank;
    }

    @Override
    public int fill(ForgeDirection side, FluidStack aFluid, boolean aDoFill) {
        if (checkTarget()) {
            final IGregTechTileEntity tTile = mTarget.getBaseMetaTileEntity();
            final IFluidHandler tankTile = getTank();
            if (tankTile != null) return tankTile.fill(tTile.getFrontFacing(), aFluid, aDoFill);
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection side, FluidStack aFluid, boolean aDoDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection side, int aMaxDrain, boolean aDoDrain) {
        return null;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELongDistancePipelineFluid(mName, mTier, getDescription()[0], mTextures);
    }

    @Override
    public ITexture[] getTextureOverlays() {
        ITexture[] overlays = new ITexture[4];
        overlays[INPUT_INDEX] = TextureFactory.of(OVERLAY_PIPELINE_FLUID_FRONT);
        overlays[OUTPUT_INDEX] = TextureFactory.of(OVERLAY_PIPELINE_FLUID_BACK);
        overlays[SIDE_UP_DOWN_INDEX] = TextureFactory.of(
            TextureFactory.of(OVERLAY_PIPELINE_FLUID_SIDE_UP_DOWN),
            TextureFactory.builder()
                .addIcon(OVERLAY_PIPELINE_FLUID_SIDE_UP_DOWN_GLOW)
                .glow()
                .build());
        overlays[SIDE_LEFT_RIGHT_INDEX] = TextureFactory.of(
            TextureFactory.of(OVERLAY_PIPELINE_FLUID_SIDE_LEFT_RIGHT),
            TextureFactory.builder()
                .addIcon(OVERLAY_PIPELINE_FLUID_SIDE_LEFT_RIGHT_GLOW)
                .glow()
                .build());

        return overlays;
    }
}
