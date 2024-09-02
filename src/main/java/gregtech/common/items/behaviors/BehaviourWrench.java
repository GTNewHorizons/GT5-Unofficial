package gregtech.common.items.behaviors;

import java.util.Arrays;
import java.util.List;
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
import gregtech.api.enums.SoundResource;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import ic2.api.tile.IWrenchable;

public class BehaviourWrench extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.wrench", "Rotates Blocks on Rightclick");

    public BehaviourWrench(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        final Block aBlock = aWorld.getBlock(aX, aY, aZ);
        if (aBlock == null) {
            return false;
        }
        final int aMeta = aWorld.getBlockMetadata(aX, aY, aZ);
        final short targetSideOrdinal = (short) GTUtility.determineWrenchingSide(side, hitX, hitY, hitZ)
            .ordinal();
        final TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);

        final WrenchHandler handler = new WrenchHandler(
            aBlock,
            aMeta,
            targetSideOrdinal,
            aTileEntity,
            aPlayer,
            aWorld,
            aX,
            aY,
            aZ,
            aStack,
            (MetaGeneratedTool) aItem,
            mCosts);

        try {
            return handler.handle() && !aWorld.isRemote;
        } catch (Throwable ignored) {}
        return false;
    }

    /**
     * <p>
     * A class to simplify wrenching operation,
     * stopping "checking creative", "trying to damage tool",
     * "doing the logic" and "playing sound" again and again.
     * This should have been a record, but it's not available in Java 8.
     * </p>
     * <p>
     * {@link WrenchHandler#handle()} is the entry point of main logic.
     * </p>
     */
    private static class WrenchHandler {

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
                        return doWrenchOperation(costs, () -> {
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
                    if (!player.isSneaking() && direction == up) return doWrenchOperation(costs, () -> {
                        orientable.setOrientation(tempFront.getRotation(tempUp), tempUp);
                        return true;
                    });
                    if (player.isSneaking() && direction == front) return doWrenchOperation(costs, () -> {
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

                return damageWrench(costs);
            }
            if (world.isRemote) return false;
            // IC2 Wrenchable
            if (tileEntity instanceof IWrenchable wrenchable) {
                if (wrenchable.wrenchCanSetFacing(player, targetSideOrdinal)) {
                    return doWrenchOperation(costs, () -> {
                        wrenchable.setFacing(targetSideOrdinal);
                        return true;
                    });
                }
                return false;
            }

            if (block == Blocks.powered_repeater || block == Blocks.unpowered_repeater
                || block == Blocks.powered_comparator
                || block == Blocks.unpowered_comparator) return setBlockMeta(costs, meta / 4 * 4 + (meta % 4 + 1) % 4);

            // hopper cannot face sky
            if (block == Blocks.hopper && targetSideOrdinal != 1) return setBlockMeta(costs, targetSideOrdinal);

            if (isVanillaAllSideRotatable(block)) if (meta < 6) return setBlockMeta(costs, targetSideOrdinal);

            // blocks like chests and furnaces have only four directions
            if (isVanillaCantFaceAxisY(block)) {
                if (targetSideOrdinal > 1) return setBlockMeta(costs, targetSideOrdinal);
                else return false;
            }
            if (tileEntity instanceof IPartHost) return false;

            final int logWoodId = OreDictionary.getOreID("logWood");
            if (Arrays.stream(OreDictionary.getOreIDs(new ItemStack(block)))
                .anyMatch(id -> id == logWoodId)) {
                // The meta just work
                return setBlockMeta(costs, (meta + 4) % 12);
            }

            // vanilla block rotate logic
            if ((Arrays.asList(block.getValidRotations(world, x, y, z))
                .contains(direction))) return rotateBlock(costs, direction);
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

        private final MetaGeneratedTool item;
        private final int costs;

        public WrenchHandler(Block block, int meta, short targetSideOrdinal, TileEntity tileEntity, EntityPlayer player,
            World world, int x, int y, int z, ItemStack stack, MetaGeneratedTool item, int costs) {
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
            this.costs = costs;
        }

        /**
         * this will run the operation, damage the tool and play the sound if possible (creative mode or
         * {@link MetaGeneratedTool#canWrench(EntityPlayer, int, int, int)})
         *
         * @param damage    damage to be applied to the wrench
         * @param operation the real operation of the click
         * @return true if the operation was successful
         * @see #setBlockMeta(int, int)
         * @see #rotateBlock(int, ForgeDirection)
         * @see #rotateBlock(int, ForgeDirection)
         */
        boolean doWrenchOperation(int damage, BooleanSupplier operation) {
            if (player.capabilities.isCreativeMode || item.canWrench(player, x, y, z)) {
                if (operation.getAsBoolean()) {
                    item.doDamage(stack, damage);
                    GTUtility.sendSoundToPlayers(world, SoundResource.IC2_TOOLS_WRENCH, 1.0F, -1.0F, x, y, z);
                    return true;
                }
            }
            return false;
        }

        boolean setBlockMeta(int damage, int newMeta) {
            return doWrenchOperation(damage, () -> setBlockMetadataWithNotify(newMeta));
        }

        boolean rotateBlock(int damage, ForgeDirection direction) {
            return doWrenchOperation(damage, () -> block.rotateBlock(world, x, y, z, direction));
        }

        boolean damageWrench(int damage) {
            return doWrenchOperation(damage, () -> true);
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

    public static boolean isVanillaAllSideRotatable(Block block) {
        return GTUtility.arrayContains(block, Blocks.piston, Blocks.sticky_piston, Blocks.dispenser, Blocks.dropper);
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
