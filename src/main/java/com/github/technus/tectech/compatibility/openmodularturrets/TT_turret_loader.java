package com.github.technus.tectech.compatibility.openmodularturrets;

import com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads.TurretHeadEM;
import com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads.TurretHeadItemRenderEM;
import com.github.technus.tectech.compatibility.openmodularturrets.blocks.turretheads.TurretHeadRenderEM;
import com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles.projectileEM;
import com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles.projectileRenderEM;
import com.github.technus.tectech.compatibility.openmodularturrets.tileentity.turret.TileTurretHeadEM;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import net.minecraft.item.Item;
import net.minecraftforge.client.MinecraftForgeClient;

public class TT_turret_loader implements Runnable {
    @Override
    public void run() {
        TurretHeadRenderEM turretHeadRenderEM = new TurretHeadRenderEM();
        ClientRegistry.bindTileEntitySpecialRenderer(TileTurretHeadEM.class, turretHeadRenderEM);
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(TurretHeadEM.INSTANCE), new TurretHeadItemRenderEM(turretHeadRenderEM, new TileTurretHeadEM()));

        RenderingRegistry.registerEntityRenderingHandler(projectileEM.class, new projectileRenderEM());
    }
}
