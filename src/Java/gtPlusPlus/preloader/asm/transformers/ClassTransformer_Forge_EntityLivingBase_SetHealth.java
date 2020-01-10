package gtPlusPlus.preloader.asm.transformers;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import cpw.mods.fml.relauncher.FMLRelaunchLog;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.DamageSource;

public class ClassTransformer_Forge_EntityLivingBase_SetHealth {	
	
	private boolean isValid = false;
	private ClassReader mReader = null;
	private ClassWriter mWriter = null;	
	private boolean didPatch = false;

	public ClassTransformer_Forge_EntityLivingBase_SetHealth(String aClassName, byte[] basicClass) {
		if (basicClass == null) {
			return;
		}
		
		ClassReader reader = new ClassReader(basicClass);
		ClassWriter writer = new ClassWriter(reader, ClassWriter.COMPUTE_MAXS);
		ClassVisitor visitor = writer;
		SetHealthVisitor aVisitor = new SetHealthVisitor(visitor);
		visitor = aVisitor;
		reader.accept(visitor, 0);		
		if (reader != null && writer != null) {
			isValid = true;
		}
		else {
			isValid = false;
		}
		mReader = reader;
		mWriter = writer;
		didPatch = aVisitor.didPatchInternal;
	}

	public boolean isValidTransformer() {
		return isValid;
	}

	public ClassReader getReader() {
		return mReader;
	}

	public ClassWriter getWriter() {
		return mWriter;
	}


	public static class SetHealthVisitor extends ClassVisitor {
		private String clsName = null;
		private boolean didPatchInternal = false;
		private static final String callbackOwner = org.objectweb.asm.Type.getInternalName(SetHealthVisitor.class);

		private SetHealthVisitor(ClassVisitor cv) {
			super(Opcodes.ASM5, cv);
		}

		@Override
		public void visit(
				int version, int access, String name, String signature, String superName, String[] interfaces
		) {
			super.visit(
					version, access, name, signature, superName, interfaces
			);
			this.clsName = name;
		}

		@Override
		public MethodVisitor visitMethod(
				int mAccess, final String mName, final String mDesc, String mSignature, String[] mExceptions
		) {
			final boolean warn = !(clsName.equals(
					"net/minecraft/entity/EntityLivingBase"
			));			

			return new MethodVisitor(
					Opcodes.ASM5, super.visitMethod(
							mAccess, mName, mDesc, mSignature, mExceptions
					)
			) {
				@Override
				public void visitMethodInsn(
						int opcode, String owner, String name, String desc, boolean isIntf
				) {
					if (owner.equals(
							"net/minecraft/entity/EntityLivingBase"
					) && name.equals("setHealth") && desc.equals("(F)V")) {
						if (warn) {
							FMLRelaunchLog.warning(
									"============================================================="
							);
							FMLRelaunchLog.warning(
									"MOD HAS DIRECT REFERENCE Entity.setHealth() THIS IS NOT ALLOWED!"
							);
							FMLRelaunchLog.warning(
									"Offendor: %s.%s%s", SetHealthVisitor.this.clsName, mName, mDesc
							);
							FMLRelaunchLog.warning(
									"Use EntityLiving.attackEntityFrom(DamageSource, damageDealt) instead"
							);
							FMLRelaunchLog.warning(
									"============================================================="
							);
						}
						didPatchInternal = true;
                        //opcode = Opcodes.INVOKESTATIC; // Set it static
						//owner = SetHealthVisitor.callbackOwner; 
						//name = "setHealthGeneric"; // Replace the method name
						//desc = "(Lnet/minecraft/entity/EntityLivingBase;F)V"; // Replace the method desc
						
					}
					super.visitMethodInsn(opcode, owner, name, desc, isIntf);
				}
			};
		}
		

	    private final static DamageSource mGenericDamageSource = new DamageSource("gtpp.generic");
		
		public static void setHealthGeneric(EntityLivingBase aEntity, float aValue) {
			aEntity.attackEntityFrom(mGenericDamageSource, aValue);
		}

	}


	public boolean didPatchClass() {
		return didPatch;
	}

}
