package gregtech.common.misc;

import static gregtech.api.enums.Mods.Baubles;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

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

    @SubscribeEvent()
    public void onServerTick(TickEvent.ServerTickEvent event) {
        if (event.phase == TickEvent.Phase.END) {
            if (++tickCounter % CHARGE_TICK == 0) {
                for (EntityPlayer player : PlayerUtils.getOnlinePlayers()) {
                    chargePlayerItems(player);
                }
            }
        }
    }

    public static void addCharger(@NotNull IWirelessCharger charger) {
        CHARGER_MAP.put(
            CoordinatePacker.pack(
                charger.getChargerTE()
                    .getCoords()),
            charger);
    }

    public static IWirelessCharger getCharger(int x, int y, int z) {
        return CHARGER_MAP.get(CoordinatePacker.pack(x, y, z));
    }

    public static void removeCharger(@NotNull IWirelessCharger charger) {
        CHARGER_MAP.remove(
            CoordinatePacker.pack(
                charger.getChargerTE()
                    .getCoords()));
    }

    public static double calcDistance(@NotNull EntityPlayer player, @NotNull IGregTechTileEntity te) {
        final int x = MathHelper.floor_double(player.posX) - te.getXCoord();
        final int y = MathHelper.floor_double(player.boundingBox.minY) + 1 - te.getYCoord();
        final int z = MathHelper.floor_double(player.posZ) - te.getZCoord();

        return Math.sqrt(x * x + y * y + z * z);
    }

    private void chargePlayerItems(@NotNull EntityPlayer player) {
        ItemStack[] armourItems = player.inventory.armorInventory;
        ItemStack[] inventoryItems = player.inventory.mainInventory;
        ItemStack[] baubleItems = {};
        if (Baubles.isModLoaded()) {
            IInventory baubleInv = BaublesApi.getBaubles(player);
            if (baubleInv != null) {
                baubleItems = new ItemStack[baubleInv.getSizeInventory()];
                for (int i = 0; i < baubleInv.getSizeInventory(); i++) {
                    baubleItems[i] = baubleInv.getStackInSlot(i);
                }
            }
        }

        ItemStack[] stacks = Stream
            .concat(Stream.concat(Stream.of(armourItems), Stream.of(inventoryItems)), Stream.of(baubleItems))
            .toArray(ItemStack[]::new);

        for (IWirelessCharger charger : CHARGER_MAP.values()) {
            if (!charger.canChargePlayerItems(player)) continue;
            charger.chargePlayerItems(stacks, player);
        }
    }
}
