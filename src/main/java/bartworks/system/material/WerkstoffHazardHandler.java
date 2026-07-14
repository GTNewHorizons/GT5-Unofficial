package bartworks.system.material;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import gregtech.api.hazards.HazardProtection;
import gregtech.api.material.GTMaterialProperties;
import ic2.core.IC2Potion;

/// Applies the toxic/radioactive carry hazards of hazardous materials to MaterialLib items.
/// `BWMetaGeneratedItems#onUpdate` used to poison/irradiate the carrier of any toxic/radioactive werkstoff
/// item without the matching hazmat suit; the cut-over MaterialLib shape items are shared across all
/// materials, so the behavior moves to a player-tick inventory scan reading the canonical
/// [GTMaterialProperties#TOXIC]/[GTMaterialProperties#IS_RADIOACTIVE] properties instead of per-item
/// subclassing.
public class WerkstoffHazardHandler {

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.side.isClient()) return;
        EntityPlayer player = event.player;
        boolean toxic = false;
        boolean radioactive = false;
        for (ItemStack stack : player.inventory.mainInventory) {
            if (stack == null || !(stack.getItem() instanceof ShapeItem)) continue;
            Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
            if (material == null) continue;
            if (!toxic) toxic = Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.TOXIC));
            if (!radioactive)
                radioactive = Boolean.TRUE.equals(material.getProperty(GTMaterialProperties.IS_RADIOACTIVE));
            if (toxic && radioactive) break;
        }
        if (toxic && !HazardProtection.isWearingFullBioHazmat(player)) {
            player.addPotionEffect(new PotionEffect(Potion.poison.getId(), 80, 4));
        }
        if (radioactive && !HazardProtection.isWearingFullRadioHazmat(player)) {
            player.addPotionEffect(new PotionEffect(IC2Potion.radiation.id, 80, 4));
        }
    }
}
