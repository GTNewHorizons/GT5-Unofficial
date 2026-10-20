Горячие жидкости: {color_hot:{fluid:lava}}, {color_hot:{fluid:ic2hotcoolant}}, {color_hot:{fluid:molten.solarsalthot}}
На выходе: {color_cold:{fluid:ic2pahoehoelava}}, {color_cold:{fluid:ic2coolant}}, {color_cold:{fluid:molten.solarsaltcold}}
Превращает {color_name:дистиллированную воду} в {color_steam:пар}
Если подача горячей жидкости достигает {color_note:порога ПГ}, выдаёт вдвое меньше {color_steam:перегретого пара}
{color_danger:Взрывается через 100 секунд без дистиллированной воды}
{hr}
{color_hot:{fluid:lava}}: порог ПГ {color_amount:1 000 {L}/с}, макс. ввод {color_amount:2 000 {L}/с}, макс. выход {color_steam:8 000 {L}/т}
{color_hot:{fluid:ic2hotcoolant}}: порог ПГ {color_amount:800 {L}/с}, макс. ввод {color_amount:1 600 {L}/с}, макс. выход {color_steam:16 000 {L}/т}
{color_hot:{fluid:molten.solarsalthot}}: порог ПГ {color_amount:160 {L}/с}, макс. ввод {color_amount:320 {L}/с}, макс. выход {color_steam:16 000 {L}/т}
{hr}
{color_name:Запрограммированная схема} в контроллере снижает порог ПГ на {color_amount:3.75%} и выработку пара на {color_loss:1.5%} за каждое значение выше 1
