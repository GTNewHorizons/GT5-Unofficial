package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;

import java.util.LinkedHashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.google.common.collect.ImmutableList;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.api.util.GTSplit;
import gregtech.common.gui.modularui.hatch.MTEHatchDataAccessGui;
import gregtech.common.tileentities.machines.ISmartInputHatch;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeInputHatch;

/// The AL recipe input hatch that pulls its recipes from data sticks.
@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDataAccess extends MTEHatchDataBase implements ISmartInputHatch, IALRecipeInputHatch {

    private int timeout = 4;

    protected ALRecipeDataPacket cachedRecipes = null;

    public MTEHatchDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            16,
            GTSplit.splitLocalizedFormatted("gt.blockmachines.hatch.dataaccess.desc", aTier == 4 ? 4 : 16));
    }

    public MTEHatchDataAccess(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aTier == 4 ? 4 : 16, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_DATA_ACCESS) };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDataAccess(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mTier >= 8 && !aBaseMetaTileEntity.isActive();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return mTier >= 8 && !aBaseMetaTileEntity.isActive();
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (aBaseMetaTileEntity.isServerSide() && aBaseMetaTileEntity.isActive()) {
            timeout--;
            if (timeout <= 0) {
                aBaseMetaTileEntity.setActive(false);
            }
        }
    }

    @Override
    public void setActive(boolean mActive) {
        getBaseMetaTileEntity().setActive(mActive);
        timeout = mActive ? 4 : 0;
    }

    @Override
    public void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        cachedRecipes = null;
        // Adding/removing a data stick changes which assembly-line recipes are available, so push a recipe check.
        notifyWatchers();
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        cachedRecipes = null;
    }

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        if (cachedRecipes == null) {
            LinkedHashSet<RecipeAssemblyLine> recipes = new LinkedHashSet<>();

            for (int i = 0; i < getSizeInventory(); i++) {
                recipes.addAll(AssemblyLineUtils.findALRecipeFromDataStick(getStackInSlot(i)));
            }

            this.cachedRecipes = new ALRecipeDataPacket(ImmutableList.copyOf(recipes));
        }

        return cachedRecipes;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDataAccessGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Providing";
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return ItemList.Tool_DataStick.isStackEqual(itemStack, false, true)
            && super.isItemValidForSlot(index, itemStack);
    }
}
