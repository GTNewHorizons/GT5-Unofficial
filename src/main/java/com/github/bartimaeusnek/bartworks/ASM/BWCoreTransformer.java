/*
 * Copyright (c) 2018-2020 bartimaeusnek
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.bartimaeusnek.bartworks.ASM;

import static org.objectweb.asm.Opcodes.*;

import java.util.Arrays;
import java.util.List;
import net.minecraft.launchwrapper.IClassTransformer;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.*;

public class BWCoreTransformer implements IClassTransformer {
    public static final String[] DESCRIPTIONFORCONFIG = {
        "REMOVING RAIN FROM LAST MILLENIUM (EXU)",
        "REMVOING CREATURES FROM LAST MILLENIUM (EXU)",
        "PATCHING GLOBAL RENDERER FOR USE WITH MY GALACTIC DIMS",
        "PATCHING THAUMCRAFT WAND PEDESTAL TO PREVENT VIS DUPLICATION",
        "PLACING MY GLASS-BLOCK RUNNABLE INTO THE GT_API",
        "DUCTTAPING RWG WORLDEN FAILS",
        "PATCHING CRAFTING MANAGER FOR CACHING RECIPES"
        // "REMOVING 12% BONUS OUTPUTS FROM GT++ SIFTER"
    };
    public static final String[] CLASSESBEEINGTRANSFORMED = {
        "com.rwtema.extrautils.worldgen.endoftime.WorldProviderEndOfTime",
        "com.rwtema.extrautils.worldgen.endoftime.ChunkProviderEndOfTime",
        "net.minecraft.client.renderer.RenderGlobal",
        "thaumcraft.common.tiles.TileWandPedestal",
        "gregtech.GT_Mod",
        "rwg.world.ChunkGeneratorRealistic",
        "net.minecraft.item.crafting.CraftingManager"
        // "gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing.GregtechMetaTileEntity_IndustrialSifter"
    };
    static boolean obfs;

    public static boolean[] shouldTransform = new boolean[BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length];

    /**
     * Made by DarkShaddow44
     */
    private static MethodNode transformThaumcraftWandPedestal(MethodNode method) {
        InsnList nu = new InsnList();
        for (int j = 0; j < method.instructions.size(); j++) {
            AbstractInsnNode instruction = method.instructions.get(j);
            nu.add(instruction);
            if (instruction.getOpcode() == INVOKEVIRTUAL) {
                MethodInsnNode invokevirtual = (MethodInsnNode) instruction;
                if (invokevirtual.name.equals("addVis")) {
                    AbstractInsnNode beginning = method.instructions.get(j - 7);
                    LabelNode label = new LabelNode();
                    nu.insertBefore(beginning, new VarInsnNode(ALOAD, 0));
                    nu.insertBefore(
                            beginning,
                            new FieldInsnNode(
                                    GETFIELD,
                                    "thaumcraft/common/tiles/TileWandPedestal",
                                    obfs ? "field_145850_b" : "worldObj",
                                    "Lnet/minecraft/world/World;"));
                    nu.insertBefore(
                            beginning,
                            new FieldInsnNode(
                                    GETFIELD, "net/minecraft/world/World", obfs ? "field_72995_K" : "isRemote", "Z"));
                    nu.insertBefore(beginning, new JumpInsnNode(IFNE, label));
                    nu.add(new InsnNode(POP));
                    nu.add(label);
                    j++; // Skip actual Pop
                }
            }
        }
        method.instructions = nu;
        return method;
    }

    public static byte[] transform(int id, byte[] basicClass) {
        if (!BWCoreTransformer.shouldTransform[id]) {
            BWCore.BWCORE_LOG.info(
                    "Patch: " + BWCoreTransformer.DESCRIPTIONFORCONFIG[id] + " is disabled, will not patch!");
            return basicClass;
        }

        if (id < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length) {
            BWCore.BWCORE_LOG.info(BWCoreTransformer.DESCRIPTIONFORCONFIG[id]);
            ClassReader classReader = new ClassReader(basicClass);
            ClassNode classNode = new ClassNode();
            classReader.accept(classNode, ClassReader.SKIP_FRAMES);
            List<MethodNode> methods = classNode.methods;
            scase:
            switch (id) {
                case 0: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "canDoRainSnowIce";

                    String dsc_deObfs = "(Lnet/minecraft/world/chunk/Chunk;)Z";
                    String dsc_Obfs = "(Lapx;)Z";
                    for (int i = 0; i < methods.size(); i++) {
                        if (methods.get(i).name.equalsIgnoreCase(name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found " + name_deObfs + "! Removing!");
                            methods.remove(i);
                            break;
                        }
                    }
                    BWCore.BWCORE_LOG.info("Creating new " + name_deObfs + "!");
                    MethodNode nu = new MethodNode(
                            ACC_PUBLIC, name_deObfs, /*obfs ? dsc_Obfs :*/ dsc_deObfs, null, new String[0]);
                    InsnList insnList = new InsnList();
                    insnList.add(new InsnNode(ICONST_0));
                    insnList.add(new InsnNode(IRETURN));
                    nu.instructions = insnList;
                    nu.maxLocals = 1;
                    nu.maxStack = 1;
                    methods.add(nu);
                    break;
                }
                case 1: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "getPossibleCreatures";
                    String name_src = "func_73155_a";
                    String name_Obfs = "a";
                    String dsc_deObfs = "(Lnet/minecraft/entity/EnumCreatureType;III)Ljava/util/List;";
                    String dsc_Obfs = "(Lsx;III)Ljava/util/List;";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src)
                                && ASMUtils.isCorrectMethod(methods.get(i), dsc_deObfs, dsc_Obfs)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            MethodNode toPatch = methods.get(i);
                            InsnList insnList = new InsnList();
                            insnList.add(new InsnNode(ACONST_NULL));
                            insnList.add(new InsnNode(ARETURN));
                            toPatch.instructions = insnList;
                            toPatch.maxStack = 1;
                            toPatch.maxLocals = 5;
                            methods.set(i, toPatch);
                            break scase;
                        }
                    }
                }
                case 2: {
                    String name_deObfs = "renderSky";
                    String name_src = "func_72714_a";
                    String name_Obfs = "a";
                    String dsc_universal = "(F)V";
                    String field_deObfs = "locationSunPng";
                    String field_src = "field_110928_i";
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    for (MethodNode toPatch : methods) {
                        if (ASMUtils.isCorrectMethod(toPatch, name_deObfs, name_Obfs, name_src)
                                && ASMUtils.isCorrectMethod(toPatch, dsc_universal)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            InsnList nu = new InsnList();
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};

                            String theWorld_src = "field_72769_h";
                            String renderEngine_src = "field_72770_i";
                            String provider_src = "field_73011_w";
                            String bindTexture_src = "func_110577_a";
                            String nameFieldToPatch;

                            for (int j = 0; j < toPatch.instructions.size(); j++) {
                                if (toPatch.instructions.get(j) instanceof FieldInsnNode
                                        && toPatch.instructions.get(j).getOpcode() == GETSTATIC
                                        && !(nameFieldToPatch = ASMUtils.matchAny(
                                                        ((FieldInsnNode) toPatch.instructions.get(j)).name,
                                                        field_deObfs,
                                                        field_src))
                                                .isEmpty()) {
                                    boolean useSrc = nameFieldToPatch.equals(field_src);
                                    if (useSrc)
                                        BWCore.BWCORE_LOG.info(
                                                "Found either Optifine or Fastcraft... this patch was annoying to make compatible to them...");

                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(
                                            GETFIELD,
                                            "net/minecraft/client/renderer/RenderGlobal",
                                            useSrc ? theWorld_src : "theWorld",
                                            "Lnet/minecraft/client/multiplayer/WorldClient;"));
                                    nu.add(new FieldInsnNode(
                                            GETFIELD,
                                            "net/minecraft/client/multiplayer/WorldClient",
                                            useSrc ? provider_src : "provider",
                                            "Lnet/minecraft/world/WorldProvider;"));
                                    nu.add(
                                            new TypeInsnNode(
                                                    INSTANCEOF,
                                                    "com/github/bartimaeusnek/crossmod/galacticraft/planets/AbstractWorldProviderSpace"));
                                    nu.add(new JumpInsnNode(IFEQ, LabelNodes[0]));
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(
                                            GETFIELD,
                                            "net/minecraft/client/renderer/RenderGlobal",
                                            useSrc ? renderEngine_src : "renderEngine",
                                            "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(
                                            GETSTATIC,
                                            "com/github/bartimaeusnek/crossmod/galacticraft/planets/ross128b/SkyProviderRoss128b",
                                            "sunTex",
                                            "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(
                                            INVOKEVIRTUAL,
                                            "net/minecraft/client/renderer/texture/TextureManager",
                                            useSrc ? bindTexture_src : "bindTexture",
                                            "(Lnet/minecraft/util/ResourceLocation;)V",
                                            false));
                                    nu.add(new JumpInsnNode(GOTO, LabelNodes[1]));
                                    nu.add(LabelNodes[0]);
                                    nu.add(new VarInsnNode(ALOAD, 0));
                                    nu.add(new FieldInsnNode(
                                            GETFIELD,
                                            "net/minecraft/client/renderer/RenderGlobal",
                                            useSrc ? renderEngine_src : "renderEngine",
                                            "Lnet/minecraft/client/renderer/texture/TextureManager;"));
                                    nu.add(new FieldInsnNode(
                                            GETSTATIC,
                                            "net/minecraft/client/renderer/RenderGlobal",
                                            useSrc ? field_src : "locationSunPng",
                                            "Lnet/minecraft/util/ResourceLocation;"));
                                    nu.add(new MethodInsnNode(
                                            INVOKEVIRTUAL,
                                            "net/minecraft/client/renderer/texture/TextureManager",
                                            useSrc ? bindTexture_src : "bindTexture",
                                            "(Lnet/minecraft/util/ResourceLocation;)V",
                                            false));
                                    nu.add(LabelNodes[1]);
                                    j++;

                                } else {
                                    if (j < toPatch.instructions.size() - 2) {
                                        if (toPatch.instructions.get(j + 2) instanceof FieldInsnNode
                                                && toPatch.instructions
                                                                .get(j + 2)
                                                                .getOpcode()
                                                        == GETSTATIC
                                                && !ASMUtils.matchAny(
                                                                ((FieldInsnNode) toPatch.instructions.get(j + 2)).name,
                                                                field_deObfs,
                                                                field_src)
                                                        .isEmpty()) continue;
                                        if (toPatch.instructions.get(j + 1) instanceof FieldInsnNode
                                                && toPatch.instructions
                                                                .get(j + 1)
                                                                .getOpcode()
                                                        == GETSTATIC
                                                && !ASMUtils.matchAny(
                                                                ((FieldInsnNode) toPatch.instructions.get(j + 1)).name,
                                                                field_deObfs,
                                                                field_src)
                                                        .isEmpty()) continue;
                                    }
                                    nu.add(toPatch.instructions.get(j));
                                }
                            }
                            toPatch.instructions = nu;
                            break scase;
                        }
                    }
                }
                case 3: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "updateEntity";
                    String name_src = "func_145845_h";
                    String name_Obfs = "h";
                    String dsc_universal = "()V";

                    for (int i = 0; i < methods.size(); i++) {
                        if (ASMUtils.isCorrectMethod(methods.get(i), name_deObfs, name_Obfs, name_src)
                                && ASMUtils.isCorrectMethod(methods.get(i), dsc_universal, dsc_universal)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            methods.set(i, BWCoreTransformer.transformThaumcraftWandPedestal(methods.get(i)));
                            break scase;
                        }
                    }
                }
                case 4: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "<clinit>";
                    for (MethodNode toPatch : methods) {
                        if (ASMUtils.isCorrectMethod(toPatch, name_deObfs) && (toPatch.access & ACC_STATIC) != 0) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            InsnList nu = new InsnList();
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};
                            for (int j = 0; j < 2; j++) {
                                nu.add(toPatch.instructions.get(j));
                            }
                            nu.add(new FieldInsnNode(
                                    GETSTATIC, "gregtech/api/GregTech_API", "sBeforeGTPreload", "Ljava/util/List;"));
                            nu.add(new TypeInsnNode(
                                    NEW, "com/github/bartimaeusnek/bartworks/common/loaders/BeforeGTPreload"));
                            nu.add(new InsnNode(DUP));
                            nu.add(new MethodInsnNode(
                                    INVOKESPECIAL,
                                    "com/github/bartimaeusnek/bartworks/common/loaders/BeforeGTPreload",
                                    "<init>",
                                    "()V",
                                    false));
                            nu.add(new MethodInsnNode(
                                    INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true));
                            nu.add(new InsnNode(POP));
                            for (int j = 2; j < toPatch.instructions.size(); j++) {
                                nu.add(toPatch.instructions.get(j));
                            }
                            toPatch.instructions = nu;
                            break scase;
                        }
                    }
                }
                case 5: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "getNewNoise";
                    for (MethodNode toPatch : methods) {
                        if (ASMUtils.isCorrectMethod(toPatch, name_deObfs)) {
                            BWCore.BWCORE_LOG.info("Found " + (name_deObfs) + "! Patching!");
                            LabelNode[] LabelNodes = {new LabelNode(), new LabelNode()};
                            InsnList nu = new InsnList();
                            // if (x < -28675) x %= -28675;
                            nu.add(new VarInsnNode(ILOAD, 2));
                            nu.add(new IntInsnNode(SIPUSH, -28675));
                            nu.add(new JumpInsnNode(IF_ICMPGE, LabelNodes[0]));
                            nu.add(new VarInsnNode(ILOAD, 2));
                            nu.add(new LdcInsnNode(-28675));
                            nu.add(new InsnNode(IREM));
                            nu.add(new VarInsnNode(ISTORE, 2));
                            nu.add(LabelNodes[0]);
                            // if (y < -28675) y %= -28675;
                            nu.add(new VarInsnNode(ILOAD, 3));
                            nu.add(new IntInsnNode(SIPUSH, -28675));
                            nu.add(new JumpInsnNode(IF_ICMPGE, LabelNodes[1]));
                            nu.add(new VarInsnNode(ILOAD, 3));
                            nu.add(new LdcInsnNode(-28675));
                            nu.add(new InsnNode(IREM));
                            nu.add(new VarInsnNode(ISTORE, 3));
                            nu.add(LabelNodes[1]);

                            for (int j = 1; j < toPatch.instructions.size(); j++) {
                                nu.add(toPatch.instructions.get(j));
                            }

                            toPatch.instructions = nu;
                            break scase;
                        }
                    }
                }
                case 6: {
                    BWCore.BWCORE_LOG.info("Could find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    String name_deObfs = "findMatchingRecipe";
                    String name_Obfs = "a";
                    String name_src = "func_82787_a";
                    for (MethodNode toPatch : methods) {
                        if (ASMUtils.isCorrectMethod(toPatch, name_deObfs, name_Obfs, name_src)) {
                            toPatch.instructions = new InsnList();
                            toPatch.instructions.add(new VarInsnNode(ALOAD, 1));
                            toPatch.instructions.add(new VarInsnNode(ALOAD, 2));
                            toPatch.instructions.add(new MethodInsnNode(
                                    INVOKESTATIC,
                                    "com/github/bartimaeusnek/bartworks/ASM/BWCoreStaticReplacementMethodes",
                                    "findCachedMatchingRecipe",
                                    "(Lnet/minecraft/inventory/InventoryCrafting;Lnet/minecraft/world/World;)Lnet/minecraft/item/ItemStack;",
                                    false));
                            toPatch.instructions.add(new InsnNode(ARETURN));
                            toPatch.localVariables.clear();
                            toPatch.maxStack = 2;
                            toPatch.maxLocals = 3;
                            break scase;
                        }
                    }
                }

                    //                case 6: {
                    //                    BWCore.BWCORE_LOG.info("Could find: " +
                    // BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    //                    ((IntInsnNode) methods.get(11).instructions.get(10)).operand = 10000;
                    //                    break scase;
                    //                }
                default: {
                    BWCore.BWCORE_LOG.info("Could not find: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
                    return basicClass;
                }
            }

            classNode.methods = methods;
            ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
            classNode.accept(classWriter);
            byte[] ret = classWriter.toByteArray();
            if (Arrays.hashCode(basicClass) == Arrays.hashCode(ret))
                BWCore.BWCORE_LOG.warn("Could not patch: " + BWCoreTransformer.CLASSESBEEINGTRANSFORMED[id]);
            return ret;
        }
        return basicClass;
    }

    @Override
    public byte[] transform(String name, String transformedName, byte[] basicClass) {
        for (int i = 0; i < BWCoreTransformer.CLASSESBEEINGTRANSFORMED.length; i++) {
            if (name.equalsIgnoreCase(BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i])
                    || transformedName.equalsIgnoreCase(BWCoreTransformer.CLASSESBEEINGTRANSFORMED[i]))
                return BWCoreTransformer.transform(i, basicClass);
        }
        return basicClass;
    }
}
