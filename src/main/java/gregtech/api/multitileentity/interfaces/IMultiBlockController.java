package gregtech.api.multitileentity.interfaces;

import java.util.UUID;

import net.minecraft.util.ChunkCoordinates;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import gregtech.api.enums.InventoryType;
import gregtech.api.logic.FluidInventoryLogic;
import gregtech.api.logic.ItemInventoryLogic;
import gregtech.api.logic.PowerLogic;
import gregtech.api.logic.interfaces.FluidInventoryLogicHost;
import gregtech.api.logic.interfaces.ItemInventoryLogicHost;
import gregtech.api.multitileentity.enums.MultiTileCasingPurpose;

public interface IMultiBlockController
    extends IMultiTileEntity, FluidInventoryLogicHost, ItemInventoryLogicHost, UpgradableMuTE {

    boolean checkStructure(boolean aForceReset);

    /** Set the structure as having changed, and trigger an update */
    void onStructureChange();

    @Override
    ChunkCoordinates getCoords();

    void registerCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void unregisterCoveredPartOnSide(final ForgeDirection side, IMultiBlockPart part);

    void registerCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    void unregisterCaseWithPurpose(MultiTileCasingPurpose purpose, IMultiBlockPart part);

    PowerLogic getPowerLogic();

    ModularWindow createWindowGUI(UIBuildContext buildContext);

    UUID registerItemInventory(int slots, int tier, InventoryType type, boolean isUpgradeInventory);

    ItemInventoryLogic unregisterItemInventory(UUID id, InventoryType type);

    void changeItemInventoryDisplayName(UUID id, String displayName, InventoryType type);

    UUID registerFluidInventory(int tanks, long capacity, int tier, InventoryType type, boolean isUpgradeInventory);

    FluidInventoryLogic unregisterFluidInventory(UUID id, InventoryType type);

    void changeFluidInventoryDisplayName(UUID id, String displayName, InventoryType type);
}
