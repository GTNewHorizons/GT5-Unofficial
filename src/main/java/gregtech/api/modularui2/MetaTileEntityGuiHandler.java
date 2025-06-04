package gregtech.api.modularui2;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.FakePlayer;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.AbstractUIFactory;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.PosGuiData;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public final class MetaTileEntityGuiHandler extends AbstractUIFactory<PosGuiData> {

    public static final MetaTileEntityGuiHandler INSTANCE = new MetaTileEntityGuiHandler();
    public static final int MAX_INTERACTION_DISTANCE = 64;

    private MetaTileEntityGuiHandler() {
        super("gregtech:mte");
    }

    public static void open(EntityPlayer player, IMetaTileEntity mte) {
        IGregTechTileEntity baseTE = mte.getBaseMetaTileEntity();
        if (baseTE == null || !baseTE.canAccessData()) {
            throw new IllegalArgumentException("Can't open invalid MetaTileEntity GUI!");
        }
        if (player.worldObj != baseTE.getWorld()) {
            throw new IllegalArgumentException("MetaTileEntity must be in same dimension as the player!");
        }
        if (!(player instanceof EntityPlayerMP playerMP)) {
            throw new IllegalArgumentException("MetaTileEntity GUI must be opened on server side!");
        }
        if (player instanceof FakePlayer) {
            return;
        }
        PosGuiData data = new PosGuiData(player, baseTE.getXCoord(), baseTE.getYCoord(), baseTE.getZCoord());
        GuiManager.open(INSTANCE, data, playerMP);
    }

    @NotNull
    @Override
    public IGuiHolder<PosGuiData> getGuiHolder(PosGuiData data) {
        TileEntity te = data.getTileEntity();
        if (te instanceof IGregTechTileEntity baseTE) {
            IGuiHolder<PosGuiData> guiHolder = castGuiHolder(baseTE.getMetaTileEntity());
            if (guiHolder != null) {
                return guiHolder;
            }
        }
        throw new IllegalStateException(
            String.format(
                "TileEntity at (%s, %s, %s) is not a valid MetaTileEntity!",
                data.getX(),
                data.getY(),
                data.getZ()));
    }

    @Override
    public boolean canInteractWith(EntityPlayer player, PosGuiData guiData) {
        return super.canInteractWith(player, guiData) && guiData.getTileEntity() instanceof IGregTechTileEntity baseTE
            && baseTE.canAccessData()
            && guiData.getSquaredDistance(player) <= MAX_INTERACTION_DISTANCE;
    }

    @Override
    public void writeGuiData(PosGuiData guiData, PacketBuffer buffer) {
        buffer.writeVarIntToBuffer(guiData.getX());
        buffer.writeVarIntToBuffer(guiData.getY());
        buffer.writeVarIntToBuffer(guiData.getZ());
    }

    @Override
    public @NotNull PosGuiData readGuiData(EntityPlayer player, PacketBuffer buffer) {
        return new PosGuiData(
            player,
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer(),
            buffer.readVarIntFromBuffer());
    }
}
