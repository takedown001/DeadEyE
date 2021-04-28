#ifndef STRUCTSCOMM_H
#define STRUCTSCOMM_H

#include "PMDeadeye.h"

using namespace std;

template<class T>
struct TArray {
	kaddr Data;
    int Count;

	inline kaddr operator[](int i)
	{
		return Data + (i * sizeof(T));
	};

	T get(int i, bool deref = false) {
		kaddr ptrData = Data + (i * sizeof(T));

		if (deref)
			ptrData = getPtr(ptrData);

		return Read<T>(ptrData);
	}

	void set(T value, int i, bool deref = false) {
		kaddr ptrData = Data + (i * sizeof(T));

		if (deref)
			ptrData = getPtr(ptrData);

		Write<T>(ptrData, value);
	}
};

struct FString {
	kaddr Data;
    int Count;

	static int is_surrogate(UTF16 uc) {
		return (uc - 0xd800u) < 2048u;
	}

	static int is_high_surrogate(UTF16 uc) {
		return (uc & 0xfffffc00) == 0xd800;
	}

	static int is_low_surrogate(UTF16 uc) {
		return (uc & 0xfffffc00) == 0xdc00;
	}

	static wchar_t surrogate_to_utf32(UTF16 high, UTF16 low) {
		return (high << 10) + low - 0x35fdc00;
	}

	wchar_t* w_str() {
		wchar_t *output = new wchar_t[Count + 1];

		UTF16* source = ReadArr<UTF16>(Data, Count);

		for (int i = 0; i < Count; i++) {
			const UTF16 uc = source[i];
			if (!is_surrogate(uc)) {
				output[i] = uc;
			} else {
				if (is_high_surrogate(uc) && is_low_surrogate(source[i]))
					output[i] = surrogate_to_utf32(uc, source[i]);
				else
					output[i] = L'?';
			}
		}

		output[Count] = L'\0';
		return output;
    }
};

struct PMVector3 {
	float X;
	float Y;
	float Z;

	PMVector3() {
		this->X = 0;
		this->Y = 0;
		this->Z = 0;
	}

	PMVector3(float x, float y, float z) {
		this->X = x;
		this->Y = y;
		this->Z = z;
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

	float& operator[](int i) {
		return ((float*)this)[i];
	}

	float operator[](int i) const {
		return ((float*)this)[i];
	}

	bool operator==(const PMVector3& src) const {
		return (src.X == X) && (src.Y == Y) && (src.Z == Z);
	}

	bool operator!=(const PMVector3& src) const {
		return (src.X != X) || (src.Y != Y) || (src.Z != Z);
	}

	PMVector3& operator+=(const PMVector3& v) {
		X += v.X;
		Y += v.Y;
		Z += v.Z;
		return *this;
	}

	PMVector3& operator-=(const PMVector3& v) {
		X -= v.X;
		Y -= v.Y;
		Z -= v.Z;
		return *this;
	}

	PMVector3& operator*=(float fl) {
		X *= fl;
		Y *= fl;
		Z *= fl;
		return *this;
	}

	PMVector3& operator*=(const PMVector3& v) {
		X *= v.X;
		Y *= v.Y;
		Z *= v.Z;
		return *this;
	}

	PMVector3& operator/=(const PMVector3& v) {
		X /= v.X;
		Y /= v.Y;
		Z /= v.Z;
		return *this;
	}

	PMVector3& operator+=(float fl) {
		X += fl;
		Y += fl;
		Z += fl;
		return *this;
	}

	PMVector3& operator/=(float fl) {
		X /= fl;
		Y /= fl;
		Z /= fl;
		return *this;
	}

	PMVector3& operator-=(float fl) {
		X -= fl;
		Y -= fl;
		Z -= fl;
		return *this;
	}

	PMVector3& operator=(const PMVector3& vOther) {
		X = vOther.X;
		Y = vOther.Y;
		Z = vOther.Z;
		return *this;
	}

	PMVector3 operator-(void) const {
		return PMVector3(-X, -Y, -Z);
	}

	PMVector3 operator+(const PMVector3& v) const {
		return PMVector3(X + v.X, Y + v.Y, Z + v.Z);
	}

	PMVector3 operator-(const PMVector3& v) const {
		return PMVector3(X - v.X, Y - v.Y, Z - v.Z);
	}

	PMVector3 operator*(float fl) const {
		return PMVector3(X * fl, Y * fl, Z * fl);
	}

	PMVector3 operator*(const PMVector3& v) const {
		return PMVector3(X * v.X, Y * v.Y, Z * v.Z);
	}

	PMVector3 operator/(float fl) const {
		return PMVector3(X / fl, Y / fl, Z / fl);
	}

	PMVector3 operator/(const PMVector3& v) const {
		return PMVector3(X / v.X, Y / v.Y, Z / v.Z);
	}

	static float Dot(PMVector3 lhs, PMVector3 rhs) {
		return (((lhs.X * rhs.X) + (lhs.Y * rhs.Y)) + (lhs.Z * rhs.Z));
	}

	float sqrMagnitude() const {
		return (X * X + Y * Y + Z * Z);
	}

	float Magnitude() const {
		return sqrt(sqrMagnitude());
	}

	static float Distance(PMVector3 a, PMVector3 b) {
		PMVector3 vector = PMVector3(a.X - b.X, a.Y - b.Y, a.Z - b.Z);
		return sqrt(((vector.X * vector.X) + (vector.Y * vector.Y)) + (vector.Z * vector.Z));
	}
};

struct PMVector2 {
	float X;
	float Y;

	PMVector2() {
		this->X = 0;
		this->Y = 0;
	}

	PMVector2(float x, float y) {
		this->X = x;
		this->Y = y;
	}

	PMVector2 Rotate(PMVector2 centerpoint, float angle, bool bAngleInRadians = false) {
		if (!bAngleInRadians)
			angle = (float)M_PI * angle / 180.0f;

		return PMVector2(X * cosf(angle) - Y * sinf(angle), X * sinf(angle) + Y * cosf(angle));
	}
};

struct PMRect {
	float x;
	float y;
	float width;
	float height;

	PMRect() {
		this->x = 0;
		this->y = 0;
		this->width = 0;
		this->height = 0;
	}

	PMRect(float x, float y, float width, float height) {
		this->x = x;
		this->y = y;
		this->width = width;
		this->height = height;
	}

	bool operator==(const PMRect& src) const {
		return (src.x == this->x && src.y == this->y && src.height == this->height &&
			src.width == this->width);
	}

	bool operator!=(const PMRect& src) const {
		return (src.x != this->x && src.y != this->y && src.height != this->height &&
			src.width != this->width);
	}

	bool isZero() const {
		return (this->x == 0 && this->y == 0 && this->height == 0 && this->width == 0);
	}
};

struct FMatrix {
	float M[4][4];
};

struct Quat {
	float X;
	float Y;
	float Z;
	float W;
};

struct FTransform {
	Quat Rotation;
	PMVector3 Translation;
	uint8 pad[0x4];
	PMVector3 Scale3D;

	static FTransform ReadTransform(kaddr addr){
		FTransform transform = FTransform();
		transform.Rotation = Read<Quat>(addr);
		transform.Translation = Read<PMVector3>(addr + 0x10);
		transform.Scale3D = Read<PMVector3>(addr + 0x20);
		return transform;
	}
};

struct FLiteTransform {
	Quat Rotation;
	PMVector3 Translation;
	PMVector3 Scale3D;

	static FLiteTransform ReadLiteTransform(kaddr addr){
		return Read<FLiteTransform>(addr);
	}
};

PMVector3 MarixToVector(FMatrix matrix) {
	return PMVector3(matrix.M[3][0], matrix.M[3][1], matrix.M[3][2]);
}

FMatrix MatrixMulti(FMatrix m1, FMatrix m2) {
	FMatrix matrix = FMatrix();
	for (int i = 0; i < 4; i++) {
		for (int j = 0; j < 4; j++) {
			for (int k = 0; k < 4; k++) {
				matrix.M[i][j] += m1.M[i][k] * m2.M[k][j];
			}
		}
	}
	return matrix;
}

FMatrix TransformToMatrixWithScale(FTransform transform) {
	FMatrix matrix;

	matrix.M[3][0] = transform.Translation.X;
	matrix.M[3][1] = transform.Translation.Y;
	matrix.M[3][2] = transform.Translation.Z;

	float x2 = transform.Rotation.X + transform.Rotation.X;
	float y2 = transform.Rotation.Y + transform.Rotation.Y;
	float z2 = transform.Rotation.Z + transform.Rotation.Z;

	float xx2 = transform.Rotation.X * x2;
	float yy2 = transform.Rotation.Y * y2;
	float zz2 = transform.Rotation.Z * z2;

	matrix.M[0][0] = (1.0f - (yy2 + zz2)) * transform.Scale3D.X;
	matrix.M[1][1] = (1.0f - (xx2 + zz2)) * transform.Scale3D.Y;
	matrix.M[2][2] = (1.0f - (xx2 + yy2)) * transform.Scale3D.Z;

	float yz2 = transform.Rotation.Y * z2;
	float wx2 = transform.Rotation.W * x2;
	matrix.M[2][1] = (yz2 - wx2) * transform.Scale3D.Z;
	matrix.M[1][2] = (yz2 + wx2) * transform.Scale3D.Y;

	float xy2 = transform.Rotation.X * y2;
	float wz2 = transform.Rotation.W * z2;
	matrix.M[1][0] = (xy2 - wz2) * transform.Scale3D.Y;
	matrix.M[0][1] = (xy2 + wz2) * transform.Scale3D.X;

	float xz2 = transform.Rotation.X * z2;
	float wy2 = transform.Rotation.W * y2;
	matrix.M[2][0] = (xz2 + wy2) * transform.Scale3D.Z;
	matrix.M[0][2] = (xz2 - wy2) * transform.Scale3D.X;

	matrix.M[0][3] = 0;
	matrix.M[1][3] = 0;
	matrix.M[2][3] = 0;
	matrix.M[3][3] = 1;

	return matrix;
}

struct FRotator {
	float Pitch;
	float Yaw;
	float Roll;
};

FMatrix RotToMatrix(FRotator rotation) {
	float radPitch = rotation.Pitch * ((float)M_PI / 180.0f);
	float radYaw = rotation.Yaw * ((float)M_PI / 180.0f);
	float radRoll = rotation.Roll * ((float)M_PI / 180.0f);

	float SP = sinf(radPitch);
	float CP = cosf(radPitch);
	float SY = sinf(radYaw);
	float CY = cosf(radYaw);
	float SR = sinf(radRoll);
	float CR = cosf(radRoll);

	FMatrix matrix;

	matrix.M[0][0] = (CP * CY);
	matrix.M[0][1] = (CP * SY);
	matrix.M[0][2] = (SP);
	matrix.M[0][3] = 0;

	matrix.M[1][0] = (SR * SP * CY - CR * SY);
	matrix.M[1][1] = (SR * SP * SY + CR * CY);
	matrix.M[1][2] = (-SR * CP);
	matrix.M[1][3] = 0;

	matrix.M[2][0] = (-(CR * SP * CY + SR * SY));
	matrix.M[2][1] = (CY * SR - CR * SP * SY);
	matrix.M[2][2] = (CR * CP);
	matrix.M[2][3] = 0;

	matrix.M[3][0] = 0;
	matrix.M[3][1] = 0;
	matrix.M[3][2] = 0;
	matrix.M[3][3] = 1;

	return matrix;
}

struct MinimalViewInfo {
	PMVector3 Location;
	PMVector3 LocationLocalSpace;
	FRotator Rotation;
	float FOV;
};

struct CameraCacheEntry {
	float TimeStamp;
	uint8 pad[0xC];
	MinimalViewInfo POV;
};

PMVector2 WorldToScreen(PMVector3 worldLocation, MinimalViewInfo camViewInfo, int width, int height) {
	FMatrix tempMatrix = RotToMatrix(camViewInfo.Rotation);

	PMVector3 vAxisX(tempMatrix.M[0][0], tempMatrix.M[0][1], tempMatrix.M[0][2]);
	PMVector3 vAxisY(tempMatrix.M[1][0], tempMatrix.M[1][1], tempMatrix.M[1][2]);
	PMVector3 vAxisZ(tempMatrix.M[2][0], tempMatrix.M[2][1], tempMatrix.M[2][2]);

	PMVector3 vDelta = worldLocation - camViewInfo.Location;

	PMVector3 vTransformed(PMVector3::Dot(vDelta, vAxisY), PMVector3::Dot(vDelta, vAxisZ), PMVector3::Dot(vDelta, vAxisX));

	if (vTransformed.Z < 1.0f) {
		vTransformed.Z = 1.0f;
	}

	float fov = camViewInfo.FOV;
	float screenCenterX = (width / 2.0f);
	float screenCenterY = (height / 2.0f);

	return PMVector2(
		(screenCenterX + vTransformed.X * (screenCenterX / tanf(fov * ((float)M_PI / 360.0f))) / vTransformed.Z),
		(screenCenterY - vTransformed.Y * (screenCenterX / tanf(fov * ((float)M_PI / 360.0f))) / vTransformed.Z)
	);
}



FRotator ToRotator(PMVector3 local, PMVector3 target) {
	PMVector3 rotation = local - target;

	FRotator newViewAngle = {0};

	float hyp = sqrt(rotation.X * rotation.X + rotation.Y * rotation.Y);

	newViewAngle.Pitch = -atan(rotation.Z / hyp) * (180.f / 3.14159265358979323846f);
	newViewAngle.Yaw = atan(rotation.Y / rotation.X) * (180.f / 3.14159265358979323846f);
	newViewAngle.Roll = (float) 0.f;

	if (rotation.X >= 0.f)
		newViewAngle.Yaw += 180.0f;

	return newViewAngle;
}


#endif
