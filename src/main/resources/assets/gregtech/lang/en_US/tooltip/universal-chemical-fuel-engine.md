BURNING BURNING BURNING
Use combustible liquid to generate power
You need to supply Combustion Promoter to keep it running
It will consume all the fuel and combustion promoter in the hatch every second
Energy output to the dynamo will be distributed over the next second
If the Dynamo Hatch's buffer fills up, the machine will stop
If the amount of energy to be produced is higher than the dynamo hatch can handle then all produced energy will void
When turned on, there is a 10-second period where the machine will not stop
Even if it doesn't stop, all the fuel in the hatch will be consumed
The efficiency is determined by the proportion of Combustion Promoter to fuel
The higher the amount of promoter, the higher the efficiency
Follows an exponential curve exp(-C/(p/x))*1.5, where x is the amount of fuel in liters, p is the amount of promoter in liters
and C depends on the fuel type. Diesel: C=0.04; Gas: C=0.04; Rocket fuel: C=0.005
It creates sqrt(Current Output Power) pollution every second
If you forget to supply Combustion Promoter, this engine will swallow all the fuel without outputting energy
The efficiency is up to 150%
