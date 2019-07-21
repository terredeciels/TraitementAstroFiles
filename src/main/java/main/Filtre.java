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
//
//    private void traitement(double[] utDateNow, int pass) throws IOException {
//        lstNow = SkyAlgorithms.CalcLST((int) utDateNow[0], (int) utDateNow[1], (int) utDateNow[2], utDateNow[3], longitude, leapSec);
//
//        CSVWriter writer = new CSVWriter(new FileWriter(pathFichierResult + nomFichierSortie + pass + ".csv"), ',');
//        CSVReader reader = new CSVReader(new FileReader(pathFichierEntree));
//        String[] nextLine;
//        reader.readNext();//pass first line
//        outPutFile[0] = "name";
//        outPutFile[1] = "ra";
//        outPutFile[2] = "dec";
//        outPutFile[3] = "Const";
//        outPutFile[4] = "alt";
//        outPutFile[5] = "az";
//        writer.writeNext(outPutFile);
//        while ((nextLine = reader.readNext()) != null) {
//            convertToAzH(nextLine);
//            writer.writeNext(outPutFile);
//        }
//        writer.close();
//    }

}
