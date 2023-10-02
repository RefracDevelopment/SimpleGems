package me.refracdevelopment.simplegems.manager.data.sql;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}