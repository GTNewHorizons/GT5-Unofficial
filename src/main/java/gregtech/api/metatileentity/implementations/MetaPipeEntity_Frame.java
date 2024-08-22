package gregtech.api.metatileentity.implementations;

import static gregtech.api.recipe.RecipeMaps.assemblerRecipes;
import static gregtech.api.util.GT_Utility.calculateRecipeEU;
import static gregtech.api.util.RecipeBuilder.SECONDS;
import static gregtech.api.util.RecipeBuilder.TICKS;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_ModHandler.RecipeBits;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.OreDictUnificator;

public class MetaPipeEntity_Frame extends MetaPipeEntity {

    private static final String localizedDescFormat = GT_LanguageManager
        .addStringLocalization("gt.blockmachines.gt_frame.desc.format", "Just something you can put covers on.");
    public final Materials mMaterial;

    public MetaPipeEntity_Frame(int aID, String aName, String aNameRegional, Materials aMaterial) {
        super(aID, aName, aNameRegional, 0);
        mMaterial = aMaterial;

        OreDictUnificator.registerOre(OrePrefixes.frameGt, aMaterial, getStackForm(1));
        if (aMaterial.getProcessingMaterialTierEU() < TierEU.IV) {
            GT_ModHandler.addCraftingRecipe(
                getStackForm(2),
                RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.BUFFERED,
                new Object[] { "SSS", "SwS", "SSS", 'S', OrePrefixes.stick.get(mMaterial) });
        }

        if (!aMaterial.contains(SubTag.NO_RECIPES) && OreDictUnificator.get(OrePrefixes.stick, aMaterial, 1) != null) {
            // Auto generate frame box recipe in an assembler.
            GT_Values.RA.stdBuilder()
                .itemInputs(OreDictUnificator.get(OrePrefixes.stick, aMaterial, 4), GT_Utility.getIntegratedCircuit(4))
                .itemOutputs(getStackForm(1))
                .duration(3 * SECONDS + 4 * TICKS)
                .eut(calculateRecipeEU(aMaterial, 7))
                .addTo(assemblerRecipes);
        }
    }

    public MetaPipeEntity_Frame(String aName, Materials aMaterial) {
        super(aName, 0);
        mMaterial = aMaterial;
    }

    @Override
    public byte getTileEntityBaseType() {
        return (byte) (mMaterial == null ? 4 : (byte) (4) + Math.max(0, Math.min(3, mMaterial.mToolQuality)));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MetaPipeEntity_Frame(mName, mMaterial);
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
