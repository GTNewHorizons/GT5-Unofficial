package tectech.thing.gui.bec;

import static net.minecraft.util.EnumChatFormatting.AQUA;
import static net.minecraft.util.EnumChatFormatting.GOLD;
import static net.minecraft.util.EnumChatFormatting.GRAY;

import java.util.function.Supplier;

import net.minecraft.util.StatCollector;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.value.sync.GenericSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.TextWidget;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import gregtech.api.enums.CondensateType;
import gregtech.common.gui.modularui.adapter.CondensateListAdapter;
import gregtech.common.gui.modularui.multiblock.base.TTMultiblockBaseGui;
import tectech.mechanics.boseEinsteinCondensate.BECFactoryNetwork;
import tectech.mechanics.boseEinsteinCondensate.CondensateList;
import tectech.thing.metaTileEntity.multi.base.MTEBECMultiblockBase;

public class MTEBECMultiblockBaseGui<T extends MTEBECMultiblockBase<?>> extends TTMultiblockBaseGui<T> {

    public MTEBECMultiblockBaseGui(T multiblock) {
        super(multiblock);
    }

    protected TextWidget<?> createCondensateWidget(PanelSyncManager syncManager, ModularPanel parent,
        Supplier<CondensateList> condensateSupplier) {
        GenericSyncValue<CondensateList, ?> condensate = GenericSyncValue.builder(CondensateList.class)
            .getter(condensateSupplier)
            .adapter(new CondensateListAdapter())
            .build();

        syncManager.syncValue("condensate", condensate);

        return IKey.dynamic(() -> {
            StringBuilder ret = new StringBuilder();

            ret.append(GRAY)
                .append(StatCollector.translateToLocal("GT5U.gui.text.available-condensate"))
                .append('\n');

            if (condensate.getValue()
                .isEmpty()) {
                ret.append(GRAY)
                    .append(StatCollector.translateToLocal("GT5U.gui.text.nil"));
            }

            for (var e : condensate.getValue()
                .object2LongEntrySet()) {
                ret.append("  ")
                    .append(AQUA)
                    .append(CondensateType.getCondensateName(e.getKey()))
                    .append(GRAY)
                    .append(" x ")
                    .append(GOLD)
                    .append(NumberFormatUtil.formatFluid(e.getLongValue()))
                    .append(GRAY)
                    .append('\n');
            }

            return ret.toString();
        })
            .asWidget()
            .widthRel(1);
    }

    protected TextWidget<?> createCondensateWidget(PanelSyncManager syncManager, ModularPanel parent) {
        return createCondensateWidget(syncManager, parent, () -> {
            BECFactoryNetwork network = multiblock.getNetwork();
            return network == null ? new CondensateList() : network.getStoredCondensate(multiblock);
        });
    }
}
