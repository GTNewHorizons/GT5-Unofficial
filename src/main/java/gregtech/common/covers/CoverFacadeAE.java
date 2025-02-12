package gregtech.common.covers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import appeng.facade.IFacadeItem;
import gregtech.api.covers.CoverContext;

public class CoverFacadeAE extends CoverFacadeBase {

    public CoverFacadeAE(CoverContext context) {
        super(context);
    }

    @Override
    protected Block getTargetBlock(ItemStack aFacadeStack) {
        if (aFacadeStack == null) return null;
        final Item item = aFacadeStack.getItem();
        if (!(item instanceof IFacadeItem)) return null;
        return ((IFacadeItem) item).getBlock(aFacadeStack);
    }

    @Override
    protected int getTargetMeta(ItemStack aFacadeStack) {
        if (aFacadeStack == null) return 0;
        final Item item = aFacadeStack.getItem();
        if (!(item instanceof IFacadeItem)) return 0;
        return ((IFacadeItem) item).getMeta(aFacadeStack);
    }

    @Override
    public ItemStack getDisplayStack() {
        return coverData.mStack;
    }
}
