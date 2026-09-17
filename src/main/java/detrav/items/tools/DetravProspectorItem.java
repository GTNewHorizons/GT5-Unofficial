package detrav.items.tools;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import detrav.DetravScannerMod;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import gregtech.common.items.tools.ToolItemBase;

/**
 * A standalone hand Prospector's Scanner: right-click rock to survey the chunks around it, or bedrock to sample the
 * fluid below.
 * <p/>
 * One instance of this class is registered per tier, LV through UHV. The electric tiers use
 * {@link DetravElectricProspectorItem}, which stores energy instead of durability. See {@link ToolItemBase} for
 * everything these have in common with the other standalone tools.
 */
public class DetravProspectorItem extends ToolItemBase {

    /** Durability cost of scanning one chunk, in the unit where 100 is one durability point. */
    public static final int SCAN_COST = 15;

    /**
     * The metadata this tier held on {@code detrav.metatool.01}. Range and success chance were read off it, and
     * metadata is the crafting material now, so the tier keeps the old number for those two sums to use.
     */
    protected final int legacyMeta;

    public DetravProspectorItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        int legacyMeta) {
        this(unlocalizedName, toolStats, englishNameFormat, legacyMeta, ToolDictNames.craftingToolProspector);
    }

    protected DetravProspectorItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        int legacyMeta, ToolDictNames oreDictName) {
        super(unlocalizedName, toolStats, englishNameFormat, "", null, oreDictName);
        this.legacyMeta = legacyMeta;
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return new DetravProspectorActions(SCAN_COST, legacyMeta).onItemUse(this, stack, player, world, x, y, z);
    }

    /* ---------- DISPLAY ---------- */

    /**
     * How many chunks across this scanner reads, which depends on the material as well as the tier: a better material
     * raises the harvest level, and the harvest level is half of the range.
     */
    protected int getScanRange(ItemStack stack) {
        int range = getHarvestLevel(stack, "") / 2 + (legacyMeta / 4);
        if ((range % 2) == 0) {
            range += 1;
        }
        return range;
    }

    /**
     * The chance of any one chunk being read successfully, in percent. Only the hand scanner rolls for this; the
     * electric one reads every chunk it reaches.
     */
    protected int getSuccessChance() {
        return Math.min(((1 + legacyMeta) * 8), 100);
    }

    /**
     * The scanner has a tooltip of its own -- range, success chance and what the clicks do -- in place of the mining
     * numbers the other tools show, none of which mean anything for a tool that never breaks a block.
     */
    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        Materials material = getToolMaterial(stack);
        if (material == Materials._NULL) return;
        long maxDamage = getMaxStoredDamage(stack);
        if (maxDamage > 0) {
            list.add(
                EnumChatFormatting.GREEN + StatCollector.translateToLocalFormatted(
                    "tooltip.detrav.scanner.durability",
                    formatNumber(maxDamage - getStoredDamage(stack)),
                    formatNumber(maxDamage)));
        }
        addScannerToolTips(list, stack);
        // Only the hand scanner can come up empty on a chunk, and only its findings are reported by distance.
        list.add(
            EnumChatFormatting.GRAY + ""
                + EnumChatFormatting.ITALIC
                + StatCollector.translateToLocalFormatted(
                    "tooltip.detrav.scanner.success.chance",
                    EnumChatFormatting.RESET + formatNumber(getSuccessChance())));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.0"));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.distance.1"));
    }

    /** The lines shared by both scanner families, after whatever each shows for its durability or charge. */
    protected void addScannerToolTips(List<String> list, ItemStack stack) {
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
