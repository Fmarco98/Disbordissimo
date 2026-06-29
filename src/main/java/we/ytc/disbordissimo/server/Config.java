package we.ytc.disbordissimo.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.*;

public class Config {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_PATH = "config/config.json";
    private static final File configFile = new File(CONFIG_FILE_PATH);

    @SerializedName("version")
    public String version;

    @SerializedName("tcpServer")
    public TcpServerConfig tcpServerConfig;
    @SerializedName("udpServer")
    public UdpServerConfig udpServerConfig;
    @SerializedName("sqlConn")
    public SqlConnectionConfig sqlConnectionConfig;
    @SerializedName("activeUserCleaner")
    public ActiveClassCleanerConfig activeClassCleanerConfig;
    @SerializedName("logger")
    public LoggerConfig loggerConfig;

    private Config(String version, TcpServerConfig tcpServerConfig, UdpServerConfig udpServerConfig,
                   SqlConnectionConfig sqlConnectionConfig, ActiveClassCleanerConfig activeClassCleanerConfig,
                   LoggerConfig loggerConfig) {
        this.version = version;
        this.tcpServerConfig = tcpServerConfig;
        this.udpServerConfig = udpServerConfig;
        this.sqlConnectionConfig = sqlConnectionConfig;
        this.activeClassCleanerConfig = activeClassCleanerConfig;
        this.loggerConfig = loggerConfig;
    }

    public static class TcpServerConfig {
        public TcpServerConfig(String host, int port) {
            this.host = host;
            this.port = port;
        }
        
        @SerializedName("host")
        public String host;

        @SerializedName("port")
        public int port;
    }
    
    public static class UdpServerConfig {
        public UdpServerConfig(String host, int port) {
            this.host = host;
            this.port = port;
        }

        @SerializedName("host")
        public String host;

        @SerializedName("port")
        public int port;
    }

    public static class SqlConnectionConfig {
        public SqlConnectionConfig(String host, String user, String password, String dbName) {
            this.host = host;
            this.user = user;
            this.password = password;
            this.dbName = dbName;
        }

        @SerializedName("host")
        public String host;

        @SerializedName("user")
        public String user;

        @SerializedName("pswd")
        public String password;

        @SerializedName("name")
        public String dbName;
    }

    public static class ActiveClassCleanerConfig {
        public ActiveClassCleanerConfig(int cleaningSleep, int userTimeout) {
            this.cleaningSleep = cleaningSleep;
            this.userTimeout = userTimeout;
        }
        
        @SerializedName("cleaningSleep")
        public int cleaningSleep;
        
        @SerializedName("userTimeout")
        public int userTimeout;
    }

    public static class LoggerConfig {
        public LoggerConfig(boolean isFileEnabled, boolean isDefaultLogFile, String filePath, boolean isConsoleEnabled) {
            this.isFileEnabled = isFileEnabled;
            this.isDefaultLogFile = isDefaultLogFile;
            this.filePath = filePath;
            this.isConsoleEnabled = isConsoleEnabled;
        }

        @SerializedName("file")
        public boolean isFileEnabled;

        @SerializedName("defaultLogFile")
        public boolean isDefaultLogFile;

        @SerializedName("filePath")
        public String filePath;

        @SerializedName("console")
        public boolean isConsoleEnabled;
    }

    public static Config loadConfig() {
        try {
            FileReader fr = new FileReader(configFile);
            Config c = gson.fromJson(fr, Config.class);

            if (c != null) {
                fr.close();

                Main.getLogger().logMsg("Config file loaded correctly!");
                return c;
            } else {
                throw new JsonParseException("Could Not Parse Json");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Config defaultConfig() {
        String ver = "1.0-alpha";
        TcpServerConfig tcp = new TcpServerConfig("localhost", 6969);
        UdpServerConfig udp = new UdpServerConfig("localhost", 6969);
        SqlConnectionConfig sql = new SqlConnectionConfig("localhost", "", "", "");
        ActiveClassCleanerConfig accc = new ActiveClassCleanerConfig(150000, 150000);
        LoggerConfig log = new LoggerConfig(false, false, "", true);

        try {
            Config c = new Config(ver, tcp, udp, sql, accc, log);

            if (!configFileExists()) {
                configFile.getParentFile().mkdirs();
                configFile.createNewFile();
            }

            PrintWriter pw = new PrintWriter(configFile);
            pw.println(gson.toJson(c, Config.class));

            Main.getLogger().logMsg("Default config file was successfully created!");

            pw.close();
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean configFileExists() {
        return configFile.exists();
    }
}
