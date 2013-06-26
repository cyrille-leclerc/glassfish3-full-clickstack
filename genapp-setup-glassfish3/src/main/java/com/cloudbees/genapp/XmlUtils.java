/*
 * Copyright 2010-2013, CloudBees Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cloudbees.genapp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * @author <a href="mailto:cleclerc@cloudbees.com">Cyrille Le Clerc</a>
 */
public class XmlUtils {
    private final static XPath xpath = XPathFactory.newInstance().newXPath();
    public static Element getUniqueElement(Document document, String xpathExpression) {
        return getUniqueElement((Node) document, xpathExpression);

    }

    public static Element getUniqueElement(Element element, String xpathExpression) {
        return getUniqueElement((Node) element, xpathExpression);
    }

    public static Element getUniqueElement(Node element, String xpathExpression) {
        try {
            NodeList nl = (NodeList) xpath.compile(xpathExpression).evaluate(element, XPathConstants.NODESET);
            if (nl.getLength() == 0 || nl.getLength() > 1) {
                throw new RuntimeException("More or less (" + nl.getLength() + ") than 1 element found");
            }
            return (Element) nl.item(0);
        } catch (Exception e) {
            throw new RuntimeException("Exception evaluating xpath '" + xpathExpression + "' on " + element, e);
        }
    }


}
