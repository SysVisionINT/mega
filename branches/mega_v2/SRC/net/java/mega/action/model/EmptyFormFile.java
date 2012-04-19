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

package net.java.mega.action.model;

import java.io.InputStream;
import java.io.Serializable;

import net.java.mega.action.api.FormFile;

public class EmptyFormFile implements FormFile, Serializable{
	private static final long serialVersionUID = 6837230366274118527L;

	public String getContentType() {
		return null;
	}

	public String getFileName() {
		return null;
	}

	public long getFileSize() {
		return 0;
	}

	public InputStream getInputStream() {
		return null;
	}
}
