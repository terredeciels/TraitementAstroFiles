package main;

import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import static astronomy.IJU.decToSex;

public class Main2 extends EMain2 {

    private double lstNow;
    private String[] outPutFile;

    public static void main(String[] args) throws IOException {
        new Main2();
    }

    private Main2() throws IOException {
        outPutFile = new String[5];

        double[] utDateNow0 = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);

        traitement(utDateNow0,0);
        traitement(utDateNow1,1);

    }

    private void traitement(double[] utDateNow,int pass) throws IOException {
        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

        CSVWriter writer = new CSVWriter(new FileWriter(pathFichierResult+ nomFichierResult +pass+".csv"), ',');
        CSVReader reader = new CSVReader(new FileReader(pathFichierEntree));
        String[] nextLine;
        reader.readNext();//pass first line
        outPutFile[0] = "name";
        outPutFile[1] = "ra";
        outPutFile[2] = "dec";
        outPutFile[3] = "alt";
        outPutFile[4] = "az";
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
        double ra = Double.parseDouble(raS);
        double dec = Double.parseDouble(decS);
        double ha = lstNow - ra;
        final double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        outPutFile[3] = Double.toString(altitude);
        double azimuth = altaz[1];
        outPutFile[4] = Double.toString(azimuth);
    }

}
