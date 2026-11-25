Uses up to {color_amount:{var:fuel_percent}%} of {color_name:Fuel} per operation
Requires at least {color_amount:{var:min_helium}%} {color_key:Helium} to start operation
{color_key:Helium} increases heat exchanger effectiveness linearly up to {color_amount:100%} at max capacity
The reactor loses {color_amount:{var:helium_lost}%} of its stored {color_key:Helium} per operation
Uses {color_eu_usage:{var:power} {EU/t}}, increasing by up to {color_amount:{var:power_penalty}x} when lacking {color_key:Helium}
{hr}
Operation time scales with reactor fill level
Between {color_amount:{var:min_time}s} and {color_amount:{var:max_time}s}
{color_amount:10%} of this value is flat, {color_amount:90%} follows the formula {aqua:y = 1 - (1 - x)^3} ({white:x} is % fill level)
Maintenance problems decrease efficiency by {color_amount:20%} for each issue
{hr}
Supplying {color_key:Coolant}, {color_key:Water}, or both speeds up the recipe by
{color_amount:{var:coolant_speedup}%} / {color_amount:{var:water_speedup}%} / {color_amount:{var:total_speedup}%} total recipe time per second
The amount of fluid needed for max bonus scales with {color_key:TRISO Balls}
{color_amount:{var:coolant_per_ball}} {color_key:Coolant}/tick/ball and {color_amount:{var:water_per_ball}} {color_key:Distilled Water}/tick/ball
