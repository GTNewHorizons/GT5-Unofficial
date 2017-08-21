package openmodularturrets;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;
import openmodularturrets.blocks.turretheads.TurretHeadEM;
import openmodularturrets.blocks.turretheads.TurretHeadItemRenderEM;
import openmodularturrets.blocks.turretheads.TurretHeadRenderEM;
import openmodularturrets.entity.projectiles.projectileEM;
import openmodularturrets.entity.projectiles.projectileRenderEM;
import openmodularturrets.tileentity.turret.TileTurretHeadEM;

public class TT_turret_loader implements Runnable {
    @Override
    public void run() {
        TurretHeadRenderEM turretHeadRenderEM=new TurretHeadRenderEM();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTurretHeadEM.class, turretHeadRenderEM);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TurretHeadEM.INSTANCE), new TurretHeadItemRenderEM(turretHeadRenderEM, new TileTurretHeadEM()));

        RenderingRegistry.registerEntityRenderingHandler(projectileEM.class, new projectileRenderEM());
    }
}
