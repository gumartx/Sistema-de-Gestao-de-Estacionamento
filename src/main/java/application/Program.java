package application;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Program {

	public static void main(String[] args) {
		
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("aws-springboot-jpa");
		EntityManager em = emf.createEntityManager();
		
		em.close();
		emf.close();
		
	}

}
