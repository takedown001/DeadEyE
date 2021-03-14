#ifndef VECTOR3_H
#define VECTOR3_H

class PMVector3 {
public:
    float x;
    float y;
    float z;

    PMVector3() {
        this->x = 0;
        this->y = 0;
        this->z = 0;
    }

    PMVector3(float x, float y, float z) {
        this->x = x;
        this->y = y;
        this->z = z;
    }

    PMVector3(float x, float y) {
        this->x = x;
        this->y = y;
        this->z = 0;
    }

    static PMVector3 Zero() {
        return PMVector3(0.0f, 0.0f, 0.0f);
    }

    static PMVector3 Up() {
        return PMVector3(0.0f, 1.0f, 0.0f);
    }

    static PMVector3 Down() {
        return PMVector3(0.0f, -1.0f, 0.0f);
    }

    static PMVector3 Back() {
        return PMVector3(0.0f, 0.0f, -1.0f);
    }

    static PMVector3 Forward() {
        return PMVector3(0.0f, 0.0f, 1.0f);
    }

    static PMVector3 Left() {
        return PMVector3(-1.0f, 0.0f, 0.0f);
    }

    static PMVector3 Right() {
        return PMVector3(1.0f, 0.0f, 0.0f);
    }

    float &operator[](int i) {
        return ((float *) this)[i];
    }

    float operator[](int i) const {
        return ((float *) this)[i];
    }

    bool operator==(const PMVector3 &src) const {
        return (src.x == x) && (src.y == y) && (src.z == z);
    }

    bool operator!=(const PMVector3 &src) const {
        return (src.x != x) || (src.y != y) || (src.z != z);
    }

    PMVector3 &operator+=(const PMVector3 &v) {
        x += v.x;
        y += v.y;
        z += v.z;
        return *this;
    }

    PMVector3 &operator-=(const PMVector3 &v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        return *this;
    }

    PMVector3 &operator*=(float fl) {
        x *= fl;
        y *= fl;
        z *= fl;
        return *this;
    }

    PMVector3 &operator*=(const PMVector3 &v) {
        x *= v.x;
        y *= v.y;
        z *= v.z;
        return *this;
    }

    PMVector3 &operator/=(const PMVector3 &v) {
        x /= v.x;
        y /= v.y;
        z /= v.z;
        return *this;
    }

    PMVector3 &operator+=(float fl) {
        x += fl;
        y += fl;
        z += fl;
        return *this;
    }

    PMVector3 &operator/=(float fl) {
        x /= fl;
        y /= fl;
        z /= fl;
        return *this;
    }

    PMVector3 &operator-=(float fl) {
        x -= fl;
        y -= fl;
        z -= fl;
        return *this;
    }

    PMVector3 &operator=(const PMVector3 &vOther) {
        x = vOther.x;
        y = vOther.y;
        z = vOther.z;
        return *this;
    }

    PMVector3 operator-(void) const {
        return PMVector3(-x, -y, -z);
    }

    PMVector3 operator+(const PMVector3 &v) const {
        return PMVector3(x + v.x, y + v.y, z + v.z);
    }

    PMVector3 operator-(const PMVector3 &v) const {
        return PMVector3(x - v.x, y - v.y, z - v.z);
    }

    PMVector3 operator*(float fl) const {
        return PMVector3(x * fl, y * fl, z * fl);
    }

    PMVector3 operator*(const PMVector3 &v) const {
        return PMVector3(x * v.x, y * v.y, z * v.z);
    }

    PMVector3 operator/(float fl) const {
        return PMVector3(x / fl, y / fl, z / fl);
    }

    PMVector3 operator/(const PMVector3 &v) const {
        return PMVector3(x / v.x, y / v.y, z / v.z);
    }

    static float Distance(PMVector3 a, PMVector3 b) {
        PMVector3 vector = PMVector3(a.x - b.x, a.y - b.y, a.z - b.z);
        return sqrt(((vector.x * vector.x) + (vector.y * vector.y)) + (vector.z * vector.z));
    }

    static float Dot(PMVector3 lhs, PMVector3 rhs) {
        return (((lhs.x * rhs.x) + (lhs.y * rhs.y)) + (lhs.z * rhs.z));
    }

    float sqrMagnitude() const {
        return (x * x + y * y + z * z);
    }
};

#endif
