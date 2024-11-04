package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector3i;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;

import appeng.api.AEApi;
import appeng.api.config.SecurityPermissions;
import appeng.api.features.ILocatable;
import appeng.api.implementations.parts.IPartCable;
import appeng.api.implementations.tiles.IWirelessAccessPoint;
import appeng.api.networking.IGrid;
import appeng.api.networking.IGridNode;
import appeng.api.networking.energy.IEnergyGrid;
import appeng.api.networking.security.ISecurityGrid;
import appeng.api.networking.storage.IStorageGrid;
import appeng.api.parts.IPartItem;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.tile.misc.TileSecurity;
import appeng.tile.networking.TileWireless;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.api.util.Lazy;
import gregtech.common.blocks.BlockMachines;
import gregtech.common.items.matterManipulator.BlockAnalyzer.RegionAnalysis;
import gregtech.common.tileentities.machines.multi.MTEMMUplink;

/**
 * The NBT state of a manipulator.
 */
class NBTState {

    static final Gson GSON = new GsonBuilder().create();

    public Config config = new Config();

    public Long encKey, uplinkAddress;
    public double charge;

    public transient TileSecurity securityTerminal;
    public transient IGridNode gridNode;
    public transient IGrid grid;
    public transient IStorageGrid storageGrid;
    public transient IMEMonitor<IAEItemStack> itemStorage;

    public static NBTState load(NBTTagCompound tag) {
        NBTState state = GSON.fromJson(MMUtils.toJsonObject(tag), NBTState.class);

        if (state == null) state = new NBTState();
        if (state.config == null) state.config = new NBTState.Config();

        return state;
    }

    public NBTTagCompound save() {
        return (NBTTagCompound) MMUtils.toNbt(GSON.toJsonTree(this));
    }

    /**
     * True if the ME system could be connected to.
     */
    public boolean hasMEConnection() {
        return encKey != null && securityTerminal != null
            && gridNode != null
            && grid != null
            && storageGrid != null
            && itemStorage != null;
    }

    /**
     * Tries to connect to an ME system, if possible.
     */
    public boolean connectToMESystem() {
        grid = null;
        storageGrid = null;
        itemStorage = null;

        if (encKey == null) return false;

        ILocatable grid = AEApi.instance()
            .registries()
            .locatable()
            .getLocatableBy(encKey);

        if (grid instanceof TileSecurity security) {
            this.securityTerminal = security;
            this.gridNode = security.getGridNode(ForgeDirection.UNKNOWN);
            if (this.gridNode != null) {
                this.grid = this.gridNode.getGrid();
                this.storageGrid = this.grid.getCache(IStorageGrid.class);
                if (this.storageGrid != null) {
                    this.itemStorage = this.storageGrid.getItemInventory();
                }
            }
        }

        return hasMEConnection();
    }

    private transient IWirelessAccessPoint prevAccessPoint;

    /**
     * Checks if the player is currently within range of an access point and the access point is online.
     */
    public boolean canInteractWithAE(EntityPlayer player) {
        if (grid == null) {
            return false;
        }

        IEnergyGrid eg = grid.getCache(IEnergyGrid.class);
        if (!eg.isNetworkPowered()) {
            return false;
        }

        ISecurityGrid sec = grid.getCache(ISecurityGrid.class);
        if (!sec.hasPermission(player, SecurityPermissions.EXTRACT)
            || !sec.hasPermission(player, SecurityPermissions.INJECT)) {
            return false;
        }

        if (checkAEDistance(player, prevAccessPoint)) {
            return true;
        }

        for (IGridNode node : grid.getMachines(TileWireless.class)) {
            if (checkAEDistance(player, (IWirelessAccessPoint) node.getMachine())) {
                prevAccessPoint = (IWirelessAccessPoint) node.getMachine();
                return true;
            }
        }

        prevAccessPoint = null;

        return false;
    }

    private boolean checkAEDistance(EntityPlayer player, IWirelessAccessPoint accessPoint) {
        if (accessPoint != null && accessPoint.getGrid() == grid && accessPoint.isActive()) {
            DimensionalCoord coord = accessPoint.getLocation();

            if (coord.getWorld().provider.dimensionId != player.worldObj.provider.dimensionId) {
                return false;
            }

            double distance = player.getDistanceSq(coord.x, coord.y, coord.z);

            return Math.pow(accessPoint.getRange(), 2) >= distance;
        } else {
            return false;
        }
    }

    public transient MTEMMUplink uplink;

    /**
     * Tries to connect to the uplink, if possible.
     */
    public boolean connectToUplink() {
        uplink = null;

        if (uplinkAddress != null && uplinkAddress != 0) {
            uplink = MTEMMUplink.getUplink(uplinkAddress);

            if (uplink != null) {
                if (!uplink.getBaseMetaTileEntity()
                    .isActive()) {
                    uplink = null;
                }
            }
        }

        return hasUplinkConnection();
    }

    public boolean hasUplinkConnection() {
        return uplink != null;
    }

    // #region Pending blocks

    /**
     * Gets the pending blocks for this manipulator.
     * Note: moving uses a special algorithm, so its value returned here should only be used for drawing the hints.
     */
    public List<PendingBlock> getPendingBlocks(World world) {
        return switch (config.placeMode) {
            case COPYING, MOVING -> getAnalysis(world);
            case GEOMETRY -> getGeomPendingBlocks(world);
            case EXCHANGING -> getExchangeBlocks(world);
            case CABLES -> getCableBlocks(world);
        };
    }

    private List<PendingBlock> getAnalysis(World world) {
        Location coordA = config.coordA;
        Location coordB = config.coordB;
        Location coordC = config.coordC;

        if (!Location.areCompatible(coordA, coordB, coordC) || !coordA.isInWorld(world)) {
            return new ArrayList<>();
        }

        // MOVING's result is only used visually since it has a special build algorithm
        RegionAnalysis analysis = BlockAnalyzer
            .analyzeRegion(world, coordA, coordB, config.placeMode == PlaceMode.COPYING ? true : false);

        for (PendingBlock block : analysis.blocks) {
            block.x += coordC.x;
            block.y += coordC.y;
            block.z += coordC.z;
        }

        return analysis.blocks;
    }

    private List<PendingBlock> getExchangeBlocks(World world) {
        Location coordA = config.coordA;
        Location coordB = config.coordB;

        if (!Location.areCompatible(coordA, coordB) || !coordA.isInWorld(world)) {
            return new ArrayList<>();
        }

        if (config.replaceWhitelist == null || config.replaceWhitelist.isEmpty()) {
            return new ArrayList<>();
        }

        Vector3i deltas = MMUtils.getRegionDeltas(coordA, coordB);

        ArrayList<PendingBlock> pending = new ArrayList<>();

        Set<ItemId> whitelist = config.replaceWhitelist.stream()
            .map(Config::loadStack)
            .map(stack -> ItemId.create(stack))
            .collect(Collectors.toSet());

        ItemStack replacement = Config.loadStack(config.replaceWith);

        for (Vector3i voxel : MMUtils.getBlocksInBB(coordA, deltas)) {
            PendingBlock existing = PendingBlock.fromBlock(world, voxel.x, voxel.y, voxel.z);

            if (existing != null && existing.toStack() != null
                && whitelist.contains(ItemId.create(existing.toStack()))) {
                pending.add(new PendingBlock(world.provider.dimensionId, voxel.x, voxel.y, voxel.z, replacement));
            }
        }

        return pending;
    }

    private List<PendingBlock> getCableBlocks(World world) {
        Location coordA = config.coordA;
        Location coordB = config.coordB;

        if (!Location.areCompatible(coordA, coordB) || !coordA.isInWorld(world)) {
            return new ArrayList<>();
        }

        Vector3i a = coordA.toVec();
        Vector3i b = pinToAxes(a, coordB.toVec());

        ArrayList<PendingBlock> out = new ArrayList<>();

        ItemStack stack = config.getCables();

        if (stack == null) {
            TileAnalysisResult noop = new TileAnalysisResult();

            for (Vector3i voxel : getLineVoxels(a.x, a.y, a.z, b.x, b.y, b.z)) {
                PendingBlock pendingBlock = new PendingBlock(
                    world.provider.dimensionId,
                    voxel.x,
                    voxel.y,
                    voxel.z,
                    null);

                pendingBlock.tileData = noop;

                out.add(pendingBlock);
            }
        } else {
            Block block = Block.getBlockFromItem(stack.getItem());

            if (block instanceof BlockMachines) {
                int start = 0, end = 0;

                // calculate the start & end mConnections flags
                switch (new Vector3i(b).sub(a)
                    .maxComponent()) {
                    case 0: {
                        start = b.x > 0 ? ForgeDirection.WEST.flag : ForgeDirection.EAST.flag;
                        end = b.x < 0 ? ForgeDirection.WEST.flag : ForgeDirection.EAST.flag;
                        break;
                    }
                    case 1: {
                        start = b.y > 0 ? ForgeDirection.DOWN.flag : ForgeDirection.UP.flag;
                        end = b.y < 0 ? ForgeDirection.DOWN.flag : ForgeDirection.UP.flag;
                        break;
                    }
                    case 2: {
                        start = b.z > 0 ? ForgeDirection.NORTH.flag : ForgeDirection.SOUTH.flag;
                        end = b.z < 0 ? ForgeDirection.NORTH.flag : ForgeDirection.SOUTH.flag;
                        break;
                    }
                }

                for (Vector3i voxel : getLineVoxels(a.x, a.y, a.z, b.x, b.y, b.z)) {
                    byte existingConnections = 0;

                    // respect existing connections if possible
                    if (world.getTileEntity(voxel.x, voxel.y, voxel.z) instanceof IGregTechTileEntity igte
                        && igte.getMetaTileEntity() instanceof IConnectable connectable) {
                        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                            if (connectable.isConnectedAtSide(dir)) {
                                existingConnections |= dir.flag;
                            }
                        }
                    }

                    PendingBlock pendingBlock = new PendingBlock(
                        world.provider.dimensionId,
                        voxel.x,
                        voxel.y,
                        voxel.z,
                        stack);

                    pendingBlock.tileData = new TileAnalysisResult();
                    pendingBlock.tileData.mConnections = (byte) (existingConnections | start | end);

                    out.add(pendingBlock);
                }

                // stop the ends from connecting to nothing
                if (!out.isEmpty()) {
                    out.get(0).tileData.mConnections &= ~start;
                    out.get(out.size() - 1).tileData.mConnections &= ~end;
                }
            } else if (stack.getItem() instanceof IPartItem partItem) {
                if (partItem.createPartFromItemStack(stack) instanceof IPartCable cable) {
                    Block cableBus = PendingBlock.AE_BLOCK_CABLE.get();

                    for (Vector3i voxel : getLineVoxels(a.x, a.y, a.z, b.x, b.y, b.z)) {
                        PendingBlock pendingBlock = new PendingBlock(
                            world.provider.dimensionId,
                            voxel.x,
                            voxel.y,
                            voxel.z,
                            stack);

                        pendingBlock.setBlock(cableBus, 0);

                        pendingBlock.tileData = new TileAnalysisResult();
                        pendingBlock.tileData.mAEParts = new AEPartData[7];
                        pendingBlock.tileData.mAEParts[ForgeDirection.UNKNOWN.ordinal()] = new AEPartData(cable);

                        out.add(pendingBlock);
                    }
                }
            }
        }

        return out;
    }

    private List<PendingBlock> getGeomPendingBlocks(World world) {
        Location coordA = config.coordA;
        Location coordB = config.coordB;
        Location coordC = config.coordC;

        if (!Location.areCompatible(coordA, coordB) || !coordA.isInWorld(world)) {
            return new ArrayList<>();
        }

        if (config.shape.requiresC()) {
            if (!Location.areCompatible(coordA, coordC) || !coordA.isInWorld(world)) {
                return new ArrayList<>();
            }
        }

        int x1 = config.coordA.x;
        int y1 = config.coordA.y;
        int z1 = config.coordA.z;
        int x2 = config.coordB.x;
        int y2 = config.coordB.y;
        int z2 = config.coordB.z;

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        ArrayList<PendingBlock> pending = new ArrayList<>();

        switch (config.shape) {
            case LINE: {
                iterateLine(pending, x1, y1, z1, x2, y2, z2);
                break;
            }
            case CUBE: {
                iterateCube(pending, minX, minY, minZ, maxX, maxY, maxZ);
                break;
            }
            case SPHERE: {
                iterateSphere(pending, minX, minY, minZ, maxX, maxY, maxZ);
                break;
            }
            case CYLINDER: {
                iterateCylinder(pending, coordA.toVec(), coordB.toVec(), coordC.toVec());
                break;
            }
        }

        return pending;
    }

    private static List<Vector3i> getLineVoxels(int x1, int y1, int z1, int x2, int y2, int z2) {
        List<Vector3i> voxels = new ArrayList<>();

        int dx = Math.abs(x1 - x2), dy = Math.abs(y1 - y2), dz = Math.abs(z1 - z2);
        int sx = x1 < x2 ? 1 : -1, sy = y1 < y2 ? 1 : -1, sz = z1 < z2 ? 1 : -1;

        voxels.add(new Vector3i(x1, y1, z1));

        if (dx >= dy && dx >= dz) {
            int p1 = 2 * dy - dx;
            int p2 = 2 * dz - dx;

            while (x1 != x2) {
                x1 += sx;

                if (p1 >= 0) {
                    y1 += sy;
                    p1 -= 2 * dx;
                }
                if (p2 >= 0) {
                    z1 += sz;
                    p2 -= 2 * dx;
                }

                p1 += 2 * dy;
                p2 += 2 * dz;

                voxels.add(new Vector3i(x1, y1, z1));
            }
        } else if (dy >= dx && dy >= dz) {
            int p1 = 2 * dx - dy;
            int p2 = 2 * dz - dy;

            while (y1 != y2) {
                y1 += sy;

                if (p1 >= 0) {
                    x1 += sx;
                    p1 -= 2 * dy;
                }
                if (p2 >= 0) {
                    z1 += sz;
                    p2 -= 2 * dy;
                }

                p1 += 2 * dx;
                p2 += 2 * dz;

                voxels.add(new Vector3i(x1, y1, z1));
            }
        } else {
            int p1 = 2 * dy - dz;
            int p2 = 2 * dx - dz;

            while (z1 != z2) {
                z1 += sz;

                if (p1 >= 0) {
                    y1 += sy;
                    p1 -= 2 * dz;
                }
                if (p2 >= 0) {
                    x1 += sx;
                    p2 -= 2 * dz;
                }

                p1 += 2 * dy;
                p2 += 2 * dx;

                voxels.add(new Vector3i(x1, y1, z1));
            }
        }

        return voxels;
    }

    private void iterateLine(ArrayList<PendingBlock> pending, int x1, int y1, int z1, int x2, int y2, int z2) {
        ItemStack edges = config.getEdges();

        for (Vector3i voxel : getLineVoxels(x1, y1, z1, x2, y2, z2)) {
            pending.add(new PendingBlock(config.coordA.worldId, voxel.x, voxel.y, voxel.z, edges));
        }
    }

    private void iterateCube(ArrayList<PendingBlock> pending, int minX, int minY, int minZ, int maxX, int maxY,
        int maxZ) {
        ItemStack corners = config.getCorners();
        ItemStack edges = config.getEdges();
        ItemStack faces = config.getFaces();
        ItemStack volumes = config.getVolumes();

        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    int insideCount = 0;

                    if (x > minX && x < maxX) insideCount++;
                    if (y > minY && y < maxY) insideCount++;
                    if (z > minZ && z < maxZ) insideCount++;

                    ItemStack selection = switch (insideCount) {
                        case 0 -> corners;
                        case 1 -> edges;
                        case 2 -> faces;
                        case 3 -> volumes;
                        default -> null;
                    };

                    pending.add(new PendingBlock(config.coordA.worldId, x, y, z, selection, insideCount, insideCount));
                }
            }
        }
    }

    private void iterateSphere(ArrayList<PendingBlock> pending, int minX, int minY, int minZ, int maxX, int maxY,
        int maxZ) {
        ItemStack faces = config.getFaces();
        ItemStack volumes = config.getVolumes();

        int sx = maxX - minX + 1;
        int sy = maxY - minY + 1;
        int sz = maxZ - minZ + 1;

        double rx = sx / 2.0;
        double ry = sy / 2.0;
        double rz = sz / 2.0;

        boolean[][][] present = new boolean[sx + 2][sy + 2][sz + 2];

        for (int x = 0; x < sx; x++) {
            for (int y = 0; y < sy; y++) {
                for (int z = 0; z < sz; z++) {
                    // the ternaries here check whether the given axis is 1, in which case this is a circle and not a
                    // sphere
                    // spotless:off
                    double distance = Math.sqrt(
                        (rx > 1 ? Math.pow((x - rx + 0.5) / rx, 2.0) : 0) +
                        (ry > 1 ? Math.pow((y - ry + 0.5) / ry, 2.0) : 0) +
                        (rz > 1 ? Math.pow((z - rz + 0.5) / rz, 2.0) : 0)
                    );
                    // spotless:on

                    if (distance <= 1) {
                        PendingBlock block = new PendingBlock(
                            config.coordA.worldId,
                            x + minX,
                            y + minY,
                            z + minZ,
                            volumes,
                            1,
                            1);

                        present[x + 1][y + 1][z + 1] = true;
                        pending.add(block);
                    }
                }
            }
        }

        ArrayList<ForgeDirection> directions = new ArrayList<>();

        if (rx > 1) {
            directions.add(ForgeDirection.EAST);
            directions.add(ForgeDirection.WEST);
        }

        if (ry > 1) {
            directions.add(ForgeDirection.UP);
            directions.add(ForgeDirection.DOWN);
        }

        if (rz > 1) {
            directions.add(ForgeDirection.NORTH);
            directions.add(ForgeDirection.SOUTH);
        }

        for (PendingBlock block : pending) {
            for (ForgeDirection dir : directions) {
                if (!present[block.x - minX + 1 + dir.offsetX][block.y - minY + 1 + dir.offsetY][block.z - minZ
                    + 1
                    + dir.offsetZ]) {
                    block.setBlock(faces);
                    block.buildOrder = 0;
                    block.renderOrder = 0;
                    break;
                }
            }
        }
    }

    private static int signum(int x) {
        return x > 0 ? 1 : x < 0 ? -1 : 0;
    }

    private void iterateCylinder(ArrayList<PendingBlock> pending, Vector3i coordA, Vector3i coordB, Vector3i coordC) {
        ItemStack faces = config.getFaces();
        ItemStack volumes = config.getVolumes();
        ItemStack edges = config.getEdges();

        Vector3i b2 = pinToPlanes(coordA, coordB);
        Vector3i height = pinToLine(coordA, b2, coordC).sub(coordA);

        Vector3i delta = new Vector3i(b2).sub(coordA);

        delta.x += signum(delta.x);
        delta.y += signum(delta.y);
        delta.z += signum(delta.z);

        // the deltas for each dimension (A/B/Height)
        int dA = 0, dB = 0, dH = 0;
        // used to determine the final block position
        Vector3i vecA, vecB, vecH;

        // calculate the delta vectors for each axis
        // this is kinda cursed and I don't really understand it anymore, so good luck changing it
        switch (delta.minComponent()) {
            case 0: {
                dA = delta.y;
                dB = delta.z;
                dH = height.x;
                vecA = new Vector3i(0, signum(delta.y), 0);
                vecB = new Vector3i(0, 0, signum(delta.z));
                vecH = new Vector3i(signum(height.x), 0, 0);
                break;
            }
            case 1: {
                dA = delta.x;
                dB = delta.z;
                dH = height.y;
                vecA = new Vector3i(signum(delta.x), 0, 0);
                vecB = new Vector3i(0, 0, signum(delta.z));
                vecH = new Vector3i(0, signum(height.y), 0);
                break;
            }
            case 2: {
                dA = delta.x;
                dB = delta.y;
                dH = height.z;
                vecA = new Vector3i(signum(delta.x), 0, 0);
                vecB = new Vector3i(0, signum(delta.y), 0);
                vecH = new Vector3i(0, 0, signum(height.z));
                break;
            }
            default: {
                throw new AssertionError();
            }
        }

        int absA = Math.abs(dA);
        int absB = Math.abs(dB);
        int absH = Math.abs(dH) + 1; // I have no idea why this +1 is needed

        float rA = absA / 2f;
        float rB = absB / 2f;

        boolean[][][] present = new boolean[absA + 2][absH + 2][absB + 2];

        // generate the blocks in A,B,H space
        // at this point, x=A, z=B, and y=H
        for (int a = 0; a < absA; a++) {
            for (int b = 0; b < absB; b++) {
                double distance = Math.pow((a - rA + 0.5) / rA, 2.0) + Math.pow((b - rB + 0.5) / rB, 2.0);

                if (distance <= 1) {
                    for (int h = 0; h < absH; h++) {
                        PendingBlock block = new PendingBlock(config.coordA.worldId, a, h, b, volumes, 2, 0);

                        present[a + 1][h + 1][b + 1] = true;
                        pending.add(block);
                    }
                }
            }
        }

        // check the adjacent blocks for each block and determine whether the block should be a volume, edge, or face
        for (PendingBlock block : pending) {
            byte adj = 0;

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (present[block.x + 1 + dir.offsetX][block.y + 1 + dir.offsetY][block.z + 1 + dir.offsetZ]) {
                    adj |= dir.flag;
                }
            }

            // I know this looks :ConcerningRead: but this is just an easy way to check which blocks are adjacent to
            // this one

            // if this block is missing an adjacent block, it's not a volume
            if (adj != 0b111111) {
                // if this block is missing one of the N/S/E/W blocks, it's an edge (the surface)
                if ((adj & 0b111100) == 0b111100) {
                    block.setBlock(edges);
                    block.buildOrder = 1;
                    block.renderOrder = 1;
                } else {
                    // otherwise, it's a face (top & bottom)
                    block.setBlock(faces);
                    block.buildOrder = 2;
                    block.renderOrder = 0;
                }
            }
        }

        // transform the positions of each block from relative A,B,H space into absolute X,Y,Z space
        for (PendingBlock block : pending) {
            int a = block.x, b = block.z, h = block.y;

            // why, yes, that is an integer matrix
            block.x = a * vecA.x + b * vecB.x + h * vecH.x + coordA.x;
            block.y = a * vecA.y + b * vecB.y + h * vecH.y + coordA.y;
            block.z = a * vecA.z + b * vecB.z + h * vecH.z + coordA.z;
        }
    }

    // #endregion

    static class Config {

        public PendingAction action;
        public BlockSelectMode blockSelectMode = BlockSelectMode.ALL;
        public BlockRemoveMode removeMode = BlockRemoveMode.NONE;
        public PlaceMode placeMode = PlaceMode.GEOMETRY;
        public Shape shape = Shape.LINE;

        public Location coordA, coordB, coordC;
        // these are used to determine which blocks are being moved
        // if any are non-null, then the corresponding block is being moved
        public Vector3i coordAOffset, coordBOffset, coordCOffset;

        public JsonElement corners, edges, faces, volumes, cables;

        /** These blocks should be replaced when exchanging */
        public List<JsonElement> replaceWhitelist;
        /** These blocks are what gets placed when exchanging */
        public JsonElement replaceWith;

        public static JsonElement saveStack(ItemStack stack) {
            if (stack == null || stack.getItem() == null) {
                return null;
            }

            return MMUtils.toJsonObject(stack.writeToNBT(new NBTTagCompound()));
        }

        public static ItemStack loadStack(JsonElement stack) {
            if (stack == null) return null;

            return ItemStack.loadItemStackFromNBT((NBTTagCompound) MMUtils.toNbt(stack));
        }

        public void setCorners(ItemStack corners) {
            this.corners = saveStack(corners);
        }

        public ItemStack getCorners() {
            return loadStack(corners);
        }

        public void setEdges(ItemStack edges) {
            this.edges = saveStack(edges);
        }

        public ItemStack getEdges() {
            return loadStack(edges);
        }

        public void setFaces(ItemStack faces) {
            this.faces = saveStack(faces);
        }

        public ItemStack getFaces() {
            return loadStack(faces);
        }

        public void setVolumes(ItemStack volumes) {
            this.volumes = saveStack(volumes);
        }

        public ItemStack getVolumes() {
            return loadStack(volumes);
        }

        public void setCables(ItemStack cables) {
            this.cables = saveStack(cables);
        }

        public ItemStack getCables() {
            return loadStack(cables);
        }

        public Location getCoordA(World world, Vector3i lookingAt) {
            if (coordAOffset == null) {
                return coordA;
            } else {
                return new Location(world, new Vector3i(lookingAt).add(coordAOffset));
            }
        }

        public Location getCoordB(World world, Vector3i lookingAt) {
            if (coordBOffset == null) {
                return coordB;
            } else {
                return new Location(world, new Vector3i(lookingAt).add(coordBOffset));
            }
        }

        public Location getCoordC(World world, Vector3i lookingAt) {
            if (coordCOffset == null) {
                return coordC;
            } else {
                return new Location(world, new Vector3i(lookingAt).add(coordCOffset));
            }
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((action == null) ? 0 : action.hashCode());
            result = prime * result + ((blockSelectMode == null) ? 0 : blockSelectMode.hashCode());
            result = prime * result + ((removeMode == null) ? 0 : removeMode.hashCode());
            result = prime * result + ((placeMode == null) ? 0 : placeMode.hashCode());
            result = prime * result + ((shape == null) ? 0 : shape.hashCode());
            result = prime * result + ((coordA == null) ? 0 : coordA.hashCode());
            result = prime * result + ((coordB == null) ? 0 : coordB.hashCode());
            result = prime * result + ((coordC == null) ? 0 : coordC.hashCode());
            result = prime * result + ((coordAOffset == null) ? 0 : coordAOffset.hashCode());
            result = prime * result + ((coordBOffset == null) ? 0 : coordBOffset.hashCode());
            result = prime * result + ((coordCOffset == null) ? 0 : coordCOffset.hashCode());
            result = prime * result + ((corners == null) ? 0 : corners.hashCode());
            result = prime * result + ((edges == null) ? 0 : edges.hashCode());
            result = prime * result + ((faces == null) ? 0 : faces.hashCode());
            result = prime * result + ((volumes == null) ? 0 : volumes.hashCode());
            result = prime * result + ((cables == null) ? 0 : cables.hashCode());
            result = prime * result + ((replaceWhitelist == null) ? 0 : replaceWhitelist.hashCode());
            result = prime * result + ((replaceWith == null) ? 0 : replaceWith.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Config other = (Config) obj;
            if (action != other.action) return false;
            if (blockSelectMode != other.blockSelectMode) return false;
            if (removeMode != other.removeMode) return false;
            if (placeMode != other.placeMode) return false;
            if (shape != other.shape) return false;
            if (coordA == null) {
                if (other.coordA != null) return false;
            } else if (!coordA.equals(other.coordA)) return false;
            if (coordB == null) {
                if (other.coordB != null) return false;
            } else if (!coordB.equals(other.coordB)) return false;
            if (coordC == null) {
                if (other.coordC != null) return false;
            } else if (!coordC.equals(other.coordC)) return false;
            if (coordAOffset == null) {
                if (other.coordAOffset != null) return false;
            } else if (!coordAOffset.equals(other.coordAOffset)) return false;
            if (coordBOffset == null) {
                if (other.coordBOffset != null) return false;
            } else if (!coordBOffset.equals(other.coordBOffset)) return false;
            if (coordCOffset == null) {
                if (other.coordCOffset != null) return false;
            } else if (!coordCOffset.equals(other.coordCOffset)) return false;
            if (corners == null) {
                if (other.corners != null) return false;
            } else if (!corners.equals(other.corners)) return false;
            if (edges == null) {
                if (other.edges != null) return false;
            } else if (!edges.equals(other.edges)) return false;
            if (faces == null) {
                if (other.faces != null) return false;
            } else if (!faces.equals(other.faces)) return false;
            if (volumes == null) {
                if (other.volumes != null) return false;
            } else if (!volumes.equals(other.volumes)) return false;
            if (cables == null) {
                if (other.cables != null) return false;
            } else if (!cables.equals(other.cables)) return false;
            if (replaceWhitelist == null) {
                if (other.replaceWhitelist != null) return false;
            } else if (!replaceWhitelist.equals(other.replaceWhitelist)) return false;
            if (replaceWith == null) {
                if (other.replaceWith != null) return false;
            } else if (!replaceWith.equals(other.replaceWith)) return false;
            return true;
        }
    }

    /**
     * Pins a point to the axis planes around an origin.
     * 
     * @return The pinned point
     */
    public static Vector3i pinToPlanes(Vector3i origin, Vector3i point) {
        int dX = Math.abs(point.x - origin.x);
        int dY = Math.abs(point.y - origin.y);
        int dZ = Math.abs(point.z - origin.z);

        int shortest = GTUtility.min(dX, dY, dZ);

        if (shortest == dX) {
            return new Vector3i(origin.x, point.y, point.z);
        } else if (shortest == dY) {
            return new Vector3i(point.x, origin.y, point.z);
        } else {
            return new Vector3i(point.x, point.y, origin.z);
        }
    }

    /**
     * Pins a point to the normal of the axis plane described by origin,b.
     * 
     * @param origin The origin
     * @param b      A point on an axis plane of origin
     * @param point  The point to pin
     * @return The pinned point on the normal
     */
    public static Vector3i pinToLine(Vector3i origin, Vector3i b, Vector3i point) {
        return switch (new Vector3i(b).sub(origin)
            .minComponent()) {
            case 0 -> new Vector3i(point.x, origin.y, origin.z);
            case 1 -> new Vector3i(origin.x, point.y, origin.z);
            case 2 -> new Vector3i(origin.x, origin.y, point.z);
            default -> throw new AssertionError();
        };
    }

    /**
     * Pins a point to the cardinal axes.
     */
    public static Vector3i pinToAxes(Vector3i origin, Vector3i point) {
        return switch (new Vector3i(point).sub(origin)
            .maxComponent()) {
            case 0 -> new Vector3i(point.x, origin.y, origin.z);
            case 1 -> new Vector3i(origin.x, point.y, origin.z);
            case 2 -> new Vector3i(origin.x, origin.y, point.z);
            default -> throw new AssertionError();
        };
    }

    static enum Shape {

        LINE,
        CUBE,
        SPHERE,
        CYLINDER;

        public boolean requiresC() {
            return switch (this) {
                case LINE, CUBE, SPHERE -> false;
                case CYLINDER -> true;
            };
        }
    }

    static enum PendingAction {
        MOVING_COORDS,
        MARK_COPY_A,
        MARK_COPY_B,
        MARK_CUT_A,
        MARK_CUT_B,
        MARK_PASTE,
        GEOM_SELECTING_BLOCK,
        EXCH_SET_TARGET,
        EXCH_ADD_REPLACE,
        EXCH_SET_REPLACE,
        PICK_CABLE,
    }

    static enum BlockSelectMode {
        CORNERS,
        EDGES,
        FACES,
        VOLUMES,
        ALL,
    }

    static enum BlockRemoveMode {
        NONE,
        REPLACEABLE,
        ALL
    }

    static enum PlaceMode {
        GEOMETRY,
        MOVING,
        COPYING,
        EXCHANGING,
        CABLES,
    }

    /**
     * This represents a block in the world.
     * It stores everything the building algorithm needs to know to place a block.
     */
    static class PendingBlock extends Location {

        public UniqueIdentifier blockId;
        public int metadata;
        public TileAnalysisResult tileData;
        // various sort orders, one is for drawing hints and one is for the build order
        public int renderOrder, buildOrder;

        public transient Item item;
        public transient Block block;

        public PendingBlock(int worldId, int x, int y, int z, ItemStack block) {
            super(worldId, x, y, z);
            setBlock(block);
        }

        public PendingBlock(int worldId, int x, int y, int z, ItemStack block, int renderOrder, int buildOrder) {
            this(worldId, x, y, z, block);
            this.renderOrder = renderOrder;
            this.buildOrder = buildOrder;
        }

        public PendingBlock(int worldId, int x, int y, int z, Block block, int meta) {
            super(worldId, x, y, z);
            setBlock(block, meta);
        }

        /**
         * Clears this block's block but not its position.
         */
        public PendingBlock reset() {
            this.block = null;
            this.item = null;
            this.blockId = null;
            this.metadata = 0;

            return this;
        }

        public PendingBlock setBlock(Block block, int metadata) {
            reset();

            this.blockId = GameRegistry.findUniqueIdentifierFor(block == null ? Blocks.air : block);
            this.metadata = metadata;

            return this;
        }

        /**
         * If the item in the stack isn't an ItemBlock, this just resets the stored block.
         */
        public PendingBlock setBlock(ItemStack stack) {
            reset();

            Optional<Block> block = Optional.ofNullable(stack)
                .map(ItemStack::getItem)
                .map(Block::getBlockFromItem);

            if (block.isPresent()) {
                this.block = block.get();
                this.item = (ItemBlock) Item.getItemFromBlock(block.get());
                this.blockId = GameRegistry.findUniqueIdentifierFor(block.get());
                this.metadata = this.item != null && this.item.getHasSubtypes() ? Items.feather.getDamage(stack) : 0;
            }

            return this;
        }

        public Block getBlock() {
            if (block == null) {
                block = blockId == null ? Blocks.air : GameRegistry.findBlock(blockId.modId, blockId.name);
            }

            return block;
        }

        public Item getItem() {
            if (item == null) {
                Block block = getBlock();

                if (block != null) {
                    item = MMUtils.getItemFromBlock(block, metadata);
                }
            }

            return item;
        }

        public ItemStack toStack() {
            Item item = getItem();

            if (item == null) return null;

            if (item.getHasSubtypes()) {
                return new ItemStack(item, 1, metadata);
            } else {
                return new ItemStack(item, 1, 0);
            }
        }

        public static final Lazy<Block> AE_BLOCK_CABLE = new Lazy<>(
            () -> AEApi.instance()
                .definitions()
                .blocks()
                .multiPart()
                .maybeBlock()
                .get());

        public boolean isFree() {
            Block block = getBlock();

            if (block == Blocks.air) {
                return true;
            }

            if (block == AE_BLOCK_CABLE.get() && tileData != null) {
                return true;
            }

            return false;
        }

        /**
         * Creates a PendingBlock from an existing block in the world.
         */
        public static PendingBlock fromBlock(World world, int x, int y, int z) {
            Block block = world.getBlock(x, y, z);
            int meta = world.getBlockMetadata(x, y, z);

            Item item = MMUtils.getItemFromBlock(block, meta);

            if (item == null) {
                return new PendingBlock(world.provider.dimensionId, x, y, z, Blocks.air, 0);
            }

            block = MMUtils.getBlockFromItem(item, meta);

            meta = item.getHasSubtypes() ? block.getDamageValue(world, x, y, z) : meta;

            return new PendingBlock(world.provider.dimensionId, x, y, z, block, meta);
        }

        /**
         * Creates a PendingBlock from a picked block.
         */
        public static PendingBlock fromPickBlock(World world, EntityPlayer player, MovingObjectPosition hit) {
            if (hit == null || hit.typeOfHit != MovingObjectType.BLOCK) return null;

            return fromBlock(world, hit.blockX, hit.blockY, hit.blockZ);
        }

        /**
         * Checks if two PendingBlocks contain the same Block.
         */
        public static boolean isSameBlock(PendingBlock a, PendingBlock b) {
            if (!Objects.equals(a.blockId, b.blockId)) return false;

            Item item = a.getItem();

            if (item != null && !item.getHasSubtypes()) {
                return true;
            }

            return a.metadata == b.metadata;
        }

        @Override
        public String toString() {
            return "PendingBlock [blockId=" + blockId
                + ", metadata="
                + metadata
                + ", tileData="
                + tileData
                + ", renderOrder="
                + renderOrder
                + ", buildOrder="
                + buildOrder
                + ", x="
                + x
                + ", y="
                + y
                + ", z="
                + z
                + ", worldId="
                + worldId
                + ", world="
                + getWorld()
                + "]";
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = super.hashCode();
            result = prime * result + ((blockId == null) ? 0 : blockId.hashCode());
            result = prime * result + metadata;
            result = prime * result + ((tileData == null) ? 0 : tileData.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!super.equals(obj)) return false;
            if (getClass() != obj.getClass()) return false;
            PendingBlock other = (PendingBlock) obj;
            if (blockId == null) {
                if (other.blockId != null) return false;
            } else if (!blockId.equals(other.blockId)) return false;
            if (metadata != other.metadata) return false;
            if (tileData == null) {
                if (other.tileData != null) return false;
            } else if (!tileData.equals(other.tileData)) return false;
            return true;
        }

        /**
         * A comparator for sorting blocks prior to building.
         */
        public static Comparator<PendingBlock> getComparator() {
            Comparator<UniqueIdentifier> blockId = Comparator.nullsFirst(
                Comparator.comparing((UniqueIdentifier id) -> id.modId)
                    .thenComparing(id -> id.name));

            return Comparator.comparingInt((PendingBlock b) -> b.buildOrder)
                .thenComparing(Comparator.nullsFirst(Comparator.comparing(b -> b.blockId, blockId)))
                .thenComparingInt(b -> b.metadata)
                .thenComparingLong(b -> {
                    int chunkX = b.x >> 4;
                    int chunkZ = b.z >> 4;

                    return chunkX | (chunkZ << 32);
                });
        }
    }

    /**
     * Represents a location in a world.
     * Can probably be improved, but it's not a big problem yet since these aren't meant to be kept around for very
     * long.
     */
    static class Location {

        public int worldId;
        public int x, y, z;

        public Location() {

        }

        public Location(int worldId, int x, int y, int z) {
            this.worldId = worldId;
            this.x = x;
            this.y = y;
            this.z = z;
        }

        public Location(@Nonnull World world, int x, int y, int z) {
            this(world.provider.dimensionId, x, y, z);
        }

        public Location(@Nonnull World world, Vector3i v) {
            this(world, v.x, v.y, v.z);
        }

        @Override
        public String toString() {
            return String.format("X=%,d Y=%,d Z=%,d", x, y, z);
        }

        public Vector3i toVec() {
            return new Vector3i(x, y, z);
        }

        public boolean isInWorld(@Nonnull World world) {
            return world.provider.dimensionId == worldId;
        }

        public int distanceTo2(Location other) {
            int dx = x - other.x;
            int dy = y - other.y;
            int dz = z - other.z;
            return dx * dx + dy * dy + dz * dz;
        }

        public double distanceTo(Location other) {
            return Math.sqrt(distanceTo2(other));
        }

        public World getWorld() {
            if (FMLCommonHandler.instance()
                .getSide() == Side.CLIENT) {
                return getWorldClient();
            } else {
                return DimensionManager.getWorld(this.worldId);
            }
        }

        @SideOnly(Side.CLIENT)
        private World getWorldClient() {
            World world = Minecraft.getMinecraft().theWorld;

            return world.provider.dimensionId == this.worldId ? world : null;
        }

        public Location offset(ForgeDirection dir) {
            this.x += dir.offsetX;
            this.y += dir.offsetY;
            this.z += dir.offsetZ;
            return this;
        }

        public Location offset(int dx, int dy, int dz) {
            this.x += dx;
            this.y += dy;
            this.z += dz;
            return this;
        }

        public Location clone() {
            return new Location(worldId, x, y, z);
        }

        /**
         * Checks if two locations are compatible (in the same world).
         */
        public static boolean areCompatible(Location a, Location b) {
            if (a == null || b == null) return false;

            if (a.worldId != b.worldId) return false;

            return true;
        }

        /**
         * Checks if three locations are compatible (in the same world).
         */
        public static boolean areCompatible(Location a, Location b, Location c) {
            if (a == null || b == null || c == null) return false;

            if (a.worldId != b.worldId) return false;
            if (a.worldId != c.worldId) return false;

            return true;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + worldId;
            result = prime * result + x;
            result = prime * result + y;
            result = prime * result + z;
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            Location other = (Location) obj;
            if (worldId != other.worldId) return false;
            if (x != other.x) return false;
            if (y != other.y) return false;
            if (z != other.z) return false;
            return true;
        }
    }
}
