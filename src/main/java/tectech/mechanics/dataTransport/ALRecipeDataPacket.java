package tectech.mechanics.dataTransport;

import java.util.LinkedHashSet;

import net.minecraft.nbt.NBTTagCompound;

import com.google.common.collect.ImmutableSet;

import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;

public class ALRecipeDataPacket {

    public static final ALRecipeDataPacket EMPTY_PACKET = new ALRecipeDataPacket(ImmutableSet.of());

    public final ImmutableSet<RecipeAssemblyLine> contents;

    public ALRecipeDataPacket(ImmutableSet<RecipeAssemblyLine> contents) {
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

            this.contents = ImmutableSet.copyOf(recipes);
        } else {
            this.contents = ImmutableSet.of();
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

            int i = 0;
            for (RecipeAssemblyLine content : contents) {
                compound.setTag(Integer.toString(i), AssemblyLineUtils.saveRecipe(content));
                i++;
            }
        }

        return root;
    }
}
