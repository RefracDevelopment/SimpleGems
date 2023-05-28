package me.refracdevelopment.simplegems.database;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}