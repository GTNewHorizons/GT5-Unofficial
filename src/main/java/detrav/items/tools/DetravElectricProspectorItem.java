package detrav.items.tools;

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
import gregtech.common.items.tools.ToolElectricItemBase;

/**
 * A standalone Electric Prospector's Scanner: right-click air to scan everything around the player into the map GUI,
 * sneak right-click to pick what it looks for, or right-click a block for the single-chunk reading the hand scanner
 * gives.
 * <p/>
 * Runs on EU alone, via {@link ToolElectricItemBase} -- the old version kept a durability bar too, but only spent it
 * on one action in twenty-five, and the energy cost per action is unchanged. This cannot extend
 * {@link DetravProspectorItem} for the shared tooltip the way the metadata-based version implied it should, because
 * it already has to extend {@code ToolElectricItemBase} for its energy model; the shared tooltip lines come from
 * {@link DetravScannerBehavior} instead, exactly the pattern
 * {@code gregtech.common.items.tools.WrenchBehavior} documents for the wrench/screwdriver/wire-cutter family.
 */
public class DetravElectricProspectorItem extends ToolElectricItemBase implements DetravScannerBehavior {

    /** Energy one scanned chunk costs, which is what it cost before the durability rework too. */
    public static final long EU_PER_USE = 100;

    /**
     * The metadata this tier held on {@code detrav.metatool.01}, kept only to build a {@link DetravProspectorActions}
     * with -- see {@link DetravProspectorItem#legacyMeta}. Range and success chance were read off it there; here it is
     * only ever passed straight through to {@link #actions()}.
     */
    private final int legacyMeta;

    public DetravElectricProspectorItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        int legacyMeta, long maxCharge, long voltage, int tier) {
        super(
            unlocalizedName,
            toolStats,
            englishNameFormat,
            "",
            maxCharge,
            voltage,
            tier,
            null,
            ToolDictNames.craftingToolElectricProspector);
        this.legacyMeta = legacyMeta;
        setCreativeTab(DetravScannerMod.TAB_DETRAV);
    }

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    /* ---------- USE ---------- */

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int ordinalSide,
        float hitX, float hitY, float hitZ) {
        if (getToolMaterial(stack) == Materials._NULL) return false;
        return actions().onItemUse(this, stack, player, world, x, y, z);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (getToolMaterial(stack) == Materials._NULL) return stack;
        // Telling the client the item was used is what plays the swing; the scan itself runs on the server.
        if (world.isRemote) return stack.copy();
        actions().onItemRightClick(this, stack, world, player);
        return stack;
    }

    private DetravElectricProspectorActions actions() {
        return new DetravElectricProspectorActions(legacyMeta);
    }

    /* ---------- SCAN AREA ---------- */

    /**
     * How far the area scan reaches: the chunk the player stands in plus this many chunks in every direction. This is
     * the one place it is defined -- the tooltip below reports what this returns, so the two cannot drift apart.
     */
    public int getScanRadius(ItemStack stack) {
        return getHarvestLevel(stack, "");
    }

    /**
     * The scanned grid, in chunks across. The hand scanner's sum does not apply here: it adds a quarter of the old
     * tool-type metadata, which was 100 or more for every electric tier, so it read about 25 chunks too wide. The
     * scan itself has always been the harvest level in each direction, and that is what this now reports.
     */
    @Override
    public int getScanRange(ItemStack stack) {
        return 2 * getScanRadius(stack) + 1;
    }

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        if (getToolMaterial(stack) == Materials._NULL) return;
        getElectricStorage().addChargeToolTip(list, stack);
        addScannerToolTips(list, stack);
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.2"));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.3"));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.4"));
    }
}
