import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import static astronomy.IJU.decToSex;

public class Main {
    private static double lat = 49.0;
    private static double lon = 1.887;
    private static double leapSec = 0;// ?
    private final double lstNow;
    private String[] outPutFile;

    private Main() throws IOException {
        outPutFile = new String[5];

        double[] utDateNow = getDateTime(19, 7, 2019,
                21, 0, 0, 0);
        System.out.println("utDateNow = " + Arrays.toString(utDateNow));

        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], lon, leapSec);
        System.out.println("LST now (°) : " + lstNow);
        String lstNowSex = decToSex(lstNow, 0, 24, false);
        System.out.println("LST now (hms) : " + lstNowSex);

        CSVWriter writer = new CSVWriter(new FileWriter("D:\\IdeaProjects\\TraitementAstroFiles\\aavso_azalt.csv"), ',');
        CSVReader reader = new CSVReader(new FileReader("D:\\IdeaProjects\\TraitementAstroFiles\\aavso_target_index_convert.csv"));

        String[] nextLine;
        reader.readNext();//pass first line
        outPutFile[0]="name";outPutFile[1]="ra";outPutFile[2]="dec";outPutFile[3]="alt";outPutFile[4]="az";
        writer.writeNext(outPutFile);
        while ((nextLine = reader.readNext()) != null) {
            convertToAzH(nextLine);
            writer.writeNext(outPutFile);
        }
        writer.close();
    }

    private void convertToAzH(String[] nextLine) {
        //System.out.println(raS + " " + decS);
        String name = nextLine[0];
        outPutFile[0] = name;
        String raS = nextLine[1];
        outPutFile[1] = raS;
        String decS = nextLine[2];
        outPutFile[2] = decS;
        double ra = Double.parseDouble(raS);
        double dec = Double.parseDouble(decS);
        double ha = lstNow - ra;
        final double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, lat);
        double altitude = altaz[0];
        outPutFile[3] = Double.toString(altitude);
        double azimuth = altaz[1];
        outPutFile[4] = Double.toString(azimuth);
        System.out.println(name + " " + azimuth + " " + altitude + " " + raS + " " + decS);
    }

    public static void main(String[] args) throws IOException {
        new Main();

    }

    private double[] getDateTime(double DAY_OF_MONTH, double MONTH, double YEAR,
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
