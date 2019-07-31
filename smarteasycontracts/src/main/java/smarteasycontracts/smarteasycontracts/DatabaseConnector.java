package smarteasycontracts.smarteasycontracts;
import java.net.ConnectException;
import java.sql.*;
/**
 * @author Javier Iglesias Sanz
 * @version 1.0
 * @since 2019-02-21
 *	This class is gonna make the connections with the DB
 */
public class DatabaseConnector {
	Connection  conn;
	public DatabaseConnector() {
		try {
		/*Raspberry*/
		 Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(
					"jdbc:mysql://localhost:3306/SNEC?autoReconnect=true&useSSL=false",
					"root", "8111992j");
					
			/*Azure DB
			 * String url = "jdbc:mariadb://snec.mariadb.database.azure.com:3306/snec";
			conn = DriverManager.getConnection(url, "snec@snec", "Smarteasycontracts2019");*/
			if(conn == null) throw new ConnectException();
		}catch (Exception ex) {
			System.out.println("No se pudo establedcer la conexion con la BBDD, compruebe que esta operativa.");
			ex.printStackTrace();
		}
	}
	public enum ConditionOperator {
		OR("OR"), AND("AND"), EQ("=");
		private String condition;
		ConditionOperator(String data) {
			this.condition = data;
		}
		public String toString() {
			return this.condition;
		}
	}
	public ResultSet Select(String table, ConditionOperator operator, String field, String value) {
		try	{
			if(conn == null) throw new ConnectException();
			String _subquery = "SELECT * FROM " + table + " WHERE "+ 
					field + operator.toString() + "\'"+ value + "\'";
			Statement query = conn.createStatement();
			ResultSet results = query.executeQuery(_subquery);
			if(results.next())
				return results;
			return null;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	public int Update(String table, String column,String newValue, 
			String whereColumn, String whereValue, ConditionOperator condition) {
		int results=0;
		try	{
			if(!Ping()) return -1;
			String updateQuery = "UPDATE " + table +" SET "
					+ column +" = '"+ newValue + "' WHERE " 
					+ whereColumn +condition.toString() + "'"+whereValue+"'";
			Statement query = conn.createStatement();
			results = query.executeUpdate(updateQuery);
		} catch (Exception ex) {
			ex.printStackTrace();
			return -1;
		}
		System.out.println("Contraseña de la wallet actualizada " + results);
		return results;
	}
	public int Insert(String table, String[] columns, String[] values) {
		try	{
			if(!conn.isValid(1000)) return -1;
			String introQuery = "INSERT INTO " + table +" (";
			String columnsSubQuery = "", valuesSubquery = "";
			for(int i = 0; i < columns.length; i++) {
				columnsSubQuery += columns[i].toUpperCase();
				valuesSubquery += "\""+values[i]+"\"";
				if(i < (columns.length -1)) {
					columnsSubQuery += ",";
					valuesSubquery += ",";
				}
			}
			String subQuery = introQuery + columnsSubQuery + ") VALUES (" + valuesSubquery + ")";
			Statement query = conn.createStatement();
			int results = query.executeUpdate(subQuery);
			if(results != -1)
				return results;
			return 0;
		} catch (Exception ex) {
			System.err.println("Insert DB error:");
			ex.printStackTrace();
			return -1;
		}
	}
	/**
	 * Delete the value from the table.
	 * */
	public boolean Delete(String table, String column,String value, ConditionOperator operator) {
		try	{
			if(Ping()) return false;
			String fullQuery = "DELETE FROM " + table + " WHERE "+ column + operator + value;
			Statement query = conn.createStatement();
			if(query.execute(fullQuery)) {
				conn.commit();
				return true;
			}
			else return false;
		} catch (Exception ex) {
			ex.printStackTrace();
			return false;
		}
	}
	/**
	 * Send a ping to the DB to see if its connect or not.
	 * */
	public boolean Ping() {
		try {
			if(conn.isClosed() || !conn.isValid(1000))
				return false;
		}catch(Exception ex) {
			System.err.println("Databse Connection closed;");
			ex.printStackTrace();
			return false;
		}
		return true;
	}
	public void EndConnection() {
		try {
			conn.close();
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}