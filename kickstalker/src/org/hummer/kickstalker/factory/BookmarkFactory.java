/*
 * This file is subject to the terms and conditions defined in
 * file 'LICENSE.txt', which is part of this source code package.
 */
package org.hummer.kickstalker.factory;

import java.io.IOException;
import java.io.StreamCorruptedException;

import org.hummer.kickstalker.data.BookmarkBundle;
import org.hummer.kickstalker.data.BookmarkBundle.BookmarkType;

import android.content.Context;

/**
 * @author gernot.hummer
 *
 * @version 1.0
 *
 */
public class BookmarkFactory {

	//extension means "BookMarkLibrary", thus bml.
	private static final String EXTENSION = ".bml";
	
	public static void store(Context context, BookmarkBundle bmb) throws IOException{
		BaseFactory.store(context, bmb, 
				bmb.getBookmarkType().toString() + EXTENSION);
	}
	
	public static BookmarkBundle load(Context context, BookmarkType type) 
			throws StreamCorruptedException, IOException, ClassNotFoundException{
		
		return (BookmarkBundle) BaseFactory.load(context, 
				type.toString() + EXTENSION);
		
	}
	
}
