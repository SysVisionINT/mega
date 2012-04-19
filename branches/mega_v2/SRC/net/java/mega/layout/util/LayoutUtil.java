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
package net.java.mega.layout.util;

import net.java.mega.layout.model.Block;
import net.java.mega.layout.model.Layout;
import net.java.mega.layout.model.LayoutPath;
import net.java.mega.layout.model.Page;
import net.java.mega.layout.model.PageBase;
import net.java.mega.layout.model.PageExtend;

public class LayoutUtil {

	public static String getLayoutLocation(Layout layout, String pageName) {
		String ret = null;

		Page page = layout.getPage(pageName);

		if (page != null) {
			PageBase pageBase = null;

			while (ret == null && page != null) {
				pageBase = page.getPageBase();

				if (pageBase instanceof LayoutPath) {
					ret = ((LayoutPath) pageBase).getPath();
				} else {
					page = layout.getPage(((PageExtend) pageBase).getPageName());
				}
			}
		}

		return ret;
	}

	public static Block findBlock(Layout layout, Page page, String blockName) {
		Block block = null;
		PageBase pageBase = null;
		Page currentPage = page;

		while (block == null && currentPage != null) {
			block = currentPage.getBlock(blockName);

			if (block == null) {
				pageBase = currentPage.getPageBase();

				if (pageBase instanceof PageExtend) {
					currentPage = layout.getPage(((PageExtend) pageBase).getPageName());
				} else {
					currentPage = null;
				}
			}
		}

		return block;
	}
}
