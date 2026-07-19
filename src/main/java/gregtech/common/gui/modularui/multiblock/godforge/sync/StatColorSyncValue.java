package gregtech.common.gui.modularui.multiblock.godforge.sync;

import org.apache.commons.lang3.mutable.MutableObject;

import com.cleanroommc.modularui.utils.serialization.ByteBufAdapters;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;

import tectech.thing.metaTileEntity.multi.godforge.color.ForgeOfGodsStarColor;
import tectech.thing.metaTileEntity.multi.godforge.util.ForgeOfGodsData;

public class StatColorSyncValue extends GenericSyncValue<ForgeOfGodsStarColor, StatColorSyncValue> {

    private final ForgeOfGodsData data;

    public StatColorSyncValue(MutableObject<ForgeOfGodsStarColor> mut, ForgeOfGodsData data) {
        super(
            ForgeOfGodsStarColor.class,
            mut::getValue,
            mut::setValue,
            ByteBufAdapters
                .makeAdapter(ForgeOfGodsStarColor::readFromBuffer, ForgeOfGodsStarColor::writeToBuffer, null));

        this.data = data;
    }

    @Override
    public void setValue(ForgeOfGodsStarColor value, boolean setSource, boolean sync) {
        if (value == null) {
            value = data.getStarColors()
                .newTemplateColor();
        }
        super.setValue(value, setSource, sync);
    }
}
