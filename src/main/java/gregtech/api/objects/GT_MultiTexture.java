package gregtech.api.objects;

import gregtech.api.interfaces.ITexture;

/**
 * <p>Lets Multiple ITextures Render overlay over each other.<</p>
 * <p>I should have done this much earlier...</p>
  * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GT_MultiTexture extends gregtech.common.render.GT_MultiTexture implements ITexture {
    public GT_MultiTexture(ITexture... aTextures) {
        super(aTextures);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
