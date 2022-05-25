package algo;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Scanner;
import java.io.File;

//https://www.codejava.net/java-se/file-io/how-to-read-and-write-binary-files-in-java
// * - Saprašanai prezentācijas veidotājiem

public class Main {
	static LZ77 compressor = new LZ77();
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		String choiseStr;
		String sourceFile, resultFile, firstFile, secondFile;
		
		loop: while (true) {
			
			choiseStr = sc.next();
								
			switch (choiseStr) {
			case "comp":
				System.out.print("source file name: ");
				sourceFile = sc.next();
				System.out.print("archive name: ");
				resultFile = sc.next();
				comp(sourceFile, resultFile);
				break;
			case "decomp":
				System.out.print("archive name: ");
				sourceFile = sc.next();
				System.out.print("file name: ");
				resultFile = sc.next();
				decomp(sourceFile, resultFile);
				break;
			case "size":
				System.out.print("file name: ");
				sourceFile = sc.next();
				size(sourceFile);
				break;
			case "equal":
				System.out.print("first file name: ");
				firstFile = sc.next();
				System.out.print("second file name: ");
				secondFile = sc.next();
				System.out.println(equal(firstFile, secondFile));
				break;
			case "about":
				about();
				break;
			case "exit":
				break loop;
			}
		}
		sc.close();
	}

	public static void comp(String sourceFile, String resultFile) {
		File sourceF = new File(sourceFile);
		if(!sourceF.exists()) {
			System.out.println("File " + sourceFile +  "doesn't exist");
			return;
		}
		compressor.LZ77_encrypt(sourceFile, resultFile);
		compressor.compressionRatio(sourceFile, resultFile);
	}

	public static void decomp(String sourceFile, String resultFile) {
		File sourceF = new File(sourceFile);
		if(!sourceF.exists()) {
			System.out.println("File doesn't exist");
			return;
		}
		compressor.LZ77_decrypt(sourceFile, resultFile);
	}
	
	public static void size(String sourceFile) {
		try {
			FileInputStream f = new FileInputStream(sourceFile);
			System.out.println("size: " + f.available());
			f.close();
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
		}
		
	}
	
	public static boolean equal(String firstFile, String secondFile) {
		try {
			FileInputStream f1 = new FileInputStream(firstFile);
			FileInputStream f2 = new FileInputStream(secondFile);
			int k1, k2;
			byte[] buf1 = new byte[1000];
			byte[] buf2 = new byte[1000];
			do {
				k1 = f1.read(buf1);
				k2 = f2.read(buf2);
				if (k1 != k2) {
					f1.close();
					f2.close();
					return false;
				}
				for (int i=0; i<k1; i++) {
					if (buf1[i] != buf2[i]) {
						f1.close();
						f2.close();
						return false;
					}
						
				}
			} while (k1 == 0 && k2 == 0);
			f1.close();
			f2.close();
			return true;
		}
		catch (IOException ex) {
			System.out.println(ex.getMessage());
			return false;
		}
	}
	
	public static void about() {
		System.out.println("Krišjānis Ivbulis 7. grupa 191RMC097");
		System.out.println("Artūrs Uldis Gaisiņš 13. grupa 211RDB317");
		System.out.println("Miks Šics 1. grupa 211RDB197");
		System.out.println("Aleksejs Kareļins 7. grupa 211RDB141");
		System.out.println("Aija Monika Vainiņa 9. grupa 211RDB004");
		System.out.println("Kate Anete Jansone 1. grupa 211RDB202");
	}
}
