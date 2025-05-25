package gregtech.common.tileentities.machines.multi.nanochip.modules;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.NAC_MODULE;
import static gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyComplex.TOOLTIP_CC;
import static gtnhlanth.util.DescTextLocalization.addDotText;

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

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import gregtech.common.tileentities.machines.multi.nanochip.MTENanochipAssemblyModuleBase;
import gregtech.common.tileentities.machines.multi.nanochip.hatches.MTEHatchParticleSensor;
import gregtech.common.tileentities.machines.multi.nanochip.util.CircuitComponent;
import gregtech.common.tileentities.machines.multi.nanochip.util.ModuleStructureDefinition;
import gtPlusPlus.core.util.math.MathUtils;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class EtchingArray extends MTENanochipAssemblyModuleBase<EtchingArray> {

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

    public static final IStructureDefinition<EtchingArray> STRUCTURE_DEFINITION = ModuleStructureDefinition
        .<EtchingArray>builder()
        .addShape(STRUCTURE_PIECE_MAIN, ETCHING_STRUCTURE)
        // White casing block
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings8, 5))
        // Black casing block
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings8, 10))
        // Infinity Cooled Casings
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings8, 14))
        // Particle Beam Guidance Pipe Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings9, 14))
        // Enriched Holmium Frame box
        .addElement('E', ofFrame(Materials.EnrichedHolmium))
        // Non-Photonic Matter Exclusion Glass
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 3))
        // Black glass
        .addElement('G', ofBlock(GregTechAPI.sBlockTintedGlass, 3))
        // Beamline input
        .addElement(
            'H',
            buildHatchAdder(EtchingArray.class).hatchClass(MTEHatchInputBeamline.class)
                .atLeast(SpecialHatchElement.BeamlineInput)
                .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(10))
                .dot(1)
                .adder(EtchingArray::addBeamLineInputHatch)
                .build())
        // Particle Indicator Hatch
        .addElement(
            'I',
            lazy(
                t -> GTStructureUtility.<EtchingArray>buildHatchAdder()
                    .atLeast(SpecialHatchElement.ParticleSensor)
                    .dot(2)
                    .cacheHint(() -> "Particle Indicator")
                    .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(5))
                    .build()))
        .build();

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

        else return null;
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

    private enum SpecialHatchElement implements IHatchElement<EtchingArray> {

        ParticleSensor(EtchingArray::addParticleSensorToMachineList, MTEHatchParticleSensor.class) {

            @Override
            public long count(EtchingArray gtMetaTileEntityEtchingArray) {
                return gtMetaTileEntityEtchingArray.particleSensor != null ? 1 : 0;
            }
        },

        BeamlineInput(EtchingArray::addBeamLineInputHatch, MTEHatchInputBeamline.class) {

            @Override
            public long count(EtchingArray gtMetaTileEntityEtchingArray) {
                return gtMetaTileEntityEtchingArray.beamlineInput != null ? 1 : 0;
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<EtchingArray> adder;

        @SafeVarargs
        SpecialHatchElement(IGTHatchAdder<EtchingArray> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGTHatchAdder<? super EtchingArray> adder() {
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

    public EtchingArray(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected EtchingArray(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<EtchingArray> getStructureDefinition() {
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
        return survivialBuildPiece(
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
            .addInfo("Etches your Chip " + TOOLTIP_CC + "s")
            .addInfo("Outputs into the VCO with the same color as the input VCI")
            .addStructureInfo("Any base casing - Vacuum Conveyor Input")
            .addStructureInfo("Any base casing - Vacuum Conveyor Output")
            .addOtherStructurePart("Beamline Input Hatch", addDotText(1))
            .addOtherStructurePart("Particle Indicator", addDotText(2))
            .toolTipFinisher("GregTech");
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new EtchingArray(this.mName);
    }

    public static void registerLocalName(String unprocessedName, CircuitComponent component) {
        component.fallbackLocalizedName = "Etched " + unprocessedName;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.nanochipEtchingArray;
    }
}
