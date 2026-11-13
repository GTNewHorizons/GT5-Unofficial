package gregtech.crossmod.liquidlogged;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.liquidlogged.api.FluidHolderPredicate;
import com.gtnewhorizons.liquidlogged.api.LiquidLoggedAPI;

import gregtech.api.GregTechAPI;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.common.blocks.BlockFrameBox;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;

public final class GTFluidHolders {

    /** Frames hold fluid until a cover is attached. */
    private static final FluidHolderPredicate FRAME = (world, x, y, z, block, meta, fluid) -> {
        if (world.getTileEntity(x, y, z) instanceof BaseMetaPipeEntity pipe) {
            return !hasCover(pipe);
        }
        return Boolean.TRUE;
    };

    /** Lets pipes and wires hold fluid and leaves machines unchanged. */
    private static final FluidHolderPredicate PIPE = (world, x, y, z, block, meta, fluid) -> {
        // sBlockMachines also holds machines, so the check goes by tile entity type
        TileEntity tile = world.getTileEntity(x, y, z);
        return tile instanceof BaseMetaPipeEntity ? Boolean.TRUE : null;
    };

    private GTFluidHolders() {}

    /** Registers the predicates for frames, pipes and wires; call once after all blocks are registered. */
    public static void register() {
        for (Object o : Block.blockRegistry) {
            Block block = (Block) o;
            if (isFrameBox(block)) {
                LiquidLoggedAPI.registerPredicate(block, FRAME);
            }
        }
        LiquidLoggedAPI.registerPredicate(GregTechAPI.sBlockMachines, PIPE);
    }

    private static boolean isFrameBox(Block block) {
        return block instanceof BlockFrameBox
            || (block instanceof BlockBaseModular modular && modular.blockType == BlockTypes.FRAME);
    }

    private static boolean hasCover(BaseMetaPipeEntity pipe) {
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            if (pipe.hasCoverAtSide(side)) return true;
        }
        return false;
    }
}
