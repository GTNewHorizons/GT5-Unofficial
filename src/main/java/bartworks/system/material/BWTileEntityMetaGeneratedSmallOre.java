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

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.XSTR;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class BWTileEntityMetaGeneratedSmallOre extends BWTileEntityMetaGeneratedOre {

    @Override
    public ArrayList<ItemStack> getDrops(int aFortune) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        Materials aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData)
            .getBridgeMaterial();

        if (aMaterial != null) {
            Random tRandom = new XSTR(this.xCoord ^ this.yCoord ^ this.zCoord);
            ArrayList<ItemStack> tSelector = new ArrayList<>();

            ItemStack tStack = GTOreDictUnificator
                .get(OrePrefixes.gemExquisite, aMaterial, GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 1; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawless, aMaterial, GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 2; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 12; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator
                .get(OrePrefixes.gemFlawed, aMaterial, GTOreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(
                OrePrefixes.gemChipped,
                aMaterial,
                GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L),
                1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GTOreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            if (tSelector.size() > 0) {
                int i = 0;

                for (int j = Math.max(1, aFortune > 0 ? tRandom.nextInt(1 + aFortune) : 0); i < j; ++i) {
                    rList.add(GTUtility.copyAmount(1L, tSelector.get(tRandom.nextInt(tSelector.size()))));
                }
            }
            if (tRandom.nextInt(3 + aFortune) > 1) {
                rList.add(
                    GTOreDictUnificator
                        .get(tRandom.nextInt(3) > 0 ? OrePrefixes.dustImpure : OrePrefixes.dust, Materials.Stone, 1L));
            }
        }
        return rList;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, ForgeDirection side) {
        Werkstoff aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData);
        if (aMaterial != null) {
            ITexture aIconSet = TextureFactory
                .of(aMaterial.getTexSet().mTextures[OrePrefixes.oreSmall.mTextureIndex], aMaterial.getRGBA());
            return new ITexture[] { TextureFactory.of(Blocks.stone), aIconSet };
        }
        return new ITexture[] { TextureFactory.of(Blocks.stone),
            TextureFactory.of(gregtech.api.enums.TextureSet.SET_NONE.mTextures[OrePrefixes.oreSmall.mTextureIndex]) };
    }

    @Override
    protected Block GetProperBlock() {
        return WerkstoffLoader.BWSmallOres;
    }
}
