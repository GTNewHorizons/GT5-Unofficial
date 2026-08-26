package tectech.thing.metaTileEntity.multi.godforge.util;

import java.util.Arrays;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.GTMod;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtil;
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
                if (module != null && module.getBaseMetaTileEntity() != null) {
                    buf.writeBoolean(true);
                    IGregTechTileEntity igtte = module.getBaseMetaTileEntity();
                    buf.writeInt(igtte.getXCoord());
                    buf.writeInt(igtte.getYCoord());
                    buf.writeInt(igtte.getZCoord());
                    buf.writeInt(igtte.getWorld().provider.dimensionId);
                } else {
                    buf.writeBoolean(false);
                }
            })
            .deserializer(buf -> {
                if (!buf.readBoolean()) {
                    return null;
                }

                int x = buf.readInt();
                int y = buf.readInt();
                int z = buf.readInt();
                World world = DimensionManager.getWorld(buf.readInt());
                // on the client, world is null here. this sets it to the client value in this case without a side check
                if (world == null) world = GTMod.GT.getThePlayer().worldObj;
                TileEntity te = GTUtil.getTileEntity(world, x, y, z, false);
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
