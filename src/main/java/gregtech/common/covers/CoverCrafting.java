package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverCrafting extends Cover {

    public CoverCrafting(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        ICoverable coverable = coveredTile.get();
        if ((aPlayer instanceof EntityPlayerMP && coverable != null)) {
            ((EntityPlayerMP) aPlayer).getNextWindowId();
            ((EntityPlayerMP) aPlayer).playerNetServerHandler.sendPacket(
                new S2DPacketOpenWindow(((EntityPlayerMP) aPlayer).currentWindowId, 1, "Crafting", 9, true));
            aPlayer.openContainer = new ContainerWorkbench(
                aPlayer.inventory,
                aPlayer.worldObj,
                coverable.getXCoord(),
                coverable.getYCoord(),
                coverable.getZCoord()) {

                @Override
                public boolean canInteractWith(EntityPlayer player) {
                    return true;
                }
            };
            aPlayer.openContainer.windowId = ((EntityPlayerMP) aPlayer).currentWindowId;
            aPlayer.openContainer.addCraftingToCrafters((EntityPlayerMP) aPlayer);
        }
        return true;
    }
}
