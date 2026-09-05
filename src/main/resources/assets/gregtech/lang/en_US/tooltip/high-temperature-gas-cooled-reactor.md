Uses up to {red:{var:fuel_percent}}% of fuel per operation, {red:10}% of this value is flat and {red:90}% dependent on the formula y=1-(1-x)^3 (x is % fill level)
Maintenance problems decrease the efficiency of cooling by {red:20}% for each issue
Uses {red:{var:power}} EU/t increasing by up to {red:{var:power_penalty}} times when lacking Helium Gas
Helium gas increases effectiveness of heat exchangers linearly up to {red:100}% at max capacity
The Reactor loses {red:{var:helium_lost}}% helium per operation and requires at least {red:{var:min_helium}}% helium to start operation
One Operation takes longer based on reactor fill level (between {red:{var:min_time}}s and {red:{var:max_time}}s)
Providing coolant/water/both speeds up recipe by {red:{var:coolant_speedup}}%/{red:{var:water_speedup}}%/{red:{var:total_speedup}}% total recipe time/second
The amount of necessary fluid for maximum bonus speed scales with TRISO Balls, {red:{var:coolant_per_ball}} coolant/tick/ball and {red:{var:water_per_ball}} distilled water/tick/ball
