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

        ArrayList<String[]> LTnomFichierEntree = fileToTabList(path, nomFichierEntree);
        print(LTnomFichierEntree);
        System.out.println();
        writeToFile(LTnomFichierEntree, path, nomFichierSortie);

        ArrayList<String[]> LTnomFichierEntreeSansDouble = supprDoublons(LTnomFichierEntree);
         print(LTnomFichierEntreeSansDouble);

        writeToFile(LTnomFichierEntreeSansDouble, path, nomFichierSortieSansDoublon);
        ArrayList<String> LTLTnomFichierEntreeSansDouble = supprDoublon_Noms(LTnomFichierEntreeSansDouble);

        writeNamesToFile(LTLTnomFichierEntreeSansDouble, path, nomFichierSortieSansDoublonNoms);
        //printMagMaxMinPeriod(LTnomFichierEntree);

    }

    private void printMagMaxMinPeriod(ArrayList<String[]> LTinData) {
        for (String[] tab : LTinData) {
            String smin = tab[5].split(" ")[0];
            String smax = tab[6].split(" ")[0];
            String speriod = tab[7].split(" ")[0];

            double dmax, dmin, dperiod;
            String Dmax, Dmin, Dperiod;

            try {
                dmax = Double.parseDouble(smax);
                Dmax = String.valueOf(dmax);
            } catch (NumberFormatException e) {
                Dmax = "?";
            }
            try {
                dmin = Double.parseDouble(smin);
                Dmin = String.valueOf(dmin);
            } catch (NumberFormatException e) {
                Dmin = "?";
            }
            try {
                dperiod = Double.parseDouble(speriod);
                Dperiod = String.valueOf(dperiod);
            } catch (NumberFormatException e) {
                Dperiod = "?";
            }

            System.out.println(Dmin + " " + Dmax + " " + Dperiod);
        }
    }

    private void writeNamesToFile(ArrayList<String> LT, String path, String nomFichierSortie) throws IOException {
        FileWriter writer = new FileWriter(path + nomFichierSortie + ".txt");
        writer.write("AAVSO Target");
        writer.write("\r\n");
        for (String s : LT) {
            writer.write(s);
            writer.write("\r\n");
        }
        writer.close();
    }

    private void writeToFile(ArrayList<String[]> LT, String path, String nomFichierSortie) throws IOException {
        CSVWriter writer = new CSVWriter(new FileWriter(path + nomFichierSortie + ".csv"), ',');
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

        for (String[] tab : LT) {
            writer.writeNext(tab);
        }
        writer.close();

    }

    private ArrayList<String> supprDoublon_Noms(ArrayList<String[]> LT) {
        ArrayList<String> LTsansDoubleNoms = new ArrayList<>();
        for (String[] tab : LT) {
            LTsansDoubleNoms.add(tab[0]);
        }

        return LTsansDoubleNoms;
    }

    private ArrayList<String[]> supprDoublons(ArrayList<String[]> LTinData) {
        ArrayList<String[]> LTinDataDoubleDelete = new ArrayList<>();
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

    private ArrayList<String[]> fileToTabList(String path, String nomFichierEntree) throws IOException {
        ArrayList<String[]> LTinData;

        outPutFile = new String[10];
        temPoutPutFile = new String[10];
        LTinData = new ArrayList<>();
        double[] utDateNow = getDateTime(day_of_month0, month0, year0,
                hours0, minutes0, seconds0, milliseconds0);
        double[] utDateNow1 = getDateTime(day_of_month1, month1, year1,
                hours1, minutes1, seconds1, milliseconds1);

        filtreAltAz = new Filtre(20, 70, 330, 30);
        filtrePeriod = new Filtre(2, 30);// 2 jours à 30 jours
        filtreMag = new Filtre(12);

        // traitement(utDateNow0, 0);
        //  traitement(utDateNow1, 1);
        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);

        CSVReader reader = new CSVReader(new FileReader(path + nomFichierEntree + ".csv"));
        String[] nextLine;
        reader.readNext();//pass first line
//        outPutFile[0] = "name";
//        outPutFile[1] = "ra";
//        outPutFile[2] = "dec";
//        outPutFile[3] = "Const";
//        outPutFile[4] = "VarType";
//        outPutFile[5] = "MinMag";
//        outPutFile[6] = "MaxMag";
//        outPutFile[7] = "Period";
//
//        outPutFile[8] = "alt";
//        outPutFile[9] = "az";
//        LTinData.add(outPutFile);
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

//        temPoutPutFile[5] = nextLine[5];
//        temPoutPutFile[6] = nextLine[6];
//        temPoutPutFile[7] = nextLine[7];

        String smin = nextLine[5].split(" ")[0];//MaxMag
        String smax = nextLine[6].split(" ")[0];//MinMag
        String speriod = nextLine[7].split(" ")[0];//Period

        double dmax, dmin, dperiod;
        String Dmax, Dmin, Dperiod;

        try {
            dmax = Double.parseDouble(smax);
            Dmax = String.valueOf(dmax);
        } catch (NumberFormatException e) {
            Dmax = "?";
        }
        try {
            dmin = Double.parseDouble(smin);
            Dmin = String.valueOf(dmin);
        } catch (NumberFormatException e) {
            Dmin = "?";
        }
        try {
            dperiod = Double.parseDouble(speriod);
            Dperiod = String.valueOf(dperiod);
        } catch (NumberFormatException e) {
            Dperiod = "?";
        }
        temPoutPutFile[5] = Dmin;//MaxMag
        temPoutPutFile[6] = Dmax;//MinMag
        temPoutPutFile[7] = Dperiod;//Period

        double ha = lstNow - ra;
        double[] altaz = SkyAlgorithms.EquatorialToHorizontal(ha, dec, latitude);
        double altitude = altaz[0];
        temPoutPutFile[8] = Double.toString(altitude);
        double azimuth = altaz[1];
        temPoutPutFile[9] = Double.toString(azimuth);

        return validate(altitude, azimuth, Dmin, Dperiod);
    }

}
