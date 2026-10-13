Расходует до {green:{var:fuel_percent}%} {white:топлива} за операцию
Для запуска нужно минимум {green:{var:min_helium}%} {gold:гелия}
{gold:Гелий} линейно повышает эффективность теплообменников вплоть до {green:100%} при полном запасе
Реактор теряет {green:{var:helium_lost}%} запаса {gold:гелия} за операцию
Потребляет {aqua:{var:power} EU/t}, до {green:{var:power_penalty}x} больше при нехватке {gold:гелия}
{gray:{hr}}
Длительность операции зависит от заполненности реактора
От {green:{var:min_time} с} до {green:{var:max_time} с}
{green:10%} этого значения фиксированы, {green:90%} зависят от формулы {aqua:y = 1 - (1 - x)^3} ({white:x}, это заполненность в %)
Каждая проблема обслуживания снижает эффективность на {green:20%}
{gray:{hr}}
Подача {gold:хладагента}, {gold:воды} или обоих сокращает время рецепта на
{green:{var:coolant_speedup}%} / {green:{var:water_speedup}%} / {green:{var:total_speedup}%} от общего времени в секунду
Количество жидкости для максимального бонуса зависит от числа {gold:шариков TRISO}
{green:{var:coolant_per_ball}} {gold:хладагента}/тик/шарик и {green:{var:water_per_ball}} {gold:дистиллированной воды}/тик/шарик
