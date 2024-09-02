package gregtech.common.render.items;

import static gregtech.api.enums.Mods.Avaritia;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL20;

import codechicken.lib.render.TextureUtils;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;
import gregtech.common.render.GTRenderUtil;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class UniversiumRenderer extends GeneratedMaterialRenderer {

    private static final float cosmicOpacity = 2.5f;

    @Override
    public boolean renderFluidDisplayItem(ItemRenderType type, ItemStack aStack, Object... data) {
        Item item = aStack.getItem();
        if (item == null) return false;

        magicRenderMethod(
            type,
            ItemList.Emitter_UEV.get(1), // hack to make it render correctly
            item.getIconFromDamage(aStack.getItemDamage()),
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
        if (!Avaritia.isModLoaded()) {
            return;
        }

        RenderItem r = RenderItem.getInstance();
        Minecraft mc = Minecraft.getMinecraft();
        Tessellator t = Tessellator.instance;
        float minU = tIcon.getMinU();
        float maxU = tIcon.getMaxU();
        float minV = tIcon.getMinV();
        float maxV = tIcon.getMaxV();

        processLightLevel(type, data);

        GL11.glPushMatrix();
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        if (type == ItemRenderType.INVENTORY) {
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
                GTRenderUtil.renderItem(type, tIcon);
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

            // Draw cosmic overlay
            GTRenderUtil.renderItem(type, tIcon);

            CosmicRenderShenanigans.releaseShader();
            CosmicRenderShenanigans.inventoryRender = false;

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        } else {
            // RENDER ITEM
            GTRenderUtil.renderItem(type, tIcon);

            int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            CosmicRenderShenanigans.cosmicOpacity = cosmicOpacity;
            CosmicRenderShenanigans.useShader();

            // RENDER COSMIC OVERLAY
            GTRenderUtil.renderItem(type, tIcon);
            CosmicRenderShenanigans.releaseShader();
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            GL20.glUseProgram(program);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glPopMatrix();
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
