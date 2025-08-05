package gregtech.api.modularui2;

import static gregtech.api.modularui2.MetaTileEntityGuiHandler.MAX_INTERACTION_DISTANCE;

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
import gregtech.common.covers.Cover;

/**
 * Responsible for creating cover GUI. See also: {@link Cover#buildUI}
 */
public class CoverUIFactory extends AbstractUIFactory<CoverGuiData> {

    public static final CoverUIFactory INSTANCE = new CoverUIFactory();

    private CoverUIFactory() {
        super("gregtech:cover");
    }

    /**
     * Call this method on server side to open cover window. Actual code to create GUI is located at
     * {@link Cover#buildUI}.
     *
     * @param player  Player opened this UI
     * @param coverID ID of the cover
     * @param tile    TileEntity this cover is attached to
     * @param side    Side this cover is attached to
     */
    public void open(EntityPlayerMP player, int coverID, ICoverable tile, ForgeDirection side) {
        if (player instanceof FakePlayer) return;
        CoverGuiData guiData = new CoverGuiData(
            player,
            coverID,
            tile.getXCoord(),
            tile.getYCoord(),
            tile.getZCoord(),
            side);
        GuiManager.open(this, guiData, player);
    }

    @Override
    public @NotNull IGuiHolder<CoverGuiData> getGuiHolder(CoverGuiData data) {
        TileEntity tile = data.getTileEntity();
        // TODO: Figure out a more graceful way to handle mismatches.
        if (!(tile instanceof ICoverable coverable)) {
            throw new RuntimeException(
                String.format(
                    "TileEntity at %s, %s, %s is not an instance of ICoverable!",
                    tile.xCoord,
                    tile.yCoord,
                    tile.zCoord));
        }
        Cover cover = coverable.getCoverAtSide(data.getSide());
        if (!(cover.getCoverID() == data.getCoverID())) {
            throw new RuntimeException(
                String.format(
                    "Cover at %s, %s, %s on side %s is not the expected kind! Expected %s, got %s",
                    tile.xCoord,
                    tile.yCoord,
                    tile.zCoord,
                    data.getSide(),
                    data.getCoverID(),
                    cover.getCoverID()));
        }
        return cover;
    }

    @Override
    public boolean canInteractWith(EntityPlayer player, CoverGuiData guiData) {
        return super.canInteractWith(player, guiData) && guiData.getTileEntity() instanceof ICoverable coverable
            && coverable.getCoverAtSide(guiData.getSide())
                .getCoverID() == guiData.getCoverID()
            && guiData.getSquaredDistance(player) <= MAX_INTERACTION_DISTANCE;
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
    }

    @Override
    public @NotNull CoverGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        return new CoverGuiData(
            player,
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            ForgeDirection.getOrientation(buffer.readByte()));
    }
}
