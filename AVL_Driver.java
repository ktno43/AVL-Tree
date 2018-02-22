import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;

public class AVL_Driver {

	public static void main(String[] args) throws IOException {
		TreeAVL<Integer, Integer> tree = new TreeAVL<Integer, Integer>();

		try {
			OutputStream treeStream = new FileOutputStream("Tree.txt");
			OutputStreamWriter treeWriter = new OutputStreamWriter(treeStream);
			FileInputStream in = new FileInputStream("Tree.txt");
			int content = 0;

			System.out.println("Inserting values 10 to 20");
			for (int i = 1, j = 10; i <= 20; i++, j++) {
				System.out.println("Inserting " + j + " into the AVL Tree");
				tree.insert(j, j);

				tree.printTree2(treeWriter);
				treeWriter.flush();

				System.out.println();

				while ((content = in.read()) != -1) {
					System.out.print((char) content);
				}
				System.out.println();
				System.out.println();
			}
			
			System.out.println();
			System.out.println("Deleting key/element 13: ");
			tree.delete(13);
			
			tree.printTree2(treeWriter);
			treeWriter.flush();
			
			while ((content = in.read()) != -1) {
				System.out.print((char) content);
			}
			
			System.out.println();
			System.out.println();
			
			treeWriter.close();
			in.close();
		}

		catch (Exception ex) {
			System.out.println("error");
			ex.printStackTrace();
		}

		ArrayList<Integer> mahList = tree.inorderTraversal();
		String listAsString = ""; // Array list to string

		for (Integer e : mahList) { // For every element in resultList, put into e and add it to the string
			listAsString += e + " ";
		}
		System.out.println("Printing balance (Inorder Traversal): ");
		System.out.println(listAsString);
		tree.printBalance();
		
	}
}