package gregtech.api.covers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GTUtility;

public final class CoverContext {

    private final ItemStack coverItem;
    private final ForgeDirection side;
    private final ICoverable coverable;

    public CoverContext(ItemStack coverItem, ForgeDirection side, ICoverable coverable) {
        this.side = side;
        this.coverable = coverable;
        this.coverItem = coverItem;
    }

    public int getCoverId() {
        return GTUtility.stackToInt(coverItem);
    }

    public ItemStack getCoverInitializer() {
        return coverItem;
    }

    public ForgeDirection getSide() {
        return side;
    }

    public ICoverable getCoverable() {
        return coverable;
    }
}
