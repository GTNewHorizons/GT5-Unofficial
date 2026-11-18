package tectech.mechanics.dataTransport;

import java.util.LinkedHashSet;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.ImmutableList;

import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;

public class ALRecipeDataPacket {

    public static final RecipeAssemblyLine[] EMPTY_ARRAY = new RecipeAssemblyLine[0];
    public static final ALRecipeDataPacket EMPTY_PACKET = new ALRecipeDataPacket(ImmutableList.of());

    public final ImmutableList<RecipeAssemblyLine> contents;

    public ALRecipeDataPacket(ImmutableList<RecipeAssemblyLine> contents) {
        this.contents = contents;
    }

    public ALRecipeDataPacket(NBTTagCompound compound) {
        NBTTagCompound qContent = compound.getCompoundTag("qContent");

        int count = qContent.getInteger("count");

        if (count > 0) {
            // loadRecipe returns every recipe sharing the saved output and lists don't dedupe
            LinkedHashSet<RecipeAssemblyLine> recipes = new LinkedHashSet<>();

            for (int i = 0; i < count; i++) {
                recipes.addAll(AssemblyLineUtils.loadRecipe(qContent.getCompoundTag(Integer.toString(i))));
            }

            this.contents = ImmutableList.copyOf(recipes);
        } else {
            this.contents = ImmutableList.of();
        }
    }

    public boolean isEmpty() {
        return contents.isEmpty();
    }

    public NBTTagCompound toNBT() {
        NBTTagCompound root = new NBTTagCompound();

        NBTTagCompound compound = new NBTTagCompound();
        root.setTag("qContent", compound);

        if (!contents.isEmpty()) {
            compound.setInteger("count", contents.size());

            for (int i = 0; i < contents.size(); i++) {
                compound.setTag(Integer.toString(i), AssemblyLineUtils.saveRecipe(contents.get(i)));
            }
        }

        return root;
    }
}
