package gregtech.loaders.oreprocessing;

import java.util.function.Predicate;
import java.util.function.Supplier;

import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import detrav.items.processing.ProcessingDetravToolProspector;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.materials.BlockShapes;
import gregtech.api.enums.materials.CellShapes;
import gregtech.api.enums.materials.OreShapes;
import gregtech.api.enums.materials.PipeMaterials;
import gregtech.api.enums.materials.Shapes;
import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.interfaces.IOreRecipeRegistrator;
import gregtech.api.material.GTMaterialFlag;
import gregtech.api.material.MaterialParts;
import gregtech.api.material.MaterialUtils;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.common.oredict.OreDictRegistrationHandler;
import gregtech.common.ores.GTOreAdapter;

/// The recipe generators of every MaterialLib part shape. Each row runs a `Processing*` registrator once per material
/// MaterialLib serves the shape for, with that pair's stack, under the oredict prefix the registrator switches on.
/// Rows run at MaterialLib's postInit in table order; the stack-size clamps run at init.
///
/// Materials flagged [GTMaterialFlag#NO_RECIPES], prefixes a material ignores and ignored oredict names are skipped.
/// Registrators are [Supplier]s so each `Processing*` class initializes when its rows first run, after every mod's
/// items exist.
public final class ShapeRecipeTable {

    private ShapeRecipeTable() {}

    /// Registers every row. Runs from `GTMod`'s `MaterialRegistrationEvent` handler.
    public static void register() {
        clamp(BlockShapes.block, OrePrefixes.block);
        clamp(OreShapes.ore, OrePrefixes.ore);
        clamp(OreShapes.oreSmall, OrePrefixes.oreSmall);

        on(BlockShapes.block, OrePrefixes.block, () -> ProcessingBlock.INSTANCE);
        on(BlockShapes.blockCasing, OrePrefixes.blockCasing, () -> ProcessingCasing.INSTANCE);
        onOreVariants(OreShapes.ore, OrePrefixes.ore, () -> ProcessingOre.INSTANCE);
        on(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingRawOre.INSTANCE);
        onOreVariants(OreShapes.ore, OrePrefixes.ore, () -> ProcessingIceOre.INSTANCE);
        on(Shapes.rawOre, OrePrefixes.rawOre, () -> ProcessingIceOre.INSTANCE);
        on(Shapes.crushedCentrifuged, OrePrefixes.crushedCentrifuged, () -> ProcessingCrushedOre.INSTANCE);
        on(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingCrushedOre.INSTANCE);
        on(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingPure.INSTANCE);
        on(Shapes.crushed, OrePrefixes.crushed, () -> ProcessingDirty.INSTANCE);
        on(Shapes.dust, OrePrefixes.dust, () -> ProcessingDust.INSTANCE);
        on(Shapes.dustPure, OrePrefixes.dustPure, () -> ProcessingDust.INSTANCE);
        on(Shapes.dustImpure, OrePrefixes.dustImpure, () -> ProcessingDust.INSTANCE);
        on(Shapes.dustSmall, OrePrefixes.dustSmall, () -> ProcessingDust.INSTANCE);
        on(Shapes.dustTiny, OrePrefixes.dustTiny, () -> ProcessingDust.INSTANCE);
        on(Shapes.dust, OrePrefixes.dust, () -> ProcessingGemImplosion.INSTANCE);
        on(Shapes.crushed, OrePrefixes.crushed, () -> ProcessingOreSmelting.INSTANCE);
        on(Shapes.crushedPurified, OrePrefixes.crushedPurified, () -> ProcessingOreSmelting.INSTANCE);
        on(Shapes.crushedCentrifuged, OrePrefixes.crushedCentrifuged, () -> ProcessingOreSmelting.INSTANCE);
        on(Shapes.dust, OrePrefixes.dust, () -> ProcessingOreSmelting.INSTANCE);
        on(Shapes.dustImpure, OrePrefixes.dustImpure, () -> ProcessingOreSmelting.INSTANCE);
        on(Shapes.dustPure, OrePrefixes.dustPure, () -> ProcessingOreSmelting.INSTANCE);

        on(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingIngot.INSTANCE);
        on(Shapes.ingotHot, OrePrefixes.ingotHot, () -> ProcessingIngot.INSTANCE);
        on(Shapes.plate, OrePrefixes.plate, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateDouble, OrePrefixes.plateDouble, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateTriple, OrePrefixes.plateTriple, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateQuadruple, OrePrefixes.plateQuadruple, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateQuintuple, OrePrefixes.plateQuintuple, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateDense, OrePrefixes.plateDense, () -> ProcessingPlate.INSTANCE);
        on(Shapes.plateSuperdense, OrePrefixes.plateSuperdense, () -> ProcessingPlate.INSTANCE);
        on(Shapes.itemCasing, OrePrefixes.itemCasing, () -> ProcessingPlate.INSTANCE);
        on(Shapes.gearGt, OrePrefixes.gearGt, () -> ProcessingGear.INSTANCE);
        on(Shapes.gearGtSmall, OrePrefixes.gearGtSmall, () -> ProcessingGear.INSTANCE);
        on(Shapes.rotor, OrePrefixes.rotor, () -> ProcessingRotor.INSTANCE);
        on(Shapes.round, OrePrefixes.round, () -> ProcessingRound.INSTANCE);
        on(Shapes.screw, OrePrefixes.screw, () -> ProcessingScrew.INSTANCE);
        on(Shapes.bolt, OrePrefixes.bolt, () -> ProcessingBolt.INSTANCE);
        on(Shapes.stick, OrePrefixes.stick, () -> ProcessingStick.INSTANCE);
        on(Shapes.stickLong, OrePrefixes.stickLong, () -> ProcessingStickLong.INSTANCE);

        on(Shapes.foil, OrePrefixes.foil, () -> ProcessingFoil.INSTANCE);
        on(Shapes.wireFine, OrePrefixes.wireFine, () -> ProcessingFineWire.INSTANCE);
        on(Shapes.nugget, OrePrefixes.nugget, () -> ProcessingNugget.INSTANCE);
        on(Shapes.lens, OrePrefixes.lens, () -> ProcessingLens.INSTANCE);
        on(Shapes.gem, OrePrefixes.gem, () -> ProcessingGem.INSTANCE);
        on(Shapes.gemChipped, OrePrefixes.gemChipped, () -> ProcessingGem.INSTANCE);
        on(Shapes.gemExquisite, OrePrefixes.gemExquisite, () -> ProcessingGem.INSTANCE);
        on(Shapes.gemFlawed, OrePrefixes.gemFlawed, () -> ProcessingGem.INSTANCE);
        on(Shapes.gemFlawless, OrePrefixes.gemFlawless, () -> ProcessingGem.INSTANCE);
        on(Shapes.toolHeadBuzzSaw, OrePrefixes.toolHeadBuzzSaw, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadChainsaw, OrePrefixes.toolHeadChainsaw, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadDrill, OrePrefixes.toolHeadDrill, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadFile, OrePrefixes.toolHeadFile, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadSaw, OrePrefixes.toolHeadSaw, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadWrench, OrePrefixes.toolHeadWrench, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.turbineBlade, OrePrefixes.turbineBlade, () -> ProcessingToolHead.INSTANCE);
        on(Shapes.toolHeadHammer, OrePrefixes.toolHeadHammer, () -> ProcessingToolOther.INSTANCE);

        on(CellShapes.cell, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        on(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        on(CellShapes.cellSolid, OrePrefixes.cell, () -> ProcessingCell.INSTANCE);
        on(CellShapes.cellPlasma, OrePrefixes.cellPlasma, () -> ProcessingCell.INSTANCE);
        on(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingCell.INSTANCE);
        on(TEBlockShapes.wireGt01, OrePrefixes.wireGt01, () -> ProcessingWire.INSTANCE);
        on(TEBlockShapes.wireGt02, OrePrefixes.wireGt02, () -> ProcessingWire.INSTANCE);
        on(TEBlockShapes.wireGt04, OrePrefixes.wireGt04, () -> ProcessingWire.INSTANCE);
        on(TEBlockShapes.wireGt08, OrePrefixes.wireGt08, () -> ProcessingWire.INSTANCE);
        on(TEBlockShapes.wireGt12, OrePrefixes.wireGt12, () -> ProcessingWire.INSTANCE);
        on(TEBlockShapes.wireGt16, OrePrefixes.wireGt16, () -> ProcessingWire.INSTANCE);
        fluidPipe(TEBlockShapes.pipeTiny, OrePrefixes.pipeTiny);
        fluidPipe(TEBlockShapes.pipeSmall, OrePrefixes.pipeSmall);
        fluidPipe(TEBlockShapes.pipeMedium, OrePrefixes.pipeMedium);
        fluidPipe(TEBlockShapes.pipeLarge, OrePrefixes.pipeLarge);
        fluidPipe(TEBlockShapes.pipeHuge, OrePrefixes.pipeHuge);
        fluidPipe(TEBlockShapes.pipeQuadruple, OrePrefixes.pipeQuadruple);
        fluidPipe(TEBlockShapes.pipeNonuple, OrePrefixes.pipeNonuple);
        on(TEBlockShapes.itemPipeTiny, OrePrefixes.pipeTiny, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeSmall, OrePrefixes.pipeSmall, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeMedium, OrePrefixes.pipeMedium, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeLarge, OrePrefixes.pipeLarge, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeHuge, OrePrefixes.pipeHuge, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeRestrictiveTiny, OrePrefixes.pipeRestrictiveTiny, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeRestrictiveSmall, OrePrefixes.pipeRestrictiveSmall, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeRestrictiveMedium, OrePrefixes.pipeRestrictiveMedium, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeRestrictiveLarge, OrePrefixes.pipeRestrictiveLarge, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.itemPipeRestrictiveHuge, OrePrefixes.pipeRestrictiveHuge, () -> ProcessingPipe.INSTANCE);
        on(TEBlockShapes.frameGt, OrePrefixes.frameGt, () -> ProcessingFrame.INSTANCE);
        on(BlockShapes.sheetmetal, OrePrefixes.sheetmetal, () -> ProcessingSheetmetal.INSTANCE);
        on(Shapes.ingot, OrePrefixes.ingot, () -> ProcessingShaping.INSTANCE);
        on(Shapes.dust, OrePrefixes.dust, () -> ProcessingShaping.INSTANCE);
        registerTransforming();
        registerRecycling();
        on(Shapes.toolHeadDrill, OrePrefixes.toolHeadDrill, () -> ProcessingDetravToolProspector.INSTANCE);
    }

    /// Chemical-bath and polarizer recipes for every material-carrying prefix that is neither a container nor
    /// enchantable, on each of the prefix's shapes.
    private static void registerTransforming() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (prefix.getMaterialAmount() <= 0 || prefix.isContainer() || prefix.isEnchantable()) continue;
            for (Shape shape : MaterialParts.shapes(prefix)) {
                on(shape, prefix, () -> ProcessingTransforming.INSTANCE);
            }
        }
    }

    /// Emptying recipes for every container prefix holding material. Bolted casings are marked as containers but hold
    /// nothing to empty.
    private static void registerRecycling() {
        for (OrePrefixes prefix : OrePrefixes.VALUES) {
            if (!prefix.isMaterialBased() || prefix.getMaterialAmount() <= 0 || !prefix.isContainer()) continue;
            if (prefix == OrePrefixes.blockCasing || prefix == OrePrefixes.blockCasingAdvanced) continue;
            on(MaterialParts.shape(prefix), prefix, () -> ProcessingRecycling.INSTANCE);
        }
        on(CellShapes.cellPlasmaLight, OrePrefixes.cellPlasma, () -> ProcessingRecycling.INSTANCE);
        on(CellShapes.cellGas, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
        on(CellShapes.cellSolid, OrePrefixes.cell, () -> ProcessingRecycling.INSTANCE);
    }

    /// High Pressure (Redstone) fluid pipes have no generated recipes.
    private static void fluidPipe(Shape shape, OrePrefixes prefix) {
        on(
            shape,
            prefix,
            material -> !PipeMaterials.isHighPressurePipe(prefix, material),
            () -> ProcessingPipe.INSTANCE);
    }

    /// Clamps the shape's item to the prefix's default stack size. The clamp mutates the [Item] shared by a whole
    /// shape, so each material reapplies it.
    private static void clamp(Shape shape, OrePrefixes prefix) {
        if (shape == null) return;
        MaterialLibAPI.registerShapeConsumer("gregtech", shape, (s, material) -> {
            ItemStack stack = MaterialLibAPI.getStack(material, s, 1);
            if (stack == null) return;
            Item item = stack.getItem();
            if (item instanceof ItemBlock && prefix.getDefaultStackSize() < item.getItemStackLimit(stack)) {
                item.setMaxStackSize(prefix.getDefaultStackSize());
            }
        });
    }

    private static void on(Shape shape, OrePrefixes prefix, Supplier<IOreRecipeRegistrator> registrator) {
        on(shape, prefix, material -> true, registrator);
    }

    /// A null `shape` registers nothing. `filter` runs before the material's stack is resolved.
    private static void on(Shape shape, OrePrefixes prefix, Predicate<Material> filter,
        Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            if (!filter.test(material)) return;
            dispatch(registrator, prefix, material, MaterialLibAPI.getStack(material, s, 1));
        });
    }

    /// Runs the registrator once per [StoneType] the material has an ore block on, under that stone's prefix. All
    /// variants of one material share a [GTRecipeBuilder#withVariantGroup] tag named by `groupPrefix`.
    ///
    /// [StoneType] and [GTOreAdapter] must only be touched inside the callback; see [OreShapes].
    private static void onOreVariants(Shape shape, OrePrefixes groupPrefix,
        Supplier<IOreRecipeRegistrator> registrator) {
        if (shape == null) return;
        MaterialLibAPI.registerPostInitShapeConsumer("gregtech", shape, (s, material) -> {
            String group = groupPrefix.getName() + ":" + MaterialUtils.internalName(material);
            for (StoneType stoneType : StoneType.VALUES) {
                ItemStack stack = GTOreAdapter.INSTANCE.getVariantStack(material, stoneType, false);
                if (stack == null) continue;
                GTRecipeBuilder
                    .withVariantGroup(group, () -> dispatch(registrator, stoneType.getPrefix(), material, stack));
            }
        });
    }

    private static void dispatch(Supplier<IOreRecipeRegistrator> registrator, OrePrefixes prefix, Material material,
        ItemStack stack) {
        if (MaterialUtils.hasFlag(material, GTMaterialFlag.NO_RECIPES) || prefix.isIgnored(material)) return;
        String oreName = prefix.getName() + MaterialUtils.internalName(material);
        if (OreDictRegistrationHandler.isIgnoredName(oreName)) return;
        registrator.get()
            .registerOre(prefix, material, oreName, "materiallib", stack);
    }
}
