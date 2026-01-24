package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ETCHING_ARRAY_GLOW;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.gui.modularui.multiblock.MTENanochipAssemblyComplexGui.colorString;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.CASING_INDEX_WHITE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CCs;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCI;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_VCO;
import static gtnhlanth.util.DescTextLocalization.addHintNumber;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchParticleSensor;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleTypes;
import gtPlusPlus.core.util.math.MathUtils;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEtchingArrayModule extends MTENanochipAssemblyModuleBase<MTEEtchingArrayModule> {

    protected static final String STRUCTURE_PIECE_MAIN = "main";
    protected static final int ETCHING_OFFSET_X = 3;
    protected static final int ETCHING_OFFSET_Y = 4;
    protected static final int ETCHING_OFFSET_Z = 0;
    protected static final String[][] ETCHING_STRUCTURE = new String[][] {
        { "  EEE  ", " EBBBE ", " EBHBE ", " BBBBB " }, { "  AGA  ", " A   A ", " G F G ", "B     B" },
        { "  AGA  ", " A   A ", " G F G ", "B     B" }, { "  AGA  ", " A   A ", " G F G ", "B     B" },
        { "  AGA  ", " A   A ", " G F G ", "B     B" }, { "  AGA  ", " BCCCB ", " BCDCB ", "BBCCCBB" },
        { "  EEE  ", " EAEAE ", " EAIAE ", " BAEAB " } };

    private MTEHatchInputBeamline beamlineInput = null;
    private MTEHatchParticleSensor particleSensor = null;

    int requiredEnergy = 1;
    Particle requiredParticle = Particle.ALPHA;

    public static final IStructureDefinition<MTEEtchingArrayModule> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<MTEEtchingArrayModule>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ETCHING_STRUCTURE)
        // Nanochip Primary Casing
        .addElement('A', Casings.NanochipPrimaryCasing.asElement())
        // Nanochip Secondary Casing
        .addElement('B', Casings.NanochipSecondaryCasing.asElement())
        // Infinity Cooled Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 14))
        // Particle Beam Guidance Pipe Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 14))
        // Enriched Holmium Frame box
        .addElement('E', ofFrame(Materials.EnrichedHolmium))
        // Non-Photonic Matter Exclusion Glass
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 3))
        // Nanochip Glass
        .addElement('G', Casings.NanochipGlass.asElement())
        // Beamline input
        .addElement(
            'H',
            buildHatchAdder(MTEEtchingArrayModule.class).hatchClass(MTEHatchInputBeamline.class)
                .atLeast(SpecialHatchElement.BeamlineInput)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .hint(1)
                .adder(MTEEtchingArrayModule::addBeamLineInputHatch)
                .build())
        // Particle Indicator Hatch
        .addElement(
            'I',
            lazy(
                t -> GTStructureUtility.<MTEEtchingArrayModule>buildHatchAdder()
                    .atLeast(SpecialHatchElement.ParticleSensor)
                    .hint(2)
                    .cacheHint(() -> "Particle Indicator")
                    .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(5))
                    .build()))
        .build();

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ETCHING_ARRAY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(CASING_INDEX_WHITE) };
    }

    public boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline beamlineInput) {
            beamlineInput.updateTexture(casingIndex);
            this.beamlineInput = beamlineInput;
            return true;
        }

        return false;
    }

    @Nullable
    private BeamInformation getInputInformation() {
        if (this.beamlineInput == null) return null;

        if (this.beamlineInput.dataPacket == null) return new BeamInformation(0, 0, 0, 0);

        return this.beamlineInput.dataPacket.getContent();
    }

    private Particle inputParticle() {
        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo != null) return Particle.getParticleFromId(
            inputInfo.getParticle()
                .ordinal());

        else return Particle.ELECTRON;
    }

    public boolean addParticleSensorToMachineList(IGregTechTileEntity te, int aBaseCasingIndex) {
        if (te == null) return false;
        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte instanceof MTEHatchParticleSensor sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            this.particleSensor = sensor;
            return true;
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<MTEEtchingArrayModule> {

        ParticleSensor(MTEEtchingArrayModule::addParticleSensorToMachineList, MTEHatchParticleSensor.class) {

            @Override
            public long count(MTEEtchingArrayModule gtMetaTileEntityEtchingArray) {
                return gtMetaTileEntityEtchingArray.particleSensor != null ? 1 : 0;
            }
        },

        BeamlineInput(MTEEtchingArrayModule::addBeamLineInputHatch, MTEHatchInputBeamline.class) {

            @Override
            public long count(MTEEtchingArrayModule gtMetaTileEntityEtchingArray) {
                return gtMetaTileEntityEtchingArray.beamlineInput != null ? 1 : 0;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEEtchingArrayModule> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<MTEEtchingArrayModule> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super MTEEtchingArrayModule> adder() {
            return adder;
        }
    }

    @Override
    public @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;

        float inputEnergy = inputInfo.getEnergy();

        if (inputParticle() != requiredParticle) {
            return CheckRecipeResultRegistry.WRONG_PARTICLE;
        }

        if (inputEnergy <= requiredEnergy) {
            return CheckRecipeResultRegistry.LOW_ENERGY;
        }

        return CheckRecipeResultRegistry.SUCCESSFUL;

    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);
        // Update sensor hatch
        if (particleSensor != null) {
            particleSensor.updateRedstoneOutput(this.requiredParticle.ordinal());
        }
    }

    private long ticker = 0;

    public boolean onRunningTick(ItemStack aStack) {
        if (!super.onRunningTick(aStack)) {
            return false;
        }
        if (ticker % (5 * SECONDS) == 0) {
            updateRequiredPatricle();
            ticker = 0;
        }
        ticker++;
        return true;
    }

    private void updateRequiredPatricle() {
        int particle = MathUtils.randInt(1, 3);
        requiredParticle = switch (particle) {
            case 1 -> Particle.ELECTRON;
            case 2 -> Particle.ALPHA;
            case 3 -> Particle.POSITRON;
            default -> throw new IllegalStateException("Unexpected Particle: " + particle);
        };
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("particle")) requiredParticle = Particle.getParticleFromId(aNBT.getInteger("particle"));
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("particle", requiredParticle.ordinal());
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("particle", requiredParticle.getName());
        tag.setString("inputparticle", inputParticle().getName());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(EnumChatFormatting.LIGHT_PURPLE + "Particle Needed" + ": " + tag.getString("particle"));
        currentTip.add(EnumChatFormatting.LIGHT_PURPLE + "Current Particle" + ": " + tag.getString("inputparticle"));
    }

    public MTEEtchingArrayModule(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected MTEEtchingArrayModule(String aName) {
        super(aName);
    }

    @Override
    public ModuleTypes getModuleType() {
        return ModuleTypes.EtchingArray;
    }

    @Override
    public IStructureDefinition<MTEEtchingArrayModule> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        buildPiece(STRUCTURE_PIECE_MAIN, trigger, hintsOnly, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        // Should only construct the main structure, since the base structure is built by the nanochip assembly complex.
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            trigger,
            ETCHING_OFFSET_X,
            ETCHING_OFFSET_Y,
            ETCHING_OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        particleSensor = null;
        beamlineInput = null;
        // Check base structure
        if (!super.checkMachine(aBaseMetaTileEntity, aStack)) return false;
        // Now check module structure
        return checkPiece(STRUCTURE_PIECE_MAIN, ETCHING_OFFSET_X, ETCHING_OFFSET_Y, ETCHING_OFFSET_Z);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        return new MultiblockTooltipBuilder().addMachineType("NAC Module")
            .addInfo(NAC_MODULE)
            .addSeparator()
            .addInfo("Uses beamline particles to etch your Chip " + TOOLTIP_CCs)
            .addInfo(
                "Outputs are placed in the " + TOOLTIP_VCO
                    + " with the same "
                    + colorString()
                    + " as the input "
                    + TOOLTIP_VCI)
            .addInfo(
                "Every " + EnumChatFormatting.RED
                    + "5"
                    + EnumChatFormatting.GRAY
                    + " seconds, picks a random particle between "
                    + EnumChatFormatting.WHITE
                    + "Positron"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.WHITE
                    + "Electron"
                    + EnumChatFormatting.GRAY
                    + " and "
                    + EnumChatFormatting.WHITE
                    + "Alpha")
            .addInfo(
                "Use the " + EnumChatFormatting.YELLOW
                    + "Particle Indicator Hatch"
                    + EnumChatFormatting.GRAY
                    + " to read out this particle")
            .addInfo(
                "To start a recipe, the correct particle must be present in the " + EnumChatFormatting.YELLOW
                    + "Beamline Input Hatch")
            .addInfo("Has " + EnumChatFormatting.WHITE + EnumChatFormatting.UNDERLINE + "unlimited parallel")
            .addSeparator()
            .addInfo(
                EnumChatFormatting.LIGHT_PURPLE + ""
                    + EnumChatFormatting.ITALIC
                    + "The energy of a particle accelerator is used to etch even finer details into your chips")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .addOtherStructurePart("Beamline Input Hatch", addHintNumber(1))
            .addOtherStructurePart("Particle Indicator", addHintNumber(2))
            .toolTipFinisher();
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEtchingArrayModule(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Etched " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEtchingArray;
    }
}
