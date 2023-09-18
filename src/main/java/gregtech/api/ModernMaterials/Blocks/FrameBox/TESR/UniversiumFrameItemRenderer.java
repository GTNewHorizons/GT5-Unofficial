package gregtech.api.ModernMaterials.Blocks.FrameBox.TESR;

import gregtech.api.ModernMaterials.ModernMaterialUtilities;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import fox.spiteful.avaritia.render.CosmicRenderShenanigans;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.IIcon;
import org.lwjgl.opengl.GL11;

import static gregtech.api.ModernMaterials.Blocks.FrameBox.TESR.CustomTextureRegister.universiumFrameTexture;
import static gregtech.api.ModernMaterials.ModernMaterialUtilities.renderBlock;

public class UniversiumFrameItemRenderer implements IItemRenderer {
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        // I don't really understand fully how this works, so I won't be of much help, sorry.

        GL11.glPushMatrix();

        CosmicRenderShenanigans.inventoryRender = !(type == ItemRenderType.EQUIPPED_FIRST_PERSON);
        CosmicRenderShenanigans.cosmicOpacity = 2.5f;


        CosmicRenderShenanigans.useShader();

        CosmicRenderShenanigans.bindItemTexture();

        GL11.glTranslated(0.5, 0.5, 0.5);

        // This is a bit of a hack here, we do this solely because rendering a block icon will cause the stars
        // in the shader to not appear as they are part of the item atlas. So we store our block in the item atlas
        // to draw it along with the stars. Otherwise, the shader would need to be edited.
        CosmicRenderShenanigans.bindItemTexture();

        renderBlock(universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();
        GL11.glPopMatrix();


    }
}
