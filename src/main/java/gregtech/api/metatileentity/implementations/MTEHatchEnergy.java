package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchEnergyGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@IMetaTileEntity.SkipGenerateDescription
@IMetaTileEntity.SkipGenerateName
public class MTEHatchEnergy extends MTEHatch {

    public static final int FUSE_SLOT = 0;
    public static final int[] BACKUP_CAPACITOR_SLOTS = { 1, 2 };
    private static final int FUSE_BOX_SLOTS = 3;

    /** Whether this hatch has a fuse box, with a slot for a fuse and two for backup capacitors. */
    private final boolean hasFuseBox;

    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier) {
        this(aID, aName, aNameRegional, aTier, false);
    }

    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier, boolean hasFuseBox) {
        super(aID, aName, aNameRegional, aTier, hasFuseBox ? FUSE_BOX_SLOTS : 0, (String) null);
        this.hasFuseBox = hasFuseBox;
    }

    @SuppressWarnings("unused") // needed in an addon
    public MTEHatchEnergy(int aID, String aName, String aNameRegional, int aTier, int aInventorySize,
        String[] aDescriptionArray) {
        super(aID, aName, aNameRegional, aTier, aInventorySize, aDescriptionArray);
        this.hasFuseBox = false;
    }

    public MTEHatchEnergy(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, false, aDescription, aTextures);
    }

    public MTEHatchEnergy(String aName, int aTier, boolean hasFuseBox, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, hasFuseBox ? FUSE_BOX_SLOTS : 0, aDescription, aTextures);
        this.hasFuseBox = hasFuseBox;
        // every hatch comes with a fuse, a saved hatch without one removes it again when loading
        if (hasFuseBox) mInventory[FUSE_SLOT] = ItemList.ENERGY_HATCH_FUSES[aTier].get(1);
    }

    public MTEHatchEnergy(String name, int tier, int invSlotCount, String[] description, ITexture[][][] textures) {
        super(name, tier, invSlotCount, description, textures);
        this.hasFuseBox = false;
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
        return hasFuseBox && aIndex >= 0 && aIndex < FUSE_BOX_SLOTS;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean isItemValidForSlot(int aIndex, ItemStack aStack) {
        if (!isValidSlot(aIndex)) return false;
        ItemList[] fitting = aIndex == FUSE_SLOT ? ItemList.ENERGY_HATCH_FUSES : ItemList.BACKUP_CAPACITORS;
        return fitting[mTier].isStackEqual(aStack);
    }

    /**
     * The hatch only accepts energy through an intact fuse.
     */
    public boolean hasFuse() {
        return hasFuseBox && ItemList.ENERGY_HATCH_FUSES[mTier].isStackEqual(mInventory[FUSE_SLOT]);
    }

    /**
     * The fuse blows instead of the hatch exploding, and without an intact fuse there is nothing to explode.
     */
    @Override
    public boolean absorbOvervoltage(long aVoltage) {
        if (!hasFuseBox) return false;
        if (hasFuse()) {
            mInventory[FUSE_SLOT] = ItemList.Energy_Hatch_Fuse_Broken.get(1);
            markDirty();
        }
        return true;
    }

    public boolean hasBackupCapacitor() {
        if (!hasFuseBox) return false;
        for (int slot : BACKUP_CAPACITOR_SLOTS) {
            if (isChargedBackupCapacitor(slot)) return true;
        }
        return false;
    }

    /**
     * Replaces the first charged backup capacitor with a depleted one.
     */
    public void dischargeBackupCapacitor() {
        for (int slot : BACKUP_CAPACITOR_SLOTS) {
            if (isChargedBackupCapacitor(slot)) {
                mInventory[slot] = ItemList.Backup_Capacitor_Depleted.get(1);
                markDirty();
                return;
            }
        }
    }

    private boolean isChargedBackupCapacitor(int slot) {
        return ItemList.BACKUP_CAPACITORS[mTier].isStackEqual(mInventory[slot]);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (hasFuseBox && mInventory[FUSE_SLOT] == null) aNBT.setBoolean("noFuse", true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (hasFuseBox && aNBT.getBoolean("noFuse")) mInventory[FUSE_SLOT] = null;
    }

    /**
     * A hatch that lost its fuse keeps it lost when broken and placed again.
     */
    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        super.setItemNBT(aNBT);
        if (hasFuseBox && !hasFuse()) aNBT.setBoolean("noFuse", true);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        if (hasFuseBox && !hasFuse()) tag.setBoolean("noFuse", true);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("noFuse")) currentTip.add(StatCollector.translateToLocal("GT5U.waila.energy_hatch.no_fuse"));
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        super.addAdditionalTooltipInformation(stack, tooltip);
        if (stack.hasTagCompound() && stack.getTagCompound()
            .getBoolean("noFuse")) tooltip.add(StatCollector.translateToLocal("GT5U.waila.energy_hatch.no_fuse"));
    }

    /**
     * An intact fuse is soldered in, so it stays with the hatch.
     */
    @Override
    public boolean shouldDropItemAt(int index) {
        return !(index == FUSE_SLOT && hasFuse()) && super.shouldDropItemAt(index);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!hasFuseBox) return super.onRightclick(aBaseMetaTileEntity, aPlayer);
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
        return hasFuseBox && !hasFuse() ? 0 : 2;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchEnergy(mName, mTier, hasFuseBox, mDescriptionArray, mTextures);
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
        if (!hasFuseBox) return description;
        return ArrayUtils.addAll(description, GTSplit.splitLocalized("gt.blockmachines.energy_hatch.fuse.desc"));
    }
}
