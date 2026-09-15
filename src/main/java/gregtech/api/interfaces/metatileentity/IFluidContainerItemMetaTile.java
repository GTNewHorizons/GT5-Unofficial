package gregtech.api.interfaces.metatileentity;

/**
 * MetaTileEntities that persist fluid in the block item's NBT when broken, and that are intended to work with
 * {@link net.minecraftforge.fluids.IFluidContainerItem} behavior on {@link gregtech.common.blocks.ItemMachines}.
 */
public interface IFluidContainerItemMetaTile {

    default String getFluidNbtKey() {
        return "mFluid";
    }
}
