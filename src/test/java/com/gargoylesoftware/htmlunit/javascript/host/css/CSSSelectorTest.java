/*
 * Copyright (c) 2002-2011 Gargoyle Software Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.gargoylesoftware.htmlunit.javascript.host.css;

import junit.framework.Assert;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.DomNodeList;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

/**
 * Tests for CSS selectors.
 *
 * @version $Revision: 6406 $
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class CSSSelectorTest extends WebDriverTestCase {

    /**
     * Test for bug 3300434; don't throw an error
     * if the css parsing fails.
     *
     * @throws Exception if an error occurs
     */
    @Test
    public void simple() throws Exception {
        final String html
            = "<html><body>"
            + "</body></html>";

        final HtmlPage page = loadPage(html);
        final String selector = "table:nth-child(1) td";
        final DomNodeList<DomNode> nodes = page.querySelectorAll(selector);
        Assert.assertEquals(0, nodes.size());
    }
}
