package gregtech.loaders.postload;

import java.util.Map;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;

import com.gtnewhorizons.postea.api.ChunkTransformContext;
import com.gtnewhorizons.postea.api.CustomDataTransformContext;
import com.gtnewhorizons.postea.api.IVersionedTransformer;
import com.gtnewhorizons.postea.api.PlayerDataTransformContext;
import com.gtnewhorizons.postea.api.VersionedReplacementManager;

/// Rewrites saved references to retired fluid names onto the fluid that replaced them, in chunks (tile entities
/// and entities), player data and registered custom storage. A reference is a string tag holding the old name
/// under `FluidName` (Forge's `FluidStack` form) or under any other key naming a fluid, which covers the
/// bare-string fields machines, covers and filters keep. Numeric fluid ids and data outside the world are not
/// reached.
public final class FluidNameMigration implements IVersionedTransformer {

    /// Retired fluid name -> the fluid that replaced it. Bartworks' iodine was the gas slot of the Iodine material
    /// that merged with gtPlusPlus', whose molten iodine the recipes use.
    private static final Map<String, String> RENAMES = Map.of("iodine", "molten.iodine");

    private static final int NBT_COMPOUND = 10;

    private FluidNameMigration() {}

    public static void register() {
        VersionedReplacementManager.register(new FluidNameMigration());
    }

    @Override
    public String key() {
        return "gregtech:fluidNames";
    }

    @Override
    public int currentVersion() {
        return 1;
    }

    @Override
    public void transformChunk(ChunkTransformContext ctx) {
        rename(ctx.tileEntityTags());
        rename(ctx.entityTags());
    }

    @Override
    public void transformPlayer(PlayerDataTransformContext ctx) {
        rename(ctx.playerTag());
    }

    @Override
    public void transformCustomData(CustomDataTransformContext ctx) {
        rename(ctx.root());
    }

    static void rename(NBTBase tag) {
        if (tag instanceof NBTTagList list) {
            if (list.func_150303_d() != NBT_COMPOUND) return;
            for (int i = 0; i < list.tagCount(); i++) rename(list.getCompoundTagAt(i));
        } else if (tag instanceof NBTTagCompound compound) {
            for (Object key : compound.func_150296_c()) {
                String name = (String) key;
                NBTBase value = compound.getTag(name);
                if (value instanceof NBTTagString string) {
                    String renamed = RENAMES.get(string.func_150285_a_());
                    if (renamed != null && name.toLowerCase()
                        .contains("fluid")) compound.setString(name, renamed);
                } else {
                    rename(value);
                }
            }
        }
    }

}
