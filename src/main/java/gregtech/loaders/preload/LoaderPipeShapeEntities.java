package gregtech.loaders.preload;

import com.ruling_0.materiallib.api.MaterialLibAPI;
import com.ruling_0.materiallib.api.Shape;

import gregtech.api.enums.materials.PipeShapes;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEFluidPipe;
import gregtech.api.metatileentity.implementations.MTEFrame;
import gregtech.api.metatileentity.implementations.MTEItemPipe;
import gregtech.common.blocks.FrameShapeBlock;
import gregtech.common.blocks.PipeShapeBlock;

/// Registers the canonical material-agnostic MTE instance behind each [PipeShapes] shape, one per
/// shape at the id its block declares: wires and cables ([MTECable]), fluid pipes ([MTEFluidPipe]), item
/// pipes ([MTEItemPipe]), and the frame box ([MTEFrame]). Each instance derives its material and stats from
/// its host block's metadata instead of carrying them per registration. The material membership of each
/// shape is declared at material registration; the frame set's authoritative source is
/// [PipeMaterials#frameMaterials].
public final class LoaderPipeShapeEntities implements Runnable {

    @Override
    public void run() {
        cable(PipeShapes.wireGt01);
        cable(PipeShapes.wireGt02);
        cable(PipeShapes.wireGt04);
        cable(PipeShapes.wireGt08);
        cable(PipeShapes.wireGt12);
        cable(PipeShapes.wireGt16);
        cable(PipeShapes.cableGt01);
        cable(PipeShapes.cableGt02);
        cable(PipeShapes.cableGt04);
        cable(PipeShapes.cableGt08);
        cable(PipeShapes.cableGt12);
        cable(PipeShapes.cableGt16);
        fluidPipe(PipeShapes.pipeTiny);
        fluidPipe(PipeShapes.pipeSmall);
        fluidPipe(PipeShapes.pipeMedium);
        fluidPipe(PipeShapes.pipeLarge);
        fluidPipe(PipeShapes.pipeHuge);
        fluidPipe(PipeShapes.pipeQuadruple);
        fluidPipe(PipeShapes.pipeNonuple);
        itemPipe(PipeShapes.itemPipeTiny);
        itemPipe(PipeShapes.itemPipeSmall);
        itemPipe(PipeShapes.itemPipeMedium);
        itemPipe(PipeShapes.itemPipeLarge);
        itemPipe(PipeShapes.itemPipeHuge);
        itemPipe(PipeShapes.itemPipeRestrictiveTiny);
        itemPipe(PipeShapes.itemPipeRestrictiveSmall);
        itemPipe(PipeShapes.itemPipeRestrictiveMedium);
        itemPipe(PipeShapes.itemPipeRestrictiveLarge);
        itemPipe(PipeShapes.itemPipeRestrictiveHuge);
        FrameShapeBlock frame = (FrameShapeBlock) MaterialLibAPI.getBlock(PipeShapes.frameGt);
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
