package gregtech.common.items.matterManipulator;

import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.function.Function;

import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

/**
 * Various hacks to get things to behave properly.
 */
public class AnalysisHacks {

    public static enum RotationHacks {

        // spotless:off
        Drawers(
            "com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers",
            Rotator.forgeDirectionIntRotator("getDirection", "setDirection"))

        ;
        // spotless:on

        public final String className;
        public final Class<?> clazz;
        public final Rotator rotator;

        public static final HashMap<Class<?>, RotationHacks> ROTHACKS_BY_CLASS = new HashMap<>();

        private RotationHacks(String className, Function<Class<?>, Rotator> rotator) {
            Class<?> clazz = null;

            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                // do nothing
            }

            this.className = className;
            this.clazz = clazz;
            this.rotator = clazz == null ? null : rotator.apply(clazz);
        }

        public static ForgeDirection getRotation(TileEntity tile) {
            if (tile == null) return ForgeDirection.UNKNOWN;

            RotationHacks hack = ROTHACKS_BY_CLASS.get(tile.getClass());

            if (hack != null) {
                return hack.rotator.getRotation(tile);
            } else {
                for (RotationHacks hack2 : values()) {
                    if (hack2.clazz != null && hack2.clazz.isAssignableFrom(tile.getClass())) {
                        ROTHACKS_BY_CLASS.put(tile.getClass(), hack2);
                        return hack2.rotator.getRotation(tile);
                    }
                }

                return ForgeDirection.UNKNOWN;
            }
        }

        public static void setRotation(TileEntity tile, ForgeDirection direction) {
            if (tile == null) return;

            RotationHacks hack = ROTHACKS_BY_CLASS.get(tile.getClass());

            if (hack != null) {
                hack.rotator.setRotation(tile, direction);
            } else {
                for (RotationHacks hack2 : values()) {
                    if (hack2.clazz != null && hack2.clazz.isAssignableFrom(tile.getClass())) {
                        ROTHACKS_BY_CLASS.put(tile.getClass(), hack2);
                        hack2.rotator.setRotation(tile, direction);
                        break;
                    }
                }
            }
        }
    }

    private static interface Rotator {

        public ForgeDirection getRotation(TileEntity tile);

        public void setRotation(TileEntity tile, ForgeDirection direction);

        public static Function<Class<?>, Rotator> forgeDirectionIntRotator(String getterName, String setterName) {
            return clazz -> {
                try {
                    Method getter = clazz.getDeclaredMethod(getterName);
                    Method setter = clazz.getDeclaredMethod(setterName, int.class);

                    final MethodHandles.Lookup lookup = MethodHandles.lookup();
                    MethodHandle getterHandle = lookup.unreflect(getter);
                    MethodHandle setterHandle = lookup.unreflect(setter);

                    interface Getter {

                        public int get(Object tile);
                    }

                    interface Setter {

                        public void set(Object tile, int side);
                    }

                    Getter getterFn = (Getter) LambdaMetafactory
                        .metafactory(
                            lookup,
                            "get",
                            MethodType.methodType(Getter.class),
                            MethodType.methodType(int.class, Object.class),
                            getterHandle,
                            getterHandle.type())
                        .getTarget()
                        .invokeExact();
                    Setter setterFn = (Setter) LambdaMetafactory
                        .metafactory(
                            lookup,
                            "set",
                            MethodType.methodType(Setter.class),
                            MethodType.methodType(void.class, Object.class, int.class),
                            setterHandle,
                            setterHandle.type())
                        .getTarget()
                        .invokeExact();

                    return new Rotator() {

                        @Override
                        public ForgeDirection getRotation(TileEntity tile) {
                            return ForgeDirection.getOrientation(getterFn.get(tile));
                        }

                        @Override
                        public void setRotation(TileEntity tile, ForgeDirection direction) {
                            setterFn.set(tile, direction.ordinal());
                        }
                    };
                } catch (Throwable t) {
                    t.printStackTrace();
                }

                return null;
            };
        }
    }
}
