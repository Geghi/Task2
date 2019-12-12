import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class EditText {

	public static void main(String[] args) {

		String line = null;
		String str= null;
		String[] splitted;
		int index, indexLine;
		try {
			File f1 = new File("../moviesDB.txt");
			File f2 = new File("../cinemaEvaluationDB.txt");
			FileReader fr = new FileReader(f1);
			BufferedReader br = new BufferedReader(fr);
			FileWriter fw = new FileWriter(f2);
			BufferedWriter out = new BufferedWriter(fw);
			line = br.readLine();
			while (line != null) {
				indexLine = line.indexOf("\"Genre\":") + 8;
				if(indexLine == 7) {
					line = br.readLine();
					continue;
				}
				splitted = line.split("\"Genre\":");
				
				splitted[1] = '[' + splitted[1];
				index = splitted[1].indexOf("\",") +1;
				
				str = splitted[1].substring(0,index) + ']';
				for(int i = 0; i < str.length(); i++) {
					char c = str.charAt(i);
					if(c == ',') {
						str = str.substring(0, i) + "\", \"" + str.substring((i+2));
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
