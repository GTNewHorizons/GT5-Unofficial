package gregtech.common.gui.modularui.multiblock.godforge.sync;

import java.lang.ref.WeakReference;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;

public class Submodule {

    private static final Map<Submodule, WeakReference<Submodule>> pool = new WeakHashMap<>();

    private final Modules<?> module;
    private final int submoduleIndex;

    public Submodule(Modules<?> module, int submoduleIndex) {
        this.module = module;
        this.submoduleIndex = submoduleIndex;
    }

    public static Submodule create(Modules<?> module, int submoduleIndex) {
        return new Submodule(module, submoduleIndex);
    }

    public Modules<?> getModule() {
        return this.module;
    }

    public int getSubmoduleIndex() {
        return this.submoduleIndex;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Submodule o)) return false;
        if (this.module != o.module) return false;
        return this.submoduleIndex == o.submoduleIndex;
    }

    @Override
    public int hashCode() {
        return Objects.hash(module, submoduleIndex);
    }
}
