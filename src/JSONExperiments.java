package src;

import java.io.PrintWriter;

/* Authors: Christina Vu, Joyce Gill, and Livia Stein Freitas
 * Experiments to check that the JSON classes work as expected.
 */

public class JSONExperiments {
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);

    // create and print a JSONInteger
    JSONInteger integerTest = new JSONInteger(15);
    pen.println(integerTest.toString());

    // creaate and print a JSONReal
    JSONReal realTest = new JSONReal(2.2);
    pen.println(realTest.toString());

    // create and print a JSONString
    JSONString stringTest = new JSONString("mp9");
    pen.println(stringTest);

    // create a JSONArray, add to it, print it, and get a value
    JSONArray arrayTest = new JSONArray();
    arrayTest.add(integerTest);
    arrayTest.add(realTest);
    arrayTest.add(stringTest);
    pen.println(arrayTest.toString());
    pen.println(arrayTest.get(1));

    // create a JSONHash, set one value, then print it
    JSONHash hashTest = new JSONHash();
    hashTest.set(stringTest, integerTest);
    pen.println(hashTest.toString());
    //duplicatet test
    hashTest.set(stringTest, realTest);
    pen.println(hashTest.toString());

  }
}
