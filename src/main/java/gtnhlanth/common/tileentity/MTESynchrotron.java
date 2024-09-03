package gtnhlanth.common.tileentity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlockAdder;
import static gregtech.api.enums.GTValues.VN;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_OIL_CRACKER_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gtnhlanth.util.DescTextLocalization.addDotText;

import java.util.ArrayList;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.API.BorosilicateGlass;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.TickTime;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.render.TextureFactory;
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

public class MTESynchrotron extends MTEEnhancedMultiBlockBase<MTESynchrotron> implements ISurvivalConstructable {

    private static final IStructureDefinition<MTESynchrotron> STRUCTURE_DEFINITION;

    protected static final String STRUCTURE_PIECE_ENTRANCE = "entrance";
    protected static final String STRUCTURE_PIECE_BASE = "base";

    public static final int CONSUMED_FLUID = 32_000; // Fluid consumed per processed recipe, maybe increase with EU
    public static final int MIN_INPUT_FOCUS = 25; // Inclusive

    private ArrayList<MTEHatchInputBeamline> mInputBeamline = new ArrayList<>();
    private ArrayList<MTEHatchOutputBeamline> mOutputBeamline = new ArrayList<>();

    public ArrayList<BlockAntennaCasing> mAntennaCasings = new ArrayList<>();

    private static final int CASING_INDEX = GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings5, 14);

    private static final byte MIN_GLASS_TIER = 6;

    private int energyHatchTier;

    private int antennaeTier;

    private Byte glassTier;

    /*
     * c: Shielded accelerator casing v: Vacuum k: Superconducting coil d: Coolant Delivery casing
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
                    		"  ccc   ccccccccccccccccc           ",
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
                    		"     ccccc             ccccc        ",
                    		"    c-----cc         cc-----c       ",
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
                    		"      ccccc           cccccc        "

                    	},
                    	{
                    		"       ccccccc     cccccccccc       ",
                    		"     cc-------ccccc--------cccc     ",
                    		"    c---------kdkdk--------ccccccccc",
                    		"    c---------kdkdk--------ccccccccc",
                    		"    c---------kdkdk--------ccccccccc",
                    		"     cc-------ccccc--------cccc     ",
                    		"       ccccccc     cccccccc         "

                    	},
                    	{
                    		"        cccccccccccccccccccc        ",
                    		"      cc-------------------ccccccccc",
                    		"     cc---------------------------cg",
                    		"     c----------------------------cg",
                    		"     cc---------------------------cg",
                    		"       c-------------------ccccccccc",
                    		"        ccccccccccccccccccc         "

                    	},
                    	{
                    		"         ccccccccccccccccccc        ",
                    		"       cc-----------------cccccccccc",
                    		"       c--------------------------cg",
                    		"      cc---------------------------b",
                    		"       c--------------------------cg",
                    		"        c-----------------cccccccccc",
                    		"         ccccccccccccccccc          "

                    	},
                    	{
                    		"            ccccccccccccccc         ",
                    		"         ccc-------------ccccccccccc",
                    		"        cc------------------------cg",
                    		"        cc------------------------cg",
                    		"        cc------------------------cg",
                    		"         ccc-------------ccccccccccc",
                    		"            ccccccccccccc           "

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
                .addElement('e', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(Energy, 4)).dot(6).casingIndex(CASING_INDEX).build())
                .addElement('n', ofBlock(LanthItemList.NIOBIUM_CAVITY_CASING, 0))
                .addElement('a', ofBlockAdder(MTESynchrotron::addAntenna, LanthItemList.ANTENNA_CASING_T1, 0)) //Antenna Casings
                .addElement('i', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(InputHatch, 2)).dot(4).casingIndex(CASING_INDEX).build())
                .addElement('o', buildHatchAdder(MTESynchrotron.class).atLeast(ImmutableMap.of(OutputHatch, 2)).dot(5).casingIndex(CASING_INDEX).build())
                .addElement('v', buildHatchAdder(MTESynchrotron.class).hatchClass(MTEHatchInputBeamline.class).casingIndex(CASING_INDEX)
                        .dot(1).adder(MTESynchrotron::addBeamlineInputHatch).build())
                .addElement('b', buildHatchAdder(MTESynchrotron.class).hatchClass(MTEHatchOutputBeamline.class).casingIndex(CASING_INDEX)
                        .dot(2).adder(MTESynchrotron::addBeamlineOutputHatch).build())
                .addElement('g', BorosilicateGlass.ofBoroGlass((byte) 0, MIN_GLASS_TIER, Byte.MAX_VALUE, (te, t) ->  te.glassTier = t, te -> te.glassTier))
                .addElement('j',
                		buildHatchAdder(MTESynchrotron.class).atLeast(Maintenance).dot(3).casingIndex(CASING_INDEX)
                		.buildAndChain(LanthItemList.SHIELDED_ACCELERATOR_CASING, 0))

                .build();



    }

    // spotless:on

    /*
     * v = pi * lorentz^2 * sfreq sfreq = sw / 2pi sw = e * B / mass(e) * c v = (e * B * l^2) / (2 * mass(e) * c) =
     * 292.718624222 * l^2 * B
     */

    private float outputEnergy;
    private int outputRate;
    private int outputParticle;
    private float outputFocus;
    private float machineFocus;

    private int machineTemp;

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
            .addInfo("Controller block for the Synchrotron")
            .addInfo("Torus-shaped, accelerates electrons to produce high-energy electromagnetic radiation")
            .addInfo(DescTextLocalization.BLUEPRINT_INFO)
            .addInfo(DescTextLocalization.BEAMLINE_SCANNER_INFO)
            .addInfo("Valid Coolants:");

        // Valid coolant list
        for (String fluidName : BeamlineRecipeLoader.coolantMap.keySet()) {

            tt.addInfo(
                "- " + FluidRegistry.getFluid(fluidName)
                    .getLocalizedName(null));

        }

        tt.addInfo("Requires 32 kL/s of coolant")
            .addSeparator()
            .beginStructureBlock(36, 7, 34, true)
            .addController("Front middle")
            .addCasingInfoExactly(LanthItemList.SHIELDED_ACCELERATOR_CASING.getLocalizedName(), 676, false)
            .addCasingInfoExactly("Superconducting Coil Block", 90, false)
            .addCasingInfoExactly("Niobium Cavity Casing", 64, false)
            .addCasingInfoExactly(LanthItemList.COOLANT_DELIVERY_CASING.getLocalizedName(), 28, false)
            .addCasingInfoExactly("Borosilicate Glass Block (LuV+)", 16, false)
            .addCasingInfoExactly("Antenna Casing (must match)", 4, true)
            .addOtherStructurePart("Beamline Input Hatch", addDotText(1))
            .addOtherStructurePart("Beamline Output Hatch", addDotText(2))
            .addMaintenanceHatch(addDotText(3))
            .addInputHatch(addDotText(4))
            .addOutputHatch(addDotText(5))
            .addEnergyHatch(addDotText(6))

            .toolTipFinisher("GTNH: Lanthanides");
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

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean active, boolean aRedstone) {
        // Placeholder
        if (side == facing) {
            if (active) return new ITexture[] { casingTexturePages[1][14], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][14], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_OIL_CRACKER)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_OIL_CRACKER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][14] };
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_ENTRANCE, stackSize, hintsOnly, 16, 3, 1);
        buildPiece(STRUCTURE_PIECE_BASE, stackSize, hintsOnly, 16, 3, 0);

    }

    @Override
    public IStructureDefinition<MTESynchrotron> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    public boolean addEnergyInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchEnergy) {

            MTEHatchEnergy hatch = (MTEHatchEnergy) aMetaTileEntity;

            // First energy hatch added
            if (this.mEnergyHatches.size() == 0) this.energyHatchTier = hatch.mTier;

            // Disallow any hatches that don't match the tier of the first hatch added
            if (hatch.mTier != this.energyHatchTier) return false;

            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            return mEnergyHatches.add(hatch);
        }
        return false;
    }

    private boolean addAntenna(Block block, int meta) {

        if (block == null) return false;

        if (!(block instanceof BlockAntennaCasing)) return false;

        BlockAntennaCasing antennaBlock = (BlockAntennaCasing) block;

        int antennaTier = antennaBlock.getTier();

        // First antenna casing added
        if (this.mAntennaCasings.size() == 0) this.antennaeTier = antennaTier;

        if (antennaTier != this.antennaeTier) return false;

        return mAntennaCasings.add(antennaBlock);

    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {

        float inputEnergy = 0;
        float inputFocus = 0;
        float inputRate = 0;
        int inputParticleId = 0;

        machineFocus = 0;
        machineTemp = 0;

        outputEnergy = 0;
        outputFocus = 0;
        outputRate = 0;
        outputParticle = 0;

        float tempFactor = 0;

        float voltageFactor = 0;

        ArrayList<FluidStack> fluidList = this.getStoredFluids();

        if (fluidList.size() == 0) {

            this.stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.nocoolant"));

            return false;
        }

        this.mEfficiency = (10000 - (this.getIdealStatus() - this.getRepairStatus()) * 1000);
        this.mEfficiencyIncrease = 10000;

        if (this.getInputInformation() == null) {
            return false;
        }

        if (this.getInputInformation()
            .getEnergy() == 0) { // Only really applies if there's no input
            return false;
        }

        if (this.getInputInformation()
            .getFocus() < MIN_INPUT_FOCUS) {
            return false;
        }

        inputParticleId = this.getInputInformation()
            .getParticleId();

        Particle inputParticle = Particle.getParticleFromId(inputParticleId);

        if (!inputParticle.canAccelerate()) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.noaccel"));
            return false;
        }

        mMaxProgresstime = TickTime.SECOND;

        long voltage = this.getMaxInputVoltage();
        mEUt = (int) (-voltage / GTValues.V[(int) this.getInputVoltageTier()]
            * GTValues.VP[(int) this.getInputVoltageTier()]); // Multiply VP by amps

        outputParticle = 1; // Photon

        FluidStack primaryFluid = fluidList.get(0);

        int fluidTemperature;

        if (primaryFluid.isFluidEqual(new FluidStack(FluidRegistry.getFluid("ic2coolant"), 1))) {
            fluidTemperature = 60; // Default temp of 300 is unreasonable
        } else {
            fluidTemperature = primaryFluid.getFluid()
                .getTemperature();
        }

        machineTemp = fluidTemperature; // Solely for tricorder info

        machineFocus = getMachineFocus(fluidTemperature);

        inputFocus = this.getInputInformation()
            .getFocus();

        outputFocus = (inputFocus > machineFocus) ? ((inputFocus + machineFocus) / 2.5f)
            : inputFocus * (machineFocus / 100); // If input focus > machine focus, divide their sum by 2.5, else
                                                 // weigh the former by the latter. This punishes having too low a
                                                 // machine focus for low values of input focus
                                                 // E.g. An input focus of 50 requires a machine focus of 100 to get an
                                                 // output focus of 50,
                                                 // whereas an input focus of 60 only requires around 80
                                                 // In general, as input focus increases, output scales better with
                                                 // machine focus

        voltageFactor = getVoltageFactor(voltage, this.antennaeTier);

        inputEnergy = this.getInputInformation()
            .getEnergy();
        float mass = inputParticle.getMass();

        // Perhaps divide by mass somehow here too
        outputEnergy = (float) calculateOutputParticleEnergy(voltage, inputEnergy, this.antennaeTier); // maybe
                                                                                                       // adjust
                                                                                                       // behaviour here

        inputRate = this.getInputInformation()
            .getRate();

        outputRate = (int) (inputRate * getOutputRatetio(voltageFactor, this.antennaeTier));

        if (outputRate == 0) {
            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.low_input_rate"));
            return false;
        }

        if (Util.coolantFluidCheck(primaryFluid, CONSUMED_FLUID)) {

            stopMachine(SimpleShutDownReason.ofCritical("gtnhlanth.inscoolant"));
            return false;

        }

        primaryFluid.amount -= CONSUMED_FLUID;

        Fluid fluidOutput = BeamlineRecipeLoader.coolantMap.get(
            primaryFluid.getFluid()
                .getName());

        if (Objects.isNull(fluidOutput)) return false;

        FluidStack fluidOutputStack = new FluidStack(fluidOutput, CONSUMED_FLUID);

        if (Objects.isNull(fluidOutputStack)) return false;

        this.addFluidOutputs(new FluidStack[] { fluidOutputStack });

        outputAfterRecipe();

        return true;
    }

    private void outputAfterRecipe() {

        if (!mOutputBeamline.isEmpty()) {

            BeamLinePacket packet = new BeamLinePacket(
                new BeamInformation(outputEnergy, outputRate, outputParticle, outputFocus));

            for (MTEHatchOutputBeamline o : mOutputBeamline) {

                o.q = packet;
            }
        }
    }

    @Override
    public void stopMachine() {

        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;
        machineFocus = 0;
        machineTemp = 0;
        super.stopMachine();

    }

    @Override
    public void stopMachine(ShutDownReason reason) {

        outputFocus = 0;
        outputEnergy = 0;
        outputParticle = 0;
        outputRate = 0;
        machineFocus = 0;
        machineTemp = 0;
        super.stopMachine(reason);

    }

    private BeamInformation getInputInformation() {

        for (MTEHatchInputBeamline in : this.mInputBeamline) {

            if (in.q == null) return new BeamInformation(0, 0, 0, 0);
            // if (in.q == null) return new BeamInformation(10000, 10, 0, 90); // TODO temporary for testing purposes

            return in.q.getContent();
        }
        return null;
    }

    private static float getVoltageFactor(long mEU, int antennaTier) {

        // float factor = (float) Math.pow(1.00004, -mEU * Math.pow(antennaTier, 1.0/3.0) + 80000);
        float factor = (float) -Math.pow(1.1, -mEU / 2000 * Math.pow(antennaTier, 2.0 / 3.0)) + 1; // Strictly improves
                                                                                                   // with higher tier
                                                                                                   // antenna
        return (float) Math.max(1.0, factor);

    }

    /*
     * private static float getTemperatureFactor(int temperature) { float factor = (float) Math.pow(1.11, 0.18 *
     * temperature); return factor; }
     */
    private static double calculateOutputParticleEnergy(long voltage, double inputParticleEnergy, int antennaTier) {

        /*
         * Energy in general increases as input energy increases, with the relationship between the machine EUt and
         * energy being an negative exponential, with a maximum depending on both input particle energy and antenna
         * tier. The extent to which the output depends on the former is determined by the cbrt of the latter, meaning
         * that increases in antenna tier result in diminishing returns. In the same way, the curve of the output energy
         * vs. machine voltage exponential depends on antenna tier, with an increase in antenna tier resulting in a more
         * shallow curve up. The effect of this also increases with the tier. LaTeX:
         * -\frac{l^{1.11t^{\frac{1}{3}}}}{40000000}\cdot\left(0.15^{\frac{2}{t^{\frac{3}{2}}}}\right)^{\frac{x}{60768}}
         * \ +\ \frac{l^{1.11t^{\frac{1}{3}}}}{40000000}
         */

        double energy = (Math.pow(inputParticleEnergy, 1.13 * Math.pow(antennaTier, 4.0 / 9.0)) / 40_000_000)
            * (-Math.pow(Math.pow(0.15, 2.0 / (Math.pow(antennaTier, 5.0 / 2.0))), voltage / 60768.0) + 1); // In
                                                                                                            // keV

        return energy;

    }

    private static float getMachineFocus(int temperature) {

        return (float) Math.max(Math.min(Math.pow(1.5, -1.0 / 40.0 * (temperature - 480)), 90), 10);
    }

    // Punny, right?
    private static float getOutputRatetio(float voltageFactor, int antennaTier) {
        return (float) (voltageFactor / (10 / Math.pow(2.5, antennaTier))); // Scale ratio with antenna tier, such a
                                                                            // high
                                                                            // exponential should be fine so long as
                                                                            // there
                                                                            // are only few antenna tiers
    }

    @Override
    public String[] getStructureDescription(ItemStack arg0) {
        return DescTextLocalization.addText("Synchrotron.hint", 12);
    }

    @Override
    public String[] getInfoData() {

        long storedEnergy = 0;
        long maxEnergy = 0;
        for (MTEHatchEnergy tHatch : mEnergyHatches) {
            if (tHatch.isValid()) {
                storedEnergy += tHatch.getBaseMetaTileEntity()
                    .getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity()
                    .getEUCapacity();
            }
        }

        BeamInformation information = this.getInputInformation();

        return new String[] {
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
                + GTUtility.formatNumbers(getMaxInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*2A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + VN[GTUtility.getTier(getMaxInputVoltage())]
                + EnumChatFormatting.RESET,
            /* 5 */ StatCollector.translateToLocal("GT5U.multiblock.problems") + ": "
                + EnumChatFormatting.RED
                + (getIdealStatus() - getRepairStatus())
                + EnumChatFormatting.RESET
                + " "
                + StatCollector.translateToLocal("GT5U.multiblock.efficiency")
                + ": "
                + EnumChatFormatting.YELLOW
                + Float.toString(mEfficiency / 100.0F)
                + EnumChatFormatting.RESET
                + " %",

            /* 7 */ EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.info")
                + ": "
                + EnumChatFormatting.RESET,
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
                + 32_000
                + EnumChatFormatting.RESET
                + " kL/s", // 32 kL/s

            /* 8 */ EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.in_pre")
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": " // "Multiblock Beamline Input:"
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(information.getParticleId())
                    .getLocalisedName() // e.g. "Electron
                                        // (e-)"
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
            EnumChatFormatting.BOLD + StatCollector.translateToLocal("beamline.out_pre") // "Multiblock Beamline
                                                                                         // Output:"
                + ": "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.particle") + ": "
                + EnumChatFormatting.GOLD
                + Particle.getParticleFromId(this.outputParticle)
                    .getLocalisedName()
                + " "
                + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.energy") + ": "
                + EnumChatFormatting.DARK_RED
                + this.outputEnergy * 1000
                + EnumChatFormatting.RESET
                + " eV",
            StatCollector.translateToLocal(
                "beamline.focus") + ": " + EnumChatFormatting.BLUE + this.outputFocus + " " + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("beamline.amount") + ": "
                + EnumChatFormatting.LIGHT_PURPLE
                + this.outputRate, };
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {

        this.mInputBeamline.clear();
        this.mOutputBeamline.clear();

        this.mAntennaCasings.clear();

        this.mEnergyHatches.clear();
        this.energyHatchTier = 0;
        this.antennaeTier = 0;

        this.glassTier = 0;

        this.outputEnergy = 0;
        this.outputRate = 0;
        this.outputFocus = 0;
        this.outputParticle = 0;

        if (!checkPiece(STRUCTURE_PIECE_ENTRANCE, 16, 3, 1)) return false;
        if (!checkPiece(STRUCTURE_PIECE_BASE, 16, 3, 0)) return false;

        return this.mInputBeamline.size() == 1 && this.mOutputBeamline.size() == 1
            && this.mMaintenanceHatches.size() == 1
            && this.mEnergyHatches.size() == 4
            && this.glassTier >= MIN_GLASS_TIER;
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
