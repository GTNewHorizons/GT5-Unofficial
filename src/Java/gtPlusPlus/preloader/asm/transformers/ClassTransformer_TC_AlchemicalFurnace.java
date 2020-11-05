package gtPlusPlus.preloader.asm.transformers;

import static org.objectweb.asm.Opcodes.*;

import org.apache.logging.log4j.Level;
import org.objectweb.asm.*;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import gtPlusPlus.preloader.Preloader_Logger;

public class ClassTransformer_TC_AlchemicalFurnace {

	private final boolean isValid;
	private final ClassReader reader;
	private final ClassWriter writer;

	public ClassTransformer_TC_AlchemicalFurnace(byte[] basicClass) {

		ClassReader aTempReader = null;
		ClassWriter aTempWriter = null;

		aTempReader = new ClassReader(basicClass);
		aTempWriter = new ClassWriter(aTempReader, ClassWriter.COMPUTE_FRAMES);
		localClassVisitor aTempMethodRemover = new localClassVisitor(aTempWriter);
		aTempReader.accept(aTempMethodRemover, 0);
		boolean wasMethodObfuscated = aTempMethodRemover.getObfuscatedRemoval();

		if (aTempReader != null && aTempWriter != null) {
			isValid = true;
		} else {
			isValid = false;
		}

		if (isValid) {
			Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Attempting Field Injection.");
			boolean fields = addField(aTempWriter);
			Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Success? "+fields);
			Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Attempting Method Injection.");
			boolean methods = injectMethod(wasMethodObfuscated, aTempWriter);
			Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Success? "+methods);
		}

		Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Valid patch? " + isValid + ".");
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

	// Add a field to hold the smelting cache
	public boolean addField(ClassWriter cv) {
		Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Field injection complete.");		
		FieldVisitor fv = cv.visitField(ACC_PRIVATE, "smeltingCache", "LgtPlusPlus/api/objects/minecraft/ThaumcraftDataStack;", null, null);
		if (fv != null) {
			fv.visitEnd();
			return true;
		}
		return false;
	}	

	public boolean injectMethod(boolean wasMethodObfuscated, ClassWriter cw) {
		MethodVisitor mv;
		boolean didInject = false;

		// Get the right string to use for the environment we are in.
		String aItemStack = "net/minecraft/item/ItemStack";
		String aItemStack_Obf = "add";		
		String aCorrectString = wasMethodObfuscated ? aItemStack_Obf : aItemStack;

		// thaumcraft/common/tiles/TileAlchemyFurnace
		// Replace the original canSmelt with one that uses the optimized cache
		{
			mv = cw.visitMethod(ACC_PRIVATE, "canSmelt", "()Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(306, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(307, l2);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(309, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "thaumcraft/common/tiles/TileAlchemyFurnace", "getAspectsFromInventoryItem", "()Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(310, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "thaumcraft/common/lib/crafting/ThaumcraftCraftingManager", "getBonusTags", "(L"+aCorrectString+";Lthaumcraft/api/aspects/AspectList;)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(311, l4);
			mv.visitVarInsn(ALOAD, 1);
			Label l5 = new Label();
			mv.visitJumpInsn(IFNULL, l5);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "size", "()I", false);
			mv.visitJumpInsn(IFEQ, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitLineNumber(312, l6);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "visSize", "()I", false);
			mv.visitVarInsn(ISTORE, 2);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(313, l7);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "maxVis", "I");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "vis", "I");
			mv.visitInsn(ISUB);
			Label l8 = new Label();
			mv.visitJumpInsn(IF_ICMPLE, l8);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(314, l9);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l8);
			mv.visitLineNumber(316, l8);
			mv.visitFrame(Opcodes.F_APPEND,2, new Object[] {"thaumcraft/api/aspects/AspectList", Opcodes.INTEGER}, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ILOAD, 2);
			mv.visitIntInsn(BIPUSH, 10);
			mv.visitInsn(IMUL);
			mv.visitInsn(I2F);
			mv.visitInsn(FCONST_1);
			mv.visitLdcInsn(new Float("0.125"));
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "bellows", "I");
			mv.visitInsn(I2F);
			mv.visitInsn(FMUL);
			mv.visitInsn(FSUB);
			mv.visitInsn(FMUL);
			mv.visitInsn(F2I);
			mv.visitFieldInsn(PUTFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "smeltTime", "I");
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(317, l10);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l5);
			mv.visitLineNumber(320, l5);
			mv.visitFrame(Opcodes.F_CHOP,1, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitInsn(IRETURN);
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLocalVariable("this", "Lthaumcraft/common/tiles/TileAlchemyFurnace;", null, l0, l11, 0);
			mv.visitLocalVariable("al", "Lthaumcraft/api/aspects/AspectList;", null, l3, l11, 1);
			mv.visitLocalVariable("vs", "I", null, l7, l5, 2);
			mv.visitMaxs(5, 3);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC, "smeltItem", "()V", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(330, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "thaumcraft/common/tiles/TileAlchemyFurnace", "canSmelt", "()Z", false);
			Label l1 = new Label();
			mv.visitJumpInsn(IFEQ, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(331, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "thaumcraft/common/tiles/TileAlchemyFurnace", "getAspectsFromInventoryItem", "()Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(332, l3);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKESTATIC, "thaumcraft/common/lib/crafting/ThaumcraftCraftingManager", "getBonusTags", "(L"+aCorrectString+";Lthaumcraft/api/aspects/AspectList;)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(334, l4);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "getAspects", "()[Lthaumcraft/api/aspects/Aspect;", false);
			mv.visitInsn(DUP);
			mv.visitVarInsn(ASTORE, 5);
			mv.visitInsn(ARRAYLENGTH);
			mv.visitVarInsn(ISTORE, 4);
			mv.visitInsn(ICONST_0);
			mv.visitVarInsn(ISTORE, 3);
			Label l5 = new Label();
			mv.visitJumpInsn(GOTO, l5);
			Label l6 = new Label();
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_FULL, 6, new Object[] {"thaumcraft/common/tiles/TileAlchemyFurnace", "thaumcraft/api/aspects/AspectList", Opcodes.TOP, Opcodes.INTEGER, Opcodes.INTEGER, "[Lthaumcraft/api/aspects/Aspect;"}, 0, new Object[] {});
			mv.visitVarInsn(ALOAD, 5);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitInsn(AALOAD);
			mv.visitVarInsn(ASTORE, 2);
			Label l7 = new Label();
			mv.visitLabel(l7);
			mv.visitLineNumber(335, l7);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "aspects", "Lthaumcraft/api/aspects/AspectList;");
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "getAmount", "(Lthaumcraft/api/aspects/Aspect;)I", false);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "add", "(Lthaumcraft/api/aspects/Aspect;I)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitInsn(POP);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLineNumber(334, l8);
			mv.visitIincInsn(3, 1);
			mv.visitLabel(l5);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 3);
			mv.visitVarInsn(ILOAD, 4);
			mv.visitJumpInsn(IF_ICMPLT, l6);
			Label l9 = new Label();
			mv.visitLabel(l9);
			mv.visitLineNumber(338, l9);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "aspects", "Lthaumcraft/api/aspects/AspectList;");
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "visSize", "()I", false);
			mv.visitFieldInsn(PUTFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "vis", "I");
			Label l10 = new Label();
			mv.visitLabel(l10);
			mv.visitLineNumber(339, l10);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitInsn(DUP);
			mv.visitFieldInsn(GETFIELD, ""+aCorrectString+"", "stackSize", "I");
			mv.visitInsn(ICONST_1);
			mv.visitInsn(ISUB);
			mv.visitFieldInsn(PUTFIELD, ""+aCorrectString+"", "stackSize", "I");
			Label l11 = new Label();
			mv.visitLabel(l11);
			mv.visitLineNumber(340, l11);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitFieldInsn(GETFIELD, ""+aCorrectString+"", "stackSize", "I");
			mv.visitJumpInsn(IFGT, l1);
			Label l12 = new Label();
			mv.visitLabel(l12);
			mv.visitLineNumber(341, l12);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(ACONST_NULL);
			mv.visitInsn(AASTORE);
			mv.visitLabel(l1);
			mv.visitLineNumber(345, l1);
			mv.visitFrame(Opcodes.F_FULL, 1, new Object[] {"thaumcraft/common/tiles/TileAlchemyFurnace"}, 0, new Object[] {});
			mv.visitInsn(RETURN);
			Label l13 = new Label();
			mv.visitLabel(l13);
			mv.visitLocalVariable("this", "Lthaumcraft/common/tiles/TileAlchemyFurnace;", null, l0, l13, 0);
			mv.visitLocalVariable("al", "Lthaumcraft/api/aspects/AspectList;", null, l3, l1, 1);
			mv.visitLocalVariable("a", "Lthaumcraft/api/aspects/Aspect;", null, l7, l8, 2);
			mv.visitMaxs(4, 6);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PUBLIC, "isItemValidForSlot", "(IL"+aCorrectString+";)Z", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(362, l0);
			mv.visitVarInsn(ILOAD, 1);
			Label l1 = new Label();
			mv.visitJumpInsn(IFNE, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(363, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitMethodInsn(INVOKESPECIAL, "thaumcraft/common/tiles/TileAlchemyFurnace", "getAspectsFromInventoryItem", "()Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(364, l3);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKESTATIC, "thaumcraft/common/lib/crafting/ThaumcraftCraftingManager", "getBonusTags", "(L"+aCorrectString+";Lthaumcraft/api/aspects/AspectList;)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 3);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLineNumber(365, l4);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitJumpInsn(IFNULL, l1);
			mv.visitVarInsn(ALOAD, 3);
			mv.visitMethodInsn(INVOKEVIRTUAL, "thaumcraft/api/aspects/AspectList", "size", "()I", false);
			mv.visitJumpInsn(IFLE, l1);
			Label l5 = new Label();
			mv.visitLabel(l5);
			mv.visitLineNumber(366, l5);
			mv.visitInsn(ICONST_1);
			mv.visitInsn(IRETURN);
			mv.visitLabel(l1);
			mv.visitLineNumber(370, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ILOAD, 1);
			mv.visitInsn(ICONST_1);
			Label l6 = new Label();
			mv.visitJumpInsn(IF_ICMPNE, l6);
			mv.visitVarInsn(ALOAD, 2);
			mv.visitMethodInsn(INVOKESTATIC, "thaumcraft/common/tiles/TileAlchemyFurnace", "isItemFuel", "(L"+aCorrectString+";)Z", false);
			Label l7 = new Label();
			mv.visitJumpInsn(GOTO, l7);
			mv.visitLabel(l6);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitInsn(ICONST_0);
			mv.visitLabel(l7);
			mv.visitFrame(Opcodes.F_SAME1, 0, null, 1, new Object[] {Opcodes.INTEGER});
			mv.visitInsn(IRETURN);
			Label l8 = new Label();
			mv.visitLabel(l8);
			mv.visitLocalVariable("this", "Lthaumcraft/common/tiles/TileAlchemyFurnace;", null, l0, l8, 0);
			mv.visitLocalVariable("par1", "I", null, l0, l8, 1);
			mv.visitLocalVariable("par2ItemStack", "L"+aCorrectString+";", null, l0, l8, 2);
			mv.visitLocalVariable("al", "Lthaumcraft/api/aspects/AspectList;", null, l3, l1, 3);
			mv.visitMaxs(2, 4);
			mv.visitEnd();
		}

		{
			mv = cw.visitMethod(ACC_PRIVATE, "getAspectsFromInventoryItem", "()Lthaumcraft/api/aspects/AspectList;", null, null);
			mv.visitCode();
			Label l0 = new Label();
			mv.visitLabel(l0);
			mv.visitLineNumber(416, l0);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "smeltingCache", "LgtPlusPlus/api/objects/minecraft/ThaumcraftDataStack;");
			Label l1 = new Label();
			mv.visitJumpInsn(IFNONNULL, l1);
			Label l2 = new Label();
			mv.visitLabel(l2);
			mv.visitLineNumber(417, l2);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitTypeInsn(NEW, "gtPlusPlus/api/objects/minecraft/ThaumcraftDataStack");
			mv.visitInsn(DUP);
			mv.visitIntInsn(BIPUSH, 20);
			mv.visitMethodInsn(INVOKESPECIAL, "gtPlusPlus/api/objects/minecraft/ThaumcraftDataStack", "<init>", "(I)V", false);
			mv.visitFieldInsn(PUTFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "smeltingCache", "LgtPlusPlus/api/objects/minecraft/ThaumcraftDataStack;");
			mv.visitLabel(l1);
			mv.visitLineNumber(419, l1);
			mv.visitFrame(Opcodes.F_SAME, 0, null, 0, null);
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "smeltingCache", "LgtPlusPlus/api/objects/minecraft/ThaumcraftDataStack;");
			mv.visitVarInsn(ALOAD, 0);
			mv.visitFieldInsn(GETFIELD, "thaumcraft/common/tiles/TileAlchemyFurnace", "furnaceItemStacks", "[L"+aCorrectString+";");
			mv.visitInsn(ICONST_0);
			mv.visitInsn(AALOAD);
			mv.visitMethodInsn(INVOKEVIRTUAL, "gtPlusPlus/api/objects/minecraft/ThaumcraftDataStack", "getAspectsForStack", "(L"+aCorrectString+";)Lthaumcraft/api/aspects/AspectList;", false);
			mv.visitVarInsn(ASTORE, 1);
			Label l3 = new Label();
			mv.visitLabel(l3);
			mv.visitLineNumber(420, l3);
			mv.visitVarInsn(ALOAD, 1);
			mv.visitInsn(ARETURN);
			Label l4 = new Label();
			mv.visitLabel(l4);
			mv.visitLocalVariable("this", "Lthaumcraft/common/tiles/TileAlchemyFurnace;", null, l0, l4, 0);
			mv.visitLocalVariable("al", "Lthaumcraft/api/aspects/AspectList;", null, l3, l4, 1);
			mv.visitMaxs(4, 2);
			mv.visitEnd();
		}

		didInject = true;

		Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Method injection complete.");
		return didInject;
	}

	public final class localClassVisitor extends ClassVisitor {

		boolean obfuscated = false;

		public localClassVisitor(ClassVisitor cv) {
			super(ASM5, cv);
		}

		public boolean getObfuscatedRemoval() {
			return obfuscated;
		}

		@Override
		public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
			//Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found Field "+name+" | "+desc);				
			return cv.visitField(access, name, desc, signature, value); 
		}

		@Override
		public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
			MethodVisitor methodVisitor;
			String aDesc1_1 = "()Z";
			String aDesc1_2 = "()V";
			String aDesc1_3 = "(ILnet/minecraft/item/ItemStack;)Z";
			String aDesc2_3 = "(ILadd;)Z";
			if (name.equals("canSmelt")) {	
				if (desc.equals(aDesc1_1)) {
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found canSmelt to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					obfuscated = false;
					methodVisitor = null;					
				}
				else {
					Preloader_Logger.INFO("Found canSmelt: "+desc+" | "+signature);
					if (desc.toLowerCase().contains("item")) {
						obfuscated = false;		
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					}
					else {
						obfuscated = true;	
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is obfuscated.");					
					}	
					methodVisitor = null;				
				}
			}
			else if (name.equals("smeltItem")) {	
				if (desc.equals(aDesc1_2)) {
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found smeltItem to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					obfuscated = false;
					methodVisitor = null;					
				}
				else {
					Preloader_Logger.INFO("Found smeltItem: "+desc+" | "+signature);
					if (desc.toLowerCase().contains("item")) {
						obfuscated = false;		
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					}
					else {
						obfuscated = true;	
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is obfuscated.");					
					}	
					methodVisitor = null;				
				}
			}
			else if (name.equals("isItemValidForSlot")) {	
				if (desc.equals(aDesc1_3)) {
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found isItemValidForSlot to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					obfuscated = false;
					methodVisitor = null;					
				}
				else if (desc.equals(aDesc2_3)) {
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found isItemValidForSlot to remove: "+desc+" | "+signature);
					Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is obfuscated.");	
					obfuscated = true;
					methodVisitor = null;					
				}
				else {
					Preloader_Logger.INFO("Found isItemValidForSlot: "+desc+" | "+signature);
					if (desc.toLowerCase().contains("item")) {
						obfuscated = false;		
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is not obfuscated.");
					}
					else {
						obfuscated = true;	
						Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Is obfuscated.");					
					}	
					methodVisitor = null;				
				}
			}
			else {
				methodVisitor = super.visitMethod(access, name, desc, signature, exceptions);
			}

			if (methodVisitor == null) {
				Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Found method " + name + ", removing.");
				Preloader_Logger.LOG("TC Alchemy Furnace Patch", Level.INFO, "Descriptor: "+desc+" | "+signature);
			}
			return methodVisitor;
		}
	}

}
