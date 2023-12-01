package src;

import java.io.PrintWriter;

public class JSONExperiments {
  public static void main(String[] args) throws Exception {
    PrintWriter pen = new PrintWriter(System.out, true);

    JSONInteger integerTest = new JSONInteger(15);
    pen.println(integerTest.toString());

    JSONReal realTest = new JSONReal(2.2);
    pen.println(realTest.toString());

    JSONString stringTest = new JSONString("mp9");

    JSONArray arrayTest = new JSONArray();
    arrayTest.add(integerTest);
    arrayTest.add(realTest);
    arrayTest.add(stringTest);
    pen.println(arrayTest.toString());
    pen.println(arrayTest.get(1));

    //create, set one value, then print a hash
    JSONHash hashTest = new JSONHash();
    hashTest.set(stringTest, integerTest);
    pen.println(hashTest.toString());
    //duplicatet test
    hashTest.set(stringTest, realTest);
    pen.println(hashTest.toString());

  }
}
