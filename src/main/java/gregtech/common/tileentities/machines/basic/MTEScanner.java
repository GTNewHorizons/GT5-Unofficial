package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_SCANNER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_SCANNER_GLOW;
import static gregtech.api.recipe.RecipeMaps.scannerFakeRecipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.GTMod;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.IGTScannerHandler;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTScannerResult;
import gregtech.api.util.GTUtility;

public class MTEScanner extends MTEBasicMachine {

    public MTEScanner(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.SCANNER.tooltipDescription(),
            1,
            1,
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_SCANNER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_SCANNER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_SCANNER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_SCANNER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_SCANNER_GLOW)
                    .glow()
                    .build()));
    }

    public MTEScanner(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEScanner(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    private IGTScannerHandler mLastSuccesfulHandler = null;

    @Override
    public int checkRecipe() {
        // too full to do anything
        if (this.getOutputAt(0) != null) {
            this.mOutputBlocked += 1;
            return 0;
        }
        // filter out bad input
        ItemStack input = this.getInputAt(0);
        if (GTUtility.isStackInvalid(input) || input.stackSize <= 0) {
            return 0;
        }
        // null special if bad
        ItemStack specialSlot = this.getSpecialSlot();
        if (GTUtility.isStackInvalid(specialSlot) || specialSlot.stackSize <= 0) {
            specialSlot = null;
        }
        // null fluid if bad
        FluidStack fluid = this.getFillableStack();
        if (fluid == null || fluid.amount <= 0 || fluid.getFluid() == null) {
            fluid = null;
        }

        // check handlers
        GTScannerResult result = RecipeMaps.scannerHandlers
            .findRecipeWithCache(this.mLastSuccesfulHandler, this, input, specialSlot, fluid);
        // abort if no recipe found
        if (result == null) {
            return DID_NOT_FIND_RECIPE;
        }
        // abort if req were not met internally.
        if (result.isNotMet()) return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        // check if oc fails
        calculateOverclockedNess(result.eut, result.duration);
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        // consume
        input.stackSize -= result.inputConsume;
        if (specialSlot != null) specialSlot.stackSize -= result.specialConsume;
        if (fluid != null) fluid.amount -= result.fluidConsume;
        this.mOutputItems[0] = result.output;
        // return code
        return FOUND_AND_SUCCESSFULLY_USED_RECIPE;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (mProgresstime >= (mMaxProgresstime - 1)) {
            if ((this.mOutputItems[0] != null) && (this.mOutputItems[0].getUnlocalizedName()
                .equals("gt.metaitem.01.32707"))) {
                GTMod.achievements.issueAchievement(
                    aBaseMetaTileEntity.getWorld()
                        .getPlayerEntityByName(aBaseMetaTileEntity.getOwnerName()),
                    "scanning");
            }
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return scannerFakeRecipes;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && getRecipeMap().containsInput(aStack);
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1) {
            GTUtility.doSoundAtClient(SoundResource.GTCEU_OP_PORTABLE_SCANNER, 10, 1.0F, aX, aY, aZ);
        }
    }

    @Override
    public void startProcess() {
        sendLoopStart((byte) 1);
    }
}
