package com.asterlsker.housepit.core.config

import org.hibernate.boot.model.naming.Identifier
import org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment

class JpaNamingStrategy : PhysicalNamingStrategyStandardImpl() {
    val PREFIX: String = "tbl_"

    override fun toPhysicalTableName(name: Identifier, context: JdbcEnvironment?): Identifier {
        val prefixedTableName: Identifier = convertToSnakeLowerCase(PREFIX, name)
        return super.toPhysicalTableName(prefixedTableName, context)
    }

    override fun toPhysicalSequenceName(name: Identifier, context: JdbcEnvironment?): Identifier {
        val prefixedSequenceName: Identifier = convertToSnakeLowerCase(PREFIX, name)
        return super.toPhysicalSequenceName(prefixedSequenceName, context)
    }

    override fun toPhysicalColumnName(name: Identifier, context: JdbcEnvironment?): Identifier {
        val snakeLowerCaseName: Identifier = convertToSnakeLowerCase(name)
        return super.toPhysicalColumnName(snakeLowerCaseName, context)
    }

    // fun
    fun convertToSnakeLowerCase(name: Identifier): Identifier {
        val regex = "(?<=[a-zA-Z])[A-Z]".toRegex()
        val newName = regex
            .replace(name.text) { "_${it.value}" }
            .lowercase()
        return Identifier(newName, name.isQuoted)
    }

    fun convertToSnakeLowerCase(prefix: String, name: Identifier): Identifier {
        val regex = "(?<=[a-zA-Z])[A-Z]".toRegex()
        val newName = regex
            .replace(name.text) { "_${it.value}" }
            .lowercase()

        return Identifier(prefix + newName, name.isQuoted)
    }
}