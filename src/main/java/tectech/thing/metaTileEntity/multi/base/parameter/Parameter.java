package tectech.thing.metaTileEntity.multi.base.parameter;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public abstract class Parameter<T, S extends SyncHandler<?>> {

    private T value;
    private final String nbtKey;
    private final String langKey;
    private final Object[] langArgs;
    private boolean showInGui = true;
    private S syncHandler;

    public Parameter(T value, String langKey, String nbtKey, Object... langArgs) {
        this.value = value;
        this.langKey = langKey;
        this.nbtKey = nbtKey;
        this.langArgs = langArgs;
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

    public void disableGui() {
        this.showInGui = false;
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

    protected abstract S createSyncHandler();

    public void registerSyncValue(PanelSyncManager syncManager) {
        this.registerSyncValue(syncManager, "");
    }

    protected void registerSyncValue(PanelSyncManager syncManager, String prefix) {
        syncManager.syncValue(prefix + getNbtKey(), this.createSyncHandler());
    }

    public S getSyncHandler() {
        if (syncHandler == null) syncHandler = createSyncHandler();
        return syncHandler;
    }
}
