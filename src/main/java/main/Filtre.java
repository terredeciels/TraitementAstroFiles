package main;

class Filtre {

    private double altmax;
    private double altmin;
    private double azmax;
    private double azmin;

    Filtre(double altmin, double altmax, double azmin, double azmax) {
        this.altmin = altmin;
        this.altmax = altmax;
        this.azmin = azmin;
        this.azmax = azmax;
    }

    boolean exe(double altitude, double azimuth) {
        //new Filtre(20, 70, 330, 30);
        boolean v = altitude < altmax && altitude > altmin;
        v = v && (azimuth < azmax && azimuth >= 0 || azimuth > azmin && azimuth <= 360);
        return v;
    }

}
