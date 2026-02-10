package tectech.thing.metaTileEntity.multi.godforge.color;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;

import com.cleanroommc.modularui.value.sync.GenericListSyncHandler;

public class StarColorStorage {

    private static final String NBT_LIST_KEY = "customStarColors";

    private final Map<String, ForgeOfGodsStarColor> nameMapping = new HashMap<>();
    private final List<ForgeOfGodsStarColor> indexMapping = new ArrayList<>();

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
        int i = 1;
        while (nameMapping.containsKey(name)) {
            name = "New Star Color " + i;
            i++;
        }
        return new ForgeOfGodsStarColor(name);
    }

    /** Store a unique star color. Will append numbers to the name to guarantee a unique name. */
    public void store(ForgeOfGodsStarColor color) {
        indexMapping.add(color);
        String name = color.getName();
        int i = 1;
        while (nameMapping.containsKey(name)) {
            name = color.getName() + i;
            i++;
        }

        if (!color.getName()
            .equals(name)) {
            color.setName(name);
        }
        nameMapping.put(name, color);
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

    public GenericListSyncHandler<ForgeOfGodsStarColor> getSyncer() {
        return GenericListSyncHandler.<ForgeOfGodsStarColor>builder()
            .getter(() -> indexMapping)
            .setter(val -> {
                indexMapping.clear();
                nameMapping.clear();
                for (ForgeOfGodsStarColor color : val) {
                    indexMapping.add(color);
                    nameMapping.put(color.getName(), color);
                }
            })
            .serializer(ForgeOfGodsStarColor::writeToBuffer)
            .deserializer(ForgeOfGodsStarColor::readFromBuffer)
            .build();
    }
}
