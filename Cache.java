import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Cache {

	static int l1_s;
	static int l1_b;
	static int l1_tag;

	static int l2_s;
	static int l2_b;
	static int l2_tag;

	static int l1_S;
	static int l1_E;
	static int l1_B;

	static int l2_S;
	static int l2_E;
	static int l2_B;

	static Line[][] L1I;
	static Line[][] L1D;
	static Line[][] L2;

	static int L1I_hit = 0;
	static int L1I_miss = 0;
	static int L1I_eviction = 0;

	static int L1D_hit = 0;
	static int L1D_miss = 0;
	static int L1D_eviction = 0;

	static int L2_hit = 0;
	static int L2_miss = 0;
	static int L2_eviction = 0;

	static List<String> ram = null;

	public static void main(String[] args) throws FileNotFoundException {

	

		Scanner scanner = new Scanner(System.in);

		l1_s = scanner.nextInt();
		l1_E = scanner.nextInt();
		l1_b = scanner.nextInt();

		l2_s = scanner.nextInt();
		l2_E = scanner.nextInt();
		l2_b = scanner.nextInt();
		
		 // Creating a File object that represents the disk file. 
       PrintStream o = new PrintStream(new File("out.txt")); 
 
       // Store current System.out before assigning a new value 
       PrintStream console = System.out; 
		
		l1_S = (int) Math.pow(2, l1_s);
		l1_B = (int) Math.pow(2, l1_b);
		l2_S = (int) Math.pow(2, l2_s);
		l2_B = (int) Math.pow(2, l2_b);

//		tag = 32- b -s 
		l1_tag = 32 - l1_b - l1_s;

		l2_tag = 32 - l2_b - l1_s;

		createCache(l1_S, l1_E, l1_B, l2_S, l2_E, l2_B);

		try (FileReader readerRam = new FileReader("ram.txt"); BufferedReader br = new BufferedReader(readerRam)) {
			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				ram = new ArrayList<>(Arrays.asList(line.split(" ")));

			}
			readerRam.close();
			br.close();

		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

		try (FileReader reader = new FileReader("test_large.trace"); BufferedReader br = new BufferedReader(reader)) {
			// read line by line
			String line;
			String[] splitted = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				splitted = line.split("\\s+|,\\s*|\\.\\s*");

				string_instruction(splitted);

				// bitince nullasın arrayi
				splitted = null;
			}

			// Assign o to output stream 
			System.setOut(o); 

			System.out.print("L1I-hits:	" + L1I_hit + "\t");
			System.out.print("L1I-misses:	" + L1I_miss + "\t");
			System.out.print("L1I-evictions:	" + L1I_eviction + "\n");

			System.out.print("L1D-hits:	" + L1D_hit + "\t");
			System.out.print("L1D-misses:	" + L1D_miss + "\t");
			System.out.print("L1D-evictions:	" + L1D_eviction + "\n");

//			L2-hits:1 L2-misses:2 L2-evictions:0
			System.out.print("L2-hits:	" + L2_hit + "\t");
			System.out.print("L2-misses:	" + L2_miss + "\t");
			System.out.print("L2-evictions:	" + L2_eviction + "\n\n");

			
			printCache(L1I, L1D, L2);

			
	
		} catch (IOException e) {
			System.err.format("IOException: %s%n", e);
		}

	}

	public static void createCache(int l1S, int l1E, int l1B, int l2S, int l2E, int l2B) {

		L1I = new Line[l1S][l1E];
		L1D = new Line[l1S][l1E];
		L2 = new Line[l2S][l2E];

		for (int i = 0; i < l1S; i++) {
			for (int j = 0; j < l1E; j++) {
				L1I[i][j] = new Line();
				L1D[i][j] = new Line();
			}
		}

		for (int i = 0; i < l2S; i++) {
			for (int j = 0; j < l2E; j++) {
				L2[i][j] = new Line();
			}
		}
	}

	public static int getDecimal(String hex) {

		String digits = "0123456789ABCDEF";
		hex = hex.toUpperCase();
		int val = 0;
		for (int i = 0; i < hex.length(); i++) {
			char c = hex.charAt(i);
			int d = digits.indexOf(c);
			val = 16 * val + d;
		}
		return val;
	}

	public static String hexToBinary(String hex) {

		// This function takes a string which includes just full of hexadecimal numbers
		// and converts them in to appropriate binary representation

		int length = hex.length();// length variable just a control variable for while loop end point
		String unsign = "";// this unsign string value for storing converted whole binary number

		int i = 0;
		while (i < length) {
			switch (hex.charAt(i)) {// assigned related hexadecimal value to its binary representation
			case '0':
				unsign = unsign + "0000";
				break;
			case '1':
				unsign = unsign + "0001";
				break;
			case '2':
				unsign = unsign + "0010";
				break;
			case '3':
				unsign = unsign + "0011";
				break;
			case '4':
				unsign = unsign + "0100";
				break;
			case '5':
				unsign = unsign + "0101";
				break;
			case '6':
				unsign = unsign + "0110";
				break;
			case '7':
				unsign = unsign + "0111";
				break;
			case '8':
				unsign = unsign + "1000";
				break;
			case '9':
				unsign = unsign + "1001";
				break;
			case 'a':
				unsign = unsign + "1010";
				break;
			case 'b':
				unsign = unsign + "1011";
				break;
			case 'c':
				unsign = unsign + "1100";
				break;
			case 'd':
				unsign = unsign + "1101";
				break;
			case 'e':
				unsign = unsign + "1110";
				break;
			case 'f':
				unsign = unsign + "1111";
				break;
			default:
			}
			i++;
		}

		return unsign;
	}

	// trace filedan alınan string
	public static void string_instruction(String[] instruction) {

		String binary = hexToBinary(instruction[1]);

		int decAddress = Integer.parseInt(instruction[1], 16);

		String L1_tag_binary = "";
		String L1_block_binary = "";
		String L1_set_binary = "";

		String L2_tag_binary = "";
		String L2_block_binary = "";
		String L2_set_binary = "";

		String L1_tag_string = "";
		String L1_block_string = "";
		String L1_set_string = "";

		String L2_tag_string = "";
		String L2_block_string = "";
		String L2_set_string = "";

		int L1_set_dec = 0;
		int L2_set_dec = 0;

		int L1_tag_dec = 0;
		int L2_tag_dec = 0;

		int L1_block_dec = 0;
		int L2_block_dec = 0;

		int blockSize = Integer.parseInt(instruction[2]);

		int L1I_write_index = 0;
		int L1D_write_index = 0;

		int L2_write_index = 0;

		int L1I_min_time = 1000000;
		int L1D_min_time = 1000000;

		int L2_min_time = 1000000;

		boolean isL1I_hit = false;
		boolean isL1I_miss = false;

		boolean isL1D_hit = false;
		boolean isL1D_miss = false;

		boolean isL2_hit = false;
		boolean isL2_miss = false;

		if (instruction[0].equals("I")) {

			L1_tag_binary = binary.substring(0, l1_tag);
			L1_tag_dec = Integer.parseInt(L1_tag_binary, 2);
			L1_tag_string = Integer.toHexString(L1_tag_dec);

			if (l1_s != 0) {
				L1_set_binary = binary.substring(l1_tag, (l1_tag + l1_s));
				L1_set_dec = Integer.parseInt(L1_set_binary, 2);
				L1_set_string = Integer.toHexString(L1_set_dec);

			} else {
				L1_set_dec = 0;

			}

			L1_block_binary = binary.substring(l1_tag + l1_s);
			L1_block_dec = Integer.parseInt(L1_block_binary, 2);

			// ramdan alıyoruz block offset kadarını
			for (int i = decAddress; i < (l1_B + decAddress); i++) {// decAddress+dec
				L1_block_string = L1_block_string + "" + ram.get(i);
			}

			for (int j = 0; j < l1_E; j++) {

				if (L1I[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1I[L1_set_dec][j].isV())) {
					L1I_hit++;
					isL1I_hit = true;

					System.out.print("L1I hit, ");
					break;

				}

			}

			if (!isL1I_hit) {
				System.out.print("L1I miss, ");
				for (int j = 0; j < l1_E; j++) {
					if (!L1I[L1_set_dec][j].isV()) {
						L1I_write_index = j;
						L1I_miss++;
						isL1I_miss = true;
						break;
					} else {
						if (L1I[L1_set_dec][j].getTime() < L1I_min_time) {
							L1I_min_time = L1I[L1_set_dec][j].getTime();
							L1I_write_index = j;
						}
					}
				}
				if (!isL1I_miss) {
					L1I_eviction++;
				}

				L1I[L1_set_dec][L1I_write_index].updateLine(true, L1_tag_string, 1, L1_block_string);
			} // L1I end

			L2_tag_binary = binary.substring(0, l2_tag);
			L2_tag_dec = Integer.parseInt(L2_tag_binary, 2);
			L2_tag_string = Integer.toHexString(L2_tag_dec);

			L2_set_binary = binary.substring(l2_tag, l2_tag + l2_s);
			L2_set_dec = Integer.parseInt(L2_set_binary, 2);
			L2_set_string = Integer.toHexString(L2_set_dec);

			L2_block_binary = binary.substring(l2_tag + l2_s);
			L2_block_dec = Integer.parseInt(L2_block_binary, 2);
			// l2_block_s=Integer.toHexString(dec);

			// ramdan alıyoruz block offset kadarını
			for (int i = decAddress; i < (l2_B + decAddress); i++) {// decAddress+dec
				L2_block_string = L2_block_string + "" + ram.get(i);
			}

			for (int j = 0; j < l2_E; j++) {

				if (L2[L2_set_dec][j].getTag().equals(L2_tag_string) && (L2[L2_set_dec][j].isV())) {
					L2_hit++;
					isL2_hit = true;

					System.out.print("L2 hit.");
					break;
				}
			}

			if (!isL2_hit) {

				System.out.print("L2 miss.\n");
				for (int j = 0; j < l2_E; j++) {
					if (!L2[L2_set_dec][j].isV()) {
						L2_write_index = j;

						L2_miss++;
						isL2_miss = true;
						break;
					} else {
						if (L2[L2_set_dec][j].getTime() < L2_min_time) {
							L2_min_time = L2[L2_set_dec][j].getTime();
							L2_write_index = j;
						}
					}
				}
				if (!isL2_miss) {
					L2_eviction++;
				}

				L2[L2_set_dec][L2_write_index].updateLine(true, L2_tag_string, 1, L2_block_string);
				System.out.println("Place in L2 set " + L2_set_dec + " , L1I.\n");
			}

		} // if I end

		else if (instruction[0].equals("L")) {

			L1_tag_binary = binary.substring(0, l1_tag);
			L1_tag_dec = Integer.parseInt(L1_tag_binary, 2);
			L1_tag_string = Integer.toHexString(L1_tag_dec);

			if (l1_s != 0) {
				L1_set_binary = binary.substring(l1_tag, (l1_tag + l1_s));
				L1_set_dec = Integer.parseInt(L1_set_binary, 2);
				L1_set_string = Integer.toHexString(L1_set_dec);

			} else {
				L1_set_dec = 0;

			}

			L1_block_binary = binary.substring(l1_tag + l1_s);
			L1_block_dec = Integer.parseInt(L1_block_binary, 2);

			// ramdan alıyoruz block offset kadarını
			for (int i = decAddress; i < (l1_B + decAddress); i++) {// decAddress+dec
				L1_block_string = L1_block_string + "" + ram.get(i);
			}

			for (int j = 0; j < l1_E; j++) {

				if (L1D[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1D[L1_set_dec][j].isV())) {
					L1D_hit++;
					isL1D_hit = true;

					System.out.print("L1D hit, ");
					break;
				}

			}

			if (!isL1D_hit) {

				System.out.print("L1D miss, ");
				for (int j = 0; j < l1_E; j++) {
					if (!L1D[L1_set_dec][j].isV()) {
						L1D_write_index = j;

						L1D_miss++;
						isL1D_miss = true;
						break;
					} else {
						if (L1D[L1_set_dec][j].getTime() < L1D_min_time) {
							L1D_min_time = L1D[L1_set_dec][j].getTime();
							L1D_write_index = j;
						}
					}
				}
				if (isL1D_miss) {
					L1D_eviction++;
				}

				L1D[L1_set_dec][L1I_write_index].updateLine(true, L1_tag_string, 1, L1_block_string);
			} // L1D end

			L2_tag_binary = binary.substring(0, l2_tag);
			L2_tag_dec = Integer.parseInt(L2_tag_binary, 2);
			L2_tag_string = Integer.toHexString(L2_tag_dec);

			L2_set_binary = binary.substring(l2_tag, l2_tag + l2_s);
			L2_set_dec = Integer.parseInt(L2_set_binary, 2);
			L2_set_string = Integer.toHexString(L2_set_dec);

			L2_block_binary = binary.substring(l2_tag + l2_s);
			L2_block_dec = Integer.parseInt(L2_block_binary, 2);
			// l2_block_s=Integer.toHexString(dec);

			// ramdan alıyoruz block offset kadarını
			for (int i = decAddress; i < (l2_B + decAddress); i++) {// decAddress+dec
				L2_block_string = L2_block_string + "" + ram.get(i);
			}

			for (int j = 0; j < l2_E; j++) {

				if (L2[L2_set_dec][j].getTag().equals(L2_tag_string) && (L2[L2_set_dec][j].isV())) {
					L2_hit++;
					isL2_hit = true;

					System.out.print("L2 hit.\n");
					break;
				}
			}

			if (!isL2_hit) {

				System.out.print("L2 miss.\n");
				for (int j = 0; j < l2_E; j++) {
					if (!L2[L2_set_dec][j].isV()) {
						L2_write_index = j;

						L2_miss++;
						isL2_miss = true;
						break;
					} else {
						if (L2[L2_set_dec][j].getTime() < L2_min_time) {
							L2_min_time = L2[L2_set_dec][j].getTime();
							L2_write_index = j;
						}
					}
				}
				if (!isL2_miss) {
					L2_eviction++;
				}

				L2[L2_set_dec][L2_write_index].updateLine(true, L2_tag_string, 1, L2_block_string);
				System.out.println("Place in L2 set " + L2_set_dec + " , L1D.\n");
			}

		} // if L end

		else if (instruction[0].equals("S")) { // store operation
			
			// given data stored at given address
				// look the  caches, if the data already placed cache, update caches and RAM
					// if data don't placed at cache do nothing on caches only update RAM 

			L1_tag_binary = binary.substring(0, l1_tag);
			L1_tag_dec = Integer.parseInt(L1_tag_binary, 2);
			L1_tag_string = Integer.toHexString(L1_tag_dec);

			String insORdata = "";

			if (l1_s != 0) {
				L1_set_binary = binary.substring(l1_tag, (l1_tag + l1_s));
				L1_set_dec = Integer.parseInt(L1_set_binary, 2);
				L1_set_string = Integer.toHexString(L1_set_dec);

			} else {
				L1_set_dec = 0;

			}

			L1_block_binary = binary.substring(l1_tag + l1_s);
			L1_block_dec = Integer.parseInt(L1_block_binary, 2);

			for (int i = decAddress; i < (Integer.parseInt(instruction[2]) + decAddress); i++) {
				L1_block_string = L1_block_string + "" + ram.get(i);
			}

			for (int j = 0; j < l1_E; j++) {

				if (L1D[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1D[L1_set_dec][j].isV())) {
					L1D_hit++;
					isL1D_hit = true;

					System.out.print("L1D hit, ");
					L1D[L1_set_dec][j].updateLine(true, L1_tag_string, 1, instruction[3].toUpperCase());
					insORdata = "D";

					break;
				}
			}

			if (!isL1D_hit) {
				L1D_miss++;
				System.out.print("L1D miss, ");
			}

			for (int j = 0; j < l1_E; j++) {

				if (L1I[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1I[L1_set_dec][j].isV())) {
					L1I_hit++;
					isL1I_hit = true;

					System.out.print("L1I hit, ");
					L1I[L1_set_dec][j].updateLine(true, L1_tag_string, 1, instruction[3].toUpperCase());
					insORdata = "I";

					break;
				}
			}

			if (!isL1I_hit) {
				L1I_miss++;
				System.out.print("L1I miss, ");
			}

			L2_tag_binary = binary.substring(0, l2_tag);
			L2_tag_dec = Integer.parseInt(L2_tag_binary, 2);
			L2_tag_string = Integer.toHexString(L2_tag_dec);

			L2_set_binary = binary.substring(l2_tag, l2_tag + l2_s);
			L2_set_dec = Integer.parseInt(L2_set_binary, 2);
			L2_set_string = Integer.toHexString(L2_set_dec);

			L2_block_binary = binary.substring(l2_tag + l2_s);
			L2_block_dec = Integer.parseInt(L2_block_binary, 2);

			for (int j = 0; j < l2_E; j++) {

				if (L2[L2_set_dec][j].getTag().equals(L2_tag_string) && (L2[L2_set_dec][j].isV())) {
					L2_hit++;
					isL2_hit = true;

					System.out.print("L2 hit.\n");
					L2[L2_set_dec][j].updateLine(true, L2_tag_string, 1, instruction[3].toUpperCase());

					break;
				}

			}

			if (!isL2_hit) {
				L2_miss++;
				System.out.print("L2 miss.\n");
			}

			int ramIndex = decAddress;
			int stringIndex = 0;

			for (int i = 0; i < Integer.parseInt(instruction[2]); i++) {

				ram.set(ramIndex, (instruction[3].substring(stringIndex, stringIndex + 2)).toUpperCase());
				ramIndex++;
				stringIndex += 2;

			}
			System.out.println("Store in L1" + insORdata + " ,L2 ,RAM.\n");

		} // if S end

		else if (instruction[0].equals("M")) { // modify operation
			
			// 1. do the load operation: if the data don't placed in caches, place the data in available caches
				// 2. do the store operation: if the data placed in caches, update it, if don't do nothing on caches
					// then update RAM

			L1_tag_binary = binary.substring(0, l1_tag);
			L1_tag_dec = Integer.parseInt(L1_tag_binary, 2);
			L1_tag_string = Integer.toHexString(L1_tag_dec);

			if (l1_s != 0) {
				L1_set_binary = binary.substring(l1_tag, (l1_tag + l1_s));
				L1_set_dec = Integer.parseInt(L1_set_binary, 2);
				L1_set_string = Integer.toHexString(L1_set_dec);

			} else {
				L1_set_dec = 0;

			}

			L1_block_binary = binary.substring(l1_tag + l1_s);
			L1_block_dec = Integer.parseInt(L1_block_binary, 2);

			// modify load

			int modifyUpdate = 0;

			for (int j = 0; j < l1_E; j++) {// for load, is old data placed in L1 data cache?

				if (L1D[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1D[L1_set_dec][j].isV())) {// if yes increment L1D_hit
					L1D_hit++;
					isL1D_hit = true;
					j = modifyUpdate;

					System.out.print("L1D hit, ");
					break;
				}

			}

			if (!isL1D_hit) {//  if there is no hit write the new data to L1D cache

				L1D_miss++;
				L1D[L1_set_dec][modifyUpdate].updateLine(true, L1_tag_string, 1, instruction[3].toUpperCase());
				System.out.print("L1D miss, ");

			} // L1D end

			L2_tag_binary = binary.substring(0, l2_tag);
			L2_tag_dec = Integer.parseInt(L2_tag_binary, 2);
			L2_tag_string = Integer.toHexString(L2_tag_dec);

			L2_set_binary = binary.substring(l2_tag, l2_tag + l2_s);
			L2_set_dec = Integer.parseInt(L2_set_binary, 2);
			L2_set_string = Integer.toHexString(L2_set_dec);

			L2_block_binary = binary.substring(l2_tag + l2_s);
			L2_block_dec = Integer.parseInt(L2_block_binary, 2);

			for (int j = 0; j < l2_E; j++) {// fpr load, is old data placed in L2 cache?

				if (L2[L2_set_dec][j].getTag().equals(L2_tag_string) && (L2[L2_set_dec][j].isV())) {// if yes increment L2_hit
					L2_hit++;
					isL2_hit = true;
					j = modifyUpdate;

					System.out.print("L2 hit.\n");
					break;
				}
			}

			if (!isL2_hit) {// if there is no hit increment L2_miss

				L2_miss++;
				L2[L1_set_dec][modifyUpdate].updateLine(true, L2_tag_string, 1, instruction[3].toUpperCase());// update L2 cache with new
				System.out.print("L2 miss.\n");

			}

			// modify store'u

			for (int j = 0; j < l1_E; j++) {// for store, is old data is placed in L1 data cache?

				if (L1D[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1D[L1_set_dec][j].isV())) {// if yes, increment L1D_hit
					L1D_hit++;
					isL1D_hit = true;

					L1D[L1_set_dec][j].updateLine(true, L1_tag_string, 1, instruction[3].toUpperCase());// if yes, update cache L1D with new
					System.out.print("L1D modified, ");

					break;
				}

			} 

			if (!isL1D_hit) {// if there is no hit, increment L1D_miss
				L1D_miss++;
			}

			for (int j = 0; j < l1_E; j++) {// for store, is old data is placed in L1 instruction cache?

				if (L1I[L1_set_dec][j].getTag().equals(L1_tag_string) && (L1I[L1_set_dec][j].isV())) {// if yes, increment L1I_hit
					L1I_hit++;
					isL1I_hit = true;

					L1I[L1_set_dec][j].updateLine(true, L1_tag_string, 1, instruction[3].toUpperCase());//  if yes, update cache L1I with new
					System.out.print("L1I modified, RAM modified.\n");

					break;
				}
			}

			if (!isL1I_hit) {// if there is no hit, increment L1I_miss
				L1I_miss++;
			}

			
			for (int j = 0; j < l2_E; j++) {// for store, is old data is placed in L2 cache?

				if (L2[L2_set_dec][j].getTag().equals(L2_tag_string) && (L2[L2_set_dec][j].isV())) {// if yes, increment L2_hit
					L2_hit++;
					isL2_hit = true;

					L2[L2_set_dec][j].updateLine(true, L2_tag_string, 1, instruction[3].toUpperCase());// if yes, update cache L2 with new
					System.out.print("L2 modified, RAM modified.\n");

					break;
				}

			}

			if (!isL2_hit) {// if there is no hit, increment L2_miss
				L2_miss++;
			}

			int ramIndex = decAddress;
			int stringIndex = 0;

			for (int i = 0; i < Integer.parseInt(instruction[2]); i++) {// take byte by byte data by trace file

				ram.set(ramIndex, (instruction[3].substring(stringIndex, stringIndex + 2)).toUpperCase());// update RAM
				ramIndex++;// fit true index of RAM for data 
				stringIndex += 2;// byte by byte
			}

		} // if M end

	}// string_instruction() end

	public static void printCache(Line L1I[][], Line L1D[][], Line L2[][]) {// bu dosyaya yazılacak!!

		System.out.println("\t CACHE L1I \t");
		System.out.println("Tag\tTime\tValid\tDataBlock");
		for (int i = 0; i < L1I.length; i++) {
			System.out.println("Set " + i);
			for (int j = 0; j < L1I[i].length; j++) {
				System.out.println(L1I[i][j].toString());
				
//				System.out.print("Line " + j++);
//				System.out.println(
//						L1I[i][j].getTag() + L1I[i][j].getTime() + L1I[i][j].isV() + L1I[i][j].getBlockOffset());
			}
		}
		System.out.println("\n");
		System.out.println("\t CACHE L1D \t");
		for (int i = 0; i < L1D.length; i++) {
			System.out.println("Set " + i);
			for (int j = 0; j < L1D[i].length; j++) {
				System.out.println(L1D[i][j].toString());
				
//				System.out.print("Line " + j++);
//				System.out.println(
//						L1I[i][j].getTag() + L1I[i][j].getTime() + L1I[i][j].isV() + L1I[i][j].getBlockOffset());
			}
		}
		
		System.out.println("\t CACHE L2 \t");
		System.out.println("\n");
		
		for (int i = 0; i < L2.length; i++) {
			System.out.println("Set " + i);
			for (int j = 0; j < L2[i].length; j++) {
				System.out.println(L2[i][j].toString());
				
//				System.out.print("Line " + j++);
//				System.out.println(
//						L1I[i][j].getTag() + L1I[i][j].getTime() + L1I[i][j].isV() + L1I[i][j].getBlockOffset());
			}
		}
		

	}

}
