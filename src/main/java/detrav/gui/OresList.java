package detrav.gui;

import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.client.GuiScrollingList;
import detrav.items.DetravMetaGeneratedTool01;
import detrav.net.ProspectingPacket;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

class OresList extends GuiScrollingList {

    private final Object2IntOpenHashMap<String> colors = new Object2IntOpenHashMap<>();
    private final List<String> keys;
    private final GuiScreen parent;
    private final BiConsumer<String, Boolean> onSelected;
    private boolean invert = false;

    private int selected = -1;

    public OresList(GuiScreen parent, int width, int height, int top, int bottom, int left, int entryHeight,
        ProspectingPacket packet, BiConsumer<String, Boolean> onSelected) {
        super(parent.mc, width, height, top, bottom, left, entryHeight);
        this.parent = parent;
        this.onSelected = onSelected;
        keys = packet.objects.short2ObjectEntrySet()
            .stream()
            .map(
                e -> e.getValue()
                    .left())
            .collect(Collectors.toList());
        Collections.sort(keys);
        if (packet.ptype == DetravMetaGeneratedTool01.MODE_POLLUTION) {
            keys.clear();
            keys.add(StatCollector.translateToLocal("gui.detrav.scanner.pollution"));
        } else if (keys.size() > 1) {
            keys.add(0, "All");
        }
        selected = 0;

        for (var e : packet.objects.short2ObjectEntrySet()) {
            this.colors.put(
                e.getValue()
                    .left(),
                e.getValue()
                    .rightInt());
        }
    }

    @Override
    protected int getSize() {
        return keys.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        selected = index;
        if (doubleClick) this.invert = !this.invert;

        if (onSelected != null) onSelected.accept(keys.get(index), this.invert);
    }

    @Override
    protected boolean isSelected(int index) {
        return selected == index;
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        parent.drawString(
            parent.mc.fontRenderer,
            parent.mc.fontRenderer.trimStringToWidth(keys.get(slotIdx), listWidth - 10),
            this.left + 3,
            slotTop - 1,
            colors.getOrDefault(keys.get(slotIdx), 0x7d7b76));
    }
}
