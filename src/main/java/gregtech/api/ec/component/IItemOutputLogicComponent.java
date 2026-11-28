package gregtech.api.ec.component;

import java.util.List;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.ComponentKey;

public interface IItemOutputLogicComponent extends IUniqueComponent {

    ComponentKey.UniqueKey<IItemOutputLogicComponent> KEY = ComponentKey.unique();

    boolean addOutputAtomic(@NotNull ItemStack stack, boolean protectExcess);

    boolean addOutputAtomic(@NotNull List<ItemStack> stacks, boolean protectExcess);

    int addOutputPartial(@NotNull ItemStack stack, boolean protectExcess);

    default boolean addOutputPartial(@NotNull List<ItemStack> stacks, boolean protectExcess) {
        return addOutputPartial(stacks, protectExcess, null);
    }

    boolean addOutputPartial(@NotNull List<ItemStack> stacks, boolean protectExcess,
        @Nullable List<ItemStack> remaining);
}
