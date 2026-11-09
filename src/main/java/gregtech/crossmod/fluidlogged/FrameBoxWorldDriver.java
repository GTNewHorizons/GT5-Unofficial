package gregtech.crossmod.fluidlogged;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;

import gregtech.common.blocks.BlockFrameBox;
import gtPlusPlus.core.block.base.BasicBlock.BlockTypes;
import gtPlusPlus.core.block.base.BlockBaseModular;
import mega.fluidlogged.api.world.WorldDriver;

public class FrameBoxWorldDriver implements WorldDriver {

    public static void register() {
        WorldDriver.register(new FrameBoxWorldDriver());
    }

    @Override
    public boolean canBeFluidLogged(@NotNull Block block, int meta, @NotNull Fluid fluid) {
        return isFrameBox(block);
    }

    private static boolean isFrameBox(Block block) {
        return block instanceof BlockFrameBox
            || (block instanceof BlockBaseModular modular && modular.blockType == BlockTypes.FRAME);
    }
}
