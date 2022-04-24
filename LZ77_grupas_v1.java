import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.EOFException;

class LZ77_triple {
	 byte back;
	byte length;
	char next;

	LZ77_triple(byte b, byte l, char n) {
		back = b;
		length = l;
		next = n;
	}
	public byte getback() {
		return back;
	}
	public byte getlength() {
		return length;
	}
	public int getnext() {
		return next;
	}
	public void print() {
		System.out.print("(" + back + ";" + length + ";" + next + ")");
	}
	public void cprint() {
		System.out.print(back +" "+length +" "+(int)next+" " );
	}
}

class LZ77 {
	int bufferlength = 6;
	private ArrayList<LZ77_triple> triplelist = new ArrayList<>();
	private ArrayList<Character> purechar = new ArrayList<>();
	private ArrayList<Character> decryptchar = new ArrayList<>();

	public void readFile(String filename) {
		File file = new File(filename);

		try (FileReader fr = new FileReader(file)) {
			int content;
			while ((content = (fr.read())) != -1) {
				purechar.add((char) content);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void purePrint() {
		for (char c : purechar) {
			System.out.print(c);
		}
		System.out.println(purechar.size());
	}

	public void encode() {
		for (int i = 0; i < purechar.size(); i++) {

			int maxlength = 0, length = 0, pos = i;

			int j = i - bufferlength;
			if (j < 0)
				j = 0;
			for (; j < i||(j<purechar.size()&&length>0); j++) {
				if (i + length+1 < purechar.size() && purechar.get(j) == purechar.get(i + length)) {
					length++;
					if (maxlength <= length) {
						maxlength = length;
						pos = j - maxlength+1;
					}
				} else {
					j = j - length;
					length = 0;

				}

			}
//			System.out.print(i + "  ");
//			System.out.print(pos + "  ");
//			System.out.print(maxlength);
			LZ77_triple temp = new LZ77_triple((byte)(i - pos), (byte)maxlength, purechar.get(i + maxlength));
			i=i+maxlength;
			//temp.print();
			triplelist.add(temp);

		}
	}

	public void encryptPrint() {
		for (LZ77_triple t : triplelist) {
			t.print();
		}
	}
	public void encryptcompactPrint() {
		for (LZ77_triple t : triplelist) {
			t.cprint();
		}
	}
public void create(String filename) {
		
		try {
			FileWriter out = new FileWriter(filename);

			for (LZ77_triple t: triplelist) {
				
				try {

					out.write((char)t.getback());
					out.write((char)t.getlength());
					out.write((char)t.getnext());
				}
				catch (Exception e) {
					System.out.println("input-output error");
					break;
				}
			}
			
			out.close();
		} 
		catch (Exception ex) {
			System.out.println(ex.getMessage());
		}

	}
}

public class LZ77_grupas_v1 {

	public static void main(String[] args) {

		LZ77 test = new LZ77();

		test.readFile("doc.txt");
		test.purePrint();
		System.out.println();
		test.encode();
		test.encryptPrint();
		test.encryptcompactPrint();
		test.create("test.dat");

	}
}