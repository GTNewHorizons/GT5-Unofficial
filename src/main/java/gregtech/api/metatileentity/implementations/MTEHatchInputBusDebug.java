package gregtech.api.metatileentity.implementations;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.item.ItemStackHandler;
import com.cleanroommc.modularui.utils.item.LimitingItemStackHandler;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusDebugGui;

public class MTEHatchInputBusDebug extends MTEHatchInputBus implements IConfigurationCircuitSupport {

    private static final int SLOT_COUNT = 16;
    public final ItemStackHandler phantomHolder = new LimitingItemStackHandler(SLOT_COUNT, 1);

    public MTEHatchInputBusDebug(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier, 1);
    }

    public MTEHatchInputBusDebug(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (phantomHolder != null) {
            aNBT.setTag("phantomInventory", phantomHolder.serializeNBT());
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (phantomHolder != null) {
            phantomHolder.deserializeNBT(aNBT.getCompoundTag("phantomInventory"));
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_BUS_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_BUS_IN_DEBUG) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBusDebug(mName, mTier, new String[0], mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
    }

    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stocks Items internally",
            EnumChatFormatting.GRAY + "Configure Items in the UI",
            EnumChatFormatting.GRAY + "Configured Items will not be consumed in processing",
            EnumChatFormatting.ITALIC + "Who knew it was this easy???", "Author: " + GREEN + BOLD + "Chrom" };
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, int ordinalSide) {
        return false;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, int ordinalSide) {
        return false;
    }

    @Override
    public int getCircuitSlot() {
        return 0;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlotX() {
        return 152;
    }

    @Override
    public int getCircuitSlotY() {
        return 59;
    }

    @Override
    public int getSizeInventory() {
        // Add fake slots so that multis can detect the stocked items properly
        // 0 to 15: stocked items
        // 16: circuit
        return SLOT_COUNT + 1;
    }

    @Override
    public ItemStack getStackInSlot(int slotIndex) {
        if (slotIndex < 0 || slotIndex >= getSizeInventory()) return null;
        if (slotIndex == getCircuitSlot() + SLOT_COUNT) return mInventory[getCircuitSlot()];

        ItemStack stack = phantomHolder.getStackInSlot(slotIndex);
        if (stack == null) return null;

        ItemStack returnStack = stack.copy();
        returnStack.stackSize = Integer.MAX_VALUE;

        return returnStack;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusDebugGui(this).build(data, syncManager, uiSettings);
    }
}
