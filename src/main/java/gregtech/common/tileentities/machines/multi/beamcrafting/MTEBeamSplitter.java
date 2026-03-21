package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_SPLITTER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_SPLITTER_ACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;

public class MTEBeamSplitter extends MTEBeamMultiBase<MTEBeamSplitter> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.

    private static final IStructureDefinition<MTEBeamSplitter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBeamSplitter>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "   EEE   ",
                "   ECE   ",
                "   E~E   "
            },{
                "    B    ",
                "   B B   ",
                "    B    "
            },{
                "    B    ",
                "   A A   ",
                "    B    "
            },{
                "    B    ",
                "   A A   ",
                "    B    "
            },{
                "    B    ",
                "   B B   ",
                "    B    "
            },{
                "   BAB   ",
                "  B   B  ",
                "   BBB   "
            },{
                "   BAB   ",
                " BB   BB ",
                "   BBB   "
            },{
                "  BAAAB  ",
                "BB     BB",
                "  BBBBB  "
            },{
                "EEEEEEEEE",
                "EDBDBDBDE",
                "EEEEEEEEE"
            }})
        //spotless:on
        .addElement('B', Casings.ShieldedAcceleratorCasing.asElement())
        .addElement('A', chainAllGlasses())
        .addElement(
            'C',
            buildHatchAdder(MTEBeamSplitter.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTEBeamSplitter::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            'D',
            buildHatchAdder(MTEBeamSplitter.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(3)
                .adder(
                    (splitter, te, casingIndex) -> splitter
                        .addAdvancedBeamlineOutputHatch(te, casingIndex, FundamentalForce.All))
                .build()) // adv beamline output hatch
        .addElement('E', Casings.GrateMachineCasing.asElement())
        .build();

    public MTEBeamSplitter(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEBeamSplitter(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEBeamSplitter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEBeamSplitter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_SPLITTER_ACTIVE)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Casings.ShieldedAcceleratorCasing.getCasingTexture(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_SPLITTER)
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
        int randomInt = (int) (Math.random() * 101);
        String beanOrBeamSplitter = StatCollector
            .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamsplitter.machinetype");
        if (randomInt == 69) {
            beanOrBeamSplitter = StatCollector
                .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamsplitter.machinetypefake");
        }
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(beanOrBeamSplitter)
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamsplitter.tooltip1"))
            .addInfo(
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.beamsplitter.tooltip2"))
            .beginStructureBlock(9, 3, 9, false)
            .addController(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcontroller"))
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcasing"),
                68,
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
                StatCollector
                    .translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeamouthatchfiltered"),
                4,
                false)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addTecTechHatchInfo()
            .toolTipFinisher(GTValues.AuthorHamCorp);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 2, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mInputBeamline.clear();
        mAdvancedOutputBeamline.clear();
        return checkPiece(STRUCTURE_PIECE_MAIN, 4, 2, 0);
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        this.mMaxProgresstime = TickTime.SECOND;
        BeamInformation inputInfo = this.getNthInputParticle(0);

        if (inputInfo == null || inputInfo.getRate() == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        outputPacketAfterRecipe(inputInfo);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void outputPacketAfterRecipe(BeamInformation inputInfo) {
        int numValidOutputs = 0;
        if (!this.mAdvancedOutputBeamline.isEmpty()) {
            for (MTEHatchAdvancedOutputBeamline o : this.mAdvancedOutputBeamline) {
                if (o.acceptedInputMap.getOrDefault(Particle.getParticleFromId(inputInfo.getParticleId()), false)) {
                    numValidOutputs++;
                }
            }
            if (numValidOutputs > 0) {
                for (MTEHatchAdvancedOutputBeamline o : this.mAdvancedOutputBeamline) {
                    if (o.acceptedInputMap.getOrDefault(Particle.getParticleFromId(inputInfo.getParticleId()), false)) {
                        BeamInformation outputInfo = new BeamInformation(
                            inputInfo.getEnergy(),
                            inputInfo.getRate() / numValidOutputs,
                            inputInfo.getParticleId(),
                            inputInfo.getFocus());
                        BeamLinePacket packet = new BeamLinePacket(outputInfo);
                        o.dataPacket = packet;
                    }
                }
            }
        }
    }
}
