package gregtech.common.tileentities.machines.multi.pcb;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.gui.MTEMultiBlockBaseGui;
import gregtech.api.util.GTUtility;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationPlant;
import gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * base class for the PCB Factory upgrades.
 * <p>
 * Made by guid118, with heavy inspiration from {@link gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase} by NotAPinguin
 */
public abstract class MTEPCBUpgradeBase<T extends MTEEnhancedMultiBlockBase<T>>
    extends MTEEnhancedMultiBlockBase<T> {


    private enum LinkResult {
        /**
         * Link target was out of range of the main controller
         */
        TOO_FAR,
        /**
         * No valid MTEPCBFactory was found at the link target position.
         */
        NO_VALID_FACTORY,
        /**
         * Link successful
         */
        SUCCESS,
    }


    /**
     * //TODO allow multiple controllers to "wallshare" this unit
     * Coordinates of the main purification plant controller. These can be used to find the controller again on world
     * load.
     */
    private int controllerX, controllerY, controllerZ;

    /**
     * Whether a controller was previously set.
     */
    private boolean controllerSet = false;

    /**
     * Pointers to the controllers
     * //TODO allow multiple controllers to "wallshare" this unit
     */
    private MTEPCBFactory controller = null;

    /**
     * Coolant usage per second.
     */
    private final int COOLANT_CONSUMED_PER_SEC = 10;


    protected MTEPCBUpgradeBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPCBUpgradeBase(String aName) {
        super(aName);
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // The individual purification unit structures cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    protected void setHatchRecipeMap(MTEHatchInput hatch) {
        // Do nothing, we don't have any hatches
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
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // If a linked controller was found, load its coordinates.
        // The unit will try to link to the real controller block periodically in onPostTick()

        // We cannot do this linking here yet because the controller block might not be loaded yet.
        //TODO make this work with any number of controllers
        if (aNBT.hasKey("controller")) {
            NBTTagCompound controllerNBT = aNBT.getCompoundTag("controller");
            controllerX = controllerNBT.getInteger("x");
            controllerY = controllerNBT.getInteger("y");
            controllerZ = controllerNBT.getInteger("z");
            controllerSet = true;
        }
        if (aNBT.hasKey("numberOfControllers")) {
            //TODO:
            //numberOfControllers = aNBT.getInteger("numberOfControllers");
        }
    }


    public NBTTagCompound saveLinkDataToNBT() {
        NBTTagCompound controllerNBT = new NBTTagCompound();
        controllerNBT.setInteger("x", controllerX);
        controllerNBT.setInteger("y", controllerY);
        controllerNBT.setInteger("z", controllerZ);
        return controllerNBT;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (controllerSet) {
            NBTTagCompound controllerNBT = saveLinkDataToNBT();
            aNBT.setTag("controller", controllerNBT);
        }
        //TODO:
        //aNBT.setInteger("numberOfControllers", numberOfControllers);
    }

    private LinkResult trySetControllerFromCoord(int x, int y, int z) {
        IGregTechTileEntity ourBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // First check whether the controller we try to link to is within range. The range is defined
        // as a max distance in each axis.
        if (Math.abs(ourBaseMetaTileEntity.getXCoord() - x) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getYCoord() - y) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getZCoord() - z) > MTEPurificationPlant.MAX_UNIT_DISTANCE)
            return LinkResult.TOO_FAR;

        // Find the block at the requested coordinated and check if it is a purification plant controller.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return LinkResult.NO_VALID_FACTORY;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity))
            return LinkResult.NO_VALID_FACTORY;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof MTEPCBFactory)) return LinkResult.NO_VALID_FACTORY;

        // Before linking, unlink from current controller, so we don't end up with units linked to multiple
        // controllers.
        MTEPCBFactory oldController = getController();
        if (oldController != null) {
            oldController.unregisterLinkedUnit(this);
            this.unlinkController();
        }

        // Now link to new controller
        controllerX = x;
        controllerY = y;
        controllerZ = z;
        controllerSet = true;
        controller = (MTEPCBFactory) metaTileEntity;
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
            .equals("PCBFactory")) {
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
        } else if (result == LinkResult.NO_VALID_FACTORY) {
            aPlayer.addChatMessage(new ChatComponentText("Link failed: No PCB Factory found at link location"));
        }

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {

        // Right-clicking could be a data stick linking action, otherwise we can ignore, as this multi does not have a GUI.
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }
        //TODO open a special GUI, instead of the normal machine GUI
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }


    public MTEPCBFactory getController() {
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
        MTEPCBFactory controller = getController();
        if (controller != null) {
            //TODO make this method in the PCB Factory class
            //controller.unregisterLinkedUnit(this);
        }
        super.onBlockDestroyed();
    }


    @Override
    public String[] getInfoData() {
        var ret = new ArrayList<String>();
        // if this unit has a controller, give the coordinates
        if (getController() != null) {
            ret.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.infodata.pcb_upgrade_base.linked_at",
                    controllerX,
                    controllerY,
                    controllerZ));
        } else ret.add(StatCollector.translateToLocal("GT5U.infodata.pcb_upgrade_base.not_linked"));
        return ret.toArray(new String[0]);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
                             IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.getBoolean("linked")) {
            currenttip.add(
                EnumChatFormatting.AQUA + "Linked to PCB Factory at: "
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

        List<String> supertip = new ArrayList<>();
        super.getWailaBody(itemStack, supertip, accessor, config);

        for (String s : supertip)
            if (!s.contains("Running Fine") && !s.contains("Idle")) {
                currenttip.add(s);
            }
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


}
