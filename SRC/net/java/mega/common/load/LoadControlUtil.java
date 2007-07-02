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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.java.sjtools.util.TextUtil;

public class LoadControlUtil {
	public static final String LOAD_CONTROL_SAME_REQUEST = "_LD_CNTRL_SM_RQST_";
	public static final String LOAD_CONTROL = LoadControl.class.getName();

	public static LoadControl getLoadControl(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		LoadControl controler = (LoadControl) session.getAttribute(LOAD_CONTROL);

		if (controler == null) {
			controler = createLoadControl(session);
		}

		return controler;
	}

	private static synchronized LoadControl createLoadControl(HttpSession session) {
		LoadControl controler = (LoadControl) session.getAttribute(LOAD_CONTROL);

		if (controler == null) {
			controler = new LoadControl();
			session.setAttribute(LOAD_CONTROL, controler);
		}

		return controler;
	}
	
	public static void markAsSameRequest(HttpServletRequest request) {
		request.setAttribute(LOAD_CONTROL_SAME_REQUEST, LOAD_CONTROL_SAME_REQUEST);
	}

	public static boolean isTheSameRequest(HttpServletRequest request) {
		String rid = (String) request.getAttribute(LOAD_CONTROL_SAME_REQUEST);
		
		return !TextUtil.isEmptyString(rid);
	}	
}
