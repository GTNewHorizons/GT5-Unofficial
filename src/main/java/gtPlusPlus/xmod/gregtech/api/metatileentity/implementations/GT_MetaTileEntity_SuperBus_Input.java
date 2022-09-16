package gtPlusPlus.xmod.gregtech.api.metatileentity.implementations;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.gui.GT_GUIDialogSelectItem;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.net.GT_Packet_SetConfigurationCircuit;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.extensions.ArrayExt;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class GT_MetaTileEntity_SuperBus_Input extends GT_MetaTileEntity_Hatch_InputBus {
    public GT_MetaTileEntity_SuperBus_Input(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, getSlots(aTier) + 1);
    }

    public GT_MetaTileEntity_SuperBus_Input(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    /**
     * Returns a factor of 16 based on tier.
     *
     * @param aTier The tier of this bus.
     * @return (1 + aTier) * 16
     */
    public static int getSlots(int aTier) {
        return (1 + aTier) * 16;
    }

    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SuperBus_Input(
                this.mName, this.mTier, ArrayExt.of(this.mDescription), this.mTextures);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return null;
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            "Item Input for Multiblocks",
            "This bus has no GUI, but can have items extracted",
            "" + (getSlots(this.mTier) + 1) + " Slots",
            "To set circuit slot, left click with empty hand",
            CORE.GT_Tooltip
        };
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide() && aPlayer.getCurrentEquippedItem() == null) openCircuitSelector();
    }

    @SideOnly(Side.CLIENT)
    private void openCircuitSelector() {
        List<ItemStack> circuits = getConfigurationCircuits();
        Minecraft.getMinecraft()
                .displayGuiScreen(new GT_GUIDialogSelectItem(
                        StatCollector.translateToLocal("GT5U.machines.select_circuit"),
                        getStackForm(0),
                        null,
                        this::onCircuitSelected,
                        circuits,
                        GT_Utility.findMatchingStackInList(circuits, getStackInSlot(getCircuitSlot()))));
    }

    @SideOnly(Side.CLIENT)
    private void onCircuitSelected(ItemStack selected) {
        GT_Values.NW.sendToServer(new GT_Packet_SetConfigurationCircuit(getBaseMetaTileEntity(), selected));
        // we will not do any validation on client side
        // it doesn't get to actually decide what inventory contains anyway
        setInventorySlotContents(getCircuitSlot(), selected);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (aBaseMetaTileEntity.isClientSide()) {
            return true;
        } else {
            // Logger.INFO("Trying to display Super Input Bus contents.");
            displayBusContents(aPlayer);
            return true;
        }
    }

    public void displayBusContents(EntityPlayer aPlayer) {
        String STRIP = "Item Array: ";
        String aNameString = ItemUtils.getArrayStackNames(mInventory);
        aNameString = aNameString.replace(STRIP, "");

        String[] aNames;
        if (aNameString.length() < 1) {
            aNames = null;
        } else {
            aNames = aNameString.split(",");
        }

        if (aNames == null || aNames.length == 0) {
            PlayerUtils.messagePlayer(aPlayer, "This Super Bus (I) is Empty. Total Slots: " + mInventory.length);
            return;
        }

        PlayerUtils.messagePlayer(aPlayer, "This Super Bus (I) contains: [" + mInventory.length + "]");

        if (aNames.length <= 12) {
            for (String s : aNames) {
                if (s.startsWith(" ")) {
                    s = s.substring(1);
                }
                // Logger.INFO("Trying to display Super Input Bus contents. "+s);
                PlayerUtils.messagePlayer(aPlayer, s);
            }
        } else {

            StringBuilder superString = new StringBuilder();

            for (String s : aNames) {
                if (s.startsWith(" ")) {
                    s = s.substring(1);
                }
                superString.append(s).append(", ");
            }
            PlayerUtils.messagePlayer(aPlayer, superString.toString());
        }
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
    }
}
