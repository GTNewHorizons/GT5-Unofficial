package gregtech.client;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeItem;
import com.ruling_0.materiallib.api.StackMaterialInfo;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialFormulas;
import gregtech.api.material.MaterialUtils;
import gregtech.common.config.Client;

/// Adds material tooltip lines to [ShapeItem] and [ShapeBlock.ShapeBlockItem] stacks. The chemical formula and
/// flavor text go directly below the display name, and the hot-ingot and hazard warnings go at the end.
///
/// Each line is gated by its `Client.tooltip` toggle.
public class MaterialFormulaTooltip {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        if (stack == null) return;
        Item item = stack.getItem();
        if (!(item instanceof ShapeItem) && !(item instanceof ShapeBlock.ShapeBlockItem)) return;
        StackMaterialInfo info = MaterialLibAPI.lookupStack(stack);
        if (info == null) return;

        Material material = info.material();
        List<String> tooltip = event.toolTip;
        int insertAt = Math.min(1, tooltip.size());
        if (Client.tooltip.showFormula) {
            String line = MaterialFormulas.forTooltip(material);
            if (line != null) tooltip.add(insertAt++, line);
        }
        if (Client.tooltip.showFlavorText) {
            String flavor = MaterialUtils.flavorText(material);
            if (flavor != null) {
                tooltip.add(
                    Math.min(insertAt, tooltip.size()),
                    EnumChatFormatting.DARK_GRAY.toString() + EnumChatFormatting.ITALIC + flavor);
            }
        }
        if (item == Shapes.ingotHot && Client.tooltip.showHotIngotText) {
            tooltip.add(StatCollector.translateToLocal("gtpp.tooltip.ingot.very_hot"));
        }
        MaterialUtils.addHazardTooltips(material, tooltip);
    }
}
