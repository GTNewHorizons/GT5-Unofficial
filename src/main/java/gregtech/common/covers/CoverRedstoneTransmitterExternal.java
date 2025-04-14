package gregtech.common.covers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;

import org.jetbrains.annotations.NotNull;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;

public class CoverRedstoneTransmitterExternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterExternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    @Override
    public void onCoverRemoval() {
        GregTechAPI.sWirelessRedstone.remove(coverData);
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        GregTechAPI.sWirelessRedstone.put(coverData, aInputRedstone);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return true;
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        NBTTagCompound tag = (NBTTagCompound) nbt;
        if (tag.hasKey("frequency") && !(tag.getInteger("frequency") == coverData)) {
            GregTechAPI.sWirelessRedstone.remove(coverData);
        }
        coverData = tag.getInteger("frequency");
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        NBTTagCompound tag = new NBTTagCompound();
        tag.setInteger("frequency", coverData);
        return tag;
    }
}
