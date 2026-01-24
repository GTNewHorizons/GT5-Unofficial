package gregtech.common.tileentities.machines.multi.pcb;

import static gregtech.common.tileentities.machines.multi.pcb.MTEPCBFactory.UPGRADE_RANGE;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

/**
 * base class for the PCB Factory upgrades.
 * <p>
 * Made by guid118, with heavy inspiration from
 * {@link gregtech.common.tileentities.machines.multi.purification.MTEPurificationUnitBase} by NotAPinguin
 */
public abstract class MTEPCBUpgradeBase<T extends MTEEnhancedMultiBlockBase<T>> extends MTEEnhancedMultiBlockBase<T> {

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

    /*
     * Coordinates of the PCB Factories. These can be used to find the factories again on world
     * load.
     */
    protected final Set<Vec3Impl> controllerCoords = new HashSet<>();

    /**
     * Queue of all currently active factories, sorted by time left in their recipe.
     */
    private final Queue<MTEPCBFactory> activeFactories = new PriorityQueue<>(
        (a, b) -> Integer.compare(b.mMaxProgresstime - b.mProgresstime, a.mMaxProgresstime - a.mProgresstime));

    private MTEPCBFactory currentFactory;

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
        if (aTimer % 100 == 5 && !controllerCoords.isEmpty()) {
            for (Vec3Impl controllerCoord : controllerCoords) {
                trySetControllerFromCoord(controllerCoord.get(0), controllerCoord.get(1), controllerCoord.get(2));
            }
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // If a linked controller was found, load its coordinates.
        // The unit will try to link to the real controller block periodically in onPostTick()

        // We cannot do this linking here yet because the controller block might not be loaded yet.
        if (aNBT.hasKey("controllers")) {
            int[] coordinates = aNBT.getIntArray("controllers");
            controllerCoords.clear();
            // If not all coordinates got saved, just clear the list.
            if (coordinates.length % 3 != 0) return;

            for (int i = 0; i < coordinates.length; i += 3) {
                controllerCoords.add(new Vec3Impl(coordinates[i], coordinates[i + 1], coordinates[i + 2]));
            }
        }
    }

    public NBTTagIntArray saveLinkDataToNBT() {
        int[] array = new int[controllerCoords.size() * 3];
        int i = 0;
        for (Vec3Impl controllerCoord : controllerCoords) {
            array[i++] = controllerCoord.get(0);
            array[i++] = controllerCoord.get(1);
            array[i++] = controllerCoord.get(2);
        }
        return new NBTTagIntArray(array);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (!controllerCoords.isEmpty()) {
            NBTTagIntArray controllerNBT = saveLinkDataToNBT();
            aNBT.setTag("controllers", controllerNBT);
        }
    }

    /**
     * Remove the given factory from the list of linked factories.
     *
     * @param factory factory to remove
     */
    public void removeController(MTEPCBFactory factory) {
        IGregTechTileEntity BMTE = factory.getBaseMetaTileEntity();
        controllerCoords.removeIf(
            controllerCoord -> controllerCoord.get(1) == BMTE.getXCoord() && controllerCoord.get(1) == BMTE.getYCoord()
                && controllerCoord.get(1) == BMTE.getZCoord());
    }

    private LinkResult trySetControllerFromCoord(int x, int y, int z) {
        IGregTechTileEntity ourBaseMetaTileEntity = this.getBaseMetaTileEntity();
        // First check whether the controller we try to link to is within range. The range is defined
        // as a max distance in each axis.
        if (Math.abs(ourBaseMetaTileEntity.getXCoord() - x) > UPGRADE_RANGE) return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getYCoord() - y) > UPGRADE_RANGE) return LinkResult.TOO_FAR;
        if (Math.abs(ourBaseMetaTileEntity.getZCoord() - z) > UPGRADE_RANGE) return LinkResult.TOO_FAR;

        // Find the block at the requested coordinated and check if it is a purification plant controller.
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return LinkResult.NO_VALID_FACTORY;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return LinkResult.NO_VALID_FACTORY;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof MTEPCBFactory)) return LinkResult.NO_VALID_FACTORY;

        // Now link to new controller
        MTEPCBFactory factory = (MTEPCBFactory) metaTileEntity;
        factory.registerLinkedUnit(this);

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
        if (result == LinkResult.SUCCESS) {
            controllerCoords.add(new Vec3Impl(x, y, z));
        }

        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        // Right-clicking could be a data stick linking action, otherwise we can ignore, as this multi does not have a
        // GUI.
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }
        return super.onRightclick(aBaseMetaTileEntity, aPlayer);
    }

    // If the controller is broken this can be called to explicitly unlink the controller, so we don't have any
    // references lingering around
    public void unlinkController(MTEPCBFactory oldController) {
        IGregTechTileEntity tileEntity = oldController.getBaseMetaTileEntity();
        if (tileEntity != null) controllerCoords
            .remove(new Vec3Impl(tileEntity.getXCoord(), tileEntity.getYCoord(), tileEntity.getZCoord()));
    }

    @Override
    public void onBlockDestroyed() {
        // When this block is destroyed, explicitly unlink it from the controller if there is any.
        for (Vec3Impl controllerCoord : controllerCoords) {
            TileEntity TE = this.getBaseMetaTileEntity()
                .getWorld()
                .getTileEntity(controllerCoord.get(0), controllerCoord.get(1), controllerCoord.get(2));
            if (TE instanceof IGregTechTileEntity GTTE) {
                if (GTTE.getMetaTileEntity() instanceof MTEPCBFactory controller) {
                    controller.unregisterLinkedUnit(this);
                }
            }

        }
        super.onBlockDestroyed();
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        if (!controllerCoords.isEmpty()) {
            for (Vec3Impl controllerCoord : controllerCoords) {
                info.add(
                    StatCollector.translateToLocalFormatted(
                        "GT5U.infodata.pcb_upgrade_base.linked_at",
                        controllerCoord.get(0),
                        controllerCoord.get(0),
                        controllerCoord.get(0)));
            }
        } else info.add(StatCollector.translateToLocal("GT5U.infodata.pcb_upgrade_base.not_linked"));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.hasKey("controllers")) {
            int[] coordinates = tag.getIntArray("controllers");
            // If not all coordinates got saved, just clear the list.
            if (coordinates.length % 3 != 0) return;
            for (int i = 0; i < coordinates.length; i += 3) {
                currentTip.add(
                    EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                        "GT5U.waila.pcb.upgrade_base.linked_to",
                        coordinates[i],
                        coordinates[i + 1],
                        coordinates[i + 2]));
            }
        } else {
            currentTip.add(EnumChatFormatting.AQUA + StatCollector.translateToLocal("GT5U.waila.base.unlinked"));
        }

        boolean isActive = tag.getBoolean("isActive");
        if (isActive) {
            int progresstime = tag.getInteger("mProgressTime");
            int maxProgresstime = tag.getInteger("mMaxProgressTime");
            currentTip.add(
                StatCollector.translateToLocalFormatted(
                    "GT5U.waila.machine.in_progress",
                    (double) progresstime / 20,
                    (double) maxProgresstime / 20,
                    (Math.round((double) progresstime / maxProgresstime * 1000) / 10.0)));
        } else {
            currentTip.add(StatCollector.translateToLocalFormatted("GT5U.waila.machine.idle"));
        }

        currentTip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.facing",
                getFacingNameLocalized(
                    this.getBaseMetaTileEntity()
                        .getFrontFacing()
                        .ordinal())));

    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        boolean isActive = this.getBaseMetaTileEntity()
            .isActive();
        tag.setBoolean("isActive", isActive);

        if (isActive) {
            tag.setInteger("mProgressTime", mProgresstime);
            tag.setInteger("mMaxProgressTime", mMaxProgresstime);
        }
        if (!controllerCoords.isEmpty()) {
            tag.setTag("controllers", saveLinkDataToNBT());
        } else tag.removeTag("controllers");
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        return CheckRecipeResultRegistry.NONE;
    }

    /**
     * removes any factories that are not there anymore, in case onBlockBreak didn't fire
     */
    protected void checkFactories() {
        for (Vec3Impl coords : controllerCoords) {
            TileEntity TE = this.getBaseMetaTileEntity()
                .getWorld()
                .getTileEntity(coords.get(0), coords.get(1), coords.get(2));
            if (TE instanceof IGregTechTileEntity GTTE) {
                IMetaTileEntity MTE = GTTE.getMetaTileEntity();
                if (!(MTE instanceof MTEPCBFactory)) {
                    controllerCoords.remove(coords);
                }
            } else controllerCoords.remove(coords);
        }
    }

    @Override
    protected void runMachine(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        MTEPCBFactory factory = activeFactories.peek();
        if (factory != null && !factory.equals(currentFactory)) {
            this.currentFactory = factory;
            this.mProgresstime = factory.mProgresstime;
            this.mMaxProgresstime = factory.mMaxProgresstime;
        }
        if (factory != null && factory.mMaxProgresstime <= 0) {
            activeFactories.remove();
        }
        super.runMachine(aBaseMetaTileEntity, aTick);
    }

    public void addRecipe(MTEPCBFactory factory) {
        IGregTechTileEntity baseFactory = factory.getBaseMetaTileEntity();
        for (MTEPCBFactory listedFactory : activeFactories) {
            IGregTechTileEntity listedBaseFactory = listedFactory.getBaseMetaTileEntity();
            if (baseFactory.getXCoord() == listedBaseFactory.getXCoord()
                && baseFactory.getYCoord() == listedBaseFactory.getYCoord()
                && baseFactory.getZCoord() == listedBaseFactory.getZCoord()) {
                return;
            }
        }
        activeFactories.add(factory);
    }

    public void cancelRecipe(MTEPCBFactory factory) {
        activeFactories.remove(factory);
    }

}
