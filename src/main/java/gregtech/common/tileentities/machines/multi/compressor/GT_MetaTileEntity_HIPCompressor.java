package gregtech.common.tileentities.machines.multi.compressor;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.GT_Values.Ollie;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofCoil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.bartimaeusnek.bartworks.util.MathUtils;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.metadata.CompressionTierKey;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.api.util.shutdown.SimpleShutDownReason;
import gregtech.common.blocks.GT_Block_Casings10;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_HIPCompressor extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_HIPCompressor> implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_HIPCompressor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_HIPCompressor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            transpose(new String[][]{
                {"               ","               ","               "," CCCCCC DDDDDD ","               ","               ","               "},
                {"               ","               ","               "," C    C D    D ","               ","               ","               "},
                {"               ","      HHH      ","     HHHHH     "," C   HHHHH   D ","     HHHHH     ","      HHH      ","               "},
                {"      BBB      ","     BBBBB     ","    BBBBBBB    "," C  BBBBBBB  D ","    BBBBBBB    ","     BBBBB     ","      BBB      "},
                {"      HHH      ","     B   B     ","    BF   FB    "," C  BF   FB  D ","    BF   FB    ","     B   B     ","      HHH      "},
                {"      HAH      ","     B   B     ","    GF   FG    "," C  GF   FG  D ","    GF   FG    ","     B   B     ","      HAH      "},
                {"      HAH      ","     B   B     "," E  GF   FG  E ","EEE GF   FG EEE"," E  GF   FG  E ","     B   B     ","      HAH      "},
                {"      HAH      ","     B   B     "," A  GF   FG  A ","A A GF   FG A A"," A  GF   FG  A ","     B   B     ","      HAH      "},
                {"      HHH      ","     B   B     "," A  BF   FB  A ","A A BF   FB A A"," A  BF   FB  A ","     B   B     ","      HHH      "},
                {"      B~B      ","     BBBBB     "," E  BBBBBBB  E ","EEE BBBBBBB EEE"," E  BBBBBBB  E ","     BBBBB     ","      BBB      "}
            }))
            //spotless:on
        .addElement('A', Glasses.chainAllGlasses())
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_HIPCompressor.class)
                .atLeast(Maintenance, Energy, SpecialHatchElement.HeatSensor)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(4))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_HIPCompressor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 4))))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings10, 9))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings10, 10))
        .addElement('E', ofBlock(GregTech_API.sBlockCasings4, 1))
        .addElement(
            'F',
            ofCoil(GT_MetaTileEntity_HIPCompressor::setCoilLevel, GT_MetaTileEntity_HIPCompressor::getCoilLevel))
        .addElement(
            'G',
            buildHatchAdder(GT_MetaTileEntity_HIPCompressor.class).atLeast(InputBus, OutputBus)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(5))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_HIPCompressor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 5))))
        .addElement('H', ofBlock(GregTech_API.sBlockCasings10, 5))
        .build();

    private final ArrayList<GT_MetaTileEntity_HeatSensor> sensorHatches = new ArrayList<>();

    private HeatingCoilLevel heatLevel;
    private int coilTier = 0;

    private float heat = 0;
    private boolean cooling = false;

    public GT_MetaTileEntity_HIPCompressor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_HIPCompressor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_HIPCompressor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_HIPCompressor(this.mName);
    }

    @Override
    public void onValueUpdate(byte aValue) {
        boolean oCooling = cooling;
        cooling = (aValue & 1) == 1;
        if (oCooling != cooling) getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public byte getUpdateData() {
        return (byte) (cooling ? 1 : 0);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (cooling) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 4)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_COOLING_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 4)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 4)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_COMPRESSOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 4)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Compressor")
            .addInfo("Controller Block for the Hot Isostatic Pressurization Unit")
            .addInfo("HIP Unit heats up while running")
            .addInfo(
                "When it reaches maximum heat, it becomes " + EnumChatFormatting.DARK_RED
                    + EnumChatFormatting.BOLD
                    + "overheated!")
            .addInfo("This is only resolved by letting the machine fully cool down")
            .addInfo(
                "When " + EnumChatFormatting.DARK_RED
                    + "overheated"
                    + EnumChatFormatting.GRAY
                    + ", recipes are slowed down drastically")
            .addSeparator()
            .addInfo(
                "Some recipes " + EnumChatFormatting.BOLD
                    + "require"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " HIP")
            .addInfo(
                "If the machine " + EnumChatFormatting.DARK_RED
                    + "overheats"
                    + EnumChatFormatting.GRAY
                    + " during these recipes, recipe will be voided!")
            .addInfo("Read the current heat using Heat Sensor Hatches")
            .addSeparator()
            .addInfo("More advanced coils allow better heat control - the unit will take longer to overheat")
            .addInfo(
                "Unit heats by " + EnumChatFormatting.GREEN
                    + "(5% x 0.90 ^ (Coil Tier - 1))"
                    + EnumChatFormatting.GRAY
                    + " every second while running")
            .addInfo(
                "Unit cools by " + EnumChatFormatting.GREEN
                    + "2%"
                    + EnumChatFormatting.GRAY
                    + " every second while not running")
            .addSeparator()
            .addInfo(
                "250% " + EnumChatFormatting.RED
                    + "faster"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.BLUE
                    + "slower"
                    + EnumChatFormatting.GRAY
                    + " than singleblock machines of the same voltage")
            .addInfo(
                "Uses " + EnumChatFormatting.RED
                    + "75%"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.BLUE
                    + "110%"
                    + EnumChatFormatting.GRAY
                    + " the EU/t normally required")
            .addInfo(
                "Gains " + EnumChatFormatting.RED
                    + "4"
                    + EnumChatFormatting.GRAY
                    + "/"
                    + EnumChatFormatting.BLUE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + " parallels per voltage tier")
            .addInfo(AuthorFourIsTheNumber + EnumChatFormatting.RESET + " & " + Ollie)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Electric Compressor Casing", 95, false)
            .addCasingInfoMin("Compressor Pipe Casing", 45, false)
            .addCasingInfoExactly("Coolant Duct", 12, false)
            .addCasingInfoExactly("Heating Duct", 12, false)
            .addCasingInfoExactly("EV+ Glass", 22, false)
            .addCasingInfoExactly("Clean Stainless Steel Machine Casing", 20, false)
            .addCasingInfoExactly("Coil", 30, true)
            .addInputBus("Pipe Casings on Side", 2)
            .addOutputBus("Pipe Casings on Side", 2)
            .addEnergyHatch("Any Electric Compressor Casing", 1)
            .addMaintenanceHatch("Any Electric Compressor Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 7, 9, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 7, 9, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        setCoilLevel(HeatingCoilLevel.None);
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 7, 9, 0) && mCasingAmount >= 95;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setFloat("heat", heat);
        aNBT.setBoolean("cooling", cooling);
        aNBT.setInteger("coilTier", coilTier);
        aNBT.setBoolean("doingHIP", doingHIP);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("heat")) heat = aNBT.getFloat("heat");
        if (aNBT.hasKey("cooling")) cooling = aNBT.getBoolean("cooling");
        if (aNBT.hasKey("coilTier")) coilTier = aNBT.getInteger("coilTier");
        if (aNBT.hasKey("doingHIP")) doingHIP = aNBT.getBoolean("doingHIP");
        super.loadNBTData(aNBT);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("heat", Math.round(heat));
        tag.setBoolean("cooling", cooling);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        if (tag.getBoolean("cooling")) currentTip.add(
            "HIP Heat: " + EnumChatFormatting.RED
                + EnumChatFormatting.BOLD
                + tag.getInteger("heat")
                + "%"
                + EnumChatFormatting.RESET);
        else currentTip.add(
            "HIP Heat: " + EnumChatFormatting.AQUA
                + EnumChatFormatting.BOLD
                + tag.getInteger("heat")
                + "%"
                + EnumChatFormatting.RESET);
    }

    private boolean doingHIP = false;

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GT_Recipe recipe) {
                doingHIP = false;
                setSpeedBonus(1F / 1.25F);
                setEuModifier(0.75F);

                if (cooling) {
                    setSpeedBonus(2.5F);
                    setEuModifier(1.1F);
                }

                if (recipe.getMetadataOrDefault(CompressionTierKey.INSTANCE, 1) > 0) doingHIP = true;
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        if (cooling && doingHIP) {
            stopMachine(SimpleShutDownReason.ofCritical("overheated"));
            doingHIP = false;
        }
        return super.onRunningTick(aStack);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aTick % 20 == 0) {

            // Default to cooling by 2%
            float heatMod = -2;

            // If the machine is running, heat by 5% x 0.90 ^ (Coil Tier)
            // Cupronickel is 0, so base will be 5% increase
            if (this.maxProgresstime() != 0) {
                heatMod = (float) (5 * Math.pow(0.9, coilTier));
            }

            heat = MathUtils.clamp(heat + heatMod, 0, 100);

            if ((cooling && heat <= 0) || (!cooling && heat >= 100)) {
                cooling = !cooling;
            }
        }

        // Update all the sensors
        for (GT_MetaTileEntity_HeatSensor hatch : sensorHatches) {
            hatch.updateRedstoneOutput(heat);
        }

    }

    public int getMaxParallelRecipes() {
        return cooling ? GT_Utility.getTier(this.getMaxInputVoltage())
            : (4 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.compressorRecipes;
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
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    public HeatingCoilLevel getCoilLevel() {
        return heatLevel;
    }

    public void setCoilLevel(HeatingCoilLevel aCoilLevel) {
        heatLevel = aCoilLevel;
        coilTier = aCoilLevel.getTier();
    }

    public boolean addSensorHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity instanceof GT_MetaTileEntity_HeatSensor sensor) {
            sensor.updateTexture(aBaseCasingIndex);
            return this.sensorHatches.add(sensor);
        }
        return false;
    }

    private enum SpecialHatchElement implements IHatchElement<GT_MetaTileEntity_HIPCompressor> {

        HeatSensor(GT_MetaTileEntity_HIPCompressor::addSensorHatchToMachineList, GT_MetaTileEntity_HeatSensor.class) {

            @Override
            public long count(GT_MetaTileEntity_HIPCompressor gtMetaTileEntityHIPCompressor) {
                return gtMetaTileEntityHIPCompressor.sensorHatches.size();
            }
        };

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGT_HatchAdder<GT_MetaTileEntity_HIPCompressor> adder;

        @SafeVarargs
        SpecialHatchElement(IGT_HatchAdder<GT_MetaTileEntity_HIPCompressor> adder,
            Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        public IGT_HatchAdder<? super GT_MetaTileEntity_HIPCompressor> adder() {
            return adder;
        }
    }
}
