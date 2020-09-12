package colewski.fr;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

/**
 * Retire dans le fichier model.ts généré par OPEN API generator les models non-dto
 */
public class Main {


	public static void main(String[] args) throws IOException {


		if (args.length < 2 || args[0] == null || args[1] == null) {
			System.err.println("Error: Two args required");
			System.exit(21);
		}

		String path = args[0];

		String value = args[1];

		try (Stream<Path> paths = Files.walk(Paths.get(path))) {
			paths
					.filter(Files::isRegularFile)
					.filter(path1 ->
							!path1.getFileName().toString().contains(value) &&
									!path1.getFileName().toString().equals("models.ts"))
					.forEach(path2 -> {
						try {
							Files.deleteIfExists(path2);
						} catch (IOException e) {
							e.printStackTrace();
						}
					});
		}

		File inputFile = new File(path + "/models.ts");
		File tempFile = new File(path + "/modelsTemp.ts");

		try (BufferedReader reader = new BufferedReader(new FileReader(inputFile));
		     BufferedWriter writer = new BufferedWriter(new FileWriter(tempFile));) {

			String currentLine;


			while ((currentLine = reader.readLine()) != null) {

				if (!currentLine.contains(value)) continue;
				writer.write(currentLine + System.getProperty("line.separator"));
			}


			System.out.println("DONE !");
		}


	}
}
