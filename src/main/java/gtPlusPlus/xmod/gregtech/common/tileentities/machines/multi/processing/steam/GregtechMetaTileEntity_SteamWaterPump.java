package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.steam;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.GT_Values.AuthorEvgenWarGold;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.VoidProtectionHelper;
import gregtech.common.blocks.GT_Block_Casings9;
import gtPlusPlus.core.util.minecraft.PlayerUtils;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GregtechMeta_SteamMultiBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GregtechMetaTileEntity_SteamWaterPump
    extends GregtechMeta_SteamMultiBase<GregtechMetaTileEntity_SteamWaterPump> implements ISurvivalConstructable {

    public GregtechMetaTileEntity_SteamWaterPump(String aName) {
        super(aName);
    }

    public GregtechMetaTileEntity_SteamWaterPump(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GregtechMetaTileEntity_SteamWaterPump(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Water Pump";
    }

    private static IStructureDefinition<GregtechMetaTileEntity_SteamWaterPump> STRUCTURE_DEFINITION = null;

    private static final int HORIZONTAL_OFF_SET = 1;
    private static final int VERTICAL_OFF_SET = 2;
    private static final int DEPTH_OFF_SET = 0;
    private static final String tier1 = "tier1";
    private static final String tier2 = "tier2";

    // Base amount of water produced per second, before applying humidity and tier modifiers.
    private static final int BASE_WATER_PER_SECOND = 1_500;
    private static final int PROGRESSION_TIME_TICKS = 20;

    private static final int BASE_STEAM_PER_SECOND = 400;

    private int mSetTier = 1;

    private float currentHumidity;

    private static final Fluid water = FluidRegistry.getFluid("water");

    private FluidStack[] getWater() {
        return new FluidStack[] { new FluidStack(water, calculateFinalWaterOutput()) };
    }

    private int mCountCasing;

    private float getHumidity() {
        return this.getBaseMetaTileEntity()
            .getWorld()
            .getBiomeGenForCoords(getBaseMetaTileEntity().getXCoord(), getBaseMetaTileEntity().getZCoord()).rainfall;
    }

    private int calculateFinalWaterOutput() {
        return (int) (currentHumidity * BASE_WATER_PER_SECOND * mSetTier);
    }

    // spotless:off
    @Override
    public IStructureDefinition<GregtechMetaTileEntity_SteamWaterPump> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {

            STRUCTURE_DEFINITION = StructureDefinition.<GregtechMetaTileEntity_SteamWaterPump>builder()

                .addShape(
                    tier1,
                    transpose(
                        new String[][] {
                            { " A ", " A ", "AAA", " A " },
                            { " A ", "   ", "A A", " A " },
                            { "C~C", "CCC", "CCC", "CCC" } }))
                .addShape(
                    tier2,
                    transpose(
                        new String[][] {
                            { " D ", " D ", "DDD", " D " },
                            { " D ", "   ", "D D", " D " },
                            { "C~C", "CCC", "CCC", "CCC" } }))
                .addElement('A', ofFrame(Materials.Bronze))
                .addElement('D', ofFrame(Materials.Steel))
                .addElement(
                    'C',
                        ofChain(
                            buildSteamInput(GregtechMetaTileEntity_SteamWaterPump.class)
                                .casingIndex(((GT_Block_Casings9) GregTech_API.sBlockCasings9).getTextureIndex(2))
                                .dot(1)
                                .build(),
                            buildHatchAdder(GregtechMetaTileEntity_SteamWaterPump.class)
                                .atLeast(OutputHatch)
                                .casingIndex(((GT_Block_Casings9) GregTech_API.sBlockCasings9).getTextureIndex(2))
                                .dot(1)
                                .buildAndChain(onElementPass(x -> ++x.mCountCasing, ofBlock(GregTech_API.sBlockCasings9, 2)))
                        )
                    )
                .build();

        }
        return STRUCTURE_DEFINITION;
    }
    // spotless:on

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        if (stackSize.stackSize == 1) {
            this.buildPiece(tier1, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
        } else {
            this.buildPiece(tier2, stackSize, hintsOnly, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET);
        }
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (this.mMachine) return -1;
        int built = 0;
        if (stackSize.stackSize == 1) {
            mSetTier = 1;
            built += this.survivialBuildPiece(
                tier1,
                stackSize,
                HORIZONTAL_OFF_SET,
                VERTICAL_OFF_SET,
                DEPTH_OFF_SET,
                elementBudget,
                env,
                false,
                true);
        } else {
            mSetTier = 2;
            built += this.survivialBuildPiece(
                tier2,
                stackSize,
                HORIZONTAL_OFF_SET,
                VERTICAL_OFF_SET,
                DEPTH_OFF_SET,
                elementBudget,
                env,
                false,
                true);
        }
        return built;
    }

    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCountCasing = 0;
        mSetTier = 1;
        if (!checkPiece(tier1, HORIZONTAL_OFF_SET, VERTICAL_OFF_SET, DEPTH_OFF_SET)) {
            if (!checkPiece(tier2, HORIZONTAL_OFF_SET,VERTICAL_OFF_SET, DEPTH_OFF_SET)) return false;
            mSetTier = 2;
        }

        if (this.mOutputHatches.size() != 1 || this.mSteamInputFluids.size() != 1) return false;

        currentHumidity = getHumidity();
        return mCountCasing >= 9;
    }

    @Override
    public ITexture[] getTexture(final IGregTechTileEntity aBaseMetaTileEntity, final ForgeDirection side,
        final ForgeDirection facing, final int aColorIndex, final boolean aActive, final boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] {
                Textures.BlockIcons
                    .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 2)),
                aActive ? getFrontOverlayActive() : getFrontOverlay() };
        }
        return new ITexture[] { Textures.BlockIcons
            .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings9, 2)) };
    }

    @Override
    protected GT_RenderedTexture getFrontOverlay() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP);
    }

    @Override
    protected GT_RenderedTexture getFrontOverlayActive() {
        return new GT_RenderedTexture(Textures.BlockIcons.OVERLAY_FRONT_WATER_PUMP_ACTIVE);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Water Pump")
            .addInfo("Generates water based on biome humidity")
            .addInfo("Has 2 tiers: Bronze and Steel")
            .addInfo("Steel tier extracts 2x water")
            .addInfo(
                EnumChatFormatting.AQUA + "Generates: "
                    + EnumChatFormatting.WHITE
                    + " humidity * Tier * " + BASE_WATER_PER_SECOND + " L/s"
                    + EnumChatFormatting.AQUA + " of water."
                    + EnumChatFormatting.RESET)
            .addInfo(
                EnumChatFormatting.RED + "Consumes: "
                    + EnumChatFormatting.WHITE
                    + BASE_STEAM_PER_SECOND + " L/s"
                    + EnumChatFormatting.RED + " of steam."
                    + EnumChatFormatting.RESET)
            .addSeparator()
            .beginStructureBlock(3, 3, 5, false)
            .addInputHatch(EnumChatFormatting.GOLD + "1" + EnumChatFormatting.GRAY + " Any casing", 1)
            .addStructureInfo(
                EnumChatFormatting.WHITE + "Steam Input Hatch "
                    + EnumChatFormatting.GOLD
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " Any casing")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 1)
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Bronze Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Wooden Casing")
            .addStructureInfo(EnumChatFormatting.BLUE + "Tier " + EnumChatFormatting.DARK_PURPLE + 2)
            .addStructureInfo(EnumChatFormatting.GOLD + "10" + EnumChatFormatting.GRAY + " Steel Frame Box")
            .addStructureInfo(EnumChatFormatting.GOLD + "10 " + EnumChatFormatting.GRAY + " Wooden Casing")
            .toolTipFinisher(AuthorEvgenWarGold);
        return tt;
    }

    @Override
    @NotNull
    public CheckRecipeResult checkProcessing() {

        VoidProtectionHelper voidProtection = new VoidProtectionHelper().setMachine(this)
            .setFluidOutputs(getWater())
            .build();

        if (voidProtection.isFluidFull()) {
            mOutputFluids = null;
            mMaxProgresstime = 0;
            return CheckRecipeResultRegistry.FLUID_OUTPUT_FULL;
        } else {
            if (getTotalSteamStored() >= BASE_STEAM_PER_SECOND) {
                mMaxProgresstime = PROGRESSION_TIME_TICKS;
                tryConsumeSteam(BASE_STEAM_PER_SECOND);
                mOutputFluids = getWater();
                updateSlots();
                return CheckRecipeResultRegistry.SUCCESSFUL;
            } else return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            if ((aTick % 1200) == 0) {
                currentHumidity = getHumidity();
            }
        }
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        NBTTagCompound tag = accessor.getNBTData();

        currenttip.add(
            StatCollector.translateToLocal("GT5U.machines.tier") + ": "
                + EnumChatFormatting.BLUE
                + tag.getInteger("mSetTier")
                + EnumChatFormatting.RESET);
        currenttip.add(
            StatCollector.translateToLocal("GT5U.biomes.humidity") + " "
                + EnumChatFormatting.BLUE
                + tag.getFloat("humidity")
                + " %"
                + EnumChatFormatting.RESET);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setFloat("humidity", currentHumidity * 100);
        tag.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mSetTier", mSetTier);
    }

    @Override
    public void loadNBTData(final NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        mSetTier = aNBT.getInteger("mSetTier");
    }

    @Override
    public int getMaxParallelRecipes() {
        return 1;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }

}
