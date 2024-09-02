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

import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import bartworks.API.recipe.BartWorksRecipeMaps;
import bartworks.util.BWTooltipReference;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicGenerator;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;

public class MTEAcidGenerator extends MTEBasicGenerator {

    public MTEAcidGenerator(int aID, String aName, String aNameRegional, int aTier, ITexture... aTextures) {
        super(aID, aName, aNameRegional, aTier, new String[] {}, aTextures);
    }

    public MTEAcidGenerator(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public int getPollution() {
        return 0;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return BartWorksRecipeMaps.acidGenFuels;
    }

    @Override
    public int getEfficiency() {
        return 100 - 3 * this.mTier;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEAcidGenerator(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture[] getFront(byte aColor) {
        return new ITexture[] { super.getFront(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBack(byte aColor) {
        return new ITexture[] { super.getBack(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public ITexture[] getBottom(byte aColor) {
        return new ITexture[] { super.getBottom(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public ITexture[] getTop(byte aColor) {
        return new ITexture[] { super.getTop(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            TextureFactory.of(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSides(byte aColor) {
        return new ITexture[] { super.getSides(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public ITexture[] getFrontActive(byte aColor) {
        return new ITexture[] { super.getFrontActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            Textures.BlockIcons.OVERLAYS_ENERGY_OUT[this.mTier] };
    }

    @Override
    public ITexture[] getBackActive(byte aColor) {
        return new ITexture[] { super.getBackActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public ITexture[] getBottomActive(byte aColor) {
        return new ITexture[] { super.getBottomActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public ITexture[] getTopActive(byte aColor) {
        return new ITexture[] { super.getTopActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_SOLID_STEEL),
            TextureFactory
                .of(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE")),
            TextureFactory.builder()
                .addIcon(new Textures.BlockIcons.CustomIcon("basicmachines/chemical_reactor/OVERLAY_FRONT_ACTIVE_GLOW"))
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getSidesActive(byte aColor) {
        return new ITexture[] { super.getSidesActive(aColor)[0],
            TextureFactory.of(Textures.BlockIcons.MACHINE_CASING_ACIDHAZARD) };
    }

    @Override
    public boolean isOutputFacing(ForgeDirection side) {
        return side == this.getBaseMetaTileEntity()
            .getFrontFacing();
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("tooltip.tile.acidgen.0.name"),
            StatCollector.translateToLocal("tooltip.tile.acidgen.1.name"),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.0.name") + " "
                + EnumChatFormatting.YELLOW
                + GTValues.V[this.mTier],
            StatCollector.translateToLocal("tooltip.rotor.2.name") + " "
                + EnumChatFormatting.YELLOW
                + this.getEfficiency(),
            StatCollector.translateToLocal("tooltip.tile.tiereddsc.2.name") + " "
                + EnumChatFormatting.YELLOW
                + this.maxAmperesOut(),
            BWTooltipReference.ADDED_BY_BARTIMAEUSNEK_VIA_BARTWORKS.get() };
    }
}
