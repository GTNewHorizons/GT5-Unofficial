package gtPlusPlus.preloader.asm.transformers;

import static gregtech.api.enums.Mods.TinkerConstruct;
import static org.objectweb.asm.Opcodes.AALOAD;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ASM5;
import static org.objectweb.asm.Opcodes.BIPUSH;
import static org.objectweb.asm.Opcodes.D2F;
import static org.objectweb.asm.Opcodes.DADD;
import static org.objectweb.asm.Opcodes.DDIV;
import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.DUP_X1;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.GOTO;
import static org.objectweb.asm.Opcodes.I2D;
import static org.objectweb.asm.Opcodes.IADD;
import static org.objectweb.asm.Opcodes.ICONST_0;
import static org.objectweb.asm.Opcodes.ICONST_1;
import static org.objectweb.asm.Opcodes.IFEQ;
import static org.objectweb.asm.Opcodes.IFLE;
import static org.objectweb.asm.Opcodes.IFNE;
import static org.objectweb.asm.Opcodes.IFNONNULL;
import static org.objectweb.asm.Opcodes.IFNULL;
import static org.objectweb.asm.Opcodes.IF_ICMPEQ;
import static org.objectweb.asm.Opcodes.ILOAD;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.IREM;
import static org.objectweb.asm.Opcodes.ISTORE;
import static org.objectweb.asm.Opcodes.ISUB;
import static org.objectweb.asm.Opcodes.POP;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import gtPlusPlus.core.util.reflect.ReflectionUtils;
import gtPlusPlus.preloader.Preloader_Logger;

public class ClassTransformer_TT_ThaumicRestorer {

    private final boolean isValid;
    private final ClassReader reader;
    private final ClassWriter writer;

    private static boolean mInit = false;

    private static Class mTileRepairerClass;
    private static Class mTTConfigHandler;
    private static Class mTinkersConstructCompat;
    private static Class mThaumicTinkerer;
    private static Class mTCProxy;

    private static Method mIsTcTool;
    private static Method mGetTcDamage;
    private static Method mFixTcDamage;
    private static Method mSparkle;
    private static Method mDrawEssentia;

    private static Field mRepairTiconTools;
    private static Field mTicksExisted;
    private static Field mInventory;
    private static Field mTookLastTick;
    private static Field mDamageLastTick;
    private static Field mProxyTC;

    private static boolean repairTConTools = false;

    // thaumic.tinkerer.common.block.tile.TileRepairer

    private static final boolean isTConstructTool(ItemStack aStack) {
        return ReflectionUtils.invoke(null, mIsTcTool, new Object[] { aStack });
    }

    private static final int getDamage(ItemStack aStack) {
        return (int) ReflectionUtils.invokeNonBool(null, mGetTcDamage, new Object[] { aStack });
    }

    private static final boolean fixDamage(ItemStack aStack, int aAmount) {
        return ReflectionUtils.invoke(null, mFixTcDamage, new Object[] { aStack, aAmount });
    }

    private static final int drawEssentia(TileEntity aTile) {
        return (int) ReflectionUtils.invokeNonBool(aTile, mDrawEssentia, new Object[] {});
    }

    private static final void sparkle(float a, float b, float c, int d) {
        ReflectionUtils.invokeVoid(ReflectionUtils.getFieldValue(mProxyTC), mSparkle, new Object[] { a, b, c, d });
    }

    public static void updateEntity(TileEntity aTile) {
        if (!mInit) {
            // Set the classes we need
            mTileRepairerClass = ReflectionUtils.getClass("thaumic.tinkerer.common.block.tile.TileRepairer");
            mTTConfigHandler = ReflectionUtils.getClass("thaumic.tinkerer.common.core.handler.ConfigHandler");
            mTinkersConstructCompat = ReflectionUtils.getClass("thaumic.tinkerer.common.compat.TinkersConstructCompat");
            mThaumicTinkerer = ReflectionUtils.getClass("thaumic.tinkerer.common.ThaumicTinkerer");
            mTCProxy = ReflectionUtils.getClass("thaumcraft.common.CommonProxy");
            // Set the methods we need
            mIsTcTool = ReflectionUtils
                    .getMethod(mTinkersConstructCompat, "isTConstructTool", new Class[] { ItemStack.class });
            mGetTcDamage = ReflectionUtils
                    .getMethod(mTinkersConstructCompat, "getDamage", new Class[] { ItemStack.class });
            mFixTcDamage = ReflectionUtils
                    .getMethod(mTinkersConstructCompat, "fixDamage", new Class[] { ItemStack.class, int.class });
            mSparkle = ReflectionUtils
                    .getMethod(mTCProxy, "sparkle", new Class[] { float.class, float.class, float.class, int.class });
            mDrawEssentia = ReflectionUtils.getMethod(mTileRepairerClass, "drawEssentia", new Class[] {});
            // Set the fields we need
            mRepairTiconTools = ReflectionUtils.getField(mTTConfigHandler, "repairTConTools");
            mTicksExisted = ReflectionUtils.getField(mTileRepairerClass, "ticksExisted");
            mInventory = ReflectionUtils.getField(mTileRepairerClass, "inventorySlots");
            mTookLastTick = ReflectionUtils.getField(mTileRepairerClass, "tookLastTick");
            mDamageLastTick = ReflectionUtils.getField(mTileRepairerClass, "dmgLastTick");
            mProxyTC = ReflectionUtils.getField(mThaumicTinkerer, "tcProxy");
            repairTConTools = ReflectionUtils.getFieldValue(mRepairTiconTools);
            mInit = true;
        }
        if (mInit) {
            if (mTileRepairerClass.isInstance(aTile)) {
                int ticksExisted = (int) ReflectionUtils.getFieldValue(mTicksExisted, aTile);
                ItemStack[] inventorySlots = (ItemStack[]) ReflectionUtils.getFieldValue(mInventory, aTile);
                boolean tookLastTick = (boolean) ReflectionUtils.getFieldValue(mTookLastTick, aTile);
                int dmgLastTick = (int) ReflectionUtils.getFieldValue(mDamageLastTick, aTile);
                ticksExisted++;
                ReflectionUtils.setField(aTile, mTicksExisted, ticksExisted);
                boolean aDidRun = false;
                if (ticksExisted % 10 == 0) {
                    if (TinkerConstruct.isModLoaded() && repairTConTools
                            && inventorySlots[0] != null
                            && isTConstructTool(inventorySlots[0])) {
                        final int dmg = getDamage(inventorySlots[0]);
                        if (dmg > 0) {
                            final int essentia = drawEssentia(aTile);
                            fixDamage(inventorySlots[0], essentia);
                            aTile.markDirty();
                            if (dmgLastTick != 0 && dmgLastTick != dmg) {
                                sparkle(
                                        (float) (aTile.xCoord + 0.25 + Math.random() / 2.0),
                                        (float) (aTile.yCoord + 1 + Math.random() / 2.0),
                                        (float) (aTile.zCoord + 0.25 + Math.random() / 2.0),
                                        0);
                                tookLastTick = true;
                            } else {
                                tookLastTick = false;
                            }
                        } else {
                            tookLastTick = false;
                        }
                        dmgLastTick = ((inventorySlots[0] == null) ? 0 : getDamage(inventorySlots[0]));
                        aDidRun = true;
                    }
                    if (inventorySlots[0] != null && inventorySlots[0].getItemDamage() > 0
                            && inventorySlots[0].getItem().isRepairable()) {
                        final int essentia2 = drawEssentia(aTile);
                        final int dmg2 = inventorySlots[0].getItemDamage();
                        inventorySlots[0].setItemDamage(Math.max(0, dmg2 - essentia2));
                        aTile.markDirty();
                        if (dmgLastTick != 0 && dmgLastTick != dmg2) {
                            sparkle(
                                    (float) (aTile.xCoord + 0.25 + Math.random() / 2.0),
                                    (float) (aTile.yCoord + 1 + Math.random() / 2.0),
                                    (float) (aTile.zCoord + 0.25 + Math.random() / 2.0),
                                    0);
                            tookLastTick = true;
                        } else {
                            tookLastTick = false;
                        }
                    } else {
                        tookLastTick = false;
                    }
                    dmgLastTick = ((inventorySlots[0] == null) ? 0 : inventorySlots[0].getItemDamage());
                    aDidRun = true;
                }
                if (aDidRun) {
                    ReflectionUtils.setField(aTile, mInventory, inventorySlots);
                    ReflectionUtils.setField(aTile, mTookLastTick, tookLastTick);
                    ReflectionUtils.setField(aTile, mDamageLastTick, dmgLastTick);
                }
            }
        }
    }

    public ClassTransformer_TT_ThaumicRestorer(byte[] basicClass) {
        ClassReader aTempReader = null;
        ClassWriter aTempWriter = null;
        boolean obfuscated = false;
        aTempReader = new ClassReader(basicClass);
        aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
        AddAdapter aMethodStripper = new AddAdapter(aTempWriter);
        aTempReader.accept(aMethodStripper, 0);
        obfuscated = aMethodStripper.isObfuscated;
        String aUpdateEntity = obfuscated ? "func_145845_h" : "updateEntity";
        Preloader_Logger.LOG(
                "Thaumic Tinkerer RepairItem Patch",
                Level.INFO,
                "Patching: " + aUpdateEntity + ", Are we patching obfuscated methods? " + obfuscated);
        // injectMethod(aUpdateEntity, aTempWriter, obfuscated);
        injectMethodNew(aTempWriter, obfuscated);
        if (aTempReader != null && aTempWriter != null) {
            isValid = true;
        } else {
            isValid = false;
        }
        Preloader_Logger.LOG("Thaumic Tinkerer RepairItem Patch", Level.INFO, "Valid? " + isValid + ".");
        reader = aTempReader;
        writer = aTempWriter;
    }

    public boolean isValidTransformer() {
        return isValid;
    }

    public ClassReader getReader() {
        return reader;
    }

    public ClassWriter getWriter() {
        return writer;
    }

    public boolean injectMethodNew(ClassWriter cw, boolean obfuscated) {
        MethodVisitor mv;
        boolean didInject = false;
        String aUpdateEntity = obfuscated ? "func_145845_h" : "updateEntity";
        String aTileEntity = obfuscated ? "aor" : "net/minecraft/tileentity/TileEntity";
        Preloader_Logger.LOG("Thaumic Tinkerer RepairItem Patch", Level.INFO, "Injecting " + aUpdateEntity + ".");
        mv = cw.visitMethod(ACC_PUBLIC, aUpdateEntity, "()V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(60, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "gtPlusPlus/preloader/asm/transformers/ClassTransformer_TT_ThaumicRestorer",
                "updateEntity",
                "(L" + aTileEntity + ";)V",
                false);
        Label l1 = new Label();
        mv.visitLabel(l1);
        mv.visitLineNumber(61, l1);
        mv.visitInsn(RETURN);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLocalVariable("this", "Lthaumic/tinkerer/common/block/tile/TileRepairer;", null, l0, l2, 0);
        mv.visitMaxs(1, 1);
        mv.visitEnd();
        didInject = true;
        Preloader_Logger.LOG(
                "Thaumic Tinkerer RepairItem Patch",
                Level.INFO,
                "Method injection complete. " + (obfuscated ? "Obfuscated" : "Non-Obfuscated"));
        return didInject;
    }

    public boolean injectMethod(String aMethodName, ClassWriter cw, boolean obfuscated) {
        MethodVisitor mv;
        boolean didInject = false;
        Preloader_Logger.LOG("Thaumic Tinkerer RepairItem Patch", Level.INFO, "Injecting " + aMethodName + ".");

        String aItemStack = obfuscated ? "add" : "net/minecraft/item/ItemStack";
        String aItem = obfuscated ? "adb" : "net/minecraft/item/Item";
        String aGetItemDamage = obfuscated ? "func_150976_a" : "getItemDamage";
        String aGetItem = obfuscated ? "func_77973_b" : "getItem";
        String aSetItemDamage = obfuscated ? "func_77964_b" : "setItemDamage";
        String aIsRepairable = obfuscated ? "func_82789_a" : "isRepairable";

        mv = cw.visitMethod(ACC_PUBLIC, aMethodName, "()V", null, null);
        mv.visitCode();
        Label l0 = new Label();
        mv.visitLabel(l0);
        mv.visitLineNumber(59, l0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(DUP);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "ticksExisted", "I");
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(DUP_X1);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "ticksExisted", "I");
        mv.visitIntInsn(BIPUSH, 10);
        mv.visitInsn(IREM);
        Label l1 = new Label();
        mv.visitJumpInsn(IFNE, l1);
        Label l2 = new Label();
        mv.visitLabel(l2);
        mv.visitLineNumber(60, l2);
        mv.visitLdcInsn(TinkerConstruct.ID);
        mv.visitMethodInsn(INVOKESTATIC, "cpw/mods/fml/common/Loader", "isModLoaded", "(Ljava/lang/String;)Z", false);
        Label l3 = new Label();
        mv.visitJumpInsn(IFEQ, l3);
        mv.visitFieldInsn(GETSTATIC, "thaumic/tinkerer/common/core/handler/ConfigHandler", "repairTConTools", "Z");
        mv.visitJumpInsn(IFEQ, l3);
        Label l4 = new Label();
        mv.visitLabel(l4);
        mv.visitLineNumber(61, l4);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitJumpInsn(IFNULL, l3);
        Label l5 = new Label();
        mv.visitLabel(l5);
        mv.visitLineNumber(62, l5);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumic/tinkerer/common/compat/TinkersConstructCompat",
                "isTConstructTool",
                "(L" + aItemStack + ";)Z",
                false);
        mv.visitJumpInsn(IFEQ, l3);
        Label l6 = new Label();
        mv.visitLabel(l6);
        mv.visitLineNumber(63, l6);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumic/tinkerer/common/compat/TinkersConstructCompat",
                "getDamage",
                "(L" + aItemStack + ";)I",
                false);
        mv.visitVarInsn(ISTORE, 1);
        Label l7 = new Label();
        mv.visitLabel(l7);
        mv.visitLineNumber(64, l7);
        mv.visitVarInsn(ILOAD, 1);
        Label l8 = new Label();
        mv.visitJumpInsn(IFLE, l8);
        Label l9 = new Label();
        mv.visitLabel(l9);
        mv.visitLineNumber(65, l9);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "drawEssentia",
                "()I",
                false);
        mv.visitVarInsn(ISTORE, 2);
        Label l10 = new Label();
        mv.visitLabel(l10);
        mv.visitLineNumber(66, l10);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumic/tinkerer/common/compat/TinkersConstructCompat",
                "fixDamage",
                "(L" + aItemStack + ";I)Z",
                false);
        mv.visitInsn(POP);
        Label l11 = new Label();
        mv.visitLabel(l11);
        mv.visitLineNumber(67, l11);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "thaumic/tinkerer/common/block/tile/TileRepairer", "markDirty", "()V", false);
        Label l12 = new Label();
        mv.visitLabel(l12);
        mv.visitLineNumber(68, l12);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        Label l13 = new Label();
        mv.visitJumpInsn(IFEQ, l13);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        mv.visitVarInsn(ILOAD, 1);
        mv.visitJumpInsn(IF_ICMPEQ, l13);
        Label l14 = new Label();
        mv.visitLabel(l14);
        mv.visitLineNumber(69, l14);
        mv.visitFieldInsn(
                GETSTATIC,
                "thaumic/tinkerer/common/ThaumicTinkerer",
                "tcProxy",
                "Lthaumcraft/common/CommonProxy;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "xCoord", "I");
        mv.visitInsn(I2D);
        mv.visitLdcInsn(new Double("0.25"));
        mv.visitInsn(DADD);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "yCoord", "I");
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(I2D);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "zCoord", "I");
        mv.visitInsn(I2D);
        mv.visitLdcInsn(new Double("0.25"));
        mv.visitInsn(DADD);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/common/CommonProxy", "sparkle", "(FFFI)V", false);
        Label l15 = new Label();
        mv.visitLabel(l15);
        mv.visitLineNumber(70, l15);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_1);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        Label l16 = new Label();
        mv.visitLabel(l16);
        mv.visitLineNumber(71, l16);
        Label l17 = new Label();
        mv.visitJumpInsn(GOTO, l17);
        mv.visitLabel(l13);
        mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER }, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_0);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        Label l18 = new Label();
        mv.visitLabel(l18);
        mv.visitLineNumber(72, l18);
        mv.visitJumpInsn(GOTO, l17);
        mv.visitLabel(l8);
        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_0);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        mv.visitLabel(l17);
        mv.visitLineNumber(73, l17);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        Label l19 = new Label();
        mv.visitJumpInsn(IFNONNULL, l19);
        mv.visitInsn(ICONST_0);
        Label l20 = new Label();
        mv.visitJumpInsn(GOTO, l20);
        mv.visitLabel(l19);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer" });
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(
                INVOKESTATIC,
                "thaumic/tinkerer/common/compat/TinkersConstructCompat",
                "getDamage",
                "(L" + aItemStack + ";)I",
                false);
        mv.visitLabel(l20);
        mv.visitFrame(
                Opcodes.F_FULL,
                2,
                new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer", Opcodes.INTEGER },
                2,
                new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer", Opcodes.INTEGER });
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        Label l21 = new Label();
        mv.visitLabel(l21);
        mv.visitLineNumber(74, l21);
        mv.visitInsn(RETURN);
        mv.visitLabel(l3);
        mv.visitLineNumber(78, l3);
        mv.visitFrame(Opcodes.F_CHOP, 1, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        Label l22 = new Label();
        mv.visitJumpInsn(IFNULL, l22);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItemStack + "", "" + aGetItemDamage + "", "()I", false);
        mv.visitJumpInsn(IFLE, l22);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItemStack + "", "" + aGetItem + "", "()L" + aItem + ";", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItem + "", "" + aIsRepairable + "", "()Z", false);
        mv.visitJumpInsn(IFEQ, l22);
        Label l23 = new Label();
        mv.visitLabel(l23);
        mv.visitLineNumber(79, l23);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(
                INVOKEVIRTUAL,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "drawEssentia",
                "()I",
                false);
        mv.visitVarInsn(ISTORE, 1);
        Label l24 = new Label();
        mv.visitLabel(l24);
        mv.visitLineNumber(80, l24);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItemStack + "", "" + aGetItemDamage + "", "()I", false);
        mv.visitVarInsn(ISTORE, 2);
        Label l25 = new Label();
        mv.visitLabel(l25);
        mv.visitLineNumber(81, l25);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitInsn(ICONST_0);
        mv.visitVarInsn(ILOAD, 2);
        mv.visitVarInsn(ILOAD, 1);
        mv.visitInsn(ISUB);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "max", "(II)I", false);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItemStack + "", "" + aSetItemDamage + "", "(I)V", false);
        Label l26 = new Label();
        mv.visitLabel(l26);
        mv.visitLineNumber(82, l26);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "thaumic/tinkerer/common/block/tile/TileRepairer", "markDirty", "()V", false);
        Label l27 = new Label();
        mv.visitLabel(l27);
        mv.visitLineNumber(84, l27);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        Label l28 = new Label();
        mv.visitJumpInsn(IFEQ, l28);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        mv.visitVarInsn(ILOAD, 2);
        mv.visitJumpInsn(IF_ICMPEQ, l28);
        Label l29 = new Label();
        mv.visitLabel(l29);
        mv.visitLineNumber(85, l29);
        mv.visitFieldInsn(
                GETSTATIC,
                "thaumic/tinkerer/common/ThaumicTinkerer",
                "tcProxy",
                "Lthaumcraft/common/CommonProxy;");
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "xCoord", "I");
        mv.visitInsn(I2D);
        mv.visitLdcInsn(new Double("0.25"));
        mv.visitInsn(DADD);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "yCoord", "I");
        mv.visitInsn(ICONST_1);
        mv.visitInsn(IADD);
        mv.visitInsn(I2D);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(GETFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "zCoord", "I");
        mv.visitInsn(I2D);
        mv.visitLdcInsn(new Double("0.25"));
        mv.visitInsn(DADD);
        mv.visitMethodInsn(INVOKESTATIC, "java/lang/Math", "random", "()D", false);
        mv.visitLdcInsn(new Double("2.0"));
        mv.visitInsn(DDIV);
        mv.visitInsn(DADD);
        mv.visitInsn(D2F);
        mv.visitInsn(ICONST_0);
        mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/common/CommonProxy", "sparkle", "(FFFI)V", false);
        Label l30 = new Label();
        mv.visitLabel(l30);
        mv.visitLineNumber(86, l30);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_1);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        Label l31 = new Label();
        mv.visitLabel(l31);
        mv.visitLineNumber(87, l31);
        Label l32 = new Label();
        mv.visitJumpInsn(GOTO, l32);
        mv.visitLabel(l28);
        mv.visitFrame(Opcodes.F_APPEND, 2, new Object[] { Opcodes.INTEGER, Opcodes.INTEGER }, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_0);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        Label l33 = new Label();
        mv.visitLabel(l33);
        mv.visitLineNumber(88, l33);
        mv.visitJumpInsn(GOTO, l32);
        mv.visitLabel(l22);
        mv.visitFrame(Opcodes.F_CHOP, 2, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitInsn(ICONST_0);
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "tookLastTick", "Z");
        mv.visitLabel(l32);
        mv.visitLineNumber(90, l32);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        Label l34 = new Label();
        mv.visitJumpInsn(IFNONNULL, l34);
        mv.visitInsn(ICONST_0);
        Label l35 = new Label();
        mv.visitJumpInsn(GOTO, l35);
        mv.visitLabel(l34);
        mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer" });
        mv.visitVarInsn(ALOAD, 0);
        mv.visitFieldInsn(
                GETFIELD,
                "thaumic/tinkerer/common/block/tile/TileRepairer",
                "inventorySlots",
                "[L" + aItemStack + ";");
        mv.visitInsn(ICONST_0);
        mv.visitInsn(AALOAD);
        mv.visitMethodInsn(INVOKEVIRTUAL, "" + aItemStack + "", "" + aGetItemDamage + "", "()I", false);
        mv.visitLabel(l35);
        mv.visitFrame(
                Opcodes.F_FULL,
                1,
                new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer" },
                2,
                new Object[] { "thaumic/tinkerer/common/block/tile/TileRepairer", Opcodes.INTEGER });
        mv.visitFieldInsn(PUTFIELD, "thaumic/tinkerer/common/block/tile/TileRepairer", "dmgLastTick", "I");
        mv.visitLabel(l1);
        mv.visitLineNumber(92, l1);
        mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
        mv.visitInsn(RETURN);
        Label l36 = new Label();
        mv.visitLabel(l36);
        mv.visitLocalVariable("this", "Lthaumic/tinkerer/common/block/tile/TileRepairer;", null, l0, l36, 0);
        mv.visitLocalVariable("dmg", "I", null, l7, l3, 1);
        mv.visitLocalVariable("essentia", "I", null, l10, l18, 2);
        mv.visitLocalVariable("essentia", "I", null, l24, l33, 1);
        mv.visitLocalVariable("dmg", "I", null, l25, l33, 2);
        mv.visitMaxs(9, 3);
        mv.visitEnd();
        didInject = true;
        Preloader_Logger.LOG(
                "Thaumic Tinkerer RepairItem Patch",
                Level.INFO,
                "Method injection complete. " + (obfuscated ? "Obfuscated" : "Non-Obfuscated"));
        return didInject;
    }

    public class AddAdapter extends ClassVisitor {

        public AddAdapter(ClassVisitor cv) {
            super(ASM5, cv);
            this.cv = cv;
        }

        private final String[] aMethodsToStrip = new String[] { "updateEntity", "func_145845_h" };
        public boolean isObfuscated = false;

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {

            MethodVisitor methodVisitor;
            boolean found = false;

            for (String s : aMethodsToStrip) {
                if (name.equals(s)) {
                    found = true;
                    if (s.equals(aMethodsToStrip[1])) {
                        isObfuscated = true;
                    }
                    break;
                }
            }
            if (!found) {
                methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
            } else {
                methodVisitor = null;
            }

            if (found) {
                Preloader_Logger
                        .LOG("Thaumic Tinkerer RepairItem Patch", Level.INFO, "Found method " + name + ", removing.");
            }
            return methodVisitor;
        }
    }
}
