/**
 * Disbordissimo: a voice chat application.
 * Copyright (C) <2026>  authors: YTC_Fmarco98; Harly
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package we.ytc.disbordissimo.server.internal.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;

import java.io.*;

public class Operations {
    private static final String CONFIG_FILEPATH = ".config/config.json";

    public static Config load() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileReader reader = null;

        System.out.println("Config filepath: "+CONFIG_FILEPATH);

        try {
            reader = new FileReader(CONFIG_FILEPATH);
            Config conf = gson.fromJson(reader, Config.class);

            if (conf == null) {
                throw new JsonParseException("Could Not Parse Json");
            }

            System.out.println("Config file loaded correctly!");
            System.out.println("Init config validation");

            if(!validate(conf)) {
                System.out.println("Invalid config");
                try {
                    System.in.read();
                } catch (IOException ex) {}
                System.exit(1); //TODO: custom exit codes
            }

            System.out.println("Valid config loaded");
            return conf;

        } catch (FileNotFoundException e) {
            System.out.println("Config file not found");
            genDefault();

            try {
                System.in.read();
            } catch (IOException ex) {}
            System.exit(1); //TODO: custom exit codes
        } finally {
            try {
                if(reader != null) reader.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return null; //Pk lo richiede
    }

    public static boolean validate(Config conf) {
        boolean isValid = true;

        if(conf.tcpServer.port <= 0 || conf.tcpServer.port >= 65536) {
            System.out.println("tcpServer.port must be a valid port [1 : 65535]");
            isValid = false;
        }

        if(conf.webrtc.stunUrl.isBlank()) {
            System.out.println("webrtc.stunUrl mustn't be blank");
            isValid = false;
        }
        if(conf.webrtc.janusUrl.isBlank()) {
            System.out.println("webrtc.janusUrl mustn't be blank");
            isValid = false;
        }

        if(conf.sql.host.isBlank()) {
            System.out.println("sql.host mustn't be blank");
            isValid = false;
        }
        if(conf.sql.port <= 0 || conf.sql.port >= 65536) {
            System.out.println("sql.port must be a valid port [1 : 65535]");
            isValid = false;
        }
        if(conf.sql.user.isBlank()) {
            System.out.println("sql.user mustn't be blank");
            isValid = false;
        }
        if(conf.sql.dbName.isBlank()) {
            System.out.println("sql.name mustn't be blank");
            isValid = false;
        }

        if(conf.misc.roomCleaningInterval <= 0) {
            System.out.println("misc.roomCleaningInterval must be >0");
            isValid = false;
        }

        return isValid;
    }

    public static void genDefault() {
        final Gson gson = new GsonBuilder().setPrettyPrinting().create();
        File confFile = new File(CONFIG_FILEPATH);

        System.out.println("Generating a default config");

        try {
            confFile.getParentFile().mkdir();
            confFile.createNewFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Config defaultConf = new Config();

        try(PrintWriter writer = new PrintWriter(confFile)) {
                writer.println(gson.toJson(defaultConf, Config.class));
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }

        System.out.println("Default config successfully generated");
    }
}
