package com.synisys.msggen.implementations;

import com.synisys.msggen.domains.Message;
import com.synisys.msggen.interfaces.ConnectionConfig;
import com.synisys.msggen.interfaces.Dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * <b>Author:</b> David Shahbazyan <br/>
 * <b>Date:</b> 7/16/15 <br/>
 * <b>Time:</b> 6:05 PM <br/>
 */
public abstract class DaoImpl implements Dao {
    protected ConnectionConfig connectionConfig;

    public DaoImpl(ConnectionConfig connectionConfig) {
        this.connectionConfig = connectionConfig;
    }

    /**
     *
     * @param query     The none formatted query.
     * @param params    A <code>key - value</code> pair.
     * @return
     */
    public String setQueryParams(String query, Map<String, Object> params) {
        String newQuery = query;
        for (Map.Entry<String, Object> entry : params.entrySet()) {
            if (entry.getValue() != null) {
                newQuery = newQuery.replaceAll(entry.getKey(), entry.getValue().toString());
            }
        }
        return newQuery;
    }

    @Override
    public List<String> loadSchemaNames() throws SQLException {
        return null;
    }

    @Override
    public List<Message> loadMessages(Map<String, Object> params) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = setQueryParams(connectionConfig.getSqlQuery().getLoadMessages(), params);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeQuery();
            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                messages.add(new Message(resultSet.getInt(1), resultSet.getString(2)));
            }
        } finally {
            if (connection != null) connection.close();
            if (preparedStatement != null) preparedStatement.close();
            if (resultSet != null) resultSet.close();
        }
        return messages;
    }

    @Override
    public List<Message> loadEmptyMessages(Map<String, Object> params) throws SQLException {
        List<Message> messages = new ArrayList<>();
        String query = setQueryParams(connectionConfig.getSqlQuery().getLoadUnusedMessageIds(), params);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeQuery();
            resultSet = preparedStatement.getResultSet();
            while (resultSet.next()) {
                messages.add(new Message(resultSet.getInt(1)));
            }
        } finally {
            if (connection != null) connection.close();
            if (preparedStatement != null) preparedStatement.close();
            if (resultSet != null) resultSet.close();
        }
        return messages;
    }

    @Override
    public void generateNewEmptyMessages(Map<String, Object> params) throws SQLException {
        String query = setQueryParams(connectionConfig.getSqlQuery().getCreateEmptyMessages(), params);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } finally {
            if (connection != null) connection.close();
            if (preparedStatement != null) preparedStatement.close();
        }
    }

    @Override
    public void removeUnusedMessages(Map<String, Object> params) throws SQLException {
        String query = setQueryParams(connectionConfig.getSqlQuery().getRemoveUnusedMessages(), params);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } finally {
            if (connection != null) connection.close();
            if (preparedStatement != null) preparedStatement.close();
        }
    }

    @Override
    public void transferMessage(Map<String, Object> params) throws SQLException {
        String query = setQueryParams(connectionConfig.getSqlQuery().getUpdateMessage(), params);
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        try {
            connection = connectionConfig.getDataSource().getConnection();
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.execute();
        } finally {
            if (connection != null) connection.close();
            if (preparedStatement != null) preparedStatement.close();
        }
    }
}
