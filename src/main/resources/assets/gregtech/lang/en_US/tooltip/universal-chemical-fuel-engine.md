BURNING BURNING BURNING
Reacts {gold:Combustion Promoter} with {gold:Gas}, {gold:Diesel}, or {gold:Rocket Fuel} to generate power with up to {aqua:150%} efficiency
No soft caps or upper limits on power output, other than the {white:Dynamo Hatch} size
No power is produced without {gold:Combustion Promoter}
Excess power is voided if the {white:Dynamo Hatch} is full or the output exceeds the dynamo throughput
{gray:{hr}}
Efficiency is determined by the ratio (R) of {gold:Combustion Promoter} to fuel
The more {gold:Combustion Promoter}, the higher the efficiency
Follows an exponential curve {aqua:exp(-C/R) * {var:efficiency_ceiling}}, where {white:C} is a constant based on the fuel type
{gold:Gas/Diesel Fuel}: {white:C}={aqua:{var:gas_coefficient}} | {gold:Rocket Fuel}: {white:C}={aqua:{var:rocket_coefficient}}
{gray:{hr}}
Produces {dark_purple:sqrt(EU/t)} pollution per second
