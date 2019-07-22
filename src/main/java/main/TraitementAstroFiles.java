package main;

import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class TraitementAstroFiles extends ETraitementAF {

    private final Filtre filtre;
    private double lstNow;
    private String[] outPutFile;
    private String[] temPoutPutFile;

    private TraitementAstroFiles() throws IOException {
        outPutFile = new String[10];
        temPoutPutFile = new String[10];

        double[] utDateNow0 = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);

        filtre = new Filtre(20, 70, -30, 30);
        traitement(utDateNow0, 0);
        //  traitement(utDateNow1, 1);

    }

    public static void main(String[] args) throws IOException {
        new TraitementAstroFiles();
    }

    private void traitement(double[] utDateNow, int pass) throws IOException {
        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

        CSVWriter writer = new CSVWriter(new FileWriter(pathFichierResult + nomFichierSortie + pass + ".csv"), ',');
        CSVReader reader = new CSVReader(new FileReader(pathFichierResult + nomFichierEntree + ".csv"));
        String[] nextLine;
        reader.readNext();//pass first line
        outPutFile[0] = "name";
        outPutFile[1] = "ra";
        outPutFile[2] = "dec";
        outPutFile[3] = "Const";
        outPutFile[4] = "VarType";
        outPutFile[5] = "MinMag";
        outPutFile[6] = "MaxMag";
        outPutFile[7] = "Period";

        outPutFile[8] = "alt";
        outPutFile[9] = "az";

        writer.writeNext(outPutFile);
        while ((nextLine = reader.readNext()) != null) {
            convertToAzH(nextLine);
            writer.writeNext(outPutFile);
        }
        writer.close();
    }

    private void convertToAzH(String[] nextLine) {
        double ra, dec;
        temPoutPutFile[0] = nextLine[0];//name

        String raS = nextLine[1];
        ra = 15 * ETraitementAF.sexToDec(raS);
        temPoutPutFile[1] = Double.toString(ra);// ra
        String decS = nextLine[2];
        dec = ETraitementAF.sexToDec(decS);
        temPoutPutFile[2] = Double.toString(dec);// dec

        temPoutPutFile[3] = nextLine[3];//Const
        temPoutPutFile[4] = nextLine[4];//VarType
        temPoutPutFile[5] = nextLine[5];//MinMag
        temPoutPutFile[6] = nextLine[6];//MaxMag
        temPoutPutFile[7] = nextLine[7];//Period

//        try {
////            ra = Double.parseDouble(raS);
////            dec = Double.parseDouble(decS);
//            ra = 15 * ETraitementAF.sexToDec(raS);
//            dec = ETraitementAF.sexToDec(decS);
//        } catch (NumberFormatException e) {
//            /* @TODO */
//            ra = 0;
//            dec = 0;
//        }
        double ha = lstNow - ra;
        double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        temPoutPutFile[8] = Double.toString(altitude);
        double azimuth = altaz[1];
        temPoutPutFile[9] = Double.toString(azimuth);

        validate(altitude, azimuth);
    }

    private void validate(double altitude, double azimuth) {
        boolean valid = filtre.exe(altitude, azimuth);
        if (valid)
            System.arraycopy(temPoutPutFile, 0, outPutFile, 0, temPoutPutFile.length);
    }

}
