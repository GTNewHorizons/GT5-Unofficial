package gtPlusPlus.plugin.villagers;

import java.util.HashMap;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.plugin.villagers.block.BlockGenericSpawner;
import net.minecraft.util.ResourceLocation;

public class Core_VillagerAdditions implements IPlugin {

	public final static Core_VillagerAdditions mInstance;
	private static boolean shouldLoad = false;

	public static final HashMap<Integer, ResourceLocation> mVillagerSkins = new HashMap<Integer, ResourceLocation>();
	public static final AutoMap<Pair<Integer, IVillageTradeHandler>> mVillagerTrades = new AutoMap<Pair<Integer, IVillageTradeHandler>>();

	static {
		mInstance = new Core_VillagerAdditions();
		Core_Manager.registerPlugin(mInstance);
		mInstance.log("Preparing "+mInstance.getPluginName()+" for use.");
	}

	@Override
	public boolean preInit() {
		if (/*CORE.ConfigSwitches.enableSulfuricAcidFix || */CORE.DEVENV) {
			shouldLoad = true;
		}
		if (shouldLoad) {			
			//Try register some test spawners
			Utils.createNewMobSpawner(0, EntityGiantChickenBase.class);
			Utils.createNewMobSpawner(1, EntitySickBlaze.class);
			Utils.createNewMobSpawner(2, EntityStaballoyConstruct.class);
			
			//Register all Villager ID's and their Custom Trades.
			if (mVillagerTrades.size() > 0) {
				for (Pair<Integer, IVillageTradeHandler> g : mVillagerTrades) {
					if (g != null && g.getKey() != null) {
						VillagerRegistry.instance().registerVillagerId(g.getKey());
						if (g.getValue() != null) {
							VillagerRegistry.instance().registerVillageTradeHandler(g.getKey(), g.getValue());
						}
						if (mVillagerSkins.get(g.getKey()) != null) {
							VillagerRegistry.instance().registerVillagerSkin(g.getKey(), mVillagerSkins.get(g.getKey()));
						}
					}
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean init() {
		if (shouldLoad) {
			ModBlocks.blockCustomMobSpawner = new BlockGenericSpawner();
			return true;
		}
		return false;
	}

	@Override
	public boolean postInit() {		
		if (shouldLoad) {
			return true;
		}
		return false;
	}

	@Override
	public String getPluginName() {
		return "GT++ Enhanced Villagers";
	}

	@Override
	public String getPluginAbbreviation() {
		return "Bank";
	}

}
