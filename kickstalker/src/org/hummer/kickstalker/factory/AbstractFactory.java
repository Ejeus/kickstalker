/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.factory;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.io.StreamCorruptedException;

import android.content.Context;

/**
 * Generic static methods to retrieve objects from private
 * file storage, or persist them to the file storage.
 * 
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class AbstractFactory {

	/**
	 * @param context, Context. The current context.
	 * @param object, Serializable. The object to persist.
	 * @param filename, String. The filename to use.
	 * @throws IOException
	 */
	public static void store(Context context, Serializable object, String filename) 
			throws IOException{
		
		OutputStream os = context.openFileOutput(filename, Context.MODE_PRIVATE);
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(object);
		
		oos.close();
		
	}
	
	/**
	 * @param context, Context. The current context.
	 * @param fileName, String. The filename of the file to load.
	 * @return Object. The object contained in the file.
	 * @throws StreamCorruptedException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public static Object load(Context context, String fileName) 
			throws StreamCorruptedException, IOException, ClassNotFoundException{
		
		InputStream is = context.openFileInput(fileName);
		ObjectInputStream ois = new ObjectInputStream(is);
		
		return ois.readObject();
		
	}
	
}
