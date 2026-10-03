package gregtech.common.render.items;

import static gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent.tryGetFromFakeStack;
import static gregtech.loaders.ExtraIcons.circuitComponentOverlay;
import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.ItemRenderUtil;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.common.config.Client;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;

public class CircuitComponentItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(final ItemStack item, final ItemRenderType type) {
        return type == INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(final ItemRenderType type, final ItemStack item,
        final ItemRendererHelper helper) {
        return false;
    }

    @Override
    public void renderItem(final ItemRenderType type, final ItemStack item, final Object... data) {
        CircuitComponent cc = tryGetFromFakeStack(item);
        if (cc == null) return;
        if (!cc.isProcessed) {
            GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);

            GL11.glPushMatrix();
            GL11.glTranslatef(2.4f, 2.4f, 0);
            GL11.glScalef(0.7f, 0.7f, 0);

            ItemStack realItem = cc.realComponent.get();
            if (realItem == null) return; // in case a CC is relying on a real item that is yet to be implemented.
            if (realItem.getItem() instanceof ItemBlock) {
                RenderHelper.enableGUIStandardItemLighting();
                RenderItem.getInstance()
                    .renderItemIntoGUI(
                        Minecraft.getMinecraft().fontRenderer,
                        Minecraft.getMinecraft().renderEngine,
                        realItem,
                        0,
                        0,
                        false);
                RenderHelper.disableStandardItemLighting();
            } else {
                IItemRenderer baseRenderer = MinecraftForgeClient.getItemRenderer(realItem, INVENTORY);
                if (baseRenderer != null) {
                    baseRenderer.renderItem(type, realItem, data);
                } else if (realItem.getItem() != null) {
                    // This case is just used for GT++ items because their rendering is insane. Remove this if
                    // they ever get IItemRenderers :)
                    int coloration = realItem.getItem()
                        .getColorFromItemStack(realItem, 0);

                    int r = (coloration & 0xFF0000) >> 16;
                    int g = (coloration & 0xFF00) >> 8;
                    int b = (coloration & 0xFF);

                    GL11.glColor3f(r / 255F, g / 255F, b / 255F);

                    ItemRenderUtil.renderItem(
                        type,
                        realItem.getItem()
                            .getIcon(realItem, 0));
                }
            }

            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glColor3f(1, 1, 1);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            Minecraft.getMinecraft()
                .getTextureManager()
                .bindTexture(TextureMap.locationItemsTexture);
            GL11.glTranslatef(0f, 0f, 2f);
            ItemRenderUtil.renderItem(type, circuitComponentOverlay);

            GL11.glDisable(GL11.GL_BLEND);

            GL11.glPopMatrix();

            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPopAttrib();
        } else {
            switch (cc) {
                case ProcessedBoltCosmicNeutronium -> {
                    if (Client.render.renderCosmicNeutroniumFancy) {
                        GL11.glEnable(GL11.GL_BLEND);
                        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                        GL11.glDisable(GL11.GL_ALPHA_TEST);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);

                        CosmicNeutroniumRenderer.renderHalo(type);

                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                        ItemRenderUtil.renderItem(type, item.getIconIndex());

                        return;
                    }
                }
                case ProcessedFrameboxMagMatter, ProcessedWireMagMatter -> {
                    if (Client.render.renderInfinityFancy) {
                        IIcon icon = item.getIconIndex();
                        InfinityRenderer.renderHalo();
                        InfinityRenderer.renderPulse(icon, icon);

                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);

                        ItemRenderUtil.renderItem(type, icon);
                        return;
                    }
                }
                case ProcessedBoltTranscendentMetal -> {
                    if (Client.render.renderTransMetalFancy) {
                        GL11.glPushMatrix();
                        GL11.glEnable(GL11.GL_ALPHA_TEST);

                        IIcon icon = item.getIconIndex();
                        TranscendentalMetaItemRenderer.applyEffect(type);

                        boolean flip = false;
                        if (type.equals(IItemRenderer.ItemRenderType.INVENTORY)) {
                            GL11.glScalef(16, 16, 32);
                            flip = true;
                        }

                        ItemRenderer.renderItemIn2D(
                            Tessellator.instance,
                            flip ? icon.getMinU() : icon.getMaxU(),
                            flip ? icon.getMinV() : icon.getMaxV(),
                            flip ? icon.getMaxU() : icon.getMinU(),
                            flip ? icon.getMaxV() : icon.getMinV(),
                            icon.getIconWidth(),
                            icon.getIconHeight(),
                            0.0625F);

                        GL11.glPopMatrix();
                        return;
                    }
                }
                case ProcessedWireUniversium, SupermassiveSpool, CosmologicalStrands, BundledStellarHarmonyWire -> {
                    if (Client.render.renderUniversiumFancy) {
                        IIconContainer mask = switch (cc) {
                            case ProcessedWireUniversium -> Textures.ItemIcons.MASK_SPOOL;
                            case SupermassiveSpool -> Textures.ItemIcons.MASK_SUPERMASSIVE;
                            case CosmologicalStrands -> Textures.ItemIcons.MASK_STRANDS;
                            case BundledStellarHarmonyWire -> Textures.ItemIcons.MASK_HARMONY;
                            default -> null;
                        };
                        UniversiumMetaItemRenderer.magicRenderMethod(type, item.getIconIndex(), mask.getIcon(), data);
                        return;
                    }
                }
                case ProcessedPlanckCircuitCasing -> {
                    IIcon icon = item.getIconIndex();
                    IIconContainer mask = Textures.ItemIcons.MASK_ENCASEMENT;

                    if (Client.render.renderInfinityFancy) {
                        InfinityRenderer.renderHalo();
                        InfinityRenderer.renderPulse(icon, icon);

                        GL11.glEnable(GL11.GL_ALPHA_TEST);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                        GL11.glDisable(GL11.GL_BLEND);
                        GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
                    }

                    if (Client.render.renderUniversiumFancy) {
                        UniversiumMetaItemRenderer.magicRenderMethod(type, icon, mask.getIcon(), data);
                    } else {
                        ItemRenderUtil.renderItem(type, item.getIconIndex());
                    }

                    return;
                }
            }
            ItemRenderUtil.renderItem(type, item.getIconIndex());
        }
    }
}
