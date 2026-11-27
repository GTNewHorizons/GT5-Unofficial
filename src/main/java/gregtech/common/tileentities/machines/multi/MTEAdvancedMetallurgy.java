package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.casing.Casings;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.Rotation;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.INEIPreviewModifier;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhintergalactic.tile.multi.elevatormodules.TileEntityModuleBase;

/**
 * Advanced Metallurgy multiblock to make alloys
 * Copied over from other multis for now, work in progress
 */
@IMetaTileEntity.SkipGenerateDescription
public class MTEAdvancedMetallurgy extends MTEEnhancedMultiBlockBase<MTEAdvancedMetallurgy>
    implements ISurvivalConstructable, INEIPreviewModifier, ICasingTextureProvider {

    /** List of project modules in this elevator */
    public ArrayList<TileEntityModuleBase> mProjectModuleHatches = new ArrayList<>();

    public List<MTEHatchInput> getElevatorInputHatches() {
        return mInputHatches;
    }

    /** Interval in which the modules will be supplied with power in ticks */
    private static final int MODULE_CHARGE_INTERVAL = 20;
    /** Multiplier for the internal EU buffer */
    private static final int INTERNAL_BUFFER_MULTIPLIER = 8;

    /** Index of the Space Elevator base casing */
    public static final int CASING_INDEX_BASE = 176;

    /** Name of the main structure piece */
    private static final String STRUCTURE_PIECE_MAIN = "main";

    /** Vertical offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_VERT_OFFSET = 0;
    /** Horizontal offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_HOR_OFFSET = 4;
    /** Depth offset of the main structure piece */
    private static final int STRUCTURE_PIECE_MAIN_DEPTH_OFFSET = 0;

    private int mCasingAmount;

    // spotless:off
    /** Structure definition of this machine */
    private static final IStructureDefinition<MTEAdvancedMetallurgy> STRUCTURE_DEFINITION =
        StructureDefinition.<MTEAdvancedMetallurgy>builder()
            .addShape(STRUCTURE_PIECE_MAIN, new String[][] {
                {"XXXX~XXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"},
                {"XXXXXXXXX"}
            })
            .addElement(
                'X',
                buildHatchAdder(MTEAdvancedMetallurgy.class)
                    .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                    .casingIndex(CASING_INDEX_BASE)
                    .hint(1)
                    .buildAndChain(
                        onElementPass(MTEAdvancedMetallurgy::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 0))))
            .build();
    // spotless:on

    private void onCasingAdded() {
        mCasingAmount++;
    }

    /**
     * Create a new Space Elevator
     *
     * @param aID           ID of the controller
     * @param aName         Name of the controller
     * @param aNameRegional Localized name of the controller
     */
    public MTEAdvancedMetallurgy(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    /**
     * Create a new Space Elevator
     *
     * @param aName Name of the controller
     */
    protected MTEAdvancedMetallurgy(String aName) {
        super(aName);
    }

    /**
     * Get a new meta tile entity of this controller
     *
     * @param aTileEntity this
     * @return New meta tile entity
     */
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvancedMetallurgy(this.mName);
    }

    /**
     * Load the controller data from NBT
     *
     * @param aNBT NBT data containing information
     */
    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
    }

    /**
     * Save the controller data to NBT
     *
     * @param aNBT NBT data to which will be saved
     */
    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
    }

    /**
     * Get the size of the internal energy buffer
     *
     * @return Size of the internal energy buffer
     */
    @Override
    public long maxEUStore() {
        return INTERNAL_BUFFER_MULTIPLIER * super.maxEUStore();
    }

    /**
     * Get the number of installed project modules
     *
     * @return Number of installed project modules
     */
    public int getNumberOfModules() {
        return mProjectModuleHatches != null ? mProjectModuleHatches.size() : 0;
    }

    /**
     * Get the chunk X coordinate in which the controller resides
     *
     * @return Chunk X coordinate
     */
    public int getChunkX() {
        assert getBaseMetaTileEntity() != null;
        return getBaseMetaTileEntity().getXCoord() >> 4;
    }

    /**
     * Get the chunk Z coordinate in which the controller resides
     *
     * @return Chunk Z coordinate
     */
    public int getChunkZ() {
        assert getBaseMetaTileEntity() != null;
        return getBaseMetaTileEntity().getZCoord() >> 4;
    }

    /**
     * Get the structure of the Space Elevator
     *
     * @return Structure definition
     */
    @Override
    public IStructureDefinition<MTEAdvancedMetallurgy> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    /**
     * Get possible alignments of this controller
     *
     * @return List of alignments that are possible or denied
     */
    @Override
    public IAlignmentLimits getAlignmentLimits() {
        // The elevator should only be buildable upright
        return IAlignmentLimits.Builder.allowAll()
            .deny(ForgeDirection.DOWN)
            .deny(ForgeDirection.UP)
            .deny(Rotation.UPSIDE_DOWN)
            .deny(Rotation.CLOCKWISE)
            .deny(Rotation.COUNTER_CLOCKWISE)
            .build();
    }

    /**
     * Construct the structure of Space Elevator
     *
     * @param stackSize Hologram projector item stack
     * @param hintsOnly Should only hints be displayed?
     */
    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            hintsOnly,
            STRUCTURE_PIECE_MAIN_HOR_OFFSET,
            STRUCTURE_PIECE_MAIN_VERT_OFFSET,
            STRUCTURE_PIECE_MAIN_DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int consumedBudget = survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            STRUCTURE_PIECE_MAIN_HOR_OFFSET,
            STRUCTURE_PIECE_MAIN_VERT_OFFSET,
            STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
        return consumedBudget;
    }

    /**
     * Check if the structure of this machine is valid
     *
     * @param aBaseMetaTileEntity This
     * @param aStack              Item stack present in the controller GUI
     * @return True if valid, else false
     */
    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        checkPiece(
            STRUCTURE_PIECE_MAIN,
            STRUCTURE_PIECE_MAIN_HOR_OFFSET,
            STRUCTURE_PIECE_MAIN_VERT_OFFSET,
            STRUCTURE_PIECE_MAIN_DEPTH_OFFSET,
            errors);
    }

    /**
     * Add a project module to the module list
     *
     * @param aTileEntity      Project module
     * @param aBaseCasingIndex Index of the casing texture it should take
     * @return True if input entity is a valid module and could be added, else false
     */
    public boolean addProjectModuleToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof TileEntityModuleBase) {
            return mProjectModuleHatches.add((TileEntityModuleBase) aMetaTileEntity);
        }
        return false;
    }

    /**
     * Callback that will be invoked when the controller is removed
     */
    @Override
    public void onRemoval() {
        if (mProjectModuleHatches != null && !mProjectModuleHatches.isEmpty()) {
            for (TileEntityModuleBase projectModule : mProjectModuleHatches) {
                projectModule.disconnect();
            }
        }
        super.onRemoval();
    }

    /**
     * Callback that will be invoked on post tick
     *
     * @param aBaseMetaTileEntity This
     * @param aTick               Tick
     */
    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if (aBaseMetaTileEntity.isAllowedToWork()) {
                // Recipe logic will come here later
            }
        }
    }

    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.TECTECH_MACHINES_FX_WHOOUM;
    }

    /**
     * Create the tooltip of this controller
     *
     * @return Tooltip builder
     */
    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(StatCollector.translateToLocal("gt.mbtt.machine_type.large_chemical_reactor"))
            .addMarkdown(new ResourceLocation("gregtech", "large-chemical-reactor"))
            .beginStructureBlock(9, 9, 9, false)
            .addController(StatCollector.translateToLocal("gt.mbtt.structure.front_center_2nd_layer"))
            .addCasing("8-22", Casings.ChemicallyInertMachineCasing.getLocalizedName(), false)
            .addCasing("1", Casings.PTFEPipeCasing.getLocalizedName(), false)
            .addCasing("1", StatCollector.translateToLocal("GT5U.structure.heating_coil"), false)
            .addEnergyHatch("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addMaintenanceHatch("1", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addInputAny("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addOutputAny("1+", StatCollector.translateToLocal("gt.mbtt.structure.any_casing"), 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("gt.mbtt.structure.heating_coil_any_tier_any_side"))
            .addSubChannel(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher();
        return tt;
    }

    /**
     * Get the texture of this controller
     *
     * @param aBaseMetaTileEntity This
     * @param side                Side for which the texture will be gotten
     * @param colorIndex          Color index
     * @param aActive             Flag if the controller is active
     * @return Texture array of this controller
     */
    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
                                 int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE,
            OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return casingTexturePages[1][48];
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.metalineRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic();
    }

    /**
     * Get the information data of this controller
     *
     * @return String, that contains the provided information data
     */
    @Override
    public String[] getInfoData() {
        return new String[] { "ig.infodata.hdr.operational_data",
            getRepairStatus() == getIdealStatus() ? "ig.infodata.maintenance.ok" : "ig.infodata.maintenance.problems",
            "---------------------------------------------" };
    }

    /**
     * @return True as the elevator doesn't have maintenance issues
     */
    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    /**
     * Will this machine explode in rain?
     *
     * @return False
     */
    @Override
    public boolean willExplodeInRain() {
        return false;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

}

