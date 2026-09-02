package com.grupo14.dao;

import com.grupo14.datos.Personal;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class PersonalDao extends BaseDao{

    public int agregar(Personal objeto) {
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

    public void actualizar(Personal objeto) {
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

    public void eliminar(Personal objeto) {
        try {
            iniciaOperacion();
            objeto = (Personal) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Personal traer(int id) {
        Personal objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(Personal.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Personal traerPorDni(long dni) {
        Personal personal = null;
        try {
            iniciaOperacion();
            personal = (Personal) session.createQuery(
                    "from Personal p where p.dni = :dni"
            ).setParameter("dni", dni).uniqueResult();
        } finally {
            session.close();
        }
        return personal;
    }

    public Set<Personal> traer() {
        Set<Personal> staff = new HashSet<>();
        try {
            iniciaOperacion();
            Query<Personal> query = session.createQuery(
                    "from Personal p order by p.apellido asc, p.nombre asc", Personal.class
            );
            staff = new HashSet<>(query.getResultList());
        } finally {
            session.close();
        }
        return staff;
    }
}