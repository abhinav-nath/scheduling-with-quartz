package com.codecafe.scheduling.repository;

import com.codecafe.scheduling.entity.BatchJobExecutionDetail;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

import static org.springframework.util.CollectionUtils.isEmpty;

@Repository
public class BatchJobRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;

    public BatchJobRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<BatchJobExecutionDetail> getLastSuccessfulJob(String jobName, String status) {
        final String sqlQuery = "select job_execution_id, start_time, end_time, status from batch_job_execution "
                + "where job_instance_id in (select job_instance_id from batch_job_instance where job_name = :jobName) and "
                + "status = :status "
                + "order by job_execution_id desc LIMIT 1";

        List<BatchJobExecutionDetail> jobDetails = jdbcTemplate.query(sqlQuery,
                new MapSqlParameterSource().addValue("jobName", jobName).addValue("status", status),
                new BatchJobExecutionDetailRowMapper());

        if (!isEmpty(jobDetails))
            return Optional.of(jobDetails.get(0));

        return Optional.empty();
    }

    private static final class BatchJobExecutionDetailRowMapper implements RowMapper<BatchJobExecutionDetail> {

        public BatchJobExecutionDetail mapRow(ResultSet rs, int rowNum) throws SQLException {
            BatchJobExecutionDetail jobDetail = new BatchJobExecutionDetail();
            jobDetail.setJobExecutionId(rs.getInt("job_execution_id"));

            if (rs.getTimestamp("start_time") != null)
                jobDetail.setStartTime(rs.getTimestamp("start_time"));

            if (rs.getTimestamp("end_time") != null)
                jobDetail.setEndTime(rs.getTimestamp("end_time"));

            jobDetail.setStatus(rs.getString("status"));
            return jobDetail;
        }

    }

}