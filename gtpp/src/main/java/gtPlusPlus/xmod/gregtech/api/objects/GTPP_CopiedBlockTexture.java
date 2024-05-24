package gtPlusPlus.xmod.gregtech.api.objects;

import net.minecraft.block.Block;

import gregtech.api.enums.Dyes;
import gregtech.api.objects.GT_CopiedBlockTexture;

/**
 * Made this to get rid of deprecation warnings <i>everywhere</i>.
 * 
 * @author Alkalus
 *
 */
@SuppressWarnings("deprecation")
public class GTPP_CopiedBlockTexture extends GT_CopiedBlockTexture {

    public GTPP_CopiedBlockTexture(Block aBlock, int aSide, int aMeta, short[] aRGBa, boolean aAllowAlpha) {
        super(aBlock, aMeta, aMeta, aRGBa, aAllowAlpha);
    }

    public GTPP_CopiedBlockTexture(Block aBlock, int aSide, int aMeta, short[] aRGBa) {
        this(aBlock, aSide, aMeta, aRGBa, true);
    }

    public GTPP_CopiedBlockTexture(Block aBlock, int aSide, int aMeta) {
        this(aBlock, aSide, aMeta, Dyes._NULL.mRGBa);
    }
}
