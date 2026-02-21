package gregtech.api.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.util.GTUtility.addSeparatorIfNeeded;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GTValues;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.util.GTUtility;
import gregtech.common.misc.NoTooltipElectricItemManager;
import ic2.api.item.IElectricItemManager;
import ic2.api.item.ISpecialElectricItem;

public class ItemAugmentCore extends ItemAugmentAbstract implements ISpecialElectricItem {

    public final Cores core;

    public ItemAugmentCore(Cores core) {
        super(core);
        this.core = core;
        this.maxStackSize = 1;
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return super.getRarity(p_77613_1_);
    }

    @Override
    protected boolean showAllInfo() {
        // Don't require holding shift to display all info because cores don't have many lines
        return true;
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        desc.add(
            GRAY + GTUtility
                .translate("GT5U.armor.tooltip.energycore", core.getRarity().rarityColor.toString() + core.getTier()));

        addSeparatorIfNeeded(desc);

        super.addAdditionalToolTips(desc, augmentStack, player);

        addSeparatorIfNeeded(desc);

        NBTTagCompound tag = augmentStack.getTagCompound();
        if (tag == null) tag = new NBTTagCompound();

        boolean infinite = this.core == Cores.Singularity;
        String stored = infinite ? "∞" : formatNumber(Math.round(tag.getDouble("charge")));
        String capacity = infinite ? "∞" : formatNumber(core.getChargeMax());
        String voltage = formatNumber(GTValues.V[core.getChargeTier()]);

        addSeparatorIfNeeded(desc);

        desc.add(
            EnumChatFormatting.AQUA + GTUtility.translate("item.itemBaseEuItem.tooltip.3", stored, capacity, voltage));

        addSeparatorIfNeeded(desc);
    }

    @Override
    public IElectricItemManager getManager(ItemStack itemStack) {
        return NoTooltipElectricItemManager.INSTANCE;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public final Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public final Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return core.getChargeMax();
    }

    @Override
    public int getTier(ItemStack aStack) {
        return core.getChargeTier();
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return Math.pow(2, 2 * core.getChargeTier() + 3);
    }
}
