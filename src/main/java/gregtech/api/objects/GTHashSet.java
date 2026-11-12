package gregtech.api.objects;

import java.util.HashSet;

import net.minecraft.item.ItemStack;

import gregtech.api.util.GTUtility;

public class GTHashSet extends HashSet<GTItemStack> {

    public final boolean add(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return false;
        return super.add(new GTItemStack(stack));
    }
}
