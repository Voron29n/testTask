/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.aem.epam.training.core.models;

import org.apache.jackrabbit.oak.commons.PathUtils;
import org.junit.Test;

/**
 * Simple JUnit test verifying the HelloWorldModel
 */
public class TestHelloWorldModel {

    //@Inject
    private HelloWorldModel hello;

    private String slingId;

//    @Before
//    public void setup() throws Exception {
//        SlingSettingsService settings = mock(SlingSettingsService.class);
//        slingId = UUID.randomUUID().toString();
//        when(settings.getSlingId()).thenReturn(slingId);
//
//        hello = new HelloWorldModel();
//        PrivateAccessor.setField(hello, "settings", settings);
//        hello.init();
//    }

    @Test
    public void testGetMessage() throws Exception {
        String pagePath = "/content/EpamTestTasks/en/jcr:content/par/gridparsys/par_1/hello_world";
        String pathToParNode = PathUtils.getAncestorPath(pagePath, 1);
        if (PathUtils.getName(pathToParNode).contains("par")) {
            System.out.println(PathUtils.getName(pathToParNode));
        }
        System.out.println(pathToParNode);
    }

    @Test
    public void testGridParNodeIterator() throws Exception {
        String pagePath = "/content/EpamTestTasks/en/jcr:content/par/gridparsys/par_1/helloWorld";
        String pathPar = pagePath.substring(pagePath.indexOf("par_"));
        if (pagePath.contains("/")) {
            pathPar = pathPar.substring(0, pathPar.indexOf("/"));
        }

        int counter = Integer.parseInt(pathPar.substring(pathPar.indexOf("_") + 1));


        System.out.println(counter);

//        String pathToParNode = PathUtils.getAncestorPath(pagePath, 1);
//        if (PathUtils.getName(pathToParNode).contains("par")){
//            System.out.println(PathUtils.getName(pathToParNode));
//        }
//        System.out.println(pathToParNode);
    }
}
