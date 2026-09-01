package com.grupo14.dao;

import com.grupo14.datos.Cocinero;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CocineroDao extends BaseDao {

    public int agregar(Cocinero objeto) {
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

    public void actualizar(Cocinero objeto) {
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

    public void eliminar(Cocinero objeto) {
        try {
            iniciaOperacion();
            objeto = (Cocinero) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Cocinero traer(int id) {
        Cocinero objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(Cocinero.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Cocinero traerPorDni(long dni) {
        Cocinero cocinero = null;
        try {
            iniciaOperacion();
            cocinero = (Cocinero) session.createQuery(
                    "from Cocinero co where co.dni = :dni"
            ).setParameter("dni", dni).uniqueResult();
        } finally {
            session.close();
        }
        return cocinero;
    }

    public Set<Cocinero> traer() {
        Set<Cocinero> lista = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Cocinero> query = session.createQuery(
                    "from Cocinero co order by co.apellido asc, co.nombre asc", Cocinero.class
            );
            lista = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return lista;
    }

}