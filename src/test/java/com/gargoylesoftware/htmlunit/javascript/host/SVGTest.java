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
package com.gargoylesoftware.htmlunit.javascript.host;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.NotYetImplemented;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Very simple test for SVG "support".
 *
 * @version $Revision: 6472 $
 * @author Marc Guillemot
 */
@RunWith(BrowserRunner.class)
public class SVGTest extends WebDriverTestCase {

    /**
     * Test for issue 3313921.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = "svgElem", IE = "exception")
    @NotYetImplemented(Browser.FF) // a simple hack doesn't work to make it working without breaking anything else
    public void getAttribute() throws Exception {
        final String html = "<html><body><script>\n"
            + "try {\n"
            + "  var svg = document.createElementNS('http://www.w3.org/2000/svg', 'svg');\n"
            + "  svg.setAttribute('id', 'svgElem');\n"
            + "  document.body.appendChild(svg);\n"
            + "  alert(document.getElementById('svgElem').getAttribute('id'));\n"
            + "} catch (e) { alert('exception'); }\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }
}
