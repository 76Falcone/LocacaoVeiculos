/*package testes;
       
import util.FabricaConexao;
import java.sql.Connection;

public class testeConexao {
     public static void main(String[] args) {
         try {
             System.out.println("Tentando conectar ao banco de dados...");
             Connection con = FabricaConexao.getConexao();
             if (con != null) {
                 System.out.println("Conexão realizada com sucesso!");
                 con.close();
             } else {
                 System.out.println("Falha ao realizar a conexão: a conexão retornou null.");
             }
         } catch (Exception e) {
             System.out.println("Erro ao conectar: " + e.getMessage());
             e.printStackTrace();
         }
     }
}
*/