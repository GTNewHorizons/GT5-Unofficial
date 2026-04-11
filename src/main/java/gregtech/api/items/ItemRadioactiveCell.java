package gregtech.api.items;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import gregtech.common.items.ItemDepletedCell;
import ic2.api.item.IBoxable;
import ic2.core.util.StackUtil;

public class ItemRadioactiveCell extends GTGenericItem implements IBoxable {

    protected int cellCount;
    protected int maxDmg;
    protected int dura;

    public ItemRadioactiveCell(String aUnlocalized, String aEnglish, int aCellcount) {
        super(aUnlocalized, aEnglish, null);
        this.setMaxStackSize(64);
        this.setMaxDamage(100);
        setNoRepair();
        this.cellCount = Math.max(1, aCellcount);
    }

    public static int getDurabilityOfStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        return tNBT.getInteger("advDmg");
    }

    protected static int triangularNumber(int x) {
        return (x * x + x) / 2;
    }

    @Override
    public boolean isBookEnchantable(ItemStack ingredient, ItemStack bookEnchant) {
        return false;
    }

    public void setDamageForStack(ItemStack stack, int advDmg) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        nbtData.setInteger("advDmg", advDmg);
        if (this.maxDmg > 0) {
            double p = (double) advDmg / (double) this.maxDmg;
            int newDmg = (int) (stack.getMaxDamage() * p);
            if (newDmg >= stack.getMaxDamage()) {
                newDmg = stack.getMaxDamage() - 1;
            }
            stack.setItemDamage(newDmg);
            this.dura = newDmg;
        }
    }

    public int getDamageOfStack(ItemStack stack) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        this.dura = nbtData.getInteger("advDmg");
        return this.dura;
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
