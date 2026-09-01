package com.grupo14.dao;

import com.grupo14.datos.Cajero;
import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.ArrayList;
import java.util.List;

public class CajeroDao extends BaseDao{

    public int agregar(Cajero objeto) {
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

    public void actualizar(Cajero objeto) {
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

    public void eliminar(Cajero objeto) {
        try {
            iniciaOperacion();
            objeto = (Cajero) session.merge(objeto);
            session.delete(objeto);
            tx.commit();
        } catch (HibernateException he) {
            manejaExcepcion(he);
        } finally {
            session.close();
        }
    }

    public Cajero traer(int id) {
        Cajero objeto = null;
        try {
            iniciaOperacion();
            objeto = session.get(Cajero.class, id);
        } finally {
            session.close();
        }
        return objeto;
    }

    public Cajero traerPorDni(long dni) {
        Cajero cajero = null;
        try {
            iniciaOperacion();
            cajero = (Cajero) session.createQuery(
                    "from Cajero c where c.dni = :dni"
            ).setParameter("dni", dni).uniqueResult();
        } finally {
            session.close();
        }
        return cajero;
    }

    public List<Cajero> traer() {
        List<Cajero> lista = new ArrayList<>();
        try {
            iniciaOperacion();
            Query<Cajero> query = session.createQuery(
                    "from Cajero c order by c.apellido asc, c.nombre asc", Cajero.class
            );
            lista = query.getResultList();
        } finally {
            session.close();
        }
        return lista;
    }

    public List<Cajero> traerPorTurno(String turno) {
        List<Cajero> lista = new ArrayList<>();
        try {
            iniciaOperacion();
            Query<Cajero> query = session.createQuery(
                    "from Cajero c where c.turno = :turno order by c.apellido asc", Cajero.class
            );
            query.setParameter("turno", turno);
            lista = query.getResultList();
        } finally {
            session.close();
        }
        return lista;
    }
}