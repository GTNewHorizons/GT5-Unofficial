package gregtech.common.covers;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;

import gregtech.api.GregTechAPI;
import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;

public class CoverRedstoneTransmitterInternal extends CoverRedstoneWirelessBase {

    public CoverRedstoneTransmitterInternal(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public boolean isRedstoneSensitive(long aTimer) {
        return false;
    }

    @Override
    public void onCoverRemoval() {
        GregTechAPI.sWirelessRedstone.remove(getMapFrequency());
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        GregTechAPI.sWirelessRedstone.put(getMapFrequency(), coverable.getOutputRedstoneSignal(coverSide));
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return true;
    }

    @Override
    public boolean manipulatesSidedRedstoneOutput() {
        return true;
    }

    // TODO: Remove this in 2.9 unless class moved from CoverLegacyData
    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        if (nbt instanceof NBTTagInt nbtInt) {
            int data = nbtInt.func_150287_d();
            processCoverData(getFlagFrequency(data), getFlagCheckbox(data));
            return;
        }
        NBTTagCompound tag = (NBTTagCompound) nbt;
        processCoverData(tag.getInteger("frequency"), tag.getBoolean("privateChannel"));
    }
}
