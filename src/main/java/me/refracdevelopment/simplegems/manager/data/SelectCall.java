package me.refracdevelopment.simplegems.manager.data;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}