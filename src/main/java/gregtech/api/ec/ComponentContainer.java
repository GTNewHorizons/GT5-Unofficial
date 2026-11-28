package gregtech.api.ec;

import java.util.Arrays;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.GTValues;

public final class ComponentContainer {

    private final @NotNull IECMachine owner;
    private final @NotNull Runnable onHostReady;

    public ComponentContainer(@NotNull IECMachine owner, @NotNull Runnable onHostReady) {
        this.owner = owner;
        this.onHostReady = onHostReady;
    }

    private enum LifecycleState {
        UNINITIALIZED,
        INITIALIZING,
        READY
    }

    private LifecycleState state = LifecycleState.UNINITIALIZED;

    public boolean isInitialized() {
        return state != LifecycleState.UNINITIALIZED;
    }

    public boolean isInitializing() {
        return state == LifecycleState.INITIALIZING;
    }

    public boolean isReady() {
        return state == LifecycleState.READY;
    }

    public void initialize(@NotNull Runnable register) {
        if (state != LifecycleState.UNINITIALIZED) return;
        state = LifecycleState.INITIALIZING;
        register.run();
        state = LifecycleState.READY;
        notifyReady();
    }

    private boolean notifyDirty = false;
    private boolean readyDirty = false;

    private void notifyReady() {
        if (notifyDirty) {
            readyDirty = true;
            return;
        }
        notifyDirty = true;
        do {
            readyDirty = false;
            onHostReady.run();
            for (var component : getComponents(IComponent.KEY)) {
                component.onComponentsReady();
            }
        } while (readyDirty);
        notifyDirty = false;
    }

    private long[] mask = GTValues.emptyLongArray;
    private Object[] entries = GTValues.emptyObjectArray;

    private int indexOf(@NotNull ComponentKey<?> key) {
        int w = key.wordIndex;
        if (w >= mask.length) return -1;
        long word = mask[w];
        if ((word & key.bitMask) == 0) return -1;
        int index = Long.bitCount(word & key.lowerMask);
        for (int i = 0; i < w; i++) {
            index += Long.bitCount(mask[i]);
        }
        return index;
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> @Nullable T tryGetComponent(@NotNull ComponentKey.UniqueKey<T> key) {
        int index = indexOf(key);
        return index != -1 ? (T) entries[index] : null;
    }

    public <T extends IComponent> boolean hasComponent(@NotNull ComponentKey<T> key) {
        int w = key.wordIndex;
        if (w >= mask.length) return false;
        return (mask[w] & key.bitMask) != 0;
    }

    @SuppressWarnings("unchecked")
    public <T extends IComponent> @NotNull T[] getComponents(@NotNull ComponentKey.ListKey<T> key) {
        int index = indexOf(key);
        return index != -1 ? (T[]) entries[index] : key.emptyArray;
    }

    public <T extends IComponent> void addComponent(@NotNull T component) {
        for (var c : getComponents(IComponent.KEY)) {
            if (c == component) {
                throw new IllegalStateException("Can't add duplicate component instance: "+ component.getClass().getSimpleName());
            }
        }
        var keys = ComponentKey.getKeysForImpl(component.getClass());
        for (var key : keys) {
            if (key instanceof ComponentKey.UniqueKey<?> uniqueKey) {
                Object existing = tryGetComponent(uniqueKey);
                if (existing != null) {
                    throw new IllegalStateException(
                        String.format(
                            "Can't add multiple unique components for interface %s, attempted to add %s, but already has %s",
                            uniqueKey.interfaceType.getSimpleName(),
                            component.getClass()
                                .getSimpleName(),
                            existing
                                .getClass()
                                .getSimpleName()));
                }
            }
        }
        for (var key : keys) {
            switch (key) {
                case ComponentKey.UniqueKey<?> _ -> addEntry(key, component);
                case ComponentKey.ListKey<?> listKey -> {
                    int index = indexOf(listKey);
                    Object existing = index != -1 ? entries[index] : null;
                    Object updated = listKey.append(existing, component);
                    if (index != -1) entries[index] = updated;
                    else addEntry(listKey, updated);
                }
            }
        }
        component.onAddToMachine(owner);
        if (isReady()) {
            notifyReady();
        }
    }

    private void addEntry(@NotNull ComponentKey<?> key, @NotNull Object value) {
        int w = key.wordIndex;
        int denseIndex = 0;
        if (w >= mask.length) {
            mask = Arrays.copyOf(mask, w + 1);
        } else {
            denseIndex += Long.bitCount(mask[w] & key.lowerMask);
        }
        for (int i = 0; i < w; i++) {
            denseIndex += Long.bitCount(mask[i]);
        }
        mask[w] |= key.bitMask;
        var updated = new Object[entries.length + 1];
        if (denseIndex > 0) {
            System.arraycopy(entries, 0, updated, 0, denseIndex);
        }
        updated[denseIndex] = value;
        if (denseIndex < entries.length) {
            System.arraycopy(entries, denseIndex, updated, denseIndex + 1, entries.length - denseIndex);
        }
        entries = updated;
    }

    private void removeEntry(@NotNull ComponentKey<?> key, int denseIndex) {
        int w = key.wordIndex;
        mask[w] &= ~key.bitMask;
        if (entries.length == 1) {
            entries = GTValues.emptyObjectArray;
            return;
        }
        var updated = new Object[entries.length - 1];
        if (denseIndex > 0) {
            System.arraycopy(entries, 0, updated, 0, denseIndex);
        }
        if (denseIndex < entries.length - 1) {
            System.arraycopy(entries, denseIndex + 1, updated, denseIndex, entries.length - denseIndex - 1);
        }
        entries = updated;
    }

    private boolean removeComponentInternal(@NotNull IComponent component) {
        boolean removed = false;
        for (var key : ComponentKey.getKeysForImpl(component.getClass())) {
            int denseIndex = indexOf(key);
            if (denseIndex == -1) return false;
            Object current = entries[denseIndex];
            switch (key) {
                case ComponentKey.UniqueKey<?> _ -> {
                    if (current == component) {
                        removeEntry(key, denseIndex);
                        removed = true;
                    }
                }
                case ComponentKey.ListKey<?> listKey -> {
                    Object updated = listKey.remove(current, component);
                    if (updated != current) {
                        if (updated == null) removeEntry(key, denseIndex);
                        else entries[denseIndex] = updated;
                        removed = true;
                    }
                }
            }
        }
        return removed;
    }

    public boolean removeComponent(@NotNull IComponent component) {
        boolean removed = removeComponentInternal(component);
        if (removed) {
            component.onRemoveFromMachine(owner);
            if (isReady()) {
                notifyReady();
            }
        }
        return removed;
    }

    public <T extends IComponent> boolean removeComponent(@NotNull ComponentKey.ListKey<T> key) {
        T[] targets = getComponents(key);
        if (targets.length == 0) return false;
        boolean removed = false;
        for (var target : targets) {
            if (removeComponentInternal(target)) {
                target.onRemoveFromMachine(owner);
                removed = true;
            }
        }
        if (removed && isReady()) {
            notifyReady();
        }
        return removed;
    }
}
