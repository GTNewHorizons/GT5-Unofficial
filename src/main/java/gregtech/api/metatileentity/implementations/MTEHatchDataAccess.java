package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_DATA_ACCESS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.AssemblyLineUtils;
import gregtech.api.util.GTRecipe.RecipeAssemblyLine;
import gregtech.common.gui.modularui.hatch.MTEHatchDataAccessGUI;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchDataAccess extends MTEHatch implements IAddUIWidgets {

    private int timeout = 4;

    private List<RecipeAssemblyLine> cachedRecipes = null;

    public MTEHatchDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            16,
            new String[] { "Data Access for Multiblocks",
                "Adds " + (aTier == 4 ? 4 : 16) + " extra slots for Data Sticks" });
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

    public void setActive(boolean mActive) {
        getBaseMetaTileEntity().setActive(mActive);
        timeout = mActive ? 4 : 0;
    }

    @Override
    protected void onContentsChanged(int slot) {
        super.onContentsChanged(slot);

        cachedRecipes = null;
    }

    public List<RecipeAssemblyLine> getAssemblyLineRecipes() {
        if (cachedRecipes == null) {
            cachedRecipes = new ArrayList<>();

            for (int i = 0; i < getSizeInventory(); i++) {
                cachedRecipes.addAll(AssemblyLineUtils.findALRecipeFromDataStick(getStackInSlot(i)));
            }
        }

        return cachedRecipes;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchDataAccessGUI(this).build(data, syncManager, uiSettings);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("recipeCount", getAssemblyLineRecipes().size());
    }

    protected String getWailaDataI18nKey() {
        return "tt.keyphrase.AL_Recipe_Providing";
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);

        NBTTagCompound tag = accessor.getNBTData();
        currenttip.add(translate(getWailaDataI18nKey(), tag.getInteger("recipeCount")));
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> lines = new ArrayList<>();

        if (!getAssemblyLineRecipes().isEmpty()) {
            for (RecipeAssemblyLine recipe : getAssemblyLineRecipes()) {
                lines.add(translate("tt.keyphrase.AL_Recipe_Desc", recipe.mOutput.getDisplayName()));
            }
        } else {
            lines.add(translate("tt.keyphrase.AL_Recipe_None"));
        }

        lines.sort(String::compareTo);

        lines.add(0, translate("tt.keyphrase.AL_Recipe_Header"));

        return lines.toArray(new String[lines.size()]);
    }
}
