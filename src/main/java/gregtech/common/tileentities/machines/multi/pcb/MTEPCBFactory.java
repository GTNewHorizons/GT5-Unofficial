package gregtech.common.tileentities.machines.multi.pcb;

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
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
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

import gregtech.api.GregTechAPI;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures.BlockIcons;
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
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.gui.modularui.multiblock.MTEPCBFactoryGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

@SuppressWarnings("SpellCheckingInspection")
public class MTEPCBFactory extends MTEExtendedPowerMultiBlockBase<MTEPCBFactory>
    implements ISurvivalConstructable, INEIPreviewModifier {

    public static final int UPGRADE_RANGE = 16;
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";
    private static final String tier3 = "tier3";
    private static final int COOLANT_CONSUMED_PER_SEC = 10;
    private float mRoughnessMultiplier = 1;
    private byte mTier = 1;
    private int mMaxParallel = 0;

    // for backwards compatibility (upgrades don't need a controller in this mode)
    private CompatMode compatMode = new CompatMode();
    private static final String OCUpgradeCompat = "OCUpgradeCompat";

    private MTEPCBBioChamber mBioChamber;
    private int mBioChamberX;
    private int mBioChamberY;
    private int mBioChamberZ;
    private MTEPCBCoolingTower mCoolingTower;
    private int mCoolingTowerX;
    private int mCoolingTowerY;
    private int mCoolingTowerZ;
    private final ArrayList<MTEHatchNanite> naniteBuses = new ArrayList<>();
    private static final byte mTextureBitmap = 0b1;
    private static final IStructureDefinition<MTEPCBFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEPCBFactory>builder()
        .addShape(
            tier1,
            transpose(
                new String[][] {
                    // spotless:off
                    {"       ", "E     E", "E     E", "EEEEEEE", "E     E", "E     E", "       "},
                    {"EEEEEEE", "CAAAAAC", "CAAAAAC", "CCCCCCC", "CCCCCCC", "CCCCCCC", "E     E"},
                    {"EAAAAAE", "C-----C", "C-----C", "C-----C", "C-----C", "C-----C", "ECCCCCE"},
                    {"EAAAAAE", "C-----C", "B-----B", "B-----B", "B-----B", "C-----C", "ECCCCCE"},
                    {"EAAAAAE", "C-----C", "B-FFF-B", "B-FFF-B", "B-FFF-B", "C-----C", "EPPPPPE"},
                    {"ECC~CCE", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "CDDDDDC", "EPPPPPE"}
                    //spotless:on
                }))
        .addShape(
            tier2,
            transpose(
                new String[][] {
                    // spotless:off
                    {"    ", "    ", "    ", "HGGH", "HGGH", "HGGH", "HGGH", "HGGH", "    ", "    ", "    "},
                    {"    ", "    ", "HGGH", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "HGGH", "    ", "    "},
                    {"    ", "HGGH", "GGGG", "G  G", "G  G", "G  G", "G  G", "G  G", "GGGG", "HGGH", "    "},
                    {"    ", "HGGH", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "HGGH", "    "},
                    {"HGGH", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "HGGH"},
                    {"HGGH", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "G  G", "HGGH"},
                    {"HGGH", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "GGGG", "HGGH"}
                    //spotless:on
                }))
        .addShape(
            tier3,
            transpose(
                new String[][] {
                    // spotless:off
                    {"       ", "       ", "       ", "       ", "   I   ", "   I   ", "       ", "       ", "       ", "       "},
                    {"       ", "       ", "       ", "   I   ", "   I   ", "   I   ", "   I   ", "       ", "       ", "       "},
                    {"       ", "       ", "  KKK  ", "  KIK  ", "  K K  ", "  K K  ", "   I   ", "       ", "       ", "       "},
                    {"       ", "       ", "  KKK  ", "  K K  ", "  K K  ", "  K K  ", "   I   ", "       ", "       ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", "  K K  ", "   I   ", "       ", "       ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", "  K K  ", "   I   ", "       ", "       ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", "  K K  ", "  KIK  ", "       ", "       ", "       "},
                    {"       ", "  I I  ", " I K I ", " I   I ", " I   I ", "  K K  ", "  KIK  ", "       ", "       ", "       "},
                    {"       ", "  I I  ", " I K I ", " I   I ", " I   I ", "  K K  ", "  K K  ", "  KKK  ", "       ", "       "},
                    {"       ", "  I I  ", " I K I ", " I   I ", " I   I ", "  K K  ", "  K K  ", "  KKK  ", "       ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", "  K K  ", "  K K  ", "  K K  ", "  K K  ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "       ", "  KKK  ", "  K K  ", "  K K  ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "       ", "  KKK  ", "  K K  ", "  K K  ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "       ", "  KKK  ", "  K K  ", "  K K  ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {"       ", "  III  ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", " I   I ", "  III  ", "       "},
                    {" II~II ", "IIJJJII", "IJJJJJI", "IJJJJJI", "IJJJJJI", "IJJJJJI", "IJJJJJI", "IJJJJJI", "IIJJJII", " IIIII "}
                    //spotless:on
                }))
        .addShape(
            OCUpgradeCompat,
            transpose(
                new String[][] {
                    // spotless:off
                {"     ", "     ","  L  ","     ","     "}
                //spotless:on
                }))
        .addElement('A', chainAllGlasses())
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings3, 10))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockReinforced, 2))
        .addElement('E', ofFrame(Materials.DamascusSteel))
        .addElement('F', ofFrame(Materials.VibrantAlloy))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 12))
        .addElement('H', ofFrame(Materials.Duranium))
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings8, 13))
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
                .hint(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(13))
                .buildAndChain(GregTechAPI.sBlockCasings8, 13))
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings8, 10))
        .addElement(
            'L',
            buildHatchAdder(MTEPCBFactory.class).hatchClass(MTEHatchInput.class)
                .adder(MTEPCBFactory::addCoolantInputToMachineList)
                .casingIndex(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings8, 12))
                .hint(2)
                .buildAndChain(GregTechAPI.sBlockCasings8, 12))
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
                .hint(1)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(11))
                .buildAndChain(GregTechAPI.sBlockCasings8, 11))
        .build();

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize < 3) {
            buildPiece(tier1, stackSize, hintsOnly, 3, 5, 0);
            if (stackSize.stackSize == 2) {
                buildPiece(tier2, stackSize, hintsOnly, 7, 6, 2);
            }
            return;
        }
        buildPiece(tier3, stackSize, hintsOnly, 3, 21, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;
        if (mMachine) return -1;
        if (stackSize.stackSize < 3) {
            built += survivalBuildPiece(tier1, stackSize, 3, 5, 0, elementBudget, env, false, true);
            if (stackSize.stackSize == 2) {
                built += survivalBuildPiece(tier2, stackSize, 7, 6, 2, elementBudget, env, false, true);
            }
        } else {
            built += survivalBuildPiece(tier3, stackSize, 3, 21, 0, elementBudget, env, false, true);
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
    public byte getUpdateData() {
        return (mTier < 3 ? 0 : mTextureBitmap);
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
            mTier < 3 ? ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(11)
                : ((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(13)) };
    }

    @Override
    public IStructureDefinition<MTEPCBFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void receiveClientEvent(byte eventID, byte value) {
        if (eventID == 1) {
            if ((value & 1) == 1) {
                mTier = 3;
            }
        }
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        byte newTier = checkForNewTier();
        if (newTier == 0) return false;
        if (newTier != mTier) {
            mTier = newTier;
            getBaseMetaTileEntity().sendBlockEvent(GregTechTileClientEvents.CHANGE_CUSTOM_DATA, getUpdateData());
        }

        if (mMaintenanceHatches.size() != 1) {
            return false;
        }

        if (!checkExoticAndNormalEnergyHatches()) {
            return false;
        }

        if (compatMode.isSet && compatMode.OCTier != 0) {
            if (!checkPiece(OCUpgradeCompat, compatMode.OCX, 0, compatMode.OCZ)) return false;
        }

        return true;
    }

    /**
     * check wether the tier of the structure has changed.
     * Optimised by first looking at the previously known structure tier
     *
     * @return 0 = invalid, others = tier number
     */
    private byte checkForNewTier() {
        if (mTier < 3) {
            byte tier1Or2 = getTier1Or2();
            if (tier1Or2 > 0) return tier1Or2;
            return (byte) (checkPiece(tier3, 3, 21, 0) ? 3 : 0);
        }

        // mTier == 3
        return checkPiece(tier3, 3, 21, 0) ? 3 : getTier1Or2();
    }

    /**
     * return wether the structure is tier 1, 2 or neither
     *
     * @return 0 = neither tier 1 or 2, 1 = tier 1, 2 = tier 2
     */
    private byte getTier1Or2() {
        if (checkPiece(tier1, 3, 5, 0)) {
            return (byte) (checkPiece(tier2, 7, 6, 2) ? 2 : 1);
        }
        return 0;
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
                if (!compatMode.isSet) {
                    if (requiredUpgrade == PCBFactoryUpgrade.BIO && mBioChamber == null
                        || requiredUpgrade == PCBFactoryUpgrade.BIO && !mBioChamber.isAllowedToWork())
                        return SimpleCheckRecipeResult.ofFailure("bio_upgrade_missing");
                } else {
                    if (requiredUpgrade == PCBFactoryUpgrade.BIO && !compatMode.bioUpgrade) {
                        return SimpleCheckRecipeResult.ofFailure("bio_upgrade_missing");
                    }
                }
                int requiredPCBTier = recipe.getMetadataOrDefault(PCBFactoryTierKey.INSTANCE, 1);
                if (requiredPCBTier > mTier) return CheckRecipeResultRegistry.insufficientMachineTier(requiredPCBTier);

                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                int structures = 1;
                if (mBioChamber != null) {
                    structures++;
                }
                if (mCoolingTower != null) {
                    structures++;
                }
                if (compatMode.isSet) {
                    structures = 1;
                    if (compatMode.OCTier != 0 || mCoolingTower != null) {
                        structures++;
                    }
                    if (compatMode.bioUpgrade) structures++;
                    return super.createOverclockCalculator(recipe).setNoOverclock(!isOC())
                        .setEUtDiscount(Math.sqrt(structures))
                        .setDurationModifier(getDurationMultiplierFromRoughness())
                        .setDurationDecreasePerOC(compatMode.OCTier == 2 ? 4.0 : 2.0);
                }
                if (mCoolingTower != null) {
                    return super.createOverclockCalculator(recipe).setNoOverclock(!isOC())
                        .setEUtDiscount(Math.sqrt(structures))
                        .setDurationModifier(getDurationMultiplierFromRoughness())
                        .setDurationDecreasePerOC(!mCoolingTower.isTier1 ? 4.0 : 2.0);
                }
                return super.createOverclockCalculator(recipe).setNoOverclock(!isOC())
                    .setEUtDiscount(Math.sqrt(structures))
                    .setDurationModifier(getDurationMultiplierFromRoughness());
            }

            @Nonnull
            @Override
            protected ParallelHelper createParallelHelper(@Nonnull GTRecipe recipe) {
                int structures = 1;
                if (mBioChamber != null) {
                    structures++;
                }
                if (mCoolingTower != null) {
                    structures++;
                }
                return super.createParallelHelper(recipe).setEUtModifier((float) Math.sqrt(structures))
                    .setChanceMultiplier(mRoughnessMultiplier);
            }
        };
    }

    private boolean isOC() {
        if (compatMode.isSet && compatMode.OCTier != 0) {
            return true;
        }
        if (mCoolingTower == null) return false;

        return mCoolingTower.isAllowedToWork();
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
            if (isOC()) {
                if (compatMode.isSet && compatMode.OCTier != 0) {
                    if (compatMode.coolantHatch != null) {
                        FluidStack tFluid = compatMode.OCTier == 1
                            ? GTModHandler.getDistilledWater(COOLANT_CONSUMED_PER_SEC)
                            : Materials.SuperCoolant.getFluid(COOLANT_CONSUMED_PER_SEC);
                        if (!drain(compatMode.coolantHatch, tFluid, true)) {
                            stopMachine(ShutDownReasonRegistry.outOfFluid(tFluid));
                        }
                    } else {
                        stopMachine(ShutDownReasonRegistry.STRUCTURE_INCOMPLETE);
                    }
                } else {
                    FluidStack tFluid = mCoolingTower.isTier1 ? GTModHandler.getDistilledWater(COOLANT_CONSUMED_PER_SEC)
                        : Materials.SuperCoolant.getFluid(COOLANT_CONSUMED_PER_SEC);
                    if (!mCoolingTower.drain(mCoolingTower.mCoolantInputHatch, tFluid, true)) {
                        stopMachine(ShutDownReasonRegistry.outOfFluid(tFluid));
                        return false;
                    }
                }
            }
            ticker = 0;
        }

        ticker++;

        return true;
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        if (mCoolingTower != null) {
            mCoolingTower.cancelRecipe(this);
        }
        if (mBioChamber != null) {
            mBioChamber.cancelRecipe(this);
        }
        super.stopMachine(reason);
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
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;

        // Save link data to data stick, very similar to Crafting Input Buffer.

        ItemStack dataStick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(dataStick, false, true)) return;

        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", "PCBFactory");
        tag.setInteger("x", aBaseMetaTileEntity.getXCoord());
        tag.setInteger("y", aBaseMetaTileEntity.getYCoord());
        tag.setInteger("z", aBaseMetaTileEntity.getZCoord());

        dataStick.stackTagCompound = tag;
        dataStick.setStackDisplayName(
            "PCB Factory Link Data Stick (" + aBaseMetaTileEntity
                .getXCoord() + ", " + aBaseMetaTileEntity.getYCoord() + ", " + aBaseMetaTileEntity.getZCoord() + ")");
        aPlayer.addChatMessage(new ChatComponentText("Saved Link Data to Data Stick"));
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return (int) (10000f * mRoughnessMultiplier);
    }

    private int getTier() {
        return mTier;
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
    protected long getActualEnergyUsage() {
        return (-this.lEUt * 10000) / Math.min(Math.max(1000, mEfficiency), 10000);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatTrans(
            aPlayer,
            inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEPCBFactoryGui(this);
    }

    public int getTraceSize() {
        return (int) ((1f / mRoughnessMultiplier) * 100f);
    }

    public void setTraceSize(int value) {
        mRoughnessMultiplier = 100f / (int) value;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        int mCurrentParallel = 0;

        info.add(translateToLocal("GT5U.multiblock.parallelism") + ": "
            + EnumChatFormatting.GREEN
            + mMaxParallel
            + ", "
            + translateToLocal("GT5U.multiblock.curparallelism")
            + ": "
            + EnumChatFormatting.GREEN
            + mCurrentParallel);

        info.add(translateToLocal("GT5U.multiblock.upgrades") + ": "
            + EnumChatFormatting.GREEN
            + (mBioChamber == null ? "" : "Bio Chamber ")
            + (mBioChamber != null && mCoolingTower != null ? ", " : "")
            + EnumChatFormatting.GREEN
            + (mCoolingTower == null ? ""
            : " Cooling Tower Tier " + EnumChatFormatting.GOLD + (mCoolingTower.isTier1 ? "1" : "2"))
            + (mBioChamber == null && mCoolingTower == null ? EnumChatFormatting.RED + "None" : ""));
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Circuit Board Fabricator")
            .addInfo("Tier of the machine determines the available recipes")
            .addInfo("Each tier and upgrade requires additional structures")
            .addInfo("Power consumption is multiplied by Sqrt(structures)")
            .addInfo("Tier 2 and 3 allow parallel by using extra nanites")
            .addInfo("Nanites have to be placed in a Nanite Containment Bus")
            .addInfo(
                "The formula for parallels is the " + TooltipHelper.parallelText("amount of nanites^0.75")
                    + ", rounded up")
            .addInfo("Maximum parallel is " + TooltipHelper.parallelText("256"))
            .addInfo("Recipes require a cooling upgrade to be overclocked")
            .addInfo(
                "Liquid Cooling uses " + EnumChatFormatting.BLUE
                    + "10 L/s"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.BLUE
                    + "distilled water"
                    + EnumChatFormatting.GRAY
                    + " and enables default overclocks")
            .addInfo(
                "Thermosink uses " + EnumChatFormatting.AQUA
                    + "10 L/s"
                    + EnumChatFormatting.GRAY
                    + " of "
                    + EnumChatFormatting.AQUA
                    + "Super Coolant"
                    + EnumChatFormatting.GRAY
                    + " and enables "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "perfect overclocks")
            .addInfo("Trace size can be changed to modify the material usage and machine speed")
            .addInfo("Configure Trace Size in UI")
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
                    + " TT energy hatch",
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
            .addStructureInfo(EnumChatFormatting.GOLD + "25" + EnumChatFormatting.GRAY + " Any Tiered Glass")
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
            .addStructureInfo(EnumChatFormatting.GOLD + "72" + EnumChatFormatting.GRAY + " Any Tiered Glass")
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
    public void onBlockDestroyed() {
        if (mBioChamber != null) {
            mBioChamber.removeController(this);
        }
        if (mCoolingTower != null) {
            mCoolingTower.removeController(this);
        }

        super.onBlockDestroyed();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mBioChamber != null) {
            NBTTagCompound bioChamber = new NBTTagCompound();
            bioChamber.setInteger("x", mBioChamberX);
            bioChamber.setInteger("y", mBioChamberY);
            bioChamber.setInteger("z", mBioChamberZ);
            aNBT.setTag("mBioChamber", bioChamber);
        }
        if (mCoolingTower != null) {
            NBTTagCompound coolingTower = new NBTTagCompound();
            coolingTower.setInteger("x", mCoolingTowerX);
            coolingTower.setInteger("y", mCoolingTowerY);
            coolingTower.setInteger("z", mCoolingTowerZ);
            aNBT.setTag("mCoolingTower", coolingTower);
        }
        if (compatMode.isSet) {
            compatMode.saveNBTData(aNBT);
        }
        aNBT.setFloat("mRoughnessMultiplier", mRoughnessMultiplier);
        aNBT.setByte("mTier", mTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("mSeparate")) {
            // backwards compatibility
            inputSeparation = aNBT.getBoolean("mSeparate");
        }

        if (aNBT.hasKey("mSetTier")) {
            // backwards compatibility
            boolean mBioUpgrade = aNBT.getBoolean("mBioUpgrade");
            boolean mOCTier1 = aNBT.getBoolean("mOCTier1Upgrade");
            boolean mOCTier2 = aNBT.getBoolean("mOCTier2Upgrade");
            // set compatibility mode if any of these upgrades are enabled
            compatMode.isSet = mBioUpgrade || mOCTier1 || mOCTier2;
            compatMode.bioUpgrade = mBioUpgrade;

            if (mOCTier1) {
                compatMode.OCX = aNBT.getInteger("mOCTier1OffsetX");
                compatMode.OCZ = aNBT.getInteger("mOCTier1OffsetZ");
                compatMode.OCTier = 1;
            }
            if (mOCTier2) {
                compatMode.OCX = aNBT.getInteger("mOCTier2OffsetX");
                compatMode.OCZ = aNBT.getInteger("mOCTier2OffsetZ");
                compatMode.OCTier = 2;
            }
        }
        // more backwards compatibility, but the NBT already contains the required data.
        if (aNBT.hasKey("compatMode")) {
            compatMode = new CompatMode(aNBT);
        }

        if (aNBT.hasKey("mBioChamber")) {
            NBTTagCompound bioChamber = aNBT.getCompoundTag("mBioChamber");
            mBioChamberX = bioChamber.getInteger("x");
            mBioChamberY = bioChamber.getInteger("y");
            mBioChamberZ = bioChamber.getInteger("z");
        }
        if (aNBT.hasKey("mCoolingTower")) {
            NBTTagCompound coolingTower = aNBT.getCompoundTag("mCoolingTower");
            mCoolingTowerX = coolingTower.getInteger("x");
            mCoolingTowerY = coolingTower.getInteger("y");
            mCoolingTowerZ = coolingTower.getInteger("z");
        }

        mRoughnessMultiplier = aNBT.getFloat("mRoughnessMultiplier");
        mTier = aNBT.getByte("mTier");
    }

    public boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (compatMode.isSet) {
            if (aTileEntity == null) return false;
            IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity == null) return false;
            if (aMetaTileEntity instanceof MTEHatchInput) {
                ((MTEHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                ((MTEHatchInput) aMetaTileEntity).mRecipeMap = null;
                compatMode.coolantHatch = (MTEHatchInput) aMetaTileEntity;
                return true;
            }
        }
        return false;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_ASSEMBLER;
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

    public void registerLinkedUnit(MTEPCBUpgradeBase<?> unit) {
        if (unit instanceof MTEPCBBioChamber) {
            if (mBioChamber != null && mBioChamber != unit) {
                mBioChamber.unlinkController(this);
            }
            mBioChamber = (MTEPCBBioChamber) unit;
            mBioChamberX = unit.getBaseMetaTileEntity()
                .getXCoord();
            mBioChamberY = unit.getBaseMetaTileEntity()
                .getYCoord();
            mBioChamberZ = unit.getBaseMetaTileEntity()
                .getZCoord();
        } else if (unit instanceof MTEPCBCoolingTower) {
            if (mCoolingTower != null && mCoolingTower != unit) {
                mCoolingTower.unlinkController(this);
            }
            mCoolingTower = (MTEPCBCoolingTower) unit;
            mCoolingTowerX = unit.getBaseMetaTileEntity()
                .getXCoord();
            mCoolingTowerY = unit.getBaseMetaTileEntity()
                .getYCoord();
            mCoolingTowerZ = unit.getBaseMetaTileEntity()
                .getZCoord();
        }
    }

    public void unregisterLinkedUnit(MTEPCBUpgradeBase<?> unit) {
        if (unit instanceof MTEPCBBioChamber) {
            mBioChamber = null;
        } else if (unit instanceof MTEPCBCoolingTower) mCoolingTower = null;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        CheckRecipeResult result = super.checkProcessing();
        if (result.wasSuccessful()) {
            if (mBioChamber != null && mBioChamber.isAllowedToWork()) {
                mBioChamber.addRecipe(this);
            }
            if (mCoolingTower != null && mCoolingTower.isAllowedToWork()) {
                mCoolingTower.addRecipe(this);
            }
        }
        return result;
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
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();

        // Display linked controller in Waila.
        if (tag.hasKey("mBioChamber")) {
            NBTTagCompound bioChamber = tag.getCompoundTag("mBioChamber");
            currenttip.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.pcb.linked_to_bio_chamber_at",
                    bioChamber.getInteger("x"),
                    bioChamber.getInteger("y"),
                    bioChamber.getInteger("z")));
        }
        if (tag.hasKey("mCoolingTower")) {
            NBTTagCompound coolingTower = tag.getCompoundTag("mCoolingTower");
            currenttip.add(
                EnumChatFormatting.AQUA + StatCollector.translateToLocalFormatted(
                    "GT5U.waila.pcb.linked_to_colling_tower_at",
                    coolingTower.getInteger("x"),
                    coolingTower.getInteger("y"),
                    coolingTower.getInteger("z")));
        }
        if (tag.hasKey("compatMode")) {
            CompatMode compat = new CompatMode(tag);
            if (compat.isSet) {
                currenttip.add(EnumChatFormatting.RED + StatCollector.translateToLocal("GT5U.waila.pcb.compat_mode"));
            }
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        if (mBioChamber != null) {
            NBTTagCompound bioChamber = new NBTTagCompound();
            bioChamber.setInteger("x", mBioChamberX);
            bioChamber.setInteger("y", mBioChamberY);
            bioChamber.setInteger("z", mBioChamberZ);
            tag.setTag("mBioChamber", bioChamber);
        }
        if (mCoolingTower != null) {
            NBTTagCompound coolingTower = new NBTTagCompound();
            coolingTower.setInteger("x", mCoolingTowerX);
            coolingTower.setInteger("y", mCoolingTowerY);
            coolingTower.setInteger("z", mCoolingTowerZ);
            tag.setTag("mCoolingTower", coolingTower);
        }
        if (compatMode.isSet) {
            compatMode.saveNBTData(tag);
        }
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
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

    private static class CompatMode {

        byte OCTier;
        int OCX;
        int OCZ;
        MTEHatchInput coolantHatch;

        boolean bioUpgrade;

        boolean isSet = false;

        public CompatMode() {}

        public CompatMode(NBTTagCompound aNBT) {
            NBTTagCompound compat = aNBT.getCompoundTag("compatMode");
            isSet = true;

            OCTier = compat.getByte("OCTier");
            OCX = compat.getInteger("OCX");
            OCZ = compat.getInteger("OCZ");

            bioUpgrade = compat.getBoolean("bioUpgrade");
        }

        public void saveNBTData(NBTTagCompound aNBT) {
            NBTTagCompound compat = new NBTTagCompound();
            compat.setByte("OCTier", OCTier);
            compat.setInteger("OCX", OCX);
            compat.setInteger("OCZ", OCZ);
            compat.setBoolean("bioUpgrade", bioUpgrade);
            aNBT.setTag("compatMode", compat);
        }
    }
}
