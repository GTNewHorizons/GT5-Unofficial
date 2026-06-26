package tectech.thing.metaTileEntity.multi.base.parameter;

import net.minecraft.nbt.NBTTagCompound;

import com.cleanroommc.modularui.value.sync.EnumSyncValue;

public class EnumParameter<T extends Enum<T>> extends Parameter<T, EnumSyncValue<T, ?>> {

    private final Class<T> enumClass;

    public EnumParameter(Class<T> enumClass, T value, String langKey, String nbtKey, Object... langArgs) {
        super(value, langKey, nbtKey, langArgs);
        this.enumClass = enumClass;
    }

    public Class<T> getEnumClass() {
        return enumClass;
    }

    @Override
    public void saveNBT(NBTTagCompound tag) {
        tag.setInteger(
            this.getNbtKey(),
            this.getValue()
                .ordinal());
    }

    @Override
    public void loadNBT(NBTTagCompound tag) {
        if (!tag.hasKey(this.getNbtKey())) return;
        this.setValue(enumClass.getEnumConstants()[tag.getInteger(this.getNbtKey())]);
    }

    @Override
    public void saveToParameterCard(NBTTagCompound tag) {
        super.saveToParameterCard(tag);
        tag.setString("type", "enum");

        T value = this.getValue();
        tag.setString("value", value.name());
        tag.setString("displayName", value.toString());
    }

    @Override
    public void loadFromParameterCard(NBTTagCompound tag) {
        this.setValue(Enum.valueOf(enumClass, tag.getString("value")));
    }

    @Override
    protected EnumSyncValue<T, ?> createSyncHandler() {
        return new EnumSyncValue<>(enumClass, this::getValue, this::setValue).allowC2S();
    }
}
