package gregtech.api.gui.modularui2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiManager;

import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.util.CoverBehaviorBase;

/**
 * Responsible for creating cover GUI. See also: {@link CoverBehaviorBase#buildUI}
 */
public class CoverUIFactory extends AbstractUIFactory<CoverGuiData> {

    public static final CoverUIFactory INSTANCE = new CoverUIFactory();

    private CoverUIFactory() {
        super("gregtech:cover");
    }

    /**
     * Call this method on server side to open cover window. Actual code to create GUI is located at
     * {@link CoverBehaviorBase#buildUI}.
     *
     * @param player        Player opened this UI
     * @param coverID       ID of the cover
     * @param tile          TileEntity this cover is attached to
     * @param side          Side this cover is attached to
     * @param anotherWindow If cover UI is shown on top of another window
     */
    public void open(EntityPlayerMP player, int coverID, ICoverable tile, ForgeDirection side, boolean anotherWindow) {
        if (player instanceof FakePlayer) return;
        CoverGuiData guiData = new CoverGuiData(
            player,
            coverID,
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            side,
            anotherWindow);
        GuiManager.open(this, guiData, player);
    }

    @Override
    public @NotNull IGuiHolder<CoverGuiData> getGuiHolder(CoverGuiData data) {
        TileEntity tile = data.getTileEntity();
        if (!(tile instanceof ICoverable coverable)) {
            throw new RuntimeException(
                String.format(
                    "TileEntity at %s, %s, %s is not an instance of ICoverable!",
                    tile.xCoord,
                    tile.yCoord,
                    tile.zCoord));
        }
        return coverable.getCoverBehaviorAtSideNew(data.getSide());
    }

    @Override
    public void writeGuiData(CoverGuiData guiData, PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(guiData.getCoverID());
        buffer.writeVarIntToBuffer(guiData.getX());
        buffer.writeVarIntToBuffer(guiData.getY());
        buffer.writeVarIntToBuffer(guiData.getZ());
        buffer.writeByte(
            guiData.getSide()
                .ordinal());
        buffer.writeBoolean(guiData.isAnotherWindow());
    }

    @Override
    public @NotNull CoverGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        return new CoverGuiData(
            player,
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            ForgeDirection.getOrientation(buffer.readByte()),
            buffer.readBoolean());
    }
}
