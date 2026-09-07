package gregtech.api.graphs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.graphs.consumers.ConsumerNode;
import gregtech.api.graphs.paths.NodePath;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.common.covers.Cover;

/** Preparation only. An integration owner must keep one planner per generated component. */
final class PowerRoutePlanner {

    static final int MAX_ENTRIES = 8;
    // ponytail: bound preparation storage; oversized components keep legacy transfer.
    static final int MAX_NODES = 4096, MAX_CABLES = 65536;
    private final Node root;
    private final Map<Node, Tree> entries = new LinkedHashMap<>(16, 0.75f, true);
    private Snapshot snapshot;
    private boolean invalid;
    private boolean checking;

    PowerRoutePlanner(Node root) {
        this.root = root;
    }

    private static TileEntity neighbor(BaseMetaPipeEntity base, ForgeDirection side) {
        World world = base.getWorldObj();
        if (world != null
            && !world.blockExists(base.xCoord + side.offsetX, base.yCoord + side.offsetY, base.zCoord + side.offsetZ))
            return null;
        return base.getTileEntityAtSide(side);
    }

    /** Null means unsupported/stale topology; it is not a zero-demand answer. */
    Tree prepare(Node entry) {
        if (!refresh() || !snapshot.nodes.contains(entry) || !(entry.mTileEntity instanceof BaseMetaPipeEntity))
            return null;
        Tree existing = entries.get(entry);
        if (existing != null) return refresh() && snapshot == existing.stamp ? existing : null;
        Snapshot before = snapshot;
        Tree result;
        try {
            result = search(entry, before);
        } catch (ArithmeticException unsupportedWeight) {
            entries.clear();
            snapshot = null;
            return null;
        }
        // Cover/extension queries during preparation may synchronously change the graph.
        if (!refresh() || snapshot != before) return null;
        entries.put(entry, result);
        if (entries.size() > MAX_ENTRIES) entries.remove(
            entries.keySet()
                .iterator()
                .next());
        return result;
    }

    private boolean refresh() {
        if (invalid || checking) return false;
        Snapshot next;
        checking = true;
        try {
            next = Snapshot.capture(root);
        } catch (ArithmeticException unsupportedWeight) {
            next = null;
        } finally {
            checking = false;
        }
        if (next == null || snapshot != null && !next.sameTopology(snapshot)) {
            invalid = true;
            snapshot = null;
            entries.clear();
            return false;
        }
        if (!next.same(snapshot)) {
            entries.clear();
            snapshot = next;
        }
        return snapshot != null;
    }

    final class Tree {

        private final Snapshot stamp;
        private final Map<Node, Route> routes;

        private Tree(Snapshot stamp, Map<Node, Route> routes) {
            this.stamp = stamp;
            this.routes = routes;
        }

        boolean isCurrent() {
            return refresh() && snapshot == stamp && refresh() && snapshot == stamp;
        }

        Route routeTo(Node endpoint) {
            if (!isCurrent()) throw new IllegalStateException("Routing state changed");
            return routes.get(endpoint);
        }
    }

    static final class Route {

        final Node node;
        final Route previous;
        final MetaPipeEntity[] interior;
        final MetaPipeEntity self;
        final boolean reverse;
        final long loss;
        final int cableCount;

        Route(Node node, Route previous, MetaPipeEntity[] interior, boolean reverse, long loss, int cableCount) {
            this.node = node;
            this.previous = previous;
            this.interior = interior;
            this.self = node.mSelfPath == null ? null : node.mSelfPath.getPipes()[0];
            this.reverse = reverse;
            this.loss = loss;
            this.cableCount = cableCount;
        }

        // ponytail: reconstruct ties on demand; ranked prefixes can replace this if tie-heavy profiles justify it.
        List<MetaPipeEntity> cables() {
            List<Route> steps = new ArrayList<>();
            for (Route route = this; route != null; route = route.previous) steps.add(route);
            List<MetaPipeEntity> result = new ArrayList<>(cableCount);
            for (int i = steps.size() - 1; i >= 0; i--) {
                Route route = steps.get(i);
                for (int j = 0; j < route.interior.length; j++)
                    result.add(route.interior[route.reverse ? route.interior.length - 1 - j : j]);
                if (route.self != null) result.add(route.self);
            }
            return result;
        }
    }

    private Tree search(Node entry, Snapshot state) {
        Map<Node, Route> best = new IdentityHashMap<>();
        PriorityQueue<Route> queue = new PriorityQueue<>((a, b) -> compare(a, b, state));
        Route initial = new Route(entry, null, new MetaPipeEntity[0], false, state.selfLoss(entry), 1);
        best.put(entry, initial);
        queue.add(initial);
        while (!queue.isEmpty()) {
            Route current = queue.remove();
            if (best.get(current.node) != current) continue;
            // Machines are endpoints, not forwarding edges in this component.
            if (!(current.node.mTileEntity instanceof BaseMetaPipeEntity)) continue;
            for (int side = 0; side < 6; side++) {
                Node next = current.node.mNeighbourNodes[side];
                if (next == null) continue;
                NodePath path = current.node.mNodePaths[side];
                Lock lock = current.node.locks[side];
                if (lock != null && lock.isLocked() || path.lock.isLocked()) continue;
                MetaPipeEntity[] pipes = path.getPipes();
                boolean reverse = pipes.length > 0
                    && neighbor((BaseMetaPipeEntity) current.node.mTileEntity, ForgeDirection.getOrientation(side))
                        != pipes[0].getBaseMetaTileEntity();
                relax(current, next, pipes, reverse, state, best, queue);
            }
            if (current.node.alternateEdges != null) {
                for (Node.AlternateEdge edge : current.node.alternateEdges) {
                    boolean reverse = edge.second == current.node;
                    relax(current, reverse ? edge.first : edge.second, edge.pipes, reverse, state, best, queue);
                }
            }
        }
        return new Tree(state, best);
    }

    private static void relax(Route from, Node to, MetaPipeEntity[] pipes, boolean reverse, Snapshot state,
        Map<Node, Route> best, PriorityQueue<Route> queue) {
        TileEntity previous = from.node.mTileEntity;
        long added = state.selfLoss(to);
        for (int i = 0; i < pipes.length; i++) {
            MetaPipeEntity pipe = pipes[reverse ? pipes.length - 1 - i : i];
            Cable cable = state.cables.get(pipe);
            if (!state.connected(previous, cable.base)) return;
            previous = cable.base;
            added = Math.addExact(added, cable.loss);
        }
        if (!state.connected(previous, to.mTileEntity) || from.loss > Long.MAX_VALUE - added) return;
        if (to instanceof ConsumerNode consumer && !(to.mTileEntity instanceof BaseMetaPipeEntity)
            && (to.mTileEntity.xCoord + consumer.mSide.offsetX != previous.xCoord
                || to.mTileEntity.yCoord + consumer.mSide.offsetY != previous.yCoord
                || to.mTileEntity.zCoord + consumer.mSide.offsetZ != previous.zCoord))
            return;
        Route candidate = new Route(
            to,
            from,
            pipes,
            reverse,
            from.loss + added,
            from.cableCount + pipes.length + (to.mSelfPath == null ? 0 : 1));
        Route old = best.get(to);
        if (old == null || compare(candidate, old, state) < 0) {
            best.put(to, candidate);
            queue.add(candidate);
        }
    }

    private static int compare(Route a, Route b, Snapshot state) {
        int c = Long.compare(a.loss, b.loss);
        if (c == 0) c = Integer.compare(a.cableCount, b.cableCount);
        if (c != 0) return c;
        List<MetaPipeEntity> left = a.cables(), right = b.cables();
        for (int i = 0; i < left.size(); i++) {
            Cable x = state.cables.get(left.get(i)), y = state.cables.get(right.get(i));
            c = Integer.compare(x.x, y.x);
            if (c == 0) c = Integer.compare(x.y, y.y);
            if (c == 0) c = Integer.compare(x.z, y.z);
            if (c != 0) return c;
        }
        int sideA = a.node instanceof ConsumerNode consumer ? consumer.mSide.ordinal() : -1;
        int sideB = b.node instanceof ConsumerNode consumer ? consumer.mSide.ordinal() : -1;
        return Integer.compare(sideA, sideB);
    }

    private static final class Cable {

        final BaseMetaPipeEntity base;
        final long loss;
        final int x, y, z, open;

        Cable(BaseMetaPipeEntity base, long loss, int open) {
            this.base = base;
            this.loss = loss;
            this.open = open;
            x = base.xCoord;
            y = base.yCoord;
            z = base.zCoord;
        }
    }

    private static final class Snapshot {

        final List<Node> nodes = new ArrayList<>();
        final Map<MetaPipeEntity, Cable> cables = new IdentityHashMap<>();
        // Exact identity/value comparison, not a collision-prone topology hash.
        final List<Object> identities = new ArrayList<>();
        final List<Long> values = new ArrayList<>();
        final List<Long> availability = new ArrayList<>();

        boolean same(Snapshot other) {
            return sameTopology(other) && availability.equals(other.availability);
        }

        boolean sameTopology(Snapshot other) {
            if (other == null || identities.size() != other.identities.size() || !values.equals(other.values))
                return false;
            for (int i = 0; i < identities.size(); i++) if (identities.get(i) != other.identities.get(i)) return false;
            return true;
        }

        static Snapshot capture(Node root) {
            Snapshot state = new Snapshot();
            Set<Node> seen = Collections.newSetFromMap(new IdentityHashMap<>());
            Set<NodePath> paths = Collections.newSetFromMap(new IdentityHashMap<>());
            Set<List<ConsumerNode>> consumerLists = Collections.newSetFromMap(new IdentityHashMap<>());
            List<Node> pending = new ArrayList<>(List.of(root));
            for (int index = 0; index < pending.size(); index++) {
                Node node = pending.get(index);
                if (!seen.add(node)) continue;
                if (seen.size() > MAX_NODES || !state.tile(node.mTileEntity)) return null;
                if (node.mConsumers == null || node.mConsumers.size() > MAX_NODES
                    || node.mNeighbourNodes.length != 6
                    || node.mNodePaths.length != 6
                    || node.locks.length != 6) return null;
                state.nodes.add(node);
                state.identities.add(node);
                state.identities.add(node.mSelfPath);
                if (node.mTileEntity instanceof BaseMetaPipeEntity base) {
                    if (base.getNode() != node || node.mSelfPath == null
                        || node.mSelfPath.getPipes().length != 1
                        || node.mSelfPath.getPipes()[0] != base.getMetaTileEntity()) return null;
                } else if (node.mSelfPath != null) return null;
                if (node.mSelfPath != null && !state.path(node.mSelfPath, paths)) return null;
                state.values.add(node instanceof ConsumerNode consumer ? (long) consumer.mSide.ordinal() : -1L);
                state.identities.add(node.mConsumers);
                if (consumerLists.add(node.mConsumers)) {
                    state.identities.addAll(node.mConsumers);
                    state.values.add((long) node.mConsumers.size());
                }
                for (int side = 0; side < 6; side++) {
                    Node next = node.mNeighbourNodes[side];
                    NodePath path = node.mNodePaths[side];
                    state.identities.add(next);
                    state.identities.add(path);
                    state.identities.add(node.locks[side]);
                    state.availability.add(node.locks[side] != null && node.locks[side].isLocked() ? 1L : 0L);
                    if (next != null) {
                        if (path == null) return null;
                        pending.add(next);
                    }
                    if (path != null && !state.path(path, paths)) return null;
                }
                state.values.add(node.alternateEdges == null ? 0L : (long) node.alternateEdges.size());
                if (node.alternateEdges != null) {
                    for (Node.AlternateEdge edge : node.alternateEdges) {
                        state.identities.add(edge);
                        state.identities.add(edge.first);
                        state.identities.add(edge.second);
                        state.values.add((long) edge.firstSide.ordinal());
                        state.values.add((long) edge.secondSide.ordinal());
                        pending.add(edge.first);
                        pending.add(edge.second);
                        if (!state.members(edge.pipes)) return null;
                    }
                }
            }
            long total = 0;
            for (Cable cable : state.cables.values()) total = Math.addExact(total, cable.loss);
            return state;
        }

        boolean path(NodePath path, Set<NodePath> seen) {
            if (!seen.add(path)) return true;
            identities.add(path.lock);
            availability.add(path.lock.isLocked() ? 1L : 0L);
            return members(path.getPipes());
        }

        boolean members(MetaPipeEntity[] members) {
            if (members.length > MAX_CABLES) return false;
            values.add((long) members.length);
            for (MetaPipeEntity member : members) {
                identities.add(member);
                if (cables.containsKey(member)) continue;
                if (cables.size() >= MAX_CABLES || !(member instanceof MTECable cable)
                    || !(member.getBaseMetaTileEntity() instanceof BaseMetaPipeEntity base)
                    || base.getMetaTileEntity() != member
                    || !tile(base)
                    || cable.mCableLossPerMeter < 0) return false;
                int connected = 0, open = 0;
                for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                    if (!cable.isConnectedAtSide(side)) continue;
                    connected |= side.flag;
                    identities.add(neighbor(base, side));
                    Cover cover = base.getCoverAtSide(side);
                    if (cover != null && (!cover.isValid() || cable.letsIn(cover) && cable.letsOut(cover)))
                        open |= side.flag;
                }
                values.add((long) connected);
                availability.add((long) open);
                values.add((long) base.getColorization());
                values.add(cable.mCableLossPerMeter);
                identities.add(base.getNode());
                identities.add(base.getNodePath());
                cables.put(member, new Cable(base, cable.mCableLossPerMeter, open));
            }
            return true;
        }

        boolean tile(TileEntity tile) {
            identities.add(tile);
            World world = tile.getWorldObj();
            identities.add(world);
            values.add((long) tile.xCoord);
            values.add((long) tile.yCoord);
            values.add((long) tile.zCoord);
            return !tile.isInvalid() && !(tile instanceof BaseTileEntity base && base.isDead())
                && (world == null || world.blockExists(tile.xCoord, tile.yCoord, tile.zCoord)
                    && world.getTileEntity(tile.xCoord, tile.yCoord, tile.zCoord) == tile);
        }

        long selfLoss(Node node) {
            return node.mSelfPath == null ? 0 : cables.get(node.mSelfPath.getPipes()[0]).loss;
        }

        boolean connected(TileEntity from, TileEntity to) {
            for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                if (from.xCoord + side.offsetX != to.xCoord || from.yCoord + side.offsetY != to.yCoord
                    || from.zCoord + side.offsetZ != to.zCoord) continue;
                if (from instanceof BaseMetaPipeEntity base) {
                    Cable cable = cables.get(base.getMetaTileEntity());
                    if (cable == null || (cable.open & side.flag) == 0 || neighbor(base, side) != to) return false;
                }
                if (to instanceof BaseMetaPipeEntity base) {
                    Cable cable = cables.get(base.getMetaTileEntity());
                    if (cable == null || (cable.open & side.getOpposite().flag) == 0
                        || neighbor(base, side.getOpposite()) != from) return false;
                    if (from instanceof BaseMetaPipeEntity source && source.getColorization() >= 0
                        && base.getColorization() >= 0
                        && source.getColorization() != base.getColorization()) return false;
                }
                return true;
            }
            return false;
        }
    }
}
