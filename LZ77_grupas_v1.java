import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.EOFException;
import java.io.IOException;

class LZ77_triple {
	int back;
	int length;
	int next;

	LZ77_triple(int b, int l, int n) {
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

	public int getnext() {
		return next;
	}

	public void print() {
		System.out.print("(" + back + ";" + length + ";" + next + ")");
	}

	public void cprint() {
		System.out.print(back + " " + length + " " + (int) next + " ");
	}
}

class LZ77 {
	static final int bufferlength = 100000;
	private ArrayList<LZ77_triple> triplelist = new ArrayList<>();
	private ArrayList<LZ77_triple> decrypttriple = new ArrayList<>();
	private ArrayList<Integer> purechar = new ArrayList<>();
	private ArrayList<Integer> decryptchar = new ArrayList<>();

	public void LZ77_encrypt(String entryfilename, String exitfilename) {
		readFile(entryfilename);
		encode();
		encryptFile(exitfilename);
	}

	public void LZ77_decrypt(String entryfilename, String exitfilename) {
		readEncrypted(entryfilename);
		decode();
		decodedFile(exitfilename);
	}

	public void readFile(String filename) {
		// TODO binary input streams uz int
		DataInputStream in = null;
		try {
			 in = new DataInputStream(new FileInputStream("doc.txt"));
		} catch (Exception e) {
			e.printStackTrace();
		}

		int content;
		boolean EOF = false;
		while (!EOF) {
			try {
				content = in.read(); 
				if(content == -1){
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
	
		
		
		// File file = new File(filename);

		// try (FileReader fr = new FileReader(file)) {
		// int content;
		// while ((content = (fr.read())) != -1) {
		// purechar.add((int) content);
		// }

		// } catch (Exception e) {
		// e.printStackTrace();
		// }

	}

	public void purePrint() {
		for (int c : purechar) {
			System.out.print(c);
			System.out.print(" ");
		}

	}

	public void decodedPrint() {
		for (int c : decryptchar) {
			System.out.print(c);
		}
	}

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
				if (i + length + 1 < purechar.size() && purechar.get(j) == purechar.get(i + length)) {
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

	public void readEncrypted(String filename) {
		File file = new File(filename);

		try (FileReader fr = new FileReader(file)) {
			int content, i = 0, tempb = 0, templ = 0;
			while ((content = (fr.read())) != -1) {
				// purechar.add((char) content);
				switch (i % 3) {
					case 0:
						tempb = content;
						break;
					case 1:
						templ = content;
						break;
					case 2:
						decrypttriple.add(new LZ77_triple(tempb, templ, (int) content));
						break;
				}
				i++;

			}

		} catch (Exception e) {
			e.printStackTrace();
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

	public void encryptFile(String filename) {
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

	public void decodedFile(String filename) {
		// TODO binary output streams
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
}

public class LZ77_grupas_v1 {

	public static void main(String[] args) {

		LZ77 test = new LZ77();
		test.LZ77_encrypt("doc.txt", "test.dat");

		test.purePrint();
		System.out.println();
		test.encryptPrint();

		// test.LZ77_decrypt("test.dat", "doc2.txt");

		// System.out.println();
		// test.decryptPrint();
		// System.out.println();
		// test.decodedPrint();

	}
}
