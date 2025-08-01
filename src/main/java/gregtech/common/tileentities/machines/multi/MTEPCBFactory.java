package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorBlueWeabo;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ASSEMBLY_LINE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
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
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedRow;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.GregTechTileClientEvents;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchNanite;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.recipe.metadata.PCBFactoryTierKey;
import gregtech.api.recipe.metadata.PCBFactoryUpgrade;
import gregtech.api.recipe.metadata.PCBFactoryUpgradeKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.ParallelHelper;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.blocks.BlockCasings8;

@SuppressWarnings("SpellCheckingInspection")
public class MTEPCBFactory extends MTEExtendedPowerMultiBlockBase<MTEPCBFactory>
    implements ISurvivalConstructable, INEIPreviewModifier {

    private static final String xOffsetText = "GT5U.MBTT.PCB.XOffset";
    private static final String zOffsetText = "GT5U.MBTT.PCB.ZOffset";
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final String tier3 = "tier3";
    private static final String bioUpgrade = "bioUpgrade";
    private static final String ocTier1Upgrade = "ocTier1Upgrade";
    private static final String ocTier2Upgrade = "ocTier2Upgrade";
    private float mRoughnessMultiplier = 1;
    private int mTier = 1;
    private int mSetTier = 1;
    private int mUpgradesInstalled = 0;
    private int mMaxParallel = 0;
    private boolean mBioUpgrade = false, mBioRotate = false, mOCTier1 = false, mOCTier2 = false;
    private final int[] mBioOffsets = new int[] { -5, -1 };
    private final int[] mOCTier1Offsets = new int[] { 2, -11 };
    private final int[] mOCTier2Offsets = new int[] { 2, -11 };
    private MTEHatchInput mCoolantInputHatch;
    private final ArrayList<MTEHatchNanite> naniteBuses = new ArrayList<>();
    private static final int mBioRotateBitMap = 0b1000000;
    private static final int mOCTier2BitMap = 0b100000;
    private static final int mOCTier1BitMap = 0b10000;
    private static final int mBioBitMap = 0b1000;
    private static final int mTier3BitMap = 0b100;
    private static final int mTier2BitMap = 0b10;
    private static final int mTier1BitMap = 0b1;
    private static final int COOLANT_CONSUMED_PER_SEC = 10;
    private static final IStructureDefinition<MTEPCBFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPCBFactory>builder()
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
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockReinforced, 2))
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings3, 10))
        .addElement('F', ofFrame(Materials.VibrantAlloy))
        .addElement(
            'P',
            buildHatchAdder(MTEPCBFactory.class)
                .atLeast(
                    InputHatch,
                    OutputBus,
                    InputBus,
                    Maintenance,
                    Energy.or(ExoticEnergy),
                    SpecialHatchElement.NaniteBus)
                .dot(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(11))
                .buildAndChain(GregTechAPI.sBlockCasings8, 11))
        .addElement('H', ofFrame(Materials.Duranium))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 12))
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings8, 13))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement(
            'J',
            buildHatchAdder(MTEPCBFactory.class)
                .atLeast(
                    InputHatch,
                    OutputBus,
                    InputBus,
                    Maintenance,
                    Energy.or(ExoticEnergy),
                    SpecialHatchElement.NaniteBus)
                .dot(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(13))
                .buildAndChain(GregTechAPI.sBlockCasings8, 13))
        .addElement('L', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'M',
            buildHatchAdder(MTEPCBFactory.class).hatchClass(MTEHatchInput.class)
                .adder(MTEPCBFactory::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 12))
        .addElement('N', ofBlock(GregTechAPI.sBlockCasings2, 15))
        .addElement('O', ofBlock(GregTechAPI.sBlockCasings8, 4))
        .addElement(
            'S',
            buildHatchAdder(MTEPCBFactory.class).hatchClass(MTEHatchInput.class)
                .adder(MTEPCBFactory::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .dot(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 12))
        .addElement('R', ofFrame(Materials.Americium))
        .addElement('Q', ofBlock(GregTechAPI.sBlockCasings8, 14))
        .addElement('T', ofBlock(GregTechAPI.sBlockCasings1, 15))
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
        int built = 0;
        if (mMachine) return -1;
        if (mSetTier < 3) {
            built += survivalBuildPiece(tier1, stackSize, 3, 5, 0, elementBudget, env, false, true);
            if (mSetTier == 2) {
                built += survivalBuildPiece(tier2, stackSize, 7, 6, 2, elementBudget, env, false, true);
            }
        } else {
            built += survivalBuildPiece(tier3, stackSize, 3, 21, 0, elementBudget, env, false, true);
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
                built += survivalBuildPiece(
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
            built += survivalBuildPiece(
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
            built += survivalBuildPiece(
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

    public MTEPCBFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEPCBFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEPCBFactory(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection sideDirection,
        ForgeDirection facingDirection, int colorIndex, boolean active, boolean redstoneLevel) {
        if (sideDirection == facingDirection) {
            if (active) return new ITexture[] {
                BlockIcons.getCasingTextureForId(
                    getTier() < 3 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 11)
                        : GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 13)),
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
                    getTier() < 3 ? GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 11)
                        : GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 13)),
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
            mSetTier < 3 ? ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(11)
                : ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(13)) };
    }

    @Override
    public IStructureDefinition<MTEPCBFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mTier = 0;
        mUpgradesInstalled = 0;
        mCoolantInputHatch = null;
        naniteBuses.clear();
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
            protected CheckRecipeResult validateRecipe(@Nonnull GTRecipe recipe) {
                // Here we check the dynamic parallels, which depend on the recipe
                int numberOfNanites = 0;
                Materials naniteMaterial = recipe.getMetadata(GTRecipeConstants.PCB_NANITE_MATERIAL);
                if (naniteMaterial != null) {
                    if (naniteBuses.isEmpty()) {
                        return SimpleCheckRecipeResult.ofFailure("nanites_missing");
                    }
                    boolean nanitesFound = false;
                    for (MTEHatchNanite naniteBus : naniteBuses) {
                        ItemStack storedNanites = naniteBus.getItemStack();
                        Materials storedNaniteMaterial = naniteBus.getStoredNaniteMaterial();
                        if (storedNanites == null || storedNaniteMaterial != naniteMaterial) {
                            continue;
                        }
                        numberOfNanites = naniteBus.getItemCount();
                        nanitesFound = true;
                        break;
                    }
                    if (!nanitesFound) {
                        return SimpleCheckRecipeResult.ofFailure("nanites_missing");
                    }
                }
                maxParallel = (int) Math.min(Math.max(Math.ceil(Math.pow(numberOfNanites, 0.75)), 1), 256);
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
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                return super.createOverclockCalculator(recipe).setNoOverclock(isNoOC())
                    .setEUtDiscount(Math.sqrt(mUpgradesInstalled == 0 ? 1 : mUpgradesInstalled))
                    .setDurationModifier(getDurationMultiplierFromRoughness())
                    .setDurationDecreasePerOC(mOCTier2 ? 4.0 : 2.0);
            }

            @Nonnull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                return super.createParallelHelper(recipe)
                    .setEUtModifier((float) Math.sqrt(mUpgradesInstalled == 0 ? 1 : mUpgradesInstalled))
                    .setChanceMultiplier(mRoughnessMultiplier);
            }
        };
    }

    private boolean isNoOC() {
        return !mOCTier1 && !mOCTier2;
    }

    private double getDurationMultiplierFromRoughness() {
        return mRoughnessMultiplier * mRoughnessMultiplier;
    }

    private int ticker = 0;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }

        if (ticker % 20 == 0) {
            if (!isNoOC()) {
                FluidStack tFluid = mOCTier1 ? GTModHandler.getDistilledWater(COOLANT_CONSUMED_PER_SEC)
                    : Materials.SuperCoolant.getFluid(COOLANT_CONSUMED_PER_SEC);
                if (!drain(mCoolantInputHatch, tFluid, true)) {
                    stopMachine(ShutDownReasonRegistry.outOfFluid(tFluid));
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
            // Updates every 10 sec
            if (mUpdate <= -150) mUpdate = 50;
        }
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return (int) (10000f * mRoughnessMultiplier);
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

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput) {
            ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
            mCoolantInputHatch = (MTEHatchInput) aMetaTileEntity;
            return true;
        }
        return false;
    }

    @Override
    protected long getActualEnergyUsage() {
        return (-this.lEUt * 10000) / Math.min(Math.max(1000, mEfficiency), 10000);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatToPlayer(aPlayer, translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatch tHatch : getExoticAndNormalEnergyHatchList()) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }
        long voltage = getAverageInputVoltage();
        long amps = getMaxInputAmps();

        int mCurrentParallel = 0;
        return new String[] {
            /* 1 */ translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(voltage)
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + amps
                + " A)"
                + translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(voltage)]
                + EnumChatFormatting.RESET,
            /* 5 */ translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + mEfficiency / 100.0F
                + EnumChatFormatting.RESET
                + " %",
            /* 6 */ translateToLocal("GT5U.multiblock.pollution") + ": "
                + EnumChatFormatting.GREEN
                + getAveragePollutionPercentage()
                + EnumChatFormatting.RESET
                + " %",
            /* 7 */ translateToLocal("GT5U.multiblock.parallelism") + ": " + EnumChatFormatting.GREEN + mMaxParallel,
            /* 8 */ translateToLocal("GT5U.multiblock.curparallelism") + ": "
                + EnumChatFormatting.GREEN
                + mCurrentParallel };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Circuit Board Fabricator")
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
            .addInfo("Nanites have to be placed in a Nanite Containment Bus.")
            .addInfo("The formula for parallels is the amount of nanites^0.75, rounded up.")
            .addInfo("Maximum parallel is 256.")
            .addInfo("Recipes require a cooling upgrade to be overclocked.")
            .addInfo("Liquid Cooling uses 10 L/s of distilled water and enables default overclocks.")
            .addInfo("Thermosink uses 10 L/s of Super Coolant and enables perfect overclocks.")
            .addInfo("Trace size can be changed to modify the material usage and machine speed.")
            .addTecTechHatchInfo()
            .beginStructureBlock(30, 38, 13, false)
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
                EnumChatFormatting.WHITE + "Nanite Containment Bus: "
                    + EnumChatFormatting.GOLD
                    + "0"
                    + EnumChatFormatting.GRAY
                    + "+")
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Coolant Hatch (Input Hatch): "
                    + EnumChatFormatting.GOLD
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
            .toolTipFinisher(AuthorBlueWeabo);
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
    protected SoundResource getProcessStartSound() {
        return SoundResource.IC2_MACHINES_MAGNETIZER_LOOP;
    }

    @Override
    public byte getUpdateData() {
        byte data = 0;
        if (mSetTier == 1) {
            data += mTier1BitMap;
        } else if (mSetTier == 2) {
            data += mTier2BitMap;
        } else if (mSetTier == 3) {
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
    public Pos2d getStructureUpdateButtonPos() {
        return new Pos2d(80, 91);
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
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_CYCLIC);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip(translateToLocal("GT5U.MBTT.ConfigMenu"))
                .setPos(174, 130))
            .widget(
                new TextWidget(new Text(translateToLocal("GT5U.machines.tier"))).setTextAlignment(Alignment.Center)
                    .setScale(0.91f)
                    .setSize(20, 16)
                    .setPos(173, 98))
            .widget(
                new NumericWidget().setGetter(() -> mSetTier)
                    .setSetter(val -> mSetTier = (int) val)
                    .setBounds(1, 3)
                    .setTextColor(Color.WHITE.normal)
                    .setTextAlignment(Alignment.Center)
                    .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tier"))
                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                    .setSize(18, 18)
                    .setPos(173, 110));
    }

    protected ModularWindow createConfigurationWindow(final EntityPlayer player) {
        ModularWindow.Builder builder = ModularWindow.builder(200, 160);
        builder.setBackground(GTUITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.setGuiTint(getGUIColorization());
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                .setPos(5, 5)
                .setSize(16, 16))
            .widget(new TextWidget(translateToLocal("GT5U.MBTT.ConfigMenu")).setPos(25, 9))
            .widget(
                ButtonWidget.closeWindowButton(true)
                    .setPos(185, 3))
            .widget(
                new DynamicPositionedColumn().setSynced(false)
                    .widget(
                        new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mBioUpgrade, val -> {
                            mBioUpgrade = val;
                            if (!mBioUpgrade) {
                                GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.BioOff"));
                            } else {
                                GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.BioOn"));
                            }
                        })
                            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                            .setSize(90, 18)
                            .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tooltip.1")))
                            .addChild(
                                new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                                    .setSize(18, 18))
                            .addChild(
                                new TextWidget(translateToLocal("GT5U.MBTT.PCB.Bio")).setTextAlignment(Alignment.Center)
                                    .setPos(23, 5))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mBioRotate, val -> {
                        mBioRotate = val;
                        if (!mBioRotate) {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.RotBioOff"));
                        } else {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.RotBioOn"));
                        }
                    })
                        .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tooltip.2")))
                        .addChild(
                            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget(translateToLocal("GT5U.MBTT.PCB.RotBio")).setTextAlignment(Alignment.Center)
                                .setPos(23, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mOCTier1, val -> {
                        mOCTier1 = val;
                        mOCTier2 = false;
                        if (!mOCTier1) {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.T1CoolOff"));
                        } else {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.T1CoolOn"));
                        }
                    })
                        .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tooltip.3")))
                        .addChild(
                            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget(translateToLocal("GT5U.MBTT.PCB.LiqCool")).setTextAlignment(Alignment.Center)
                                .setPos(20, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(new MultiChildWidget().addChild(new CycleButtonWidget().setToggle(() -> mOCTier2, val -> {
                        mOCTier2 = val;
                        mOCTier1 = false;
                        if (!mOCTier2) {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.T2CoolOff"));
                        } else {
                            GTUtility.sendChatToPlayer(player, translateToLocal("GT5U.MBTT.PCB.T2CoolOn"));
                        }
                    })
                        .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
                        .setSize(90, 18)
                        .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tooltip.4")))
                        .addChild(
                            new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CYCLIC)
                                .setSize(18, 18))
                        .addChild(
                            new TextWidget(translateToLocal("GT5U.MBTT.PCB.Sink")).setTextAlignment(Alignment.Center)
                                .setPos(20, 5))
                        .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text(translateToLocal("GT5U.MBTT.PCB.Size"))).setSize(90, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive())
                            .setPos(0, 4))
                    .widget(
                        new NumericWidget().setGetter(() -> (int) ((1f / mRoughnessMultiplier) * 100f))
                            .setSetter(val -> mRoughnessMultiplier = 100f / (int) val)
                            .setBounds(50, 200)
                            .setTextColor(Color.WHITE.normal)
                            .setTextAlignment(Alignment.Center)
                            .addTooltip(translateToLocal("GT5U.MBTT.PCB.Tooltip.5"))
                            .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                            .setSize(90, 16))
                    .widget(
                        new DrawableWidget().setDrawable(GTUITextures.OVERLAY_BUTTON_CROSS)
                            .setSize(18, 18)
                            .addTooltip(new Text(translateToLocal("GT5U.MBTT.PCB.Tooltip.6")).color(Color.RED.dark(3)))
                            .setEnabled(widget -> getBaseMetaTileEntity().isActive()))
                    .setPos(10, 25))
            .widget(
                new DynamicPositionedColumn().setSynced(false)
                    .widget(
                        new TextWidget(new Text(translateToLocal("GT5U.MBTT.PCB.Offsets"))).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new NumericWidget().setGetter(() -> mBioOffsets[0])
                                    .setSetter(val -> mBioOffsets[0] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(xOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new NumericWidget().setGetter(() -> mBioOffsets[1])
                                    .setSetter(val -> mBioOffsets[1] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(zOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text(translateToLocal("GT5U.MBTT.PCB.T1CoolOffset"))).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new NumericWidget().setGetter(() -> mOCTier1Offsets[0])
                                    .setSetter(val -> mOCTier1Offsets[0] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(xOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new NumericWidget().setGetter(() -> mOCTier1Offsets[1])
                                    .setSetter(val -> mOCTier1Offsets[1] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(zOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new TextWidget(new Text(translateToLocal("GT5U.MBTT.PCB.T2CoolOffset"))).setSize(72, 18)
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .widget(
                        new DynamicPositionedRow().setSynced(false)
                            .widget(
                                new NumericWidget().setGetter(() -> mOCTier2Offsets[0])
                                    .setSetter(val -> mOCTier2Offsets[0] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(xOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .widget(
                                new NumericWidget().setGetter(() -> mOCTier2Offsets[1])
                                    .setSetter(val -> mOCTier2Offsets[1] = (int) val)
                                    .setBounds(-16, 16)
                                    .setTextColor(Color.WHITE.normal)
                                    .setTextAlignment(Alignment.Center)
                                    .addTooltip(translateToLocal(zOffsetText))
                                    .setBackground(GTUITextures.BACKGROUND_TEXT_FIELD)
                                    .setSize(36, 18))
                            .setEnabled(widget -> !getBaseMetaTileEntity().isActive()))
                    .setPos(110, 25));
        return builder.build();
    }

    public boolean addNaniteBusToMachineList(IGregTechTileEntity tileEntity, int baseCasingIndex) {
        if (tileEntity == null) return false;
        IMetaTileEntity metaTileEntity = tileEntity.getMetaTileEntity();
        if (metaTileEntity instanceof MTEHatchNanite naniteBus) {
            naniteBus.updateTexture(baseCasingIndex);
            this.naniteBuses.add(naniteBus);
            return true;
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEPCBFactory> {

        NaniteBus(MTEPCBFactory::addNaniteBusToMachineList, MTEHatchNanite.class) {

            @Override
            public long count(MTEPCBFactory gtMetaTileEntityPCBFactory) {
                return gtMetaTileEntityPCBFactory.naniteBuses.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEPCBFactory> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEPCBFactory> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEPCBFactory> adder() {
            return adder;
        }
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public void onPreviewConstruct(@NotNull ItemStack trigger) {
        mSetTier = trigger.stackSize;
    }
}
