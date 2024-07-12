package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.withChannel;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.ExoticEnergy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.Muffler;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;
import static gregtech.api.util.GT_Utility.filterValidMTEs;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.API.BorosilicateGlass;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_OverclockCalculator;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class GregTechMetaTileEntity_MegaAlloyBlastSmelter
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GregTechMetaTileEntity_MegaAlloyBlastSmelter>
    implements ISurvivalConstructable {

    private static final int MAX_PARALLELS = 256;
    private HeatingCoilLevel coilLevel;
    private byte glassTier = -1;
    private boolean hasNormalCoils;

    private static final IStructureDefinition<GregTechMetaTileEntity_MegaAlloyBlastSmelter> STRUCTURE_DEFINITION = StructureDefinition
        .<GregTechMetaTileEntity_MegaAlloyBlastSmelter>builder()
        .addShape(
            "main",
            new String[][] {
                { "           ", "           ", "           ", "           ", "           ", "           ",
                    "           ", "           ", "           ", "           ", "           ", "           ",
                    "           ", "   DDDDD   ", "   CCCCC   ", "   AEEEA   ", "   AE~EA   ", "   AEEEA   ",
                    "   CCCCC   ", "   ZZZZZ   " },
                { "   DDDDD   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ",
                    "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ",
                    "   DDDDD   ", "  D     D  ", "  C     C  ", "  A     A  ", "  A     A  ", "  A     A  ",
                    "  C     C  ", "  ZZZZZZZ  " },
                { "  DDDDDDD  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ",
                    "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ",
                    "  DBBBBBD  ", " D BBBBB D ", " C BBBBB C ", " A BBBBB A ", " A BBBBB A ", " A BBBBB A ",
                    " C BBBBB C ", " ZZZZZZZZZ " },
                { " DDDDDDDDD ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " DB     BD ", "D B     B D", "C B     B C", "A B     B A", "A B     B A", "A B     B A",
                    "C B     B C", "ZZZZZZZZZZZ" },
                { " DDDDDDDDD ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " DB     BD ", "D B     B D", "C B     B C", "A B     B A", "A B     B A", "A B     B A",
                    "C B     B C", "ZZZZZZZZZZZ" },
                { " DDDDFDDDD ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " DB     BD ", "D B     B D", "C B     B C", "A B     B A", "A B     B A", "A B     B A",
                    "C B     B C", "ZZZZZZZZZZZ" },
                { " DDDDDDDDD ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " DB     BD ", "D B     B D", "C B     B C", "A B     B A", "A B     B A", "A B     B A",
                    "C B     B C", "ZZZZZZZZZZZ" },
                { " DDDDDDDDD ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ", " AB     BA ",
                    " DB     BD ", "D B     B D", "C B     B C", "A B     B A", "A B     B A", "A B     B A",
                    "C B     B C", "ZZZZZZZZZZZ" },
                { "  DDDDDDD  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ",
                    "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ", "  ABBBBBA  ",
                    "  DBBBBBD  ", " D BBBBB D ", " C BBBBB C ", " A BBBBB A ", " A BBBBB A ", " A BBBBB A ",
                    " C BBBBB C ", " ZZZZZZZZZ " },
                { "   DDDDD   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ",
                    "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ",
                    "   DDDDD   ", "  D     D  ", "  C     C  ", "  A     A  ", "  A     A  ", "  A     A  ",
                    "  C     C  ", "  ZZZZZZZ  " },
                { "           ", "           ", "           ", "           ", "           ", "           ",
                    "           ", "           ", "           ", "           ", "           ", "           ",
                    "           ", "   DDDDD   ", "   CCCCC   ", "   AAAAA   ", "   AAAAA   ", "   AAAAA   ",
                    "   CCCCC   ", "   ZZZZZ   " } })
        .addElement(
            'B',
            withChannel(
                "coil",
                ofChain(
                    onElementPass(
                        te -> te.hasNormalCoils = false,
                        ofCoil(
                            GregTechMetaTileEntity_MegaAlloyBlastSmelter::setCoilLevel,
                            GregTechMetaTileEntity_MegaAlloyBlastSmelter::getCoilLevel)),
                    onElementPass(te -> te.hasNormalCoils = true, ofBlock(ModBlocks.blockCasingsMisc, 14)))))

        .addElement(
            'Z',
            buildHatchAdder(GregTechMetaTileEntity_MegaAlloyBlastSmelter.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy, ExoticEnergy)
                .casingIndex(TAE.GTPP_INDEX(15))
                .dot(1)
                .buildAndChain(ofBlock(ModBlocks.blockCasingsMisc, 15)))
        .addElement(
            'E',
            buildHatchAdder(GregTechMetaTileEntity_MegaAlloyBlastSmelter.class).atLeast(Maintenance)
                .casingIndex(TAE.GTPP_INDEX(15))
                .dot(2)
                .buildAndChain(ofBlock(ModBlocks.blockCasingsMisc, 15)))
        .addElement('D', ofBlock(ModBlocks.blockCasingsMisc, 15))
        .addElement('C', ofBlock(ModBlocks.blockCasingsMisc, 14))
        .addElement(
            'A',
            withChannel(
                "glass",
                BorosilicateGlass.ofBoroGlass((byte) -1, (te, t) -> te.glassTier = t, te -> te.glassTier)))
        .addElement('F', Muffler.newAny(TAE.GTPP_INDEX(15), 3))
        .build();

    public GregTechMetaTileEntity_MegaAlloyBlastSmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GregTechMetaTileEntity_MegaAlloyBlastSmelter(String aName) {
        super(aName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                if (glassTier < GT_Utility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GT_Utility.getTier(recipe.mEUt));
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected GT_OverclockCalculator createOverclockCalculator(@NotNull GT_Recipe recipe) {
                return super.createOverclockCalculator(recipe)
                    .setSpeedBoost((float) (1.0 - getCoilDiscount(coilLevel)));
            }
        }.setMaxParallel(MAX_PARALLELS);
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
    }

    @Override
    public boolean addOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        boolean exotic = addExoticEnergyInputToMachineList(aTileEntity, aBaseCasingIndex);
        return super.addToMachineList(aTileEntity, aBaseCasingIndex) || exotic;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        glassTier = -1;
        coilLevel = HeatingCoilLevel.None;
        if (!checkPiece("main", 5, 16, 0)) return false;
        if (hasNormalCoils) coilLevel = HeatingCoilLevel.None;
        if (mMaintenanceHatches.size() != 1) return false;
        if (mMufflerHatches.size() != 1) return false;
        if (this.glassTier < 10 && !getExoticAndNormalEnergyHatchList().isEmpty()) {
            for (GT_MetaTileEntity_Hatch hatchEnergy : getExoticAndNormalEnergyHatchList()) {
                if (this.glassTier < hatchEnergy.mTier) {
                    return false;
                }
            }
        }
        // Disallow lasers if the glass is below UV tier
        if (glassTier < 8) {
            for (GT_MetaTileEntity_Hatch hatchEnergy : getExoticEnergyHatches()) {
                if (hatchEnergy.getConnectionType() == GT_MetaTileEntity_Hatch.ConnectionType.LASER) {
                    return false;
                }
            }
        }
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

    public double getCoilDiscount(HeatingCoilLevel lvl) {
        // Since there are only 14 tiers (starting from 0), this is what the function is.
        double unRounded = (lvl != null ? lvl.getTier() : 0) / 130.0D;
        if (unRounded < 0) return 0F;
        double rounded = Math.floor(unRounded * 1000) / 1000;

        return Math.max(0, rounded);
    }

    @Override
    public void explodeMultiblock() {
        super.explodeMultiblock();
    }

    @Override
    public List<GT_MetaTileEntity_Hatch> getExoticAndNormalEnergyHatchList() {
        List<GT_MetaTileEntity_Hatch> tHatches = new ArrayList<>();
        tHatches.addAll(mExoticEnergyHatches);
        tHatches.addAll(mEnergyHatches);
        return tHatches;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 5, 16, 0);
    }

    @Override
    public IStructureDefinition<GregTechMetaTileEntity_MegaAlloyBlastSmelter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid Alloy Cooker")
            .addInfo("Controller block for the Mega Alloy Blast Smelter")
            .addInfo(
                "Runs the same recipes as the normal ABS, except with up to " + EnumChatFormatting.BOLD
                    + EnumChatFormatting.UNDERLINE
                    + MAX_PARALLELS
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " parallels.")
            .addInfo("Every coil tier above cupronickel grants a speed bonus, based on this function:")
            .addInfo("Bonus = TIER / 150, rounded to the nearest thousandth.")
            .addInfo(
                EnumChatFormatting.ITALIC
                    + "Can also use normal ABS coils in their place instead, if you don't like the bonuses :)"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY)
            .addInfo("The glass limits the tier of the energy hatch. UEV glass unlocks all tiers.")
            .addInfo("UV glass required for TecTech laser hatches.")
            .addInfo(
                EnumChatFormatting.ITALIC + "\"all it does is make metals hot\""
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY)
            .beginStructureBlock(11, 20, 11, false)
            .addStructureInfo("This structure is too complex! See schematic for details.")
            .addMaintenanceHatch("Around the controller", 2)
            .addOtherStructurePart("Input Bus, Output Bus, Input Hatch, Output Bus, Energy Hatch", "Bottom Casing", 1)
            .addMufflerHatch("1 in the center of the top layer", 3)
            .toolTipFinisher(
                EnumChatFormatting.AQUA + "MadMan310 "
                    + EnumChatFormatting.GRAY
                    + "via "
                    + EnumChatFormatting.RED
                    + "GT++");
        return tt;
    }

    @Override
    public String[] getInfoData() {
        long storedEnergy = 0;
        long maxEnergy = 0;
        int paras = getBaseMetaTileEntity().isActive() ? processingLogic.getCurrentParallels() : 0;
        int discountP = (int) (getCoilDiscount(coilLevel) * 1000) / 10;

        for (GT_MetaTileEntity_Hatch tHatch : filterValidMTEs(mExoticEnergyHatches)) {
            storedEnergy += tHatch.getBaseMetaTileEntity()
                .getStoredEU();
            maxEnergy += tHatch.getBaseMetaTileEntity()
                .getEUCapacity();
        }

        return new String[] { "------------ Critical Information ------------",
            StatCollector.translateToLocal("GT5U.multiblock.Progress") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(mProgresstime)
                + EnumChatFormatting.RESET
                + "t / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(mMaxProgresstime)
                + EnumChatFormatting.RESET
                + "t",
            StatCollector.translateToLocal("GT5U.multiblock.energy") + ": "
                + EnumChatFormatting.GREEN
                + GT_Utility.formatNumbers(storedEnergy)
                + EnumChatFormatting.RESET
                + " EU / "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(maxEnergy)
                + EnumChatFormatting.RESET
                + " EU",
            StatCollector.translateToLocal("GT5U.multiblock.usage") + ": "
                + EnumChatFormatting.RED
                + GT_Utility.formatNumbers(-lEUt)
                + EnumChatFormatting.RESET
                + " EU/t",
            StatCollector.translateToLocal("GT5U.multiblock.mei") + ": "
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getAverageInputVoltage())
                + EnumChatFormatting.RESET
                + " EU/t(*"
                + EnumChatFormatting.YELLOW
                + GT_Utility.formatNumbers(getMaxInputAmps())
                + EnumChatFormatting.RESET
                + "A) "
                + StatCollector.translateToLocal("GT5U.machines.tier")
                + ": "
                + EnumChatFormatting.YELLOW
                + GT_Values.VN[GT_Utility.getTier(getAverageInputVoltage())]
                + EnumChatFormatting.RESET,
            "Parallels: " + EnumChatFormatting.BLUE + paras + EnumChatFormatting.RESET,
            "Coil Discount: " + EnumChatFormatting.BLUE + discountP + "%" + EnumChatFormatting.RESET,
            "-----------------------------------------" };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregTechMetaTileEntity_MegaAlloyBlastSmelter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced_Active)
                        .extFacing()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.Overlay_Machine_Controller_Advanced)
                    .extFacing()
                    .build() };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)) };
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.alloyBlastSmelterRecipes;
    }

    public HeatingCoilLevel getCoilLevel() {
        return coilLevel;
    }

    public void setCoilLevel(HeatingCoilLevel coilLevel) {
        this.coilLevel = coilLevel;
    }

    @Override
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (aPlayer.isSneaking()) {
            // Lock to single recipe
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ);
        } else {
            inputSeparation = !inputSeparation;
            GT_Utility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.machines.separatebus") + " " + inputSeparation);
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GT_Utility.sendChatToPlayer(aPlayer, "Batch recipes.");
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "Don't batch recipes.");
            }
        }

        return true;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 102400;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivialBuildPiece("main", stackSize, 5, 16, 0, elementBudget, env, false, true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.glassTier = aNBT.getByte("glassTier");
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("separateBusses");
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setByte("glassTier", glassTier);
        super.saveNBTData(aNBT);
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }
}
