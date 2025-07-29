package gregtech.common.misc;

import static gregtech.api.enums.Mods.Baubles;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.gtnhlib.datastructs.space.ArrayProximityMap4D;
import com.gtnewhorizon.gtnhlib.datastructs.space.VolumeShape;

import baubles.api.BaublesApi;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.interfaces.tileentity.IWirelessCharger;

public class WirelessChargerManager {

    public static final int CHARGE_TICK = 20;
    private final ArrayProximityMap4D<IWirelessCharger> CHARGER_MAP = new ArrayProximityMap4D<>(VolumeShape.SPHERE);

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.side == Side.SERVER && event.phase == TickEvent.Phase.END) {
            if (CHARGER_MAP.isEmpty()) return;
            if (event.player.ticksExisted % CHARGE_TICK == 5) {
                final EntityPlayer player = event.player;
                CHARGER_MAP.forEachInRange(player.dimension, player.posX, player.posY, player.posZ, charger -> {
                    if (charger.canChargePlayerItems(player)) {
                        charger.chargePlayerItems(
                            player,
                            player.inventory.armorInventory,
                            player.inventory.mainInventory,
                            getBaublesItems(player));
                    }
                });
            }
        }
    }

    public void addCharger(@NotNull IWirelessCharger charger, int range) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP
            .put(charger, te.getWorld().provider.dimensionId, te.getXCoord(), te.getYCoord(), te.getZCoord(), range);
    }

    public void removeCharger(@NotNull IWirelessCharger charger) {
        final IGregTechTileEntity te = charger.getChargerTE();
        CHARGER_MAP.remove(te.getWorld().provider.dimensionId, te.getXCoord(), te.getYCoord(), te.getZCoord());
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
