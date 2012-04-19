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

import java.util.List;

import javax.servlet.http.HttpSession;

import net.java.mega.action.model.Action;
import net.java.sjtools.logging.plus.RLog;
import net.java.sjtools.util.TextUtil;

public class OptionContextUtil {
	public static void store(HttpSession session, String actionPath, Action action) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("store(..., " + actionPath + ", " + action.getClass().getName() + ")");
		}

		// Identico ao UPDATE - INICIO
		OptionContextEntry root = getRootOptionContextEntry(session);
		List pathParts = getPathParts(actionPath);

		if (pathParts == null || pathParts.size() == 1) {
			if (root != null) {
				root.clear();
				setRootOptionContextEntry(session, null);
			}

			return;
		}

		if (root == null) {
			root = new OptionContextEntry((String) pathParts.get(0));
		}

		OptionContextEntry last = null;
		OptionContextEntry temp = root;

		for (int i = 0; i < pathParts.size() - 1; i++) {
			if (temp == null || !temp.getKey().equals(pathParts.get(i))) {
				if (temp != null) {
					temp.clear();
				}

				temp = new OptionContextEntry((String) pathParts.get(i));

				if (last != null) {
					last.setNext(temp);
				}

				last = temp;
				temp = null;
			} else {
				last = temp;
				temp = last.getNext();
			}
		}

		last.setNext(null);
		// Identico ao UPDATE - FIM
		
		String actionName = (String) pathParts.get(pathParts.size() - 1);
		last.put(actionName, action);

		setRootOptionContextEntry(session, last);

		if (RLog.isTraceEnabled()) {
			RLog.trace(actionName + " added to " + last.getKey());
		}
	}

	public static Action get(HttpSession session, String actionPath) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("get(..., " + actionPath + ")");
		}

		OptionContextEntry root = getRootOptionContextEntry(session);
		List pathParts = getPathParts(actionPath);

		if (root == null || pathParts == null || pathParts.size() == 1) {
			if (RLog.isTraceEnabled()) {
				if (root == null) {
					RLog.trace("root == null");
				}
				
				if (pathParts == null) {
					RLog.trace("pathParts == null");
				} else if(pathParts.size() == 1) {
					RLog.trace("pathParts.size() == 1");
				}
			}
			
			return null;
		}

		OptionContextEntry temp = root;
		OptionContextEntry old = null;

		for (int i = 0; i < pathParts.size() - 1; i++) {
			if (temp != null && temp.getKey().equals(pathParts.get(i))) {
				old = temp;
				temp = old.getNext();
			} else {
				if (RLog.isTraceEnabled()) {
					if (temp == null) {
						RLog.trace("temp == null");
					} else if (!temp.getKey().equals(pathParts.get(i))) {
						RLog.trace(temp.getKey() + " != " + pathParts.get(i));
					}
				}
				
				return null;
			}
		}

		String actionName = (String) pathParts.get(pathParts.size() - 1);

		Action ret = old.get(actionName);

		if (RLog.isTraceEnabled()) {
			RLog.trace(actionName + "=" + (ret == null? "Not Found!": ret.getClass().getName()));
		}

		return ret;
	}

	private static List getPathParts(String actionPath) {
		String temp = actionPath;

		if (temp != null && temp.startsWith("/")) {
			if (temp.length() > 1) {
				temp = temp.substring(1);
			} else {
				temp = null;
			}
		}

		if (temp == null) {
			return null;
		}

		return TextUtil.split(temp, "/");
	}

	private static OptionContextEntry getRootOptionContextEntry(HttpSession session) {
		return (OptionContextEntry) session.getAttribute(Constants.OPTION_CONTEXT_SESSION_OBJECT);
	}

	private static void setRootOptionContextEntry(HttpSession session, OptionContextEntry root) {
		if (root != null) {
			session.setAttribute(Constants.OPTION_CONTEXT_SESSION_OBJECT, root);
		} else {
			session.removeAttribute(Constants.OPTION_CONTEXT_SESSION_OBJECT);
		}
	}

	public static void update(HttpSession session, String actionPath) {
		if (RLog.isTraceEnabled()) {
			RLog.trace("update(..., " + actionPath + ")");
		}
		
		// Identico ao STORE - INICIO
		OptionContextEntry root = getRootOptionContextEntry(session);
		List pathParts = getPathParts(actionPath);

		if (pathParts == null || pathParts.size() == 1) {
			if (root != null) {
				root.clear();
				setRootOptionContextEntry(session, null);
			}

			return;
		}

		if (root == null) {
			root = new OptionContextEntry((String) pathParts.get(0));
		}

		OptionContextEntry last = null;
		OptionContextEntry temp = root;

		for (int i = 0; i < pathParts.size() - 1; i++) {
			if (temp == null || !temp.getKey().equals(pathParts.get(i))) {
				if (temp != null) {
					temp.clear();
				}

				temp = new OptionContextEntry((String) pathParts.get(i));

				if (last != null) {
					last.setNext(temp);
				}

				last = temp;
				temp = null;
			} else {
				last = temp;
				temp = last.getNext();
			}
		}

		last.setNext(null);
		// Identico ao STORE - FIM

		setRootOptionContextEntry(session, root);
	}
}
