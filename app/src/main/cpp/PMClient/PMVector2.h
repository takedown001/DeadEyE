#ifndef PMVECTOR2_H
#define PMVECTOR2_H

class PMVector2 {
public:
    float x;
    float y;

    PMVector2() {
        Init();
    }

    PMVector2(float X, float Y) {
        Init(X, Y);
    }

    void Init(float ix = 0.0f, float iy = 0.0f) {
        x = ix;
        y = iy;
    }

    static PMVector2 Zero() {
        return PMVector2(0.0f, 0.0f);
    }

    static PMVector2 Up() {
        return PMVector2(0.0f, 1.0f);
    }

    static PMVector2 Down() {
        return PMVector2(0.0f, -1.0f);
    }

    static PMVector2 Back() {
        return PMVector2(0.0f, 0.0f);
    }

    static PMVector2 Forward() {
        return PMVector2(0.0f, 0.0f);
    }

    static PMVector2 Left() {
        return PMVector2(-1.0f, 0.0f);
    }

    static PMVector2 Right() {
        return PMVector2(1.0f, 0.0f);
    }

    float &operator[](int i) {
        return ((float *) this)[i];
    }

    float operator[](int i) const {
        return ((float *) this)[i];
    }

    bool operator==(const PMVector2 &src) const {
        return (src.x == x) && (src.y == y);
    }

    bool operator!=(const PMVector2 &src) const {
        return (src.x != x) || (src.y != y);
    }

    PMVector2 &operator+=(const PMVector2 &v) {
        x += v.x;
        y += v.y;
        return *this;
    }

    PMVector2 &operator-=(const PMVector2 &v) {
        x -= v.x;
        y -= v.y;
        return *this;
    }

    PMVector2 &operator*=(float fl) {
        x *= fl;
        y *= fl;
        return *this;
    }

    PMVector2 &operator*=(const PMVector2 &v) {
        x *= v.x;
        y *= v.y;
        return *this;
    }

    PMVector2 &operator/=(const PMVector2 &v) {
        x /= v.x;
        y /= v.y;
        return *this;
    }

    PMVector2 &operator+=(float fl) {
        x += fl;
        y += fl;
        return *this;
    }

    PMVector2 &operator/=(float fl) {
        x /= fl;
        y /= fl;
        return *this;
    }

    PMVector2 &operator-=(float fl) {
        x -= fl;
        y -= fl;
        return *this;
    }

    PMVector2 &operator=(const PMVector2 &vOther) {
        x = vOther.x;
        y = vOther.y;
        return *this;
    }

    PMVector2 operator-(void) const {
        return PMVector2(-x, -y);
    }

    PMVector2 operator+(const PMVector2 &v) const {
        return PMVector2(x + v.x, y + v.y);
    }

    PMVector2 operator-(const PMVector2 &v) const {
        return PMVector2(x - v.x, y - v.y);
    }

    PMVector2 operator*(float fl) const {
        return PMVector2(x * fl, y * fl);
    }

    PMVector2 operator*(const PMVector2 &v) const {
        return PMVector2(x * v.x, y * v.y);
    }

    PMVector2 operator/(float fl) const {
        return PMVector2(x / fl, y / fl);
    }

    PMVector2 operator/(const PMVector2 &v) const {
        return PMVector2(x / v.x, y / v.y);
    }
};

#endif
