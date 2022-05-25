package algo;
import java.io.File;
import java.util.ArrayList;
import java.io.FileOutputStream;
import java.io.FileInputStream;


class LZ77 {
	static final int bufferlength = 65536; // * - Bufera garums
	static final int stringlength = 256; // * - Virknes garums
	private ArrayList<LZ77_triple> triplelist = new ArrayList<>();// no purebyte uztaisa triples
	private ArrayList<LZ77_triple> decrypttriple = new ArrayList<>();
	private ArrayList<Byte> purebyte = new ArrayList<>(); // faila ielasīšana
	private ArrayList<Byte> decryptbyte = new ArrayList<>();

	// * - Metode, lai uzsāktu enkodēšanu
	public void LZ77_encrypt(String entryfilename, String exitfilename) {
		readFile(entryfilename);
		encode();
		encryptFile(exitfilename);
		purebyte.clear();
		triplelist.clear();
	}

	// * - Metode, lai uzsāktu dekodēšanu
	public void LZ77_decrypt(String entryfilename, String exitfilename) {
		readEncrypted(entryfilename);
		decode();
		decodedFile(exitfilename);
		decryptbyte.clear();
		decrypttriple.clear();
	}

	// * - Metode, lai izlasītu sākuma failu
	public void readFile(String filename)/* good stuff */ {
		File file = new File(filename); // * - Izvēlām failu
		byte[] fileData = new byte[(int) file.length()]; // - * Baitu masīvs faila datiem
		try {
			FileInputStream dis = new FileInputStream(file); // * - Izvēlām failu, lai sāktu lasīšanu
			dis.read(fileData); // * - Nolasām failu masīvā
			dis.close();
		} catch (Exception e) {
			e.printStackTrace(); // * Kļūdu novērošana
		}
		for (byte b : fileData) { // ielasa iekš ArrayList
			purebyte.add(b);
		}
	}

	// * - Metode, lai izlasītu enkodētu failu
	public void readEncrypted(String filename) {
		File file = new File(filename); // * - Izvēlām failu
		byte[] fileData = new byte[(int) file.length()]; // * - Baitu masīvs faila datiem
		try {
			FileInputStream dis = new FileInputStream(file); // * - Izvēlām failu, lai sāktu lasīšanu
			dis.read(fileData); // * - Nolasām failu masīvā
			dis.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		/*
		Artūrs - 
		Basically, 'back' var saturēt skaitļus līdz izmēram 2 baiti. 
		Koda lasītājs saspiesto failu lasa pa 1 baitam. 
		Tātad izmanto gan 0, gan 1 baitu lai izveidotu kopējo skaitli. 
		Tā kā 1 baits var saturēt skaitļus (-128;127), tad lai dabūtu no diviem baitiem vienu pozitīvu skaitli mēs izdaram ("1.baits"+128)*256+("2.baits"+128). 
		Priekš 'length' ir tikai viens baits, tāpēc tikai +128
		*/

		int i = 0, tempb = 0, templ = 0; 
		for (int content : fileData) { // * - Cikls datiem, lai dekodētu (lasām pa 1 baitu)
			// purebyte.add((byte) content);
			switch (i % 4) { // * - If statements, lai pareizi dekodētu datus
			case 0, 1: // * - Dabūt pozitīvu skaitli no diviem baitiem
				content+=128;
				tempb *= 256;
				tempb += content;
				break;
			case 2:
				templ = content+128;
				break;
			case 3:
				decrypttriple.add(new LZ77_triple(tempb, templ, (byte) content));
				//new LZ77_triple(tempb, templ, (byte) content).print();
				tempb=0;
				break;
			}
			i++;
		}

	}

	// * - Metode, lai uzsāktu faila enkodēšanu (detalizēti)
	public void encode() {
		for (int i = 0; i < purebyte.size(); i++) { // * - Cikls katram purebyte masīvā

			int maxlength = 0, length = 0, pos = i;

			int j = i - bufferlength;

			if (j < 0) // * - Ja bufera garums ir lielāks, nekā pozicīja, tad piešķir 0
				j = 0;

				// * - Cikls ar mainīgo j iekšā ciklā ar purebyte, pildīt līdz momentam, kad garums ir lielāks, nekā virknes garums
			for (/* int j = 0*/; (j < i || (j < purebyte.size() && length > 0 )) && length < stringlength; j++) {
				if (i + length + 1 < purebyte.size() && purebyte.get(j) == purebyte.get(i + length)) { // * - Ja pozīcijas, garuma un 1 summa ir mazāk, nekā purebyte skaits, un purebyte ar indeksu j ir vienāds ar purebyte ar indeksu i + garums
					length++;
					if (maxlength <= length) { // * - Ja garums ir lielāks, nekā maksimālais, atjaunot maksimālo
						maxlength = length;
						pos = j - maxlength + 1;
					}
				} else if (length != 0) { // * - Ja garums nav vienāds ar 0, tad atjaunot j un anulēt garumu
					j = j - length;
					length = 0;
				}
			}

			LZ77_triple temp = new LZ77_triple((int) (i - pos) /* attalums */, (int) maxlength /* garums */, purebyte.get(i + maxlength));
			i = i + maxlength;
			// temp.print();
			triplelist.add(temp);

		}
	}

	// * - Metode, lai uzsāktu faila dekodēšanu (detalizēti)
	public void decode() {
		int reader = 0; // lasīšanas pozīcija
		for (LZ77_triple t : decrypttriple   /* saspiests array list */) {
			//t.print();
			reader = decryptbyte.size()
					/* tuksh uzpustais array list */ - (t.getback()) /* atgriez attalumu atpakaļ */;
			for (int i = t.getlength() /* garumu */; i > 0; reader++, i--) {
				//System.out.println(reader);
				decryptbyte.add(decryptbyte.get(reader)); // pārveidot uz ArrayList
				// i--;
			}
			decryptbyte.add(t.getnext());
		}
	}

	// * - Metode, lai ierakstītu enkodētu failu
	public void encryptFile(String filename) { // ieraksta sakompresētu iekšā failā

		try {
			FileOutputStream out = new FileOutputStream(filename);

			for (LZ77_triple t : triplelist) {

				try { // skaisti <3
					out.write((byte) (((t.getback()) /256) % 256)-128);
					out.write((byte) (((t.getback())) % 256)-128);
					out.write((byte) t.getlength()-128);
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

	// * - Metode, lai ierakstītu dekodētu failu
	public void decodedFile(String filename) { // printē uzpūsto failu !failā!

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

	// * - Metode, lai parādītu cik tika samazināts fails
	public void compressionRatio(String fileName, String compressed) {
		File f = new File(fileName);
		File c = new File(compressed);
		double fileSize = f.length();
		double SizeCompressed = c.length();

		// double ratio = SizeCompressed / fileSize;
    double ratio = fileSize / SizeCompressed;

		System.out.println("The ratio: " + ratio);
	}
}