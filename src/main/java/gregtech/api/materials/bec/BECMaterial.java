package gregtech.api.materials.bec;

import java.text.MessageFormat;
import java.util.Collection;
import java.util.EnumSet;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import org.jetbrains.annotations.Nullable;

import com.gtnewhorizon.gtnhlib.color.ImmutableColor;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.materials.MaterialWithParts;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;

public class BECMaterial implements MaterialWithParts {

    public static final ItemBECMaterialPart ITEM = new ItemBECMaterialPart();

    public static final BECMaterial[] MATERIALS = new BECMaterial[1000];
    public static final Map<String, BECMaterial> MATERIALS_BY_NAME = new Object2ObjectOpenHashMap<>();

    public final int id;
    public final String name;

    public BECTextureSet textureSet;
    public final PartOrePrefix.PrefixMap<Int2ObjectMap<ImmutableColor>> palettesByPrefix = new PartOrePrefix.PrefixMap<>();

    public final EnumSet<PartOrePrefix> presentParts;

    public BECMaterial(int id, String name, Collection<PartOrePrefix> presentParts) {
        this.id = id;
        this.name = name;
        this.presentParts = EnumSet.copyOf(presentParts);

        if (MATERIALS[id] != null) {
            throw new IllegalArgumentException(
                MessageFormat.format(
                    "cannot register material {0} with id {1} because this id is already taken by material {2}",
                    name,
                    id,
                    MATERIALS[id].name));
        }

        MATERIALS[id] = this;
        MATERIALS_BY_NAME.put(name, this);
    }

    @Override
    public @Nullable ItemStack getPart(OrePrefixes prefix, int amount) {
        return ITEM.getPart(this, prefix, amount);
    }

    public String getLocalizedName() {
        return GTUtility.translate(MessageFormat.format("material.{0}.name", name));
    }

    public String getItemName(OrePrefixes prefix) {
        String override = MessageFormat.format("override.{0}{1}.name", prefix.name(), name);

        if (StatCollector.canTranslate(override)) return GTUtility.translate(override);

        String prefixKey = MessageFormat.format("prefix.{0}.name", prefix.name());

        return GTUtility.translate(prefixKey, getLocalizedName());
    }
}
