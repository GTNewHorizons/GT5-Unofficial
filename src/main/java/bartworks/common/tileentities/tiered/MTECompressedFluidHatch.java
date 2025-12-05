/*
 * Copyright (c) 2018-2020 bartimaeusnek Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following
 * conditions: The above copyright notice and this permission notice shall be included in all copies or substantial
 * portions of the Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN
 * ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package bartworks.common.tileentities.tiered;

import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;

import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.util.GTUtility;

@IMetaTileEntity.SkipGenerateDescription
public class MTECompressedFluidHatch extends MTEHatchInput {

    final int CAPACITY = 100_000_000;

    public MTECompressedFluidHatch(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    public MTECompressedFluidHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return GTUtility.areFluidsEqual(aFluid, Materials.LiquidAir.getFluid(1))
            || GTUtility.areFluidsEqual(aFluid, Materials.NetherSemiFluid.getFluid(1));
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTECompressedFluidHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    protected FluidSlotWidget createFluidSlot() {
        return super.createFluidSlot()
            .setFilter(f -> (f == Materials.LiquidAir.mFluid || f == Materials.NetherSemiFluid.mFluid));
    }
}
