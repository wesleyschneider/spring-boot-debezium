package br.com.wesleyschneider.springbootdebezium.listener;

import br.com.wesleyschneider.springbootdebezium.service.CreateService;
import br.com.wesleyschneider.springbootdebezium.service.DeleteService;
import br.com.wesleyschneider.springbootdebezium.service.UpdateService;
import io.debezium.config.Configuration;
import io.debezium.embedded.Connect;
import io.debezium.engine.DebeziumEngine;
import io.debezium.engine.RecordChangeEvent;
import io.debezium.engine.format.ChangeEventFormat;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.text.CaseUtils;
import org.apache.kafka.connect.data.Field;
import org.apache.kafka.connect.data.Struct;
import org.apache.kafka.connect.source.SourceRecord;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import static io.debezium.data.Envelope.FieldName.*;
import static io.debezium.data.Envelope.Operation;

@Component
public class DebeziumListener {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final DebeziumEngine<RecordChangeEvent<SourceRecord>> debeziumEngine;

    @Autowired
    private ApplicationContext context;

    public DebeziumListener(Configuration estudanteConnectorConfiguration) {
        this.debeziumEngine = DebeziumEngine
                .create(ChangeEventFormat.of(Connect.class))
                .using(estudanteConnectorConfiguration.asProperties()).notifying(this::handleChange)
                .build();
    }

    private void handleChange(RecordChangeEvent<SourceRecord> sourceRecordRecordChangeEvent) {
        Struct sourceRecordChangeValue = (Struct) sourceRecordRecordChangeEvent.record().value();

        if (sourceRecordChangeValue == null) return;

        Operation operation = Operation.forCode((String) sourceRecordChangeValue.get(OPERATION));

        if (operation == Operation.READ) return;

        String record = operation == Operation.DELETE ? BEFORE : AFTER;

        // Get value changes
        Struct struct = (Struct) sourceRecordChangeValue.get(record);
        Map<String, Object> payload = struct.schema().fields().stream()
                .map(Field::name)
                .filter(fieldName -> struct.get(fieldName) != null)
                .map(fieldName -> Pair.of(fieldName, struct.get(fieldName)))
                .collect(Collectors.toMap(Pair::getKey, Pair::getValue));

        // Get table name
        Struct source = (Struct) sourceRecordChangeValue.get("source");
        String table = (String) source.get("table");

        String beanName = CaseUtils.toCamelCase(table, true, '_') + "Service";

        try {
            var service = context.getBean(beanName);

            switch(operation){
                case CREATE -> {
                    if(service instanceof CreateService) ((CreateService) service).create(payload);
                }
                case UPDATE -> {
                    if(service instanceof UpdateService) ((UpdateService) service).update(payload);
                }
                case DELETE -> {
                    if(service instanceof DeleteService) ((DeleteService) service).delete(payload);
                }
            }
        } catch (NoSuchBeanDefinitionException e) {
            System.out.println(e.getMessage());
        }
    }

    @PostConstruct
    private void start() {
        this.executor.execute(debeziumEngine);
    }

    @PreDestroy
    private void stop() throws IOException {
        if (this.debeziumEngine != null) {
            this.debeziumEngine.close();
        }
    }
}
