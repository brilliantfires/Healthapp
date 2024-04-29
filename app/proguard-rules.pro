# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn java.lang.management.ManagementFactory
-dontwarn java.lang.management.ThreadInfo
-dontwarn java.lang.management.ThreadMXBean
-dontwarn java.rmi.server.UID
-dontwarn javax.management.MBeanServer
-dontwarn javax.management.ObjectInstance
-dontwarn javax.management.ObjectName
# Please add these rules to your existing keep rules in order to suppress warnings.
# This is generated automatically by the Android Gradle plugin.
-dontwarn com.oracle.bmc.ConfigFileReader$ConfigFile
-dontwarn com.oracle.bmc.ConfigFileReader
-dontwarn java.sql.JDBCType
-dontwarn java.sql.SQLType
-dontwarn javax.naming.InvalidNameException
-dontwarn javax.naming.ldap.LdapName
-dontwarn javax.naming.ldap.Rdn
-dontwarn javax.security.auth.callback.NameCallback
-dontwarn javax.security.auth.login.Configuration
-dontwarn javax.security.auth.login.LoginContext
-dontwarn javax.security.sasl.Sasl
-dontwarn javax.security.sasl.SaslClient
-dontwarn javax.security.sasl.SaslClientFactory
-dontwarn javax.security.sasl.SaslException
-dontwarn javax.xml.stream.XMLEventWriter
-dontwarn javax.xml.stream.XMLInputFactory
-dontwarn javax.xml.stream.XMLOutputFactory
-dontwarn javax.xml.stream.XMLStreamException
-dontwarn javax.xml.stream.XMLStreamReader
-dontwarn javax.xml.transform.stax.StAXResult
-dontwarn javax.xml.transform.stax.StAXSource
# 添加混淆规则
-keep class com.example.healthapp.data.entity.** { *; }
-keep class com.example.healthapp.data.viewmodel.** { *; }
# Retrofit service definitions
-keep class com.example.healthapp.data.mysql.** { *; }
-keepattributes Signature
-keepattributes *Annotation*
-keepattributes Exceptions

# Retrofit and Gson
-keep class retrofit2.** { *; }
-keepattributes RuntimeVisibleAnnotations, RuntimeVisibleParameterAnnotations
-keepattributes EnclosingMethod

# Gson specific classes
-keep class com.google.gson.** { *; }
-keep class com.example.healthapp.data.LocalDateTimeAdapter { *; }
-dontwarn okhttp3.**
-keep class okhttp3.internal.** { *; }
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-dontwarn kotlinx.coroutines.**
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
-keep class * {
    *;
}


