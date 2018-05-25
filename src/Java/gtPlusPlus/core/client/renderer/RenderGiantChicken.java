package gtPlusPlus.core.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.util.ResourceLocation;

import gtPlusPlus.core.util.reflect.ReflectionUtils;

@SideOnly(Side.CLIENT)
public class RenderGiantChicken extends RenderChicken {
	
	/**
	 * Fancy reflective handling of nabbing the original chicken texture object, should save reload the texture to memory. :)
	 */
    private static final ResourceLocation chickenTexturesEx;    
    static {
        ResourceLocation mChicken;
		try {
			mChicken = (ResourceLocation) ReflectionUtils.getField(RenderGiantChicken.class, "chickenTextures").get(null);
		}
		catch (IllegalArgumentException | IllegalAccessException | NoSuchFieldException e) {
			mChicken = new ResourceLocation("textures/entity/chicken.png");
		}
        chickenTexturesEx = mChicken;
    }

    public RenderGiantChicken(ModelBase p_i1252_1_, float p_i1252_2_)
    {
        super(p_i1252_1_, p_i1252_2_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(EntityChicken p_110775_1_)
    {
        return chickenTexturesEx;
    }
}