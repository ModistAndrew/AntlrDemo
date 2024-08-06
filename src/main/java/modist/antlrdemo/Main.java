package modist.antlrdemo;

public class Main {

    public static void main(String[] args) {
        // Create a new instance of the ExprLexer
        int[] array = {1, 2, 3, 4, 5, 6, 7, 8};
        int i ;
        for (i = 0; i < array.length; i++) {
            System.out.println(array[i]);
        }
        int[][] tokens = {{i, 4, 3, 5, 1, 6, 8, },{2, 7,},{}};
    }
}
