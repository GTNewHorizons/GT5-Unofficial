package gregtech.nei.dumper;

import static gregtech.api.enums.Mods.NewHorizonsCoreMod;

import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;

import codechicken.nei.NEIClientUtils;
import codechicken.nei.config.DataDumper;

public abstract class GregTechIDDumper extends DataDumper {

    public GregTechIDDumper(String name) {
        super("tools.dump.gt5u." + name);
    }

    @Override
    public Iterable<String[]> dump(int modeInt) {
        return dump(getMode(modeInt));
    }

    protected abstract Iterable<String[]> dump(Mode mode);

    @Override
    public String modeButtonText() {
        return NEIClientUtils.lang.translate("options.tools.dump.gt5u.mode." + getMode());
    }

    @Override
    public void dumpFile() {
        super.dumpFile();
        logWarn();
    }

    protected void super$dumpFile() {
        super.dumpFile();
    }

    protected void logWarn() {
        if (!NewHorizonsCoreMod.isModLoaded()) {
            NEIClientUtils.printChatMessage(
                    new ChatComponentTranslation("nei.options.tools.dump.gt5u.warn_env").setChatStyle(
                            new ChatStyle().setColor(EnumChatFormatting.DARK_RED)));
        }
    }

    @Override
    public int modeCount() {
        return Mode.values().length;
    }

    protected Mode getMode(int modeInt) {
        return Mode.values()[modeInt];
    }

    protected enum Mode {
        FREE,
        USED
    }
}
