package main;

class EMain2 {
    final double latitude = 49.0;
    final double longitude = 1.887;
    double leapSec = 0;// ?
    final String nomFichierResult = "aavso_azalt_";
    final String pathFichierResult = "D:\\IdeaProjects\\TraitementAstroFiles\\";
    final String pathFichierEntree = "D:\\IdeaProjects\\TraitementAstroFiles\\aavso_target_index_convert.csv";
    final double day_of_month0 = 20;
    final double day_of_month1 = 20;
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
}
