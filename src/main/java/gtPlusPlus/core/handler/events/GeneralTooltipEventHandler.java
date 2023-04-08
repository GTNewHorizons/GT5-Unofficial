package gtPlusPlus.core.handler.events;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import advsolar.common.AdvancedSolarPanel;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import gregtech.api.enums.Mods;
import gtPlusPlus.GTplusplus;
import gtPlusPlus.GTplusplus.INIT_PHASE;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.xmod.gregtech.api.enums.GregtechItemList;

public class GeneralTooltipEventHandler {

    ItemStack[] mGregtechTurbines = new ItemStack[6];
    String mTurbine;
    String mExtra;

    @SubscribeEvent
    public void onItemTooltip(ItemTooltipEvent event) {

        if (GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.STARTED
                && GTplusplus.CURRENT_LOAD_PHASE != INIT_PHASE.SERVER_START) {
            return;
        }
        if (event.itemStack == null) {
            return;
        }
        if (CORE.ConfigSwitches.chanceToDropFluoriteOre > 0) {
            if (BlockEventHandler.blockLimestone != null && !BlockEventHandler.blockLimestone.isEmpty()) {
                for (ItemStack h : BlockEventHandler.blockLimestone) {
                    if (h != null && Block.getBlockFromItem(h.getItem())
                            == Block.getBlockFromItem(event.itemStack.getItem())) {
                        if (ItemUtils.getModId(h) != null
                                && !ItemUtils.getModId(h).toLowerCase().contains("biomesoplenty")) {
                            event.toolTip.add("May contain Fluorite Ore");
                        }
                    }
                }
            }
            if (BlockEventHandler.oreLimestone != null && !BlockEventHandler.oreLimestone.isEmpty()) {
                for (ItemStack h : BlockEventHandler.oreLimestone) {
                    if (h != null && Block.getBlockFromItem(h.getItem())
                            == Block.getBlockFromItem(event.itemStack.getItem())) {
                        if (ItemUtils.getModId(h) != null
                                && !ItemUtils.getModId(h).toLowerCase().contains("biomesoplenty")) {
                            event.toolTip.add("May contain Fluorite Ore");
                        }
                    }
                }
            }
        }

        // Material Collector Tooltips
        if (ModBlocks.blockPooCollector != null
                && Block.getBlockFromItem(event.itemStack.getItem()) == ModBlocks.blockPooCollector) {
            // Normal
            if (event.itemStack.getItemDamage() == 0) {
                event.toolTip.add("Used to collect animal waste");
                event.toolTip.add("Collects in a 5x4x5 area starting at Y+1");
                event.toolTip.add("Use Hoppers/Pipes to empty");
                event.toolTip.add(EnumChatFormatting.GOLD + "Capacity: " + EnumChatFormatting.AQUA + "8000L");
            }
            // Advanced
            else {
                event.toolTip.add("Used to collect waste (Works on more than animals)");
                event.toolTip.add("Significantly faster than the simple version");
                event.toolTip.add("Collects in a 5x4x5 area starting at Y+1");
                event.toolTip.add("Use Hoppers/Pipes to empty");
                event.toolTip.add(EnumChatFormatting.GOLD + "Capacity: " + EnumChatFormatting.AQUA + "128000L");
            }
        }

        // Custom Flasks

        if (GregtechItemList.VOLUMETRIC_FLASK_8k.hasBeenSet()
                && event.itemStack.getItem() == GregtechItemList.VOLUMETRIC_FLASK_8k.getItem()) {
            event.toolTip.add("Max Capacity: " + MathUtils.formatNumbers(8000) + "L");
        }
        if (GregtechItemList.VOLUMETRIC_FLASK_32k.hasBeenSet()
                && event.itemStack.getItem() == GregtechItemList.VOLUMETRIC_FLASK_32k.getItem()) {
            event.toolTip.add("Max Capacity: " + MathUtils.formatNumbers(32000) + "L");
        }

        if (Mods.AdvancedSolarPanel.isModLoaded()) {
            molecularTransformer(event);
        }
    }

    @Optional.Method(modid = Mods.Names.ADVANCED_SOLAR_PANEL)
    public static void molecularTransformer(ItemTooltipEvent event) {
        if (event.itemStack.getItem() == Item.getItemFromBlock(AdvancedSolarPanel.blockMolecularTransformer)) {
            event.toolTip.add("" + EnumChatFormatting.RED + "Disabled, Use the multiblock");
        }
    }
}
