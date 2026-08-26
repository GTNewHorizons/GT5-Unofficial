package gregtech.common.gui.modularui.multiblock.godforge.data;

@FunctionalInterface
public interface TriFunction<T, U, O, R> {

    R apply(T t, U u, O o);
}
