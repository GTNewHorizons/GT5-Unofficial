package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorNoc;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_BREWERY_GLOW;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.google.common.collect.ImmutableList;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.ProgressBar;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;

public class MTENeutronCollector extends MTEExtendedPowerMultiBlockBase<MTENeutronCollector>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTENeutronCollector> STRUCTURE_DEFINITION = StructureDefinition
        .<MTENeutronCollector>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                // spotless:off
            new String[][]{
                {"  CCC             "," CCCCC            ","CCCCCCC           ","CCCCCCC           ","CCCCC             "," CCC              ","  CC              ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  "},
                {"                  ","  AAA             "," A   A            "," A   DC           "," A  C             ","  AD              ","   C              ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  "},
                {"                  ","  AAA             "," A   A            "," A   DC           "," A  C             ","  AD              ","   C              ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  "},
                {"                  ","  AAA             "," A   A            "," A   DC           "," A  C             ","  AD              ","   C              ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  ","                  "},
                {"                  ","  AAA  CCCCC      "," A   ACCCCCCCCC   "," A   DC     CCCC  "," A  C          CC ","  AD           CC ","  CC           CC "," CC             CC"," CC             CC"," CC             CC"," CC             CC"," CC             CC","  CC           CC ","  CC           CC ","  CC           CC ","   CCCC     CCCC  ","    CCCCCCCCCCC   ","       CCCCC      "},
                {"                  ","  AAA             "," A   A DDDDD      "," A  DDD  E  DDD   "," A DC E  E  E  D  ","  AD  E  E  E  D  ","   DEEE  E  EEED  ","  D    E E E    D ","  D     BBB     D ","  DEEEEEBFBEEEEED ","  D     BBB     D ","  D    E E E    D ","   DEEE  E  EEED  ","   D  E  E  E  D  ","   D  E  E  E  D  ","    DDD  E  DDD   ","       DDDDD      ","                  "},
                {"                  ","  A~A             "," A   A BBBBB      "," A  BBBGGFGGBBB   "," A BGGFGGFGGFGGB  ","  ABGGFGGFGGFGGB  ","   BFFFGGFGGFFFB  ","  BGGGGFGFGFGGGGB ","  BGGGGGBBBGGGGGB ","  BFFFFFB BFFFFFB ","  BGGGGGBBBGGGGGB ","  BGGGGFGFGFGGGGB ","   BFFFGGFGGFFFB  ","   BGGFGGFGGFGGB  ","   BGGFGGFGGFGGB  ","    BBBGGFGGBBB   ","       BBBBB      ","                  "},
                {"                  ","  AAA             "," A   A DDDDD      "," A  DDD     DDD   "," A D           D  ","  AD           D  ","   D           D  ","  D             D ","  D     BBB     D ","  D     BBB     D ","  D     BBB     D ","  D             D ","   D           D  ","   D           D  ","   D           D  ","    DDD     DDD   ","       DDDDD      ","                  "},
                {"  CCC             "," CCCCC CCCCC      ","CCCCCCCCCCCCCCC   ","CCCCCCCCCCCCCCCC  ","CCCCCCCCCCCCCCCCC "," CCCCCCCCCCCCCCCC ","  CCCCCCCCCCCCCCC "," CCCCCCCCCCCCCCCCC"," CCCCCCCCCCCCCCCCC"," CCCCCCCCCCCCCCCCC"," CCCCCCCCCCCCCCCCC"," CCCCCCCCCCCCCCCCC","  CCCCCCCCCCCCCCC ","  CCCCCCCCCCCCCCC ","  CCCCCCCCCCCCCCC ","   CCCCCCCCCCCCC  ","    CCCCCCCCCCC   ","       CCCCC      "}
            }))
        //spotless:on
        .addElement(
            'A',
            buildHatchAdder(MTENeutronCollector.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(6))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTENeutronCollector::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 6))))
        .addElement('B', ofBlock(GregTechAPI.sBlockCasings10, 7))
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 11))
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings10, 12))
        .addElement('E', ofFrame(Materials.BlackPlutonium))
        .addElement('F', ofBlock(GregTechAPI.sBlockGlass1, 4))
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings8, 2))
        .build();

    public MTENeutronCollector(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENeutronCollector(String aName) {
        super(aName);
    }

    private int capacity = 1000;
    private float speed = 1;
    private int particles = 0;
    private float autoModifier = 0;
    private int[] tierSpeed = new int[] { 1, 10, 100, 1000, 10000 };
    private int[] tierCapacity = new int[] { 1000, 10000, 100000, 1000000, 10000000 };
    private float[] tierAuto = new float[] { 0, 0.25F, 0.5F, 0.75F, 1F };

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mParticles", particles);
        aNBT.setInteger("mSpeedTier", speedTier);
        aNBT.setInteger("mCapacityTier", capacityTier);
        aNBT.setInteger("mAutoTier", autoTier);
        // Might remove cost later
        aNBT.setInteger("mSpeedCost", speedCost);
        aNBT.setInteger("mCapacityCost", capacityCost);
        aNBT.setInteger("mAutoCost", autoCost);
        super.saveNBTData(aNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        particles = aNBT.getInteger("mParticles");
        speedTier = aNBT.getInteger("mSpeedTier");
        speed = tierSpeed[speedTier - 1];
        capacityTier = aNBT.getInteger("mCapacityTier");
        capacity = tierCapacity[capacityTier - 1];
        autoTier = aNBT.getInteger("mAutoTier");
        autoModifier = tierAuto[autoTier - 1];
        speedCost = aNBT.getInteger("mSpeedCost");
        capacityCost = aNBT.getInteger("mCapacityCost");
        autoCost = aNBT.getInteger("mAutoCost");
        super.saveNBTData(aNBT);
    }

    @Override
    public IStructureDefinition<MTENeutronCollector> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTENeutronCollector(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_BREWERY_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GTUtility.getCasingTextureIndex(GregTechAPI.sBlockCasings10, 15)) };
        }
        return rTexture;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Neutron Collector")
            .addInfo("Collects Neutronic Particles from deep space")
            .addInfo("Can form Cosmic Neutronium")
            .beginStructureBlock(3, 5, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Reinforced Wooden Casing", 14, false)
            .addCasingInfoExactly("Any Glass", 6, false)
            .addCasingInfoExactly("Steel Frame Box", 4, false)
            .addInputBus("Any Wooden Casing", 1)
            .addOutputBus("Any Wooden Casing", 1)
            .addInputHatch("Any Wooden Casing", 1)
            .addOutputHatch("Any Wooden Casing", 1)
            .addEnergyHatch("Any Wooden Casing", 1)
            .addMaintenanceHatch("Any Wooden Casing", 1)
            .toolTipFinisher(String.valueOf(AuthorNoc));
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 6, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 6, 1, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 3, 6, 1) && mCasingAmount >= 14;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isClientSide() || aTick % 20 != 0 || !mMachine) return;

        int nextParticles = particles + Math.round(speed * (autoDumpEnabled ? autoModifier : 1));

        particles = Math.min(nextParticles, capacity);

        if (autoDumpEnabled && particles >= capacity) doDump();
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
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }

    private void doDump() {
        int neutronium = particles / 100;
        particles -= neutronium * 100;
        while (neutronium > 0) {
            int canDump = Math.min(64, neutronium);
            addOutput(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CosmicNeutronium, canDump));
            neutronium -= canDump;
        }
    }

    // Upgrade statistics

    private int speedTier = 1, capacityTier = 1, autoTier = 1;
    private int speedCost = 100, capacityCost = 100, autoCost = 100;
    private boolean autoDumpEnabled = false;

    private void doSpeedUpgrade() {
        if (particles < speedCost) return;
        particles -= speedCost;
        speedTier++;
        switch (speedTier) {
            case 2 -> {
                speed = 10;
                speedCost = 1000;
            }
            case 3 -> {
                speed = 100;
                speedCost = 2000;
            }
            case 4 -> {
                speed = 1000;
                speedCost = 3000;
            }
            case 5 -> {
                speed = 10000;
                speedCost = Integer.MAX_VALUE;
            }
        }
    }

    private void doCapacityUpgrade() {
        if (particles < capacityCost) return;
        particles -= capacityCost;
        capacityTier++;
        switch (capacityTier) {
            case 2 -> {
                capacity = 10000;
                capacityCost = 1000;
            }
            case 3 -> {
                capacity = 100000;
                capacityCost = 2000;
            }
            case 4 -> {
                capacity = 1000000;
                capacityCost = 3000;
            }
            case 5 -> {
                capacity = 10000000;
                capacityCost = Integer.MAX_VALUE;
            }
        }
    }

    private void doAutoUpgrade() {
        if (particles < autoCost) return;
        particles -= autoCost;
        autoTier++;
        switch (autoTier) {
            case 2 -> {
                autoModifier = 0.25F;
                autoCost = 1000;
            }
            case 3 -> {
                autoModifier = 0.5F;
                autoCost = 2000;
            }
            case 4 -> {
                autoModifier = 0.75F;
                autoCost = 3000;
            }
            case 5 -> {
                autoModifier = 1;
                autoCost = Integer.MAX_VALUE;
            }
        }
    }

    // UI

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(createParticleProgressBar(builder));

        createDumpButton(builder);
        createAutoDumpToggle(builder);
        createUpgradeButtons(builder);
    }

    protected Widget createParticleProgressBar(ModularWindow.Builder builder) {
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> particles, val -> particles = val));
        builder.widget(new FakeSyncWidget.IntegerSyncer(() -> capacity, val -> capacity = val));

        return new ProgressBar().setProgress(() -> (float) particles / capacity)
            .setDirection(ProgressBar.Direction.UP)
            .setTexture(GTUITextures.PROGRESSBAR_STEAM_FILL_STEEL, 54)
            .setSynced(true, false)
            .dynamicTooltip(() -> Collections.singletonList("NP: " + particles + "/" + capacity))
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setUpdateTooltipEveryTick(true)
            .setSize(10, 54)
            .setPos(7, 24);
    }

    protected void createAutoDumpToggle(IWidgetBuilder<?> builder) {
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { autoDumpEnabled = !autoDumpEnabled; })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (autoDumpEnabled) {
                        ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                        if (autoTier != 1) {
                            ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_ON);
                        } else {
                            ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_ON_DISABLED);
                        }
                    } else {
                        ret.add(GTUITextures.BUTTON_STANDARD);
                        if (autoTier != 1) {
                            ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF);
                        } else {
                            ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                        }
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip("Enable Auto-Dump")
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(174, 130))
                .setSize(16, 16));
    }

    protected void createDumpButton(IWidgetBuilder<?> builder) {
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { doDump(); })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    ret.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                    return ret.toArray(new IDrawable[0]);
                })
                .addTooltip("Dump Particles")
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(174, 148))
                .setSize(16, 16));
    }

    protected void createUpgradeButtons(IWidgetBuilder<?> builder) {
        // Speed upgrade
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { doSpeedUpgrade(); })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (particles < speedCost) {
                        ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    } else ret.add(GTUITextures.BUTTON_STANDARD);
                    if (speedTier < 5) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .dynamicTooltip(this::refreshSpeed)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(174, 25))
                .setSize(16, 16)
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> speedCost, val -> speedCost = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> speedTier, val -> speedTier = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange()));

        // Capacity upgrade
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { doCapacityUpgrade(); })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (particles < capacityCost) {
                        ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    } else ret.add(GTUITextures.BUTTON_STANDARD);
                    if (capacityTier < 5) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_BLUE_UP);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .dynamicTooltip(this::refreshCapacity)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(174, 43))
                .setSize(16, 16)
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> capacityCost, val -> capacityCost = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> capacityTier, val -> capacityTier = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange()));

        // Auto upgrade
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> { doAutoUpgrade(); })
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (particles < autoCost) {
                        ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                    } else ret.add(GTUITextures.BUTTON_STANDARD);
                    if (autoTier < 5) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_RED_UP);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_CHECKMARK);
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .dynamicTooltip(this::refreshAuto)
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(new Pos2d(174, 61))
                .setSize(16, 16)
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> autoCost, val -> autoCost = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange())
                .attachSyncer(
                    new FakeSyncWidget.IntegerSyncer(() -> autoTier, val -> autoTier = val),
                    builder,
                    (widget, val) -> widget.notifyTooltipChange()));

    }

    private List<String> refreshSpeed() {
        return ImmutableList.of(
            translateToLocal("nc.button.upgrade.speed") + EnumChatFormatting.DARK_GRAY
                + "\n"
                + (speedCost == Integer.MAX_VALUE ? "Fully Upgraded"
                    : translateToLocal("nc.button.upgrade.tooltip.cost") + " "
                        + speedCost
                        + " "
                        + translateToLocal("nc.button.upgrade.tooltip.points")));
    }

    private List<String> refreshCapacity() {
        return ImmutableList.of(
            translateToLocal("nc.button.upgrade.capacity") + EnumChatFormatting.DARK_GRAY
                + "\n"
                + (capacityCost == Integer.MAX_VALUE ? "Fully Upgraded"
                    : translateToLocal("nc.button.upgrade.tooltip.cost") + " "
                        + capacityCost
                        + " "
                        + translateToLocal("nc.button.upgrade.tooltip.points")));
    }

    private List<String> refreshAuto() {
        return ImmutableList.of(
            translateToLocal("nc.button.upgrade.flush") + EnumChatFormatting.DARK_GRAY
                + "\n"
                + (autoCost == Integer.MAX_VALUE ? "Fully Upgraded"
                    : translateToLocal("nc.button.upgrade.tooltip.cost") + " "
                        + autoCost
                        + " "
                        + translateToLocal("nc.button.upgrade.tooltip.points")));
    }
}
