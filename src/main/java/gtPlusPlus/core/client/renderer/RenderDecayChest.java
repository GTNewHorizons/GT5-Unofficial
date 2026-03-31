package gtPlusPlus.core.client.renderer;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.MinecraftForgeClient;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.client.model.ModelDecayChest;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;

@SideOnly(Side.CLIENT)
public class RenderDecayChest extends TileEntitySpecialRenderer implements IItemRenderer {

    private static final ResourceLocation mChestTexture = new ResourceLocation(
        GTPlusPlus.ID,
        "textures/blocks/TileEntities/DecayablesChest_full.png");
    private final ModelDecayChest mChestModel = new ModelDecayChest();
    private static final TileEntityDecayablesChest ITEM_RENDER_TILE = new TileEntityDecayablesChest();

    public static RenderDecayChest INSTANCE;
    public final int mRenderID;

    public RenderDecayChest() {
        INSTANCE = this;
        this.mRenderID = 22;
        final Item chestItem = Item.getItemFromBlock(ModBlocks.blockDecayablesChest);
        if (chestItem != null) {
            MinecraftForgeClient.registerItemRenderer(chestItem, this);
        }
        Logger.INFO("Registered Lead Lined Chest Renderer.");
    }

    public void renderTileEntityAt(TileEntityDecayablesChest tile, double xPos, double yPos, double zPos,
        float partialTick) {
        int facing = 3;
        if (tile.hasWorldObj()) {
            facing = tile.getFacing();
        }
        this.bindTexture(mChestTexture);
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) xPos, (float) yPos + 1.0F, (float) zPos + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);

        float f1 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;

        int k = 0;
        if (facing == 2) {
            k = 180;
        } else if (facing == 4) {
            k = 90;
        } else if (facing == 5) {
            k = -90;
        }
        GL11.glRotatef(k, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        f1 = 1.0F - f1;
        f1 = 1.0F - f1 * f1 * f1;
        mChestModel.chestLid.rotateAngleX = -(f1 * GTPPCore.PI / 2.0F);
        mChestModel.renderAll();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void renderTileEntityAt(TileEntity p_147500_1_, double p_147500_2_, double p_147500_4_, double p_147500_6_,
        float p_147500_8_) {
        this.renderTileEntityAt(
            (TileEntityDecayablesChest) p_147500_1_,
            p_147500_2_,
            p_147500_4_,
            p_147500_6_,
            p_147500_8_);
    }

    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return type == ItemRenderType.ENTITY || type == ItemRenderType.EQUIPPED
            || type == ItemRenderType.EQUIPPED_FIRST_PERSON
            || type == ItemRenderType.INVENTORY;
    }

    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        if (type != ItemRenderType.INVENTORY) {
            double offset = -0.5D;
            if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
                offset = 0.0D;
            } else if (type == ItemRenderType.ENTITY) {
                GL11.glScalef(0.5F, 0.5F, 0.5F);
            }
            if (data.length > 0 && data[0] instanceof RenderBlocks renderBlocks) {
                CustomItemBlockRenderer.renderItemAsBlock(renderBlocks, item, offset, offset, offset);
            }
            return;
        }

        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        this.renderTileEntityAt(ITEM_RENDER_TILE, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
    }
}
