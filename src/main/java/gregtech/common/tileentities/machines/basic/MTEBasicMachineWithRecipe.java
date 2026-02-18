package gregtech.common.tileentities.machines.basic;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLER_IV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_LuV;
import static gregtech.api.enums.MetaTileEntityIDs.ASSEMBLING_MACHINE_UMV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_LV;
import static gregtech.api.enums.MetaTileEntityIDs.CIRCUIT_ASSEMBLER_UV;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Locale;

import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import gregtech.common.gui.modularui.singleblock.base.MTEBasicMachineWithRecipeBaseGui;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public class MTEBasicMachineWithRecipe extends MTEBasicMachine {

    private final RecipeMap<?> mRecipes;
    private final int mTankCapacity;
    private final SpecialEffects mSpecialEffect;
    private final SoundResource mSoundResource;
    private FallbackableUITexture progressBarTexture;
    private int recipeCatalystPriority;

    /**
     * Registers machine with multi-line descriptions, specific tank capacity, and sound specified by SoundResource.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            aRecipes.getAmperage(),
            aDescription,
            aInputSlots,
            aOutputSlots,
            TextureFactory.of(
                TextureFactory.of(
                    new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE_ACTIVE_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT_ACTIVE_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP_ACTIVE_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory.of(
                    new CustomIcon(
                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM_ACTIVE")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM_ACTIVE_GLOW")))
                    .glow()
                    .build()),
            TextureFactory.of(
                TextureFactory
                    .of(new CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM")),
                TextureFactory.builder()
                    .addIcon(
                        (new CustomIcon(
                            "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM_GLOW")))
                    .glow()
                    .build()));
        this.mTankCapacity = aTankCapacity;
        this.mSoundResource = aSound;
        this.mSpecialEffect = aSpecialEffect;
        this.mRecipes = aRecipes;
        this.progressBarTexture = mRecipes.getFrontend()
            .getUIProperties().progressBarTexture;

        GTModHandler.addMachineCraftingRecipe(getStackForm(1), aRecipe, this.mTier);
    }

    /**
     * Registers machine with multi-line descriptions, auto-scaled fluid tank, and sound specified by SoundResource. Has
     * no recipe.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, boolean usesFluids, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays) {
        this(
            aID,
            aName,
            aNameRegional,
            aTier,
            aDescription,
            aRecipes,
            aInputSlots,
            aOutputSlots,
            usesFluids ? getCapacityForTier(aTier) : 0,
            aSound,
            aSpecialEffect,
            aOverlays,
            null);
    }

    /**
     * Registers machine with multi-line descriptions, specific tank capacity, and sound specified by SoundResource. has
     * no recipe.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays) {
        this(
            aID,
            aName,
            aNameRegional,
            aTier,
            aDescription,
            aRecipes,
            aInputSlots,
            aOutputSlots,
            aTankCapacity,
            aSound,
            aSpecialEffect,
            aOverlays,
            null);
    }

    /**
     * For {@link #newMetaEntity}.
     */
    public MTEBasicMachineWithRecipe(String aName, int aTier, String[] aDescription, RecipeMap<?> aRecipes,
        int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage, ITexture[][][] aTextures,
        SoundResource aSound, SpecialEffects aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots);
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRecipes = aRecipes;
        this.mSoundResource = aSound;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBasicMachineWithRecipe(
            this.mName,
            this.mTier,
            this.mDescriptionArray,
            this.mRecipes,
            this.mInputSlotCount,
            this.mOutputItems == null ? 0 : this.mOutputItems.length,
            this.mTankCapacity,
            this.mAmperage,
            this.mTextures,
            this.mSoundResource,
            this.mSpecialEffect).setProgressBarTexture(this.progressBarTexture)
                .setRecipeCatalystPriority(this.recipeCatalystPriority);
    }

    public MTEBasicMachineWithRecipe setProgressBarTexture(FallbackableUITexture progressBarTexture) {
        this.progressBarTexture = progressBarTexture;
        return this;
    }

    public MTEBasicMachineWithRecipe setProgressBarTextureName(String name, UITexture fallback) {
        return setProgressBarTexture(GTUITextures.fallbackableProgressbar(name, fallback));
    }

    public MTEBasicMachineWithRecipe setProgressBarTextureName(String name) {
        return setProgressBarTextureName(name, GTUITextures.PROGRESSBAR_ARROW);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, side, aStack)) return false;
        switch (this.mInputSlotCount) {
            case 0 -> {
                return false;
            }
            case 1 -> {
                if (this.getFillableStack() == null) return this.getRecipeMap()
                    .containsInput(aStack);
                else return this.getRecipeMap()
                    .findRecipeQuery()
                    .items(appendSelectedCircuit(aStack))
                    .fluids(this.getFillableStack())
                    .specialSlot(this.getSpecialSlot())
                    .voltage(V[this.mTier])
                    .cachedRecipe(this.mLastRecipe)
                    .dontCheckStackSizes(true)
                    .notUnificated(true)
                    .find() != null;
            }
            case 2 -> {
                return ((this.getInputAt(0) != null && this.getInputAt(1) != null)
                    || (this.getInputAt(0) == null && this.getInputAt(1) == null ? this.getRecipeMap()
                        .containsInput(aStack)
                        : (this.getRecipeMap()
                            .containsInput(aStack)
                            && this.getRecipeMap()
                                .findRecipeQuery()
                                .items(
                                    aIndex == this.getInputSlot() ? appendSelectedCircuit(aStack, this.getInputAt(1))
                                        : appendSelectedCircuit(this.getInputAt(0), aStack))
                                .fluids(this.getFillableStack())
                                .specialSlot(this.getSpecialSlot())
                                .voltage(V[this.mTier])
                                .cachedRecipe(this.mLastRecipe)
                                .dontCheckStackSizes(true)
                                .notUnificated(true)
                                .find() != null)));
            }
            default -> {
                int tID = this.getBaseMetaTileEntity()
                    .getMetaTileID();
                if (tID >= ASSEMBLER_LV.ID && tID <= ASSEMBLER_IV.ID
                    || tID >= CIRCUIT_ASSEMBLER_LV.ID && tID <= CIRCUIT_ASSEMBLER_UV.ID
                    || tID >= ASSEMBLING_MACHINE_LuV.ID && tID <= ASSEMBLING_MACHINE_UMV.ID) { // assembler
                    // lv-iv;
                    // circuit
                    // asseblers
                    // lv -
                    // uv;
                    // assemblers
                    // luv-umv
                    if (GTUtility.isStackValid(aStack)) for (int oreID : OreDictionary.getOreIDs(aStack)) {
                        if (OreDictionary.getOreName(oreID)
                            .startsWith("circuit")) return true;
                    }
                }
                return this.getRecipeMap()
                    .containsInput(aStack);
            }
        }
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {
            // noinspection SwitchStatementWithTooFewBranches
            switch (this.mSpecialEffect) {
                case TOP_SMOKE -> {
                    if (aBaseMetaTileEntity.getFrontFacing() != UP && !aBaseMetaTileEntity.hasCoverAtSide(UP)
                        && !aBaseMetaTileEntity.getOpacityAtSide(UP)) {

                        new ParticleEventBuilder().setMotion(0.0D, 0.0D, 0.0D)
                            .setIdentifier(ParticleFX.SMOKE)
                            .setPosition(
                                aBaseMetaTileEntity.getXCoord() + 0.8F - XSTR_INSTANCE.nextFloat() * 0.6F,
                                aBaseMetaTileEntity.getYCoord() + 0.9F + XSTR_INSTANCE.nextFloat() * 0.2F,
                                aBaseMetaTileEntity.getZCoord() + 0.8F - XSTR_INSTANCE.nextFloat() * 0.6F)
                            .setWorld(aBaseMetaTileEntity.getWorld())
                            .run();
                    }
                }
                default -> {}
            }
        }
    }

    /**
     * Handles {@link Block#randomDisplayTick} driven Special Effects
     *
     * @param aBaseMetaTileEntity The entity that will handle the {@see Block#randomDisplayTick}
     */
    @SideOnly(Side.CLIENT)
    @Override
    public void onRandomDisplayTick(IGregTechTileEntity aBaseMetaTileEntity) {

        // noinspection SwitchStatementWithTooFewBranches
        switch (this.mSpecialEffect) {
            case MAIN_RANDOM_SPARKS -> {
                // Random Sparkles at main face
                if (aBaseMetaTileEntity.isActive() && XSTR_INSTANCE.nextInt(3) == 0) {

                    final ForgeDirection mainFacing = this.mMainFacing;

                    if ((mainFacing.flag & (ForgeDirection.UP.flag | ForgeDirection.DOWN.flag)) == 0
                        && !aBaseMetaTileEntity.hasCoverAtSide(mainFacing)
                        && !aBaseMetaTileEntity.getOpacityAtSide(mainFacing)) {

                        final double oX = aBaseMetaTileEntity.getXCoord();
                        final double oY = aBaseMetaTileEntity.getYCoord();
                        final double oZ = aBaseMetaTileEntity.getZCoord();
                        final double offset = 0.02D;
                        final double horizontal = 0.5D + XSTR_INSTANCE.nextFloat() * 8D / 16D - 4D / 16D;

                        final double x, y, z, mX, mZ;

                        y = oY + XSTR_INSTANCE.nextFloat() * 10D / 16D + 5D / 16D;

                        if (mainFacing == ForgeDirection.WEST) {
                            x = oX - offset;
                            mX = -.05D;
                            z = oZ + horizontal;
                            mZ = 0D;
                        } else if (mainFacing == ForgeDirection.EAST) {
                            x = oX + offset;
                            mX = .05D;
                            z = oZ + horizontal;
                            mZ = 0D;
                        } else if (mainFacing == ForgeDirection.NORTH) {
                            x = oX + horizontal;
                            mX = 0D;
                            z = oZ - offset;
                            mZ = -.05D;
                        } else // if (frontFacing == ForgeDirection.SOUTH.ordinal())
                        {
                            x = oX + horizontal;
                            mX = 0D;
                            z = oZ + offset;
                            mZ = .05D;
                        }

                        ParticleEventBuilder particleEventBuilder = (new ParticleEventBuilder()).setMotion(mX, 0, mZ)
                            .setPosition(x, y, z)
                            .setWorld(getBaseMetaTileEntity().getWorld());
                        particleEventBuilder.setIdentifier(ParticleFX.LAVA)
                            .run();
                    }
                }
            }
            default -> {}
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return this.mRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return recipeCatalystPriority;
    }

    public MTEBasicMachineWithRecipe setRecipeCatalystPriority(int recipeCatalystPriority) {
        this.recipeCatalystPriority = recipeCatalystPriority;
        return this;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEBasicMachineWithRecipeBaseGui(this, this.getUIProperties()).build(data, syncManager, uiSettings);
    }

    @Override
    public int getCapacity() {
        return this.mTankCapacity;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return mSoundResource;
    }

    @Override
    protected BasicUIProperties getUIProperties() {
        return super.getUIProperties().toBuilder()
            .progressBarTexture(progressBarTexture)
            .build();
    }

    public enum X {
        PUMP,
        WIRE,
        WIRE4,
        HULL,
        PIPE,
        GLASS,
        PLATE,
        MOTOR,
        ROTOR,
        SENSOR,
        PISTON,
        CIRCUIT,
        EMITTER,
        CONVEYOR,
        ROBOT_ARM,
        COIL_HEATING,
        COIL_ELECTRIC,
        STICK_MAGNETIC,
        STICK_DISTILLATION,
        BETTER_CIRCUIT,
        FIELD_GENERATOR,
        COIL_HEATING_DOUBLE,
        STICK_ELECTROMAGNETIC
    }

    /**
     * Special Effects
     */
    public enum SpecialEffects {

        NONE,
        TOP_SMOKE,
        MAIN_RANDOM_SPARKS;

        static final SpecialEffects[] VALID_SPECIAL_EFFECTS = { NONE, TOP_SMOKE, MAIN_RANDOM_SPARKS };

        static SpecialEffects fromId(int id) {
            return id >= 0 && id < VALID_SPECIAL_EFFECTS.length ? VALID_SPECIAL_EFFECTS[id] : NONE;
        }
    }
}
