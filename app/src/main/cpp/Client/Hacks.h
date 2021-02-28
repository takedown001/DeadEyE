#ifndef HACKS_H
#define HACKS_H

#include "kmods.h"

bool isValidPlayer(PlayerData data){
    return (data.Body != Vector2::Zero() && data.Head != Vector2::Zero());
}

bool isValidItem(ItemData data){
    return (data.Location != Vector2::Zero());
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

Vector2 pushToScreenBorder(Vector2 Pos, Vector2 screen, int offset) {
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
    return Vector2(x, y);
}

bool isOutsideSafeZone(Vector2 pos, Vector2 screen) {
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

void DrawESP(ESP esp, int screenWidth, int screenHeight) {
    if(isESP){
        esp.DrawCrosshair(Color(0, 0, 0, 255), Vector2(screenWidth / 2, screenHeight / 2), 42);

        Vector2 screen(screenWidth, screenHeight);
        float mScale = screenHeight / (float) 1080;

        Response response = getData(screenWidth, screenHeight);
        if(response.Success){
            if(isNearEnemy) {
                Color color = (response.NearEnemy > 0) ? Color::Red() : Color::Green();

                string enemyData = "Enemy: ";
                enemyData += to_string(response.NearEnemy);

                int boxWidth = 180;
                int boxHeight = 60;

                esp.DrawRect(color, 4, Rect((screenWidth / 2) - (boxWidth / 2), 77, boxWidth, boxHeight));
                esp.DrawText(color, enemyData.c_str(), Vector2((screenWidth / 2), 117), 30);
            }

            int count = response.PlayerCount;
            if(count > 0){
                for(int i=0; i < count; i++){
                    PlayerData player = response.Players[i];
                    if(!isValidPlayer(player)){ continue;}

                    bool isTeamMate = player.TeamID == response.MyTeamID;
                    if(isTeamMate && !isTeamMateShow){ continue;}

                    Vector2 location = player.Body;
                    if(isPlayer360 && isOutsideSafeZone(location, screen)){
                        string dist;
                        dist += to_string((int) player.Distance);
                        dist += "M";

                        Vector2 hintDotRenderPos = pushToScreenBorder(location, screen,
                                                                      (int) ((mScale * 100) / 3));
                        Vector2 hintTextRenderPos = pushToScreenBorder(location, screen,
                                                                       -(int) ((mScale * 36)));
                        esp.DrawFilledCircle((isTeamMate ? Color(0,255,0,128) : Color(255,0,0,128)), hintDotRenderPos, (mScale * 100));
                        esp.DrawText(Color::White(), dist.c_str(), hintTextRenderPos, playerTextSize);
                        continue;
                    }

                    float boxHeight = fabsf(player.Root.y - player.Head.y);
                    float boxWidth = boxHeight * 0.56;
                    Rect Box(player.Head.x - (boxWidth / 2), player.Head.y, boxWidth, boxHeight);

                    if(isPlayerSkel){
                        esp.DrawFilledCircle(Color::White(), player.Neck, boxWidth / 25);

                        esp.DrawLine(Color::White(), 2, player.Neck, player.Chest);
                        esp.DrawLine(Color::White(), 2, player.Chest, player.Pelvis);

                        esp.DrawLine(Color::White(), 2, player.Chest, player.LShoulder);
                        esp.DrawLine(Color::White(), 2, player.Chest, player.RShoulder);

                        esp.DrawLine(Color::White(), 2, player.LShoulder, player.LElbow);
                        esp.DrawLine(Color::White(), 2, player.RShoulder, player.RElbow);

                        esp.DrawLine(Color::White(), 2, player.LElbow, player.LWrist);
                        esp.DrawLine(Color::White(), 2, player.RElbow, player.RWrist);

                        esp.DrawLine(Color::White(), 2, player.Pelvis, player.LThigh);
                        esp.DrawLine(Color::White(), 2, player.Pelvis, player.RThigh);

                        esp.DrawLine(Color::White(), 2, player.LThigh, player.LKnee);
                        esp.DrawLine(Color::White(), 2, player.RThigh, player.RKnee);

                        esp.DrawLine(Color::White(), 2, player.LKnee, player.LAnkle);
                        esp.DrawLine(Color::White(), 2, player.RKnee, player.RAnkle);
                    }

                    if(isPlayerLine){
                        esp.DrawLine((isTeamMate ? Color(0,255,0) : Color(255,0,0)), 1, Vector2((screenWidth / 2), 0), player.Neck);
                    }

                    if(isPlayerBox){
                        esp.DrawRect((isTeamMate ? Color(0,255,0) : Color(255,0,0)), 1, Box);
                    }

                    if(isPlayerName) {
                        wstring pname = player.PlayerName;
                        if(player.isBot){
                            pname += L"(Bot)";
                        }
                        pname += L"[";
                        pname += to_wstring(player.TeamID);
                        pname += L"]";

                        esp.DrawPlayerText(Color::Yellow(), pname.c_str(),
                                     Vector2(Box.x + (Box.width / 2), Box.y - 12),
                                     playerTextSize);
                    }

                    if(isPlayerDist) {
                        string dist;
                        dist += "[ ";
                        dist += to_string((int) player.Distance);
                        dist += "M";
                        dist += " ]";

                        esp.DrawText(Color::Yellow(), dist.c_str(),
                                     Vector2(Box.x + (Box.width / 2),
                                             Box.y + Box.height + 25), playerTextSize);
                    }

                    if(isPlayerHealth) {
                        if(player.Distance < 80){
                            esp.DrawVerticalHealthBar(
                                    Vector2(Box.x + Box.width, Box.y),
                                    boxHeight,
                                    100, player.Health);
                        } else {
                            esp.DrawHorizontalHealthBar(
                                    Vector2(Box.x - (35 * mScale), Box.y - 33),
                                    (80 * mScale),
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

                    Vector2 location = item.Location;

                    string dist;
                    dist += "[ ";
                    dist += to_string((int) item.Distance);
                    dist += "M";
                    dist += " ]";

                    if(isItemName) {
                        esp.DrawText(Color::Yellow(), item.Name, Vector2(
                                location.x, location.y + (20 * mScale)), itemTextSize);
                    }
                    if(isItemDist) {
                        esp.DrawText(Color::White(), dist.c_str(), Vector2(
                                location.x, location.y + (40 * mScale)), itemTextSize);
                    }
                }
            }
        }
    }
}

#endif //HACKS_H
