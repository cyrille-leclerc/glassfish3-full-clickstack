package com.cloudbees.genapp.glassfish3;

import com.cloudbees.genapp.metadata.EnvBuilder;
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
        System.err.print("insert glassfish resources " + Arrays.asList(args));
        MetadataFinder metadataFinder = new MetadataFinder();
        // Build the environment with bash-safe names, and no deprecated values.
        metadataFinder.setup("/.genapp/control/env_safe", new EnvBuilder(true, false));
        // Build the environment properties (bash-unsafe)
        metadataFinder.setup("/.genapp/control/env", new EnvBuilder(false, false));
        // Build Glassfish 3 domain.xml file
        metadataFinder.setup("/glassfish3/glassfish/domains/domain1/config/domain.xml", new DomainXmlBuilder());
    }
}
