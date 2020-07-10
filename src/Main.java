import java.util.InputMismatchException;
import java.util.Scanner;

/**
 * Using the menu, we have access to the functions for processing a 2-3-tree.
 *
 * @author Syniuk Valentyn
 */
public class Main {

    public static void main(String[] args) {

        Operations<Integer> tree = new Operations<>();
        Scanner scanner = new Scanner(System.in);

        /* Menu */
        while (true) {
            System.out.println("\n2-3-Tree Operations:");
            System.out.println("1) Insert new element");
            System.out.println("2) Remove element");
            System.out.println("3) Search element");
            System.out.println("4) Size tree");
            System.out.println("5) Check empty");
            System.out.println("6) Clear tree");
            System.out.println("7) Get minimum value");
            System.out.println("8) Get maximum value");
            System.out.println("9) Display tree");
            System.out.println("10) Exit...\n");

            try {
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                switch (choice) {
                    case 1: {
                        System.out.print("Enter integer element to insert: ");
                        tree.add(scanner.nextInt());
                        break;
                    }
                    case 2: {
                        if (tree.isEmpty()) {
                            System.out.println("The tree is empty...");
                        } else {
                            System.out.print("Enter integer element to remove: ");
                            System.out.println("Remove result: " + tree.remove(scanner.nextInt()));
                        }
                        break;
                    }
                    case 3: {
                        if (tree.isEmpty()) {
                            System.out.println("The tree is empty...");
                        } else {
                            System.out.print("Enter integer element to search: ");
                            System.out.println("Search result: " + tree.search(scanner.nextInt()));
                        }
                        break;
                    }
                    case 4: {
                        System.out.println("Size tree: " + tree.size());
                        break;
                    }
                    case 5: {
                        System.out.println("Empty status: " + tree.isEmpty());
                        break;
                    }
                    case 6: {
                        System.out.println("\nTree Cleared!");
                        tree.clear();
                        break;
                    }
                    case 7: {
                        System.out.println("Minimum element: " + tree.findMin());
                        break;
                    }
                    case 8: {
                        System.out.println("Maximum element: " + tree.findMax());
                        break;
                    }
                    case 9: {
                        System.out.print("\nIn-order: ");
                        tree.inOrder();
                        System.out.print("\nPre-order: ");
                        tree.preOrder();
                        System.out.print("\nPost-order: ");
                        tree.postOrder();
                        System.out.println();
                        break;
                    }
                    case 10: {
                        return;
                    }
                    default: {
                        System.out.println("\nWrong Entry!");
                        break;
                    }
                }
            } catch (InputMismatchException e) {
                System.out.println("The value entered is invalid. Try again.");
                break;
            }
        }
    }
}
