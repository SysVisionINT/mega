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
package net.java.mega.action.error;

public class ActionCreationException extends RuntimeException {
	private static final long serialVersionUID = 7230587565673672223L;

	public ActionCreationException (Class clazz) {
		super("Error while creating instance of " + clazz.getName());
	}
	
	public ActionCreationException (String path) {
		super("Error while searching action " + path + " configuratin");
	}
}
