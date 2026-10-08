package gtPlusPlus.core.client.renderer;

import static gregtech.api.enums.Mods.GTPlusPlus;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.IBlockAccess;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gtPlusPlus.core.client.model.ModelDecayChest;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.tileentities.general.TileEntityDecayablesChest;

@SideOnly(Side.CLIENT)
public class RenderDecayChest extends TileEntitySpecialRenderer implements ISimpleBlockRenderingHandler {

    private static final ResourceLocation mChestTexture = new ResourceLocation(
        GTPlusPlus.ID,
        "textures/blocks/TileEntities/DecayablesChest_full.png");
    private final ModelDecayChest mChestModel = new ModelDecayChest();

    public static RenderDecayChest INSTANCE;
    public final int mRenderID;

    public RenderDecayChest() {
        INSTANCE = this;
        this.mRenderID = RenderingRegistry.getNextAvailableRenderId();
    }

    public void renderTileEntityAt(TileEntityDecayablesChest tile, double xPos, double yPos, double zPos,
        float partialTick) {
        int facing = 3;
        if (tile.hasWorldObj()) {
            facing = tile.getFacing();
        }
        this.bindTexture(mChestTexture);
        float f1 = tile.prevLidAngle + (tile.lidAngle - tile.prevLidAngle) * partialTick;
        this.renderChest(f1, facing, xPos, yPos, zPos);
    }

    @Override
    public void renderInventoryBlock(Block block, int metadata, int modelId, RenderBlocks renderer) {
        this.bindTexture(mChestTexture);
        GL11.glPushMatrix();
        GL11.glRotatef(90.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        this.renderChest(0.0F, 3, 0.0D, 0.0D, 0.0D);
        GL11.glPopMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
    }

    @Override
    public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId,
        RenderBlocks renderer) {
        return false;
    }

    @Override
    public boolean shouldRender3DInInventory(int modelId) {
        return true;
    }

    @Override
    public int getRenderId() {
        return this.mRenderID;
    }

    private void renderChest(float lidAngle, int facing, double xPos, double yPos, double zPos) {
        GL11.glPushMatrix();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glTranslatef((float) xPos, (float) yPos + 1.0F, (float) zPos + 1.0F);
        GL11.glScalef(1.0F, -1.0F, -1.0F);
        GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        GL11.glRotatef(this.getFacingRotation(facing), 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
        final float closedAngle = 1.0F - lidAngle;
        final float animatedAngle = 1.0F - closedAngle * closedAngle * closedAngle;
        mChestModel.chestLid.rotateAngleX = -(animatedAngle * GTPPCore.PI / 2.0F);
        mChestModel.renderAll();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glPopMatrix();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private float getFacingRotation(int facing) {
        if (facing == 2) {
            return 180.0F;
        }
        if (facing == 4) {
            return 90.0F;
        }
        if (facing == 5) {
            return -90.0F;
        }
        return 0.0F;
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
}
