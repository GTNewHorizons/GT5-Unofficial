package gregtech.common.powergoggles;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.WorldServer;

import appeng.api.util.DimensionalCoord;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.net.GTPacketUpdatePowerGoggles;
import kekztech.common.tileentities.MTELapotronicSuperCapacitor;

public class PowerGogglesClient {

    private DimensionalCoord lscLink;

    public void updateLscLink(ItemStack itemstack) {
        NBTTagCompound tag = itemstack.getTagCompound();
        DimensionalCoord newLink = null;
        if (tag != null && !tag.hasNoTags()) {
            newLink = new DimensionalCoord(
                tag.getInteger("x"),
                tag.getInteger("y"),
                tag.getInteger("z"),
                tag.getInteger("dim"));
        }

        if (!Objects.equals(lscLink, newLink)) {
            setLscLink(newLink);
        }
    }

    public void setLscLink(DimensionalCoord lscLink) {
        this.lscLink = lscLink;
    }

    public void updatePlayer(EntityPlayerMP playerMP) {
        MTELapotronicSuperCapacitor lsc = getLsc(playerMP);
        NW.sendToPlayer(
            new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar()), lsc.maxEUStore(), false),
            playerMP);
    }

    private MTELapotronicSuperCapacitor getLsc(EntityPlayerMP playerMP) {
        WorldServer lscDim = playerMP.mcServer.worldServerForDimension(lscLink.getDimension());
        TileEntity tileEntity = lscDim.getTileEntity(lscLink.x, lscLink.y, lscLink.z);
        return ((MTELapotronicSuperCapacitor) ((IGregTechTileEntity) tileEntity).getMetaTileEntity());
    }
}
