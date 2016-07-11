package com.share.commons.util;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class FileUtil {

	public static void Delete(File source) {
		if (source == null) {
			return;
		}
		if (source.isDirectory()) {
			for (File file : source.listFiles()) {
				Delete(file);
			}
		}
		source.delete();
	}

	public static void Copy(String source, String destPath) throws IOException {
		CopyFile(new File(source), new File(destPath));
	}

	public static void CopyFile(String source, String destPath, String suffix)
			throws IOException {
		File sourceFile = new File(source);
		copyDeep(sourceFile, new File(destPath), suffix);
	}

	public static void CopyFile(File source, File dest) throws IOException {
		copyDeep(source, dest, null);
	}

	private static void copy(File source, File dest) throws IOException {
		if (source == null || dest == null) {
			return;
		}
		if (source.exists()) {
			File destParent = dest.getParentFile();
			if (destParent.exists()) {
				if (!destParent.isDirectory()) {
					destParent.delete();
					destParent.mkdirs();
				}
			} else {
				destParent.mkdirs();
			}

			FileInputStream in = new FileInputStream(source);
			byte[] fileByte = new byte[in.available()];
			if (dest.isFile() && !dest.exists())
				dest.createNewFile();
			FileOutputStream out = new FileOutputStream(dest);
			int size;
			while ((size = in.read(fileByte)) > 0) {
				out.write(fileByte, 0, size);
			}
			out.flush();
			closeIO(out, in);
		}
	}

	public static void closeIO(Closeable... closeables) {
		for (Closeable closeable : closeables) {
			if (closeable != null) {
				try {
					closeable.close();
				} catch (Exception e) {
					System.err.println(e.getLocalizedMessage());
				}
			}
		}
	}

	/**
	 * do nothing when source or destination is NULL or source cannot be read or
	 * destination cannot be written
	 * 
	 * @param source
	 * @param dest
	 * @param suffix
	 * @throws IOException
	 */
	private static void copyDeep(File source, File dest, String suffix)
			throws IOException {
		if (source == null || dest == null || !source.canRead()
				|| !checkAccess4Write(dest)) {
			return;
		} else if (source.isDirectory()) {
			safeDeepCopy(source, dest, suffix);
		} else if (suffix == null || suffix.isEmpty()
				|| dest.getName().endsWith(suffix)) {
			copy(source, dest);
		}
	}

	public static boolean checkAccess4Write(File dest) {
		return dest.exists() ? dest.canWrite() : checkAccess4Write(dest
				.getParentFile());
	}

	/**
	 * after check access for writing
	 * 
	 * @param source
	 * @param dest
	 * @param suffix
	 * @throws IOException
	 */
	private static void safeDeepCopy(File source, File dest, String suffix)
			throws IOException {
		if (source.isDirectory()) {
			for (String item : source.list()) {
				File child = new File(source, item);
				if (child.canRead()
						&& (child.isDirectory() || suffix == null
								|| suffix.isEmpty() || item.endsWith(suffix))) {
					safeDeepCopy(child, new File(dest, item), suffix);
				}
			}
		} else {
			copy(source, dest);
		}
	}
}
