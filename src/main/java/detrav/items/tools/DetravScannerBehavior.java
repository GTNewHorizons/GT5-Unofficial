package detrav.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

/**
 * The tooltip lines shared by the hand and electric Prospector's Scanner -- range and the two usage hints -- in
 * place of the mining numbers other tools show, none of which mean anything for a tool that never breaks a block.
 * <p/>
 * {@link DetravProspectorItem} and {@link DetravElectricProspectorItem} cannot share this by one extending the
 * other: each already extends a different base for its durability or energy model, exactly the problem
 * {@code gregtech.common.items.tools.WrenchBehavior} documents for the wrench/screwdriver/wire-cutter/file family.
 * Each supplies its own {@link #getScanRange(ItemStack)} -- the hand and electric formulas have never agreed -- and
 * gets the shared lines below for free.
 */
public interface DetravScannerBehavior {

    /**
     * The scanned grid, in chunks across. Each implementation computes this its own way: the hand scanner from the
     * harvest level and the old tool-type metadata, the electric one from the harvest level alone.
     */
    int getScanRange(ItemStack stack);

    /** The lines shared by both scanner families, after whatever each shows for its durability or charge. */
    default void addScannerToolTips(List<String> list, ItemStack stack) {
        final int range = getScanRange(stack);
        list.add(
            EnumChatFormatting.WHITE + StatCollector
                .translateToLocalFormatted("tooltip.detrav.scanner.range", formatNumber(range), formatNumber(range)));
        list.add(
            EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.0")
                + EnumChatFormatting.GRAY);
        list.add(
            EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.1")
                + EnumChatFormatting.GRAY);
    }
}
