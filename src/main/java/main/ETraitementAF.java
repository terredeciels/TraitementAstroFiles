package main;

import java.util.ArrayList;

class ETraitementAF {
    Filtre filtre;
    String[] outPutFile;
    String[] temPoutPutFile;

    final String nomFichierEntree = "index";
    final String nomFichierSortie = "aavso_azalt_";
    final String nomFichierSortieSansDoublon = "aavso_azalt_dd_";
    final String nomFichierSortieSansDoublonNoms = "aavso_azalt_dd_names_";
    final String path = "D:\\Astronomie\\";

    final double latitude = 49.0;
    final double longitude = 1.887;
    final double day_of_month0 = 22;
    final double day_of_month1 = 22;
    final double month0 = 7;
    final double month1 = 7;
    final double year0 = 2019;
    final double year1 = 2019;
    final int hours0 = 21;
    final int hours1 = 23;
    final int minutes0 = 0;
    final int minutes1 = 30;
    final int seconds0 = 0;
    final int seconds1 = 0;
    final int milliseconds0 = 0;
    final int milliseconds1 = 0;
    double leapSec = 0;// ?


    static double sexToDec(String text) {
        double X = Double.NaN;
        boolean XNegative = false;
        String[] pieces = text.replace("-", " -").replaceAll("[^0-9\\.\\-]{1,}", " ").trim().split("[^0-9\\.\\-]{1,}");
        if (pieces.length > 0) {
            X = parseDouble(pieces[0]);
            if (!Double.isNaN(X) && pieces[0].contains("-")) {
                X = -X;
                XNegative = true;
            }
            if (pieces.length > 1) {
                X += Math.abs(parseDouble(pieces[1])) / 60.0;
            }
            if (pieces.length > 2) {
                X += Math.abs(parseDouble(pieces[2])) / 3600.0;
            }
        }

        if (XNegative) {
            X = -X;
        }
        return X;
    }

    private static double parseDouble(String s) {
        return parseDouble(s, Double.NaN);

    }

    private static double parseDouble(String s, double defaultValue) {
        if (s == null) {
            return defaultValue;
        }
        try {
            defaultValue = Double.parseDouble(s);
        } catch (NumberFormatException ignored) {
        }
        return defaultValue;
    }

    double[] getDateTime(double DAY_OF_MONTH, double MONTH, double YEAR,
                         int hours, int minutes, int seconds, int milliseconds) {
        // UTC
        double[] utdate = {0, 0, 0, 0};
        double ut;

        utdate[0] = YEAR;
        utdate[1] = MONTH;
        utdate[2] = DAY_OF_MONTH;

        ut = ((double) milliseconds) / 3600000.
                + ((double) seconds) / 3600.
                + ((double) minutes) / 60.
                + ((double) hours);
        utdate[3] = ut;
        return utdate;
    }


    boolean validate(double altitude, double azimuth) {
        boolean valid = filtre.exe(altitude, azimuth);
        outPutFile = new String[10];
        if (valid)
            System.arraycopy(temPoutPutFile, 0, outPutFile, 0, temPoutPutFile.length);
        return valid;
    }

    void print(ArrayList<String[]> LTinData) {
        StringBuilder line = new StringBuilder();
        for (String[] tab : LTinData) {
            for (String s : tab) {
                line.append(s).append(";");
            }
            System.out.println(line);
            line = new StringBuilder();
        }

    }
}
