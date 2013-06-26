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

import com.cloudbees.genapp.XmlUtils;
import com.cloudbees.genapp.metadata.Metadata;
import com.cloudbees.genapp.metadata.resource.Database;
import com.cloudbees.genapp.metadata.resource.Email;
import com.cloudbees.genapp.metadata.resource.Resource;
import com.cloudbees.genapp.metadata.resource.SessionStore;
import com.mysql.jdbc.NonRegisteringDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPathExpressionException;
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Logger;

public class DomainXmlBuilder {

    private final Logger logger = Logger.getLogger(getClass().getName());
    private Document xmlDocument;
    private Metadata metadata;
    private List<String> databaseProperties = Arrays.asList("AllowLoadLocalInfile",
            "AllowMultiQueries",
            "AllowNanAndInf",
            "AllowUrlInLocalInfile",
            "AlwaysSendSetIsolation",
            "AutoClosePStmtStreams",
            "AutoDeserialize",
            "AutoGenerateTestcaseScript",
            "AutoReconnectForPools",
            "AutoSlowLog",
            "BlobsAreStrings",
            "BlobSendChunkSize",
            "CacheCallableStatements",
            "CacheCallableStmts",
            "CachePreparedStatements",
            "CachePrepStmts",
            "CacheResultSetMetadata",
            "CacheServerConfiguration",
            "CallableStatementCacheSize",
            "CallableStmtCacheSize",
            "CapitalizeTypeNames",
            "ClientCertificateKeyStoreType",
            "ClientInfoProvider",
            "ClobberStreamingResults",
            "CompensateOnDuplicateKeyUpdateCounts",
            "ConnectTimeout",
            "ContinueBatchOnError",
            "CreateDatabaseIfNotExist",
            "DefaultAuthenticationPlugin",
            "DefaultFetchSize",
            "DisconnectOnExpiredPasswords",
            "DontTrackOpenResources",
            "DumpMetadataOnColumnNotFound",
            "DumpQueriesOnException",
            "DynamicCalendars",
            "ElideSetAutoCommits",
            "EmptyStringsConvertToZero",
            "EmulateLocators",
            "EmulateUnsupportedPstmts",
            "EnablePacketDebug",
            "EnableQueryTimeouts",
            "ExplainSlowQueries",
            "FailOverReadOnly",
            "FunctionsNeverReturnBlobs",
            "GatherPerfMetrics",
            "GatherPerformanceMetrics",
            "GenerateSimpleParameterMetadata",
            "HoldResultsOpenOverStatementClose",
            "IgnoreNonTxTables",
            "IncludeInnodbStatusInDeadlockExceptions",
            "IncludeThreadDumpInDeadlockExceptions",
            "IncludeThreadNamesAsStatementComment",
            "InitialTimeout",
            "InteractiveClient",
            "IsInteractiveClient",
            "JdbcCompliantTruncation",
            "JdbcCompliantTruncationForReads",
            "LoadBalanceAutoCommitStatementThreshold",
            "LoadBalanceBlacklistTimeout",
            "LoadBalanceEnableJMX",
            "LoadBalanceExceptionChecker",
            "LoadBalancePingTimeout",
            "LoadBalanceStrategy",
            "LoadBalanceValidateConnectionOnSwapServer",
            "LocatorFetchBufferSize",
            "Logger",
            "LoggerClassName",
            "LoginTimeout",
            "LogSlowQueries",
            "LogXaCommands",
            "MaintainTimeStats",
            "MaxAllowedPacket",
            "MaxQuerySizeToLog",
            "MaxReconnects",
            "MaxRows",
            "MetadataCacheSize",
            "NetTimeoutForStreamingResults",
            "NoAccessToProcedureBodies",
            "NoDatetimeStringSync",
            "NoTimezoneConversionForTimeType",
            "NullCatalogMeansCurrent",
            "NullNamePatternMatchesAll",
            "OverrideSupportsIntegrityEnhancementFacility",
            "PacketDebugBufferSize",
            "PadCharsWithSpace",
            "Paranoid",
            "ParseInfoCacheFactory",
            "Pedantic",
            "PinGlobalTxToPhysicalConnection",
            "PopulateInsertRowWithDefaultValues",
            "PreparedStatementCacheSize",
            "PreparedStatementCacheSqlLimit",
            "PrepStmtCacheSize",
            "PrepStmtCacheSqlLimit",
            "ProcessEscapeCodesForPrepStmts",
            "ProfilerEventHandler",
            "ProfileSQL",
            "ProfileSql",
            "QueriesBeforeRetryMaster",
            "QueryTimeoutKillsConnection",
            "ReconnectAtTxEnd",
            "RelaxAutoCommit",
            "ReportMetricsIntervalMillis",
            "RequireSSL",
            "ResultSetSizeThreshold",
            "RetainStatementAfterResultSetClose",
            "RetriesAllDown",
            "RewriteBatchedStatements",
            "RollbackOnPooledClose",
            "RoundRobinLoadBalance",
            "RunningCTS13",
            "SecondsBeforeRetryMaster",
            "SelfDestructOnPingMaxOperations",
            "SelfDestructOnPingSecondsLifetime",
            "ServerConfigCacheFactory",
            "SlowQueryThresholdMillis",
            "SlowQueryThresholdNanos",
            "SocketFactory",
            "SocketFactoryClassName",
            "SocketTimeout",
            "StrictFloatingPoint",
            "StrictUpdates",
            "TcpKeepAlive",
            "TcpNoDelay",
            "TcpRcvBuf",
            "TcpSndBuf",
            "TcpTrafficClass",
            "TinyInt1isBit",
            "TraceProtocol",
            "TransformedBitIsBoolean",
            "TreatUtilDateAsTimestamp",
            "TrustCertificateKeyStoreType",
            "UltraDevHack",
            "UseAffectedRows",
            "UseBlobToStoreUTF8OutsideBMP",
            "UseColumnNamesInFindColumn",
            "UseCompression",
            "UseCursorFetch",
            "UseDirectRowUnpack",
            "UseDynamicCharsetInfo",
            "UseFastDateParsing",
            "UseFastIntParsing",
            "UseGmtMillisForDatetimes",
            "UseHostsInPrivileges",
            "UseInformationSchema",
            "UseJDBCCompliantTimezoneShift",
            "UseJvmCharsetConverters",
            "UseLegacyDatetimeCode",
            "UseLocalSessionState",
            "UseLocalTransactionState",
            "UseNanosForElapsedTime",
            "UseOldAliasMetadataBehavior",
            "UseOldUTF8Behavior",
            "UseOnlyServerErrorMessages",
            "UseReadAheadInput",
            "UseServerPreparedStmts",
            "UseServerPrepStmts",
            "UseSqlStateCodes",
            "UseSSL",
            "UseSSPSCompatibleTimezoneShift",
            "UseStreamLengthsInPrepStmts",
            "UseTimezone",
            "UseUltraDevWorkAround",
            "UseUnbufferedInput",
            "UseUnicode",
            "UseUsageAdvisor",
            "VerifyServerCertificate",
            "YearIsDateType",
            "ZeroDateTimeBehavior");


    public DomainXmlBuilder(Metadata metadata) {
        this.metadata = metadata;
    }

    public DomainXmlBuilder create(Metadata metadata) {
        return new DomainXmlBuilder(metadata);
    }

    private DomainXmlBuilder addResources(Metadata metadata) {
        for (Resource resource : metadata.getResources().values()) {
            if (resource instanceof Database) {
                addDatabase((Database) resource);
            } else if (resource instanceof Email) {
                addEmail((Email) resource);
            } else if (resource instanceof SessionStore) {
                addSessionStore((SessionStore) resource);
            }

        }
        return this;
    }

    private DomainXmlBuilder addDatabase(Database database) {
        logger.info("Insert DataSource " + database.getName());
        // <jdbc-connection-pool>
        Element connectionPool = xmlDocument.createElement("jdbc-connection-pool");
        connectionPool.setAttribute("driver-classname", database.getJavaDriver());
        connectionPool.setAttribute("connection-validation-method", database.getProperty("connection-validation-method", "auto-commit"));
        connectionPool.setAttribute("is-connection-validation-required", database.getProperty("is-connection-validation-required", "true"));
        connectionPool.setAttribute("validation-table-name", database.getProperty("validation-table-name", ""));

        connectionPool.setAttribute("datasource-classname", database.getDataSourceClassName());
        connectionPool.setAttribute("max-pool-size", database.getProperty("max-pool-size", "20"));
        connectionPool.setAttribute("wrap-jdbc-objects", "false");
        connectionPool.setAttribute("res-type", "javax.sql.DataSource");
        connectionPool.setAttribute("steady-pool-size", database.getProperty("steady-pool-size", "1"));
        String connectionPoolName = database.getName() + "ConnectionPool";
        connectionPool.setAttribute("name", connectionPoolName);

        String jdbcUrl = "jdbc:" + database.getUrl();
        addProperty("URL", jdbcUrl, connectionPool);
        addProperty("Url", jdbcUrl, connectionPool);
        Properties properties;
        try {
            properties = new NonRegisteringDriver().parseURL(jdbcUrl, null);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        addProperty("ServerName", properties.getProperty(NonRegisteringDriver.HOST_PROPERTY_KEY), connectionPool);
        addProperty("Port", properties.getProperty(NonRegisteringDriver.PORT_PROPERTY_KEY), connectionPool);
        addProperty("DatabaseName", properties.getProperty(NonRegisteringDriver.DBNAME_PROPERTY_KEY), connectionPool);
        addProperty("User", database.getUsername(), connectionPool);
        addProperty("Password", database.getPassword(), connectionPool);

        Map<String, String> optionalParameters = database.filterProperties(databaseProperties);
        for (Map.Entry<String, String> entry : optionalParameters.entrySet()) {
            connectionPool.setAttribute(entry.getKey(), entry.getValue());
        }
        Element glassfishResourcesElement = XmlUtils.getUniqueElement(xmlDocument, "/domain/resources");

        glassfishResourcesElement.appendChild(connectionPool);

        // <jdbc-resource>
        Element jdbcResource = xmlDocument.createElement("jdbc-resource");
        jdbcResource.setAttribute("pool-name", connectionPoolName);
        String jndiName = "jdbc/" + database.getName();
        jdbcResource.setAttribute("jndi-name", jndiName);
        glassfishResourcesElement.appendChild(jdbcResource);


        // <resource-ref ref="..." />
        Element glassfishServerElement = XmlUtils.getUniqueElement(xmlDocument, "/domain/servers/server");

        Element resourceRefElement = xmlDocument.createElement("resource-ref");
        resourceRefElement.setAttribute("ref", jndiName);
        glassfishServerElement.appendChild(resourceRefElement);

        return this;
    }

    protected void addProperty(String name, String value, Element parent) {
        Element property = parent.getOwnerDocument().createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        parent.appendChild(property);
    }

    private void addAuthenticationRealm(Metadata metadata) throws XPathExpressionException {


        String jdbcRealmBinding = metadata.getRuntimeParameter("glassfish3", "auth-realm.database", null);
        if (jdbcRealmBinding == null) {
            return;
        }

        // Verify that the database binding exists
        Resource resource = metadata.getResource(jdbcRealmBinding);
        if (resource == null || !(resource instanceof Database)) {
            throw new RuntimeException("Database binding '" + jdbcRealmBinding + "' declared for RuntimeParameter " +
                    "glassfish3" + "#" + "auth-realm.database" + " does not exist! Existing Resources " + metadata.getResources().keySet());
        }

        String realmName = "authentication-realm";
        String groupsTable = "cb_groups";
        String usersTable = "cb_users";
        String usernameColumn = "username";
        String passwordColumn = "password";
        String groupNameColumn = "groupname";
        String encodingAlgorithm = "SHA-256";
        String encoding = "Base64";

        logger.info("Insert JdbcRealm '" + realmName + "' associated to database '" + jdbcRealmBinding + "'");

        Element authRealm = xmlDocument.createElement("auth-realm");
        authRealm.setAttribute("name", realmName);
        authRealm.setAttribute("classname", "com.sun.enterprise.security.auth.realm.jdbc.JDBCRealm");
        // jaas-context:jdbcRealm must match an enentry in login.conf
        addProperty("jaas-context", "jdbcRealm", authRealm);
        addProperty("password-column", passwordColumn, authRealm);
        addProperty("datasource-jndi", "jdbc/" + jdbcRealmBinding, authRealm);
        addProperty("group-table", groupsTable, authRealm);
        addProperty("user-table", usersTable, authRealm);
        addProperty("group-name-column", groupNameColumn, authRealm);
        addProperty("digestrealm-password-enc-algorithm", encodingAlgorithm, authRealm);
        addProperty("digest-algorithm", encodingAlgorithm, authRealm);
        addProperty("encoding", encoding, authRealm);
        addProperty("user-name-column", usernameColumn, authRealm);

        Element securityService = XmlUtils.getUniqueElement(xmlDocument, "domain/configs/config[@name='server-config']/security-service");
        securityService.appendChild(authRealm);

    }

    private DomainXmlBuilder addEmail(Email email) {
        logger.warning("email is not yet supported, ignore it");
        return this;
    }

    private DomainXmlBuilder addSessionStore(SessionStore store) {
        logger.warning("session store is not yet supported, ignore it");
        return this;
    }

    private DomainXmlBuilder fromExistingDocument(Document domainDocument) throws XPathExpressionException {
        xmlDocument = domainDocument;
        return this;
    }

    private DomainXmlBuilder fromExistingDocument(File file) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        fromExistingDocument(document);
        return this;
    }

    public void writeConfiguration(String configurationRelativePath) throws Exception {


        Map<String, String> env = System.getenv();
        String configurationPath = env.get("app_dir") + configurationRelativePath;

        // Locate configuration file
        File configurationFile = new File(configurationPath);
        if (!configurationFile.exists())
            throw new Exception("Missing context config file: " + configurationFile.getAbsolutePath());

        Document contextXml = this.create(metadata).fromExistingDocument(configurationFile).buildContextDocument();

        // Write the content into XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        transformer.transform(new DOMSource(contextXml), new StreamResult(configurationFile));
    }

    private Document buildContextDocument() throws ParserConfigurationException, XPathExpressionException {
        if (xmlDocument == null) {
            throw new IllegalStateException();
        }

        addResources(metadata);
        addAuthenticationRealm(metadata);
        return xmlDocument;
    }

}
