package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fluids.FluidStack;

public class PendingBuild extends AbstractBuildable {

    public LinkedList<PendingBlock> pendingBlocks;
    
    private boolean printedProtectedBlockWarning = false;

    @Override
    public void tryPlaceBlocks(ItemStack stack, EntityPlayer player) {
        List<PendingBlock> toPlace = new ArrayList<>(tier.mPlaceSpeed);

        Integer lastChunkX = null, lastChunkZ = null;
        int shuffleCount = 0;

        World world = placingPlayer.worldObj;

        PendingBuildApplyContext applyContext = new PendingBuildApplyContext(stack);

        while (toPlace.size() < tier.mPlaceSpeed && pendingBlocks.size() > 0) {
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
                    player.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.GOLD + "Tried to break/place a block in a protected area!"));
                    printedProtectedBlockWarning = true;
                }

                pendingBlocks.removeFirst();
                continue;
            }

            if (!toPlace.isEmpty() && !areBlocksBasicallyEqual(next, toPlace.get(0))) {
                break;
            }

            PendingBlock existing = PendingBlock.fromBlock(world, x, y, z);

            if (PendingBlock.isSameBlock(next, existing)) {
                PendingBlock block = pendingBlocks.removeFirst();

                if (block.tileData != null) {
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
                if (!tryConsumePower(stack, existing)) {
                    player.addChatMessage(
                        new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
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
                player.addChatMessage(
                    new ChatComponentText(EnumChatFormatting.RED + "Matter Manipulator ran out of EU."));
                break;
            }

            toPlace.add(pendingBlocks.removeFirst());
        }

        actuallyGivePlayerStuff();

        if (toPlace.isEmpty()) {
            if (!pendingBlocks.isEmpty()) {
                GTUtility.sendErrorToPlayer(player, "Could not place " + pendingBlocks.size() + " remaining blocks.");
            } else {
                GTUtility.sendChatToPlayer(player, EnumChatFormatting.ITALIC.toString() + EnumChatFormatting.GRAY + "Finished placing blocks.");
            }

            player.setItemInUse(null, 0);
            return;
        }

        if (!toPlace.get(0).isFree()) {
            ItemStack item = toPlace.get(0).toStack();
            if (item != null) {
                item.stackSize = toPlace.size();

                List<ItemStack> rejected = tryConsumeItemsAllowPartial(Arrays.asList(item));

                if (!rejected.isEmpty()) {
                    ItemStack rejectedStack = rejected.get(0);

                    toPlace = toPlace.subList(0, toPlace.size() - rejectedStack.stackSize);

                    player.addChatMessage(
                        new ChatComponentText(
                            EnumChatFormatting.RED + "Could not find item, the corresponding blocks will be skipped: "
                                + rejectedStack.getDisplayName()
                                + " x "
                                + rejectedStack.stackSize));
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

                if (pending.tileData != null) {
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
                    (WorldServer) PendingBuild.this.placingPlayer.worldObj,
                    PendingBuild.this.placingPlayer.getGameProfile());
            }

            return fakePlayer;
        }

        @Override
        public TileEntity getTileEntity() {
            if (pendingBlock.isInWorld(placingPlayer.worldObj)) {
                return placingPlayer.worldObj.getTileEntity(pendingBlock.x, pendingBlock.y, pendingBlock.z);
            } else {
                return null;
            }
        }

        @Override
        public EntityPlayer getRealPlayer() {
            return placingPlayer;
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
        public boolean tryConsumeItems(ItemStack... items) {
            return PendingBuild.this.tryConsumeItems(items);
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
                placingPlayer,
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
                placingPlayer,
                String.format(
                    "§cError at block %d, %d, %d: %s§r",
                    pendingBlock.x,
                    pendingBlock.y,
                    pendingBlock.z,
                    message));
        }
    }
}
