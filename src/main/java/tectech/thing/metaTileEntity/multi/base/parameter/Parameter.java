package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.function.Supplier;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.cleanroommc.modularui.value.sync.SyncHandler;

public abstract class Parameter<T> {

    private T value;
    // Suppliers relevant to number parameters only
    private Supplier<T> min;
    private Supplier<T> max;
    private final String nbtKey;
    private final String langKey;
    private final Object[] langArgs;
    private boolean showInGui = true;

    public Parameter(T value, String langKey, String nbtKey, Object... langArgs) {
        this.value = value;
        this.langKey = langKey;
        this.nbtKey = nbtKey;
        this.langArgs = langArgs;
    }

    public Parameter(T value, String langKey, String nbtKey, Supplier<T> min, Supplier<T> max, Object... langArgs) {
        this(value, langKey, nbtKey, langArgs);
        this.min = min;
        this.max = max;
    }

    public T getValue() {
        return value;
    }

    public void setValue(T value) {
        this.value = value;
    }

    public String getNbtKey() {
        return nbtKey;
    }

    public String getLangKey() {
        return langKey;
    }

    public Object[] getLangArgs() {
        return langArgs;
    }

    public T getMin() {
        return min.get();
    }

    public T getMax() {
        return max.get();
    }

    public Parameter<T> disableGui() {
        this.showInGui = false;
        return this;
    }

    public abstract void saveNBT(NBTTagCompound tag);

    public abstract void loadNBT(NBTTagCompound tag);

    public void saveToParameterCard(NBTTagCompound tag) {
        tag.setString("langKey", langKey);
        if (langArgs != null) {
            NBTTagList tagList = new NBTTagList();

            for (Object arg : langArgs) tagList.appendTag(new NBTTagString(arg.toString()));

            tag.setTag("langArgs", tagList);
        }
    }

    public abstract void loadFromParameterCard(NBTTagCompound tag);

    public boolean shouldShowInGui() {
        return showInGui;
    }

    public abstract SyncHandler createSyncHandler();
}
