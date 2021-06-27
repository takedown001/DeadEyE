#ifndef HACKS_H
#define HACKS_H

#include "PMDeadeye.h"

bool isValidPlayer(PlayerData data){
    return (data.Body != PMVector2::Zero() && data.Head != PMVector2::Zero());
}

bool isValidItem(ItemData data){
    return (data.Location != PMVector2::Zero());
}
bool isValidVehical(VehicalData data){
    return (data.Location != PMVector2::Zero());
}
/*int isOutsideSafezone(Vector2 pos, Vector2 screen) {
    Vector2 mSafezoneTopLeft(screen.x * 0.04f, screen.y * 0.04f);
    Vector2 mSafezoneBottomRight(screen.x * 0.96f, screen.y * 0.96f);

    int result = 0;
    if (pos.y < mSafezoneTopLeft.y) {
        // top
        result |= 1;
    }
    if (pos.x > mSafezoneBottomRight.x) {
        // right
        result |= 2;
    }
    if (pos.y > mSafezoneBottomRight.y) {
        // bottom
        result |= 4;
    }
    if (pos.x < mSafezoneTopLeft.x) {
        // left
        result |= 8;
    }
    return result;
}*/

PMVector2 pushToScreenBorder(PMVector2 Pos, PMVector2 screen, int offset) {
    int x = (int)Pos.x;
    int y = (int)Pos.y;
    if (Pos.y < 0) {
        // top
        y = -offset;
    }
    if (Pos.x > screen.x) {
        // right
        x = (int)screen.x + offset;
    }
    if (Pos.y > screen.y) {
        // bottom
        y = (int)screen.y + offset;
    }
    if (Pos.x < 0) {
        // left
        x = -offset;
    }
    return PMVector2(x, y);
}

bool isInCenter(PMRect box,int screenWidth, int screenHeight){
    PMVector2 centerPos = PMVector2(screenWidth / 2, screenHeight / 2);
    bool  w = false;
    if(box.x < centerPos.x  && box.y < centerPos.y){
        w = true;
    }
    return w;
}

bool isOutsideSafeZone(PMVector2 pos, PMVector2 screen) {
    if (pos.y < 0) {
        return true;
    }
    if (pos.x > screen.x) {
        return true;
    }
    if (pos.y > screen.y) {
        return true;
    }
    return pos.x < 0;
}

void DrawESP(PMESP esp, int screenWidth, int screenHeight) {
    if (isfov) {
        esp.DrawCircle(PMColor(255, 0, 0, 200), 2,
                       PMVector2(screenWidth / 2, screenHeight / 2), 130);
    }
    if(iscrosshair){
        esp.DrawCrosshair(PMColor(0, 0, 0, 255), PMVector2(screenWidth / 2, screenHeight / 2), 42);
    }
        if (isdaemon) {
            esp.DrawText(PMColor(255,69,0), "Daemon Status :", PMVector2(500, 55), 20);
            esp.DrawText(PMColor::Green(), "Success", PMVector2(650, 55), 20);

        } else {
            esp.DrawText(PMColor(255,69,0), "Daemon Status :", PMVector2(500, 55), 20);
            esp.DrawText(PMColor::Orange(), "Failed", PMVector2(650, 55), 20);
        }
    PMVector2 screen(screenWidth, screenHeight);
    float mScale = screenHeight / (float) 1080;
    esp.DrawText(PMColor::Red(), "@DeadEye_TG", PMVector2(
            (screenWidth) - 130, (screenHeight) - 60), 20);

    Response response = getData(screenWidth, screenHeight);
    if (response.Success) {
        if(isNearEnemy) {
            PMColor color = PMColor(75,175,80,110);
            PMColor coloor = PMColor(255,65,0,110);
            PMColor colooor = PMColor(255,0,0,110);
            if (response.NearEnemy == 0) {
                string enemyData = " CLEAR ";
                int boxWidth = 180;
                int boxHeight = 60;
                esp.DrawFilledRect(color, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawRect(PMColor(0,200,0), 5, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawText(PMColor::White(), enemyData.c_str(), PMVector2((screenWidth / 2), 117), 30);
            } else if (response.NearEnemy > 0 && response.NearEnemy < 8) {
                string enemyData = to_string(response.NearEnemy);
                int boxWidth = 180;
                int boxHeight = 60;
                esp.DrawFilledRect(coloor, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawRect(PMColor::OrangeD(), 5, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawText(PMColor::White(), enemyData.c_str(), PMVector2((screenWidth / 2), 117), 30);
            } else {
                string enemyData = to_string(response.NearEnemy);
                int boxWidth = 180;
                int boxHeight = 60;
                esp.DrawFilledRect(colooor, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawRect(PMColor::Red(), 5, PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawText(PMColor::White(), enemyData.c_str(), PMVector2((screenWidth / 2), 117), 30);
            }
        }
        int count = response.PlayerCount;
        if (count > 0) {
            for (int i = 0; i < count; i++) {
                 PlayerData player = response.Players[i];
                if (!isValidPlayer(player)) { continue; }

                bool isTeamMate = player.TeamID == response.MyTeamID;
                if (isTeamMate && !isTeamMateShow) { continue; }

                PMVector2 location = player.Body;
                if (isPlayer360 && isOutsideSafeZone(location, screen)) {
                    string dist;
                    dist += to_string((int) player.Distance);
                    dist += "m";

                    PMVector2 hintDotRenderPos = pushToScreenBorder(location, screen,
                                                                    (int) ((mScale * 100) /
                                                                           3));
                    PMVector2 hintTextRenderPos = pushToScreenBorder(location, screen,
                                                                     -(int) ((mScale *
                                                                              36)));
                    if(player.Distance < 70) {
                        esp.DrawFilledCircle(
                                PMColor::RedTT(),
                                hintDotRenderPos, (mScale * 100));
                        esp.DrawText(PMColor::White(), dist.c_str(), hintTextRenderPos,
                                     playerTextSize);
                    } else if(player.Distance < 140 && player.Distance > 70) {
                        esp.DrawFilledCircle(
                                PMColor::OrangeTT(),
                                hintDotRenderPos, (mScale * 100));
                        esp.DrawText(PMColor::White(), dist.c_str(), hintTextRenderPos,
                                     playerTextSize);
                    } else {
                        esp.DrawFilledCircle(
                                PMColor::GreenTT(),
                                hintDotRenderPos, (mScale * 100));
                        esp.DrawText(PMColor::White(), dist.c_str(), hintTextRenderPos,
                                     playerTextSize);
                    }
                    continue;
                }

                float boxHeight = fabsf(player.Root.y - player.Head.y);
                float boxWidth = boxHeight * 0.56;
                PMRect Box(player.Head.x - (boxWidth / 2), player.Head.y, boxWidth,
                           boxHeight);
                bool playerInCenter = isInCenter(Box,screenWidth,screenHeight);

                if (isPlayerSkel) {

                    if (!player.isBot) {
                        PMColor skclr = playerInCenter ? PMColor::Alien() : PMColor(255,10,0);
                        esp.DrawCircle(skclr, 3, player.Neck, boxWidth / 6);
                        esp.DrawLine(skclr, 3, player.Neck, player.Chest);
                        esp.DrawLine(skclr, 3, player.Chest, player.Pelvis);
                        esp.DrawLine(skclr, 3, player.Chest, player.LShoulder);
                        esp.DrawLine(skclr, 3, player.Chest, player.RShoulder);
                        esp.DrawLine(skclr, 3, player.LShoulder, player.LElbow);
                        esp.DrawLine(skclr, 3, player.RShoulder, player.RElbow);
                        esp.DrawLine(skclr, 3, player.LElbow, player.LWrist);
                        esp.DrawLine(skclr, 3, player.RElbow, player.RWrist);
                        esp.DrawLine(skclr, 3, player.Pelvis, player.LThigh);
                        esp.DrawLine(skclr, 3, player.Pelvis, player.RThigh);
                        esp.DrawLine(skclr, 3, player.LThigh, player.LKnee);
                        esp.DrawLine(skclr, 3, player.RThigh, player.RKnee);
                        esp.DrawLine(skclr, 3, player.LKnee, player.LAnkle);
                        esp.DrawLine(skclr, 3, player.RKnee, player.RAnkle);
                    } else {
                        PMColor skoclr = playerInCenter ? PMColor::Alien() : PMColor::Cyan();
                        esp.DrawCircle(skoclr, 3, player.Neck, boxWidth / 6);
                        esp.DrawLine(skoclr, 3, player.Neck, player.Chest);
                        esp.DrawLine(skoclr, 3, player.Chest, player.Pelvis);
                        esp.DrawLine(skoclr, 3, player.Chest, player.LShoulder);
                        esp.DrawLine(skoclr, 3, player.Chest, player.RShoulder);
                        esp.DrawLine(skoclr, 3, player.LShoulder, player.LElbow);
                        esp.DrawLine(skoclr, 3, player.RShoulder, player.RElbow);
                        esp.DrawLine(skoclr, 3, player.LElbow, player.LWrist);
                        esp.DrawLine(skoclr, 3, player.RElbow, player.RWrist);
                        esp.DrawLine(skoclr, 3, player.Pelvis, player.LThigh);
                        esp.DrawLine(skoclr, 3, player.Pelvis, player.RThigh);
                        esp.DrawLine(skoclr, 3, player.LThigh, player.LKnee);
                        esp.DrawLine(skoclr, 3, player.RThigh, player.RKnee);
                        esp.DrawLine(skoclr, 3, player.LKnee, player.LAnkle);
                        esp.DrawLine(skoclr, 3, player.RKnee, player.RAnkle);
                    }
                }
                if (isPlayerLine) {
                    if (player.isBot) {

                        PMColor lcolor = playerInCenter ? PMColor::Alien() : PMColor::Cyan();
                        if(islinecenter){
                            esp.DrawLine(lcolor, 2, PMVector2((screenWidth / 2), (screenHeight / 2)), player.Neck);
                        }else if (islinedown){
                            esp.DrawLine(lcolor, 2,
                                         PMVector2((screenWidth / 2), screenHeight), (player.Root));
                        }else{
                            esp.DrawLine(lcolor, 2, PMVector2((screenWidth / 2), 0), player.Neck);
                        }
                    } else {
                        PMColor lcolor = playerInCenter ? PMColor::Alien() : PMColor::Red();
                        if(islinecenter){
                            esp.DrawLine(lcolor, 2, PMVector2((screenWidth / 2), (screenHeight / 2)), player.Neck);
                        }else if (islinedown){
                            esp.DrawLine(lcolor, 2,
                                         PMVector2((screenWidth / 2), screenHeight), (player.Root));
                        }else{
                            esp.DrawLine(lcolor, 2, PMVector2((screenWidth / 2), 0), player.Neck);
                        }

                    }
                }

                if (isPlayerBox) {
                    if (player.isBot) {
                        PMColor bxclr = playerInCenter ? PMColor::Alien() : PMColor::Cyan();
                        PMColor bxxclr = playerInCenter ? PMColor::AlienT() : PMColor::CyanT();
                        esp.DrawFilledRect(bxxclr, Box);
                        esp.DrawRect(bxclr, 2, Box);
                    } else {
                        PMColor bxclr = playerInCenter ? PMColor::Alien() : PMColor::Red();
                        PMColor bxxclr = playerInCenter ? PMColor::AlienT() : PMColor::RedT();
                        esp.DrawFilledRect(bxxclr, Box);
                        esp.DrawRect(bxclr, 2, Box);


                    }

                }

                PMRect Boxxx((player.Head.x - (boxWidth / 2)) + 10, (player.Head.y - 10), boxWidth, boxHeight);
                PMRect Boxx((player.Head.x - (boxWidth / 2)) - 10, player.Head.y, boxWidth, boxHeight);
                if(is3DPlayerBox){
                    if (player.isBot) {
                       PMColor bxclr = playerInCenter ? PMColor::Alien() : PMColor::Cyan();
                         PMColor bxxclr = playerInCenter ? PMColor::AlienT() : PMColor::CyanT();
                        esp.DrawFilledRect(bxxclr, Boxxx);
                        esp.DrawFilledRect(bxxclr, Boxx);
                        esp.DrawRect(bxclr, 3, Boxxx);
                        esp.DrawRect(bxclr, 3, Boxx);
                    } else {
                        PMColor bxclr = playerInCenter ? PMColor::Alien() : PMColor::Red();
                        PMColor bxxclr = playerInCenter ? PMColor::AlienT() : PMColor::RedT();
                        esp.DrawFilledRect(bxxclr, Boxxx);
                        esp.DrawFilledRect(bxxclr, Boxx);
                        esp.DrawRect(bxclr, 3, Boxxx);
                        esp.DrawRect(bxclr, 3, Boxx);
                    }
                }

                if (isPlayerHealth) {
                    esp.DrawHealthBar(
                            PMVector2(Box.x + (Box.width / 2) - 65, Box.y - 30),
                            (115 * mScale),
                            100, player.Health);
                }
                if(isverticlhealth){
                    esp.DrawVerticalHealthBar(
                            PMVector2(Box.x + Box.width, Box.y),
                            boxHeight,
                            100, player.Health);
                }
                if(isPlayerName) {
                    wstring pname = player.PlayerName;
                    wstring bname = L" AI ";


                    if(player.isBot) {
                        esp.DrawPlayerText(PMColor::White(), bname.c_str(),
                                           PMVector2(Box.x + (Box.width / 2), Box.y -18),
                                           ((30 * mScale)) / 2);
                    } else {
                        esp.DrawPlayerText(PMColor::White(), pname.c_str(),
                                           PMVector2(Box.x + (Box.width / 2) , Box.y -18),
                                           ((30 * mScale)) / 2);

                    }
                }
                if (isteamid) {
                    wstring teamid = L" " + to_wstring(player.TeamID) ;
                    esp.DrawPlayerText(PMColor::Orange(), teamid.c_str(),
                                       PMVector2((Box.x + (Box.width / 2)) - 75,
                                                 Box.y - 30),
                                       ((35 * mScale)) / 2);
                }


//                if (isplayeruid) {
//                   wstring playerid = L" " + to_wstring(player.PlayerID);
//                    esp.DrawPlayerText(PMColor::White(), playerid.c_str(),
//                                       PMVector2(Box.x + (Box.width / 2) - 70,
//                                                 Box.y + 22),
//                                       ((32 * mScale)) / 2);
    //            }

                if (isPlayerDist) {
                    string dist;
                    dist += to_string((int) player.Distance);
                    dist += "m";

                    esp.DrawText(PMColor::Orange(), dist.c_str(),
                                 PMVector2(Box.x + (Box.width / 2),
                                           Box.y + Box.height + 25),
                                 playerTextSize);
                }


            }
        }

        count = response.ItemsCount;
        if(count > 0) {
            for (int i = 0; i < count; i++) {
                ItemData item = response.Items[i];
                if (!isValidItem(item)) { continue; }

                PMVector2 location = item.Location;

                string dist;
                dist += to_string((int) item.Distance);
                dist += "m";

                if ((strstr(item.Name, "MedKit") && isMedKit) ||
                    (strstr(item.Name, "FirstAid") && isFirstAid) ||
                    (strstr(item.Name, "Injection") && isInjection) ||
                    (strstr(item.Name, "Painkiller") && isPainkiller) ||
                    (strstr(item.Name, "Drink") && isDrink) ||
                    (strstr(item.Name, "Bandage") && isBandage)) {
                    esp.DrawText(PMColor::Alien(), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
                if ((strstr(item.Name, "Armor L3") && armor3) ||
                    (strstr(item.Name, "Helmet L3") && helmet3) ||
                    (strstr(item.Name, "Bag L3") && bagl3) ||
                    (strstr(item.Name, "Armor L2") && armor2) ||
                    (strstr(item.Name, "Helmet L2") && helmet2) ||
                    (strstr(item.Name, "Bag L2") && bagl2) || (strstr(item.Name, "Bag L1") && bagl1) || (strstr(item.Name, "Armor L1") && armor1) || (strstr(item.Name, "Helmet L1") && helmet1)) {
                    esp.DrawText(PMColor::SmokeyBlack(), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
                if ((strstr(item.Name, "7.62mm") && isssm) ||
                    (strstr(item.Name, "5.56mm") && isffm) ||
                    (strstr(item.Name, "45ACP") && isACP) ||
                    (strstr(item.Name, "9mm") && is9mm) ||
                    (strstr(item.Name, "300Magnum") && is300magneum) ||
                    (strstr(item.Name, "arrow") && isarrow) || (strstr(item.Name, "12 Guage") && is12guage)) {
                    esp.DrawText(PMColor::Gold(), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
                if ((strstr(item.Name, "HoloSignt") && hollow) ||
                    (strstr(item.Name, "Canted Sight") && Caneted) ||
                    (strstr(item.Name, "RedDot") && Reddot) ||
                    (strstr(item.Name, "8x") && is8x) ||
                    (strstr(item.Name, "4x") && is4x) ||
                    (strstr(item.Name, "2x") && is2x) || (strstr(item.Name, "3x") && is3x) || (strstr(item.Name, "6x") && is6x)) {
                    esp.DrawText(PMColor(179,61,81), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
                if ((strstr(item.Name, "FlareGun") && isflare) ||
                    (strstr(item.Name, "Ghillie") && Gilli) ||
                    (strstr(item.Name, "AirDrop") && Airdrop) ||
                    (strstr(item.Name, "Plane") && DropPlane) ||
                    (strstr(item.Name, "Crate") && Crate)) {
                    esp.DrawText(PMColor::Crimson(), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
                if ((strstr(item.Name, "AKM") && isakm) ||
                    (strstr(item.Name, "M416") && ism416) ||
                    (strstr(item.Name, "QBZ") && isQBZ) ||
                    (strstr(item.Name, "M24") && ism24) ||
                    (strstr(item.Name, "SCAR L") && isscarl) ||
                    (strstr(item.Name, "M762") && ism762)||
                    (strstr(item.Name, "M16A4") && ism416a4)||
                    (strstr(item.Name, "Mk47 Mutant") && ismk47)||
                    (strstr(item.Name, "G36C") && isG36C)||
                    (strstr(item.Name, "Groza") && isGroza)||
                    (strstr(item.Name, "AUG A3") && isAug)||
                    (strstr(item.Name, "QBU") && isQBU)||
                    (strstr(item.Name, "SLR") && isSLR)||
                    (strstr(item.Name, "SKS") && issks)||
                    (strstr(item.Name, "Kar98k") && iskar98 ) ||
                    (strstr(item.Name, "VSS") && isvss  ) ||
                    (strstr(item.Name, "Win94") && iswin94 ) ||
                    (strstr(item.Name, "PP19Bizon") && isBizon ) ||
                    (strstr(item.Name, "Uzi") && isuzi )||
                    (strstr(item.Name, "MK14") && ismk14 )||
                    (strstr(item.Name, "M249") && ism249 ) ||
                    (strstr(item.Name, "Vector") && isvector ) ||
                    (strstr(item.Name, "DP28") && isdp28 ) ||
                    (strstr(item.Name, "AWM") && isAWM ) ||
                    (strstr(item.Name, "Mini14") && isMini14 ) ||
                    (strstr(item.Name, "UMP9") && isUmp ) ||
                    (strstr(item.Name, "MP5K") && ismp5k ) ||
                    (strstr(item.Name, "Tommy Gun") && istommy ) ||
                    (strstr(item.Name, "Mosin Nagant") && isMosin ) ||
                    (strstr(item.Name, "FAMAS")  )
                        ){
                    esp.DrawText(PMColor(108,31,146), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }

                if (strstr(item.Name, "Grenadew") && iswarning) {
                    if (item.Distance < 15) {
                        esp.DrawText(PMColor::Red(),
                                     "Warning : There is Grenade near You",
                                     PMVector2(screenWidth / 2, (screenHeight / 8) + 9),
                                     30);
                    }
                    if (!(i % 2 == 0)) {
                        esp.DrawCircle(PMColor::Yellow(), 3, PMVector2(
                                location.x, location.y + (21 * mScale)), 15);
                        esp.DrawFilledCircle(PMColor::YellowLight(), PMVector2(
                                location.x, location.y + (20 * mScale)), 15);
                    } else {
                        esp.DrawCircle(PMColor::Red(), 3, PMVector2(
                                location.x, location.y + (21 * mScale)), 15);
                        esp.DrawFilledCircle(PMColor::RedLight(), PMVector2(
                                location.x, location.y + (20 * mScale)), 15);
                    }

                    esp.DrawText(PMColor::Red(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);
                }
                if(strstr(item.Name, "Molotovw") && iswarning){
                    if(item.Distance < 15) {
                        esp.DrawText(PMColor::Red(), "Warning : There is Mototov near You",
                                     PMVector2(screenWidth / 2, (screenHeight /8 ) + 9),
                                     30);
                    }    esp.DrawText(PMColor::Red(),"Molotov", PMVector2(
                            location.x, location.y + (20 * mScale)),
                                      itemTextSize);
                    esp.DrawText(PMColor::Red(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);

                }
                if(strstr(item.Name, "Sticky BombW") && iswarning){
                    if(item.Distance < 15) {
                        esp.DrawText(PMColor::Red(), "Warning : There is Sticky Bomb near You",
                                     PMVector2(screenWidth / 2, (screenHeight /8 ) + 9),
                                     30);
                    }    esp.DrawText(PMColor::Red(),"Sticky Bomb", PMVector2(
                            location.x, location.y + (20 * mScale)),
                                      itemTextSize);
                    esp.DrawText(PMColor::Red(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);

                }
                if(strstr(item.Name, "Spike TrapW") && iswarning){
                    if(item.Distance < 15) {
                        esp.DrawText(PMColor::Red(), "Warning : There is Spike Trap near You",
                                     PMVector2(screenWidth / 2, (screenHeight /8 ) + 9),
                                     30);
                    }    esp.DrawText(PMColor::Red(),"Spike Trap", PMVector2(
                            location.x, location.y + (20 * mScale)),
                                      itemTextSize);
                    esp.DrawText(PMColor::Red(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);

                }
                if ((strstr(item.Name, "Grenade") && isgranade) || (strstr(item.Name, "Molotov") && ismolo) || (strstr(item.Name, "Smoke") && issmoke) || (strstr(item.Name, "Sticky Bomb") && isstickeybomb) || (strstr(item.Name, "Spike Trap") && isspiketrap) ){

                    esp.DrawText(PMColor::Almond(),item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)),
                                 itemTextSize);
                    esp.DrawText(PMColor::White(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);
                }

                if (isItemName) {
                    esp.DrawText(PMColor::MagentaD(), item.Name, PMVector2(
                            location.x, location.y + (20 * mScale)),
                                 itemTextSize);

                }

                if (isItemDist) {
                    esp.DrawText(PMColor::Coral(), dist.c_str(),
                                 PMVector2(
                                         location.x,
                                         location.y + (40 * mScale)),
                                 itemTextSize);
                }
            }
        }
        count = response.VehicalCount;
        if(count>0) {
            for (int i = 0; i < count; i++) {

                VehicalData vehical = response.Vehical[i];
                if (!isValidVehical(vehical)) { continue; }

                PMVector2 location = vehical.Location;
                string dist;
                dist += to_string((int) vehical.Distance);
                dist += "m";

                if ((strstr(vehical.Name, "Buggy") && isbuggy) ||
                    (strstr(vehical.Name, "UAZ") && isUAZ) ||
                    (strstr(vehical.Name, "Dacia") && isDacia) ||
                    (strstr(vehical.Name, "Rony") && isRony) ||
                    (strstr(vehical.Name, "Mirado") && isMirado) ||
                    (strstr(vehical.Name, "Scooter") && isscooter) ||
                    (strstr(vehical.Name, "LadaNiva") && isLadaNiva) ||
                    (strstr(vehical.Name, "Pickup") && istruck)) {
                    esp.DrawText(PMColor::Orange(), vehical.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);
                    esp.DrawText(PMColor::Alien(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);

                }

                if ((strstr(vehical.Name, "Bike") && isBike) ||
                    (strstr(vehical.Name, "Trike") && istrick) ||
                    (strstr(vehical.Name, "Tuk Tuk") && isTukTuk) ||
                        (strstr(vehical.Name, "Motor Glider") && ismotarglider) ||(strstr(vehical.Name, "Ferris Car") && isFerrisCar)) {
                    esp.DrawText(PMColor::Orange(), vehical.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);
                    esp.DrawText(PMColor::Alien(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);

                }
                if ((strstr(vehical.Name, "SnowBike") && issnowbike) ||
                    (strstr(vehical.Name, "SnowMobile") && issnowmobile) ||
                    (strstr(vehical.Name, "BRDM") && isBRDM)) {
                    esp.DrawText(PMColor::Orange(), vehical.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);
                    esp.DrawText(PMColor::Alien(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);

                }
                if ((strstr(vehical.Name, "AquaRail") && isjet) ||
                    (strstr(vehical.Name, "Boat") && isBoat)) {
                    esp.DrawText(PMColor::Orange(), vehical.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);
                    esp.DrawText(PMColor::Alien(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);

                }
                if (
                        (strstr(vehical.Name, "AirDrop") && Airdrop) ||
                        (strstr(vehical.Name, "AirDropPlane") && DropPlane) ||
                        (strstr(vehical.Name, "Crate") && Crate)) {
                    esp.DrawText(PMColor::Crimson(), vehical.Name, PMVector2(
                            location.x, location.y + (20 * mScale)), itemTextSize);

                    esp.DrawText(PMColor::White(), dist.c_str(), PMVector2(
                            location.x, location.y + (40 * mScale)), itemTextSize);
                }
//                            if (strstr(vehical.Name, "Truck") && isMonstertruck) {
//                                esp.DrawText(PMColor::Orange(), vehical.Name, PMVector2(
//                                        location.x, location.y + (20 * mScale)), itemTextSize);
//                                esp.DrawText(PMColor::Green(), dist.c_str(), PMVector2(
//                                        location.x, location.y + (40 * mScale)), itemTextSize);
//                            }
            }


        }
    }
}


#endif //HACKS_H
