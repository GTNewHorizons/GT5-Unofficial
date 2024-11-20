package gregtech.common.tileentities.machines.multi.artificialorganisms;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.ArtificialOrganism;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.recipe.AORecipeData;
import gregtech.common.tileentities.machines.multi.artificialorganisms.hatches.MTEHatchBioInput;
import gtPlusPlus.core.util.minecraft.PlayerUtils;

public abstract class MTEAOUnitBase<T extends MTEExtendedPowerMultiBlockBase<T>>
    extends MTEExtendedPowerMultiBlockBase<T> {

    protected MTEHatchBioInput bioHatch;

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
                if (data == null) return CheckRecipeResultRegistry.NO_RECIPE;

                ArtificialOrganism currentOrganism = getAO();
                if (currentOrganism == null) return SimpleCheckRecipeResult.ofFailure("missing_ao");
                else if (currentOrganism.getCount() <= data.requiredCount)
                    return SimpleCheckRecipeResult.ofFailure("insufficient_ao");
                else if (currentOrganism.getIntelligence() <= data.requiredIntelligence)
                    return SimpleCheckRecipeResult.ofFailure("ao_too_stupid");

                setSpeedBonus(currentOrganism.calculateSpeedBonus());

                AOsInUse = currentOrganism.consumeAOs(data.requiredCount);
                return super.validateRecipe(recipe);
            }

            @Override
            public ProcessingLogic clear() {
                ArtificialOrganism currentOrganism = getAO();
                if (currentOrganism != null) {
                    currentOrganism.increaseSentience(1);
                    currentOrganism.replenishAOs(AOsInUse);
                    AOsInUse = 0;
                }
                return super.clear();
            }
        };
    }

    protected ArtificialOrganism getAO() {
        if (bioHatch != null) return bioHatch.getAO();
        return null;
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (bioHatch != null) {
            if (bioHatch.getAO() != null) {
                PlayerUtils.messagePlayer(
                    aPlayer,
                    "Current AO: " + bioHatch.getAO()
                        .toString());
            }
        }
    }

    protected boolean addBioHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof MTEHatchBioInput) {
                ((MTEHatchBioInput) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                if (bioHatch == null) {
                    bioHatch = (MTEHatchBioInput) aMetaTileEntity;
                    return true;
                }
            }
        }
        return false;
    }
}
