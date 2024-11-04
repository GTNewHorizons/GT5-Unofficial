package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import it.unimi.dsi.fastutil.Pair;

/**
 * Handles all building logic.
 */
public class PendingBuild extends AbstractBuildable {

    private LinkedList<PendingBlock> pendingBlocks;

    public PendingBuild(EntityPlayer player, NBTState state, ManipulatorTier tier,
        LinkedList<PendingBlock> pendingBlocks) {
        super(player, state, tier);
        this.pendingBlocks = pendingBlocks;
    }

    @Override
    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        List<PendingBlock> toPlace = new ArrayList<>(tier.placeSpeed);

        Integer lastChunkX = null, lastChunkZ = null;
        int shuffleCount = 0;

        World world = player.worldObj;

        PendingBuildApplyContext applyContext = new PendingBuildApplyContext(stack);

        // check every pending block that's left
        while (toPlace.size() < tier.placeSpeed && pendingBlocks.size() > 0) {
            PendingBlock next = pendingBlocks.getFirst();

            int x = next.x, y = next.y, z = next.z;

            int chunkX = x >> 4;
            int chunkZ = z >> 4;

            // if this block's chunk isn't loaded, ignore it completely
            if (!Objects.equals(chunkX, lastChunkX) || !Objects.equals(chunkZ, lastChunkZ)) {
                if (!world.getChunkProvider()
                    .chunkExists(chunkX, chunkZ)) {
                    pendingBlocks.removeFirst();
                    continue;
                } else {
                    lastChunkX = chunkX;
                    lastChunkZ = chunkZ;
                }
            }

            // if this block is protected, ignore it completely and print a warning
            if (!isEditable(world, x, y, z)) {
                pendingBlocks.removeFirst();
                continue;
            }

            // if this block is different from the last one, stop checking blocks
            // since the pending blocks are sorted by their contained block, this is usually true
            if (!toPlace.isEmpty() && !PendingBlock.isSameBlock(next, toPlace.get(0))) {
                break;
            }

            PendingBlock existing = PendingBlock.fromBlock(world, x, y, z);

            // if the existing block is the same as the one we're trying to place, just apply its tile data
            if (PendingBlock.isSameBlock(next, existing)) {
                PendingBlock block = pendingBlocks.removeFirst();

                if (block.getItem() != null && !block.getItem()
                    .getHasSubtypes()) {
                    if (world.getBlockMetadata(block.x, block.y, block.z) != block.metadata) {
                        world.setBlockMetadataWithNotify(block.x, block.y, block.z, block.metadata, 3);
                    }
                }

                if (block.tileData != null && tier.hasCap(ItemMatterManipulator.ALLOW_CONFIGURING)) {
                    applyContext.pendingBlock = block;
                    block.tileData.apply(applyContext);
                }

                continue;
            }

            Block existingBlock = existing == null ? Blocks.air : existing.getBlock();

            // checks if the existing block is removable
            boolean canPlace = switch (state.config.removeMode) {
                case NONE -> existingBlock.isAir(world, x, y, z);
                case REPLACEABLE -> existingBlock.isReplaceable(world, x, y, z);
                case ALL -> true;
            };

            canPlace &= existingBlock.getBlockHardness(world, x, y, z) >= 0;

            // we don't want to remove these even though they'll never be placed because we want to see how many blocks
            // couldn't be placed
            if (!canPlace) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            // if there's an existing block then remove it if possible
            if (!existingBlock.isAir(world, x, y, z)) {
                if (!tier.hasCap(ItemMatterManipulator.ALLOW_REMOVING)) {
                    pendingBlocks.removeFirst();
                    continue;
                }

                if (!tryConsumePower(stack, existing)) {
                    GTUtility.sendErrorToPlayer(player, "Matter Manipulator ran out of EU.");
                    break;
                }

                removeBlock(world, x, y, z, existingBlock, existing == null ? 0 : existing.metadata);
            }

            // check block dependencies for things like levers
            // if we can't place this block, shuffle it to the back of the list
            if (!next.getBlock()
                .canPlaceBlockAt(world, next.x, next.y, next.z)) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                // if we've shuffled every block, then we'll never be able to place any of them
                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

            if (!tryConsumePower(stack, next)) {
                GTUtility.sendErrorToPlayer(player, "Matter Manipulator ran out of EU.");
                break;
            }

            toPlace.add(pendingBlocks.removeFirst());
        }

        // check if we could place any blocks
        if (toPlace.isEmpty()) {
            if (!pendingBlocks.isEmpty()) {
                GTUtility.sendErrorToPlayer(player, "Could not place " + pendingBlocks.size() + " remaining blocks.");
            } else {
                GTUtility.sendInfoToPlayer(player, "Finished placing blocks.");
            }
            return;
        }

        // if the block we're placing isn't free (ae cable busses) we need to consume it
        PendingBlock first = toPlace.get(0);
        if (!first.isFree()) {
            ItemStack item = first.toStack();

            if (item != null) {
                item.stackSize = toPlace.size();

                List<IAEItemStack> extracted = tryConsumeItems(Arrays.asList(AEItemStack.create(item)), CONSUME_PARTIAL)
                    .right();

                ItemStack extractedStack = extracted.size() == 1 ? extracted.get(0)
                    .getItemStack() : null;

                int extractedAmount = extractedStack == null ? 0 : extractedStack.stackSize;

                // if we only consumed some of the blocks, only place as many as we got
                if (extractedAmount < item.stackSize) {
                    GTUtility.sendErrorToPlayer(
                        player,
                        "Could not find item, the corresponding blocks will be skipped: " + item.getDisplayName()
                            + " x "
                            + (item.stackSize - extractedAmount));

                    toPlace = toPlace.subList(0, extractedAmount);
                }
            }
        }

        for (PendingBlock pending : toPlace) {
            int x = pending.x;
            int y = pending.y;
            int z = pending.z;

            playSound(world, x, y, z, SoundResource.MOB_ENDERMEN_PORTAL);

            Block block = pending.getBlock();
            Item item = pending.getItem();

            if (item != null) {
                int metadata = item.getHasSubtypes() ? item.getMetadata(pending.metadata) : pending.metadata;

                if (item instanceof ItemBlock itemBlock) {
                    itemBlock.placeBlockAt(
                        pending.toStack(),
                        player,
                        player.worldObj,
                        x,
                        y,
                        z,
                        ForgeDirection.UNKNOWN.ordinal(),
                        0,
                        0,
                        0,
                        metadata);
                } else {
                    if (!world.setBlock(x, y, z, block, metadata, 3)) {
                        continue;
                    }

                    if (world.getBlock(x, y, z) == block) {
                        block.onBlockPlacedBy(world, x, y, z, player, stack);
                        block.onPostBlockPlaced(world, x, y, z, metadata);
                    }
                }

                if (!item.getHasSubtypes()) {
                    world.setBlockMetadataWithNotify(x, y, z, metadata, 3);
                }

                if (pending.tileData != null && tier.hasCap(ItemMatterManipulator.ALLOW_CONFIGURING)) {
                    applyContext.pendingBlock = pending;
                    pending.tileData.apply(applyContext);
                }

                world.notifyBlockOfNeighborChange(x, y, z, Blocks.air);
            }
        }

        actuallyGivePlayerStuff();
        playSounds();
    }

    private class PendingBuildApplyContext implements IBlockApplyContext {

        public static final double EU_PER_ACTION = 8192;

        private EntityPlayer fakePlayer;

        public ItemStack manipulatorItemStack;
        public PendingBlock pendingBlock;

        public PendingBuildApplyContext(ItemStack manipulatorItemStack) {
            this.manipulatorItemStack = manipulatorItemStack;
        }

        @Override
        public EntityPlayer getFakePlayer() {
            if (fakePlayer == null) {
                fakePlayer = new FakePlayer(
                    (WorldServer) PendingBuild.this.player.worldObj,
                    PendingBuild.this.player.getGameProfile());
            }

            return fakePlayer;
        }

        @Override
        public TileEntity getTileEntity() {
            if (pendingBlock.isInWorld(player.worldObj)) {
                return player.worldObj.getTileEntity(pendingBlock.x, pendingBlock.y, pendingBlock.z);
            } else {
                return null;
            }
        }

        @Override
        public EntityPlayer getRealPlayer() {
            return player;
        }

        @Override
        public boolean tryApplyAction(double complexity) {
            return PendingBuild.this.tryConsumePower(
                manipulatorItemStack,
                pendingBlock.x,
                pendingBlock.y,
                pendingBlock.z,
                EU_PER_ACTION * complexity);
        }

        @Override
        public Pair<Boolean, List<IAEItemStack>> tryConsumeItems(List<IAEItemStack> items, int flags) {
            return PendingBuild.this.tryConsumeItems(items, flags);
        }

        @Override
        public void givePlayerItems(ItemStack... items) {
            PendingBuild.this.givePlayerItems(items);
        }

        @Override
        public void givePlayerFluids(FluidStack... fluids) {
            PendingBuild.this.givePlayerFluids(fluids);
        }

        @Override
        public void warn(String message) {
            GTUtility.sendChatToPlayer(
                player,
                String.format(
                    "§cWarning at block %d, %d, %d: %s§r",
                    pendingBlock.x,
                    pendingBlock.y,
                    pendingBlock.z,
                    message));
        }

        @Override
        public void error(String message) {
            GTUtility.sendChatToPlayer(
                player,
                String.format(
                    "§cError at block %d, %d, %d: %s§r",
                    pendingBlock.x,
                    pendingBlock.y,
                    pendingBlock.z,
                    message));
        }
    }
}
