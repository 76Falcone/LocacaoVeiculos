package dao;

// Padrão Factory (GoF) — centraliza a criação de objetos DAO
public class DAOFactory {

    public static IVeiculoDAO getVeiculoDAO() {
        return new VeiculoDAO();
    }

    public static IUsuarioDAO getUsuarioDAO() {
        return new UsuarioDAO();
    }

    public static ILocacaoDAO getLocacaoDAO() {
        return new LocacaoDAO();
    }

    public static ILoginDAO getLoginDAO() {
        return new LoginDAO();
    }
}
