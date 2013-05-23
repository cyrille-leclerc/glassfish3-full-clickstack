package com.cloudbees.genapp.glassfish3;

import com.cloudbees.genapp.metadata.ConfigurationBuilder;
import com.cloudbees.genapp.metadata.Metadata;
import com.cloudbees.genapp.resource.Database;
import com.cloudbees.genapp.resource.Email;
import com.cloudbees.genapp.resource.Resource;
import com.cloudbees.genapp.resource.SessionStore;
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
import java.io.File;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Properties;

public class DomainXmlBuilder implements ConfigurationBuilder {

    private Element glassfishResourcesElement;
    private Element glassfishServerElement;
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


    public DomainXmlBuilder() {
    }

    private DomainXmlBuilder(Metadata metadata) {
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
        System.out.println(" insert datasource " + database.getName());
        // <jdbc-connection-pool>
        Element connectionPool = glassfishResourcesElement.getOwnerDocument().createElement("jdbc-connection-pool");
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

        this.glassfishResourcesElement.appendChild(connectionPool);

        // <jdbc-resource>
        Element jdbcResource = glassfishResourcesElement.getOwnerDocument().createElement("jdbc-resource");
        jdbcResource.setAttribute("pool-name", connectionPoolName);
        String jndiName = "jdbc/" + database.getName();
        jdbcResource.setAttribute("jndi-name", jndiName);
        this.glassfishResourcesElement.appendChild(jdbcResource);


        // <resource-ref ref="..." />
        Element resourceRefElement = glassfishServerElement.getOwnerDocument().createElement("resource-ref");
        resourceRefElement.setAttribute("ref", jndiName);
        this.glassfishServerElement.appendChild(resourceRefElement);

        return this;
    }

    protected void addProperty(String name, String value, Element parent) {
        Element property = parent.getOwnerDocument().createElement("property");
        property.setAttribute("name", name);
        property.setAttribute("value", value);
        parent.appendChild(property);
    }

    private DomainXmlBuilder addEmail(Email email) {
        System.err.print("email is not yet supported, ignore it");
        return this;
    }

    private DomainXmlBuilder addSessionStore(SessionStore store) {
        System.err.print("session store is not yet supported, ignore it");
        return this;
    }

    private DomainXmlBuilder fromExistingDocument(Document domainDocument) {
        String rootElementName = domainDocument.getDocumentElement().getNodeName();
        if (!rootElementName.equals("domain"))
            throw new IllegalArgumentException("Document is missing root <domain> element");
        this.glassfishResourcesElement = (Element) domainDocument.getElementsByTagName("resources").item(0);
        Element serversElement = (Element) domainDocument.getElementsByTagName("servers").item(0);
        this.glassfishServerElement = (Element) serversElement.getElementsByTagName("server").item(0);
        return this;
    }

    private DomainXmlBuilder fromExistingDocument(File file) throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(file);
        fromExistingDocument(document);
        return this;
    }

    @Override
    public void writeConfiguration(Metadata metadata, File configurationFile) throws Exception {
        Document contextXml = this.create(metadata).fromExistingDocument(configurationFile).buildContextDocument();

        // Write the content into XML file
        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        transformer.setOutputProperty(OutputKeys.STANDALONE, "no");

        transformer.transform(new DOMSource(contextXml), new StreamResult(configurationFile));
    }

    private Document buildContextDocument() throws ParserConfigurationException {
        if (glassfishResourcesElement == null) {
            throw new IllegalStateException();
        }

        addResources(metadata);
        return glassfishResourcesElement.getOwnerDocument();
    }
}
