package com.share.commons.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;

public class ArrayPrintWriter extends PrintWriter {
	private LinkedList<String> res;

	public ArrayPrintWriter() {
		super(new StringWriter());
		res = new LinkedList<String>();
	}

	@Override
	public void println() {
		synchronized (lock) {
			flush();
			StringBuffer s = getBuffer();
			res.add(s.toString());
			s.setLength(0);
		}
	}

	public String[] toArray() {
		synchronized (lock) {
			StringBuffer s = getBuffer();
			String remaining = s.toString();
			int size = res.size();
			if (size == 0) {
				return new String[] { remaining };
			}
			String[] array = new String[size + 1];
			String[] narray = res.toArray(array);
			if (narray != array) {
				array = new String[narray.length + 1];
				System.arraycopy(narray, 0, array, 0, narray.length);
			}
			array[array.length - 1] = remaining;
			return array;
		}
	}

	private StringBuffer getBuffer() {
		return ((StringWriter) out).getBuffer();
	}

	public void reset() {
		synchronized (lock) {
			res = new LinkedList<String>();
			StringBuffer buf = getBuffer();
			buf.setLength(0);
			buf.trimToSize();
		}
	}

}
