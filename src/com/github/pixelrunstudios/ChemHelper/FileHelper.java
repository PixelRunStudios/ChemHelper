package com.github.pixelrunstudios.ChemHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public final class FileHelper {

	private FileHelper(){}

	public static byte[] readBytes(File file) throws IOException{
		FileInputStream a = null;
		byte[] d = null;
		try{
			a = new FileInputStream(file);
			int b;
			int c = 0;
			byte[] e = null;
			b = a.read();
			while(b >= 0){
				e = new byte[c + 1];
				if(d != null){
					System.arraycopy(d, 0, e, 0, d.length);
				}
				else{
					d = new byte[1];
				}
				e[c] = (byte) b;
				d = e.clone();
				c++;
				b = a.read();
			}
		}
		finally{
			if(a != null){
				a.close();
			}
		}
		return d;
	}

	public static void writeBytes(File file, byte[] ba) throws IOException{
		FileOutputStream a = null;
		try{
			a = new FileOutputStream(file);
			a.write(ba);
		}
		finally{
			if(a != null){
				a.close();
			}
		}
	}

	public static String readFile(File file) throws IOException{

		FileReader a = null;
		char[] d = null;
		try{
			a = new FileReader(file);
			int b;
			int c = 0;
			char[] e = null;
			b = a.read();
			while(b >= 0){
				e = new char[c + 1];
				if(d != null){
					System.arraycopy(d, 0, e, 0, d.length);
				}
				else{
					d = new char[1];
				}
				e[c] = (char) b;
				d = e.clone();
				c++;
				b = a.read();
			}
		}
		finally{
			if(a != null){
				a.close();
			}
		}
		if(d != null){
			return String.copyValueOf(d);
		}
		else{
			return "";
		}
	}

	public static String[] readLineFile(File file) throws IOException{
		BufferedReader a = null;
		String[] d = null;
		try{
			a = new BufferedReader(new FileReader(file));
			String b;
			int c = 0;
			String[] e = null;
			b = a.readLine();
			while(b != null){
				e = new String[c + 1];
				if(d != null){
					System.arraycopy(d, 0, e, 0, d.length);
				}
				else{
					d = new String[1];
				}
				e[c] = b;
				d = e.clone();
				c++;
				b = a.readLine();
			}
		}
		finally{
			if(a != null){
				a.close();
			}
		}
		if(d != null){
			return d.clone();
		}
		else{
			return null;
		}
	}

	public static void writeFile(File file, String text) throws IOException{
		FileWriter a = null;
		try{
			a = new FileWriter(file);
			a.write(text);
		}
		finally{
			if(a != null){
				a.close();
			}
		}
	}
}
