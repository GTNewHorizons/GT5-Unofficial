package gtPlusPlus.core.common;

import api.player.client.ClientPlayerAPI;
import api.player.client.ClientPlayerBase;
import gtPlusPlus.core.handler.events.CustomMovementHandler;
import gtPlusPlus.core.handler.events.SneakManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInputFromOptions;

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
	public void onLivingUpdate() {
		
		super.onLivingUpdate();		
		EntityPlayer aPlayer = this.player;
		if (aPlayer != null) {
			SneakManager aSneak = SneakManager.get(aPlayer);
			if (!aSneak.isWearingRing()) {
				return;
			}
		}		
		
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


			

			final boolean isJumping = this.player.movementInput.jump;

			final float minSpeed = 0.8F;
			final boolean isMovingForward = this.player.movementInput.moveForward >= minSpeed;
			this.customMovementInput.update(this.mc, (MovementInputFromOptions)this.player.movementInput, this.player);


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
				if(aSneak.optionDoubleTap && this.player.onGround && !isMovingForward && (this.player.movementInput.moveForward >= minSpeed) && !this.player.isSprinting() && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness))
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

				if(!this.player.isSprinting() && (this.player.movementInput.moveForward >= minSpeed) && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && this.settings.keyBindSprint.getIsKeyPressed())
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

				if(!this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness) && !this.customMovementInput.sprintHeldAndReleased)
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

				if(canDoubleTap && !state && this.player.onGround && !isMovingForward && (this.player.movementInput.moveForward >= minSpeed) && !this.player.isSprinting() && !this.player.isUsingItem() && !this.player.isPotionActive(Potion.blindness))
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
			if(this.player.isSprinting() && ((this.player.movementInput.moveForward < minSpeed) || this.player.isCollidedHorizontally))
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

		}
	}
}