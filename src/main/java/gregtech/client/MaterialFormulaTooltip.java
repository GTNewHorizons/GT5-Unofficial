package gregtech.client;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.material.MaterialFormulas;
import gregtech.api.material.MaterialUtils;
import gregtech.common.config.Client;

/// Renders the material tooltip lines on MaterialLib-served stacks (items, block item forms, and fluid
/// containers -- all backed by [ShapeItem] or [ShapeBlock.ShapeBlockItem]): the chemical formula directly below
/// the display name, then the hot-ingot and handling-hazard warnings at the end.
///
/// An [ItemTooltipEvent] handler reading declaration data through [MaterialFormulas] and [MaterialUtils] rather
/// than baked MaterialLib tooltip lines, so the `Client.tooltip` toggles gate each line at render time and
/// localized formulas re-resolve on language switch.
public class MaterialFormulaTooltip {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        ItemStack stack = event.itemStack;
        if (stack == null) return;
        Item item = stack.getItem();
        if (!(item instanceof ShapeItem) && !(item instanceof ShapeBlock.ShapeBlockItem)) return;

        Material material = MaterialLibAPI.getMaterialByIndex(stack.getItemDamage());
        List<String> tooltip = event.toolTip;
        if (Client.tooltip.showFormula) {
            String line = MaterialFormulas.forTooltip(material);
            if (line != null) tooltip.add(Math.min(1, tooltip.size()), line);
        }
        if (item == Shapes.ingotHot && Client.tooltip.showHotIngotText) {
            tooltip.add(StatCollector.translateToLocal("gtpp.tooltip.ingot.very_hot"));
        }
        MaterialUtils.addHazardTooltips(material, tooltip);
    }
}
