package gregtech.api.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import gregtech.common.items.ItemDepletedCell;
import ic2.api.item.IBoxable;

public class ItemRadioactiveCell extends GTGenericItem implements IBoxable {

    protected int cellCount;
    protected int maxDmg;

    public ItemRadioactiveCell(String aUnlocalized, String aEnglish, int aCellcount) {
        super(aUnlocalized, aEnglish, null);
        this.setMaxStackSize(64);
        this.setMaxDamage(100);
        setNoRepair();
        this.cellCount = Math.max(1, aCellcount);
    }

    public static int getDurabilityOfStack(ItemStack aStack) {
        return ItemStackNBT.getInteger(aStack, "advDmg");
    }

    protected static int sumUp(int a) {
        int b = 0;
        for (int c = 1; c <= a; c++) {
            b += c;
        }
        return b;
    }

    protected static int triangularNumber(int x) {
        return (x * x + x) / 2;
    }

    protected boolean outputPulseForStack(ItemStack aStack) {
        final int output = ItemStackNBT.getInteger(aStack, "output");
        ItemStackNBT.setInteger(aStack, "output", output + 1);
        return false; // (this.pulserate > 0) || (tNBT.getInteger("output") % -this.pulserate == 0);
    }

    protected boolean incrementPulseForStack(ItemStack aStack) {
        final int pulse = ItemStackNBT.getInteger(aStack, "pulse");
        ItemStackNBT.setInteger(aStack, "pulse", pulse + 1);
        return false; // (this.pulserate > 0) || (tNBT.getInteger("pulse") % -this.pulserate == 0);
    }

    protected void setDurabilityForStack(ItemStack aStack, int aDurability) {
        ItemStackNBT.setInteger(aStack, "durability", aDurability);
    }

    public int getMaxNuclearDurability() {
        return 0; // return this.maxDelay;
    }

    public int func_77619_b() {
        return 0;
    }

    @Override
    public boolean isBookEnchantable(ItemStack ingredient, ItemStack bookEnchant) {
        return false;
    }

    // getIsRepairable
    public boolean func_82789_a(ItemStack toBeRepaired, ItemStack repairWith) {
        return false;
    }

    public void setDamageForStack(ItemStack stack, int advDmg) {
        ItemStackNBT.setInteger(stack, "advDmg", advDmg);
        if (this.maxDmg > 0) {
            double p = (double) advDmg / (double) this.maxDmg;
            int newDmg = (int) (stack.getMaxDamage() * p);
            if (newDmg >= stack.getMaxDamage()) {
                newDmg = stack.getMaxDamage() - 1;
            }
            stack.setItemDamage(newDmg);
        }
    }

    public int getDamageOfStack(ItemStack stack) {
        return ItemStackNBT.getInteger(stack, "advDmg");
    }

    public int getControlTagOfStack(ItemStack stack) {
        return ItemStackNBT.getInteger(stack, "tag");
    }

    public void setControlTagOfStack(ItemStack stack, int tag) {
        ItemStackNBT.setInteger(stack, "tag", tag);
    }

    public int getMaxDamageEx() {
        return this.maxDmg;
    }

    public void damageItemStack(ItemStack stack, int Dmg) {
        setDamageForStack(stack, getDamageOfStack(stack) + Dmg);
    }

    @Override
    public void addAdditionalToolTips(List<String> aList, ItemStack aStack, EntityPlayer aPlayer) {
        super.addAdditionalToolTips(aList, aStack, aPlayer);
        // aList.add("Time left: " + (this.maxDelay - getDurabilityOfStack(aStack)) + " secs");
        int rDmg = getDurabilityOfStack(aStack) * 6 / this.maxDmg;
        EnumChatFormatting color2 = switch (rDmg) {
            case 0, 1 -> EnumChatFormatting.WHITE;
            case 2, 3, 4 -> EnumChatFormatting.GRAY;
            default -> EnumChatFormatting.DARK_GRAY;
        };
        EnumChatFormatting color1 = this instanceof ItemDepletedCell ? color2 = EnumChatFormatting.DARK_GRAY
            : EnumChatFormatting.WHITE;
        aList.add(
            color1 + translateToLocalFormatted(
                "gt.item.desc.durability",
                color2 + formatNumber(this.maxDmg - getDurabilityOfStack(aStack)) + color1,
                formatNumber(this.maxDmg)));
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }
}
