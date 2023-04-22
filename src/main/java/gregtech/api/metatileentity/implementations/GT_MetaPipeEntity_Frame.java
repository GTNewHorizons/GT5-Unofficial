package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.RA;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.*;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gregtech.api.util.GT_OreDictUnificator;

public class GT_MetaPipeEntity_Frame extends MetaPipeEntity {

    private static final String localizedDescFormat = GT_LanguageManager
        .addStringLocalization("gt.blockmachines.gt_frame.desc.format", "Just something you can put covers on.");
    public final Materials mMaterial;

    public GT_MetaPipeEntity_Frame(int aID, String aName, String aNameRegional, Materials aMaterial) {
        super(aID, aName, aNameRegional, 0);
        mMaterial = aMaterial;

        GT_OreDictUnificator.registerOre(OrePrefixes.frameGt, aMaterial, getStackForm(1));
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GT_ModHandler.addCraftingRecipe(
                getStackForm(2),
                RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SSS", "SwS", "SSS", 'S', OrePrefixes.stick.get(mMaterial) });
        }

        if (!aMaterial.contains(SubTag.NO_RECIPES)) {
            // Auto generate frame box recipe in an assembler.
            RA.addAssemblerRecipe(
                GT_OreDictUnificator.get(OrePrefixes.stick, aMaterial, 4),
                ItemList.Circuit_Integrated.getWithDamage(0, 4),
                getStackForm(1),
                64,
                calculateRecipeEU(aMaterial, 7));
        }
    }

    public GT_MetaPipeEntity_Frame(String aName, Materials aMaterial) {
        super(aName, 0);
        mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mMaterial == null ? 4 : (byte) (4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaPipeEntity_Frame(mName, mMaterial);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection, int connections,
        int colorIndex, boolean active, boolean redstoneLevel) {
        return new ITexture[] { TextureFactory.of(
            mMaterial.mIconSet.mTextures[OrePrefixes.frameGt.mTextureIndex],
            Dyes.getModulation(colorIndex, mMaterial.mRGBa)) };
    }

    @Override
    public String[] getDescription() {
        return localizedDescFormat.split("\\R");
    }

    @Override
    public final boolean isSimpleMachine() {
        return true;
    }

    @Override
    public final boolean isFacingValid(ForgeDirection facing) {
        return false;
    }

    @Override
    public final boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public final boolean renderInside(ForgeDirection side) {
        return true;
    }

    @Override
    public final float getThickNess() {
        return 1.0F;
    }

    @Override
    public final void saveNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final void loadNBTData(NBTTagCompound aNBT) {
        /* Do nothing */
    }

    @Override
    public final boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public final boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public int connect(ForgeDirection side) {
        return 0;
    }

    @Override
    public void disconnect(ForgeDirection side) {
        /* Do nothing */
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return true;
    }
}
