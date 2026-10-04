Uses up to {green:{var:fuel_percent}%} of {white:Fuel} per operation
Requires at least {green:{var:min_helium}%} {gold:Helium} to start operation
{gold:Helium} increases heat exchanger effectiveness linearly up to {green:100%} at max capacity
The reactor loses {green:{var:helium_lost}%} of its stored {gold:Helium} per operation
Uses {aqua:{var:power} EU/t}, increasing by up to {green:{var:power_penalty}x} when lacking {gold:Helium}
{gray:{hr}}
Operation time scales with reactor fill level
Between {green:{var:min_time}s} and {green:{var:max_time}s}
{green:10%} of this value is flat, {green:90%} follows the formula {aqua:y = 1 - (1 - x)^3} ({white:x} is % fill level)
Maintenance problems decrease efficiency by {green:20%} for each issue
{gray:{hr}}
Supplying {gold:Coolant}, {gold:Water}, or both speeds up the recipe by
{green:{var:coolant_speedup}%} / {green:{var:water_speedup}%} / {green:{var:total_speedup}%} total recipe time per second
The amount of fluid needed for max bonus scales with {gold:TRISO Balls}
{green:{var:coolant_per_ball}} {gold:Coolant}/tick/ball and {green:{var:water_per_ball}} {gold:Distilled Water}/tick/ball
