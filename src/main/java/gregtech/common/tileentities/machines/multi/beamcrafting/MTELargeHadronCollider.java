package gregtech.common.tileentities.machines.multi.beamcrafting;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAnyMeta;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_ACCELERATOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_ACCELERATOR_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_COLLIDER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_LHC_COLLIDER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static java.lang.Math.max;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.gui.modularui.multiblock.MTELargeHadronColliderGui;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.register.LanthItemList;

public class MTELargeHadronCollider extends MTEBeamMultiBase<MTELargeHadronCollider> implements ISurvivalConstructable {

    private static final int MACHINEMODE_ACCELERATOR = 0;
    private static final int MACHINEMODE_COLLIDER = 1;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String STRUCTURE_PIECE_EM = "EM";
    private static final String STRUCTURE_PIECE_WEAK = "Weak";
    private static final String STRUCTURE_PIECE_STRONG = "Strong";
    private static final String STRUCTURE_PIECE_GRAV = "Grav";

    private static final int CASING_INDEX_CENTRE = 1662; // Shielded Acc.

    private float outputEnergy;
    private int outputRate;
    private int outputParticleID;
    private float outputFocus;
    public double playerTargetBeamEnergyeV = 1_000_000_000;
    public int playerTargetAccelerationCycles = 10;

    BeamInformation initialParticleInfo = null;
    BeamInformation cachedOutputParticle = null;
    public int accelerationCycleCounter = 0;

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("machineMode", machineMode);
        if (cachedOutputParticle != null) {
            aNBT.setFloat("energy", cachedOutputParticle.getEnergy());
            aNBT.setInteger("rate", cachedOutputParticle.getRate());
            aNBT.setInteger("particleId", cachedOutputParticle.getParticleId());
            aNBT.setFloat("focus", cachedOutputParticle.getFocus());
        }
        if (initialParticleInfo != null) {
            aNBT.setFloat("iniParticleEnergy", initialParticleInfo.getEnergy());
            aNBT.setInteger("iniParticleRate", initialParticleInfo.getRate());
            aNBT.setInteger("iniParticleId", initialParticleInfo.getParticleId());
            aNBT.setFloat("iniParticleFocus", initialParticleInfo.getFocus());
        }
        aNBT.setDouble("playerBeamEnergy", playerTargetBeamEnergyeV);
        aNBT.setInteger("playerAccelCycles", playerTargetAccelerationCycles);
        aNBT.setInteger("accelerationCycleCounter", accelerationCycleCounter);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        machineMode = aNBT.getInteger("machineMode");
        if (aNBT.hasKey("energy") && aNBT.hasKey("rate") && aNBT.hasKey("particleId") && aNBT.hasKey("focus")) {
            cachedOutputParticle = new BeamInformation(
                aNBT.getFloat("energy"),
                aNBT.getInteger("rate"),
                aNBT.getInteger("particleId"),
                aNBT.getFloat("focus"));
        }
        if (aNBT.hasKey("iniParticleEnergy") && aNBT.hasKey("iniParticleRate")
            && aNBT.hasKey("iniParticleId")
            && aNBT.hasKey("iniParticleFocus")) {
            initialParticleInfo = new BeamInformation(
                aNBT.getFloat("iniParticleEnergy"),
                aNBT.getInteger("iniParticleRate"),
                aNBT.getInteger("iniParticleId"),
                aNBT.getFloat("iniParticleFocus"));
        }
        playerTargetBeamEnergyeV = aNBT.getDouble("playerBeamEnergy");
        playerTargetAccelerationCycles = aNBT.getInteger("playerAccelCycles");
        accelerationCycleCounter = aNBT.getInteger("accelerationCycleCounter");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.MULTI_LHC.mode." + machineMode);
    }

    @Override
    public int nextMachineMode() {
        if (machineMode == MACHINEMODE_ACCELERATOR) return MACHINEMODE_COLLIDER;
        else return MACHINEMODE_ACCELERATOR;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    private static final IStructureDefinition<MTELargeHadronCollider> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeHadronCollider>builder()

        .addShape(STRUCTURE_PIECE_MAIN, LHCStructureString.MAIN_STRUCTURE)
        .addShape(STRUCTURE_PIECE_EM, LHCStructureString.EM_MODULE)
        .addShape(STRUCTURE_PIECE_WEAK, LHCStructureString.WEAK_MODULE)
        .addShape(STRUCTURE_PIECE_STRONG, LHCStructureString.STRONG_MODULE)
        .addShape(STRUCTURE_PIECE_GRAV, LHCStructureString.GRAV_MODULE)

        .addElement(
            'C', // collider casing
            buildHatchAdder(MTELargeHadronCollider.class).atLeast(Energy, ExoticEnergy)
                .casingIndex(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(10))
                .hint(1)
                .buildAndChain(GregTechAPI.sBlockCasings13, 10))
        .addElement('A', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
        .addElement('D', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
        .addElement('E', lazy(t -> { // neonite saffron mango or whatever
            if (Mods.Chisel.isModLoaded()) {
                Block neonite = GameRegistry.findBlock(Mods.Chisel.ID, "neonite");
                return ofBlockAnyMeta(neonite, 1);
            } else {
                return ofBlockAnyMeta(Blocks.air);
            }
        }))
        .addElement(
            'F',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchInputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(2)
                .adder(MTELargeHadronCollider::addBeamLineInputHatch)
                .build()) // beamline input hatch
        .addElement(
            '1',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(3)
                .adder((collider, te, casingIndex) -> collider.addAdvancedBeamlineOutputHatch(te, casingIndex, 0))
                .build()) // EM beam output hatch
        .addElement(
            '2',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(4)
                .adder((collider, te, casingIndex) -> collider.addAdvancedBeamlineOutputHatch(te, casingIndex, 1))
                .build()) // Weak beam output hatch
        .addElement(
            '3',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(5)
                .adder((collider, te, casingIndex) -> collider.addAdvancedBeamlineOutputHatch(te, casingIndex, 2))
                .build()) // Strong beam output hatch
        .addElement(
            '4',
            buildHatchAdder(MTELargeHadronCollider.class).hatchClass(MTEHatchAdvancedOutputBeamline.class)
                .casingIndex(CASING_INDEX_CENTRE)
                .hint(6)
                .adder((collider, te, casingIndex) -> collider.addAdvancedBeamlineOutputHatch(te, casingIndex, 3))
                .build()) // Grav beam output hatch

        .addElement('B', ofBlock(GregTechAPI.sBlockCasings13, 11)) // CMS Casing (EM)
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings13, 12)) // ATLAS Casing (Weak)
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings13, 13)) // ALICE Casing (Strong)
        .addElement('O', ofBlock(GregTechAPI.sBlockCasings13, 14)) // LHCB Casing (Grav)
        .build();

    public MTELargeHadronCollider(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTELargeHadronCollider(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTELargeHadronCollider> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeHadronCollider(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if ((aActive) && (this.machineMode == 0)) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_ACCELERATOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_ACCELERATOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else if ((aActive) && (this.machineMode == 1)) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_COLLIDER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC_COLLIDER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings13, 10)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_LHC)
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
        tt.addMachineType(
            StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.machinetype"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip1"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip2"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip3"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip4"))
            .addSeparator()
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip5"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip6"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip7"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip8"))
            .addInfo(StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip9"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip10"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip11"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip12"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip13"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip14"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip15"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip16"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip17"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip18"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip19"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip20"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip21"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip22"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip23"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip24"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip25"))
            .addSeparator()
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip26"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip27"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip28"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip29"))
            .addInfo(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.LHC.tooltip30"))
            .addSeparator()
            .beginStructureBlock(109, 13, 122, false)
            .addController(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcontroller"))
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttcasing"),
                6034,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttehatch"),
                1,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttshieldacccasing"),
                16,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttshieldaccglass"),
                20,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttbeaminhatch"),
                1,
                false)
            .addCasingInfoExactly(
                StatCollector.translateToLocalFormatted("gt.blockmachines.multimachine.beamcrafting.ttanyneonite"),
                73,
                false)
            .addTecTechHatchInfo()
            .toolTipFinisher(GTValues.AuthorHamCorp);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 54, 4, 1);
        buildPiece(STRUCTURE_PIECE_EM, stackSize, hintsOnly, 5, -1, -113);
        if (stackSize.stackSize > 1) {
            buildPiece(STRUCTURE_PIECE_WEAK, stackSize, hintsOnly, 5, -1, -9);
        }
        if (stackSize.stackSize > 2) {
            buildPiece(STRUCTURE_PIECE_STRONG, stackSize, hintsOnly, 57, -1, -60);
        }
        if (stackSize.stackSize > 3) {
            buildPiece(STRUCTURE_PIECE_GRAV, stackSize, hintsOnly, -47, -1, -60);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int built = 0;

        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);

        built += survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 54, 4, 1, realBudget, env, false, true);
        built += survivalBuildPiece(STRUCTURE_PIECE_EM, stackSize, 5, -1, -113, realBudget, env, false, true);
        if (stackSize.stackSize > 1) {
            built += survivalBuildPiece(STRUCTURE_PIECE_WEAK, stackSize, 5, -1, -9, realBudget, env, false, true);
        }
        if (stackSize.stackSize > 2) {
            built += survivalBuildPiece(STRUCTURE_PIECE_STRONG, stackSize, 57, -1, -60, realBudget, env, false, true);
        }
        if (stackSize.stackSize > 3) {
            built += survivalBuildPiece(STRUCTURE_PIECE_GRAV, stackSize, -47, -1, -60, realBudget, env, false, true);
        }
        return built;
    }

    public boolean emEnabled;
    public boolean weakEnabled;
    public boolean strongEnabled;
    public boolean gravEnabled;

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        mInputBeamline.clear();
        mAdvancedOutputBeamline.clear();

        emEnabled = checkPiece(STRUCTURE_PIECE_EM, 5, -1, -113);
        weakEnabled = checkPiece(STRUCTURE_PIECE_WEAK, 5, -1, -9);
        strongEnabled = checkPiece(STRUCTURE_PIECE_STRONG, 57, -1, -60);
        gravEnabled = checkPiece(STRUCTURE_PIECE_GRAV, -47, -1, -60);

        return checkPiece(STRUCTURE_PIECE_MAIN, 54, 4, 1)
            && ((mExoticEnergyHatches.size() == 1) ^ (mEnergyHatches.size() == 1));

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
    public String[] getInfoData() {

        BeamLinePacket dataPacket = new BeamLinePacket(cachedOutputParticle);

        return new String[] {
            StatCollector.translateToLocalFormatted("tt.keyword.Content") + ": "
                + EnumChatFormatting.AQUA
                + (dataPacket != null ? dataPacket.getContentString() : 0),
            StatCollector.translateToLocalFormatted("tt.keyword.PacketHistory") + ": "
                + EnumChatFormatting.RED
                + (dataPacket != null ? dataPacket.getTraceSize() : 0), };
    }

    public double getCachedBeamEnergy() {
        return cachedOutputParticle != null ? cachedOutputParticle.getEnergy() : 0;
    }

    public int getCachedBeamRate() {
        return cachedOutputParticle != null ? cachedOutputParticle.getRate() : 0;
    }

    public final float MAXIMUM_PARTICLE_ENERGY_keV = 2_000_000_000; // 2TeV max
    public final double keVEURatio = 0.1 / 1000; // 1 EU = 0.1 eV, so 1 EU = 0.1/1000 keV
    public final float rateScaleFactor = 1.1F;

    public BeamInformation accelerateParticle(BeamInformation particle) {

        float inputEnergy = particle.getEnergy();
        int inputRate = particle.getRate();

        float outEnergy = inputEnergy;
        int outRate = inputRate;

        long machineVoltage = getAverageInputVoltage();
        // inputEnergy is in keV, playerTargetBeamEnergyeV is in eV did this so player can type '2G eV' instead of '2M
        // keV'
        if (inputEnergy <= playerTargetBeamEnergyeV / 1000) {
            outEnergy += (float) (Math.pow(accelerationCycleCounter + 1, 2) * this.mMaxProgresstime
                * machineVoltage
                * keVEURatio);
            if (outEnergy >= MAXIMUM_PARTICLE_ENERGY_keV) {
                return new BeamInformation(
                    MAXIMUM_PARTICLE_ENERGY_keV,
                    outRate,
                    particle.getParticleId(),
                    particle.getFocus());
            }

        } else {
            outRate = (int) Math.ceil(outRate * rateScaleFactor);
        }

        return new BeamInformation(outEnergy, outRate, particle.getParticleId(), particle.getFocus());
    }

    public long calculateEnergyCostAccelerator(BeamInformation particle) {
        long machineVoltage = getAverageInputVoltage();
        // counter starts at 0, so +1
        return (long) -(machineVoltage * Math.pow(accelerationCycleCounter + 1, 2) * particle.getRate());

    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        initialParticleInfo = null;
        cachedOutputParticle = null;
        accelerationCycleCounter = 0;
        machineMode = MACHINEMODE_ACCELERATOR;
        super.stopMachine(reason);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        int oldMachineMode = machineMode;
        if (aValue == 0) {
            machineMode = 0;
        } else {
            machineMode = 1;
        }
        if (oldMachineMode != machineMode) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) ((machineMode == 1) ? 1 : 0);
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {

        BeamInformation inputInfo = this.getInputInformation();

        if (inputInfo == null || inputInfo.getRate() == 0) return CheckRecipeResultRegistry.NO_RECIPE;

        if (machineMode == MACHINEMODE_ACCELERATOR) {

            this.mEfficiency = 10000;
            this.mEfficiencyIncrease = 10000;
            this.mMaxProgresstime = TickTime.SECOND;

            if (cachedOutputParticle == null) {
                // assign cachedOutputParticle, which will be accelerated in consequent processing cycles
                // also assign initialParticleInfo, which inputInfo is compared against every cycle
                cachedOutputParticle = inputInfo.copy();
                initialParticleInfo = inputInfo.copy();
                accelerationCycleCounter = 0;
            } else {
                // if cachedOutputParticle exists, then apply acceleration cycle logic
                if (!cachedOutputParticle.getParticle()
                    .canAccelerate()) {
                    // if the input beam is not charged particles, crash
                    stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }
                if (!initialParticleInfo.isEqual(inputInfo)) {
                    // if the input beam is ever modified or interrupted, crash the LHC
                    stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inputinterrupt"));
                    return CheckRecipeResultRegistry.NO_RECIPE;
                } else {

                    lEUt = calculateEnergyCostAccelerator(cachedOutputParticle);

                    if (accelerationCycleCounter < playerTargetAccelerationCycles) {
                        cachedOutputParticle = accelerateParticle(cachedOutputParticle);
                        accelerationCycleCounter += 1;
                    } else {
                        machineMode = MACHINEMODE_COLLIDER;
                    }
                }
            }
        } else {

            float inputEnergy = cachedOutputParticle.getEnergy();
            Particle inputParticle = Particle.getParticleFromId(cachedOutputParticle.getParticleId());
            int inputRate = cachedOutputParticle.getRate();

            if (inputEnergy == 0) return CheckRecipeResultRegistry.NO_RECIPE;
            float inputFocus = cachedOutputParticle.getFocus();

            if (!inputParticle.canAccelerate()) {
                stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            this.mEfficiency = 10000;
            this.mEfficiencyIncrease = 10000;
            this.mMaxProgresstime = TickTime.SECOND;

            // Generate output particle:

            // 1. Determine output energy (= collision energy)
            this.outputEnergy = (inputEnergy) * 2; // output energy = collision energy = input energy * 2

            // 2. Use collision energy to generate particle ID
            this.outputParticleID = GenerateOutputParticleID(this.outputEnergy);

            // 3. Use input rate and output particle rest mass to determine output rate
            Particle outputParticle = Particle.getParticleFromId(this.outputParticleID);
            float outputMass = outputParticle.getMass();
            this.outputRate = (int) max(0, (1 - (outputMass / this.outputEnergy)) * (inputRate));

            // 4. Focus is unused
            this.outputFocus = (int) (inputFocus);

            if (this.outputRate == 0) {
                stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.low_input_eut"));
                return CheckRecipeResultRegistry.NO_RECIPE;
            }

            outputPacketAfterRecipe();
        }
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private int GenerateOutputParticleID(float collisionEnergy) {

        // restMass is in MeV
        // beamline energies are in keV
        // :(

        double collisionEnergyMeV = collisionEnergy / 1000;

        int n = Particle.VALUES.length;

        double[] weights = new double[n];
        double totalWeight = 0.0;

        for (int i = 0; i < n; i++) {
            Particle p = Particle.getParticleFromId(i);
            double thresholdMeV = max(p.getMass(), 0.5); // massless particles have a threshold of 0.5 (arbitrary).
            // massive particles have a threshold equal to their rest mass.
            double w = (collisionEnergyMeV < thresholdMeV) ? 0.0 : p.getLHCWeight();

            if (w < 0 || Double.isNaN(w) || Double.isInfinite(w)) w = 0.0;

            weights[i] = w;
            totalWeight += w;
        }
        if (totalWeight <= 0.0) {
            return 0; // default to photons
        }

        double[] cumulative = new double[n];
        double run = 0.0;
        for (int i = 0; i < n; i++) {
            run += weights[i];
            cumulative[i] = run;
        }
        double r = Math.random() * totalWeight;
        int idx = java.util.Arrays.binarySearch(cumulative, r);
        if (idx < 0) idx = -idx - 1; // insertion point
        if (idx >= n) idx = n - 1; // safety clamp

        return idx;

    }

    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    private void outputPacketAfterRecipe() {
        if (!this.mAdvancedOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(this.outputEnergy, this.outputRate, this.outputParticleID, this.outputFocus));
            for (MTEHatchAdvancedOutputBeamline o : this.mAdvancedOutputBeamline) {
                if (o.acceptedInputMap.getOrDefault(Particle.getParticleFromId(this.outputParticleID), false)
                    == false) {
                    o.dataPacket = null;
                    continue;
                }
                o.dataPacket = packet;

            }
        }
    }

    @Override
    public void onDisableWorking() {
        initialParticleInfo = null;
        cachedOutputParticle = null;
        accelerationCycleCounter = 0;
        machineMode = MACHINEMODE_ACCELERATOR;
        super.onDisableWorking();
    }

    @Override
    protected boolean forceUseMui2() {
        return true;
    }

    @Override
    protected @NotNull MTELargeHadronColliderGui getGui() {
        return new MTELargeHadronColliderGui(this);
    }

    protected SoundResource getProcessStartSound() {
        return SoundResource.GT_MACHINES_LHC_SPIN_UP;
    }

}
