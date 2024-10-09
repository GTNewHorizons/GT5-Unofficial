package gtPlusPlus.core.handler.events;

import static gregtech.api.enums.Mods.Names.ADVANCED_SOLAR_PANEL;

import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.gtnewhorizon.gtnhlib.eventbus.EventBusSubscriber;

import advsolar.common.AdvancedSolarPanel;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;

@EventBusSubscriber(side = Side.CLIENT)
public class MolecularTransformerTooltipNotice {

    @SubscribeEvent
    @Optional.Method(modid = ADVANCED_SOLAR_PANEL)
    public static void molecularTransformer(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == Item.getItemFromBlock(AdvancedSolarPanel.blockMolecularTransformer)) {
            event.toolTip.add(EnumChatFormatting.RED + "Disabled, use the multiblock");
        }
    }
}
