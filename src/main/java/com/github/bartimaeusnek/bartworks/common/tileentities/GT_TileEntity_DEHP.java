/*
 * Copyright (c) 2019 bartimaeusnek
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

package com.github.bartimaeusnek.bartworks.common.tileentities;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.gui.GT_GUIContainer_MultiMachine;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.common.tileentities.machines.multi.GT_MetaTileEntity_DrillerBase;
import net.minecraft.entity.player.InventoryPlayer;

public class GT_TileEntity_DEHP extends GT_MetaTileEntity_DrillerBase {

    public GT_TileEntity_DEHP(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_TileEntity_DEHP(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_TileEntity_DEHP(this.mName);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_MultiMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), "DrillingRig.png");
    }

    @Override
    public String[] getDescription() {
        return new String[0];
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_HeatProof;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Tungsten;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 13;
    }

    @Override
    protected int getMinTier() {
        return 5;
    }

    @Override
    protected boolean checkHatches() {
        return !this.mMaintenanceHatches.isEmpty() && !this.mOutputHatches.isEmpty() && !this.mInputHatches.isEmpty();
    }

    @Override
    protected void setElectricityStats() {


    }
}
