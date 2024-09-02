package goodgenerator.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;

import ic2.api.item.IBoxable;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;

public class ItemDepletedFuelRod extends RadioactiveItem implements IReactorComponent, IBoxable {

    public ItemDepletedFuelRod(String name, String[] tooltip, CreativeTabs Tab, int Rad) {
        super(name, tooltip, Tab, Rad);
    }

    @Override
    public boolean canBeStoredInToolbox(ItemStack itemStack) {
        return true;
    }

    @Override
    public void processChamber(IReactor iReactor, ItemStack itemStack, int i, int i1, boolean b) {}

    @Override
    public boolean acceptUraniumPulse(IReactor iReactor, ItemStack itemStack, ItemStack itemStack1, int i, int i1,
        int i2, int i3, boolean b) {
        return false;
    }

    @Override
    public boolean canStoreHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor iReactor, ItemStack itemStack, int i, int i1) {
        return 0;
    }

    @Override
    public int alterHeat(IReactor iReactor, ItemStack itemStack, int i, int i1, int i2) {
        return 0;
    }

    @Override
    public float influenceExplosion(IReactor iReactor, ItemStack itemStack) {
        return 0;
    }
}
