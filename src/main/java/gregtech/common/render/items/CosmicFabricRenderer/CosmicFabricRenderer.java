package gregtech.common.render.items.CosmicFabricRenderer;

import fox.spiteful.avaritia.items.ItemResource;
import fox.spiteful.avaritia.items.LudicrousItems;
import fox.spiteful.avaritia.render.ICosmicRenderItem;
import gregtech.common.render.items.GT_GeneratedMaterial_Renderer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import singulariteam.eternalsingularity.render.CosmicRenderStuffs;


public class CosmicFabricRenderer extends GT_GeneratedMaterial_Renderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
    return true;
}

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {

        int spread = 4;
        IIcon halo = ((ItemResource) LudicrousItems.resource).halo[0];
        int haloColour = -16777216;

        RenderItem r = RenderItem.getInstance();
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator t = Tessellator.instance;

        this.processLightLevel(type, item, data);

        switch (type) {
            case ENTITY: {
                GL11.glPushMatrix();
                GL11.glTranslatef(-0.5F, 0F, 0F);
                if (item.isOnItemFrame()) GL11.glTranslatef(0F, -0.3F, 0.01F);
                render(item, null);
                GL11.glPopMatrix();

                break;
            }
            case EQUIPPED: {
                render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                render(item, data[1] instanceof EntityPlayer ? (EntityPlayer) data[1] : null);
                break;
            }
            case INVENTORY: {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                float ca = (float) (haloColour >> 24 & 255) / 255.0F;
                float cr = (float) (haloColour >> 16 & 255) / 255.0F;
                float cg = (float) (haloColour >> 8 & 255) / 255.0F;
                float cb = (float) (haloColour & 255) / 255.0F;
                GL11.glColor4f(cr, cg, cb, ca);

                t.startDrawingQuads();
                t.addVertexWithUV(0 - spread, 0 - spread, 0, halo.getMinU(), halo.getMinV());
                t.addVertexWithUV(0 - spread, 16 + spread, 0, halo.getMinU(), halo.getMaxV());
                t.addVertexWithUV(16 + spread, 16 + spread, 0, halo.getMaxU(), halo.getMaxV());
                t.addVertexWithUV(16 + spread, 0 - spread, 0, halo.getMaxU(), halo.getMinV());
                t.draw();

                r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                r.renderWithColor = true;

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();

                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), item, 0, 0, true);

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                CosmicRenderStuffs.cosmicOpacity = 1.0f;
                CosmicRenderStuffs.inventoryRender = true;
                CosmicRenderStuffs.useShader();

                IIcon cosmicicon = item.getIconIndex();

                GL11.glColor4d(1, 1, 1, 1);

                float minu = cosmicicon.getMinU();
                float maxu = cosmicicon.getMaxU();
                float minv = cosmicicon.getMinV();
                float maxv = cosmicicon.getMaxV();

                t.startDrawingQuads();
                t.addVertexWithUV(0, 0, 0, minu, minv);
                t.addVertexWithUV(0, 16, 0, minu, maxv);
                t.addVertexWithUV(16, 16, 0, maxu, maxv);
                t.addVertexWithUV(16, 0, 0, maxu, minv);
                t.draw();

                CosmicRenderStuffs.releaseShader();
                CosmicRenderStuffs.inventoryRender = false;

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                r.renderWithColor = true;

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
                break;
            }
            default:
                break;
        }
    }

    public void render(ItemStack item, EntityPlayer player) {
        int passes = 1;
        if (item.getItem().requiresMultipleRenderPasses()) {
            passes = item.getItem().getRenderPasses(item.getItemDamage());
        }

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);
        // ItemRenderer.renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(),
        // scale);

        float r, g, b;
        IIcon icon;
        float f, f1, f2, f3;
        float scale = 1F / 16F;

        // Lumberjack.log(Level.INFO, "passes: "+passes);

        for (int i = 0; i < passes; i++) {
            icon = this.getStackIcon(item, i, player);

            // Lumberjack.log(Level.INFO, "icon "+i+": "+icon);

            f = icon.getMinU();
            f1 = icon.getMaxU();
            f2 = icon.getMinV();
            f3 = icon.getMaxV();

            int colour = item.getItem().getColorFromItemStack(item, i);
            r = (float) (colour & 255) / 255.0F;
            g = (float) (colour & 255) / 255.0F;
            b = (float) (colour & 255) / 255.0F;
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            ItemRenderer.renderItemIn2D(
                Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);
        }

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        CosmicRenderStuffs.cosmicOpacity = 1.0f;
        CosmicRenderStuffs.useShader();

        IIcon cosmicicon = item.getIconIndex();

        float minu = cosmicicon.getMinU();
        float maxu = cosmicicon.getMaxU();
        float minv = cosmicicon.getMinV();
        float maxv = cosmicicon.getMaxV();
        ItemRenderer.renderItemIn2D(
            Tessellator.instance,
            maxu,
            minv,
            minu,
            maxv,
            cosmicicon.getIconWidth(),
            cosmicicon.getIconHeight(),
            scale);
        CosmicRenderStuffs.releaseShader();
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    public void processLightLevel(ItemRenderType type, ItemStack item, Object... data) {
        switch (type) {
            case ENTITY: {
                EntityItem ent = (EntityItem) (data[1]);
                if (ent != null) {
                    CosmicRenderStuffs.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case EQUIPPED: {
                EntityLivingBase ent = (EntityLivingBase) (data[1]);
                if (ent != null) {
                    CosmicRenderStuffs.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case EQUIPPED_FIRST_PERSON: {
                EntityLivingBase ent = (EntityLivingBase) (data[1]);
                if (ent != null) {
                    CosmicRenderStuffs.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
                break;
            }
            case INVENTORY: {
                CosmicRenderStuffs.setLightLevel(1.2f);
                return;
            }
            default: {
                CosmicRenderStuffs.setLightLevel(1.0f);
            }
        }
    }

    public IIcon getStackIcon(ItemStack stack, int pass, EntityPlayer player) {
        return stack.getItem().getIcon(stack, pass);
    }
}
