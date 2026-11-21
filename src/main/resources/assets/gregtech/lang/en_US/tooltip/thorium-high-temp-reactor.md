Heats coolant with the radioactive decay of TRISO Pebbles
Insert at least {gold:{var:min_pebbles} Pebbles} and {yellow:{var:helium}{L} Helium} to start
{color_note:Having any less than {var:max_pebbles} Pebbles reduces efficiency}
{hr}
A lower efficiency means a higher power cost and less hot coolant
{color_eu_usage:Power Cost (EU/t)}{color_formula: = {var:power_usage} / Efficiency}
{color_hot:Hot Coolant ({L}/t)}{color_formula: = {var:coolant_rate} * Efficiency}
{hr}
Each operation is {color_time:{var:duration} hours} regardless of the Pebble amount
Consumes all {yellow:Helium} and up to {color_amount:{var:consumption}%} of the stored {gold:Pebbles} on startup
Outputs burned out TRISO Pebbles at the end
There is no consequence for running out of {aqua:Coolant} whilst running
