#ifndef HACKS_H
#define HACKS_H

#include "PMDeadeye.h"
bool isValidPlayer(PlayerData data){
    return (data.Body != PMVector2::Zero() && data.Head != PMVector2::Zero());
}

bool isValidItem(ItemData data){
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
    if(isESP){
        esp.DrawCrosshair(PMColor(0, 0, 0, 255), PMVector2(screenWidth / 2, screenHeight / 2), 42);
        if(isfov){
            esp.DrawCircle(PMColor(255, 0, 0, 200),2,PMVector2(screenWidth / 2, screenHeight / 2),300);
        }
        PMVector2 screen(screenWidth, screenHeight);
        float mScale = screenHeight / (float) 1080;
        if(isfree){
        esp.DrawText(PMColor::Red(), "DeadEyE.Gcc-org.com", PMVector2(
                (screenWidth) - 130, (screenHeight) - 60), 20);
        esp.DrawText(PMColor::Red(), "@DeadEYE_TG", PMVector2(
                 155,(screenHeight) - 60 ), 20);
            }
        Response response = getData(screenWidth, screenHeight);
        if(response.Success){
            if(isNearEnemy) {
                PMColor color =  PMColor::Black();
                PMColor bg = (response.NearEnemy > 0) ? PMColor::Yellow4() : PMColor::Green4();
                string enemyData ;
                PMColor bg1 = (response.NearEnemy > 0) ? PMColor::Orange1() : PMColor::Green1();
                PMColor bg2 = (response.NearEnemy > 0) ? PMColor::Orange2() : PMColor::Green2();
                PMColor bg3 = (response.NearEnemy > 0) ? PMColor::Orange3() : PMColor::Green3();
                if(response.NearEnemy == 0){
                enemyData = "CLEAR";

                } else
                {
                    enemyData = to_string(response.NearEnemy);
                }
                int boxWidth = 140;
                int boxHeight = 40;
                int booxWidth = 160;
                int boooxWidth = 190;
                int booooxWidth = 220;

                esp.DrawFilledRect(bg3,PMRect((screenWidth / 2) - (booooxWidth / 2), 77, booooxWidth, boxHeight));//transp
                esp.DrawFilledRect(bg2,PMRect((screenWidth / 2) - (boooxWidth / 2), 77, boooxWidth, boxHeight));//transp
                esp.DrawFilledRect(bg1,PMRect((screenWidth / 2) - (booxWidth / 2), 77, booxWidth, boxHeight));//transp
                esp.DrawFilledRect(bg,PMRect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));//solid
                esp.DrawText(color, enemyData.c_str(), PMVector2((screenWidth / 2), 108), 21);
            }

            int count = response.PlayerCount;
            if(count > 0){
                for(int i=0; i < count; i++){
                    PlayerData player = response.Players[i];
                    if(!isValidPlayer(player)){ continue;}

                    bool isTeamMate = player.TeamID == response.MyTeamID;
                    if(isTeamMate && !isTeamMateShow){ continue;}

                    PMVector2 location = player.Body;
                    if(isPlayer360 && isOutsideSafeZone(location, screen)){
                        string dist;
                        dist += to_string((int) player.Distance);
                        dist += "M";

                        PMVector2 hintDotRenderPos = pushToScreenBorder(location, screen,
                                                                      (int) ((mScale * 100) / 3));
                        PMVector2 hintTextRenderPos = pushToScreenBorder(location, screen,
                                                                       -(int) ((mScale * 36)));
                        esp.DrawFilledCircle((isTeamMate ? PMColor(0,255,0,128) : PMColor(255,0,0,128)), hintDotRenderPos, (mScale * 100));
                        esp.DrawText(PMColor::White(), dist.c_str(), hintTextRenderPos, playerTextSize);
                        continue;
                    }

                    float boxHeight = fabsf(player.Root.y - player.Head.y);
                    float boxWidth = boxHeight * 0.56;
                    PMRect Box(player.Head.x - (boxWidth / 2), player.Head.y, boxWidth, boxHeight);

                    if(isPlayerSkel){
                        esp.DrawFilledCircle(PMColor::Red(), player.Neck, boxWidth / 25);

                        esp.DrawLine(PMColor::Red(), 2, player.Neck, player.Chest);
                        esp.DrawLine(PMColor::Red(), 2, player.Chest, player.Pelvis);

                        esp.DrawLine(PMColor::Red(), 2, player.Chest, player.LShoulder);
                        esp.DrawLine(PMColor::Red(), 2, player.Chest, player.RShoulder);

                        esp.DrawLine(PMColor::Red(), 2, player.LShoulder, player.LElbow);
                        esp.DrawLine(PMColor::White(), 2, player.RShoulder, player.RElbow);

                        esp.DrawLine(PMColor::Red(), 2, player.LElbow, player.LWrist);
                        esp.DrawLine(PMColor::Red(), 2, player.RElbow, player.RWrist);

                        esp.DrawLine(PMColor::Red(), 2, player.Pelvis, player.LThigh);
                        esp.DrawLine(PMColor::Red(), 2, player.Pelvis, player.RThigh);

                        esp.DrawLine(PMColor::Red(), 2, player.LThigh, player.LKnee);
                        esp.DrawLine(PMColor::Red(), 2, player.RThigh, player.RKnee);

                        esp.DrawLine(PMColor::Red(), 2, player.LKnee, player.LAnkle);
                        esp.DrawLine(PMColor::Red(), 2, player.RKnee, player.RAnkle);
                    }

                    if(isPlayerLine){
                        esp.DrawLine((isTeamMate ? PMColor(0,255,0) : PMColor(255,0,0)), 1, PMVector2((screenWidth / 2) - (boxWidth / 2), 77+40), player.Neck);
                    }

                    if(isPlayerBox){
                        if(player.isBot){
                            esp.DrawRect((isTeamMate ? PMColor(0,255,0) : PMColor(255,0,0)), 1, Box);
                        }
                        else {

                            esp.DrawRect((isTeamMate ? PMColor(0, 255, 0) : PMColor(255, 0, 0)), 1,
                                         Box);
                        }
                    }

                    if(isPlayerName) {
                        wstring pname = player.PlayerName;
                        if(player.isBot){
                            pname += L"[ Bot ]";
                        }
                        else {
                            pname += L"[";
                            pname += to_wstring(player.TeamID);
                            pname += L"]";
                        }
                        esp.DrawPlayerText(PMColor::White(), pname.c_str(),
                                           PMVector2(Box.x + (Box.width / 2), Box.y - 12),
                                           playerTextSize);
                    }

                    if(isPlayerDist) {
                        string dist;
                        dist += "[ ";
                        dist += to_string((int) player.Distance);
                        dist += "M";
                        dist += " ]";

                        esp.DrawText(PMColor::Orange(), dist.c_str(),
                                     PMVector2(Box.x + (Box.width / 2),
                                             Box.y + Box.height + 25), playerTextSize);
                    }

                    if(isPlayerHealth) {
                        if(player.Distance > 60){
                            esp.DrawHorizontalHealthBar(
                                    PMVector2(Box.x - (35 * mScale), Box.y - 33),
                                    (80 * mScale),
                                    100, player.Health);
                        } else {
                            esp.DrawVerticalHealthBar(
                                    PMVector2(Box.x + Box.width, Box.y),
                                    boxHeight,
                                    100, player.Health);
                        }
                    }
                }
            }

            count = response.ItemsCount;
            if(count > 0){
                for(int i=0; i < count; i++){
                    ItemData item = response.Items[i];
                    if(!isValidItem(item)){ continue;}

                    if(item.isVehicle && !isVehicle){
                        continue;
                    } else if(item.isLootBox && !isLootBox){
                        continue;
                    } else if(item.isAirDrop && !isAirDrop){
                        continue;
                    } else if(item.isLootItem && !isLootItems){
                        continue;
                    }

                    PMVector2 location = item.Location;

                    string dist;
                    dist += "( ";
                    dist += to_string((int) item.Distance);
                    dist += "M";
                    dist += " )";

                    if(isItemName) {
                        esp.DrawText(PMColor::Orange(), item.Name, PMVector2(
                                location.x, location.y + (20 * mScale)), itemTextSize);
                    }
                    if(isItemDist) {
                        esp.DrawText(PMColor::Green(), dist.c_str(), PMVector2(
                                location.x, location.y + (40 * mScale)), itemTextSize);
                    }
                }
            }
        }
    }
}

#endif //HACKS_H
