/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2024  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech;

import static kubatech.loaders.BlockLoader.registerBlocks;
import static kubatech.loaders.ItemLoader.registerItems;

import net.minecraftforge.common.MinecraftForge;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import kubatech.api.LoaderReference;
import kubatech.commands.CommandHandler;
import kubatech.config.Config;
import kubatech.loaders.MTLoader;
import kubatech.loaders.MobHandlerLoader;
import kubatech.loaders.RecipeLoader;
import kubatech.loaders.TCLoader;
import kubatech.savedata.PlayerDataManager;

public class CommonProxy {

    public void preInit(FMLPreInitializationEvent event) {
        kubatech.info("Initializing ! Version: " + Tags.VERSION);

        Config.init(event.getModConfigurationDirectory());
        Config.synchronizeConfiguration();
        FMLCommonHandler.instance()
            .bus()
            .register(new FMLEventHandler());
        MinecraftForge.EVENT_BUS.register(new PlayerDataManager());
        registerItems();
        registerBlocks();
        MobHandlerLoader.init();
    }

    public void init(FMLInitializationEvent event) {
        if (LoaderReference.MineTweaker) MTLoader.init();
    }

    public void postInit(FMLPostInitializationEvent event) {
        RecipeLoader.addRecipes();
        if (LoaderReference.Thaumcraft) TCLoader.init();
    }

    public void serverAboutToStart(FMLServerAboutToStartEvent event) {}

    public void serverStarting(FMLServerStartingEvent event) {
        RecipeLoader.addRecipesLate();
        CommandHandler cmd = new CommandHandler();
        event.registerServerCommand(cmd);
    }

    public void serverStarted(FMLServerStartedEvent event) {}

    public void serverStopping(FMLServerStoppingEvent event) {}

    public void serverStopped(FMLServerStoppedEvent event) {}

    public void loadComplete(FMLLoadCompleteEvent event) {}
}
