package gregtech.api.material;

import net.minecraft.item.ItemStack;

import org.jetbrains.annotations.Nullable;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.StackMaterialInfo;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.util.GTUtility;

/// Applies each material's [ProcessingSubstitute] to the outputs of the recipes ore processing generates. An output
/// whose material declares a substitute becomes the same dust form of the substitute, scaled by the substitute's
/// [ProcessingSubstitute.Rule]. Outputs are rewritten in place and returned.
///
/// `input` is the material the recipe processes; a null input spares no output.
public final class ProcessingSubstitutes {

    private ProcessingSubstitutes() {}

    /// Applies every substitute to the outputs of an ore-processing recipe, doubling concentrates.
    public static ItemStack[] ore(@Nullable Material input, ItemStack... outputs) {
        return replace(input, outputs, true, 2);
    }

    /// Applies every substitute to the outputs of a decomposition or washing recipe, keeping concentrates at their
    /// amount.
    public static ItemStack[] decomposition(@Nullable Material input, ItemStack... outputs) {
        return replace(input, outputs, true, 1);
    }

    /// Applies only the platinum-group substitutes.
    public static ItemStack[] platinumGroup(@Nullable Material input, ItemStack... outputs) {
        return replace(input, outputs, false, 0);
    }

    /// Applies the smelting substitutes to `output`, the result of smelting `inputPrefix` of `input`. Smelting a
    /// material's own dust keeps its metal.
    public static @Nullable ItemStack smelting(Material input, OrePrefixes inputPrefix, @Nullable ItemStack output) {
        StackMaterialInfo info = lookup(output);
        if (info == null) return output;
        ProcessingSubstitute substitute = info.material()
            .getProperty(GTMaterialProperties.PROCESSING_SUBSTITUTE);
        if (substitute == null || !substitute.rule().smelting || spares(input, substitute)) return output;
        if (input == info.material() && (inputPrefix == OrePrefixes.dust || inputPrefix == OrePrefixes.dustTiny)) {
            return output;
        }
        Shape shape = info.shape() == Shapes.nugget ? Shapes.dustTiny : Shapes.dust;
        return MaterialLibAPI.getStack(
            substitute.into()
                .resolve(),
            shape,
            Math.min(64, output.stackSize * 2));
    }

    /// Applies `material`'s substitute to `output`, its dust from a crafting-table hammer recipe.
    public static @Nullable ItemStack crafting(@Nullable Material material, @Nullable ItemStack output) {
        if (material == null || !GTUtility.isStackValid(output)) return output;
        ProcessingSubstitute substitute = material.getProperty(GTMaterialProperties.PROCESSING_SUBSTITUTE);
        if (substitute == null || substitute.rule().craftingMultiplier == 0) return output;
        return MaterialLibAPI.getStack(
            substitute.into()
                .resolve(),
            Shapes.dust,
            Math.min(64, output.stackSize * substitute.rule().craftingMultiplier));
    }

    /// The clean dust that `stackSize` impure or purified dusts of `material` wash into in a cauldron.
    public static @Nullable ItemStack washing(Material material, int stackSize) {
        ProcessingSubstitute substitute = material.getProperty(GTMaterialProperties.PROCESSING_SUBSTITUTE);
        Material clean = substitute != null && substitute.rule().washes ? substitute.into()
            .resolve() : material;
        return MaterialLibAPI.findStack(clean, Shapes.dust, stackSize);
    }

    private static ItemStack[] replace(@Nullable Material input, ItemStack[] outputs, boolean concentrates,
        int concentrateMultiplier) {
        for (int i = 0; i < outputs.length; i++) {
            StackMaterialInfo info = lookup(outputs[i]);
            if (info == null) continue;
            ProcessingSubstitute substitute = info.material()
                .getProperty(GTMaterialProperties.PROCESSING_SUBSTITUTE);
            if (substitute == null || spares(input, substitute)) continue;
            ProcessingSubstitute.Rule rule = substitute.rule();
            if (!rule.platinumGroup && !concentrates) continue;
            Shape shape = substituteShape(info.shape(), rule);
            if (shape == null) continue;
            int multiplier = rule.platinumGroup ? 2 : concentrateMultiplier;
            outputs[i] = MaterialLibAPI.getStack(
                substitute.into()
                    .resolve(),
                shape,
                Math.min(64, outputs[i].stackSize * multiplier));
        }
        return outputs;
    }

    private static @Nullable StackMaterialInfo lookup(@Nullable ItemStack stack) {
        return GTUtility.isStackValid(stack) ? MaterialLibAPI.lookupStack(stack) : null;
    }

    private static boolean spares(@Nullable Material input, ProcessingSubstitute substitute) {
        return substitute.rule().platinumGroup
            && MaterialUtils.hasFlag(input, GTMaterialFlag.KEEPS_PLATINUM_GROUP_OUTPUTS);
    }

    /// The dust form of the substitute that replaces `shape`, or null when the rule leaves `shape` alone.
    private static @Nullable Shape substituteShape(Shape shape, ProcessingSubstitute.Rule rule) {
        if (shape == Shapes.dust) return Shapes.dust;
        if (shape == Shapes.dustImpure || shape == Shapes.dustPure) return rule.platinumGroup ? Shapes.dust : null;
        if (shape == Shapes.dustSmall || shape == Shapes.dustTiny) return rule.smallDusts ? shape : null;
        return null;
    }
}
