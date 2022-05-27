package algo;

import java.io.*;
import java.util.ArrayList;

class LZ77 {
	static final int bufferlength = 65536;
	static final int stringlength = 255;
	private ArrayList<LZ77_triple> triplelist = new ArrayList<>();// no purebyte uztaisa triples
	private ArrayList<LZ77_triple> decrypttriple = new ArrayList<>();
	private ArrayList<Byte> purebyte = new ArrayList<>(); // faila ielasīšana
	private ArrayList<Byte> decryptbyte = new ArrayList<>();

	public void LZ77_encrypt(String entryfilename, String exitfilename) {
		readFile(entryfilename);
		encode();
		outputEncryptedFile(exitfilename);
		purebyte.clear();
		triplelist.clear();
	}

	public void LZ77_decrypt(String entryfilename, String exitfilename) {
		readEncrypted(entryfilename);
		decode();
		outputDecodedFile(exitfilename);
		decryptbyte.clear();
		decrypttriple.clear();

	}

	public void readFile(String filename) {
		File file = new File(filename);
		byte[] fileData = new byte[(int) file.length()];
		try {
			FileInputStream dis = new FileInputStream(file);
			dis.read(fileData);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		for (byte b : fileData) { // ielasa iekš ArrayList
			purebyte.add(b);

		}
	}

	public void encode() {
		for (int i = 0; i < purebyte.size(); i++) {

			int maxlength = 0, length = 0, pos = i;

			int j = i - bufferlength;
			if (j < 0)
				j = 0;
			for (; (j < i || (j < purebyte.size() && length > 0)) && length < stringlength; j++) {
				if (i + length + 1 < purebyte.size() && purebyte.get(j) == purebyte.get(i + length)) {
					length++;
					if (maxlength <= length) {
						maxlength = length;
						pos = j - maxlength + 1;
					}
				} else if (length != 0) {
					j = j - length;
					length = 0;
				}

			}

			LZ77_triple temp = new LZ77_triple((int) (i - pos) /* attalums */,
					(int) maxlength /* garums */, purebyte.get(i + maxlength));
			i = i + maxlength;
			triplelist.add(temp);

		}
	}

	public void readEncrypted(String filename) {
		File file = new File(filename);
		byte[] fileData = new byte[(int) file.length()];
		try {
			FileInputStream dis = new FileInputStream(file);
			dis.read(fileData);
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		int i = 0, tempb = 0, templ = 0;
		for (int content : fileData) {
			switch (i % 4) {
				case 0:
					content += 128;
					tempb *= 256;
					tempb += content;
					break;
				case 1:
					content += 128;
					tempb *= 256;
					tempb += content;
					break;
				case 2:
					templ = content + 128;
					break;
				case 3:
					decrypttriple.add(new LZ77_triple(tempb, templ, (byte) content));
					tempb = 0;
					break;
			}
			i++;

		}

	}

	public void decode() {
		int reader = 0; // lasīšanas pozīcija
		for (LZ77_triple t : decrypttriple /* saspiests array list */) {
			reader = decryptbyte.size()
					/* tuksh uzpustais array list */ - (t.getback()) /* atgriez attalumu atpakaļ */;
			for (int i = t.getlength() /* garumu */; i > 0; reader++, i--) {
				decryptbyte.add(decryptbyte.get(reader)); // pārveidot uz ArrayList
			}
			decryptbyte.add(t.getnext());
		}
	}

	public void outputEncryptedFile(String filename) { // ieraksta sakompresētu iekšā failā

		try {
			FileOutputStream out = new FileOutputStream(filename);

			for (LZ77_triple t : triplelist) {

				try {
					out.write((byte) (((t.getback()) / 256) % 256) - 128);
					out.write((byte) (((t.getback())) % 256) - 128);
					out.write((byte) t.getlength() - 128);
					out.write((byte) t.getnext());

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

	public void outputDecodedFile(String filename) { // printē uzpūsto failu !failā!

		try {
			FileOutputStream out = new FileOutputStream(filename); // lai rakstītu ar bytes

			for (byte c : decryptbyte) {

				try {
					out.write(c); // printē baitus no masīva

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

	public void compressionRatio(String uncompressed, String compressed) {
		File f = new File(uncompressed);
		File c = new File(compressed);
		double fileSize = f.length();
		double sizeCompressed = c.length();

		// double ratio = sizeCompressed / fileSize;
		// System.out.println("The ratio: " + ratio + "");
		double ratioTimes = fileSize / sizeCompressed;
		double ratioProc = sizeCompressed / fileSize * 100;
		System.out.println("The ratio (%) " + ratioProc);
		System.out.println("The ratio (times) " + ratioTimes);
	}

	public boolean sameCheck(String fileOld, String fileNew) {
		File o = new File(fileOld);
		File n = new File(fileNew);
		byte[] oldFileData = new byte[(int) o.length()];
		byte[] newFileData = new byte[(int) n.length()];
		try {
			FileInputStream old = new FileInputStream(o);
			old.read(oldFileData);
			old.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			FileInputStream newe = new FileInputStream(n);
			newe.read(newFileData);
			newe.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (oldFileData.length != newFileData.length)
			return false;
		for (int i = 0; i < oldFileData.length; i++)
			if (oldFileData[i] != newFileData[i]) {
				System.out.println(oldFileData[i] + " " + newFileData[i]);
				return false;

			}
		return true;

	}
}