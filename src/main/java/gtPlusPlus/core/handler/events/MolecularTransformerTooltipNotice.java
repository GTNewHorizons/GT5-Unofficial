package gtPlusPlus.core.handler.events;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import advsolar.common.AdvancedSolarPanel;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class MolecularTransformerTooltipNotice {

    @SubscribeEvent
    public void molecularTransformer(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == Item.getItemFromBlock(AdvancedSolarPanel.blockMolecularTransformer)) {
            event.toolTip.add(EnumChatFormatting.RED + "Disabled, use the multiblock");
        }
    }
}
