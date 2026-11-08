package tectech.thing.metaTileEntity.hatch;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.util.EnumChatFormatting;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;

import gregtech.api.enums.Dyes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchDataBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import tectech.mechanics.dataTransport.ALRecipeDataPacket;
import tectech.mechanics.dataTransport.IALRecipeInputHatch;

@IMetaTileEntity.SkipGenerateDescription
public class MTEHatchDebugDataAccess extends MTEHatchDataBase implements IALRecipeInputHatch {

    public MTEHatchDebugDataAccess(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            0,
            new String[] { translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.0"),
                translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.1"),
                EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.dataaccess.desc.2") });
    }

    public MTEHatchDebugDataAccess(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchDebugDataAccess(this.mName, this.mTier, getDescription(), this.mTextures);
    }

    @Override
    public @NotNull ALRecipeDataPacket getAssemblyLineRecipes() {
        return new ALRecipeDataPacket(ImmutableList.copyOf(GTRecipe.RecipeAssemblyLine.sAssemblylineRecipes));
    }

    @Override
    public void setActive(boolean active) {
        // do nothing
    }

    @Override
    protected String getWailaDataKey() {
        return "tt.keyphrase.AL_Recipe_Receiving";
    }

    @Override
    public String[] getInfoData() {
        return new String[] { translateToLocal("tt.keyphrase.AL_Recipe_Header"),
            translateToLocalFormatted("tt.keyphrase.AL_Recipe_All", getAssemblyLineRecipes().contents.size()) };
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(
                MTEHatchDataConnector.EM_D_ACTIVE,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(MTEHatchDataConnector.EM_D_CONN) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture,
            TextureFactory.of(
                MTEHatchDataConnector.EM_D_SIDES,
                Dyes.getModulation(getBaseMetaTileEntity().getColorization(), MACHINE_METAL.getRGBA())),
            TextureFactory.of(MTEHatchDataConnector.EM_D_CONN) };
    }
}
