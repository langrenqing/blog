package calcite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.apache.calcite.adapter.jdbc.JdbcSchema;
import org.apache.calcite.jdbc.CalciteConnection;
import org.apache.calcite.schema.SchemaPlus;

public class TestCalcite {

	public static void main(String[] args) throws ClassNotFoundException, SQLException {
		Class.forName("org.apache.calcite.jdbc.Driver");
		Connection connection = DriverManager.getConnection("jdbc:calcite:");
		CalciteConnection calciteConnection = connection.unwrap(CalciteConnection.class);
		
		SchemaPlus rootSchema = calciteConnection.getRootSchema();
		final DataSource ds = JdbcSchema.dataSource("jdbc:mysql://localhost/world"
				, "com.mysql.jdbc.Driver", "root", "123456");
		// 这两个world 好像都是对应schema
		rootSchema.add("WORLD", JdbcSchema.create(rootSchema, "WORLD", ds, null, null));
		
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("select * from WORLD.\"city\" limit 1");
		print(resultSet);
		resultSet.close();
		statement.close();
		connection.close();

	}

	private static void print(ResultSet resultSet) throws SQLException {
		ResultSetMetaData rsmd = resultSet.getMetaData();
		int columnCount = rsmd.getColumnCount();
		StringBuilder sb = null;
		while (resultSet.next()) {
			sb = new StringBuilder();
			for (int i = 0; i < columnCount; i++) {
				sb.append(resultSet.getObject(i + 1)).append("\t");
			}
			System.out.println(sb);
		}
	}

}
