package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.api.recipe.GoodGeneratorRecipeMaps;
import goodgenerator.loader.Loaders;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoltageIndex;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.structure.error.StructureErrors;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.OverclockCalculator;
import gregtech.common.misc.GTStructureChannels;

public class MTEComponentAssemblyLine extends MTEExtendedPowerMultiBlockBase<MTEComponentAssemblyLine>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private int casingTier = -1;
    private int glassTier = -1;
    private double speedBonus;
    protected static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEComponentAssemblyLine> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEComponentAssemblyLine>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            new String[][] {
                { "         ", "   III   ", " HHI~IHH ", "HH III HH", "H       H", "H       H", "H  JJJ  H",
                    "H  JJJ  H", "H  N N  H", "HHHHHHHHH" },
                { "         ", " ELHHHLE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "AG  C  GA", "AG     GA", "AG     GA",
                    "AG HHH GA", "AG     GA", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "HG  C  GH", "HG     GH", "HG     GH",
                    "HG HHH GH", "HG     GH", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "AG  C  GA", "AG     GA", "AG     GA",
                    "AG HHH GA", "AG     GA", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "HG  C  GH", "HG     GH", "HG     GH",
                    "HG HHH GH", "HG     GH", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "AG  C  GA", "AG     GA", "AG     GA",
                    "AG HHH GA", "AG     GA", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "HG  C  GH", "HG     GH", "HG     GH",
                    "HG HHH GH", "HG     GH", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "   BBB   ", " EL   LE ", "E GGDGG E", "HGG D GGH", "AG  C  GA", "AG     GA", "AG     GA",
                    "AG HHH GA", "AG     GA", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A  n n  A", "MHHHHHHHM" },
                { "   HBH   ", " EL   LE ", "E       E", "HC     CH", "AC     CA", "AC     CA", "A D   D A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "         ", " ELHHHLE ", "E       E", "H       H", "A       A", "A       A", "A       A",
                    "A  HHH  A", "A       A", "MHHHHHHHM" },
                { "         ", "         ", " HHHHHHH ", "HH     HH", "H       H", "H       H", "H       H",
                    "H       H", "H  KKK  H", "HHHHHHHHH" } })
        .addElement('A', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings8, 7))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings2, 5))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings2, 9))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings9, 0))
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings9, 1))
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings4, 1))
        .addElement(
            'B',
            GTStructureChannels.COMPONENT_ASSEMBLYLINE_CASING.use(
                ofBlocksTiered(
                    (block, meta) -> block == Loaders.componentAssemblylineCasing ? meta : null,
                    IntStream.range(0, 14)
                        .mapToObj(i -> Pair.of(Loaders.componentAssemblylineCasing, i))
                        .collect(Collectors.toList()),
                    -1,
                    MTEComponentAssemblyLine::setCasingTier,
                    MTEComponentAssemblyLine::getCasingTier)))
        .addElement(
            'J',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(InputBus)
                .hint(1)
                .casingIndex(183)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'N',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(InputBus)
                .hint(1)
                .casingIndex(183)
                .buildAndChain(GTStructureUtility.ofFrame(Materials.TungstenSteel)))
        .addElement(
            'K',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(OutputBus)
                .hint(5)
                .casingIndex(183)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'L',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(Energy, ExoticEnergy)
                .hint(3)
                .casingIndex(183)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'I',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(Maintenance)
                .hint(4)
                .casingIndex(183)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement(
            'M',
            GTStructureUtility.buildHatchAdder(MTEComponentAssemblyLine.class)
                .atLeast(InputHatch)
                .hint(2)
                .casingIndex(183)
                .buildAndChain(GregTechAPI.sBlockCasings8, 7))
        .addElement('n', GTStructureUtility.ofFrame(Materials.TungstenSteel))
        .build();

    public MTEComponentAssemblyLine(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEComponentAssemblyLine(String aName) {
        super(aName);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 2, 0);
    }

    @Override
    public IStructureDefinition<MTEComponentAssemblyLine> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("High-Capacity Component Assembler, CoAL")
            .addInfo("Assembles basic components (motors, pumps, etc.) in large batches")
            .addInfo(
                "The " + EnumChatFormatting.BOLD
                    + EnumChatFormatting.YELLOW
                    + "Component Assembly Line Casing "
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + "limits the recipes the machine can perform. See the NEI pages for details")
            .addInfo("Using casings above the required recipe tier provides a speed bonus:")
            .addInfo(EnumChatFormatting.YELLOW + "Halves recipe time per tier above recipe")
            .addInfo(EnumChatFormatting.ITALIC + "Much more efficient than other competing brands!")
            .addSupportAny()
            .addUnlimitedTierSkips()
            .beginStructureBlock(33, 9, 10, true)
            .addController("Front center, 8th layer")
            .addCasing("516-646", "Advanced Iridium Plated Machine Casing", false)
            .addCasing("280", "UV+ Tiered Glass", false)
            .addCasing("126", "PBI Pipe Casing", false)
            .addCasing("124", "Advanced Filter Casing", false)
            .addCasing("55", "Assembly Line Casing", false)
            .addCasing("43", "Component Assembly Line Casing", true)
            .addCasing("30", "Assembler Machine Casing", false)
            .addCasing("2-4", "Tungstensteel Frame Box", false)
            .addEnergyHatch("1+", "Any outer iridium casing on the 2nd to top layer", 3)
            .addMaintenanceHatch("1", "Around controller", 4)
            .addInputBus("1-8", "Any casing or frame box at front of conveyor belt", 1)
            .addInputHatch("0+", "Any side bottom edge casing", 2)
            .addOutputBus("1+", "Any casing at end of conveyor belt", 5)
            .addStructureInfo("")
            .addSubChannel(GTStructureChannels.BOROGLASS)
            .addSubChannel(GTStructureChannels.COMPONENT_ASSEMBLYLINE_CASING)
            .toolTipFinisher(EnumChatFormatting.AQUA + "MadMan310");
        return tt;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEComponentAssemblyLine(mName);
    }

    /**
     * Changes and adds new information to the default info data for the scanner.
     */
    @Override
    public String[] getInfoData() {
        String[] origin = super.getInfoData();
        String[] ret = new String[origin.length + 1];
        System.arraycopy(origin, 0, ret, 0, origin.length);
        ret[origin.length] = IGregTechDeviceInformation.encode(
            "scanner.info.CASS.tier",
            casingTier >= 0 ? GTValues.VN[casingTier + 1] : GTUtility.translate("scanner.info.CASS.tier.none"));
        return ret;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_COMPONENT_ASSEMBLY_LINE,
            OVERLAY_FRONT_COMPONENT_ASSEMBLY_LINE_GLOW,
            OVERLAY_FRONT_COMPONENT_ASSEMBLY_LINE_ACTIVE,
            OVERLAY_FRONT_COMPONENT_ASSEMBLY_LINE_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Textures.BlockIcons.getCasingTextureForId(183);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (recipe.mSpecialValue > casingTier + 1) {
                    return CheckRecipeResultRegistry.insufficientMachineTier(recipe.mSpecialValue);
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            @Nonnull
            @Override
            protected OverclockCalculator createOverclockCalculator(@Nonnull GTRecipe recipe) {
                speedBonus = GTUtility.powInt(2, -(casingTier + 1 - recipe.mSpecialValue));
                return super.createOverclockCalculator(recipe).setDurationModifier(speedBonus);
            }
        };
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        int realBudget = elementBudget >= 200 ? elementBudget : Math.min(200, elementBudget * 5);
        return survivalBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 2, 0, realBudget, env, false, true);
    }

    private void setCasingTier(int tier) {
        this.casingTier = tier;
    }

    private int getCasingTier() {
        return this.casingTier;
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        this.casingTier = -1;
        this.glassTier = -1;
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 4, 2, 0, errors)) return;
        if (this.glassTier < VoltageIndex.UV) {
            errors.add(StructureErrors.glassTierNotEnough(VoltageIndex.UV));
        }
        checkHasAnyEnergy(errors);
        checkOneMaintenanceHatch(errors);
        checkHasInputBus(errors);
        checkHasOutputBus(errors);
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        inputSeparation = !inputSeparation;
        GTUtility.sendChatTrans(
            aPlayer,
            inputSeparation ? "GT5U.machines.separatebus.true" : "GT5U.machines.separatebus.false");
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GoodGeneratorRecipeMaps.componentAssemblyLineRecipes;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("casingTier", casingTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        casingTier = aNBT.getInteger("casingTier");
        if (!aNBT.hasKey(INPUT_SEPARATION_NBT_KEY)) {
            inputSeparation = aNBT.getBoolean("mSeparate");
        }
    }
}
