/*
 * KubaTech - Gregtech Addon Copyright (C) 2022 - 2023 kuba6000 This library is free software; you can redistribute it
 * and/or modify it under the terms of the GNU Lesser General Public License as published by the Free Software
 * Foundation; either version 3 of the License, or (at your option) any later version. This library is distributed in
 * the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details. You should have
 * received a copy of the GNU Lesser General Public License along with this library. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package kubatech.api.utils;

import java.lang.reflect.Field;

import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLiving;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class MobUtils {

    private static Field mainmodelfield = null;

    @SideOnly(Side.CLIENT)
    public static float getDesiredScale(EntityLiving e, float desiredHeight) {
        return getDesiredScale(getMobHeight(e), desiredHeight);
    }

    @SideOnly(Side.CLIENT)
    public static float getDesiredScale(float entityHeight, float desiredHeight) {
        return desiredHeight / entityHeight;
    }

    @SideOnly(Side.CLIENT)
    public static float getMobHeight(EntityLiving e) {
        try {
            if (mainmodelfield == null) {
                mainmodelfield = RendererLivingEntity.class
                    .getDeclaredField(ModUtils.isDeobfuscatedEnvironment ? "mainModel" : "field_77045_g");
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
            return eheight;
        } catch (Exception ex) {
            return 1f;
        }
    }
}
