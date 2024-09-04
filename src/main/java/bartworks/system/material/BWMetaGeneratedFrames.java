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

package bartworks.system.material;

import static gregtech.api.enums.GTValues.RA;
import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTModHandler.RecipeBits;
import gregtech.api.util.GTOreDictUnificator;

public class BWMetaGeneratedFrames extends MetaPipeEntity {

    public final Werkstoff mMaterial;

    public BWMetaGeneratedFrames(int aID, String aName, String aNameRegional, Werkstoff aMaterial) {
        super(aID, aName, aNameRegional, 0);
        this.mMaterial = aMaterial;

        GTOreDictUnificator.registerOre(OrePrefixes.frameGt, aMaterial, this.getStackForm(1));
        GTModHandler.addCraftingRecipe(
            this.getStackForm(2),
            RecipeBits.NOT_REMOVABLE | GTModHandler.RecipeBits.BUFFERED,
            new Object[] { "SSS", "SwS", "SSS", 'S', this.mMaterial.get(OrePrefixes.stick) });

        RA.stdBuilder()
            .itemInputs(
                GTOreDictUnificator.get(OrePrefixes.stick, aMaterial.getVarName(), 4),
                ItemList.Circuit_Integrated.getWithDamage(0, 4))
            .itemOutputs(getStackForm(1))
            .duration(3 * SECONDS + 4 * TICKS)
            .eut(8)
            .addTo(assemblerRecipes);
    }

    private BWMetaGeneratedFrames(String aName, Werkstoff aMaterial) {
        super(aName, 0);
        this.mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        return 4;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new BWMetaGeneratedFrames(this.mName, this.mMaterial);
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, byte aConnections,
        int aColorIndex, boolean aConnected, boolean aRedstone) {
        return new ITexture[] { TextureFactory.of(
            this.mMaterial.getTexSet().mTextures[OrePrefixes.frameGt.mTextureIndex],
            Dyes.getModulation(aColorIndex, this.mMaterial.getRGBA())) };
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Just something you can put a Cover or CFoam on." };
    }

    @Override
    public final boolean isSimpleMachine() {
        return true;
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return true;
    }

    @Override
    public final float getThickNess() {
        return 1.0F;
    }

    @Override
    public final void saveNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final void loadNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int connect(ForgeDirection side) {
        return 0;
    }

    @Override
    public void disconnect(ForgeDirection side) {
        /* Do nothing */
    }
}
