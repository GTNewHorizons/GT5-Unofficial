package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.ExoticDynamo;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_GLOW_ON;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_OFF;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_TOKAMAK_ON;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.List;
import java.util.function.Supplier;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.opengl.GL11;

import com.cleanroommc.modularui.utils.Color;
import com.cleanroommc.modularui.value.sync.FloatSyncValue;
import com.cleanroommc.modularui.value.sync.IntSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.client.renderer.postprocessing.shaders.BloomShader;
import com.gtnewhorizon.gtnhlib.client.renderer.shader.ShaderProgram;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.objects.XSTR;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.gui.modularui.multiblock.MTEQuadcellTokamakGui;
import gregtech.common.render.IMTERenderer;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialsElements;
import gtnhlanth.common.register.LanthItemList;
import io.netty.buffer.ByteBuf;

public class MTEQuadcellTokamak extends MTEExtendedPowerMultiBlockBase<MTEQuadcellTokamak>
    implements ISurvivalConstructable, ICasingTextureProvider, IMTERenderer {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private final XSTR random = XSTR.XSTR_INSTANCE;
    private static final int WIDTH_OFFSET = 9;
    private static final int HEIGHT_OFFSET = 12;
    private static final int DEPTH_OFFSET = 1;

    // How many cycles should it take between outputs of residue
    public static final int CYCLES_FOR_RESIDUE = 5;
    // The rate of plasma to residue, X plasma makes 1 residue
    public static final int RESIDUE_CONVERSION_DIVISOR = 300;

    public int FORCE_CURRENT_DR = 0;
    public int RUNITE_CURRENT_DR = 0;
    public int CELESTIAL_TUNGSTEN_CURRENT_DR = 0;
    public int ORIKALKUM_CURRENT_DR = 0;

    public float FORCE_CURRENT_BOOST = 0f;
    public float RUNITE_CURRENT_BOOST = 0f;
    public float CELESTIAL_TUNGSTEN_CURRENT_BOOST = 0f;
    public float ORIKALKUM_CURRENT_BOOST = 1f;

    public int residueCycles = 0;
    public int drainedSinceLastOutput = 0;

    public boolean terminalSwitch = false;

    private static final IStructureDefinition<MTEQuadcellTokamak> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEQuadcellTokamak>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
           transpose(new String[][]{
               {"                   ","                   ","        BBB        ","       BBBBB       ","       BBBBB       ","       BBBBB       ","        BBB        ","                   ","                   "},
               {"                   ","        DDD        ","       D   D       ","      D     D      ","      D  C  D      ","      D     D      ","       D   D       ","        DDD        ","                   "},
               {"                   ","                   ","        BBB        ","       B   B       ","       B   B       ","       B   B       ","        BBB        ","                   ","                   "},
               {"                   ","                   ","         B         ","        BBB        ","       BB BB       ","        BBB        ","         B         ","                   ","                   "},
               {"                   ","                   ","                   ","         C         ","        C C        ","         C         ","                   ","                   ","                   "},
               {"                   ","                   ","         B         ","        BCB        ","       BC CB       ","        BCB        ","         B         ","                   ","                   "},
               {"                   ","        BBB        ","       BBABB       "," D    BBAAABB    D "," D    BAA AAB    D "," D    BBAAABB    D ","       BBABB       ","        BBB        ","                   "},
               {"         D         ","       DD DD       "," D    B     B    D ","B B   BA   AB   B B","B BB BA     AB BB B","B B   BA   AB   B B"," D    B     B    D ","       DD DD       ","         D         "},
               {"        DDD        "," D    DD   DD    D ","B B   B     B   B B","B  B BA     AB B  B","B  BCCA     ACCB  B","B  B BA     AB B  B","B B   B     B   B B"," D    DD   DD    D ","        DDD        "},
               {"       DDDDD       "," D    D     D    D ","B BB BA     AB BB B","B  BCCA     ACCB  B","BC               CB","B  BCCA     ACCB  B","B BB BA     AB BB B"," D    D     D    D ","       DDDDD       "},
               {"        DDD        "," D    DD   DD    D ","B B   B     B   B B","B  B BA     AB B  B","B  BCCA     ACCB  B","B  B BA     AB B  B","B B   B     B   B B"," D    DD   DD    D ","        DDD        "},
               {"         D         ","       DD DD       "," D    B     B    D ","B B   BA   AB   B B","B BB BA     AB BB B","B B   BA   AB   B B"," D    B     B    D ","       DD DD       ","         D         "},
               {"                   ","        B~B        ","       BBABB       "," D    BBAAABB    D "," D    BAA AAB    D "," D    BBAAABB    D ","       BBABB       ","        BBB        ","                   "},
               {"                   ","                   ","         B         ","        BCB        ","       BC CB       ","        BCB        ","         B         ","                   ","                   "},
               {"                   ","                   ","                   ","         C         ","        C C        ","         C         ","                   ","                   ","                   "},
               {"                   ","                   ","         B         ","        BBB        ","       BB BB       ","        BBB        ","         B         ","                   ","                   "},
               {"                   ","                   ","        BBB        ","       B   B       ","       B   B       ","       B   B       ","        BBB        ","                   ","                   "},
               {"                   ","        DDD        ","       D   D       ","      D     D      ","      D  C  D      ","      D     D      ","       D   D       ","        DDD        ","                   "},
               {"                   ","                   ","        BBB        ","       BBBBB       ","       BBBBB       ","       BBBBB       ","        BBB        ","                   ","                   "}
           }))
        .addElement('A', Casings.SuperconductingCoilBlock.asElement())
        .addElement('B',buildHatchAdder(MTEQuadcellTokamak.class).atLeast(Dynamo.or(ExoticDynamo), InputHatch, OutputHatch)
                    .casingIndex(Casings.PressureContainmentCasing.textureId)
                    .hint(1)
                    .buildAndChain(Casings.PressureContainmentCasing.asElement()))
        .addElement('C', Casings.InsulatedFluidPipeCasing.asElement())
        .addElement('D', ofBlock(LanthItemList.SHIELDED_ACCELERATOR_GLASS, 0))
        //spotless:on
        .build();

    public MTEQuadcellTokamak(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEQuadcellTokamak(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEQuadcellTokamak> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEQuadcellTokamak(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_TOKAMAK_OFF,
            OVERLAY_TOKAMAK_GLOW,
            OVERLAY_TOKAMAK_ON,
            OVERLAY_TOKAMAK_GLOW_ON);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.AdvancedIridiumPlatedMachineCasing.getCasingTexture();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Tokamak, QT")
            .beginStructureBlock(23, 13, 9, true)
            .addController("Front center, 2nd layer")
            .addInfo("Burns certain Plasmas to generate power")
            .addInfo(
                "Set " + EnumChatFormatting.GREEN
                    + "Drain Rate (L/s)"
                    + EnumChatFormatting.GRAY
                    + " of Plasmas in the Controller")
            .addSeparator()
            .addInfo("Will burn Plasmas up to " + EnumChatFormatting.GREEN + "X L/s")
            .addInfo(
                "Plasmas have varying " + EnumChatFormatting.AQUA
                    + "Densities (EU/L)"
                    + EnumChatFormatting.GRAY
                    + " and provide "
                    + EnumChatFormatting.WHITE
                    + "Buffs"
                    + EnumChatFormatting.GRAY
                    + " to all burned Plasmas")
            .addInfo(
                "Drain Rate amounts below the maximum will provide proportional " + EnumChatFormatting.WHITE
                    + "Buffs"
                    + EnumChatFormatting.GRAY
                    + " of [Current / "
                    + EnumChatFormatting.GREEN
                    + "Max"
                    + EnumChatFormatting.GRAY
                    + "]")
            .addInfo(
                getPlasmaTextFormatted(
                    "Force",
                    EnumChatFormatting.GOLD,
                    formatNumber(PlasmaType.FORCE.maxDR),
                    formatNumber(PlasmaType.FORCE.density),
                    "+20% EU/L"))
            .addInfo(
                getPlasmaTextFormatted(
                    "Runite",
                    EnumChatFormatting.BLUE,
                    formatNumber(PlasmaType.RUNITE.maxDR),
                    formatNumber(PlasmaType.RUNITE.density),
                    "10% chance to not consume Plasma"))
            .addInfo(
                getPlasmaTextFormatted(
                    "Celestial Tungsten",
                    EnumChatFormatting.DARK_GREEN,
                    formatNumber(PlasmaType.CELESTIAL.maxDR),
                    formatNumber(PlasmaType.CELESTIAL.density),
                    "+5% EU/L, +5% chance to not consume Plasma"))
            .addInfo(
                getPlasmaTextFormatted(
                    "Orikalkum",
                    EnumChatFormatting.RED,
                    formatNumber(PlasmaType.ORIKALKUM.maxDR),
                    formatNumber(PlasmaType.ORIKALKUM.density),
                    "+50% to other Plasma Buffs"))
            .addSeparator()
            .addInfo(
                "Consumed Plasmas are " + EnumChatFormatting.BOLD
                    + "NOT"
                    + EnumChatFormatting.RESET
                    + EnumChatFormatting.GRAY
                    + " returned in molten form")
            .addInfo(
                "If " + EnumChatFormatting.GOLD
                    + "Force"
                    + EnumChatFormatting.GRAY
                    + ", "
                    + EnumChatFormatting.BLUE
                    + "Runite"
                    + EnumChatFormatting.GRAY
                    + ", and "
                    + EnumChatFormatting.DARK_GREEN
                    + "Celestial Tungsten"
                    + EnumChatFormatting.GRAY
                    + " Plasmas are supplied:")
            .addInfo(
                "Periodically outputs " + EnumChatFormatting.DARK_AQUA
                    + "Tokamak Residue"
                    + EnumChatFormatting.GRAY
                    + " at a rate of 1L per "
                    + RESIDUE_CONVERSION_DIVISOR
                    + "L of Plasma burned")
            .addSupportAny()
            .addStructureInfo("")
            .toolTipFinisher();
        return tt;
    }

    private String getPlasmaTextFormatted(String plasma, EnumChatFormatting plasmaColor, String dr, String density,
        String buff) {
        return String.format(
            "%s%s%s / %s%s%s / %s%s%s / %s%s ",
            plasmaColor,
            plasma,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.GREEN,
            dr,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.AQUA,
            density,
            EnumChatFormatting.GRAY,
            EnumChatFormatting.WHITE,
            buff);
    }

    private static final int CYCLE_TIME = 20;

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {

        // Increment Bonuses, if needed, up to the cap.

        // Bonuses increment every cycle until they are at the cap of their respective bonus.
        // It takes 10 full cycles to increment the bonus all the way (10 seconds)
        // The bonus cap is equal to [SET_DR / MAX_DR] * MAX_BONUS
        lEUt = 0;
        if (FORCE_CURRENT_BOOST != PlasmaType.FORCE.maxBoost) {
            float FORCE_CURRENT_CAP = PlasmaType.FORCE.maxBoost * FORCE_CURRENT_DR / PlasmaType.FORCE.maxDR;
            float step = FORCE_CURRENT_CAP / 10;
            FORCE_CURRENT_BOOST = Math.min(PlasmaType.FORCE.maxBoost, FORCE_CURRENT_BOOST + step);
        }

        if (RUNITE_CURRENT_BOOST != PlasmaType.RUNITE.maxBoost) {
            float RUNITE_CURRENT_CAP = PlasmaType.RUNITE.maxBoost * RUNITE_CURRENT_DR / PlasmaType.RUNITE.maxDR;
            float step = RUNITE_CURRENT_CAP / 10;
            RUNITE_CURRENT_BOOST = Math.min(PlasmaType.RUNITE.maxBoost, RUNITE_CURRENT_BOOST + step);
        }

        if (CELESTIAL_TUNGSTEN_CURRENT_BOOST != PlasmaType.CELESTIAL.maxBoost) {
            float CELESTIAL_TUNGSTEN_CURRENT_CAP = PlasmaType.CELESTIAL.maxBoost * CELESTIAL_TUNGSTEN_CURRENT_DR
                / PlasmaType.CELESTIAL.maxDR;
            float step = CELESTIAL_TUNGSTEN_CURRENT_CAP / 10;
            CELESTIAL_TUNGSTEN_CURRENT_BOOST = Math
                .min(PlasmaType.CELESTIAL.maxBoost, CELESTIAL_TUNGSTEN_CURRENT_BOOST + step);
        }

        if (ORIKALKUM_CURRENT_BOOST != PlasmaType.ORIKALKUM.maxBoost) {
            float ORIKALKUM_CURRENT_CAP = PlasmaType.ORIKALKUM.maxBoost * ORIKALKUM_CURRENT_DR
                / PlasmaType.ORIKALKUM.maxDR;
            float step = ORIKALKUM_CURRENT_CAP / 10;
            ORIKALKUM_CURRENT_BOOST = Math.min(PlasmaType.ORIKALKUM.maxBoost, ORIKALKUM_CURRENT_BOOST + step);
        }

        float eutBoost = 1 + ((FORCE_CURRENT_BOOST + CELESTIAL_TUNGSTEN_CURRENT_BOOST) * ORIKALKUM_CURRENT_BOOST); // >1
        float nonDrainChance = (RUNITE_CURRENT_BOOST + CELESTIAL_TUNGSTEN_CURRENT_BOOST) * ORIKALKUM_CURRENT_BOOST; // <1
        long euRate = 0;

        FluidStack forceStack = PlasmaType.FORCE.getFluid(FORCE_CURRENT_DR);
        FluidStack runiteStack = PlasmaType.RUNITE.getFluid(RUNITE_CURRENT_DR);
        FluidStack celestialtungstenStack = PlasmaType.CELESTIAL.getFluid(CELESTIAL_TUNGSTEN_CURRENT_DR);
        FluidStack orikalkumStack = PlasmaType.ORIKALKUM.getFluid(ORIKALKUM_CURRENT_DR);

        // if fluid can't be drained, turn off and clear all bonuses
        if (FORCE_CURRENT_DR > 0) {
            if (!processFluid(forceStack, nonDrainChance)) {
                return crashMachine(forceStack);
            }
            euRate += (long) (eutBoost * FORCE_CURRENT_DR * PlasmaType.FORCE.density / 20);
        }
        if (RUNITE_CURRENT_DR > 0) {
            if (!processFluid(runiteStack, nonDrainChance)) {
                return crashMachine(runiteStack);
            }
            euRate += (long) (eutBoost * RUNITE_CURRENT_DR * PlasmaType.RUNITE.density / 20);
        }
        if (CELESTIAL_TUNGSTEN_CURRENT_DR > 0) {
            if (!processFluid(celestialtungstenStack, nonDrainChance)) {
                return crashMachine(celestialtungstenStack);
            }
            euRate += (long) (eutBoost * CELESTIAL_TUNGSTEN_CURRENT_DR * PlasmaType.CELESTIAL.density / 20);
        }
        if (ORIKALKUM_CURRENT_DR > 0) {
            if (!processFluid(orikalkumStack, nonDrainChance)) {
                return crashMachine(orikalkumStack);
            }
            euRate += (long) (eutBoost * ORIKALKUM_CURRENT_DR * PlasmaType.ORIKALKUM.density / 20);
        }

        // if successfully drained all expected fluids, check to output residue
        if (FORCE_CURRENT_DR > 0 && RUNITE_CURRENT_DR > 0 && CELESTIAL_TUNGSTEN_CURRENT_DR > 0) {
            if (residueCycles == CYCLES_FOR_RESIDUE) {
                int residueOutput = Math.floorDiv(drainedSinceLastOutput, RESIDUE_CONVERSION_DIVISOR);
                addOutput(Materials.TokamakResidue.getFluid(residueOutput));
                residueCycles = 0;
                drainedSinceLastOutput = 0;
            }

            drainedSinceLastOutput += FORCE_CURRENT_DR;
            drainedSinceLastOutput += RUNITE_CURRENT_DR;
            drainedSinceLastOutput += CELESTIAL_TUNGSTEN_CURRENT_DR;
            drainedSinceLastOutput += ORIKALKUM_CURRENT_DR;
            residueCycles++;
        } else {
            residueCycles = 0;
            drainedSinceLastOutput = 0;
        }

        lEUt += euRate;
        mEfficiency = 10000;
        mEfficiencyIncrease = 10000;
        mMaxProgresstime = CYCLE_TIME;
        recipesDone++;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    public boolean processFluid(FluidStack stack, double nonDrainChance) {
        if (!this.depleteInput(stack, true)) {
            this.depleteInput(stack);
            return false; // fluid couldn't be fully processed
        }
        if (random.nextFloat() >= nonDrainChance) this.depleteInput(stack);
        return true;
    }

    public void resetBoosts() {
        FORCE_CURRENT_BOOST = 0;
        RUNITE_CURRENT_BOOST = 0;
        CELESTIAL_TUNGSTEN_CURRENT_BOOST = 0;
        ORIKALKUM_CURRENT_BOOST = 1;
    }

    public CheckRecipeResult crashMachine(FluidStack stack) {
        resetBoosts();
        lEUt = 0;
        residueCycles = 0;
        drainedSinceLastOutput = 0;
        stopMachine(ShutDownReasonRegistry.outOfFluid(stack));
        return CheckRecipeResultRegistry.CRASH;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        // cannot have maintenance issues, so do nothing
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    @Override
    public boolean showRecipeTextInGUI() {
        return false;
    }

    @Override
    public boolean showMachineStatusInGUI() {
        return false;
    }

    @Override
    public boolean hasRunningText() {
        return false;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            WIDTH_OFFSET,
            HEIGHT_OFFSET,
            DEPTH_OFFSET,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        if (!checkPiece(STRUCTURE_PIECE_MAIN, WIDTH_OFFSET, HEIGHT_OFFSET, DEPTH_OFFSET, errors)) return;
        checkOneDynamoHatchMaybeExotic(errors);
        checkHasInputHatch(errors);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.FORCE_CURRENT_DR = aNBT.getInteger("ForceDR");
        this.RUNITE_CURRENT_DR = aNBT.getInteger("RuniteDR");
        this.CELESTIAL_TUNGSTEN_CURRENT_DR = aNBT.getInteger("CelestialDR");
        this.ORIKALKUM_CURRENT_DR = aNBT.getInteger("OrikalkumDR");
        this.FORCE_CURRENT_BOOST = aNBT.getInteger("ForceBoost");
        this.RUNITE_CURRENT_BOOST = aNBT.getInteger("RuniteBoost");
        this.CELESTIAL_TUNGSTEN_CURRENT_BOOST = aNBT.getInteger("CelestialBoost");
        this.ORIKALKUM_CURRENT_BOOST = aNBT.getInteger("OrikalkumBoost");
        this.shouldRender = aNBT.getBoolean("shouldRender");
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("ForceDR", FORCE_CURRENT_DR);
        aNBT.setInteger("RuniteDR", RUNITE_CURRENT_DR);
        aNBT.setInteger("CelestialDR", CELESTIAL_TUNGSTEN_CURRENT_DR);
        aNBT.setInteger("OrikalkumDR", ORIKALKUM_CURRENT_DR);
        aNBT.setFloat("ForceBoost", FORCE_CURRENT_BOOST);
        aNBT.setFloat("RuniteBoost", RUNITE_CURRENT_BOOST);
        aNBT.setFloat("CelestialBoost", CELESTIAL_TUNGSTEN_CURRENT_BOOST);
        aNBT.setFloat("OrikalkumBoost", ORIKALKUM_CURRENT_BOOST);
        aNBT.setBoolean("shouldRender", shouldRender);
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return false;
    }

    @Override
    protected @NotNull MTEQuadcellTokamakGui getGui() {
        return new MTEQuadcellTokamakGui(this);
    }

    // render code
    private boolean shouldRender = true;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        shouldRender = !shouldRender;
        getBaseMetaTileEntity().issueTileUpdate();
        GTUtility.sendChatTrans(aPlayer, "GT5U.machines.animations." + (shouldRender ? "enabled" : "disabled"));
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        buffer.writeBoolean(shouldRender);
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        shouldRender = buffer.readBoolean();
    }

    @Override
    public void renderTESR(double x, double y, double z, float timeSinceLastTick) {
        if (!shouldRender || !getBaseMetaTileEntity().isActive()) {
            return;
        }

        IGregTechTileEntity gte = getBaseMetaTileEntity();

        World world = gte.getWorld();
        if (world == null) return;
        Minecraft.getMinecraft().renderEngine.bindTexture(TextureMap.locationBlocksTexture);

        GL11.glPushMatrix();
        GL11.glPushAttrib(GL11.GL_ALL_ATTRIB_BITS);
        Vec3 abc = this.getExtendedFacing()
            .getWorldOffset(Vec3.createVectorHelper(0, -3, 3));
        GL11.glTranslated(abc.xCoord + x + 0.5, abc.yCoord + y + 0.5, abc.zCoord + z + 0.5);

        GL11.glEnable(GL11.GL_DEPTH_TEST | GL11.GL_LIGHT0 | GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_LIGHTING | GL11.GL_ALPHA_TEST | GL11.GL_CULL_FACE);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

        drawCube(PlasmaType.FORCE, world, false, timeSinceLastTick, 1, 1, -1, 1, 1.3, 1);
        drawCube(PlasmaType.RUNITE, world, false, timeSinceLastTick, -1, -1, 1, 1, 1.2, 2);
        drawCube(PlasmaType.CELESTIAL, world, false, timeSinceLastTick, 1, -1, -1, 0.8, 1.2, 4);
        drawCube(PlasmaType.ORIKALKUM, world, false, timeSinceLastTick, -1, 1, -1, 0.6, 1.2, 8);

        BloomShader.getInstance()
            .bindFramebuffer();
        drawCube(PlasmaType.FORCE, world, true, timeSinceLastTick, 1, 1, -1, 1, 1.3, 1);
        drawCube(PlasmaType.RUNITE, world, true, timeSinceLastTick, -1, -1, 1, 1, 1.2, 2);
        drawCube(PlasmaType.CELESTIAL, world, true, timeSinceLastTick, 1, -1, -1, 0.8, 1.2, 4);
        drawCube(PlasmaType.ORIKALKUM, world, true, timeSinceLastTick, -1, 1, -1, 0.6, 1.2, 8);
        BloomShader.unbind();

        ShaderProgram.clear();
        GL11.glPopAttrib();
        GL11.glPopMatrix();

    }

    private double lerp(double start, double end, double t) {
        return start + t * (end - start);
    }

    public void drawCube(PlasmaType plasma, World world, boolean bloom, float timeSinceLastTick, double xMod,
        double yMod, double zMod, double scaleBottomEnd, double scaleTopEnd, double lerpRate) {

        GL11.glPushMatrix();
        // rotation factor is inverse of scale rate.
        double rotationTimer = Math.toRadians(
            (10 * (10 - lerpRate)
                * world.getWorldInfo()
                    .getWorldTotalTime()
                + timeSinceLastTick));

        GL11.glRotated(rotationTimer, xMod, yMod, zMod);

        Tessellator tess = Tessellator.instance;

        IIcon icon = plasma.getIcon();

        short[] rgba = plasma.material.get()
            .getRGBA();
        float r = bloom ? rgba[0] / 255f : 0;
        float g = bloom ? rgba[1] / 255f : 0;
        float b = bloom ? rgba[2] / 255f : 0;

        GL11.glColor4f(r, g, b, 1);
        tess.startDrawingQuads();

        // draw cube
        // Front face(z = -0.5)
        tess.addVertexWithUV(-0.5, 0.5, -0.5, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, 0.5, -0.5, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, -0.5, -0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(-0.5, -0.5, -0.5, icon.getMinU(), icon.getMinV());

        // Back face (z = 0.5)
        tess.addVertexWithUV(-0.5, 0.5, 0.5, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(-0.5, -0.5, 0.5, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(0.5, -0.5, 0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0.5, 0.5, 0.5, icon.getMaxU(), icon.getMaxV());

        // Left face (x = -0.5)
        tess.addVertexWithUV(-0.5, -0.5, -0.5, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(-0.5, -0.5, 0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(-0.5, 0.5, 0.5, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(-0.5, 0.5, -0.5, icon.getMinU(), icon.getMaxV());

        // Right face (x = 0.5)
        tess.addVertexWithUV(0.5, 0.5, -0.5, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, 0.5, 0.5, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, -0.5, 0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0.5, -0.5, -0.5, icon.getMinU(), icon.getMinV());

        // Top face (y = 0.5)
        tess.addVertexWithUV(-0.5, 0.5, 0.5, icon.getMinU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, 0.5, 0.5, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(0.5, 0.5, -0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(-0.5, 0.5, -0.5, icon.getMinU(), icon.getMinV());

        // Bottom face (y = -0.5)
        tess.addVertexWithUV(-0.5, -0.5, -0.5, icon.getMinU(), icon.getMinV());
        tess.addVertexWithUV(0.5, -0.5, -0.5, icon.getMaxU(), icon.getMinV());
        tess.addVertexWithUV(0.5, -0.5, 0.5, icon.getMaxU(), icon.getMaxV());
        tess.addVertexWithUV(-0.5, -0.5, 0.5, icon.getMinU(), icon.getMaxV());
        // lerps scale based on the sin of the total time % 360.
        double val = Math.sin(
            Math.toRadians(
                (lerpRate / 2
                    * world.getWorldInfo()
                        .getWorldTotalTime())
                    % 360));
        double lerped = lerp(scaleBottomEnd, scaleTopEnd, val);
        GL11.glScaled(lerped, lerped, lerped);
        tess.draw();
        GL11.glPopMatrix();
    }

    @Override
    public AxisAlignedBB getRenderBoundingBox(int x, int y, int z) {
        return AxisAlignedBB.getBoundingBox(x - 40, y - 40, z - 40, x + 40, y + 40, z + 40);
    }

    public enum PlasmaType {

        FORCE("ForceDR", "ForceBoost", 10_000, 0.15f, 200_000, () -> MaterialsElements.STANDALONE.FORCE),
        RUNITE("RuniteDR", "RuniteBoost", 20_000, 0.1f, 800_000, () -> MaterialsElements.STANDALONE.RUNITE),
        CELESTIAL("CelestialTungstenDR", "CelestialTungstenBoost", 80_000, 0.05f, 3_200_000,
            () -> MaterialsElements.STANDALONE.CELESTIAL_TUNGSTEN),
        ORIKALKUM("OrikalkumDR", "OrikalkumBoost", 320_000, 1.5f, 8_000_000, () -> Materials.Orikalkum);

        private final String syncKey;
        private final String boostSyncKey;
        private final int maxDR;
        private final float maxBoost;
        private final int density;
        private final Supplier<IOreMaterial> material;

        PlasmaType(String syncKey, String boostSyncKey, int maxDrainRate, float maxBoost, int density,
            Supplier<IOreMaterial> material) {
            this.syncKey = syncKey;
            this.boostSyncKey = boostSyncKey;
            this.maxDR = maxDrainRate;
            this.maxBoost = maxBoost;
            this.density = density;
            this.material = material;
        }

        public IntSyncValue getSyncValue(PanelSyncManager syncManager) {
            return syncManager.findSyncHandler(this.syncKey, IntSyncValue.class);
        }

        public FloatSyncValue getBoostSyncValue(PanelSyncManager syncManager) {
            return syncManager.findSyncHandler(this.boostSyncKey, FloatSyncValue.class);
        }

        public int getMaxDR() {
            return maxDR;
        }

        public int getRGB() {
            short[] rgba = material.get()
                .getRGBA();
            return Color.rgb(rgba[0], rgba[1], rgba[2]);
        }

        public String getLocalName() {
            return material.get()
                .getLocalizedName();
        }

        public FluidStack getFluid() {
            return this.getFluid(1);
        }

        public FluidStack getFluid(int amount) {
            IOreMaterial m = material.get();
            if (m instanceof Material gtppMaterial) {
                return new FluidStack(gtppMaterial.getPlasma(), amount);
            } else if (m instanceof Materials gtMaterial) {
                return gtMaterial.getPlasma(amount);
            } else throw new IllegalStateException();
        }

        public IIcon getIcon() {
            IOreMaterial m = material.get();

            if (m instanceof Material gtppMat) {
                return gtppMat.getPlasma()
                    .getIcon();
            } else if (m instanceof Materials gtMat) {
                return gtMat.mPlasma.getIcon();
            } else throw new IllegalStateException();
        }

    }
}
