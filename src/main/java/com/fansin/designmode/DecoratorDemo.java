package com.fansin.designmode;

import com.xiaoleilu.hutool.util.StrUtil;

import java.util.Map;
import java.util.Properties;

/**
 * Created with IntelliJ IDEA.
 *
 * @author fansin
 * @version 1.0
 * @date 17 -12-25 下午11:23
 */
public class DecoratorDemo {
    /*
     *装饰者模式:
     * 跟建造者模式不同的是,对象的生成过程不是唯一的,可以被0个或多个装饰者修饰.
     * 其思想是动态的为对象添加新功能.
     * 应用场景:
     * 在jdk的各种流中,这个模式用的很多,比如为普通的流提供缓存的BufferedXXX流
     * 在实际项目中的应用,需求:动态生成update的sql脚本,其中条件部分
     * where field=value and .....  field in (xxx,yyy,zzz) group by field1,field2... order by field1,field2...asc|desc
     * 这里只是简单演示,并没有覆盖所有的情景
     *
     */

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        AbstractUpdateSql updateSQL = new DynamicUpdateSql();
        Properties properties = new Properties();
        properties.setProperty("name","testDecoratorMode!");
        properties.setProperty("demo","由装饰模式生成的sql执行!");
        updateSQL.updateSQL("test.replication_table",properties);
        System.out.println("SQL:"+updateSQL.toString());
        WhereConditionSql whereSQL = new WhereConditionSql(updateSQL);
        properties.clear();
        properties.setProperty("name","pool-1-thread-1");
        properties.setProperty("demo","1494523774786");
        whereSQL.updateWhere(properties,"=");
        System.out.println("Where SQL:"+whereSQL.toString());
    }
}

/**
 * The type Abstract update sql.
 */
abstract class AbstractUpdateSql {

    /**
     * Update sql.
     *
     * @param table      the table
     * @param properties the properties
     */
    abstract void updateSQL(String table,Properties properties);

    /**
     * Update sql.
     *
     * @param properties the properties
     * @param operator   the operator
     * @param delimiter  the delimiter
     */
    abstract void updateSQL(Properties properties,String operator,String delimiter);

    /**
     * Update sql.
     *
     * @param buffer     the buffer
     * @param properties the properties
     * @param operator   the operator
     * @param delimiter  the delimiter
     */
    public void updateSQL(StringBuffer buffer,Properties properties,String operator,String delimiter){
        if (properties == null && StrUtil.isNotBlank(operator)){
            buffer.append(String.format(" %s ",operator));
            return;
        }

        for (Map.Entry<Object, Object> entry : properties != null ? properties.entrySet() : null) {
            String sqlUnit = String.format(" %1$s %2$s '%3$s' %4$s ",entry.getKey(),operator,entry.getValue(),delimiter);
            sqlUnit = sqlUnit.replaceAll("\\s+"," ");
            buffer.append(sqlUnit);
        }
        buffer.setLength(buffer.length()-delimiter.length()-1);
    }

}

/**
 * The type Dynamic update sql.
 * 基本组件
 */
class DynamicUpdateSql extends AbstractUpdateSql {
    private StringBuffer cache;

    /**
     * Instantiates a new Dynamic update sql.
     */
    public DynamicUpdateSql() {
    }

    @Override
    public String toString() {
        return cache.toString();
    }

    /**
     * 创建 update table set xx=ccc 语句
     * @param table
     * @param properties
     */
    @Override
    void updateSQL(String table,Properties properties) {
        cache = new StringBuffer("UPDATE ").append(table).append(" SET ");
        for (Map.Entry<Object, Object> entry : properties.entrySet()) {
            cache.append(entry.getKey()).append(" = '").append(entry.getValue()).append("' , ");
        }
        cache.setLength(cache.length()-2);
    }

    /**
     * 提供 自定义参数设置
     * @param properties
     * @param operator
     * @param delimiter
     */
    @Override
    void updateSQL(Properties properties,String operator,String delimiter) {
        updateSQL(cache,properties,operator,delimiter);
    }
}

/**
 * The type Condition sql.
 */
class ConditionSql extends AbstractUpdateSql {

    private AbstractUpdateSql updateSQL;

    /**
     * Instantiates a new Condition sql.
     *
     * @param updateSQL the update sql
     */
    public ConditionSql(AbstractUpdateSql updateSQL) {
        this.updateSQL = updateSQL;
    }

    @Override
    void updateSQL(String table, Properties properties) {
        updateSQL.updateSQL(table,properties);
    }

    @Override
    void updateSQL(Properties properties,String operator,String delimiter) {
        updateSQL.updateSQL(properties,operator,delimiter);
    }

    @Override
    public String toString() {
        return updateSQL.toString();
    }
}

/**
 * The type Where condition sql.
 */
class WhereConditionSql extends ConditionSql {

    /**
     * Instantiates a new Where condition sql.
     *
     * @param updateSQL the update sql
     */
    public WhereConditionSql(AbstractUpdateSql updateSQL) {
        super(updateSQL);
        updateWhere();
    }

    private  void updateWhere(){
        updateSQL(null,"WHERE","");
    }

    /**
     * Update where.
     *
     * @param properties the properties
     * @param operator   the operator
     */
    public  void  updateWhere(Properties properties,String operator){
        updateSQL(properties,operator,"AND");
    }

}

