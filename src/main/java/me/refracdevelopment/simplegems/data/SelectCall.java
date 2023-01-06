package me.refracdevelopment.simplegems.data;

import java.sql.ResultSet;

public interface SelectCall {

    void call(ResultSet resultSet);
}