package gtPlusPlus.plugin.villagers;

import static gtPlusPlus.plugin.villagers.VillagerUtils.mVillagerMap;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;

import cpw.mods.fml.common.registry.VillagerRegistry;
import cpw.mods.fml.common.registry.VillagerRegistry.IVillageTradeHandler;
import gtPlusPlus.api.interfaces.IPlugin;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.plugin.manager.Core_Manager;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerBanker;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerTechnician;
import gtPlusPlus.plugin.villagers.trade.TradeHandlerTrader;

// Called by Core_Manager#veryEarlyInit
@SuppressWarnings("unused")
public class Core_VillagerAdditions implements IPlugin {

    public static final Core_VillagerAdditions mInstance;
    private static boolean shouldLoad = false;

    public static final HashMap<Integer, ResourceLocation> mVillagerSkins = new HashMap<>();
    public static final AutoMap<Pair<Integer, IVillageTradeHandler>> mVillagerTrades = new AutoMap<>();

    static {
        mInstance = new Core_VillagerAdditions();
        Core_Manager.registerPlugin(mInstance);
        mInstance.log("Preparing " + mInstance.getPluginName() + " for use.");
    }

    @Override
    public boolean preInit() {
        if (
        /* CORE.ConfigSwitches.enableSulfuricAcidFix || */ CORE.DEVENV) {
            shouldLoad = true;
        }
        if (shouldLoad) {
            // Register Custom Villager Entity
            VillagerUtils.registerNewVillager(0, "Banker", "Banker", "Banker", "banker", new TradeHandlerBanker());
            VillagerUtils.registerNewVillager(
                    1,
                    "Technician",
                    "Technician",
                    "Technician",
                    "technician",
                    new TradeHandlerTechnician());
            VillagerUtils.registerNewVillager(2, "Trader", "Trader", "Trader", "trader", new TradeHandlerTrader());

            if (mVillagerMap.size() > 0) {
                for (VillagerObject g : mVillagerMap.values()) {
                    if (g != null && g.mID >= 0) {
                        VillagerRegistry.instance().registerVillagerId(7735 + g.mID);
                        log("Registered a Custom Villager with ID of " + g.mID + ".");
                        // Utils.createNewMobSpawner(10+g.mID, EntityBaseVillager.class);
                        if (mVillagerSkins.get(g.mID) != null) {
                            VillagerRegistry.instance().registerVillagerSkin(7735 + g.mID, mVillagerSkins.get(g.mID));
                            log("Registered a Custom Skin for Villager with ID of " + g.mID + ".");
                        }
                    }
                }
            }

            // Register all Villager ID's and their Custom Trades.
            if (mVillagerTrades.size() > 0) {
                for (Pair<Integer, IVillageTradeHandler> g : mVillagerTrades) {
                    if (g != null && g.getKey() != null) {
                        if (g.getValue() != null) {
                            VillagerRegistry.instance().registerVillageTradeHandler(g.getKey(), g.getValue());
                            log("Registered a Custom Trade for Villager with ID of " + g.getKey() + ".");
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
        return shouldLoad;
    }

    @Override
    public boolean postInit() {
        return shouldLoad;
    }

    @Override
    public boolean serverStart() {
        return shouldLoad;
    }

    @Override
    public boolean serverStop() {
        return shouldLoad;
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
