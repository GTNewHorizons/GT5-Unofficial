package gregtech.api.interfaces;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.common.blocks.ItemMachines;

/**
 * Implement this interface on your {@link IMetaTileEntity} so that energy
 * info doesn't get automatically added in the tooltip by {@link ItemMachines#addInformation}
 */
public interface IHideTooltipEnergyInfo {
}
