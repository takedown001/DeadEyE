#ifndef PMCOLOR_H
#define PMCOLOR_H

class PMColor {
public:
    float r;
    float g;
    float b;
    float a;

    PMColor() {
        this->r = 0;
        this->g = 0;
        this->b = 0;
        this->a = 0;
    }

    PMColor(float r, float g, float b, float a) {
        this->r = r;
        this->g = g;
        this->b = b;
        this->a = a;
    }

    PMColor(float r, float g, float b) {
        this->r = r;
        this->g = g;
        this->b = b;
        this->a = 225;
    }

    static PMColor Clear(){
        return PMColor(0, 0, 0, 0);
    }

    static PMColor Black(){
        return PMColor(0, 0, 0);
    }

    static PMColor White(){
        return PMColor(255, 255, 255);
    }

    static PMColor Red(){
        return PMColor(255, 0, 0);
    }

    static PMColor Yellow(){
        return PMColor(255, 255, 0,250);
    }

    static PMColor Green(){
        return PMColor(0, 255, 0,200);
    }

    static PMColor Yellow4(){
        return PMColor(255, 145, 0,170);
    }

    static PMColor Green4(){
        return PMColor(0, 210, 0,170);
    }

    static PMColor Orange1(){
        return PMColor(255, 155, 50,53);
    }

    static PMColor Green1(){
        return PMColor(0, 220,0 ,150);
    }

    static PMColor Orange2(){
        return PMColor(255, 155, 60,52);
    }

    static PMColor Green2(){
        return PMColor(0, 255, 0,100);
    }

    static PMColor Orange3(){
        return PMColor(255, 255, 0,51);
    }
    static PMColor Orange(){
        return PMColor(255, 155, 0,255);
    }
    static PMColor Green3(){
        return PMColor(0, 255, 0, 50);
    }

    static PMColor Cyan(){
        return PMColor(0, 255, 255);
    }

    static PMColor Blue(){
        return PMColor(0, 0, 255);
    }

    static PMColor Purple(){
        return PMColor(128, 0, 128);
    }

    static PMColor Maroon(){
        return PMColor(128, 0, 0);
    }
};

#endif
