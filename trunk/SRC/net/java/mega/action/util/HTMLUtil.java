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
	public static String filter(String obj) {
		if (obj == null) {
			return "";
		}

		StringBuffer buffer = new StringBuffer();
		char c = '\0';

		for (int i = 0; i < obj.length(); i++) {
			c = obj.charAt(i);

			switch (c) {
			case '«':
				buffer.append("&laquo;");
				break;
			case '»':
				buffer.append("&raquo;");
				break;
			case '€':
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
			case 'º':
				buffer.append("&ordm;");
				break;
			case 'ª':
				buffer.append("&ordf;");
				break;
			case 'ç':
				buffer.append("&ccedil;");
				break;
			case 'Ç':
				buffer.append("&Ccedil;");
				break;
			case 'ñ':
				buffer.append("&ntilde;");
				break;
			case 'Ñ':
				buffer.append("&Ntilde;");
				break;
			case 'á':
				buffer.append("&aacute;");
				break;
			case 'à':
				buffer.append("&agrave;");
				break;
			case 'ã':
				buffer.append("&atilde;");
				break;
			case 'â':
				buffer.append("&acirc;");
				break;
			case 'Á':
				buffer.append("&Aacute;");
				break;
			case 'À':
				buffer.append("&Agrave;");
				break;
			case 'Ã':
				buffer.append("&Atilde;");
				break;
			case 'Â':
				buffer.append("&Acirc;");
				break;
			case 'é':
				buffer.append("&eacute;");
				break;
			case 'è':
				buffer.append("&egrave;");
				break;
			case 'ê':
				buffer.append("&ecirc;");
				break;
			case 'É':
				buffer.append("&Eacute;");
				break;
			case 'È':
				buffer.append("&Egrave;");
				break;
			case 'Ê':
				buffer.append("&Ecirc;");
				break;
			case 'í':
				buffer.append("&iacute;");
				break;
			case 'ì':
				buffer.append("&igrave;");
				break;
			case 'î':
				buffer.append("&icirc;");
				break;
			case 'Í':
				buffer.append("&Iacute;");
				break;
			case 'Ì':
				buffer.append("&Igrave;");
				break;
			case 'Î':
				buffer.append("&Icirc;");
				break;
			case 'ó':
				buffer.append("&oacute;");
				break;
			case 'ò':
				buffer.append("&ograve;");
				break;
			case 'õ':
				buffer.append("&otilde;");
				break;
			case 'ô':
				buffer.append("&ocirc;");
				break;
			case 'Ó':
				buffer.append("&Oacute;");
				break;
			case 'Ò':
				buffer.append("&Ograve;");
				break;
			case 'Õ':
				buffer.append("&Otilde;");
				break;
			case 'Ô':
				buffer.append("&Ocirc;");
				break;
			case 'ú':
				buffer.append("&uacute;");
				break;
			case 'ù':
				buffer.append("&ugrave;");
				break;
			case 'û':
				buffer.append("&ucirc;");
				break;
			case 'Ú':
				buffer.append("&Uacute;");
				break;
			case 'Ù':
				buffer.append("&Ugrave;");
				break;
			case 'Û':
				buffer.append("&Ucirc;");
				break;
			default:
				buffer.append(c);
			}
		}

		return buffer.toString();
	}
}
