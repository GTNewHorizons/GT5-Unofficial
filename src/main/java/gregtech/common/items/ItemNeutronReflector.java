package gregtech.common.items;

import net.minecraft.item.ItemStack;

import gregtech.api.items.GTGenericItem;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;

public class ItemNeutronReflector extends GTGenericItem implements IReactorComponent {

    public ItemNeutronReflector(String aUnlocalized, String aEnglish, int aMaxDamage) {
        super(aUnlocalized, aEnglish, "Undestructable");
        this.setMaxStackSize(64);
        this.setMaxDamage(aMaxDamage);
    }

    @Override
    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY,
        int pulseX, int pulseY, boolean heatrun) {
        if (!heatrun) {
            ((IReactorComponent) pulsingStack.getItem())
                .acceptUraniumPulse(reactor, pulsingStack, yourStack, pulseX, pulseY, youX, youY, heatrun);
        }
        return true;
    }

    @Override
    public boolean canStoreHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor aReactor, ItemStack aStack, int x, int y) {
        return 0;
    }

    @Override
    public float influenceExplosion(IReactor aReactor, ItemStack aStack) {
        return -1.0F;
    }

    @Override
    public int alterHeat(IReactor aReactor, ItemStack aStack, int x, int y, int aHeat) {
        return aHeat;
    }

    @Override
    public void processChamber(IReactor aReactor, ItemStack aStack, int x, int y, boolean aHeatRun) {}
}
