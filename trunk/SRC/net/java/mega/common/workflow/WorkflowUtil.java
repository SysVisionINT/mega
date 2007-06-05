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
package net.java.mega.common.workflow;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class WorkflowUtil {
	public static final String WORKFLOW_CONTROL = WorkflowControl.class.getName();
	public static final String WORKFLOW_CONTROL_FIELD = "_WRK_FLW_CNTRL_";

	public static WorkflowControl getWorkflowControl(HttpServletRequest request) {
		HttpSession session = request.getSession(true);

		WorkflowControl controler = (WorkflowControl) session.getAttribute(WORKFLOW_CONTROL);

		if (controler == null) {
			controler = createWorkflowControl(session);
		}

		return controler;
	}

	public static String getCurrentToken(HttpServletRequest request) {
		WorkflowControl control = getWorkflowControl(request);

		return String.valueOf(control.getCurrentToken());
	}

	public static String generateToken(HttpServletRequest request) {
		WorkflowControl control = getWorkflowControl(request);

		control.setCurrentToken(System.currentTimeMillis());

		updateWorkflowControl(request, control);

		return String.valueOf(control.getCurrentToken());
	}
	
	public static String getUserToken(HttpServletRequest request) {
		return request.getParameter(WORKFLOW_CONTROL_FIELD);
	}

	private static void updateWorkflowControl(HttpServletRequest request, WorkflowControl control) {
		HttpSession session = request.getSession(true);
		session.setAttribute(WORKFLOW_CONTROL, control);
	}

	private static synchronized WorkflowControl createWorkflowControl(HttpSession session) {
		WorkflowControl controler = (WorkflowControl) session.getAttribute(WORKFLOW_CONTROL);

		if (controler == null) {
			controler = new WorkflowControl();
			session.setAttribute(WORKFLOW_CONTROL, controler);
		}

		return controler;
	}
}
