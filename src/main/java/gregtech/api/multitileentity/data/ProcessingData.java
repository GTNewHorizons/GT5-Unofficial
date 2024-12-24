package gregtech.api.multitileentity.data;

import com.badlogic.ashley.core.Component;
import com.gtnewhorizons.mutecore.api.data.WorldStateValidator;

import net.minecraft.nbt.NBTTagCompound;

public class ProcessingData implements Component, WorldStateValidator {

    protected long energyUsage = 0;
    protected long duration = 0;
    protected long progress = 0;

    public long getEnergyUsage() {
        return energyUsage;
    }

    public void setEnergyUsage(long energyUsage) {
        this.energyUsage = energyUsage;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getProgress() {
        return progress;
    }

    public void setProgress(long progress) {
        this.progress = progress;
    }

    @Override
    public void save(NBTTagCompound nbt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void load(NBTTagCompound nbt) {
        // TODO Auto-generated method stub

    }
}
