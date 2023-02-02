package com.school_21.fixme.router.entity;

import com.school_21.fixme.utils.messages.Message;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import javax.sql.DataSource;

@AllArgsConstructor
@NoArgsConstructor
@PropertySource("classpath:../application.properties")
public class MessageHandler {

    @Value("${db.driver}")
    private String driver;
    @Value("${db.url}")
    private String url;
    @Value("${db.username}")
    private String userName;
    @Value("${db.password}")
    private String password;
    private final JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

    public void save(Message message) {
        jdbcTemplate.update("INSERT INTO transactions(msg_length, msg_type, date, username, item_id, amount, price, market_name, check_sum)" +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)",
                message.get("9"), message.get("35"), message.get("52"), message.get("553"), message.get("100"), message.get("101"), message.get("102"),
                message.get("103"), message.get("10"));
    }

    public DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName(driver);
        dataSource.setUrl(url);
        dataSource.setUsername(userName);
        dataSource.setPassword(password);
        return dataSource;
    }
}
