package gregtech.api.objects;

import gregtech.api.interfaces.ITexture;
import gregtech.common.render.GTMultiTextureRender;

/**
 * <p>
 * Lets Multiple ITextures Render overlay over each other.<
 * </p>
 * <p>
 * I should have done this much earlier...
 * </p>
 *
 * @deprecated Replaced by the {@link gregtech.api.render.TextureFactory} API.
 */
@Deprecated
public class GTMultiTexture extends GTMultiTextureRender implements ITexture {

    public GTMultiTexture(ITexture... aTextures) {
        super(aTextures);
    }

    @Override
    public boolean isOldTexture() {
        return true;
    }
}
