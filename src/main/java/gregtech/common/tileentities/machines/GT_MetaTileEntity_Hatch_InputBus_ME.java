package gregtech.common.tileentities.machines;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import appeng.api.config.Actionable;
import appeng.api.networking.GridFlags;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.GT_Container_InputBus_ME;
import gregtech.common.gui.GT_GUIContainer_InputBus_ME;
import gregtech.api.util.GT_Utility;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

public class GT_MetaTileEntity_Hatch_InputBus_ME extends GT_MetaTileEntity_Hatch_InputBus {
    private static final int SLOT_COUNT = 16;
    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;
    private ItemStack[] shadowInventory = new ItemStack[SLOT_COUNT];
    private IAEItemStack[] viewInventory = new IAEItemStack[SLOT_COUNT];
    private int[] savedStackSizes = new int[SLOT_COUNT];
    private boolean processingRecipe = false;
    public GT_MetaTileEntity_Hatch_InputBus_ME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1, SLOT_COUNT * 2 + 1, new String[] {
            "Advanced item input for Multiblocks",
            "Retrieves directly from ME",
            "Keeps 16 item types in stock"
        });
        disableSort = true;
    }

    public GT_MetaTileEntity_Hatch_InputBus_ME(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, SLOT_COUNT * 2 + 1, aDescription, aTextures);
        disableSort = true;
    }

    public IAEItemStack getViewItem(int aIndex) {
        if (aIndex > 0 && aIndex < SLOT_COUNT) {
            return viewInventory[aIndex];
        }
        return null;
    }

    public int getSlotCount() {
        return SLOT_COUNT;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_InputBus_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] {aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH_ACTIVE)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH)};
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_InputBus_ME(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_InputBus_ME(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing((byte) forgeDirection.ordinal()) ? AECableType.SMART : AECableType.NONE;
    }


    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            if (getBaseMetaTileEntity() instanceof IGridProxyable) {
                gridProxy = new AENetworkProxy((IGridProxyable) getBaseMetaTileEntity(), "proxy", ItemList.Hatch_Output_Bus_ME.get(1), true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                if (getBaseMetaTileEntity().getWorld() != null)
                    gridProxy.setOwner(getBaseMetaTileEntity().getWorld().getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
            }
        }
        return this.gridProxy;
    }

    @Override
    @Optional.Method(modid = "appliedenergistics2")
    public void gridChanged() {
        if (getBaseMetaTileEntity() != null && getBaseMetaTileEntity().getTimer() > 1) {
            gridProxy = null;
            getProxy();
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT)
    {
        super.saveNBTData(aNBT);
        if (GregTech_API.mAE2) {
            gridProxy.writeToNBT(aNBT);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (GregTech_API.mAE2) {
            getProxy().readFromNBT(aNBT);
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[]{
            "The bus is " + ((getProxy() != null && getProxy().isActive()) ?
            EnumChatFormatting.GREEN + "online" : EnumChatFormatting.RED + "offline")
                + EnumChatFormatting.RESET};
    }
    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
    }

    @Override
    public void updateSlots() {
    }

    @Override
    public int getCircuitSlot() { return SLOT_COUNT * 2; }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (!processingRecipe)
            return super.getStackInSlot(aIndex);
        if (aIndex < 0 && aIndex > mInventory.length)
            return null;
        if (aIndex >= SLOT_COUNT && aIndex < SLOT_COUNT * 2)
            //Display slots
            return null;
        if (aIndex == getCircuitSlot())
            return mInventory[aIndex];
        if (GregTech_API.mAE2 && mInventory[aIndex] != null) {
            AENetworkProxy proxy = getProxy();
            if (proxy == null) {
                return null;
            }
            try {
                IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
                IAEItemStack request = AEItemStack.create(mInventory[aIndex]);
                request.setStackSize(Integer.MAX_VALUE);
                IAEItemStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
                if (result != null) {
                    this.shadowInventory[aIndex] = result.getItemStack();
                    this.viewInventory[aIndex] = result;
                    this.savedStackSizes[aIndex] = this.shadowInventory[aIndex].stackSize;
                    //Show that the request was successful
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, GT_Utility.copyAmount(
                        this.shadowInventory[aIndex].stackSize > 64 ? 64 : this.shadowInventory[aIndex].stackSize,
                         new Object[] {result.getItemStack()}));
                    return this.shadowInventory[aIndex];
                } else {
                    //Request failed
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
            }
            catch( final GridAccessException ignored )
            {
            }
            return null;
        } else {
            //AE available but no items requested
            this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
        }
        return mInventory[aIndex];
    }

    @Optional.Method(modid = "appliedenergistics2")
    private BaseActionSource getRequestSource() {
        if (requestSource == null)
            requestSource = new MachineSource((IActionHost)getBaseMetaTileEntity());
        return requestSource;
    }

    @Override
    public void onExplosion() {
        for (int i = 0; i < SLOT_COUNT; i++) {
            mInventory[i] = null;
        }
    }

    public void startRecipeProcessing() {
        processingRecipe = true;
    }

    public void endRecipeProcessing() {
        if (GregTech_API.mAE2) {
            for (int i = 0; i < SLOT_COUNT; ++i) {
                if (savedStackSizes[i] != 0) {
                    if (shadowInventory[i] == null || shadowInventory[i].stackSize < savedStackSizes[i]) {
                        AENetworkProxy proxy = getProxy();
                        try {
                            IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
                            IAEItemStack request = AEItemStack.create(mInventory[i]);
                            request.setStackSize(savedStackSizes[i] - shadowInventory[i].stackSize);
                            sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                        }
                        catch (final GridAccessException ignored) {
                        }
                    }
                    savedStackSizes[i] = 0;
                    shadowInventory[i] = null;
                }
            }
        }
        processingRecipe = false;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }
}
