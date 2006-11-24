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
package net.java.mega.action.xml;

import java.io.InputStream;

import net.java.mega.action.model.ControllerConfig;
import net.java.sjtools.xml.SimpleParser;

public class ActionConfigReader {
	public static ControllerConfig getControllerConfig(InputStream inputStream) throws Exception {

		ActionConfigHandler handler = new ActionConfigHandler();

		SimpleParser parser = new SimpleParser(handler, true);
		
		parser.addDTD("-//MEGA//DTD mega-action 1.0//EN", handler.getClass().getResource("/mega-action.dtd").toString());

		return (ControllerConfig) parser.parse(inputStream);
	}
}
