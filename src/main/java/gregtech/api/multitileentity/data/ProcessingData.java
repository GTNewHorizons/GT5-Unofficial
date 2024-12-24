package gregtech.api.multitileentity.data;

import com.badlogic.ashley.core.Component;

public class ProcessingData implements Component {

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
}
