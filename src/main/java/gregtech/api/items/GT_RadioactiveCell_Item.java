package gregtech.api.items;

import static gregtech.api.util.GT_Utility.formatNumbers;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import gregtech.common.items.GT_DepletetCell_Item;
import ic2.api.item.IBoxable;
import ic2.core.util.StackUtil;

public class GT_RadioactiveCell_Item extends GT_Generic_Item implements IBoxable {

    protected int cellCount;
    protected int maxDmg;
    protected int dura;

    public GT_RadioactiveCell_Item(String aUnlocalized, String aEnglish, int aCellcount) {
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
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("output", tNBT.getInteger("output") + 1);
        return false; // (this.pulserate > 0) || (tNBT.getInteger("output") % -this.pulserate == 0);
    }

    protected boolean incrementPulseForStack(ItemStack aStack) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("pulse", tNBT.getInteger("pulse") + 1);
        return false; // (this.pulserate > 0) || (tNBT.getInteger("pulse") % -this.pulserate == 0);
    }

    protected void setDurabilityForStack(ItemStack aStack, int aDurability) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        if (tNBT == null) {
            tNBT = new NBTTagCompound();
            aStack.setTagCompound(tNBT);
        }
        tNBT.setInteger("durability", aDurability);
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

    public int getControlTagOfStack(ItemStack stack) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        return nbtData.getInteger("tag");
    }

    public void setControlTagOfStack(ItemStack stack, int tag) {
        NBTTagCompound nbtData = StackUtil.getOrCreateNbtData(stack);
        nbtData.setInteger("tag", tag);
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
        EnumChatFormatting color1 = this instanceof GT_DepletetCell_Item ? color2 = EnumChatFormatting.DARK_GRAY
                : EnumChatFormatting.WHITE;
        aList.add(
                color1 + String.format(
                        transItem("001", "Durability: %s/%s"),
                        "" + color2 + formatNumbers(this.maxDmg - getDurabilityOfStack(aStack)) + color1,
                        "" + formatNumbers(this.maxDmg)));
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemstack) {
        return true;
    }
}
