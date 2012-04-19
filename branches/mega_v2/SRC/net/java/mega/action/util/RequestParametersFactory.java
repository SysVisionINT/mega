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

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import net.java.mega.action.api.RequestParameters;
import net.java.mega.action.model.EmptyFormFile;
import net.java.mega.action.model.FormFileImpl;
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.TextUtil;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class RequestParametersFactory {
	public static RequestParameters getRequestParameters(HttpServletRequest request) throws Exception {
		if (RLog.isTraceEnabled()) {
			RLog.trace("getRequestParameters()");
		}

		Hashtable parameters = new Hashtable();

		if (ServletFileUpload.isMultipartContent(request)) {
			if (RLog.isTraceEnabled()) {
				RLog.trace("isMultipartContent...");
			}

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

				if (RLog.isTraceEnabled()) {
					RLog.trace("fieldName = " + item.getFieldName());
				}

				if (!item.isFormField()) {
					if (RLog.isTraceEnabled()) {
						RLog.trace(item.getFieldName() + " NOT isFormField...");
					}

					if (TextUtil.isEmptyString(item.getName())) {
						if (RLog.isTraceEnabled()) {
							RLog.trace(item.getFieldName() + " NO fileName");
						}
						
						parameters.put(fieldName, new EmptyFormFile());
					} else {
						if (RLog.isTraceEnabled()) {
							RLog.trace(item.getFieldName() + " = File(" + item.getName() + ")");
						}
						
						FormFileImpl file = new FormFileImpl();
						file.setFileName(item.getName());
						file.setContentType(item.getContentType());
						file.setFileSize(item.getSize());
						file.setInputStream(item.getInputStream());

						parameters.put(fieldName, file);
					}
				} else {
					if (RLog.isTraceEnabled()) {
						RLog.trace(item.getFieldName() + " isFormField...");
					}

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
			if (RLog.isTraceEnabled()) {
				RLog.trace("NOT isMultipartContent...");
			}

			parameters.putAll(request.getParameterMap());
		}

		return new RequestParameters(parameters);
	}
}
