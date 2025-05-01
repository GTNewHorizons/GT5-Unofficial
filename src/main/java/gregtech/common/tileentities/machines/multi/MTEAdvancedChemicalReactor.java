package gregtech.common.tileentities.machines.multi;

import bartworks.common.tileentities.multis.mega.MTEMegaBlastFurnace;
import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronAccelerator;
import goodgenerator.blocks.tileEntity.GTMetaTileEntity.MTENeutronSensor;
import goodgenerator.util.ItemRefer;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gregtech.api.util.GlassTier;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings8;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import tectech.util.FluidStackLong;
import tectech.util.ItemStackLong;

import javax.annotation.Nullable;
import java.math.BigInteger;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.lazy;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static gregtech.api.enums.HatchElement.*;
import static gregtech.api.enums.Textures.BlockIcons.*;
import static gregtech.api.util.GTStructureUtility.*;

public class MTEAdvancedChemicalReactor extends MTEExtendedPowerMultiBlockBase<MTEAdvancedChemicalReactor>
    implements ISurvivalConstructable {

    private static final Logger log = LogManager.getLogger(MTEAdvancedChemicalReactor.class);
    private boolean isRightModule = false;
    private boolean isLeftModule = false;
    private int glassTier = -1;

    private int MODULE_LEFT = 0;
    private int MODULE_RIGHT = 0;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final String TEMP_HEAT_MODULE_L = "tempHeatL";
    private static final String TEMP_HEAT_MODULE_R = "tempHeatR";
    private static final String TEMP_COOL_MODULE_L = "tempCoolL";
    private static final String TEMP_COOL_MODULE_R = "tempCoolR";
    private static final String PRESSURE_MODULE_L = "PressureL";
    private static final String PRESSURE_MODULE_R = "PressureR";

    private boolean isbuilt = false;
    private static boolean isTempModule = false;
    private static boolean isPressureModule = false;

    private double CurrentTemp = 0;
    private double CurrentPressure = 0;

    protected int CoolCoilTier = 0;
    protected int HeatCoilTier = 0;
    private int getHeatCoilTier() {
        return HeatCoilTier;
    }
    private int getCoolCoilTier() {
        return HeatCoilTier;
    }
    private void setHeatCoilTier(int tier) {
        HeatCoilTier = tier;
    }
    private void setCoolCoilTier(int tier) {
        CoolCoilTier = tier;
    }

    private static final IStructureDefinition<MTEAdvancedChemicalReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEAdvancedChemicalReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                "AA~AA"
            },{
                "AAAAA",
                "A   A",
                "P   P",
                "A   A",
                "A   A",
                "AAAAA"
            },{
                "AAAAA",
                "A   A",
                "A   A",
                "A   A",
                "A   A",
                "AAAAA"
            },{
                "AAAAA",
                "A   A",
                "P   P",
                "A   A",
                "A   A",
                "AAAAA"
            },{
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                " AAA ",
                "AAAAA"
            }}
        )
        .addShape(
            TEMP_HEAT_MODULE_L,
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                " HH",
                " H ",
                " H ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                " HH",
                " H ",
                " H ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            }}
        )
        .addShape(
            TEMP_HEAT_MODULE_R,
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                "HH ",
                " H ",
                " H ",
                "AAA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                "HH ",
                " H ",
                " H ",
                "AAA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            }}
            // spotless:on
        )
        .addShape(
            TEMP_COOL_MODULE_L,
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                " CC",
                " C ",
                " C ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                " CC",
                " C ",
                " C ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            }}
        )
        .addShape(
            TEMP_COOL_MODULE_R,
            new String[][]{{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                "CC ",
                " C ",
                " C ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            },{
                "CC ",
                " C ",
                " C ",
                "ADA"
            },{
                "   ",
                "   ",
                "   ",
                "AAA"
            }}
        )

        .addElement('D', chainAllGlasses(-1, (te, t) -> te.glassTier = t, te -> te.glassTier))
        .addElement('P', ofBlock(GregTechAPI.sBlockCasings8, 1))
        .addElement(
            'H',
            withChannel(
                "heat_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getHeatCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 4),
                        Pair.of(GregTechAPI.sBlockCoilACR, 5),
                        Pair.of(GregTechAPI.sBlockCoilACR, 6),
                        Pair.of(GregTechAPI.sBlockCoilACR, 7)),
                    -1,
                    MTEAdvancedChemicalReactor::setHeatCoilTier,
                    MTEAdvancedChemicalReactor::getHeatCoilTier)))
        .addElement(
            'C',
            withChannel(
                "cool_pipe",
                ofBlocksTiered(
                    MTEAdvancedChemicalReactor::getCoolCoilMeta,
                    ImmutableList.of(
                        Pair.of(GregTechAPI.sBlockCoilACR, 0),
                        Pair.of(GregTechAPI.sBlockCoilACR, 1),
                        Pair.of(GregTechAPI.sBlockCoilACR, 2),
                        Pair.of(GregTechAPI.sBlockCoilACR, 3)),
                    -1,
                    MTEAdvancedChemicalReactor::setHeatCoilTier,
                    MTEAdvancedChemicalReactor::getHeatCoilTier)))
        .addElement('A', buildHatchAdder(MTEAdvancedChemicalReactor.class)
            .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy.or(ExoticEnergy))
            .casingIndex(((BlockCasings8) GregTechAPI.sBlockCasings8).getTextureIndex(0))
            .dot(1)
            .buildAndChain(GregTechAPI.sBlockCasings8, 0))
        .build();

    public MTEAdvancedChemicalReactor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAdvancedChemicalReactor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEAdvancedChemicalReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAdvancedChemicalReactor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
                                 int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[1][48], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_LARGE_CHEMICAL_REACTOR_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[1][48] };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Advanced Chemical Reactor")
            .addInfo("I have no idea what to type here")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Chemically Inert Casing", 14, false)
            .addCasingInfoExactly("Any Tiered Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Chemically Inert Casing", 1)
            .addOutputBus("Any Chemically Inert Casing", 1)
            .addInputHatch("Any Chemically Inert Casing", 1)
            .addOutputHatch("Any Chemically Inert Casing", 1)
            .addEnergyHatch("Any Chemically Inert Casing", 1)
            .addMaintenanceHatch("Any Chemically Inert Casing", 1)
            .addSubChannelUsage("glass", "Glass Tier")
            .toolTipFinisher(EnumChatFormatting.BLUE + "VorTex");
        return tt;
    }


    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 5, 0);
        /*
        0 - none
        1 - heat
        2 - freeze
        3 - compress
        4 - pump
         */
        MODULE_LEFT = 0;
        MODULE_RIGHT = 0;
        isLeftModule = false;
        if (HeatCoilTier > 0 && !isLeftModule) {MODULE_LEFT = 1; isLeftModule = true;}
        if (CoolCoilTier > 0 && !isLeftModule) {MODULE_LEFT = 2; isLeftModule = true;}
        isRightModule = false;
        if (HeatCoilTier > 0 && !isRightModule) {MODULE_RIGHT = 1; isRightModule = true;}
        if (CoolCoilTier > 0 && !isRightModule) {MODULE_RIGHT = 2; isRightModule = true;}
        switch (MODULE_LEFT) {
            case 1 -> buildPiece(TEMP_HEAT_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
            case 2 -> buildPiece(TEMP_COOL_MODULE_L, stackSize, hintsOnly, 5, 3, 0);
            default -> isLeftModule = false; //this line does nothing
        }
        switch (MODULE_RIGHT) {
            case 1 -> buildPiece(TEMP_HEAT_MODULE_R, stackSize, hintsOnly, 5, 3, 0);
            case 2 -> buildPiece(TEMP_COOL_MODULE_R, stackSize, hintsOnly, 5, 3, 0);
            default -> isRightModule = false; //this line does nothing
        }

    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 5, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 5, 0)) {
            isbuilt = false;
            return false;
        }
        isbuilt = true;
        isTempModule = checkPiece(TEMP_HEAT_MODULE_R, -3, 3, 0) ||
            checkPiece(TEMP_HEAT_MODULE_L, 5, 3, 0);
        return true;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GTUtility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    public int getMaxParallelRecipes() {return (4*GTUtility.getTier(this.getMaxInputVoltage()));}
    @Override
    public RecipeMap<?> getRecipeMap() {return RecipeMaps.multiblockAdvancedChemicalReactorRecipes;}
    @Override
    public int getMaxEfficiency(ItemStack aStack) {return 10000;}
    @Override
    public int getDamageToComponent(ItemStack aStack) {return 0;}
    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {return false;}
    @Override
    public boolean supportsVoidProtection() {return true;}
    @Override
    public boolean supportsBatchMode() {return true;}
    @Override
    public boolean supportsInputSeparation() {return true;}
    @Override
    public boolean supportsSingleRecipeLocking() {return true;}

    @Nullable
    private static Integer getHeatCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID < 4 || metaID > 7) return null;
        return metaID - 3;
    }
    @Nullable
    private static Integer getCoolCoilMeta(Block block, Integer metaID) {
        if (block != GregTechAPI.sBlockCoilACR) return null;
        if (metaID > 3) return null;
        return metaID + 1;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (isbuilt && (aTick%20==0)) {
            if (isTempModule) {
                CurrentTemp += 1.0;
                CurrentTemp = CurrentTemp / 1.05;
                CurrentTemp = Math.max(0, CurrentTemp);
            }
            else CurrentTemp = 300;
            if (isPressureModule) {
                CurrentPressure = Math.max(0, CurrentPressure);
            }
            else CurrentPressure = 101;
            System.out.println(CurrentTemp);
        }
        System.out.println(HeatCoilTier);
        System.out.println(isRightModule);
        System.out.println(isLeftModule);
    }
}
