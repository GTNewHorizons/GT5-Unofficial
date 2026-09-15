package gregtech.common.items.tools;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;

/**
 * The material &lt;-&gt; metadata mapping shared by every standalone tool item.
 * <p/>
 * All tool items agree on it, so a Copper wrench and a Copper LV wrench carry the same metadata and differ only by
 * which item they are. The Postea migration handler relies on that agreement too.
 * <p/>
 * Two bands exist:
 * <ul>
 * <li><b>0 - 999</b>: ordinary GregTech materials, at their existing {@link Materials#mMetaItemSubID}, which is what
 * {@link gregtech.api.items.MetaGeneratedItem} already uses for dusts, plates and the rest.</li>
 * <li><b>16000 and up</b>: Bartworks Werkstoffs, at {@link #WERKSTOFF_META_OFFSET} plus the Werkstoff's own id. Their
 * bridge materials are built at runtime by {@code MaterialBuilder} and keep the default {@code mMetaItemSubID} of -1,
 * so they have no slot in the first band. Werkstoff ids are fixed per Werkstoff, so this stays stable across
 * restarts.</li>
 * </ul>
 */
public final class ToolMaterialIndex {

    /** Start of the Werkstoff band. Chosen to clear both the 1000 GT material ids and the Werkstoff id offsets. */
    public static final int WERKSTOFF_META_OFFSET = 16_000;

    /** Item metadata is a short, and 32767 is the wildcard value, so nothing may be assigned at or above this. */
    private static final int MAX_META = 32_767;

    private static final Int2ObjectOpenHashMap<Materials> MATERIAL_BY_META = new Int2ObjectOpenHashMap<>();
    private static final Object2IntOpenHashMap<Materials> META_BY_MATERIAL = new Object2IntOpenHashMap<>();

    static {
        META_BY_MATERIAL.defaultReturnValue(-1);
    }

    private ToolMaterialIndex() {}

    /**
     * Assigns a material its metadata, which for an ordinary GregTech material is simply its existing sub id.
     *
     * @return the metadata, or -1 if the material has no sub id and therefore cannot be encoded.
     */
    public static synchronized int assign(Materials material) {
        return assign(material, -1);
    }

    /**
     * Assigns a material its metadata, falling back to {@code preferredMeta} for a material that has no sub id -- a
     * Bartworks bridge material, in practice.
     * <p/>
     * The first assignment for a material wins, and a material that has a sub id always uses it whatever the caller
     * suggests. That keeps the mapping independent of the order the recipe loaders happen to run in: a Werkstoff whose
     * bridge material is a real GregTech material still lands in the ordinary band.
     *
     * @return the metadata, or -1 if there is none to be had or it is already taken by a different material.
     */
    public static synchronized int assign(Materials material, int preferredMeta) {
        if (material == null || material == Materials._NULL) return -1;
        int existing = META_BY_MATERIAL.getInt(material);
        if (existing >= 0) return existing;

        int subID = material.mMetaItemSubID;
        int meta = subID >= 0 && subID < GregTechAPI.sGeneratedMaterials.length ? subID : preferredMeta;
        if (meta < 0 || meta >= MAX_META) return -1;

        Materials other = MATERIAL_BY_META.get(meta);
        if (other != null && other != material) return -1;

        MATERIAL_BY_META.put(meta, material);
        META_BY_MATERIAL.put(material, meta);
        return meta;
    }

    /**
     * @return the metadata assigned to this material, or -1 if it has none.
     */
    public static int getMeta(Materials material) {
        if (material == null || material == Materials._NULL) return -1;
        int meta = META_BY_MATERIAL.getInt(material);
        if (meta >= 0) return meta;
        // Not registered for any tool, but still addressable if it has an ordinary sub id.
        int subID = material.mMetaItemSubID;
        return subID >= 0 && subID < GregTechAPI.sGeneratedMaterials.length ? subID : -1;
    }

    /**
     * @return the material this metadata stands for, or {@link Materials#_NULL} if it stands for none.
     */
    public static Materials getMaterial(int meta) {
        if (meta < 0) return Materials._NULL;
        Materials material = MATERIAL_BY_META.get(meta);
        if (material != null) return material;
        if (meta < GregTechAPI.sGeneratedMaterials.length) {
            material = GregTechAPI.sGeneratedMaterials[meta];
            if (material != null) return material;
        }
        return Materials._NULL;
    }
}
