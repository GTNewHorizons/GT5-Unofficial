package gtPlusPlus.xmod.gregtech.api.world;

import gtPlusPlus.xmod.gregtech.HANDLER_GT;
import net.minecraft.block.Block;

import static gtPlusPlus.xmod.gregtech.HANDLER_GT.sCustomWorldgenFile;

import java.util.ArrayList;
import java.util.Collection;

public abstract class GTPP_Worldgen_Ore extends GTPP_Worldgen {
    public final int mBlockMeta, mAmount, mSize, mMinY, mMaxY, mProbability, mDimensionType;
    public final Block mBlock;
    public final Collection<String> mBiomeList;
    public final boolean mAllowToGenerateinVoid;
    private final String aTextWorldgen = "worldgen.";

    public GTPP_Worldgen_Ore(String aName, boolean aDefault, Block aBlock, int aBlockMeta, int aDimensionType, int aAmount, int aSize, int aProbability, int aMinY, int aMaxY, Collection<String> aBiomeList, boolean aAllowToGenerateinVoid) {
        super(aName, HANDLER_GT.sCustomWorldgenList, aDefault);
        mDimensionType = aDimensionType;
        mBlock = aBlock;
        mBlockMeta = Math.min(Math.max(aBlockMeta, 0), 15);
        mProbability = sCustomWorldgenFile.get(aTextWorldgen + mWorldGenName, "Probability", aProbability);
        mAmount = sCustomWorldgenFile.get(aTextWorldgen + mWorldGenName, "Amount", aAmount);
        mSize = sCustomWorldgenFile.get(aTextWorldgen + mWorldGenName, "Size", aSize);
        mMinY = sCustomWorldgenFile.get(aTextWorldgen + mWorldGenName, "MinHeight", aMinY);
        mMaxY = sCustomWorldgenFile.get(aTextWorldgen + mWorldGenName, "MaxHeight", aMaxY);
        if (aBiomeList == null) mBiomeList = new ArrayList<String>();
        else mBiomeList = aBiomeList;
        mAllowToGenerateinVoid = aAllowToGenerateinVoid;
    }
}