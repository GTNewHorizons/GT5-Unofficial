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
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessCharger;

public class WirelessChargerManager {

    public static final int CHARGE_TICK = 20;
    private final Map<Long, IWirelessCharger> CHARGER_MAP = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            if (!CHARGER_MAP.isEmpty() && event.player.ticksExisted % CHARGE_TICK == 5) {
                chargePlayerItems(event.player);
            }
        }
    }

    public void addCharger(@NotNull IWirelessCharger charger) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP.put(CoordinatePacker.pack(te.getXCoord(), te.getYCoord(), te.getZCoord()), charger);
    }

    public IWirelessCharger getCharger(int x, int y, int z) {
        return CHARGER_MAP.get(CoordinatePacker.pack(x, y, z));
    }

    public void removeCharger(@NotNull IWirelessCharger charger) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP.remove(CoordinatePacker.pack(te.getXCoord(), te.getYCoord(), te.getZCoord()));
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
