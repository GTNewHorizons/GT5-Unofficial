package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatFluid;
import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_FRIDGE_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.Optional;

import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import bartworks.common.configs.Configuration;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.GregTechAPI;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEEndothermicFridgeGui;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.thermalfoundation.fluid.TFFluids;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEEndothermicFridge extends MTEExtendedPowerMultiBlockBase<MTEEndothermicFridge>
    implements ISurvivalConstructable {

    // todo: some structure tweaks, mechanic, old t2 integration, tooltip
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int HORIZONTAL_OFFSET = 11;
    private static final int VERTICAL_OFFSET = 12;
    private static final int DEPTH_OFFSET = 3;
    private static final int TEXTURE_ID = Casings.FridgeCasing.getTextureId();
    private static final IStructureDefinition<MTEEndothermicFridge> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEEndothermicFridge>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(
                new String[][]{
                {"                       ","                       ","                       ","                       ","                       ","                       ","                       ","                       ","         ECCCE         ","        CEFFFEC        ","       CCECCCECC       ","       CCECCCECC       ","       CCECCCECC       ","        CEFFFEC        ","         ECCCE         ","                       ","                       ","                       ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","                       ","          CCC          ","       CCECCCECC       ","      CCCECCCECCC      ","     CCCCFDDDFCCCC     ","     CCCFDDDDDFCCC     ","    CCCCDDDDDDDCCCC    ","     CCCFDDDDDFCCC     ","     CCCCFDDDFCCCC     ","      CCCECCCECCC      ","       CCECCCECC       ","          CCC          ","                       ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","                       ","       CCECCCECC       ","     CCCCGGGGGCCCC     ","    CCCGG     GGCCC    ","    CCG         GCC    ","   CCCG         GCCC   ","   CCDDD       DDDCC   ","   CCCG         GCCC   ","    CCG         GCC    ","    CCCGG     GGCCC    ","     CCCCGGGGGCCCC     ","       CCECCCECC       ","                       ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          DDD          ","      CCCECCCECCC      ","     CC         CC     ","    C             C    ","   CC             CC   ","  FC               CF  ","  FCD             DCF  ","  FC               CF  ","   CC             CC   ","    C             C    ","     CC         CC     ","      CCCECCCECCC      ","          DDD          ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","        DEFFFED        ","      CCCEDDDECCC      ","    CC           CC    ","   CC             CC   ","  FC               CF  ","  EC               CE  ","  EDI             IDE  ","  EC               CE  ","  FC               CF  ","   CC             CC   ","    CC           CC    ","      CCCEDDDECCC      ","        DEFFFED        ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","       DDE   EDD       ","     CCCFDAAADFCCC     ","   CCC           CCC   ","  FC               CF  ","  EC               CE  ","   E               E   ","  GFI             IFG  ","   E               E   ","  EC               CE  ","  FC               CF  ","   CCC           CCC   ","     CCCFDAAADFCCC     ","       DDE   EDD       ","                       ","                       ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","       DFE   EFD       ","    CCCCDDAAADDCCCC    ","   CCDDD       DDDCC   ","  FCD             DCF  ","  EDI             IDE  ","  GFI             IFG  "," BBBB             BBBB ","  GFI             IFG  ","  EDI             IDE  ","  FCD             DCF  ","   CCDDDB     BDDDCC   ","    CCCCBDAAADBCCCC    ","       DBE   EBD       ","        B     B        ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","       DDE   EDD       ","     CCCFDAAADFCCC     ","   CCC           CCC   ","  FC               CF  ","  EC               CE  ","   E               E   ","  GFI             IFG  ","   E               E   ","  EC               CE  ","  FC               CF  ","   CCC           CCC   ","     CCCFDAAADFCCC     ","       DDE   EDD       ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","        DEFFFED        ","     GCCCEDDDECCCG     ","    CC           CC    ","   CC             CC   ","  FC               CF  ","  EC               CE  ","  EDI             IDE  ","  EC               CE  ","  FC               CF  ","   CC             CC   ","    CC           CC    ","     GCCCEDDDECCCG     ","        DEFFFED        ","        E     E        ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","                       ","          DDD          ","     GCCCECCCECCCG     ","    GCC         CCG    ","    C             C    ","   CC             CC   ","  FC               CF  ","  FCD             DCF  ","  FC               CF  ","   CC             CC   ","    C             C    ","    GCC         CCG    ","     GCCCECCCECCCG     ","          DDD          ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","                       ","           E           ","           E           ","     G CCECECECC G     ","    GCCCCGGGGGCCCCG    ","    CCCGG     GGCCC    ","    CCG         GCC    ","   CCCG         GCCC   ","   CCDDD   B   DDDCC   ","   CCCG         GCCC   ","    CCG         GCC    ","    CCCGG     GGCCC    ","    GCCCCGGGGGCCCCG    ","     G CCECCCECC G     ","                       ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","          CCC          ","           E           ","                       ","     G    CCC    G     ","    GIGCCECCCECCGIG    ","     GCCCECCCECCCG     ","     CCCCFDDDFCCCC     ","     CCCFDEEEDFCCC     ","    CCCCDDEBEDDCCCC    ","     CCCFDEEEDFCCC     ","     CCCCFDDDFCCCC     ","     GCCCECCCECCCG     ","    GIGCCECCCECCGIG    ","     G    CCC    G     ","                       ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","          C~C          ","          CEC          ","                       ","     G           G     ","    GI           IG    ","         ECCCE         ","        CEFFFEC        ","       CCEIIIECC       ","       CCEIBIECC       ","       CCEIIIECC       ","        CEFFFEC        ","         ECCCE         ","    GI           IG    ","     G           G     ","                       ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","          CCC          ","          CEC          ","       E       E       ","     G   JJJJJ   G     ","    GI  G     G  IG    ","       G       G       ","      J  G   G  J      ","      J  G   G  J      ","      J  G B G  J      ","      J  G   G  J      ","      J  G   G  J      ","       G       G       ","    GI  G     G  IG    ","     G   JJJJJ   G     ","       E       E       ","                       ","        B     B        ","                       ","                       ","                       "},
                {"                       ","                       ","                       ","          FJF          ","       FFJFJFJFF       ","      FEFFJJJFFEF      ","     GJFJJJJJJJFJG     ","    GIGJJJJJJJJJGIG    ","    FGJJJJJJJJJJJGF    ","    JFJJJJJJJJJJJFJ    ","   FFJJJJJJJJJJJJJFF   ","   JJJJJJJJBJJJJJJJJ   ","   FFJJJJJJBJJJJJJFF   ","    JFJJJJJBJJJJJFJ    ","    FGJJJJJBJJJJJGF    ","    GIGJJJJBJJJJGIG    ","     GJFJJJBJJJFJG     ","      FEEFJBJFEEF      ","       FEJFBFJEF       ","        BBBBBBB        ","          FFF          ","                       ","                       "},
                {"          HHH          ","       HHHCCCHHH       ","     HHCCCCCCCCCHH     ","    HCCCCCHJHCCCCCH    ","   HCCCHHHHCHHHHCCCH   ","  HCCCHHHHCCCHHHHCCCH  ","  HCCHJHCHJCJHCHJHCCH  "," HCCHHHJJCJCJCJJHHHCCH "," HCCHHCJJJCCCJJJCHHCCH "," HCCHHHCJCJCJCJCHHHCCH ","HCCHHCJJCJCCCJCJJCHHCCH","HCCJCCCCCCCCCCCCCCCJCCH","HCCHHCJJCJCCCJCJJCHHCCH"," HCCHHHCJCJCJCJCHHHCCH "," HCCHHCJJJCCCJJJCHHCCH "," HCCHHHJJCJCJCJJHHHCCH ","  HCCHJHCHJCJHCHJHCCH  ","  HCCCHHHHCCCHHHHCCCH  ","   HCCCHHHHCHHHHCCCH   ","    HCCCCCHJHCCCCCH    ","     HHCCCCCCCCCHH     ","       HHHCCCHHH       ","          HHH          "}
            }
        ))
    //spotless:on
        .addElement('A', chainAllGlasses())
        .addElement('B', Casings.CoolantDuct.asElement())
        .addElement(
            'C',
            buildHatchAdder(MTEEndothermicFridge.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(Casings.FridgeCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(MTEEndothermicFridge::onCasingAdded, Casings.FridgeCasing.asElement())))
        .addElement('D', Casings.FrostProofMachineCasing.asElement())
        .addElement('E', Casings.TungstensteelPipeCasing.asElement())
        .addElement('F', Casings.RobustTungstenSteelMachineCasing.asElement())
        .addElement('G', ofFrame(Materials.CallistoIce))
        .addElement('H', Casings.TungstenSteelReinforcedBlock.asElement())
        .addElement('I', ofSheetMetal(Materials.Ledox))
        .addElement(
            'J',
            ofBlocksTiered(
                MTEEndothermicFridge::getTierFromBlock,
                ImmutableList.of(Pair.of(GregTechAPI.sBlockCasings2, 1), Pair.of(GregTechAPI.sBlockCasings8, 14)),
                -1,
                MTEEndothermicFridge::setMachineTier,
                MTEEndothermicFridge::getMachineTier))
        .build();

    public MTEEndothermicFridge(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEEndothermicFridge(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEEndothermicFridge> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui<?> getGui() {
        return new MTEEndothermicFridgeGui(this);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEEndothermicFridge(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Vacuum Freezer, Fridge, MVF")
            .addStaticParallelInfo(Configuration.Multiblocks.megaMachinesMax)
            .addSeparator()
            .addInfo(
                "While active, the machine will cool down and increase its speed bonus up to "
                    + EnumChatFormatting.GREEN
                    + "1.5x")
            .addInfo(
                "Takes " + EnumChatFormatting.LIGHT_PURPLE
                    + "5 minutes"
                    + EnumChatFormatting.GRAY
                    + " of constant running to reach maximum bonus")
            .addInfo("While not running, the machine will return to normal temperatures")
            .addInfo(
                "Optionally supply " + EnumChatFormatting.DARK_AQUA
                    + formatFluid(CRYOTHEUM_DRAIN_BASE)
                    + EnumChatFormatting.GRAY
                    + "/s of "
                    + EnumChatFormatting.AQUA
                    + "Cryotheum"
                    + EnumChatFormatting.GRAY
                    + " to speed up cooling by "
                    + EnumChatFormatting.BLUE
                    + "5x")
            .addInfo(
                "The drain rate of " + EnumChatFormatting.AQUA
                    + "Cryotheum"
                    + EnumChatFormatting.GRAY
                    + " will increase "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "linearly"
                    + EnumChatFormatting.GRAY
                    + " with the speed modifier")
            .addSeparator()
            .addInfo(
                "Upgrade to " + EnumChatFormatting.LIGHT_PURPLE
                    + "Tier 2"
                    + EnumChatFormatting.GRAY
                    + " to unlock "
                    + EnumChatFormatting.DARK_AQUA
                    + "Subspace Cooling")
            .addInfo(
                "Will further multiply " + EnumChatFormatting.GREEN
                    + "speed bonus "
                    + EnumChatFormatting.GRAY
                    + "by "
                    + EnumChatFormatting.GOLD
                    + "consuming "
                    + EnumChatFormatting.LIGHT_PURPLE
                    + "exotic coolants:")
            .addInfo(getCoolantTextFormatted("Molten Infinity", 200))
            .addInfo(getCoolantTextFormatted("Molten Spacetime", 400))
            .addInfo(getCoolantTextFormatted("Molten Eternity", 800))
            .addInfo(
                EnumChatFormatting.AQUA + "Cryotheum"
                    + EnumChatFormatting.GRAY
                    + " drain rate is further multiplied by the "
                    + EnumChatFormatting.GREEN
                    + "speed bonus")
            .addSeparator()
            .addTecTechHatchInfo()
            .addUnlimitedTierSkips()
            .addSeparator()
            .addInfo(EnumChatFormatting.ITALIC + "" + EnumChatFormatting.AQUA + "... without the other!")
            .beginStructureBlock(23, 16, 23, false)
            .addController("Front center, 4th layer")
            .addCasingInfoMin("Fridge Casing", 750, false)
            .addCasingInfoExactly("Tungstensteel Reinforced Block", 148, false)
            .addCasingInfoExactly("Tungstensteel Pipe Casing", 148, false)
            .addCasingInfoExactly("Callisto Ice Frame Box", 146, false)
            .addCasingInfoExactly("Robust Tungstensteel Machine Casing", 135, false)
            .addCasingInfoExactly("Coolant Duct", 50, false)
            .addCasingInfoExactly("Ledox Sheetmetal", 40, false)
            .addCasingInfoExactly("Any Tiered Glass", 18, false)
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier 1 Structure:")
            .addCasingInfoExactlyColored(
                "Frost Proof Machine Casing",
                EnumChatFormatting.GRAY,
                351,
                EnumChatFormatting.GOLD,
                false)
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier 2 Structure:")
            .addStructureInfo(EnumChatFormatting.GRAY + "Replace Frost Proof Machine Casing On")
            .addStructureInfo("Layers 1-3 With Infinity Cooled Casing")
            .addCasingInfoExactlyColored(
                "Frost Proof Machine Casing",
                EnumChatFormatting.GRAY,
                143,
                EnumChatFormatting.GOLD,
                false)
            .addCasingInfoExactlyColored(
                "Infinity Cooled Casing",
                EnumChatFormatting.GRAY,
                208,
                EnumChatFormatting.GOLD,
                false)
            .addInputBus("Any Fridge Casing", 1)
            .addOutputBus("Any Fridge Casing", 1)
            .addInputHatch("Any Fridge Casing", 1)
            .addOutputHatch("Any Fridge Casing", 1)
            .addEnergyHatch("Any Fridge Casing", 1)
            .addMaintenanceHatch("Any Fridge Casing", 1)
            .addSubChannelUsage(GTStructureChannels.BOROGLASS)
            .addStructureAuthors("Pix3lated")
            .toolTipFinisher();
        return tt;
    }

    private String getCoolantTextFormatted(String fluidType, int speedBoost) {
        return String.format(
            "%s%d L/s%s : %s%d%% %s: %s%s",
            EnumChatFormatting.GOLD,
            BOOSTER_DRAIN,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.GREEN,
            speedBoost,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.LIGHT_PURPLE,
            fluidType);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            HORIZONTAL_OFFSET,
            VERTICAL_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setBoolean("fluidDraining", isCryoEnabled);
        nbt.setFloat("speedBoost", speedBoost);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        this.isCryoEnabled = nbt.getBoolean("fluidDraining");
        this.speedBoost = nbt.getFloat("speedBoost");
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        tag.setBoolean("cryotheum", isCryoEnabled);
        tag.setInteger("drain", (int) Math.floor(speedBoost * speedMultiplier * CRYOTHEUM_DRAIN_BASE));
        tag.setFloat("speedBoost", speedBoost * speedMultiplier);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        boolean cryotheumActive = tag.getBoolean("cryotheum");
        float speedModifier = tag.getFloat("speedBoost");
        currentTip.add(translateToLocalFormatted("GT5U.waila.mvf.speedboost", formatNumber(speedModifier)));
        if (cryotheumActive) {
            currentTip.add(translateToLocalFormatted("GT5U.waila.mvf.cryotheum", formatFluid(tag.getInteger("drain"))));
        }

    }

    private void onCasingAdded() {
        casingAmount++;
    }

    @Nullable
    private static Integer getTierFromBlock(Block block, Integer metaID) {
        if (block == GregTechAPI.sBlockCasings2 && metaID == 1) { // frost proof
            return 1;
        }
        if (block == GregTechAPI.sBlockCasings8 && metaID == 14) {
            return 2; // infinity cooled casing
        }
        return null;
    }

    private void setMachineTier(int tier) {
        this.machineTier = tier;
    }

    private int getMachineTier() {
        return this.machineTier;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        machineTier = -1;
        return checkPiece(STRUCTURE_PIECE_MAIN, HORIZONTAL_OFFSET, VERTICAL_OFFSET, DEPTH_OFFSET)
            && casingAmount >= 750;
    }

    public BoosterFluid findBoosterFluid() {
        for (MTEHatchInput hatch : this.mInputHatches) {
            Optional<BoosterFluid> fluid = BOOSTER_FLUIDS.stream()
                .filter(candidate -> drain(hatch, candidate.getStack(), false))
                .findFirst();
            if (fluid.isPresent()) return fluid.get();
        }
        return null;
    }

    private boolean checkFluid(int amount) {
        final FluidStack fluidToDrain = new FluidStack(TFFluids.fluidCryotheum, amount);
        return this.depleteInput(fluidToDrain, true);
    }

    private static final int BOOSTER_DRAIN = 20;
    private static final List<BoosterFluid> BOOSTER_FLUIDS = ImmutableList.of(
        new BoosterFluid(Materials.Infinity, 2f, BOOSTER_DRAIN),
        new BoosterFluid(Materials.SpaceTime, 4f, BOOSTER_DRAIN),
        new BoosterFluid(Materials.Eternity, 8f, BOOSTER_DRAIN));

    // without cryotheum, max speed up takes 5 minutes of running to reach max speed (50%)
    // with cryotheum, it takes 1 minute.
    private static final int CRYOTHEUM_DRAIN_BASE = 250;
    private static final float INCREMENT_BASE = 1F / 60;
    private static final float INCREMENT_CRYO = INCREMENT_BASE * 5;

    private int casingAmount;
    private int machineTier;
    public boolean isCryoEnabled;
    private int runningTickCounter = 0;
    private float speedBoost = 1;
    private float speedMultiplier = 1;
    private BoosterFluid currentBoosterFluid = null;

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        runningTickCounter++;
        if (runningTickCounter % 20 == 0) { // drain every 1 second

            if (this.machineTier == 2 && this.currentBoosterFluid != null) { // booster fluid
                FluidStack fluidStack = this.currentBoosterFluid.getStack();
                if (!this.depleteInput(fluidStack, true)) stopMachine(ShutDownReasonRegistry.outOfFluid(fluidStack));
                this.depleteInput(fluidStack, false);
            }
            if (isCryoEnabled) { // cryotheum for incrementing
                final FluidStack cryotheum = new FluidStack(
                    TFFluids.fluidCryotheum,
                    (int) Math.floor(CRYOTHEUM_DRAIN_BASE * speedBoost * speedMultiplier));
                if (!this.depleteInput(cryotheum, false)) stopMachine(ShutDownReasonRegistry.outOfFluid(cryotheum));
            }
        }
        if (runningTickCounter % 100 == 0 && speedBoost < 1.5f) {
            float increment = isCryoEnabled ? INCREMENT_CRYO : INCREMENT_BASE;
            speedBoost = Math.min(1.5f, speedBoost + increment);
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMTE, long tick) {
        super.onPostTick(baseMTE, tick);
        if (!baseMTE.isServerSide()) return;
        if (mMaxProgresstime == 0 && speedBoost > 1) {
            if (tick % 20 == 0) speedBoost = Math.max(1, speedBoost - (3 * INCREMENT_BASE));
        }
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        speedMultiplier = 1;
        if (machineTier == 2) {
            currentBoosterFluid = findBoosterFluid();
            speedMultiplier = currentBoosterFluid == null ? 1 : currentBoosterFluid.speedMultiplier;
        }
        logic.setSpeedBonus(1f / (speedMultiplier * speedBoost));
        logic.setAvailableVoltage(this.getMaxInputEu());
        logic.setAvailableAmperage(1);
        logic.setUnlimitedTierSkips();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (isCryoEnabled) {
                    if (!checkFluid((int) Math.floor(CRYOTHEUM_DRAIN_BASE * speedBoost * speedMultiplier)))
                        return SimpleCheckRecipeResult.ofFailure("invalidfluidsup");
                }

                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getTrueParallel)
            .setSpeedBonus(1 / (speedBoost * speedMultiplier));
    }

    @Override
    public int getMaxParallelRecipes() {
        return Configuration.Multiblocks.megaMachinesMax;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.vacuumFreezerRecipes;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_FRIDGE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons.getCasingTextureForId(TEXTURE_ID) };
        }
        return rTexture;
    }

    @SideOnly(Side.CLIENT)
    @Override
    protected SoundResource getActivitySoundLoop() {
        return SoundResource.GT_MACHINES_MULTI_MEGA_VACUUM_FREEZER_LOOP;
    }

    // data class
    public static class BoosterFluid {

        public Materials material;
        public float speedMultiplier;
        public int amount;

        public BoosterFluid(Materials material, float speedMultiplier, int amount) {
            this.material = material;
            this.speedMultiplier = speedMultiplier;
            this.amount = amount;
        }

        public FluidStack getStack() {
            FluidStack stack = material.getFluid(amount);
            if (stack == null) {
                return material.getMolten(amount);
            }
            return stack;
        }
    }
}
