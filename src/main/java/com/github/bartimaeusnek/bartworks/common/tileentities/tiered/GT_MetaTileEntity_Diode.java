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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode12A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode16A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode2A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode4A_ULV;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_MAX;
import static gregtech.api.enums.MetaTileEntityIDs.Diode8A_ULV;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicHull;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Diode extends GT_MetaTileEntity_BasicHull {

    private long maxAmps;
    private long aAmps;

    public GT_MetaTileEntity_Diode(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, StatCollector.translateToLocal("tooltip.tile.diode.0.name"));
        this.maxAmps = this.getAmpsfromMeta(aID);
        this.aAmps = this.maxAmps;
    }

    public GT_MetaTileEntity_Diode(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (this.maxAmps == 0 && !this.getBaseMetaTileEntity()
            .getWorld().isRemote) {
            this.maxAmps = this.getAmpsfromMeta(
                this.getBaseMetaTileEntity()
                    .getMetaTileID());
            this.aAmps = this.maxAmps;
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);

        if (this.getBaseMetaTileEntity()
            .getWorld().isRemote) return;
        if (!aPlayer.isSneaking()) {
            --this.aAmps;
            if (this.aAmps < 0) this.aAmps = this.maxAmps;
        } else {
            ++this.aAmps;
            if (this.aAmps > this.maxAmps) this.aAmps = 0;
        }
        GT_Utility.sendChatToPlayer(aPlayer, "Max Amps: " + this.aAmps);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setLong("maxAmp", this.maxAmps);
        aNBT.setLong("Amps", this.aAmps);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        this.maxAmps = aNBT.getLong("maxAmp");
        this.aAmps = aNBT.getLong("Amps");
        super.loadNBTData(aNBT);
    }

    @Override
    public long maxAmperesOut() {
        return this.aAmps;
    }

    @Override
    public long maxAmperesIn() {
        return this.aAmps;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Diode(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private long getAmpsfromMeta(int meta) {
        if (meta >= Diode2A_ULV.ID && meta <= Diode2A_MAX.ID) return 2L;
        if (meta >= Diode4A_ULV.ID && meta <= Diode4A_MAX.ID) return 4L;
        if (meta >= Diode8A_ULV.ID && meta <= Diode8A_MAX.ID) return 8L;
        if (meta >= Diode12A_ULV.ID && meta <= Diode12A_MAX.ID) return 12L;
        if (meta >= Diode16A_ULV.ID && meta <= Diode16A_MAX.ID) return 16L;
        return 0L;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(this.mDescriptionArray);
    }
}
