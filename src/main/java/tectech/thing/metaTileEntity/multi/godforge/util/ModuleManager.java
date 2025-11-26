package tectech.thing.metaTileEntity.multi.godforge.util;

import java.util.Arrays;
import java.util.Set;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import tectech.thing.metaTileEntity.multi.godforge.MTEBaseModule;

public class ModuleManager {

    private static final int MAX_MODULES = 16;
    private final MTEBaseModule[] modules = new MTEBaseModule[MAX_MODULES];
    private final ObjectSet<MTEBaseModule> moduleSet = new ObjectOpenHashSet<>(MAX_MODULES);
    private final boolean[] queuedForRemoval = new boolean[MAX_MODULES];

    public int getInstalledModules() {
        int num = 0;
        for (MTEBaseModule module : modules) {
            if (module != null) {
                num++;
            }
        }
        return num;
    }

    public Set<MTEBaseModule> getModules() {
        return ObjectSets.unmodifiable(moduleSet);
    }

    public MTEBaseModule getModuleAt(int index) {
        if (queuedForRemoval[index]) {
            return null;
        }
        return modules[index];
    }

    public void disconnectAll() {
        for (MTEBaseModule module : modules) {
            if (module != null) {
                module.disconnect();
            }
        }
    }

    public void startQueueForRemoval() {
        Arrays.fill(queuedForRemoval, true);
    }

    public void endQueueForRemoval() {
        moduleSet.clear();
        for (int i = 0; i < MAX_MODULES; i++) {
            if (queuedForRemoval[i]) {
                modules[i] = null;
            } else if (modules[i] != null) {
                moduleSet.add(modules[i]);
            }
        }
        Arrays.fill(queuedForRemoval, false);
    }

    public boolean installModule(MTEBaseModule module, int moduleIndex) {
        modules[moduleIndex] = module;
        queuedForRemoval[moduleIndex] = false;
        return true;
    }
}
