package gregtech.api.ec;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Consumer;

import org.jetbrains.annotations.NotNull;

public final class OrderedSectionBuilder<T> {

    public record Entry<T> (@NotNull String id, int priority, @NotNull T section) {}

    private final Map<String, Entry<T>> entries = new LinkedHashMap<>();

    public OrderedSectionBuilder<T> add(@NotNull String id, int priority, T section) {
        if (entries.containsKey(id)) {
            throw new IllegalStateException("Duplicate section id: " + id);
        }
        entries.put(id, new Entry<>(id, priority, section));
        return this;
    }

    public OrderedSectionBuilder<T> add(@NotNull ISectionKey key, T section) {
        return add(key.getSectionId(), key.getPriority(), section);
    }

    public OrderedSectionBuilder<T> add(@NotNull String id, @NotNull ISectionKey anchor, T section) {
        return add(id, anchor.getPriority(), section);
    }

    public OrderedSectionBuilder<T> addIf(boolean condition, @NotNull String id, int priority, T section) {
        if (condition) add(id, priority, section);
        return this;
    }

    public OrderedSectionBuilder<T> addIf(boolean condition, @NotNull ISectionKey key, T section) {
        if (condition) add(key.getSectionId(), key.getPriority(), section);
        return this;
    }

    public OrderedSectionBuilder<T> addIf(boolean condition, @NotNull String id, @NotNull ISectionKey anchor,
        T section) {
        if (condition) add(id, anchor.getPriority(), section);
        return this;
    }

    public OrderedSectionBuilder<T> put(@NotNull String id, int priority, T section) {
        entries.put(id, new Entry<>(id, priority, section));
        return this;
    }

    public OrderedSectionBuilder<T> put(@NotNull ISectionKey key, T section) {
        return put(key.getSectionId(), key.getPriority(), section);
    }

    public OrderedSectionBuilder<T> replace(@NotNull String id, int priority, T section) {
        if (entries.get(id) != null) {
            entries.put(id, new Entry<>(id, priority, section));
        }
        return this;
    }

    public OrderedSectionBuilder<T> replace(@NotNull ISectionKey key, T section) {
        return replace(key.getSectionId(), key.getPriority(), section);
    }

    public OrderedSectionBuilder<T> remove(@NotNull String id) {
        entries.remove(id);
        return this;
    }

    public OrderedSectionBuilder<T> remove(@NotNull ISectionKey key) {
        return remove(key.getSectionId());
    }

    public void forEach(@NotNull Consumer<T> consumer) {
        if (entries.isEmpty()) return;
        var sorted = new ArrayList<>(entries.values());
        sorted.sort(Comparator.comparingInt(Entry::priority));
        for (var entry : sorted) {
            consumer.accept(entry.section);
        }
    }
}
