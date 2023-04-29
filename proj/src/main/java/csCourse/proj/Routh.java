package csCourse.proj;
import java.util.Arrays;
class Routh {
    public static  int routh_hurwitz(double[] coefficients) {
        double EPS = 0.0001;
        int len = coefficients.length;
        int n = Math.round((float) len / (float) 2.0);
        int f = 0, s = 0;

        double[] firstRow = new double[n];
        double[] secondRow = new double[n];

        for (int i = 0; i < len; i++) {
            if (i % 2 == 0) {
                firstRow[f++] = coefficients[i];
            } else {
                secondRow[s++] = coefficients[i];
            }
        }

        double[][] table = new double[len][n];
        for (int i = 0; i < len; i++) {
            Arrays.fill(table[i], 0.0);
        }

        for (int j = 0; j < n; j++) {
            table[0][j] = firstRow[j];
            table[1][j] = secondRow[j];
        }

        for (int i = 2; i < len; i++) {
            if (table[i - 1][0] == 0) {
                table[i - 1][0] = EPS;
            }
            for (int j = 0; j < n - 1; j++) {
                table[i][j] = ((table[i - 1][0] * table[i - 2][j + 1]) - (table[i - 2][0] * table[i - 1][j + 1]))
                        / table[i - 1][0];
            }


            boolean flag = false;
            for (int k = 0; k < n; k++) {
                if (table[i][k] != 0) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                int Order = len - i;
                int c = 0;
                for (int j = 0; j < n - 1; j++) {
                    table[i][j] = (Order - c) * table[i - 1][j];
                    c += 2;
                }
            }
        }
            int rightPoles = 0;
            for (int i = 0; i < len - 1; i++) {
                if ((table[i][0] * table[i + 1][0]) < 0) {
                    rightPoles++;
                }
            }

        if(rightPoles>0){
            System.out.println("System is Unstable");
        }
        else{
            System.out.println("System is Stable");
        }
        System.out.print("Number of right poles = ");
        return rightPoles;
    }

}
