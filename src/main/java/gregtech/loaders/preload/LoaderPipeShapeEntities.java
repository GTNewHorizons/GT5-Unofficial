package gregtech.loaders.preload;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.materials.TEBlockShapes;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;

/// Registers the canonical material-agnostic MTE instance behind each [TEBlockShapes] shape, one per
/// shape at the id its block declares: wires and cables ([MTECable]), fluid pipes ([MTEFluidPipe]), item
/// pipes ([MTEItemPipe]), and the frame box ([MTEFrame]). Each instance derives its material and stats from
/// its host block's metadata instead of carrying them per registration. The material membership of each
/// shape is declared at material registration; the frame set's authoritative source is
/// [PipeMaterials#frameMaterials].
public final class LoaderPipeShapeEntities implements Runnable {

    @Override
    public void run() {
        cable(TEBlockShapes.wireGt01);
        cable(TEBlockShapes.wireGt02);
        cable(TEBlockShapes.wireGt04);
        cable(TEBlockShapes.wireGt08);
        cable(TEBlockShapes.wireGt12);
        cable(TEBlockShapes.wireGt16);
        cable(TEBlockShapes.cableGt01);
        cable(TEBlockShapes.cableGt02);
        cable(TEBlockShapes.cableGt04);
        cable(TEBlockShapes.cableGt08);
        cable(TEBlockShapes.cableGt12);
        cable(TEBlockShapes.cableGt16);
        fluidPipe(TEBlockShapes.pipeTiny);
        fluidPipe(TEBlockShapes.pipeSmall);
        fluidPipe(TEBlockShapes.pipeMedium);
        fluidPipe(TEBlockShapes.pipeLarge);
        fluidPipe(TEBlockShapes.pipeHuge);
        fluidPipe(TEBlockShapes.pipeQuadruple);
        fluidPipe(TEBlockShapes.pipeNonuple);
        itemPipe(TEBlockShapes.itemPipeTiny);
        itemPipe(TEBlockShapes.itemPipeSmall);
        itemPipe(TEBlockShapes.itemPipeMedium);
        itemPipe(TEBlockShapes.itemPipeLarge);
        itemPipe(TEBlockShapes.itemPipeHuge);
        itemPipe(TEBlockShapes.itemPipeRestrictiveTiny);
        itemPipe(TEBlockShapes.itemPipeRestrictiveSmall);
        itemPipe(TEBlockShapes.itemPipeRestrictiveMedium);
        itemPipe(TEBlockShapes.itemPipeRestrictiveLarge);
        itemPipe(TEBlockShapes.itemPipeRestrictiveHuge);
        FrameShapeBlock frame = (FrameShapeBlock) MaterialLibAPI.getBlock(TEBlockShapes.frameGt);
        new MTEFrame(frame.getMteId(), "shape." + frame.getName(), frame);
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
