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

import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_BACK;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_FRONT;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPELINE_FLUID_SIDE_GLOW;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

public class GT_MetaTileEntity_LongDistancePipelineFluid extends GT_MetaTileEntity_LongDistancePipelineBase {

    static final FluidTankInfo[] emptyTank = { new FluidTankInfo(null, Integer.MAX_VALUE) };

    public GT_MetaTileEntity_LongDistancePipelineFluid(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, "Sends fluids over long distances");
    }

    public GT_MetaTileEntity_LongDistancePipelineFluid(String aName, int aTier, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public boolean isSameClass(GT_MetaTileEntity_LongDistancePipelineBase other) {
        return other instanceof GT_MetaTileEntity_LongDistancePipelineFluid;
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
        return new GT_MetaTileEntity_LongDistancePipelineFluid(mName, mTier, getDescription()[0], mTextures);
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_PIPELINE_FLUID_FRONT) };
        else if (sideDirection == facingDirection.getOpposite()) return new ITexture[] {
            MACHINE_CASINGS[mTier][colorIndex + 1], TextureFactory.of(OVERLAY_PIPELINE_FLUID_BACK) };
        else return new ITexture[] { MACHINE_CASINGS[mTier][colorIndex + 1],
            TextureFactory.of(OVERLAY_PIPELINE_FLUID_SIDE), TextureFactory.builder()
                .addIcon(OVERLAY_PIPELINE_FLUID_SIDE_GLOW)
                .glow()
                .build() };
    }
}
