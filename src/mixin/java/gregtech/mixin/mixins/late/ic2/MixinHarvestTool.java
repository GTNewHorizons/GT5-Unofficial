package gregtech.mixin.mixins.late.ic2;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;

import org.spongepowered.asm.mixin.Mixin;

@Mixin(
    value = { ic2.core.block.BlockTileEntity.class, ic2.core.block.machine.BlockMachine.class,
        ic2.core.block.machine.BlockMachine2.class, ic2.core.block.machine.BlockMachine3.class,
        ic2.core.block.kineticgenerator.block.BlockKineticGenerator.class,
        ic2.core.block.heatgenerator.block.BlockHeatGenerator.class,
        ic2.core.block.generator.block.BlockGenerator.class, ic2.core.block.reactor.block.BlockReactorAccessHatch.class,
        ic2.core.block.reactor.block.BlockReactorChamber.class,
        ic2.core.block.reactor.block.BlockReactorFluidPort.class,
        ic2.core.block.reactor.block.BlockReactorRedstonePort.class,
        ic2.core.block.reactor.block.BlockReactorVessel.class, ic2.core.block.personal.BlockPersonal.class,
        ic2.core.block.wiring.BlockChargepad.class, ic2.core.block.wiring.BlockElectric.class,
        ic2.core.block.wiring.BlockLuminator.class })
public class MixinHarvestTool extends Block {

    protected MixinHarvestTool(Material materialIn) {
        super(materialIn);
    }

    @Override
    public String getHarvestTool(int aMeta) {
        return "wrench";
    }

}
