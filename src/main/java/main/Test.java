package main;


public class Test {


    public static void main(String[] args) {
        double res = ETraitementAF.sexToDec("01h 45m 03s");
        res = 15 * res;
        System.out.println(res);

        //res = CoordinateParseUtils.parseDMS("+37d 56m 33s");
        // System.out.println(res);
    }
}
