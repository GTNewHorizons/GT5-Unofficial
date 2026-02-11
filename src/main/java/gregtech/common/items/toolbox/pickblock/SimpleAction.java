package gregtech.common.items.toolbox.pickblock;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;

/**
 * Contains a class to test for and an action to run if the object being passed is of that type.
 *
 */
public class SimpleAction<V> implements IDeciderAction {

    private final Class<V> targetClass;
    private final BiFunction<V, ForgeDirection, List<ToolboxSlot>> testFunction;

    /**
     * @param targetClass  A targeted tile entity class. Can be either a {@link TileEntity} or a {@link IMetaTileEntity}
     * @param testFunction A function with the first argument being an instance of the targetClass, second argument as
     *                     the side of the block to act on (respecting the side selection wireframe)
     */
    public SimpleAction(Class<V> targetClass, BiFunction<V, ForgeDirection, List<ToolboxSlot>> testFunction) {
        this.targetClass = targetClass;
        this.testFunction = testFunction;
    }

    public SimpleAction(final Class<V> targetClass, ToolboxSlot... slots) {
        this(targetClass, (x, y) -> ImmutableList.copyOf(slots));
    }

    public List<ToolboxSlot> apply(Object obj, ForgeDirection wrenchedSide) {
        return cast(obj).map(casted -> testFunction.apply(casted, wrenchedSide))
            .orElse(ImmutableList.of());
    }

    @Override
    public boolean isValid(final Object obj) {
        return targetClass.isInstance(obj);
    }

    public Optional<V> cast(Object obj) {
        if (targetClass.isInstance(obj)) {
            return Optional.of(targetClass.cast(obj));
        }

        return Optional.empty();
    }
}
