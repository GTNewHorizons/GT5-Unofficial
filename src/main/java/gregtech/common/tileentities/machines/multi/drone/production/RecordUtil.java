package gregtech.common.tileentities.machines.multi.drone.production;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecordUtil {

    private static final long MASK_TYPE = 0xF000000000000000L;
    private static final long MASK_ID = 0x0000FFFF00000000L;
    private static final long MASK_META = 0x00000000FFFFFFFFL;

    private static final long TYPE_ITEM = 0x0000000000000000L;
    private static final long TYPE_FLUID = 0x1000000000000000L;
    private static final long TYPE_ENERGY = 0x2000000000000000L;

    public static long packItem(ItemStack stack) {
        if (stack == null || stack.getItem() == null) return -1;
        long id = Item.getIdFromItem(stack.getItem());
        long meta = stack.getItemDamage();
        return TYPE_ITEM | (id << 32) | meta;
    }

    public static long packFluid(FluidStack stack) {
        if (stack == null || stack.getFluid() == null) return -1;
        long id = stack.getFluidID();

        return TYPE_FLUID | (id << 32);
    }

    public static long packEnergy() {
        return TYPE_ENERGY;
    }

    public static boolean isItem(long key) {
        return (key & MASK_TYPE) == TYPE_ITEM;
    }

    public static boolean isFluid(long key) {
        return (key & MASK_TYPE) == TYPE_FLUID;
    }

    public static boolean isEnergy(long key) {
        return (key & MASK_TYPE) == TYPE_ENERGY;
    }

    public static int getId(long key) {
        return (int) ((key & MASK_ID) >>> 32);
    }

    public static int getMeta(long key) {
        return (int) (key & MASK_META);
    }
}
