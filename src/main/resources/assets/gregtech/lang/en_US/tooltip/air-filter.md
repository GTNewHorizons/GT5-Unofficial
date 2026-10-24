Requires a {color_name:Turbine} in the controller to run
Cleans pollution in a {color_range:{var:chunk_size}x{var:chunk_size}} chunk area
Consumes {color_voltage:{var:eu_usage}} {color_tier:{EU/t}} while active
{hr}
Each {color_name:Muffler Hatch} cleans pollution from one chunk per second based on
{color_amount:{var:global_multiplier} * B * turbineEff * ({var:scaling_factor}^mufflerTier)}
- Machine Tier Bonus (B): {color_amount:{var:tier_bonus}}
- turbineEff: {color_name:Turbine} efficiency
- Muffler Tier is capped by {color_name:Energy Hatch} Tier
{hr}
Supply {color_key:Absorption Filters} to an {color_name:Input Bus} to multiply output by {color_amount:{var:boost_per_filter}x}
Each filter lasts {color_time:{var:filter_lifespan}} seconds
Each maintenance issue reduces cleaning by {color_amount:{var:maint_penalty}%}
