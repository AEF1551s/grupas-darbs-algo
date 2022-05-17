import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStream;
import java.util.ArrayList;

import java.io.DataInputStream;
import java.io.DataOutputStream;

import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;

class LZ77_triple {
	int back;
	int length;
	byte next;

	LZ77_triple(int b, int l, byte n) {
		back = b;
		length = l;
		next = n;
	}

	public int getback() {
		return back;
	}

	public int getlength() {
		return length;
	}

	public byte getnext() {
		return next;
	}

	public void print() {
		System.out.print("(" + back + ";" + length + ";" + (int) next + ")");
	}

	public void cprint() {
		System.out.print(back + " " + length + " " + (int) next + " ");
	}
}

class LZ77 {
	static final int bufferlength = (int)2147483647;
	private ArrayList<LZ77_triple> triplelist = new ArrayList<>();
	private ArrayList<LZ77_triple> decrypttriple = new ArrayList<>();
	private ArrayList<Byte> purechar = new ArrayList<>();
	private ArrayList<Byte> decryptchar = new ArrayList<>();

	public void LZ77_encrypt(String entryfilename, String exitfilename) {
		readFile(entryfilename);
		encode();
		outputEncryptedFile(exitfilename);
		purechar.clear();
		triplelist.clear();
	}

	public void LZ77_decrypt(String entryfilename, String exitfilename) {
		readEncrypted(entryfilename);
		decode();
		outputDecodedFile(exitfilename);
		decryptchar.clear();
		decrypttriple.clear();
	}

	// TODO TEST READFILE
	public void readFile(String filename) {
		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}

		Byte content;
		boolean EOF = false;
		while (!EOF) {
			try {
				content = in.readByte();
				if (content == -1) {
					break;
				}
				purechar.add(content);
			} catch (EOFException e) {
				EOF = true;
			} catch (IOException e) {
				System.out.println("bad file format, not all numbers has been read succesfully");
				break;
			}
		}

	}

	public void purePrint() {
		for (byte c : purechar) {
			System.out.print((int) c);
			System.out.print(" ");
		}
	}

	public void decodedPrint() {
		for (byte c : decryptchar) {
			System.out.print(c);
		}
	}

	// TODO FIX
	public void decode() {
		int reader = 0;
		for (LZ77_triple t : decrypttriple) {
			reader = decryptchar.size() - t.getback();
			for (int i = t.getlength(); i > 0; reader++) {
				decryptchar.add(decryptchar.get(reader));
				i--;
			}
			decryptchar.add(t.getnext()); // ?
		}
	}

	public void encode() {
		for (int i = 0; i < purechar.size(); i++) {

			int maxlength = 0, length = 0, pos = i;

			int j = i - bufferlength;
			if (j < 0)
				j = 0;
			for (; j < i || (j < purechar.size() && length > 0); j++) {
				if (i + length + 1 < purechar.size() && (int) purechar.get(j) == (int) purechar.get(i + length)) {
					length++;
					if (maxlength <= length) {
						maxlength = length;
						pos = j - maxlength + 1;
					}
				} else {
					j = j - length;
					length = 0;

				}

			}
			// System.out.print(i + " ");
			// System.out.print(pos + " ");
			// System.out.print(maxlength);
			LZ77_triple temp = new LZ77_triple((int) (i - pos), (int) maxlength, purechar.get(i + maxlength));
			i = i + maxlength;
			// temp.print();
			triplelist.add(temp);

		}
	}

	// TODO nolasit no input streama.
	public void readEncrypted(String filename) {

		DataInputStream in = null;
		try {
			in = new DataInputStream(new FileInputStream(filename));
		} catch (Exception e) {
			e.printStackTrace();
		}
		boolean EOF = false;
		int content = 0;
		int i = 0, tempb = 0, templ = 0;
		while (!EOF) {
			try {
				switch (i % 3) {
					case 0:
						content = in.readInt();
						tempb = content;
						break;
					case 1:
						content = in.readInt();
						templ = content;
						break;
					case 2:
						content = in.readByte();
						decrypttriple.add(new LZ77_triple(tempb, templ, (byte) content));
						break;
				}
				i++;
			} catch (EOFException e) {
				EOF = true;
			} catch (IOException e) {
				System.out.println("Error while reading encrypted file!");
				break;
			}
		}
	}

	public void encryptPrint() {
		for (LZ77_triple t : triplelist) {
			t.print();
		}
	}

	public void decryptPrint() {
		for (LZ77_triple t : decrypttriple) {
			t.print();
		}
	}

	public void encryptcompactPrint() {
		for (LZ77_triple t : triplelist) {
			t.cprint();
		}
	}

	public void outputEncryptedFile(String filename) {
		try {
			FileWriter out = new FileWriter(filename);

			for (LZ77_triple t : triplelist) {

				try {

					out.write((int) t.getback());
					out.write((int) t.getlength());
					out.write((int) t.getnext());
				} catch (Exception e) {
					System.out.println("input-output error");
					break;
				}
			}

			out.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void outputDecodedFile(String filename) {
		// TODO binary streams
		try {
			FileWriter out = new FileWriter(filename);

			for (int c : decryptchar) {

				try {
					out.write(c);

				} catch (Exception e) {
					System.out.println("input-output error");
					break;
				}
			}

			out.close();
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}

	public void compressionRatio(String fileName, String compressed) {
		File f = new File(fileName);
		File c = new File(compressed);
		double fileSize = f.length();
		double SizeCompressed = c.length();

		double ratio = fileSize / SizeCompressed;
		System.out.println("The ratio: " + ratio);
	}
}

public class LZ77_grupas_v1 {

	public static void main(String[] args) {
		LZ77 test = new LZ77();
		test.LZ77_encrypt("File2.html", "test.dat");
		// test.purePrint();
		System.out.println();
		// test.encryptPrint();

		// test.LZ77_decrypt("test.dat", "doc2.txt");
		// System.out.println();
		// test.decryptPrint();
		// System.out.println();
		// test.decodedPrint();
		test.compressionRatio("File2.html", "test.dat");
	}
}
