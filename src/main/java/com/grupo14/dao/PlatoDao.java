package com.grupo14.dao;

import com.grupo14.datos.Plato;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;

import java.util.HashSet;
import java.util.Set;

public class PlatoDao extends BaseDao{

    public int agregar(Plato objeto) {
        int id = 0;
        try {
            iniciaOperacion();
            id = Integer.parseInt(session.save(objeto).toString());
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
        return id;
    }

    public void actualizar(Plato objeto) {
        try {
            iniciaOperacion();
            session.update(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public void eliminar(Plato objeto) {
        try {
            iniciaOperacion();
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Plato traer(int idPlato) {
        Plato objeto = null;
        try {
            iniciaOperacion();
            objeto = (Plato) session.get(Plato.class, idPlato);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Plato traer(String nombre) {
        Plato plato = null;
        try {
            iniciaOperacion();
            plato = (Plato) session.createQuery(
                    "from Plato p where p.nombre = :nombre"
            ).setParameter("nombre", nombre).uniqueResult();
        } finally {
            session.close();
        }
        return plato;
    }

    public Set<Plato> traer() {
        Set<Plato> platos = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Plato> query = session.createQuery("from Plato p order by p.nombre asc", Plato.class);
            platos = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return platos;
    }
}