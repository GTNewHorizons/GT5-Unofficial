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

package com.github.bartimaeusnek.crossmod.galacticgreg;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public class GT_TileEntity_VoidMiners {

    public static class VMLUV extends GT_TileEntity_VoidMiner_Base {

        public VMLUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 1);
        }

        public VMLUV(String aName, int tier) {
            super(aName, tier);
        }

        @Override
        protected ItemList getCasingBlockItem() {
            return ItemList.Casing_UV;
        }

        @Override
        protected Materials getFrameMaterial() {
            return Materials.Europium;
        }

        @Override
        protected int getCasingTextureIndex() {
            return 8;
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMLUV(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMZPM extends GT_TileEntity_VoidMiner_Base {

        public VMZPM(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 2);
        }

        public VMZPM(String aName, int tier) {
            super(aName, tier);
        }

        @Override
        protected ItemList getCasingBlockItem() {
            return ItemList.Casing_MiningBlackPlutonium;
        }

        @Override
        protected Materials getFrameMaterial() {
            return Materials.BlackPlutonium;
        }

        @Override
        protected int getCasingTextureIndex() {
            return 179;
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMZPM(this.mName, this.TIER_MULTIPLIER);
        }
    }

    public static class VMUV extends GT_TileEntity_VoidMiner_Base {

        public VMUV(int aID, String aName, String aNameRegional) {
            super(aID, aName, aNameRegional, 3);
        }

        public VMUV(String aName, int tier) {
            super(aName, tier);
        }

        @Override
        protected ItemList getCasingBlockItem() {
            return ItemList.Casing_MiningNeutronium;
        }

        @Override
        protected Materials getFrameMaterial() {
            return Materials.Neutronium;
        }

        @Override
        protected int getCasingTextureIndex() {
            return 178;
        }

        @Override
        public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
            return new VMUV(this.mName, this.TIER_MULTIPLIER);
        }
    }
}
