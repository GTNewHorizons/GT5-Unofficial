package gregtech.loaders.preload;

import com.ruling_0.materiallib.api.Material;
import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.GTMod;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.materials2.Materials2PipeShapes;
import gregtech.api.material.MU;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;

/// Registers the canonical material-agnostic MTE instance behind each [Materials2PipeShapes] shape, one per
/// shape at the id its block declares: wires and cables ([MTECable]), fluid pipes ([MTEFluidPipe]), item
/// pipes ([MTEItemPipe]), and the frame box ([MTEFrame]). Each instance derives its material and stats from
/// its host block's metadata instead of carrying them per registration.
public final class LoaderPipeShapeEntities implements Runnable {

    @Override
    public void run() {
        cable(Materials2PipeShapes.wireGt01);
        cable(Materials2PipeShapes.wireGt02);
        cable(Materials2PipeShapes.wireGt04);
        cable(Materials2PipeShapes.wireGt08);
        cable(Materials2PipeShapes.wireGt12);
        cable(Materials2PipeShapes.wireGt16);
        cable(Materials2PipeShapes.cableGt01);
        cable(Materials2PipeShapes.cableGt02);
        cable(Materials2PipeShapes.cableGt04);
        cable(Materials2PipeShapes.cableGt08);
        cable(Materials2PipeShapes.cableGt12);
        cable(Materials2PipeShapes.cableGt16);
        fluidPipe(Materials2PipeShapes.pipeTiny);
        fluidPipe(Materials2PipeShapes.pipeSmall);
        fluidPipe(Materials2PipeShapes.pipeMedium);
        fluidPipe(Materials2PipeShapes.pipeLarge);
        fluidPipe(Materials2PipeShapes.pipeHuge);
        fluidPipe(Materials2PipeShapes.pipeQuadruple);
        fluidPipe(Materials2PipeShapes.pipeNonuple);
        itemPipe(Materials2PipeShapes.itemPipeTiny);
        itemPipe(Materials2PipeShapes.itemPipeSmall);
        itemPipe(Materials2PipeShapes.itemPipeMedium);
        itemPipe(Materials2PipeShapes.itemPipeLarge);
        itemPipe(Materials2PipeShapes.itemPipeHuge);
        itemPipe(Materials2PipeShapes.itemPipeRestrictiveTiny);
        itemPipe(Materials2PipeShapes.itemPipeRestrictiveSmall);
        itemPipe(Materials2PipeShapes.itemPipeRestrictiveMedium);
        itemPipe(Materials2PipeShapes.itemPipeRestrictiveLarge);
        itemPipe(Materials2PipeShapes.itemPipeRestrictiveHuge);
        FrameShapeBlock frame = (FrameShapeBlock) MaterialLibAPI.getBlock(Materials2PipeShapes.frameGt);
        new MTEFrame(frame.getMteId(), "shape." + frame.getName(), frame);
        verifyFrameMembership();
    }

    /// Checks the declared frame-shape membership ([Materials2PipeMaterials#frameMaterials]) against the live
    /// legacy frame predicate (every generated material with metal items, plus Wood), which is unreadable at
    /// material registration where membership had to be settled. Logs each divergence.
    private static void verifyFrameMembership() {
        for (int meta = 0; meta < GregTechAPI.sGeneratedMaterials.length; meta++) {
            Materials legacy = GregTechAPI.sGeneratedMaterials[meta];
            if (legacy == null) continue;
            boolean legacyFrame = legacy.hasMetalItems() || legacy == Materials.Wood;
            Material material = MU.material(legacy);
            boolean shapeFrame = material != null && material.hasShape(Materials2PipeShapes.frameGt);
            if (legacyFrame != shapeFrame) {
                GTMod.GT_FML_LOGGER.error(
                    "Frame shape membership for {} diverges from the legacy frame set (legacy {}, shape {})",
                    legacy.mName,
                    legacyFrame,
                    shapeFrame);
            }
        }
    }

    private static void cable(Shape shape) {
        PipeShapeBlock block = block(shape);
        new MTECable(block.getMteId(), "shape." + block.getName(), block);
    }

    private static void fluidPipe(Shape shape) {
        PipeShapeBlock block = block(shape);
        new MTEFluidPipe(block.getMteId(), "shape." + block.getName(), block);
    }

    private static void itemPipe(Shape shape) {
        PipeShapeBlock block = block(shape);
        new MTEItemPipe(block.getMteId(), "shape." + block.getName(), block);
    }

    private static PipeShapeBlock block(Shape shape) {
        return (PipeShapeBlock) MaterialLibAPI.getBlock(shape);
    }
}
