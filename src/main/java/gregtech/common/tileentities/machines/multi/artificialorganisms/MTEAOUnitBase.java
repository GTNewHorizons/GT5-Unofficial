package gregtech.common.tileentities.machines.multi.artificialorganisms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.factory.artificialorganisms.MTEHatchAO;
import gregtech.api.gui.modularui.GUITextureSet;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.AORecipeData;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public abstract class MTEAOUnitBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    protected MTEHatchAO bioHatch;

    protected int AOsInUse = 0;

    protected MTEAOUnitBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEAOUnitBase(String aName) {
        super(aName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

                AORecipeData data = recipe.getMetadata(GTRecipeConstants.AO_DATA);
                // If there is no AO data on this recipe, skip AO logic. Multiblocks like the bio lab use
                // recipemaps with no AO data and should implement their own logic.
                if (data == null) {
                    return super.validateRecipe(recipe);
                }

                ArtificialOrganism currentOrganism = getAO();
                if (currentOrganism == null) return SimpleCheckRecipeResult.ofFailure("missing_ao");
                else if (currentOrganism.getCount() <= data.requiredCount)
                    return SimpleCheckRecipeResult.ofFailure("insufficient_ao");
                else if (currentOrganism.getIntelligence() <= data.requiredIntelligence)
                    return SimpleCheckRecipeResult.ofFailure("ao_too_stupid");

                setSpeedBonus(currentOrganism.calculateSpeedBonus());

                doAORecipeModifiers(recipe);

                AOsInUse = Math
                    .round((float) currentOrganism.consumeAOs(data.requiredCount) * (100 - data.dangerLevel) / 100F);
                return super.validateRecipe(recipe);
            }
        };
    }

    /**
     * Called during validateRecipe so AO multis can easily do modifications to the recipe.
     */
    public void doAORecipeModifiers(@NotNull GTRecipe recipe) {

    }

    @Override
    public void onRecipeEnd() {
        ArtificialOrganism currentOrganism = getAO();
        if (currentOrganism != null) {
            currentOrganism.increaseSentience(10);
            currentOrganism.replenishAOs(AOsInUse);
            AOsInUse = 0;
        }
        super.onRecipeEnd();
    }

    protected ArtificialOrganism getAO() {
        if (bioHatch != null) return bioHatch.getNetwork()
            .getSpecies();
        return null;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (bioHatch != null) {
            if (bioHatch.getNetwork()
                .getSpecies() != null) {
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Current AO: " + bioHatch.getNetwork()
                        .getSpecies()
                        .toString());
            }
        }
    }

    protected boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchAO) {
                ((MTEHatchAO) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (bioHatch == null) {
                    bioHatch = (MTEHatchAO) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public GUITextureSet getGUITextureSet() {
        return GUITextureSet.ORGANIC;
    }
}
