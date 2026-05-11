package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEFilterBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.MTEFilterGui;

public class MTEFilter extends MTEFilterBase {

    private static final int NUM_FILTER_SLOTS = 9;
    private boolean ignoreNbt = false;

    public MTEFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            19,
            new String[] { "Filters up to 9 different Items", "Use Screwdriver to regulate output stack size" });
    }

    public MTEFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public boolean isIgnoreNbt() {
        return ignoreNbt;
    }

    public void setIgnoreNbt(boolean ignoreNbt) {
        this.ignoreNbt = ignoreNbt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFilter(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_FILTER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_FILTER_GLOW)
                .glow()
                .build());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bIgnoreNBT", this.ignoreNbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.ignoreNbt = aNBT.getBoolean("bIgnoreNBT");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack)) {
            return false;
        }
        if (this.invertFilter) {
            for (int i = 0; i < NUM_FILTER_SLOTS; i++) {
                if (GTUtility.areStacksEqual(this.mInventory[FILTER_SLOT_INDEX + i], aStack, this.ignoreNbt)) {
                    return false;
                }
            }
            return true;
        }
        return GTUtility.areStacksEqual(this.mInventory[(FILTER_SLOT_INDEX + aIndex)], aStack, this.ignoreNbt);
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEFilterGui(this).build(guiData, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return false;
    }
}
