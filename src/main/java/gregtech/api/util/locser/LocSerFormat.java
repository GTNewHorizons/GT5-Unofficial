package gregtech.api.util.locser;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import gregtech.api.util.GTUtility;

/**
 * Only accept `%s` as format specifier.
 */
public class LocSerFormat implements ILocSer {

    public String key;
    public ILocSer[] args;

    public LocSerFormat() {}

    public LocSerFormat(String key, ILocSer... args) {
        this.key = key;
        this.args = args;
    }

    @Override
    public void encode(PacketBuffer out) {
        NetworkUtils.writeStringSafe(out, getId());
        NetworkUtils.writeStringSafe(out, key);
        out.writeInt(args.length);
        for (ILocSer arg : args) {
            arg.encode(out);
        }
    }

    @Override
    public void decode(PacketBuffer in) {
        this.key = NetworkUtils.readStringSafe(in);
        this.args = new ILocSer[in.readInt()];
        for (int i = 0; i < this.args.length; i++) {
            this.args[i] = ILocSerManager.decode(in);
        }
    }

    @Override
    public String localize() {
        return GTUtility.processFormatStacks(StatCollector.translateToLocalFormatted(this.key, processArgs(this.args)));
    }

    @Override
    public String getId() {
        return "gt:format";
    }

    public static Object[] processArgs(ILocSer[] args) {
        String[] localizedArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            localizedArgs[i] = GTUtility.wrapStack(args[i].localize());
        }
        return localizedArgs;
    }
}
