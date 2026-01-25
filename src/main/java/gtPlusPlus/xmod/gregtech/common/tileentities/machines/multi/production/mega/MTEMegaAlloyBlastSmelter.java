package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.production.mega;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.ExoticEnergy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.activeCoils;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofCoil;

import java.util.ArrayList;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableMap;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.IStructureElement;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.TAE;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.api.util.tooltip.TooltipHelper;
import gregtech.api.util.tooltip.TooltipTier;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEMegaAlloyBlastSmelter extends MTEExtendedPowerMultiBlockBase<MTEMegaAlloyBlastSmelter>
    implements ISurvivalConstructable {

    private static final int MAX_PARALLELS = 256;
    private HeatingCoilLevel coilLevel;
    private int glassTier = -1;
    private double speedBonus = 1;
    private double energyDiscount = 1;
    private CoilType coilType = CoilType.Unknown;

    private static final IStructureDefinition<MTEMegaAlloyBlastSmelter> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEMegaAlloyBlastSmelter>builder()
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
        .addElement('B', getCoilElement())
        .addElement(
            'Z',
            buildHatchAdder(MTEMegaAlloyBlastSmelter.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Energy, ExoticEnergy)
                .casingIndex(TAE.GTPP_INDEX(15))
                .hint(1)
                .buildAndChain(ofBlock(ModBlocks.blockCasingsMisc, 15)))
        .addElement(
            'E',
            buildHatchAdder(MTEMegaAlloyBlastSmelter.class).atLeast(Maintenance)
                .casingIndex(TAE.GTPP_INDEX(15))
                .hint(2)
                .buildAndChain(ofBlock(ModBlocks.blockCasingsMisc, 15)))
        .addElement('D', ofBlock(ModBlocks.blockCasingsMisc, 15))
        .addElement('C', ofBlock(ModBlocks.blockCasingsMisc, 14))
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('F', Muffler.newAny(TAE.GTPP_INDEX(15), 3))
        .build();

    private static IStructureElement<MTEMegaAlloyBlastSmelter> getCoilElement() {
        IStructureElement<MTEMegaAlloyBlastSmelter> heatingCoilElem = GTStructureChannels.HEATING_COIL
            .use(activeCoils(ofCoil(MTEMegaAlloyBlastSmelter::setCoilLevel, MTEMegaAlloyBlastSmelter::getCoilLevel)));
        IStructureElement<MTEMegaAlloyBlastSmelter> basicCoilElem = ofBlock(ModBlocks.blockCasingsMisc, 14);
        return partitionBy(
            te -> te.coilType,
            ImmutableMap.of(
                CoilType.Unknown,
                ofChain(
                    onElementPass(te -> te.coilType = CoilType.HeatingCoil, heatingCoilElem),
                    onElementPass(te -> te.coilType = CoilType.BasicCoil, basicCoilElem)),
                CoilType.HeatingCoil,
                heatingCoilElem,
                CoilType.BasicCoil,
                basicCoilElem));
    }

    public MTEMegaAlloyBlastSmelter(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMegaAlloyBlastSmelter(String aName) {
        super(aName);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (glassTier < VoltageIndex.UMV && glassTier < GTUtility.getTier(recipe.mEUt)) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(GTUtility.getTier(recipe.mEUt));
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @NotNull
            @Override
            protected OverclockCalculator createOverclockCalculator(@NotNull GTRecipe recipe) {
                calculateEnergyDiscount(coilLevel, recipe);
                return super.createOverclockCalculator(recipe).setDurationModifier(speedBonus)
                    .setEUtDiscount(energyDiscount);
            }
        }.setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return MAX_PARALLELS;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
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
        coilType = CoilType.Unknown;
        if (!checkPiece("main", 5, 16, 0)) return false;
        if (coilType == CoilType.BasicCoil) coilLevel = HeatingCoilLevel.None;
        if (mMufflerHatches.size() != 1) return false;
        if (this.glassTier < VoltageIndex.UMV && !getExoticAndNormalEnergyHatchList().isEmpty()) {
            for (MTEHatch hatchEnergy : getExoticAndNormalEnergyHatchList()) {
                if (this.glassTier < hatchEnergy.mTier) {
                    return false;
                }
            }
        }
        // Disallow lasers if the glass is below UV tier
        if (glassTier < VoltageIndex.UV) {
            for (MTEHatch hatchEnergy : getExoticEnergyHatches()) {
                if (hatchEnergy.getConnectionType() == MTEHatch.ConnectionType.LASER) {
                    return false;
                }
            }
        }
        calculateSpeedBonus(coilLevel, glassTier);
        return true;
    }

    private void calculateSpeedBonus(HeatingCoilLevel lvl, int glassTier) {
        int bonusTier = lvl != null ? Math.min(lvl.getTier() - 3, glassTier - 2) : 0;
        if (bonusTier < 0) {
            speedBonus = 1;
            return;
        }
        speedBonus = 1 - 0.05f * bonusTier;
    }

    private void calculateEnergyDiscount(HeatingCoilLevel lvl, GTRecipe recipe) {
        int recipeTier = GTUtility.getTier(recipe.mEUt);
        int tierDifference = lvl != null ? lvl.getTier() + 1 - recipeTier : 0;
        if (tierDifference < 0) {
            energyDiscount = 1;
            return;
        }
        energyDiscount = GTUtility.powInt(0.95, tierDifference);
    }

    @Override
    public void explodeMultiblock() {
        super.explodeMultiblock();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece("main", stackSize, hintsOnly, 5, 16, 0);
    }

    @Override
    public IStructureDefinition<MTEMegaAlloyBlastSmelter> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fluid Alloy Cooker, MABS")
            .addInfo(
                TooltipHelper.coloredText(
                    TooltipHelper.italicText("\"all it does is make metals hot\""),
                    EnumChatFormatting.DARK_GRAY))
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addInfo(
                TooltipHelper.speedText("-5%") + " Recipe Time per "
                    + TooltipHelper.tierText(TooltipTier.COIL)
                    + " Tier above TPV (additive)")
            .addInfo("if the equivalent or better " + TooltipHelper.tierText(TooltipTier.GLASS) + " is present")
            .addInfo(
                TooltipHelper.effText("-5%") + " EU Usage per "
                    + TooltipHelper.tierText(TooltipTier.COIL)
                    + " Tier above the Recipe Tier (multiplicative)")
            .addSeparator()
            .addInfo(
                "Recipe Tier limited by " + TooltipHelper.tierText(TooltipTier.GLASS)
                    + " Tier, "
                    + TooltipHelper.voltageText(VoltageIndex.UMV)
                    + " unlocks all")
            .addInfo("Can also use normal ABS coils in their place instead, if you don't like the bonuses :)")
            .addSeparator()
            .addTecTechHatchInfo()
            .addMinGlassForLaser(VoltageIndex.UV)
            .addGlassEnergyLimitInfo(VoltageIndex.UMV)
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(11, 20, 11, false)
            .addController("Mid of the fourth layer")
            .addCasingInfoExactly("Blast Smelter Casing Block", 218, false)
            .addCasingInfoExactly("Blast Smelter Heat Containment Coil", 56, false)
            .addCasingInfoExactly("Coil", 360, true)
            .addCasingInfoExactly("Any Tiered Glass", 339, true)
            .addMaintenanceHatch("Around the controller", 2)
            .addOtherStructurePart(
                StatCollector.translateToLocal("GTPP.tooltip.structure.many_bus_hatch"),
                "Bottom Casing",
                1)
            .addMufflerHatch("1 in the center of the top layer", 3)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addSubChannelUsage(GTStructureChannels.HEATING_COIL)
            .toolTipFinisher(EnumChatFormatting.AQUA + "MadMan310");
        return tt;
    }

    @Override
    public void getExtraInfoData(ArrayList<String> info) {
        int paras = getBaseMetaTileEntity().isActive() ? processingLogic.getCurrentParallels() : 0;
        int moreSpeed = (int) ((1 - speedBonus) * 100);
        int lessEnergy = (int) ((1 - energyDiscount) * 100);

        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.abs.mega.parallels",
                "" + EnumChatFormatting.BLUE + paras + EnumChatFormatting.RESET));

        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.abs.mega.speed_bonus",
                "" + EnumChatFormatting.BLUE + moreSpeed + "%" + EnumChatFormatting.RESET));

        info.add(
            StatCollector.translateToLocalFormatted(
                "gtpp.infodata.abs.mega.energy_discount",
                "" + EnumChatFormatting.BLUE + lessEnergy + "%" + EnumChatFormatting.RESET));
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMegaAlloyBlastSmelter(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) {
                return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCAMegaAlloyBlastSmelterActive)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(TexturesGtBlock.oMCAMegaAlloyBlastSmelterActiveGlow)
                        .extFacing()
                        .glow()
                        .build() };
            }
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TAE.GTPP_INDEX(15)),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAMegaAlloyBlastSmelter)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCAMegaAlloyBlastSmelterGlow)
                    .extFacing()
                    .glow()
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
    public final void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            // Lock to single recipe
            super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
        } else {
            inputSeparation = !inputSeparation;
            GTUtility.sendChatTrans(
                aPlayer,
                inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
        }
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        if (aPlayer.isSneaking()) {
            batchMode = !batchMode;
            if (batchMode) {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOn");
            } else {
                GTUtility.sendChatTrans(aPlayer, "misc.BatchModeTextOff");
            }
            return true;
        }
        return false;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return 102400;
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return survivalBuildPiece("main", stackSize, 5, 16, 0, elementBudget, env, false, true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("separateBusses");
        }
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    private enum CoilType {
        Unknown, // check not done yet, or not a valid structure
        HeatingCoil, // using tiered heating coil
        BasicCoil, // using containment coil
    }
}
