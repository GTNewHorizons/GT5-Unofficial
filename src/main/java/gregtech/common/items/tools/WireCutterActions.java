package gregtech.common.items.tools;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.parts.IPartHost;
import appeng.api.parts.SelectedPart;
import appeng.core.sync.GuiBridge;
import appeng.tile.AEBaseTile;
import appeng.tile.storage.TileChest;
import appeng.tile.storage.TileDrive;
import appeng.util.Platform;

/**
 * The "right click an AE2 machine to rename it" half of a wire cutter, moved out of {@code BehaviourWireCutter}.
 * <p/>
 * Costs the tool nothing, which is how it always worked: opening a rename dialog is not wear and tear.
 */
public final class WireCutterActions {

    private WireCutterActions() {}

    /**
     * Opens AE2's renamer for the machine or cable part the player clicked.
     *
     * @return whether the click was consumed.
     */
    public static boolean use(EntityPlayer player, World world, int x, int y, int z, ForgeDirection side, float hitX,
        float hitY, float hitZ) {
        if (world.isRemote) return false;

        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity == null) return false;

        // Drives and chests have their own right-click GUI, so renaming them needs the sneak modifier.
        boolean requiresSneakForRename = tileEntity instanceof TileDrive || tileEntity instanceof TileChest;
        if (requiresSneakForRename && !player.isSneaking()) return false;

        ForgeDirection renameSide = side;
        if (tileEntity instanceof IPartHost partHost) {
            SelectedPart part = partHost.selectPart(Vec3.createVectorHelper(hitX, hitY, hitZ));
            if (part == null) return false;
            renameSide = part.side;
        } else if (!(tileEntity instanceof AEBaseTile)) {
            return false;
        }

        Platform.openGUI(player, tileEntity, renameSide, GuiBridge.GUI_RENAMER);
        return true;
    }
}
