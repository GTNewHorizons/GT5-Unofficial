package gregtech.common.gui.modularui.synchandler;

import java.util.List;
import java.util.function.Supplier;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

import gregtech.common.items.ItemRedstoneSniffer;

public class SnifferEntryListSyncHandler extends GenericListSyncHandler<ItemRedstoneSniffer.SnifferEntry> {

    public SnifferEntryListSyncHandler(@NotNull Supplier<List<ItemRedstoneSniffer.SnifferEntry>> getter) {
        super(
            getter,
            null,
            ItemRedstoneSniffer.SnifferEntry::deserialize,
            ItemRedstoneSniffer.SnifferEntry::serialize,
            ItemRedstoneSniffer.SnifferEntry::areEqual,
            null);
    }
}
