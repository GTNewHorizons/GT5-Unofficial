Needs a {white:Turbine} in the controller to run
Cleans pollution in the surrounding {green:{var:chunk_size}x{var:chunk_size}} chunks
Consumes a fixed {aqua:{var:eu_usage} EU/t} while active
{gray:{hr}}
Each {white:Muffler Hatch} removes the following amount of pollution from one chunk per second
{green:{var:global_multiplier} * B * turbineEff * FLOOR({var:scaling_factor}^mufflerTier)}
- This Machine's Tier Bonus (B) = {green:{var:tier_bonus}}
- turbineEff = {white:Turbine} efficiency
- Effective {white:Muffler Hatch} Tier is limited by {white:Energy Hatch} Tier
{gray:{hr}}
Supply {gold:Absorption Filters} through an {white:Input Bus} to double pollution removed ({gold:{var:filter_lifespan}}s per)
Each maintenance issue reduces the amount of pollution removed by {green:{var:maint_penalty}%}
