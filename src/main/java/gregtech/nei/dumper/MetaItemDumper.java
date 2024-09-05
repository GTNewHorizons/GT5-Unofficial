package gregtech.nei.dumper;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;

import codechicken.nei.NEIClientUtils;
import gregtech.api.items.MetaGeneratedItem;

public class MetaItemDumper extends GregTechIDDumper {

    private final MetaGeneratedItem item;
    private final String nameSuffix;

    public MetaItemDumper(MetaGeneratedItem item, String name) {
        super(name);
        this.nameSuffix = name;
        this.item = item;
    }

    @Override
    public String[] header() {
        return new String[] { "id", "stackName", "metaID" };
    }

    @Override
    protected Iterable<String[]> dump(Mode mode) {
        List<String[]> list = new ArrayList<>();
        for (int i = 0; i < item.mItemAmount; i++) {
            int metaID = item.mOffset + i;
            boolean generated = item.mEnabledItems.get(i);
            if (mode == Mode.FREE && !generated) {
                list.add(new String[] { String.valueOf(i), "", String.valueOf(metaID), });
            } else if (mode == Mode.USED && generated) {
                list.add(
                    new String[] { String.valueOf(i), new ItemStack(item, 1, metaID).getDisplayName(),
                        String.valueOf(metaID), });
            }
        }
        return list;
    }

    @Override
    public void dumpFile() {
        super.super$dumpFile();
        NEIClientUtils.printChatMessage(
            new ChatComponentText(String.format("mOffset: %s, mItemAmount: %s", item.mOffset, item.mItemAmount)));
        logWarn();
    }

    @Override
    public String translateN(String s, Object... args) {
        if (name.equals(s) || (name + "s").equals(s)) {
            return nameSuffix;
        }
        return super.translateN(s, args);
    }
}
