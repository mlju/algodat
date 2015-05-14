import com.sun.org.apache.xpath.internal.SourceTree;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;


/**
 * This class includes the {@link #main main()}-method for the program
 * and {@link #printDebug(String)} to print debug messages
 * <p>
 * <b>IMPORTANT:</b> Do not change this class. All changes made to this class
 * are removed after the submission of the solution. Thereby, the submitted solution
 * can fail during the tests.
 * </p>
 */
public class Tester {

    /**
     * Name of file with test instances. Is <code>
     * null</code>, if read from {@link System#in}.
     */
    private static String fileName = null;

    /**
     * Adapted path
     */
    private static String choppedFileName;

    /**
     * Test flag for output during test run
     */
    private static boolean test = false;

    /**
     * Debug flag for additional debug messages
     */
    private static boolean debug = false;

    /**
     * Factor used during tests
     */
    private static int factor = 10;

    /**
     * Prints message <code>msg</code> and exits program.
     *
     * @param msg the printed message.
     */
    private static void bailOut(String msg) {
        System.out.println();
        System.err.println((test ? choppedFileName + ": " : "") + "ERR " + msg);
        System.exit(1);
    }

    /**
     * Generates a chopped String representation of the file name.
     */
    private static void chopFileName() {
        if (fileName == null) {
            choppedFileName = "System.in";
            return;
        }

        int i = fileName.lastIndexOf(File.separatorChar);

        if (i > 0)
            i = fileName.lastIndexOf(File.separatorChar, i - 1);
        if (i == -1)
            i = 0;
        choppedFileName = ((i > 0) ? "..." : "") + fileName.substring(i);
    }

    /**
     * Prints a debugging message. If the program is started with <code>-d</code>
     * a message <code>msg</code> and the file name of the test instance
     * is printed. Otherwise nothing is printed.
     *
     * @param msg output text.
     */
    public static void printDebug(String msg) {
        /*TODO: ubedingt wieder wegmachen den kommentar
        if (!debug)
            return;
            */

        System.out.println(choppedFileName + ": DBG " + msg);
    }

    /**
     * Opens the input file and returns an object of type {@link Scanner} for reading from the file.
     * If no file name is given, input is read from {@link System#in}.
     *
     * @return {@link Scanner} reading from input file.
     */
    private static Scanner openInputFile() {
        if (fileName != null)
            try {
                return new Scanner(new File(fileName));
            } catch (Exception e) {
                bailOut("could not open \"" + fileName + "\" for reading");
            }
        return new Scanner(System.in);
    }

    /**
     * Interprets parameters for the program  and returns
     * a {@link Scanner} reading from test file.
     *
     * @param args command line parameters
     * @return {@link Scanner} reading from test file.
     */

    private static Scanner processArgs(String[] args) {
        for (String a : args) {
            if (a.equals("-t")) {
                test = true;
            } else if (a.equals("-d")) {
                debug = test = true;
            } else if (fileName == null) {
                fileName = a;
                break;
            }
        }
        return openInputFile();
    }

    /**
     * The constructor is private to hide it from JavaDoc.
     */
    private Tester() {
    }

    /**
     * Compares the input of a given tree with a tree set build from the original data source
     *
     * @param tree  tree to compare to
     * @param input tree set build from original data source
     */
    private static void testContent(Tree tree, TreeSet<Integer> input) {
        if (tree.isEmpty() != input.isEmpty()) {
            bailOut("Failure when calling isEmpty()!");
        }

        if (tree.size() != input.size()) {
            bailOut("Incorrect size - " + " tree: " + tree.size() + " tree set: " + input.size());
        }
        int counter = 0;
        for (int i : input) {
            if (tree.exists(i) == false) {
                bailOut("Element " + i + " is missing!");
            }
            if (tree.valueAtPosition(counter++) != i) {
                bailOut("Element " + i + " is at the wrong position!");
            }
        }
    }

    /**
     * Tries to insert integers in a given tree and a given tree set. This method proceeds as follows: (1) Compare the
     * structure of the given tree and the given tree set to ensure a proper initial situation. (2) Insertion of values.
     * If a value is already present in both data structures no insertion should take place. (3) Compare the structure
     * of the given tree and the given tree set to ensure that the insertion procedure worked properly and both data
     * structures include the same values.
     *
     * @param tree  tree build from original data source
     * @param input tree set build from original data source
     */
    private static void testInsert(Tree tree, TreeSet<Integer> input) {
        testContent(tree, input);
        int first = input.first();
        int last = input.last();
        for (int i = first; i <= last; i++) {
            input.add(i);
            tree.insert(i);
        }
        testContent(tree, input);
    }

    /**
     * Tries to delete integers in a given tree and a given tree set. This method proceeds as follows: (1) Compare the
     * structure of the given tree and the given tree set to ensure a proper initial situation. (2) Deletion of values.
     * If a value is not present in both data structures no deletion should take place. (3) Compare the structure of the
     * given tree and the given tree set to ensure that the deletion procedure worked properly and both data structures
     * include the same values.
     *
     * @param tree  tree build from original data source
     * @param input tree set build from original data source
     */
    private static void testDelete(Tree tree, TreeSet<Integer> input) {
        testContent(tree, input);
        int first = input.first();
        int last = input.last();
        for (int i = first; i <= last; i += factor) {
            input.remove(i);
            tree.delete(i);
        }
        testContent(tree, input);
    }

    /**
     * Tests the implementation of the method position in the tree implementation.
     * Results from a tree set are used for comparison.
     *
     * @param tree  tree build from original data source
     * @param input tree set build from original data source
     */
    private static void testPositions(Tree tree, TreeSet<Integer> input) {
        testContent(tree, input);
        Integer[] testarr = input.toArray(new Integer[0]);
        int counter = input.size();
        for (int i = 0; i < counter; i += factor) {
            if (tree.position(testarr[i]) != i) {
                bailOut("Position mismatch!");
            }
        }
        testContent(tree, input);
    }

    /**
     * Tests the implementation of the method values in the tree implementation.
     * Results from a tree set are used for comparison.
     *
     * @param tree  tree build from original data source
     * @param input tree set build from original data source
     */
    private static void testValues(Tree tree, TreeSet<Integer> input) {
        testContent(tree, input);
        Integer[] testarr = input.toArray(new Integer[0]);
        int mark1, mark2, run;
        int size = input.size();
        int counter = size / factor;
        for (int i = 0; i < counter; i = (i + 1) * 2) {
            mark1 = i;
            mark2 = size - 1 - i;
            for (int e : tree.values(testarr[mark1], testarr[mark2])) {
                if (e != testarr[mark1++]) {
                    bailOut("Failure when calling method values()!");
                }
            }
            mark1 = i;
            mark2 = size - 1 - i;
            run = 0;
            for (int e : tree.values(testarr[mark2], testarr[mark1])) {
                if (e != testarr[run <= mark1 ? run++ : mark2++]) {
                    bailOut("Failure when calling method values()!");
                }
            }
        }
        testContent(tree, input);
    }

    /**
     * Tests if a given tree is balanced.
     *
     * @param tree  tree build from original data source
     * @param input tree set build from original data source
     */
    private static void testBalance(Tree tree, TreeSet<Integer> input) {
        testContent(tree, input);
        int height = tree.height();
        int height2 = -1;
        if(input.size() > 0) {
            height2 = (int) Math.floor(Math.log(input.size()) / Math.log(2));
        }
        if (height != height2) {
            bailOut("Tree is not balanced!");
        }
        testContent(tree, input);
    }


    /**
     * Tests the implementation with an empty tree and the example tree from the exercise sheet.
     */
    private static void initialTest() {
        Tree t = new Tree();
        int[] arr = new int[]{30, 20, 45, 10, 25, 38, 50, 5, 13, 22, 29, 12, 14};
        if (!t.isEmpty() || t.size() != 0 || t.height() != -1) {
            bailOut("Error when testing empty tree!");
        }
        for (int i : arr) {
            t.insert(i);
        }

        //System.out.println(t.toString());

        if (t.isEmpty()) {
            bailOut("Check the implementation of isEmpty()!");
        }
        if (t.size() != arr.length) {
            bailOut("Check the implementation of size()!");
        }
        if (!t.exists(20) || t.exists(23)) {
            bailOut("Check the implementation of exists()!");
        }
        if (t.valueAtPosition(3) != 13 || t.valueAtPosition(0) != 5) {
            bailOut("Check the implementation of valueAtPosition()!");
        }
        if (t.position(10) != 1 || t.position(29) != 8 || t.position(31) != 10) {
            bailOut("Check the implementation of position()!");
        }
        if (t.height() != 4) {
            bailOut("Check the implementation of height()!");
        }
        try{
            t.valueAtPosition(1000);
        }catch(IllegalArgumentException e) {
            printDebug(e.getMessage());
        }
    }


    private static void debugInformation(long estimatedTime, String first, String second, Tree tree){
        printDebug(first + estimatedTime*1.0/1000000000 + " seconds");
        printDebug(second + " tree size = " + tree.size() + ", tree height = " + tree.height());
    }

    public static void main(String[] args) {
        TreeSet<Integer> input = null;
        Scanner s = null;
        try {
            s = new Scanner(new File("src/0001"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        chopFileName();

        try {
            SecurityManager sm = new ADS1SecurityManager();
            System.setSecurityManager(sm);
        } catch (SecurityException e) {
            bailOut("Error: could not set security manager: " + e);
        }

        try {
            Tree tree = null;
            int val;
            if (s.hasNextLine()) {
                tree = new Tree();
                input = new TreeSet<Integer>();
                val = Integer.valueOf(s.nextLine());
                tree.insert(val);
                input.add(val);
            } else {
                bailOut("Empty input file!");
            }


            while (s.hasNextLine()) {
                val = Integer.valueOf(s.nextLine());
                tree.insert(val);
                input.add(val);
            }

            initialTest();
            printDebug("After initial test: tree size = " + tree.size() + " tree height = " + tree.height());

            long startTime = System.nanoTime();
            testInsert(tree, input);
            long estimatedTime = System.nanoTime() - startTime;
            debugInformation(estimatedTime, "Insertion test took ", "After insertion test:", tree);

            startTime = System.nanoTime();
            testDelete(tree, input);
            estimatedTime = System.nanoTime() - startTime;
            debugInformation(estimatedTime, "Deletion test took ", "After deletion test:", tree);

            startTime = System.nanoTime();
            testPositions(tree, input);
            estimatedTime = System.nanoTime() - startTime;
            debugInformation(estimatedTime, "Testing positions took ", "After testing positions:", tree);

            startTime = System.nanoTime();
            testValues(tree, input);
            estimatedTime = System.nanoTime() - startTime;
            debugInformation(estimatedTime, "Testing value lists took ", "After creating value lists:", tree);

            startTime = System.nanoTime();
            tree.simpleBalance();
            testBalance(tree, input);
            estimatedTime = System.nanoTime() - startTime;
            debugInformation(estimatedTime, "Testing tree balance took ", "After balancing:", tree);

            StringBuffer msg = new StringBuffer(test ? choppedFileName + ": " : "");
            msg.append("OK");
            System.out.println("");
            System.out.println(msg.toString());

        } catch (SecurityException se) {
            bailOut("Method call not allowed: \"" + se.toString() + "\"");
        } catch (NumberFormatException e) {
            bailOut("Wrong input format: \"" + e.toString() + "\"");
        } catch (Exception e) {
            e.printStackTrace();
            bailOut("Caught exception \"" + e.toString() + "\"");
        }
    }
}
