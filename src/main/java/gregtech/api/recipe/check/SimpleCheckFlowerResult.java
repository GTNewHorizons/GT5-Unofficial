package gregtech.api.recipe.check;

import java.util.HashSet;
import java.util.StringJoiner;

import javax.annotation.Nonnull;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

/**
 * Simple implementation of {@link CheckRecipeResult}. Missing flower details would be provided.
 */

public class SimpleCheckFlowerResult implements CheckRecipeResult {

    private HashSet<String> flowers;

    private final static String key = "MegaApiary_flowererror";

    SimpleCheckFlowerResult() {
        this.flowers = new HashSet<>();
    }

    SimpleCheckFlowerResult(HashSet<String> flowers) {
        this.flowers = new HashSet<>(flowers);
    }

    @Override
    public @NotNull String getID() {
        return "simple_flower_result";
    }

    @Override
    public boolean wasSuccessful() {
        return false;
    }

    @Override
    @Nonnull
    public @NotNull String getDisplayString() {
        if (flowers.isEmpty()) {
            return StatCollector.translateToLocal("GT5U.gui.text.MegaApiary_noflowers");
        }
        String text = StatCollector.translateToLocal("GT5U.gui.text." + key);
        StringJoiner joiner = new StringJoiner(",");
        for (String flower : flowers) {
            joiner.add(flower);
        }

        return String.format(text, joiner.toString());
    }

    @Override
    public @NotNull NBTTagCompound writeToNBT(@NotNull NBTTagCompound tag) {
        NBTTagList tagList = new NBTTagList();
        for (String flower : flowers) {
            tagList.appendTag(new NBTTagString(flower));
        }
        tag.setTag("flowers", tagList);
        return tag;
    }

    @Override
    public void readFromNBT(@NotNull NBTTagCompound tag) {
        flowers.clear();
        NBTTagList tagList = tag.getTagList("flowers", 10);
        for (int i = 0; i < tagList.tagCount(); i++) {
            String flower = tagList.getStringTagAt(i);
            flowers.add(flower);
        }
    }

    @Override
    @Nonnull
    public @NotNull CheckRecipeResult newInstance() {
        return new SimpleCheckFlowerResult(new HashSet<String>());
    }

    @Override
    public void encode(@Nonnull PacketBuffer buffer) {
        StringJoiner joiner = new StringJoiner(",");
        for (String flower : flowers) {
            joiner.add(flower);
        }
        NetworkUtils.writeStringSafe(buffer, joiner.toString());
    }

    @Override
    public void decode(@Nonnull PacketBuffer buffer) {
        String[] flowers = NetworkUtils.readStringSafe(buffer)
            .split(",");
        for (String flower : flowers) {
            this.flowers.add(flower);
        }
    }

    @Override
    public boolean persistsOnShutdown() {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SimpleCheckFlowerResult that = (SimpleCheckFlowerResult) o;
        return flowers.equals(that.flowers);
    }

    @Nonnull
    public static CheckRecipeResult ofFailure(HashSet<String> flowers) {
        return new SimpleCheckFlowerResult(flowers);
    }
}
