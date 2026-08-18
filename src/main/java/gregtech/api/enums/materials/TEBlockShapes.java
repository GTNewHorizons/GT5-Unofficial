package gregtech.api.enums.materials;

import static gregtech.api.enums.materials.GTShapeStore.reg;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;
import com.ruling_0.materiallib.api.ShapeBlock;

import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;
import gregtech.common.blocks.PipeShapeBlock.PipeFamily;

/// Block [Shape] declarations for the pipe families: wires, cables, fluid pipes (single- and multi-channel),
/// item pipes (normal and restrictive), and the frame box. Each shape is backed by one [PipeShapeBlock] (or
/// [FrameShapeBlock]) carrying the id of the material-agnostic MTE registered for it by
/// [gregtech.loaders.preload.LoaderPipeShapeEntities].
///
/// The fluid and item pipe prefixes share their oredict name strings (`pipeTiny`..`pipeHuge` serve both
/// families for disjoint material sets), so the item-pipe shape NAMES differ (`itemPipeTiny`..) while their
/// oredict prefixes keep the legacy strings. These shapes feed [gregtech.api.material.MaterialParts]'s prefix-to-shape
/// map; the `itemPipe*` shapes, whose names match no `OrePrefixes`, are folded there under the
/// `pipeTiny`..`pipeHuge` / `pipeRestrictive*` prefix keys, the fluid shape staying the first candidate.
public class TEBlockShapes {

    // spotless:off
    public static Shape wireGt01;
    public static Shape wireGt02;
    public static Shape wireGt04;
    public static Shape wireGt08;
    public static Shape wireGt12;
    public static Shape wireGt16;
    public static Shape cableGt01;
    public static Shape cableGt02;
    public static Shape cableGt04;
    public static Shape cableGt08;
    public static Shape cableGt12;
    public static Shape cableGt16;
    public static Shape pipeTiny;
    public static Shape pipeSmall;
    public static Shape pipeMedium;
    public static Shape pipeLarge;
    public static Shape pipeHuge;
    public static Shape pipeQuadruple;
    public static Shape pipeNonuple;
    public static Shape itemPipeTiny;
    public static Shape itemPipeSmall;
    public static Shape itemPipeMedium;
    public static Shape itemPipeLarge;
    public static Shape itemPipeHuge;
    public static Shape itemPipeRestrictiveTiny;
    public static Shape itemPipeRestrictiveSmall;
    public static Shape itemPipeRestrictiveMedium;
    public static Shape itemPipeRestrictiveLarge;
    public static Shape itemPipeRestrictiveHuge;
    public static Shape frameGt;
    // spotless:on

    public static void init() {
        // spotless:off
        wireGt01 = register(new PipeShapeBlock("wireGt01", "1x %s Wire", "gt.oreprefix.1x_material_wire", 5800, PipeFamily.WIRE, 0, 1, 0.125F, "wire", "wireGt01"));
        wireGt02 = register(new PipeShapeBlock("wireGt02", "2x %s Wire", "gt.oreprefix.2x_material_wire", 5801, PipeFamily.WIRE, 1, 1, 0.25F, "wire", "wireGt02"));
        wireGt04 = register(new PipeShapeBlock("wireGt04", "4x %s Wire", "gt.oreprefix.4x_material_wire", 5802, PipeFamily.WIRE, 2, 1, 0.375F, "wire", "wireGt04"));
        wireGt08 = register(new PipeShapeBlock("wireGt08", "8x %s Wire", "gt.oreprefix.8x_material_wire", 5803, PipeFamily.WIRE, 3, 1, 0.5F, "wire", "wireGt08"));
        wireGt12 = register(new PipeShapeBlock("wireGt12", "12x %s Wire", "gt.oreprefix.12x_material_wire", 5804, PipeFamily.WIRE, 4, 1, 0.625F, "wire", "wireGt12"));
        wireGt16 = register(new PipeShapeBlock("wireGt16", "16x %s Wire", "gt.oreprefix.16x_material_wire", 5805, PipeFamily.WIRE, 5, 1, 0.75F, "wire", "wireGt16"));
        cableGt01 = register(new PipeShapeBlock("cableGt01", "1x %s Cable", "gt.oreprefix.1x_material_cable", 5806, PipeFamily.CABLE, 0, 1, 0.25F, "wire", "cableGt01"));
        cableGt02 = register(new PipeShapeBlock("cableGt02", "2x %s Cable", "gt.oreprefix.2x_material_cable", 5807, PipeFamily.CABLE, 1, 1, 0.375F, "wire", "cableGt02"));
        cableGt04 = register(new PipeShapeBlock("cableGt04", "4x %s Cable", "gt.oreprefix.4x_material_cable", 5808, PipeFamily.CABLE, 2, 1, 0.5F, "wire", "cableGt04"));
        cableGt08 = register(new PipeShapeBlock("cableGt08", "8x %s Cable", "gt.oreprefix.8x_material_cable", 5809, PipeFamily.CABLE, 3, 1, 0.625F, "wire", "cableGt08"));
        cableGt12 = register(new PipeShapeBlock("cableGt12", "12x %s Cable", "gt.oreprefix.12x_material_cable", 5810, PipeFamily.CABLE, 4, 1, 0.75F, "wire", "cableGt12"));
        cableGt16 = register(new PipeShapeBlock("cableGt16", "16x %s Cable", "gt.oreprefix.16x_material_cable", 5811, PipeFamily.CABLE, 5, 1, 0.875F, "wire", "cableGt16"));
        pipeTiny = register(new PipeShapeBlock("pipeTiny", "Tiny %s Fluid Pipe", "gt.oreprefix.tiny_material_fluid_pipe", 5812, PipeFamily.FLUID, 0, 1, 0.25F, "pipeTiny", "pipeTiny"));
        pipeSmall = register(new PipeShapeBlock("pipeSmall", "Small %s Fluid Pipe", "gt.oreprefix.small_material_fluid_pipe", 5813, PipeFamily.FLUID, 1, 1, 0.375F, "pipeSmall", "pipeSmall"));
        pipeMedium = register(new PipeShapeBlock("pipeMedium", "%s Fluid Pipe", "gt.oreprefix.material_fluid_pipe", 5814, PipeFamily.FLUID, 2, 1, 0.5F, "pipeMedium", "pipeMedium"));
        pipeLarge = register(new PipeShapeBlock("pipeLarge", "Large %s Fluid Pipe", "gt.oreprefix.large_material_fluid_pipe", 5815, PipeFamily.FLUID, 3, 1, 0.75F, "pipeLarge", "pipeLarge"));
        pipeHuge = register(new PipeShapeBlock("pipeHuge", "Huge %s Fluid Pipe", "gt.oreprefix.huge_material_fluid_pipe", 5816, PipeFamily.FLUID, 4, 1, 0.875F, "pipeHuge", "pipeHuge"));
        pipeQuadruple = register(new PipeShapeBlock("pipeQuadruple", "Quadruple %s Fluid Pipe", "gt.oreprefix.quadruple_material_fluid_pipe", 5817, PipeFamily.FLUID_MULTI, 2, 4, 1.0F, "pipeQuadruple", "pipeQuadruple"));
        pipeNonuple = register(new PipeShapeBlock("pipeNonuple", "Nonuple %s Fluid Pipe", "gt.oreprefix.nonuple_material_fluid_pipe", 5818, PipeFamily.FLUID_MULTI, 2, 9, 1.0F, "pipeNonuple", "pipeNonuple"));
        itemPipeTiny = register(new PipeShapeBlock("itemPipeTiny", "Tiny %s Item Pipe", "gt.oreprefix.tiny_material_item_pipe", 5819, PipeFamily.ITEM, 0, 1, 0.25F, "pipeTiny", "pipeTiny"));
        itemPipeSmall = register(new PipeShapeBlock("itemPipeSmall", "Small %s Item Pipe", "gt.oreprefix.small_material_item_pipe", 5820, PipeFamily.ITEM, 1, 1, 0.375F, "pipeSmall", "pipeSmall"));
        itemPipeMedium = register(new PipeShapeBlock("itemPipeMedium", "%s Item Pipe", "gt.oreprefix.material_item_pipe", 5821, PipeFamily.ITEM, 2, 1, 0.5F, "pipeMedium", "pipeMedium"));
        itemPipeLarge = register(new PipeShapeBlock("itemPipeLarge", "Large %s Item Pipe", "gt.oreprefix.large_material_item_pipe", 5822, PipeFamily.ITEM, 3, 1, 0.75F, "pipeLarge", "pipeLarge"));
        itemPipeHuge = register(new PipeShapeBlock("itemPipeHuge", "Huge %s Item Pipe", "gt.oreprefix.huge_material_item_pipe", 5823, PipeFamily.ITEM, 4, 1, 1.0F, "pipeHuge", "pipeHuge"));
        itemPipeRestrictiveTiny = register(new PipeShapeBlock("itemPipeRestrictiveTiny", "Tiny Restrictive %s Item Pipe", "gt.oreprefix.tiny_restrictive_material_item_pipe", 5824, PipeFamily.ITEM_RESTRICTIVE, 0, 1, 0.25F, "pipeTiny", "pipeRestrictiveTiny"));
        itemPipeRestrictiveSmall = register(new PipeShapeBlock("itemPipeRestrictiveSmall", "Small Restrictive %s Item Pipe", "gt.oreprefix.small_restrictive_material_item_pipe", 5825, PipeFamily.ITEM_RESTRICTIVE, 1, 1, 0.375F, "pipeSmall", "pipeRestrictiveSmall"));
        itemPipeRestrictiveMedium = register(new PipeShapeBlock("itemPipeRestrictiveMedium", "Restrictive %s Item Pipe", "gt.oreprefix.restrictive_material_item_pipe", 5826, PipeFamily.ITEM_RESTRICTIVE, 2, 1, 0.5F, "pipeMedium", "pipeRestrictiveMedium"));
        itemPipeRestrictiveLarge = register(new PipeShapeBlock("itemPipeRestrictiveLarge", "Large Restrictive %s Item Pipe", "gt.oreprefix.large_restrictive_material_item_pipe", 5827, PipeFamily.ITEM_RESTRICTIVE, 3, 1, 0.75F, "pipeLarge", "pipeRestrictiveLarge"));
        itemPipeRestrictiveHuge = register(new PipeShapeBlock("itemPipeRestrictiveHuge", "Huge Restrictive %s Item Pipe", "gt.oreprefix.huge_restrictive_material_item_pipe", 5828, PipeFamily.ITEM_RESTRICTIVE, 4, 1, 0.875F, "pipeHuge", "pipeRestrictiveHuge"));
        frameGt = register(new FrameShapeBlock("frameGt", "%s Frame Box", 5829, "frameGt"));
        // spotless:on
    }

    private static Shape register(ShapeBlock block) {
        return reg(MaterialLibAPI.registerBlockShape(block));
    }

    private TEBlockShapes() {}
}
