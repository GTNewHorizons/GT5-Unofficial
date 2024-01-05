package gregtech.api.modernmaterials;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;

public abstract class RendererRegistration {

    public void initRenderersSafely(ModernMaterial.ModernMaterialBuilder builder) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            registerRenderers(builder);
        }
    }

    protected abstract void registerRenderers(ModernMaterial.ModernMaterialBuilder builder);

}
