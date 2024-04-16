package gregtech.common.tileentities.machines.multi.purification;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;

/// Base class for purification units. This class handles all shared behaviour between units.
/// This includes
/// - Linking using data sticks and storing data about the linked purification plant controller
/// -
/// When inheriting from this, make sure to call super.loadNBTData() and super.saveNBTData()
/// if you override these methods, or linking will break.
public abstract class GT_MetaTileEntity_PurificationUnitBase<T extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T>>
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<T> {

    private int controllerX, controllerY, controllerZ;
    private boolean controllerSet = false;

    private GT_MetaTileEntity_PurificationPlant controller = null;

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
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
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

    private GT_MetaTileEntity_PurificationPlant trySetControllerFromCoord(int x, int y, int z) {
        var tileEntity = getBaseMetaTileEntity().getWorld()
            .getTileEntity(x, y, z);
        if (tileEntity == null) return null;
        if (!(tileEntity instanceof IGregTechTileEntity gtTileEntity)) return null;
        var metaTileEntity = gtTileEntity.getMetaTileEntity();
        if (!(metaTileEntity instanceof GT_MetaTileEntity_PurificationPlant)) return null;
        controllerX = x;
        controllerY = y;
        controllerZ = z;
        controllerSet = true;
        controller = (GT_MetaTileEntity_PurificationPlant) metaTileEntity;
        return controller;
    }

    private boolean tryLinkDataStick(EntityPlayer aPlayer) {
        ItemStack dataStick = aPlayer.inventory.getCurrentItem();

        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) {
            return false;
        }
        if (!dataStick.hasTagCompound() || !dataStick.stackTagCompound.getString("type")
            .equals("PurificationPlant")) {
            return false;
        }

        NBTTagCompound nbt = dataStick.stackTagCompound;
        int x = nbt.getInteger("x");
        int y = nbt.getInteger("y");
        int z = nbt.getInteger("z");
        if (trySetControllerFromCoord(x, y, z) != null) {
            aPlayer.addChatMessage(new ChatComponentText("Link successful"));
            return true;
        }
        aPlayer.addChatMessage(new ChatComponentText("Link failed"));
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) {
            return false;
        }
        if (tryLinkDataStick(aPlayer)) {
            return true;
        }

        GT_MetaTileEntity_PurificationPlant controller = getController();
        if (controller != null) {
            return controller.onRightclick(controller.getBaseMetaTileEntity(), aPlayer);
        }
        return false;
    }

    public GT_MetaTileEntity_PurificationPlant getController() {
        if (controller == null) return null;
        // Controller disappeared
        if (controller.getBaseMetaTileEntity() == null) return null;
        return controller;
    }
}
