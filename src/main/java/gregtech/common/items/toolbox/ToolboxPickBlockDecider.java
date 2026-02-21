package gregtech.common.items.toolbox;

import java.util.List;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import appeng.api.util.IOrientable;
import appeng.tile.AEBaseTile;
import gregtech.api.enums.ToolboxSlot;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.api.util.GTUtility;
import gregtech.common.items.toolbox.pickblock.CoverableAction;
import gregtech.common.items.toolbox.pickblock.EnderIOAction;
import gregtech.common.items.toolbox.pickblock.IDeciderAction;
import gregtech.common.items.toolbox.pickblock.ProjectRedAction;
import gregtech.common.items.toolbox.pickblock.RailcraftAction;
import gregtech.common.items.toolbox.pickblock.SimpleAction;
import ic2.api.tile.IWrenchable;

/**
 * Contains various methods to test the block the player is looking at for obvious choices to select as the
 * current tool.
 */
public class ToolboxPickBlockDecider {

    private final static Set<Block> WRENCH_BLOCKS = ImmutableSet.of(
        Blocks.hopper,
        Blocks.bookshelf,
        Blocks.ender_chest,
        Blocks.piston,
        Blocks.sticky_piston,
        Blocks.crafting_table,
        Blocks.unpowered_repeater,
        Blocks.powered_repeater,
        Blocks.unpowered_comparator,
        Blocks.powered_comparator,
        Blocks.dispenser,
        Blocks.dropper,
        Blocks.pumpkin,
        Blocks.lit_pumpkin,
        Blocks.furnace,
        Blocks.lit_furnace,
        Blocks.chest,
        Blocks.trapped_chest);

    private final static Set<Block> SOFT_MALLET_BLOCKS = ImmutableSet
        .of(Blocks.redstone_lamp, Blocks.lit_redstone_lamp, Blocks.golden_rail, Blocks.activator_rail);

    private final static Set<Block> CROWBAR_BLOCKS = ImmutableSet.of(Blocks.rail, Blocks.detector_rail);

    /**
     * This list contains deciders for various classes of blocks in the world. The decider function evaluates these in
     * order, stopping once it finds a match. When adding to this list, be as specific as possible with the target class
     * and put more specific examples earlier in the list.
     */
    private static final List<IDeciderAction> CLASS_ACTIONS = ImmutableList.of(
        new CoverableAction<>(MTEFluidPipe.class, ToolboxSlot.WRENCH),
        new CoverableAction<>(MTEItemPipe.class, ToolboxSlot.WRENCH),
        new CoverableAction<>(MTECable.class, ToolboxSlot.WIRE_CUTTER),

        // Fallback for GT machines
        new CoverableAction<>(IMetaTileEntity.class, ToolboxSlot.WRENCH),

        // Cross-mod support
        new SimpleAction<>(AEBaseTile.class, ToolboxSlot.WRENCH),
        new SimpleAction<>(IOrientable.class, ToolboxSlot.WRENCH),
        new SimpleAction<>(IWrenchable.class, ToolboxSlot.WRENCH),
        new ProjectRedAction(),
        new EnderIOAction(),
        new RailcraftAction());

    private ToolboxPickBlockDecider() {}

    /**
     * Returns a list of potential tools that are valid for the block the player is looking at.
     * <p>
     * Returned tools will be in order of preference. E.g.: Looking at the side of a pipe with a cover on it will
     * return a list of [crowbar, wrench], with the crowbar being the highest priority pick and the wrench being the
     * lowest. This allows the caller to default to the wrench if there is no crowbar in the toolbox.
     *
     * @param player The player that is doing the looking
     * @return A list of {@link ToolboxSlot ToolboxSlots}, in order of descending priority. Empty list returned if
     *         nothing matches
     */
    public static List<ToolboxSlot> getSuggestedTool(final EntityPlayer player, MovingObjectPosition position) {

        if (position != null && position.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK) {
            final Block block = player.worldObj.getBlock(position.blockX, position.blockY, position.blockZ);

            if (block != Blocks.air) {
                if (WRENCH_BLOCKS.contains(block)) {
                    return ImmutableList.of(ToolboxSlot.WRENCH);
                } else if (SOFT_MALLET_BLOCKS.contains(block)) {
                    return ImmutableList.of(ToolboxSlot.SOFT_MALLET);
                } else if (CROWBAR_BLOCKS.contains(block)) {
                    return ImmutableList.of(ToolboxSlot.CROWBAR);
                }

                if (block.hasTileEntity(
                    player.worldObj.getBlockMetadata(position.blockX, position.blockY, position.blockZ))) {
                    final TileEntity baseTE = player.worldObj
                        .getTileEntity(position.blockX, position.blockY, position.blockZ);

                    if (baseTE != null) {
                        final Object chosen = Objects.firstNonNull(GTUtility.getMetaTileEntity(baseTE), baseTE);

                        for (IDeciderAction action : CLASS_ACTIONS) {
                            if (action.isValid(chosen)) {
                                return action.apply(
                                    chosen,
                                    GTUtility.determineWrenchingSide(
                                        ForgeDirection.getOrientation(position.sideHit),
                                        (float) position.hitVec.xCoord,
                                        (float) position.hitVec.yCoord,
                                        (float) position.hitVec.zCoord));
                            }
                        }
                    }
                }
            }
        }

        return ImmutableList.of();
    }

    /** @see #getSuggestedTool(EntityPlayer, MovingObjectPosition) */
    public static List<ToolboxSlot> getSuggestedTool(final EntityPlayer player) {
        return getSuggestedTool(player, GTUtility.getPlayerLookingTarget(player));
    }

    /** @see #getSuggestedTool(EntityPlayer, MovingObjectPosition) */
    public static List<ToolboxSlot> getSuggestedTool(final DrawBlockHighlightEvent event) {
        return getSuggestedTool(event.player, event.target);
    }
}
