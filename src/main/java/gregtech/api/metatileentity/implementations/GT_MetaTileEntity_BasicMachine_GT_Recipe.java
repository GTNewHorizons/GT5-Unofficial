package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.GT_Values.W;
import static gregtech.api.enums.GT_Values.ticksBetweenSounds;
import static gregtech.api.enums.ModIDs.BartWorks;
import static gregtech.api.objects.XSTR.XSTR_INSTANCE;

import java.util.Locale;

import net.minecraft.block.Block;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import com.gtnewhorizons.modularui.api.drawable.FallbackableUITexture;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.*;
import gregtech.api.enums.ParticleFX;
import gregtech.api.enums.Textures.BlockIcons.CustomIcon;
import gregtech.api.gui.GT_Container_BasicMachine;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.WorldSpawnedEventBuilder;
import gregtech.api.util.WorldSpawnedEventBuilder.ParticleEventBuilder;
import ic2.core.Ic2Items;

/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor Extend this class to make a simple
 * Machine
 */
public class GT_MetaTileEntity_BasicMachine_GT_Recipe extends GT_MetaTileEntity_BasicMachine {

    private final GT_Recipe.GT_Recipe_Map mRecipes;
    private final int mTankCapacity;
    private final SpecialEffects mSpecialEffect;
    private final ResourceLocation mSoundResourceLocation;
    private final boolean mSharedTank, mRequiresFluidForFiltering;
    private final byte mGUIParameterA, mGUIParameterB;
    private FallbackableUITexture progressBarTexture;

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(int aID, String aName, String aNameRegional, int aTier,
            String aDescription, GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity,
            int aGUIParameterA, int aGUIParameterB, String aGUIName, ResourceLocation aSound, boolean aSharedTank,
            boolean aRequiresFluidForFiltering, SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
        super(
                aID,
                aName,
                aNameRegional,
                aTier,
                aRecipes.mAmperage,
                aDescription,
                aInputSlots,
                aOutputSlots,
                aGUIName,
                aRecipes.mNEIName,
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                + "/OVERLAY_SIDE_ACTIVE")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_SIDE_ACTIVE_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_SIDE_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                + "/OVERLAY_FRONT_ACTIVE")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_FRONT_ACTIVE_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_FRONT_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                + "/OVERLAY_TOP_ACTIVE")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_TOP_ACTIVE_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_TOP_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                + "/OVERLAY_BOTTOM_ACTIVE")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_BOTTOM_ACTIVE_GLOW")))
                                      .glow()
                                      .build()),
                TextureFactory.of(
                        TextureFactory.of(
                                new CustomIcon(
                                        "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM")),
                        TextureFactory.builder()
                                      .addIcon(
                                              (new CustomIcon(
                                                      "basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH)
                                                              + "/OVERLAY_BOTTOM_GLOW")))
                                      .glow()
                                      .build()));
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSoundResourceLocation = aSound;
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;
        this.progressBarTexture = mRecipes.getProgressBarTextureRaw();

        // TODO: CHECK
        if (aRecipe != null) {
            for (int i = 3; i < aRecipe.length; i++) {
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CIRCUIT) {
                    aRecipe[i] = Tier.ELECTRIC[this.mTier].mManagingObject;
                    continue;
                }
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.BETTER_CIRCUIT) {
                    aRecipe[i] = Tier.ELECTRIC[this.mTier].mBetterManagingObject;
                    continue;
                }
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.HULL) {
                    aRecipe[i] = Tier.ELECTRIC[this.mTier].mHullObject;
                    continue;
                }
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE) {
                    aRecipe[i] = Tier.ELECTRIC[this.mTier].mConductingObject;
                    continue;
                }
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.WIRE4) {
                    aRecipe[i] = Tier.ELECTRIC[this.mTier].mLargerConductingObject;
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.GLASS) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                        case 2:
                        case 3:
                            aRecipe[i] = new ItemStack(Blocks.glass, 1, W);
                            break;
                        case 4:
                        case 5:
                        case 6:
                        case 7:
                        case 8:
                            if (BartWorks.isModLoaded()) { // todo remove via provider pattern on all enums?
                                aRecipe[i] = "blockGlass" + VN[aTier];
                                break;
                            }
                        default:
                            if (BartWorks.isModLoaded()) { // todo remove via provider pattern on all enums?
                                aRecipe[i] = "blockGlass" + VN[8];
                            } else {
                                aRecipe[i] = Ic2Items.reinforcedGlass;
                            }
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.Steel);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.Aluminium);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.StainlessSteel);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.Titanium);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.TungstenSteel);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.HSSG);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.HSSE);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.plate.get(Materials.Neutronium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PIPE) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Bronze);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Steel);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.StainlessSteel);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Titanium);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.TungstenSteel);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.pipeSmall.get(Materials.Ultimate);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Ultimate);
                            break;
                        case 8:
                            aRecipe[i] = OrePrefixes.pipeLarge.get(Materials.Ultimate);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.pipeHuge.get(Materials.Ultimate);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.AnyCopper);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Cupronickel);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Kanthal);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Nichrome);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.TPV);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.HSSG);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Naquadah);
                            break;
                        case 8:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.NaquadahAlloy);
                            break;
                        case 9:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.AnyCopper);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Cupronickel);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Kanthal);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Nichrome);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.TPV);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.HSSG);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Naquadah);
                            break;
                        case 8:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.NaquadahAlloy);
                            break;
                        case 9:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.NaquadahAlloy);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt16.get(Materials.NaquadahAlloy);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION) {
                    switch (this.mTier) {
                        default:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Blaze);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_MAGNETIC) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.IronMagnetic);
                            break;
                        case 2:
                        case 3:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.SteelMagnetic);
                            break;
                        case 4:
                        case 5:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.NeodymiumMagnetic);
                            break;
                        case 6:
                        case 7:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.SamariumMagnetic);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.stickLong.get(Materials.SamariumMagnetic);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.AnyIron);
                            break;
                        case 2:
                        case 3:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Steel);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Neodymium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.VanadiumGallium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_ELECTRIC) {
                    switch (this.mTier) {
                        case 0:
                            aRecipe[i] = OrePrefixes.wireGt01.get(Materials.Lead);
                            break;
                        case 1:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Tin);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.AnyCopper);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.AnyCopper);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.AnnealedCopper);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.wireGt16.get(Materials.AnnealedCopper);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.YttriumBariumCuprate);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.Iridium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt16.get(Materials.Osmium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROBOT_ARM) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Robot_Arm_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Robot_Arm_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Robot_Arm_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Robot_Arm_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Robot_Arm_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Robot_Arm_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Robot_Arm_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Robot_Arm_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Robot_Arm_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Robot_Arm_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Robot_Arm_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Robot_Arm_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Robot_Arm_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Robot_Arm_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Robot_Arm_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PUMP) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Electric_Pump_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Electric_Pump_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Electric_Pump_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Electric_Pump_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Electric_Pump_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Electric_Pump_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Electric_Pump_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Electric_Pump_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Electric_Pump_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Electric_Pump_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Electric_Pump_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Electric_Pump_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Electric_Pump_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Electric_Pump_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Electric_Pump_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Tin);
                            break;
                        case 2:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Bronze);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Steel);
                            break;
                        case 4:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.StainlessSteel);
                            break;
                        case 5:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.TungstenSteel);
                            break;
                        case 6:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Chrome);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Iridium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.rotor.get(Materials.Osmium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.MOTOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Electric_Motor_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Electric_Motor_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Electric_Motor_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Electric_Motor_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Electric_Motor_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Electric_Motor_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Electric_Motor_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Electric_Motor_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Electric_Motor_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Electric_Motor_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Electric_Motor_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Electric_Motor_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Electric_Motor_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Electric_Motor_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Electric_Motor_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PISTON) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Electric_Piston_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Electric_Piston_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Electric_Piston_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Electric_Piston_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Electric_Piston_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Electric_Piston_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Electric_Piston_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Electric_Piston_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Electric_Piston_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Electric_Piston_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Electric_Piston_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Electric_Piston_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Electric_Piston_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Electric_Piston_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Electric_Piston_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.CONVEYOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Conveyor_Module_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Conveyor_Module_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Conveyor_Module_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Conveyor_Module_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Conveyor_Module_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Conveyor_Module_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Conveyor_Module_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Conveyor_Module_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Conveyor_Module_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Conveyor_Module_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Conveyor_Module_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Conveyor_Module_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Conveyor_Module_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Conveyor_Module_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Conveyor_Module_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.EMITTER) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Emitter_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Emitter_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Emitter_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Emitter_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Emitter_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Emitter_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Emitter_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Emitter_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Emitter_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Emitter_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Emitter_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Emitter_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Emitter_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Emitter_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Emitter_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.SENSOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Sensor_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Sensor_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Sensor_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Sensor_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Sensor_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Sensor_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Sensor_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Sensor_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Sensor_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Sensor_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Sensor_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Sensor_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Sensor_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Sensor_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Sensor_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.FIELD_GENERATOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = ItemList.Field_Generator_LV;
                            break;
                        case 2:
                            aRecipe[i] = ItemList.Field_Generator_MV;
                            break;
                        case 3:
                            aRecipe[i] = ItemList.Field_Generator_HV;
                            break;
                        case 4:
                            aRecipe[i] = ItemList.Field_Generator_EV;
                            break;
                        case 5:
                            aRecipe[i] = ItemList.Field_Generator_IV;
                            break;
                        case 6:
                            aRecipe[i] = ItemList.Field_Generator_LuV;
                            break;
                        case 7:
                            aRecipe[i] = ItemList.Field_Generator_ZPM;
                            break;
                        case 8:
                            aRecipe[i] = ItemList.Field_Generator_UV;
                            break;
                        case 9:
                            aRecipe[i] = ItemList.Field_Generator_UHV;
                            break;
                        case 10:
                            aRecipe[i] = ItemList.Field_Generator_UEV;
                            break;
                        case 11:
                            aRecipe[i] = ItemList.Field_Generator_UIV;
                            break;
                        case 12:
                            aRecipe[i] = ItemList.Field_Generator_UMV;
                            break;
                        case 13:
                            aRecipe[i] = ItemList.Field_Generator_UXV;
                            break;
                        case 14:
                            aRecipe[i] = ItemList.Field_Generator_MAX;
                            break;
                        default:
                            aRecipe[i] = ItemList.Field_Generator_MAX;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] instanceof GT_MetaTileEntity_BasicMachine_GT_Recipe.X)
                    throw new IllegalArgumentException("MISSING TIER MAPPING FOR: " + aRecipe[i] + " AT TIER " + mTier);
            }

            if (!GT_ModHandler.addCraftingRecipe(
                    getStackForm(1),
                    GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.BUFFERED
                            | GT_ModHandler.RecipeBits.NOT_REMOVABLE
                            | GT_ModHandler.RecipeBits.REVERSIBLE,
                    aRecipe)) {
                throw new IllegalArgumentException("INVALID CRAFTING RECIPE FOR: " + getStackForm(1).getDisplayName());
            }
        }
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(int aID, String aName, String aNameRegional, int aTier,
            String aDescription, GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity,
            int aGUIParameterA, int aGUIParameterB, String aGUIName, SoundResource aSound, boolean aSharedTank,
            boolean aRequiresFluidForFiltering, SpecialEffects aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
                aGUIParameterA,
                aGUIParameterB,
                aGUIName,
                aSound.resourceLocation,
                aSharedTank,
                aRequiresFluidForFiltering,
                aSpecialEffect,
                aOverlays,
                aRecipe);
    }

    /**
     * @inheritDoc
     * @deprecated Use {@link #GT_MetaTileEntity_BasicMachine_GT_Recipe( int aID, String, String, int, String,
     *             GT_Recipe.GT_Recipe_Map, int, int, int, int, int, String, ResourceLocation, boolean, boolean,
     *             SpecialEffects, String , Object[])}
     */
    @Deprecated
    public GT_MetaTileEntity_BasicMachine_GT_Recipe(int aID, String aName, String aNameRegional, int aTier,
            String aDescription, GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity,
            int aGUIParameterA, int aGUIParameterB, String aGUIName, String aSound, boolean aSharedTank,
            boolean aRequiresFluidForFiltering, int aSpecialEffect, String aOverlays, Object[] aRecipe) {
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
                aGUIParameterA,
                aGUIParameterB,
                aGUIName,
                new ResourceLocation(aSound),
                aSharedTank,
                aRequiresFluidForFiltering,
                SpecialEffects.fromId(aSpecialEffect),
                aOverlays,
                aRecipe);
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(String aName, int aTier, String aDescription,
            GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage,
            int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName,
            String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots, aGUIName, aNEIName);
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = SpecialEffects.fromId(aSpecialEffect);
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSoundResourceLocation = new ResourceLocation(aSound);
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(String aName, int aTier, String[] aDescription,
            GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage,
            int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName,
            String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots, aGUIName, aNEIName);
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = SpecialEffects.fromId(aSpecialEffect);
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSoundResourceLocation = new ResourceLocation(aSound);
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BasicMachine_GT_Recipe(
                this.mName,
                this.mTier,
                this.mDescriptionArray,
                this.mRecipes,
                this.mInputSlotCount,
                this.mOutputItems == null ? 0 : this.mOutputItems.length,
                this.mTankCapacity,
                this.mAmperage,
                this.mGUIParameterA,
                this.mGUIParameterB,
                this.mTextures,
                this.mGUIName,
                this.mNEIName,
                this.mSoundResourceLocation.toString(),
                this.mSharedTank,
                this.mRequiresFluidForFiltering,
                this.mSpecialEffect.ordinal()).setProgressBarTexture(this.progressBarTexture);
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe setProgressBarTexture(FallbackableUITexture progressBarTexture) {
        this.progressBarTexture = progressBarTexture;
        return this;
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe setProgressBarTextureName(String name, UITexture fallback) {
        return setProgressBarTexture(
                new FallbackableUITexture(UITexture.fullImage("gregtech", "gui/progressbar/" + name), fallback));
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe setProgressBarTextureName(String name) {
        return setProgressBarTextureName(name, GT_UITextures.PROGRESSBAR_ARROW);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_BasicMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicMachine(
                aPlayerInventory,
                aBaseMetaTileEntity,
                this.getLocalName(),
                this.mGUIName,
                GT_Utility.isStringValid(this.mNEIName) ? this.mNEIName
                        : this.getRecipeList() != null ? this.getRecipeList().mUnlocalizedName : "",
                this.mGUIParameterA,
                this.mGUIParameterB);
    }

    @Override
    protected boolean allowPutStackValidated(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide,
            ItemStack aStack) {
        if (!super.allowPutStackValidated(aBaseMetaTileEntity, aIndex, aSide, aStack)) return false;
        switch (this.mInputSlotCount) {
            case 0:
                return false;
            case 1:
                if (this.getFillableStack() == null) return !this.mRequiresFluidForFiltering && this.getRecipeList()
                                                                                                    .containsInput(
                                                                                                            aStack);
                else return this.getRecipeList()
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
            case 2:
                return (!this.mRequiresFluidForFiltering || this.getFillableStack() != null) && (((this.getInputAt(0)
                        != null && this.getInputAt(1) != null)
                        || (this.getInputAt(0) == null && this.getInputAt(1) == null ? this.getRecipeList()
                                                                                           .containsInput(aStack)
                                : (this.getRecipeList()
                                       .containsInput(aStack)
                                        && this.getRecipeList()
                                               .findRecipe(
                                                       this.getBaseMetaTileEntity(),
                                                       this.mLastRecipe,
                                                       true,
                                                       true,
                                                       V[this.mTier],
                                                       new FluidStack[] { this.getFillableStack() },
                                                       this.getSpecialSlot(),
                                                       aIndex == this.getInputSlot()
                                                               ? appendSelectedCircuit(aStack, this.getInputAt(1))
                                                               : appendSelectedCircuit(this.getInputAt(0), aStack))
                                                != null))));
            default: {
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
                    if (GT_Utility.isStackValid(aStack)) for (int oreID : OreDictionary.getOreIDs(aStack)) {
                        if (OreDictionary.getOreName(oreID)
                                         .startsWith("circuit"))
                            return true;
                    }
                }
                return this.getRecipeList()
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
                case TOP_SMOKE:
                    final byte topFacing = (byte) ForgeDirection.UP.ordinal();

                    if (aBaseMetaTileEntity.getFrontFacing() != topFacing
                            && aBaseMetaTileEntity.getCoverIDAtSide(topFacing) == 0
                            && !aBaseMetaTileEntity.getOpacityAtSide(topFacing)) {

                        new WorldSpawnedEventBuilder.ParticleEventBuilder().setMotion(0.0D, 0.0D, 0.0D)
                                                                           .setIdentifier(ParticleFX.SMOKE)
                                                                           .setPosition(
                                                                                   aBaseMetaTileEntity.getXCoord()
                                                                                           + 0.8F
                                                                                           - XSTR_INSTANCE.nextFloat()
                                                                                                   * 0.6F,
                                                                                   aBaseMetaTileEntity.getYCoord()
                                                                                           + 0.9F
                                                                                           + XSTR_INSTANCE.nextFloat()
                                                                                                   * 0.2F,
                                                                                   aBaseMetaTileEntity.getZCoord()
                                                                                           + 0.8F
                                                                                           - XSTR_INSTANCE.nextFloat()
                                                                                                   * 0.6F)
                                                                           .setWorld(aBaseMetaTileEntity.getWorld())
                                                                           .run();
                    }
                    break;
                default:
                    break;
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
            case MAIN_RANDOM_SPARKS:
                // Random Sparkles at main face
                if (aBaseMetaTileEntity.isActive() && XSTR_INSTANCE.nextInt(3) == 0) {

                    final byte mainFacing = (byte) this.mMainFacing;

                    if (mainFacing > 1 && aBaseMetaTileEntity.getCoverIDAtSide(mainFacing) == 0
                            && !aBaseMetaTileEntity.getOpacityAtSide(mainFacing)) {

                        final double oX = aBaseMetaTileEntity.getXCoord();
                        final double oY = aBaseMetaTileEntity.getYCoord();
                        final double oZ = aBaseMetaTileEntity.getZCoord();
                        final double offset = 0.02D;
                        final double horizontal = 0.5D + XSTR_INSTANCE.nextFloat() * 8D / 16D - 4D / 16D;

                        final double x, y, z, mX, mZ;

                        y = oY + XSTR_INSTANCE.nextFloat() * 10D / 16D + 5D / 16D;

                        if (mainFacing == ForgeDirection.WEST.ordinal()) {
                            x = oX - offset;
                            mX = -.05D;
                            z = oZ + horizontal;
                            mZ = 0D;
                        } else if (mainFacing == ForgeDirection.EAST.ordinal()) {
                            x = oX + offset;
                            mX = .05D;
                            z = oZ + horizontal;
                            mZ = 0D;
                        } else if (mainFacing == ForgeDirection.NORTH.ordinal()) {
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
                                                                                                .setWorld(
                                                                                                        getBaseMetaTileEntity().getWorld());
                        particleEventBuilder.setIdentifier(ParticleFX.LAVA)
                                            .run();
                    }
                }
                break;
            default:
                break;
        }
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeList() {
        return this.mRecipes;
    }

    @Override
    public int getCapacity() {
        return this.mTankCapacity;
    }

    @Override
    public void startSoundLoop(byte aIndex, double aX, double aY, double aZ) {
        super.startSoundLoop(aIndex, aX, aY, aZ);
        if (aIndex == 1 && this.mSoundResourceLocation != null
                && GT_Utility.isStringValid(this.mSoundResourceLocation.getResourceDomain())
                && GT_Utility.isStringValid(this.mSoundResourceLocation.getResourcePath()))
            GT_Utility.doSoundAtClient(this.mSoundResourceLocation, 100, 1.0F, aX, aY, aZ);
    }

    @Override
    public void startProcess() {
        BaseMetaTileEntity myMetaTileEntity = ((BaseMetaTileEntity) this.getBaseMetaTileEntity());
        // Added to throttle sounds. To reduce lag, this is on the server side so BlockUpdate packets aren't sent.
        if (myMetaTileEntity.mTickTimer > (myMetaTileEntity.mLastSoundTick + ticksBetweenSounds)) {
            if (this.mSoundResourceLocation != null
                    && GT_Utility.isStringValid(this.mSoundResourceLocation.getResourceDomain())
                    && GT_Utility.isStringValid(this.mSoundResourceLocation.getResourcePath()))
                this.sendLoopStart((byte) 1);
            // Does not have overflow protection, but they are longs.
            myMetaTileEntity.mLastSoundTick = myMetaTileEntity.mTickTimer;
        }
    }

    @Override
    public FluidStack getFillableStack() {
        return this.mSharedTank ? this.getDrainableStack() : super.getFillableStack();
    }

    @Override
    public FluidStack setFillableStack(FluidStack aFluid) {
        return this.mSharedTank ? this.setDrainableStack(aFluid) : super.setFillableStack(aFluid);
    }

    @Override
    protected boolean displaysOutputFluid() {
        return !this.mSharedTank;
    }

    @Override
    protected ProgressBar createProgressBar(UITexture texture, int imageSize, ProgressBar.Direction direction,
            Pos2d pos, Size size) {
        return super.createProgressBar(texture, imageSize, direction, pos, size).setTexture(
                progressBarTexture.get(),
                mRecipes.getProgressBarImageSize());
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
