package com.github.technus.tectech.thing.metaTileEntity.multi.em_collider;

import static com.github.technus.tectech.loader.TecTechConfig.DEBUG_MODE;
import static com.github.technus.tectech.mechanics.elementalMatter.core.transformations.EMTransformationRegistry.EM_COUNT_PER_MATERIAL_AMOUNT;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_OK;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_HIGH;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_TOO_LOW;
import static com.github.technus.tectech.thing.metaTileEntity.multi.base.LedStatus.STATUS_WRONG;
import static com.github.technus.tectech.util.DoubleCount.add;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.util.GT_StructureUtility.ofHatchAdderOptional;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.HashMap;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMComplexAspectDefinition;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.EMPrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.decay.EMDecayResult;
import com.github.technus.tectech.mechanics.elementalMatter.core.definitions.EMPrimitiveTemplate;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.maps.EMInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.EMInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.EMHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.EMQuarkDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.INameFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.IStatusFunction;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.Parameters;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_collider extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {

    // region variables
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenON_Slave;
    private static Textures.BlockIcons.CustomIcon ScreenOFF_Slave;

    protected static final byte FUSE_MODE = 0, COLLIDE_MODE = 1;
    private static double MASS_TO_EU_INSTANT;
    private static int STARTUP_COST, KEEPUP_COST;

    protected byte eTier = 0;
    protected EMInstanceStack stack;
    private long plasmaEnergy;

    protected boolean started = false;
    // endregion

    // region collision handlers
    public static final HashMap<Long, IColliderHandler> FUSE_HANDLERS = new HashMap<>();
    public static final HashMap<String, IPrimitiveColliderHandler> PRIMITIVE_FUSE_HANDLERS = new HashMap<>();

    static {
        FUSE_HANDLERS.put(
                ((long) EMAtomDefinition.getClassTypeStatic() << 16) | EMAtomDefinition.getClassTypeStatic(),
                new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        try {
                            EMDefinitionStackMap defs = new EMDefinitionStackMap();
                            defs.putUnifyAllExact(in1.getDefinition().getSubParticles());
                            defs.putUnifyAllExact(in2.getDefinition().getSubParticles());
                            EMAtomDefinition atom = new EMAtomDefinition(
                                    defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
                            out.putUnify(new EMInstanceStack(atom, Math.min(in1.getAmount(), in2.getAmount())));
                        } catch (Exception e) {
                            out.putUnifyAll(in1, in2);
                            return;
                        }
                        if (in1.getAmount() > in2.getAmount()) {
                            out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                        } else if (in2.getAmount() > in1.getAmount()) {
                            out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 1;
                    }
                });
        registerSimpleAtomFuse(EMHadronDefinition.getClassTypeStatic());
        registerSimpleAtomFuse(EMComplexAspectDefinition.getClassTypeStatic());
        registerSimpleAtomFuse(EMPrimitiveTemplate.getClassTypeStatic());

        FUSE_HANDLERS.put(
                ((long) EMHadronDefinition.getClassTypeStatic() << 16) | EMHadronDefinition.getClassTypeStatic(),
                new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        try {
                            EMDefinitionStackMap defs = new EMDefinitionStackMap();
                            defs.putUnifyAllExact(in1.getDefinition().getSubParticles());
                            defs.putUnifyAllExact(in2.getDefinition().getSubParticles());
                            EMHadronDefinition hadron = new EMHadronDefinition(
                                    defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
                            out.putUnify(new EMInstanceStack(hadron, Math.min(in1.getAmount(), in2.getAmount())));
                        } catch (Exception e) {
                            out.putUnifyAll(in1, in2);
                            return;
                        }
                        if (in1.getAmount() > in2.getAmount()) {
                            out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                        } else if (in2.getAmount() > in1.getAmount()) {
                            out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 2;
                    }
                });
        FUSE_HANDLERS.put(
                ((long) EMHadronDefinition.getClassTypeStatic() << 16) | EMPrimitiveTemplate.getClassTypeStatic(),
                new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        try {
                            EMDefinitionStackMap defs = new EMDefinitionStackMap();
                            defs.putUnifyAllExact(in1.getDefinition().getSubParticles());
                            defs.putUnifyExact(in2.getDefinition().getStackForm(1));
                            EMHadronDefinition hadron = new EMHadronDefinition(
                                    defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
                            out.putUnify(new EMInstanceStack(hadron, Math.min(in1.getAmount(), in2.getAmount())));
                        } catch (Exception e) {
                            out.putUnifyAll(in1, in2);
                            return;
                        }
                        if (in1.getAmount() > in2.getAmount()) {
                            out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                        } else if (in2.getAmount() > in1.getAmount()) {
                            out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 2;
                    }
                });

        registerSimpleAspectFuse(EMComplexAspectDefinition.getClassTypeStatic());
        registerSimpleAspectFuse(EMPrimitiveTemplate.getClassTypeStatic());

        FUSE_HANDLERS.put(
                ((long) EMPrimitiveTemplate.getClassTypeStatic() << 16) | EMPrimitiveTemplate.getClassTypeStatic(),
                new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        IPrimitiveColliderHandler collisionHandler = PRIMITIVE_FUSE_HANDLERS.get(
                                in1.getDefinition().getClass().getName() + '\0'
                                        + in2.getDefinition().getClass().getName());
                        if (collisionHandler != null) {
                            collisionHandler.collide(in2, in1, out);
                        } else {
                            out.putUnifyAll(in1, in2);
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 2;
                    }
                });

        PRIMITIVE_FUSE_HANDLERS
                .put(EMQuarkDefinition.class.getName() + '\0' + EMQuarkDefinition.class.getName(), (in1, in2, out) -> {
                    try {
                        EMDefinitionStackMap defs = new EMDefinitionStackMap();
                        defs.putUnifyExact(in1.getDefinition().getStackForm(1));
                        defs.putUnifyExact(in2.getDefinition().getStackForm(1));
                        EMHadronDefinition hadron = new EMHadronDefinition(
                                defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
                        out.putUnify(new EMInstanceStack(hadron, Math.min(in1.getAmount(), in2.getAmount())));
                    } catch (Exception e) {
                        out.putUnifyAll(in1, in2);
                        return;
                    }
                    if (in1.getAmount() > in2.getAmount()) {
                        out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                    } else if (in2.getAmount() > in1.getAmount()) {
                        out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                    }
                });
        PRIMITIVE_FUSE_HANDLERS.put(
                EMPrimalAspectDefinition.class.getName() + '\0' + EMPrimalAspectDefinition.class.getName(),
                (in1, in2, out) -> {
                    if (fuseAspects(in1, in2, out)) return;
                    if (in1.getAmount() > in2.getAmount()) {
                        out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                    } else if (in2.getAmount() > in1.getAmount()) {
                        out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                    }
                });
    }

    private static boolean fuseAspects(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
        try {
            EMDefinitionStackMap defs = new EMDefinitionStackMap();
            defs.putUnifyExact(in1.getDefinition().getStackForm(1));
            defs.putUnifyExact(in2.getDefinition().getStackForm(1));
            EMComplexAspectDefinition aspect = new EMComplexAspectDefinition(
                    defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
            out.putUnify(new EMInstanceStack(aspect, Math.min(in1.getAmount(), in2.getAmount())));
        } catch (Exception e) {
            out.putUnifyAll(in1, in2);
            return true;
        }
        return false;
    }

    private static void registerSimpleAspectFuse(int classTypeStatic) {
        FUSE_HANDLERS.put(
                ((long) EMComplexAspectDefinition.getClassTypeStatic() << 16) | classTypeStatic,
                new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        if (fuseAspects(in1, in2, out)) return;
                        if (in1.getAmount() > in2.getAmount()) {
                            out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                        } else if (in2.getAmount() > in1.getAmount()) {
                            out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 1;
                    }
                });
    }

    private static void registerSimpleAtomFuse(int classTypeStatic) {
        FUSE_HANDLERS
                .put(((long) EMAtomDefinition.getClassTypeStatic() << 16) | classTypeStatic, new IColliderHandler() {

                    @Override
                    public void collide(EMInstanceStack in1, EMInstanceStack in2, EMInstanceStackMap out) {
                        try {
                            EMDefinitionStackMap defs = new EMDefinitionStackMap();
                            defs.putUnifyAllExact(in1.getDefinition().getSubParticles());
                            defs.putUnifyExact(in2.getDefinition().getStackForm(1));
                            EMAtomDefinition atom = new EMAtomDefinition(
                                    defs.toImmutable_optimized_unsafe_LeavesExposedElementalTree());
                            out.putUnify(new EMInstanceStack(atom, Math.min(in1.getAmount(), in2.getAmount())));
                        } catch (Exception e) {
                            out.putUnifyAll(in1, in2);
                            return;
                        }
                        if (in1.getAmount() > in2.getAmount()) {
                            out.putUnify(new EMInstanceStack(in1.getDefinition(), in1.getAmount() - in2.getAmount()));
                        } else if (in2.getAmount() > in1.getAmount()) {
                            out.putUnify(new EMInstanceStack(in2.getDefinition(), in2.getAmount() - in1.getAmount()));
                        }
                    }

                    @Override
                    public byte getRequiredTier() {
                        return 1;
                    }
                });
    }
    // endregion

    // region parameters
    protected Parameters.Group.ParameterIn mode;
    private static final IStatusFunction<GT_MetaTileEntity_EM_collider> MODE_STATUS = (base_EM, p) -> {
        if (base_EM.isMaster()) {
            double mode = p.get();
            if (mode == FUSE_MODE || mode == COLLIDE_MODE) {
                return STATUS_OK;
            } else if (mode > 1) {
                return STATUS_TOO_HIGH;
            } else if (mode < 0) {
                return STATUS_TOO_LOW;
            }
            return STATUS_WRONG;
        }
        return STATUS_OK;
    };
    private static final INameFunction<GT_MetaTileEntity_EM_collider> MODE_NAME = (base_EM, p) -> {
        if (base_EM.isMaster()) {
            double mode = p.get();
            if (mode == FUSE_MODE) {
                return translateToLocal("gt.blockmachines.multimachine.em.collider.mode.0"); // Mode: Fuse
            } else if (mode == COLLIDE_MODE) {
                return translateToLocal("gt.blockmachines.multimachine.em.collider.mode.1"); // Mode: Collide
            }
            return translateToLocal("gt.blockmachines.multimachine.em.collider.mode.2"); // Mode: Undefined
        }
        return translateToLocal("gt.blockmachines.multimachine.em.collider.mode.3"); // Currently Slaves...
    };
    // endregion

    // region structure
    // use multi A energy inputs, use less power the longer it runs
    private static final String[] description = new String[] {
            EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
            translateToLocal("gt.blockmachines.multimachine.em.collider.hint.0"), // 1 - Classic Hatches or High Power
                                                                                  // Casing
            translateToLocal("gt.blockmachines.multimachine.em.collider.hint.1"), // 2 - Elemental Input Hatches or
                                                                                  // Molecular Casing
            translateToLocal("gt.blockmachines.multimachine.em.collider.hint.2"), // 3 - Elemental Output Hatches or
                                                                                  // Molecular Casing
            translateToLocal("gt.blockmachines.multimachine.em.collider.hint.3"), // 4 - Elemental Overflow Hatches or
                                                                                  // Molecular
            // Casing
            translateToLocal("gt.blockmachines.multimachine.em.collider.hint.4"), // General - Another Controller facing
                                                                                  // opposite
            // direction
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_collider> STRUCTURE_DEFINITION = IStructureDefinition
            .<GT_MetaTileEntity_EM_collider>builder()
            .addShape(
                    "tier1",
                    transpose(
                            new String[][] { { "         A A A         ", "        AAAAAAA        ",
                                    "      bbbbIIIbbbb      ", "     bAAAAAAAAAAAb     ", "    bAAAA     AAAAb    ",
                                    "   bAAA         AAAb   ", "  bAAA           AAAb  ", "  bAA             AAb  ",
                                    " AbAA             AAbA ", "AAbA               AbAA", " AIA               AIA ",
                                    "AAIA               AIAA", " AIA               AIA ", "AAbA               AbAA",
                                    " AbAA             AAbA ", "  bAA             AAb  ", "  bAAA           AAAb  ",
                                    "   bAAA         AAAb   ", "    bAAAAbJJJbAAAAb    ", "     bAHHbbbbbHHAb     ",
                                    "      bbbbGFGbbbb      " },
                                    { "         AAAAA         ", "       AADDDDDAA       ", "      cDDeeeeeDDc      ",
                                            "     cDeeDDDDDeeDc     ", "    cDeDD     DDeDc    ",
                                            "   cDeD         DeDc   ", "  cDeD           DeDc  ",
                                            " ADeD             DeDA ", " ADeD             DeDA ",
                                            "ADeD               DeDA", "ADeD               DeDA",
                                            "ADeD               DeDA", "ADeD               DeDA",
                                            "ADeD               DeDA", " ADeD             DeDA ",
                                            " ADeD             DeDA ", "  cDeD           DeDc  ",
                                            "   cDeD         DeDc   ", "    cDeDDbJ~JbDDeDc    ",
                                            "     cDeeDDDDDeeDc     ", "      cDDeeeeeDDc      " },
                                    { "         A A A         ", "        AAAAAAA        ", "      bbbbIIIbbbb      ",
                                            "     bAAAAAAAAAAAb     ", "    bAAAA     AAAAb    ",
                                            "   bAAA         AAAb   ", "  bAAA           AAAb  ",
                                            "  bAA             AAb  ", " AbAA             AAbA ",
                                            "AAbA               AbAA", " AIA               AIA ",
                                            "AAIA               AIAA", " AIA               AIA ",
                                            "AAbA               AbAA", " AbAA             AAbA ",
                                            "  bAA             AAb  ", "  bAAA           AAAb  ",
                                            "   bAAA         AAAb   ", "    bAAAAbJJJbAAAAb    ",
                                            "     bAHHbbbbbHHAb     ", "      bbbbGFGbbbb      " } }))
            .addShape(
                    "tier2",
                    transpose(
                            new String[][] { { "         A A A         ", "        AAAAAAA        ",
                                    "      BBBBIIIBBBB      ", "     BAAAAAAAAAAAB     ", "    BAAAA     AAAAB    ",
                                    "   BAAA         AAAB   ", "  BAAA           AAAB  ", "  BAA             AAB  ",
                                    " ABAA             AABA ", "AABA               ABAA", " AIA               AIA ",
                                    "AAIA               AIAA", " AIA               AIA ", "AABA               ABAA",
                                    " ABAA             AABA ", "  BAA             AAB  ", "  BAAA           AAAB  ",
                                    "   BAAA         AAAB   ", "    BAAAABJJJBAAAAB    ", "     BAHHBBBBBHHAB     ",
                                    "      BBBBGFGBBBB      " },
                                    { "         AAAAA         ", "       AADDDDDAA       ", "      CDDEEEEEDDC      ",
                                            "     CDEEDDDDDEEDC     ", "    CDEDD     DDEDC    ",
                                            "   CDED         DEDC   ", "  CDED           DEDC  ",
                                            " ADED             DEDA ", " ADED             DEDA ",
                                            "ADED               DEDA", "ADED               DEDA",
                                            "ADED               DEDA", "ADED               DEDA",
                                            "ADED               DEDA", " ADED             DEDA ",
                                            " ADED             DEDA ", "  CDED           DEDC  ",
                                            "   CDED         DEDC   ", "    CDEDDBJ~JBDDEDC    ",
                                            "     CDEEDDDDDEEDC     ", "      CDDEEEEEDDC      " },
                                    { "         A A A         ", "        AAAAAAA        ", "      BBBBIIIBBBB      ",
                                            "     BAAAAAAAAAAAB     ", "    BAAAA     AAAAB    ",
                                            "   BAAA         AAAB   ", "  BAAA           AAAB  ",
                                            "  BAA             AAB  ", " ABAA             AABA ",
                                            "AABA               ABAA", " AIA               AIA ",
                                            "AAIA               AIAA", " AIA               AIA ",
                                            "AABA               ABAA", " ABAA             AABA ",
                                            "  BAA             AAB  ", "  BAAA           AAAB  ",
                                            "   BAAA         AAAB   ", "    BAAAABJJJBAAAAB    ",
                                            "     BAHHBBBBBHHAB     ", "      BBBBGFGBBBB      " } }))
            .addElement('A', ofBlock(sBlockCasingsTT, 4)).addElement('b', ofBlock(sBlockCasingsTT, 4))
            .addElement('B', ofBlock(sBlockCasingsTT, 5)).addElement('c', ofBlock(sBlockCasingsTT, 4))
            .addElement('C', ofBlock(sBlockCasingsTT, 6)).addElement('D', ofBlock(sBlockCasingsTT, 7))
            .addElement('e', ofBlock(sBlockCasingsTT, 8)).addElement('E', ofBlock(sBlockCasingsTT, 9))
            .addElement('I', ofBlock(QuantumGlassBlock.INSTANCE, 0))
            .addElement(
                    'J',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_collider::addClassicToMachineList,
                            textureOffset,
                            1,
                            sBlockCasingsTT,
                            0))
            .addElement(
                    'H',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_collider::addElementalInputToMachineList,
                            textureOffset + 4,
                            2,
                            sBlockCasingsTT,
                            4))
            .addElement(
                    'F',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_collider::addElementalOutputToMachineList,
                            textureOffset + 4,
                            3,
                            sBlockCasingsTT,
                            4))
            .addElement(
                    'G',
                    ofHatchAdderOptional(
                            GT_MetaTileEntity_EM_collider::addElementalMufflerToMachineList,
                            textureOffset + 4,
                            4,
                            sBlockCasingsTT,
                            4))
            .build();

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_collider> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    // endregion

    public GT_MetaTileEntity_EM_collider(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_collider(String aName) {
        super(aName);
    }

    public static void setValues(int heliumPlasmaValue) {
        double MASS_TO_EU_PARTIAL = heliumPlasmaValue / (1.75893000478707E07 * EM_COUNT_PER_MATERIAL_AMOUNT); // mass
                                                                                                              // diff
        MASS_TO_EU_INSTANT = MASS_TO_EU_PARTIAL * 20;
        STARTUP_COST = -heliumPlasmaValue * 10000;
        KEEPUP_COST = -heliumPlasmaValue;
    }

    protected double fuse(GT_MetaTileEntity_EM_collider partner) { // /CAN MAKE EU
        if (partner.stack != null && stack != null) { // todo add single event mode as an option
            boolean check = stack.getDefinition().fusionMakesEnergy(stack.getEnergy())
                    && partner.stack.getDefinition().fusionMakesEnergy(partner.stack.getEnergy());

            EMInstanceStack stack2 = partner.stack;
            double preMass = add(stack2.getMass(), stack.getMass());
            // System.out.println("preMass = " + preMass);

            EMInstanceStackMap map = new EMInstanceStackMap();
            IColliderHandler colliderHandler;
            if (stack2.getDefinition().getMatterMassType() > stack.getDefinition().getMatterMassType()) { // always
                                                                                                          // bigger
                                                                                                          // first
                colliderHandler = FUSE_HANDLERS.get(
                        (stack2.getDefinition().getMatterMassType() << 16) | stack.getDefinition().getMatterMassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            } else {
                colliderHandler = FUSE_HANDLERS.get(
                        (stack.getDefinition().getMatterMassType() << 16) | stack2.getDefinition().getMatterMassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            }
            for (EMInstanceStack newStack : map.valuesToArray()) {
                check &= newStack.getDefinition().fusionMakesEnergy(newStack.getEnergy());
            }
            // System.out.println("outputEM[0].getMass() = " + outputEM[0].getMass());
            outputEM = new EMInstanceStackMap[] { map };

            partner.stack = stack = null;

            return check ? preMass - map.getMass() : Math.min(preMass - map.getMass(), 0);
        }
        return 0;
    }

    protected double collide(GT_MetaTileEntity_EM_collider partner) { // DOES NOT MAKE EU!
        if (partner.stack != null && stack != null) { // todo add single event mode as an option
            EMInstanceStack stack2 = partner.stack;
            double preMass = stack2.getMass() + stack.getMass();
            // System.out.println("preMass = " + preMass);

            EMInstanceStackMap map = new EMInstanceStackMap();
            IColliderHandler colliderHandler;
            if (stack2.getDefinition().getMatterMassType() > stack.getDefinition().getMatterMassType()) { // always
                                                                                                          // bigger
                                                                                                          // first
                colliderHandler = FUSE_HANDLERS.get(
                        (stack2.getDefinition().getMatterMassType() << 16) | stack.getDefinition().getMatterMassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            } else {
                colliderHandler = FUSE_HANDLERS.get(
                        (stack.getDefinition().getMatterMassType() << 16) | stack2.getDefinition().getMatterMassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            }
            outputEM = new EMInstanceStackMap[] { map };

            partner.stack = stack = null;

            return Math.min(preMass - map.getMass(), 0);
        }
        return 0;
    }

    private boolean handleRecipe(EMInstanceStack stack2, EMInstanceStackMap map, IColliderHandler colliderHandler) {
        if (colliderHandler != null && eTier >= colliderHandler.getRequiredTier()) {
            colliderHandler.collide(stack2, stack, map);
        } else {
            map.putUnifyAll(stack, stack2);
            outputEM = new EMInstanceStackMap[] { map };
            return true;
        }
        return false;
    }

    protected GT_MetaTileEntity_EM_collider getPartner() {
        IGregTechTileEntity iGregTechTileEntity = getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY * 4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 4;
        IGregTechTileEntity gregTechBaseTileEntity = iGregTechTileEntity.getIGregTechTileEntityOffset(xDir, yDir, zDir);
        if (gregTechBaseTileEntity != null) {
            IMetaTileEntity gregTechMetaTileEntity = gregTechBaseTileEntity.getMetaTileEntity();
            return gregTechMetaTileEntity instanceof GT_MetaTileEntity_EM_collider
                    && ((GT_MetaTileEntity_EM_collider) gregTechMetaTileEntity).mMachine
                    && gregTechBaseTileEntity.getBackFacing() == iGregTechTileEntity.getFrontFacing()
                            ? (GT_MetaTileEntity_EM_collider) gregTechMetaTileEntity
                            : null;
        }
        return null;
    }

    protected final boolean isMaster() {
        return getBaseMetaTileEntity().getFrontFacing() % 2 == 0;
    }

    private void makeEU(double massDiff) {
        plasmaEnergy += massDiff * MASS_TO_EU_INSTANT;
        if (DEBUG_MODE) {
            System.out.println("plasmaEnergy = " + plasmaEnergy);
        }
    }

    private EMInstanceStackMap tickStack() {
        if (stack == null) {
            return null;
        }
        stack.setAge(stack.getAge() + 1);
        EMDecayResult newInstances = stack.decay(1, stack.getAge(), 0);
        if (newInstances == null) {
            stack.nextColor();
            return null;
        } else {
            stack = newInstances.getOutput().removeKey(newInstances.getOutput().getLast().getDefinition());
            return newInstances.getOutput();
        }
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_collider(mName);
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 2;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 2;
        if (iGregTechTileEntity.getBlockOffset(xDir, 0, zDir) != sBlockCasingsTT) {
            eTier = 0;
            return false;
        }

        if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 8) {
            eTier = 1;
        } else if (iGregTechTileEntity.getMetaIDOffset(xDir, 0, zDir) == 9) {
            eTier = 2;
        } else {
            eTier = 0;
            return false;
        }
        if (structureCheck_EM("tier" + eTier, 11, 1, 18)) {
            return true;
        }
        eTier = 0;
        return false;
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        GT_MetaTileEntity_EM_collider partner = getPartner();
        if (partner == null) {
            return false;
        }
        mEfficiencyIncrease = 10000;
        if (started) {
            if (stack == null) {
                for (GT_MetaTileEntity_Hatch_InputElemental inputElemental : eInputHatches) {
                    EMInstanceStackMap container = inputElemental.getContentHandler();
                    if (container.isEmpty()) {
                        continue;
                    }
                    stack = container.removeKey(container.getFirst().getDefinition());
                    long eut = KEEPUP_COST + (long) (KEEPUP_COST
                            * Math.abs(stack.getMass() / EMAtomDefinition.getSomethingHeavy().getMass())) / 2;
                    if (eut < Integer.MIN_VALUE + 7) {
                        return false;
                    }
                    mMaxProgresstime = 20;
                    mEUt = (int) eut;
                    eAmpereFlow = 5;
                    return true;
                }
                mMaxProgresstime = 20;
                mEUt = KEEPUP_COST;
                eAmpereFlow = 1;
                return true;
            }
            mMaxProgresstime = 20;
            mEUt = KEEPUP_COST;
            eAmpereFlow = 2;
        } else {
            started = true;
            mMaxProgresstime = 20;
            mEUt = STARTUP_COST;
            eAmpereFlow = 10;
        }
        return true;
    }

    @Override
    public void outputAfterRecipe_EM() {
        GT_MetaTileEntity_EM_collider partner = getPartner();
        if (partner == null) {
            if (stack != null) {
                cleanMassEM_EM(stack.getMass());
                stack = null;
            }
            return;
        }
        if (isMaster()) {
            switch ((int) mode.get()) {
                case FUSE_MODE:
                    makeEU(fuse(partner));
                    break;
                case COLLIDE_MODE:
                    collide(partner); // todo
                    break;
                default: {
                    outputEM = new EMInstanceStackMap[2];
                    outputEM[1] = tickStack();
                    if (outputEM[1] == null) {
                        outputEM[1] = partner.tickStack();
                    } else {
                        EMInstanceStackMap map = partner.tickStack();
                        if (map != null) {
                            outputEM[1].putUnifyAll(map);
                        }
                    }
                }
            }
            if (outputEM != null) {
                for (int i = 0, lim = Math.min(outputEM.length, eOutputHatches.size()); i < lim; i++) {
                    if (outputEM[i] != null) {
                        eOutputHatches.get(i).getContentHandler().putUnifyAll(outputEM[i]);
                        outputEM[i] = null;
                    }
                }
            }
        }
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.collider.name")) // Machine Type: Matter
                                                                                              // Collider
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.collider.desc.0")) // Controller block of
                                                                                               // the Matter Collider
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.collider.desc.1")) // This machine needs a
                                                                                               // mirrored
                // copy of it to work
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.collider.desc.2")) // One needs to be set to
                                                                                               // 'Fuse
                // Mode' and the other to 'Collide
                // Mode'
                .addInfo(translateToLocal("gt.blockmachines.multimachine.em.collider.desc.3")) // Fuses two elemental
                                                                                               // matter to
                // create another (and power)
                .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
                .addSeparator().beginStructureBlock(21, 3, 23, false)
                .addOtherStructurePart(
                        translateToLocal("gt.blockmachines.multimachine.em.collider.name"),
                        translateToLocal("gt.blockmachines.multimachine.em.collider.Structure.AdditionalCollider"),
                        2) // Matter Collider: Needs another Matter Collider that is mirrored to this one
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalInput"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing2D"),
                        2) // Elemental Input Hatch: Any Molecular Casing with 2 dots
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOutput"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing3D"),
                        2) // Elemental Output Hatch: Any Molecular Casing with 3 dots
                .addOtherStructurePart(
                        translateToLocal("tt.keyword.Structure.ElementalOverflow"),
                        translateToLocal("tt.keyword.Structure.AnyMolecularCasing4D"),
                        2) // Elemental Overflow Hatch: Any Molecular Casing with 4 dots
                .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                                // High Power Casing
                .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                     // Hatch: Any High
                                                                                                     // Power Casing
                .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        ScreenOFF = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER");
        ScreenON = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_ACTIVE");
        ScreenOFF_Slave = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_SLAVE");
        ScreenON_Slave = new Textures.BlockIcons.CustomIcon("iconsets/EM_COLLIDER_ACTIVE_SLAVE");
        super.registerIcons(aBlockIconRegister);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex,
            boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if (aFacing % 2 == 0) {
                return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][4],
                        new TT_RenderedExtendedFacingTexture(aActive ? ScreenON : ScreenOFF) };
            } else {
                return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][4],
                        new TT_RenderedExtendedFacingTexture(aActive ? ScreenON_Slave : ScreenOFF_Slave) };
            }
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][4] };
    }

    @Override
    protected void parametersInstantiation_EM() {
        Parameters.Group hatch_0 = parametrization.getGroup(0);
        mode = hatch_0.makeInParameter(0, FUSE_MODE, MODE_NAME, MODE_STATUS);
    }

    @Override
    public void parametersStatusesWrite_EM(boolean machineBusy) {
        if (isMaster()) {
            super.parametersStatusesWrite_EM(machineBusy);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("eTier", eTier); // collider tier
        aNBT.setBoolean("eStarted", started);
        if (stack != null) {
            aNBT.setTag("eStack", stack.toNBT(TecTech.definitionsRegistry));
        }
        aNBT.setLong("ePlasmaEnergy", plasmaEnergy);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        eTier = aNBT.getByte("eTier"); // collider tier
        started = aNBT.getBoolean("eStarted");
        if (aNBT.hasKey("eStack")) {
            stack = EMInstanceStack.fromNBT(TecTech.definitionsRegistry, aNBT.getCompoundTag("eStack"));
        }
        plasmaEnergy = aNBT.getLong("ePlasmaEnergy");
    }

    @Override
    public void stopMachine() {
        started = false;
        if (stack != null) {
            cleanMassEM_EM(stack.getMass());
            stack = null;
        }
        super.stopMachine();
    }

    @Override
    protected void afterRecipeCheckFailed() {
        started = false;
        if (stack != null) {
            cleanMassEM_EM(stack.getMass());
            stack = null;
        }
        getBaseMetaTileEntity().disableWorking();
        super.afterRecipeCheckFailed();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if (!aBaseMetaTileEntity.isAllowedToWork()) {
            started = false;
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        IGregTechTileEntity iGregTechTileEntity = getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX * 4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY * 4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ * 4;
        if (hintsOnly) {
            StructureLibAPI.hintParticle(
                    iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord() + xDir,
                    iGregTechTileEntity.getYCoord() + yDir,
                    iGregTechTileEntity.getZCoord() + zDir,
                    StructureLibAPI.getBlockHint(),
                    12);
        }
        structureBuild_EM("tier" + (((trigger.stackSize - 1) % 2) + 1), 11, 1, 18, trigger, hintsOnly);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }
}
