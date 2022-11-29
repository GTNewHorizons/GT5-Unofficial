package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.VN;
import static gregtech.api.enums.Textures.BlockIcons.*;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.loader.Loaders;
import goodgenerator.util.MyRecipeAdder;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;

public class ComponentAssemblyLine extends GT_MetaTileEntity_EnhancedMultiBlockBase<ComponentAssemblyLine>
        implements ISurvivalConstructable {

    private int casingTier;
    private final double log4 = Math.log(4);
    private long EU_per_tick = 0;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<ComponentAssemblyLine> STRUCTURE_DEFINITION =
            StructureDefinition.<ComponentAssemblyLine>builder()
                    .addShape(STRUCTURE_PIECE_MAIN, new String[][] {
                        {
                            "         ",
                            "   III   ",
                            " HHI~IHH ",
                            "HH III HH",
                            "H       H",
                            "H       H",
                            "H  JJJ  H",
                            "H  N N  H",
                            "H  N N  H",
                            "HHHHHHHHH"
                        },
                        {
                            "         ",
                            " EHHHHHE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "AG  C  GA",
                            "AG     GA",
                            "AG     GA",
                            "AG HHH GA",
                            "AG     GA",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "HG  C  GH",
                            "HG     GH",
                            "HG     GH",
                            "HG HHH GH",
                            "HG     GH",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "AG  C  GA",
                            "AG     GA",
                            "AG     GA",
                            "AG HHH GA",
                            "AG     GA",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "HG  C  GH",
                            "HG     GH",
                            "HG     GH",
                            "HG HHH GH",
                            "HG     GH",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "AG  C  GA",
                            "AG     GA",
                            "AG     GA",
                            "AG HHH GA",
                            "AG     GA",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "HG  C  GH",
                            "HG     GH",
                            "HG     GH",
                            "HG HHH GH",
                            "HG     GH",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "   BBB   ",
                            " EL   LE ",
                            "E GGDGG E",
                            "HGG D GGH",
                            "AG  C  GA",
                            "AG     GA",
                            "AG     GA",
                            "AG HHH GA",
                            "AG     GA",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A  N N  A",
                            "MHHHHHHHM"
                        },
                        {
                            "   HBH   ",
                            " EL   LE ",
                            "E       E",
                            "HC     CH",
                            "AC     CA",
                            "AC     CA",
                            "A D   D A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "         ",
                            " EHHHHHE ",
                            "E       E",
                            "H       H",
                            "A       A",
                            "A       A",
                            "A       A",
                            "A  HHH  A",
                            "A       A",
                            "MHHHHHHHM"
                        },
                        {
                            "         ",
                            "         ",
                            " HHHHHHH ",
                            "HH     HH",
                            "H       H",
                            "H       H",
                            "H       H",
                            "H       H",
                            "H  KKK  H",
                            "HHHHHHHHH"
                        }
                    })
                    .addElement(
                            'A',
                            ofChain(
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 5),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 13),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 14),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks", 15),
                                    ofBlockUnlocalizedName("bartworks", "BW_GlasBlocks2", 0)))
                    .addElement('H', ofBlock(GregTech_API.sBlockCasings8, 7))
                    .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 5))
                    .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 9))
                    .addElement('G', ofBlock(GregTech_API.sBlockCasings9, 0))
                    .addElement('E', ofBlock(GregTech_API.sBlockCasings9, 1))
                    .addElement('F', ofBlock(GregTech_API.sBlockCasings4, 1))
                    .addElement(
                            'B',
                            ofBlocksTiered(
                                    (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : -1,
                                    IntStream.range(0, 14)
                                            .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                                            .collect(Collectors.toList()),
                                    -1,
                                    (t, meta) -> t.casingTier = meta,
                                    t -> t.casingTier))
                    .addElement(
                            'J',
                            GT_StructureUtility.buildHatchAdder(ComponentAssemblyLine.class)
                                    .atLeast(InputBus)
                                    .dot(1)
                                    .casingIndex(183)
                                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
                    .addElement(
                            'K',
                            GT_StructureUtility.buildHatchAdder(ComponentAssemblyLine.class)
                                    .atLeast(OutputBus)
                                    .dot(2)
                                    .casingIndex(183)
                                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
                    .addElement(
                            'L',
                            GT_StructureUtility.buildHatchAdder(ComponentAssemblyLine.class)
                                    .atLeast(Energy, ExoticEnergy)
                                    .dot(3)
                                    .casingIndex(183)
                                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
                    .addElement(
                            'I',
                            GT_StructureUtility.buildHatchAdder(ComponentAssemblyLine.class)
                                    .atLeast(Maintenance)
                                    .dot(4)
                                    .casingIndex(183)
                                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
                    .addElement(
                            'M',
                            GT_StructureUtility.buildHatchAdder(ComponentAssemblyLine.class)
                                    .atLeast(InputHatch)
                                    .dot(5)
                                    .casingIndex(183)
                                    .buildAndChain(GregTech_API.sBlockCasings8, 7))
                    .addElement('N', GT_StructureUtility.ofFrame(Materials.TungstenSteel))
                    .build();

    public ComponentAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public ComponentAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 2, 0);
    }

    @Override
    public IStructureDefinition<ComponentAssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("High-Capacity Component Assembler")
                .addInfo("Controller block for the Component Assembly Line.")
                .addInfo("Assembles basic components (motors, pumps, etc.) in large batches.")
                .addInfo("The " + EnumChatFormatting.BOLD + EnumChatFormatting.YELLOW
                        + "Special Component Assembly Line Casing" + EnumChatFormatting.RESET + EnumChatFormatting.GRAY)
                .addInfo("limits the recipes the machine can perform. See the NEI pages for details.")
                .addInfo("Supports " + EnumChatFormatting.BLUE + "Tec" + EnumChatFormatting.DARK_BLUE + "Tech"
                        + EnumChatFormatting.GRAY + " laser and multi-amp hatches!")
                .addInfo("Supports overclocking beyond MAX!")
                .addInfo(EnumChatFormatting.ITALIC + "Much more efficient than other competing brands!")
                .beginStructureBlock(9, 10, 33, false)
                .addStructureInfo("This structure is too complex! See schematic for details.")
                .addOtherStructurePart("Borosilicate Glass", "Can be UV tier or higher")
                .addInputBus("Start of conveyor belt", 1)
                .addOutputBus("End of conveyor belt", 2)
                .addEnergyHatch("Second-top layer", 3)
                .addMaintenanceHatch("Around the controller", 4)
                .addInputHatch("Bottom left and right corners", 5)
                .toolTipFinisher(EnumChatFormatting.AQUA + "MadMan310" + EnumChatFormatting.GRAY + " via "
                        + EnumChatFormatting.GREEN + "Good Generator");

        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new ComponentAssemblyLine(mName);
    }

    /**
     * Changes and adds new information to the default info data for the scanner.
     * */
    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;

        for (GT_MetaTileEntity_Hatch tHatch : mExoticEnergyHatches) {
            if (isValidMetaTileEntity(tHatch)) {
                storedEnergy += tHatch.getBaseMetaTileEntity().getStoredEU();
                maxEnergy += tHatch.getBaseMetaTileEntity().getEUCapacity();
            }
        }
        return new String[] {
            "------------ Critical Information ------------",
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": " + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(mProgresstime) + EnumChatFormatting.RESET + "t / "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(mMaxProgresstime) + EnumChatFormatting.RESET + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": " + EnumChatFormatting.GREEN
                    + GT_Utility.formatNumbers(storedEnergy) + EnumChatFormatting.RESET + " EU / "
                    + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(maxEnergy) + EnumChatFormatting.RESET + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": " + EnumChatFormatting.RED
                    + GT_Utility.formatNumbers(-EU_per_tick) + EnumChatFormatting.RESET + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": " + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(
                            GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList()))
                    + EnumChatFormatting.RESET + " EU/t(*" + EnumChatFormatting.YELLOW
                    + GT_Utility.formatNumbers(
                            GT_ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList()))
                    + EnumChatFormatting.RESET + "A) " + StatCollector.translateToLocal("GT5U.machines.tier")
                    + ": " + EnumChatFormatting.YELLOW
                    + VN[
                            GT_Utility.getTier(GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(
                                    getExoticAndNormalEnergyHatchList()))]
                    + EnumChatFormatting.RESET,
            StatCollector.translateToLocal("scanner.info.CASS.tier")
                    + (casingTier >= 0 ? GT_Values.VN[casingTier + 1] : "None!")
        };
    }

    @Override
    public ITexture[] getTexture(
            IGregTechTileEntity aBaseMetaTileEntity,
            byte aSide,
            byte aFacing,
            byte aColorIndex,
            boolean aActive,
            boolean aRedstone) {
        if (aSide == aFacing) {
            if (aActive)
                return new ITexture[] {
                    Textures.BlockIcons.getCasingTextureForId(183),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE)
                            .extFacing()
                            .build(),
                    TextureFactory.builder()
                            .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_ACTIVE_GLOW)
                            .extFacing()
                            .glow()
                            .build()
                };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(183),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY)
                        .extFacing()
                        .build(),
                TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_PROCESSING_ARRAY_GLOW)
                        .extFacing()
                        .glow()
                        .build()
            };
        }
        return new ITexture[] {Textures.BlockIcons.getCasingTextureForId(183)};
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean checkRecipe(ItemStack aStack) {
        long tVoltage = GT_ExoticEnergyInputHelper.getMaxInputVoltageMulti(getExoticAndNormalEnergyHatchList());
        long tAmps = GT_ExoticEnergyInputHelper.getMaxInputAmpsMulti(getExoticAndNormalEnergyHatchList());
        long totalEU = tVoltage * tAmps;
        ItemStack[] tItems = getStoredInputs().toArray(new ItemStack[0]);
        FluidStack[] tFluids = getStoredFluids().toArray(new FluidStack[0]);
        GT_Recipe foundRecipe = getRecipeMap().findRecipe(getBaseMetaTileEntity(), false, totalEU, tFluids, tItems);
        if (foundRecipe == null) return false;
        if (foundRecipe.mSpecialValue > casingTier + 1) return false;
        if (!foundRecipe.isRecipeInputEqual(true, tFluids, tItems)) return false;

        // Logic for overclocking calculations.
        double EU_input_tier = Math.log(totalEU) / log4;
        double EU_recipe_tier = Math.log(foundRecipe.mEUt) / log4;
        long overclock_count = (long) Math.floor(EU_input_tier - EU_recipe_tier);

        // Vital recipe info. Calculate overclocks here if necessary.
        EU_per_tick = (long) -(foundRecipe.mEUt * Math.pow(4, overclock_count));

        mMaxProgresstime = (int) (foundRecipe.mDuration / Math.pow(2, overclock_count));
        mMaxProgresstime = Math.max(1, mMaxProgresstime);

        mOutputItems = foundRecipe.mOutputs;
        updateSlots();
        return true;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (EU_per_tick < 0) {
            if (!drainEnergyInput(-EU_per_tick)) {
                EU_per_tick = 0;
                criticalStopMachine();
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean drainEnergyInput(long aEU) {
        return GT_ExoticEnergyInputHelper.drainEnergy(aEU, getExoticAndNormalEnergyHatchList());
    }

    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> tHatches = new ArrayList<>();
        tHatches.addAll(mExoticEnergyHatches);
        tHatches.addAll(mEnergyHatches);
        return tHatches;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 2, 0, realBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingTier = -1;
        return checkPiece(STRUCTURE_PIECE_MAIN, 4, 2, 0);
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        mExoticEnergyHatches.clear();
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

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return MyRecipeAdder.instance.COMPASSLINE_RECIPES;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("casingTier", casingTier);
        aNBT.setLong("euPerTick", EU_per_tick);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        casingTier = aNBT.getInteger("casingTier");
        EU_per_tick = aNBT.getLong("euPerTick");
        super.loadNBTData(aNBT);
    }
}
