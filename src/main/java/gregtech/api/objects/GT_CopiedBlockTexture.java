package gregtech.api.objects;

import net.minecraft.block.Block;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;

/**
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GT_CopiedBlockTexture extends gregtech.common.render.GT_CopiedBlockTexture implements ITexture {

    // Backwards Compat
    @Deprecated
    public short[] mRGBa;

    public GT_CopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa, boolean aAllowAlpha) {
        super(aBlock, ordinalSide, aMeta, aRGBa, aAllowAlpha);
        GT_CopiedBlockTexture.this.mRGBa = aRGBa;
    }

    public GT_CopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa) {
        this(aBlock, ordinalSide, aMeta, aRGBa, true);
    }

    public GT_CopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta) {
        this(aBlock, ordinalSide, aMeta, Dyes._NULL.mRGBa);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
