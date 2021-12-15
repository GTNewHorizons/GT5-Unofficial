package gtPlusPlus.australia.entity.render;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.australia.entity.type.EntityBoar;
import gtPlusPlus.core.lib.CORE;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class RenderBoar extends RenderLiving
{
    private static final ResourceLocation saddledBoarTextures = new ResourceLocation("textures/entity/boar/boar_saddle.png");
    private static final ResourceLocation boarTextures = new ResourceLocation(CORE.MODID+":"+"textures/entity/australia/boar.png");

    public RenderBoar(ModelBase p_i1265_1_, ModelBase p_i1265_2_, float p_i1265_3_)
    {
        super(p_i1265_1_, p_i1265_3_);
        this.setRenderPassModel(p_i1265_2_);
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityBoar p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        if (p_77032_2_ == 0 && p_77032_1_.getSaddled())
        {
            this.bindTexture(saddledBoarTextures);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityBoar p_110775_1_)
    {
        return boarTextures;
    }

    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityLivingBase p_77032_1_, int p_77032_2_, float p_77032_3_)
    {
        return this.shouldRenderPass((EntityBoar)p_77032_1_, p_77032_2_, p_77032_3_);
    }

    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(Entity p_110775_1_)
    {
        return this.getEntityTexture((EntityBoar)p_110775_1_);
    }
}