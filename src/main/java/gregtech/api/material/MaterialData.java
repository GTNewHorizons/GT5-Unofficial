package gregtech.api.material;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.util.GTRecipeBuilder.WILDCARD;

import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.objects.GTItemStack;
import gregtech.api.objects.ItemData;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTRecipeRegistrator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.OreDictUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;

/// What each item stack is made of ([ItemData]), keyed by item and damage.
///
/// An entry is an association (`plateIron` as a plate of Iron), a composition (a machine's plates and circuits), or
/// both merged.
///
/// Storing an entry with recyclable material registers the stack's recycling recipes. Unified copies
/// ([OreDictUtils#isUnifiedCopy]) get none, and an item interchangeable with its oredict target
/// ([OreDictUtils#isInterchangeableWithTarget]) gets no machine recipes.
public final class MaterialData {

    private static final Map<ItemStack, ItemData> dataByStack = new Object2ObjectOpenCustomHashMap<>(
        GTItemStack.ITEMSTACK_HASH_STRATEGY2);
    private static boolean batchRegisteredRecyclingRecipes = false;

    private MaterialData() {}

    /// Adds `data` for `stack`, unless the stack already has an explicit composition.
    public static void add(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || data == null) return;

        ItemData prevData = get(stack);
        if (prevData == null || !prevData.hasExplicitComposition) {
            set(stack, data);
        }
    }

    /// Adds a composition for `output` made of `inputs`, each an [ItemStack] (read through [#get]) or an
    /// [ItemData], unless the output already has an explicit composition.
    public static void addFromInputs(ItemStack output, Object... inputs) {
        int length = inputs.length;
        ItemData[] dataInputs = new ItemData[length];
        for (int i = 0; i < length; i++) {
            if (inputs[i] instanceof ItemStack stack) {
                dataInputs[i] = get(stack);
            } else if (inputs[i] instanceof ItemData data) {
                dataInputs[i] = data;
            } else {
                throw new IllegalArgumentException("Illegal item data: " + inputs[i]);
            }
        }
        if (GTUtility.arrayContainsNonNull(dataInputs)) {
            add(output, new ItemData(dataInputs));
        }
    }

    /// Sets `data` for `stack`, scaled down to a single item and merged into any existing association.
    private static void set(ItemStack stack, ItemData data) {
        if (GTUtility.isStackInvalid(stack) || data == null) return;

        if (stack.stackSize > 1) {
            data = data.dividedBy(stack.stackSize);
            stack = GTUtility.copyAmount(1, stack);
        }

        ItemData prevData = get(stack);
        if (prevData != null && prevData.hasValidPrefixMaterialData()) {
            data = new ItemData(prevData, data);
        }

        dataByStack.put(stack, data);
        processRecycling(stack, data, prevData);
    }

    public static void remove(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return;
        dataByStack.remove(stack);
    }

    /// Adds an association for a stack: the prefix and material an oredict name stands for, e.g. `plateIron` as
    /// `OrePrefixes.plate` + `Materials.Iron`.
    ///
    /// An existing association is never rewritten. An existing explicit composition is merged into the
    /// association, and the association's material wins when the two differ. A wildcard-damage stack also gets the
    /// association at damages 0 to 15.
    public static void addAssociation(OrePrefixes prefix, Material material, ItemStack stack) {
        if (prefix == null || material == null || GTUtility.isStackInvalid(stack)) return;
        addAssociation(() -> new ItemData(prefix, material), stack);
    }

    /// Same as [#addAssociation(OrePrefixes, Material, ItemStack)], with a name-only association (see
    /// [ItemData#ItemData(OrePrefixes, String)]) for oredict entries whose material side is not a material.
    public static void addAssociation(OrePrefixes prefix, String materialName, ItemStack stack) {
        if (prefix == null || materialName == null || GTUtility.isStackInvalid(stack)) return;
        addAssociation(() -> new ItemData(prefix, materialName), stack);
    }

    @Nullable
    public static ItemData get(ItemStack stack) {
        if (GTUtility.isStackInvalid(stack)) return null;
        ItemData data = dataByStack.get(stack);
        if (data == null) {
            data = dataByStack.get(GTItemStack.internalCopyStack(stack, true));
        }
        return data;
    }

    /// Returns the stack's data when it carries a prefix+material association. Returns null otherwise, including for
    /// a name-only association.
    @Nullable
    public static ItemData getAssociation(ItemStack stack) {
        ItemData data = get(stack);
        return data != null && data.hasValidPrefixMaterialData() ? data : null;
    }

    private static void addAssociation(Supplier<ItemData> association, ItemStack stack) {
        if (Items.feather.getDamage(stack) == WILDCARD) {
            for (byte i = 0; i < 16; i++) {
                addAssociationForStack(association.get(), GTUtility.copyAmountAndMetaData(1, i, stack));
            }
        }

        addAssociationForStack(association.get(), stack);
    }

    private static void addAssociationForStack(ItemData association, ItemStack stack) {
        ItemData prevData = get(stack);
        if (prevData != null && prevData.hasUnificationName()) return;
        if (prevData != null && prevData.hasExplicitComposition) {
            association = new ItemData(association, prevData);
        }
        set(stack, association);
    }

    private static void processRecycling(ItemStack stack, ItemData data, ItemData prevData) {
        if (data.hasValidMaterialData()) {
            long recyclableMaterialAmount = 0;

            if (!MaterialUtils.hasFlag(data.material.material, GTMaterialFlag.NO_RECYCLING)) {
                recyclableMaterialAmount += data.material.amount >= 0 ? data.material.amount : M;
            }

            for (MaterialStack material : data.byProducts) {
                if (!MaterialUtils.hasFlag(material.material, GTMaterialFlag.NO_RECYCLING)) {
                    recyclableMaterialAmount += material.amount >= 0 ? material.amount : M;
                }
            }

            if (recyclableMaterialAmount < M) {
                GTModHandler.addToRecyclerBlackList(stack);
            }
        }

        if (!batchRegisteredRecyclingRecipes) {
            if (!GregTechAPI.sLoadStarted) return;
            batchRegisteredRecyclingRecipes = true;

            for (Entry<ItemStack, ItemData> entry : dataByStack.entrySet()) {
                ItemStack entryStack = entry.getKey();
                ItemData entryData = entry.getValue();

                if (shouldRegisterMaterialRecycling(entryData) && !OreDictUtils.isUnifiedCopy(entryStack)) {
                    registerMaterialRecycling(entryStack, entryData);
                }
            }
        } else if (shouldRegisterMaterialRecycling(data) && !shouldRegisterMaterialRecycling(prevData)
            && !OreDictUtils.isUnifiedCopy(stack)) {
                registerMaterialRecycling(stack, data);
            }
    }

    private static void registerMaterialRecycling(ItemStack stack, ItemData data) {
        if (OreDictUtils.isInterchangeableWithTarget(stack)) {
            GTRecipeBuilder
                .withoutRecipesTaking(stack, () -> GTRecipeRegistrator.registerMaterialRecycling(stack, data));
        } else {
            GTRecipeRegistrator.registerMaterialRecycling(stack, data);
        }
    }

    private static boolean shouldRegisterMaterialRecycling(ItemData data) {
        if (data == null || isHighPressurePipe(data)) return false;
        return data.hasExplicitComposition || !data.hasValidPrefixData() || data.prefix.isRecyclable();
    }

    private static boolean isHighPressurePipe(ItemData data) {
        return !data.hasExplicitComposition && data.hasValidPrefixMaterialData()
            && PipeMaterials.isHighPressurePipe(data.prefix, data.material.material);
    }
}
