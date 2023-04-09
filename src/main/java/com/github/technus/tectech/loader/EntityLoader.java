package com.github.technus.tectech.loader;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.openmodularturrets.entity.projectiles.projectileEM;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;

import static gregtech.api.enums.Mods.OpenModularTurrets;

/**
 * Created by Tec on 30.07.2017.
 */
public class EntityLoader implements Runnable {

    @Override
    public void run() {
        if (OpenModularTurrets.isModLoaded()) {
            EntityRegistry.registerModEntity(projectileEM.class, "projectileEM", 0, TecTech.instance, 16, 5, true);
        }
    }
}
