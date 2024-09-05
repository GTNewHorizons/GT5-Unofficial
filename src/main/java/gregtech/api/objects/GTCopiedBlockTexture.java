package gregtech.api.objects;

import net.minecraft.block.Block;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.common.render.GTCopiedBlockTextureRender;

/**
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GTCopiedBlockTexture extends GTCopiedBlockTextureRender implements ITexture {

    // Backwards Compat
    @Deprecated
    public short[] mRGBa;

    public GTCopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa, boolean aAllowAlpha) {
        super(aBlock, ordinalSide, aMeta, aRGBa, aAllowAlpha);
        GTCopiedBlockTexture.this.mRGBa = aRGBa;
    }

    public GTCopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta, short[] aRGBa) {
        this(aBlock, ordinalSide, aMeta, aRGBa, true);
    }

    public GTCopiedBlockTexture(Block aBlock, int ordinalSide, int aMeta) {
        this(aBlock, ordinalSide, aMeta, Dyes._NULL.mRGBa);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
