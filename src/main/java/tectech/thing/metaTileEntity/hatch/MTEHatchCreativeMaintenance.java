package tectech.thing.metaTileEntity.hatch;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchMaintenance;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.render.TextureFactory;
import tectech.util.CommonValues;

public class MTEHatchCreativeMaintenance extends MTEHatchMaintenance {

    private static Textures.BlockIcons.CustomIcon face;

    public MTEHatchCreativeMaintenance(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEHatchCreativeMaintenance(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        return new String[] { CommonValues.THETA_MOVEMENT,
            translateToLocal("gt.blockmachines.debug.tt.maintenance.desc.0"), // For automatically maintaining
                                                                              // Multiblocks
            translateToLocal("gt.blockmachines.debug.tt.maintenance.desc.1"), // Does fix everything but itself.
            EnumChatFormatting.AQUA + translateToLocal("gt.blockmachines.debug.tt.maintenance.desc.2") // Fixing is
                                                                                                       // for plebs!
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        face = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_FULLAUTOMAINTENANCE");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(face) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(face) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchCreativeMaintenance(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        this.mWrench = this.mScrewdriver = this.mSoftMallet = this.mHardHammer = this.mCrowbar = this.mSolderingTool = true;
        aBaseMetaTileEntity.disableTicking();
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public void onMaintenancePerformed(MTEMultiBlockBase aMaintenanceTarget) {
        setMaintenanceSound(SoundResource.GT_MAINTENANCE_CREATIVE_HATCH, 1.0F, 1.0F);
        super.onMaintenancePerformed(aMaintenanceTarget);
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
}
