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
package net.java.mega.layout.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Page implements Serializable {
	private static final long serialVersionUID = 1817285356150217857L;
	
	private String pageName = null;
	private PageBase pageBase = null;
	private Map blockMap = new HashMap();

	public String getPageName() {
		return pageName;
	}

	public void setPageName(String pageName) {
		this.pageName = pageName;
	}

	public PageBase getPageBase() {
		return pageBase;
	}

	public void setPageBase(PageBase pageBase) {
		this.pageBase = pageBase;
	}
	
	public void addBlock(Block bock) {
		blockMap.put(bock.getBlockName(), bock);
	}
	
	public Block getBlock(String bockName) {
		return (Block) blockMap.get(bockName);
	}
}
