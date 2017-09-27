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
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cassandra.core.cql.CqlIdentifier;
import org.springframework.core.env.Environment;
import org.springframework.data.cassandra.core.CassandraAdminOperations;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import ru.techlab.risks.calculation.config.AppConfig;
import ru.techlab.risks.calculation.model.BaseDelay;

import java.io.IOException;
import java.util.HashMap;

/**
 * Created by dim777999 on 20.09.2017.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@SpringBootTest
@ActiveProfiles("test")
public class CassandraRepositoryTests {
    private static final Logger LOGGER = LoggerFactory.getLogger(CassandraRepositoryTests.class);

    private final static String DATA_TABLE_NAME = "SRRU";

    @Autowired
    private CassandraAdminOperations adminTemplate;

    @BeforeClass
    public static void startEmbeddedCassandra() throws IOException, TTransportException, InterruptedException {
        EmbeddedCassandraServerHelper.startEmbeddedCassandra("embedded-cassandra.yaml", 20000);
        final Cluster cluster = Cluster.builder().addContactPoints("127.0.0.1").withPort(9152).build();
        LOGGER.info("Server Started at 127.0.0.1:9152... ");
        final Session session = cluster.connect();
        session.execute("CREATE KEYSPACE IF NOT EXISTS testKeySpace WITH replication = { 'class': 'SimpleStrategy', 'replication_factor': '3' };");
        session.execute("USE testKeySpace;");
        LOGGER.info("KeySpace created and activated.");
        Thread.sleep(5000);
    }

    @Before
    public void createTable() {
        adminTemplate.createTable(
                true,
                CqlIdentifier.cqlId(DATA_TABLE_NAME),
                BaseDelay.class, new HashMap<>()
        );
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
