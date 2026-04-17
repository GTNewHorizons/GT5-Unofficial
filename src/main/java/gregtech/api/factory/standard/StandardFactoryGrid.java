package gregtech.api.factory.standard;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.SetMultimap;

import gregtech.api.factory.IFactoryElement;
import gregtech.api.factory.IFactoryGrid;
import gregtech.api.factory.IFactoryNetwork;

/**
 * This handles all network topology updates, and should be compatible with most pipe systems. A factory element should
 * always register itself with this grid, even when it's not connected to anything.
 */
public abstract class StandardFactoryGrid<TSelf extends StandardFactoryGrid<TSelf, TElement, TNetwork>, TElement extends IFactoryElement<TElement, TNetwork, TSelf>, TNetwork extends IFactoryNetwork<TNetwork, TElement, TSelf>>
    implements IFactoryGrid<TSelf, TElement, TNetwork> {

    public final HashSet<TNetwork> networks = new HashSet<>();
    public final HashSet<TElement> vertices = new HashSet<>();
    public final SetMultimap<TElement, TElement> edges = MultimapBuilder.hashKeys()
        .hashSetValues()
        .build();

    protected StandardFactoryGrid() {

    }

    @Override
    public void updateElement(TElement element) {
        Set<TElement> previous = new HashSet<>(edges.get(element));

        Set<TElement> current = new HashSet<>();
        element.getNeighbours(current);

        boolean topologyChanged = false;

        for (TElement adj : previous) {
            if (current.contains(adj)) continue;

            topologyChanged = true;
            removeEdge(element, adj, false);

            element.onEdgeRemoved(adj);
            adj.onEdgeRemoved(element);
        }

        for (TElement adj : current) {
            if (previous.contains(adj)) continue;

            topologyChanged = true;
            addEdge(element, adj);

            element.onEdgeAdded(adj);
            adj.onEdgeRemoved(element);
        }

        if (element.getNetwork() == null) {
            TNetwork network = createNetwork();

            element.setNetwork(network);
            network.addElement(element);

            networks.add(network);
        }

        element.getNetwork()
            .onElementUpdated(element, topologyChanged);
    }

    @Override
    public void removeElement(TElement element) {
        for (TElement adj : new HashSet<>(edges.get(element))) {
            removeEdge(element, adj, true);
        }
    }

    protected void removeEdge(TElement from, TElement to, boolean isFullRemove) {
        edges.remove(from, to);
        edges.remove(to, from);

        Set<TElement> neighbours = new HashSet<>(edges.get(from));
        neighbours.add(to);

        TNetwork network = from.getNetwork();

        // 'from' only had one edge, so we can just remove this element without fixing anything
        if (neighbours.size() == 1) {
            network.removeElement(from);
            from.setNetwork(null);

            // The network doesn't have any elements left, there aren't any adjacent neighbours to fix
            if (network.getElements()
                .isEmpty()) {
                network.onNetworkRemoved();
                networks.remove(network);
            }

            if (isFullRemove) {
                vertices.remove(from);
            } else {
                network = createNetwork();

                from.setNetwork(network);
                network.addElement(from);

                networks.add(network);
            }

            return;
        }

        HashSet<HashSet<TElement>> neighbouringClumps = new HashSet<>();

        // The list of all discovered elements; if one is in here, it means we've visited it already and can skip
        // iterating its neighbours
        HashSet<TElement> discovered = new HashSet<>();

        for (TElement neighbour : neighbours) {
            if (discovered.contains(neighbour)) continue;

            // Find all elements connected to this neighbour
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

        for (HashSet<TElement> smallerClump : neighbouringClumps) {
            if (smallerClump != biggestClump) {
                for (TElement e : smallerClump) {
                    network.removeElement(e);
                }

                TNetwork newNetwork = createNetwork();

                for (TElement e : smallerClump) {
                    e.setNetwork(newNetwork);
                    newNetwork.addElement(e);
                }
            }
        }

    }

    protected void addEdge(TElement from, TElement to) {
        if (edges.get(from)
            .isEmpty()) vertices.add(from);

        edges.put(from, to);
        edges.put(to, from);

        HashSet<TElement> discovered = new HashSet<>();
        HashSet<TNetwork> networks = new HashSet<>();

        walkAdjacency(from, discovered, networks, false);

        if (networks.isEmpty()) {
            // There are no neighbours, or the neighbours didn't have a network somehow (which is an illegal state!
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
            // There was one network adjacent, so we can just add all discovered elements to it if they aren't already
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

            for (TNetwork network : networks) {
                if (network != biggestNetwork) {
                    subsume(biggestNetwork, network);
                }
            }

            for (TElement e : discovered) {
                if (e.getNetwork() == null) {
                    e.setNetwork(biggestNetwork);
                    biggestNetwork.addElement(e);
                }
            }
        }
    }

    protected abstract TNetwork createNetwork();

    protected void subsume(TNetwork dest, TNetwork source) {
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

    protected void walkAdjacency(TElement start, HashSet<TElement> discovered, HashSet<TNetwork> networks,
        boolean recurseIntoNetworked) {
        ArrayDeque<TElement> queue = new ArrayDeque<>();

        queue.add(start);

        while (!queue.isEmpty()) {
            TElement current = queue.removeFirst();

            if (!discovered.add(current)) continue;;

            if (networks != null) networks.add(current.getNetwork());

            if (current == start || (recurseIntoNetworked ? true : current.getNetwork() == null)) {
                queue.addAll(edges.get(current));
            }
        }

        if (networks != null) networks.remove(null);
    }
}
