package gregtech.common.items.toolbox;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.base.Objects;
import com.google.common.collect.ImmutableList;

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
import gregtech.common.items.toolbox.pickblock.SimpleAction;

/**
 * Contains various methods to test the block the player is looking at for obvious choices to select as the
 * current tool.
 */
public class ToolboxPickBlockDecider {

    private static final List<IDeciderAction> ACTIONS = ImmutableList.of(
        new CoverableAction<>(MTEFluidPipe.class, ToolboxSlot.WRENCH),
        new CoverableAction<>(MTEItemPipe.class, ToolboxSlot.WRENCH),
        new CoverableAction<>(MTECable.class, ToolboxSlot.WIRE_CUTTER),
        new SimpleAction<>(AEBaseTile.class, ToolboxSlot.WRENCH),
        new ProjectRedAction(),
        new EnderIOAction());

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
            final TileEntity baseTE = player.worldObj.getTileEntity(position.blockX, position.blockY, position.blockZ);
            final IMetaTileEntity gregTE = GTUtility.getMetaTileEntity(baseTE);

            if (baseTE != null) {
                final Object chosen = Objects.firstNonNull(gregTE, baseTE);

                for (IDeciderAction action : ACTIONS) {
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
