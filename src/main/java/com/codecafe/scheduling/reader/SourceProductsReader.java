package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;

@Slf4j
@StepScope
@Component
public class SourceProductsReader extends JdbcCursorItemReader<SourceProduct> {

    private ExecutionContext executionContext;

    public SourceProductsReader(DataSource dataSource, @Value("#{stepExecution.jobExecution.executionContext}") ExecutionContext executionContext) {
        log.info("==> Entered inside SourceProductsReader constructor");

        this.executionContext = executionContext;
        setDataSource(dataSource);
        setDataSource(dataSource);
        setSql(createQuery());
        //setFetchSize(100);
        setRowMapper(new SourceProductRowMapper());
        setSaveState(true);

        log.info("<== Exiting from SourceProductsReader constructor");
    }

    private String createQuery() {
        log.info("==> Entered inside SourceProductReader::createQuery method");

        String sqlStr = "SELECT * FROM source_products";
        Optional<Date> lastCompletedJobTime = (Optional<Date>) executionContext.get("lastCompletedJobTime");
        if (lastCompletedJobTime.isPresent()) {
            sqlStr = new StringBuilder("SELECT * FROM source_products sp WHERE sp.modified_at >= '")
                    .append(lastCompletedJobTime.get())
                    .append("' ORDER BY sp.modified_at ASC").toString();
        } else {
            log.info("createQuery :: No previous completed job found");
        }

        log.info("<== Exiting from SourceProductReader::createQuery method");
        return sqlStr;
    }

    public static class SourceProductRowMapper implements RowMapper<SourceProduct> {
        @Override
        public SourceProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            SourceProduct sourceProduct = new SourceProduct();
            sourceProduct.setId(rs.getInt("id"));
            sourceProduct.setName(rs.getString("name"));

            if (rs.getTimestamp("created_at") != null)
                sourceProduct.setCreatedAt(rs.getTimestamp("created_at"));

            if (rs.getTimestamp("modified_at") != null)
                sourceProduct.setModifiedAt(rs.getTimestamp("modified_at"));

            sourceProduct.setDeleted(rs.getBoolean("is_deleted"));
            return sourceProduct;
        }
    }

}