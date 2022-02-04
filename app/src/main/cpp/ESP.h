#ifndef DESI_ESP_IMPORTANT_ESP_H
#define DESI_ESP_IMPORTANT_ESP_H
#include "struct.h"

class ESP {
private:
    JNIEnv *_env;
    jobject _cvsView;
    jobject _cvs;
    jclass canvasView;
    jmethodID drawline;
    jmethodID drawrect;
    jmethodID drawfilledrect;


public:
    ESP() {
        _env = nullptr;
        _cvsView = nullptr;
        _cvs = nullptr;
        canvasView = nullptr;
        drawline = nullptr;
        drawrect = nullptr;
        drawfilledrect= nullptr;
    }

    ESP(JNIEnv *env, jobject cvsView, jobject cvs) {
        this->_env = env;
        this->_cvsView = cvsView;
        this->_cvs = cvs;
        canvasView = _env->GetObjectClass(_cvsView);
        drawline = _env->GetMethodID(canvasView, "DrawLine",
                                     "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        drawrect = _env->GetMethodID(canvasView, "DrawRect",
                                     "(Landroid/graphics/Canvas;IIIIFFFFF)V");
        drawfilledrect= _env->GetMethodID(canvasView, "DrawFilledRect",
                                          "(Landroid/graphics/Canvas;IIIIFFFF)V");
    }

    bool isValid() const {
        return (_env != nullptr && _cvsView != nullptr && _cvs != nullptr);
    }

    int getWidth() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getWidth", "()I");
            _env->DeleteLocalRef(canvas);
            return _env->CallIntMethod(_cvs, width);

        }
        return 0;
    }

    int getHeight() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getHeight", "()I");
            _env->DeleteLocalRef(canvas);
            return _env->CallIntMethod(_cvs, width);
        }
        return 0;
    }

    void DrawLine(Color color, float thickness, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawline, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }
    void DrawRect(Color color, float thickness, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawFilledRect(Color color, Vec2 start, Vec2 end) {
        if (isValid()) {
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 start.x, start.y, end.x, end.y);
        }
    }
    void DebugText(char * s){
        jmethodID mid = _env->GetMethodID(canvasView, "DebugText", "(Ljava/lang/String;)V");
        jstring name = _env->NewStringUTF(s);
        _env->CallVoidMethod(_cvsView, mid, name);
        _env->DeleteLocalRef(name);
    }

    void DrawText(Color color, const char *txt, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawText",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }
    void DrawWeapon(Color color, int wid,int ammo, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawWeapon",
                                                   "(Landroid/graphics/Canvas;IIIIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 wid,ammo, pos.x, pos.y, size);
        }
    }
    void DrawName(Color color, const char *txt,int teamid, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawName",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;IFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 s,teamid, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }
    void DrawItems(const char *txt, float distance, Vec2 pos, float size) {



        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawItems",
                                                   "(Landroid/graphics/Canvas;Ljava/lang/String;FFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs,
                                 s,distance, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }
    void DrawVehicles(const char *txt, float distance, Vec2 pos, float size) {
        if (isValid()) {
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawVehicles",
                                                   "(Landroid/graphics/Canvas;Ljava/lang/String;FFFF)V");
            jstring s=_env->NewStringUTF(txt);
            _env->CallVoidMethod(_cvsView, drawtext, _cvs,
                                 s ,distance, pos.x, pos.y, size);
            _env->DeleteLocalRef(s);
        }
    }
    void DrawItemByID(int itemID, float distance, Vec2 pos, float size) {
        jmethodID drawitemid = _env->GetMethodID(canvasView, "DrawItems",
                                       "(Landroid/graphics/Canvas;IFFFF)V");
        _env->CallVoidMethod(_cvsView, drawitemid, _cvs,
                             itemID,distance, pos.x, pos.y, size);
    }
    void DrawVehiclesWN(float distance, Vec2 pos, float size) {
        jmethodID drawvechwn = _env->GetMethodID(canvasView, "DrawVehicles",
                                                 "(Landroid/graphics/Canvas;FFFF)V");
        _env->CallVoidMethod(_cvsView, drawvechwn, _cvs,
                             distance, pos.x, pos.y, size);

    }
    void DrawFilledCircle(Color color, Vec2 pos, float radius) {
        if (isValid()) {
            jmethodID drawfilledcircle = _env->GetMethodID(canvasView, "DrawFilledCircle",
                                                           "(Landroid/graphics/Canvas;IIIIFFF)V");

            _env->CallVoidMethod(_cvsView, drawfilledcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius);
        }
    }

    void DrawCircle(Color color, Vec2 pos, float radius,float thickness) {
        if (isValid()) {
            jmethodID drawcircle = _env->GetMethodID(canvasView, "DrawCircle",
                                                           "(Landroid/graphics/Canvas;IIIIFFFF)V");

            _env->CallVoidMethod(_cvsView, drawcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius,thickness);
        }
    }

};


#endif //DESI_ESP_IMPORTANT_ESP_H
