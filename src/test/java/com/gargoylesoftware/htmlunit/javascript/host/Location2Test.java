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

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.gargoylesoftware.htmlunit.BrowserRunner;
import com.gargoylesoftware.htmlunit.BrowserRunner.Alerts;
import com.gargoylesoftware.htmlunit.WebDriverTestCase;

/**
 * Tests for {@link Location}.
 *
 * @version $Revision: 6470 $
 * @author <a href="mailto:mbowler@GargoyleSoftware.com">Mike Bowler</a>
 * @author Michael Ottati
 * @author Marc Guillemot
 * @author Daniel Gredler
 * @author Ahmed Ashour
 * @author Ronald Brill
 */
@RunWith(BrowserRunner.class)
public class Location2Test extends WebDriverTestCase {

    /**
     * Regression test for bug 742902.
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testDocumentLocationGet() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("ok")
    public void testDocumentLocationSet() throws Exception {
        final String html1 =
              "<html>\n"
            + "<head>\n"
            + "  <title>test1</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      document.location = 'foo.html';\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";
        final String html2 =
              "<html>\n"
            + "<head>\n"
            + "  <title>test2</title>\n"
            + "  <script>\n"
            + "    function test() {\n"
            + "      alert('ok');\n"
            + "    }\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body onload='test()'></body>\n"
            + "</html>";

        getMockWebConnection().setResponse(new URL(getDefaultUrl(), "foo.html"), html2);
        loadPageWithAlerts2(html1);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testDocumentLocationHref() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    alert(top.document.location.href);\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts(FF = { "", "about:blank", "", "", "about:", "" },
            IE = { "", "about:blank", "/blank", "", "about:", "" })
    public void about_blank_attributes() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    var location = frames[0].document.location;\n"
            + "    alert(location.hash);\n"
            + "    alert(location.href);\n"
            + "    alert(location.pathname);\n"
            + "    alert(location.port);\n"
            + "    alert(location.protocol);\n"
            + "    alert(location.search);\n"
            + "}\n</script></head>\n"
            + "<body onload='doTest()'>\n"
            + "<iframe src='about:blank'></iframe></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "#a b", "§§URL§§#a%20b", "#a b", "§§URL§§#a%20b", "#abc;,/?:@&=+$-_.!~*()ABC123foo",
            "#% ^[]|\"<>{}\\" },
            IE8 = { "#a b", "§§URL§§#a%20b", "#a b", "§§URL§§#a%20b", "#abc;,/?:@&=+$-_.!~*()ABC123foo",
            "#% ^[]|\"<>{}\\" },
            IE = { "#a b", "§§URL§§#a b", "#a%20b", "§§URL§§#a%20b", "#abc;,/?:@&=+$-_.!~*()ABC123foo",
            "#%25%20%5E%5B%5D%7C%22%3C%3E%7B%7D%5C" })
    public void hashEncoding() throws Exception {
        final String html = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    window.location.hash = 'a b';\n"
            + "    alert(window.location.hash);\n"
            + "    alert(window.location.href);\n"
            + "    window.location.hash = 'a%20b';\n"
            + "    alert(window.location.hash);\n"
            + "    alert(window.location.href);\n"
            + "    window.location.hash = 'abc;,/?:@&=+$-_.!~*()ABC123foo';\n"
            + "    alert(window.location.hash);\n"
            + "    window.location.hash = '%25%20%5E%5B%5D%7C%22%3C%3E%7B%7D%5C';\n"
            + "    alert(window.location.hash);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "#myDataTable=foo=ojkoj", "§§URL§§#myDataTable=foo%3Dojkoj" },
            IE = { "#myDataTable=foo%3Dojkoj", "§§URL§§#myDataTable=foo%3Dojkoj" },
            IE8 = { "#myDataTable=foo=ojkoj", "§§URL§§#myDataTable=foo%3Dojkoj" })
    public void hashEncoding2() throws Exception {
        final String html = "<html><body><script>\n"
            + "window.location.hash = 'myDataTable=foo%3Dojkoj';\n"
            + "alert(window.location.hash);\n"
            + "alert(window.location.href);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = { "#üöä", "§§URL§§#%C3%BC%C3%B6%C3%A4" },
            IE = { "#üöä", "§§URL§§#üöä" },
            IE8 = { "#üöä", "§§URL§§#%C3%BC%C3%B6%C3%A4" })
    public void hashEncoding3() throws Exception {
        final String html = "<html><body><script>\n"
            + "window.location.hash = 'üöä';\n"
            + "alert(window.location.hash);\n"
            + "alert(window.location.href);\n"
            + "</script></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if an error occurs
     */
    @Test
    @Alerts(FF = "#<a>foobar</a>",
            IE8 = "#<a>foobar</a>",
            IE = "#%3Ca%3Efoobar%3C/a%3E")
    public void hash() throws Exception {
        final String html = "<html><body onload='test()'>\n"
            + "<script>\n"
            + "function test() {\n"
            + "  alert(document.location.hash);\n"
            + "}\n"
            + "</script>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse(html);
        loadPageWithAlerts2(html, new URL(getDefaultUrl().toExternalForm() + "?#<a>foobar</a>"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "#hello", "#hi" })
    public void testSetHash2() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    window.location.hash = 'hello';\n"
            + "    alert(window.location.hash);\n"
            + "    window.location.hash = '#hi';\n"
            + "    alert(window.location.hash);\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a hash behaves like setting <tt>location.hash</tt>
     * (ie doesn't result in a server hit). See bug 2089341.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHrefWithOnlyHash() throws Exception {
        final String html = "<html><body><script>document.location.href = '#x';</script></body></html>";
        loadPageWithAlerts2(html);
    }

    /**
     * Verifies that setting <tt>location.href</tt> to a new URL with a hash, where the only difference between the
     * new URL and the old URL is the hash, behaves like setting <tt>location.hash</tt> (ie doesn't result in a
     * server hit). See bug 2101735.
     * @throws Exception if an error occurs
     */
    @Test
    public void testSetHrefWithOnlyHash2() throws Exception {
        final String html = "<script>document.location.href = '" + getDefaultUrl() + "#x';</script>";
        loadPageWithAlerts2(html);
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplace() throws Exception {
        final String html
            = "<html><head><title>First</title><script>\n"
            + "function doTest() {\n"
            + "    location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        final WebDriver webdriver = loadPageWithAlerts2(html);

        assertEquals("Second", webdriver.getTitle());
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceLastInHistory() throws Exception {
        final String startContent = "<html><head><title>First Page</title></head><body></body></html>";
        getMockWebConnection().setResponse(URL_FIRST, startContent);

        final String secondContent
            = "<html><head><title>Second Page</title><script>\n"
            + "function doTest() {\n"
            + "    location.replace('" + URL_THIRD + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String thirdContent = "<html><head><title>Third Page</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);
        getMockWebConnection().setResponse(URL_THIRD, thirdContent);

        final WebDriver webdriver = loadPageWithAlerts2(URL_FIRST);
        webdriver.get(URL_SECOND.toExternalForm());

        assertEquals("Third Page", webdriver.getTitle());

        // navigate back
        webdriver.navigate().back();
        assertEquals("First Page", webdriver.getTitle());
    }

    /**
     * Test for <tt>replace</tt>.
     * @throws Exception if the test fails
     */
    @Test
    public void testReplaceFirstInHistory() throws Exception {
        final String firstContent
            = "<html><head><title>First Page</title><script>\n"
            + "function doTest() {\n"
            + "    location.replace('" + URL_SECOND + "');\n"
            + "}\n"
            + "</script></head><body onload='doTest()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second Page</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstContent);
        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver webdriver = loadPageWithAlerts2(URL_FIRST);

        assertEquals("Second Page", webdriver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testAssign() throws Exception {
        final String firstContent
            = "<html><head><title>First</title><script>\n"
            + "  function test() {\n"
            + "    location.assign('" + URL_SECOND + "');\n"
            + "  }\n"
            + "</script></head><body onload='test()'>\n"
            + "</body></html>";

        final String secondContent = "<html><head><title>Second</title></head><body></body></html>";

        getMockWebConnection().setResponse(URL_SECOND, secondContent);

        final WebDriver driver = loadPage2(firstContent, URL_FIRST);
        assertEquals("Second", driver.getTitle());
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void testChangeLocationToNonHtml() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'foo.txt';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        getMockWebConnection().setResponse(new URL(URL_FIRST + "foo.txt"), "bla bla", "text/plain");
        final WebDriver driver = loadPage2(html, URL_FIRST);
        assertTrue(driver.getPageSource().contains("bla bla"));
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("foo")
    public void jsLocation() throws Exception {
        final String html =
              "<html><head>\n"
            + "  <script>\n"
            + "      document.location.href = 'javascript:alert(\"foo\")';\n"
            + "  </script>\n"
            + "</head>\n"
            + "<body></body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts("§§URL§§")
    public void testToString() throws Exception {
        final String html =
            "<html><head>\n"
            + "<script>\n"
            + " alert(window.location.toString());\n"
            + "</script>\n"
            + "<body>\n"
            + "</body></html>";

        loadPageWithAlerts2(html);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    @Alerts({ "1", "2", "3" })
    public void href_postponed() throws Exception {
        final String firstHtml =
            "<html><head><script>\n"
            + "function test() {\n"
            + "  alert('1');\n"
            + "  self.frames['frame1'].document.location.href='" + URL_SECOND + "';\n"
            + "  alert('2');\n"
            + "}\n"
            + "</script></head>\n"
            + "<body onload='test()'>\n"
            + "  <iframe name='frame1' id='frame1'/>\n"
            + "</body></html>";
        final String secondHtml = "<html><body><script>alert('3');</script></body></html>";

        getMockWebConnection().setResponse(URL_FIRST, firstHtml);
        getMockWebConnection().setResponse(URL_SECOND, secondHtml);

        loadPageWithAlerts2(URL_FIRST);
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void onlick_set_location() throws Exception {
        final String html =
            "<html><head></head>\n"
            + "<body>\n"
            + "<a href='foo2.html' onclick='document.location = \"foo3.html\"'>click me</a>\n"
            + "</body></html>";

        getMockWebConnection().setDefaultResponse("");
        final WebDriver driver = loadPageWithAlerts2(html);
        driver.findElement(By.tagName("a")).click();

        final String[] expectedRequests = {"", "foo3.html", "foo2.html"};
        assertEquals(expectedRequests, getMockWebConnection().getRequestedUrls(getDefaultUrl()));

        assertEquals(new URL(getDefaultUrl(), "foo2.html").toString(), driver.getCurrentUrl());
    }
}
