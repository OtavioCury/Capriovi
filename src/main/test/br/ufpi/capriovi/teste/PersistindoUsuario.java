package br.ufpi.capriovi.teste;
import br.ufpi.capriovi.dao.AdministradorDAO;
import br.ufpi.capriovi.dao.PeaoDAO;
import br.ufpi.capriovi.dao.PecuaristaDAO;
import br.ufpi.capriovi.entidades.cadastros.Administrador;
import br.ufpi.capriovi.entidades.cadastros.Peao;
import br.ufpi.capriovi.entidades.cadastros.Pecuarista;
import br.ufpi.capriovi.manage.JpaUtil;


public class PersistindoUsuario {


	public static void main(String[] args) {
	
		
		
		
		
		AdministradorDAO administradores = new AdministradorDAO();
		administradores.beginTransaction();
		Administrador admin = new Administrador();
		admin.setCpf("1111");
		admin.setLogin("Gustavo");
		administradores.save(admin);
		administradores.commit();
		administradores.closeTransaction();
		
	
		
		//Listando administradores
		for(Administrador a: administradores.ListarAdministradores()){
			System.out.println(a.getLogin());
		}
		
		PeaoDAO peoes = new PeaoDAO();
		peoes.beginTransaction();
		Peao peao = new Peao();
		peao.setCpf("1111");
		peao.setLogin("Otavio");
		peoes.save(peao);
		peoes.commit();
		peoes.closeTransaction();
		
		//Listando peoes
		for(Peao p: peoes.ListarPeao()){
			System.out.println(p.getLogin());
		}
		
		PecuaristaDAO pecuaristas = new PecuaristaDAO();
		pecuaristas.beginTransaction();
		Pecuarista pecuarista = new Pecuarista();
		pecuarista.setCpf("1111");
		pecuarista.setLogin("Irvayne");
		pecuaristas.save(pecuarista);
		pecuaristas.commit();
		pecuaristas.closeTransaction();
		
		//Listando pecuaristas
		for(Pecuarista pe: pecuaristas.ListarPecuarista()){
			System.out.println(pe.getLogin());
		}
		
		JpaUtil.close();
			
	
		
		

	}

}
