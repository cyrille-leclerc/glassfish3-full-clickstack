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
package com.cloudbees.genapp.glassfish3;

import com.cloudbees.genapp.metadata.EnvBuilder;
import com.cloudbees.genapp.metadata.Metadata;
import com.cloudbees.genapp.metadata.MetadataFinder;

import java.util.Arrays;

/**
 * This class contains the main method to get the Genapp metadata and configure Glassfish 3.
 */
public class Setup {
    /**
     * The main method takes optional arguments for the location of the
     * domain.xml file to modify, as well as the location of the metadata.json
     * file. Defaults are:<br/>
     * CONTEXT_XML_PATH = $app_dir/server/conf/domain.xml<br/>
     * METADATA_PATH = $genapp_dir/metadata.json<br/>
     *
     * @param args Two optional args: [ CONTEXT_XML_PATH [ METADATA_PATH ]]
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        System.out.print("insert glassfish resources " + Arrays.asList(args));
        MetadataFinder metadataFinder = new MetadataFinder();
        Metadata metadata = metadataFinder.getMetadata();
        // Build the environment with bash-safe names, and no deprecated values.
        new EnvBuilder(true, false, metadata).writeControlFile("env_safe");
        // Build Glassfish 3 domain.xml file
        new DomainXmlBuilder(metadata).writeConfiguration("/glassfish3/glassfish/domains/domain1/config/domain.xml");
    }
}
