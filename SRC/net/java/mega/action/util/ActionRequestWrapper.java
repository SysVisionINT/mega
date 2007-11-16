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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletInputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.java.mega.action.model.FormFileImpl;
import net.java.sjtools.util.TextUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class ActionRequestWrapper implements HttpServletRequest {
	private HttpServletRequest request = null;
	private Hashtable parameters = new Hashtable();
	
	public ActionRequestWrapper (HttpServletRequest request) throws Exception {
		this.request = request;
		
		if (ServletFileUpload.isMultipartContent(request)) {
			FileItemFactory factory = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(factory);
			List items = upload.parseRequest(request);
			
			String encoding = request.getCharacterEncoding();
			
			if (encoding == null) {
				encoding = "ISO-8859-1";
			}
			
			Map textFields = new HashMap();
			String fieldName = null;

			for (Iterator i = items.iterator(); i.hasNext();) {
				FileItem item = (FileItem) i.next();

				fieldName = item.getFieldName();
				
				if (!item.isFormField()) {
					if (TextUtil.isEmptyString(fieldName)) {
						continue;
					}

					FormFileImpl file = new FormFileImpl();
					file.setFileName(item.getName());
					file.setContentType(item.getContentType());
					file.setFileSize(item.getSize());
					file.setInputStream(item.getInputStream());

					parameters.put(fieldName, file);
				} else {
					String value = null;
					
					try {
		                value = item.getString(encoding);
		            } catch (UnsupportedEncodingException e) {
		                value = item.getString();
		            }
		            
					List valueList = (List) textFields.get(fieldName);
					
					if (valueList == null) {
						valueList = new ArrayList();
					}
					
					valueList.add(value);
				    
					textFields.put(fieldName, valueList);
				}
			}
			
			for (Iterator i = textFields.keySet().iterator(); i.hasNext();) {
				String name = (String) i.next();
				List values = (List) textFields.get(name);
				
				parameters.put(name, values.toArray(new String[values.size()]));
			}
		} else {
			parameters.putAll(request.getParameterMap());
		}
	}
	
	public boolean isRequestedSessionIdFromCookie() {
		return request.isRequestedSessionIdFromCookie();
	}

	public boolean isRequestedSessionIdFromURL() {
		return request.isRequestedSessionIdFromURL();
	}

	public boolean isRequestedSessionIdValid() {
		return request.isRequestedSessionIdValid();
	}

	public String getAuthType() {
		return request.getAuthType();
	}

	public String getContextPath() {
		return request.getContextPath();
	}

	public String getMethod() {
		return request.getMethod();
	}

	public String getPathInfo() {
		return request.getPathInfo();
	}

	public String getPathTranslated() {
		return request.getPathTranslated();
	}

	public String getQueryString() {
		return request.getQueryString();
	}

	public String getRemoteUser() {
		return request.getRemoteUser();
	}

	public String getRequestURI() {
		return request.getRequestURI();
	}

	public String getRequestedSessionId() {
		return request.getRequestedSessionId();
	}

	public String getServletPath() {
		return request.getServletPath();
	}

	public int getIntHeader(String arg0) {
		return request.getIntHeader(arg0);
	}

	public long getDateHeader(String arg0) {
		return request.getDateHeader(arg0);
	}

	public boolean isUserInRole(String arg0) {
		return request.isUserInRole(arg0);
	}

	public StringBuffer getRequestURL() {
		return request.getRequestURL();
	}

	public Principal getUserPrincipal() {
		return request.getUserPrincipal();
	}

	public Enumeration getHeaderNames() {
		return request.getHeaderNames();
	}

	public Cookie[] getCookies() {
		return request.getCookies();
	}

	public HttpSession getSession() {
		return request.getSession();
	}

	public HttpSession getSession(boolean arg0) {
		return request.getSession(arg0);
	}

	public String getHeader(String arg0) {
		return request.getHeader(arg0);
	}

	public Enumeration getHeaders(String arg0) {
		return request.getHeaders(arg0);
	}

	public int getContentLength() {
		return request.getContentLength();
	}

	public int getLocalPort() {
		return request.getLocalPort();
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}

	public int getServerPort() {
		return request.getServerPort();
	}

	public boolean isSecure() {
		return request.isSecure();
	}

	public BufferedReader getReader() throws IOException {
		return request.getReader();
	}

	public String getCharacterEncoding() {
		return request.getCharacterEncoding();
	}

	public String getContentType() {
		return request.getContentType();
	}

	public String getLocalAddr() {
		return request.getLocalAddr();
	}

	public String getLocalName() {
		return request.getLocalName();
	}

	public String getProtocol() {
		return request.getProtocol();
	}

	public String getRemoteAddr() {
		return request.getRemoteAddr();
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public String getScheme() {
		return request.getScheme();
	}

	public String getServerName() {
		return request.getServerName();
	}

	public void removeAttribute(String arg0) {
		request.removeAttribute(arg0);
	}

	public void setCharacterEncoding(String arg0) throws UnsupportedEncodingException {
		request.setCharacterEncoding(arg0);
	}

	public Enumeration getAttributeNames() {
		return request.getAttributeNames();
	}

	public Enumeration getLocales() {
		return request.getLocales();
	}

	public Enumeration getParameterNames() {
		return parameters.elements();
	}

	public Locale getLocale() {
		return request.getLocale();
	}

	public Map getParameterMap() {
		return parameters;
	}

	public ServletInputStream getInputStream() throws IOException {
		return request.getInputStream();
	}

	public Object getAttribute(String arg0) {
		return request.getAttribute(arg0);
	}

	public void setAttribute(String arg0, Object arg1) {
		request.setAttribute(arg0, arg1);
	}

	public String getParameter(String name) {
		String[] values =  (String[]) parameters.get(name);
		
		if (values != null) {
			return values[0];
		}
		
		return null;
	}

	public String[] getParameterValues(String name) {
		return (String[]) parameters.get(name);
	}

	public RequestDispatcher getRequestDispatcher(String arg0) {
		return request.getRequestDispatcher(arg0);
	}

	/*
	 * @deprecated
	 */
	public boolean isRequestedSessionIdFromUrl() {
		return false;
	}

	/*
	 * @deprecated
	 */
	public String getRealPath(String arg0) {
		return null;
	}
}
