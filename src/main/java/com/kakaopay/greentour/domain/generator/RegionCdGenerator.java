package com.kakaopay.greentour.domain.generator;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SharedSessionContractImplementor;
import org.hibernate.id.IdentifierGenerator;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Slf4j
public class RegionCdGenerator implements IdentifierGenerator {

    @Override
    public Serializable generate(SharedSessionContractImplementor session, Object object)
            throws HibernateException {

        String prefix = "reg";
        Connection connection = session.connection();
        try {
            Statement statement = connection.createStatement();
            ResultSet rs = statement.executeQuery("SELECT count(region_cd) AS Id FROM REGION");

            if (rs.next()) {
                int id = rs.getInt(1) + 1;
                return prefix + StringUtils.leftPad("" + id, 5, '0');
            }
        } catch (SQLException e) {
            log.error("failed to generate regionCd");
            e.printStackTrace();
        }
        return null;
    }
}