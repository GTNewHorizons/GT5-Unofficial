package gtPlusPlus.plugin.villagers;

import static gtPlusPlus.plugin.villagers.VillagerUtils.mVillagerMap;

import java.util.HashMap;

import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.entity.EntityPrimedMiningExplosive;
import gtPlusPlus.core.entity.monster.EntityGiantChickenBase;
import gtPlusPlus.core.entity.monster.EntitySickBlaze;
import gtPlusPlus.core.entity.monster.EntityStaballoyConstruct;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.plugin.villagers.block.BlockGenericSpawner;
import gtPlusPlus.plugin.villagers.entity.EntityBaseVillager;
import gtPlusPlus.plugin.villagers.entity.EntityNativeAustralian;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerAboriginal;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerBanker;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerTechnician;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerTrader;
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
			//Register Custom Villager Entity
			EntityRegistry.registerGlobalEntityID(EntityBaseVillager.class, "WiseVillager", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(180, 120, 120), Utils.rgbtoHexValue(0, 0, 0));
			EntityRegistry.registerGlobalEntityID(EntityNativeAustralian.class, "Aboriginal", EntityRegistry.findGlobalUniqueEntityId(), Utils.rgbtoHexValue(50, 50, 50), Utils.rgbtoHexValue(25, 25, 25));
	        VillagerUtils.registerNewVillager(0, "Banker", "Banker", "Banker", "banker", new TradeHandlerBanker());
			VillagerUtils.registerNewVillager(1, "Technician", "Technician", "Technician", "technician", new TradeHandlerTechnician());
			VillagerUtils.registerNewVillager(2, "Trader", "Trader", "Trader", "trader", new TradeHandlerTrader());
			VillagerUtils.registerNewVillager(3, "Aboriginal", "Aboriginal", "Aboriginal", "aboriginal", new TradeHandlerAboriginal());
			
			if (mVillagerMap.size() > 0) {
				for (VillagerObject g : mVillagerMap.values()) {
					if (g != null && g.mID >= 0) {
						VillagerRegistry.instance().registerVillagerId(7735+g.mID);	
						log("Registered a Custom Villager with ID of "+g.mID+".");
						//Utils.createNewMobSpawner(10+g.mID, EntityBaseVillager.class);
						if (mVillagerSkins.get(g.mID) != null) {
							VillagerRegistry.instance().registerVillagerSkin(7735+g.mID, mVillagerSkins.get(g.mID));
							log("Registered a Custom Skin for Villager with ID of "+g.mID+".");
						}
					}
				}
			}			
			
			//Register all Villager ID's and their Custom Trades.
			if (mVillagerTrades.size() > 0) {
				for (Pair<Integer, IVillageTradeHandler> g : mVillagerTrades) {
					if (g != null && g.getKey() != null) {
						if (g.getValue() != null) {
							VillagerRegistry.instance().registerVillageTradeHandler(g.getKey(), g.getValue());
							log("Registered a Custom Trade for Villager with ID of "+g.getKey()+".");
						}
					}
				}
			}	
			return true;
		}
		return false;
	}

	@Override
	public boolean init() {
		if (shouldLoad) {
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
	public boolean serverStart() {
		if (shouldLoad) {
			return true;
		}
		return false;
	}

	@Override
	public boolean serverStop() {
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
