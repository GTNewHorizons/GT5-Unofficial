package gregtech.common.tileentities.machines.multi.nanochip;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public final class VacuumConveyorPipeClientStateManager {

    public static final VacuumConveyorPipeClientStateManager INSTANCE = new VacuumConveyorPipeClientStateManager();

    @SuppressWarnings("unchecked")
    private final List<MTEVacuumConveyorPipe>[] pipes = new ArrayList[100];

    private int tickCounter = 0;
    private final Set<MTEVacuumConveyorPipe> toUnregister = new HashSet<>();

    VacuumConveyorPipeClientStateManager() {
        for (int i = 0; i < pipes.length; i++) {
            pipes[i] = new ArrayList<>();
        }
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    public void register(MTEVacuumConveyorPipe pipe) {
        pipes[tickCounter].add(pipe);
    }

    public void unregister(MTEVacuumConveyorPipe pipe) {
        toUnregister.add(pipe);
    }

    public void clear() {
        for (int i = 0; i < pipes.length; i++) {
            pipes[i].clear();
        }
        toUnregister.clear();
    }

    @SubscribeEvent
    public void update(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.START) return;
        tickCounter++;
        if (tickCounter >= pipes.length) {
            tickCounter = 0;
        }
        Iterator<MTEVacuumConveyorPipe> iter = pipes[tickCounter].iterator();
        while (iter.hasNext()) {
            MTEVacuumConveyorPipe pipe = iter.next();
            if (toUnregister.remove(pipe)) {
                iter.remove();
                continue;
            }
            pipe.toggleClientActiveState();
        }
    }
}
