package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.network.play.server.S2DPacketOpenWindow;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.GT_CoverBehavior;
import gregtech.api.util.ISerializableObject;

public class GT_Cover_Crafting extends GT_CoverBehavior {

    /**
     * @deprecated use {@link #GT_Cover_Crafting(ITexture coverTexture)} instead
     */
    @Deprecated
    public GT_Cover_Crafting() {
        this(null);
    }

    public GT_Cover_Crafting(ITexture coverTexture) {
        super(coverTexture);
    }

    @Override
    public boolean isRedstoneSensitive(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        long aTimer) {
        return false;
    }

    @Override
    public boolean onCoverRightclick(ForgeDirection side, int aCoverID, int aCoverVariable, ICoverable aTileEntity,
        EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if ((aPlayer instanceof EntityPlayerMP)) {
            ((EntityPlayerMP) aPlayer).getNextWindowId();
            ((EntityPlayerMP) aPlayer).playerNetServerHandler.sendPacket(
                new S2DPacketOpenWindow(((EntityPlayerMP) aPlayer).currentWindowId, 1, "Crafting", 9, true));
            aPlayer.openContainer = new ContainerWorkbench(
                aPlayer.inventory,
                aPlayer.worldObj,
                aTileEntity.getXCoord(),
                aTileEntity.getYCoord(),
                aTileEntity.getZCoord()) {

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

    @Override
    protected boolean isGUIClickableImpl(ForgeDirection side, int aCoverID,
        ISerializableObject.LegacyCoverData aCoverVariable, ICoverable aTileEntity) {
        return false;
    }
}
