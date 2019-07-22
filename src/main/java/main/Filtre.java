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
        azimuth = azimuth - 360;
        return !(altitude > altmax) && !(altitude < altmin)
                && !(azimuth > azmax) && !(azimuth < azmin);
    }

}
