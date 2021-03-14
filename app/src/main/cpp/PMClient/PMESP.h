#ifndef ESP_H
#define ESP_H

#include <jni.h>
#include "PMColor.h"
#include "PMRect.h"
#include "PMVector2.h"
#include "PMVector3.h"

class PMESP {
private:
    JNIEnv *_env;
    jobject _cvsView;
    jobject _cvs;
public:
    PMESP() {
        _env = nullptr;
        _cvsView = nullptr;
        _cvs = nullptr;
    }

    PMESP(JNIEnv *env, jobject cvsView, jobject cvs) {
        this->_env = env;
        this->_cvsView = cvsView;
        this->_cvs = cvs;
    }

    JNIEnv *getEnviroument() const {
        return _env;
    }

    jobject getEspView() const {
        return _cvsView;
    }

    jobject getCanavas() const {
        return _cvs;
    }

    bool isValid() const {
        return (_env != nullptr && _cvsView != nullptr && _cvs != nullptr);
    }

    int getWidth() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getWidth", "()I");
            return _env->CallIntMethod(_cvs, width);
        }
        return 0;
    }

    int getHeight() const {
        if (isValid()) {
            jclass canvas = _env->GetObjectClass(_cvs);
            jmethodID width = _env->GetMethodID(canvas, "getHeight", "()I");
            return _env->CallIntMethod(_cvs, width);
        }
        return 0;
    }

    void invalidate() {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID inavlidate = _env->GetMethodID(canvasView, "invalidate", "()V");
            _env->CallVoidMethod(_cvsView, inavlidate);
        }
    }

    void postInvalidate() {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID inavlidate = _env->GetMethodID(canvasView, "postInvalidate", "()V");
            _env->CallVoidMethod(_cvsView, inavlidate);
        }
    }

    void ClearCanvas() {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID clearCanvas = _env->GetMethodID(canvasView, "ClearCanvas",
                                                      "(Landroid/graphics/Canvas;)V");
            _env->CallVoidMethod(_cvsView, clearCanvas, _cvs);
        }
    }

    void DrawLine(PMColor color, float thickness, PMVector2 start, PMVector2 end) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawline = _env->GetMethodID(canvasView, "DrawLine",
                                                   "(Landroid/graphics/Canvas;IIIIFFFFF)V");
            _env->CallVoidMethod(_cvsView, drawline, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 start.x, start.y, end.x, end.y);
        }
    }

    void DrawText(PMColor color, const char *txt, PMVector2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawText",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 _env->NewStringUTF(txt), pos.x, pos.y, size);
        }
    }

    void DrawPlayerText(PMColor color, const wchar_t *txt, PMVector2 pos, float size) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawtext = _env->GetMethodID(canvasView, "DrawText",
                                                   "(Landroid/graphics/Canvas;IIIILjava/lang/String;FFF)V");
            _env->CallVoidMethod(_cvsView, drawtext, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 wcstojstr(_env, txt), pos.x, pos.y, size);
        }
    }

    static jstring wcstojstr(JNIEnv *env, const wchar_t *input) {
        jobject bb = env->NewDirectByteBuffer((void *)input, wcslen(input) * sizeof(wchar_t));
        jstring UTF32LE = env->NewStringUTF("UTF-32LE");

        jclass charsetClass = env->FindClass("java/nio/charset/Charset");
        jmethodID forNameMethod = env->GetStaticMethodID(charsetClass, "forName", "(Ljava/lang/String;)Ljava/nio/charset/Charset;");
        jobject charset = env->CallStaticObjectMethod(charsetClass, forNameMethod, UTF32LE);

        jmethodID decodeMethod = env->GetMethodID(charsetClass, "decode", "(Ljava/nio/ByteBuffer;)Ljava/nio/CharBuffer;");
        jobject cb = env->CallObjectMethod(charset, decodeMethod, bb);

        jclass charBufferClass = env->FindClass("java/nio/CharBuffer");
        jmethodID toStringMethod = env->GetMethodID(charBufferClass, "toString", "()Ljava/lang/String;");
        jstring ret = (jstring)env->CallObjectMethod(cb, toStringMethod);

        env->DeleteLocalRef(bb);
        env->DeleteLocalRef(UTF32LE);
        env->DeleteLocalRef(charsetClass);
        env->DeleteLocalRef(charset);
        env->DeleteLocalRef(cb);
        env->DeleteLocalRef(charBufferClass);

        return ret;
    }

    void DrawCircle(PMColor color, float thickness, PMVector2 pos, float radius) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawcircle = _env->GetMethodID(canvasView, "DrawCircle",
                                                     "(Landroid/graphics/Canvas;IIIIFFFF)V");
            _env->CallVoidMethod(_cvsView, drawcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 thickness,
                                 pos.x, pos.y, radius);
        }
    }

    void DrawFilledCircle(PMColor color, PMVector2 pos, float radius) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledcircle = _env->GetMethodID(canvasView, "DrawFilledCircle",
                                                           "(Landroid/graphics/Canvas;IIIIFFF)V");
            _env->CallVoidMethod(_cvsView, drawfilledcircle, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, pos.x, pos.y, radius);
        }
    }

    void DrawRect(PMColor color, int thickness, PMRect rect) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawrect = _env->GetMethodID(canvasView, "DrawRect",
                                                   "(Landroid/graphics/Canvas;IIIIIFFFF)V");
            _env->CallVoidMethod(_cvsView, drawrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b, thickness,
                                 rect.x, rect.y, rect.width, rect.height);
        }
    }

    void DrawFilledRect(PMColor color, PMRect rect) {
        if (isValid()) {
            jclass canvasView = _env->GetObjectClass(_cvsView);
            jmethodID drawfilledrect = _env->GetMethodID(canvasView, "DrawFilledRect",
                                                         "(Landroid/graphics/Canvas;IIIIFFFF)V");
            _env->CallVoidMethod(_cvsView, drawfilledrect, _cvs, (int) color.a, (int) color.r,
                                 (int) color.g, (int) color.b,
                                 rect.x, rect.y, rect.width, rect.height);
        }
    }

    void DrawLineBorder(PMColor color, float stroke, PMRect rect) {
        // top left
        PMVector2 v1 = PMVector2(rect.x, rect.y);
        // top right
        PMVector2 v2 = PMVector2(rect.x + rect.width, rect.y);
        // bottom right
        PMVector2 v3 = PMVector2(rect.x + rect.width, rect.y + rect.height);
        // bottom left
        PMVector2 v4 = PMVector2(rect.x, rect.y + rect.height);

        // top left to top right
        DrawLine(color, stroke, v1, v2);
        // top right to bottom right
        DrawLine(color, stroke, v2, v3);
        // bottom right to bottom left
        DrawLine(color, stroke, v3, v4);
        // bottom left to top left
        DrawLine(color, stroke, v4, v1);
    }

    void DrawHorizontalHealthBar(PMVector2 screenPos, float width, float maxHealth, float currentHealth) {
        screenPos -= PMVector2(0.0f, 8.0f);
        DrawLineBorder(PMColor(0, 0, 0, 255), 3, PMRect(screenPos.x, screenPos.y, width + 2, 5.0f));
        screenPos += PMVector2(1.0f, 1.0f);
        PMColor clr = PMColor(0, 255, 0, 255);
        float hpWidth = (currentHealth * width) / maxHealth;
        if (currentHealth <= (maxHealth * 0.6)) {
            clr = PMColor(255, 255, 0, 255);
        }
        if (currentHealth < (maxHealth * 0.3)) {
            clr = PMColor(255, 0, 0, 255);
        }
        DrawLineBorder(clr, 3, PMRect(screenPos.x, screenPos.y, hpWidth, 4.0f));
    }

    void DrawVerticalHealthBar(PMVector2 screenPos, float height, float maxHealth, float currentHealth) {
        screenPos += PMVector2(8.0f, 0.0f);
        DrawLineBorder(PMColor(0, 0, 0, 255), 3, PMRect(screenPos.x, screenPos.y, 5.0f, height + 10));
        screenPos += PMVector2(1.0f, 1.0f);
        PMColor clr = PMColor(0, 255, 0, 255);
        float barHeight = (currentHealth * height) / maxHealth;
        if (currentHealth <= (maxHealth * 0.6)) {
            clr = PMColor(255, 255, 0, 255);
        }
        if (currentHealth < (maxHealth * 0.3)) {
            clr = PMColor(255, 0, 0, 255);
        }
        DrawLineBorder(clr, 3, PMRect(screenPos.x, screenPos.y, 3.0f, barHeight));
    }

    void DrawCrosshair(PMColor clr, PMVector2 center, float size = 20) {
        float x = center.x - (size / 2.0f);
        float y = center.y - (size / 2.0f);
        DrawLine(clr, 3, PMVector2(x, center.y), PMVector2(x + size, center.y));
        DrawLine(clr, 3, PMVector2(center.x, y), PMVector2(center.x, y + size));
    }
};

#endif
