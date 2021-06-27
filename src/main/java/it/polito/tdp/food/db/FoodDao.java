package it.polito.tdp.food.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import it.polito.tdp.food.model.Condiment;
import it.polito.tdp.food.model.Correlazione;
import it.polito.tdp.food.model.Food;
import it.polito.tdp.food.model.Portion;

public class FoodDao {
	public List<Food> listAllFoods(){
		String sql = "SELECT * FROM food" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Food> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Food(res.getInt("food_code"),
							res.getString("display_name")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<Condiment> listAllCondiments(){
		String sql = "SELECT * FROM condiment" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Condiment> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Condiment(res.getInt("condiment_code"),
							res.getString("display_name"),
							res.getDouble("condiment_calories"), 
							res.getDouble("condiment_saturated_fats")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public List<Portion> listAllPortions(){
		String sql = "SELECT * FROM portions" ;
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			List<Portion> list = new ArrayList<>() ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					list.add(new Portion(res.getInt("portion_id"),
							res.getDouble("portion_amount"),
							res.getString("portion_display_name"), 
							res.getDouble("calories"),
							res.getDouble("saturated_fats"),
							res.getInt("food_code")
							));
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return list ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}

	}
	
	public List<String> getAllVertex(int calorie){
		List<String> vertici = new ArrayList<>();
		
		String sql ="SELECT p.portion_display_name "
				+ "FROM portions p "
				+ "WHERE p.portion_id   IN ( SELECT pt.portion_id FROM portions pt WHERE pt.portion_display_name = p.portion_display_name AND pt.calories< ?)"
				+ "GROUP BY p.portion_display_name "
				+ "HAVING COUNT(p.portion_id) > 0 ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setInt(1, calorie);
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					vertici .add(res.getString("portion_display_name"));
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return vertici ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
	}
	
	public Correlazione getArchi(String porzione1, String porzione2){
		//List<Correlazione> result = new ArrayList<>();
		Correlazione result =null;
		
		String sql= "SELECT p1.portion_display_name AS tipo1, p2.portion_display_name AS tipo2, COUNT(DISTINCT p1.food_code) AS peso "
				+ "FROM portions p1, portions p2 "
				+ "WHERE p1.food_code = p2.food_code AND p1.portion_display_name=? AND p2.portion_display_name=? "
				+ "GROUP BY tipo1, tipo2 ";
		
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			st.setString(1, porzione1);
			st.setString(2, porzione2);
			
			ResultSet res = st.executeQuery() ;
			
			if(res.next()) {
				try {
					
				result = new Correlazione(res.getString("tipo1"), res.getString("tipo2"), res.getInt("peso"));	
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

	public List<Correlazione> creaArchi(List<String> vertici) {
		List<Correlazione> result = new ArrayList<>();
		String sql="SELECT p1.portion_display_name AS tipo1, p2.portion_display_name AS tipo2, COUNT(p1.food_code) AS peso "
				+ "FROM portions p1, portions p2 "
				+ "WHERE p1.food_code = p2.food_code AND p1.portion_display_name <p2.portion_display_name "
				+ "GROUP BY tipo1, tipo2";
		try {
			Connection conn = DBConnect.getConnection() ;

			PreparedStatement st = conn.prepareStatement(sql) ;
			
			ResultSet res = st.executeQuery() ;
			
			while(res.next()) {
				try {
					
					
					if(vertici.contains(res.getString("tipo1")) && vertici.contains(res.getString("tipo2"))) {
						Correlazione c = new Correlazione(res.getString("tipo1"), res.getString("tipo2"), res.getInt("peso"));
					    result.add(c);
					
					}
						
					
				
					
					
				} catch (Throwable t) {
					t.printStackTrace();
				}
			}
			
			conn.close();
			return result ;

		} catch (SQLException e) {
			e.printStackTrace();
			return null ;
		}
		
	}

}
