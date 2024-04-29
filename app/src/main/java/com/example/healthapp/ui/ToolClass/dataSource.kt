package com.example.healthapp.ui.ToolClass

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import javax.sql.DataSource

object DatabaseConfig {
    fun dataSource(): DataSource {
        val config = HikariConfig()
        config.jdbcUrl = "jdbc:mysql://localhost:3306/HealthApp"
        config.username = "root"
        config.password = "123456"
        config.driverClassName = "com.mysql.cj.jdbc.Driver"

        // 设置连接超时
        config.connectionTimeout = 1000 // 1秒

        // 设置空闲超时
        config.idleTimeout = 60000 // 60秒

        // 设置最大连接数
        config.maximumPoolSize = 10

        return HikariDataSource(config)
    }
}


