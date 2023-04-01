package gregtech.common.render;

import net.minecraft.client.renderer.entity.RenderArrow;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.client.registry.RenderingRegistry;

public class GT_Renderer_Entity_Arrow extends RenderArrow {

    private final ResourceLocation mTexture;

    public GT_Renderer_Entity_Arrow(Class<? extends EntityArrow> aArrowClass, String aTextureName) {
        this.mTexture = new ResourceLocation("gregtech:textures/entity/" + aTextureName + ".png");
        RenderingRegistry.registerEntityRenderingHandler(aArrowClass, this);
    }

    @Override
    protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
        return this.mTexture;
    }
}
