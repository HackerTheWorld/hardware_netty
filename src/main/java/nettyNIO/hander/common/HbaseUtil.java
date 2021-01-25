package nettyNIO.hander.common;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Admin;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptor;
import org.apache.hadoop.hbase.client.ColumnFamilyDescriptorBuilder;
import org.apache.hadoop.hbase.client.Connection;
import org.apache.hadoop.hbase.client.ConnectionFactory;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Table;
import org.apache.hadoop.hbase.client.TableDescriptorBuilder;

public class HbaseUtil {

    private Configuration configuration = HBaseConfiguration.create();
    private final String BASE_INFO = "baseInfo";

    public HbaseUtil(String hbaseServer){
        configuration.set("hbase.zookeeper.quorum", hbaseServer);
    }

    //获得与hbase的连接
    private Connection getConnection() throws Exception {
        Connection connection = ConnectionFactory.createConnection(configuration);
        return connection;
    }

    //插入数据的方法
    public void insertList(String tableNameStr,String columnFamily,List<Map<String,Object>> list) throws Exception {
        TableName tableName = TableName.valueOf(tableNameStr);
        try(Connection con = getConnection();
            Admin admin = con.getAdmin();
            Table table = con.getTable(tableName);){
            
            Set<String> hashSet = new HashSet<String>();
            hashSet.add(BASE_INFO);
            createdTable(admin, tableNameStr, hashSet);
            
            for (Map<String,Object> map : list) {
                String id = String.valueOf(map.get("uuid"));
                Put put = new Put(id.getBytes());
                for(Map.Entry<String,Object> entry:map.entrySet()){
                    put.addColumn(columnFamily.getBytes(), entry.getKey().getBytes(), String.valueOf(entry.getValue()).getBytes());
                }
                table.put(put);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    
    }

    /**
     * 如果表不存在创建 admin由调用方关闭
     * @param admin 账户
     * @param tableName 表名
     * @param columns 列簇
     */
    public void createdTable(Admin admin,String tableName,Set<String> columns){
        TableName table = TableName.valueOf(tableName);
        //如果表不存在就新建
        try{
            if(!admin.tableExists(table)){
                //创建表描述对象
                TableDescriptorBuilder  tableBuilder = TableDescriptorBuilder.newBuilder(table);
                //创建列族
                for(String key:columns){
                    ColumnFamilyDescriptor of = ColumnFamilyDescriptorBuilder.of(key);
                    tableBuilder.setColumnFamily(of);
                }
                admin.createTable(tableBuilder.build());
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }

}
