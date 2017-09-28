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
			log.error("java -jar RisksCalculationApplication.jar --login=<cassandra_login> --pass=<cassandra_password>");
			log.error("--login=<cassandra_login> - required, Cassandra login");
			log.error("--pass=<cassandra_password> - required, Cassandra pass");
			return;
		}

		String login = IntStream.range(0, args.length)
				.filter(i -> args[i].startsWith("--login="))
				.mapToObj(i -> args[i].split("--login=")[0])
				.findFirst()
				.orElseThrow(() -> new IOException("Cassandra login must be provided"));

		String password = IntStream.range(0, args.length)
				.filter(i -> args[i].startsWith("--pass="))
				.mapToObj(i -> args[i].split("--pass=")[0])
				.findFirst()
				.orElseThrow(() -> new IOException("Cassandra password must be provided"));


		SpringApplication.run(RisksCalculationApplication.class, args);
	}
}
