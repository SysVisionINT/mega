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
package net.java.mega.common.load;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

public class ResponseWrapper extends HttpServletResponseWrapper {
	private ServletOutputStreamWrapper output = new ServletOutputStreamWrapper();
	private PrintWriter pw = new PrintWriter(output);
	
	private Integer status = null;
	private Integer error = null;
	private String errorMsg = null;
	private String characterEncoding = null;
	private String contentType = null;
	private List cookieList = new ArrayList();
	private Map headerMap = new HashMap();
	private String redirect = null;

	public ResponseWrapper(HttpServletResponse response) {
		super(response);
	}

	public void sendError(int error, String msg) throws IOException {
		sendError(error);
		errorMsg = msg;
	}

	public void sendError(int error) throws IOException {
		this.error = new Integer(error);
	}

	public ServletOutputStream getOutputStream() throws java.io.IOException {
		return output;
	}

	public PrintWriter getWriter() throws java.io.IOException {
		return pw;
	}
	
	public void flushBuffer() throws IOException {
		output.flush();
	}
	
	public String getResponseContentAsString() {
		return output.toString();
	}

	public void addCookie(Cookie cookie) {
		cookieList.add(cookie);
	}

	public void addDateHeader(String name, long value) {
		headerMap.put(name, new Long(value));
	}

	public void addHeader(String name, String value) {
		headerMap.put(name, value);
	}

	public void addIntHeader(String name, int value) {
		headerMap.put(name, new Integer(value));
	}

	public void setDateHeader(String name, long value) {
		headerMap.put(name, new Long(value));
	}

	public void setHeader(String name, String value) {
		headerMap.put(name, value);
	}

	public void setIntHeader(String name, int value) {
		headerMap.put(name, new Integer(value));
	}
	
	public boolean containsHeader(String name) {
		return headerMap.containsKey(name);
	}

	public void setStatus(int value) {
		status = new Integer(value);
	}

	public void setCharacterEncoding(String value) {
		characterEncoding = value;
	}
	
	public void setContentType(String value) {
		contentType = value;
	}
	
	public void sendRedirect(String location) throws java.io.IOException {
		redirect = location;
	}
	
	public String getRedirect() {
		return redirect;
	}

	public void update(HttpServletResponse response) throws IOException {	
		if (error != null) {
			if (errorMsg != null) {
				response.sendError(error.intValue(), errorMsg);
			} else {
				response.sendError(error.intValue());
			}
			
			return;
		}
		
		for (Iterator i = cookieList.iterator(); i.hasNext();) {
			response.addCookie((Cookie) i.next());
		}
		
		String name = null;
		Object value = null;
		
		for (Iterator i = headerMap.keySet().iterator(); i.hasNext();) {
			name = (String) i.next();
			value = headerMap.get(name);
			
			if (value instanceof String) {
				if (response.containsHeader(name)) {
					response.setHeader(name, (String) value);
				} else {
					response.addHeader(name, (String) value);
				}
			} else if (value instanceof Integer) {
				if (response.containsHeader(name)) {
					response.setIntHeader(name, ((Integer) value).intValue());
				} else {
					response.addIntHeader(name, ((Integer) value).intValue());
				}
			} else if (value instanceof Long) {
				if (response.containsHeader(name)) {
					response.setDateHeader(name, ((Long) value).longValue());
				} else {
					response.addDateHeader(name, ((Long) value).longValue());
				}
			}
		}
		
		if (redirect != null) {
			response.sendRedirect(redirect);
			return;
		}
		
		if (status != null) {
			response.setStatus(status.intValue());
		}
		
		if (characterEncoding != null) {
			response.setCharacterEncoding(characterEncoding);
		}
		
		if (contentType != null) {
			response.setContentType(contentType);
		}
		
		response.getOutputStream().write(output.getBuffer());
		
		response.flushBuffer();
	}

	public String getCharacterEncoding() {
		return characterEncoding;
	}

	public String getContentType() {
		return contentType;
	}

	public List getCookieList() {
		return cookieList;
	}

	public Map getHeaderMap() {
		return headerMap;
	}

	public Integer getStatus() {
		return status;
	}
}
