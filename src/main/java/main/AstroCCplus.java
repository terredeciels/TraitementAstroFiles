package main;

import Coordinate_Converter.astroj.SkyAlgorithms;

import java.util.Arrays;

import static astronomy.IJU.decToSex;

public class AstroCCplus {
    //double[] utDateNow = {0.0, 0.0, 0.0, 0.0};

    private AstroCCplus() {
        double lon = 1.887;
        double leapSec = 0; // ?

       // double[] utDateNow = SkyAlgorithms.UTDateNow();

        double[] utDateNow = getDateTime(19,7,2019,
                21,0,0,0);
        System.out.println("utDateNow = "+ Arrays.toString(utDateNow));

        double lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], lon, leapSec);
        System.out.println("LST now (°) : " + lstNow);
        String lstNowSex = decToSex(lstNow, 0, 24, false);
        System.out.println("LST now (hms) : " + lstNowSex);
//         lstNow = SkyAlgorithms.LSTNow(longitude,leapSec);
//        System.out.println(lstNow);
//         lstNowSex = decToSex(lstNow, 0, 24, false);
//        System.out.println(lstNowSex);
        double lat = 49.0;
        double alt = 30.0;
        double az = 350.0;
        calcul(lstNow, lat, alt, az);
        alt = 30.0;
        az = 10.0;
        calcul(lstNow, lat, alt, az);
        alt = 50.0;
        az = 350.0;
        calcul(lstNow, lat, alt, az);
        alt = 50.0;
        az = 10.0;
        calcul(lstNow, lat, alt, az);
//        final double[] hadec = SkyAlgorithms.HorizontalToEquatorial(alt, az, latitude);
//        System.out.println("ha = " + hadec[0] + "dec = " + hadec[1]);
//        double Ra = (lstNow - hadec[0]);
//        if (Ra < 0) Ra += 24;
//        System.out.println(Ra);
    }

    public static void main(String[] args) {
        AstroCCplus astroCCplus = new AstroCCplus();
    }

    private void calcul(double lstNow, double lat, double alt, double az) {
        final double[] hadec = SkyAlgorithms.HorizontalToEquatorial(alt, az, lat);
        // System.out.println("ha = " + hadec[0] + "dec = " + hadec[1]);
        double dec = hadec[1];
        double Ra = (lstNow - hadec[0]);
        if (Ra < 0) Ra += 24;
        System.out.println("az = " + az + " ; " + "alt = " + alt + "  : ");
        System.out.println("     Ra = " + Ra);
        System.out.println("     Dec = " + dec);
    }

    public static double[] getDateTime(double DAY_OF_MONTH, double MONTH,double YEAR,
                                     int hours,int minutes,int seconds,int milliseconds) {
        // UTC
        double[] utdate = {0, 0, 0, 0};
        double ut;

        utdate[0] = YEAR;
        utdate[1] = MONTH ;
        utdate[2] = DAY_OF_MONTH;

        ut = ((double) milliseconds) / 3600000.
                + ((double) seconds) / 3600.
                + ((double) minutes) / 60.
                + ((double) hours);
        utdate[3] = ut;
        return utdate;
    }

}
