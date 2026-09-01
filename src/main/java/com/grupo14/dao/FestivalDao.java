package com.grupo14.dao;

import com.grupo14.datos.Festival;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FestivalDao extends BaseDao{

    public int agregar(Festival objeto) {
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

    public void actualizar(Festival objeto) {
        try {
            iniciaOperacion();
            session.merge(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public void eliminar(Festival objeto) {
        try {
            iniciaOperacion();
            objeto = (Festival) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Festival traer(int id) {
        Festival objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(Festival.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Festival traer(String nombre) {
        Festival festival = null;
        try {
            iniciaOperacion();
            festival = (Festival) session.createQuery(
                    "from Festival f where f.nombre = :nombre"
            ).setParameter("nombre", nombre).uniqueResult();
        } finally {
            session.close();
        }
        return festival;
    }

    public Set<Festival> traer() {
        Set<Festival> festivales = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Festival> query = session.createQuery(
                    "from Festival f order by f.nombre asc", Festival.class
            );
            festivales = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return festivales;
    }
}