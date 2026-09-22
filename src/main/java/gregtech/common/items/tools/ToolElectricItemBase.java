package gregtech.common.items.tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.enums.ToolDictNames;
import gregtech.api.interfaces.IToolStats;
import gregtech.api.objects.GTHashSet;

/**
 * A standalone electric tool's energy plumbing: durability reported as zero, charge delegated to
 * {@link ToolElectricStorage}, and the tooltip and creative-tab lines an electric tool needs that a tool which wears
 * out does not.
 * <p/>
 * Every electric tool type extends this directly now, including the ones with a hand version (wrench, screwdriver,
 * wire cutter, file, and the Detrav prospector). It used to be that only the tool types with no hand version at all
 * (drill, chainsaw, jackhammer, buzzsaw) could: a type with a hand version needed its electric class to extend the
 * hand class to pick up its behaviour and cross-mod interfaces, and Java's single inheritance meant it could not
 * also extend this, so it repeated this class's dozen members by hand instead. That behaviour and those interfaces
 * now come from a small per-type {@code *Behavior} interface (see {@link WrenchBehavior} for the shape of it and why
 * it works), composed into both the hand class and this one's subclass, which frees every electric tool to extend
 * this class uniformly.
 * <p/>
 * These run on EU only. The old metadata-based versions carried a durability bar as well, but only lost a point of it
 * on one action in twenty-five, so it was noise on top of the energy cost; the energy cost per action is unchanged,
 * since {@code GTModHandler.damageOrDechargeItem} always reached these through {@code IDamagableItem} and so spent
 * 100 units per use either way.
 */
public abstract class ToolElectricItemBase extends ToolItemBase implements IElectricToolItem {

    private final ToolElectricStorage electricStorage;

    protected ToolElectricItemBase(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        String englishTooltip, long maxCharge, long voltage, int tier, GTHashSet toolList,
        ToolDictNames... oreDictNames) {
        super(unlocalizedName, toolStats, englishNameFormat, englishTooltip, toolList, oreDictNames);
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

    /* ---------- DISPLAY ---------- */

    @Override
    protected void addAdditionalToolTips(List<String> list, ItemStack stack, EntityPlayer player) {
        if (getToolMaterial(stack) != Materials._NULL) electricStorage.addChargeToolTip(list, stack);
        super.addAdditionalToolTips(list, stack, player);
    }

    /**
     * Hands out fully charged tools, so that one spawned from NEI or the creative tab is usable straight away rather
     * than being a flat battery.
     */
    @Override
    @SideOnly(Side.CLIENT)
    public void getSubItems(Item item, CreativeTabs creativeTab, List list) {
        final int firstAdded = list.size();
        super.getSubItems(item, creativeTab, list);
        for (int i = firstAdded; i < list.size(); i++) electricStorage.fillToFull((ItemStack) list.get(i));
    }
}
