package gtPlusPlus.core.handler.events;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gtPlusPlus.core.item.base.dusts.BaseItemDustEx.mCachedPileLinkages;

import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.StringUtils;

import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent;
import cpw.mods.fml.common.event.FMLMissingMappingsEvent.MissingMapping;
import gtPlusPlus.api.objects.Logger;

public class MissingMappingsEvent {

    @EventHandler
    public void onMissingMapping(FMLMissingMappingsEvent event) {
        for (MissingMapping mapping : event.get()) {
            boolean bool1 = mapping.name.contains(GTPlusPlus.ID);
            // Missing Blocks
            if (mapping.type == cpw.mods.fml.common.registry.GameRegistry.Type.BLOCK && bool1) {

                // Example
                // if(mapping.name.equals("PneumaticCraft:etchingAcid")) {
                // mapping.remap(Fluids.etchingAcid.getBlock());
                // }

            }

            // Missing Items
            if (mapping.type == cpw.mods.fml.common.registry.GameRegistry.Type.ITEM && bool1) {
                if (mapping.name.contains("miscutils:itemDustTiny")) {
                    ItemStack stack = null;
                    String missingItemString = StringUtils.remove(mapping.name, "miscutils:itemDustTiny");
                    missingItemString = StringUtils.prependIfMissing(missingItemString, "item.itemDust");
                    if (mCachedPileLinkages.containsKey(missingItemString)) {
                        // stack = ItemUtils.getSimpleStack(Item.itemRegistry.)
                        Logger.REFLECTION("Mapping Event Found Missing Item in the Pile Linkage Cache.");
                    }

                    // mapping.remap(stack.getItem());
                }
            }
        }
    }
}
