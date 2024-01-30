package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorBlueWeabo;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Input;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Muffler;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.objects.ItemData;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PCBFactoryUpgrade;
import gregtech.api.recipe.metadata.PCBFactoryUpgradeKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_ModHandler;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_ParallelHelper;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings8;

@SuppressWarnings("SpellCheckingInspection")
public class GT_MetaTileEntity_PCBFactory extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_PCBFactory> implements ISurvivalConstructable {

    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final String tier3 = "tier3";
    private static final String bioUpgrade = "bioUpgrade";
    private static final String ocTier1Upgrade = "ocTier1Upgrade";
    private static final String ocTier2Upgrade = "ocTier2Upgrade";
    private float mRoughnessMultiplier = 1;
    private int mTier = 1, mSetTier = 1, mUpgradesInstalled = 0, mCurrentParallel = 0, mMaxParallel = 0;
    private boolean mBioUpgrade = false, mBioRotate = false, mOCTier1 = false, mOCTier2 = false;
    private final int[] mBioOffsets = new int[] { -5, -1 };
    private final int[] mOCTier1Offsets = new int[] { 2, -11 };
    private final int[] mOCTier2Offsets = new int[] { 2, -11 };
    private GT_MetaTileEntity_Hatch_Input mCoolantInputHatch;
    private static final int mBioRotateBitMap = 0b1000000;
    private static final int mOCTier2BitMap = 0b100000;
    private static final int mOCTier1BitMap = 0b10000;
    private static final int mBioBitMap = 0b1000;
    private static final int mTier3BitMap = 0b100;
    private static final int mTier2BitMap = 0b10;
    private static final int mTier1BitMap = 0b1;
    private static final int COOLANT_CONSUMED_PER_SEC = 10;
    private static final IStructureDefinition<GT_MetaTileEntity_PCBFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_PCBFactory>builder()
        .addShape(
            tier1,
            transpose(
                new String[][] {
                    // spotless:off
                        {"       ","E     E","E     E","EEEEEEE","E     E","E     E","       "},
                        {"EEEEEEE","CAAAAAC","CAAAAAC","CCCCCCC","CCCCCCC","CCCCCCC","E     E"},
                        {"EAAAAAE","C-----C","C-----C","C-----C","C-----C","C-----C","ECCCCCE"},
                        {"EAAAAAE","C-----C","B-----B","B-----B","B-----B","C-----C","ECCCCCE"},
                        {"EAAAAAE","C-----C","B-FFF-B","B-FFF-B","B-FFF-B","C-----C","EPPPPPE"},
                        {"ECC~CCE","CDDDDDC","CDDDDDC","CDDDDDC","CDDDDDC","CDDDDDC","EPPPPPE"}
                        //spotless:on
                }))
        .addShape(
            tier2,
            transpose(
                new String[][] {
                    // spotless:off
                        {"    ","    ","    ","HGGH","HGGH","HGGH","HGGH","HGGH","    ","    ","    "},
                        {"    ","    ","HGGH","GGGG","GGGG","GGGG","GGGG","GGGG","HGGH","    ","    "},
                        {"    ","HGGH","GGGG","G  G","G  G","G  G","G  G","G  G","GGGG","HGGH","    "},
                        {"    ","HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH","    "},
                        {"HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH"},
                        {"HGGH","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","G  G","HGGH"},
                        {"HGGH","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","GGGG","HGGH"}
                        //spotless:on
                }))
        .addShape(
            tier3,
            transpose(
                new String[][] {
                    // spotless:off
                        {"       ","       ","       ","       ","   I   ","   I   ","       ","       ","       ","       "},
                        {"       ","       ","       ","   I   ","   I   ","   I   ","   I   ","       ","       ","       "},
                        {"       ","       ","  KKK  ","  KIK  ","  K K  ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","   I   ","       ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I ","  K K  ","  KIK  ","       ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  KIK  ","       ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  K K  ","  KKK  ","       ","       "},
                        {"       ","  I I  "," I K I "," I   I "," I   I ","  K K  ","  K K  ","  KKK  ","       ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I ","  K K  ","  K K  ","  K K  ","  K K  "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","       ","  KKK  ","  K K  ","  K K  "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {"       ","  III  "," I   I "," I   I "," I   I "," I   I "," I   I "," I   I ","  III  ","       "},
                        {" II~II ","IIJJJII","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IJJJJJI","IIJJJII"," IIIII "}
                        //spotless:on
                }))
        .addShape(
            bioUpgrade,
            transpose(
                new String[][] {
                    // spotless:off
                        {"            ","            ","   LLLLLL   ","            ","            "},
                        {"            ","            ","  L      L  ","            ","            "},
                        {"E   E  E   E"," LLL    LLL "," LLL    LLL "," LLL    LLL ","E   E  E   E"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"EAAAE  EAAAE","A   A  A   A","A   A  A   A","A   A  A   A","EAAAE  EAAAE"},
                        {"ELLLE  ELLLE","LLLLL  LLLLL","LLLLL  LLLLL","LLLLL  LLLLL","ELLLE  ELLLE"}
                        //spotless:on
                }))
        .addShape(
            ocTier1Upgrade,
            transpose(
                new String[][] {
                    // spotless:off
                        {"EKKKE","K   K","K   K","K   K","EKKKE"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," NNN "," N N "," NNN ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"EOOOE","OKKKO","OK KO","OKKKO","EOOOE"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"E   E"," KKK "," K K "," KKK ","E   E"},
                        {"ENNNE","NKKKN","NK KN","NKKKN","ENNNE"},
                        {"EGGGE","GGGGG","GGMGG","GGGGG","EGGGE"}
                        //spotless:on
                }))
        .addShape(
            ocTier2Upgrade,
            transpose(
                new String[][] {
                    // spotless:off
                        {"RGGGR","G   G","G   G","G   G","RGGGR"},
                        {"R   R"," GGG "," GTG "," GGG ","R   R"},
                        {"R   R"," NNN "," NTN "," NNN ","R   R"},
                        {"R   R"," QQQ "," QTQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QTQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QTQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QTQ "," QQQ ","R   R"},
                        {"R   R"," QQQ "," QTQ "," QQQ ","R   R"},
                        {"RNNNR","NQQQN","NQTQN","NQQQN","RNNNR"},
                        {"RGGGR","GGGGG","GGSGG","GGGGG","RGGGR"}
                        //spotless:on
                }))
        .addElement('E', ofFrame(Materials.DamascusSteel))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings8, 11))
        .addElement('D', ofBlock(GregTech_API.sBlockReinforced, 2))
        .addElement('A', Glasses.chainAllGlasses())
        .addElement('B', ofBlock(GregTech_API.sBlockCasings3, 10))
        .addElement('F', ofFrame(Materials.VibrantAlloy))
        .addElement(
            'P',
            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11))
                .buildAndChain(GregTech_API.sBlockCasings8, 11))
        .addElement('H', ofFrame(Materials.Duranium))
        .addElement('G', ofBlock(GregTech_API.sBlockCasings8, 12))
        .addElement('I', ofBlock(GregTech_API.sBlockCasings8, 13))
        .addElement('K', ofBlock(GregTech_API.sBlockCasings8, 10))
        .addElement(
            'J',
            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class)
                .atLeast(InputHatch, OutputBus, InputBus, Maintenance, Energy.or(ExoticEnergy))
                .dot(1)
                .casingIndex(((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(13))
                .buildAndChain(GregTech_API.sBlockCasings8, 13))
        .addElement('L', ofBlock(GregTech_API.sBlockCasings4, 1))
        .addElement(
            'M',
            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class).hatchClass(GT_MetaTileEntity_Hatch_Input.class)
                .adder(GT_MetaTileEntity_PCBFactory::addCoolantInputToMachineList)
                .casingIndex(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 12))
                .dot(2)
                .buildAndChain(GregTech_API.sBlockCasings8, 12))
        .addElement('N', ofBlock(GregTech_API.sBlockCasings2, 15))
        .addElement('O', ofBlock(GregTech_API.sBlockCasings8, 4))
        .addElement(
            'S',
            buildHatchAdder(GT_MetaTileEntity_PCBFactory.class).hatchClass(GT_MetaTileEntity_Hatch_Input.class)
                .adder(GT_MetaTileEntity_PCBFactory::addCoolantInputToMachineList)
                .casingIndex(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 12))
                .dot(2)
                .buildAndChain(GregTech_API.sBlockCasings8, 12))
        .addElement('R', ofFrame(Materials.Americium))
        .addElement('Q', ofBlock(GregTech_API.sBlockCasings8, 14))
        .addElement('T', ofBlock(GregTech_API.sBlockCasings1, 15))
        .build();

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (mSetTier < 3) {
            buildPiece(tier1, stackSize, hintsOnly, 3, 5, 0);
            if (mSetTier == 2) {
                buildPiece(tier2, stackSize, hintsOnly, 7, 6, 2);
            }
        } else {
            buildPiece(tier3, stackSize, hintsOnly, 3, 21, 0);
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                getStructureDefinition().buildOrHints(
                    this,
                    stackSize,
                    bioUpgrade,
                    tTile.getWorld(),
                    transformFacing(getExtendedFacing()),
                    tTile.getXCoord(),
                    tTile.getYCoord(),
                    tTile.getZCoord(),
                    mBioOffsets[1],
                    6,
                    mBioOffsets[0],
                    hintsOnly);
            } else {
                buildPiece(bioUpgrade, stackSize, hintsOnly, mBioOffsets[0], 6, mBioOffsets[1]);
            }
        }

        if (mOCTier1 && !mOCTier2) {
            buildPiece(ocTier1Upgrade, stackSize, hintsOnly, mOCTier1Offsets[0], 9, mOCTier1Offsets[1]);
        }

        if (!mOCTier1 && mOCTier2) {
            buildPiece(ocTier2Upgrade, stackSize, hintsOnly, mOCTier2Offsets[0], 9, mOCTier2Offsets[1]);
        }
    }

    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int built = 0;
        if (mSetTier < 3) {
            built += survivialBuildPiece(tier1, stackSize, 3, 5, 0, elementBudget, env, false, true);
            if (mSetTier == 2) {
                built += survivialBuildPiece(tier2, stackSize, 7, 6, 2, elementBudget, env, false, true);
            }
        } else {
            built += survivialBuildPiece(tier3, stackSize, 3, 21, 0, elementBudget, env, false, true);
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                getStructureDefinition().survivalBuild(
                    this,
                    stackSize,
                    bioUpgrade,
                    tTile.getWorld(),
                    transformFacing(getExtendedFacing()),
                    tTile.getXCoord(),
                    tTile.getYCoord(),
                    tTile.getZCoord(),
                    mBioOffsets[1],
                    6,
                    mBioOffsets[0],
                    elementBudget,
                    env,
                    false);
            } else {
                built += survivialBuildPiece(
                    bioUpgrade,
                    stackSize,
                    mBioOffsets[0],
                    6,
                    mBioOffsets[1],
                    elementBudget,
                    env,
                    false,
                    true);
            }
        }

        if (mOCTier1 && !mOCTier2) {
            built += survivialBuildPiece(
                ocTier1Upgrade,
                stackSize,
                mOCTier1Offsets[0],
                9,
                mOCTier1Offsets[1],
                elementBudget,
                env,
                false,
                true);
        }
        if (!mOCTier1 && mOCTier2) {
            built += survivialBuildPiece(
                ocTier2Upgrade,
                stackSize,
                mOCTier2Offsets[0],
                9,
                mOCTier2Offsets[1],
                elementBudget,
                env,
                false,
                true);
        }

        return built;
    }

    public GT_MetaTileEntity_PCBFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_PCBFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_PCBFactory(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active)
                return new ITexture[] {
                    BlockIcons.getCasingTextureForId(
                        getTier() < 3 ? GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 11)
                            : GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 13)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            return new ITexture[] {
                BlockIcons.getCasingTextureForId(
                    getTier() < 3 ? GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 11)
                        : GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings8, 13)),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ASSEMBLY_LINE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { BlockIcons.getCasingTextureForId(
            mSetTier < 3 ? ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(11)
                : ((GT_Block_Casings8) GregTech_API.sBlockCasings8).getTextureIndex(13)) };
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_PCBFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mTier = 0;
        mUpgradesInstalled = 0;
        mCoolantInputHatch = null;
        if (mSetTier < 3) {
            if (!checkPiece(tier1, 3, 5, 0)) {
                return false;
            }
            if (mSetTier == 2) {
                if (!checkPiece(tier2, 7, 6, 2)) {
                    return false;
                }
                mTier = 2;
            } else {
                mTier = 1;
            }
        } else {
            if (!checkPiece(tier3, 3, 21, 0)) {
                return false;
            }
            mTier = 3;
        }

        if (mBioUpgrade) {
            if (mBioRotate) {
                final IGregTechTileEntity tTile = getBaseMetaTileEntity();
                if (!getStructureDefinition().check(
                    this,
                    bioUpgrade,
                    tTile.getWorld(),
                    transformFacing(getExtendedFacing()),
                    tTile.getXCoord(),
                    tTile.getYCoord(),
                    tTile.getZCoord(),
                    mBioOffsets[1],
                    6,
                    mBioOffsets[0],
                    !mMachine)) {
                    return false;
                }
            } else {
                if (!checkPiece(bioUpgrade, mBioOffsets[0], 6, mBioOffsets[1])) {
                    return false;
                }
            }
            mUpgradesInstalled++;
        }

        if (mOCTier1 && !mOCTier2) {
            if (!checkPiece(ocTier1Upgrade, mOCTier1Offsets[0], 9, mOCTier1Offsets[1])) {
                return false;
            }
            if (mCoolantInputHatch == null) {
                return false;
            }
            mUpgradesInstalled++;
        }

        if (mOCTier2 && !mOCTier1) {
            if (!checkPiece(ocTier2Upgrade, mOCTier2Offsets[0], 9, mOCTier2Offsets[1])) {
                return false;
            }
            if (mCoolantInputHatch == null) {
                return false;
            }
            mUpgradesInstalled++;
        }

        getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());

        if (mMaintenanceHatches.size() != 1) {
            return false;
        }

        if (!checkExoticAndNormalEnergyHatches()) {
            return false;
        }

        return mTier > 0;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.pcbFactoryRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@Nonnull GT_Recipe recipe) {
                // Here we check the dynamic parallels, which depend on the recipe
                int numberOfNanites = 0;
                ItemStack aNanite = recipe.getRepresentativeInput(1);
                ItemData naniteData = GT_OreDictUnificator.getAssociation(aNanite);
                if (naniteData != null && naniteData.mPrefix != null && naniteData.mPrefix.equals(OrePrefixes.nanite)) {
                    for (ItemStack aItem : inputItems) {
                        if (aItem != null && aItem.isItemEqual(aNanite)) {
                            numberOfNanites += aItem.stackSize;
                        }
                    }
                }
                maxParallel = (int) Math.max(Math.ceil(Math.log(numberOfNanites) / Math.log(2) + 0.00001), 1);
                mMaxParallel = maxParallel;

                PCBFactoryUpgrade requiredUpgrade = recipe.getMetadata(PCBFactoryUpgradeKey.INSTANCE);
                if (requiredUpgrade == PCBFactoryUpgrade.BIO && !mBioUpgrade)
                    return SimpleCheckRecipeResult.ofFailure("bio_upgrade_missing");

                int requiredPCBTier = recipe.getMetadataOrDefault(PCBFactoryTierKey.INSTANCE, 1);
                if (requiredPCBTier > mTier) return CheckRecipeResultRegistry.insufficientMachineTier(requiredPCBTier);

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@Nonnull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(isNoOC())
                    .setEUtDiscount((float) Math.sqrt(mUpgradesInstalled == 0 ? 1 : mUpgradesInstalled))
                    .setSpeedBoost(getDurationMultiplierFromRoughness())
                    .setDurationDecreasePerOC(mOCTier2 ? 2 : 1);
            }

            @Nonnull
            @Override
            protected GT_ParallelHelper createParallelHelper(@Nonnull GT_Recipe recipe) {
                return super.createParallelHelper(recipe)
                    .setEUtModifier((float) Math.sqrt(mUpgradesInstalled == 0 ? 1 : mUpgradesInstalled))
                    .setChanceMultiplier(mRoughnessMultiplier);
            }
        };
    }

    private boolean isNoOC() {
        return !mOCTier1 && !mOCTier2;
    }

    private float getDurationMultiplierFromRoughness() {
        return (float) Math.pow(mRoughnessMultiplier, 2);
    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            criticalStopMachine();
            return false;
        }

        if (ticker % 20 == 0) {
            if (mOCTier1) {
                if (mCoolantInputHatch == null) {
                    criticalStopMachine();
                    return false;
                }
                FluidStack tFluid = GT_ModHandler.getDistilledWater(COOLANT_CONSUMED_PER_SEC);
                if (!drain(mCoolantInputHatch, tFluid, true)) {
                    criticalStopMachine();
                    return false;
                }
            }

            if (mOCTier2) {
                if (mCoolantInputHatch == null) {
                    criticalStopMachine();
                    return false;
                }
                Fluid superCoolant = FluidRegistry.getFluid("supercoolant");
                FluidStack tFluid = new FluidStack(superCoolant, COOLANT_CONSUMED_PER_SEC);
                if (!drain(mCoolantInputHatch, tFluid, true)) {
                    criticalStopMachine();
                    return false;
                }
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            // TODO: Look for proper fix
            // Updates every 30 sec
            if (mUpdate <= -550) mUpdate = 50;
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return (int) (10000f * mRoughnessMultiplier);
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    private int getTier() {
        return mSetTier;
    }

    @Override
    public void receiveClientEvent(byte aEventID, byte aValue) {
        if (aEventID == 1) {
            if ((aValue & mTier1BitMap) == mTier1BitMap) {
                mSetTier = 1;
            }

            if ((aValue & mTier2BitMap) == mTier2BitMap) {
                mSetTier = 2;
            }

            if ((aValue & mTier3BitMap) == mTier3BitMap) {
                mSetTier = 3;
            }

            if ((aValue & mBioBitMap) == mBioBitMap) {
                mBioUpgrade = true;
            }

            if ((aValue & mBioRotateBitMap) == mBioRotateBitMap) {
                mBioRotate = true;
            }

            if ((aValue & mOCTier1BitMap) == mOCTier1BitMap) {
                mOCTier1 = true;
            }

            if ((aValue & mOCTier2BitMap) == mOCTier2BitMap) {
                mOCTier2 = true;
            }
        }
    }

    private ExtendedFacing transformFacing(ExtendedFacing facing) {
        ForgeDirection curDirection = facing.getDirection();
        Rotation curRotation = facing.getRotation();
        Flip curFlip = facing.getFlip();
        ForgeDirection newDirection = curDirection;
        Rotation newRotation = curRotation;
        Flip newFlip = curFlip;

        if (curDirection == ForgeDirection.UP || curDirection == ForgeDirection.DOWN) {
            switch (curRotation) {
                case CLOCKWISE, COUNTER_CLOCKWISE -> {
                    newFlip = curFlip == Flip.NONE ? Flip.HORIZONTAL : Flip.NONE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.NORTH : ForgeDirection.SOUTH;
                }
                case NORMAL -> {
                    newRotation = curDirection == ForgeDirection.UP ? Rotation.CLOCKWISE : Rotation.COUNTER_CLOCKWISE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.EAST : ForgeDirection.WEST;
                    newFlip = Flip.NONE;
                }
                case UPSIDE_DOWN -> {
                    newRotation = curDirection == ForgeDirection.UP ? Rotation.COUNTER_CLOCKWISE : Rotation.CLOCKWISE;
                    newDirection = curDirection == ForgeDirection.UP ? ForgeDirection.EAST : ForgeDirection.WEST;
                    newFlip = Flip.NONE;
                }
            }
        } else if (curRotation == Rotation.CLOCKWISE || curRotation == Rotation.COUNTER_CLOCKWISE) {
            newFlip = curRotation == Rotation.CLOCKWISE ? curFlip == Flip.NONE ? Flip.NONE : Flip.HORIZONTAL
                : curFlip != Flip.NONE ? Flip.NONE : Flip.HORIZONTAL;
            newDirection = curRotation == Rotation.CLOCKWISE ? ForgeDirection.UP : ForgeDirection.DOWN;
        } else {
            newDirection = switch (curDirection) {
                case EAST -> ForgeDirection.SOUTH;
                case NORTH -> ForgeDirection.EAST;
                case WEST -> ForgeDirection.NORTH;
                case SOUTH -> ForgeDirection.WEST;
                default -> curDirection;
            };
        }

        if (curRotation == Rotation.UPSIDE_DOWN) {
            if (curDirection != ForgeDirection.UP && curDirection != ForgeDirection.DOWN) {
                newFlip = curFlip == Flip.NONE ? Flip.HORIZONTAL : Flip.NONE;
            }
        }

        return ExtendedFacing.of(newDirection, newRotation, newFlip);
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_Input) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((GT_MetaTileEntity_Hatch_Input) aMetaTileEntity).mRecipeMap = null;
            mCoolantInputHatch = (GT_MetaTileEntity_Hatch_Input) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    protected long getActualEnergyUsage() {
        return (-this.lEUt * 10000) / Math.min(Math.max(1000, mEfficiency), 10000);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        inputSeparation = !inputSeparation;
        GT_Utility.sendChatToPlayer(
            aPlayer,
            StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
    }

    @Override
    public String[] getInfoData() {
        int mPollutionReduction = 0;
        for (GT_MetaTileEntity_Hatch_Muffler tHatch : filterValidMTEs(mMufflerHatches)) {
            mPollutionReduction = Math.max(tHatch.calculatePollutionReduction(100), mPollutionReduction);
        }

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (GT_MetaTileEntity_Hatch tHatch : getExoticAndNormalEnergyHatchList()) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long voltage = getAverageInputVoltage();
        long amps = getMaxInputAmps();

        return new String[] {
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(voltage)
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + amps
                + " A)"
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GT_Utility.getTier(voltage)]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ StatCollector.translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + mPollutionReduction
                + EnumChatFormatting.RESET
                + " %",
            /* 7 */ StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.GREEN
                + mMaxParallel,
            /* 8 */ StatCollector.translateToLocal("GT5U.multiblock.curparallelism") + ": "
                + EnumChatFormatting.GREEN
                + mCurrentParallel };
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Circuit Board Fabricator")
            .addInfo("Controller for the PCB Factory")
            .addInfo(
                EnumChatFormatting.GOLD.toString() + EnumChatFormatting.BOLD
                    + "IMPORTANT!"
                    + " Check the configuration menu before building.")
            .addInfo("Tier of the machine determines the available recipes.")
            .addInfo("Machine tier (1-3) is set in the controller GUI.")
            .addInfo("The configuration menu can be used to add upgrades.")
            .addInfo("Each tier and upgrade requires additional structures.")
            .addInfo("Power consumption is multiplied by Sqrt(structures).")
            .addInfo("Tier 2 and 3 allow parallel by using extra nanites.")
            .addInfo("Every doubling of nanites adds one parallel.")
            .addInfo("1x->1, 2x->2, 4x->3, 8x->4 with no limit.")
            .addInfo("Recipes require a cooling upgrade to be overclocked.")
            .addInfo("Liquid Cooling uses 10 L/s of distilled water and enables default overclocks.")
            .addInfo("Thermosink uses 10 L/s of Super Coolant and enables perfect overclocks.")
            .addInfo("Trace size can be changed to modify the material usage and machine speed.")
            .addInfo(AuthorBlueWeabo)
            .beginStructureBlock(30, 38, 13, false)
            .addSeparator()
            .addMaintenanceHatch(EnumChatFormatting.GOLD + "1", 1)
            .addEnergyHatch(
                EnumChatFormatting.GOLD + "1"
                    + EnumChatFormatting.GRAY
                    + "-"
                    + EnumChatFormatting.GOLD
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " or "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " TT energy hatch.",
                1)
            .addInputBus(EnumChatFormatting.GOLD + "0" + EnumChatFormatting.GRAY + "+", 1)
            .addOutputBus(EnumChatFormatting.GOLD + "0" + EnumChatFormatting.GRAY + "+", 1)
            .addInputHatch(EnumChatFormatting.GOLD + "0" + EnumChatFormatting.GRAY + "+", 1)
            .addStructureInfo(
                "Coolant Hatch (Input Hatch): " + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Center of the Liquid Cooling/Thermosink")
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Base Multi (Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 1
                    + EnumChatFormatting.BLUE
                    + "):")
            .addStructureInfo(EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "9" + EnumChatFormatting.GRAY + " Vibrant Alloy Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "25" + EnumChatFormatting.GRAY + " Reinforced Glass")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "77" + EnumChatFormatting.GRAY + " Basic Photolithography Framework Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "12" + EnumChatFormatting.GRAY + " Grate Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "25" + EnumChatFormatting.GRAY + " Plascrete Block")
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 2
                    + EnumChatFormatting.BLUE
                    + " (Adds to Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 1
                    + EnumChatFormatting.BLUE
                    + "):")
            .addStructureInfo(EnumChatFormatting.GOLD + "34" + EnumChatFormatting.GRAY + " Duranium Frame Box")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "158"
                    + EnumChatFormatting.GRAY
                    + " Reinforced Photolithography Framework Casing")
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 3 + EnumChatFormatting.BLUE + ":")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "292"
                    + EnumChatFormatting.GRAY
                    + " Radiation Proof Photolithography Framework Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "76" + EnumChatFormatting.GRAY + " Radiant Naquadah Alloy Casing")
            .addStructureInfo(EnumChatFormatting.BLUE + "Biochamber Upgrade")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "68" + EnumChatFormatting.GRAY + " Clean Stainless Steel Machine Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "72" + EnumChatFormatting.GRAY + " Reinforced Glass")
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Liquid Cooling Tower (Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 1
                    + EnumChatFormatting.BLUE
                    + "):")
            .addStructureInfo(EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Damascus Steel Frame Box")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "68" + EnumChatFormatting.GRAY + " Radiant Naquadah Alloy Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "12" + EnumChatFormatting.GRAY + " Extreme Engine Intake Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "20" + EnumChatFormatting.GRAY + " Tungstensteel Pipe Casing")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "21"
                    + EnumChatFormatting.GRAY
                    + " Reinforced Photolithography Framework Casing")
            .addStructureInfo(
                EnumChatFormatting.BLUE + "Thermosink Radiator(Tier "
                    + EnumChatFormatting.DARK_PURPLE
                    + 2
                    + EnumChatFormatting.BLUE
                    + "):")
            .addStructureInfo(EnumChatFormatting.GOLD + "40" + EnumChatFormatting.GRAY + " Americium Frame Box")
            .addStructureInfo(
                EnumChatFormatting.GOLD + "41"
                    + EnumChatFormatting.GRAY
                    + " Reinforced Photolithography Framework Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "8" + EnumChatFormatting.GRAY + " Superconducting Coil Block")
            .addStructureInfo(EnumChatFormatting.GOLD + "20" + EnumChatFormatting.GRAY + " Tungstensteel Pipe Casing")
            .addStructureInfo(EnumChatFormatting.GOLD + "48" + EnumChatFormatting.GRAY + " Infinity Cooled Casing")
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("mBioUpgrade", mBioUpgrade);
        aNBT.setBoolean("mBioRotate", mBioRotate);
        aNBT.setInteger("mBioOffsetX", mBioOffsets[0]);
        aNBT.setInteger("mBioOffsetZ", mBioOffsets[1]);
        aNBT.setBoolean("mOCTier1Upgrade", mOCTier1);
        aNBT.setInteger("mOCTier1OffsetX", mOCTier1Offsets[0]);
        aNBT.setInteger("mOCTier1OffsetZ", mOCTier1Offsets[1]);
        aNBT.setBoolean("mOCTier2Upgrade", mOCTier2);
        aNBT.setInteger("mOCTier2OffsetX", mOCTier2Offsets[0]);
        aNBT.setInteger("mOCTier2OffsetZ", mOCTier2Offsets[1]);
        aNBT.setFloat("mRoughnessMultiplier", mRoughnessMultiplier);
        aNBT.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mSeparate")) {
            // backward compatibility
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
        mBioUpgrade = aNBT.getBoolean("mBioUpgrade");
        mBioRotate = aNBT.getBoolean("mBioRotate");
        mBioOffsets[0] = aNBT.getInteger("mBioOffsetX");
        mBioOffsets[1] = aNBT.getInteger("mBioOffsetZ");
        mOCTier1 = aNBT.getBoolean("mOCTier1Upgrade");
        mOCTier1Offsets[0] = aNBT.getInteger("mOCTier1OffsetX");
        mOCTier1Offsets[1] = aNBT.getInteger("mOCTier1OffsetZ");
        mOCTier2 = aNBT.getBoolean("mOCTier2Upgrade");
        mOCTier2Offsets[0] = aNBT.getInteger("mOCTier2OffsetX");
        mOCTier2Offsets[1] = aNBT.getInteger("mOCTier2OffsetZ");
        mRoughnessMultiplier = aNBT.getFloat("mRoughnessMultiplier");
        mSetTier = aNBT.getInteger("mSetTier");
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (mSetTier == 1) {
            data += mTier1BitMap;
        } else if (mSetTier == 2) {
            data += mTier2BitMap;
        } else {
            data += mTier3BitMap;
        }

        if (mBioUpgrade) {
            data += mBioBitMap;
        }

        if (mBioRotate) {
            data += mBioRotateBitMap;
        }

        if (mOCTier1) {
            data += mOCTier1BitMap;
        }

        if (mOCTier2) {
            data += mOCTier2BitMap;
        }

        return data;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        buildContext.addSyncedWindow(10, this::createConfigurationWindow);
        builder.widget(
            new ButtonWidget().setOnClick(
                (clickData, widget) -> {
                    if (!widget.isClient()) widget.getContext()
                        .openSyncedWindow(10);
                })
                .setSize(16, 16)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GT_UITextures.BUTTON_STANDARD);
                    ret.add(GT_UITextures.OVERLAY_BUTTON_CYCLIC);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip("Configuration Menu")
                .setPos(174, 130))
            .widget(
                new TextWidget(new Text("Tier")).setTextAlignment(Alignment.Center)
                    .setScale(0.91f)
                    .setSize(20, 16)
                    .setPos(173, 98))
            .widget(
                new TextFieldWidget().setGetterInt(() -> mSetTier)
                    .setSetterInt(val -> mSetTier = val)
                    .setNumbers(1, 3)
                    .setTextColor(Color.WHITE.normal)
                    .setTextAlignment(Alignment.Center)
                    .addTooltip("PCB Factory Tier")
                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                    .setSize(18, 18)
                    .setPos(173, 110));
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 160);
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget("Configuration Menu").setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(185, 3))
            .widget(
                new DynamicPositionedColumn().setSynced(false)
                    .widget(
                        new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mBioUpgrade, val -> {
                            mBioUpgrade = val;
                            if (!mBioUpgrade) {
                                GT_Utility
                                    .sendChatToPlayer(player, GT_Utility.trans("339.1", "Biochamber Upgrade Disabled"));
                            } else {
                                GT_Utility
                                    .sendChatToPlayer(player, GT_Utility.trans("339", "Biochamber Upgrade Enabled"));
                            }
                        })
                            .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                            .setSize(90, 18)
                            .addTooltip(
                                "Enables nanites to construct organic circuitry. Required for Bioware and Wetware boards."))
                            .addChild(
                                new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                    .setSize(18, 18))
                            .addChild(
                                new TextWidget("Biochamber").setTextAlignment(Alignment.Center)
                                    .setPos(23, 5))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mBioRotate, val -> {
                        mBioRotate = val;
                        if (!mBioRotate) {
                            GT_Utility
                                .sendChatToPlayer(player, GT_Utility.trans("340.1", "Rotated biochamber disabled"));
                        } else {
                            GT_Utility.sendChatToPlayer(player, GT_Utility.trans("340", "Rotated biochamber enabled"));
                        }
                    })
                        .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip("Rotates the biochamber by 90 degrees."))
                        .addChild(
                            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget("Bio Rotation").setTextAlignment(Alignment.Center)
                                .setPos(23, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mOCTier1, val -> {
                        mOCTier1 = val;
                        if (!mOCTier1) {
                            GT_Utility.sendChatToPlayer(player, GT_Utility.trans("341.1", "Tier 1 cooling disabled"));
                        } else {
                            GT_Utility.sendChatToPlayer(player, GT_Utility.trans("341", "Tier 1 cooling enabled"));
                        }
                    })
                        .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip(
                            "Allows for overclocking. Requires 10 L/s of distilled water. Cooling upgrades are mutually exclusive."))
                        .addChild(
                            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget("Liquid Cooling").setTextAlignment(Alignment.Center)
                                .setPos(20, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mOCTier2, val -> {
                        mOCTier2 = val;
                        if (!mOCTier2) {
                            GT_Utility.sendChatToPlayer(player, GT_Utility.trans("342.1", "Tier 2 cooling disabled"));
                        } else {
                            GT_Utility.sendChatToPlayer(player, GT_Utility.trans("342", "Tier 2 cooling enabled"));
                        }
                    })
                        .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip(
                            "Enables perfect overclocking by allowing nanites to work with extreme speed and efficiency. Uses 10 L/s of Super Coolant."))
                        .addChild(
                            new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget("Thermosink").setTextAlignment(Alignment.Center)
                                .setPos(20, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text("Trace Size")).setSize(90, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                            .setPos(0, 4))
                    .widget(
                        new TextFieldWidget().setGetterInt(() -> (int) ((1f / mRoughnessMultiplier) * 100f))
                            .setSetterInt(val -> mRoughnessMultiplier = 100f / val)
                            .setNumbers(50, 200)
                            .setTextColor(Color.WHITE.normal)
                            .setTextAlignment(Alignment.Center)
                            .addTooltip(
                                "Set the trace size. Smaller traces allow material savings but take longer to fabricate. Larger traces waste material but are fast. 50-200 μm.")
                            .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                            .setSize(90, 16))
                    .widget(
                        new DrawableWidget().setDrawable(GT_UITextures.OVERLAY_BUTTON_CROSS)
                            .setSize(18, 18)
                            .addTooltip(new Text("Can't change configuration when running !").color(Color.RED.dark(3)))
                            .setEnabled(widget -> getBaseMetaTileEntity().isActive()))
                    .setPos(10, 25))
            .widget(
                new DynamicPositionedColumn().setSynced(false)
                    .widget(
                        new TextWidget(new Text("Bio Upgrade Offsets")).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mBioOffsets[0])
                                    .setSetterInt(val -> mBioOffsets[0] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("X Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mBioOffsets[1])
                                    .setSetterInt(val -> mBioOffsets[1] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("Z Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text("Cooler Tier 1 Offsets")).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mOCTier1Offsets[0])
                                    .setSetterInt(val -> mOCTier1Offsets[0] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("X Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mOCTier1Offsets[1])
                                    .setSetterInt(val -> mOCTier1Offsets[1] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("Z Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text("Cooler Tier 2 Offsets")).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mOCTier2Offsets[0])
                                    .setSetterInt(val -> mOCTier2Offsets[0] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("X Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new TextFieldWidget().setGetterInt(() -> mOCTier2Offsets[1])
                                    .setSetterInt(val -> mOCTier2Offsets[1] = val)
                                    .setNumbers(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip("Z Offset")
                                    .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .setPos(110, 25));
        return builder.build();
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }
}
