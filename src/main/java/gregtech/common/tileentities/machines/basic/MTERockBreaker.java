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
import static gregtech.api.recipe.RecipeMaps.rockBreakerFakeRecipes;
import static gregtech.api.util.GTRecipeBuilder.TICKS;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipeBuilder;
import gregtech.api.util.GTUtility;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class MTERockBreaker extends MTEBasicMachine {

    private static final Int2ObjectMap<Set<RockBreakerRecipe>> ROCK_BREAKER_RECIPES = new Int2ObjectOpenHashMap<>();

    public MTERockBreaker(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.ROCKBREAKER.tooltipDescription(),
            1,
            1,
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

    public MTERockBreaker(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 1);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERockBreaker(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return rockBreakerFakeRecipes;
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && getRecipeMap().containsInput(aStack);
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int checkRecipe() {
        IGregTechTileEntity aBaseMetaTileEntity = getBaseMetaTileEntity();

        Block topBlock = aBaseMetaTileEntity.getBlockOffset(0, 1, 0);
        Block bottomBlock = aBaseMetaTileEntity.getBlockOffset(0, -1, 0);
        Block[] sideBlocks = new Block[] { aBaseMetaTileEntity.getBlockOffset(0, 0, 1),
            aBaseMetaTileEntity.getBlockOffset(0, 0, -1), aBaseMetaTileEntity.getBlockOffset(-1, 0, 0),
            aBaseMetaTileEntity.getBlockOffset(1, 0, 0) };

        // Lock to only recipes with the specified circuit. Prevents "collisions" when a recipe
        // has the same in-world requirements but a differentiating item.
        ItemStack circuitStack = getStackInSlot(getCircuitSlot());
        int circuitNum = circuitStack == null ? -1 : circuitStack.getItemDamage();
        Set<RockBreakerRecipe> potentialRecipes = ROCK_BREAKER_RECIPES.get(circuitNum);
        if (potentialRecipes == null) {
            potentialRecipes = ROCK_BREAKER_RECIPES.get(-1); // fallback for circuit with no recipes
        }

        for (RockBreakerRecipe recipe : potentialRecipes) {
            if (!recipe.testInWorld(sideBlocks, topBlock, bottomBlock)) continue;
            if (!recipe.testInputs(getInputAt(0))) continue;

            // Found successful recipe
            ItemStack output = recipe.getOutputItem();
            if (canOutput(output)) {
                if (recipe.isInputConsumed()) {
                    getInputAt(0).stackSize -= 1;
                }
                calculateOverclockedNess((int) TierEU.RECIPE_LV, recipe.getDuration());
                // In case recipe is too OP for that machine
                if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
                    return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;
                this.mOutputItems[0] = output;
                return 2;
            }
        }

        return 0;
    }

    public static void addRockBreakerRecipe(UnaryOperator<RockBreakerRecipe.Builder> u) {
        RockBreakerRecipe recipe = u.apply(new RockBreakerRecipe.Builder())
            .build();
        // Organize by circuit for recipe differentiation
        ROCK_BREAKER_RECIPES.computeIfAbsent(recipe.circuit, $ -> new HashSet<>())
            .add(recipe);
    }

    public static class RockBreakerRecipe {

        private final Block topBlock, bottomBlock;
        private final Block[] sideBlocks, anywhereBlocks;
        private final int circuit;
        private final int duration;
        private final ItemStack inputItem;
        private final boolean inputConsumed;
        private final ItemStack outputItem;

        private RockBreakerRecipe(Block topBlock, Block bottomBlock, Block[] sideBlocks, Block[] anywhereBlocks,
            int circuit, int duration, ItemStack inputItem, boolean inputConsumed, ItemStack outputItem) {
            this.topBlock = topBlock;
            this.bottomBlock = bottomBlock;
            this.sideBlocks = sideBlocks;
            this.anywhereBlocks = anywhereBlocks;
            this.circuit = circuit;
            this.duration = duration;
            this.inputItem = inputItem;
            this.inputConsumed = inputConsumed;
            this.outputItem = outputItem;
        }

        public boolean testInWorld(Block[] sideBlocks, Block topBlock, Block bottomBlock) {
            if (this.topBlock != null && this.topBlock != topBlock) {
                return false;
            }
            if (this.bottomBlock != null && this.bottomBlock != bottomBlock) {
                return false;
            }

            if (this.sideBlocks != null) {
                for (Block sideBlock : this.sideBlocks) {
                    if (!ArrayUtils.contains(sideBlocks, sideBlock)) {
                        return false;
                    }
                }
            }

            if (this.anywhereBlocks != null) {
                for (Block anywhereBlock : this.anywhereBlocks) {
                    if (topBlock == anywhereBlock) continue;
                    if (bottomBlock == anywhereBlock) continue;
                    if (ArrayUtils.contains(sideBlocks, anywhereBlock)) continue;
                    return false;
                }
            }

            return true;
        }

        public boolean testInputs(ItemStack inputStack) {
            if (inputItem != null) {
                return GTUtility.areStacksEqual(inputStack, inputItem);
            }
            return true;
        }

        private int getDuration() {
            return this.duration;
        }

        private boolean isInputConsumed() {
            return this.inputConsumed;
        }

        private ItemStack getOutputItem() {
            return this.outputItem.copy();
        }

        public static class Builder {

            private Block topBlock, bottomBlock;
            private Block[] sideBlocks, anywhereBlocks;
            private int circuit = -1;
            private int duration = 16 * TICKS;
            private ItemStack inputItem;
            private boolean inputConsumed;
            private ItemStack outputItem;
            private String recipeDescription = "IT'S FREE! Place Lava on Side";

            /**
             * @param block Require a specific block above the Rock Breaker.
             */
            public Builder topBlock(Block block) {
                this.topBlock = block;
                return this;
            }

            /**
             * @param block Require a specific block below the Rock Breaker.
             */
            public Builder bottomBlock(Block block) {
                this.bottomBlock = block;
                return this;
            }

            /**
             * @param blocks Require specific blocks on the side of the Rock Breaker.
             */
            public Builder sideBlocks(Block... blocks) {
                this.sideBlocks = blocks;
                return this;
            }

            /**
             * @param blocks Require specific blocks on any side, top, or bottom of the Rock Breaker.
             */
            public Builder anywhereBlocks(Block... blocks) {
                this.anywhereBlocks = blocks;
                return this;
            }

            public Builder circuit(int number) {
                this.circuit = number;
                return this;
            }

            public Builder duration(int ticks) {
                this.duration = ticks;
                return this;
            }

            public Builder inputItem(ItemStack inputItem, boolean consumed) {
                this.inputItem = inputItem;
                this.inputConsumed = consumed;
                return this;
            }

            public Builder outputItem(ItemStack outputItem) {
                this.outputItem = outputItem;
                return this;
            }

            /**
             * @param desc A description to show in NEI if there are no recipe inputs. For example: "IT'S FREE! Place
             *             Lava on Side"
             */
            public Builder recipeDescription(String desc) {
                this.recipeDescription = desc;
                return this;
            }

            public RockBreakerRecipe build() {
                if (outputItem == null) {
                    throw new IllegalArgumentException("Rock Breaker Output Item must be set!");
                }
                if (sideBlocks != null && sideBlocks.length > 4) {
                    throw new IllegalArgumentException("Cannot have more than 4 Rock Breaker side blocks!");
                }
                if (anywhereBlocks != null && anywhereBlocks.length > 6) {
                    throw new IllegalArgumentException("Cannot have more than 6 Rock Breaker anywhere blocks!");
                }

                addFakeRecipe();

                return new RockBreakerRecipe(
                    topBlock,
                    bottomBlock,
                    sideBlocks,
                    anywhereBlocks,
                    circuit,
                    duration,
                    inputItem,
                    inputConsumed,
                    outputItem);
            }

            private void addFakeRecipe() {
                GTRecipeBuilder b = GTValues.RA.stdBuilder()
                    .itemOutputs(this.outputItem.copy())
                    .duration(this.duration)
                    .eut(TierEU.RECIPE_LV)
                    .ignoreCollision()
                    .fake();

                List<ItemStack> inputs = new ArrayList<>();
                if (this.inputItem != null) {
                    if (this.inputConsumed) {
                        inputs.add(this.inputItem.copy());
                    } else {
                        inputs.add(GTUtility.copyAmount(0, this.inputItem));
                    }
                } else {
                    // Add the "IT'S FREE" item
                    inputs.add(ItemList.Display_ITS_FREE.getWithName(1, this.recipeDescription));
                }
                b.itemInputs(inputs.toArray(new ItemStack[0]));
                if (this.circuit != -1) {
                    b.circuit(this.circuit);
                }
                b.addTo(rockBreakerFakeRecipes);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_FIRE;
    }

}
