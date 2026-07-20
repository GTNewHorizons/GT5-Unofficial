package gregtech.common.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

import net.minecraft.block.Block;
import net.minecraft.world.World;

import com.gtnewhorizon.gtnhlib.util.CoordinatePacker;

import it.unimi.dsi.fastutil.longs.Long2DoubleMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class DronePathfinder {

    public static class Node implements Comparable<Node> {

        public final int x, y, z;
        public double gCost = Double.MAX_VALUE;
        public double hCost;
        public Node parent;

        public Node(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public double getFCost() {
            return gCost + hCost;
        }

        @Override
        public int compareTo(Node o) {
            return Double.compare(this.getFCost(), o.getFCost());
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Node node)) return false;
            return x == node.x && y == node.y && z == node.z;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            result = 31 * result + z;
            return result;
        }
    }

    private static final int[] DX = { 1, -1, 0, 0, 0, 0 };
    private static final int[] DY = { 0, 0, 1, -1, 0, 0 };
    private static final int[] DZ = { 0, 0, 0, 0, 1, -1 };

    private static final double[] COS_TABLE = new double[16];
    private static final double[] SIN_TABLE = new double[16];

    static {
        for (int i = 0; i < 16; i++) {
            double angle = i * 2.0 * Math.PI / 16;
            COS_TABLE[i] = Math.cos(angle);
            SIN_TABLE[i] = Math.sin(angle);
        }
    }

    public static List<Node> findSmoothPath(World world, int startX, int startY, int startZ, int endX, int endY,
        int endZ) {
        Long2DoubleMap cache = new Long2DoubleOpenHashMap();
        cache.defaultReturnValue(-1.0);
        List<Node> rawPath = findPath(world, startX, startY, startZ, endX, endY, endZ, cache);
        if (rawPath == null || rawPath.isEmpty()) {
            return null;
        }
        return smoothPath(world, rawPath, cache);
    }

    public static List<Node> findPath(World world, int startX, int startY, int startZ, int endX, int endY, int endZ,
        Long2DoubleMap cache) {
        Node startNode = new Node(startX, startY, startZ);
        Node targetNode = new Node(endX, endY, endZ);

        startNode.gCost = 0;
        startNode.hCost = getHeuristic(startX, startY, startZ, endX, endY, endZ);

        Long2ObjectMap<Node> allNodes = new Long2ObjectOpenHashMap<>();
        PriorityQueue<Node> openSet = new PriorityQueue<>();

        long startKey = CoordinatePacker.pack(startX, startY, startZ);
        allNodes.put(startKey, startNode);
        openSet.add(startNode);

        int nodesExpanded = 0;
        int maxNodes = 10000;

        while (!openSet.isEmpty() && nodesExpanded < maxNodes) {
            Node current = openSet.poll();
            nodesExpanded++;

            long currentKey = CoordinatePacker.pack(current.x, current.y, current.z);
            Node bestTracked = allNodes.get(currentKey);
            if (bestTracked == null || current.gCost > bestTracked.gCost) {
                continue;
            }
            current = bestTracked;

            if (current.x == targetNode.x && current.y == targetNode.y && current.z == targetNode.z) {
                return reconstructPath(current);
            }

            for (int i = 0; i < 6; i++) {
                int nx = current.x + DX[i];
                int ny = current.y + DY[i];
                int nz = current.z + DZ[i];

                if (ny < 0 || ny > 255) continue;

                double cost = getBlockCost(world, nx, ny, nz, cache);
                if (cost >= 9999.0) continue;

                long neighborKey = CoordinatePacker.pack(nx, ny, nz);
                Node trackedNeighbor = allNodes.get(neighborKey);
                double tentativeGCost = current.gCost + cost;

                if (trackedNeighbor == null) {
                    Node neighbor = new Node(nx, ny, nz);
                    neighbor.hCost = getHeuristic(nx, ny, nz, targetNode.x, targetNode.y, targetNode.z);
                    neighbor.gCost = tentativeGCost;
                    neighbor.parent = current;
                    allNodes.put(neighborKey, neighbor);
                    openSet.add(neighbor);
                } else if (tentativeGCost < trackedNeighbor.gCost) {
                    trackedNeighbor.gCost = tentativeGCost;
                    trackedNeighbor.parent = current;

                    Node openSetEntry = new Node(nx, ny, nz);
                    openSetEntry.gCost = tentativeGCost;
                    openSetEntry.hCost = trackedNeighbor.hCost;
                    openSet.add(openSetEntry);
                }
            }
        }

        return null;
    }

    private static double getHeuristic(int ax, int ay, int az, int bx, int by, int bz) {
        return Math.abs(ax - bx) + Math.abs(ay - by) + Math.abs(az - bz);
    }

    private static List<Node> reconstructPath(Node node) {
        List<Node> path = new ArrayList<>();
        Node curr = node;
        while (curr != null) {
            path.add(curr);
            curr = curr.parent;
        }
        Collections.reverse(path);
        return path;
    }

    public static double getBlockCost(World world, int x, int y, int z, Long2DoubleMap cache) {
        if (y < 0 || y > 255) return 9999.0;
        long key = CoordinatePacker.pack(x, y, z);
        double cached = cache.get(key);
        if (cached >= 0.0) {
            return cached;
        }
        double cost = getBlockCostRaw(world, x, y, z);
        cache.put(key, cost);
        return cost;
    }

    private static double getBlockCostRaw(World world, int x, int y, int z) {
        if (!world.blockExists(x, y, z)) {
            return 9999.0;
        }
        try {
            Block block = world.getBlock(x, y, z);
            if (block == null || block.isAir(world, x, y, z)) {
                return 1.0;
            }
            if (block.getCollisionBoundingBoxFromPool(world, x, y, z) == null) {
                return 1.2;
            }
            if (block.getMaterial() != null && block.getMaterial()
                .isLiquid()) {
                return 2.0;
            }
        } catch (Throwable e) {
            return 5.0;
        }
        return 5.0;
    }

    public static boolean hasLineOfSight(World world, int x0, int y0, int z0, int x1, int y1, int z1,
        Long2DoubleMap cache) {
        int dx = Math.abs(x1 - x0);
        int dy = Math.abs(y1 - y0);
        int dz = Math.abs(z1 - z0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int sz = z0 < z1 ? 1 : -1;

        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;
            while (x0 != x1) {
                x0 += sx;
                if (p1 >= 0) {
                    y0 += sy;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z0 += sz;
                    p2 -= 2 * dx;
                }
                p1 += 2 * dy;
                p2 += 2 * dz;
                if (getBlockCost(world, x0, y0, z0, cache) > 1.5) {
                    return false;
                }
            }
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;
            while (y0 != y1) {
                y0 += sy;
                if (p1 >= 0) {
                    x0 += sx;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z0 += sz;
                    p2 -= 2 * dy;
                }
                p1 += 2 * dx;
                p2 += 2 * dz;
                if (getBlockCost(world, x0, y0, z0, cache) > 1.5) {
                    return false;
                }
            }
        } else {
            int p1 = 2 * dx - dz;
            int p2 = 2 * dy - dz;
            while (z0 != z1) {
                z0 += sz;
                if (p1 >= 0) {
                    x0 += sx;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    y0 += sy;
                    p2 -= 2 * dz;
                }
                p1 += 2 * dx;
                p2 += 2 * dy;
                if (getBlockCost(world, x0, y0, z0, cache) > 1.5) {
                    return false;
                }
            }
        }
        return true;
    }

    public static List<Node> smoothPath(World world, List<Node> path, Long2DoubleMap cache) {
        if (path == null || path.size() <= 2) return path;

        List<Node> smoothed = new ArrayList<>();
        smoothed.add(path.getFirst());

        int i = 0;
        while (i < path.size() - 1) {
            int bestNext = i + 1;
            for (int j = path.size() - 1; j > i + 1; j--) {
                Node start = path.get(i);
                Node end = path.get(j);
                if (hasLineOfSight(world, start.x, start.y, start.z, end.x, end.y, end.z, cache)) {
                    bestNext = j;
                    break;
                }
            }
            smoothed.add(path.get(bestNext));
            i = bestNext;
        }

        return smoothed;
    }

    public static double[] findSafeOrbit(World world, double ctrlX, double ctrlY, double ctrlZ) {
        double defaultY = ctrlY + 2.5;
        double defaultR = 3.0;
        Long2DoubleMap cache = new Long2DoubleOpenHashMap();
        cache.defaultReturnValue(-1.0);

        for (double dy = 2.5; dy <= 6.5; dy += 1.0) {
            double testY = ctrlY + dy;
            for (double r = 3.0; r <= 6.0; r += 1.0) {
                if (isOrbitClear(world, ctrlX, testY, ctrlZ, r, cache)) {
                    return new double[] { r, testY };
                }
            }
        }

        return new double[] { defaultR, defaultY };
    }

    private static boolean isOrbitClear(World world, double ctrlX, double y, double ctrlZ, double r,
        Long2DoubleMap cache) {
        for (int i = 0; i < 16; i++) {
            double px = ctrlX + r * COS_TABLE[i];
            double pz = ctrlZ + r * SIN_TABLE[i];

            if (getBlockCost(world, (int) Math.floor(px), (int) Math.floor(y), (int) Math.floor(pz), cache) > 1.5
                || getBlockCost(world, (int) Math.floor(px), (int) Math.floor(y + 1.0), (int) Math.floor(pz), cache)
                    > 1.5) {
                return false;
            }
        }
        return true;
    }
}
