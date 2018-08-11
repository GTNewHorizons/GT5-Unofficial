package com.github.technus.tectech.thing.metaTileEntity.multi;

import com.github.technus.tectech.CommonValues;
import com.github.technus.tectech.TecTech;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.dComplexAspectDefinition;
import com.github.technus.tectech.compatibility.thaumcraft.elementalMatter.definitions.ePrimalAspectDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalInstanceStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.cElementalMutableDefinitionStackMap;
import com.github.technus.tectech.mechanics.elementalMatter.core.stacks.cElementalInstanceStack;
import com.github.technus.tectech.mechanics.elementalMatter.core.templates.cElementalPrimitive;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.atom.dAtomDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.complex.hadron.dHadronDefinition;
import com.github.technus.tectech.mechanics.elementalMatter.definitions.primitive.eQuarkDefinition;
import com.github.technus.tectech.thing.block.QuantumGlassBlock;
import com.github.technus.tectech.thing.casing.TT_Container_Casings;
import com.github.technus.tectech.thing.metaTileEntity.IConstructable;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputElemental;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedTexture;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.HashMap;

import static com.github.technus.tectech.Util.StructureBuilderExtreme;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class GT_MetaTileEntity_EM_collider extends GT_MetaTileEntity_MultiblockBase_EM implements IConstructable {
    private static Textures.BlockIcons.CustomIcon ScreenOFF;
    private static Textures.BlockIcons.CustomIcon ScreenON;
    private static Textures.BlockIcons.CustomIcon ScreenON_Slave;
    private static Textures.BlockIcons.CustomIcon ScreenOFF_Slave;

    private static double MASS_TO_EU_PARTIAL,MASS_TO_EU_INSTANT;
    private static int STARTUP_COST,KEEPUP_COST;

    private long plasmaEnergy;

    public static void setValues(int heliumPlasmaValue){
        MASS_TO_EU_PARTIAL = heliumPlasmaValue / 1.75893000478707E07;//mass diff
        MASS_TO_EU_INSTANT = MASS_TO_EU_PARTIAL * 20;
        STARTUP_COST=-heliumPlasmaValue*10000;
        KEEPUP_COST=-heliumPlasmaValue;
    }

    //region collision handlers
    public static final HashMap<Integer, ColliderHandler> FUSE_HANDLERS =new HashMap<>();
    public static final HashMap<String, PrimitiveColliderHandler> PRIMITIVE_FUSE_HANDLERS =new HashMap<>();
    public interface PrimitiveColliderHandler {
        void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out);
    }
    public interface ColliderHandler extends PrimitiveColliderHandler {
        byte getRequiredTier();
    }
    static {
        FUSE_HANDLERS.put((dAtomDefinition.getClassTypeStatic() << 16) | dAtomDefinition.getClassTypeStatic(), new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                try {
                    cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
                    defs.putUnifyAll(in1.definition.getSubParticles());
                    defs.putUnifyAll(in2.definition.getSubParticles());
                    dAtomDefinition atom = new dAtomDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
                    out.putUnify(new cElementalInstanceStack(atom,Math.min(in1.amount,in2.amount)));
                }catch (Exception e){
                    out.putUnifyAll(in1,in2);
                    return;
                }
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }

            @Override
            public byte getRequiredTier() {
                return 1;
            }
        });
        registerSimpleAtomFuse(dHadronDefinition.getClassTypeStatic());
        registerSimpleAtomFuse(dComplexAspectDefinition.getClassTypeStatic());
        registerSimpleAtomFuse(cElementalPrimitive.getClassTypeStatic());

        FUSE_HANDLERS.put((dHadronDefinition.getClassTypeStatic() << 16) | dHadronDefinition.getClassTypeStatic(), new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                try {
                    cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
                    defs.putUnifyAll(in1.definition.getSubParticles());
                    defs.putUnifyAll(in2.definition.getSubParticles());
                    dHadronDefinition hadron = new dHadronDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
                    out.putUnify(new cElementalInstanceStack(hadron,Math.min(in1.amount,in2.amount)));
                }catch (Exception e){
                    out.putUnifyAll(in1,in2);
                    return;
                }
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }

            @Override
            public byte getRequiredTier() {
                return 2;
            }
        });
        FUSE_HANDLERS.put((dHadronDefinition.getClassTypeStatic() << 16) | cElementalPrimitive.getClassTypeStatic(), new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                try {
                    cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
                    defs.putUnifyAll(in1.definition.getSubParticles());
                    defs.putUnify(in2.definition.getStackForm(1));
                    dHadronDefinition hadron = new dHadronDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
                    out.putUnify(new cElementalInstanceStack(hadron,Math.min(in1.amount,in2.amount)));
                }catch (Exception e){
                    out.putUnifyAll(in1,in2);
                    return;
                }
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }

            @Override
            public byte getRequiredTier() {
                return 2;
            }
        });

        registerSimpleAspectFuse(dComplexAspectDefinition.getClassTypeStatic());
        registerSimpleAspectFuse(cElementalPrimitive.getClassTypeStatic());

        FUSE_HANDLERS.put((cElementalPrimitive.getClassTypeStatic() << 16) | cElementalPrimitive.getClassTypeStatic(), new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                PrimitiveColliderHandler collisionHandler= PRIMITIVE_FUSE_HANDLERS.get(in1.definition.getClass().getName()+'\0'+in2.definition.getClass().getName());
                if (collisionHandler != null) {
                    collisionHandler.collide(in2, in1, out);
                } else {
                    out.putUnifyAll(in1,in2);
                }
            }

            @Override
            public byte getRequiredTier() {
                return 2;
            }
        });

        PRIMITIVE_FUSE_HANDLERS.put(eQuarkDefinition.class.getName() + '\0' + eQuarkDefinition.class.getName(), new PrimitiveColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                try {
                    cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
                    defs.putUnify(in1.definition.getStackForm(1));
                    defs.putUnify(in2.definition.getStackForm(1));
                    dHadronDefinition hadron = new dHadronDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
                    out.putUnify(new cElementalInstanceStack(hadron,Math.min(in1.amount,in2.amount)));
                }catch (Exception e){
                    out.putUnifyAll(in1,in2);
                    return;
                }
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }
        });
        PRIMITIVE_FUSE_HANDLERS.put(ePrimalAspectDefinition.class.getName() + '\0' + ePrimalAspectDefinition.class.getName(), new PrimitiveColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                if (fuseAspects(in1, in2, out)) return;
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }
        });
    }

    private static boolean fuseAspects(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
        try {
            cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
            defs.putUnify(in1.definition.getStackForm(1));
            defs.putUnify(in2.definition.getStackForm(1));
            dComplexAspectDefinition aspect = new dComplexAspectDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
            out.putUnify(new cElementalInstanceStack(aspect,Math.min(in1.amount,in2.amount)));
        }catch (Exception e){
            out.putUnifyAll(in1,in2);
            return true;
        }
        return false;
    }

    private static void registerSimpleAspectFuse(byte classTypeStatic) {
        FUSE_HANDLERS.put((dComplexAspectDefinition.getClassTypeStatic() << 16) | classTypeStatic, new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                if (fuseAspects(in1, in2, out)) return;
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }

            @Override
            public byte getRequiredTier() {
                return 1;
            }
        });
    }

    private static void registerSimpleAtomFuse(byte classTypeStatic) {
        FUSE_HANDLERS.put((dAtomDefinition.getClassTypeStatic() << 16) | classTypeStatic, new ColliderHandler() {
            @Override
            public void collide(cElementalInstanceStack in1, cElementalInstanceStack in2, cElementalInstanceStackMap out) {
                try {
                    cElementalMutableDefinitionStackMap defs=new cElementalMutableDefinitionStackMap();
                    defs.putUnifyAll(in1.definition.getSubParticles());
                    defs.putUnify(in2.definition.getStackForm(1));
                    dAtomDefinition atom = new dAtomDefinition(defs.toImmutable_optimized_unsafeLeavesExposedElementalTree());
                    out.putUnify(new cElementalInstanceStack(atom,Math.min(in1.amount,in2.amount)));
                }catch (Exception e){
                    out.putUnifyAll(in1,in2);
                    return;
                }
                if(in1.amount>in2.amount){
                    out.putUnify(new cElementalInstanceStack(in1.definition,in1.amount-in2.amount));
                }else if (in2.amount>in1.amount){
                    out.putUnify(new cElementalInstanceStack(in2.definition,in2.amount-in1.amount));
                }
            }

            @Override
            public byte getRequiredTier() {
                return 1;
            }
        });
    }
    //endregion

    protected byte eTier = 0;
    protected cElementalInstanceStack stack;

    protected static final byte FUSE_MODE=0, COLLIDE_MODE =1;
    protected boolean started=false;

    //region Structure
    //use multi A energy inputs, use less power the longer it runs
    private static final String[][] shape = new String[][]{
            {"I0A0A0","I00000","I0A0A0",},
            {"H0000000","G001111100","H0000000",},
            {"F22223332222","F41155555114","F22223332222",},
            {"E2000000000002","E4155111115514","E2000000000002",},
            {"D20000E00002","D41511E11514","D20000E00002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"B200M002","A0151M1510","B200M002",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A030O030","0151O1510","A030O030",},
            {"0030O0300","0151O1510","0030O0300",},
            {"A030O030","0151O1510","A030O030",},
            {"0020O0200","0151O1510","0020O0200",},
            {"A0200M0020","A0151M1510","A0200M0020",},
            {"B200M002","A0151M1510","B200M002",},
            {"B2000K0002","B4151K1514","B2000K0002",},
            {"C2000I0002","C4151I1514","C2000I0002",},
            {"D200002   200002","D415112 . 211514","D200002   200002",},
            {"E20!!22222!!02","E4155111115514","E20!!22222!!02",},
            {"F2222#\"#2222","F41155555114","F2222#\"#2222",},
    };
    private static final Block[] blockType = new Block[]{
            sBlockCasingsTT,
            sBlockCasingsTT,
            sBlockCasingsTT,
            QuantumGlassBlock.INSTANCE,
            sBlockCasingsTT,
            sBlockCasingsTT
    };
    private static final byte[] blockMeta1 = new byte[]{4, 7, 4, 0, 4, 8};
    private static final byte[] blockMeta2 = new byte[]{4, 7, 5, 0, 6, 9};
    private static final String[] addingMethods = new String[]{"addClassicToMachineList", "addElementalInputToMachineList", "addElementalOutputToMachineList", "addElementalMufflerToMachineList"};
    private static final short[] casingTextures = new short[]{textureOffset, textureOffset + 4, textureOffset + 4, textureOffset + 4};
    private static final Block[] blockTypeFallback = new Block[]{sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT, sBlockCasingsTT};
    private static final byte[] blockMetaFallback = new byte[]{0, 4, 4, 4};
    private static final String[] description = new String[]{
            EnumChatFormatting.AQUA+"Hint Details:",
            "1 - Classic Hatches or High Power Casing",
            "2 - Elemental Input Hatches or Molecular Casing",
            "3 - Elemental Output Hatches or Molecular Casing",
            "4 - Elemental Overflow Hatches or Molecular Casing",
            "General - Another Controller facing opposite direction",
    };
    //endregion

    public GT_MetaTileEntity_EM_collider(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_collider(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_collider(mName);
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
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            if(aFacing%2==0){
                return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new TT_RenderedTexture(aActive ? ScreenON : ScreenOFF)};
            }else{
                return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4], new TT_RenderedTexture(aActive ? ScreenON_Slave : ScreenOFF_Slave)};
            }
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[texturePage][4]};
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setByte("eTier", eTier);//collider tier
        aNBT.setBoolean("eStarted",started);
        if(stack!=null) {
            aNBT.setTag("eStack", stack.toNBT());
        }
        aNBT.setLong("ePlasmaEnergy",plasmaEnergy);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        eTier = aNBT.getByte("eTier");//collider tier
        started=aNBT.getBoolean("eStarted");
        if(aNBT.hasKey("eStack")){
            stack=cElementalInstanceStack.fromNBT(aNBT.getCompoundTag("eStack"));
        }
        plasmaEnergy=aNBT.getLong("ePlasmaEnergy");
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*2;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*2;
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

        boolean test;
        switch (eTier) {
            case 1:
                test = structureCheck_EM(shape, blockType, blockMeta1, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 11, 1, 18);
                break;
            case 2:
                test = structureCheck_EM(shape, blockType, blockMeta2, addingMethods, casingTextures, blockTypeFallback, blockMetaFallback, 11, 1, 18);
                break;
            default:
                eTier = 0;
                return false;
        }
        if (test) {
            return true;
        }
        eTier = 0;
        return false;
    }

    @Override
    public void construct(int stackSize, boolean hintsOnly) {
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY*4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*4;
        if(hintsOnly){
            TecTech.proxy.hint_particle(iGregTechTileEntity.getWorld(),
                    iGregTechTileEntity.getXCoord()+xDir,
                    iGregTechTileEntity.getYCoord()+yDir,
                    iGregTechTileEntity.getZCoord()+zDir,
                    TT_Container_Casings.sHintCasingsTT,12);
        } else{
            if(iGregTechTileEntity.getBlockOffset(xDir,0,zDir).getMaterial() == Material.air) {
                iGregTechTileEntity.getWorld().setBlock(iGregTechTileEntity.getXCoord() + xDir, iGregTechTileEntity.getYCoord()+yDir, iGregTechTileEntity.getZCoord() + zDir, TT_Container_Casings.sHintCasingsTT, 12, 2);
            }
        }
        if ((stackSize & 1) == 1) {
            StructureBuilderExtreme(shape, blockType, blockMeta1, 11, 1, 18, iGregTechTileEntity,this, hintsOnly);
        } else {
            StructureBuilderExtreme(shape, blockType, blockMeta2, 11, 1, 18, iGregTechTileEntity,this, hintsOnly);
        }
    }

    @Override
    public void parametersOutAndStatusesWrite_EM(boolean machineBusy) {
        if(isMaster()) {
            double mode = getParameterIn(0, 0);
            if (mode == FUSE_MODE || mode == COLLIDE_MODE) {
                setStatusOfParameterIn(0, 0, STATUS_OK);
            } else if (mode > 1) {
                setStatusOfParameterIn(0, 0, STATUS_TOO_HIGH);
            } else if (mode < 0) {
                setStatusOfParameterIn(0, 0, STATUS_TOO_LOW);
            }else{
                setStatusOfParameterIn(0,0,STATUS_WRONG);
            }
        }
    }

    @Override
    public boolean checkRecipe_EM(ItemStack itemStack) {
        GT_MetaTileEntity_EM_collider partner=getPartner();
        if(partner==null){
            return false;
        }
        mEfficiencyIncrease=10000;
        if(started) {
            if(stack==null) {
                for (GT_MetaTileEntity_Hatch_InputElemental inputElemental : eInputHatches) {
                    cElementalInstanceStackMap container = inputElemental.getContainerHandler();
                    if (container.isEmpty()) {
                        continue;
                    }
                    stack = container.remove(container.getFirst().definition);
                    long eut = KEEPUP_COST+(long)(KEEPUP_COST * Math.abs(stack.getMass() / dAtomDefinition.getSomethingHeavy().getMass()))/2;
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
            return true;
        }else{
            started=true;
            mMaxProgresstime=20;
            mEUt=STARTUP_COST;
            eAmpereFlow=10;
            return true;
        }
    }

    protected double fuse(GT_MetaTileEntity_EM_collider partner){
        if(partner.stack!=null && stack!=null) {//todo add single event mode as an option
            boolean check=stack.definition.decayMakesEnergy(stack.getEnergy()) &&
                    partner.stack.definition.decayMakesEnergy(partner.stack.getEnergy());

            cElementalInstanceStack stack2 = partner.stack;
            partner.stack = null;
            double preMass = stack2.getMass() + stack.getMass();
            //System.out.println("preMass = " + preMass);

            cElementalInstanceStackMap map = new cElementalInstanceStackMap();
            ColliderHandler colliderHandler;
            if (stack2.definition.getClassType() > stack.definition.getClassType()) {//always bigger first
                colliderHandler = FUSE_HANDLERS.get((stack2.definition.getClassType() << 16) | stack.definition.getClassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            } else {
                colliderHandler = FUSE_HANDLERS.get((stack.definition.getClassType() << 16) | stack2.definition.getClassType());
                if (handleRecipe(stack2, map, colliderHandler)) return 0;
            }
            for (cElementalInstanceStack newStack : map.values()) {
                check &= newStack.definition.decayMakesEnergy(newStack.getEnergy());
            }
            //System.out.println("outputEM[0].getMass() = " + outputEM[0].getMass());
            outputEM = new cElementalInstanceStackMap[]{map};
            return check ? preMass - map.getMass() :
                    Math.min(preMass - map.getMass(), 0);
        }
        return 0;
    }

    private boolean handleRecipe(cElementalInstanceStack stack2, cElementalInstanceStackMap map, ColliderHandler colliderHandler) {
        if (colliderHandler != null && eTier>= colliderHandler.getRequiredTier()) {
            colliderHandler.collide(stack2, stack, map);
        } else {
            map.putUnifyAll(stack,stack2);
            outputEM=new cElementalInstanceStackMap[]{map};
            return true;
        }
        return false;
    }

    @Override
    protected void afterRecipeCheckFailed() {
        started=false;
        if(stack!=null){
            cleanMassEM_EM(stack.getMass());
            stack=null;
        }
        getBaseMetaTileEntity().disableWorking();
        super.afterRecipeCheckFailed();
    }

    @Override
    public void stopMachine() {
        started=false;
        if(stack!=null){
            cleanMassEM_EM(stack.getMass());
            stack=null;
        }
        super.stopMachine();
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if(!aBaseMetaTileEntity.isAllowedToWork()){
            started=false;
        }
        super.onPreTick(aBaseMetaTileEntity, aTick);
    }

    protected GT_MetaTileEntity_EM_collider getPartner(){
        IGregTechTileEntity iGregTechTileEntity=getBaseMetaTileEntity();
        int xDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetX*4;
        int yDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetY*4;
        int zDir = ForgeDirection.getOrientation(iGregTechTileEntity.getBackFacing()).offsetZ*4;
        IGregTechTileEntity gregTechBaseTileEntity=iGregTechTileEntity.getIGregTechTileEntityOffset(xDir, yDir, zDir);
        if(gregTechBaseTileEntity!=null) {
            IMetaTileEntity gregTechMetaTileEntity = gregTechBaseTileEntity.getMetaTileEntity();
            return gregTechMetaTileEntity instanceof GT_MetaTileEntity_EM_collider &&
                    ((GT_MetaTileEntity_EM_collider) gregTechMetaTileEntity).mMachine &&
                    gregTechBaseTileEntity.getBackFacing() == iGregTechTileEntity.getFrontFacing() ?
                    (GT_MetaTileEntity_EM_collider) gregTechMetaTileEntity : null;
        }
        return null;
    }

    protected final boolean isMaster(){
        return getBaseMetaTileEntity().getFrontFacing()%2==0;
    }

    @Override
    public void outputAfterRecipe_EM(){
        GT_MetaTileEntity_EM_collider partner=getPartner();
        if(partner==null){
            if(stack!=null){
                cleanMassEM_EM(stack.getMass());
                stack=null;
            }
            return;
        }
        if (isMaster()) {
            switch (getParameterInInt(0,0)){
                case FUSE_MODE:
                    makeEU(fuse(partner));
                    break;
                case COLLIDE_MODE:
                    //collide(partner);//todo
                    break;
                default: {
                    outputEM=new cElementalInstanceStackMap[2];
                    outputEM[1]=tickStack();
                    if(outputEM[1]==null){
                        outputEM[1]=partner.tickStack();
                    }else {
                        cElementalInstanceStackMap map=partner.tickStack();
                        if(map!=null){
                            outputEM[1].putUnifyAll(map);
                        }
                    }
                }
            }
            if(outputEM!=null) {
                for(int i=0,lim=Math.min(outputEM.length,eOutputHatches.size());i<lim;i++){
                    if(outputEM[i]!=null) {
                        eOutputHatches.get(i).getContainerHandler().putUnifyAll(outputEM[i]);
                        outputEM[i]=null;
                    }
                }
            }
        }
    }

    private void makeEU(double massDiff){
        plasmaEnergy+=massDiff*MASS_TO_EU_INSTANT;
    }

    private cElementalInstanceStackMap tickStack(){
        if(stack==null){
            return null;
        }
        cElementalInstanceStackMap newInstances = stack.decay(1, stack.age += 1, 0);
        if (newInstances == null) {
            stack.nextColor();
        } else {
            stack=newInstances.remove(newInstances.getLast().definition);
        }
        return newInstances;
    }

    @Override
    public String[] getStructureDescription(int stackSize) {
        return description;
    }

    @Override
    public String[] getDescription() {
        return new String[]{
                CommonValues.TEC_MARK_EM,
                "Collide matter at extreme velocities.",
                EnumChatFormatting.AQUA.toString() + EnumChatFormatting.BOLD + "Faster than light*!!!"
        };
    }
}
