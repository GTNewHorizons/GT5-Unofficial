package gtPlusPlus.core.common;

import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.IItemRenderer;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.common.registry.GameRegistry;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.InternalEntityRegistry;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.BurnableFuelHandler;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.COMPAT_IntermodStaging;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.handler.events.EnderDragonDeathHandler;
import gtPlusPlus.core.handler.events.EntityDeathHandler;
import gtPlusPlus.core.handler.events.GeneralTooltipEventHandler;
import gtPlusPlus.core.handler.events.PlayerSleepEventHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.CORE_Preloader;
import gtPlusPlus.xmod.gregtech.api.util.SpecialBehaviourTooltipHandler;
import gtPlusPlus.xmod.gregtech.recipes.GregtechRecipeAdder;
import gtPlusPlus.xmod.ic2.CustomInternalName;

public class CommonProxy {

    public CommonProxy() {
        // Should Register Gregtech Materials I've Made
        Utils.registerEvent(this);
    }

    public void preInit(final FMLPreInitializationEvent e) {
        Logger.INFO("Doing some house cleaning.");
        CORE.RA = new GregtechRecipeAdder();
        Logger.INFO("Created Gregtech recipe handler.");
        if (!CORE_Preloader.DEBUG_MODE) {
            Logger.WARNING("Development mode not enabled.");
        } else if (CORE_Preloader.DEBUG_MODE) {
            Logger.INFO("Development mode enabled.");
        } else {
            Logger.WARNING("Development mode not set.");
        }

        AddToCreativeTab.initialiseTabs();
        CustomInternalName.init();

        ModItems.init();
        ModBlocks.init();
        CI.preInit();
        COMPAT_IntermodStaging.preInit(e);
        BookHandler.run();
        // Registration of entities and renderers
        Logger.INFO("[Proxy] Calling Entity registration.");
        registerEntities();
        Logger.INFO("[Proxy] Calling Tile Entity registration.");
        registerTileEntities();

        Logger.INFO("[Proxy] Calling Render registration.");
        registerRenderThings();
    }

    public void init(final FMLInitializationEvent e) {
        CI.init();

        Utils.registerEvent(new GeneralTooltipEventHandler());
        // Handles Tooltips for items giving custom multiblock behaviour
        Utils.registerEvent(new SpecialBehaviourTooltipHandler());
        // Handles Sleep Benefits
        PlayerSleepEventHandler.init();
        // Handles Magic Feather
        Utils.registerEvent(ModItems.itemMagicFeather);

        Utils.registerEvent(new EnderDragonDeathHandler());
        Utils.registerEvent(new EntityDeathHandler());

        // Compat Handling
        COMPAT_HANDLER.registerMyModsOreDictEntries();
        COMPAT_HANDLER.intermodOreDictionarySupport();
        COMPAT_IntermodStaging.init(e);
    }

    public void postInit(final FMLPostInitializationEvent e) {
        Logger.INFO("Cleaning up, doing postInit.");
        PlayerCache.initCache();

        // Make Burnables burnable
        if (!CORE.burnables.isEmpty()) {
            BurnableFuelHandler fuelHandler = new BurnableFuelHandler();
            GameRegistry.registerFuelHandler(fuelHandler);
            Logger.INFO("[Fuel Handler] Registering " + fuelHandler.getClass().getName());
        }

        // Compat Handling
        Logger.INFO("Removing recipes from other mods.");
        COMPAT_HANDLER.RemoveRecipesFromOtherMods();
        Logger.INFO("Initialising Handler, Then Adding Recipes");
        COMPAT_HANDLER.InitialiseHandlerThenAddRecipes();
        Logger.INFO("Loading Intermod staging.");
        COMPAT_IntermodStaging.postInit(e);
        Logger.INFO("Loading queued recipes.");
        COMPAT_HANDLER.runQueuedRecipes();
        Logger.INFO("Registering custom mob drops.");
        registerCustomMobDrops();

        // Moved last, to prevent recipes being generated post initialisation.
        Logger.INFO("Loading Gregtech API recipes.");
        COMPAT_HANDLER.startLoadingGregAPIBasedRecipes();
    }

    public void serverStarting(final FMLServerStartingEvent e) {
        COMPAT_HANDLER.InitialiseLateHandlerThenAddRecipes();
    }

    public void onLoadComplete(FMLLoadCompleteEvent event) {
        COMPAT_IntermodStaging.onLoadComplete(event);
        COMPAT_HANDLER.onLoadComplete(event);
    }

    public void registerNetworkStuff() {
        GuiHandler.init();
    }

    public void registerEntities() {
        InternalEntityRegistry.registerEntities();
    }

    public void registerTileEntities() {
        ModTileEntities.init();
    }

    public void registerRenderThings() {}

    public int addArmor(final String armor) {
        return 0;
    }

    public void registerCustomMobDrops() {

        // Blazes
        if (ItemUtils.doesOreDictHaveEntryFor("dustPyrotheum")) {
            EntityUtils.registerDropsForMob(
                    EntityBlaze.class,
                    ItemUtils.getItemStackOfAmountFromOreDict("dustPyrotheum", 1),
                    1,
                    10);
            EntityUtils.registerDropsForMob(
                    EntityBlaze.class,
                    ItemUtils.getItemStackOfAmountFromOreDict("dustPyrotheum", 1),
                    1,
                    10);
        }

        // GalaxySpace Support
        if (ReflectionUtils.doesClassExist("galaxyspace.core.entity.mob.EntityEvolvedColdBlaze")) {
            Class<?> aColdBlaze = ReflectionUtils.getClass("galaxyspace.core.entity.mob.EntityEvolvedColdBlaze");
            ItemStack aSmallBlizz, aTinyBlizz, aSmallCryo, aTinyCryo;
            aSmallBlizz = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallBlizz", 1);
            aTinyBlizz = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyBlizz", 1);
            aSmallCryo = ItemUtils.getItemStackOfAmountFromOreDict("dustSmallCryotheum", 1);
            aTinyCryo = ItemUtils.getItemStackOfAmountFromOreDict("dustTinyCryotheum", 1);
            EntityUtils.registerDropsForMob(
                    aColdBlaze,
                    ItemUtils.getItemStackOfAmountFromOreDict("stickBlizz", 1),
                    2,
                    500);
            if (aSmallBlizz != null) {
                EntityUtils.registerDropsForMob(aColdBlaze, aSmallBlizz, 2, 750);
            }
            if (aTinyBlizz != null) {
                EntityUtils.registerDropsForMob(aColdBlaze, aTinyBlizz, 4, 1500);
            }
            if (aSmallCryo != null) {
                EntityUtils.registerDropsForMob(aColdBlaze, aSmallCryo, 1, 50);
            }
            if (aTinyCryo != null) {
                EntityUtils.registerDropsForMob(aColdBlaze, aTinyCryo, 2, 100);
            }
        }
    }

    protected final AutoMap<Pair<Item, IItemRenderer>> mItemRenderMappings = new AutoMap<>();

    public World getClientWorld() {
        return null;
    }

    /**
     * Returns a side-appropriate EntityPlayer for use during message handling
     */
    public EntityPlayer getPlayerEntity(MessageContext ctx) {
        return ctx.getServerHandler().playerEntity;
    }
}
