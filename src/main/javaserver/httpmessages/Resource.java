package main.javaserver.httpmessages;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import main.javaserver.WebServer;
import main.javaserver.confreaders.Htaccess;
import main.javaserver.confreaders.HttpdConf;

public class Resource {

    private String absolutePath;
    private boolean isScript;
    private boolean isProtected;
	private String accessFilePath;

    public Resource (String uri, HttpdConf httpdConf) throws FileNotFoundException, IOException {
		absolutePath = setAbsolutePath(uri, httpdConf);
		System.out.println("Request resource: " + absolutePath);
	}

	private String setAbsolutePath(String uri, HttpdConf httpdConf) throws FileNotFoundException, IOException {
		uri = resolveAliases(uri, httpdConf);
		uri = resolveRelativeURI(uri, httpdConf);
		uri = resolveDirectoryIndexes(uri, httpdConf);
		isProtected = resolveIsProtected(uri, httpdConf);

		return uri;
	}

	/**
	 * Handles resolving httpd.conf Alias and ScriptAlias directives by replacing the mapped values over the matched substrings within the URI.
	 * @param uri The URI potentially containing aliases to be mapped to official directories.
	 * @param httpdConf The HttpdConf object containing alias mappings to use.
	 * @return The official URI to a resource within the server.
	 */
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

	private String resolveRelativeURI(String uri, HttpdConf httpdConf) {
		if (uri.charAt(0) != '/') return uri;
		String root = (httpdConf.getDocumentRoot() == null) ? httpdConf.getServerRoot() : httpdConf.getDocumentRoot();

		return root + uri;
	}

	private String resolveDirectoryIndexes(String uri, HttpdConf httpdConf) {
		File resourceFile = new File(uri);
		if (resourceFile.isFile()) return uri;
		if (resourceFile.isDirectory() &&  uri.charAt(uri.length()-1) != '/') uri += "/";

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

	private boolean resolveIsProtected(String uri, HttpdConf httpdConf) throws FileNotFoundException, IOException {
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

	public Htaccess getAccessFile() {
		return WebServer.getHtaccess(accessFilePath);
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