package com.policecom.imageactivity;

import java.io.File;
import java.io.FileFilter;
import java.util.Comparator;

//public class FileComparator implements Comparator<PhotoItem> {NewImageItem
public class FileComparator implements Comparator<NewImageItem> {	
	
	public int compare(NewImageItem file1, NewImageItem file2) {
		if (file1.lastModified > file2.lastModified) {
			return -1;
		} else {
			return 1;
		}
	}

	public FileFilter fileFilter = new FileFilter() {
		public boolean accept(File file) {
			String tmp = file.getName().toLowerCase();
			if (tmp.endsWith(".mov") || tmp.endsWith(".jpg")) {
				return true;
			}
			return false;
		}
	};
}
