package gregtech.common.tileentities.machines.multi.beamcrafting;

import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_STABILIZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_BEAM_STABILIZER_ACTIVE;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.ArrayList;

import javax.annotation.Nullable;

import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.gui.modularui.multiblock.MTEBeamStabilizerGui;
import gregtech.common.misc.GTStructureChannels;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;

public class MTEBeamStabilizer extends MTEExtendedPowerMultiBlockBase<MTEBeamStabilizer>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.
    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    private static final IStructureDefinition<MTEBeamStabilizer> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEBeamStabilizer>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "       ",
                "       ",
                "  BBB  ",
                "  BCB  ",
                "  B~B  ",
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
                "  BBB  ",
                "  BDB  ",
                "  BBB  ",
                "       ",
                "       "
            }})
        //spotless:on
        .addElement(
            'B', // collider casing
            buildHatchAdder(MTEBeamStabilizer.class).atLeast(Energy, ExoticEnergy)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(10))
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings13, 10))
        .addElement('A', chainAllGlasses()) // new glass type todo: (?)
        .addElement(
            'C',
            buildHatchAdder(MTEBeamStabilizer.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTEBeamStabilizer::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            'D',
            buildHatchAdder(MTEBeamStabilizer.class).hatchClass(MTEHatchOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(3)
                .adder(MTEBeamStabilizer::addBeamLineOutputHatch)
                .build()) // beamline input hatch
        .build();

    private boolean addBeamLineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addBeamLineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }

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
    public boolean doRandomMaintenanceDamage() {
        // Cannot have maintenance issues, so do nothing.
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
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
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_STABILIZER_ACTIVE)
                        .extFacing()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_BEAM_STABILIZER)
                        .extFacing()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Beam Stabilizer")
            .addInfo(
                "Accepts and stores up to 100000 " + EnumChatFormatting.GREEN
                    + "Particles "
                    + EnumChatFormatting.GRAY
                    + "from ")
            .addInfo(
                "potentially intermittent high " + EnumChatFormatting.RED
                    + "Rate "
                    + EnumChatFormatting.GRAY
                    + "particle beam packets")
            .addInfo("and re-releases those particles as a stable beam at a ")
            .addInfo(EnumChatFormatting.RED + "Rate " + EnumChatFormatting.GRAY + "of your choosing")
            .addSeparator()
            .addInfo(
                "If a particle beam with a different " + EnumChatFormatting.GREEN
                    + "Particle"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.GOLD
                    + "Energy "
                    + EnumChatFormatting.GRAY
                    + "or ")
            .addInfo(
                EnumChatFormatting.AQUA + "Focus "
                    + EnumChatFormatting.GRAY
                    + "is sent to the machine during operation, it is")
            .addInfo("ignored UNLESS there are no currently stored " + EnumChatFormatting.GREEN + "Particles ")
            .beginStructureBlock(7, 7, 11, false)
            .addController("Front Center")
            .addCasingInfoExactly("Collider Casing", 109, false)
            .addCasingInfoExactly("Any Glass", 9, true)
            .addCasingInfoExactly("Beamline Input Hatch", 1, false)
            .addCasingInfoExactly("Beamline Output Hatch", 1, false)
            .addEnergyHatch("Any Collider Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .toolTipFinisher();
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
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mInputBeamline.clear();
        mOutputBeamline.clear();
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0);
    }

    @Nullable
    private BeamInformation getInputParticle() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        this.mMaxProgresstime = TickTime.SECOND;
        BeamInformation inputInfo = this.getInputParticle();

        cumulateStoredBeamPacket();
        outputPacketAfterRecipe(this.playerTargetBeamRate);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    int storedParticleID = 0;
    float storedBeamEnergy = 0;
    // Stored particle Amount
    int cumulativeBeamRate = 0;
    // for completeness. not used anywhere but beamline
    float storedBeamFocus = 0;

    private void cumulateStoredBeamPacket() {
        BeamInformation inputParticle = getInputParticle();

        // if input particle energy == 0, do nothing
        if (inputParticle.getEnergy() == 0) {
            return;
        }
        // if we reach this point, input particle energy is > 0, and is therefore a meaningful packet
        // if the same particle AND energy appear, add to accumulation
        if (this.storedBeamEnergy == inputParticle.getEnergy()
            && this.storedParticleID == inputParticle.getParticleId()) {
            this.cumulativeBeamRate += inputParticle.getRate();
            this.cumulativeBeamRate = Math.min(this.cumulativeBeamRate, 100000); // capped at 100_000 particles
            return;
        }
        // if the stored cumulativeBeamRate is 0, then update all cached values
        if (this.cumulativeBeamRate == 0) {
            this.storedBeamEnergy = inputParticle.getEnergy();
            this.storedBeamFocus = inputParticle.getFocus();
            this.storedParticleID = inputParticle.getParticleId();
            this.cumulativeBeamRate += inputParticle.getRate();
            this.cumulativeBeamRate = Math.min(this.cumulativeBeamRate, 100000); // capped at 100_000 particles
        }
        // if we reach this point, then the incoming packet is a different particle-energy combo than what is stored,
        // and there are still particles being output by the machine. therefore the input packet is just ignored
        // (voided)

    }

    public int playerTargetBeamRate = 100;

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
        BeamInformation inputParticle = getInputParticle();
        return inputParticle != null ? inputParticle.getRate() : 0;
    }

    @Override
    protected @NotNull MTEBeamStabilizerGui getGui() {
        return new MTEBeamStabilizerGui(this);
    }
}
