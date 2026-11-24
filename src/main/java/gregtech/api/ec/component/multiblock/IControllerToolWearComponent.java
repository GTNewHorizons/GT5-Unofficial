package gregtech.api.ec.component.multiblock;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;

public interface IControllerToolWearComponent extends IControllerPartComponent {

    ComponentKey.ListKey<IControllerToolWearComponent> KEY = ComponentKey.list();

    long calculateDamageModifier(long eut);

    long getExpectedDamage(@Nullable ItemStack tool, long eut);
}
