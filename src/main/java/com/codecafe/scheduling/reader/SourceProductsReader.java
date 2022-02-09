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

@Slf4j
@StepScope
@Component
public class SourceProductsReader extends JdbcCursorItemReader<SourceProduct> {

    private ExecutionContext executionContext;

    public SourceProductsReader(DataSource dataSource,
                                @Value("#{stepExecution.jobExecution.executionContext}") ExecutionContext executionContext) {
        log.info("==> Entered inside SourceProductsReader constructor");
        this.executionContext = executionContext;
        setDataSource(dataSource);
        setSql(prepareSql());
        //setFetchSize(100);
        setRowMapper(new SourceProductRowMapper());
        log.info("<== Exiting from SourceProductsReader constructor");
    }

    private String prepareSql() {
        String sql = "SELECT * FROM source_products";
        Date lastCompletedJobTime = (Date) executionContext.get("lastCompletedJobTime");
        if (lastCompletedJobTime != null) {
            sql = new StringBuilder("SELECT * FROM source_products sp WHERE sp.modified_at >= '")
                    .append(lastCompletedJobTime)
                    .append("' ORDER BY sp.modified_at ASC").toString();
        }
        return sql;
    }

    public static class SourceProductRowMapper implements RowMapper<SourceProduct> {
        @Override
        public SourceProduct mapRow(ResultSet rs, int rowNum) throws SQLException {
            SourceProduct sourceProduct = new SourceProduct();
            sourceProduct.setId(rs.getInt("id"));
            sourceProduct.setName(rs.getString("name"));

            if (rs.getTimestamp("created_at") != null)
                sourceProduct.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());

            if (rs.getTimestamp("modified_at") != null)
                sourceProduct.setModifiedAt(rs.getTimestamp("modified_at").toLocalDateTime());

            return sourceProduct;
        }
    }

}