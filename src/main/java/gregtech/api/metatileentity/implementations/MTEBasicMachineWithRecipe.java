package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.GTValues.W;
import static gregtech.api.enums.GTValues.ticksBetweenSounds;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;
import static net.minecraftforge.common.util.ForgeDirection.UP;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.enums.Tier;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.recipe.BasicUIProperties;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ExternalMaterials;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;

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
    private final ResourceLocation mSoundResourceLocation;
    private FallbackableUITexture progressBarTexture;
    private int recipeCatalystPriority;

    /**
     * Registers machine with single-line description, specific tank capacity, and sound specified by ResourceLocation.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, ResourceLocation aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
        this(
            aID,
            aName,
            aNameRegional,
            aTier,
            new String[] { aDescription },
            aRecipes,
            aInputSlots,
            aOutputSlots,
            aTankCapacity,
            aSound,
            aSpecialEffect,
            aOverlays,
            aRecipe);
    }

    /**
     * Registers machine with multi-line descriptions, specific tank capacity, and sound specified by ResourceLocation.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, ResourceLocation aSound,
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
        this.mSpecialEffect = aSpecialEffect;
        this.mRecipes = aRecipes;
        this.mSoundResourceLocation = aSound;
        this.progressBarTexture = mRecipes.getFrontend()
            .getUIProperties().progressBarTexture;

        // TODO: CHECK
        if (aRecipe != null) {
            for (int i = 3; i < aRecipe.length; i++) {
                if (!(aRecipe[i] instanceof X)) continue;

                // spotless:off
                aRecipe[i] = switch ((X) aRecipe[i]) {
                    case CIRCUIT            -> Tier.ELECTRIC[this.mTier].mManagingObject;
                    case BETTER_CIRCUIT     -> Tier.ELECTRIC[this.mTier].mBetterManagingObject;
                    case HULL               -> Tier.ELECTRIC[this.mTier].mHullObject;
                    case WIRE               -> Tier.ELECTRIC[this.mTier].mConductingObject;
                    case WIRE4              -> Tier.ELECTRIC[this.mTier].mLargerConductingObject;
                    case STICK_DISTILLATION -> OrePrefixes.stick.get(Materials.Blaze);

                    case GLASS -> switch (this.mTier) {
                        case 0, 1, 2, 3    -> new ItemStack(Blocks.glass, 1, W);
                        case 4, 5, 6, 7, 8 -> "blockGlass" + VN[aTier];
                        default            -> "blockGlass" + VN[8];
                    };

                    case PLATE -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.plate.get(Materials.Steel);
                        case 2    -> OrePrefixes.plate.get(Materials.Aluminium);
                        case 3    -> OrePrefixes.plate.get(Materials.StainlessSteel);
                        case 4    -> OrePrefixes.plate.get(Materials.Titanium);
                        case 5    -> OrePrefixes.plate.get(Materials.TungstenSteel);
                        case 6    -> OrePrefixes.plate.get(Materials.HSSG);
                        case 7    -> OrePrefixes.plate.get(Materials.HSSE);
                        default   -> OrePrefixes.plate.get(Materials.Neutronium);
                    };

                    case PIPE -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.pipeMedium.get(Materials.Bronze);
                        case 2    -> OrePrefixes.pipeMedium.get(Materials.Steel);
                        case 3    -> OrePrefixes.pipeMedium.get(Materials.StainlessSteel);
                        case 4    -> OrePrefixes.pipeMedium.get(Materials.Titanium);
                        case 5    -> OrePrefixes.pipeMedium.get(Materials.TungstenSteel);
                        case 6    -> OrePrefixes.pipeSmall.get(Materials.Ultimate);
                        case 7    -> OrePrefixes.pipeMedium.get(Materials.Ultimate);
                        case 8    -> OrePrefixes.pipeLarge.get(Materials.Ultimate);
                        default   -> OrePrefixes.pipeHuge.get(Materials.Ultimate);
                    };

                    case COIL_HEATING -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.wireGt02.get(Materials.AnyCopper);
                        case 2    -> OrePrefixes.wireGt02.get(Materials.Cupronickel);
                        case 3    -> OrePrefixes.wireGt02.get(Materials.Kanthal);
                        case 4    -> OrePrefixes.wireGt02.get(Materials.Nichrome);
                        case 5    -> OrePrefixes.wireGt02.get(Materials.TPV);
                        case 6    -> OrePrefixes.wireGt02.get(Materials.HSSG);
                        case 7    -> OrePrefixes.wireGt02.get(Materials.Naquadah);
                        case 8    -> OrePrefixes.wireGt02.get(Materials.NaquadahAlloy);
                        case 9    -> OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                        default   -> OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                    };

                    case COIL_HEATING_DOUBLE -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.wireGt04.get(Materials.AnyCopper);
                        case 2    -> OrePrefixes.wireGt04.get(Materials.Cupronickel);
                        case 3    -> OrePrefixes.wireGt04.get(Materials.Kanthal);
                        case 4    -> OrePrefixes.wireGt04.get(Materials.Nichrome);
                        case 5    -> OrePrefixes.wireGt04.get(Materials.TPV);
                        case 6    -> OrePrefixes.wireGt04.get(Materials.HSSG);
                        case 7    -> OrePrefixes.wireGt04.get(Materials.Naquadah);
                        case 8    -> OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                        case 9    -> OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                        default   -> OrePrefixes.wireGt16.get(Materials.NaquadahAlloy);
                    };

                    case STICK_MAGNETIC -> switch (this.mTier) {
                        case 0, 1       -> OrePrefixes.stick.get(Materials.IronMagnetic);
                        case 2, 3       -> OrePrefixes.stick.get(Materials.SteelMagnetic);
                        case 4, 5       -> OrePrefixes.stick.get(Materials.NeodymiumMagnetic);
                        case 6, 7, 8, 9 -> OrePrefixes.stick.get(Materials.SamariumMagnetic);
                        default         -> OrePrefixes.stick.get(Materials.TengamAttuned);
                    };

                    case STICK_ELECTROMAGNETIC -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.stick.get(Materials.AnyIron);
                        case 2, 3 -> OrePrefixes.stick.get(Materials.Steel);
                        case 4    -> OrePrefixes.stick.get(Materials.Neodymium);
                        default   -> OrePrefixes.stick.get(Materials.VanadiumGallium);
                    };

                    case COIL_ELECTRIC -> switch (this.mTier) {
                        case 0  -> OrePrefixes.wireGt01.get(Materials.Lead);
                        case 1  -> OrePrefixes.wireGt02.get(Materials.Tin);
                        case 2  -> OrePrefixes.wireGt02.get(Materials.AnyCopper);
                        case 3  -> OrePrefixes.wireGt04.get(Materials.AnyCopper);
                        case 4  -> OrePrefixes.wireGt08.get(Materials.AnnealedCopper);
                        case 5  -> OrePrefixes.wireGt16.get(Materials.AnnealedCopper);
                        case 6  -> OrePrefixes.wireGt04.get(Materials.YttriumBariumCuprate);
                        case 7  -> OrePrefixes.wireGt08.get(Materials.Iridium);
                        default -> OrePrefixes.wireGt16.get(Materials.Osmium);
                    };

                    case ROBOT_ARM -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Robot_Arm_LV;
                        case 2    -> ItemList.Robot_Arm_MV;
                        case 3    -> ItemList.Robot_Arm_HV;
                        case 4    -> ItemList.Robot_Arm_EV;
                        case 5    -> ItemList.Robot_Arm_IV;
                        case 6    -> ItemList.Robot_Arm_LuV;
                        case 7    -> ItemList.Robot_Arm_ZPM;
                        case 8    -> ItemList.Robot_Arm_UV;
                        case 9    -> ItemList.Robot_Arm_UHV;
                        case 10   -> ItemList.Robot_Arm_UEV;
                        case 11   -> ItemList.Robot_Arm_UIV;
                        case 12   -> ItemList.Robot_Arm_UMV;
                        case 13   -> ItemList.Robot_Arm_UXV;
                        default   ->  ItemList.Robot_Arm_MAX;
                    };

                    case PUMP -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Electric_Pump_LV;
                        case 2    -> ItemList.Electric_Pump_MV;
                        case 3    -> ItemList.Electric_Pump_HV;
                        case 4    -> ItemList.Electric_Pump_EV;
                        case 5    -> ItemList.Electric_Pump_IV;
                        case 6    -> ItemList.Electric_Pump_LuV;
                        case 7    -> ItemList.Electric_Pump_ZPM;
                        case 8    -> ItemList.Electric_Pump_UV;
                        case 9    -> ItemList.Electric_Pump_UHV;
                        case 10   -> ItemList.Electric_Pump_UEV;
                        case 11   -> ItemList.Electric_Pump_UIV;
                        case 12   -> ItemList.Electric_Pump_UMV;
                        case 13   -> ItemList.Electric_Pump_UXV;
                        default   -> ItemList.Electric_Pump_MAX;
                    };

                    case MOTOR -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Electric_Motor_LV;
                        case 2    -> ItemList.Electric_Motor_MV;
                        case 3    -> ItemList.Electric_Motor_HV;
                        case 4    -> ItemList.Electric_Motor_EV;
                        case 5    -> ItemList.Electric_Motor_IV;
                        case 6    -> ItemList.Electric_Motor_LuV;
                        case 7    -> ItemList.Electric_Motor_ZPM;
                        case 8    -> ItemList.Electric_Motor_UV;
                        case 9    -> ItemList.Electric_Motor_UHV;
                        case 10   -> ItemList.Electric_Motor_UEV;
                        case 11   -> ItemList.Electric_Motor_UIV;
                        case 12   -> ItemList.Electric_Motor_UMV;
                        case 13   -> ItemList.Electric_Motor_UXV;
                        default   -> ItemList.Electric_Motor_MAX;
                    };

                    case PISTON -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Electric_Piston_LV;
                        case 2    -> ItemList.Electric_Piston_MV;
                        case 3    -> ItemList.Electric_Piston_HV;
                        case 4    -> ItemList.Electric_Piston_EV;
                        case 5    -> ItemList.Electric_Piston_IV;
                        case 6    -> ItemList.Electric_Piston_LuV;
                        case 7    -> ItemList.Electric_Piston_ZPM;
                        case 8    -> ItemList.Electric_Piston_UV;
                        case 9    -> ItemList.Electric_Piston_UHV;
                        case 10   -> ItemList.Electric_Piston_UEV;
                        case 11   -> ItemList.Electric_Piston_UIV;
                        case 12   -> ItemList.Electric_Piston_UMV;
                        case 13   -> ItemList.Electric_Piston_UXV;
                        default   -> ItemList.Electric_Piston_MAX;
                    };

                    case CONVEYOR -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Conveyor_Module_LV;
                        case 2    -> ItemList.Conveyor_Module_MV;
                        case 3    -> ItemList.Conveyor_Module_HV;
                        case 4    -> ItemList.Conveyor_Module_EV;
                        case 5    -> ItemList.Conveyor_Module_IV;
                        case 6    -> ItemList.Conveyor_Module_LuV;
                        case 7    -> ItemList.Conveyor_Module_ZPM;
                        case 8    -> ItemList.Conveyor_Module_UV;
                        case 9    -> ItemList.Conveyor_Module_UHV;
                        case 10   -> ItemList.Conveyor_Module_UEV;
                        case 11   -> ItemList.Conveyor_Module_UIV;
                        case 12   -> ItemList.Conveyor_Module_UMV;
                        case 13   -> ItemList.Conveyor_Module_UXV;
                        default   -> ItemList.Conveyor_Module_MAX;
                    };

                    case EMITTER -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Emitter_LV;
                        case 2    -> ItemList.Emitter_MV;
                        case 3    -> ItemList.Emitter_HV;
                        case 4    -> ItemList.Emitter_EV;
                        case 5    -> ItemList.Emitter_IV;
                        case 6    -> ItemList.Emitter_LuV;
                        case 7    -> ItemList.Emitter_ZPM;
                        case 8    -> ItemList.Emitter_UV;
                        case 9    -> ItemList.Emitter_UHV;
                        case 10   -> ItemList.Emitter_UEV;
                        case 11   -> ItemList.Emitter_UIV;
                        case 12   -> ItemList.Emitter_UMV;
                        case 13   -> ItemList.Emitter_UXV;
                        default   -> ItemList.Emitter_MAX;
                    };

                    case SENSOR -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Sensor_LV;
                        case 2    -> ItemList.Sensor_MV;
                        case 3    -> ItemList.Sensor_HV;
                        case 4    -> ItemList.Sensor_EV;
                        case 5    -> ItemList.Sensor_IV;
                        case 6    -> ItemList.Sensor_LuV;
                        case 7    -> ItemList.Sensor_ZPM;
                        case 8    -> ItemList.Sensor_UV;
                        case 9    -> ItemList.Sensor_UHV;
                        case 10   -> ItemList.Sensor_UEV;
                        case 11   -> ItemList.Sensor_UIV;
                        case 12   -> ItemList.Sensor_UMV;
                        case 13   -> ItemList.Sensor_UXV;
                        default   -> ItemList.Sensor_MAX;
                    };

                    case FIELD_GENERATOR -> switch (this.mTier) {
                        case 0, 1 -> ItemList.Field_Generator_LV;
                        case 2    -> ItemList.Field_Generator_MV;
                        case 3    -> ItemList.Field_Generator_HV;
                        case 4    -> ItemList.Field_Generator_EV;
                        case 5    -> ItemList.Field_Generator_IV;
                        case 6    -> ItemList.Field_Generator_LuV;
                        case 7    -> ItemList.Field_Generator_ZPM;
                        case 8    -> ItemList.Field_Generator_UV;
                        case 9    -> ItemList.Field_Generator_UHV;
                        case 10   -> ItemList.Field_Generator_UEV;
                        case 11   -> ItemList.Field_Generator_UIV;
                        case 12   -> ItemList.Field_Generator_UMV;
                        case 13   -> ItemList.Field_Generator_UXV;
                        default   -> ItemList.Field_Generator_MAX;
                    };

                    case ROTOR -> switch (this.mTier) {
                        case 0, 1 -> OrePrefixes.rotor.get(Materials.Tin);
                        case 2    -> OrePrefixes.rotor.get(Materials.Bronze);
                        case 3    -> OrePrefixes.rotor.get(Materials.Steel);
                        case 4    -> OrePrefixes.rotor.get(Materials.StainlessSteel);
                        case 5    -> OrePrefixes.rotor.get(Materials.TungstenSteel);
                        case 6    -> OrePrefixes.rotor.get(ExternalMaterials.getRhodiumPlatedPalladium());
                        case 7    -> OrePrefixes.rotor.get(Materials.Iridium);
                        default   -> OrePrefixes.rotor.get(Materials.Osmium);
                    };

                    default -> throw new IllegalArgumentException("MISSING TIER MAPPING FOR: " + aRecipe[i] + " AT TIER " + this.mTier);
                };
                // spotless:on
            }

            if (!GTModHandler.addCraftingRecipe(
                getStackForm(1),
                GTModHandler.RecipeBits.DISMANTLEABLE | GTModHandler.RecipeBits.BUFFERED
                    | GTModHandler.RecipeBits.NOT_REMOVABLE
                    | GTModHandler.RecipeBits.REVERSIBLE,
                aRecipe)) {
                throw new IllegalArgumentException("INVALID CRAFTING RECIPE FOR: " + getStackForm(1).getDisplayName());
            }
        }
    }

    /**
     * Registers machine with single-line description, auto-scaled fluid tank, and sound specified by SoundResource.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, boolean usesFluids, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
            aSound.resourceLocation,
            aSpecialEffect,
            aOverlays,
            aRecipe);
    }

    /**
     * Registers machine with multi-line descriptions, auto-scaled fluid tank, and sound specified by SoundResource.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, boolean usesFluids, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
            aSound.resourceLocation,
            aSpecialEffect,
            aOverlays,
            aRecipe);
    }

    /**
     * Registers machine with single-line description, specific tank capacity, and sound specified by SoundResource.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
            aSound.resourceLocation,
            aSpecialEffect,
            aOverlays,
            aRecipe);
    }

    /**
     * Registers machine with multi-line descriptions, specific tank capacity, and sound specified by SoundResource.
     */
    public MTEBasicMachineWithRecipe(int aID, String aName, String aNameRegional, int aTier, String[] aDescription,
        RecipeMap<?> aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, SoundResource aSound,
        SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
            aSound.resourceLocation,
            aSpecialEffect,
            aOverlays,
            aRecipe);
    }

    /**
     * For {@link #newMetaEntity}.
     */
    public MTEBasicMachineWithRecipe(String aName, int aTier, String[] aDescription, RecipeMap<?> aRecipes,
        int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage, ITexture[][][] aTextures,
        ResourceLocation aSound, SpecialEffects aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots);
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRecipes = aRecipes;
        this.mSoundResourceLocation = aSound;
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
            this.mSoundResourceLocation,
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
                    .findRecipe(
                        this.getBaseMetaTileEntity(),
                        this.mLastRecipe,
                        true,
                        true,
                        V[this.mTier],
                        new FluidStack[] { this.getFillableStack() },
                        this.getSpecialSlot(),
                        appendSelectedCircuit(aStack))
                    != null;
            }
            case 2 -> {
                return ((this.getInputAt(0) != null && this.getInputAt(1) != null)
                    || (this.getInputAt(0) == null && this.getInputAt(1) == null ? this.getRecipeMap()
                        .containsInput(aStack)
                        : (this.getRecipeMap()
                            .containsInput(aStack)
                            && this.getRecipeMap()
                                .findRecipe(
                                    this.getBaseMetaTileEntity(),
                                    this.mLastRecipe,
                                    true,
                                    true,
                                    V[this.mTier],
                                    new FluidStack[] { this.getFillableStack() },
                                    this.getSpecialSlot(),
                                    aIndex == this.getInputSlot() ? appendSelectedCircuit(aStack, this.getInputAt(1))
                                        : appendSelectedCircuit(this.getInputAt(0), aStack))
                                != null)));
            }
            default -> {
                int tID = this.getBaseMetaTileEntity()
                    .getMetaTileID();
                if (tID >= 211 && tID <= 218 || tID >= 1180 && tID <= 1187 || tID >= 10780 && tID <= 10786) { // assembler
                    // lv-iv;
                    // circuit
                    // asseblers
                    // lv -
                    // uv;
                    // assemblers
                    // luv-uev
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
                    if (aBaseMetaTileEntity.getFrontFacing() != UP && aBaseMetaTileEntity.getCoverIDAtSide(UP) == 0
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
                        && aBaseMetaTileEntity.getCoverIDAtSide(mainFacing) == 0
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
    public int getCapacity() {
        return this.mTankCapacity;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1 && this.mSoundResourceLocation != null
            && GTUtility.isStringValid(this.mSoundResourceLocation.getResourceDomain())
            && GTUtility.isStringValid(this.mSoundResourceLocation.getResourcePath()))
            GTUtility.doSoundAtClient(this.mSoundResourceLocation, 100, 1.0F, aX, aY, aZ);
    }

    @Override
    public void startProcess() {
        BaseMetaTileEntity myMetaTileEntity = ((BaseMetaTileEntity) this.getBaseMetaTileEntity());
        // Added to throttle sounds. To reduce lag, this is on the server side so BlockUpdate packets aren't sent.
        if (myMetaTileEntity.mTickTimer > (myMetaTileEntity.mLastSoundTick + ticksBetweenSounds)) {
            if (this.mSoundResourceLocation != null
                && GTUtility.isStringValid(this.mSoundResourceLocation.getResourceDomain())
                && GTUtility.isStringValid(this.mSoundResourceLocation.getResourcePath())) this.sendLoopStart((byte) 1);
            // Does not have overflow protection, but they are longs.
            myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
        }
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
