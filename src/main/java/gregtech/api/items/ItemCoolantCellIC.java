package gregtech.api.items;

import net.minecraft.item.ItemStack;

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;

public class ItemCoolantCellIC extends ItemCoolantCell implements IReactorComponent {

    public ItemCoolantCellIC(String aUnlocalized, String aEnglish, int aMaxStore) {
        super(aUnlocalized, aEnglish, aMaxStore);
    }

    @Override
    public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y, boolean aHeatRun) {}

    @Override
    public boolean acceptUraniumPulse(IReactor aReactor, ItemStack aStack, ItemStack pulsingStack, int youX, int youY,
        int pulseX, int pulseY, boolean aHeatRun) {
        return false;
    }

    @Override
    public boolean canStoreHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return !aReactor.isFluidCooled() || getControlTagOfStack(aStack) == 0;
    }

    @Override
    public int getMaxHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return this.heatStorage;
    }

    @Override
    public int getCurrentHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return getHeatOfStack(aStack);
    }

    @Override
    public float influenceExplosion(IReactor aReactor, ItemStack aStack) {
        return 1.0F + this.heatStorage / 30000.0F;
    }

    @Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        int tHeat = getHeatOfStack(aStack);
        if ((tHeat == 0) && (getControlTagOfStack(aStack) != 0)) {
            setControlTagOfStack(aStack, 0);
        }
        tHeat += aHeat;
        if (tHeat > this.heatStorage) {
            aReactor.setItemAt(x, y, null);
            aHeat = this.heatStorage - tHeat + 1;
        } else {
            if (tHeat < 0) {
                aHeat = tHeat;
                tHeat = 0;
            } else {
                aHeat = 0;
            }
            if ((tHeat > 0) && (getControlTagOfStack(aStack) == 0) && (!aReactor.isFluidCooled())) {
                setControlTagOfStack(aStack, 1);
            }
            setHeatForStack(aStack, tHeat);
        }
        return aHeat;
    }
}
