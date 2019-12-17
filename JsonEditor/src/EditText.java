import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class EditText {

	public static void main(String[] args) {

//		editGenre();
		editDesc();

	}

	public static void editDesc() {
		String line1 = null;
		String line2 = null;
		String title1 = null;
		String title2 = null;
		String desc = null;
		String str = null;
		String[] splitted1;
		String[] splitted2;
		int index1, indexLine1, index2, indexLine2;

		try {
			File f1 = new File("../moviesDB.txt");
			File f2 = new File("../moviesDesc.txt");
			File f3 = new File("../newMoviesDB.txt");

			FileReader fr1 = new FileReader(f1);
			BufferedReader br1 = new BufferedReader(fr1);
			FileReader fr2 = new FileReader(f2);
			BufferedReader br2 = new BufferedReader(fr2);
			FileWriter fw = new FileWriter(f3);
			BufferedWriter out = new BufferedWriter(fw);
			line1 = br1.readLine();
			while (line1 != null) {
				indexLine1 = line1.indexOf("\"Title\":") + 8;
				if (indexLine1 == 7) {
					line1 = br1.readLine();
					continue;
				}

				splitted1 = line1.split("\"Title\":");
				System.out.println(splitted1[1]);

				index1 = splitted1[1].indexOf("\",");
				title1 = splitted1[1].substring(1, index1);

				line2 = br2.readLine();
				while (line2 != null) {
					indexLine2 = line2.indexOf("\"Title\":") + 8;
					if (indexLine2 == 7) {
						line2 = br2.readLine();
						continue;
					}

					splitted2 = line2.split("\"Title\":");

					index2 = splitted2[1].indexOf("\",");

					title2 = splitted2[1].substring(1, index2);
					
					System.out.println(title2);
					
					if(title1.equals(title2)) {
						indexLine2 = line2.indexOf("\"Plot\":") + 7;
						if (indexLine2 == 6) {
							line2 = br2.readLine();
							continue;
						}

						splitted2 = line2.split("\"Plot\":");
						index2 = splitted2[1].indexOf("\"}");
						desc = splitted2[1].substring(1, index2);
						System.out.println(desc);
						
						break;
					} 
					
					desc = null;
					line2 = br2.readLine();
				}
				
				if(desc != null) {
					indexLine1 = line1.indexOf("\"Plot\":") + 7;
					if (indexLine1 == 6) {
						line1 = br1.readLine();
						continue;
					}

					splitted1 = line1.split("\"Plot\":");

					index1 = splitted1[1].indexOf("\",");
					String desc2 = splitted1[1].substring(1, index1);
					System.out.println(desc2);
					str = line1.substring(0, indexLine1) + '\"' + desc + splitted1[1].substring(index1); 
					System.out.println(str);

				}
					

				/*
				 * System.out.println(str); //Riga modificata // System.out.println(line); //
				 * Confronto con riga prima della modifica out.write(str);
				 * 
				 */

				line1 = br1.readLine();
				break;
			}
			fr1.close();
			fr2.close();
			br1.close();
			out.flush();
			out.close();
			System.out.println("All working properly");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public static void editGenre() {
		try {
			String line = null;
			String str = null;
			String[] splitted;
			int index, indexLine;

			File f1 = new File("../moviesDB.txt");
			File f2 = new File("../cinemaEvaluationDB.txt");

			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(f2);
			BufferedWriter out = new BufferedWriter(fw);
			line = br.readLine();
			while (line != null) {
				indexLine = line.indexOf("\"Genre\":") + 8;
				if (indexLine == 7) {
					line = br.readLine();
					continue;
				}
				splitted = line.split("\"Genre\":");

				splitted[1] = '[' + splitted[1];
				index = splitted[1].indexOf("\",") + 1;

				str = splitted[1].substring(0, index) + ']';
				for (int i = 0; i < str.length(); i++) {
					char c = str.charAt(i);
					if (c == ',') {
						str = str.substring(0, i) + "\", \"" + str.substring((i + 2));
						i++;
					}
				}
				str = line.substring(0, indexLine) + str + splitted[1].substring(index);
//				System.out.println(str);  //Riga modificata
//				System.out.println(line); // Confronto con riga prima della modifica
				out.write(str);
				line = br.readLine();
			}
			fr.close();
			br.close();
			out.flush();
			out.close();
			System.out.println("All working properly");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
