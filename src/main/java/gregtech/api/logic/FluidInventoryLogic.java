package gregtech.api.logic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import com.gtnewhorizons.modularui.api.fluids.FluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.IFluidTankLong;
import com.gtnewhorizons.modularui.api.fluids.IFluidTanksHandler;
import com.gtnewhorizons.modularui.api.fluids.ListFluidHandler;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.FluidSlotWidget;
import com.gtnewhorizons.modularui.common.widget.Scrollable;

/**
 * Generic Fluid logic for MuTEs.
 * 
 * @author BlueWeabo
 */
public class FluidInventoryLogic {

    private static final int DEFAULT_COLUMNS_PER_ROW = 4;
    private static final int POSITION_INTERVAL = 18;
    private static final Size SIZE = new Size(18, 18);

    protected String displayName = "";
    @Nonnull
    protected final IFluidTanksHandler inventory;
    protected final Map<Fluid, IFluidTankLong> fluidToTankMap;
    protected int tier = 0;
    protected boolean isUpgradeInventory = false;

    public FluidInventoryLogic(int numberOfSlots, long capacityOfEachTank) {
        this(new FluidTanksHandler(numberOfSlots, capacityOfEachTank), 0, false);
    }

    public FluidInventoryLogic(int numberOfSlots, long capacityOfEachTank, int tier) {
        this(new FluidTanksHandler(numberOfSlots, capacityOfEachTank), tier, false);
    }

    public FluidInventoryLogic(int numberOfSlots, long capacityOfEachTank, int tier, boolean isUpgradeInventory) {
        this(new FluidTanksHandler(numberOfSlots, capacityOfEachTank), tier, isUpgradeInventory);
    }

    public FluidInventoryLogic(@Nonnull IFluidTanksHandler inventory, int tier, boolean isUpgradeInventory) {
        this.inventory = inventory;
        fluidToTankMap = new HashMap<>(inventory.getTanks());
        this.tier = tier;
        this.isUpgradeInventory = isUpgradeInventory;
    }

    public FluidInventoryLogic(Collection<IFluidTanksHandler> inventories) {
        this(new ListFluidHandler(inventories), -1, false);
    }

    @Nullable
    public String getDisplayName() {
        return displayName;
    }

    public int getTier() {
        return tier;
    }

    public boolean isUpgradeInventory() {
        return isUpgradeInventory;
    }

    public void setDisplayName(@Nullable String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return The Fluid Inventory Logic as an NBTTagList to be saved in another nbt as how one wants.
     */
    @Nonnull
    public NBTTagCompound saveToNBT() {
        final NBTTagCompound nbt = new NBTTagCompound();
        final NBTTagList tList = new NBTTagList();
        for (int tankNumber = 0; tankNumber < inventory.getTanks(); tankNumber++) {
            final IFluidTankLong tank = inventory.getFluidTank(tankNumber);
            if (tank == null) continue;

            final NBTTagCompound tag = new NBTTagCompound();
            tag.setByte("s", (byte) tankNumber);
            tank.saveToNBT(tag);
            tList.appendTag(tag);
        }
        nbt.setTag("inventory", tList);
        nbt.setInteger("tier", tier);
        if (displayName != null) {
            nbt.setString("displayName", displayName);
        }
        nbt.setBoolean("isUpgradeInventory", isUpgradeInventory);
        return nbt;
    }

    /**
     * Loads the Item Inventory Logic from an NBTTagList.
     */
    public void loadFromNBT(@Nonnull NBTTagCompound nbt) {
        NBTTagList nbtList = nbt.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < nbtList.tagCount(); i++) {
            final NBTTagCompound tankNBT = nbtList.getCompoundTagAt(i);
            final int tank = tankNBT.getShort("s");
            if (tank >= 0 && tank < inventory.getTanks()) inventory.getFluidTank(tank)
                .loadFromNBT(tankNBT);
            if (inventory.getFluidInTank(tank) != null) {
                fluidToTankMap.put(inventory.getFluidInTank(tank), inventory.getFluidTank(tank));
            }
        }
        tier = nbt.getInteger("tier");
        if (nbt.hasKey("displayName")) {
            displayName = nbt.getString("displayName");
        }
        isUpgradeInventory = nbt.getBoolean("isUpgradeInventory");
    }

    @Nonnull
    public IFluidTanksHandler getInventory() {
        return inventory;
    }

    @Nonnull
    public FluidStack[] getStoredFluids() {
        final FluidStack[] fluids = inventory.getFluids()
            .stream()
            .filter(fluid -> fluid != null)
            .collect(Collectors.toList())
            .toArray(new FluidStack[0]);
        if (fluids == null) {
            return new FluidStack[0];
        }
        return fluids;
    }

    public boolean isFluidValid(@Nullable Fluid fluid) {
        return fluid != null;
    }

    /**
     * @param fluid  What we are trying to input
     * @param amount amount of fluid we are trying to put
     * @return amount of fluid filled into the tank
     */
    public long fill(@Nullable Fluid fluid, long amount, boolean simulate) {
        if (!isFluidValid(fluid)) return 0;
        IFluidTankLong tank = fluidToTankMap.get(fluid);
        if (tank != null) {
            return tank.fill(fluid, amount, !simulate);
        }
        int tankNumber = 0;
        tank = inventory.getFluidTank(tankNumber++);
        while (tank.getStoredFluid() != fluid && tank.getStoredFluid() != null) {
            tank = inventory.getFluidTank(tankNumber++);
        }
        fluidToTankMap.put(fluid, tank);
        return tank.fill(fluid, amount, !simulate);
    }

    @Nullable
    public FluidStack fill(@Nullable FluidStack fluid) {
        if (fluid == null) return null;
        for (int i = 0; i < inventory.getTanks(); i++) {
            fill(fluid.getFluid(), fluid.amount, false);
        }
        return fluid;
    }

    /**
     * Try and drain the first fluid found for that amount. Used by GT_Cover_Pump
     * 
     * @param amount Fluid to drain from the tank
     * @return A fluidstack with the possible amount drained
     */
    @Nullable
    public FluidStack drain(long amount, boolean simulate) {
        for (int i = 0; i < inventory.getTanks(); i++) {
            Fluid fluid = inventory.getFluidInTank(i);
            FluidStack drained = drain(fluid, amount, simulate);
            if (drained != null) return drained;
        }

        return null;
    }

    @Nullable
    public FluidStack drain(Fluid fluid, long amount, boolean simulate) {
        if (!isFluidValid(fluid)) return null;
        IFluidTankLong tank = fluidToTankMap.get(fluid);
        if (tank != null) {
            return tank.drain(amount, !simulate);
        }
        int tankNumber = 0;
        tank = inventory.getFluidTank(tankNumber++);
        while (tank.getStoredFluid() != fluid) {
            tank = inventory.getFluidTank(tankNumber++);
        }
        fluidToTankMap.put(fluid, tank);
        return tank.drain(amount, !simulate);
    }

    public void update() {
        for (int i = 0; i < inventory.getTanks(); i++) {
            IFluidTankLong tank = inventory.getFluidTank(i);
            if (tank.getFluidAmountLong() > 0) continue;
            tank.setFluid(null, 0);
        }
    }

    public long calculateAmountOfTimesFluidCanBeTaken(Fluid fluid, long amountToTake) {
        if (!isFluidValid(fluid)) return 0;
        IFluidTankLong tank = fluidToTankMap.get(fluid);
        if (tank == null) return 0;
        return tank.getFluidAmountLong() / amountToTake;
    }

    @Nonnull
    public Map<Fluid, Long> getMapOfStoredFluids() {
        Map<Fluid, Long> map = new HashMap<>();
        for (int i = 0; i < inventory.getTanks(); i++) {
            IFluidTankLong tank = inventory.getFluidTank(i);
            if (tank == null) continue;
            Fluid fluid = tank.getStoredFluid();
            if (fluid == null) continue;
            map.put(fluid, map.getOrDefault(fluid, 0L) + tank.getFluidAmountLong());
        }
        return map;
    }

    /**
     * Return a scrollable widget with only the inventory.
     */
    @Nonnull
    public Widget getGuiPart() {
        return getGUIPart(DEFAULT_COLUMNS_PER_ROW);
    }

    /**
     * Return a scrollable widget with only the inventory.
     */
    @Nonnull
    public Widget getGUIPart(int columnsPerRow) {
        final Scrollable scrollable = new Scrollable();
        scrollable.setVerticalScroll();
        for (int rows = 0; rows * 4 < inventory.getTanks(); rows++) {
            final int columnsToMake = Math.min(inventory.getTanks() - rows * 4, 4);
            for (int column = 0; column < columnsToMake; column++) {
                final FluidSlotWidget fluidSlot = new FluidSlotWidget(inventory, rows * 4 + column);
                scrollable.widget(
                    fluidSlot.setPos(column * POSITION_INTERVAL, rows * POSITION_INTERVAL)
                        .setSize(SIZE));
            }
        }
        return scrollable;
    }
}
