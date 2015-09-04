package com.synisys.msggen.enums;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 6/5/15 <br/>
 * <b>Time:</b> 10:25 PM <br/>
 */
public enum FileType {
	JSP(".jsp"),
	JAVA(".java"),
	XHTML(".xhtml"),
	ALL("*");

	private String extension;

	FileType(String extension) {
		this.extension = extension;
	}

	public String getExtension() {
		return this.extension;
	}

}
