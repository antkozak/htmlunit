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
package com.gargoylesoftware.htmlunit;

import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.gargoylesoftware.htmlunit.BrowserRunner.Browser;
import com.gargoylesoftware.htmlunit.BrowserRunner.Browsers;

/**
 * Tests for {@link BrowserVersionFeatures}.
 *
 * @version $Revision: 6204 $
 * @author Ahmed Ashour
 */
@RunWith(BrowserRunner.class)
public class BrowserVersionFeaturesTest extends WebTestCase {

    /**
     * Test of alphabetical order.
     */
    @Test
    @Browsers(Browser.NONE)
    public void lexicographicOrder() {
        String lastFeatureName = null;
        for (final BrowserVersionFeatures feature : BrowserVersionFeatures.values()) {
            final String featureName = feature.name();
            if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                fail("BrowserVersionFeatures.java: \""
                    + featureName + "\" should be before \"" + lastFeatureName + '"');
            }
            lastFeatureName = featureName;
        }
    }

    /**
     * @throws Exception if the test fails
     */
    @Test
    public void lexicographicOrder_properties() throws Exception {
        final String path = "com/gargoylesoftware/htmlunit/javascript/configuration/"
            + getBrowserVersion().getNickname() + ".properties";
        final InputStream is = getClass().getClassLoader().getResourceAsStream(path);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        try {
            String lastFeatureName = null;
            String line;
            while ((line = reader.readLine()) != null) {
                final String featureName = line.trim();
                if (lastFeatureName != null && featureName.compareTo(lastFeatureName) < 1) {
                    fail(path + ": \"" + featureName + "\" should be before \"" + lastFeatureName + '"');
                }
                lastFeatureName = featureName;
            }
        }
        finally {
            reader.close();
        }
    }
}
