package tectech.thing.metaTileEntity.multi.base.parameter;

import java.util.List;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import org.jetbrains.annotations.UnmodifiableView;

import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.value.sync.SyncHandler;

public class CompositeParameter extends Parameter<@UnmodifiableView List<Parameter<?>>> {

    public CompositeParameter(List<Parameter<?>> value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
    }

    @Override
    public void setValue(@UnmodifiableView List<Parameter<?>> value) {
        throw new UnsupportedOperationException("Parameters must be set directly!");
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        NBTTagCompound parameters = new NBTTagCompound();

        for (Parameter<?> parameter : getValue()) parameter.saveNBT(parameters);
        tag.setTag(getNbtKey(), parameters);
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        NBTTagCompound parameters = tag.getCompoundTag(getNbtKey());
        for (Parameter<?> parameter : getValue()) parameter.loadNBT(parameters);
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "composite");

        NBTTagList parameters = new NBTTagList();
        for (Parameter<?> parameter : getValue()) {
            NBTTagCompound parameterTag = new NBTTagCompound();
            parameter.saveToParameterCard(parameterTag);
            parameters.appendTag(parameterTag);
        }

        tag.setTag("value", parameters);
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        NBTTagList parameterTags = tag.getTagList("value", Constants.NBT.TAG_COMPOUND);
        List<Parameter<?>> parameters = getValue();

        for (int i = 0; i < parameters.size(); i++) parameters.get(i)
            .loadFromParameterCard(parameterTags.getCompoundTagAt(i));
    }

    @Override
    public SyncHandler<?> createSyncHandler() {
        throw new UnsupportedOperationException("This parameter type does not have a sync handler!");
    }

    @Override
    public void registerSyncValue(PanelSyncManager syncManager, String prefix) {
        for (Parameter<?> parameter : getValue()) parameter.registerSyncValue(syncManager, prefix + getNbtKey() + ".");
    }
}
