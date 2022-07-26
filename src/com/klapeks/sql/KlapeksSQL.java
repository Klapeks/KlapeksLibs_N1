package com.klapeks.sql;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

import com.klapeks.sql.anno.Column;
import com.klapeks.sql.anno.Limit;
import com.klapeks.sql.anno.Primary;
import com.klapeks.sql.anno.PrimaryConstraint;
import com.klapeks.sql.anno.Table;

public class KlapeksSQL {
	
//	public static void main(String[] args) {
//		try {
//			Class.forName("com.mysql.jdbc.Driver");
//		} catch (ClassNotFoundException e) {
//			System.err.println("Can't find MySQL Driver");
//			throw new RuntimeException(e);
//		}
//		
//		KlapeksSQL db = new KlapeksSQL();
//		db.connect("jdbc:mysql://localhost:3306/klapekstestweb", "root", "root");
//		db.createIfNotExists(TitlePlayer.class);
//		
//		TitlePlayer tp = new TitlePlayer();
//		tp.player = "vladek";
//		tp.category = "test";
//		tp.titles = new ArrayList<>();
//		tp.titles.add("sussye22");
//		tp.titles.add("errra44");
//		tp.titles.add("eeee");
//		tp.newtime = 68;
//		
//		db.updateOrInsert(tp);
////		TitlePlayer tp = db.selectFirst(TitlePlayer.class, db.where("`player` = ?", "skepalk"));
////		System.out.println(tp.player);
////		System.out.println(tp.category);
////		System.out.println(tp.titles);
////		System.out.println(tp.newtime);
//	}
	
	public Where where(String query, Object... placeholders) {
		return new Where(query, placeholders);
	}
	public <T> T selectFirst(Class<T> clazz, Where where) {
		List<T> list = select(clazz, where);
		if (list == null || list.isEmpty()) return null;
		return list.get(0);
	}
	public <T> List<T> select(Class<T> clazz, Where where) {
		Table table = clazz.getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(clazz + " is not Table");
		StringBuilder query = new StringBuilder();
		query.append("SELECT * FROM `");
		query.append(table.value());
		query.append("` WHERE ");
		query.append(where.query);
		if (where.limit > 0) {
			query.append(" LIMIT ");
			query.append(where.limit);
		}
		try {
			System.out.println(query.toString());
			PreparedStatement st = connection.prepareStatement(query.toString());
			int index = 0;
			for (Object o : where.placeholders) {
				st.setObject(++index, o);
			}
			ResultSet result = st.executeQuery();
			List<T> list = new ArrayList<>();
			while (result.next()) {
				list.add(generateFromResultSet(clazz, result));
				while(list.contains(null)) list.remove(null);
			}
			return list;
		} catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}
	@SuppressWarnings("unchecked")
	private <T> T generateFromResultSet(Class<T> clazz, ResultSet result) {
		try {
			T t = clazz.getConstructor().newInstance();
			for (Field field : clazz.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column==null) continue;
				field.setAccessible(true);
				Object o = result.getObject(column.value());
				if (field.getType().isAssignableFrom(List.class)) {
					String[] g = o.toString().replace("\r", "").split("\n");
					o = new ArrayList<Object>();
					for (String s : g) {
						((List<Object>) o).add(s);
					}
				}
				field.set(t, o);
			}
			return t;
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}
//		System.out.println(result.getObject("player"));
//		System.out.println(result.getObject("category"));
//		System.out.println(result.getObject("titles"));
//		System.out.println();
//		return null;
	}
	public void updateOrInsert(Object object, Where where) {
		Table table = object.getClass().getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(object.getClass() + " is not Table");
		where.setLimit(1);
		if (selectFirst(object.getClass(), where)==null) {
			System.out.println("-Insert");
			insert(object);
			return;
		}
		System.out.println("-Update");
		update(object);
	}
	public int update(Object object, Where where) {
		Table table = object.getClass().getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(object.getClass() + " is not Table");
		
		StringBuilder query = new StringBuilder();
		query.append("UPDATE `");
		query.append(table.value());
		query.append("` SET ");
		List<Object> placeholders = new ArrayList<>();
		int index = 0;
		for (Field field : object.getClass().getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column==null) continue;
			if (index++>0) query.append(" , ");
			try {
				field.setAccessible(true);
				Object a = field.get(object);
				query.append("`");
				query.append(column.value());
				query.append("` = ?");
				placeholders.add(convertTo(a));
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		query.append(" WHERE ");
		query.append(where.query);
		for (Object o : where.placeholders) {
			placeholders.add(o);
		}
		System.out.println(query);
		try {
			PreparedStatement st = connection.prepareStatement(query.toString());
			index = 0;
			for (Object o : placeholders) {
				st.setObject(++index, o);
			}
			return st.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeSQLException(e);
		}
	}
	
	public void updateOrInsert(Object object) {
		Table table = object.getClass().getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(object.getClass() + " is not Table");
		updateOrInsert(object, generateWhere(object));
	}
	public void update(Object object) {
		Table table = object.getClass().getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(object.getClass() + " is not Table");
		update(object, generateWhere(object));
	}
	private Where generateWhere(Object object) {
		List<Object> placeholders = new ArrayList<>();
		StringBuilder query = new StringBuilder();
		int index = 0;
		for (Field field : object.getClass().getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column==null) continue;
			if (field.getAnnotation(Primary.class)==null) continue;
			if (index++>0) query.append(" AND ");
			try {
				field.setAccessible(true);
				placeholders.add(convertTo(field.get(object)));
				query.append("`");
				query.append(column.value());
				query.append("` = ?");
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		return new Where(query.toString(), placeholders);
	}

	public int insert(Object object) {
		Table table = object.getClass().getAnnotation(Table.class);
		if (table==null) throw new RuntimeException(object.getClass() + " is not Table");
		
		StringBuilder query = new StringBuilder();
		query.append("INSERT INTO `");
		query.append(table.value());
		query.append("` ( ");
		
		int index = 0;
		for (Field field : object.getClass().getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column==null) continue;
			if (index++>0) query.append(" , ");
			query.append("`");
			query.append(column.value());
			query.append("`");
		}
		query.append(" ) VALUES ( ");
		for (int i = 0; i < index; i++) {
			if (i>0) query.append(" , ");
			query.append("?");
		}
		query.append(" );");
		System.out.println(query);
		try {
			PreparedStatement st = connection.prepareStatement(query.toString());
			index = 0;
			for (Field field : object.getClass().getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column==null) continue;
				index++;
				try {
					field.setAccessible(true);
					st.setObject(index, convertTo(field.get(object)));
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
			System.out.println(st);
			return st.executeUpdate();
//			return 0;
		} catch (SQLException e) {
            throw new RuntimeSQLException(e);
		}
	}

	private Object convertTo(Object o) {
		if (o instanceof List<?>) {
			o = ((List<?>) o).stream().map(s -> s+"").collect(Collectors.joining("\n"));
		}
		return o;
	}

	public void createTableIfNotExists(Class<?> table) {
		if (checkIfTableExists(table)) return;
		createTable(table);
	}
	public void createTable(Class<?> table) {
		StringBuilder query = new StringBuilder();
		query.append("CREATE TABLE `");
		query.append(table.getAnnotation(Table.class).value());
		query.append("` ( ");
		int index = 0;
		for (Field field : table.getDeclaredFields()) {
			Column column = field.getAnnotation(Column.class);
			if (column==null) continue;
			if (index++>0) query.append(" , ");
			query.append("`");
			query.append(column.value());
			query.append("` ");
			if (field.getType() == Integer.class) query.append("INT");
			if (field.getType() == Long.class) query.append("BIGINT");
			if (field.getType() == int.class) query.append("INT");
			if (field.getType() == long.class) query.append("BIGINT");
			if (field.getType() == String.class) {
				Limit limit = field.getAnnotation(Limit.class);
				if (limit!=null) {
					query.append("VARCHAR(");
					query.append(limit.value());
					query.append(")");
				} else query.append("TEXT");
			}
			else if (field.getType().isAssignableFrom(List.class)) {
				query.append("TEXT");
			}
			query.append(" NOT NULL");
		}
		query.append(" );");
		System.out.println(query);
		try {
			this.connection.prepareStatement(query.toString()).executeUpdate();
		} catch (SQLException e) {
            throw new RuntimeSQLException(e);
		}
		updateTable(table);
	}
	private void updateTable(Class<?> table) {
		PrimaryConstraint pc = table.getAnnotation(PrimaryConstraint.class);
		Table t = table.getAnnotation(Table.class);
		if (pc!=null) {
			StringBuilder query = new StringBuilder();
			query.append("ALTER TABLE `");
			query.append(t.value());
			query.append("` ADD CONSTRAINT ");
			query.append(pc.value());
			query.append(" PRIMARY KEY ( ");
			int index = 0;
			for (Field field : table.getDeclaredFields()) {
				Column column = field.getAnnotation(Column.class);
				if (column==null) continue;
				if (field.getAnnotation(Primary.class)==null) continue;
				if (index++ > 0) query.append(" , ");
				query.append("`");
				query.append(column.value());
				query.append("`");
			}
			query.append(" );");
			System.out.println(query);
			try {
				this.connection.prepareStatement(query.toString()).executeUpdate();
			} catch (SQLException e) {
	            throw new RuntimeSQLException(e);
			}
		}
		
	}

	public boolean checkIfTableExists(Class<?> table) {
		try {
			Table t = table.getAnnotation(Table.class);
            String query = "SELECT count(*) FROM information_schema.tables WHERE table_name = '" 
            		+ t.value() + "' AND table_schema = '" + connection.getCatalog() + "' LIMIT 1;";
            ResultSet rs = this.connection.prepareStatement(query).executeQuery();
            int records = 0;
            if (rs.next()) records = rs.getInt(1);
            return records > 0;
        } catch (SQLException e) {
            throw new RuntimeSQLException(e);
        }
	}
	
	private Connection connection;

	public void connect(String url, Properties properties) {
		disconnect();
		try {
			connection = DriverManager.getConnection(url, properties);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public void connect(String url, String username, String password) {
		Properties properties = new Properties();
		properties.setProperty("user", username);
		properties.setProperty("password", password);
		properties.setProperty("characterEncoding", "utf8");
		connect(url, properties);
	}
	public void disconnect() {
		if (connection == null) return;
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connection = null;
	}
}
