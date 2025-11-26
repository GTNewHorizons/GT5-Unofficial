package tectech.thing.metaTileEntity.multi.godforge.util;

import java.util.Arrays;
import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.tileentity.TileEntity;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
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

    public GenericListSyncHandler<MTEBaseModule> getSyncer() {
        return GenericListSyncHandler.<MTEBaseModule>builder()
            .getterArray(() -> modules)
            .setter(val -> {
                for (int i = 0; i < MAX_MODULES; i++) {
                    modules[i] = val.get(i);
                }
            })
            .serializer((buf, module) -> {
                buf.writeBoolean(module != null);
                if (module != null) {
                    IGregTechTileEntity igtte = module.getBaseMetaTileEntity();
                    buf.writeInt(igtte.getXCoord());
                    buf.writeInt(igtte.getYCoord());
                    buf.writeInt(igtte.getZCoord());
                }
            })
            .deserializer(buf -> {
                if (!buf.readBoolean()) {
                    return null;
                }
                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                TileEntity te = Minecraft.getMinecraft().theWorld.getTileEntity(x, y, z);
                if (te == null) return null;
                IMetaTileEntity mte = GTUtility.getMetaTileEntity(te);
                if (mte instanceof MTEBaseModule module) {
                    return module;
                }
                return null;
            })
            .build();
    }
}
