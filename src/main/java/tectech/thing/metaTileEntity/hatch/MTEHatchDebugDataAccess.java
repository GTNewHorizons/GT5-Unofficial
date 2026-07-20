package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataAccess;
import gregtech.api.util.GTRecipe;
import tectech.util.CommonValues;

public class MTEHatchDebugDataAccess extends MTEHatchDataAccess {

    public MTEHatchDebugDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchDebugDataAccess(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDebugDataAccess(this.mName, this.mTier, getDescription(), this.mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        return true;
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
    public int getSizeInventory() {
        return 0;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        return null;
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return false;
    }

    @Override
    public List<GTRecipe.RecipeAssemblyLine> getAssemblyLineRecipes() {
        return GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes;
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.TEC_MARK_EM, translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.0"),
            translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.1"),
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.2") };
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocal("tt.keyphrase.AL_Recipe_Header"),
            translateToLocalFormatted("tt.keyphrase.AL_Recipe_All", getAssemblyLineRecipes().size()) };
    }
}
