package gregtech.common.render.items;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
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

import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.shaders.UniversiumShader;
import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import codechicken.lib.render.TextureUtils;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IGT_ItemWithMaterialRenderer;

@SuppressWarnings("RedundantLabeledSwitchRuleCodeBlock")
public class UniversiumRenderer extends GeneratedMaterialRenderer {

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
                magicRenderMethod(type, aStack, tIcon, false, data);
            }

            GL11.glDisable(GL11.GL_LIGHTING);

            if (tOverlay != null) {
                GL11.glColor3f(1.0F, 1.0F, 1.0F);
                TextureUtils.bindAtlas(aItem.getSpriteNumber());
                renderItemOverlay(type, tOverlay);
            }

            GL11.glDisable(GL11.GL_BLEND);
        }
    }

    private void magicRenderMethod(ItemRenderType type, ItemStack aStack, IIcon tIcon, boolean fluidDisplay,
        Object... data) {

        Minecraft mc = Minecraft.getMinecraft();

        final UniversiumShader shader = UniversiumShader.getInstance();

        processLightLevel(type, shader, data);

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
                ItemRenderUtil.renderItem(type, tIcon);
            }

            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableGUIStandardItemLighting();

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            if (fluidDisplay) {
                GL11.glDisable(GL11.GL_BLEND);
            }

            shader.setRenderInInventory()
                .use();

            GL11.glColor4f(1, 1, 1, 1);

            // Draw cosmic overlay
            ItemRenderUtil.renderItem(type, tIcon);

            UniversiumShader.clear();

            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        } else {
            // RENDER ITEM
            ItemRenderUtil.renderItem(type, tIcon);

            int program = GL11.glGetInteger(GL20.GL_CURRENT_PROGRAM);

            GL11.glDisable(GL11.GL_ALPHA_TEST);
            GL11.glDepthFunc(GL11.GL_EQUAL);
            shader.use();

            // RENDER COSMIC OVERLAY
            ItemRenderUtil.renderItem(type, tIcon);
            UniversiumShader.unbind();
            GL11.glDepthFunc(GL11.GL_LEQUAL);

            GL20.glUseProgram(program);
        }

        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private void processLightLevel(ItemRenderType type, UniversiumShader shader, Object... data) {
        switch (type) {
            case ENTITY -> {
                EntityItem ent = (EntityItem) (data[1]);
                if (ent != null) {
                    shader.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            case EQUIPPED, EQUIPPED_FIRST_PERSON -> {
                EntityLivingBase ent = (EntityLivingBase) (data[1]);
                if (ent != null) {
                    shader.setLightFromLocation(
                        ent.worldObj,
                        MathHelper.floor_double(ent.posX),
                        MathHelper.floor_double(ent.posY),
                        MathHelper.floor_double(ent.posZ));
                }
            }
            default -> {
                shader.setLightLevel(1.0f);
            }
        }
    }
}
