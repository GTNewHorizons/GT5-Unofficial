Needs a {color_name:Turbine} in the controller to run
Cleans pollution in the surrounding {color_range:{var:chunk_size}x{var:chunk_size}} chunks
Consumes a fixed {color_eu_usage:{var:eu_usage} {EU/t}} while active
{hr}
Each {color_name:Muffler Hatch} removes the following amount of pollution from one chunk per second
{color_amount:{var:global_multiplier} * B * turbineEff * FLOOR({var:scaling_factor}^mufflerTier)}
- This Machine's Tier Bonus (B) = {color_amount:{var:tier_bonus}}
- turbineEff = {color_name:Turbine} efficiency
- Effective {color_name:Muffler Hatch} Tier is limited by {color_name:Energy Hatch} Tier
{hr}
Supply {color_key:Absorption Filters} through an {color_name:Input Bus} to double pollution removed ({color_time:{var:filter_lifespan}}s per)
Each maintenance issue reduces the amount of pollution removed by {color_amount:{var:maint_penalty}%}
