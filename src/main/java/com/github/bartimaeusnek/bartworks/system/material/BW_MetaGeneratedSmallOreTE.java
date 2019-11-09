package com.github.bartimaeusnek.bartworks.system.material;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.objects.XSTR;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;
import java.util.Random;

public class BW_MetaGeneratedSmallOreTE extends BW_MetaGeneratedOreTE {

    public ArrayList<ItemStack> getDrops(Block aDroppedOre, int aFortune) {
        ArrayList<ItemStack> rList = new ArrayList<>();
        Materials aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData).getBridgeMaterial();

        if (aMaterial != null) {
            Random tRandom = new XSTR(this.xCoord ^ this.yCoord ^ this.zCoord);
            ArrayList<ItemStack> tSelector = new ArrayList<>();

            ItemStack tStack = GT_OreDictUnificator.get(OrePrefixes.gemExquisite, aMaterial, GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 1; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.gemFlawless, aMaterial, GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 2; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.gem, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 12; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.gemFlawed, aMaterial, GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.crushed, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.gemChipped, aMaterial, GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L), 1L);
            if (tStack != null) {
                for (int i = 0; i < 5; i++) {
                    tSelector.add(tStack);
                }
            }
            tStack = GT_OreDictUnificator.get(OrePrefixes.dustImpure, aMaterial, 1L);
            if (tStack != null) {
                for (int i = 0; i < 10; i++) {
                    tSelector.add(tStack);
                }
            }
            if (tSelector.size() > 0) {
                int i = 0;

                for(int j = Math.max(1, (aFortune > 0 ? tRandom.nextInt(1 + aFortune) : 0)); i < j; ++i) {
                    rList.add(GT_Utility.copyAmount(1L, tSelector.get(tRandom.nextInt(tSelector.size()))));
                }

            }
            if (tRandom.nextInt(3 + aFortune) > 1) {
                rList.add(GT_OreDictUnificator.get(tRandom.nextInt(3) > 0 ? OrePrefixes.dustImpure : OrePrefixes.dust, Materials.Stone, 1L));
            }
        }
        return rList;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide) {
        Werkstoff aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData);
        if ((aMaterial != null)) {
            GT_RenderedTexture aIconSet = new GT_RenderedTexture(aMaterial.getTexSet().mTextures[OrePrefixes.oreSmall.mTextureIndex], aMaterial.getRGBA());
            return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), aIconSet};
        }
        return new ITexture[]{new GT_CopiedBlockTexture(Blocks.stone, 0, 0), new GT_RenderedTexture(gregtech.api.enums.TextureSet.SET_NONE.mTextures[OrePrefixes.oreSmall.mTextureIndex])};
    }
}
