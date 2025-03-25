package tectech.mechanics.dataTransport;

import java.util.ArrayList;

import net.minecraft.nbt.NBTTagCompound;

import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;

public class ALRecipeDataPacket extends DataPacket<RecipeAssemblyLine[]> {

    public ALRecipeDataPacket(RecipeAssemblyLine[] content) {
        super(content);
    }

    public ALRecipeDataPacket(NBTTagCompound compound) {
        super(compound);
    }

    @Override
    protected RecipeAssemblyLine[] contentFromNBT(NBTTagCompound nbt) {
        int count = nbt.getInteger("count");

        if (count > 0) {
            ArrayList<RecipeAssemblyLine> recipes = new ArrayList<>();

            for (int i = 0; i < count; i++) {
                recipes.addAll(AssemblyLineUtils.loadRecipe(nbt.getCompoundTag(Integer.toString(i))));
            }

            return recipes.toArray(new RecipeAssemblyLine[0]);
        }

        return new RecipeAssemblyLine[0];
    }

    @Override
    protected NBTTagCompound contentToNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        if (content != null && content.length > 0) {
            compound.setInteger("count", content.length);

            for (int i = 0; i < content.length; i++) {
                compound.setTag(Integer.toString(i), AssemblyLineUtils.saveRecipe(content[i]));
            }
        }

        return compound;
    }

    @Override
    public boolean extraCheck() {
        return true;
    }

    @Override
    protected RecipeAssemblyLine[] unifyContentWith(RecipeAssemblyLine[] content) {
        throw new NoSuchMethodError("Unavailable to unify item stack data packet");
    }

    @Override
    public String getContentString() {
        return "Stack Count: " + (content == null ? 0 : content.length);
    }
}
