package business.empresa;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import business.exceptions.EmpresaNotFoundException;


public class EmpresaCatalog {

	private EntityManager em;
	
	public EmpresaCatalog(EntityManager em) {
		this.em = em;
	}

	public Empresa getEmpresaById(int id) throws EmpresaNotFoundException {
		try {
			TypedQuery<Empresa> query = em.createNamedQuery(Empresa.FIND_BY_ID, Empresa.class);
			query.setParameter(Empresa.ID, id);
			return query.getSingleResult();
		} catch (Exception e) {
			throw new EmpresaNotFoundException("Company with id " + id + " does not exist", e);
		}
	}
	
	

}
