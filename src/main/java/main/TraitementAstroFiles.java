package main;

import Coordinate_Converter.astroj.SkyAlgorithms;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class TraitementAstroFiles extends ETraitementAF {

    private double lstNow;

    public static void main(String[] args) throws IOException {
        new TraitementAstroFiles().traitement();
    }

    private void traitement() throws IOException {
        final String nomFichierEntree = "index";
        final String pathFichierResult = "D:\\Astronomie\\";
        ArrayList<String[]> LTinData = indexFileAAVSOtoTabList(pathFichierResult, nomFichierEntree);
        // print(LTinData);
        writeToFile(LTinData,pathFichierResult, "aavso_azalt_");

        ArrayList<String[]> LTinDataDoubleDelete = deleteDouble(LTinData);
       // print(LTinDataDoubleDelete);
        final String nomFichierSortie = "aavso_azalt_dd_";
        writeToFile(LTinDataDoubleDelete,pathFichierResult, nomFichierSortie);

    }

    private void writeToFile(ArrayList<String[]> LTinDataDoubleDelete,String pathFichierResult, String nomFichierSortie) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(pathFichierResult + nomFichierSortie + ".csv"), ',');
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

        for (String[] tab : LTinDataDoubleDelete) {
            writer.writeNext(tab);
        }
        writer.close();

    }

    private ArrayList<String[]> deleteDouble(ArrayList<String[]> LTinData) {
        ArrayList<String[]> LTinDataDoubleDelete = new ArrayList<>();
        ;
        String currentName = "";
        boolean doublon;
        boolean currentLine = false;

        for (String[] tab : LTinData) {

            if (currentLine) {
                doublon = tab[0].equals(currentName);
                if (!doublon)
                    // System.out.println(tab[0]);
                    LTinDataDoubleDelete.add(tab);
                currentName = tab[0];
            }
            currentLine = !currentLine;

        }
        return LTinDataDoubleDelete;
    }

    private ArrayList<String[]> indexFileAAVSOtoTabList(String pathFichierResult, String nomFichierEntree) throws IOException {
        ArrayList<String[]> LTinData;

        outPutFile = new String[10];
        temPoutPutFile = new String[10];
        LTinData = new ArrayList<>();
        double[] utDateNow = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);
        filtre = new Filtre(20, 70, 330, 30);
        // traitement(utDateNow0, 0);
        //  traitement(utDateNow1, 1);
        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

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
        LTinData.add(outPutFile);
        // writer.writeNext(outPutFile);
        while ((nextLine = reader.readNext()) != null) {
            boolean Valid = convertToAzH(nextLine);
            if (Valid)
                LTinData.add(outPutFile);
            //writer.writeNext(outPutFile);
        }
        // writer.close();

        return LTinData;
    }

    private boolean convertToAzH(String[] nextLine) {
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

        double ha = lstNow - ra;
        double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        temPoutPutFile[8] = Double.toString(altitude);
        double azimuth = altaz[1];
        temPoutPutFile[9] = Double.toString(azimuth);

        return validate(altitude, azimuth);
    }

}
