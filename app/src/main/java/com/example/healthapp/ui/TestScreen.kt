package com.example.healthapp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.healthapp.data.entity.User
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.sql.DataSource

fun testDatabaseConnection() {
    val jdbcUrl = "jdbc:mysql://localhost:3306/HealthApp" // 使用你的数据库URL
    val username = "root" // 使用你的数据库用户名
    val password = "123456" // 使用你的数据库密码

    try {
        // 加载和注册JDBC驱动
        Class.forName("com.mysql.cj.jdbc.Driver")
        // 建立连接
        DriverManager.getConnection(jdbcUrl, username, password).use { connection ->
            println("Connected to the database successfully")
            // 执行查询
            connection.createStatement().use { statement ->
                val resultSet = statement.executeQuery("SELECT 1")
                if (resultSet.next()) {
                    println("Query executed successfully: result is ${resultSet.getInt(1)}")
                }
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

fun connectToDatabase(): Connection? {
    val url = "jdbc:mysql://localhost:3306/HealthApp"
    val user = "root"
    val password = "123456"

    return try {
        DriverManager.getConnection(url, user, password)
    } catch (e: SQLException) {
        null
    }
}

fun dataSource(): DataSource {
    val config = HikariConfig()
    config.jdbcUrl = "jdbc:mysql://localhost:3306/HealthApp" // 确保 URL 是正确的
    config.username = "root"  // 正确的用户名
    config.password = "123456"  // 正确的密码
    config.driverClassName = "com.mysql.cj.jdbc.Driver"  // 正确的驱动类名

    config.connectionTimeout = 1000 // 1秒
    config.idleTimeout = 60000 // 60秒
    config.maximumPoolSize = 10

    return HikariDataSource(config)
}

fun testGetUserById(userId: Int): User? {
    val dataSource = dataSource()
    val sql = "SELECT * FROM users WHERE userId = ?"
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    dataSource.connection.use { conn ->
        val preparedStatement = conn.prepareStatement(sql)
        preparedStatement.setInt(1, userId)
        val resultSet = preparedStatement.executeQuery()

        if (resultSet.next()) {
            return User(
                userId = resultSet.getInt("userId"),
                username = resultSet.getString("username"),
                email = resultSet.getString("email"),
                phoneNumber = resultSet.getString("phoneNumber"),
                passwordHash = resultSet.getString("passwordHash"),
                profilePicture = resultSet.getString("profilePicture"),
                role = resultSet.getString("role"),
                bloodType = resultSet.getString("bloodType"),
                gender = resultSet.getString("gender"),
                dateOfBirth = resultSet.getString("dateOfBirth")?.let {
                    LocalDateTime.parse(it, formatter)
                },
                dateCreated = resultSet.getString("dateCreated")?.let {
                    LocalDateTime.parse(it, formatter)
                } ?: LocalDateTime.now(),
                lastLogin = resultSet.getString("lastLogin")?.let {
                    LocalDateTime.parse(it, formatter)
                },
                isWheelchairUser = resultSet.getBoolean("isWheelchairUser") && !resultSet.wasNull(),
                skinType = resultSet.getString("skinType"),
                heartRateAffectingDrugs = resultSet.getString("heartRateAffectingDrugs")
            )
        }
        return null
    }
}

fun getAllUsers(connection: Connection): List<User> {

    val sql = "SELECT * FROM users"
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(sql)

    val users = ArrayList<User>()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    while (resultSet.next()) {
        val userId = resultSet.getInt("userId")
        val username = resultSet.getString("username")
        val email = resultSet.getString("email")
        val phoneNumber = resultSet.getString("phoneNumber")
        val passwordHash = resultSet.getString("passwordHash")
        val profilePicture = resultSet.getString("profilePicture")
        val role = resultSet.getString("role")
        val bloodType = resultSet.getString("bloodType")
        val gender = resultSet.getString("gender")
        val dateOfBirth = resultSet.getString("dateOfBirth")?.let {
            LocalDateTime.parse(it, formatter)
        }
        val dateCreated = resultSet.getString("dateCreated")?.let {
            LocalDateTime.parse(it, formatter)
        } ?: LocalDateTime.now()
        val lastLogin = resultSet.getString("lastLogin")?.let {
            LocalDateTime.parse(it, formatter)
        }
        val isWheelchairUser = resultSet.getBoolean("isWheelchairUser") && resultSet.wasNull().not()
        val skinType = resultSet.getString("skinType")
        val heartRateAffectingDrugs = resultSet.getString("heartRateAffectingDrugs")

        val user = User(
            userId = userId,
            username = username,
            email = email,
            phoneNumber = phoneNumber,
            passwordHash = passwordHash,
            profilePicture = profilePicture,
            role = role,
            bloodType = bloodType,
            gender = gender,
            dateOfBirth = dateOfBirth,
            dateCreated = dateCreated,
            lastLogin = lastLogin,
            isWheelchairUser = isWheelchairUser,
            skinType = skinType,
            heartRateAffectingDrugs = heartRateAffectingDrugs
        )
        users.add(user)
    }

    return users
}


fun main() {
    // 测试数据库连接
    testDatabaseConnection()
    println("分割线\n")

    // 尝试连接到数据库
    val connection = connectToDatabase()

    if (connection != null) {
        // 如果连接成功，获取所有用户并打印
        val userList = getAllUsers(connection)
        println("用户列表:")
        userList.forEach { user ->
            println("ID: ${user.userId}, Username: ${user.username}, Email: ${user.email}, Phone: ${user.phoneNumber}")
        }
    } else {
        // 如果连接失败，输出错误信息
        println("无法连接到数据库，请检查数据库配置")
    }
    val id = 1
    val test = testGetUserById(id)
    test?.let { test ->
        println("\n ID: ${test.userId}, Username: ${test.username}, Email: ${test.email}, Phone: ${test.phoneNumber}")
    }
}

@Composable
fun UseMySQLShowDataInUI() {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = "在这里显示用户名",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

