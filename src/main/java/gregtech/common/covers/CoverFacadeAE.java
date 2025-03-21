package gregtech.common.covers;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import appeng.facade.IFacadeItem;
import gregtech.api.covers.CoverContext;
import gregtech.api.covers.CoverPlacementPredicate;

public class CoverFacadeAE extends CoverFacadeBase {

    public static CoverPlacementPredicate isCoverPlaceable = CoverFacadeBase
        .isCoverPlaceable(CoverFacadeAE::getFacadeItemBlock, CoverFacadeAE::getFacadeItemMeta);

    public CoverFacadeAE(CoverContext context) {
        super(context);
    }

    public static Block getFacadeItemBlock(ItemStack aFacadeStack) {
        if (aFacadeStack == null) return null;
        final Item item = aFacadeStack.getItem();
        if (!(item instanceof IFacadeItem)) return null;
        return ((IFacadeItem) item).getBlock(aFacadeStack);
    }

    public static int getFacadeItemMeta(ItemStack aFacadeStack) {
        if (aFacadeStack == null) return 0;
        final Item item = aFacadeStack.getItem();
        if (!(item instanceof IFacadeItem)) return 0;
        return ((IFacadeItem) item).getMeta(aFacadeStack);
    }

    @Override
    public Block getTargetBlock(ItemStack aFacadeStack) {
        return getFacadeItemBlock(aFacadeStack);
    }

    @Override
    public int getTargetMeta(ItemStack aFacadeStack) {
        return getFacadeItemMeta(aFacadeStack);
    }
}
