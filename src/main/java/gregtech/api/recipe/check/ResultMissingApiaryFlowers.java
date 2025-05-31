package gregtech.api.recipe.check;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.network.NetworkUtils;

public class ResultMissingApiaryFlowers implements CheckRecipeResult {

    protected final Set<String> missingFlowerTypes;

    public ResultMissingApiaryFlowers() {
        this(new HashSet<>());
    }

    protected ResultMissingApiaryFlowers(Set<String> missingFlowerTypes) {
        this.missingFlowerTypes = missingFlowerTypes;
    }

    public static @NotNull CheckRecipeResult newFailure(@NotNull Map<String, String> flowerCheckingMap) {
        return new ResultMissingApiaryFlowers(new HashSet<>(flowerCheckingMap.values()));
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @NotNull
    @Override
    public String getID() {
        return "apiary_missing_flowers";
    }

    @NotNull
    @Override
    public String getDisplayString() {
        String flowers = String.join(", ", missingFlowerTypes);
        return StatCollector.translateToLocalFormatted("GT5U.gui.text.apiary_missing_flowers", flowers);
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        NBTTagList flowerTypeList = new NBTTagList();
        for (String flowerType : missingFlowerTypes) {
            flowerTypeList.appendTag(new NBTTagString(flowerType));
        }
        tag.setTag("missingFlowers", flowerTypeList);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        NBTTagList flowerTypeList = tag.getTagList("missingFlowers", 8);
        for (int i = 0; i < flowerTypeList.tagCount(); i++) {
            String flowerType = flowerTypeList.getStringTagAt(i);
            missingFlowerTypes.add(flowerType);
        }
    }

    @NotNull
    @Override
    public CheckRecipeResult newInstance() {
        return new ResultMissingApiaryFlowers();
    }

    @Override
    public void encode(@NotNull PacketBuffer buffer) {
        buffer.writeInt(missingFlowerTypes.size());
        for (String missingFlowerType : missingFlowerTypes) {
            NetworkUtils.writeStringSafe(buffer, missingFlowerType);
        }
    }

    @Override
    public void decode(PacketBuffer buffer) {
        missingFlowerTypes.clear();
        int length = buffer.readInt();
        for (int i = 0; i < length; i++) {
            missingFlowerTypes.add(NetworkUtils.readStringSafe(buffer));
        }
    }

}
