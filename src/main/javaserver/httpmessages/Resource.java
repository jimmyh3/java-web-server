package main.javaserver.httpmessages;

import main.javaserver.confreaders.HttpdConf;

public class Resource {

    private String absolutePath;
    private boolean isScript;
    private boolean isProtected;

    public Resource (String uri, HttpdConf httpConf) {
        
    }

	/**
	 * @return the isProtected
	 */
	public boolean isProtected() {
		return isProtected;
	}

	/**
	 * @param isProtected the isProtected to set
	 */
	public void setIsProtected(boolean isProtected) {
		this.isProtected = isProtected;
	}

	/**
	 * @return the isScript
	 */
	public boolean isScript() {
		return isScript;
	}

	/**
	 * @param isScript the isScript to set
	 */
	public void setIsScript(boolean isScript) {
		this.isScript = isScript;
	}

	/**
	 * @return the absolutePath
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

	/**
	 * @param absolutePath the absolutePath to set
	 */
	public void setAbsolutePath(String absolutePath) {
		this.absolutePath = absolutePath;
	}

    /*
    public Resource (String _uri, HttpdConf _httpConf) {

    }
    */

}