3 режима: {light_purple:Лёгкий} | {blue:Обычный} | {dark_green:Тяжёлый}
{gray:{hr}}
{gold:{var:parallels} параллели} за {white:сумму тиров турбин}
{green:Скорость {var:speed}%}
{aqua:Потребление EU {var:eu_eff}%}
{white:Турбины меньше огромных} имеют пониженную эффективность...
Даёт {var:slots_per_tier} {white:слота под турбины} за тир {white:структуры}
Для работы требует {aqua:(тир рецепта * {var:fluid_per_tier} {var:fluid_unit}/с)} {gold:Керосина}
Подавайте вместо него {gold:Биокаталитическое ракетное топливо}, чтобы получить множитель параллелей {gold:{var:tier2_mult}x}
{yellow:Разгон ограничен тиром шлюза + 1}
{gray:{hr}}
{light_purple:Лёгкий режим}:
- {green:+{var:light_speed}% к скорости}
- Максимальный тир рецепта: {aqua:(тир вольтажа - {var:light_offset})}
{blue:Обычный режим}: без изменений
{dark_green:Тяжёлый режим}: требует {gold:Биокаталитическое ракетное топливо}
- Делит {gold:параллели} на {gold:{var:heavy_div}}
- Умножает {red:стоимость EU} на {red:{var:heavy_eu}}
- {light_purple:Разгон без ограничений}
- Некоторые рецепты {dark_red:требуют} {dark_green:Тяжёлый режим}
