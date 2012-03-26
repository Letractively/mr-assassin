package com.badideastudios.mrassassin;

import org.xml.sax.helpers.DefaultHandler;

public interface XMLDelegate {
	void parseComplete(DefaultHandler handler, Boolean result);

}
