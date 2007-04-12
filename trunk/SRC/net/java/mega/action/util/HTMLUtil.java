/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Informática, Lda.
 *  
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *  
 *  	http://www.apache.org/licenses/LICENSE-2.0
 *  
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package net.java.mega.action.util;

public class HTMLUtil {
	private static final String CHAR_LIST = "«»?<>&\"'ºªçÇñÑáàãâÁÀÃÂéèêÉÈÊíìîÍÌÎóòõôÓÒÕÔúùûÚÙÛ";
	private static final String[] ENT_LIST = { "&laquo;", "&raquo;", "&euro;", "&lt;", "&gt;", "&amp;", "&quot;",
			"&#39;", "&ordm;", "&ordf;", "&ccedil;", "&Ccedil;", "&ntilde;", "&Ntilde;", "&aacute;", "&agrave;",
			"&atilde;", "&acirc;", "&Aacute;", "&Agrave;", "&Atilde;", "&Acirc;", "&eacute;", "&egrave;", "&ecirc;",
			"&Eacute;", "&Egrave;", "&Ecirc;", "&iacute;", "&igrave;", "&icirc;", "&Iacute;", "&Igrave;", "&Icirc;",
			"&oacute;", "&ograve;", "&otilde;", "&ocirc;", "&Oacute;", "&Ograve;", "&Otilde;", "&Ocirc;", "&uacute;",
			"&ugrave;", "&ucirc;", "&Uacute;", "&Ugrave;", "&Ucirc;" };

	public static String filter(String obj) {
		if (obj == null) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();

		char c = '\0';
		int pos = 0;

		for (int i = 0; i < obj.length(); i++) {
			c = obj.charAt(i);
			pos = CHAR_LIST.indexOf(c);

			if (pos == -1) {
				buffer.append(c);
			} else {
				buffer.append(ENT_LIST[pos]);
			}
		}

		return buffer.toString();
	}
}
