package me.refracdevelopment.simplegems.plugin.manager.database;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}