package gregtech.common.items.tools;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IToolStats;

/**
 * An electric screwdriver: LV, MV or HV.
 * <p/>
 * These run on EU only. The old metadata-based electric screwdrivers carried a durability bar as well, but only lost a
 * point of it on one action in twenty-five, so it was noise on top of the energy cost; the energy cost per action is
 * unchanged, since {@code GTModHandler.damageOrDechargeItem} always reached these through {@code IDamagableItem} and
 * so spent 100 units per use either way.
 */
public class ToolScrewdriverElectricItem extends ToolScrewdriverItem implements IElectricToolItem {

    /** Energy one action costs: an adjustment, which used to cost 100. */
    public static final long EU_PER_USE = 100;

    @Override
    public long getEnergyCostPerUse() {
        return EU_PER_USE;
    }

    private final ToolElectricStorage electricStorage;

    public ToolScrewdriverElectricItem(String unlocalizedName, IToolStats toolStats, String englishNameFormat,
        long maxCharge, long voltage, int tier) {
        super(unlocalizedName, toolStats, englishNameFormat);
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
     * Hands out fully charged screwdrivers, so that one spawned from NEI or the creative tab is usable straight away
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
