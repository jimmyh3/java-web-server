package main.javaserver.httpmessages;

import java.io.File;
import java.util.List;
import java.util.Map;

import main.javaserver.confreaders.HttpdConf;

public class Resource {

    private String absolutePath;
    private boolean isScript;
    private boolean isProtected;

    public Resource (String uri, HttpdConf httpdConf) {
        absolutePath = setAbsolutePath(uri, httpdConf);
	}

	private String setAbsolutePath(String uri, HttpdConf httpdConf) {
		uri = resolveAliases(uri, httpdConf);
		uri = resolveDirectoryIndexes(uri, httpdConf);

		return uri;
	}

	private String resolveAliases(String uri, HttpdConf httpdConf) {
		Map<String, String> aliasMap = httpdConf.getAliasMap();
		Map<String, String> scriptAliasMap = httpdConf.getScriptAliasMap();

		for (Map.Entry<String, String> aliasSet : aliasMap.entrySet()) {
			String alias = aliasSet.getKey();
			String realDir = aliasSet.getValue();
			boolean hasTrailSlashAlias = alias.charAt(alias.length()-1) == '/';
			boolean hasTrailSlashRealDir = realDir.charAt(realDir.length()-1) == '/';

			if ((hasTrailSlashAlias && hasTrailSlashRealDir) || (!hasTrailSlashAlias && !hasTrailSlashRealDir)) {
				if (uri.matches(".*"+alias+".*")) {
					uri = uri.replace(alias, realDir);
				}
			}
		}

		return uri;
	}

	private String resolveDirectoryIndexes(String uri, HttpdConf httpdConf) {
		if (uri.charAt(uri.length()-1) != '/') return uri;
		if (uri.equals("/")) uri = httpdConf.getDocumentRoot();

		List<String> directoryIndexes = httpdConf.getDirectoryIndexes();

		for (String index : directoryIndexes) {
			File indexFile = new File(uri + index);
			if (indexFile.isFile()) {
				uri += index;
				break;
			}
		}
		
		return uri;
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