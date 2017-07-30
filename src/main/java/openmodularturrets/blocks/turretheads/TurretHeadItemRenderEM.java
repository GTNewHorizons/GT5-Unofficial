package openmodularturrets.blocks.turretheads;

import openmodularturrets.tileentity.turret.TileTurretHeadEM;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import openmodularturrets.client.render.models.ModelLaserTurret;
import org.lwjgl.opengl.GL11;

/**
 * Created by Tec on 30.07.2017.
 */
public class TurretHeadItemRenderEM implements IItemRenderer {
    private final TurretHeadRenderEM turretRenderer;
    private final TileTurretHeadEM turretTileEntity;
    private final ModelLaserTurret model;

    public TurretHeadItemRenderEM(TurretHeadRenderEM turretRenderer, TileTurretHeadEM turretTileEntity) {
        this.turretRenderer = turretRenderer;
        this.turretTileEntity = turretTileEntity;
        this.model = new ModelLaserTurret();
    }

    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        return true;
    }

    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }

    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        GL11.glPushMatrix();
        GL11.glTranslated(-0.5D, -0.5D, -0.5D);
        this.turretRenderer.renderTileEntityAt(this.turretTileEntity, 0.0D, 0.0D, 0.0D, 0.0F);
        GL11.glPopMatrix();
    }
}
