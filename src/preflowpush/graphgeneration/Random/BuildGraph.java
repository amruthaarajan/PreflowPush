package preflowpush.graphgeneration.Random;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

public class BuildGraph {
    public BuildGraph(String s, String s1, int i, int i1, int i2, int i3) {
        BuildGraph(s,s1,i,i1,i2,i3);
    }

    private void BuildGraph(String fileName, String directory, int vertices, int dense, int maxCapacity, int minCapacity) {
		Random random = new Random();
		try {
			String dirName = directory;//
			if (dirName.equals("")) {
				dirName = ".";
			}

			File outputfile = new File(dirName, fileName);
			int[][] Graph = new int[vertices][vertices];
			int n, m;

			for (n = 0; n < vertices; n++)
				for (m = n + 1; m < vertices; m++) {
					int randomInt = (random.nextInt((maxCapacity - minCapacity + 1)) + minCapacity);

					int k = (int) (1000.0 * Math.random() / 10.0);
					int b = (k < dense) ? 1 : 0;
					if (b == 0) {
						Graph[n][m] = Graph[m][n] = b;
					} else {
						Graph[n][m] = Graph[m][n] = randomInt;
					}
				}


			PrintWriter output = new PrintWriter(new FileWriter(outputfile));

			for (int x = 0; x < Graph.length; x++) {
				if (x == 0) {
					for (int y = 0; y < Graph[x].length; y++) {
						String value = String.valueOf(Graph[x][y]);
						if (y != 0) {
							if (value.equals("0") == false) {
								output.print("s " + String.valueOf(y) + " " + value + "\n");
							}
						}
					}
				} else {
					if (x == Graph.length - 1) {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x][y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(String.valueOf(y) + " t " + value + "\n");
								}
							}
						}
					} else {
						for (int y = 0; y < Graph[x].length; y++) {
							String value = String.valueOf(Graph[x][y]);
							if (y != 0) {
								if (value.equals("0") == false) {
									output.print(x + " " + String.valueOf(y) + " " + value + "\n");
								}
							}
						}
					}
				}

			}

			output.close();
		} catch (IOException e) {
			System.err.println("Error opening file" + e);
			return;
		}
		System.out.print("\nDone");
	}

	public static void main(String args[])
    {
        BuildGraph bg=new BuildGraph("test1.txt","/Users/amruthaa/data/",100,70,20,10);
    }
}