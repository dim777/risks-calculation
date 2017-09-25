package ru.techlab.risks.calculation;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;
import java.util.stream.IntStream;

@SpringBootApplication
public class RisksCalculationApplication {
	private static final Logger log = LoggerFactory.getLogger(RisksCalculationApplication.class);

	public static void main(String[] args) throws IOException {
		if (args.length == 0) {
			log.error("Usage:");
			log.error("java -jar RisksCalculationApplication.jar -l <cassandra_login> -p <cassandra_password> -k <cassandra_keyspace>");
			log.error("-l <cassandra_login> - required, Cassandra login");
			log.error("-p <cassandra_password> - required, Cassandra pass");
			log.error("-k <cassandra_keyspace> - required, Cassandra keyspace");
			return;
		}

		String login = IntStream.range(0, args.length)
				.filter(i -> args[i].equals("-l"))
				.mapToObj(i -> args[i + 1])
				.findFirst()
				.orElseThrow(() -> new IOException("Cassandra login must be provided"));

		String password = IntStream.range(0, args.length)
				.filter(i -> args[i].equals("-p"))
				.mapToObj(i -> args[i + 1])
				.findFirst()
				.orElseThrow(() -> new IOException("Cassandra password must be provided"));

		String keyspace = IntStream.range(0, args.length)
				.filter(i -> args[i].equals("-k"))
				.mapToObj(i -> args[i + 1])
				.findFirst()
				.orElseThrow(() -> new IOException("Cassandra keyspace must be provided"));

		SpringApplication.run(RisksCalculationApplication.class, new String[]{
				"spring.data.cassandra.username=" + login,
				"spring.data.cassandra.password=" + password,
				"spring.data.cassandra.keyspace-name" + keyspace
		});
	}
}
