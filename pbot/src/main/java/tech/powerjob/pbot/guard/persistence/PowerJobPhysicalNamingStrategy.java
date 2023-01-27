package tech.powerjob.pbot.guard.persistence;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.boot.model.naming.CamelCaseToUnderscoresNamingStrategy;
import org.hibernate.boot.model.naming.Identifier;
import org.hibernate.engine.jdbc.env.spi.JdbcEnvironment;

/**
 * description
 *
 * @author tjq
 * @since 2023/1/25
 */
public class PowerJobPhysicalNamingStrategy extends CamelCaseToUnderscoresNamingStrategy {

    /**
     * 映射物理表名称，如：把实体表 AppInfoDO 的 DO 去掉，再加上表前缀
     *
     * @param name            实体名称
     * @param jdbcEnvironment jdbc环境变量
     * @return 映射后的物理表
     */
    @Override
    public Identifier toPhysicalTableName(Identifier name, JdbcEnvironment jdbcEnvironment) {

        String tablePrefix = "";

        String text = name.getText();
        String noDOText = StringUtils.endsWithIgnoreCase(text, "do") ? text.substring(0, text.length() - 2) : text;
        String newText = StringUtils.isEmpty(tablePrefix) ? tablePrefix + noDOText : noDOText;
        return super.toPhysicalTableName(new Identifier(newText, name.isQuoted()), jdbcEnvironment);
    }
}
