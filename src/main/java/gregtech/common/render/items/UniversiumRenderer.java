package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import codechicken.lib.render.TextureUtils;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

import static gregtech.api.enums.Mods.Avaritia;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class UniversiumRenderer extends GT_GeneratedMaterial_Renderer {

    private static final float cosmicOpacity = 2.5f;

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return helper == ItemRendererHelper.ENTITY_ROTATION || helper == ItemRendererHelper.ENTITY_BOBBING;
    }

    @Override
    public boolean renderFluidDisplayItem(ItemRenderType type, ItemStack aStack, Object... data) {
        magicRenderMethod(
            type,
            ItemList.Emitter_UEV.get(1), // hack to make it render correctly
            aStack.getItem()
                .getIconFromDamage(aStack.getItemDamage()),
            true,
            data);
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack aStack, Object... data) {
        short aMetaData = (short) aStack.getItemDamage();
        if (!(aStack.getItem() instanceof IGT_ItemWithMaterialRenderer aItem)) return;

        int passes = 1;
        if (aItem.requiresMultipleRenderPasses()) {
            passes = aItem.getRenderPasses(aMetaData);
        }

        for (int pass = 0; pass < passes; pass++) {
            IIcon tIcon = aItem.getIcon(aMetaData, pass);
            IIcon tOverlay = aItem.getOverlayIcon(aMetaData, pass);

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_ALPHA_TEST);

            if (tIcon != null) {
                markNeedsAnimationUpdate(tIcon);
                magicRenderMethod(type, aStack, tIcon, false, data);
            }

            GL11.glDisable(GL11.GL_LIGHTING);

            if (tOverlay != null) {
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                TextureUtils.bindAtlas(aItem.getSpriteNumber());
                markNeedsAnimationUpdate(tOverlay);
                renderItemOverlay(type, tOverlay);
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private void magicRenderMethod(ItemRenderType type, ItemStack aStack, IIcon tIcon, boolean fluidDisplay,
        Object... data) {
        if (!Avaritia.isModLoaded()){
            return;
        }

        RenderItem r = RenderItem.getInstance();
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator t = Tessellator.instance;

        processLightLevel(type, data);

        switch (type) {
            case ENTITY -> {
                GL11.glPushMatrix();
                if (aStack.isOnItemFrame()) GL11.glTranslatef(0F, -0.3F, 0.01F);
                render(tIcon);
                GL11.glPopMatrix();

            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                render(tIcon);
            }
            case INVENTORY -> {
                GL11.glPushMatrix();
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                if (fluidDisplay) {
                    // this somehow makes shader render correctly
                    ResourceLocation resourcelocation = mc.getTextureManager()
                        .getResourceLocation(aStack.getItemSpriteNumber());
                    mc.getTextureManager()
                        .bindTexture(resourcelocation);
                } else {
                    r.renderItemIntoGUI(mc.fontRenderer, mc.getTextureManager(), aStack, 0, 0, true);
                }

                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                RenderHelper.enableGUIStandardItemLighting();

                GL11.glDisable(GL11.GL_ALPHA_TEST);
                GL11.glDisable(GL11.GL_DEPTH_TEST);

                if (fluidDisplay) {
                    GL11.glDisable(GL11.GL_BLEND);
                }

                CosmicRenderShenanigans.cosmicOpacity = cosmicOpacity;
                CosmicRenderShenanigans.inventoryRender = true;
                CosmicRenderShenanigans.useShader();

                GL11.glColor4d(1, 1, 1, 1);

                float minu = tIcon.getMinU();
                float maxu = tIcon.getMaxU();
                float minv = tIcon.getMinV();
                float maxv = tIcon.getMaxV();

                // Draw cosmic overlay
                t.startDrawingQuads();
                t.addVertexWithUV(0, 0, 0, minu, minv);
                t.addVertexWithUV(0, 16, 0, minu, maxv);
                t.addVertexWithUV(16, 16, 0, maxu, maxv);
                t.addVertexWithUV(16, 0, 0, maxu, minv);
                t.draw();

                CosmicRenderShenanigans.releaseShader();
                CosmicRenderShenanigans.inventoryRender = false;

                GL11.glEnable(GL11.GL_ALPHA_TEST);
                GL11.glEnable(GL12.GL_RESCALE_NORMAL);
                GL11.glEnable(GL11.GL_DEPTH_TEST);

                r.renderWithColor = true;

                GL11.glDisable(GL11.GL_BLEND);
                GL11.glPopMatrix();
            }
            default -> {}
        }
    }

    private void render(IIcon icon) {
        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glColor4f(1F, 1F, 1F, 1F);

        float f, f1, f2, f3;
        float scale = 1F / 16F;

        f = icon.getMinU();
        f1 = icon.getMaxU();
        f2 = icon.getMinV();
        f3 = icon.getMaxV();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        // RENDER ITEM IN HAND
        ItemRenderer
            .renderItemIn2D(Tessellator.instance, f1, f2, f, f3, icon.getIconWidth(), icon.getIconHeight(), scale);

        GL11.glDisable(GL11.GL_ALPHA_TEST);
        GL11.glDepthFunc(GL11.GL_EQUAL);
        CosmicRenderShenanigans.cosmicOpacity = cosmicOpacity;
        CosmicRenderShenanigans.useShader();

        float minu = icon.getMinU();
        float maxu = icon.getMaxU();
        float minv = icon.getMinV();
        float maxv = icon.getMaxV();

        // RENDER COSMIC OVERLAY IN HAND
        ItemRenderer.renderItemIn2D(
            Tessellator.instance,
            maxu,
            minv,
            minu,
            maxv,
            icon.getIconWidth(),
            icon.getIconHeight(),
            scale);
        CosmicRenderShenanigans.releaseShader();
        GL11.glDepthFunc(GL11.GL_LEQUAL);
        GL11.glEnable(GL11.GL_ALPHA_TEST);

        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();

        GL11.glColor4f(1F, 1F, 1F, 1F);
    }

    private void processLightLevel(ItemRenderType type, Object... data) {
        switch (type) {
            case ENTITY -> {
                EntityItem ent = (EntityItem) (data[1]);
                if (ent != null) {
                    CosmicRenderShenanigans.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                EntityLivingBase ent = (EntityLivingBase) (data[1]);
                if (ent != null) {
                    CosmicRenderShenanigans.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            case INVENTORY -> {
                CosmicRenderShenanigans.setLightLevel(10.2f);
            }
            default -> {
                CosmicRenderShenanigans.setLightLevel(1.0f);
            }
        }
    }
}
