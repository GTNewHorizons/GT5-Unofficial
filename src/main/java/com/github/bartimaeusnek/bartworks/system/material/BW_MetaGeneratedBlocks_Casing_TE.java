package com.github.bartimaeusnek.bartworks.system.material;

import com.github.bartimaeusnek.bartworks.API.SideReference;
import com.github.bartimaeusnek.bartworks.client.textures.PrefixTextureLinker;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.ITexture;
import gregtech.api.objects.GT_CopiedBlockTexture;
import gregtech.api.objects.GT_RenderedTexture;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;

public class BW_MetaGeneratedBlocks_Casing_TE extends BW_MetaGenerated_Block_TE {

    @Override
    protected Block GetProperBlock() {
        return WerkstoffLoader.BWBlockCasings;
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide) {
        if (SideReference.Side.Client) {
            Werkstoff aMaterial = Werkstoff.werkstoffHashMap.get(this.mMetaData);
            if ((aMaterial != null)) {
                TextureSet set = aMaterial.getTexSet();
                GT_RenderedTexture aIconSet = new GT_RenderedTexture(
                        PrefixTextureLinker.texMapBlocks
                                .get(WerkstoffLoader.blockCasing)
                                .getOrDefault(set, TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex]),
                        aMaterial.getRGBA()
                );
                return new ITexture[]{new GT_CopiedBlockTexture(Blocks.iron_block, 0, 0), aIconSet};
            }
        }
        return new ITexture[]{new GT_CopiedBlockTexture(Blocks.iron_block, 0, 0), new GT_RenderedTexture(gregtech.api.enums.TextureSet.SET_NONE.mTextures[OrePrefixes.block.mTextureIndex])};
    }
}