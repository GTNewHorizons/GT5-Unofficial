package gregtech.common.items.behaviors;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import gregtech.api.items.MetaBaseItem;

public class BehaviourWireCutter extends BehaviourNone {

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) return false;

        TileEntity tileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tileEntity == null) return false;

        boolean requiresSneakForRename = tileEntity instanceof TileDrive || tileEntity instanceof TileChest;
        if (requiresSneakForRename && !aPlayer.isSneaking()) {
            return false;
        }

        ForgeDirection renameSide = side;

        if (tileEntity instanceof IPartHost partHost) {
            SelectedPart part = partHost.selectPart(Vec3.createVectorHelper(hitX, hitY, hitZ));
            if (part == null) return false;
            renameSide = part.side;
        } else if (!(tileEntity instanceof AEBaseTile)) {
            return false;
        }

        Platform.openGUI(aPlayer, tileEntity, renameSide, GuiBridge.GUI_RENAMER);
        return true;
    }

}
