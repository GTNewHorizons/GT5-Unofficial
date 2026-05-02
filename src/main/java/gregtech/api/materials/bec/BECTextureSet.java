package gregtech.api.materials.bec;

import java.io.Closeable;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import net.minecraft.util.ResourceLocation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;

import gregtech.api.enums.Mods;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.materials.bec.BECPartOrePrefix.BECPrefixMap;
import gregtech.api.util.GTDataUtils;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;

/// Similar to [gregtech.api.enums.TextureSet], except this is entirely controlled by the bec-materials.json file.
/// There is no static field list, similar to GT's system, which means that textures are entirely controlled by resource
/// packs. Maybe this is overkill - we'll have to see how well it scales in the future and how many of its features are
/// used.
public class BECTextureSet implements Closeable {

    private static final Object2ObjectMap<String, BECTextureSet> TEXTURE_SETS = new Object2ObjectOpenHashMap<>();
    private static final Set<String> UNUSED_SETS = new ObjectOpenHashSet<>();

    public final String root;
    public final BECPrefixMap<List<IndexedIcon>> layers = new BECPrefixMap<>();

    public BECTextureSet(String folder) {
        Objects.requireNonNull(folder, "Texture set folder cannot be null");

        this.root = "materialicons/BEC/" + folder;

        load();

        TEXTURE_SETS.put(folder, this);
    }

    /// Adds all texture sets to the static list, so that we can clean up any unused sets later on (to prevent their
    /// icons from being registered into the texture map erroneously).
    public static void startMaterialReload() {
        UNUSED_SETS.clear();
        UNUSED_SETS.addAll(BECTextureSet.TEXTURE_SETS.keySet());
    }

    public static BECTextureSet getTextureSet(String folder) {
        UNUSED_SETS.remove(folder);

        return TEXTURE_SETS.computeIfAbsent(folder, BECTextureSet::new);
    }

    /// Removes any unused icons from unused texture sets.
    public static void endMaterialReload() {
        for (String unused : UNUSED_SETS) {
            BECTextureSet textureSet = TEXTURE_SETS.remove(unused);

            if (textureSet == null) continue;

            textureSet.close();
        }

        UNUSED_SETS.clear();
    }

    private static final Gson GSON = new GsonBuilder()
        .registerTypeAdapter(OrePrefixes.class, (JsonDeserializer<OrePrefixes>) (json, typeOfT, context) -> {
            String name = context.deserialize(json, String.class);

            OrePrefixes prefix = OrePrefixes.getPrefix(name);

            if (prefix == null) {
                throw new IllegalStateException("Invalid ore prefix '" + name + "' in texture-set.json");
            }

            return prefix;
        })
        .create();

    public void load() {
        if (!GTUtility.isClient()) return;

        close();

        ResourceLocation resource = Mods.GregTech.getResourceLocation("textures/items/" + root + "/texture-set.json");

        Map<OrePrefixes, TexturePrefixMeta> textureSetMeta = GTDataUtils
            .loadResourceMerged(GSON, OrePrefixes.class, TexturePrefixMeta.class, resource);

        for (BECPartOrePrefix prefix : BECPartOrePrefix.values()) {
            TexturePrefixMeta meta = textureSetMeta.get(prefix.prefix);

            if (meta == null) {
                layers.put(prefix, Collections.emptyList());
            } else {
                layers.put(
                    prefix,
                    meta.layers.stream()
                        .map(this::getIcon)
                        .collect(Collectors.toList()));
            }
        }
    }

    @Override
    public void close() {
        for (Map.Entry<BECPartOrePrefix, List<IndexedIcon>> entry : layers.entrySet()) {
            List<IndexedIcon> indexedIcons = entry.getValue();

            for (IndexedIcon indexedIcon : indexedIcons) {
                indexedIcon.close();
            }
        }

        layers.clear();
    }

    public static void reload() {
        TEXTURE_SETS.values()
            .forEach(BECTextureSet::load);
    }

    private IndexedIcon getIcon(String suffix) {
        return IndexedIcon.getIcon(IndexedIcon.TextureMapType.ITEMS, root + "/" + suffix);
    }

    /// DTO class that's used for loading the texture set layers from texture-set.json files
    private static class TexturePrefixMeta {

        public List<String> layers;
    }
}
