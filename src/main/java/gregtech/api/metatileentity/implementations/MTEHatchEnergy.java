package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchEnergyGui;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTEHatchEnergy extends MTEHatch {

    public static final int FUSE_SLOT = 0;

    /** Whether this hatch has a slot for an Energy Hatch Fuse. */
    private final boolean acceptsFuse;

    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier) {
        this(aID, aName, aNameRegional, aTier, false);
    }

    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier, boolean acceptsFuse) {
        super(aID, aName, aNameRegional, aTier, acceptsFuse ? 1 : 0, (String) null);
        this.acceptsFuse = acceptsFuse;
    }

    @SuppressWarnings("unused") // needed in an addon
    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier, int aInventorySize,
        String[] aDescriptionArray) {
        super(aID, aName, aNameRegional, aTier, aInventorySize, aDescriptionArray);
        this.acceptsFuse = false;
    }

    public MTEHatchEnergy(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, false, aDescription, aTextures);
    }

    public MTEHatchEnergy(String aName, int aTier, boolean acceptsFuse, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, acceptsFuse ? 1 : 0, aDescription, aTextures);
        this.acceptsFuse = acceptsFuse;
    }

    public MTEHatchEnergy(String name, int tier, int invSlotCount, String[] description, ITexture[][][] textures) {
        super(name, tier, invSlotCount, description, textures);
        this.acceptsFuse = false;
    }

    @Override
    public String getLocalName() {
        if (!hasOwnLocalName()) return super.getLocalName();
        return StatCollector.translateToLocalFormatted("gt.blockmachines.hatch.energy.name", GTValues.VN[mTier]);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_2A[mTier + 1] };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, Textures.BlockIcons.OVERLAYS_ENERGY_IN_MULTI_2A[mTier + 1] };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isEnetInput() {
        return true;
    }

    @Override
    public boolean isInputFacing(ForgeDirection side) {
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return acceptsFuse && aIndex == FUSE_SLOT;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        return isValidSlot(aIndex) && getFuseTier(aStack) == mTier;
    }

    public boolean hasFuse() {
        return acceptsFuse && getFuseTier(mInventory[FUSE_SLOT]) == mTier;
    }

    /**
     * Replaces the fuse with the tiny pile of glass dust left over from it.
     */
    public void blowFuse() {
        mInventory[FUSE_SLOT] = GTOreDictUnificator.get(OrePrefixes.dustTiny, Materials.Glass, 1L);
        markDirty();
    }

    /**
     * @return The voltage tier of an Energy Hatch Fuse, or -1 if the stack is not one.
     */
    public static int getFuseTier(ItemStack aStack) {
        if (aStack == null) return -1;
        for (int tier = 0; tier < ItemList.ENERGY_HATCH_FUSES.length; tier++) {
            if (ItemList.ENERGY_HATCH_FUSES[tier].isStackEqual(aStack)) return tier;
        }
        return -1;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!acceptsFuse) return super.onRightclick(aBaseMetaTileEntity, aPlayer);
        if (aBaseMetaTileEntity.isServerSide()) openGui(aPlayer);
        return true;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchEnergyGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public long maxEUInput() {
        return V[mTier];
    }

    @Override
    public long maxEUStore() {
        return 512L + V[mTier] * 8L;
    }

    @Override
    public long maxAmperesIn() {
        return 2;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchEnergy(mName, mTier, acceptsFuse, mDescriptionArray, mTextures);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public String[] getDescription() {
        String[] description = GTSplit.splitLocalized("gt.blockmachines.energy_hatch.desc");
        if (!acceptsFuse) return description;
        return ArrayUtils.addAll(description, GTSplit.splitLocalized("gt.blockmachines.energy_hatch.fuse.desc"));
    }
}
