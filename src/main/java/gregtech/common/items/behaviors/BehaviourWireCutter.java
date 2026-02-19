package gregtech.common.items.behaviors;

import appeng.api.parts.IPartHost;
import appeng.api.parts.SelectedPart;
import appeng.core.sync.GuiBridge;
import appeng.tile.AEBaseTile;
import appeng.util.Platform;
import gregtech.api.items.MetaBaseItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class BehaviourWireCutter extends BehaviourNone {
    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) return false;

        TileEntity tileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (tileEntity instanceof IPartHost partHost) {
            SelectedPart part = partHost.selectPart(Vec3.createVectorHelper(hitX, hitY, hitZ));
            Platform.openGUI(aPlayer, tileEntity, part.side, GuiBridge.GUI_RENAMER);
            return true;
        }
        if (tileEntity instanceof AEBaseTile) {
            Platform.openGUI(aPlayer, tileEntity, side, GuiBridge.GUI_RENAMER);
            return true;
        }
        return false;
    }
}
