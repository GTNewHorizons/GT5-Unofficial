package gregtech.api.structure;

import net.minecraft.item.ItemStack;

import com.gtnewhorizon.structurelib.alignment.constructable.ChannelDataAccessor;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

public interface IStructureChannels {

    String get();

    String getDefaultTooltip();

    void registerAsIndicator(ItemStack indicator, int channelValue);

    /**
     * Get channel data of this channel fitted to given range (both ends inclusive) ex1: if given a min of 1 and max of
     * 10, it will map channel data of 1 to 1, 2 to 2, ... 10 and any greater value to 10 ex2: if given a min of 4 and
     * max of 6, it will map channel data of 1 to 4, 2 to 5, 3 and any bigger value to 6
     *
     * @param trigger channel data holder
     * @param min     minimal return value. inclusive
     * @param max     maximal return value. inclusive
     * @return fitted channel data
     */
    default int getValueClamped(ItemStack trigger, int min, int max) {
        return Math.min(max, ChannelDataAccessor.getChannelData(trigger, get()) + min - 1);
    }

    default int getValue(ItemStack trigger) {
        return ChannelDataAccessor.getChannelData(trigger, get());
    }

    default boolean hasValue(ItemStack trigger) {
        return ChannelDataAccessor.hasSubChannel(trigger, get());
    }

    default <T> IStructureElement<T> use(IStructureElement<T> elem) {
        return StructureUtility.withChannel(get(), elem);
    }
}
