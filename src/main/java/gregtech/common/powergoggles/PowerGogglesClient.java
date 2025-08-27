package gregtech.common.powergoggles;

import static gregtech.api.enums.GTValues.NW;

import java.math.BigInteger;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import appeng.api.util.DimensionalCoord;
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
        MTELapotronicSuperCapacitor lsc = PowerGogglesUtil.getLsc(playerMP, lscLink);
        if (lsc == null) {
            return;
        }

        NW.sendToPlayer(
            new GTPacketUpdatePowerGoggles(BigInteger.valueOf(lsc.getEUVar()), lsc.maxEUStore(), false),
            playerMP);
    }

}
