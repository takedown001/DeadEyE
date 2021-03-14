#ifndef RECT_H
#define RECT_H

class PMRect {
public:
    float x;
    float y;
    float width;
    float height;

    PMRect(){
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

    bool operator==(const PMRect &src) const {
        return (src.x == this->x && src.y == this->y && src.height == this->height &&
                src.width == this->width);
    }

    bool operator!=(const PMRect &src) const {
        return (src.x != this->x && src.y != this->y && src.height != this->height &&
                src.width != this->width);
    }

    bool isZero() const {
        return (this->x == 0 && this->y == 0 && this->height == 0 && this->width == 0);
    }
};

#endif
