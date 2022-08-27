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

package com.github.bartimaeusnek.bartworks.common.tileentities.tiered;

import com.github.bartimaeusnek.bartworks.util.BWRecipes;
import com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference;
import com.github.bartimaeusnek.bartworks.util.ChatColorHelper;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicGenerator;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import net.minecraft.util.StatCollector;

public class GT_MetaTileEntity_AcidGenerator extends GT_MetaTileEntity_BasicGenerator {

    public GT_MetaTileEntity_AcidGenerator(
            int aID, String aName, String aNameRegional, int aTier, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, new String[] {}, aTextures);
    }

    public GT_MetaTileEntity_AcidGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipes() {
        return BWRecipes.instance.getMappingsFor((byte) 2);
    }

    @Override
    public int getEfficiency() {
        return 100 - 3 * this.mTier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GT_MetaTileEntity_AcidGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    public ITexture[] getFront(byte aColor) {
        return new ITexture[] {
            super.getFront(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]
        };
    }

    public ITexture[] getBack(byte aColor) {
        return new ITexture[] {
            super.getBack(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] {
            super.getBottom(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public ITexture[] getTop(byte aColor) {
        return new ITexture[] {
            super.getTop(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT")),
            TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_GLOW"))
                    .glow()
                    .build()
        };
    }

    public ITexture[] getSides(byte aColor) {
        return new ITexture[] {
            super.getSides(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] {
            super.getFrontActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier]
        };
    }

    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] {
            super.getBackActive(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] {
            super.getBottomActive(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] {
            super.getTopActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            TextureFactory.of(
                    new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")),
            TextureFactory.builder()
                    .addIcon(new Textures.BlockIcons.CustomIcon(
                            "basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE_GLOW"))
                    .glow()
                    .build()
        };
    }

    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] {
            super.getSidesActive(aColor)[0], TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD)
        };
    }

    public boolean isOutputFacing(byte aSide) {
        return aSide == this.getBaseMetaTileEntity().getFrontFacing();
    }

    public String[] getDescription() {
        return new String[] {
            StatCollector.translateToLocal("tooltip.tile.acidgen.0.name"),
            StatCollector.translateToLocal("tooltip.tile.acidgen.1.name"),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.0.name") + " " + ChatColorHelper.YELLOW
                    + GT_Values.V[this.mTier],
            StatCollector.translateToLocal("tooltip.rotor.2.name") + " " + ChatColorHelper.YELLOW
                    + this.getEfficiency(),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.2.name") + " " + ChatColorHelper.YELLOW
                    + this.maxAmperesOut(),
            BW_Tooltip_Reference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get()
        };
    }
}
