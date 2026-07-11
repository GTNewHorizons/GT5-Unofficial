package gregtech.client;

import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.ShapeBlock;
import com.ruling_0.materiallib.api.ShapeItem;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.material.MaterialFormulas;
import gregtech.common.config.Client;

/// Renders the chemical-formula tooltip line on MaterialLib-served stacks (items, block item forms, and
/// fluid containers -- all backed by [ShapeItem] or [ShapeBlock.ShapeBlockItem]), restoring what the legacy
/// generated items drew in their own `addInformation` overrides. Implemented as an [ItemTooltipEvent] handler
/// reading declaration data through [MaterialFormulas] rather than baked MaterialLib tooltip lines, so the
/// `Client.tooltip.showFormula` config keeps gating the line at render time exactly as it gated every legacy
/// renderer, and localized formulas re-resolve on language switch. The line is inserted directly below the
/// display name, matching the legacy position (the formula was the first `addInformation` line on generated
/// material items).
public class Materials2FormulaTooltip {

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {
        if (!Client.tooltip.showFormula) return;
        ItemStack stack = event.itemStack;
        if (stack == null) return;
        Item item = stack.getItem();
        if (!(item instanceof ShapeItem) && !(item instanceof ShapeBlock.ShapeBlockItem)) return;

        String line = MaterialFormulas.forTooltip(MaterialLibAPI.getMaterialByIndex(stack.getItemDamage()));
        if (line == null) return;

        List<String> tooltip = event.toolTip;
        tooltip.add(Math.min(1, tooltip.size()), line);
    }
}
