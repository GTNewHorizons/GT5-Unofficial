package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;

import appeng.api.storage.data.IAEItemStack;
import appeng.util.item.AEItemStack;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;
import gregtech.common.items.matterManipulator.ItemMatterManipulator.ManipulatorTier;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import it.unimi.dsi.fastutil.Pair;

public class PendingBuild extends AbstractBuildable {

    public LinkedList<PendingBlock> pendingBlocks;

    private boolean printedProtectedBlockWarning = false;

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

        while (toPlace.size() < tier.placeSpeed && pendingBlocks.size() > 0) {
            PendingBlock next = pendingBlocks.getFirst();

            int x = next.x, y = next.y, z = next.z;

            int chunkX = x >> 4;
            int chunkZ = z >> 4;

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

            // spotless:off
            if (!world.canMineBlock(player, x, y, z) || MinecraftServer.getServer().isBlockProtected(world, x, y, z, player)) {
                // spotless:on
                if (!printedProtectedBlockWarning) {
                    GTUtility.sendChatToPlayer(
                        player,
                        EnumChatFormatting.GOLD + "Tried to break/place a block in a protected area!");
                    printedProtectedBlockWarning = true;
                }

                pendingBlocks.removeFirst();
                continue;
            }

            if (!toPlace.isEmpty() && !MMUtils.areBlocksBasicallyEqual(next, toPlace.get(0))) {
                break;
            }

            PendingBlock existing = PendingBlock.fromBlock(world, x, y, z);

            if (PendingBlock.isSameBlock(next, existing)) {
                PendingBlock block = pendingBlocks.removeFirst();

                if (block.tileData != null && tier.hasCap(ItemMatterManipulator.ALLOW_CONFIGURING)) {
                    applyContext.pendingBlock = block;
                    block.tileData.apply(applyContext);
                }

                continue;
            }

            Block existingBlock = existing == null ? Blocks.air : existing.getBlock();

            if (existingBlock.getBlockHardness(world, x, y, z) < 0) {
                pendingBlocks.removeFirst();
                continue;
            }

            boolean canPlace = switch (state.config.removeMode) {
                case NONE -> existingBlock.isAir(world, x, y, z);
                case REPLACEABLE -> existingBlock.isReplaceable(world, x, y, z);
                case ALL -> true;
            };

            if (!canPlace) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

                if (shuffleCount > pendingBlocks.size()) {
                    break;
                } else {
                    continue;
                }
            }

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

            if (!next.getBlock()
                .canPlaceBlockAt(world, next.x, next.y, next.z)) {
                pendingBlocks.addLast(pendingBlocks.removeFirst());
                shuffleCount++;

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

        actuallyGivePlayerStuff();

        if (toPlace.isEmpty()) {
            if (!pendingBlocks.isEmpty()) {
                GTUtility.sendErrorToPlayer(player, "Could not place " + pendingBlocks.size() + " remaining blocks.");
            } else {
                GTUtility.sendInfoToPlayer(player, "Finished placing blocks.");
            }

            player.setItemInUse(null, 0);
            return;
        }

        if (!toPlace.get(0)
            .isFree()) {
            ItemStack item = toPlace.get(0)
                .toStack();
            if (item != null) {
                item.stackSize = toPlace.size();

                List<IAEItemStack> extracted = tryConsumeItems(Arrays.asList(AEItemStack.create(item)), CONSUME_PARTIAL)
                    .right();

                ItemStack extractedStack = extracted.size() == 1 ? extracted.get(0)
                    .getItemStack() : null;

                int extractedAmount = extractedStack == null ? 0 : extractedStack.stackSize;

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
            playSound(world, pending.x, pending.y, pending.z, SoundResource.MOB_ENDERMEN_PORTAL);

            ItemBlock block = pending.getItem();

            if (block != null) {
                block.placeBlockAt(
                    pending.toStack(),
                    player,
                    player.worldObj,
                    pending.x,
                    pending.y,
                    pending.z,
                    0,
                    0,
                    0,
                    0,
                    pending.metadata);

                if (pending.tileData != null && tier.hasCap(ItemMatterManipulator.ALLOW_CONFIGURING)) {
                    applyContext.pendingBlock = pending;
                    pending.tileData.apply(applyContext);
                }
            }
        }

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
