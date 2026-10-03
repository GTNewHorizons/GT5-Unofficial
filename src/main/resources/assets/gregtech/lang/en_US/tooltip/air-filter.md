Needs a {white:Turbine} in the controller
Can process {green:{var:chunk_size}x{var:chunk_size}} chunks
Each {green:Muffler Hatch} reduces pollution in one chunk of the working area by:
{aqua:{var:global_multiplier} * multiTierBonus * turbineEff * {var:scaling_factor}^mufflerTier} every second
- This controller's multiTierBonus: {green:{var:tier_bonus}}
- turbineEff = {white:Turbine} efficiency
- Effective muffler tier is limited by energy input tier
- Uses {aqua:{var:eu_usage} EU/t} while working {gray:{hr}}
Insert an {gold:Absorption Filter} into an {green:Input Bus} to double cleaning ({green:{var:filter_uses}} uses per filter)
Each maintenance issue reduces pollution cleaning by {green:{var:maint_penalty}%}
