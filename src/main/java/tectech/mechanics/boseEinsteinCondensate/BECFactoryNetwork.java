package tectech.mechanics.boseEinsteinCondensate;

import java.util.Collection;
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

    private boolean networkChanged = false;

    @Override
    public void addElement(BECFactoryElement element) {
        super.addElement(element);

        routeTracker.onElementAdded(element);
        networkChanged = true;
    }

    @Override
    public void removeElement(BECFactoryElement element) {
        super.removeElement(element);

        routeTracker.onElementRemoved(element);
        networkChanged = true;
    }

    @Override
    public void onElementUpdated(BECFactoryElement element, boolean topologyChanged) {
        if (topologyChanged) {
            networkChanged = true;
        }
    }

    public void invalidateRoutes() {
        networkChanged = true;
    }

    void onPostTick() {
        if (networkChanged) {
            networkChanged = false;

            routeTracker.updateEdges();
        }
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

    /// Returns the number of 'contaminants' in the network.
    public int getSlowdowns(NotableBECFactoryElement drainer, Collection<Fluid> validCondensate) {
        HashSet<Fluid> contained = new HashSet<>();

        // Find all fluids in the network (dumb scan, we don't care if we can actually access them)
        for (BECInventory inv : getComponents(BECInventory.class)) {
            contained.addAll(
                inv.getContents()
                    .keySet());
        }

        // Don't bother checking for the presence of valid condensate
        validCondensate.forEach(contained::remove);

        // No invalid condensate anywhere in the network. Unlikely for big setups, but it's certainly possible.
        if (contained.isEmpty()) return 0;

        HashSet<Fluid> contaminants = new HashSet<>();

        MutableBoolean found = new MutableBoolean();

        for (Fluid condensate : contained) {
            found.setValue(false);

            // Scan the network until we find a storage that contains the current condensate
            // If we find it, it's a contaminant, and it should slow assembling down
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
                // Use a set to prevent several storages with contaminant from slowing processing down excessively
                // We only care about unique contaminant
                contaminants.add(condensate);
            }
        }

        return contaminants.size();
    }
}
