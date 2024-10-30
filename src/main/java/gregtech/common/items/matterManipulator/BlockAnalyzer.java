package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;

import org.joml.Vector3i;

import com.mojang.authlib.GameProfile;

import appeng.api.storage.data.IAEItemStack;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GTUtility.FluidId;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import it.unimi.dsi.fastutil.Pair;

public class BlockAnalyzer {

    private BlockAnalyzer() {}

    public static @Nullable TileAnalysisResult analyze(IBlockAnalysisContext context) {
        TileEntity te = context.getTileEntity();

        if (te == null) {
            return null;
        }

        TileAnalysisResult result = new TileAnalysisResult(context, te);

        return result.doesAnything() ? result : null;
    }

    public static RegionAnalysis analyzeRegion(World world, Location a, Location b, boolean checkTiles) {
        if (a == null || b == null || world.provider.dimensionId != a.worldId || a.worldId != b.worldId) return null;

        long pre = System.nanoTime();

        RegionAnalysis analysis = new RegionAnalysis();

        Vector3i deltas = MMUtils.getRegionDeltas(a, b);
        analysis.deltas = deltas;

        analysis.blocks = new ArrayList<>();

        BlockAnalysisContext context = new BlockAnalysisContext(world);

        for (Vector3i voxel : MMUtils.getBlocksInBB(a, deltas)) {
            PendingBlock pending = PendingBlock.fromBlock(world, voxel.x, voxel.y, voxel.z);

            if (pending == null) {
                continue;
            }

            if (checkTiles) {
                context.voxel = voxel;
                TileAnalysisResult tile = analyze(context);

                if (tile != null && tile.doesAnything()) {
                    pending.tileData = tile;
                }
            }

            pending.x -= a.x;
            pending.y -= a.y;
            pending.z -= a.z;

            analysis.blocks.add(pending);
        }

        long post = System.nanoTime();

        System.out.println("Analysis took " + (post - pre) / 1e6 + " ms");

        return analysis;
    }

    public static class RegionAnalysis {

        public Vector3i deltas;
        public List<PendingBlock> blocks;
    }

    public static interface IBlockAnalysisContext {

        public EntityPlayer getFakePlayer();

        public TileEntity getTileEntity();
    }

    public static class BlockAnalysisContext implements IBlockAnalysisContext {

        public World world;
        public EntityPlayer fakePlayer;
        public Vector3i voxel;

        public BlockAnalysisContext(World world) {
            this.world = world;
        }

        @Override
        public EntityPlayer getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new FakePlayer(
                    (WorldServer) world,
                    new GameProfile(UUID.randomUUID(), "BlockAnalyzer Fake Player"));
            }

            return fakePlayer;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(voxel.x, voxel.y, voxel.z);
        }
    }

    public static interface IBlockApplyContext extends IBlockAnalysisContext, IPseudoInventory {

        public EntityPlayer getRealPlayer();

        public boolean tryApplyAction(double complexity);

        public void warn(String message);

        public void error(String message);
    }

    public static class BlockApplyContext implements IBlockApplyContext {

        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public PendingBuild build;
        public ItemStack manipulator;
        public FakePlayer fakePlayer;

        public static final double EU_PER_ACTION = 8192;

        @Override
        public EntityPlayer getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new FakePlayer((WorldServer) player.getEntityWorld(), player.getGameProfile());
            }

            return fakePlayer;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        @Override
        public EntityPlayer getRealPlayer() {
            return player;
        }

        @Override
        public boolean tryApplyAction(double complexity) {
            return build.tryConsumePower(manipulator, x, y, z, EU_PER_ACTION * complexity);
        }

        @Override
        public Pair<Boolean, List<IAEItemStack>> tryConsumeItems(List<IAEItemStack> items, int flags) {
            return build.tryConsumeItems(items, flags);
        }

        @Override
        public void givePlayerItems(ItemStack... items) {
            build.givePlayerItems(items);
        }

        @Override
        public void givePlayerFluids(FluidStack... fluids) {
            build.givePlayerFluids(fluids);
        }

        @Override
        public void warn(String message) {
            GTUtility.sendChatToPlayer(player, String.format("§cWarning at block %d, %d, %d: %s§r", x, y, z, message));
        }

        @Override
        public void error(String message) {
            GTUtility.sendChatToPlayer(player, String.format("§cError at block %d, %d, %d: %s§r", x, y, z, message));
        }
    }

    private static class BlockItemCheckContext implements IBlockApplyContext {

        public World world;
        public int x, y, z;
        public EntityPlayer player;
        public FakePlayer fakePlayer;

        public HashMap<ItemId, Long> requiredItems = new HashMap<>();

        public HashMap<ItemId, Long> storedItems = new HashMap<>();
        public HashMap<FluidId, Long> storedFluids = new HashMap<>();

        @Override
        public EntityPlayer getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new FakePlayer((WorldServer) player.getEntityWorld(), player.getGameProfile());
            }

            return fakePlayer;
        }

        @Override
        public TileEntity getTileEntity() {
            return world.getTileEntity(x, y, z);
        }

        @Override
        public EntityPlayer getRealPlayer() {
            return player;
        }

        @Override
        public boolean tryApplyAction(double complexity) {
            return true;
        }

        @Override
        public Pair<Boolean, List<IAEItemStack>> tryConsumeItems(List<IAEItemStack> items, int flags) {
            boolean simulate = (flags & CONSUME_SIMULATED) != 0;
            boolean fuzzy = (flags & CONSUME_FUZZY) != 0;

            List<IAEItemStack> extractedItems = new ArrayList<>();

            for (IAEItemStack req : items) {
                if (req.getStackSize() == 0) {
                    continue;
                }

                if (!fuzzy) {
                    ItemId id = ItemId.create(req.getItemStack());

                    long amtInPending = storedItems.getOrDefault(id, 0l);

                    long toRemove = Math.min(amtInPending, req.getStackSize());

                    if (toRemove > 0) {
                        extractedItems.add(
                            req.copy()
                                .setStackSize(toRemove));
                        amtInPending -= toRemove;
                        req.decStackSize(toRemove);

                        if (!simulate) {
                            if (amtInPending == 0) {
                                storedItems.remove(id);
                            } else {
                                storedItems.put(id, amtInPending);
                            }
                        }
                    }
                } else {
                    var iter = storedItems.entrySet()
                        .iterator();

                    while (iter.hasNext()) {
                        var e = iter.next();

                        if (e.getValue() == null || e.getValue() == 0) {
                            continue;
                        }

                        ItemStack stack = e.getKey()
                            .getItemStack();

                        if (stack.getItem() != req.getItem()) {
                            continue;
                        }

                        if (stack.getHasSubtypes() && Items.feather.getDamage(stack) != req.getItemDamage()) {
                            continue;
                        }

                        long amtInPending = e.getValue();
                        long toRemove = Math.min(amtInPending, req.getStackSize());

                        if (toRemove > 0) {
                            extractedItems.add(
                                req.copy()
                                    .setStackSize(toRemove));
                            amtInPending -= toRemove;
                            req.decStackSize(toRemove);

                            if (!simulate) {
                                if (amtInPending == 0) {
                                    iter.remove();
                                } else {
                                    e.setValue(amtInPending);
                                }
                            }
                        }
                    }
                }

                ItemId id;

                if (fuzzy && !req.getItem()
                    .getHasSubtypes()) {
                    id = ItemId.createAsWildcard(req.getItemStack());
                } else {
                    id = ItemId.create(req.getItemStack());
                }

                requiredItems.merge(id, req.getStackSize(), Long::sum);
            }

            return Pair.of(true, items);
        }

        @Override
        public void givePlayerItems(ItemStack... items) {
            for (ItemStack item : items) {
                storedItems.merge(ItemId.createWithStackSize(item), (long) -item.stackSize, Long::sum);
            }
        }

        @Override
        public void givePlayerFluids(FluidStack... fluids) {
            for (FluidStack fluid : fluids) {
                storedFluids.merge(FluidId.createWithAmount(fluid), (long) -fluid.amount, Long::sum);
            }
        }

        @Override
        public void warn(String message) {
            GTUtility.sendChatToPlayer(player, String.format("§cWarning at block %d, %d, %d: %s§r", x, y, z, message));
        }

        @Override
        public void error(String message) {
            GTUtility.sendChatToPlayer(player, String.format("§cError at block %d, %d, %d: %s§r", x, y, z, message));
        }
    }

    public static class RequiredItemAnalysis {

        public HashMap<ItemId, Long> requiredItems;
        public HashMap<ItemId, Long> storedItems;
        public HashMap<FluidId, Long> storedFluids;
    }

    public static RequiredItemAnalysis getRequiredItemsForBuild(EntityPlayer player, List<PendingBlock> blocks,
        boolean fromScratch) {
        BlockItemCheckContext context = new BlockItemCheckContext();
        context.player = player;
        context.world = player.getEntityWorld();

        for (PendingBlock block : blocks) {
            if (block.isInWorld(context.world)) {
                boolean isNew = true;

                if (!fromScratch && !context.world.isAirBlock(block.x, block.y, block.z)) {
                    PendingBlock existing = PendingBlock.fromBlock(context.world, block.x, block.y, block.z);

                    if (PendingBlock.isSameBlock(existing, block)) {
                        isNew = false;
                    } else {
                        if (!block.isFree()) {
                            context.givePlayerItems(existing.toStack());
                        }
                    }
                }

                if (isNew && !block.isFree()) {
                    context.tryConsumeItems(block.toStack());
                }

                context.x = block.x;
                context.y = block.y;
                context.z = block.z;

                if (block.tileData != null) {
                    if (isNew) {
                        block.tileData.getRequiredItemsForNewBlock(context);
                    } else {
                        block.tileData.getRequiredItemsForExistingBlock(context);
                    }
                }
            }
        }

        RequiredItemAnalysis analysis = new RequiredItemAnalysis();
        analysis.requiredItems = context.requiredItems;
        analysis.storedItems = context.storedItems;
        analysis.storedFluids = context.storedFluids;

        return analysis;
    }
}
