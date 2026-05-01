package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_STABILIZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_STABILIZER_ACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.MTEBeamStabilizerGui;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEBeamStabilizer extends MTEBeamMultiBase<MTEBeamStabilizer> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int ShieldedAccCasingTextureID = Casings.ShieldedAcceleratorCasing.getTextureId(); // Shielded
                                                                                                            // Acc.

    public int playerTargetBeamRate = 100;
    private int storedParticleID = 0;
    private float storedBeamEnergy = 0;
    // Stored particle Amount
    private int cumulativeBeamRate = 0;
    // for completeness. not used anywhere but beamline
    private float storedBeamFocus = 0;
    private int MAX_STORED_PARTICLES = 100_000;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("storedParticleID", storedParticleID);
        aNBT.setFloat("storedBeamEnergy", storedBeamEnergy);
        aNBT.setInteger("cumulativeBeamRate", cumulativeBeamRate);
        aNBT.setFloat("storedBeamFocus", storedBeamFocus);
        aNBT.setInteger("playerTargetBeamRate", playerTargetBeamRate);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        storedParticleID = aNBT.getInteger("storedParticleID");
        storedBeamEnergy = aNBT.getFloat("storedBeamEnergy");
        cumulativeBeamRate = aNBT.getInteger("cumulativeBeamRate");
        storedBeamFocus = aNBT.getFloat("storedBeamFocus");
        playerTargetBeamRate = aNBT.getInteger("playerTargetBeamRate");
    }

    private static final IStructureDefinition<MTEBeamStabilizer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBeamStabilizer>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "       ",
                "       ",
                "  EEE  ",
                "  ECE  ",
                "  E~E  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "  BBB  ",
                " BBABB ",
                " BB BB ",
                " BBBBB ",
                "  BBB  ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "  BBB  ",
                " B B B ",
                "B BAB B",
                "BBB BBB",
                "B BBB B",
                " B B B ",
                "  BBB  "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  BAB  ",
                "  B B  ",
                "  BBB  ",
                "       ",
                "       "
            },{
                "       ",
                "       ",
                "  EEE  ",
                "  EDE  ",
                "  EEE  ",
                "       ",
                "       "
            }})
        //spotless:on
        .addElement('B', Casings.ShieldedAcceleratorCasing.asElement())
        .addElement('A', chainAllGlasses())
        .addElement(
            'C',
            buildHatchAdder(MTEBeamStabilizer.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(ShieldedAccCasingTextureID)
                .hint(2)
                .adder(MTEBeamStabilizer::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            'D',
            buildHatchAdder(MTEBeamStabilizer.class).hatchClass(MTEHatchOutputBeamline.class)
                .casingIndex(ShieldedAccCasingTextureID)
                .hint(3)
                .adder(MTEBeamStabilizer::addBeamLineOutputHatch)
                .build()) // beamline output hatch
        .addElement('E', Casings.GrateMachineCasing.asElement())
        .build();

    public MTEBeamStabilizer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamStabilizer(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBeamStabilizer> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBeamStabilizer(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_STABILIZER_ACTIVE)
                        .extFacing()
                        .build() };
            } else {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_STABILIZER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture() };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(
            StatCollector
                .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.machinetype"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip1"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip2"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip3"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip4"))
            .addSeparator()
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip5"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip6"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamstabilizer.tooltip7"))
            .beginStructureBlock(7, 7, 11, false)
            .addController(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcontroller"))
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcasing"),
                109,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanyglass"),
                9,
                true)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeaminhatch"),
                1,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeamouthatch"),
                1,
                false)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher(GTAuthors.AuthorHamCorp, GTAuthors.Authorzub);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        mInputBeamline.clear();
        mOutputBeamline.clear();
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0, errors)) return;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        cumulateStoredBeamPacket();
        boolean packetStored = this.cumulativeBeamRate > 0;
        if (packetStored) {
            this.mMaxProgresstime = TickTime.SECOND;
            outputPacketAfterRecipe(this.playerTargetBeamRate);
            return CheckRecipeResultRegistry.SUCCESSFUL;
        }
        return CheckRecipeResultRegistry.NO_RECIPE;
    }

    private void cumulateStoredBeamPacket() {
        BeamInformation inputParticle = getNthInputParticle(0);
        if (inputParticle.getEnergy() == 0) return;

        if (this.storedBeamEnergy == inputParticle.getEnergy()
            && this.storedParticleID == inputParticle.getParticleId()) {
            this.cumulativeBeamRate += inputParticle.getRate();
            this.cumulativeBeamRate = Math.min(this.cumulativeBeamRate, MAX_STORED_PARTICLES);
            return;
        }

        if (this.cumulativeBeamRate == 0) {
            this.storedBeamEnergy = inputParticle.getEnergy();
            this.storedBeamFocus = inputParticle.getFocus();
            this.storedParticleID = inputParticle.getParticleId();
            this.cumulativeBeamRate += inputParticle.getRate();
            this.cumulativeBeamRate = Math.min(this.cumulativeBeamRate, MAX_STORED_PARTICLES);
        }
    }

    private void outputPacketAfterRecipe(int rate) {
        if (!this.mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(
                    this.storedBeamEnergy,
                    Math.min(rate, this.cumulativeBeamRate),
                    this.storedParticleID,
                    this.storedBeamFocus));

            this.cumulativeBeamRate -= rate;
            if (this.cumulativeBeamRate < 0) {
                this.cumulativeBeamRate = 0;
            }
            for (MTEHatchOutputBeamline o : this.mOutputBeamline) {
                o.dataPacket = packet;
            }
        }
    }

    public int getCachedBeamRate() {
        BeamInformation inputParticle = getNthInputParticle(0);
        return inputParticle != null ? inputParticle.getRate() : 0;
    }

    @Override
    protected @NotNull MTEBeamStabilizerGui getGui() {
        return new MTEBeamStabilizerGui(this);
    }
}
