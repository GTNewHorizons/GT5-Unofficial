package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.Textures.BlockIcons.getCasingTextureForId;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Objects;

import javax.annotation.Nonnull;

import org.joml.Vector3i;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTechAPI;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEEnhancedMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasingsAbstract;
import gtPlusPlus.core.block.ModBlocks;
import it.unimi.dsi.fastutil.chars.Char2IntArrayMap;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

public class MTEMMUplink extends MTEEnhancedMultiBlockBase<MTEMMUplink> implements ISurvivalConstructable {
    
    public MTEMMUplink(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEMMUplink(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMMUplink(mName);
    }

    private static String[][] getStructure() {
        return new String[][]{{
            "         ",
            "         ",
            "         ",
            "         ",
            "  AA~AA  ",
            "         ",
            "         ",
            "         ",
            "         "
        },{
            "         ",
            "         ",
            "  A   A  ",
            " AA   AA ",
            " A     A ",
            " AA   AA ",
            "  A   A  ",
            "         ",
            "         "
        },{
            "         ",
            "  A   A  ",
            " ACCCCCA ",
            " AD   DA ",
            "A D   D A",
            " AD   DA ",
            " ACCCCCA ",
            "  A   A  ",
            "         "
        },{
            "         ",
            " AA   AA ",
            " AD   DA ",
            "A       A",
            "A       A",
            "A       A",
            " AD   DA ",
            " AA   AA ",
            "         "
        },{
            "  A   A  ",
            " A     A ",
            "A D   D A",
            "A       A",
            "ABBE EBBA",
            "A       A",
            "A D   D A",
            " A     A ",
            "  A   A  "
        },{
            "         ",
            " AA   AA ",
            " AD   DA ",
            "A       A",
            "A       A",
            "A       A",
            " AD   DA ",
            " AA   AA ",
            "         "
        },{
            "         ",
            "  A   A  ",
            " ACCCCCA ",
            " AD   DA ",
            "A D   D A",
            " AD   DA ",
            " ACCCCCA ",
            "  A   A  ",
            "         "
        },{
            "         ",
            "         ",
            "  A   A  ",
            " AA   AA ",
            " A     A ",
            " AA   AA ",
            "  A   A  ",
            "         ",
            "         "
        },{
            "         ",
            "         ",
            "         ",
            "         ",
            "  A   A  ",
            "         ",
            "         ",
            "         ",
            "         "
        }};
    }
    
    private static final String STRUCTURE_PIECE_MAIN = "main";
    
    private BasicStructureWrapper structure = new BasicStructureWrapper();

    @Override
    public IStructureDefinition<MTEMMUplink> getStructureDefinition() {
        return structure.getStructureDefinition();
    }

    public class BasicStructureWrapper {
        public String[][] defText;
    
        public int casingCount;
        public boolean badCasingCount;
        public IStructureDefinition<MTEMMUplink> structureDefinition;

        public Vector3i offset;
        public Char2IntArrayMap defCasingCounts;

        public char casingChar = 'A';
        public int maxHatches = 8;

        public IStructureDefinition<MTEMMUplink> getStructureDefinition() {
            String[][] structure = getStructure();
    
            if (!Objects.equals(structure, defText)) {
                defText = structure;
                defCasingCounts = new Char2IntArrayMap();

                int z = 0;
                for (String[] a : defText) {
                    int y = 0;
                    for (String b : a) {
                        for (int x = 0; x < b.length(); x++) {
                            char c = b.charAt(x);
                            defCasingCounts.put(c, defCasingCounts.getOrDefault(c, 0) + 1);

                            if (c == '~') {
                                offset = new Vector3i(x, y, z);
                            }
                        }
                        y++;
                    }
                    z++;
                }

                structureDefinition = StructureDefinition
                    .<MTEMMUplink>builder()
                    .addShape(
                        STRUCTURE_PIECE_MAIN,
                        defText)
                    .addElement('A', buildHatchAdder(MTEMMUplink.class)
                        .atLeast(InputHatch, Energy, Maintenance)
                        .casingIndex(getCasingIndex())
                        .dot(1)
                        .buildAndChain(onElementPass(this::onCasingAdded, ofBlock(GregTechAPI.sBlockCasings8, 7))))
                    .addElement('B', ofFrame(Materials.NaquadahAlloy))
                    .addElement('C', ofFrame(Materials.Trinium))
                    .addElement('D', ofBlock(ModBlocks.blockCasingsMisc, 8))
                    .addElement('E', ofBlock(GregTechAPI.sBlockCasings8, 10))
                    .build();
            }
    
            return structureDefinition;
        }

        private void onCasingAdded(MTEMMUplink ignored) {
            casingCount++;
        }

        public boolean checkStructure() {
            getStructureDefinition();
            
            casingCount = 0;
            badCasingCount = false;

            if (!checkPiece(STRUCTURE_PIECE_MAIN, offset.x, offset.y, offset.z)) {
                return false;
            }
    
            if (casingCount < (defCasingCounts.get('A') - maxHatches)) {
                badCasingCount = true;
                return false;
            }
    
            return true;
        }

        public void construct(ItemStack stackSize, boolean hintsOnly) {
            getStructureDefinition();
            
            buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, offset.x, offset.y, offset.z);
        }

        public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
            getStructureDefinition();
            
            if (mMachine) return -1;
            return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, offset.x, offset.y, offset.z, elementBudget, env, false, true);
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structure.construct(stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        return structure.survivalConstruct(stackSize, elementBudget, env);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structure.checkStructure();
    }

    private int getCasingIndex() {
        return ((BlockCasingsAbstract) GregTechAPI.sBlockCasings8).getTextureIndex(7); // Advanced Iridium Plated Machine Casing
    }

    private static final Textures.BlockIcons.CustomIcon ACTIVE = new Textures.BlockIcons.CustomIcon("multitileentity/mmuplink/OVERLAY_FRONT_ACTIVE");
    private static final Textures.BlockIcons.CustomIcon ACTIVE_GLOW = new Textures.BlockIcons.CustomIcon("multitileentity/mmuplink/OVERLAY_FRONT_ACTIVE_GLOW");
    private static final Textures.BlockIcons.CustomIcon IDLE = new Textures.BlockIcons.CustomIcon("multitileentity/mmuplink/OVERLAY_FRONT_IDLE");
    
    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing, int colorIndex, boolean active, boolean redstoneLevel) {
        int casing = getCasingIndex();
        if (side == facing) {
            if (active) {
                return new ITexture[] {
                    getCasingTextureForId(casing),
                    TextureFactory.builder()
                        .addIcon(ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build()
                };
            } else {
                return new ITexture[] {
                    getCasingTextureForId(casing),
                    TextureFactory.builder()
                        .addIcon(IDLE)
                        .extFacing()
                        .build()
                };
            }
        }
        return new ITexture[] { getCasingTextureForId(casing) };
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();

        // spotless:off
        tt.addMachineType("Matter Manipulator ME Uplink")
            .toolTipFinisher("GregTech");
        // spotless:on

        return tt;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10_000;
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
    @Nonnull
    public CheckRecipeResult checkProcessing() {
        mMaxProgresstime = 20;
        mEUt = -131_072;

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }
}
