package gregtech.api.ModernMaterials.Blocks.FrameBox.Special;

import static gregtech.api.ModernMaterials.Blocks.FrameBox.Special.CustomTextureRegister.universiumFrameTexture;
import static gregtech.api.ModernMaterials.Render.Utilities.renderBlock;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import fox.spiteful.avaritia.render.CosmicRenderShenanigans;

public class UniversiumFrameItemRenderer implements IItemRenderer {

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    private boolean shouldUseInventoryCosmicRenderHelper(ItemRenderType type) {
        return switch (type) {
            case EQUIPPED, INVENTORY -> true;
            case EQUIPPED_FIRST_PERSON, FIRST_PERSON_MAP, ENTITY -> false;
        };
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        // I don't really understand fully how this works, so I won't be of much help, sorry.

        GL11.glPushMatrix();

        processLightLevel(type, data);

        CosmicRenderShenanigans.inventoryRender = shouldUseInventoryCosmicRenderHelper(type);
        CosmicRenderShenanigans.cosmicOpacity = 2.5f;

        CosmicRenderShenanigans.useShader();

        CosmicRenderShenanigans.bindItemTexture();

        // This is a bit of a hack here, we do this solely because rendering a block icon will cause the stars
        // in the shader to not appear as they are part of the item atlas. So we store our block in the item atlas
        // to draw it along with the stars. Otherwise, the shader would need to be edited.
        CosmicRenderShenanigans.bindItemTexture();

        renderBlock(universiumFrameTexture);

        CosmicRenderShenanigans.releaseShader();
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
