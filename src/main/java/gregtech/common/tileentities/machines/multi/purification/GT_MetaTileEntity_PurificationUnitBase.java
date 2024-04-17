package gregtech.common.tileentities.machines.multi.purification;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.util.GT_Recipe;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * Base class for purification units. This class handles all shared behaviour between units.
 * When inheriting from this, make sure to call super.loadNBTData() and super.saveNBTData()
 * if you override these methods, or linking will break.
 */
public abstract class GT_MetaTileEntity_PurificationUnitBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    /**
     * Small internal enum to report back the various error cases when linking purification units to the
     * purification plant.
     */
    private enum LinkResult {
        /**
         * Link target was out of range of the main controller
         */
        TOO_FAR,
        /**
         * No valid GT_MetaTileEntity_PurificationPlant was found at the link target position.
         */
        NO_VALID_PLANT,
        /**
         * Link successful
         */
        SUCCESS,
    }

    /**
     * Coordinates of the main purification plant controller. These can be used to find the controller again
     * on world load.
     */
    private int controllerX, controllerY, controllerZ;
    /**
     * Whether a controller was previously set.
     */
    private boolean controllerSet = false;

    /**
     * Pointer to the main purification plant controller.
     */
    private GT_MetaTileEntity_PurificationPlant controller = null;

    protected GT_Recipe currentRecipe = null;

    protected GT_MetaTileEntity_PurificationUnitBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_PurificationUnitBase(String aName) {
        super(aName);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // The individual purification unit structures cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        // Try to re-link to controller periodically, for example on game load.
        if (aTimer % 100 == 5 && controllerSet && getController() == null) {
            trySetControllerFromCoord(controllerX, controllerY, controllerZ);
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        // Main controller updates progress time. We can do I/O logic here.
        if (mMaxProgresstime > 0) {
            this.markDirty();
            mEfficiency = Math.max(
                0,
                Math.min(
                    mEfficiency + mEfficiencyIncrease,
                    getMaxEfficiency(mInventory[1]) - ((getIdealStatus() - getRepairStatus()) * 1000)));
        }
    }

    public void startCycle(int cycleTime, int progressTime) {
        for (FluidStack input : this.currentRecipe.mFluidInputs) {
            this.depleteInput(input);
        }
        this.mMaxProgresstime = cycleTime;
        this.mProgresstime = progressTime;
        this.mEfficiency = 10000;
        // Set this value, so it can be displayed in Waila. Note that the logic for the units is
        // specifically overridden so setting this value does not actually drain power.
        // Instead, power is drained by the main purification plant controller.
        this.lEUt = -this.getActivePowerUsage();
    }

    public void endCycle() {
        this.addFluidOutputs(this.currentRecipe.mFluidOutputs);
        this.mMaxProgresstime = 0;
        this.mProgresstime = 0;
        this.lEUt = 0;
        this.mEfficiency = 0;
        this.currentRecipe = null;
    }

    /**
     * Get the EU/t usage of this unit while it is running.
     */
    public abstract long getActivePowerUsage();

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        // The individual purification unit structures cannot have maintenance issues, so fix them all.
        this.mCrowbar = true;
        this.mWrench = true;
        this.mHardHammer = true;
        this.mSoftHammer = true;
        this.mSolderingTool = true;
        this.mScrewdriver = true;
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // If a linked controller was found, load its coordinates.
        // The unit will try to link to the real controller block periodically in onPostTick()
        // We cannot do this linking here yet because the controller block might not be loaded yet.
        if (aNBT.hasKey("controller")) {
            NBTTagCompound controllerNBT = aNBT.getCompoundTag("controller");
            controllerX = controllerNBT.getInteger("x");
            controllerY = controllerNBT.getInteger("y");
            controllerZ = controllerNBT.getInteger("z");
            controllerSet = true;
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (controllerSet) {
            NBTTagCompound controllerNBT = new NBTTagCompound();
            controllerNBT.setInteger("x", controllerX);
            controllerNBT.setInteger("y", controllerY);
            controllerNBT.setInteger("z", controllerZ);
            aNBT.setTag("controller", controllerNBT);
        }
    }

    private LinkResult trySetControllerFromCoord(int x, int y, int z) {
        IGregTechTileEntity ourBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // First check whether the controller we try to link to is within range. The range is defined
        // as a max distance in each axis.
        if (Math.abs(ourBaseMetaTileEntity.getXCoord() - x) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getYCoord() - y) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getZCoord() - z) > GT_MetaTileEntity_PurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;

        // Find the block at the requested coordinated and check if it is a purification plant controller.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return LinkResult.NO_VALID_PLANT;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return LinkResult.NO_VALID_PLANT;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof GT_MetaTileEntity_PurificationPlant)) return LinkResult.NO_VALID_PLANT;

        // Before linking, unlink from current controller so we don't end up with units linked to multiple
        // controllers.
        this.unlinkController();

        // Now link to new controller
        controllerX = x;
        controllerY = y;
        controllerZ = z;
        controllerSet = true;
        controller = (GT_MetaTileEntity_PurificationPlant) metaTileEntity;
        controller.registerLinkedUnit(this);
        return LinkResult.SUCCESS;
    }

    private boolean tryLinkDataStick(EntityPlayer aPlayer) {
        // Make sure the held item is a data stick
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return false;
        }

        // Make sure this data stick is a proper purification plant link data stick.
        if (!dataStick.hasTagCompound() || !dataStick.stackTagCompound.getString("type")
            .equals("PurificationPlant")) {
            return false;
        }

        // Now read link coordinates from the data stick.
        NBTTagCompound nbt = dataStick.stackTagCompound;
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");

        // Try to link, and report the result back to the player.
        LinkResult result = trySetControllerFromCoord(x, y, z);
        if (result == LinkResult.SUCCESS) {
            aPlayer.addChatMessage(new ChatComponentText("Link successful"));
        } else if (result == LinkResult.TOO_FAR) {
            aPlayer.addChatMessage(new ChatComponentText("Link failed: Out of range."));
        } else if (result == LinkResult.NO_VALID_PLANT) {
            aPlayer.addChatMessage(new ChatComponentText("Link failed: No Purification Plant fount at link location"));
        }

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) {
            return false;
        }

        // Right-clicking could be a data stick linking action, so try this first.
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }

        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    public GT_MetaTileEntity_PurificationPlant getController() {
        if (controller == null) return null;
        // Controller disappeared
        if (controller.getBaseMetaTileEntity() == null) return null;
        return controller;
    }

    // If the controller is broken this can be called to explicitly unlink the controller, so we don't have any
    // references lingering around
    public void unlinkController() {
        this.controllerSet = false;
        this.controller = null;
        this.controllerX = 0;
        this.controllerY = 0;
        this.controllerZ = 0;
    }

    @Override
    public void onBlockDestroyed() {
        // When this block is destroyed, explicitly unlink it from the controller if there is any.
        GT_MetaTileEntity_PurificationPlant controller = getController();
        if (controller != null) {
            controller.unregisterLinkedUnit(this);
        }
        super.onBlockDestroyed();
    }

    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        // If this purification unit is linked to a controller, add this info to the scanner output.
        if (getController() != null) {
            ret.add(
                "This Purification Unit is linked to the Water Purification Plant at " + controllerX
                    + ", "
                    + controllerY
                    + ", "
                    + controllerZ
                    + ".");
        } else ret.add("This Purification Unit is not linked to any Water Purification Plant.");
        return ret.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.getBoolean("linked")) {
            currenttip.add(
                EnumChatFormatting.AQUA + "Linked to Purification Plant at "
                    + EnumChatFormatting.WHITE
                    + tag.getInteger("controllerX")
                    + ", "
                    + tag.getInteger("controllerY")
                    + ", "
                    + tag.getInteger("controllerZ")
                    + EnumChatFormatting.RESET);
        } else {
            currenttip.add(EnumChatFormatting.AQUA + "Unlinked");
        }

        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {

        tag.setBoolean("linked", getController() != null);
        if (getController() != null) {
            tag.setInteger("controllerX", controllerX);
            tag.setInteger("controllerY", controllerY);
            tag.setInteger("controllerZ", controllerZ);
        }

        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    public PurificationUnitStatus status() {
        if (!this.mMachine) {
            return PurificationUnitStatus.INCOMPLETE_STRUCTURE;
        } else if (!this.isAllowedToWork()) {
            return PurificationUnitStatus.DISABLED;
        } else {
            return PurificationUnitStatus.ONLINE;
        }
    }
}
