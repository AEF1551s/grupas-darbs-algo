package algo;

class LZ77_triple { // * - Klase, kas glab datu par enkodētiem simboliem
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
		System.out.print(back+";"+length+";"+(char)next+"\t");
	}
}