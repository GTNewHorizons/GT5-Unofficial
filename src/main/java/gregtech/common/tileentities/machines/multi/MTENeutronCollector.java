package gregtech.common.tileentities.machines.multi;

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
import gregtech.api.enums.SteamVariant;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
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
import static gregtech.api.metatileentity.BaseTileEntity.BUTTON_FORBIDDEN_TOOLTIP;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.chainAllGlasses;
import static gregtech.api.util.GTStructureUtility.ofFrame;

public class MTENeutronCollector extends MTEExtendedPowerMultiBlockBase<MTENeutronCollector>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTENeutronCollector> STRUCTURE_DEFINITION = StructureDefinition
        .<MTENeutronCollector>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            new String[][]{{
                "BBB",
                "BBB",
                "B~B",
                "BBB",
                "C C"
            },{
                "BBB",
                "A A",
                "A A",
                "BBB",
                "   "
            },{
                "BBB",
                "BAB",
                "BAB",
                "BBB",
                "C C"
            }})
        //spotless:on
        .addElement(
            'B',
            buildHatchAdder(MTENeutronCollector.class)
                .atLeast(InputBus, OutputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(15))
                .dot(1)
                .buildAndChain(
                    onElementPass(MTENeutronCollector::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings10, 15))))
        .addElement('A', chainAllGlasses())
        .addElement('C', ofFrame(Materials.Steel))
        .build();

    public MTENeutronCollector(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTENeutronCollector(String aName) {
        super(aName);
    }

    private int capacity = 1000;
    private int speed = 1;
    private int particles = 0;

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
            .toolTipFinisher(AuthorFourIsTheNumber);
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 2, 0) && mCasingAmount >= 14;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isClientSide() || aTick % 20 != 0 || !mMachine) return;

        particles = Math.min(particles + speed, capacity);


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

    int speedTier = 1, capacityTier = 1, autoTier = 1;

    private void doSpeedUpgrade() {
        speedTier++;
        switch (speedTier) {
            case 2 -> speed = 10;
            case 3 -> speed = 100;
            case 4 -> speed = 1000;
            case 5 -> speed = 10000;
        }
    }

    private void doCapacityUpgrade() {
        capacityTier++;
        switch (capacityTier) {
            case 2 -> capacity = 10000;
            case 3 -> capacity = 100000;
            case 4 -> capacity = 1000000;
            case 5 -> capacity = 10000000;
        }
    }

    private void doAutoUpgrade() {

    }

    private void doDump() {
        int neutronium = particles / 100;
        while (neutronium > 0) {
            int canDump = Math.min(64, neutronium);
            addOutput(GTOreDictUnificator.get(OrePrefixes.ingot, Materials.CosmicNeutronium, canDump));
            neutronium -= canDump;
        }
        particles -= neutronium * 100;
    }

    // UI

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(createParticleProgressBar(builder));

        createDumpButton(builder);
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

    protected void createDumpButton(IWidgetBuilder<?> builder) {
        // Speed upgrade
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                doDump();
            })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                ret.add(GTUITextures.OVERLAY_BUTTON_AUTOOUTPUT_ITEM);
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Dump Particles")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(getPowerSwitchButtonPos())
            .setSize(16, 16));
    }

    protected void createUpgradeButtons(IWidgetBuilder<?> builder) {
        // Speed upgrade
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                doSpeedUpgrade();
            })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                    ret.add(GTUITextures.BUTTON_STANDARD);
                    if (speedTier < 5) {
                        ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_GREEN_UP);
                    } else {
                        ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                    }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Speed Upgrade")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(174, 25))
            .setSize(16, 16));

        // Capacity upgrade
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                doCapacityUpgrade();
            })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                if (capacityTier < 5) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_BLUE_UP);
                } else {
                    ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Capacity Upgrade")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(174, 43))
            .setSize(16, 16));

        // Auto upgrade
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                doAutoUpgrade();
            })
            .setPlayClickSound(true)
            .setBackground(() -> {
                List<UITexture> ret = new ArrayList<>();
                ret.add(GTUITextures.BUTTON_STANDARD);
                if (autoTier < 5) {
                    ret.add(GTUITextures.OVERLAY_BUTTON_ARROW_RED_UP);
                } else {
                    ret.add(GTUITextures.OVERLAY_BUTTON_BATCH_MODE_OFF_DISABLED);
                }
                return ret.toArray(new IDrawable[0]);
            })
            .addTooltip("Auto Upgrade")
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(new Pos2d(174, 61))
            .setSize(16, 16));
    }
}
