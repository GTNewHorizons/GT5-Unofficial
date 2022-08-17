package kubatech.api.utils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import java.lang.reflect.Field;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLiving;

public class MobUtils {

    private static Field mainmodelfield = null;

    @SideOnly(Side.CLIENT)
    public static float getDesiredScale(EntityLiving e, float desiredHeight) {
        try {
            if (mainmodelfield == null) {
                mainmodelfield = RendererLivingEntity.class.getDeclaredField(
                        ModUtils.isDeobfuscatedEnvironment ? "mainModel" : "field_77045_g");
                mainmodelfield.setAccessible(true);
            }
            float eheight = e.height;
            float ewidth = e.width;
            Render r = RenderManager.instance.getEntityRenderObject(e);
            if (r instanceof RendererLivingEntity && mainmodelfield != null) {
                ModelBase mainmodel = (ModelBase) mainmodelfield.get(r);
                for (Object box : mainmodel.boxList) {
                    if (box instanceof ModelRenderer) {
                        float minY = 999f;
                        float minX = 999f;
                        float maxY = -999f;
                        float maxX = -999f;
                        for (Object cube : ((ModelRenderer) box).cubeList) {
                            if (cube instanceof ModelBox) {
                                if (minY > ((ModelBox) cube).posY1) minY = ((ModelBox) cube).posY1;
                                if (minX > ((ModelBox) cube).posX1) minX = ((ModelBox) cube).posX1;
                                if (maxY < ((ModelBox) cube).posY2) maxY = ((ModelBox) cube).posY2;
                                if (maxX < ((ModelBox) cube).posX2) maxX = ((ModelBox) cube).posX2;
                            }
                        }
                        float cubeheight = (maxY - minY) / 10f;
                        float cubewidth = (maxX - minX) / 10f;
                        if (eheight < cubeheight) eheight = cubeheight;
                        if (ewidth < cubewidth) ewidth = cubewidth;
                    }
                }
            }
            return desiredHeight / eheight;
        } catch (Exception ex) {
            return 1f;
        }
    }
}
