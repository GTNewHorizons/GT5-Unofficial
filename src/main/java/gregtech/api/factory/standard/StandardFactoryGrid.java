package gregtech.api.factory.standard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import gregtech.GTMod;
import gregtech.api.factory.IFactoryElement;
import gregtech.api.factory.IFactoryGrid;
import gregtech.api.factory.IFactoryNetwork;

/**
 * This handles all network topology updates, and should be compatible with most pipe systems. A factory element should
 * always register itself with this grid, even when it's not connected to anything.
 */
public abstract class StandardFactoryGrid<TSelf extends StandardFactoryGrid<TSelf, TElement, TNetwork>, TElement extends IFactoryElement<TElement, TNetwork, TSelf>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TSelf>>
    implements IFactoryGrid<TSelf, TElement, TNetwork> {

    public static final Logger LOGGER = LogManager.getLogger("Standard Factory Network");

    public final HashSet<TNetwork> networks = new HashSet<>();
    public final HashSet<TElement> vertices = new HashSet<>();
    public final SetMultimap<TElement, TElement> edges = MultimapBuilder.hashKeys()
        .hashSetValues()
        .build();

    protected StandardFactoryGrid() {

    }

    @Override
    public void addElement(TElement element) {
        removeElement(element);

        vertices.add(element);
        updateNeighbours(element);

        HashSet<TElement> discovered = new HashSet<>();
        HashSet<TNetwork> networks = new HashSet<>();

        long pre = System.nanoTime();

        walkAdjacency(element, discovered, networks, false);

        long post = System.nanoTime();

        LOGGER.info("Walked adjacent elements in " + (post - pre) / 1e3 + " us");

        if (networks.size() == 0) {
            // there are no neighbours, or the neighbours didn't have a network somehow (which is an illegal state!
            // boo!)
            TNetwork network = createNetwork();
            this.networks.add(network);

            for (TElement e : discovered) {
                if (e.getNetwork() != network) {
                    e.setNetwork(network);
                    network.addElement(e);
                }
            }
        } else if (networks.size() == 1) {
            // there was one network adjacent, so we can just add all discovered elements to it if they aren't already
            TNetwork network = networks.iterator()
                .next();

            for (TElement e : discovered) {
                if (e.getNetwork() != network) {
                    e.setNetwork(network);
                    network.addElement(e);
                }
            }
        } else {
            // there were several adjacent networks; subsume all smaller networks into the biggest one
            Iterator<TNetwork> iter = networks.iterator();

            TNetwork biggestNetwork = iter.next();

            while (iter.hasNext()) {
                TNetwork network = iter.next();

                if (network.getElements()
                    .size()
                    > biggestNetwork.getElements()
                        .size())
                    biggestNetwork = network;
            }

            pre = System.nanoTime();

            for (TNetwork network : networks) {
                if (network != biggestNetwork) {
                    subsume(biggestNetwork, network);
                }
            }

            post = System.nanoTime();
            LOGGER.info("Subsumed " + (networks.size() - 1) + " networks in " + (post - pre) / 1e3 + " us");

            for (TElement e : discovered) {
                if (e.getNetwork() == null) {
                    e.setNetwork(biggestNetwork);
                    biggestNetwork.addElement(e);
                }
            }
        }
    }

    @Override
    public void addElementQuietly(TNetwork network, TElement element) {
        vertices.add(element);
        element.setNetwork(network);
        network.addElement(element);
    }

    protected abstract TNetwork createNetwork();

    @Override
    public void removeElement(TElement element) {
        if (!vertices.contains(element)) return;

        vertices.remove(element);
        Set<TElement> neighbours = edges.removeAll(element);

        TNetwork network = element.getNetwork();

        network.removeElement(element);
        element.setNetwork(null);

        // the network doesn't have any elements left, there aren't any adjacent neighbours to fix
        if (network.getElements()
            .isEmpty()) {
            network.onNetworkRemoved();
            networks.remove(network);
            return;
        }

        for (TElement neighbour : neighbours) {
            updateNeighbours(neighbour);
        }

        // if there's only one neighbour, then this element is at the end of a chain and we can return early since we
        // definitely didn't split a network
        if (neighbours.size() <= 1) return;

        HashSet<HashSet<TElement>> neighbouringClumps = new HashSet<>();

        // the list of all discovered elements; if one is in here, it means we've visited it already and can skip
        // iterating its neighbours
        HashSet<TElement> discovered = new HashSet<>();

        long pre = System.nanoTime();

        for (TElement neighbour : neighbours) {
            if (discovered.contains(neighbour)) continue;

            // find all elements connected to this neighbour
            HashSet<TElement> clump = new HashSet<>();
            walkAdjacency(neighbour, clump, null, true);

            neighbouringClumps.add(clump);
            discovered.addAll(clump);
        }

        // if there's only one clump of neighbours then the network hasn't been split
        if (neighbouringClumps.size() <= 1) {
            return;
        }

        HashSet<TElement> biggestClump = null;

        // find the biggest clump of neighbours; we'll split the other clumps from it
        for (HashSet<TElement> nn : neighbouringClumps) {
            if (biggestClump == null || nn.size() > biggestClump.size()) biggestClump = nn;
        }

        for (HashSet<TElement> nn : neighbouringClumps) {
            if (nn != biggestClump) {
                for (TElement e : nn) {
                    network.removeElement(e);
                }

                TNetwork newNetwork = createNetwork();

                for (TElement e : nn) {
                    e.setNetwork(newNetwork);
                    newNetwork.addElement(e);
                }
            }
        }

        long post = System.nanoTime();

        // temporary logging so that I can see what the performance is like
        LOGGER.info(
            "Split network in " + (post - pre) / 1e3
                + " us (added "
                + (neighbouringClumps.size() - 1)
                + " new networks)");
    }

    @Override
    public void removeElementQuietly(TElement element) {
        if (!vertices.contains(element)) return;

        element.getNetwork()
            .removeElement(element);
        vertices.remove(element);
        element.setNetwork(null);

        for (TElement neighbour : edges.removeAll(element)) {
            updateNeighbours(neighbour);
        }
    }

    @Override
    public void subsume(TNetwork dest, TNetwork source) {
        source.onNetworkSubsumedPre(dest);

        for (TElement element : new ArrayList<>(source.getElements())) {
            source.removeElement(element);
            element.setNetwork(dest);
            dest.addElement(element);
        }

        source.onNetworkSubsumedPost(dest);
        source.onNetworkRemoved();
        this.networks.remove(source);
    }

    private void walkAdjacency(TElement start, HashSet<TElement> discovered, HashSet<TNetwork> networks,
        boolean recurseIntoNetworked) {
        LinkedList<TElement> queue = new LinkedList<>();

        queue.add(start);

        while (queue.size() > 0) {
            TElement current = queue.removeFirst();

            discovered.add(current);

            if (networks != null) networks.add(current.getNetwork());

            if (recurseIntoNetworked ? true : current.getNetwork() == null) {
                for (TElement neighbour : edges.get(current)) {
                    if (!discovered.contains(neighbour)) {
                        queue.add(neighbour);
                    }
                }
            }
        }

        if (networks != null) networks.remove(null);
    }

    public void updateNeighbours(TElement element) {
        updateNeighbours(element, new HashSet<>());
    }

    private void updateNeighbours(TElement element, HashSet<TElement> updated) {
        if (updated.contains(element)) return;
        updated.add(element);

        HashSet<TElement> neighbours = new HashSet<>();

        element.getNeighbours(neighbours);

        Set<TElement> oldNeighbours = edges.removeAll(element);
        edges.putAll(element, neighbours);

        for (TElement oldNeighbour : oldNeighbours) {
            if (!neighbours.contains(oldNeighbour)) {
                updateNeighbours(oldNeighbour, updated);

                if (edges.containsEntry(oldNeighbour, element)) {
                    GTMod.GT_FML_LOGGER.error(
                        "A factory element isn't following the graph adjacency contract. Edge B -> A was kept when edge A -> B was removed. A = "
                            + element
                            + ", B = "
                            + oldNeighbour);
                }

                oldNeighbour.onNeighbourRemoved(element);
                element.onNeighbourRemoved(oldNeighbour);
            }
        }

        for (TElement currentNeighbour : neighbours) {
            if (!oldNeighbours.contains(currentNeighbour)) {
                updateNeighbours(currentNeighbour, updated);

                if (!edges.containsEntry(currentNeighbour, element)) {
                    GTMod.GT_FML_LOGGER.error(
                        "A factory element isn't following the graph adjacency contract. Edge B -> A was not added when edge A -> B was added. A = "
                            + element
                            + ", B = "
                            + currentNeighbour);
                }

                currentNeighbour.onNeighbourAdded(element);
                element.onNeighbourAdded(currentNeighbour);
            }
        }
    }
}
