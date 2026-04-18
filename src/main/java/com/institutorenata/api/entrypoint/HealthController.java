package com.institutorenata.api.entrypoint;

import java.sql.Connection;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class HealthController {

	private final DataSource dataSource;
	private final Environment environment;

	public HealthController(DataSource dataSource, Environment environment) {
		this.dataSource = dataSource;
		this.environment = environment;
	}

	@GetMapping("/health")
	public ResponseEntity<Map<String, Object>> health() {
		String database = "ok";
		try (Connection connection = dataSource.getConnection()) {
			if (!connection.isValid(2)) {
				database = "error";
			}
		} catch (Exception ex) {
			database = "error";
		}
		boolean ok = "ok".equals(database);
		String[] active = environment.getActiveProfiles();
		String envLabel = active.length > 0 ? String.join(",", active) : "default";
		String overall = ok ? "ok" : "degraded";
		Map<String, Object> body = new LinkedHashMap<>();
		body.put("status", overall);
		body.put("env", envLabel);
		body.put("database", database);
		return ResponseEntity.status(ok ? HttpStatus.OK : HttpStatus.SERVICE_UNAVAILABLE).body(body);
	}
}
