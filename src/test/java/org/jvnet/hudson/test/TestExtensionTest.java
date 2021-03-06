/*
 * The MIT License
 *
 * Copyright (c) 2016 IKEDA Yasuyuki
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package org.jvnet.hudson.test;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Lists;

import hudson.model.listeners.ItemListener;

/**
 * Tests for {@link TestExtension}
 */
public class TestExtensionTest {
    @Rule
    public JenkinsRule j = new JenkinsRule();

    @TestExtension
    public static class AllTests extends ItemListener {
    }

    @TestExtension("test1")
    public static class SingleTests extends ItemListener {
    }

    @TestExtension({"test1", "test2"})
    public static class MultipleTests extends ItemListener {
    }

    private List<Class<? extends ItemListener>> getExtensionClasses() {
        return Lists.transform(
            j.jenkins.getExtensionList(ItemListener.class),
            new Function<ItemListener, Class<? extends ItemListener>>() {
                @Override
                public Class<? extends ItemListener> apply(ItemListener arg0) {
                    return arg0.getClass();
                }
            }
        );
    }

    @Test
    public void test1() throws Exception {
        assertThat(
            getExtensionClasses(),
            Matchers.<Class<? extends ItemListener>>hasItems(AllTests.class, SingleTests.class, MultipleTests.class)
        );
    }

    @Test
    public void test2() throws Exception {
        assertThat(
            getExtensionClasses(),
            Matchers.<Class<? extends ItemListener>>hasItems(AllTests.class, MultipleTests.class)
        );
        assertThat(
            getExtensionClasses(),
            Matchers.not(Matchers.<Class<? extends ItemListener>>hasItem(SingleTests.class))
        );
    }

    @Test
    public void test3() throws Exception {
        assertThat(
            getExtensionClasses(),
            Matchers.<Class<? extends ItemListener>>hasItems(AllTests.class)
        );
        assertThat(
            getExtensionClasses(),
            Matchers.not(Matchers.<Class<? extends ItemListener>>hasItem(SingleTests.class))
        );
        assertThat(
            getExtensionClasses(),
            Matchers.not(Matchers.<Class<? extends ItemListener>>hasItem(MultipleTests.class))
        );
    }
}
