package gtPlusPlus.core.common;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.network.play.client.C0BPacketEntityAction;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraft.util.ResourceLocation;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import gtPlusPlus.core.handler.events.CustomMovementHandler;
import gtPlusPlus.core.handler.events.SneakManager;

public class BasePlayer extends ClientPlayerBase
{
	private final Minecraft mc = Minecraft.getMinecraft();
	private final CustomMovementHandler customMovementInput = new CustomMovementHandler();
	private final GameSettings settings = this.mc.gameSettings;

	public BasePlayer(final ClientPlayerAPI api)
	{
		super(api);
	}

	/*
	 * 		EntityPlayerSP.onLivingUpdate() - Adapted to PlayerAPI
	 */
	@Override
	public void onLivingUpdate()
	{
		if(this.player.sprintingTicksLeft > 0)
		{
			--this.player.sprintingTicksLeft;
			if(this.player.sprintingTicksLeft == 0)
			{
				this.player.setSprinting(false);
			}
		}

		if(this.playerAPI.getSprintToggleTimerField() > 0)
		{
			this.playerAPI.setSprintToggleTimerField(this.playerAPI.getSprintToggleTimerField() - 1);
		}

		if(this.mc.playerController.enableEverythingIsScrewedUpMode())
		{
			this.player.posX = this.player.posZ = 0.5D;
			this.player.posX = 0.0D;
			this.player.posZ = 0.0D;
			this.player.rotationYaw = this.player.ticksExisted / 12.0F;
			this.player.rotationPitch = 10.0F;
			this.player.posY = 68.5D;
		}
		else
		{
			this.player.prevTimeInPortal = this.player.timeInPortal;
			if(this.playerAPI.getInPortalField())
			{
				if(this.mc.currentScreen != null)
				{
					this.mc.displayGuiScreen((GuiScreen)null);
				}

				if(this.player.timeInPortal == 0.0F)
				{
					this.mc.getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(new ResourceLocation("portal.trigger"), (this.player.getRNG().nextFloat() * 0.4F) + 0.8F));
				}

				this.player.timeInPortal += 0.0125F;

				if(this.player.timeInPortal >= 1.0F)
				{
					this.player.timeInPortal = 1.0F;
				}

				this.playerAPI.setInPortalField(false);
			}
			else if(this.player.isPotionActive(Potion.confusion) && (this.player.getActivePotionEffect(Potion.confusion).getDuration() > 60))
			{
				this.player.timeInPortal += 0.006666667F;
				if(this.player.timeInPortal > 1.0F)
				{
					this.player.timeInPortal = 1.0F;
				}
			}
			else
			{
				if(this.player.timeInPortal > 0.0F)
				{
					this.player.timeInPortal -= 0.05F;
				}

				if(this.player.timeInPortal < 0.0F)
				{
					this.player.timeInPortal = 0.0F;
				}
			}


			if(this.player.timeUntilPortal > 0)
			{
				--this.player.timeUntilPortal;
			}

			final boolean isJumping = this.player.movementInput.jump;

			final float minSpeed = 0.8F;
			final boolean isMovingForward = this.player.movementInput.moveForward >= minSpeed;
			this.customMovementInput.update(this.mc, (MovementInputFromOptions)this.player.movementInput, this.player);

			if(this.player.isUsingItem() && !this.player.isRiding())
			{
				this.player.movementInput.moveStrafe *= 0.2F;
				this.player.movementInput.moveForward *= 0.2F;
				this.playerAPI.setSprintToggleTimerField(0);
			}

			if(this.player.movementInput.sneak && (this.player.ySize < 0.2F))
			{
				this.player.ySize = 0.2F;
			}

			this.playerAPI.localPushOutOfBlocks(this.player.posX - (this.player.width * 0.35D), this.player.boundingBox.minY + 0.5D, this.player.posZ + (this.player.width * 0.35D));
			this.playerAPI.localPushOutOfBlocks(this.player.posX - (this.player.width * 0.35D), this.player.boundingBox.minY + 0.5D, this.player.posZ - (this.player.width * 0.35D));
			this.playerAPI.localPushOutOfBlocks(this.player.posX + (this.player.width * 0.35D), this.player.boundingBox.minY + 0.5D, this.player.posZ - (this.player.width * 0.35D));
			this.playerAPI.localPushOutOfBlocks(this.player.posX + (this.player.width * 0.35D), this.player.boundingBox.minY + 0.5D, this.player.posZ + (this.player.width * 0.35D));
			final boolean enoughHunger = (this.player.getFoodStats().getFoodLevel() > 6.0F) || this.player.capabilities.isFlying;

			/*
			 * 		Begin ToggleSneak Changes - ToggleSprint
			 */
			SneakManager aSneak = SneakManager.get(this.player);

			final boolean isSprintDisabled	= false;
			final boolean canDoubleTap		= aSneak.optionDoubleTap;

			
			// Detect when ToggleSprint was disabled in the in-game options menu
			if(aSneak.wasSprintDisabled)
			{
				this.player.setSprinting(false);
				this.customMovementInput.UpdateSprint(false, false, aSneak);
				aSneak.wasSprintDisabled = false;
			}

			// Default Sprint routine converted to PlayerAPI, use if ToggleSprint is disabled - TODO - Disable sprinting as a whole
			if(isSprintDisabled)
			{
				//Utils.LOG_INFO("Sprint pressed");
				if(aSneak.optionDoubleTap && this.player.onGround && !isMovingForward && (this.player.movementInput.moveForward >= minSpeed) && !this.player.isSprinting() && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness))
				{
					if((this.playerAPI.getSprintToggleTimerField() <= 0) && !this.settings.keyBindSprint.getIsKeyPressed())
					{
						this.playerAPI.setSprintToggleTimerField(7);
					}
					else
					{
						if (aSneak.Sprinting()){
							this.player.setSprinting(true);
							this.customMovementInput.UpdateSprint(true, false, aSneak);
						}
						else {
							this.player.setSprinting(false);
							this.customMovementInput.UpdateSprint(false, false, aSneak);
						}
					}
				}

				if(!this.player.isSprinting() && (this.player.movementInput.moveForward >= minSpeed) && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && this.settings.keyBindSprint.getIsKeyPressed())
				{
					if (aSneak.Sprinting()){
						this.player.setSprinting(true);
						this.customMovementInput.UpdateSprint(true, false, aSneak);
					}
					else {
						this.player.setSprinting(false);
						this.customMovementInput.UpdateSprint(false, false, aSneak);
					}
				}
			}
			else
			{
				final boolean state = this.customMovementInput.sprint;

				// Only handle changes in state under the following conditions:
				// On ground, not hungry, not eating/using item, not blind, and not Vanilla
				//
				// 5/6/14 - onGround check removed to match vanilla's 'start sprint while jumping' behavior.
				//if(this.player.onGround && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased)

				if(enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased)
				{
					if((canDoubleTap && !this.player.isSprinting()) || !canDoubleTap)
					{
						if (aSneak.Sprinting()){
							this.player.setSprinting(state);
						} else {
							this.player.setSprinting(false);
						}
					}
				}

				if(canDoubleTap && !state && this.player.onGround && !isMovingForward && (this.player.movementInput.moveForward >= minSpeed) && !this.player.isSprinting() && enoughHunger && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness))
				{
					if(this.playerAPI.getSprintToggleTimerField() == 0)
					{
						this.playerAPI.setSprintToggleTimerField(7);
					}
					else
					{
						if (aSneak.Sprinting()){
							this.player.setSprinting(true);
							this.customMovementInput.UpdateSprint(true, true, aSneak);
							this.playerAPI.setSprintToggleTimerField(0);
						}
					}
				}
			}

			// If sprinting, break the sprint in appropriate circumstances:
			// Player stops moving forward, runs into something, or gets too hungry
			if(this.player.isSprinting() && ((this.player.movementInput.moveForward < minSpeed) || this.player.isCollidedHorizontally || !enoughHunger))
			{
				this.player.setSprinting(false);

				// Undo toggle if we resumed vanilla operation due to Hold&Release, DoubleTap, Fly, Ride
				if ((this.customMovementInput.sprintHeldAndReleased == true) || isSprintDisabled || this.customMovementInput.sprintDoubleTapped || this.player.capabilities.isFlying || this.player.isRiding())
				{
					this.customMovementInput.UpdateSprint(false, false, aSneak);
				}
			}

			/*
			 * 		End ToggleSneak Changes - ToggleSprint
			 */

			//			//
			//			//  Debug Framework - Added 5/7/2014
			//			//
			//			if (this.showDebug && this.settings.keyBindPickBlock.getIsKeyPressed() && !this.handledDebugPress)
			//			{
			//				this.player.addChatMessage(new ChatComponentText("+--------------------------------------+"));
			//				this.player.addChatMessage(new ChatComponentText("|        ToggleSneak Debug Info        |"));
			//				this.player.addChatMessage(new ChatComponentText("+--------------------------------------+"));
			//				this.player.addChatMessage(new ChatComponentText("                                        "));
			//				this.player.addChatMessage(new ChatComponentText("isFlying       - " + (this.player.capabilities.isFlying == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("isCreative     - " + (this.player.capabilities.isCreativeMode == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("enableFlyBoost - " + (SneakManager.optionEnableFlyBoost == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("flyBoostAmount - " + SneakManager.optionFlyBoostAmount));
			//				this.player.addChatMessage(new ChatComponentText("                                        "));
			//				this.player.addChatMessage(new ChatComponentText("keybindSprint  - " + (this.settings.keyBindSprint.getIsKeyPressed() == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("keybindSneak   - " + (this.settings.keyBindSneak.getIsKeyPressed() == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("keybindJump    - " + (this.settings.keyBindJump.getIsKeyPressed() == true ? "True" : "False")));
			//				this.player.addChatMessage(new ChatComponentText("                                        "));
			//				this.player.addChatMessage(new ChatComponentText("                                        "));
			//
			//				this.handledDebugPress = true;
			//			}
			//			else if (this.showDebug && !this.settings.keyBindPickBlock.getIsKeyPressed() && this.handledDebugPress)
			//			{
			//				this.handledDebugPress = false;
			//			}

			//
			//  Fly Speed Boosting - Added 5/7/2014
			//
			if(this.player.capabilities.getFlySpeed() != 0.05F)
			{
				this.player.capabilities.setFlySpeed(0.05F);
			}


			if(this.player.capabilities.allowFlying && !isJumping && this.player.movementInput.jump)
			{
				if(this.playerAPI.getFlyToggleTimerField() == 0)
				{
					this.playerAPI.setFlyToggleTimerField(7);
				}
				else
				{
					this.player.capabilities.isFlying = !this.player.capabilities.isFlying;
					this.player.sendPlayerAbilities();
					this.playerAPI.setFlyToggleTimerField(0);
				}
			}

			if(this.player.capabilities.isFlying)
			{
				if(this.player.movementInput.sneak)
				{
					this.player.motionY -= 0.15D;
				}
				if(this.player.movementInput.jump)
				{
					this.player.motionY += 0.15D;
				}
			}

			if(this.player.isRidingHorse())
			{
				if(this.playerAPI.getHorseJumpPowerCounterField() < 0)
				{
					this.playerAPI.setHorseJumpPowerCounterField(this.playerAPI.getHorseJumpPowerCounterField() + 1);
					if(this.playerAPI.getHorseJumpPowerCounterField() == 0)
					{
						this.playerAPI.setHorseJumpPowerField(0.0F);
					}
				}

				if(isJumping && !this.player.movementInput.jump)
				{
					this.playerAPI.setHorseJumpPowerCounterField(this.playerAPI.getHorseJumpPowerCounterField() - 10);
					this.playerAPI.setHorseJumpPowerCounterField(-10);
					((EntityClientPlayerMP)this.player).sendQueue.addToSendQueue(new C0BPacketEntityAction(this.player, 6, (int)(this.player.getHorseJumpPower() * 100.0F)));
				}
				else if(!isJumping && this.player.movementInput.jump)
				{
					this.playerAPI.setHorseJumpPowerCounterField(0);
					this.playerAPI.setHorseJumpPowerField(0.0F);
				}
				else if(isJumping)
				{
					this.playerAPI.setHorseJumpPowerCounterField(this.playerAPI.getHorseJumpPowerCounterField() + 1);
					if(this.playerAPI.getHorseJumpPowerCounterField() < 10)
					{
						this.playerAPI.setHorseJumpPowerField(this.playerAPI.getHorseJumpPowerCounterField() * 0.1F);
					}
					else
					{
						this.playerAPI.setHorseJumpPowerField(0.8F + ((2.0F / (this.playerAPI.getHorseJumpPowerCounterField() - 9)) * 0.1F));
					}
				}
			}
			else
			{
				this.playerAPI.setHorseJumpPowerField(0.0F);
			}

			this.playerAPI.superOnLivingUpdate();
			if(this.player.onGround && this.player.capabilities.isFlying)
			{
				this.player.capabilities.isFlying = false;
				this.player.sendPlayerAbilities();
			}
		}
	}
}