package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import appeng.api.config.Actionable;
import appeng.api.config.PowerMultiplier;
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
import appeng.util.item.AEItemStack;
import cpw.mods.fml.common.Optional;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IConfigurationCircuitSupport;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.GT_Container_InputBus_ME;
import gregtech.common.gui.GT_GUIContainer_InputBus_ME;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

public class GT_MetaTileEntity_Hatch_InputBus_ME extends GT_MetaTileEntity_Hatch_InputBus
        implements IConfigurationCircuitSupport {
    private static final int SLOT_COUNT = 16;
    private BaseActionSource requestSource = null;
    private AENetworkProxy gridProxy = null;
    private final ItemStack[] shadowInventory = new ItemStack[SLOT_COUNT];
    private final int[] savedStackSizes = new int[SLOT_COUNT];
    private boolean processingRecipe = false;

    public GT_MetaTileEntity_Hatch_InputBus_ME(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 1, SLOT_COUNT * 2 + 1, new String[] {
            "Advanced item input for Multiblocks", "Retrieves directly from ME", "Keeps 16 item types in stock"
        });
        disableSort = true;
    }

    public GT_MetaTileEntity_Hatch_InputBus_ME(
            String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, SLOT_COUNT * 2 + 1, aDescription, aTextures);
        disableSort = true;
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
                gridProxy = new AENetworkProxy(
                        (IGridProxyable) getBaseMetaTileEntity(), "proxy", ItemList.Hatch_Output_Bus_ME.get(1), true);
                gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
                if (getBaseMetaTileEntity().getWorld() != null)
                    gridProxy.setOwner(getBaseMetaTileEntity()
                            .getWorld()
                            .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
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
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        int[] sizes = new int[16];
        for (int i = 0; i < 16; ++i) sizes[i] = mInventory[i + 16] == null ? 0 : mInventory[i + 16].stackSize;
        aNBT.setIntArray("sizes", sizes);
        if (GregTech_API.mAE2) {
            gridProxy.writeToNBT(aNBT);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("sizes")) {
            int[] sizes = aNBT.getIntArray("sizes");
            if (sizes.length == 16) {
                for (int i = 0; i < 16; ++i) {
                    if (sizes[i] != 0 && mInventory[i] != null) {
                        ItemStack s = mInventory[i].copy();
                        s.stackSize = sizes[i];
                        mInventory[i + 16] = s;
                    }
                }
            }
        }
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
        return new String[] {
            "The bus is "
                    + ((getProxy() != null && getProxy().isActive())
                            ? EnumChatFormatting.GREEN + "online"
                            : EnumChatFormatting.RED + "offline")
                    + EnumChatFormatting.RESET
        };
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
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {}

    @Override
    public void updateSlots() {}

    @Override
    public int getCircuitSlot() {
        return SLOT_COUNT * 2;
    }

    @Override
    public int getCircuitSlotX() {
        return 80;
    }

    @Override
    public int getCircuitSlotY() {
        return 63;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (!processingRecipe) return super.getStackInSlot(aIndex);
        if (aIndex < 0 || aIndex > mInventory.length) return null;
        if (aIndex >= SLOT_COUNT && aIndex < SLOT_COUNT * 2)
            // Display slots
            return null;
        if (aIndex == getCircuitSlot()) return mInventory[aIndex];
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
                    this.savedStackSizes[aIndex] = this.shadowInventory[aIndex].stackSize;
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, this.shadowInventory[aIndex]);
                    return this.shadowInventory[aIndex];
                } else {
                    // Request failed
                    this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
            } catch (final GridAccessException ignored) {
            }
            return null;
        } else {
            // AE available but no items requested
            this.setInventorySlotContents(aIndex + SLOT_COUNT, null);
        }
        return mInventory[aIndex];
    }

    @Optional.Method(modid = "appliedenergistics2")
    private BaseActionSource getRequestSource() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
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
                    ItemStack oldStack = shadowInventory[i];
                    if (oldStack == null || oldStack.stackSize < savedStackSizes[i]) {
                        AENetworkProxy proxy = getProxy();
                        try {
                            IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
                            IAEItemStack request = AEItemStack.create(mInventory[i]);
                            request.setStackSize(savedStackSizes[i] - (oldStack == null ? 0 : oldStack.stackSize));
                            sg.extractItems(request, Actionable.MODULATE, getRequestSource());
                            proxy.getEnergy()
                                    .extractAEPower(
                                            request.getStackSize(), Actionable.MODULATE, PowerMultiplier.CONFIG);
                            setInventorySlotContents(i + SLOT_COUNT, oldStack);
                        } catch (final GridAccessException ignored) {
                        }
                    }
                    savedStackSizes[i] = 0;
                    shadowInventory[i] = null;
                }
            }
        }
        processingRecipe = false;
    }

    public ItemStack updateInformationSlot(int aIndex, ItemStack aStack) {
        if (GregTech_API.mAE2 && aIndex >= 0 && aIndex < SLOT_COUNT) {
            if (aStack == null) {
                super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
            } else {
                AENetworkProxy proxy = getProxy();
                if (!proxy.isActive()) {
                    super.setInventorySlotContents(aIndex + SLOT_COUNT, null);
                    return null;
                }
                try {
                    IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
                    IAEItemStack request = AEItemStack.create(mInventory[aIndex]);
                    request.setStackSize(Integer.MAX_VALUE);
                    IAEItemStack result = sg.extractItems(request, Actionable.SIMULATE, getRequestSource());
                    ItemStack s = (result != null) ? result.getItemStack() : null;
                    setInventorySlotContents(aIndex + SLOT_COUNT, s);
                    return s;
                } catch (final GridAccessException ignored) {
                }
            }
        }
        return null;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }
}
