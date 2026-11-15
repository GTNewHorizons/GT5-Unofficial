package gregtech.common.tileentities.machines.multi.nanochip.util;

import net.minecraft.network.PacketBuffer;

public class ModuleInformation {

    private final ModuleTypes type;
    private boolean running;

    public ModuleInformation(ModuleTypes type) {
        this.type = type;
    }

    private ModuleInformation(ModuleTypes type, boolean running) {
        this.type = type;
        this.running = running;
    }

    public ModuleTypes getType() {
        return type;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public static void writeToBuffer(PacketBuffer buf, ModuleInformation info) {
        buf.writeInt(info.type.ordinal());
        buf.writeBoolean(info.running);
    }

    public static ModuleInformation readFromBuffer(PacketBuffer buf) {
        ModuleTypes type = ModuleTypes.VALUES[buf.readInt()];
        boolean running = buf.readBoolean();
        return new ModuleInformation(type, running);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ModuleInformation that = (ModuleInformation) o;

        if (type != that.type) return false;
        return running == that.running;
    }
}
