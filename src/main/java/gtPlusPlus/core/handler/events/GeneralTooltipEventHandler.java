package gtPlusPlus.core.handler.events;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import advsolar.common.AdvancedSolarPanel;
import cpw.mods.fml.common.Optional;
import gregtech.api.enums.Mods;

public class GeneralTooltipEventHandler {

    @Optional.Method(modid = Mods.Names.ADVANCED_SOLAR_PANEL)
    @SuppressWarnings("unused")
    public static void molecularTransformer(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == Item.getItemFromBlock(AdvancedSolarPanel.blockMolecularTransformer)) {
            event.toolTip.add("" + EnumChatFormatting.RED + "Disabled, Use the multiblock");
        }
    }
}
