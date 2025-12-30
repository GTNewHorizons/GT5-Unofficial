package gregtech.api.util;

import static gregtech.api.util.GTUtility.getColoredTierNameFromTier;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.common.misc.GTStructureChannels;

public class CasingTier {

    private static final HashMap<Pair<Block, Integer>, Pair<Integer, Integer>> casingToTierAndIndex = new HashMap<>();
    private static final Pair<Integer, Integer> defaultCasingTier = Pair.of(null, 0);
    private static final List<Pair<Block, Integer>> casingList = new ArrayList<>();

    @Nullable
    public static Integer getCasingBlockTier(Block block, int meta) {
        return casingToTierAndIndex.getOrDefault(Pair.of(block, meta), defaultCasingTier)
            .getLeft();
    }

    public static List<Pair<Block, Integer>> getCasingList() {
        return casingList;
    }

    public static void addCasing(@NotNull Block block, int meta, int tier) {
        Objects.requireNonNull(block, "Casing block cannot be null");
        casingToTierAndIndex.put(Pair.of(block, meta), Pair.of(tier, -1));
        casingList.add(Pair.of(block, meta));

        GTStructureChannels.TIER_CASING.registerAsIndicator(new ItemStack(block, 1, meta), meta + 1);
    }

    // Register all your casings here.
    public static class RegisterCasingTiers {

        public static void run() {
            for (int meta = 0; meta < 10; ++meta) { // from ulv to uhv
                addCasing(GregTechAPI.sBlockCasings1, meta, meta + 1);
            }
            for (int meta = 10; meta < 15; ++meta) { // from uhv to max
                addCasing(GregTechAPI.sBlockCasingsNH, meta, meta + 1);
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public static class GlassTooltipHandler {

        @SideOnly(Side.CLIENT)
        @SubscribeEvent(priority = EventPriority.HIGHEST)
        public void getTooltip(ItemTooltipEvent event) {

            if (event == null || event.itemStack == null || event.itemStack.getItem() == null) return;

            final Block block = Block.getBlockFromItem(event.itemStack.getItem());
            final int meta = event.itemStack.getItemDamage();

            Integer tier = getCasingBlockTier(block, meta);
            if (tier == null) return;

            event.toolTip.add(
                StatCollector.translateToLocal("tooltip.casing_tier.0.name") + " "
                    + getColoredTierNameFromTier(tier.byteValue()));
        }
    }
}
