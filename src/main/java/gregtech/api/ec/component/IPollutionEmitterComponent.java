package gregtech.api.ec.component;

import net.minecraft.item.ItemStack;

import gregtech.api.ec.ComponentKey;
import gregtech.api.ec.IComponent;

public interface IPollutionEmitterComponent extends IComponent {

    ComponentKey.ListKey<IPollutionEmitterComponent> KEY = ComponentKey.list();

    /**
     * Gets the pollution this Device outputs to a Muffler per tick (10000 = one Pullution Block)
     *
     * @param aStack what is in controller
     */
    default int getPollutionPerTick(ItemStack aStack) {
        return getPollutionPerSecond(aStack) / 20;
    }

    /**
     * Gets the pollution produced per second by this multiblock, default to 0. Override this with its actual value in
     * the code of the multiblock.
     *
     * This returns the unmodified raw pollution value, not the one after muffler discounts.
     *
     * @param aStack what is in controller
     */
    int getPollutionPerSecond(ItemStack aStack);
}
