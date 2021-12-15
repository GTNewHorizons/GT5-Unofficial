package gtPlusPlus.core.client.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.entity.RenderChicken;

@SideOnly(Side.CLIENT)
public class RenderGiantChicken extends RenderChicken {

    public RenderGiantChicken(ModelBase p_i1252_1_, float p_i1252_2_)
    {
        super(p_i1252_1_, p_i1252_2_);
    }
}