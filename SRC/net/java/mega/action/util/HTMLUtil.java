/*  ------------------
 *  MEGA Web Framework
 *  ------------------
 *  
 *  Copyright 2006 SysVision - Consultadoria e Desenvolvimento em Sistemas de Inform�tica, Lda.
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
	public static String filter(String obj) {
		if (obj == null) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		char c = '\0';

		for (int i = 0; i < obj.length(); i++) {
			c = obj.charAt(i);

			switch (c) {
			case '�':
				buffer.append("&laquo;");
				break;
			case '�':
				buffer.append("&raquo;");
				break;
			case '�':
				buffer.append("&euro;");
				break;
			case '<':
				buffer.append("&lt;");
				break;
			case '>':
				buffer.append("&gt;");
				break;
			case '&':
				buffer.append("&amp;");
				break;
			case '"':
				buffer.append("&quot;");
				break;
			case '\'':
				buffer.append("&#39;");
				break;
			case '�':
				buffer.append("&ordm;");
				break;
			case '�':
				buffer.append("&ordf;");
				break;
			case '�':
				buffer.append("&ccedil;");
				break;
			case '�':
				buffer.append("&Ccedil;");
				break;
			case '�':
				buffer.append("&ntilde;");
				break;
			case '�':
				buffer.append("&Ntilde;");
				break;
			case '�':
				buffer.append("&aacute;");
				break;
			case '�':
				buffer.append("&agrave;");
				break;
			case '�':
				buffer.append("&atilde;");
				break;
			case '�':
				buffer.append("&acirc;");
				break;
			case '�':
				buffer.append("&Aacute;");
				break;
			case '�':
				buffer.append("&Agrave;");
				break;
			case '�':
				buffer.append("&Atilde;");
				break;
			case '�':
				buffer.append("&Acirc;");
				break;
			case '�':
				buffer.append("&eacute;");
				break;
			case '�':
				buffer.append("&egrave;");
				break;
			case '�':
				buffer.append("&ecirc;");
				break;
			case '�':
				buffer.append("&Eacute;");
				break;
			case '�':
				buffer.append("&Egrave;");
				break;
			case '�':
				buffer.append("&Ecirc;");
				break;
			case '�':
				buffer.append("&iacute;");
				break;
			case '�':
				buffer.append("&igrave;");
				break;
			case '�':
				buffer.append("&icirc;");
				break;
			case '�':
				buffer.append("&Iacute;");
				break;
			case '�':
				buffer.append("&Igrave;");
				break;
			case '�':
				buffer.append("&Icirc;");
				break;
			case '�':
				buffer.append("&oacute;");
				break;
			case '�':
				buffer.append("&ograve;");
				break;
			case '�':
				buffer.append("&otilde;");
				break;
			case '�':
				buffer.append("&ocirc;");
				break;
			case '�':
				buffer.append("&Oacute;");
				break;
			case '�':
				buffer.append("&Ograve;");
				break;
			case '�':
				buffer.append("&Otilde;");
				break;
			case '�':
				buffer.append("&Ocirc;");
				break;
			case '�':
				buffer.append("&uacute;");
				break;
			case '�':
				buffer.append("&ugrave;");
				break;
			case '�':
				buffer.append("&ucirc;");
				break;
			case '�':
				buffer.append("&Uacute;");
				break;
			case '�':
				buffer.append("&Ugrave;");
				break;
			case '�':
				buffer.append("&Ucirc;");
				break;
			default:
				buffer.append(c);
			}
		}

		return buffer.toString();
	}
}
