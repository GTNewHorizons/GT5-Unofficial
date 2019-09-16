package gregtech.api.metatileentity.implementations;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.enums.*;
import gregtech.api.gui.GT_Container_BasicMachine;
import gregtech.api.gui.GT_GUIContainer_BasicMachine;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Recipe
import gregtech.api.util.GT_Utility;
import ic2.core.Ic2Items;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

import java.util.Locale;
import java.util.Random;

import static gregtech.api.enums.GT_Values.*;

import gtPlusPlus.core.material.ALLOY;
import gtPlusPlus.core.material.ELEMENT;
import gtPlusPlus.core.material.Material;


/**
 * NEVER INCLUDE THIS FILE IN YOUR MOD!!!
 * <p/>
 * This is the main construct for my Basic Machines such as the Automatic Extractor
 * Extend this class to make a simple Machine
 */
public class GT_MetaTileEntity_BasicMachine_GT_Recipe extends GT_MetaTileEntity_BasicMachine {
    private final GT_Recipe.GT_Recipe_Map mRecipes;
    private final int mTankCapacity, mSpecialEffect;
    private final String mSound;
    private final boolean mSharedTank, mRequiresFluidForFiltering;
    private final byte mGUIParameterA, mGUIParameterB;

    public static String rotorTumbaga= "rotorTumbaga";
    public static String rotorPotin= "rotorPotin";
    public static String rotorEglinSteel= "rotorEglinSteel";
    public static String rotorZirconiumCarbide= "rotorZirconiumCarbide";
	public static String rotorIncoloy020= "rotorIncoloy020";
	public static String rotorStellite= "rotorStellite";
	public static String rotorNitinol60= "rotorNitinol60";
	public static String rotorArceusAlloy2B= "rotorArceusAlloy2B";
	public static String rotorCinobiteA243= "rotorCinobiteA243";
	public static String rotorAbyssalAlloy= "rotorAbyssalAlloy";
	public static String rotorQuantum= "rotorQuantum";
	
	public static String plateTumbaga= "plateTumbaga";
    public static String platePotin= "platePotin";
    public static String plateEglinSteel= "plateEglinSteel";
    public static String plateZirconiumCarbide= "plateZirconiumCarbide";
	public static String plateIncoloy020= "plateIncoloy020";
	public static String plateStellite= "plateStellite";
	public static String plateNitinol60= "plateNitinol60";
	public static String plateArceusAlloy2B= "plateArceusAlloy2B";
	public static String plateCinobiteA243= "plateCinobiteA243";
	public static String plateAbyssalAlloy= "plateAbyssalAlloy";
	public static String plateQuantum= "plateQuantum";
	
    public GT_MetaTileEntity_BasicMachine_GT_Recipe(int aID, String aName, String aNameRegional, int aTier, String aDescription, GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aGUIParameterA, int aGUIParameterB, String aGUIName, String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect, String aOverlays, Object[] aRecipe) {
        super(aID, aName, aNameRegional, aTier, aRecipes.mAmperage, aDescription, aInputSlots, aOutputSlots, aGUIName, aRecipes.mNEIName, new ITexture[]{new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_SIDE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_FRONT")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_TOP")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM_ACTIVE")), new GT_RenderedTexture(new Textures.BlockIcons.CustomIcon("basicmachines/" + aOverlays.toLowerCase(Locale.ENGLISH) + "/OVERLAY_BOTTOM"))});
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSound = aSound;
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;

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
                            if (Loader.isModLoaded("bartworks")) {
                                aRecipe[i] = "blockGlass"+VN[aTier];
                                break;
                            }
                        default:
                            if (Loader.isModLoaded("bartworks")) {
                                aRecipe[i] = "blockGlass"+VN[8];
                                break;
                            } else {
                                aRecipe[i] = Ic2Items.reinforcedGlass;
                                break;
                            }
                    }
                    continue;
                }

                if (GT_Mod.gregtechproxy.mGTPlusPlusHard) {
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.PLATE) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = "plateTumbaga";
                            break;
                        case 2:
                            aRecipe[i] = "platePotin";
                            break;
                        case 3:
                            aRecipe[i] = "plateEglinSteel";
                            break;
                        case 4:
                            aRecipe[i] = "plateZirconiumCarbide";
                            break;
                        case 5:
                            aRecipe[i] = "plateIncoloy020";
                            break;
                        case 6:
                            aRecipe[i] = "plateStellite";
                            break;
                        case 7:
                            aRecipe[i] = "plateNitinol60";
                            break;
                        case 8:
                            aRecipe[i] = "plateArceusAlloy2B";
                            break;
                        case 9:
                            aRecipe[i] = "plateCinobiteA243";
                            break;
                        case 10:
                            aRecipe[i] = "plateAbyssalAlloy";
                            break;
                        default:
                            aRecipe[i] = "plateQuantum";
                            break;
                    }
                    continue;
                }
                }else {
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
                            case 8:
                                aRecipe[i] = OrePrefixes.plate.get(Materials.Europium);
                                break;
                            case 9:
                                aRecipe[i] = OrePrefixes.plate.get(Materials.Neutronium);
                                break;
                            default:
                                aRecipe[i] = OrePrefixes.plate.get(Materials.Phoenixite);
                                break;
                        }
                        continue;
                    }
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
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.NiobiumTitanium);
                            break;
                        case 7:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Enderium);
                            break;
                        case 8:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Naquadah);
                            break;
                        case 9:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Infuscolium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.pipeMedium.get(Materials.Neutronium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Copper);
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
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.TungstenSteel);
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
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Europium);
                            break;
                        case 10:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Diamericiumtitanium);
                            break;
                        case 11:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Neutronium);
                            break;
                        case 12:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Quantium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.BlackPlutonium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.COIL_HEATING_DOUBLE) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Copper);
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
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.TungstenSteel);
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
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Europium);
                            break;
                        case 10:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Diamericiumtitanium);
                            break;
                        case 11:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Neutronium);
                            break;
                        case 12:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Quantium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.BlackPlutonium);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_DISTILLATION) {
                    switch (this.mTier) {
                    case 0:
                    case 1:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Copper);
                        break;
                    case 2:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Cupronickel);
                        break;
                    case 3:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Kanthal);
                        break;
                    case 4:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Nichrome);
                        break;
                    case 5:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.TungstenSteel);
                        break;
                    case 6:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.HSSG);
                        break;
                    case 7:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Naquadah);
                        break;
                    case 8:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.NaquadahAlloy);
                        break;
                    case 9:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Europium);
                        break;
                    case 10:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Diamericiumtitanium);
                        break;
                    case 11:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Neutronium);
                         break;
                    case 12:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.Quantium);
                        break;
                    default:
                        aRecipe[i] = OrePrefixes.spring.get(Materials.BlackPlutonium);
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
                            aRecipe[i] = OrePrefixes.stickLong.get(Materials.NeodymiumMagnetic);
                            break;
                        case 8:
                        case 9:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.EuropiumoxideMagnetic);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.stickLong.get(Materials.EuropiumoxideMagnetic);
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.STICK_ELECTROMAGNETIC) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Iron);
                            break;
                        case 2:
                        case 3:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Steel);
                            break;
                        case 4:
                        case 5:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.Neodymium);
                            break;
                        case 6:
                        case 7:
                            aRecipe[i] = OrePrefixes.stickLong.get(Materials.Neodymium);
                            break;
                        case 8:
                        case 9:
                            aRecipe[i] = OrePrefixes.stick.get(Materials.VanadiumGallium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.stickLong.get(Materials.VanadiumGallium);
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
                            aRecipe[i] = OrePrefixes.wireGt02.get(Materials.Copper);
                            break;
                        case 3:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Copper);
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
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Osmium);
                            break;
                        case 8:
                            aRecipe[i] = OrePrefixes.wireGt04.get(Materials.Europium);
                            break;
                        case 9:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.Diamericiumtitanium);
                            break;
                        case 10:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.Neutronium);
                            break;
                        case 11:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.Quantium);
                            break;
                        default:
                            aRecipe[i] = OrePrefixes.wireGt08.get(Materials.BlackPlutonium);
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
                        default:
                            aRecipe[i] = ItemList.Robot_Arm_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Electric_Pump_UEV;
                            break;
                    }
                    continue;
                }

                if (GT_Mod.gregtechproxy.mGTPlusPlusHard) {
                if (aRecipe[i] == GT_MetaTileEntity_BasicMachine_GT_Recipe.X.ROTOR) {
                    switch (this.mTier) {
                        case 0:
                        case 1:
                            aRecipe[i] = "rotorTumbaga";
                            break;
                        case 2:
                            aRecipe[i] = "rotorPotin";
                            break;
                        case 3:
                            aRecipe[i] = "rotorEglinSteel";
                            break;
                        case 4:
                            aRecipe[i] = "rotorZirconiumCarbide";
                            break;
                        case 5:
                            aRecipe[i] = "rotorIncoloy020";
                            break;
                        case 6:
                            aRecipe[i] = "rotorStellite";
                            break;
                        case 7:
                            aRecipe[i] = "rotorNitinol60";
                            break;
                        case 8:
                            aRecipe[i] = "rotorArceusAlloy2B";
                            break;
                        case 9:
                            aRecipe[i] = "rotorCinobiteA243";
                            break;
                        case 10:
                            aRecipe[i] = "rotorAbyssalAlloy";
                            break;
                        default:
                            aRecipe[i] = "rotorQuantum";
                            break;
                    }
                    continue;
                }
                }else {
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
                            case 8:
                                aRecipe[i] = OrePrefixes.rotor.get(Materials.Osmium);
                                break;
                            case 9:
                                aRecipe[i] = OrePrefixes.rotor.get(Materials.NaquadahAlloy);
                                break;
                            case 10:
                                aRecipe[i] = OrePrefixes.rotor.get(Materials.Neutronium);
                                break;
                            default:
                                aRecipe[i] = OrePrefixes.rotor.get(Materials.Phoenixite);
                                break;
                        }
                        continue;
                    }
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
                        default:
                            aRecipe[i] = ItemList.Electric_Motor_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Electric_Piston_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Conveyor_Module_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Emitter_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Sensor_UEV;
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
                        default:
                            aRecipe[i] = ItemList.Field_Generator_UEV;
                            break;
                    }
                    continue;
                }

                if (aRecipe[i] instanceof GT_MetaTileEntity_BasicMachine_GT_Recipe.X)
                    throw new IllegalArgumentException("MISSING TIER MAPPING FOR: " + aRecipe[i] + " AT TIER " + mTier);
            }

            if (!GT_ModHandler.addCraftingRecipe(getStackForm(1), GT_ModHandler.RecipeBits.DISMANTLEABLE | GT_ModHandler.RecipeBits.BUFFERED | GT_ModHandler.RecipeBits.NOT_REMOVABLE | GT_ModHandler.RecipeBits.REVERSIBLE, aRecipe)) {
                throw new IllegalArgumentException("INVALID CRAFTING RECIPE FOR: " + getStackForm(1).getDisplayName());
            }
        }
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(String aName, int aTier, String aDescription, GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage, int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName, String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots, aGUIName, aNEIName);
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSound = aSound;
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;
    }

    public GT_MetaTileEntity_BasicMachine_GT_Recipe(String aName, int aTier, String[] aDescription, GT_Recipe.GT_Recipe_Map aRecipes, int aInputSlots, int aOutputSlots, int aTankCapacity, int aAmperage, int aGUIParameterA, int aGUIParameterB, ITexture[][][] aTextures, String aGUIName, String aNEIName, String aSound, boolean aSharedTank, boolean aRequiresFluidForFiltering, int aSpecialEffect) {
        super(aName, aTier, aAmperage, aDescription, aTextures, aInputSlots, aOutputSlots, aGUIName, aNEIName);
        this.mSharedTank = aSharedTank;
        this.mTankCapacity = aTankCapacity;
        this.mSpecialEffect = aSpecialEffect;
        this.mRequiresFluidForFiltering = aRequiresFluidForFiltering;
        this.mRecipes = aRecipes;
        this.mSound = aSound;
        this.mGUIParameterA = (byte) aGUIParameterA;
        this.mGUIParameterB = (byte) aGUIParameterB;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_BasicMachine_GT_Recipe(this.mName, this.mTier, this.mDescriptionArray, this.mRecipes, this.mInputSlotCount, this.mOutputItems == null ? 0 : this.mOutputItems.length, this.mTankCapacity, this.mAmperage, this.mGUIParameterA, this.mGUIParameterB, this.mTextures, this.mGUIName, this.mNEIName, this.mSound, this.mSharedTank, this.mRequiresFluidForFiltering, this.mSpecialEffect);
    }

    @Override
    public Object getServerGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_Container_BasicMachine(aPlayerInventory, aBaseMetaTileEntity);
    }

    @Override
    public Object getClientGUI(int aID, InventoryPlayer aPlayerInventory, IGregTechTileEntity aBaseMetaTileEntity) {
        return new GT_GUIContainer_BasicMachine(aPlayerInventory, aBaseMetaTileEntity, this.getLocalName(), this.mGUIName, GT_Utility.isStringValid(this.mNEIName) ? this.mNEIName : this.getRecipeList() != null ? this.getRecipeList().mUnlocalizedName : "", this.mGUIParameterA, this.mGUIParameterB);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack)) return false;
        if (this.mInventory[aIndex] != null) return true;
        switch (this.mInputSlotCount) {
            case 0:
                return false;
            case 1:
                return this.getFillableStack() == null ? !this.mRequiresFluidForFiltering && this.getRecipeList().containsInput(aStack) : null != this.getRecipeList().findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, true, V[this.mTier], new FluidStack[]{this.getFillableStack()}, this.getSpecialSlot(), new ItemStack[]{aStack});
            case 2:
                return (!this.mRequiresFluidForFiltering || this.getFillableStack() != null) && (((this.getInputAt(0) != null && this.getInputAt(1) != null) || (this.getInputAt(0) == null && this.getInputAt(1) == null ? this.getRecipeList().containsInput(aStack) : (this.getRecipeList().containsInput(aStack) && null != this.getRecipeList().findRecipe(this.getBaseMetaTileEntity(), this.mLastRecipe, true, V[this.mTier], new FluidStack[]{this.getFillableStack()}, this.getSpecialSlot(), aIndex == this.getInputSlot() ? new ItemStack[]{aStack, this.getInputAt(1)} : new ItemStack[]{this.getInputAt(0), aStack})))));
            default:
                int tID = this.getBaseMetaTileEntity().getMetaTileID();
                if (tID >= 211 && tID <= 218 || tID >= 1180 && tID <= 1187 || tID >= 10783 && tID <= 10786 || tID >= 12080 && tID <= 12085){// assemblers IDs
                    if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, "circuitBasic")) return true; // allow input all LV-circuits for assemblers
                    if (GT_OreDictUnificator.isItemStackInstanceOf(aStack, "circuitAdvanced")) return true; // allow input all HV-circuits for assemblers
                }
                return this.getRecipeList().containsInput(aStack);

        }
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isClientSide() && aBaseMetaTileEntity.isActive()) {
            switch (this.mSpecialEffect) {
                case 0:
                    break;
                case 1:
                    if (aBaseMetaTileEntity.getFrontFacing() != 1 && aBaseMetaTileEntity.getCoverIDAtSide((byte) 1) == 0 && !aBaseMetaTileEntity.getOpacityAtSide((byte) 1)) {
                        Random tRandom = aBaseMetaTileEntity.getWorld().rand;
                        aBaseMetaTileEntity.getWorld().spawnParticle("smoke", aBaseMetaTileEntity.getXCoord() + 0.8F - tRandom.nextFloat() * 0.6F, aBaseMetaTileEntity.getYCoord() + 0.9F + tRandom.nextFloat() * 0.2F, aBaseMetaTileEntity.getZCoord() + 0.8F - tRandom.nextFloat() * 0.6F, 0.0D, 0.0D, 0.0D);
                    }
                    break;
            }
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
        if (aIndex == 1 && GT_Utility.isStringValid(this.mSound)) GT_Utility.doSoundAtClient(this.mSound, 100, 1.0F, aX, aY, aZ);
    }

    @Override
    public void startProcess() {
    	BaseMetaTileEntity myMetaTileEntity = ((BaseMetaTileEntity) this.getBaseMetaTileEntity());
        // Added to throttle sounds. To reduce lag, this is on the server side so BlockUpdate packets aren't sent.
        if (myMetaTileEntity.mTickTimer > (myMetaTileEntity.mLastSoundTick+ticksBetweenSounds)) {
            if (GT_Utility.isStringValid(this.mSound)) this.sendLoopStart((byte) 1);
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

    public enum X {PUMP, WIRE, WIRE4, HULL, PIPE, GLASS, PLATE, MOTOR, ROTOR, SENSOR, PISTON, CIRCUIT, EMITTER, CONVEYOR, ROBOT_ARM, COIL_HEATING, COIL_ELECTRIC, STICK_MAGNETIC, STICK_DISTILLATION, BETTER_CIRCUIT, FIELD_GENERATOR, COIL_HEATING_DOUBLE, STICK_ELECTROMAGNETIC}
}
