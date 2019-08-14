package gtPlusPlus.core.common;

import static gtPlusPlus.core.lib.CORE.DEBUG;

import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLLoadCompleteEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.OrePrefixes;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.api.objects.minecraft.ChunkManager;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.entity.InternalEntityRegistry;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.fluids.FluidFactory;
import gtPlusPlus.core.handler.BookHandler;
import gtPlusPlus.core.handler.BurnableFuelHandler;
import gtPlusPlus.core.handler.COMPAT_HANDLER;
import gtPlusPlus.core.handler.COMPAT_IntermodStaging;
import gtPlusPlus.core.handler.GuiHandler;
import gtPlusPlus.core.handler.StopAnnoyingFuckingAchievements;
import gtPlusPlus.core.handler.events.BlockEventHandler;
import gtPlusPlus.core.handler.events.EnderDragonDeathHandler;
import gtPlusPlus.core.handler.events.EntityDeathHandler;
import gtPlusPlus.core.handler.events.GeneralTooltipEventHandler;
import gtPlusPlus.core.handler.events.PickaxeBlockBreakEventHandler;
import gtPlusPlus.core.handler.events.ZombieBackupSpawnEventHandler;
import gtPlusPlus.core.item.ModItems;
import gtPlusPlus.core.item.chemistry.GenericChem;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.lib.CORE.ConfigSwitches;
import gtPlusPlus.core.lib.LoadedMods;
import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.recipe.common.CI;
import gtPlusPlus.core.tileentities.ModTileEntities;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.debug.DEBUG_INIT;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.player.PlayerCache;
import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.plugin.villagers.block.BlockGenericSpawner;
import gtPlusPlus.xmod.eio.handler.HandlerTooltip_EIO;
import gtPlusPlus.xmod.galacticraft.handler.HandlerTooltip_GC;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.common.ForgeChunkManager;

public class CommonProxy {

	private boolean mFluidsGenerated = false;

	public CommonProxy() {
		// Should Register Gregtech Materials I've Made
		Utils.registerEvent(this);
		if (!CORE.GTNH) {
			Logger.INFO("We're using Gregtech " + Utils.getGregtechVersionAsString());
		} else {
			Logger.INFO("We're using GTNH's Gregtech " + Utils.getGregtechVersionAsString());
		}
	}

	public void preInit(final FMLPreInitializationEvent e) {
		Logger.INFO("Doing some house cleaning.");
		LoadedMods.checkLoaded();
		Logger.INFO("Making sure we're ready to party!");

		if (!DEBUG) {
			Logger.WARNING("Development mode not enabled.");
		} else if (DEBUG) {
			Logger.INFO("Development mode enabled.");
		} else {
			Logger.WARNING("Development mode not set.");
		}

		AddToCreativeTab.initialiseTabs();

		// Moved from Init after Debug Loading.
		// 29/01/18 - Alkalus
		// Moved earlier into PreInit, so that Items exist before they're called upon in
		// recipes.
		// 20/03/18 - Alkalus
		ModItems.init();
		ModBlocks.init();
		CI.preInit();
		FluidFactory.preInit();
		COMPAT_IntermodStaging.preInit(e);
		BookHandler.run();
		// Registration of entities and renderers
		Logger.INFO("[Proxy] Calling Entity registrator.");
		registerEntities();
		Logger.INFO("[Proxy] Calling Tile Entity registrator.");
		registerTileEntities();

		Logger.INFO("[Proxy] Calling Render registrator.");
		registerRenderThings();

		if (!mFluidsGenerated && ItemList.valueOf("Cell_Empty").hasBeenSet()) {
			Material.generateQueuedFluids();
			mFluidsGenerated = true;
		}

	}

	public void init(final FMLInitializationEvent e) {
		// Debug Loading
		if (CORE.DEBUG) {
			DEBUG_INIT.registerHandlers();
		}

		registerCustomItemsForMaterials();
		ModBlocks.blockCustomMobSpawner = new BlockGenericSpawner();

		if (!mFluidsGenerated && ItemList.valueOf("Cell_Empty").hasBeenSet()) {
			Material.generateQueuedFluids();
			mFluidsGenerated = true;
		} else {
			Logger.INFO("[ERROR] Did not generate fluids at all.");
			Logger.WARNING("[ERROR] Did not generate fluids at all.");
			Logger.ERROR("[ERROR] Did not generate fluids at all.");
		}
		CI.init();
		FluidFactory.init();

		/**
		 * Register the Event Handlers.
		 */

		// Prevents my Safes being destroyed.
		Utils.registerEvent(new PickaxeBlockBreakEventHandler());
		// Block Handler for all events.
		Utils.registerEvent(new BlockEventHandler());
		Utils.registerEvent(new GeneralTooltipEventHandler());
		// Handles Custom tooltips for EIO.
		Utils.registerEvent(new HandlerTooltip_EIO());
		// Handles Custom Tooltips for GC
		Utils.registerEvent(new HandlerTooltip_GC());
		
		if (CORE.DEVENV) {
			Utils.registerEvent(new StopAnnoyingFuckingAchievements());
		}

		// Register Chunkloader
		ForgeChunkManager.setForcedChunkLoadingCallback(GTplusplus.instance, ChunkManager.getInstance());
		Utils.registerEvent(ChunkManager.getInstance());
		Utils.registerEvent(new EnderDragonDeathHandler());
		Utils.registerEvent(new EntityDeathHandler());

		if (ConfigSwitches.disableZombieReinforcement) {
			// Make Zombie reinforcements fuck off.
			Utils.registerEvent(new ZombieBackupSpawnEventHandler());
		}

		/**
		 * End of Subscribe Event registration.
		 */

		// Compat Handling
		COMPAT_HANDLER.registerMyModsOreDictEntries();
		COMPAT_HANDLER.intermodOreDictionarySupport();
		COMPAT_IntermodStaging.init(e);
	}

	public void postInit(final FMLPostInitializationEvent e) {
		Logger.INFO("Cleaning up, doing postInit.");
		PlayerCache.initCache();
		FluidFactory.postInit();

		// Make Burnables burnable
		if (!CORE.burnables.isEmpty()) {
			BurnableFuelHandler fuelHandler = new BurnableFuelHandler();
			GameRegistry.registerFuelHandler(fuelHandler);
			Logger.INFO("[Fuel Handler] Registering " + fuelHandler.getClass().getName());
		}

		// Compat Handling
		COMPAT_HANDLER.RemoveRecipesFromOtherMods();
		COMPAT_HANDLER.InitialiseHandlerThenAddRecipes();
		COMPAT_HANDLER.startLoadingGregAPIBasedRecipes();
		COMPAT_IntermodStaging.postInit(e);
		COMPAT_HANDLER.runQueuedRecipes();
		registerCustomMobDrops();
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

	public void registerRenderThings() {

	}

	public int addArmor(final String armor) {
		return 0;
	}

	public void generateMysteriousParticles(final Entity entity) {

	}

	public void generateMobSpawners() {
		// Try register some test spawners
		Utils.createNewMobSpawner(0, EntityGiantChickenBase.class);
		Utils.createNewMobSpawner(1, EntitySickBlaze.class);
		Utils.createNewMobSpawner(2, EntityStaballoyConstruct.class);
	}
	
	public void registerCustomItemsForMaterials() {
		Material.registerComponentForMaterial(GenericChem.CARBYNE, OrePrefixes.plate, GregtechItemList.Carbyne_Sheet_Finished.get(1));
	}
	
	public void registerCustomMobDrops() {
		
		//Zombie
		EntityUtils.registerDropsForMob(EntityZombie.class, ItemUtils.getSimpleStack(ModItems.itemRope), 3, 100);
		EntityUtils.registerDropsForMob(EntityZombie.class, ItemUtils.getSimpleStack(ModItems.itemFiber), 5, 250);
		EntityUtils.registerDropsForMob(EntityZombie.class, ItemUtils.getSimpleStack(ModItems.itemSandstoneHammer), 1, 10);
		EntityUtils.registerDropsForMob(EntityZombie.class, ItemUtils.getSimpleStack(ModItems.itemBomb), 2, 10);
		EntityUtils.registerDropsForMob(EntityZombie.class, ALLOY.TUMBAGA.getTinyDust(1), 1, 10);
		EntityUtils.registerDropsForMob(EntityZombie.class, ALLOY.POTIN.getTinyDust(1), 1, 10);
		
		//Blazes		
		if (ItemUtils.doesOreDictHaveEntryFor("dustPyrotheum")) {
			EntityUtils.registerDropsForMob(EntityBlaze.class, ItemUtils.getItemStackOfAmountFromOreDict("dustPyrotheum", 1), 1, 10);	
			EntityUtils.registerDropsForMob(EntityBlaze.class, ItemUtils.getItemStackOfAmountFromOreDict("dustPyrotheum", 1), 1, 10);			
		}
			
		
		//Special mobs Support
		if (ReflectionUtils.doesClassExist("toast.specialMobs.entity.zombie.EntityBrutishZombie")) {
			Class<?> aBrutishZombie = ReflectionUtils.getClass("toast.specialMobs.entity.zombie.EntityBrutishZombie");
			ItemStack aFortune1 = ItemUtils.getEnchantedBook(Enchantment.fortune, 1);
			ItemStack aFortune2 = ItemUtils.getEnchantedBook(Enchantment.fortune, 1);
			ItemStack aFortune3 = ItemUtils.getEnchantedBook(Enchantment.fortune, 1);
			EntityUtils.registerDropsForMob(aBrutishZombie, aFortune1, 1, 100);
			EntityUtils.registerDropsForMob(aBrutishZombie, aFortune2, 1, 50);
			EntityUtils.registerDropsForMob(aBrutishZombie, aFortune3, 1, 1);	
			EntityUtils.registerDropsForMob(aBrutishZombie, ItemUtils.getItemStackOfAmountFromOreDict("ingotRedAlloy", 1), 3, 200);		
		}
		
		
		
	}
	
	protected final AutoMap<Pair<Item, IItemRenderer>> mItemRenderMappings = new AutoMap<Pair<Item, IItemRenderer>>();
	

	public static void registerItemRendererGlobal(Item aItem, IItemRenderer aRenderer) {
		GTplusplus.proxy.registerItemRenderer(aItem, aRenderer);
	}
	
	public void registerItemRenderer(Item aItem, IItemRenderer aRenderer) {
		if (Utils.isServer()) {
			return;
		}
		else {
			mItemRenderMappings.add(new Pair<Item, IItemRenderer>(aItem, aRenderer));
		}		
	}

}
