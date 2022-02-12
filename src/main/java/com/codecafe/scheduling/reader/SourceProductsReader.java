package com.codecafe.scheduling.reader;

import com.codecafe.scheduling.entity.SourceProduct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.PostgresPagingQueryProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
@StepScope
@Component
public class SourceProductsReader extends JdbcPagingItemReader<SourceProduct> {

    private ExecutionContext executionContext;

    public SourceProductsReader(DataSource dataSource,
                                @Value("#{stepExecution.jobExecution.executionContext}") ExecutionContext executionContext) {
        log.info("==> Entered inside SourceProductsReader constructor");
        this.executionContext = executionContext;
        setDataSource(dataSource);
        setQueryProvider(createQuery());
        setPageSize(100);
        //setFetchSize(100);
        setRowMapper(new SourceProductRowMapper());
        log.info("<== Exiting from SourceProductsReader constructor");
    }

    private PostgresPagingQueryProvider createQuery() {
        final PostgresPagingQueryProvider queryProvider = new PostgresPagingQueryProvider();
        final Map<String, Order> sortKeys = new LinkedHashMap<>();
        sortKeys.put("modified_at", Order.ASCENDING);
        queryProvider.setSortKeys(sortKeys);
        queryProvider.setSelectClause("*");
        queryProvider.setFromClause("source_products");

        Date lastCompletedJobTime = (Date) executionContext.get("lastCompletedJobTime");
        if (lastCompletedJobTime != null) {
            queryProvider.setWhereClause("modified_at >= '" + lastCompletedJobTime + "'");
        }

        return queryProvider;
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