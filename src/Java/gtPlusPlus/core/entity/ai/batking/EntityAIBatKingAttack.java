package gtPlusPlus.core.entity.ai.batking;

import gtPlusPlus.core.entity.monster.EntityBatKing;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IRangedAttackMob;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;

public class EntityAIBatKingAttack extends EntityAIBase {

	private final Ranged mRangedAI;
	private final Melee mMeleeAI;
	private boolean mIsMelee = false;

	/** The Bat King in control of this AI.	 */
	private final EntityBatKing mAttackingEntity;

	/** The PathEntity of our entity. */
	private PathEntity mEntityPathEntity;

	/** The current target of Bat King in control of this AI.	 */
	private EntityLivingBase mEntityTarget;

	private final Class mClassTarget;

	public EntityAIBatKingAttack(EntityAIBatKingAttack aParent, EntityBatKing aAttacker, Class aClassTarget,
			double aMovementSpeed, int someInt, int aMaxRangedAttackTime, float someFloat, boolean aLongMemory) {
		mRangedAI = new Ranged(aParent, aMovementSpeed, someInt, aMaxRangedAttackTime, someFloat);
		mMeleeAI = new Melee(aParent, aClassTarget, aMovementSpeed, aLongMemory);		
		mAttackingEntity = aAttacker;	
		mClassTarget = aClassTarget;
	}

	@Override
	public boolean shouldExecute() {		
		EntityLivingBase entitylivingbase = this.mAttackingEntity.getAttackTarget();
		if (entitylivingbase == null) {
			this.mEntityTarget = null;
			return false;
		} else if (!entitylivingbase.isEntityAlive()) {
			return false;
		} else if (this.mClassTarget != null && !this.mClassTarget.isAssignableFrom(entitylivingbase.getClass())) {
			return false;
		} else {			
			if (this.mIsMelee) {
				return this.mMeleeAI.shouldExecute();
			}
			else {
				this.mEntityTarget = entitylivingbase;
				this.mEntityPathEntity = this.mAttackingEntity.getNavigator().getPathToEntityLiving(entitylivingbase);
				return mRangedAI != null && mMeleeAI != null && hasValidTarget();
			}						
		}
	}
	
	public EntityBatKing getBatKing() {
		return this.mAttackingEntity;
	}
	
	public IRangedAttackMob getBatKingAsIRangedAttackMob() {
		return this.mAttackingEntity;
	}

	public boolean hasValidTarget() {
		return this.mEntityTarget != null;
	}

	public EntityLivingBase getTarget() {
		return mEntityTarget;
	}

	@Override
	public boolean continueExecuting() {
		if (mIsMelee) {
			return mMeleeAI.continueExecuting();
		} else {
			return mRangedAI.continueExecuting();
		}
	}

	@Override
	public boolean isInterruptible() {
		if (mIsMelee) {

		} else {

		}
		return super.isInterruptible();
	}

	@Override
	public void startExecuting() {
		if (mIsMelee) {
			mMeleeAI.startExecuting();
		} else {
			mRangedAI.startExecuting();
		}
	}

	@Override
	public void resetTask() {
		if (mIsMelee) {
			mMeleeAI.resetTask();
		} else {
			mRangedAI.resetTask();
		}
	}

	@Override
	public void updateTask() {
		determineCombatStyle();
		if (mIsMelee) {
			mMeleeAI.updateTask();
		} else {
			mRangedAI.updateTask();
		}
	}
	
	
	private final void determineCombatStyle() {
		if (this.mEntityTarget != null && EntityUtils.getDistance(getBatKing(), mEntityTarget) < 4) {
			this.mIsMelee = true;
		}
		else {
			this.mIsMelee = false;
		}
	}

	private class Ranged {

		private final EntityAIBatKingAttack parentAI;
		
		/**
		 * A decrementing tick that spawns a ranged attack once this value reaches 0. It
		 * is then set back to the maxRangedAttackTime.
		 */
		private int rangedAttackTime;
		private double entityMoveSpeed;
		private int mCooldownTime;
		private int field_96561_g;
		/**
		 * The maximum time the AI has to wait before performing another ranged attack.
		 */
		private int maxRangedAttackTime;
		private float field_96562_i;
		private float field_82642_h;

		public Ranged(EntityAIBatKingAttack aParent, double aMovementSpeed, int someInt,
				int aMaxRangedAttackTime, float someFloat) {
			this.rangedAttackTime = -1;
			this.parentAI = aParent;
			this.entityMoveSpeed = aMovementSpeed;
			this.field_96561_g = someInt;
			this.maxRangedAttackTime = aMaxRangedAttackTime;
			this.field_96562_i = someFloat;
			this.field_82642_h = someFloat * someFloat;
			parentAI.setMutexBits(3);

		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {			
			parentAI.getBatKing().getNavigator().setPath(parentAI.mEntityPathEntity, this.entityMoveSpeed);
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting() {
			return parentAI.shouldExecute() || !parentAI.getBatKing().getNavigator().noPath();
		}

		/**
		 * Resets the task
		 */
		public void resetTask() {
			parentAI.mEntityTarget = null;
			this.mCooldownTime = 0;
			this.rangedAttackTime = -1;
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			double d0 = parentAI.getBatKing().getDistanceSq(parentAI.mEntityTarget.posX, parentAI.mEntityTarget.boundingBox.minY,
					parentAI.mEntityTarget.posZ);
			boolean flag = parentAI.getBatKing().getEntitySenses().canSee(parentAI.mEntityTarget);

			if (flag) {
				++this.mCooldownTime;
			} else {
				this.mCooldownTime = 0;
			}

			if (d0 <= (double) this.field_82642_h && this.mCooldownTime >= 20) {
				parentAI.getBatKing().getNavigator().clearPathEntity();
			} else {
				parentAI.getBatKing().getNavigator().tryMoveToEntityLiving(parentAI.mEntityTarget, this.entityMoveSpeed);
			}

			parentAI.getBatKing().getLookHelper().setLookPositionWithEntity(parentAI.mEntityTarget, 30.0F, 30.0F);
			float f;

			if (--this.rangedAttackTime == 0) {
				if (d0 > (double) this.field_82642_h || !flag) {
					return;
				}

				f = MathHelper.sqrt_double(d0) / this.field_96562_i;
				float f1 = f;

				if (f < 0.1F) {
					f1 = 0.1F;
				}

				if (f1 > 1.0F) {
					f1 = 1.0F;
				}

				parentAI.getBatKingAsIRangedAttackMob().attackEntityWithRangedAttack(parentAI.mEntityTarget, f1);
				this.rangedAttackTime = MathHelper.floor_float(
						f * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
			} else if (this.rangedAttackTime < 0) {
				f = MathHelper.sqrt_double(d0) / this.field_96562_i;
				this.rangedAttackTime = MathHelper.floor_float(
						f * (float) (this.maxRangedAttackTime - this.field_96561_g) + (float) this.field_96561_g);
			}
		}

	}

	private class Melee {

		private final EntityAIBatKingAttack parentAI;

		/**
		 * An amount of decrementing ticks that allows the entity to attack once the
		 * tick reaches 0.
		 */
		int attackTick;
		/** The speed with which the mob will approach the target */
		double speedTowardsTarget;
		/**
		 * When true, the mob will continue chasing its target, even if it can't find a
		 * path to them right now.
		 */
		boolean longMemory;
		Class classTarget;
		private int field_75445_i;
		private double field_151497_i;
		private double field_151495_j;
		private double field_151496_k;

		private int failedPathFindingPenalty;

		public Melee(EntityAIBatKingAttack aParent, Class aClassTarget,
				double aMoveToTargetSpeed, boolean aLongMemory) {
			this.parentAI = aParent;
			this.classTarget = aClassTarget;
			this.speedTowardsTarget = aMoveToTargetSpeed;
			this.longMemory = aLongMemory;
			parentAI.setMutexBits(3);
		}

		/**
		 * Returns whether the EntityAIBase should begin execution.
		 */
		public boolean shouldExecute() {			
			if (!parentAI.hasValidTarget()) {
				return false;
			}			
			EntityLivingBase entitylivingbase = parentAI.getTarget();

			if (entitylivingbase == null) {
				return false;
			} else if (!entitylivingbase.isEntityAlive()) {
				return false;
			} else if (this.classTarget != null && !this.classTarget.isAssignableFrom(entitylivingbase.getClass())) {
				return false;
			} else {
				if (--this.field_75445_i <= 0) {
					parentAI.mEntityPathEntity = parentAI.mAttackingEntity.getNavigator().getPathToEntityLiving(entitylivingbase);
					this.field_75445_i = 4 + parentAI.mAttackingEntity.getRNG().nextInt(7);
					return parentAI.mEntityPathEntity != null;
				} else {
					return true;
				}
			}
		}

		/**
		 * Returns whether an in-progress EntityAIBase should continue executing
		 */
		public boolean continueExecuting() {
			EntityLivingBase entitylivingbase = parentAI.mAttackingEntity.getAttackTarget();
			return entitylivingbase == null ? false
					: (!entitylivingbase.isEntityAlive() ? false
							: (!this.longMemory ? !parentAI.mAttackingEntity.getNavigator().noPath()
									: parentAI.mAttackingEntity.isWithinHomeDistance(MathHelper.floor_double(entitylivingbase.posX),
											MathHelper.floor_double(entitylivingbase.posY),
											MathHelper.floor_double(entitylivingbase.posZ))));
		}

		/**
		 * Execute a one shot task or start executing a continuous task
		 */
		public void startExecuting() {
			parentAI.mAttackingEntity.getNavigator().setPath(parentAI.mEntityPathEntity, this.speedTowardsTarget);
			this.field_75445_i = 0;
		}

		/**
		 * Resets the task
		 */
		public void resetTask() {
			parentAI.mAttackingEntity.getNavigator().clearPathEntity();
		}

		/**
		 * Updates the task
		 */
		public void updateTask() {
			EntityLivingBase entitylivingbase = parentAI.mAttackingEntity.getAttackTarget();
			parentAI.mAttackingEntity.getLookHelper().setLookPositionWithEntity(entitylivingbase, 30.0F, 30.0F);
			double d0 = parentAI.mAttackingEntity.getDistanceSq(entitylivingbase.posX, entitylivingbase.boundingBox.minY,
					entitylivingbase.posZ);
			double d1 = (double) (parentAI.mAttackingEntity.width * 2.0F * parentAI.mAttackingEntity.width * 2.0F + entitylivingbase.width);
			--this.field_75445_i;

			if ((this.longMemory || parentAI.mAttackingEntity.getEntitySenses().canSee(entitylivingbase)) && this.field_75445_i <= 0
					&& (this.field_151497_i == 0.0D && this.field_151495_j == 0.0D && this.field_151496_k == 0.0D
					|| entitylivingbase.getDistanceSq(this.field_151497_i, this.field_151495_j,
							this.field_151496_k) >= 1.0D
							|| parentAI.mAttackingEntity.getRNG().nextFloat() < 0.05F)) {
				this.field_151497_i = entitylivingbase.posX;
				this.field_151495_j = entitylivingbase.boundingBox.minY;
				this.field_151496_k = entitylivingbase.posZ;
				this.field_75445_i = failedPathFindingPenalty + 4 + parentAI.mAttackingEntity.getRNG().nextInt(7);

				if (parentAI.mAttackingEntity.getNavigator().getPath() != null) {
					PathPoint finalPathPoint = parentAI.mAttackingEntity.getNavigator().getPath().getFinalPathPoint();
					if (finalPathPoint != null && entitylivingbase.getDistanceSq(finalPathPoint.xCoord,
							finalPathPoint.yCoord, finalPathPoint.zCoord) < 1) {
						failedPathFindingPenalty = 0;
					} else {
						failedPathFindingPenalty += 10;
					}
				} else {
					failedPathFindingPenalty += 10;
				}

				if (d0 > 1024.0D) {
					this.field_75445_i += 10;
				} else if (d0 > 256.0D) {
					this.field_75445_i += 5;
				}

				if (!parentAI.mAttackingEntity.getNavigator().tryMoveToEntityLiving(entitylivingbase, this.speedTowardsTarget)) {
					this.field_75445_i += 15;
				}
			}

			this.attackTick = Math.max(this.attackTick - 1, 0);

			if (d0 <= d1 && this.attackTick <= 20) {
				this.attackTick = 20;

				if (parentAI.mAttackingEntity.getHeldItem() != null) {
					parentAI.mAttackingEntity.swingItem();
				}

				parentAI.mAttackingEntity.attackEntityAsMob(entitylivingbase);
			}
		}

	}

}