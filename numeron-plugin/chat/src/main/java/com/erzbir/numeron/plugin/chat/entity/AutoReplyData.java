package com.erzbir.numeron.plugin.chat.entity;


import com.erzbir.numeron.core.entity.connection.SqlConnection;
import com.erzbir.numeron.utils.NumeronLogUtil;
import com.erzbir.numeron.utils.SqlUtil;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Erzbir
 * @Date: 2022/11/25 17:21
 */
public final class AutoReplyData {
    public static final AutoReplyData INSTANCE = new AutoReplyData();
    private final Map<String, String> answer = new HashMap<>();

    private AutoReplyData() {
        String sql = """
                CREATE TABLE IF NOT EXISTS CHAT(
                KEY TEXT PRIMARY KEY NOT NULL,
                ANSWER TEXT,
                OP_ID BIGINT NOT NULL,
                OP_TIME TEXT NOT NULL
                )
                """;
        try {
            PreparedStatement prepared = SqlConnection.getConnection().prepareStatement(sql);
            SqlUtil.executeUpdateSQL(prepared);
            SqlUtil.closeResource(prepared);
            answer.putAll(init());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, String> getAnswer() {
        return answer;
    }

    public void add(String key, String answer, long opId) {
        this.answer.put(key, answer);
        new Thread(() -> add(key, answer, opId)).start();
    }

    public void remove(String key) {
        answer.remove(key);
        new Thread(() -> {
            try {
                removeQA(key);
            } catch (SQLException e) {
                NumeronLogUtil.logger.error(e);
                e.printStackTrace();
            }
        }).start();
    }

    public boolean exist(String key) {
        return answer.containsKey(key);
    }


    private Map<String, String> init() throws SQLException {
        String sql = "SELECT * FROM ILLEGALS";
        PreparedStatement prepared = SqlConnection.getConnection().prepareStatement(sql);
        ResultSet resultSet = SqlUtil.getResultSet(prepared);
        Map<String, String> ret = new HashMap<>();
        while (resultSet.next()) {
            ret.put(resultSet.getString("KEY"), resultSet.getString("ANSWER"));
        }
        SqlUtil.closeResource(null, prepared, resultSet);
        return ret;
    }

    private boolean addQA(String key, String answer, long opId) throws SQLException {
        String sql = "INSERT INTO CHAT(KEY,ANSWER,OP_ID,OP_TIME) VALUES(?,?,?,?)";
        Object[] params = {
                key, answer, opId, LocalDateTime.now()
        };
        PreparedStatement prepared = SqlConnection.getConnection().prepareStatement(sql);
        int flag = SqlUtil.executeUpdateSQL(prepared, params);
        SqlUtil.closeResource(prepared);
        return flag >= 1;
    }

    private boolean removeQA(String key) throws SQLException {
        String sql = "DELETE FROM CHAT WHERE KEY=?";
        PreparedStatement prepared = SqlConnection.getConnection().prepareStatement(sql);
        prepared.setString(1, key);
        int flag = SqlUtil.executeUpdateSQL(prepared);
        SqlUtil.closeResource(prepared);
        return flag >= 1;
    }
}
