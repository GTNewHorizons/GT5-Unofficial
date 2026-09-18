package gregtech.common.items.tools;

import java.util.Arrays;
import java.util.function.BooleanSupplier;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import appeng.api.parts.IPartHost;
import appeng.api.util.IOrientable;
import appeng.tile.misc.TileInterface;
import gregtech.GTLoggers;
import gregtech.api.enums.SoundResource;
import gregtech.api.util.GTUtil;
import gregtech.api.util.GTUtility;
import ic2.api.tile.IWrenchable;
import ic2.core.block.BlockRubWood;

/**
 * The "right click a block to turn it" half of a wrench.
 * <p/>
 * This used to live in {@code BehaviourWrench}, hung off {@link gregtech.api.items.MetaBaseItem}'s behaviour registry.
 * The wrench is no longer a {@code MetaGeneratedTool} metadata, so the logic moved here, where a plain item can call
 * it directly.
 */
public final class WrenchRotation {

    private WrenchRotation() {}

    /**
     * Tries to rotate the block the player clicked.
     *
     * @return whether the click was consumed.
     */
    public static boolean rotate(ToolWrenchItem item, ItemStack stack, EntityPlayer player, World world, int x, int y,
        int z, ForgeDirection side, float hitX, float hitY, float hitZ) {
        final Block block = world.getBlock(x, y, z);
        if (block == null) return false;

        final int meta = world.getBlockMetadata(x, y, z);
        final short targetSideOrdinal = (short) GTUtility.determineWrenchingSide(side, hitX, hitY, hitZ)
            .ordinal();
        final TileEntity tileEntity = world.getTileEntity(x, y, z);

        final Handler handler = new Handler(
            block,
            meta,
            targetSideOrdinal,
            tileEntity,
            player,
            world,
            x,
            y,
            z,
            stack,
            item);

        try {
            return handler.handle() && !world.isRemote;
        } catch (Exception e) {
            GTLoggers.GT_FML_LOGGER.error("Error wrenching", e);
        }
        return false;
    }

    private static class Handler {

        boolean handle() {
            ForgeDirection direction = ForgeDirection.getOrientation(targetSideOrdinal);

            // AE2 logic
            // default to change the up facing
            // sneak to change the forward facing
            if (tileEntity instanceof IOrientable orientable) {
                if (!orientable.canBeRotated()) return false;
                ForgeDirection front = orientable.getForward();
                ForgeDirection up = orientable.getUp();

                // mainly for me-interfaces, whose initial orientation is UNKNOWN
                if (front == ForgeDirection.UNKNOWN) {
                    if (direction == ForgeDirection.UP || direction == ForgeDirection.DOWN)
                        front = ForgeDirection.NORTH;
                    else front = ForgeDirection.UP;
                }

                ForgeDirection back = front.getOpposite();
                ForgeDirection down = up.getOpposite();

                if (tileEntity instanceof TileInterface) {
                    if (player.isSneaking()) return false;
                    if (direction == down) {
                        return doWrenchOperation(() -> {
                            orientable.setOrientation(ForgeDirection.UNKNOWN, ForgeDirection.UNKNOWN);
                            return true;
                        });
                    }
                    // interface's up-side is opposite to the arrow on texture
                    // make it intuitive by rotating it to the opposite side.
                    direction = direction.getOpposite();
                    up = up.getOpposite();
                } else if (direction == up || direction == front) {
                    // rotate around the direction axis
                    final var tempFront = front;
                    final var tempUp = up;
                    if (!player.isSneaking() && direction == up) return doWrenchOperation(() -> {
                        orientable.setOrientation(tempFront.getRotation(tempUp), tempUp);
                        return true;
                    });
                    if (player.isSneaking() && direction == front) return doWrenchOperation(() -> {
                        orientable.setOrientation(
                            tempFront,
                            tempUp.getRotation(tempFront)
                                .getRotation(tempFront));
                        return true;
                    });
                }

                if (player.isSneaking()) {
                    if (direction == up || direction == down) {
                        orientable.setOrientation(direction, down.getRotation(front.getRotation(direction)));
                    } else orientable.setOrientation(direction, up);
                } else {
                    if (direction == front || direction == back) {
                        orientable.setOrientation(back.getRotation(up.getRotation(direction)), direction);
                    } else orientable.setOrientation(front, direction);
                }

                return damageWrench();
            }
            if (world.isRemote) return false;
            // IC2 Wrenchable
            if (tileEntity instanceof IWrenchable wrenchable) {
                if (wrenchable.wrenchCanSetFacing(player, targetSideOrdinal)) {
                    return doWrenchOperation(() -> {
                        wrenchable.setFacing(targetSideOrdinal);
                        return true;
                    });
                }
                return false;
            }

            if (block == Blocks.powered_repeater || block == Blocks.unpowered_repeater
                || block == Blocks.powered_comparator
                || block == Blocks.unpowered_comparator) return setBlockMeta(meta / 4 * 4 + (meta % 4 + 1) % 4);

            // hopper cannot face sky
            if (block == Blocks.hopper && targetSideOrdinal != 1) return setBlockMeta(targetSideOrdinal);

            if (isVanillaAllSideRotatable(block)) if (meta < 6) return setBlockMeta(targetSideOrdinal);

            // blocks like chests and furnaces have only four directions
            if (isVanillaCantFaceAxisY(block)) {
                if (direction.offsetY != 0) return false;
                if (isVanillaChest(block)) {
                    // large chests needs special handling
                    return doWrenchOperation(
                        () -> GTUtil.setVanillaChestDirection(world, x, y, z, targetSideOrdinal, block, false));
                }
                return setBlockMeta(targetSideOrdinal);
            }
            if (tileEntity instanceof IPartHost) return false;

            final int logWoodId = OreDictionary.getOreID("logWood");
            if (Arrays.stream(OreDictionary.getOreIDs(new ItemStack(block)))
                .anyMatch(id -> id == logWoodId)) {
                // IC2 rubber logs carry more info than just side in the meta
                if (!(block instanceof BlockRubWood)) {
                    // The meta just work
                    return setBlockMeta((meta + 4) % 12);
                }
            }

            // vanilla block rotate logic
            if ((Arrays.asList(block.getValidRotations(world, x, y, z))
                .contains(direction))) return rotateBlock(direction);
            return false;

            // GT blocks' rotations are done by blocks themselves after this returning false
        }

        private final Block block;
        private final short targetSideOrdinal;
        private final TileEntity tileEntity;
        private final EntityPlayer player;
        private final World world;
        private final int x, y, z, meta;
        private final ItemStack stack;

        private final ToolWrenchItem item;

        Handler(Block block, int meta, short targetSideOrdinal, TileEntity tileEntity, EntityPlayer player, World world,
            int x, int y, int z, ItemStack stack, ToolWrenchItem item) {
            this.block = block;
            this.meta = meta;
            this.targetSideOrdinal = targetSideOrdinal;
            this.tileEntity = tileEntity;
            this.player = player;
            this.world = world;
            this.x = x;
            this.y = y;
            this.z = z;
            this.stack = stack;
            this.item = item;
        }

        /**
         * Runs the operation, charges the tool and plays the sound, if the player may use the wrench at all.
         *
         * @param operation the real operation of the click
         * @return true if the operation was successful
         */
        boolean doWrenchOperation(BooleanSupplier operation) {
            if (player.capabilities.isCreativeMode || item.canWrench(player, x, y, z)) {
                if (operation.getAsBoolean()) {
                    item.spendOneUse(stack);
                    GTUtility
                        .sendSoundToPlayers(world, SoundResource.GTCEU_OP_WRENCH, 1.0F, 1.0F, x + .5, y + .5, z + .5);
                    return true;
                }
            }
            return false;
        }

        boolean setBlockMeta(int newMeta) {
            return doWrenchOperation(() -> setBlockMetadataWithNotify(newMeta));
        }

        boolean rotateBlock(ForgeDirection direction) {
            return doWrenchOperation(() -> block.rotateBlock(world, x, y, z, direction));
        }

        boolean damageWrench() {
            return doWrenchOperation(() -> true);
        }

        private boolean setBlockMetadataWithNotify(int newMeta) {
            return world.setBlockMetadataWithNotify(x, y, z, newMeta, 3);
        }
    }

    public static boolean isVanillaRotatable(Block block) {
        return isVanillaCantFaceAxisY(block) || isVanillaAllSideRotatable(block) || block == Blocks.hopper;
    }

    public static boolean isVanillaCantFaceAxisY(Block block) {
        return GTUtility.arrayContains(
            block,
            Blocks.pumpkin,
            Blocks.lit_pumpkin,
            Blocks.furnace,
            Blocks.lit_furnace,
            Blocks.chest,
            Blocks.trapped_chest,
            Blocks.ender_chest);
    }

    public static boolean isVanillaChest(Block block) {
        return GTUtility.arrayContains(block, Blocks.chest, Blocks.trapped_chest);
    }

    public static boolean isVanillaAllSideRotatable(Block block) {
        return GTUtility.arrayContains(block, Blocks.piston, Blocks.sticky_piston, Blocks.dispenser, Blocks.dropper);
    }
}
