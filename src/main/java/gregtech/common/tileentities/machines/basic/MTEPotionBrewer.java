package gregtech.common.tileentities.machines.basic;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.MachineType;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;

public class MTEPotionBrewer extends MTEBasicMachine {

    public MTEPotionBrewer(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            1,
            MachineType.BREWERY.tooltipDescription(),
            1,
            0,
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_SIDE_POTIONBREWER_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_SIDE_POTIONBREWER_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_SIDE_POTIONBREWER")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_SIDE_POTIONBREWER_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_FRONT_POTIONBREWER_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_FRONT_POTIONBREWER_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_FRONT_POTIONBREWER")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_FRONT_POTIONBREWER_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_TOP_POTIONBREWER_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_TOP_POTIONBREWER_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_TOP_POTIONBREWER")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_TOP_POTIONBREWER_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    Textures.BlockIcons
                        .customOptional("basicmachines/potionbrewer/OVERLAY_BOTTOM_POTIONBREWER_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_BOTTOM_POTIONBREWER_ACTIVE_GLOW"))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(Textures.BlockIcons.customOptional("basicmachines/potionbrewer/OVERLAY_BOTTOM_POTIONBREWER")),
                TextureFactory.builder()
                    .addIcon(
                        Textures.BlockIcons
                            .customOptional("basicmachines/potionbrewer/OVERLAY_BOTTOM_POTIONBREWER_GLOW"))
                    .glow()
                    .build()));
    }

    public MTEPotionBrewer(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures, 1, 0);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPotionBrewer(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.brewingRecipes;
    }

    @Override
    public int checkRecipe() {
        int tCheck = super.checkRecipe();
        if (tCheck != DID_NOT_FIND_RECIPE) {
            return tCheck;
        }

        calculateOverclockedNess(4, 128);
        // In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return FOUND_RECIPE_BUT_DID_NOT_MEET_REQUIREMENTS;

        FluidStack aFluid = getFillableStack();
        if ((getDrainableStack() == null) && (aFluid != null) && (getInputAt(0) != null)) {
            String tInputName = aFluid.getFluid()
                .getName();
            if (tInputName.startsWith("potion.")) {
                tInputName = tInputName.replaceFirst("potion.", "");
                int tFirstDot = tInputName.indexOf('.') + 1;
                String tModifier = tFirstDot <= 0 ? "" : tInputName.substring(tFirstDot);
                if (!tModifier.isEmpty()) {
                    tInputName = tInputName.replaceFirst("." + tModifier, "");
                }
                if (GTUtility.areStacksEqual(new ItemStack(Items.fermented_spider_eye, 1, 0), getInputAt(0))) {
                    return switch (tInputName) {
                        case "poison", "health", "waterbreathing" -> setOutput("potion.damage" + tModifier);
                        case "nightvision" -> setOutput("potion.invisibility" + tModifier);
                        case "fireresistance", "speed" -> setOutput("potion.slowness" + tModifier);
                        case "strength" -> setOutput("potion.weakness" + tModifier);
                        case "regen" -> setOutput("potion.poison" + tModifier);
                        default -> setOutput("potion.weakness");
                    };
                }
                if (GTUtility.areStacksEqual(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Glowstone, 1L),
                    getInputAt(0))) {
                    if (!tModifier.startsWith("strong")) {
                        return setOutput(
                            "potion." + tInputName + ".strong" + (tModifier.isEmpty() ? "" : "." + tModifier));
                    }
                    if (tModifier.startsWith("long")) {
                        return setOutput("potion." + tInputName + tModifier.replaceFirst("long", ""));
                    }
                    return setOutput("potion.thick");
                }
                if (GTUtility
                    .areStacksEqual(GTOreDictUnificator.get(OrePrefixes.dust, Materials.Redstone, 1L), getInputAt(0))) {
                    if (!tModifier.startsWith("long")) {
                        return setOutput(
                            "potion." + tInputName + ".long" + (tModifier.isEmpty() ? "" : "." + tModifier));
                    }
                    if (tModifier.startsWith("strong")) {
                        return setOutput("potion." + tInputName + tModifier.replaceFirst("strong", ""));
                    }
                    return setOutput("potion.mundane");
                }
                if (GTUtility.areStacksEqual(
                    GTOreDictUnificator.get(OrePrefixes.dust, Materials.Gunpowder, 1L),
                    getInputAt(0))) {
                    if (!tInputName.endsWith(".splash")) {
                        return setOutput("potion." + tInputName + ".splash");
                    }
                    return setOutput("potion.mundane");
                }
            }
        }
        return 0;
    }

    private int setOutput(String aFluidName) {
        if (getFillableStack().amount < 750) {
            return 0;
        }

        this.mOutputFluid = FluidRegistry.getFluidStack(aFluidName, 750);
        if (this.mOutputFluid == null) {
            this.mOutputFluid = FluidRegistry.getFluidStack("potion.mundane", getFillableStack().amount);
        }

        getInputAt(0).stackSize -= 1;
        getFillableStack().amount -= 750;
        return 2;
    }

    @Override
    public boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)
            && getRecipeMap().containsInput(aStack);
    }

    @Override
    public boolean isFluidInputAllowed(FluidStack aFluid) {
        return (aFluid.getFluid()
            .getName()
            .startsWith("potion.")) || (super.isFluidInputAllowed(aFluid));
    }

    @Override
    public int getCapacity() {
        return getCapacityForTier(mTier);
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GTCEU_LOOP_CHEMICAL;
    }

}
