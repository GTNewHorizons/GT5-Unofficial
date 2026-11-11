package tectech.mechanics.boseEinsteinCondensate;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.concurrent.atomic.AtomicInteger;

import net.minecraftforge.fluids.Fluid;

import org.apache.commons.lang3.mutable.MutableBoolean;

import appeng.api.storage.data.IAEFluidStack;
import gregtech.api.factory.GraphRouteTracker;
import gregtech.api.factory.routing.VisitorResult;
import gregtech.api.factory.standard.StandardFactoryNetwork;
import gregtech.api.util.GTUtility;

public class BECFactoryNetwork extends StandardFactoryNetwork<BECFactoryNetwork, BECFactoryElement, BECFactoryGrid> {

    private static final AtomicInteger NETWORK_ID = new AtomicInteger(0);

    public final int id = NETWORK_ID.getAndAdd(1);

    public final GraphRouteTracker<BECFactoryElement, BECFactoryNetwork, BECFactoryGrid, NotableBECFactoryElement, BECRouteInfo> routeTracker = new GraphRouteTracker<>(
        NotableBECFactoryElement.class,
        new BECRouteInfo(0));

    private final HashMap<NotableBECFactoryElement, Set<Fluid>> reachableCondensate = new HashMap<>();
    private int condensateVersion;
    private int cachedRouteVersion = -1;
    private int cachedCondensateVersion;

    @Override
    public void addElement(BECFactoryElement element) {
        super.addElement(element);

        routeTracker.onElementAdded(element);
        routeTracker.invalidateRoutes();
    }

    @Override
    public void removeElement(BECFactoryElement element) {
        super.removeElement(element);

        routeTracker.onElementRemoved(element);
        routeTracker.invalidateRoutes();
    }

    @Override
    public void onElementUpdated(BECFactoryElement element, boolean topologyChanged) {
        if (topologyChanged) {
            routeTracker.invalidateRoutes();
        }
    }

    public void invalidateRoutes() {
        routeTracker.invalidateRoutes();
    }

    /// Drains condensate from the network and decrements the sizes in the request stacks as condensate is extracted.
    public void drainCondensate(NotableBECFactoryElement drainer, IAEFluidStack request) {
        routeTracker.iterateNetworkBFS(drainer, step -> {
            if (!step.node()
                .allowsCondensateThrough(request.getFluid())) return VisitorResult.SkipNode;

            if (step.node() instanceof BECInventory inv) {
                inv.removeCondensate(request);
            }

            return request.getStackSize() > 0 ? VisitorResult.Continue : VisitorResult.Break;
        });
    }

    public void injectCondensate(NotableBECFactoryElement start, IAEFluidStack input) {
        HashSet<BECInventory> inventories = new HashSet<>();

        routeTracker.iterateNetworkBFS(start, step -> {
            if (!step.node()
                .allowsCondensateThrough(input.getFluid())) return VisitorResult.SkipNode;

            if (step.node() instanceof BECInventory inv) {
                inventories.add(inv);
            }

            return VisitorResult.Continue;
        });

        if (inventories.isEmpty()) return;

        double totalCapacity = 0;

        for (BECInventory inv : inventories) {
            totalCapacity += inv.getCondensateCapacity();
        }

        int remaining = inventories.size();

        for (BECInventory inv : inventories) {
            long split;

            if (remaining == 1) {
                split = input.getStackSize();
            } else if (totalCapacity <= 0) {
                split = GTUtility.ceil((double) input.getStackSize() / remaining);
            } else {
                double weight = inv.getCondensateCapacity() / totalCapacity;
                totalCapacity -= inv.getCondensateCapacity();

                split = GTUtility.ceil(input.getStackSize() * weight);
            }

            remaining--;

            IAEFluidStack copy = input.empty();
            copy.setStackSize(split);
            input.decStackSize(copy.getStackSize());

            inv.addCondensate(copy);

            input.incStackSize(copy.getStackSize());
        }
    }

    public long getStoredCondensate(NotableBECFactoryElement start, Fluid condensate) {
        HashSet<BECInventory> inventories = new HashSet<>();

        routeTracker.iterateNetworkBFS(start, step -> {
            if (!step.node()
                .allowsCondensateThrough(condensate)) return VisitorResult.SkipNode;

            if (step.node() instanceof BECInventory inv) {
                inventories.add(inv);
            }

            return VisitorResult.Continue;
        });

        long sum = 0;

        for (BECInventory inv : inventories) {
            sum += inv.getContents()
                .getLong(condensate);
        }

        return sum;
    }

    public CondensateList getStoredCondensate(NotableBECFactoryElement start) {
        HashSet<Fluid> contained = new HashSet<>();

        for (BECInventory inv : getComponents(BECInventory.class)) {
            contained.addAll(
                inv.getContents()
                    .keySet());
        }

        CondensateList storedCondensate = new CondensateList();

        for (Fluid condensate : contained) {
            long amount = getStoredCondensate(start, condensate);

            if (amount <= 0) continue;

            storedCondensate.addTo(condensate, amount);
        }

        return storedCondensate;
    }

    /// Must be called when a storage gains or loses a condensate type, or when a diode's filters change.
    public void invalidateReachableCondensate() {
        condensateVersion++;
    }

    /// Returns the number of 'contaminants' in the network.
    public int getSlowdowns(NotableBECFactoryElement drainer, Collection<Fluid> validCondensate) {
        int slowdowns = 0;

        for (Fluid condensate : getReachableCondensate(drainer)) {
            if (!validCondensate.contains(condensate)) slowdowns++;
        }

        return slowdowns;
    }

    /// The condensate types held by storages the drainer can reach. Cached until the routes or the stored condensate
    /// types change.
    private Set<Fluid> getReachableCondensate(NotableBECFactoryElement drainer) {
        if (cachedRouteVersion != routeTracker.getVersion() || cachedCondensateVersion != condensateVersion) {
            reachableCondensate.clear();
            cachedRouteVersion = routeTracker.getVersion();
            cachedCondensateVersion = condensateVersion;
        }

        return reachableCondensate.computeIfAbsent(drainer, this::findReachableCondensate);
    }

    private Set<Fluid> findReachableCondensate(NotableBECFactoryElement drainer) {
        HashSet<Fluid> contained = new HashSet<>();

        // Find all fluids in the network (dumb scan, we don't care if we can actually access them)
        for (BECInventory inv : getComponents(BECInventory.class)) {
            contained.addAll(
                inv.getContents()
                    .keySet());
        }

        HashSet<Fluid> reachable = new HashSet<>();

        MutableBoolean found = new MutableBoolean();

        for (Fluid condensate : contained) {
            found.setValue(false);

            // Scan the network until we find a storage that contains the current condensate
            routeTracker.iterateNetworkBFS(drainer, step -> {
                if (!step.node()
                    .allowsCondensateThrough(condensate)) return VisitorResult.SkipNode;

                if (step.node() instanceof BECInventory inv) {
                    if (inv.getContents()
                        .getLong(condensate) > 0) {
                        found.setValue(true);
                        return VisitorResult.Break;
                    }
                }

                return VisitorResult.Continue;
            });

            if (found.booleanValue()) {
                reachable.add(condensate);
            }
        }

        return reachable;
    }
}
