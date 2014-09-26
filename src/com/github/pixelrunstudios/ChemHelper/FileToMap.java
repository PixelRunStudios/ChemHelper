package com.github.pixelrunstudios.ChemHelper;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class FileToMap{
	public static  Map<String, String> readMapFromFile(File file) throws IOException{
		return MapStringConverter.stringToMap(FileHelper.readFile(file));
	}

	public static void writeMapToFile(File file, Map<String, String> map) throws IOException{
		FileHelper.writeFile(file, MapStringConverter.mapToString(map));
	}
}
