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

    static PMColor Clear() {
        return PMColor(0, 0, 0, 0);
    }

    static PMColor Black() {
        return PMColor(0, 0, 0);
    }

    static PMColor BlackT() {
        return PMColor(0, 0, 0, 135);
    }


    static PMColor White() {
        return PMColor(255, 255, 255);
    }

    static PMColor Red() {
        return PMColor(255, 0, 0);
    }

    static PMColor RedTT() {
        return PMColor(255, 0, 0, 200);
    }


    static PMColor RedT() {
        return PMColor(255, 0, 0, 60);
    }


    static PMColor Yellow() {
        return PMColor(255, 255, 0, 250);
    }

    static PMColor Green() {
        return PMColor(0, 255, 0, 200);
    }

    static PMColor Yellow4() {
        return PMColor(255, 145, 0, 125);
    }

    static PMColor Green4() {
        return PMColor(0, 210, 0, 125);
    }

    static PMColor Orange1() {
        return PMColor(255, 155, 50, 100);
    }

    static PMColor OrangeTT() {
        return PMColor(255, 155, 50, 200);
    }

    static PMColor Green1() {
        return PMColor(0, 220, 0, 100);
    }

    static PMColor Orange2() {
        return PMColor(255, 155, 60, 75);
    }

    static PMColor Green2() {
        return PMColor(0, 255, 0, 75);
    }

    static PMColor GreenTT() {
        return PMColor(0, 255, 0, 200);
    }

    static PMColor Orange3() {
        return PMColor(255, 255, 0, 50);
    }

    static PMColor Orange() {
        return PMColor(255, 155, 0, 255);
    }

    static PMColor OrangeD() {
        return PMColor(255, 65, 0, 255);
    }

    static PMColor Green3() {
        return PMColor(0, 255, 0, 50);
    }

    static PMColor Coral() {
    return PMColor(240,128,128);
}

    static PMColor Cyan(){
        return PMColor(0, 255, 255);
    }

    static PMColor CyanT(){
        return PMColor(0, 255, 255,60);
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

    static PMColor Grey(){
        return PMColor(195,190,190);
    }

    static PMColor Gold(){
        return PMColor(255,190,0);
    }

    static PMColor SmokeyBlack(){
        return PMColor(25,0,15);
    }

    static PMColor Crimson(){
        return PMColor(225,35,55);
    }

    static PMColor Magenta() {
        return PMColor(255,20,147);
    }

    static PMColor MagentaD() {
        return PMColor(255,192,203);
    }

    static PMColor Aero(){
        return PMColor(189,59,12);
    }

    static PMColor Almond(){
        return PMColor(235,220,205);
    }

    static PMColor Ice(){
        return PMColor(240,250,255);
    }

    static PMColor Alien(){
        return PMColor(130,220,5);
    }

    static PMColor AlienT(){
        return PMColor(130,220,5, 60);
    }

    static PMColor RedLight(){
        return PMColor(255, 0, 0,100);
    }

    static PMColor LRed(){
        return PMColor(200, 0, 0,220);
    }

    static PMColor YellowLight(){
        return PMColor(255, 255, 0,100);
    }

};

#endif
