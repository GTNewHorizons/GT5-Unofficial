package gregtech.api.materials.bec;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizon.gtnhlib.color.ImmutableColor;
import com.gtnewhorizon.gtnhlib.itemrendering.IItemTexture;
import com.gtnewhorizon.gtnhlib.itemrendering.ItemWithTextures;
import com.gtnewhorizon.gtnhlib.itemrendering.TexturedItemRenderer;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTDataUtils;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

public class ItemBECMaterialPart extends Item implements ItemWithTextures {

    /// The metadata for a standard part is broken up into N prefixes, combined with M materials. Typically, N = 32 and
    /// M = 1000, which allows us to use most of the 32k ([Short#MAX_VALUE]) metadata values we have available. Mods
    /// like NEID and EID extend this further, but we don't need that many permutations.
    public static final int MATERIALS_PER_PREFIX = 1000;

    public final OrePrefixes[] prefixes = { OrePrefixes.plate, OrePrefixes.foil, OrePrefixes.stickLong,
        OrePrefixes.stick, OrePrefixes.bolt, OrePrefixes.ring, OrePrefixes.wireFine, OrePrefixes.lens, };

    public ItemBECMaterialPart() {
        setHasSubtypes(true);
        setUnlocalizedName("gt.bec.part");

        if (prefixes.length > 32) throw new IllegalStateException("cannot have more than 32 prefixes");
    }

    public void register() {
        GameRegistry.registerItem(this, "gt.bec.part");

        if (FMLCommonHandler.instance()
            .getSide()
            .isClient()) {
            registerRenderer();
        }

        for (OrePrefixes prefix : prefixes) {
            for (BECMaterial material : BECMaterial.MATERIALS) {
                if (material == null) continue;

                ItemStack stack = getPart(material, prefix, 1);

                if (stack == null) continue;

                OreDictionary.registerOre(prefix + material.name.replaceAll("[^a-zA-Z0-9]", ""), stack);
            }
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack stack) {
        return "gt.bec.part." + stack.getItemDamage();
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        BECMaterial mat = getMaterial(stack);

        if (mat == null) return "invalid material";

        OrePrefixes prefix = getPrefix(stack);

        if (prefix == null) return "invalid prefix";

        return mat.getItemName(prefix);
    }

    @SideOnly(Side.CLIENT)
    private void registerRenderer() {
        TexturedItemRenderer.register(this);
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List<ItemStack> subItems) {
        for (OrePrefixes prefix : prefixes) {
            for (BECMaterial material : BECMaterial.MATERIALS) {
                if (material == null) continue;

                ItemStack stack = getPart(material, prefix, 1);

                if (stack == null) continue;

                subItems.add(stack);
            }
        }
    }

    public @Nullable BECMaterial getMaterial(ItemStack stack) {
        return GTDataUtils.getIndexSafe(BECMaterial.MATERIALS, stack.getItemDamage() % MATERIALS_PER_PREFIX);
    }

    public @Nullable OrePrefixes getPrefix(ItemStack stack) {
        return GTDataUtils.getIndexSafe(prefixes, stack.getItemDamage() / MATERIALS_PER_PREFIX);
    }

    public ItemStack getPart(BECMaterial material, OrePrefixes prefix, int amount) {
        if (!material.presentParts.contains(BECPartOrePrefix.fromPrefix(prefix))) return null;

        int i = GTDataUtils.findIndex(prefixes, prefix);

        if (i == -1) return null;

        return new ItemStack(this, amount, material.id + i * MATERIALS_PER_PREFIX);
    }

    private Int2ObjectMap<ImmutableColor> getPalette(ItemStack stack) {
        BECMaterial mat = getMaterial(stack);

        if (mat == null) return null;

        OrePrefixes prefix = getPrefix(stack);

        if (prefix == null) return null;

        return mat.palettesByPrefix.get(prefix);
    }

    @Override
    public IItemTexture[] getTextures(ItemStack stack) {
        BECMaterial mat = getMaterial(stack);

        if (mat == null) return null;

        OrePrefixes prefix = getPrefix(stack);

        if (prefix == null) return null;

        Int2ObjectMap<ImmutableColor> palette = mat.palettesByPrefix.get(prefix);

        if (palette == null) return null;

        List<IndexedIcon> layers = mat.textureSet.layers.get(prefix);

        return GTDataUtils
            .mapToArray(layers, IItemTexture[]::new, indexedIcon -> indexedIcon.asTexture(this::getPalette));
    }
}
