package detrav.items.tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import gregtech.common.items.tools.IElectricToolItem;
import gregtech.common.items.tools.ToolElectricStorage;

/**
 * A standalone Electric Prospector's Scanner: right-click air to scan everything around the player into the map GUI,
 * sneak right-click to pick what it looks for, or right-click a block for the single-chunk reading the hand scanner
 * gives.
 * <p/>
 * Extends {@link DetravProspectorItem} for the shared tooltip and takes its energy handling from
 * {@link ToolElectricStorage} through {@link IElectricToolItem}, since Java will not let it extend
 * {@code ToolElectricItemBase} as well. Like the other migrated electric tools it runs on EU alone: the old version
 * kept a durability bar too, but only spent it on one action in twenty-five, and the energy cost per action is
 * unchanged.
 */
public class DetravElectricProspectorItem extends DetravProspectorItem implements IElectricToolItem {

    /** Energy one scanned chunk costs, which is what it cost before the durability rework too. */
    public static final long EU_PER_USE = 100;

    private final ToolElectricStorage electricStorage;

    public DetravElectricProspectorItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        int legacyMeta, long maxCharge, long voltage, int tier) {
        super(unlocalizedName, toolStats, englishNameFormat, legacyMeta, ToolDictNames.craftingToolElectricProspector);
        this.electricStorage = new ToolElectricStorage(maxCharge, voltage, tier);
    }

    @Override
    public ToolElectricStorage getElectricStorage() {
        return electricStorage;
    }

    @Override
    public Item getChargedItem(ItemStack stack) {
        return this;
    }

    @Override
    public Item getEmptyItem(ItemStack stack) {
        return this;
    }

    @Override
    public boolean getShareTag() {
        // The charge has to reach the client, or the bar and the tooltip would always read empty there.
        return true;
    }

    /* ---------- ENERGY INSTEAD OF DURABILITY ---------- */

    @Override
    public long getStoredDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public long getMaxStoredDamage(ItemStack stack) {
        return 0;
    }

    @Override
    public long getStoredCharge(ItemStack stack) {
        return electricStorage.getCharge(stack);
    }

    @Override
    public long getMaxStoredCharge(ItemStack stack) {
        return electricStorage.getMaxCharge(stack);
    }

    @Override
    public boolean doDamage(ItemStack stack, long amount) {
        return use(stack, amount, null);
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

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
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
    protected int getScanRange(ItemStack stack) {
        return 2 * getScanRadius(stack) + 1;
    }

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        if (getToolMaterial(stack) == Materials._NULL) return;
        electricStorage.addChargeToolTip(list, stack);
        addScannerToolTips(list, stack);
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.2"));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.3"));
        list.add(EnumChatFormatting.ITALIC + StatCollector.translateToLocal("tooltip.detrav.scanner.usage.4"));
    }

    /**
     * Hands out fully charged scanners, so that one spawned from NEI or the creative tab is usable straight away
     * rather than being a flat battery.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        final int firstAdded = list.size();
        super.getSubItems(item, creativeTab, list);
        for (int i = firstAdded; i < list.size(); i++) electricStorage.fillToFull((ItemStack) list.get(i));
    }
}
