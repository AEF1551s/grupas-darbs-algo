package algo;

// 211RDB004 Aija Monika Vainiņa  9.grupa
// 211RDB317 Artūrs Uldis Gaisiņš  13.grupa
// 211RDB197 Miks Šics       1.grupa
// 211RDB202 Kate Anete Jansone  1.grupa
// 191RMC097 Krišjānis Ivbulis  7.grupa 
// 211RDB141 Aleksejs Kareļins  7.grupa 

import java.io.File;
import java.util.Scanner;

public class Main {
	public static void size(String sourceFile) {
		File f = new File(sourceFile);
		double size = f.length();
		System.out.printf("\n" + sourceFile + "\t%.0f B" + "\n", size);
		return;

	}

	public static boolean checkIfExists(String fileName) {
		File f = new File(fileName);
		if (f.exists()) {
			return true;
		} else {
			System.out.println("File \"" + fileName + "\" does not exist.");
			return false;
		}
	}

	public static void about() {
		System.out.println("211RDB004 Aija Monika Vainiņa\t9.grupa");
		System.out.println("211RDB317 Artūrs Uldis Gaisiņš\t13.grupa");
		System.out.println("211RDB197 Miks Šics      \t1.grupa");
		System.out.println("211RDB202 Kate Anete Jansone\t1.grupa");
		System.out.println("191RMC097 Krišjānis Ivbulis\t7.grupa ");
		System.out.println("211RDB141 Aleksejs Kareļins\t7.grupa ");
	}

	public static void main(String[] args) {
		LZ77 test = new LZ77();

		Scanner sc = new Scanner(System.in);
		String choiseStr;
		String sourceFile, resultFile, firstFile, secondFile, compressedFile, uncompressedFile;
		System.out.println();

		loop: while (true) {
			System.out.println("\nCommands:\ncomp, decomp, size, equal, ratio, about, exit\n");

			choiseStr = sc.next();

			switch (choiseStr) {
				case "comp":
					System.out.print("source file name: ");
					sourceFile = sc.next();
					if (!checkIfExists(sourceFile)) {
						break;
					}

					System.out.print("archive name: ");
					resultFile = sc.next();
					test.LZ77_encrypt(sourceFile, resultFile);
					break;
				case "decomp":
					System.out.print("archive name: ");
					sourceFile = sc.next();
					if (!checkIfExists(sourceFile)) {
						break;
					}

					System.out.print("file name: ");
					resultFile = sc.next();
					test.LZ77_decrypt(sourceFile, resultFile);
					break;
				case "size":
					System.out.print("file name: ");
					sourceFile = sc.next();
					if (!checkIfExists(sourceFile)) {
						break;
					}

					size(sourceFile);
					break;
				case "equal":
					System.out.print("first file name: ");
					firstFile = sc.next();
					if (!checkIfExists(firstFile)) {
						break;
					}

					System.out.print("second file name: ");
					secondFile = sc.next();
					if (!checkIfExists(secondFile)) {
						break;
					}

					if (test.sameCheck(firstFile, secondFile)) {
						System.out.println("true");
					} else {
						System.out.println("false");
					}
					break;
				case "about":
					about();
					break;
				case "ratio":
					System.out.print("uncompressed file name: ");
					compressedFile = sc.next();
					if (!checkIfExists(compressedFile)) {
						break;
					}

					System.out.print("compressed file name: ");
					uncompressedFile = sc.next();
					if (!checkIfExists(uncompressedFile)) {
						break;
					}
					test.compressionRatio(uncompressedFile, compressedFile);
					break;
				case "exit":
					break loop;
				default:
					System.out.println("Incorrect command, try again.");
					break;
			}
		}

		sc.close();
	}

}