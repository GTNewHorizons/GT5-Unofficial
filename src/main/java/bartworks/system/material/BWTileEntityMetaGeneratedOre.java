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

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;

public class BWTileEntityMetaGeneratedOre extends TileEntityMetaGeneratedBlock {

    public boolean mNatural = false;

    @Override
    public void readFromNBT(NBTTagCompound aNBT) {
        super.readFromNBT(aNBT);
        this.mNatural = aNBT.getBoolean("n");
    }

    @Override
    public void writeToNBT(NBTTagCompound aNBT) {
        super.writeToNBT(aNBT);
        aNBT.setBoolean("n", this.mNatural);
    }

    @Override
    public ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        Werkstoff aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData);
        if (aMaterial != null) {
            ITexture aIconSet = TextureFactory
                .of(aMaterial.getTexSet().mTextures[OrePrefixes.ore.mTextureIndex], aMaterial.getRGBA());
            return new ITexture[] { TextureFactory.of(Blocks.stone), aIconSet };
        }
        return new ITexture[] { TextureFactory.of(Blocks.stone),
            TextureFactory.of(gregtech.api.enums.TextureSet.SET_NONE.mTextures[OrePrefixes.ore.mTextureIndex]) };
    }

    @Override
    protected Block getProperBlock() {
        return WerkstoffLoader.BWOres;
    }
}
