package gregtech.api.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.fluids.FluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.IFluidTankLong;
import com.gtnewhorizons.modularui.api.fluids.IFluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.ListFluidHandler;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;

public class FluidInventoryLogic {

    protected String displayName;
    protected final IFluidTanksHandler inventory;
    protected final Map<Fluid, IFluidTankLong> fluidToTankMap;

    public FluidInventoryLogic(int numberOfSlots, long capacityOfEachTank) {
        this(new FluidTanksHandler(numberOfSlots, capacityOfEachTank));
    }

    public FluidInventoryLogic(IFluidTanksHandler inventory) {
        this.inventory = inventory;
        fluidToTankMap = new HashMap<>(inventory.getTanks());
    }

    public FluidInventoryLogic(Collection<IFluidTanksHandler> inventories) {
        this(new ListFluidHandler(inventories));
    }

    public String getDisplayName() {
        return displayName;
    }

    public FluidInventoryLogic setDisplayName(String displayName) {
        this.displayName = displayName;
        return this;
    }

    /**
     * 
     * @return The Fluid Inventory Logic as an NBTTagList to be saved in another nbt as how one wants.
     */
    public NBTTagList saveToNBT() {
        final NBTTagList tList = new NBTTagList();
        for (int tankNumber = 0; tankNumber < inventory.getTanks(); tankNumber++) {
            final IFluidTankLong tank = inventory.getFluidTank(tankNumber);
            if (tank == null) continue;

            final NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("s", (byte) tankNumber);
            tank.saveToNBT(tag);
            tList.appendTag(tag);
        }
        return tList;
    }

    /**
     * Loads the Item Inventory Logic from an NBTTagList.
     */
    public void loadFromNBT(NBTTagList nbtList) {
        for (int i = 0; i < nbtList.tagCount(); i++) {
            final NBTTagCompound nbt = nbtList.getCompoundTagAt(i);
            final int tank = nbt.getShort("s");
            if (tank >= 0 && tank < inventory.getTanks()) inventory.getFluidTank(tank)
                .loadFromNBT(nbt);
        }
    }

    public IFluidTanksHandler getInventory() {
        return inventory;
    }

    public FluidStack[] getStoredFluids() {
        return inventory.getFluids()
            .stream()
            .filter(fluid -> fluid != null)
            .collect(Collectors.toList())
            .toArray(new FluidStack[0]);
    }

    public boolean isFluidValid(Fluid fluid) {
        return fluid != null;
    }

    /**
     * @param fluid  What we are trying to input
     * @param amount amount of fluid we are trying to put
     * @return amount of fluid filled into the tank
     */
    public long fill(Fluid fluid, long amount) {
        if (!isFluidValid(fluid)) return 0;
        IFluidTankLong tank = fluidToTankMap.get(fluid);
        if (tank != null) {
            return tank.fill(fluid, amount, true);
        }
        int tankNumber = 0;
        tank = inventory.getFluidTank(tankNumber++);
        while (tank.getStoredFluid() != fluid) {
            tank = inventory.getFluidTank(tankNumber++);
        }
        fluidToTankMap.put(fluid, tank);
        return tank.fill(fluid, amount, false);
    }

    public FluidStack drain(Fluid fluid, int amount) {
        if (!isFluidValid(fluid)) return null;
        IFluidTankLong tank = fluidToTankMap.get(fluid);
        if (tank != null) {
            return tank.drain(amount, true);
        }
        int tankNumber = 0;
        tank = inventory.getFluidTank(tankNumber++);
        while (tank.getStoredFluid() != fluid) {
            tank = inventory.getFluidTank(tankNumber++);
        }
        fluidToTankMap.put(fluid, tank);
        return tank.drain(amount, true);
    }

    public Widget getGuiPart() {
        return getGUIPart(4);
    }

    public Widget getGUIPart(int columnsPerRow) {
        final Scrollable scrollable = new Scrollable().setVerticalScroll();
        for (int rows = 0; rows * 4 < inventory.getTanks(); rows++) {
            final int columnsToMake = Math.min(inventory.getTanks() - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                final FluidSlotWidget fluidSlot = new FluidSlotWidget(inventory, rows * 4 + column);
                scrollable.widget(
                    fluidSlot.setPos(column * 18, rows * 18)
                        .setSize(18, 18));
            }
        }
        return scrollable;
    }
}
