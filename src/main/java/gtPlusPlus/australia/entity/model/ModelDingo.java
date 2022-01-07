package gtPlusPlus.australia.entity.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;
import org.lwjgl.opengl.GL11;

@SideOnly(Side.CLIENT)
public class ModelDingo extends ModelBase {
	
	/** main box for the dingo head */
	public ModelRenderer dingoHeadMain;
	/** The dingo's body */
	public ModelRenderer dingoBody;
	/** dingo'se first leg */
	public ModelRenderer dingoLeg1;
	/** dingo's second leg */
	public ModelRenderer dingoLeg2;
	/** dingo's third leg */
	public ModelRenderer dingoLeg3;
	/** dingo's fourth leg */
	public ModelRenderer dingoLeg4;
	/** The dingo's tail */
	ModelRenderer dingoTail;
	/** The dingo's mane */
	ModelRenderer dingoMane;

	public ModelDingo() {
		float f = 0.0F;
		float f1 = 13.5F;
		this.dingoHeadMain = new ModelRenderer(this, 0, 0);
		this.dingoHeadMain.addBox(-3.0F, -3.0F, -2.0F, 6, 6, 4, f);
		this.dingoHeadMain.setRotationPoint(-1.0F, f1, -7.0F);
		this.dingoBody = new ModelRenderer(this, 18, 14);
		this.dingoBody.addBox(-4.0F, -2.0F, -3.0F, 6, 9, 6, f);
		this.dingoBody.setRotationPoint(0.0F, 14.0F, 2.0F);
		this.dingoMane = new ModelRenderer(this, 21, 0);
		this.dingoMane.addBox(-4.0F, -3.0F, -3.0F, 8, 6, 7, f);
		this.dingoMane.setRotationPoint(-1.0F, 14.0F, 2.0F);
		this.dingoLeg1 = new ModelRenderer(this, 0, 18);
		this.dingoLeg1.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.dingoLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
		this.dingoLeg2 = new ModelRenderer(this, 0, 18);
		this.dingoLeg2.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.dingoLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
		this.dingoLeg3 = new ModelRenderer(this, 0, 18);
		this.dingoLeg3.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.dingoLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
		this.dingoLeg4 = new ModelRenderer(this, 0, 18);
		this.dingoLeg4.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.dingoLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
		this.dingoTail = new ModelRenderer(this, 9, 18);
		this.dingoTail.addBox(-1.0F, 0.0F, -1.0F, 2, 8, 2, f);
		this.dingoTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
		this.dingoHeadMain.setTextureOffset(16, 14).addBox(-3.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.dingoHeadMain.setTextureOffset(16, 14).addBox(1.0F, -5.0F, 0.0F, 2, 2, 1, f);
		this.dingoHeadMain.setTextureOffset(0, 10).addBox(-1.5F, 0.0F, -5.0F, 3, 3, 4, f);
	}

	/**
	 * Sets the models various rotation angles then renders the model.
	 */
	public void render(Entity p_78088_1_, float p_78088_2_, float p_78088_3_, float p_78088_4_, float p_78088_5_,
			float p_78088_6_, float p_78088_7_) {
		super.render(p_78088_1_, p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_);
		this.setRotationAngles(p_78088_2_, p_78088_3_, p_78088_4_, p_78088_5_, p_78088_6_, p_78088_7_, p_78088_1_);

		if (this.isChild) {
			float f6 = 2.0F;
			GL11.glPushMatrix();
			GL11.glTranslatef(0.0F, 5.0F * p_78088_7_, 2.0F * p_78088_7_);
			this.dingoHeadMain.renderWithRotation(p_78088_7_);
			GL11.glPopMatrix();
			GL11.glPushMatrix();
			GL11.glScalef(1.0F / f6, 1.0F / f6, 1.0F / f6);
			GL11.glTranslatef(0.0F, 24.0F * p_78088_7_, 0.0F);
			this.dingoBody.render(p_78088_7_);
			this.dingoLeg1.render(p_78088_7_);
			this.dingoLeg2.render(p_78088_7_);
			this.dingoLeg3.render(p_78088_7_);
			this.dingoLeg4.render(p_78088_7_);
			this.dingoTail.renderWithRotation(p_78088_7_);
			this.dingoMane.render(p_78088_7_);
			GL11.glPopMatrix();
		} else {
			this.dingoHeadMain.renderWithRotation(p_78088_7_);
			this.dingoBody.render(p_78088_7_);
			this.dingoLeg1.render(p_78088_7_);
			this.dingoLeg2.render(p_78088_7_);
			this.dingoLeg3.render(p_78088_7_);
			this.dingoLeg4.render(p_78088_7_);
			this.dingoTail.renderWithRotation(p_78088_7_);
			this.dingoMane.render(p_78088_7_);
		}
	}

	/**
	 * Used for easily adding entity-dependent animations. The second and third
	 * float params here are the same second and third as in the setRotationAngles
	 * method.
	 */
	public void setLivingAnimations(EntityLivingBase p_78086_1_, float p_78086_2_, float p_78086_3_, float p_78086_4_) {
		EntityWolf entitydingo = (EntityWolf) p_78086_1_;

		if (entitydingo.isAngry()) {
			this.dingoTail.rotateAngleY = 0.0F;
		} else {
			this.dingoTail.rotateAngleY = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
		}

		if (entitydingo.isSitting()) {
			this.dingoMane.setRotationPoint(-1.0F, 16.0F, -3.0F);
			this.dingoMane.rotateAngleX = ((float) Math.PI * 2F / 5F);
			this.dingoMane.rotateAngleY = 0.0F;
			this.dingoBody.setRotationPoint(0.0F, 18.0F, 0.0F);
			this.dingoBody.rotateAngleX = ((float) Math.PI / 4F);
			this.dingoTail.setRotationPoint(-1.0F, 21.0F, 6.0F);
			this.dingoLeg1.setRotationPoint(-2.5F, 22.0F, 2.0F);
			this.dingoLeg1.rotateAngleX = ((float) Math.PI * 3F / 2F);
			this.dingoLeg2.setRotationPoint(0.5F, 22.0F, 2.0F);
			this.dingoLeg2.rotateAngleX = ((float) Math.PI * 3F / 2F);
			this.dingoLeg3.rotateAngleX = 5.811947F;
			this.dingoLeg3.setRotationPoint(-2.49F, 17.0F, -4.0F);
			this.dingoLeg4.rotateAngleX = 5.811947F;
			this.dingoLeg4.setRotationPoint(0.51F, 17.0F, -4.0F);
		} else {
			this.dingoBody.setRotationPoint(0.0F, 14.0F, 2.0F);
			this.dingoBody.rotateAngleX = ((float) Math.PI / 2F);
			this.dingoMane.setRotationPoint(-1.0F, 14.0F, -3.0F);
			this.dingoMane.rotateAngleX = this.dingoBody.rotateAngleX;
			this.dingoTail.setRotationPoint(-1.0F, 12.0F, 8.0F);
			this.dingoLeg1.setRotationPoint(-2.5F, 16.0F, 7.0F);
			this.dingoLeg2.setRotationPoint(0.5F, 16.0F, 7.0F);
			this.dingoLeg3.setRotationPoint(-2.5F, 16.0F, -4.0F);
			this.dingoLeg4.setRotationPoint(0.5F, 16.0F, -4.0F);
			this.dingoLeg1.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
			this.dingoLeg2.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
			this.dingoLeg3.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F + (float) Math.PI) * 1.4F * p_78086_3_;
			this.dingoLeg4.rotateAngleX = MathHelper.cos(p_78086_2_ * 0.6662F) * 1.4F * p_78086_3_;
		}

		this.dingoHeadMain.rotateAngleZ = entitydingo.getInterestedAngle(p_78086_4_)
				+ entitydingo.getShakeAngle(p_78086_4_, 0.0F);
		this.dingoMane.rotateAngleZ = entitydingo.getShakeAngle(p_78086_4_, -0.08F);
		this.dingoBody.rotateAngleZ = entitydingo.getShakeAngle(p_78086_4_, -0.16F);
		this.dingoTail.rotateAngleZ = entitydingo.getShakeAngle(p_78086_4_, -0.2F);
	}

	/**
	 * Sets the model's various rotation angles. For bipeds, par1 and par2 are used
	 * for animating the movement of arms and legs, where par1 represents the
	 * time(so that arms and legs swing back and forth) and par2 represents how
	 * "far" arms and legs can swing at most.
	 */
	public void setRotationAngles(float p_78087_1_, float p_78087_2_, float p_78087_3_, float p_78087_4_,
			float p_78087_5_, float p_78087_6_, Entity p_78087_7_) {
		super.setRotationAngles(p_78087_1_, p_78087_2_, p_78087_3_, p_78087_4_, p_78087_5_, p_78087_6_, p_78087_7_);
		this.dingoHeadMain.rotateAngleX = p_78087_5_ / (180F / (float) Math.PI);
		this.dingoHeadMain.rotateAngleY = p_78087_4_ / (180F / (float) Math.PI);
		this.dingoTail.rotateAngleX = p_78087_3_;
	}
	
}