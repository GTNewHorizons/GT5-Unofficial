package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gtnhlanth.util.DescTextLocalization.addDotText;

import java.util.ArrayList;
import java.util.Objects;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureUtility;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.ExoticEnergyInputHelper;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReason;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gtnhlanth.common.beamline.BeamInformation;
import gtnhlanth.common.beamline.BeamLinePacket;
import gtnhlanth.common.beamline.Particle;
import gtnhlanth.common.block.BlockAntennaCasing;
import gtnhlanth.common.hatch.MTEHatchInputBeamline;
import gtnhlanth.common.hatch.MTEHatchOutputBeamline;
import gtnhlanth.common.register.LanthItemList;
import gtnhlanth.common.tileentity.recipe.beamline.BeamlineRecipeLoader;
import gtnhlanth.util.DescTextLocalization;
import gtnhlanth.util.Util;

public class MTESynchrotron extends MTEExtendedPowerMultiBlockBase<MTESynchrotron> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTESynchrotron> STRUCTURE_DEFINITION;

    protected static final String STRUCTURE_PIECE_ENTRANCE = "entrance";
    protected static final String STRUCTURE_PIECE_BASE = "base";

    public static final int CONSUMED_FLUID = 32_000; // Fluid consumed per processed recipe, maybe increase with EU
    public static final int MIN_INPUT_FOCUS = 25; // Inclusive

    private final ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private final ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    public ArrayList<BlockAntennaCasing> mAntennaCasings = new ArrayList<>();
    private static final int CASING_INDEX = 1662;

    private int energyHatchTier;
    private boolean usingExotic = false;
    private int antennaeTier = -1;
    private int glassTier = -1;
    private float machineFocus;
    private int machineTemp;
    private long energyHatchAmperage;

    private float outputEnergy;
    private int outputRate;
    private int outputParticleID;
    private float outputFocus;

    /*
     * c: Shielded accelerator casing
     * v: Vacuum
     * k: Superconducting coil
     * d: Coolant Delivery casing
     */

    // TODO: E > 1200eV for x-ray lithography
    // spotless:off
    static {

        STRUCTURE_DEFINITION = StructureDefinition.<MTESynchrotron>builder().addShape(
                STRUCTURE_PIECE_ENTRANCE,
                new String[][] {
                		{
                			"                                    ",
                			"  ccc                               ",
                			" cgggc                              ",
                        	" cgvgc                              ",
                        	" cgggc                              ",
                        	"  ccc                               "
                		}
                })
                .addShape(
                    STRUCTURE_PIECE_BASE,

                    new String[][] {
                    	{
                    		"                                    ",
                    		"  ccc                               ",
                    		" ccccc       cjjjjjc                ",
                    		" cc-cc      cjjc~cjjc               ",
                    		" ccccc       cjjjjjc                ",
                    		"  ccc                               ",
                    		"                                    "
                    	},
                    	{
                    		"                                    ",
                    		"  ccc      ccccccccccc              ",
                    		" c---c    ccc-------ccc             ",
                    		" c---c    ccc-------ccc             ",
                    		" c---c    ccc-------ccc             ",
                    		"  ccc      ccccccccccc              ",
                    		"                                    "
                    	},
                    	{
                    		"           ccccccccccc              ",
                    		"  ccc    cc-----------cc            ",
                    		" c---c  cc-------------cc           ",
                    		" c---c  cc-------------cc           ",
                    		" c---c  cc-------------cc           ",
                    		"  ccc    ccc---------ccc            ",
                    		"           ccccccccccc              "
                    	},
                    	{
                    		"         ccccccccccccccc            ",
                    		"  ccc  cc---------------cc          ",
                    		" c---ccc-----------------c          ",
                    		" c---ccc-----------------cc         ",
                    		" c---ccc-----------------c          ",
                    		"  ccc  cc---------------cc          ",
                    		"         ccccccccccccccc            ",

                    	},
                    	{
                    		"  ccc  cccccccccccccccccc           ",
                    		" ckkkccc-----------------cc         ",
                    		"ck---kc-------------------cc        ",
                    		"ck---kc--------------------c        ",
                    		"ck---kc-------------------cc        ",
                    		" ckkkccc-----------------cc         ",
                    		"  ccc  cccccccccccccccccc           "

                    	},
                    	{
                    		"  cccccccccccc     ccccccc          ",
                    		" cdddcc-------ccccc-------cc        ",
                    		"cd---d----------------------c       ",
                    		"cd---d----------------------c       ",
                    		"cd---d----------------------c       ",
                    		" cdddcc-------ccccc-------cc        ",
                    		"  cccccccccccc     ccccccc          ",
                    	},
                    	{
                    		"  ccccccccc           ccccc         ",
                    		" ckkkc-----cccc   cccc-----cc       ",
                    		"ck---k-------ccccccc--------c       ",
                    		"ck---k-------ccccccc---------c      ",
                    		"ck---k-------ccccccc--------c       ",
                    		" ckkkc-----cccc   cccc-----cc       ",
                    		"  ccccccccc           ccccc         "
                    	},
                    	{
                    		"  cccccccc             ccccc        ",
                    		" c--------cc         cc-----cc      ",
                    		"c----------cc       cc-------c      ",
                    		"c----------cc       cc-------c      ",
                    		"c----------cc       cc-------c      ",
                    		" c--------cc         cc-----cc      ",
                    		"  cccccccc             ccccc        "

                    	},
                    	{
                    		"  ccccccc               ccccc       ",
                    		" c-------c             c-----c      ",
                    		"c---------c           c-------c     ",
                    		"c---------c           c-------c     ",
                    		"c---------c           c-------c     ",
                    		" c-------c             c-----c      ",
                    		"  ccccccc               ccccc       "

                    	},
                    	{
                    		"  cccccc                 ccccc      ",
                    		" c------c               c-----c     ",
                    		"c--------c             c------c     ",
                    		"c--------c             c------c     ",
                    		"c--------c             c------c     ",
                    		" c------c               c-----c     ",
                    		"  cccccc                 ccccc      "

                    	},
                    	{
                    		"  ccccc                   cccc      ",
                    		" c-----c                 c----c     ",
                    		"c-------c               c------c    ",
                    		"c-------c               c------c    ",
                    		"c-------c               c------c    ",
                    		" c-----c                 c----c     ",
                    		"  ccccc                   cccc      "

                    	},
                    	{
                    		"  cccc                     ccc      ",
                    		" c----cc                 cc---cc    ",
                    		"c------c                 c-----c    ",
                    		"c------c                 c-----c    ",
                    		"c------c                 c-----c    ",
                    		" c----cc                 cc---cc    ",
                    		"  cccc                     ccc      "

                    	},
                    	{
                    		"  cccc                     cccc     ",
                    		" c---cc                   c----c    ",
                    		"c------c                 c-----c    ",
                    		"c------c                 c-----cc   ",
                    		"c------c                 c-----c    ",
                    		" c---cc                   cc---c    ",
                    		"  cccc                     cccc     "

                    	},
                    	{
                    		"  cccc                     cccc     ",
                    		" c---cc                   c----c    ",
                    		"c-----c                   c----cc   ",
                    		"c-----c                   c----cc   ",
                    		"c-----c                   c----cc   ",
                    		" c---cc                   cc---c    ",
                    		"  cccc                     cccc     "

                    	},
                    	{
                    		"  ccc                       ccc     ",
                    		" ckkkcc                   cckkkc    ",
                    		"ck---kc                   ck---kc   ",
                    		"ck---kc                   ck---kc   ",
                    		"ck---kc                   ck---kc   ",
                    		" ckkkcc                   cckkkc    ",
                    		"  ccc                       ccc     "

                    	},
                    	{
                    		"  cec                       cec     ",
                    		" cnanc                     cnanc    ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		" cnnnc                     cnnnc    ",
                    		"  ccc                       ccc     "

                    	},
                    	{
                    		"  cic                       cic     ",
                    		" cndnc                     cndnc    ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		" cndnc                     cndnc    ",
                    		"  coc                       coc     "

                    	},
                    	{
                    		"  cec                       cec     ",
                    		" cnanc                     cnanc    ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		"cn---nc                   cn---nc   ",
                    		" cnnnc                     cnnnc    ",
                    		"  ccc                       ccc     "

                    	},
                    	{
                    		"  ccc                       ccc     ",
                    		" ckkkcc                   cckkkc    ",
                    		"ck---kc                   ck---kc   ",
                    		"ck---kc                   ck---kc   ",
                    		"ck---kc                   ck---kc   ",
                    		" ckkkcc                   cckkkc    ",
                    		"  ccc                       ccc     "

                    	},
                    	{
                    		"  cccc                     cccc     ",
                    		" c----c                   c----c    ",
                    		"cc----c                   c----cc   ",
                    		"cc----c                   c----cc   ",
                    		"cc----c                   c----cc   ",
                    		" c---cc                   cc---c    ",
                    		"  cccc                     cccc     "

                    	},
                    	{
                    		"  cccc                     cccc     ",
                    		" c----c                   c----c    ",
                    		" c-----c                 c-----c    ",
                    		"cc-----c                 c-----cc   ",
                    		" c-----c                 c-----c    ",
                    		" c---cc                   cc---c    ",
                    		"  cccc                     cccc     "

                    	},
                    	{
                    		"   ccc                     ccc      ",
                    		" cc---cc                 cc---cc    ",
                    		" c-----c                 c-----c    ",
                    		" c-----c                 c-----c    ",
                    		" c-----c                 c-----c    ",
                    		" cc---cc                 cc---cc    ",
                    		"   ccc                     ccc      "

                    	},
                    	{
                    		"   cccc                   cccc      ",
                    		"  c----c                 c----c     ",
                    		" c------c               c------c    ",
                    		" c------c               c------c    ",
                    		" c------c               c------c    ",
                    		"  c----c                 c----c     ",
                    		"   cccc                   cccc      "

                    	},
                    	{
                    		"   ccccc                 ccccc      ",
                    		"  c-----c               c-----c     ",
                    		"  c------c             c------c     ",
                    		"  c------c             c------c     ",
                    		"  c------c             c------c     ",
                    		"  c-----c               c-----c     ",
                    		"   ccccc                 ccccc      "

                    	},
                    	{
                    		"    ccccc               ccccc       ",
                    		"   c-----c             c-----c      ",
                    		"  c-------c           c-------c     ",
                    		"  c-------c           c-------c     ",
                    		"  c-------c           c-------c     ",
                    		"   c-----c             c-----c      ",
                    		"    ccccc               ccccc       "

                    	},
                    	{
                    		"     ccccc             cccccc       ",
                    		"    c-----cc         cc------c      ",
                    		"   c-------cc       cc-------cc     ",
                    		"   c-------cc       cc-------cc     ",
                    		"   c-------cc       cc-------cc     ",
                    		"    c-----cc         cc------c      ",
                    		"     ccccc             cccccc       "

                    	},
                    	{
                    		"      ccccc           ccccccc       ",
                    		"    cc-----cccc   cccc-----ccc      ",
                    		"    c--------ccccccc--------cccc    ",
                    		"    c--------ccccccc--------cccc    ",
                    		"    c--------ccccccc--------cccc    ",
                    		"    cc-----cccc   cccc------cc      ",
                    		"      ccccc           ccccccc       "

                    	},
                    	{
                    		"       ccccccc     cccccccccc       ",
                    		"     cc-------ccccc--------cccc     ",
                    		"    c---------kdkdk--------ccccccccc",
                    		"    c---------kdkdk--------ccccccccc",
                    		"    c---------kdkdk--------ccccccccc",
                    		"     cc-------ccccc--------cccc     ",
                    		"       ccccccc     cccccccccc       "

                    	},
                    	{
                    		"        cccccccccccccccccccc        ",
                    		"      cc-------------------ccccccccc",
                    		"     cc---------------------------cg",
                    		"     c----------------------------cg",
                    		"     cc---------------------------cg",
                    		"      cc-------------------ccccccccc",
                    		"        cccccccccccccccccccc        "

                    	},
                    	{
                    		"         ccccccccccccccccccc        ",
                    		"       cc-----------------cccccccccc",
                    		"       c--------------------------cg",
                    		"      cc---------------------------b",
                    		"       c--------------------------cg",
                    		"       cc-----------------cccccccccc",
                    		"         ccccccccccccccccccc        "

                    	},
                    	{
                    		"            ccccccccccccccc         ",
                    		"         ccc-------------ccccccccccc",
                    		"        cc------------------------cg",
                    		"        cc------------------------cg",
                    		"        cc------------------------cg",
                    		"         ccc-------------ccccccccccc",
                    		"            ccccccccccccccc         "

                    	},
                    	{
                    		"                                    ",
                    		"           cccccccccccccccccc       ",
                    		"          ccc-kdkdk------ccccccccccc",
                    		"          cc--kdkdk------ccccccccccc",
                    		"          ccc-kdkdk------ccccccccccc",
                    		"           cccccccccccccccccc       "

                    	},
                    	{
                    		"                                    ",
                    		"                                    ",
                    		"             cccccccccccccccc       ",
                    		"            ccccccccccccccccc       ",
                    		"             cccccccccccccccc       ",
                    		"                                    ",
                    		"                                    "

                    	}

                   }

                ).addElement('c', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
                .addElement('k', ofBlock(GregTechAPI.sBlockCasings1, 15)) // Superconducting coils
                .addElement('d', ofBlock(LanthItemList.COOLANT_DELIVERY_CASING, 0))
                // Adder overriden due to ExoticEnergy originally calling its own adder, giving false positives
                .addElement('e', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(Energy.or(ExoticEnergy), 4)).adder(MTESynchrotron::addEnergyInputToMachineList).dot(6).casingIndex(CASING_INDEX).build())
                .addElement('n', ofBlock(LanthItemList.NIOBIUM_CAVITY_CASING, 0))
                .addElement('a', withChannel("antenna", StructureUtility.ofBlocksTiered(
                		MTESynchrotron::getAntennaBlockTier,
                		ImmutableList.of(
                				Pair.of(LanthItemList.ANTENNA_CASING_T1, 0),
                				Pair.of(LanthItemList.ANTENNA_CASING_T2, 0)),
                		-1, MTESynchrotron::setAntennaTier, MTESynchrotron::getAntennaTier)))
                .addElement('i', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(InputHatch, 2)).dot(4).casingIndex(CASING_INDEX).build())
                .addElement('o', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(OutputHatch, 2)).dot(5).casingIndex(CASING_INDEX).build())
                .addElement('v', buildHatchAdder(MTESynchrotron.class).hatchClass(MTEHatchInputBeamline.class).casingIndex(CASING_INDEX)
                        .dot(1).adder(MTESynchrotron::addBeamlineInputHatch).build())
                .addElement('b', buildHatchAdder(MTESynchrotron.class).hatchClass(MTEHatchOutputBeamline.class).casingIndex(CASING_INDEX)
                        .dot(2).adder(MTESynchrotron::addBeamlineOutputHatch).build())
                .addElement('g', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
                .addElement('j',
                		buildHatchAdder(MTESynchrotron.class).atLeast(Maintenance).dot(3).casingIndex(CASING_INDEX)
                		.buildAndChain(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))
                .build();
    }
    // spotless:on

    public MTESynchrotron(String aName) {
        super(aName);
    }

    public MTESynchrotron(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESynchrotron(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Particle Accelerator")
            .addInfo("Torus-shaped, accelerates electrons to produce high-energy electromagnetic radiation,")
            .addInfo("in the form of photons")
            .addInfo("Antenna Casings can be one of two tiers, upgrade them to improve output rate and energy scaling")
            .addInfo("Minimum input focus: " + MIN_INPUT_FOCUS)
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addInfo("Use a lower temperature coolant to improve output focus")
            .addInfo("Valid Coolants:");

        // Valid coolant list
        for (String fluidName : BeamlineRecipeLoader.coolantMap.keySet()) {
            tt.addInfo(
                "- " + FluidRegistry.getFluid(fluidName)
                    .getLocalizedName(null));
        }

        tt.addInfo("Requires 32 kL/s of coolant")
            .beginStructureBlock(36, 7, 34, true)
            .addController("Front middle")
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 676, false)
            .addCasingInfoExactly("Superconducting Coil Block", 90, false)
            .addCasingInfoExactly("Niobium Cavity Casing", 64, false)
            .addCasingInfoExactly(LanthItemList.COOLANT_DELIVERY_CASING.getLocalizedName(), 28, false)
            .addCasingInfoExactly("Any Tiered Glass (LuV+)", 16, false)
            .addCasingInfoExactly("Antenna Casing (must match)", 4, true)
            .addOtherStructurePart("Beamline Input Hatch", addDotText(1))
            .addOtherStructurePart("Beamline Output Hatch", addDotText(2))
            .addMaintenanceHatch(addDotText(3))
            .addInputHatch(addDotText(4))
            .addOutputHatch(addDotText(5))
            .addEnergyHatch(addDotText(6))
            .addSubChannelUsage("glass", "Glass Tier")
            .toolTipFinisher();
        return tt;
    }

    private boolean addBeamlineInputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchInputBeamline) {
            return this.mInputBeamline.add((MTEHatchInputBeamline) mte);
        }

        return false;
    }

    private boolean addBeamlineOutputHatch(IGregTechTileEntity te, int casingIndex) {
        if (te == null) return false;

        IMetaTileEntity mte = te.getMetaTileEntity();
        if (mte == null) return false;

        if (mte instanceof MTEHatchOutputBeamline) {
            return this.mOutputBeamline.add((MTEHatchOutputBeamline) mte);
        }

        return false;
    }

    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;

        boolean firstHatch = this.mEnergyHatches.isEmpty() && this.mExoticEnergyHatches.isEmpty();

        if (aMetaTileEntity instanceof MTEHatch hatch) {
            if (firstHatch) {
                this.energyHatchTier = hatch.mTier;
                this.energyHatchAmperage = hatch.maxWorkingAmperesIn();
            }

            // Disallow any hatches that don't match the tier of the first hatch added
            if (hatch.mTier != this.energyHatchTier) return false;
            if (hatch.maxWorkingAmperesIn() != this.energyHatchAmperage) // Prevent mixing amperages within a tier
                return false;

            if (aMetaTileEntity instanceof MTEHatchEnergy hatchNormal) {
                if (this.usingExotic) // usingExotic defaults to false, only set when known to be using exotics
                    return false; // If exotics are already being used, disallow non-exotics

                hatchNormal.updateTexture(aBaseCasingIndex);
                hatchNormal.updateCraftingIcon(this.getMachineCraftingIcon());
                return mEnergyHatches.add(hatchNormal);
            } else if (aMetaTileEntity instanceof MTEHatch hatchExotic
                && ExoticEnergyInputHelper.isExoticEnergyInput(aMetaTileEntity)) {
                    if (firstHatch) this.usingExotic = true;

                    if (!this.usingExotic) return false; // If normal hatches are already being used, disallow exotics

                    hatchExotic.updateTexture(aBaseCasingIndex);
                    hatchExotic.updateCraftingIcon(this.getMachineCraftingIcon());
                    return mExoticEnergyHatches.add(hatchExotic);
                } else return false; // Not an energy hatch
        } else return false; // Not a hatch of any kind
    }

    public void setAntennaTier(int t) {
        this.antennaeTier = t;
    }

    public int getAntennaTier() {
        return this.antennaeTier;
    }

    @Nullable
    public static Integer getAntennaBlockTier(Block block, int meta) {
        if (block == LanthItemList.ANTENNA_CASING_T1) return 1;
        else if (block == LanthItemList.ANTENNA_CASING_T2) return 2;
        else return null;
    }

    @NotNull
    @Override
    public CheckRecipeResult checkProcessing() {
        ArrayList<FluidStack> fluidList = this.getStoredFluids();
        if (fluidList.isEmpty()) {
            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.nocoolant"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        FluidStack fluidCoolant = fluidList.get(0);

        BeamInformation inputInfo = this.getInputInformation();
        if (inputInfo == null) return CheckRecipeResultRegistry.NO_RECIPE;

        float inputEnergy = inputInfo.getEnergy();
        Particle inputParticle = Particle.getParticleFromId(inputInfo.getParticleId());
        float inputFocus = inputInfo.getFocus();
        int inputRate = inputInfo.getRate();
        if (inputEnergy == 0 || inputFocus < MIN_INPUT_FOCUS) return CheckRecipeResultRegistry.NO_RECIPE;

        if (!inputParticle.canAccelerate()) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        this.machineTemp = Util.coolantFluidTemperature(fluidCoolant); // Used for tricorder too
        this.machineFocus = getMachineFocus(this.machineTemp);

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;
        this.mMaxProgresstime = TickTime.SECOND;
        this.lEUt = -GTValues.VP[GTUtility.getTier(this.getAverageInputVoltage())] * this.getMaxInputAmps();

        long voltage = this.getMaxInputEu();
        float voltageFactor = getVoltageFactor(voltage);
        this.outputEnergy = (float) calculateOutputParticleEnergy(voltage, inputEnergy, this.antennaeTier);

        this.outputParticleID = 1; // Photon

        /*
         * If input focus > machine focus, divide their sum by 2.5, else weigh the former by the latter.
         * This punishes having too low a machine focus for low values of input focus.
         * EX: An input focus of 50 requires a machine focus of 100 to get an output focus of 50,
         * whereas an input focus of 60 only requires around 80.
         * In general, as input focus increases, output scales better with machine focus
         */
        this.outputFocus = (inputFocus > this.machineFocus) ? ((inputFocus + this.machineFocus) / 2.5f)
            : inputFocus * (this.machineFocus / 100);

        this.outputRate = (int) (inputRate * getOutputRatio(voltageFactor, this.antennaeTier));

        if (this.outputRate == 0) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.low_input_eut"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        if (Util.coolantFluidCheck(fluidCoolant, CONSUMED_FLUID)) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inscoolant"));
            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        Fluid fluidOutput = BeamlineRecipeLoader.coolantMap.get(
            fluidCoolant.getFluid()
                .getName());
        if (Objects.isNull(fluidOutput)) return CheckRecipeResultRegistry.NO_RECIPE;

        fluidCoolant.amount -= CONSUMED_FLUID;
        FluidStack fluidOutputStack = new FluidStack(fluidOutput, CONSUMED_FLUID);
        this.addFluidOutputs(new FluidStack[] { fluidOutputStack });

        outputPacketAfterRecipe();
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    private void outputPacketAfterRecipe() {
        if (!this.mOutputBeamline.isEmpty()) {
            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(this.outputEnergy, this.outputRate, this.outputParticleID, this.outputFocus));
            for (MTEHatchOutputBeamline o : this.mOutputBeamline) {
                o.dataPacket = packet;
            }
        }
    }

    @Override
    public void stopMachine(@NotNull ShutDownReason reason) {
        this.outputFocus = 0;
        this.outputEnergy = 0;
        this.outputParticleID = 0;
        this.outputRate = 0;
        this.machineFocus = 0;
        this.machineTemp = 0;
        super.stopMachine(reason);
    }

    private BeamInformation getInputInformation() {
        for (MTEHatchInputBeamline in : this.mInputBeamline) {
            if (in.dataPacket == null) return new BeamInformation(0, 0, 0, 0);
            return in.dataPacket.getContent();
        }
        return null;
    }

    private static float getVoltageFactor(long mEU) {
        return (float) (Math.sqrt(mEU) / 1500);
    }

    /**
     * Calculates the energy of the output particle as a function of machine voltage, input particle's energy,
     * and the antenna tier.
     * LaTeX:
     * E_{Out} = \frac{E_{In}^{1.13*AntennaTier^{\frac{4}{9}}}}{40,000,000} *
     * (-0.15^{\frac{2*EU_{In}}{60768*AntennaTier^{2.5}}}+1)
     *
     * @return The particle energy, in kEV
     */
    private static double calculateOutputParticleEnergy(long voltage, double inputParticleEnergy, int antennaTier) {
        return (Math.pow(inputParticleEnergy, 1.13 * Math.pow(antennaTier, 4.0 / 9.0)) / 40_000_000)
            * (-(Math.pow(0.15, (2.0 * voltage) / ((Math.pow(antennaTier, 2.5) * 60768.0)))) + 1);

    }

    private static float getMachineFocus(int temperature) {
        return (float) Math.max(Math.min(Math.pow(1.5, -1.0 / 40.0 * (temperature - 480)), 90), 10);
    }

    private static float getOutputRatio(float voltageFactor, int antennaTier) {
        // Scale ratio with antenna tier, such a high exponential should be fine so long as
        // there are only a few antenna tiers
        return (float) (voltageFactor / (10.0 / Math.pow(2.5, antennaTier)));
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        this.mInputBeamline.clear();
        this.mOutputBeamline.clear();
        this.mAntennaCasings.clear();
        this.mEnergyHatches.clear();
        this.mExoticEnergyHatches.clear();

        this.energyHatchTier = 0;
        this.energyHatchAmperage = 0;
        this.usingExotic = false;
        this.antennaeTier = -1;
        this.glassTier = -1;

        this.outputEnergy = 0;
        this.outputRate = 0;
        this.outputFocus = 0;
        this.outputParticleID = 0;

        if (!checkPiece(STRUCTURE_PIECE_ENTRANCE, 16, 3, 1)) return false;
        if (!checkPiece(STRUCTURE_PIECE_BASE, 16, 3, 0)) return false;

        return this.mInputBeamline.size() == 1 && this.mOutputBeamline.size() == 1
            && this.antennaeTier > 0
            && (this.mEnergyHatches.size() == 4 || this.mExoticEnergyHatches.size() == 4)
            && this.glassTier >= VoltageIndex.LuV;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_ENTRANCE, stackSize, hintsOnly, 16, 3, 1);
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 16, 3, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        elementBudget = 200;
        if (mMachine) return -1;

        int build = survivialBuildPiece(STRUCTURE_PIECE_ENTRANCE, stackSize, 16, 3, 1, elementBudget, env, false, true);
        if (build >= 0) return build;

        return survivialBuildPiece(STRUCTURE_PIECE_BASE, stackSize, 16, 3, 0, elementBudget, env, false, true);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean active, boolean aRedstone) {
        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[12][126], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[12][126], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[12][126] };
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("Synchrotron.hint", 12);
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

        BeamInformation information = this.getInputInformation();
        if (information == null) {
            information = new BeamInformation(0, 0, 0, 0);
        }

        return new String[] {
            // from super()
            /* 1 */ StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(mProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(mMaxProgresstime / 20)
                + EnumChatFormatting.RESET
                + " s",
            /* 2 */ StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GTUtility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            /* 3 */ StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GTUtility.formatNumbers(getActualEnergyUsage())
                + EnumChatFormatting.RESET
                + " EU/t",
            /* 4 */ StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GTUtility.formatNumbers(getAverageInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + getMaxInputAmps()
                + "A)"
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getAverageInputVoltage())]
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
            /* 6 Pollution not included */
            // Beamline-specific
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.info") + ": " + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.focus") + ": " // Machine Focus:
                + EnumChatFormatting.BLUE
                + machineFocus
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.temperature") + ": " // Temperature:
                + EnumChatFormatting.DARK_RED
                + machineTemp
                + EnumChatFormatting.RESET
                + " K", // e.g. "137 K"
            StatCollector.translateToLocal("beamline.coolusage") + ": " // Coolant Usage:
                + EnumChatFormatting.AQUA
                + 32
                + EnumChatFormatting.RESET
                + " kL/s", // 32 kL/s

            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Input:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(information.getParticleId())
                    .getLocalisedName() // e.g. "Electron (e-)"
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + information.getEnergy()
                + EnumChatFormatting.RESET
                + " keV", // e.g. "10240 keV"
            StatCollector.translateToLocal("beamline.focus") + ": " // "Focus:"
                + EnumChatFormatting.BLUE
                + information.getFocus()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": " // "Amount:"
                + EnumChatFormatting.LIGHT_PURPLE
                + information.getRate(),

            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Output:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(this.outputParticleID)
                    .getLocalisedName()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": " // "Energy:"
                + EnumChatFormatting.DARK_RED
                + this.outputEnergy * 1000
                + EnumChatFormatting.RESET
                + " eV",
            StatCollector.translateToLocal("beamline.focus") + ": " // "Focus:"
                + EnumChatFormatting.BLUE
                + this.outputFocus
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": " // "Amount:"
                + EnumChatFormatting.LIGHT_PURPLE
                + this.outputRate };
    }

    @Override
    public IStructureDefinition<MTESynchrotron> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean supportsPowerPanel() {
        return false;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }
}
