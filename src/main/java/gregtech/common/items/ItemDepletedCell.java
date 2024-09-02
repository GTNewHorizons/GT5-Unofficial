package gregtech.common.items;

import net.minecraft.item.ItemStack;

import gregtech.api.items.ItemRadioactiveCellIC;
import ic2.api.reactor.IReactor;

public class ItemDepletedCell extends ItemRadioactiveCellIC {

    public ItemDepletedCell(String aUnlocalized, String aEnglish, int aRadiation) {
        super(aUnlocalized, aEnglish, 1, 1, 0, aRadiation, 0, null, false);
    }

    @Override
    public void processChamber(IReactor paramIReactor, ItemStack paramItemStack, int paramInt1, int paramInt2,
        boolean paramBoolean) {}

    @Override
    public boolean acceptUraniumPulse(IReactor paramIReactor, ItemStack paramItemStack1, ItemStack paramItemStack2,
        int paramInt1, int paramInt2, int paramInt3, int paramInt4, boolean paramBoolean) {
        return false;
    }

    @Override
    public boolean canStoreHeat(IReactor paramIReactor, ItemStack paramItemStack, int paramInt1, int paramInt2) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor paramIReactor, ItemStack paramItemStack, int paramInt1, int paramInt2) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor paramIReactor, ItemStack paramItemStack, int paramInt1, int paramInt2) {
        return 0;
    }

    @Override
    public int alterHeat(IReactor paramIReactor, ItemStack paramItemStack, int paramInt1, int paramInt2,
        int paramInt3) {
        return 0;
    }

    @Override
    public float influenceExplosion(IReactor paramIReactor, ItemStack paramItemStack) {
        return 0.0F;
    }
}
