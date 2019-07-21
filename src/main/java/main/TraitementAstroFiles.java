package main;

import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TraitementAstroFiles extends ETraitementAF {

    private double lstNow;
    private String[] outPutFile;

    private TraitementAstroFiles() throws IOException {
        outPutFile = new String[6];

        double[] utDateNow0 = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);

        traitement(utDateNow0, 0);
        traitement(utDateNow1, 1);

    }

    public static void main(String[] args) throws IOException {
        new TraitementAstroFiles();
    }

    private void traitement(double[] utDateNow, int pass) throws IOException {
        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

        CSVWriter writer = new CSVWriter(new FileWriter(pathFichierResult + nomFichierResult + pass + ".csv"), ',');
        CSVReader reader = new CSVReader(new FileReader(pathFichierEntree));
        String[] nextLine;
        reader.readNext();//pass first line
        outPutFile[0] = "name";
        outPutFile[1] = "ra";
        outPutFile[2] = "dec";
        outPutFile[3]="Const";
        outPutFile[4] = "alt";
        outPutFile[5] = "az";
        writer.writeNext(outPutFile);
        while ((nextLine = reader.readNext()) != null) {
            convertToAzH(nextLine);
            writer.writeNext(outPutFile);
        }
        writer.close();
    }

    private void convertToAzH(String[] nextLine) {
        String name = nextLine[0];
        outPutFile[0] = name;
        String raS = nextLine[1];
        outPutFile[1] = raS;
        String decS = nextLine[2];
        outPutFile[2] = decS;
        String constS = nextLine[3];
        outPutFile[3] = constS;
        double ra = 0;
        double dec = 0;
        try {
            ra = Double.parseDouble(raS);
            dec = Double.parseDouble(decS);
        } catch (NumberFormatException e) {
           ra=0;dec=0;
        }
        double ha = lstNow - ra;
        final double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        outPutFile[4] = Double.toString(altitude);
        double azimuth = altaz[1];
        outPutFile[5] = Double.toString(azimuth);
    }

}
