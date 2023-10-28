/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2023  kuba6000
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

import static kubatech.api.enums.ItemList.LegendaryRedTea;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.objectweb.asm.tree.ClassNode;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerAboutToStartEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.event.FMLServerStoppedEvent;
import cpw.mods.fml.common.event.FMLServerStoppingEvent;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import kubatech.api.enums.ItemList;
import kubatech.api.helpers.ReflectionHelper;
import kubatech.loaders.BlockLoader;
import kubatech.network.CustomTileEntityPacket;
import kubatech.network.LoadConfigPacket;

@SuppressWarnings("unused")
@Mod(
    modid = Tags.MODID,
    version = Tags.VERSION,
    name = Tags.MODNAME,
    acceptedMinecraftVersions = "[1.7.10]",
    dependencies = "required-after: gregtech; " + "required-after: gtnhmixins@[2.0.1,); "
        + "required-after: modularui; "
        + "required-after: mobsinfo; "
        + "after: EnderIO; "
        + "after: AWWayofTime; "
        + "after: ExtraUtilities; "
        + "after: InfernalMobs; "
        + "after: Thaumcraft; "
        + "after: MineTweaker3; "
        + "after: miscutils; "
        + "after: harvestcraft; "
        + "after: Forestry; "
        + "after: DraconicEvolution; "
        + "after: Avaritia; "
        + "after: dreamcraft; ")
public class kubatech {

    public static kubatech instance = null;
    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(Tags.MODID);
    public static final CreativeTabs KT = new CreativeTabs(Tags.MODID) {

        private ItemStack iconItemStack = null;

        @Override
        public ItemStack getIconItemStack() {
            if (iconItemStack == null) iconItemStack = LegendaryRedTea.get(1);
            return iconItemStack;
        }

        @Override
        public Item getTabIconItem() {
            return null;
        }

        @Override
        public String getTranslatedTabLabel() {
            return Tags.MODNAME;
        }

        @SuppressWarnings({ "unchecked", "rawtypes" })
        @Override
        public void displayAllReleventItems(List p_78018_1_) {
            super.displayAllReleventItems(p_78018_1_);
            if (ItemList.ExtremeExterminationChamber.hasBeenSet())
                p_78018_1_.add(ItemList.ExtremeExterminationChamber.get(1));
            if (ItemList.ExtremeIndustrialApiary.hasBeenSet()) p_78018_1_.add(ItemList.ExtremeIndustrialApiary.get(1));
            if (ItemList.ExtremeIndustrialGreenhouse.hasBeenSet())
                p_78018_1_.add(ItemList.ExtremeIndustrialGreenhouse.get(1));
            if (ItemList.DraconicEvolutionFusionCrafter.hasBeenSet())
                p_78018_1_.add(ItemList.DraconicEvolutionFusionCrafter.get(1));
        }
    };

    static {
        NETWORK.registerMessage(new LoadConfigPacket.Handler(), LoadConfigPacket.class, 0, Side.CLIENT);
        NETWORK.registerMessage(new CustomTileEntityPacket.Handler(), CustomTileEntityPacket.class, 1, Side.CLIENT);
    }

    private static final Logger LOG = LogManager.getLogger(Tags.MODID);

    @SidedProxy(clientSide = Tags.MODID + ".ClientProxy", serverSide = Tags.MODID + ".CommonProxy")
    public static CommonProxy proxy;

    public static Collection<ClassNode> myClasses;

    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        instance = this;
        final long timeStart = System.currentTimeMillis();
        try {
            myClasses = ReflectionHelper.getClasses(
                this.getClass()
                    .getPackage()
                    .getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        final long timeToLoad = System.currentTimeMillis() - timeStart;
        info("Class discovery took " + timeToLoad + "ms ! Found " + myClasses.size() + " classes.");
        proxy.preInit(event);
    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @Mod.EventHandler
    public void serverAboutToStart(FMLServerAboutToStartEvent event) {
        proxy.serverAboutToStart(event);
    }

    @Mod.EventHandler
    public void serverStarting(FMLServerStartingEvent event) {
        proxy.serverStarting(event);
    }

    @Mod.EventHandler
    public void serverStarted(FMLServerStartedEvent event) {
        proxy.serverStarted(event);
    }

    @Mod.EventHandler
    public void serverStopping(FMLServerStoppingEvent event) {
        proxy.serverStopping(event);
    }

    @Mod.EventHandler
    public void serverStopped(FMLServerStoppedEvent event) {
        proxy.serverStopped(event);
    }

    @Mod.EventHandler
    public void loadComplete(FMLLoadCompleteEvent event) {
        proxy.loadComplete(event);
    }

    @Mod.EventHandler
    public void missingMappings(FMLMissingMappingsEvent event) {
        for (FMLMissingMappingsEvent.MissingMapping missingMapping : event.getAll()) {
            if (missingMapping.name.equals("EMT:EMT_GTBLOCK_CASEING")) {
                if (missingMapping.type == GameRegistry.Type.BLOCK) missingMapping.remap(BlockLoader.defcCasingBlock);
                else missingMapping.remap(Item.getItemFromBlock(BlockLoader.defcCasingBlock));
            }
        }
    }

    public static void debug(String message) {
        LOG.debug(message);
    }

    public static void info(String message) {
        LOG.info(message);
    }

    public static void warn(String message) {
        LOG.warn(message);
    }

    public static void error(String message) {
        LOG.error(message);
    }
}
