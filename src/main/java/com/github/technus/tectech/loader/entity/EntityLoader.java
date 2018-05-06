package com.github.technus.tectech.loader.entity;

import com.github.technus.tectech.TecTech;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.EntityRegistry;
import openmodularturrets.entity.projectiles.projectileEM;

/**
 * Created by Tec on 30.07.2017.
 */
public class EntityLoader implements Runnable {
    @Override
    public void run() {
        if(Loader.isModLoaded("openmodularturrets")) {
            EntityRegistry.registerModEntity(projectileEM.class, "projectileEM", 0, TecTech.instance, 16, 5, true);
        }
    }
}
