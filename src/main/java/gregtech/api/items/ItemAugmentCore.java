package gregtech.api.items;

import static gregtech.api.enums.GTValues.VN;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import gregtech.api.enums.GTValues;
import gregtech.api.items.armor.MechArmorAugmentRegistries.Cores;
import gregtech.api.util.GTUtility;
import ic2.api.item.IElectricItem;

public class ItemAugmentCore extends ItemAugmentAbstract implements IElectricItem {

    public final Cores core;

    public ItemAugmentCore(Cores core) {
        super(core);
        this.core = core;
        this.maxStackSize = 1;
    }

    @Override
    protected void addAdditionalToolTips(List<String> desc, ItemStack augmentStack, EntityPlayer player) {
        desc.add(GTUtility.translate("GT5U.armor.tooltip.energycore", core.getTier()));
        desc.add(
            GTUtility.translate(
                "GT5U.armor.tooltip.chargetier",
                GTValues.TIER_COLORS[core.getChargeTier()] + VN[core.getChargeTier()]));

        String energy = core == Cores.Singularity ? EnumChatFormatting.LIGHT_PURPLE + "Infinite" : EnumChatFormatting.YELLOW + GTUtility.formatNumbers(core.getChargeMax());

        desc.add(GTUtility.translate("GT5U.armor.tooltip.maxenergy", energy));
        super.addAdditionalToolTips(desc, augmentStack, player);
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
