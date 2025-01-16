/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Sistem;
    import java.sql.Driver;
    import java.sql.DriverManager;
    import java.sql.Connection;
    import java.sql.ResultSet;
    import java.sql.SQLException;
    import java.sql.Statement;
    import javax.swing.JOptionPane;
    import javax.swing.JTable;
    import javax.swing.table.DefaultTableModel;
    import javax.swing.table.TableColumn;

/**
 *
 * @author User
 */
public class dbCRUD {
    String jdbcUrl ="jdbc:mysql://localhost:3306/pariwisata";
    String username ="root";
    String password ="";
    
    public dbCRUD(String url, String user, String pass) {

        try (Connection Koneksi = DriverManager.getConnection(url, user, pass)) {
            Driver mysqlDriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqlDriver);

            System.out.println("berhasil");
        } catch (SQLException error) {
            System.err.println(error.toString());
        }
    }
    public dbCRUD(){}
    public boolean duplicateKey(String tabel, String PrimaryKey, String value){
        boolean hasil = false;
        int jumlah = 0;
            try {
                String SQl = "SELECT * FROM "+tabel+" WHERE "+PrimaryKey+ " = '"+value+"'";
                Statement perintah = getKoneksi().createStatement();
                ResultSet hasilData = perintah.executeQuery(SQl);
                while(hasilData.next()){
                    jumlah++;
                }
                hasil = jumlah != 0; 
            } catch (Exception e) {
                System.out.println(e.toString());
        }
        return hasil;  
    }       
    public Connection getKoneksi() throws SQLException{
        try{
            Driver mysqldriver = new com.mysql.jdbc.Driver();
            DriverManager.registerDriver(mysqldriver);
            
        } catch(SQLException e) {
        
            System.err.println(e.toString());
    
    }
        return DriverManager.getConnection(this.jdbcUrl, this.username, this.password);
    }
    
    public String getTable(String[] Tabelnya) {
        String hasilnya = "";
        int deteksiIndexAkhir = Tabelnya.length - 1;

        try {
            for (int i = 0; i < Tabelnya.length; i++) {
                if (i == deteksiIndexAkhir) {
                    hasilnya += Tabelnya[i];
                } else {
                    hasilnya += Tabelnya[i] + ",";
                }                
            }
        } catch (Exception e) {
            System.out.println(e.toString());
    }
    
    return hasilnya;
} 
    public String getIsi(String[] isinya) {
        String hasilnya = "";
        int deteksiIndexAkhir = isinya.length - 1;

        try {
            for (int i = 0; i < isinya.length; i++) {
                if (i == deteksiIndexAkhir) {
                    hasilnya +="'"+isinya[i]+"'";
                } else {
                    hasilnya +="'"+isinya[i]+ "',";
                }                
            }
        } catch (Exception e) {
            System.out.println(e.toString());
    }
    
    return hasilnya;
}
    public String gabungTableIsi(String [] table, String [] isi) {
        String hasil = "";
        int index = table.length -1;
        try {
            for (int i =0; i < table.length; i++) {
                if (i==index) {
                    hasil=hasil+table[i]+"='"+isi[i]+"'";
                } else {
                    hasil=hasil+table[i]+"='"+isi[i]+"',";
                }
            } 
        
        } catch (Exception e) {
            System.out.println(e.toString());
        }
   return hasil;
 }
    
    public Object[][] isiTabel(String SQL, int jumlah){
    Object[][] data =null;
        try {
            Statement perintah = getKoneksi().createStatement();
            ResultSet dataset = perintah.executeQuery(SQL);
            dataset.last();
            int baris = dataset.getRow();
            dataset.beforeFirst();
            
            data = new Object[baris][jumlah];
            int j =0;
            while (dataset.next()) {                
                for (int i = 0; i < jumlah; i++) {
                    data[j][i]=dataset.getString(i+1);
                }
                j++;
            }
        } catch (Exception e) {
        }
    return data;
  }
    public void setTampilTabel(JTable Tabelnya, String SQL, String[] Judul){
      try {
          Tabelnya.setModel(new DefaultTableModel(isiTabel(SQL,Judul.length), Judul));
      } catch (Exception e) {
          System.out.println(e.toString());
      }
  }
    public void setJudulTable(JTable Tablenya, String[] JudulKolomnya){
        
        try {
            DefaultTableModel modelnya = new DefaultTableModel();
            Tablenya.setModel(modelnya);
            for (int i = 0; i < JudulKolomnya.length; i++) {
               modelnya.addColumn(JudulKolomnya[i]);
                
            }
        } catch (Exception e) {
            System.out.println("Informasi.dbCRUD dipanggil untuk operasi tertentu.");

        }
    }
    public void setLebarKolom(JTable tabelnya,int[]JudulKolomnya){
      try {
          TableColumn Kolomnya = new TableColumn();
          for (int i = 0; i < JudulKolomnya.length; i++) {
          Kolomnya = tabelnya.getColumnModel().getColumn(i);
          Kolomnya.setPreferredWidth(JudulKolomnya[i]);   
          }
          
        
      } catch (Exception e) {
       System.out.println("Informasi.dbCRUD dipanggil untuk operasi tertentu.");
   
      }
    }
    
    public void SimpanDinamis(String Tabel,String[] Field, String[] Value) {
    try { 
        String SQLTambah = "INSERT INTO " + Tabel + " (" + getTable(Field) + ") VALUES (" + getIsi(Value) + ")";         
        Statement perintah = getKoneksi().createStatement();       
        perintah.executeUpdate(SQLTambah);  
        
        perintah.close();
        getKoneksi().close();
    }catch (com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException e) {
        JOptionPane.showMessageDialog(null, "data primary sudah ada");
    } catch (Exception e) {
    System.err.println("Error: " + e.toString());

    }

}
    public void UbahDinamis(String NamaTabel, String PrimaryKey, String IsiPrimary, String[] Field, String[] Value) {
        try {
            String SQLUbah = "UPDATE " + NamaTabel + " SET " + gabungTableIsi(Field, Value)+ " WHERE " + PrimaryKey + "='" + IsiPrimary + "'";      
            Statement perintah = getKoneksi().createStatement();     
            perintah.executeUpdate(SQLUbah);

            perintah.close();
            getKoneksi().close();

        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }
    public void HapusDinamis(String NamaTabel, String PK, String isi){
        try {
            String SQL="DELETE FROM "+NamaTabel+" WHERE "+PK+"='"+isi+"'";
            Statement perintah = getKoneksi().createStatement();
            perintah.executeUpdate(SQL);
            perintah.close();
      
        } catch (Exception e) {
            System.out.println(e.toString());
        }
    }    

    Connection getConnection() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}
