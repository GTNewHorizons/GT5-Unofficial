package gregtech.api.ec;

import static gregtech.GTLoggers.GT_FML_LOGGER;

import java.lang.invoke.MethodHandles;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.ec.component.IUniqueComponent;
import gregtech.api.util.GTUtility;

public abstract sealed class ComponentKey<T extends IComponent> permits ComponentKey.UniqueKey,ComponentKey.ListKey {

    private static final AtomicInteger GLOBAL_ID = new AtomicInteger();
    private static final Map<Class<?>, ComponentKey<?>> BY_CLASS = new ConcurrentHashMap<>();
    public final int id;
    public @NotNull final Class<T> interfaceType;

    final int wordIndex;
    final long bitMask;
    final long lowerMask;

    protected ComponentKey(@NotNull Class<T> interfaceType) {
        if (!interfaceType.isInterface()) {
            throw new IllegalArgumentException("ComponentKey must be created with a interface");
        }
        this.id = GLOBAL_ID.getAndIncrement();
        this.interfaceType = interfaceType;
        this.wordIndex = id >> 6;
        this.bitMask = 1L << (id & 63);
        this.lowerMask = this.bitMask - 1;

        if (BY_CLASS.putIfAbsent(interfaceType, this) != null) {
            throw new IllegalStateException("Duplicate ComponentKey for interface: " + interfaceType.getName());
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends IComponent> @Nullable ComponentKey<T> getKeyForInterface(@NotNull Class<T> interfaceType) {
        return (ComponentKey<T>) INTERFACE_KEY.get(interfaceType);
    }

    public static @NotNull ComponentKey<?>[] getKeysForImpl(@NotNull Class<?> implType) {
        return IMPL_KEYS.get(implType);
    }

    public static int getKeyCount() {
        return GLOBAL_ID.get();
    }

    /**
     * Warning: Be careful with generic type erasure, always assign this to an explicitly typed variable.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static @NotNull <T extends IUniqueComponent> UniqueKey<T> unique(T... emptyArray) {
        return new UniqueKey<>(
            (Class<T>) emptyArray.getClass()
                .getComponentType());
    }

    public static @NotNull <T extends IUniqueComponent> UniqueKey<T> unique(@NotNull Class<T> type) {
        return new UniqueKey<>(type);
    }

    public static final class UniqueKey<T extends IComponent> extends ComponentKey<T> {

        private UniqueKey(@NotNull Class<T> type) {
            super(type);
        }
    }

    /**
     * Warning: Be careful with generic type erasure, always assign this to an explicitly typed variable.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static @NotNull <T extends IComponent> ListKey<T> list(T... emptyArray) {
        return new ListKey<>(
            (Class<T>) emptyArray.getClass()
                .getComponentType(),
            emptyArray,
            null);
    }

    /**
     * Warning: Be careful with generic type erasure, always assign this to an explicitly typed variable.
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static @NotNull <T extends IComponent> ListKey<T> list(@NotNull Comparator<T> comparator, T... emptyArray) {
        return new ListKey<>(
            (Class<T>) emptyArray.getClass()
                .getComponentType(),
            emptyArray,
            comparator);
    }

    public static @NotNull <T extends IComponent> ListKey<T> list(@NotNull Class<T> type, @NotNull T[] emptyArray) {
        return new ListKey<>(type, emptyArray, null);
    }

    public static @NotNull <T extends IComponent> ListKey<T> list(@NotNull Class<T> type, @NotNull T[] emptyArray,
        @NotNull Comparator<T> comparator) {
        return new ListKey<>(type, emptyArray, comparator);
    }

    public static final class ListKey<T extends IComponent> extends ComponentKey<T> {

        public final @NotNull T[] emptyArray;
        public final @Nullable Comparator<T> comparator;

        private ListKey(@NotNull Class<T> type, @NotNull T[] emptyArray, @Nullable Comparator<T> comparator) {
            super(type);
            if (IUniqueComponent.class.isAssignableFrom(type)) {
                throw new IllegalArgumentException("IUniqueComponent must use UniqueKey: " + type.getName());
            }
            this.emptyArray = emptyArray;
            this.comparator = comparator;
        }

        @SuppressWarnings("unchecked")
        public @NotNull T[] append(@Nullable Object existingObj, @NotNull IComponent comp) {
            var component = (T) comp;
            if (existingObj == null) {
                var result = Arrays.copyOf(emptyArray, 1);
                result[0] = component;
                return result;
            }
            var existing = (T[]) existingObj;
            for (T c : existing) {
                if (c == component) return existing;
            }
            var result = Arrays.copyOf(existing, existing.length + 1);
            result[existing.length] = component;
            if (comparator != null) {
                Arrays.sort(result, comparator);
            }
            return result;
        }

        @SuppressWarnings("unchecked")
        public @Nullable T[] remove(@Nullable Object existingObj, @NotNull IComponent comp) {
            if (existingObj == null) return null;
            var component = (T) comp;
            var existing = (T[]) existingObj;
            int index = -1;
            for (int i = 0; i < existing.length; i++) {
                if (existing[i] == component) {
                    index = i;
                    break;
                }
            }
            if (index == -1) return existing;
            if (existing.length == 1) return null;
            var result = Arrays.copyOf(emptyArray, existing.length - 1);
            if (index > 0) {
                System.arraycopy(existing, 0, result, 0, index);
            }
            if (index < existing.length - 1) {
                System.arraycopy(existing, index + 1, result, index, existing.length - index - 1);
            }
            return result;
        }

        public @NotNull T[] newArray(int size) {
            return Arrays.copyOf(emptyArray, size);
        }

        public void sortIfNeeded(@NotNull Object arrayObj) {
            if (comparator == null) return;
            T[] array = (T[]) arrayObj;
            if (array.length > 1) {
                Arrays.sort(array, comparator);
            }
        }
    }

    private static final ClassValue<ComponentKey<?>> INTERFACE_KEY = new ClassValue<>() {

        @Override
        protected ComponentKey<?> computeValue(@NotNull Class<?> inter) {
            try {
                MethodHandles.lookup()
                    .ensureInitialized(inter);
            } catch (IllegalAccessException _) {
                GT_FML_LOGGER.error("Failed to initialize component interface: {}", inter.getName());
            }
            return BY_CLASS.get(inter);
        }
    };
    private static final ClassValue<ComponentKey<?>[]> IMPL_KEYS = new ClassValue<>() {

        @Override
        protected ComponentKey<?>[] computeValue(@NotNull Class<?> impl) {
            Set<ComponentKey<?>> keys = new HashSet<>();
            for (var inter : GTUtility.getAllInterfaces(impl)) {
                if (!IComponent.class.isAssignableFrom(inter)) continue;
                var key = INTERFACE_KEY.get(inter);
                if (key != null) keys.add(key);
            }
            return keys.toArray(new ComponentKey<?>[0]);
        }
    };
}
