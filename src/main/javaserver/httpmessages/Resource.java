package main.javaserver.httpmessages;

import java.io.File;
import java.util.List;
import java.util.Map;

import main.javaserver.WebServer;
import main.javaserver.confreaders.HttpdConf;

public class Resource {

    private String absolutePath;
    private boolean isScript;
    private boolean isProtected;
	private String accessFilePath;

    public Resource (String uri, HttpdConf httpdConf) {
        absolutePath = setAbsolutePath(uri, httpdConf);
	}

	private String setAbsolutePath(String uri, HttpdConf httpdConf) {
		uri = resolveAliases(uri, httpdConf);
		uri = resolveDirectoryIndexes(uri, httpdConf);
		isProtected = resolveIsProtected(uri, httpdConf);

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

		for (Map.Entry<String, String> scriptAliasSet : scriptAliasMap.entrySet()) {
			String scriptAlias = scriptAliasSet.getKey();
			String realDir = scriptAliasSet.getValue();
			boolean hasTrailSlashAlias = scriptAlias.charAt(scriptAlias.length()-1) == '/';
			boolean hasTrailSlashRealDir = realDir.charAt(realDir.length()-1) == '/';

			if ((hasTrailSlashAlias && hasTrailSlashRealDir) || (!hasTrailSlashAlias && !hasTrailSlashRealDir)) {
				if (uri.matches(".*"+scriptAlias+".*")) {
					uri = uri.replace(scriptAlias, realDir);
					isScript = true;
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

	private boolean resolveIsProtected(String uri, HttpdConf httpdConf) {
		String accessFileName = httpdConf.getAccessFileName();
		String uriTotal = "";
		boolean isProtected = false;

		for (String uriPiece : uri.split("/")) {
			uriTotal += uriPiece + "/";

			File file = new File(uriTotal + accessFileName);
			if (file.isFile()) {
				isProtected = true;
				accessFilePath = uriTotal + accessFileName;
				WebServer.addInitializeHtaccess(uriTotal + accessFileName);
				break;
			}
		}

		return isProtected;
	}

	public String getAccessFilePath() {
		return accessFilePath;
	}

	/**
	 * @return the isProtected
	 */
	public boolean isProtected() {
		return isProtected;
	}

	/**
	 * @return the isScript
	 */
	public boolean isScript() {
		return isScript;
	}

	/**
	 * @return the absolutePath
	 */
	public String getAbsolutePath() {
		return absolutePath;
	}

}