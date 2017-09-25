package ru.techlab.risks.calculation;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.*;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import ru.techlab.risks.calculation.config.AppConfig;

import java.io.IOException;

/**
 * Created by dim777999 on 20.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@ActiveProfiles("test")
public class CassandraRepositoryTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraRepositoryTests.class);

    @Value("${cql.keyspaceCreationQuery}")
    private static String KEYSPACE_CREATION_QUERY;

    @Value("${cql.keyspaceActivateQuery}")
    private static String KEYSPACE_ACTIVATE_QUERY;

    @Value("${cql.dataTableName}")
    private static String DATA_TABLE_NAME;

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @BeforeClass
    public static void startEmbeddedCassandra() throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra(20000);
        final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9142).build();
        LOGGER.info("Server Started at 127.0.0.1:9142... ");
        final Session session = cluster.connect();
        session.execute(KEYSPACE_CREATION_QUERY);
        session.execute(KEYSPACE_ACTIVATE_QUERY);
        LOGGER.info("KeySpace created and activated.");
        Thread.sleep(5000);
    }

    /*@Before
    public void createTable() {
        adminTemplate.createTable(
                true, CqlIdentifier.cqlId(DATA_TABLE_NAME),
                Book.class, new HashMap<String, Object>());
    }*/

    @Test
    public void shouldHaveAnEmbeddedCassandraStartOn9175Port() throws Exception {


    }

    @After
    public void dropTable() {
        adminTemplate.dropTable(CqlIdentifier.cqlId(DATA_TABLE_NAME));
    }

    @AfterClass
    public static void cleanCassandra() {
        EmbeddedCassandraServerHelper.cleanEmbeddedCassandra();
    }
}
