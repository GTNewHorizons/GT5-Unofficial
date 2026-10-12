Heats coolant with the radioactive decay of TRISO Pebbles
Insert at least {gold:{var:min_pebbles} Pebbles} and {yellow:{var:helium} {var:unit} Helium} to start
Having any less than the maximum {var:max_pebbles} Pebbles reduces efficiency
{gray:{hr}}
A lower efficiency means a higher power cost and less hot coolant
{white:Power Cost (EU/t) =} {aqua:3,840 / Efficiency}
{white:Hot Coolant ({var:unit}/t) =} {red:{var:coolant_rate} * Efficiency}
{gray:{hr}}
Each operation is {light_purple:{var:duration} hours} regardless of the Pebble amount
Consumes up to {gold:{var:consumption}%} of the stored {gold:Pebbles} and all the {yellow:Helium} at the start
Outputs burned out TRISO Pebbles at the end
There is no consequence for running out of coolant while running
