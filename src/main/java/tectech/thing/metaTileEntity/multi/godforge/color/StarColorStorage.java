package tectech.thing.metaTileEntity.multi.godforge.color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

public class StarColorStorage {

    public static final int MAX_STAR_COLORS = 7;
    private static final String NBT_LIST_KEY = "customStarColors";

    private final Map<String, ForgeOfGodsStarColor> nameMapping = new HashMap<>(MAX_STAR_COLORS);
    private List<ForgeOfGodsStarColor> indexMapping = new ArrayList<>(MAX_STAR_COLORS);

    public StarColorStorage() {
        initPresets();
    }

    private void initPresets() {
        List<ForgeOfGodsStarColor> presets = ForgeOfGodsStarColor.getDefaultColors();
        for (ForgeOfGodsStarColor preset : presets) {
            nameMapping.put(preset.getName(), preset);
            indexMapping.add(preset);
        }
    }

    public ForgeOfGodsStarColor newTemplateColor() {
        String name = "New Star Color";
        for (int i = 0; i < MAX_STAR_COLORS; i++) {
            if (nameMapping.containsKey(name)) {
                name = "New Star Color " + (i + 1);
            } else break;
        }
        return new ForgeOfGodsStarColor(name);
    }

    /** Store a unique star color. Will append numbers to the name to guarantee a unique name. */
    public void store(ForgeOfGodsStarColor color) {
        indexMapping.add(color);
        if (!nameMapping.containsKey(color.getName())) {
            nameMapping.put(color.getName(), color);
            return;
        }
        for (int i = 0; i < MAX_STAR_COLORS; i++) {
            String newName = color.getName() + " " + (i + 1);
            if (!nameMapping.containsKey(newName)) {
                nameMapping.put(newName, color);
                color.setName(newName);
                return;
            }
        }
    }

    /** Insert a star color at a given position. */
    public void insert(ForgeOfGodsStarColor color, int pos) {
        ForgeOfGodsStarColor existing = indexMapping.set(pos, color);
        if (existing != null) {
            nameMapping.remove(existing.getName());
            nameMapping.put(color.getName(), color);
        }
    }

    public void drop(ForgeOfGodsStarColor color) {
        ForgeOfGodsStarColor existing = nameMapping.remove(color.getName());
        if (existing != null) {
            indexMapping.remove(existing);
        }
    }

    public ForgeOfGodsStarColor getByName(String name) {
        return nameMapping.get(name);
    }

    public ForgeOfGodsStarColor getByIndex(int index) {
        return indexMapping.get(index);
    }

    public boolean isFull() {
        return indexMapping.size() >= MAX_STAR_COLORS;
    }

    public int size() {
        return indexMapping.size();
    }

    public void serializeToNBT(NBTTagCompound NBT) {
        NBTTagList tagList = new NBTTagList();
        for (ForgeOfGodsStarColor color : indexMapping) {
            if (color.isPresetColor()) continue;
            tagList.appendTag(color.serializeToNBT());
        }
        if (tagList.tagCount() > 0) {
            NBT.setTag(NBT_LIST_KEY, tagList);
        }
    }

    public void rebuildFromNBT(NBTTagCompound NBT) {
        nameMapping.clear();
        indexMapping.clear();
        initPresets();

        if (NBT.hasKey(NBT_LIST_KEY)) {
            NBTTagList tagList = NBT.getTagList(NBT_LIST_KEY, Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < tagList.tagCount(); i++) {
                NBTTagCompound colorNBT = tagList.getCompoundTagAt(i);
                ForgeOfGodsStarColor color = ForgeOfGodsStarColor.deserialize(colorNBT);
                if (!color.isPresetColor()) {
                    store(color);
                }
            }
        }
    }

    public FakeSyncWidget<?> getSyncer() {
        return new FakeSyncWidget.ListSyncer<>(() -> indexMapping, val -> {
            indexMapping = val;
            nameMapping.clear();
            for (ForgeOfGodsStarColor color : indexMapping) {
                nameMapping.put(color.getName(), color);
            }
        }, ForgeOfGodsStarColor::writeToBuffer, ForgeOfGodsStarColor::readFromBuffer);
    }
}
