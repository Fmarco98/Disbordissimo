package we.ytc.disbordissimo.server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.SerializedName;

import java.io.*;

/**
 * <h1>Config class</h1>
 * Used to read and write the server's config with the help of {@link com.google.gson.Gson}
 */
public class Config {
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static final String CONFIG_FILE_PATH = "config/config.json";
    private static final File configFile = new File(CONFIG_FILE_PATH);

    @SerializedName("version")
    public String version;

    @SerializedName("tcpServer")
    public TcpServerConfig tcpServerConfig;
    @SerializedName("kcpServer")
    public KcpServerConfig kcpServerConfig;
    @SerializedName("sqlConn")
    public SqlConnectionConfig sqlConnectionConfig;
    @SerializedName("activeUserCleaner")
    public ActiveClassCleanerConfig activeClassCleanerConfig;
    @SerializedName("logger")
    public LoggerConfig loggerConfig;

    private Config() {}

    private Config(String version, TcpServerConfig tcpServerConfig, KcpServerConfig udpServerConfig,
                   SqlConnectionConfig sqlConnectionConfig, ActiveClassCleanerConfig activeClassCleanerConfig,
                   LoggerConfig loggerConfig) {
        this.version = version;
        this.tcpServerConfig = tcpServerConfig;
        this.kcpServerConfig = udpServerConfig;
        this.sqlConnectionConfig = sqlConnectionConfig;
        this.activeClassCleanerConfig = activeClassCleanerConfig;
        this.loggerConfig = loggerConfig;
    }

    /**
     * Class that represents the tcpServer JSON object
     */
    public static class TcpServerConfig {
        private TcpServerConfig() {}

        public TcpServerConfig(int port) {
            this.port = port;
        }

        @SerializedName("port")
        public int port;
    }

    /**
     * Class that represents the kcpServer JSON object
     */
    public static class KcpServerConfig {
        private KcpServerConfig() {}

        public KcpServerConfig(int port) {
            this.port = port;
        }

        @SerializedName("port")
        public int port;
    }

    /**
     * Class that represents the sqlConn JSON object
     */
    public static class SqlConnectionConfig {
        private SqlConnectionConfig() {}

        public SqlConnectionConfig(String host, int port, String user, String password, String dbName) {
            this.host = host;
            this.port = port;
            this.user = user;
            this.password = password;
            this.dbName = dbName;
        }

        @SerializedName("host")
        public String host;

        @SerializedName("port")
        public int port;

        @SerializedName("user")
        public String user;

        @SerializedName("pswd")
        public String password;

        @SerializedName("name")
        public String dbName;
    }

    /**
     * Class that represents the activeUserCleaner JSON object
     */
    public static class ActiveClassCleanerConfig {
        private ActiveClassCleanerConfig() {}

        public ActiveClassCleanerConfig(int cleaningSleep, int userTimeout) {
            this.cleaningSleep = cleaningSleep;
            this.userTimeout = userTimeout;
        }
        
        @SerializedName("cleaningSleep")
        public int cleaningSleep;
        
        @SerializedName("userTimeout")
        public int userTimeout;
    }

    /**
     * Class that represents the logger JSON object
     */
    public static class LoggerConfig {
        private LoggerConfig() {}

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

    /**
     * Loads the config from the JSON config file usually located in
     * <pre>
     *     {@code $SERVER_ROOT/config/config.json}
     * </pre>
     *
     * @return the {@link Config} class loaded from the JSON file
     */
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

    /**
     * Creates and loads a new config file with some default settings
     *
     * @return the {@link Config} class loaded from the JSON file just created
     */
    public static Config defaultConfig() {
        String ver = "1.0-alpha";
        TcpServerConfig tcp = new TcpServerConfig(10469);
        KcpServerConfig udp = new KcpServerConfig(10469);
        SqlConnectionConfig sql = new SqlConnectionConfig("localhost", 3306, "", "", "");
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

    /**
     * Public method to check if the config file already exists
     * @return {@code true} if the file exists, otherwise {@code false}
     */
    public static boolean configFileExists() {
        return configFile.exists();
    }
}
