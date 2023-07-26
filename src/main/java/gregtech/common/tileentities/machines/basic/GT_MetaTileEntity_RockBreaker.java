package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_BOTTOM_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_SIDE_ROCK_BREAKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOP_ROCK_BREAKER_GLOW;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_RockBreaker extends GT_MetaTileEntity_BasicMachine {

    public GT_MetaTileEntity_RockBreaker(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            "Put Lava and Water adjacent",
            1,
            1,
            "RockBreaker.png",
            "",
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_SIDE_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_SIDE_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_FRONT_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_TOP_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_TOP_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_ROCK_BREAKER_ACTIVE_GLOW)
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(OVERLAY_BOTTOM_ROCK_BREAKER),
                TextureFactory.builder()
                    .addIcon(OVERLAY_BOTTOM_ROCK_BREAKER_GLOW)
                    .glow()
                    .build()));
    }

    public GT_MetaTileEntity_RockBreaker(String aName, int aTier, String aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    public GT_MetaTileEntity_RockBreaker(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures,
        String aGUIName, String aNEIName) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1, aGUIName, aNEIName);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_RockBreaker(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mTextures,
            this.mGUIName,
            this.mNEIName);
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return GT_Recipe.GT_Recipe_Map.sRockBreakerFakeRecipes;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && getRecipeList().containsInput(aStack);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int checkRecipe() {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();
        if ((aBaseMetaTileEntity.getBlockOffset(0, 0, 1) == Blocks.water)
            || (aBaseMetaTileEntity.getBlockOffset(0, 0, -1) == Blocks.water)
            || (aBaseMetaTileEntity.getBlockOffset(-1, 0, 0) == Blocks.water)
            || (aBaseMetaTileEntity.getBlockOffset(1, 0, 0) == Blocks.water)) {
            ItemStack tOutput = null;
            if (aBaseMetaTileEntity.getBlockOffset(0, 1, 0) == Blocks.lava) {
                tOutput = new ItemStack(Blocks.stone, 1);
            } else if ((aBaseMetaTileEntity.getBlockOffset(0, 0, 1) == Blocks.lava)
                || (aBaseMetaTileEntity.getBlockOffset(0, 0, -1) == Blocks.lava)
                || (aBaseMetaTileEntity.getBlockOffset(-1, 0, 0) == Blocks.lava)
                || (aBaseMetaTileEntity.getBlockOffset(1, 0, 0) == Blocks.lava)) {
                    tOutput = new ItemStack(Blocks.cobblestone, 1);
                }
            if (tOutput != null) {
                if (GT_Utility.areStacksEqual(getStackInSlot(getCircuitSlot()), GT_Utility.getIntegratedCircuit(1))) {
                    if (GT_Utility.areStacksEqual(
                        getInputAt(0),
                        GT_OreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L))) {
                        tOutput = new ItemStack(Blocks.obsidian, 1);
                        if (canOutput(tOutput)) {
                            getInputAt(0).stackSize -= 1;
                            calculateOverclockedNess(30, 128);
                            // In case recipe is too OP for that machine
                            if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                                return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                            this.mOutputItems[0] = tOutput;
                            return 2;
                        }
                    }
                } else if (canOutput(tOutput)) {
                    calculateOverclockedNess(30, 16);
                    // In case recipe is too OP for that machine
                    if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                        return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                    this.mOutputItems[0] = tOutput;
                    return 2;
                }
            }
        }
        return 0;
    }
}
