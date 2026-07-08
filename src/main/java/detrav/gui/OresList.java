package detrav.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.stream.Collectors;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.client.GuiScrollingList;
import detrav.items.DetravMetaGeneratedTool01;
import detrav.net.ProspectingPacket;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

class OresList extends GuiScrollingList {

    private final Object2IntOpenHashMap<String> colors = new Object2IntOpenHashMap<>();
    private final List<String> allKeys; // every entry, unfiltered
    private final List<String> keys = new ArrayList<>(); // entries currently visible
    private final GuiScreen parent;
    private final BiConsumer<String, Boolean> onSelected;
    private boolean invert = false;
    private String selectedName;

    public OresList(GuiScreen parent, int width, int height, int top, int bottom, int left, int entryHeight,
        ProspectingPacket packet, BiConsumer<String, Boolean> onSelected) {
        super(parent.mc, width, height, top, bottom, left, entryHeight);
        this.parent = parent;
        this.onSelected = onSelected;
        allKeys = packet.objects.short2ObjectEntrySet()
            .stream()
            .map(
                e -> e.getValue()
                    .left())
            .collect(Collectors.toList());
        Collections.sort(allKeys);
        if (packet.ptype == DetravMetaGeneratedTool01.MODE_POLLUTION) {
            allKeys.clear();
            allKeys.add(StatCollector.translateToLocal("gui.detrav.scanner.pollution"));
        } else if (allKeys.size() > 1) {
            allKeys.add(0, "All");
        }
        keys.addAll(allKeys);
        selectedName = keys.isEmpty() ? null : keys.get(0);

        for (var e : packet.objects.short2ObjectEntrySet()) {
            this.colors.put(
                e.getValue()
                    .left(),
                e.getValue()
                    .rightInt());
        }
    }

    /** Rebuilds the visible list from the search query. Selection (by name) is preserved. */
    public void setFilter(String query) {
        keys.clear();
        if (query == null || query.trim()
            .isEmpty()) {
            keys.addAll(allKeys);
            return;
        }
        String lc = query.toLowerCase();
        for (String k : allKeys) {
            if (k.toLowerCase()
                .contains(lc)) keys.add(k);
        }
    }

    @Override
    protected int getSize() {
        return keys.size();
    }

    @Override
    protected void elementClicked(int index, boolean doubleClick) {
        if (index < 0 || index >= keys.size()) return;
        selectedName = keys.get(index);
        if (doubleClick) this.invert = !this.invert;

        if (onSelected != null) onSelected.accept(selectedName, this.invert);
    }

    @Override
    protected boolean isSelected(int index) {
        return selectedName != null && index >= 0 && index < keys.size() && selectedName.equals(keys.get(index));
    }

    @Override
    protected int getContentHeight() {
        // floor at the viewport height so short (filtered) lists stay top-aligned instead of centering
        return Math.max(super.getContentHeight(), bottom - top - 4);
    }

    @Override
    protected void drawBackground() {}

    @Override
    protected void drawSlot(int slotIdx, int entryRight, int slotTop, int slotBuffer, Tessellator tess) {
        String name = keys.get(slotIdx);

        int textLeft = this.left + 3;
        int textColor = 0xFFE0E0E0; // entries without a colour (e.g. "All")
        if (colors.containsKey(name)) {
            int color = 0xFF000000 | colors.getInt(name);
            // true colour swatch, plus the label tinted to match (lifted for legibility)
            Gui.drawRect(this.left + 3, slotTop, this.left + 11, slotTop + 8, color);
            textLeft = this.left + 14;
            textColor = readable(color);
        }

        parent.drawString(
            parent.mc.fontRenderer,
            parent.mc.fontRenderer.trimStringToWidth(name, listWidth - (textLeft - this.left) - 6),
            textLeft,
            slotTop - 1,
            textColor);
    }

    /** Lifts dark ore colours toward white so the label stays readable on the dark list background. */
    private static int readable(int argb) {
        int r = (argb >> 16) & 0xFF, g = (argb >> 8) & 0xFF, b = argb & 0xFF;
        int luma = (r * 299 + g * 587 + b * 114) / 1000;
        if (luma < 120) {
            float t = (120 - luma) / 120f;
            r += (int) ((255 - r) * t);
            g += (int) ((255 - g) * t);
            b += (int) ((255 - b) * t);
        }
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
