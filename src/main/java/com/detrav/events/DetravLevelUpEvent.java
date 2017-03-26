package com.detrav.events;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerPickupXpEvent;

import java.util.UUID;

/**
 * Created by Detrav on 26.03.2017.
 */
public class DetravLevelUpEvent {

    public static UUID mod_id = UUID.fromString("9a090263-953b-4d9f-947e-d4636cf3cd7e");



    @SubscribeEvent
    public void onPlayerPickupXpEvent(PlayerPickupXpEvent ev) {
        EntityPlayer player = ev.entityPlayer;
        if (player != null) {
            if (!player.getEntityWorld().isRemote) {
                if ((player.experience + ev.orb.xpValue) >= player.xpBarCap()) {
                    UpdateHealthAttribute(player);
                }
            }
        }
    }

    public static void UpdateHealthAttribute(EntityPlayer player)
    {
        if (!player.getEntityWorld().isRemote) {
            AttributeModifier mod = GetAttributeModifier(player.experienceLevel);
            player.getEntityAttribute(
                    SharedMonsterAttributes.maxHealth
            ).removeModifier(mod);
            player.getEntityAttribute(
                    SharedMonsterAttributes.maxHealth
            ).applyModifier(mod);
        }
    }

    public static AttributeModifier GetAttributeModifier(int level) {
        int hp_boost = 0;
        switch (level) {
            case 0:
                hp_boost = 0;
                break;
            case 1:
                hp_boost = 1;
                break;
            case 2:
                hp_boost = 2;
                break;
            case 3:
                hp_boost = 2;
                break;
            case 4:
                hp_boost = 3;
                break;
            case 5:
                hp_boost = 3;
                break;
            case 6:
                hp_boost = 4;
                break;
            case 7:
                hp_boost = 4;
                break;
            case 8:
                hp_boost = 5;
                break;
            case 9:
                hp_boost = 5;
                break;
            case 10:
                hp_boost = 6;
                break;
            case 11:
                hp_boost = 6;
                break;
            case 12:
                hp_boost = 7;
                break;
            case 13:
                hp_boost = 7;
                break;
            case 14:
                hp_boost = 8;
                break;
            case 15:
                hp_boost = 8;
                break;
            case 16:
                hp_boost = 9;
                break;
            case 17:
                hp_boost = 9;
                break;
            case 18:
                hp_boost = 10;
                break;
            case 19:
                hp_boost = 10;
                break;
            case 20:
                hp_boost = 11;
                break;
            case 21:
                hp_boost = 11;
                break;
            case 22:
                hp_boost = 11;
                break;
            case 23:
                hp_boost = 12;
                break;
            case 24:
                hp_boost = 12;
                break;
            case 25:
                hp_boost = 12;
                break;
            case 26:
                hp_boost = 13;
                break;
            case 27:
                hp_boost = 13;
                break;
            case 28:
                hp_boost = 13;
                break;
            case 29:
                hp_boost = 14;
                break;
            case 30:
                hp_boost = 14;
                break;
            case 31:
                hp_boost = 14;
                break;
            case 32:
                hp_boost = 15;
                break;
            case 33:
                hp_boost = 15;
                break;
            case 34:
                hp_boost = 15;
                break;
            case 35:
                hp_boost = 16;
                break;
            case 36:
                hp_boost = 16;
                break;
            case 37:
                hp_boost = 16;
                break;
            case 38:
                hp_boost = 17;
                break;
            case 39:
                hp_boost = 17;
                break;
            case 40:
                hp_boost = 17;
                break;
            case 41:
                hp_boost = 18;
                break;
            case 42:
                hp_boost = 18;
                break;
            case 43:
                hp_boost = 18;
                break;
            case 44:
                hp_boost = 18;
                break;
            case 45:
                hp_boost = 19;
                break;
            case 46:
                hp_boost = 19;
                break;
            case 47:
                hp_boost = 19;
                break;
            case 48:
                hp_boost = 19;
                break;
            case 49:
                hp_boost = 20;
                break;
            case 50:
                hp_boost = 20;
                break;
            case 51:
                hp_boost = 20;
                break;
            case 52:
                hp_boost = 20;
                break;
            case 53:
                hp_boost = 21;
                break;
            case 54:
                hp_boost = 21;
                break;
            case 55:
                hp_boost = 21;
                break;
            case 56:
                hp_boost = 21;
                break;
            case 57:
                hp_boost = 22;
                break;
            case 58:
                hp_boost = 22;
                break;
            case 59:
                hp_boost = 22;
                break;
            case 60:
                hp_boost = 22;
                break;
            case 61:
                hp_boost = 23;
                break;
            case 62:
                hp_boost = 23;
                break;
            case 63:
                hp_boost = 23;
                break;
            case 64:
                hp_boost = 23;
                break;
            case 65:
                hp_boost = 23;
                break;
            case 66:
                hp_boost = 24;
                break;
            case 67:
                hp_boost = 24;
                break;
            case 68:
                hp_boost = 24;
                break;
            case 69:
                hp_boost = 24;
                break;
            case 70:
                hp_boost = 24;
                break;
            case 71:
                hp_boost = 25;
                break;
            case 72:
                hp_boost = 25;
                break;
            case 73:
                hp_boost = 25;
                break;
            case 74:
                hp_boost = 25;
                break;
            case 75:
                hp_boost = 25;
                break;
            case 76:
                hp_boost = 26;
                break;
            case 77:
                hp_boost = 26;
                break;
            case 78:
                hp_boost = 26;
                break;
            case 79:
                hp_boost = 26;
                break;
            case 80:
                hp_boost = 26;
                break;
            case 81:
                hp_boost = 26;
                break;
            case 82:
                hp_boost = 27;
                break;
            case 83:
                hp_boost = 27;
                break;
            case 84:
                hp_boost = 27;
                break;
            case 85:
                hp_boost = 27;
                break;
            case 86:
                hp_boost = 27;
                break;
            case 87:
                hp_boost = 27;
                break;
            case 88:
                hp_boost = 28;
                break;
            case 89:
                hp_boost = 28;
                break;
            case 90:
                hp_boost = 28;
                break;
            case 91:
                hp_boost = 28;
                break;
            case 92:
                hp_boost = 28;
                break;
            case 93:
                hp_boost = 28;
                break;
            case 94:
                hp_boost = 29;
                break;
            case 95:
                hp_boost = 29;
                break;
            case 96:
                hp_boost = 29;
                break;
            case 97:
                hp_boost = 29;
                break;
            case 98:
                hp_boost = 29;
                break;
            case 99:
                hp_boost = 29;
                break;
            case 100:
                hp_boost = 30;
                break;
            case 101:
                hp_boost = 30;
                break;
            case 102:
                hp_boost = 30;
                break;
            case 103:
                hp_boost = 30;
                break;
            case 104:
                hp_boost = 30;
                break;
            case 105:
                hp_boost = 30;
                break;
            case 106:
                hp_boost = 30;
                break;
            case 107:
                hp_boost = 31;
                break;
            case 108:
                hp_boost = 31;
                break;
            case 109:
                hp_boost = 31;
                break;
            case 110:
                hp_boost = 31;
                break;
            case 111:
                hp_boost = 31;
                break;
            case 112:
                hp_boost = 31;
                break;
            case 113:
                hp_boost = 31;
                break;
            case 114:
                hp_boost = 31;
                break;
            case 115:
                hp_boost = 32;
                break;
            case 116:
                hp_boost = 32;
                break;
            case 117:
                hp_boost = 32;
                break;
            case 118:
                hp_boost = 32;
                break;
            case 119:
                hp_boost = 32;
                break;
            case 120:
                hp_boost = 32;
                break;
            case 121:
                hp_boost = 32;
                break;
            case 122:
                hp_boost = 33;
                break;
            case 123:
                hp_boost = 33;
                break;
            case 124:
                hp_boost = 33;
                break;
            case 125:
                hp_boost = 33;
                break;
            case 126:
                hp_boost = 33;
                break;
            case 127:
                hp_boost = 33;
                break;
            case 128:
                hp_boost = 33;
                break;
            case 129:
                hp_boost = 33;
                break;
            case 130:
                hp_boost = 34;
                break;
            case 131:
                hp_boost = 34;
                break;
            case 132:
                hp_boost = 34;
                break;
            case 133:
                hp_boost = 34;
                break;
            case 134:
                hp_boost = 34;
                break;
            case 135:
                hp_boost = 34;
                break;
            case 136:
                hp_boost = 34;
                break;
            case 137:
                hp_boost = 34;
                break;
            case 138:
                hp_boost = 34;
                break;
            case 139:
                hp_boost = 35;
                break;
            case 140:
                hp_boost = 35;
                break;
            case 141:
                hp_boost = 35;
                break;
            case 142:
                hp_boost = 35;
                break;
            case 143:
                hp_boost = 35;
                break;
            case 144:
                hp_boost = 35;
                break;
            case 145:
                hp_boost = 35;
                break;
            case 146:
                hp_boost = 35;
                break;
            case 147:
                hp_boost = 35;
                break;
            case 148:
                hp_boost = 36;
                break;
            case 149:
                hp_boost = 36;
                break;
            case 150:
                hp_boost = 36;
                break;
            case 151:
                hp_boost = 36;
                break;
            case 152:
                hp_boost = 36;
                break;
            case 153:
                hp_boost = 36;
                break;
            case 154:
                hp_boost = 36;
                break;
            case 155:
                hp_boost = 36;
                break;
            case 156:
                hp_boost = 36;
                break;
            case 157:
                hp_boost = 37;
                break;
            case 158:
                hp_boost = 37;
                break;
            case 159:
                hp_boost = 37;
                break;
            case 160:
                hp_boost = 37;
                break;
            case 161:
                hp_boost = 37;
                break;
            case 162:
                hp_boost = 37;
                break;
            case 163:
                hp_boost = 37;
                break;
            case 164:
                hp_boost = 37;
                break;
            case 165:
                hp_boost = 37;
                break;
            case 166:
                hp_boost = 37;
                break;
            case 167:
                hp_boost = 37;
                break;
            case 168:
                hp_boost = 38;
                break;
            case 169:
                hp_boost = 38;
                break;
            case 170:
                hp_boost = 38;
                break;
            case 171:
                hp_boost = 38;
                break;
            case 172:
                hp_boost = 38;
                break;
            case 173:
                hp_boost = 38;
                break;
            case 174:
                hp_boost = 38;
                break;
            case 175:
                hp_boost = 38;
                break;
            case 176:
                hp_boost = 38;
                break;
            case 177:
                hp_boost = 38;
                break;
            case 178:
                hp_boost = 39;
                break;
            case 179:
                hp_boost = 39;
                break;
            case 180:
                hp_boost = 39;
                break;
            case 181:
                hp_boost = 39;
                break;
            case 182:
                hp_boost = 39;
                break;
            case 183:
                hp_boost = 39;
                break;
            case 184:
                hp_boost = 39;
                break;
            case 185:
                hp_boost = 39;
                break;
            case 186:
                hp_boost = 39;
                break;
            case 187:
                hp_boost = 39;
                break;
            case 188:
                hp_boost = 39;
                break;
            case 189:
                hp_boost = 40;
                break;
            case 190:
                hp_boost = 40;
                break;
            case 191:
                hp_boost = 40;
                break;
            case 192:
                hp_boost = 40;
                break;
            case 193:
                hp_boost = 40;
                break;
            case 194:
                hp_boost = 40;
                break;
            case 195:
                hp_boost = 40;
                break;
            case 196:
                hp_boost = 40;
                break;
            case 197:
                hp_boost = 40;
                break;
            case 198:
                hp_boost = 40;
                break;
            case 199:
                hp_boost = 40;
                break;
            case 200:
                hp_boost = 40;
                break;
            case 201:
                hp_boost = 41;
                break;
            case 202:
                hp_boost = 41;
                break;
            case 203:
                hp_boost = 41;
                break;
            case 204:
                hp_boost = 41;
                break;
            case 205:
                hp_boost = 41;
                break;
            case 206:
                hp_boost = 41;
                break;
            case 207:
                hp_boost = 41;
                break;
            case 208:
                hp_boost = 41;
                break;
            case 209:
                hp_boost = 41;
                break;
            case 210:
                hp_boost = 41;
                break;
            case 211:
                hp_boost = 41;
                break;
            case 212:
                hp_boost = 41;
                break;
            case 213:
                hp_boost = 42;
                break;
            case 214:
                hp_boost = 42;
                break;
            case 215:
                hp_boost = 42;
                break;
            case 216:
                hp_boost = 42;
                break;
            case 217:
                hp_boost = 42;
                break;
            case 218:
                hp_boost = 42;
                break;
            case 219:
                hp_boost = 42;
                break;
            case 220:
                hp_boost = 42;
                break;
            case 221:
                hp_boost = 42;
                break;
            case 222:
                hp_boost = 42;
                break;
            case 223:
                hp_boost = 42;
                break;
            case 224:
                hp_boost = 42;
                break;
            case 225:
                hp_boost = 42;
                break;
            case 226:
                hp_boost = 42;
                break;
            case 227:
                hp_boost = 43;
                break;
            case 228:
                hp_boost = 43;
                break;
            case 229:
                hp_boost = 43;
                break;
            case 230:
                hp_boost = 43;
                break;
            case 231:
                hp_boost = 43;
                break;
            case 232:
                hp_boost = 43;
                break;
            case 233:
                hp_boost = 43;
                break;
            case 234:
                hp_boost = 43;
                break;
            case 235:
                hp_boost = 43;
                break;
            case 236:
                hp_boost = 43;
                break;
            case 237:
                hp_boost = 43;
                break;
            case 238:
                hp_boost = 43;
                break;
            case 239:
                hp_boost = 43;
                break;
            case 240:
                hp_boost = 44;
                break;
            case 241:
                hp_boost = 44;
                break;
            case 242:
                hp_boost = 44;
                break;
            case 243:
                hp_boost = 44;
                break;
            case 244:
                hp_boost = 44;
                break;
            case 245:
                hp_boost = 44;
                break;
            case 246:
                hp_boost = 44;
                break;
            case 247:
                hp_boost = 44;
                break;
            case 248:
                hp_boost = 44;
                break;
            case 249:
                hp_boost = 44;
                break;
            case 250:
                hp_boost = 44;
                break;
            case 251:
                hp_boost = 44;
                break;
            case 252:
                hp_boost = 44;
                break;
            case 253:
                hp_boost = 44;
                break;
            case 254:
                hp_boost = 44;
                break;
            case 255:
                hp_boost = 45;
                break;
            case 256:
                hp_boost = 45;
                break;
            case 257:
                hp_boost = 45;
                break;
            case 258:
                hp_boost = 45;
                break;
            case 259:
                hp_boost = 45;
                break;
            case 260:
                hp_boost = 45;
                break;
            case 261:
                hp_boost = 45;
                break;
            case 262:
                hp_boost = 45;
                break;
            case 263:
                hp_boost = 45;
                break;
            case 264:
                hp_boost = 45;
                break;
            case 265:
                hp_boost = 45;
                break;
            case 266:
                hp_boost = 45;
                break;
            case 267:
                hp_boost = 45;
                break;
            case 268:
                hp_boost = 45;
                break;
            case 269:
                hp_boost = 45;
                break;
            case 270:
                hp_boost = 46;
                break;
            case 271:
                hp_boost = 46;
                break;
            case 272:
                hp_boost = 46;
                break;
            case 273:
                hp_boost = 46;
                break;
            case 274:
                hp_boost = 46;
                break;
            case 275:
                hp_boost = 46;
                break;
            case 276:
                hp_boost = 46;
                break;
            case 277:
                hp_boost = 46;
                break;
            case 278:
                hp_boost = 46;
                break;
            case 279:
                hp_boost = 46;
                break;
            case 280:
                hp_boost = 46;
                break;
            case 281:
                hp_boost = 46;
                break;
            case 282:
                hp_boost = 46;
                break;
            case 283:
                hp_boost = 46;
                break;
            case 284:
                hp_boost = 46;
                break;
            case 285:
                hp_boost = 46;
                break;
            case 286:
                hp_boost = 46;
                break;
            case 287:
                hp_boost = 47;
                break;
            case 288:
                hp_boost = 47;
                break;
            case 289:
                hp_boost = 47;
                break;
            case 290:
                hp_boost = 47;
                break;
            case 291:
                hp_boost = 47;
                break;
            case 292:
                hp_boost = 47;
                break;
            case 293:
                hp_boost = 47;
                break;
            case 294:
                hp_boost = 47;
                break;
            case 295:
                hp_boost = 47;
                break;
            case 296:
                hp_boost = 47;
                break;
            case 297:
                hp_boost = 47;
                break;
            case 298:
                hp_boost = 47;
                break;
            case 299:
                hp_boost = 47;
                break;
            case 300:
                hp_boost = 47;
                break;
            case 301:
                hp_boost = 47;
                break;
            case 302:
                hp_boost = 47;
                break;
            case 303:
                hp_boost = 47;
                break;
            case 304:
                hp_boost = 48;
                break;
            case 305:
                hp_boost = 48;
                break;
            case 306:
                hp_boost = 48;
                break;
            case 307:
                hp_boost = 48;
                break;
            case 308:
                hp_boost = 48;
                break;
            case 309:
                hp_boost = 48;
                break;
            case 310:
                hp_boost = 48;
                break;
            case 311:
                hp_boost = 48;
                break;
            case 312:
                hp_boost = 48;
                break;
            case 313:
                hp_boost = 48;
                break;
            case 314:
                hp_boost = 48;
                break;
            case 315:
                hp_boost = 48;
                break;
            case 316:
                hp_boost = 48;
                break;
            case 317:
                hp_boost = 48;
                break;
            case 318:
                hp_boost = 48;
                break;
            case 319:
                hp_boost = 48;
                break;
            case 320:
                hp_boost = 48;
                break;
            case 321:
                hp_boost = 48;
                break;
            case 322:
                hp_boost = 49;
                break;
            case 323:
                hp_boost = 49;
                break;
            case 324:
                hp_boost = 49;
                break;
            case 325:
                hp_boost = 49;
                break;
            case 326:
                hp_boost = 49;
                break;
            case 327:
                hp_boost = 49;
                break;
            case 328:
                hp_boost = 49;
                break;
            case 329:
                hp_boost = 49;
                break;
            case 330:
                hp_boost = 49;
                break;
            case 331:
                hp_boost = 49;
                break;
            case 332:
                hp_boost = 49;
                break;
            case 333:
                hp_boost = 49;
                break;
            case 334:
                hp_boost = 49;
                break;
            case 335:
                hp_boost = 49;
                break;
            case 336:
                hp_boost = 49;
                break;
            case 337:
                hp_boost = 49;
                break;
            case 338:
                hp_boost = 49;
                break;
            case 339:
                hp_boost = 49;
                break;
            case 340:
                hp_boost = 49;
                break;
            case 341:
                hp_boost = 50;
                break;
            case 342:
                hp_boost = 50;
                break;
            case 343:
                hp_boost = 50;
                break;
            case 344:
                hp_boost = 50;
                break;
            case 345:
                hp_boost = 50;
                break;
            case 346:
                hp_boost = 50;
                break;
            case 347:
                hp_boost = 50;
                break;
            case 348:
                hp_boost = 50;
                break;
            case 349:
                hp_boost = 50;
                break;
            case 350:
                hp_boost = 50;
                break;
            case 351:
                hp_boost = 50;
                break;
            case 352:
                hp_boost = 50;
                break;
            case 353:
                hp_boost = 50;
                break;
            case 354:
                hp_boost = 50;
                break;
            case 355:
                hp_boost = 50;
                break;
            case 356:
                hp_boost = 50;
                break;
            case 357:
                hp_boost = 50;
                break;
            case 358:
                hp_boost = 50;
                break;
            case 359:
                hp_boost = 50;
                break;
            case 360:
                hp_boost = 50;
                break;
            case 361:
                hp_boost = 51;
                break;
            case 362:
                hp_boost = 51;
                break;
            case 363:
                hp_boost = 51;
                break;
            case 364:
                hp_boost = 51;
                break;
            case 365:
                hp_boost = 51;
                break;
            case 366:
                hp_boost = 51;
                break;
            case 367:
                hp_boost = 51;
                break;
            case 368:
                hp_boost = 51;
                break;
            case 369:
                hp_boost = 51;
                break;
            case 370:
                hp_boost = 51;
                break;
            case 371:
                hp_boost = 51;
                break;
            case 372:
                hp_boost = 51;
                break;
            case 373:
                hp_boost = 51;
                break;
            case 374:
                hp_boost = 51;
                break;
            case 375:
                hp_boost = 51;
                break;
            case 376:
                hp_boost = 51;
                break;
            case 377:
                hp_boost = 51;
                break;
            case 378:
                hp_boost = 51;
                break;
            case 379:
                hp_boost = 51;
                break;
            case 380:
                hp_boost = 51;
                break;
            case 381:
                hp_boost = 51;
                break;
            case 382:
                hp_boost = 52;
                break;
            case 383:
                hp_boost = 52;
                break;
            case 384:
                hp_boost = 52;
                break;
            case 385:
                hp_boost = 52;
                break;
            case 386:
                hp_boost = 52;
                break;
            case 387:
                hp_boost = 52;
                break;
            case 388:
                hp_boost = 52;
                break;
            case 389:
                hp_boost = 52;
                break;
            case 390:
                hp_boost = 52;
                break;
            case 391:
                hp_boost = 52;
                break;
            case 392:
                hp_boost = 52;
                break;
            case 393:
                hp_boost = 52;
                break;
            case 394:
                hp_boost = 52;
                break;
            case 395:
                hp_boost = 52;
                break;
            case 396:
                hp_boost = 52;
                break;
            case 397:
                hp_boost = 52;
                break;
            case 398:
                hp_boost = 52;
                break;
            case 399:
                hp_boost = 52;
                break;
            case 400:
                hp_boost = 52;
                break;
            case 401:
                hp_boost = 52;
                break;
            case 402:
                hp_boost = 52;
                break;
            case 403:
                hp_boost = 52;
                break;
            case 404:
                hp_boost = 52;
                break;
            case 405:
                hp_boost = 53;
                break;
            case 406:
                hp_boost = 53;
                break;
            case 407:
                hp_boost = 53;
                break;
            case 408:
                hp_boost = 53;
                break;
            case 409:
                hp_boost = 53;
                break;
            case 410:
                hp_boost = 53;
                break;
            case 411:
                hp_boost = 53;
                break;
            case 412:
                hp_boost = 53;
                break;
            case 413:
                hp_boost = 53;
                break;
            case 414:
                hp_boost = 53;
                break;
            case 415:
                hp_boost = 53;
                break;
            case 416:
                hp_boost = 53;
                break;
            case 417:
                hp_boost = 53;
                break;
            case 418:
                hp_boost = 53;
                break;
            case 419:
                hp_boost = 53;
                break;
            case 420:
                hp_boost = 53;
                break;
            case 421:
                hp_boost = 53;
                break;
            case 422:
                hp_boost = 53;
                break;
            case 423:
                hp_boost = 53;
                break;
            case 424:
                hp_boost = 53;
                break;
            case 425:
                hp_boost = 53;
                break;
            case 426:
                hp_boost = 53;
                break;
            case 427:
                hp_boost = 53;
                break;
            case 428:
                hp_boost = 54;
                break;
            case 429:
                hp_boost = 54;
                break;
            case 430:
                hp_boost = 54;
                break;
            case 431:
                hp_boost = 54;
                break;
            case 432:
                hp_boost = 54;
                break;
            case 433:
                hp_boost = 54;
                break;
            case 434:
                hp_boost = 54;
                break;
            case 435:
                hp_boost = 54;
                break;
            case 436:
                hp_boost = 54;
                break;
            case 437:
                hp_boost = 54;
                break;
            case 438:
                hp_boost = 54;
                break;
            case 439:
                hp_boost = 54;
                break;
            case 440:
                hp_boost = 54;
                break;
            case 441:
                hp_boost = 54;
                break;
            case 442:
                hp_boost = 54;
                break;
            case 443:
                hp_boost = 54;
                break;
            case 444:
                hp_boost = 54;
                break;
            case 445:
                hp_boost = 54;
                break;
            case 446:
                hp_boost = 54;
                break;
            case 447:
                hp_boost = 54;
                break;
            case 448:
                hp_boost = 54;
                break;
            case 449:
                hp_boost = 54;
                break;
            case 450:
                hp_boost = 54;
                break;
            case 451:
                hp_boost = 54;
                break;
            case 452:
                hp_boost = 54;
                break;
            case 453:
                hp_boost = 55;
                break;
            case 454:
                hp_boost = 55;
                break;
            case 455:
                hp_boost = 55;
                break;
            case 456:
                hp_boost = 55;
                break;
            case 457:
                hp_boost = 55;
                break;
            case 458:
                hp_boost = 55;
                break;
            case 459:
                hp_boost = 55;
                break;
            case 460:
                hp_boost = 55;
                break;
            case 461:
                hp_boost = 55;
                break;
            case 462:
                hp_boost = 55;
                break;
            case 463:
                hp_boost = 55;
                break;
            case 464:
                hp_boost = 55;
                break;
            case 465:
                hp_boost = 55;
                break;
            case 466:
                hp_boost = 55;
                break;
            case 467:
                hp_boost = 55;
                break;
            case 468:
                hp_boost = 55;
                break;
            case 469:
                hp_boost = 55;
                break;
            case 470:
                hp_boost = 55;
                break;
            case 471:
                hp_boost = 55;
                break;
            case 472:
                hp_boost = 55;
                break;
            case 473:
                hp_boost = 55;
                break;
            case 474:
                hp_boost = 55;
                break;
            case 475:
                hp_boost = 55;
                break;
            case 476:
                hp_boost = 55;
                break;
            case 477:
                hp_boost = 55;
                break;
            case 478:
                hp_boost = 55;
                break;
            case 479:
                hp_boost = 55;
                break;
            case 480:
                hp_boost = 56;
                break;
            case 481:
                hp_boost = 56;
                break;
            case 482:
                hp_boost = 56;
                break;
            case 483:
                hp_boost = 56;
                break;
            case 484:
                hp_boost = 56;
                break;
            case 485:
                hp_boost = 56;
                break;
            case 486:
                hp_boost = 56;
                break;
            case 487:
                hp_boost = 56;
                break;
            case 488:
                hp_boost = 56;
                break;
            case 489:
                hp_boost = 56;
                break;
            case 490:
                hp_boost = 56;
                break;
            case 491:
                hp_boost = 56;
                break;
            case 492:
                hp_boost = 56;
                break;
            case 493:
                hp_boost = 56;
                break;
            case 494:
                hp_boost = 56;
                break;
            case 495:
                hp_boost = 56;
                break;
            case 496:
                hp_boost = 56;
                break;
            case 497:
                hp_boost = 56;
                break;
            case 498:
                hp_boost = 56;
                break;
            case 499:
                hp_boost = 56;
                break;
            case 500:
                hp_boost = 56;
                break;
            case 501:
                hp_boost = 56;
                break;
            case 502:
                hp_boost = 56;
                break;
            case 503:
                hp_boost = 56;
                break;
            case 504:
                hp_boost = 56;
                break;
            case 505:
                hp_boost = 56;
                break;
            case 506:
                hp_boost = 56;
                break;
            case 507:
                hp_boost = 56;
                break;
            case 508:
                hp_boost = 57;
                break;
            case 509:
                hp_boost = 57;
                break;
            case 510:
                hp_boost = 57;
                break;
            case 511:
                hp_boost = 57;
                break;
            case 512:
                hp_boost = 57;
                break;
            case 513:
                hp_boost = 57;
                break;
            case 514:
                hp_boost = 57;
                break;
            case 515:
                hp_boost = 57;
                break;
            case 516:
                hp_boost = 57;
                break;
            case 517:
                hp_boost = 57;
                break;
            case 518:
                hp_boost = 57;
                break;
            case 519:
                hp_boost = 57;
                break;
            case 520:
                hp_boost = 57;
                break;
            case 521:
                hp_boost = 57;
                break;
            case 522:
                hp_boost = 57;
                break;
            case 523:
                hp_boost = 57;
                break;
            case 524:
                hp_boost = 57;
                break;
            case 525:
                hp_boost = 57;
                break;
            case 526:
                hp_boost = 57;
                break;
            case 527:
                hp_boost = 57;
                break;
            case 528:
                hp_boost = 57;
                break;
            case 529:
                hp_boost = 57;
                break;
            case 530:
                hp_boost = 57;
                break;
            case 531:
                hp_boost = 57;
                break;
            case 532:
                hp_boost = 57;
                break;
            case 533:
                hp_boost = 57;
                break;
            case 534:
                hp_boost = 57;
                break;
            case 535:
                hp_boost = 57;
                break;
            case 536:
                hp_boost = 57;
                break;
            case 537:
                hp_boost = 58;
                break;
            case 538:
                hp_boost = 58;
                break;
            case 539:
                hp_boost = 58;
                break;
            case 540:
                hp_boost = 58;
                break;
            case 541:
                hp_boost = 58;
                break;
            case 542:
                hp_boost = 58;
                break;
            case 543:
                hp_boost = 58;
                break;
            case 544:
                hp_boost = 58;
                break;
            case 545:
                hp_boost = 58;
                break;
            case 546:
                hp_boost = 58;
                break;
            case 547:
                hp_boost = 58;
                break;
            case 548:
                hp_boost = 58;
                break;
            case 549:
                hp_boost = 58;
                break;
            case 550:
                hp_boost = 58;
                break;
            case 551:
                hp_boost = 58;
                break;
            case 552:
                hp_boost = 58;
                break;
            case 553:
                hp_boost = 58;
                break;
            case 554:
                hp_boost = 58;
                break;
            case 555:
                hp_boost = 58;
                break;
            case 556:
                hp_boost = 58;
                break;
            case 557:
                hp_boost = 58;
                break;
            case 558:
                hp_boost = 58;
                break;
            case 559:
                hp_boost = 58;
                break;
            case 560:
                hp_boost = 58;
                break;
            case 561:
                hp_boost = 58;
                break;
            case 562:
                hp_boost = 58;
                break;
            case 563:
                hp_boost = 58;
                break;
            case 564:
                hp_boost = 58;
                break;
            case 565:
                hp_boost = 58;
                break;
            case 566:
                hp_boost = 58;
                break;
            case 567:
                hp_boost = 58;
                break;
            case 568:
                hp_boost = 59;
                break;
            case 569:
                hp_boost = 59;
                break;
            case 570:
                hp_boost = 59;
                break;
            case 571:
                hp_boost = 59;
                break;
            case 572:
                hp_boost = 59;
                break;
            case 573:
                hp_boost = 59;
                break;
            case 574:
                hp_boost = 59;
                break;
            case 575:
                hp_boost = 59;
                break;
            case 576:
                hp_boost = 59;
                break;
            case 577:
                hp_boost = 59;
                break;
            case 578:
                hp_boost = 59;
                break;
            case 579:
                hp_boost = 59;
                break;
            case 580:
                hp_boost = 59;
                break;
            case 581:
                hp_boost = 59;
                break;
            case 582:
                hp_boost = 59;
                break;
            case 583:
                hp_boost = 59;
                break;
            case 584:
                hp_boost = 59;
                break;
            case 585:
                hp_boost = 59;
                break;
            case 586:
                hp_boost = 59;
                break;
            case 587:
                hp_boost = 59;
                break;
            case 588:
                hp_boost = 59;
                break;
            case 589:
                hp_boost = 59;
                break;
            case 590:
                hp_boost = 59;
                break;
            case 591:
                hp_boost = 59;
                break;
            case 592:
                hp_boost = 59;
                break;
            case 593:
                hp_boost = 59;
                break;
            case 594:
                hp_boost = 59;
                break;
            case 595:
                hp_boost = 59;
                break;
            case 596:
                hp_boost = 59;
                break;
            case 597:
                hp_boost = 59;
                break;
            case 598:
                hp_boost = 59;
                break;
            case 599:
                hp_boost = 59;
                break;
            default:
                hp_boost = 60;
                break;
        }
        return new AttributeModifier(mod_id, "detravlevelup", hp_boost, 0);
    }

    static boolean inited = false;

    public static void register() {
        if (!inited) {
            inited = true;
            DetravLevelUpEvent handler = new DetravLevelUpEvent();
            MinecraftForge.EVENT_BUS.register(handler);
            FMLCommonHandler.instance().bus().register(handler);
        }
    }
}
