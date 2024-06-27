package me.refracdevelopment.simplegems.managers.data;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface SelectCall {

    void call(ResultSet resultSet) throws SQLException;
}