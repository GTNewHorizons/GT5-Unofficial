package gregtech.common.misc;

import static gregtech.api.enums.Mods.Baubles;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessCharger;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public class WirelessChargerManager {

    public static final int CHARGE_TICK = 20;
    private static final Map<Long, IWirelessCharger> CHARGER_MAP = new HashMap<>();
    private int tickCounter = 0;

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (!CHARGER_MAP.isEmpty() && ++tickCounter % CHARGE_TICK == 0) {
                for (EntityPlayer player : PlayerUtils.getOnlinePlayers()) {
                    chargePlayerItems(player);
                }
            }
        }
    }

    public static void addCharger(@NotNull IWirelessCharger charger) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP.put(CoordinatePacker.pack(te.getXCoord(), te.getYCoord(), te.getZCoord()), charger);
    }

    public static IWirelessCharger getCharger(int x, int y, int z) {
        return CHARGER_MAP.get(CoordinatePacker.pack(x, y, z));
    }

    public static void removeCharger(@NotNull IWirelessCharger charger) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP.remove(CoordinatePacker.pack(te.getXCoord(), te.getYCoord(), te.getZCoord()));
    }

    public static void clearChargerMap() {
        CHARGER_MAP.clear();
    }

    private void chargePlayerItems(@NotNull EntityPlayer player) {
        ItemStack[] baubleItems = null;
        boolean checkedBaubles = false;
        for (IWirelessCharger charger : CHARGER_MAP.values()) {
            if (charger.canChargePlayerItems(player)) {
                if (!checkedBaubles) {
                    baubleItems = getBaublesItems(player);
                    checkedBaubles = true;
                }
                charger.chargePlayerItems(
                    player,
                    player.inventory.armorInventory,
                    player.inventory.mainInventory,
                    baubleItems);
            }
        }
    }

    private static ItemStack[] getBaublesItems(@NotNull EntityPlayer player) {
        ItemStack[] baubleItems = null;
        if (Baubles.isModLoaded()) {
            IInventory baubleInv = BaublesApi.getBaubles(player);
            if (baubleInv != null) {
                baubleItems = new ItemStack[baubleInv.getSizeInventory()];
                for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
                    baubleItems[i] = baubleInv.getStackInSlot(i);
                }
            }
        }
        return baubleItems;
    }
}
